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
import java.util.Properties;

import org.andromda.core.anttasks.UserProperty;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StdoutLogger;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;
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
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

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
    private ICartridgeDescriptor desc = null;

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
    public ICartridgeDescriptor getDescriptor()
    {
        return desc;
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#setDescriptor(org.andromda.cartridges.interfaces.ICartridgeDescriptor)
     */
    public void setDescriptor(ICartridgeDescriptor d)
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
     * @throws  CartridgeException  if something goes wrong
     */
    public void processModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
        throws CartridgeException
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        String previousNamespace = df.getActiveNamespace();

        df.setActiveNamespace(getDescriptor().getCartridgeName());

        df.setModel((UmlPackage) context.getModelFacade().getModel());

        try
        {
            internalProcessModelElement(
                context,
                modelElement,
                stereotypeName);
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
        throws CartridgeException
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
            if (tc.getStereotype().equals(stereotypeName))
            {
                ScriptHelper scriptHelper = context.getScriptHelper();

                if (tc.getTransformClass() != null)
                {
                    // template has its own custom script helper
                    try
                    {
                        context.setScriptHelper(
                            (ScriptHelper) tc
                                .getTransformClass()
                                .newInstance());
                        context.getScriptHelper().setModel(
                            context.getModelFacade().getModel());
                    }
                    catch (IllegalAccessException iae)
                    {
                        throw new CartridgeException(iae);
                    }
                    catch (InstantiationException ie)
                    {
                        throw new CartridgeException(ie);
                    }
                }

                processModelElementWithOneTemplate(
                    context,
                    modelElement,
                    tc);

                // restore original script helper in case we were
                // using a custom template script helper
                context.setScriptHelper(scriptHelper);
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
     * @throws  CartridgeException  if something goes wrong
     */
    private void processModelElementWithOneTemplate(
        CodeGenerationContext context,
        Object modelElement,
        TemplateConfiguration tc)
        throws CartridgeException
    {
        try
        {
            logger.debug("");
            logger.debug(
                "------------------- Processing model element >>"
                    + ((ModelElement) modelElement).getName()
                    + "<< using template "
                    + tc.getSheet());

            internalProcessModelElementWithOneTemplate(
                context,
                modelElement,
                tc);
        }
        finally
        {
            logger.debug(
                "------------------- Finished processing model element >>"
                    + ((ModelElement) modelElement).getName()
                    + "<< using template "
                    + tc.getSheet());
        }
    }

    private void internalProcessModelElementWithOneTemplate(
        CodeGenerationContext context,
        Object modelElement,
        TemplateConfiguration tc)
        throws CartridgeException
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
            UmlPackage model =
                (UmlPackage) context.getModelFacade().getModel();
            DecoratorFactory df = DecoratorFactory.getInstance();

            velocityContext.put("model", df.createDecoratorObject(model));
            velocityContext.put("transform", context.getScriptHelper());
            velocityContext.put("str", new StringUtilsHelper());
            velocityContext.put(
                "class",
                df.createDecoratorObject((ModelElement) modelElement));
            velocityContext.put("date", new java.util.Date());

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
        catch (Exception e)
        {
            try
            {
                writer.flush();
                writer.close();
            }
            catch (Exception e2)
            {
            }

            logger.error(
                "Error processing velocity script on " + modelElementName,
                e);

            throw new CartridgeException(
                "Error processing velocity script on " + modelElementName,
                e);
        }

        // Handle file generation/removal if no files should be generated for
        // empty output.
        File outFile;

        if (tc.getOutputPattern().charAt(0) == '$')
        {
            outFile =
                outputFileFromVelocityContext(
                    velocityContext,
                    tc.getOutputPattern());
        }
        else
        {
            outFile =
                outputFileFromTemplateConfig(
                    modelElementName,
                    packageName,
                    tc);
        }

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
                        || modelLastModified > outFile.lastModified()
                    /*
                *  || styleSheetLastModified > outFile.lastModified()
                */
                    ))
                {
                    ensureDirectoryFor(outFile);
                    OutputStream out = new FileOutputStream(outFile);
                    out.write(result);
                    out.flush();
                    out.close();
                    logger.info("Output: " + outFile);
                    StdoutLogger.info("Output: " + outFile);
                }
            }
            catch (Exception e)
            {
                StdoutLogger.error(e);
                throw new CartridgeException(
                    "Error writing output file " + outFile.getName(),
                    e);
            }
        }
        else
        {
            if (outFile.exists())
            {
                if (!outFile.delete())
                {
                    logger.error(
                        "Error removing output file " + outFile.getName());
                    throw new CartridgeException(
                        "Error removing output file " + outFile.getName());
                }
                logger.info("Removed: " + outFile);
                StdoutLogger.info("Removed: " + outFile);
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
        TemplateConfiguration tc)
    {
        String outputLocation =
            Namespaces.instance().findNamespaceProperty(
                desc.getCartridgeName(),
                tc.getOutlet());

        return tc.getFullyQualifiedOutputFile(
            modelElementName,
            packageName,
            outputLocation);
    }

    /**
     * Creates a File object from a variable in a Velocity context.
     * 
     * @param velocityContext the context
     * @param velocityExpression the expression that evaluates to a file name 
     * @return File the output file
     */
    private File outputFileFromVelocityContext(
        VelocityContext velocityContext,
        String velocityExpression)
        throws CartridgeException
    {
        try
        {
            StringWriter writer = new StringWriter();

            ve.evaluate(
                velocityContext,
                writer,
                "mylogtag",
                velocityExpression);

            return new File(writer.getBuffer().toString());
        }
        catch (Exception e)
        {
            logger.error(e);
            throw new CartridgeException(
                "Error building file name from Velocity expression",
                e);
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
