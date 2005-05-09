package org.andromda.core.cartridge;

import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.ModelElements;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.PathMatcher;
import org.andromda.core.common.Property;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The AndroMDA Cartridge implementation of the Plugin. Cartridge instances are configured from
 * <code>META-INF/andromda-cartridge.xml</code> files discovered on the classpath.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class Cartridge
    extends BasePlugin
{
    /**
     * The default Cartridge constructor.
     */
    public Cartridge()
    {
        super();
    }

    /**
     * Cache for saving previously found model elements.
     */
    private final Map elementCache = new HashMap();

    /**
     * Stores the code generation context. Protected access to improve performance within inner class access.
     */
    protected CodeGenerationContext context;

    /**
     * The prefix to look for when determining whether or not to retrieve the output location from the template engine.
     */
    private static final String TEMPLATE_ENGINE_OUTPUT_PREFIX = "$";

    /**
     * Processes all model elements with relevant stereotypes by retrieving the model elements from the model facade
     * contained within the context.
     *
     * @param context the context containing the ModelAccessFacade (among other things).
     */
    public void processModelElements(final CodeGenerationContext context)
    {
        final String methodName = "Cartridge.processModelElements";
        ExceptionUtils.checkNull(methodName, "context", context);
        this.context = context;
        final Collection resources = this.getResources();

        if (resources != null && !resources.isEmpty())
        {
            final MetafacadeFactory factory = MetafacadeFactory.getInstance();
            factory.setModel(context.getModelFacade());
            final String previousNamespace = factory.getActiveNamespace();
            factory.setActiveNamespace(this.getName());
            for (Iterator resourceIterator = resources.iterator(); resourceIterator.hasNext();)
            {
                Resource resource = (Resource)resourceIterator.next();
                if (Template.class.isAssignableFrom(resource.getClass()))
                {
                    this.processTemplate((Template)resource);
                }
                else
                {
                    this.processResource(resource);
                }
            }
            // set the namespace back
            factory.setActiveNamespace(previousNamespace);
        }
    }

    /**
     * Processes the given <code>template</code>.
     *
     * @param template the Template instance to process.
     */
    protected void processTemplate(final Template template)
    {
        final String methodName = "Cartridge.processTemplate";
        ExceptionUtils.checkNull(methodName, "template", template);
        final ModelElements templateModelElements = template.getSupportedModeElements();
        // handle the templates WITH model elements
        if (templateModelElements != null && !templateModelElements.isEmpty())
        {
            for (Iterator templateModelElementIterator = templateModelElements.getModelElements().iterator();
                 templateModelElementIterator.hasNext();)
            {
                ModelElement templateModelElement = (ModelElement)templateModelElementIterator.next();
                Collection modelElements = null;
                // if the template model element has a stereotype
                // defined, then we filter the model elements based
                // on that stereotype, otherwise we get all model elements
                // and let the modelElement perform filtering on the
                // metafacades by type and properties
                if (templateModelElement.hasStereotype())
                {
                    modelElements = this.context.getModelFacade().findByStereotype(
                            templateModelElement.getStereotype());
                }
                else if (templateModelElement.hasTypes())
                {
                    modelElements = this.context.getModelFacade().getModelElements();
                }
                else
                {
                    continue;
                }
                Collection metafacades = MetafacadeFactory.getInstance().createMetafacades(modelElements);
                this.filterModelPackages(metafacades);
                templateModelElement.setMetafacades(metafacades);
            }
            this.processTemplateWithModelElements(template);
        }
        else
        {
            // handle any templates WITHOUT model elements.
            this.processTemplateWithoutModelElements(template);
        }
    }

    /**
     * Processes all <code>modelElements</code> for this template.
     *
     * @param template the Template object from which we process.
     * @param context  the context for the cartridge
     */
    protected void processTemplateWithModelElements(final Template template)
    {
        final String methodName = "Cartridge.processTemplateWithModelElements";
        ExceptionUtils.checkNull(methodName, "template", template);
        if (getLogger().isDebugEnabled())
            getLogger().debug(
                    "performing " + methodName + " with template '" + template + "' and context ' " + context + "'");

        final ModelElements modelElements = template.getSupportedModeElements();
        if (modelElements != null && !modelElements.isEmpty())
        {
            final Property outletProperty = Namespaces.instance().findNamespaceProperty(this.getName(),
                    template.getOutlet(), template.isRequired());

            if (outletProperty != null && !outletProperty.isIgnore())
            {
                try
                {
                    final Collection allMetafacades = modelElements.getAllMetafacades();

                    // if outputToSingleFile is true AND outputOnEmptyElements
                    // is true or we have at least one metafacade in the
                    // allMetafacades collection, then we collect the template
                    // model elements and place them into the template context
                    // by their variable names.
                    if (template.isOutputToSingleFile() &&
                            (template.isOutputOnEmptyElements() || !allMetafacades.isEmpty()))
                    {
                        final Map templateContext = new HashMap();

                        // first place all relevant model elements by the
                        // <modelElements/> variable name. If the variable
                        // isn't defined (which is possible), ignore.
                        if (StringUtils.isNotBlank(modelElements.getVariable()))
                        {
                            templateContext.put(modelElements.getVariable(), allMetafacades);
                        }

                        // now place the collections of elements
                        // by the given variable names. (skip if the variable
                        // was NOT defined)
                        final Iterator modelElementIt = modelElements.getModelElements().iterator();
                        while (modelElementIt.hasNext())
                        {
                            final ModelElement modelElement = (ModelElement)modelElementIt.next();
                            String variable = modelElement.getVariable();
                            if (StringUtils.isNotEmpty(variable))
                            {
                                // if a stereotype has the same variable defined
                                // more than one time, then get the existing
                                // model elements added from the last iteration
                                // and add the new ones to that collection
                                Collection metafacades = (Collection)templateContext.get(variable);
                                if (metafacades != null)
                                {
                                    metafacades.addAll(modelElement.getMetafacades());
                                }
                                else
                                {
                                    metafacades = modelElement.getMetafacades();
                                }
                                templateContext.put(variable, new HashSet(metafacades));
                            }
                        }
                        this.processWithTemplate(template, templateContext, outletProperty, null, null);
                    }
                    else
                    {
                        // if outputToSingleFile isn't true, then
                        // we just place the model element with the default
                        // variable defined on the <modelElements/> into the
                        // template.
                        final Iterator metafacadeIt = allMetafacades.iterator();
                        while (metafacadeIt.hasNext())
                        {
                            final Map templateContext = new HashMap();

                            final Object metafacade = metafacadeIt.next();

                            templateContext.put(modelElements.getVariable(), metafacade);

                            this.processWithTemplate(template, templateContext, outletProperty, context.getModelFacade()
                                    .getName(metafacade), context.getModelFacade().getPackageName(metafacade));
                        }
                    }
                }
                catch (Throwable th)
                {
                    String errMsg = "Error performing " + methodName;
                    throw new CartridgeException(errMsg, th);
                }
            }
        }
    }

    /**
     * Processes the <code>template</code> without model elements. This is useful if you need to generate something that
     * is part of your cartridge, however you only need to use a proprety passed in from a namespace or a template
     * object defined in your cartridge descriptor.
     *
     * @param template the template to process.
     */
    protected void processTemplateWithoutModelElements(final Template template)
    {
        final String methodName = "Cartridge.processTemplateWithoutModelElements";
        ExceptionUtils.checkNull(methodName, "template", template);
        Property outletProperty = Namespaces.instance().findNamespaceProperty(this.getName(), template.getOutlet(),
                template.isRequired());
        if (outletProperty != null && !outletProperty.isIgnore())
        {
            Map templateContext = new HashMap();
            this.processWithTemplate(template, templateContext, outletProperty, null, null);
        }
    }

    /**
     * <p/> Perform processing with the <code>template</code>.
     * </p>
     * 
     * @param template the Template containing the template path to process.
     * @param templateContext the context to which variables are added and made
     *        available to the template engine for processing. This will contain
     *        any model elements being made avaiable to the template(s) as well
     *        as properties/template objects.
     * @param outletProperty the property defining the outlet to which output
     *        will be written.
     * @param modelElementName the name of the model element (if we are
     *        processing a single model element, otherwise this will be
     *        ignored).
     * @param modelElementPackage the name of the package (if we are processing
     *        a single model element, otherwise this will be ignored).
     */
    private final void processWithTemplate(
        final Template template,
        final Map templateContext,
        final Property outletProperty,
        final String modelElementName,
        final String modelElementPackage)
    {
        final String methodName = "Cartridge.processWithTemplate";
        ExceptionUtils.checkNull(methodName, "template", template);
        ExceptionUtils.checkNull(methodName, "templateContext", templateContext);
        ExceptionUtils.checkNull(methodName, "outletProperty", outletProperty);

        File outFile = null;
        try
        {
            // populate the template context will cartridge descriptor
            // properties and template objects
            this.populateTemplateContext(templateContext);

            final StringWriter output = new StringWriter();

            // process the template with the set TemplateEngine
            this.getTemplateEngine().processTemplate(template.getPath(), templateContext, output);

            if (template.getOutputPattern().startsWith(TEMPLATE_ENGINE_OUTPUT_PREFIX))
            {
                outFile = this.outputFileFromTemplateEngineContext(template, outletProperty.getValue());
            }
            else
            {
                outFile = this.outputFileFromTemplate(modelElementName, modelElementPackage, template,
                        outletProperty.getValue());
            }
            if (outFile != null)
            {
                // only write files that do NOT exist, and
                // those that have overwrite set to 'true'
                if (!outFile.exists() || template.isOverwrite())
                {
                    final String outputString = output.toString();
                    AndroMDALogger.setSuffix(this.getName());
                    // check to see if generateEmptyFiles is true and if
                    // outString is not blank
                    if (StringUtils.isNotBlank(outputString) || template.isGenerateEmptyFiles())
                    {
                        ResourceWriter.instance().writeStringToFile(outputString, outFile, this.getName());
                        AndroMDALogger.info("Output: '" + outFile.toURI() + "'");
                    }
                    else
                    {
                        if (this.getLogger().isDebugEnabled())
                            this.getLogger().debug("Empty Output: '" + outFile.toURI() + "' --> not writing");
                    }
                    AndroMDALogger.reset();
                }
            }
        }
        catch (Throwable th)
        {
            if (outFile != null)
            {
                outFile.delete();
                this.getLogger().info("Removed: '" + outFile + "'");
            }
            String errMsg = "Error performing " + methodName + " with template '" + template.getPath() +
                    "', template context '" +
                    templateContext +
                    "' and cartridge '" +
                    this.getName() +
                    "'";
            throw new CartridgeException(errMsg, th);
        }
    }

    /**
     * Processes the given <code>resource</code>
     *
     * @param resource the resource to process.
     */
    protected void processResource(final Resource resource)
    {
        final String methodName = "Cartridge.processResource";
        ExceptionUtils.checkNull(methodName, "resource", resource);

        URL resourceUrl = ResourceUtils.getResource(resource.getPath(), this.getMergeLocation());
        if (resourceUrl == null)
        {
            // if the resourceUrl is null, the path is probably a regular
            // expression pattern so we'll see if we can match it against
            // the contents of the plugin and write any contents that do match
            List contents = this.getContents();
            if (contents != null)
            {
                AndroMDALogger.setSuffix(this.getName());
                Iterator contentIt = contents.iterator();
                while (contentIt.hasNext())
                {
                    String content = (String)contentIt.next();
                    if (StringUtils.isNotEmpty(content))
                    {
                        if (PathMatcher.wildcardMatch(content, resource.getPath()))
                        {
                            resourceUrl = ResourceUtils.getResource(content, this.getMergeLocation());
                            this.writeResource(resource, resourceUrl);
                        }
                    }
                }
                AndroMDALogger.reset();
            }
        }
        else
        {
            this.writeResource(resource, resourceUrl);
        }
    }

    /**
     * Writes the contents of <code>resourceUrl</code> to the outlet specified by <code>resource</code>.
     *
     * @param resource    contains the outlet where the resource is written.
     * @param resourceUrl the URL contents to write.
     */
    private final void writeResource(final Resource resource, final URL resourceUrl)
    {
        final String methodName = "Cartridge.writeResource";
        File outFile = null;
        try
        {
            Property outletProperty = Namespaces.instance().findNamespaceProperty(this.getName(), resource.getOutlet(),
                    resource.isRequired());
            String slash = "/";
            if (outletProperty != null && !outletProperty.isIgnore())
            {
                // make sure we don't have any back slashes
                String resourceUri = resourceUrl.toString().replaceAll("\\\\", slash);
                String uriSuffix = resourceUri.substring(resourceUri.lastIndexOf(slash), resourceUri.length());
                String outletLocation = outletProperty.getValue();
                if (outletLocation.endsWith(slash))
                {
                    // remove the extra slash
                    outletLocation = outletLocation.replaceFirst(slash, "");
                }
                outFile = resource.getOutputLocation(new String[]{uriSuffix}, new File(outletLocation));

                // only write files that do NOT exist, and
                // those that have overwrite set to 'true'
                if (!outFile.exists() || resource.isOverwrite())
                {
                    ResourceWriter.instance().writeUrlToFile(resourceUrl, outFile.toString());
                    AndroMDALogger.info("Output: '" + outFile.toURI() + "'");
                }
            }
        }
        catch (Throwable th)
        {
            if (outFile != null)
            {
                outFile.delete();
                this.getLogger().info("Removed: '" + outFile + "'");
            }
            String errMsg = "Error performing " + methodName;
            throw new CartridgeException(errMsg, th);
        }
    }

    /**
     * Creates a File object from an output pattern in the template
     * configuration.
     * 
     * @param modelElementName the name of the model element
     * @param packageName the name of the package
     * @param template the template.
     * @return File the output file
     */
    private final File outputFileFromTemplate(
        final String modelElementName,
        final String packageName,
        final Template template,
        final String outputLocation)
    {
        return template.getOutputLocation(modelElementName, packageName, new File(outputLocation));
    }

    /**
     * Creates a File object from a variable in a TemplateEngine context.
     *
     * @param resource the Cartridge resource.
     * @return outputLocation the location to which the file will be output.
     */
    private final File outputFileFromTemplateEngineContext(final Resource resource, final String outputLocation)
    {
        String fileName = this.getTemplateEngine().getEvaluatedExpression(resource.getOutputPattern());
        return new File(outputLocation, fileName);
    }

    /**
     * Filters out those model elements which <strong>should </strong> be processed and returns the filtered collection
     *
     * @param modelElements the Collection of modelElements.
     */
    protected void filterModelPackages(final Collection modelElements)
    {
        CollectionUtils.filter(modelElements, new Predicate()
        {
            public boolean evaluate(Object modelElement)
            {
                return context.getModelPackages().shouldProcess(context.getModelFacade().getPackageName(modelElement));
            }
        });
    }

    /**
     * Stores the loaded resources to be processed by this cartridge intance.
     */
    private final List resources = new ArrayList();

    /**
     * Returns the list of templates configured in this cartridge.
     *
     * @return List the template list.
     */
    public List getResources()
    {
        return this.resources;
    }

    /**
     * Adds an resource to the list of defined resources.
     *
     * @param resource the new resource to add
     */
    public void addResource(final Resource resource)
    {
        ExceptionUtils.checkNull("Cartridge.addResource", "resource", resource);
        resource.setCartridge(this);
        resources.add(resource);
    }

    /**
     * @see org.andromda.core.common.Plugin#getType()
     */
    public String getType()
    {
        return "cartridge";
    }

    /**
     * Override to provide cartridge specific shutdown (i.e. clean out the element cache so that another model won't
     * have the same elements).
     *
     * @see org.andromda.core.common.Plugin#shutdown()
     */
    public void shutdown()
    {
        super.shutdown();
        this.elementCache.clear();
    }
}