package org.andromda.templateengines.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.templateengine.TemplateEngineException;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

/**
 * The TemplateEngine implementation for VelocityTemplateEngine template
 * processor.
 * 
 * @see http://jakarta.apache.org/velocity/
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class VelocityTemplateEngine
    implements TemplateEngine
{

    protected static Logger logger = null;

    private List macrolibs = new ArrayList();

    /**
     * The directory we look in to find velocity properties.
     */
    private static final String PROPERTIES_DIR = "META-INF/";

    /**
     * The suffix for the the velocity properties.
     */
    private static final String PROPERTIES_SUFFIX = "-velocity.properties";

    /**
     * The location of external templates
     */
    private String mergeLocation;

    /**
     * Stores additional properties specified within the plugin within the file
     * META-INF/'plugin name'-velocity.properties
     */
    private Properties properties = null;

    /**
     * Constructs an instance of VelocityTemplateEngine.
     */
    public VelocityTemplateEngine()
    {}

    /**
     * the VelocityEngine instance to use
     */
    private VelocityEngine velocityEngine;

    private VelocityContext velocityContext = null;

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#init(java.lang.String)
     */
    public void init(String pluginName) throws Exception
    {
        this.initLogger(pluginName);

        ExtendedProperties engineProperties = new ExtendedProperties();

        // Tell VelocityTemplateEngine it should also use the
        // classpath when searching for templates
        // IMPORTANT: file,andromda.plugins the ordering of these
        // two things matters, the ordering allows files to override
        // the resources found on the classpath.
        engineProperties.setProperty(
            VelocityEngine.RESOURCE_LOADER,
            "file,classpath");

        engineProperties.setProperty("file." + VelocityEngine.RESOURCE_LOADER
            + ".class", FileResourceLoader.class.getName());

        engineProperties.setProperty(
            "classpath." + VelocityEngine.RESOURCE_LOADER + ".class",
            ClasspathResourceLoader.class.getName());

        // Tell VelocityTemplateEngine not to use its own logger
        // the logger but to use of this plugin.
        engineProperties.setProperty(
            VelocityEngine.RUNTIME_LOG_LOGSYSTEM,
            new VelocityLoggingReceiver());

        // Let this template engine know about the macro libraries.
        for (Iterator iter = getMacroLibraries().iterator(); iter.hasNext();)
        {
            String libraryName = (String)iter.next();
            engineProperties
                .addProperty(VelocityEngine.VM_LIBRARY, libraryName);
        }

        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setExtendedProperties(engineProperties);
        if (this.mergeLocation != null)
        {
            // set the file resource path (to the merge location)
            velocityEngine.addProperty(
                VelocityEngine.FILE_RESOURCE_LOADER_PATH,
                this.mergeLocation);
            // we need to reinitialize the velocity engine
            // with the file resource loader path
        }
        this.addProperties(pluginName);
        this.velocityEngine.init();
    }

    /**
     * Adds any properties found within META-INF/'plugin
     * name'-velocity.properties
     */
    private void addProperties(String pluginName) throws IOException
    {
        // reset any properties from previous processing
        this.properties = null;

        // see if the velocity properties exist for the current
        // plugin
        URL propertiesUri = ResourceUtils.getResource(PROPERTIES_DIR
            + StringUtils.trimToEmpty(pluginName) + PROPERTIES_SUFFIX);

        if (propertiesUri != null)
        {
            if (logger.isDebugEnabled())
                logger.debug("loading properties from --> '" + propertiesUri
                    + "'");
            this.properties = new Properties();
            this.properties.load(propertiesUri.openStream());
            Iterator propertyIt = this.properties.keySet().iterator();
            while (propertyIt.hasNext())
            {
                String property = (String)propertyIt.next();
                String value = this.properties.getProperty(property);
                if (logger.isDebugEnabled())
                    logger.debug("setting property '" + property
                        + "' with --> '" + value + "'");
                this.velocityEngine.setProperty(property, value);
            }
        }
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(java.lang.String,
     *      java.util.Map, java.io.StringWriter)
     */
    public void processTemplate(
        String templateFile,
        Map templateObjects,
        Writer output) throws Exception
    {
        final String methodName = "VelocityTemplateEngine.processTemplate";
        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with templateFile '"
                + templateFile + "' and templateObjects '" + templateObjects
                + "'");
        ExceptionUtils.checkEmpty(methodName, "templateFile", templateFile);
        ExceptionUtils.checkNull(methodName, "output", output);
        this.velocityContext = new VelocityContext();

        // copy the templateObjects to the velocityContext
        if (templateObjects != null)
        {
            Iterator namesIt = templateObjects.keySet().iterator();
            while (namesIt.hasNext())
            {
                String name = (String)namesIt.next();
                Object value = templateObjects.get(name);
                velocityContext.put(name, value);
            }
        }
        Template template = this.velocityEngine.getTemplate(templateFile);
        template.merge(velocityContext, output);
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String)
     */
    public String getEvaluatedExpression(String expression)
    {
        String evaluatedExpression = null;
        if (this.velocityContext != null && StringUtils.isNotEmpty(expression))
        {
            try
            {
                StringWriter writer = new StringWriter();
                this.velocityEngine.evaluate(
                    this.velocityContext,
                    writer,
                    "mylogtag",
                    expression);
                evaluatedExpression = writer.toString();
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing VelocityTemplateEngine.getEvaluatedExpression";
                logger.error(errMsg, th);
                throw new TemplateEngineException(errMsg, th);
            }
        }
        return evaluatedExpression;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getMacroLibraries()
     */
    public List getMacroLibraries()
    {
        return macrolibs;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#addMacroLibrary(java.lang.String)
     */
    public void addMacroLibrary(String libraryName)
    {
        this.macrolibs.add(libraryName);
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(java.lang.String)
     */
    public void setMergeLocation(String mergeLocation)
    {
        this.mergeLocation = mergeLocation;
    }

    /**
     * <p>
     * This class receives log messages from VelocityTemplateEngine and forwards
     * them to the concrete logger that is configured for this cartridge.
     * </p>
     * <p>
     * This avoids creation of one large VelocityTemplateEngine log file where
     * errors are difficult to find and track.
     * </p>
     * <p>
     * Error messages can now be traced to plugin activities.
     * </p>
     */
    private class VelocityLoggingReceiver
        implements LogSystem
    {
        /**
         * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
         */
        public void init(RuntimeServices arg0) throws Exception
        {}

        /**
         * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int,
         *      java.lang.String)
         */
        public void logVelocityMessage(int level, String message)
        {
            switch (level)
            {
                case LogSystem.WARN_ID :
                    logger.info(message);
                    break;
                case LogSystem.INFO_ID :
                    logger.info(message);
                    break;
                case LogSystem.DEBUG_ID :
                    logger.debug(message);
                    break;
                case LogSystem.ERROR_ID :
                    logger.info(message);
                    break;
                default :
                    logger.debug(message);
                    break;
            }
        }
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#shutdown()
     */
    public void shutdown()
    {
        this.shutdownLogger();
        this.velocityEngine = null;
    }

    /**
     * Opens a log file for this plugin.
     * 
     * @throws IOException if the file cannot be opened
     */
    private void initLogger(String pluginName) throws IOException
    {
        logger = AndroMDALogger.getNamespaceLogger(pluginName);
        logger.setAdditivity(false);
        FileAppender appender = new FileAppender(
            new PatternLayout("%-5p %d - %m%n"),
            AndroMDALogger.getNamespaceLogFileName(pluginName),
            true);
        logger.addAppender(appender);
    }

    /**
     * Shutdown the associated logger.
     */
    private void shutdownLogger()
    {
        Enumeration appenders = logger.getAllAppenders();
        while (appenders.hasMoreElements())
        {
            Appender appender = (Appender)appenders.nextElement();
            if (appender.getName() != null)
            {
                appender.close();
            }
        }
    }

}