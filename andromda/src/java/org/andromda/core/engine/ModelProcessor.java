package org.andromda.core.engine;

import java.io.IOException;
import java.io.InputStream;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.ModelValidationException;
import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BuildInformation;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.core.common.Introspector;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.Profile;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
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
     * Re-configures this model processor from the given <code>configuration</code>
     * instance (if different from that of the one passed in during the call to
     * {@link #initialize(Configuration)}, and runs the model processor.
     *
     * @param configuration the configuration from which to configure this model
     *        processor instance.
     */
    public void process(final Configuration configuration)
    {
        this.configure(configuration);
        this.process(configuration.getModels());
    }

    /**
     * Configures (or re-configures) the model processor if configuration
     * is required (the configuration has changed since the previous, or has
     * yet to be used).
     *
     * @param configuration the AndroMDA configuration instance.
     */
    private final void configure(final Configuration configuration)
    {
        if (this.requiresConfiguration(configuration))
        {
            configuration.initialize();
            this.reset();
            this.addTransformations(configuration.getTransformations());
            final Property[] properties = configuration.getProperties();
            final int propertyNumber = properties.length;
            final Introspector introspector = Introspector.instance();
            for (int ctr = 0; ctr < propertyNumber; ctr++)
            {
                final Property property = properties[ctr];
                try
                {
                    introspector.setProperty(
                        this,
                        property.getName(),
                        property.getValue());
                }
                catch (final Throwable throwable)
                {
                    AndroMDALogger.warn(
                        "Could not set model processor property '" + property.getName() + "' with a value of '" +
                        property.getValue() + "'");
                }
            }
            this.currentConfiguration = configuration;
        }
    }

    /**
     * Processes all <code>models</code> with the discovered plugins.
     *
     * @param models an array of URLs to models.
     */
    private final void process(Model[] models)
    {
        final long startTime = System.currentTimeMillis();

        // - filter out any invalid models (ones that don't have any uris defined)
        models = this.filterInvalidModels(models);
        if (models.length > 0)
        {
            this.processModels(models);
            AndroMDALogger.info(
                "completed model processing --> TIME: " + this.getDurationInSeconds(startTime) +
                "[s], RESOURCES WRITTEN: " + ResourceWriter.instance().getWrittenCount());
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
        String cartridgeName = null;
        try
        {
            boolean lastModifiedCheck = true;
            long lastModified = 0;
            final ResourceWriter writer = ResourceWriter.instance();

            // - get the time from the model that has the latest modified time
            for (int ctr = 0; ctr < models.length; ctr++)
            {
                final Model model = models[ctr];
                writer.resetHistory(model.getUris()[0]);
                lastModifiedCheck = model.isLastModifiedCheck() && lastModifiedCheck;

                // - we go off the model that was most recently modified.
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

                // - pre-load the models
                this.loadIfNecessary(models);
                for (final Iterator iterator = cartridges.iterator(); iterator.hasNext();)
                {
                    final Cartridge cartridge = (Cartridge)iterator.next();
                    cartridgeName = cartridge.getName();
                    if (this.shouldProcess(cartridgeName))
                    {
                        this.factory.setNamespace(cartridgeName);
                        cartridge.initialize();

                        // - process each model
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
            // - we don't want to record model validation exceptions
            throw exception;
        }
        catch (final Throwable throwable)
        {
            final String messsage =
                "Error performing ModelProcessor.process with model(s) --> '" + StringUtils.join(models, ",") + "'";
            logger.error(messsage);
            ExceptionRecorder.instance().record(messsage, throwable, cartridgeName);
            throw new ModelProcessorException(messsage, throwable);
        }
    }

    /**
     * Initializes this model processor instance with the given
     * configuration.  This configuration is overridden (if changed)
     * when calling {@link #run(Configuration)}.
     */
    public void initialize(final Configuration configuration)
    {
        final long startTime = System.currentTimeMillis();

        // - first, print the AndroMDA header
        this.printConsoleHeader();

        // - second, configure the this model processor 
        // - the ordering of this step is important: it needs to occur
        //   before everything else in the framework is initialized so that 
        //   we have all configuration information available (such as the
        //   namespace properties)
        this.configure(configuration);

        // - discover all plugins on the classpath
        PluginDiscoverer.instance().discoverPlugins();

        // - load all the logical mappings
        Mappings.initializeLogicalMappings();

        // - load the model into the repository (if it
        //   has yet to be loaded)
        if (this.repository == null)
        {
            this.repository =
                (RepositoryFacade)ComponentContainer.instance().findRequiredComponent(RepositoryFacade.class);
            this.repository.open();
        }

        // - finally the metafacade factory
        this.factory.initialize();
        this.printWorkCompleteMessage("core initialization", startTime);
    }

    /**
     * Loads the model into the repository only when necessary (the model has a timestamp
     * later than the last timestamp of the loaded model).
     *
     * @param model the model to be loaded.
     */
    protected final void loadModelIfNecessary(final Model model)
    {
        // - only load if the model has been changed from last time it was loaded
        if (model.isChanged())
        {
            final long startTime = System.currentTimeMillis();

            // - first perform any transformations
            final Transformer transformer =
                (Transformer)ComponentContainer.instance().findRequiredComponent(Transformer.class);
            final String[] uris = model.getUris();
            final int uriNumber = uris.length;
            InputStream[] streams = new InputStream[uriNumber];
            for (int ctr = 0; ctr < uriNumber; ctr++)
            {
                streams[ctr] = transformer.transform(
                        uris[ctr],
                        this.getTransformations());
            }

            // - now load the models into the repository
            for (int ctr = 0; ctr < uriNumber; ctr++)
            {
                final String uri = uris[ctr];
                AndroMDALogger.info("loading model --> '" + uri + "'");
            }
            this.repository.readModel(
                streams,
                uris,
                model.getModuleSearchLocationPaths());
            try
            {
                for (int ctr = 0; ctr < uriNumber; ctr++)
                {
                    InputStream stream = streams[ctr];
                    stream.close();
                    stream = null;
                }
            }
            catch (final IOException exception)
            {
                // ignore
            }
            this.printWorkCompleteMessage("loading", startTime);

            // - validate the model since loading has successfully occurred
            this.validateModel();
        }
    }

    /**
     * Validates the entire model with each cartridge namespace,
     * and logs the failures if model validation fails.
     *
     * @param model the model to validate
     */
    private final void validateModel()
    {
        if (this.modelValidation)
        {
            final long startTime = System.currentTimeMillis();
            AndroMDALogger.info("- validating model -");
            final Collection cartridges = PluginDiscoverer.instance().findPlugins(Cartridge.class);
            final ModelAccessFacade modelAccessFacade = this.repository.getModel();

            // - clear out the factory's caches (such as any previous validation messages, etc.)
            this.factory.clearCaches();
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
            this.printValidationMessages(this.factory.getValidationMessages());
            this.printWorkCompleteMessage("validation", startTime);

            //AndroMDALogger.info("- validation complete -");
        }
    }

    /**
     * Prints a work complete message using the type of <code>unitOfWork</code> and
     * <code>startTime</code> as input.
     * @param unitOfWork the type of unit of work that was completed
     * @param startTime the time the unit of work was started.
     */
    private final void printWorkCompleteMessage(
        final String unitOfWork,
        final long startTime)
    {
        AndroMDALogger.info("- " + unitOfWork + " complete: " + this.getDurationInSeconds(startTime) + "[s] -");
    }

    /**
     * Calcuates the duration in seconds between the
     * given <code>startTime</code> and the current time.
     * @param startTime the time to compare against.
     * @return the duration of time in seconds.
     */
    private final double getDurationInSeconds(final long startTime)
    {
        return ((System.currentTimeMillis() - startTime) / 1000.0);
    }

    /**
     * Prints any model validation errors stored within the <code>factory</code>.
     *
     * @param factory the metafacade factory (used to manage the metafacades).
     */
    private void printValidationMessages(final List messages)
    {
        // - log all error messages
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
     * The current configuration of this model processor.
     */
    private Configuration currentConfiguration = null;

    /**
     * Determines whether or not this model processor needs to be reconfigured.
     * This is based on whether or not the new configuration is different
     * than the <code>currentConfiguration</code>.  We determine this checking
     * if their contents are equal or not, if not equal this method will
     * return true, otherwise false.
     *
     * @param configuration the configuration to compare to the lastConfiguration.
     * @return true/false
     */
    private final boolean requiresConfiguration(final Configuration configuration)
    {
        boolean requiresConfiguration =
            this.currentConfiguration == null || this.currentConfiguration.getContents() == null ||
            configuration.getContents() == null;
        if (!requiresConfiguration)
        {
            requiresConfiguration = !this.currentConfiguration.getContents().equals(configuration.getContents());
        }
        return requiresConfiguration;
    }

    /**
     * Checks to see if <em>any</em> of the
     * models need to be reloaded, and if so, re-loads them.
     */
    final void loadIfNecessary(final Model[] models)
    {
        // - only allow loading when processing is not occurring.
        if (models != null && models.length > 0)
        {
            final int modelNumber = models.length;
            for (int ctr = 0; ctr < modelNumber; ctr++)
            {
                this.loadModelIfNecessary(models[ctr]);
            }
        }
    }

    /**
     * Stores the current version of AndroMDA.
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
                    final Model model = (Model)object;
                    return model != null && model.getUris() != null && model.getUris().length > 0;
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
        // - shutdown the metafacade factory instance
        MetafacadeFactory.getInstance().shutdown();

        // - shutdown the namespaces instance
        Namespaces.instance().shutdown();

        // - shutdown the container instance
        ComponentContainer.instance().shutdown();

        // - shutdown the plugin discoverer instance
        PluginDiscoverer.instance().shutdown();

        // - shutdown the profile instance
        Profile.instance().shutdown();

        // - shutdown the introspector
        Introspector.instance().shutdown();

        // - clear out any caches used by the configuration
        Configuration.clearCaches();

        if (this.repository != null)
        {
            this.repository.clear();
        }
    }

    /**
     * Reinitializes the model processor's resources.
     */
    private final void reset()
    {
        this.factory.reset();
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