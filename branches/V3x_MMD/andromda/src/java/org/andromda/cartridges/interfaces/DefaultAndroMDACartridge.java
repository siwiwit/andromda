package org.andromda.cartridges.interfaces;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.OutputUtils;
import org.andromda.core.common.Property;
import org.andromda.core.common.StdoutLogger;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Default implementation of standard AndroMDA cartridge behaviour.
 * Can be customized by derived cartridge classes.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class DefaultAndroMDACartridge implements AndroMDACartridge
{
    private CartridgeDescriptor desc = null;
    
    private CodeGenerationContext context;

    private Logger logger = null;
    
    /**
     * This is the name of the model element made available to the
     * template if <code>outputToSingleFile</code> is false.
     */
    private static final String TEMPLATE_VARIABLE_SINGLE_ELEMENT = "class";
    
    /**
     * This is the name of the Collection of model elements made available 
     * to the template if <code>outputToSingleFile</code> is true.
     */
    private static final String TEMPLATE_VARIABLE_ALL_ELEMENTS = "classes";
    
    public DefaultAndroMDACartridge() {
        this.resetLogger();   
    }
    
    /**
     * Cache for saving previous found model elements.
     */
    private Map elementCache = new HashMap();

    /**
     * @see org.andromda.cartridges.interfaces.AndroMDACartridge#getDescriptor()
     */
    public CartridgeDescriptor getDescriptor()
    {
        return desc;
    }

    /**
     * @see org.andromda.cartridges.interfaces.AndroMDACartridge#setDescriptor(org.andromda.cartridges.interfaces.CartridgeDescriptor)
     */
    public void setDescriptor(CartridgeDescriptor d)
    {
        this.desc = d;
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.AndroMDACartridge#processModelElements(org.andromda.core.common.CodeGenerationContext)
     */
    public void processModelElements(CodeGenerationContext context) {
        final String methodName = "DefaultAndroMDACartridge.processModelElements";
        ExceptionUtils.checkNull(methodName, "context", context);
            
        this.context = context;
        
        CartridgeDescriptor descriptor = 
            this.getDescriptor();
        
        Collection templates = 
            descriptor.getTemplateConfigurations();
        
        if (templates == null || templates.isEmpty()) {
            return;
        }
        
        MetafacadeFactory factory = MetafacadeFactory.getInstance();
        
        factory.setModel(context.getModelFacade());
        
        String previousNamespace = factory.getActiveNamespace();
        factory.setActiveNamespace(descriptor.getCartridgeName());
        
        Iterator templateIt = templates.iterator();
        while (templateIt.hasNext()) {
            
            TemplateConfiguration template = (TemplateConfiguration)templateIt.next();
            Collection stereotypes = template.getStereotypes();
            if (stereotypes != null && !stereotypes.isEmpty()) {
                
                Collection templateModelElements = new ArrayList();         
                Iterator stereotypeIt = stereotypes.iterator();
                while (stereotypeIt.hasNext()) {
                    String stereotype = (String)stereotypeIt.next();                            
                    Collection modelElements = (Collection)this.elementCache.get(stereotype);
                    if (modelElements == null) {
                        modelElements = context.getModelFacade().findByStereotype(stereotype);
                        elementCache.put(stereotype, modelElements);
                    }
                                    
                    templateModelElements.addAll(
                            MetafacadeFactory.getInstance().createMetafacades(modelElements));                
                }
                
                // filter out the model elements which shouldn't be processed
                this.filterModelPackages(templateModelElements);
                
                processModelElements(
                    template,
                    templateModelElements,
                    context);
                
            }
            
        }        
        
        //set the context back
        factory.setActiveNamespace(previousNamespace);
    }
    
    /**
     * Processes all <code>modelElements</code> for this template.
     * 
     * @param template the TemplateConfiguration object from which we process.
     * @param modelElements the model elements to process.
     * @param context the context for the cartridge
     */
    protected void processModelElements(
        TemplateConfiguration template,
        Collection modelElements, 
        CodeGenerationContext context) {
        final String methodName = "DefaultAndroMDACartridge.processModelElements";

        if (logger.isDebugEnabled()) {
        	logger.debug("performing " 
                + methodName 
                + " with template '" 
                + template 
                + "' modelElements '" 
                + modelElements 
                + "' and context ' " 
                + context + "'");
        }
        
        if (modelElements != null && !modelElements.isEmpty()) {

            File outFile = null;
            
            CartridgeDescriptor descriptor = 
                this.getDescriptor();
            
            Property outletProperty = 
                Namespaces.instance().findNamespaceProperty(
                        descriptor.getCartridgeName(), template.getOutlet());
            
            if (outletProperty != null && !outletProperty.isIgnore()) {
                
                String outputLocation = outletProperty.getValue();
                        
                try {       
                
                    long modelLastModified = context.getRepository().getLastModified();
                    
                    // send all model elements to one template since we are writing
                    // to one file.
                    if (template.isOutputToSingleFile()) {  
                                
                        // get rid of any duplicates (since we're outputting to
                        // one file)
                        modelElements = new HashSet(modelElements);
                        this.processWithTemplate(
                                template,
                                modelElements,
                                outletProperty,
                                null,
                                null);
                                            
                    } else {
                        
                        // process these model elements one at a time,
                        // output a file for each
                        Iterator modelElementIt = modelElements.iterator();
                        while (modelElementIt.hasNext()) {
    
                            Object modelElement = modelElementIt.next();

                            this.processWithTemplate(
                                    template,
                                    modelElement,
                                    outletProperty,
                                    context.getModelFacade().getName(modelElement),
                                    context.getModelFacade().getPackageName(modelElement));
                        }
                    }
                } catch (Throwable th) {
                    if (outFile != null && outFile.exists()) {
                        outFile.delete();
                        if (!outFile.delete())
                        {
                            logger.error(
                                "Error removing output file "
                                    + outFile.getName());
                            throw new CartridgeException(
                                "Error removing output file "
                                    + outFile.getName());
                        }
                        logger.info("Removed --> '" + outFile + "'");
                        StdoutLogger.info("Removed --> '" + outFile + "'");
                    }
                    String errMsg = "Error performing " + methodName;
                    if (MalformedURLException.class.isAssignableFrom(th.getClass())) {
                        errMsg = "ERROR! '" + th.getMessage() 
                        + "' Not a valid outputDirectory URI --> '" 
                        + outputLocation + "' for cartridge '" + outletProperty.getValue()
                        + "', please check configuration";
                    }
                    logger.error(errMsg, th);
                    throw new CartridgeException(errMsg, th);
                }
            }
        }
    }
    
    /**
     * <p>
     * Perform processing with the <code>template</code>.
     * </p>
     *
     * @param template the TemplateConfiguration containing the template
     *        sheet to process.
     * @param variable the variable for which code should be generated.  It will
     *        be either a single model element, or a collection of model elements
     *        depending (if <code>outputToSingleFile</code> is set to true).
     * @param outletProperty the property defining the outlet to which output
     *        will be written.
     * @param modelElementName the name of the model element (if we are processing
     *        a single model element, otherwise this will be ignored).
     * @param modelElementPackage the name of the package (if we are processing
     *        a single model element, otherwise this will be ignored).
     */
    private void processWithTemplate(
        TemplateConfiguration template,
        Object variable,
        Property outletProperty,
        String modelElementName,
        String modelElementPackage) {
        
        final String methodName = "DefaultAndroMDACartridge.processWithTemplate";
        ExceptionUtils.checkNull(methodName, "variable", variable);
        ExceptionUtils.checkNull(methodName, "outletProperty", outletProperty);

        // by default we'll assume we're processing with
        // one model element (as opposed to a collection)
        String variableName = TEMPLATE_VARIABLE_SINGLE_ELEMENT;
        if (Collection.class.isAssignableFrom(variable.getClass())) {
            variableName = TEMPLATE_VARIABLE_ALL_ELEMENTS;
        }
        
        File outFile = null;
        try {

            Map templateNamespace = new HashMap();
            
            // put the model element(s) into the context.           
            templateNamespace.put(variableName, variable);
            
            // add regular properties to the template context
            this.addPropertiesToContext(
                templateNamespace, 
                context.getUserProperties());
            
            // add all the TemplateObject objects to the template context
            templateNamespace.putAll(this.getDescriptor().getTemplateObjects());

            StringWriter output = new StringWriter();
            
            // process the template with the set TemplateEngine
            this.getDescriptor().getTemplateEngine().processTemplate(
                template.getSheet(), 
                templateNamespace, 
                output);
            
            if (template.getOutputPattern().charAt(0) == '$')
            {
                outFile = outputFileFromTemplateEngineContext(
                    template, 
                    outletProperty.getValue());
            }
            else
            {
                outFile =
                    this.outputFileFromTemplateConfig(
                            modelElementName,
                            modelElementPackage,
                            template,
                            outletProperty.getValue());
            }     
            
            if (outFile != null) {
                
                // do not overWrite already generated file,
                // if that is a file that the user needs to edit
                boolean writeOutputFile = !outFile.exists() || template.isOverwrite();
                
                long modelLastModified = context.getRepository().getLastModified();
                
                // only process files that have changed
                if (writeOutputFile && 
                        (!context.isLastModifiedCheck()|| 
                                modelLastModified > outFile.lastModified())) {
                 
                    String outputString = output.toString();
                    
                    StdoutLogger.setLogger(this.getDescriptor().getCartridgeName());
                    //check to see if generateEmptyFiles is true and if outString (when CLEANED)
                    //isn't empty.
                    if (StringUtils.trimToEmpty(outputString).length() > 0 || template.isGenerateEmptyFiles()) {
                        OutputUtils.writeStringToFile(outputString, outFile, true);
                        StdoutLogger.info("Output: '" + outFile.toURI() + "'");
                    } else {
                        StdoutLogger.info("Empty Output: '" + outFile.toURI() + "' --> not writing");
                    }
                    StdoutLogger.reset();
                }
           
            }
        } catch (Throwable th) {

            logger.info("Removed: " + outFile);
            StdoutLogger.info("Removed: " + outFile);
            
            // by default we'll assume we're processing with
            // one model element (as opposed to a collection)
            String variableType = "modelElement";

            if (Collection.class.isAssignableFrom(variable.getClass())) {
                variableType = "modelElements";         
            }            
            
            String errMsg = "Error performing " + methodName 
                + " with " + variableName + " '" 
                + variable 
                + "' and cartridge '" 
                + this.getDescriptor().getCartridgeName() + "'";
            logger.error(errMsg, th);
            throw new CartridgeException(errMsg, th);
        }
    }

    /**
     * Creates a File object from an output pattern in the
     * template configuration.
     * 
     * @param modelElementName the name of the model element
     * @param packageName the name of the package
     * @param tc the template configuration
     * @return File the output file
     */
    private File outputFileFromTemplateConfig(
        String modelElementName,
        String packageName,
        TemplateConfiguration tc,
        String outputLocation)
    {        
        return tc.getFullyQualifiedOutputFile(
            modelElementName,
            packageName,
            outputLocation);
    }

    /**
     * Creates a File object from a variable in a TemplateEngine context.
     * 
     * @param template the template configuration 
     * @return outputLocation the location to which the file will be output.
     */
    private File outputFileFromTemplateEngineContext(
        TemplateConfiguration template,
        String outputLocation)
    {
        String fileName = 
            this.getDescriptor().getTemplateEngine().getEvaluatedExpression(
            template.getOutputPattern());

        return new File(outputLocation, fileName);
    }
    
    /**
     * Takes all the Property values that were defined in the ant build.xml
     * file and adds them to the template context.
     *
     * @param  context  the template context
     * @param  properties the user properties
     */
    private void addPropertiesToContext(
        Map context,
        Collection properties)
    {
        for (Iterator it = properties.iterator(); it.hasNext();)
        {
            Property property = (Property) it.next();
            context.put(property.getName(), property.getValue());
        }
    }

    /**
     * @see org.andromda.cartridges.interfaces.AndroMDACartridge#init(java.util.Properties)
     */
    public void init(Properties templateEngineProperties) throws Exception
    {
        this.getDescriptor().getTemplateEngine().init(
            this.getDescriptor().getCartridgeName(),
            templateEngineProperties);
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.AndroMDACartridge#shutdown()
     */
    public void shutdown()
    {
        this.getDescriptor().getTemplateEngine().shutdown();
    }
    
    /**
     * Filters out those model elements which <strong>should</strong>
     * be processed.
     * 
     * @param modelElements the Collection of modelElements.
     */
    protected void filterModelPackages(Collection modelElements) {
        class PackageFilter implements Predicate {
            public boolean evaluate(Object modelElement) {
                return context.getModelPackages().shouldProcess(
                    context.getModelFacade().getPackageName(modelElement));
            }
        }
        CollectionUtils.filter(modelElements, new PackageFilter());
    }
    
    /**
     * Resets the logger to the default name.
     */
    private void resetLogger() {
        this.setLogger(DefaultAndroMDACartridge.class.getName());      
    }
    
    /**
     * Sets the logger to be used
     * with this Cartridge
     * 
     * @param logger The logger to set.
     */
    private void setLogger(String loggerName) {
        this.logger = Logger.getLogger(loggerName);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
    
}
