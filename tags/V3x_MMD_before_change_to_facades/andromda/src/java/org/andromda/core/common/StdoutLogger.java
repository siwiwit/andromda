package org.andromda.core.common;

import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This is a logger that writes to stdout. At the moment,
 * it is written in the simplest possible way.
 * 
 * @since 26.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class StdoutLogger
{
    private static Logger logger = Logger.getLogger("andromda");
    
    /**
     * Configures logging for the AndroMDA application
     * from the the xml resource "log4j.xml" found within
     * the same package as this class.     
     */
    public static void configure() {
    	final String methodName = "StdoutLogger.configure";
    	String loggingConfiguration = "log4j.xml";
    	URL url = StdoutLogger.class.getResource(loggingConfiguration);
    	if (url == null) {
    		throw new RuntimeException(methodName
    				+ " - could not find Logger configuration file '" 
					+ loggingConfiguration + "'");
    	}
    	configure(url);
    }
    
    /**   
     * Configures the Logger from the passed in logConfigurationXml 
     * 
     * @param logConfigurationXml
     */
    protected static void configure(URL logConfigurationXml) {
    	try {
    		DOMConfigurator.configure(logConfigurationXml);
    	} catch (Exception ex) {
    		System.err.println(
    				"Unable to initialize logging system with configuration file ("
    				+ logConfigurationXml
					+ ") --> using basic configuration.");
    		ex.printStackTrace();
    		BasicConfigurator.configure();
    	}
    }
    
    public static void debug (Object o)
    {
        logger.debug(o);
    }
    public static void info (Object o)
    {
        logger.info(o);
    }
    public static void warn (Object o)
    {
        logger.warn(o);
    }
    public static void error (Object o)
    {
        logger.error(o);
    }
}
