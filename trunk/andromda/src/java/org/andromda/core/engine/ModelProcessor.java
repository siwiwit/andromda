package org.andromda.core.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.ModelValidationException;
import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BuildInformation;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.Profile;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Transformation;
import org.andromda.core.mapping.Mappings;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.transformation.Transformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * <p>
 * Handles the processing of models. Facilitates Model Driven
 * Architecture by enabling the generation of source code, configuration files, and other such artifacts from a single
 * or multiple models. </p>
 *
 * @author Chad Brandon
 */
public class ModelProcessor
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ModelProcessor.class);

    /**
     * Creates a new instance the ModelProcessor.
     *
     * @return the shared ModelProcessor instance.
     */
    public static final ModelProcessor newInstance()
    {
        return new ModelProcessor();
    }

    private ModelProcessor()
    {
        // do not allow instantiation
    }

    /**
     * Processes all <code>models</code> with the discovered plugins.
     *
     * @param models an array of URLs to models.
     */
    public void process(Model[] models)
    {
        final long startTime = System.currentTimeMillis();
        models = this.filterInvalidModels(models);
        if (models.length > 0)
        {
            try
            {
                this.processing = true;
                this.processModels(models);
                this.processing = false;
                AndroMDALogger.info(
                    "completed model processing --> TIME: " + ((System.currentTimeMillis() - startTime) / 1000.0) +
                    "[s], RESOURCES WRITTEN: " + ResourceWriter.instance().getWrittenCount());
            }
            finally
            {
                this.reset();
                this.factory.clearNamespaceProperties();
            }
        }
        else
        {
            AndroMDALogger.warn("No model(s) found to process");
        }
    }

    /**
     * The shared metafacade factory instance.
     */
    private final MetafacadeFactory factory = MetafacadeFactory.getInstance();

    /**
     * Processes multiple <code>models</code>.
     *
     * @param repository the RepositoryFacade that will be used to read/load the model
     * @param models the Model(s) to process.
     * @param cartridges the collection of cartridge used to process the models.
     */
    private void processModels(final Model[] models)
    {
        final String methodName = "ModelProcessor.process";

        // filter out any models that are null or have null URLs
        String cartridgeName = null;
        try
        {
            boolean lastModifiedCheck = true;
            long lastModified = 0;
            final ResourceWriter writer = ResourceWriter.instance();

            // get the time from the model that has the latest modified time
            for (int ctr = 0; ctr < models.length; ctr++)
            {
                final Model model = models[ctr];
                writer.resetHistory(model.getUri());
                lastModifiedCheck = model.isLastModifiedCheck() && lastModifiedCheck;

                // we go off the model that was most recently modified.
                if (model.getLastModified() > lastModified)
                {
                    lastModified = model.getLastModified();
                }
            }

            if (lastModifiedCheck ? writer.isHistoryBefore(lastModified) : true)
            {
                final Collection cartridges = PluginDiscoverer.instance().findPlugins(Cartridge.class);
                if (cartridges.isEmpty())
                {
                    AndroMDALogger.warn("WARNING! No cartridges found, check your classpath!");
                }

                // pre-load the models
                for (int ctr = 0; ctr < models.length; ctr++)
                {
                    this.loadModelIfNecessary(models[ctr]);
                }
                for (final Iterator iterator = cartridges.iterator(); iterator.hasNext();)
                {
                    final Cartridge cartridge = (Cartridge)iterator.next();
                    cartridgeName = cartridge.getName();
                    if (this.shouldProcess(cartridgeName))
                    {
                        this.factory.setNamespace(cartridgeName);
                        cartridge.initialize();

                        // process each model
                        for (int ctr = 0; ctr < models.length; ctr++)
                        {
                            this.factory.setModel(this.repository.getModel());
                            this.factory.setModelPackages(models[ctr].getPackages());
                            cartridge.processModelElements(factory);
                            writer.writeHistory();
                        }
                        cartridge.shutdown();
                    }
                }
            }
        }
        catch (final ModelValidationException exception)
        {
            // we don't want to record model validation exceptions
            throw exception;
        }
        catch (final Throwable throwable)
        {
            final String errorMesssage =
                "Error performing " + methodName + " with model(s) --> '" + StringUtils.join(models, ",") + "'";
            logger.error(errorMesssage);
            ExceptionRecorder.instance().record(errorMesssage, throwable, cartridgeName);
            throw new ModelProcessorException(errorMesssage, throwable);
        }
    }

    /**
     * Initializes this model processor instance.
     */
    public void initialize()
    {
        this.printConsoleHeader();
        PluginDiscoverer.instance().discoverPlugins();
        Mappings.initializeLogicalMappings();
        if (this.repository == null)
        {
            final ComponentContainer container = ComponentContainer.instance();
            this.repository = (RepositoryFacade)container.findComponent(RepositoryFacade.class);
            if (this.repository == null)
            {
                throw new ModelProcessorException(
                    "No repository implementation could be found, please make sure you have a '" +
                    container.getComponentDefaultConfigurationPath(RepositoryFacade.class) +
                    "' file on your classpath");
            }
            this.repository.open();
        }
        this.factory.initialize();
    }

    /**
     * Stores the last modified times for each model that has been
     * loaded into the repository.
     */
    private final Map modelModifiedTimes = new HashMap();

    /**
     * A flag indicating if model processing is currently occuring.
     */
    private boolean processing = false;

    /**
     * Loads the model into the repository only when necessary (the model has a timestamp
     * later than the last timestamp of the loaded model).
     *
     * @param model the model to be loaded.
     */
    protected final void loadModelIfNecessary(final Model model)
    {
        final Object key = this.getModelModifiedKey(model.getUri());
        final Long previousModifiedTime = (Long)this.modelModifiedTimes.get(key);
        if (previousModifiedTime == null || (model.getLastModified() > previousModifiedTime.longValue()))
        {
            final ComponentContainer container = ComponentContainer.instance();
            final Transformer transformer = (Transformer)container.findComponent(Transformer.class);
            if (transformer == null)
            {
                throw new ModelProcessorException(
                    "No transfomer implementation could be found, please make sure you have a '" +
                    container.getComponentDefaultConfigurationPath(Transformer.class) + "' file on your classpath");
            }
            InputStream stream = transformer.transform(
                    model.getUri(),
                    this.getTransformations());
            AndroMDALogger.info("loading model --> '" + model.getUri() + "'");
            this.repository.readModel(
                stream,
                model.getUri().toString(),
                model.getModuleSearchLocations());
            AndroMDALogger.info("- loading complete -");
            this.modelModifiedTimes.put(
                key,
                new Long(model.getLastModified()));
            try
            {
                stream.close();
                stream = null;
            }
            catch (final IOException exception)
            {
                // ignore
            }
            if (this.modelValidation)
            {
                this.validateModel(model);
                AndroMDALogger.info("- model validation complete -");
            }
        }
    }

    /**
     * Validates the given <code>model</code> with each cartridge namespace,
     * and logs the failures if model validation fails.
     *
     * @param model the model to validate
     */
    private final void validateModel(final Model model)
    {
        final Collection cartridges = PluginDiscoverer.instance().findPlugins(Cartridge.class);
        final ModelAccessFacade modelAccessFacade = this.repository.getModel();
        this.factory.setModel(modelAccessFacade);
        for (final Iterator iterator = cartridges.iterator(); iterator.hasNext();)
        {
            final Cartridge cartridge = (Cartridge)iterator.next();
            final String cartridgeName = cartridge.getName();
            if (this.shouldProcess(cartridgeName))
            {
                this.factory.setNamespace(cartridgeName);
                this.factory.validateAllMetafacades();
            }
        }
        try
        {
            this.printValidationMessages(factory.getValidationMessages());
        }
        finally
        {
            this.reset();
        }
    }

    /**
     * Prints any model validation errors stored within the <code>factory</code>.
     *
     * @param factory the metafacade factory (used to manage the metafacades).
     */
    private void printValidationMessages(final List messages)
    {
        // log all the error messages
        if (messages != null && !messages.isEmpty())
        {
            final StringBuffer header =
                new StringBuffer("Model Validation Failed - " + messages.size() + " VALIDATION ERROR");
            if (messages.size() > 1)
            {
                header.append("S");
            }
            AndroMDALogger.error(header);
            this.sortValidationMessages(messages);
            final Iterator iterator = messages.iterator();
            for (int ctr = 1; iterator.hasNext(); ctr++)
            {
                final ModelValidationMessage message = (ModelValidationMessage)iterator.next();
                AndroMDALogger.error(ctr + ") " + message);
            }
            AndroMDALogger.reset();
        }
        if (this.failOnValidationErrors && !messages.isEmpty())
        {
            throw new ModelValidationException("Model validation failed!");
        }
    }

    /**
     * Creates the key used to retrieve the nodel last modified
     * time.
     *
     * @param uri the model uri.
     * @return the unique key
     */
    private final Object getModelModifiedKey(final URL uri)
    {
        return new File(uri.getFile());
    }

    /**
     * Checks to see if <em>any</em> of the
     * current <code>models</code> need to be
     * reloaded, and if so reloads them.
     */
    final void loadIfNecessary(final Model[] models)
    {
        // only allow loading when processing is not occurring.
        if (!this.processing && (models != null && models.length > 0))
        {
            final int modelNumber = models.length;
            for (int ctr = 0; ctr < modelNumber; ctr++)
            {
                this.loadModelIfNecessary(models[ctr]);
            }
        }
    }

    /**
     * Stores the current version of AndroMDA
     */
    private static final String VERSION = BuildInformation.instance().getBuildVersion();

    /**
     * Prints the console header.
     */
    protected void printConsoleHeader()
    {
        AndroMDALogger.info("");
        AndroMDALogger.info("A n d r o M D A  -  " + VERSION);
        AndroMDALogger.info("");
    }

    /**
     * Whether or not model validation should be performed.
     */
    private boolean modelValidation = true;

    /**
     * Sets whether or not model validation should occur. This is useful for
     * performance reasons (i.e. if you have a large model it can significatly descrease the amount of time it takes for
     * AndroMDA to process a model). By default this is set to <code>true</code>.
     *
     * @param modelValidation true/false on whether model validation should be performed or not.
     */
    public void setModelValidation(final boolean modelValidation)
    {
        this.modelValidation = modelValidation;
    }

    /**
     * A flag indicating whether or not failure should occur
     * when model validation errors are present.
     */
    private boolean failOnValidationErrors = true;

    /**
     * Sets whether or not processing should fail when validation errors occur, default is <code>true</code>.
     *
     * @param failOnValidationErrors whether or not processing should fail if any validation errors are present.
     */
    public void setFailOnValidationErrors(final boolean failOnValidationErrors)
    {
        this.failOnValidationErrors = failOnValidationErrors;
    }

    /**
     * Stores the cartridge filter.
     */
    private List cartridgeFilter = null;

    /**
     * Denotes whether or not the complement of filtered cartridges should be processed
     */
    private boolean negateCartridgeFilter = false;

    /**
     * Indicates whether or not the <code>namespace</code> should be processed. This is determined in conjunction with
     * {@link #setCartridgeFilter(String)}. If the <code>cartridgeFilter</code> is not defined, then this method will
     * <strong>ALWAYS </strong> return true.
     *
     * @param namespace the name of the namespace to check whether or not it should be processed.
     * @return true/false on whether or not it should be processed.
     */
    protected boolean shouldProcess(final String namespace)
    {
        boolean shouldProcess = Namespaces.instance().namespacePresent(namespace);
        if (shouldProcess)
        {
            shouldProcess = this.cartridgeFilter == null || this.cartridgeFilter.isEmpty();
            if (!shouldProcess)
            {
                shouldProcess =
                    this.negateCartridgeFilter ^ this.cartridgeFilter.contains(StringUtils.trimToEmpty(namespace));
            }
        }
        return shouldProcess;
    }

    /**
     * The prefix used for cartridge filter negation.
     */
    private static final String CARTRIDGE_FILTER_NEGATOR = "~";

    /**
     * <p/>
     * Sets the current cartridge filter. This is a comma seperated list of namespaces (matching cartridges names) that
     * should be processed. </p>
     * <p/>
     * If this filter is defined, then any cartridge names found in this list <strong>will be processed </strong>, while
     * any other discovered cartridges <strong>will not be processed </strong>. </p>
     *
     * @param namespaces a comma seperated list of the cartridge namespaces to be processed.
     */
    public void setCartridgeFilter(String namespaces)
    {
        if (namespaces != null)
        {
            namespaces = StringUtils.deleteWhitespace(namespaces);
            if (namespaces.startsWith(CARTRIDGE_FILTER_NEGATOR))
            {
                this.negateCartridgeFilter = true;
                namespaces = namespaces.substring(1);
            }
            else
            {
                this.negateCartridgeFilter = false;
            }
            if (StringUtils.isNotBlank(namespaces))
            {
                this.cartridgeFilter = Arrays.asList(namespaces.split(","));
            }
        }
    }

    /**
     * Stores any transformations that should be applied
     * to the model(s) before processing occurs.
     */
    private final List transformations = new ArrayList();

    /**
     * Adds transformation to be applied to the model(s)
     * before processing occurrs.
     *
     * @param transformation a transformation document.
     */
    public void addTransformations(final Transformation[] transformations)
    {
        this.transformations.addAll(Arrays.asList(transformations));
    }

    /**
     * Gets the current transformations that will be applied
     * to the model before processing beings.
     *
     * @return the transformations.
     */
    private Transformation[] getTransformations()
    {
        return (Transformation[])this.transformations.toArray(new Transformation[0]);
    }

    /**
     * Sets the encoding (UTF-8, ISO-8859-1, etc) for all output
     * produced during model processing.
     *
     * @param outputEncoding the encoding.
     */
    public void setOuputEncoding(final String outputEncoding)
    {
        ResourceWriter.instance().setEncoding(outputEncoding);
    }

    /**
     * Sets <code>xmlValidation</code> to be true/false. This defines whether XML resources loaded by AndroMDA (such as
     * plugin descriptors) should be validated. Sometimes underlying parsers don't support XML Schema validation and in
     * that case, we want to be able to turn it off.
     *
     * @param xmlValidation true/false on whether we should validate XML resources used by AndroMDA
     */
    public void setXmlValidation(final boolean xmlValidation)
    {
        XmlObjectFactory.setDefaultValidating(xmlValidation);
    }

    /**
     * <p/>
     * Sets the <code>loggingConfigurationUri</code> for AndroMDA. This is the URI to an external logging configuration
     * file. This is useful when you want to override the default logging configuration of AndroMDA. </p>
     * <p/>
     * You can retrieve the default log4j.xml contained within the {@link org.andromda.core.common}package, customize
     * it, and then specify the location of this logging file with this operation. </p>
     *
     * @param loggingConfigurationUri the URI to the external logging configuation file.
     */
    public void setLoggingConfigurationUri(final String loggingConfigurationUri)
    {
        AndroMDALogger.setLoggingConfigurationUri(loggingConfigurationUri);
    }

    /**
     * Filters out any <em>invalid</em> models. This means models that either are null within the specified
     * <code>models</code> array or those that don't have URLs set.
     *
     * @param models the models to filter.
     * @return the array of valid models
     */
    private final Model[] filterInvalidModels(final Model[] models)
    {
        Collection validModels = new ArrayList(Arrays.asList(models));
        CollectionUtils.filter(
            validModels,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object != null && ((Model)object).getUri() != null;
                }
            });
        return (Model[])validModels.toArray(new Model[0]);
    }

    /**
     * The repository instance which loads the models to
     * be processed.
     */
    private RepositoryFacade repository;

    /**
     * Shuts down the model processor (reclaims any
     * resources).
     */
    public void shutdown()
    {
        // shutdown the metafacade factory instance
        MetafacadeFactory.getInstance().shutdown();

        // shutdown the namespaces instance
        Namespaces.instance().shutdown();

        // shutdown the container instance
        ComponentContainer.instance().shutdown();

        // shutdown the plugin discoverer instance
        PluginDiscoverer.instance().shutdown();

        // shutdown the profile instance
        Profile.instance().shutdown();

        if (this.repository != null)
        {
            this.repository.clear();
        }
    }

    /**
     * Reinitializes the model processor's resources.
     */
    final void reset()
    {
        MetafacadeFactory.getInstance().clearCaches();
        this.cartridgeFilter = null;
        this.transformations.clear();
        this.setXmlValidation(true);
        this.setOuputEncoding(null);
        this.setModelValidation(true);
        this.setLoggingConfigurationUri(null);
        this.setFailOnValidationErrors(true);
    }

    /**
     * Sorts the validation <code>messages</code> first by type (i.e. the metafacade class) and then by the
     * <code>name</code> of the model element to which the validation message applies.
     *
     * @param messages the collection of messages to sort.
     * @return the sorted <code>messages</code> collection.
     */
    protected void sortValidationMessages(final List messages)
    {
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new ValidationMessageTypeComparator());
        chain.addComparator(new ValidationMessageNameComparator());
        Collections.sort(messages, chain);
    }

    /**
     * Used to sort validation messages by <code>metafacadeClass</code>.
     */
    private final static class ValidationMessageTypeComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private ValidationMessageTypeComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            Object objectA,
            Object objectB)
        {
            ModelValidationMessage a = (ModelValidationMessage)objectA;
            ModelValidationMessage b = (ModelValidationMessage)objectB;
            return collator.compare(
                a.getMetafacadeClass().getName(),
                b.getMetafacadeClass().getName());
        }
    }

    /**
     * Used to sort validation messages by <code>modelElementName</code>.
     */
    private final static class ValidationMessageNameComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private ValidationMessageNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            Object objectA,
            Object objectB)
        {
            ModelValidationMessage a = (ModelValidationMessage)objectA;
            ModelValidationMessage b = (ModelValidationMessage)objectB;
            return collator.compare(
                StringUtils.trimToEmpty(a.getMetafacadeName()),
                StringUtils.trimToEmpty(b.getMetafacadeName()));
        }
    }
}