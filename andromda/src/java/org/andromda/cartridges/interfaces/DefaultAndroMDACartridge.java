package org.andromda.cartridges.interfaces;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.anttasks.UserProperty;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.andromda.core.common.StdoutLogger;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Default implementation of standard AndroMDA cartridge behaviour.
 * Can be customized by derived cartridge classes.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 *
 */
public class DefaultAndroMDACartridge implements IAndroMDACartridge
{
    private CartridgeDescriptor desc = null;

    private VelocityEngine ve = null;

    private Logger logger = null;

    /**
     * <p>This class receives log messages from Velocity and
     * forwards them to the concrete logger that is configured
     * for this cartridge.</p>
     * 
     * <p>This avoids creation of one large Velocity log file
     * where errors are difficult to find and track.</p>
     * 
     * <p>Error messages can now be traced to cartridge activities.</p>
     */
    private class VelocityLoggingReceiver implements LogSystem
    {
        /* (non-Javadoc)
         * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
         */
        public void init(RuntimeServices arg0) throws Exception
        {
        }

        /* (non-Javadoc)
         * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int, java.lang.String)
         */
        public void logVelocityMessage(int level, String message)
        {
            switch (level)
            {
                case LogSystem.WARN_ID :
                    logger.warn(message);
                    break;
                case LogSystem.INFO_ID :
                    logger.info(message);
                    break;
                case LogSystem.DEBUG_ID :
                    logger.debug(message);
                    break;
                case LogSystem.ERROR_ID :
                    logger.error(message);
                    break;
                default :
                    logger.debug(message);
                    break;
            }
        }
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#getDescriptor()
     */
    public CartridgeDescriptor getDescriptor()
    {
        return desc;
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#setDescriptor(org.andromda.cartridges.interfaces.CartridgeDescriptor)
     */
    public void setDescriptor(CartridgeDescriptor d)
    {
        this.desc = d;
    }

    /**
     * <p>
     * Processes one model element with exactly one stereotype.
     * May be called several times for the same model element
     * because since UML 1.4, a model element can have multiple
     * stereotypes.
     * </p>
     *
     * @param  context         context for code generation
     * @param  modelElement    the model element for which code should be
     *                         generated
     * @param  stereotypeName  name of the stereotype which should trigger code
     *                         generation
     */
    public void processModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
    {
        MetafacadeFactory df = MetafacadeFactory.getInstance();
        String previousNamespace = df.getActiveNamespace();

        df.setActiveNamespace(getDescriptor().getCartridgeName());

        df.setModel(context.getModelFacade());

        try
        {
            // set the stdout logger to the cartridge name so we can see 
            // which cartridge is processing it from the stdout
            StdoutLogger.setLogger(this.getDescriptor().getCartridgeName());
            internalProcessModelElement(
                context,
                modelElement,
                stereotypeName);
            StdoutLogger.reset();
        }
        finally
        {
            df.setActiveNamespace(previousNamespace);
        }
    }

    private void internalProcessModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
    {
        String packageName =
            context.getModelFacade().getPackageName(modelElement);

        //if the package name shouldn't be processed,
        //skip it.
        if (!context.getModelPackages().shouldProcess(packageName))
        {
            return;
        }

        List templates = getDescriptor().getTemplateConfigurations();
        for (Iterator it = templates.iterator(); it.hasNext();)
        {
            TemplateConfiguration tc = (TemplateConfiguration) it.next();
            if (tc.getStereotypes().contains(stereotypeName))
            {

                processModelElementWithOneTemplate(
                    context,
                    modelElement,
                    tc);
            }
        }
    }

    /**
     * <p>
     * Processes one model element with exactly one template script.
     * </p>
     *
     * @param  context         context for code generation
     * @param  modelElement    the model element for which code should be
     *                         generated
     * @param  styleSheetName  name of the Velocity style sheet
     * @param  outFile         file to which to write the output
     * @param  generateEmptyFile flag, tells whether to generate empty
     *                         files or not.
     */
    private void processModelElementWithOneTemplate(
        CodeGenerationContext context,
        Object modelElement,
        TemplateConfiguration tc)
    {
        final String modelElementName =
            context.getModelFacade().getName(modelElement);
        try
        {
            if (logger.isDebugEnabled())
                logger.debug("");
            logger.debug(
                "------------------- Processing model element >>"
                    + modelElementName
                    + "<< using template "
                    + tc.getSheet());

            internalProcessModelElementWithOneTemplate(
                context,
                modelElement,
                tc);
        }
        finally
        {
            if (logger.isDebugEnabled())
                logger.debug(
                    "------------------- Finished processing model element >>"
                        + modelElementName
                        + "<< using template "
                        + tc.getSheet());
        }
    }

    private void internalProcessModelElementWithOneTemplate(
        CodeGenerationContext context,
        Object modelElement,
        TemplateConfiguration tc)
    {
        String modelElementName =
            context.getModelFacade().getName(modelElement);
        String packageName =
            context.getModelFacade().getPackageName(modelElement);

        Writer writer = null;
        ByteArrayOutputStream content = null;

        content = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(content);

        VelocityContext velocityContext = new VelocityContext();

        try
        {
            // put some objects into the velocity context

            // TODO: this has to be optimized so that decorators are not created each time we come here!!!
            Object model = context.getModelFacade().getModel();
            MetafacadeFactory df = MetafacadeFactory.getInstance();

            velocityContext.put("model", df.createFacadeObject(model));
            velocityContext.put(
                "class",
                df.createFacadeObject(modelElement));

            // add any template objects to the context now
            Map templateObjects = this.getDescriptor().getTemplateObjects();
            if (templateObjects != null && !templateObjects.isEmpty())
            {
                Iterator templateObjectIt =
                    templateObjects.keySet().iterator();
                while (templateObjectIt.hasNext())
                {
                    String name = (String) templateObjectIt.next();
                    velocityContext.put(name, templateObjects.get(name));
                }
            }

            addUserPropertiesToContext(
                velocityContext,
                context.getUserProperties());

            // get the template to process
            Template template = ve.getTemplate(tc.getSheet());

            // Process the VSL template with the context and write out
            // the result as the outFile.
            template.merge(velocityContext, writer);

            writer.flush();
            writer.close();
        }
        catch (Throwable th)
        {
            try
            {
                writer.flush();
                writer.close();
            }
            catch (Exception e2)
            {
            }
            String errMsg = "Error processing velocity script on --> '" 
                + modelElementName + "'";

            logger.error(errMsg, th);

            throw new CartridgeException(errMsg, th);
        }

        // Handle file generation/removal if no files should be generated for
        // empty output.
        File outFile;

        // find the outlet property that contains the location 
        // to which the files will be generated.
        Property property =
            Namespaces.instance().findNamespaceProperty(
                desc.getCartridgeName(),
                tc.getOutlet());

        // don't process if the outlet property is set to ignore (or its null)
        if (property != null && !property.isIgnore())
        {
            if (tc.getOutputPattern().charAt(0) == '$')
            {
                outFile =
                    outputFileFromVelocityContext(
                        velocityContext,
                        tc,
                        property.getValue());
            }
            else
            {
                outFile =
                    outputFileFromTemplateConfig(
                        modelElementName,
                        packageName,
                        tc,
                        property.getValue());
            }

            if (outFile != null)
            {
                byte[] result = content.toByteArray();
                if (result.length > 0 || tc.isGenerateEmptyFiles())
                {
                    try
                    {
                        long modelLastModified =
                            context.getRepository().getLastModified();

                        // do not overwrite already generated file,
                        // if that is a file that the user wants to edit.
                        boolean writeOutputFile =
                            !outFile.exists() || tc.isOverwrite();
                        // only process files that have changed
                        if (writeOutputFile
                            && (!context.isLastModifiedCheck()
                                || modelLastModified
                                    > outFile.lastModified()))
                        {
                            ensureDirectoryFor(outFile);
                            OutputStream out =
                                new FileOutputStream(outFile);
                            out.write(result);
                            out.flush();
                            out.close();
                            logger.info("Output: " + outFile);
                            StdoutLogger.info("Output: " + outFile);
                        }
                    }
                    catch (Throwable th)
                    {
                        String errMsg = "Error writing output file "
                                + outFile.getName();
                        StdoutLogger.error(th);
                        throw new CartridgeException(errMsg,th);
                    }
                }
                else
                {
                    if (outFile.exists())
                    {
                        if (!outFile.delete())
                        {
                            logger.error(
                                "Error removing output file "
                                    + outFile.getName());
                            throw new CartridgeException(
                                "Error removing output file "
                                    + outFile.getName());
                        }
                        logger.info("Removed: " + outFile);
                        StdoutLogger.info("Removed: " + outFile);
                    }
                }
            }
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
     * Creates a File object from a variable in a Velocity context.
     * 
     * @param velocityContext the context
     * @param tc the template configuration 
     * @return File the output file
     */
    private File outputFileFromVelocityContext(
        VelocityContext velocityContext,
        TemplateConfiguration tc,
        String outputLocation)
    {
        try
        {
            StringWriter writer = new StringWriter();

            ve.evaluate(
                velocityContext,
                writer,
                "mylogtag",
                tc.getOutputPattern());

            return new File(outputLocation, writer.getBuffer().toString());
        }
        catch (Throwable th)
        {
            String errMsg = "Error building file name from Velocity expression";
            logger.error(errMsg, th);
            throw new CartridgeException(errMsg, th);
        }
    }

    /**
     * Takes all the UserProperty values that were defined in the ant build.xml
     * ile and adds them to the Velocity context.
     *
     * @param  context  the Velocity context
     * @param  userProperties the user properties
     */
    private void addUserPropertiesToContext(
        VelocityContext context,
        Collection userProperties)
    {
        for (Iterator it = userProperties.iterator(); it.hasNext();)
        {
            UserProperty up = (UserProperty) it.next();
            context.put(up.getName(), up.getValue());
        }
    }

    /**
     * <p>
     *  Creates  directories as needed.
     * </p>
     *
     *@param  targetFile a <code>File</code> whose parent directories need to
     *                   exist
     *@exception CartridgeException if the parent directories couldn't be created
     */
    private void ensureDirectoryFor(File targetFile)
        throws CartridgeException
    {
        File directory = new File(targetFile.getParent());
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new CartridgeException(
                    "Unable to create directory: "
                        + directory.getAbsolutePath());
            }
        }
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#init(Properties)
     */
    public void init(Properties velocityProperties) throws Exception
    {
        initLogger();

        ve = new VelocityEngine();

        // Tell Velocity it should also use the classpath when searching for templates
        ExtendedProperties ep =
            ExtendedProperties.convertProperties(velocityProperties);

        ep.addProperty(
            VelocityEngine.RESOURCE_LOADER,
            "andromda.cartridges,file");

        ep.setProperty(
            "andromda.cartridges."
                + VelocityEngine.RESOURCE_LOADER
                + ".class",
            ClasspathResourceLoader.class.getName());

        // Tell Velocity not to use its own logger but to use the logger
        // of this cartridge.
        ep.setProperty(
            VelocityEngine.RUNTIME_LOG_LOGSYSTEM,
            new VelocityLoggingReceiver());

        // Let Velocity know about the macro libraries.
        for (Iterator iter = getDescriptor().getMacroLibraries().iterator();
            iter.hasNext();
            )
        {
            String libraryName = (String) iter.next();
            ep.addProperty(VelocityEngine.VM_LIBRARY, libraryName);
        }

        ve.setExtendedProperties(ep);
        ve.init();
    }

    /* (non-Javadoc)
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#shutdown()
     */
    public void shutdown()
    {
        shutdownLogger();
    }

    /**
     * Opens a log file for this cartridge.
     * @throws IOException if the file cannot be opened
     */
    private void initLogger() throws IOException
    {
        final String cartridgeName = getDescriptor().getCartridgeName();
        logger =
            Logger.getLogger("org.andromda.cartridges." + cartridgeName);
        logger.setAdditivity(false);
        logger.setLevel(Level.ALL);

        String logfile = "andromda-" + cartridgeName + ".log";
        FileAppender appender =
            new FileAppender(new PatternLayout("%d - %m%n"), logfile, true);
        logger.addAppender(appender);
    }

    /**
     * Shutdown the associated logger.
     * 
     */
    private void shutdownLogger()
    {
        Enumeration appenders = logger.getAllAppenders();
        while (appenders.hasMoreElements())
        {
            Appender appender = (Appender) appenders.nextElement();
            appender.close();
        }
    }

}
