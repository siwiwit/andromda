package org.andromda.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Creates and returns Objects based on the a set of 
 * Apache Digester rules.
 * 
 * @author Chad Brandon
 */
public class XmlObjectFactory {

	private static final Logger logger = Logger.getLogger(XmlObjectFactory.class);
	
	private static final String RULES_SUFFIX = "-Rules.xml";
    private static final String SCHEMA_SUFFIX = ".xsd";
    
    private Digester digester = null;
    
    private Class objectClass = null;
    private URL objectRulesXml = null;
    private URL schemaUri = null;
    
    /**
     * Cache containing XmlObjectFactory instances
     * which have already been configured for given
     * objectRulesXml
     */
    private static Map factoryCache = new HashMap();
    
    /**
     * Should the XmlObjectFactory be validating
     */
    private boolean validating = false;
    
    /**
     * The validation feature.
     */
    protected static final String VALIDATION = 
        "http://xml.org/sax/features/validation";
        
    /**
     * The schema validation feature.
     */
    protected static final String VALIDATION_SCHEMA =
        "http://apache.org/xml/features/validation/schema";
    
    /**
     * Creates an instance of this XmlObjectFactory with the
     * given <code>objectRulesXml</code>
     * @param objectRulesXml
     */
    private XmlObjectFactory(URL objectRulesXml) {
    	final String methodName = "XmlObjectFactory.XmlObjectFactory";
        if (logger.isDebugEnabled())
            logger.debug("performing " 
                + methodName
                + " with objectRulesXml '"
                + objectRulesXml + "'");
        ExceptionUtils.checkNull(methodName, "objectRulesXml", objectRulesXml);
        this.digester = DigesterLoader.createDigester(objectRulesXml);           
    }
    
    /**
     * Gets an instance of this XmlObjectFactory using
     * the digester rules belonging to the <code>objectClass</code>.
     * 
     * @return the shared XmlObjectFactoy instance.
     */
    public static XmlObjectFactory getInstance(Class objectClass) {
        return getInstance(objectClass, true);
    }

    /**
     * Gets an instance of this XmlObjectFactory using
     * the digester rules belonging to the <code>objectClass</code>.
     * 
     * @param objectClass the Class of the object from which to configure this factory.
     * @param validating true/false on whether or not this configuration 
     *        XML should be validated (false is default)
     * @return the shared XmlObjectFactoy instance.
     */    
    private static XmlObjectFactory getInstance(Class objectClass, boolean validating) {
        final String methodName = "XmlObjectFactory.getInstance";
        ExceptionUtils.checkNull(methodName, "objectClass", objectClass);

        URL objectRulesXml =
            XmlObjectFactory.class.getResource(
                '/' + objectClass.getName().replace('.', '/') + RULES_SUFFIX);    
        
        XmlObjectFactory factory = (XmlObjectFactory)factoryCache.get(objectRulesXml);
        if (factory == null) {
            factory = new XmlObjectFactory(objectRulesXml);
            factory.objectClass = objectClass;
            factory.objectRulesXml = objectRulesXml;
        }
  
        if (validating) {
            factory.setValidating(validating);      
        }
        
        return factory;    
    }
    
    /**
     * Sets whether or not the XmlObjectFactory
     * should be validating, default is false.  If it IS set
     * to be validating, then there needs to be a schema named
     * objectClass.xsd in the same package as the objectClass 
     * that this factory was created from.
     * 
     * @param validating true/false 
     */
    public void setValidating(boolean validating) {
        final String methodName = "XmlObjectFactory.setValidating";
        
        if (this.schemaUri == null) {
            String schemaLocation = 
                '/' + this.objectClass.getName().replace('.', '/') + SCHEMA_SUFFIX;
            this.schemaUri =
                XmlObjectFactory.class.getResource(schemaLocation);
            try {
                if (this.schemaUri != null) {
                    InputStream stream = this.schemaUri.openStream();
                    stream.close();
                }
            } catch (IOException ex) {
                this.schemaUri = null;
            }
            if (this.schemaUri == null) {
                logger.warn("WARNING! Was not able to find schemaUri --> '" 
                    + schemaLocation
                    + "' continuing in non validating mode");
            }
        }
       
        if (this.schemaUri != null) { 
            this.validating = validating;
            this.digester.setValidating(validating);
            this.digester.setSchema(this.schemaUri.toString());
            this.digester.setErrorHandler(new XmlObjectValidator());
        }
    }
    
	/**
	 * Returns a configured Object based on the objectXml configuration file
	 * 
	 * @param objectRulesXml the path to the XML resource config file that contains
	 *        the rules about the Object configuration file
	 * @param objectXml the path to the Object XML config file.
	 * @return Object the created instance.
	 */
	public Object getObject(URL objectXml) {
		final String methodName = "XmlObjectFactoryException.getObject";
		if (logger.isDebugEnabled())
			logger.debug("performing " 
				+ methodName
				+ " with objectXml '"
				+ objectXml 
				+ "'");

		ExceptionUtils.checkNull(methodName, "objectXml", objectXml);

		Object object = null;
		try {
			object =
				(Object) this.digester.parse(objectXml.openStream());
			if (object == null) {
				String errMsg = 
					"Was not able to instantiate an object using objectRulesXml '" 
					+ this.objectRulesXml 
					+ "' with objectXml '" 
					+ objectXml 
					+ "', please check either the objectXml "
					+ "or objectRulesXml file for inconsistencies";
				logger.error(errMsg);
				throw new XmlObjectFactoryException(errMsg);
			}
        } catch (SAXException ex) {
            String validationErrorMsg = "VALIDATION FAILED for --> '" 
                + objectXml + "' against SCHEMA --> '" 
                + this.schemaUri  + "' --> message: '" + ex.getMessage() + "'";
            logger.error(validationErrorMsg);
            throw new XmlObjectFactoryException(validationErrorMsg);
		} catch (Throwable th) {
			String errMsg = "Error performing " 
                + methodName 
                + ", XML resource could not be loaded --> '" 
                + objectXml + "'";
			logger.error(errMsg, th);
			throw new XmlObjectFactoryException(errMsg, th);	
		}
		return object;
	}
    
    /**
     * Handles the validation errors.
     */ 
    protected class XmlObjectValidator implements org.xml.sax.ErrorHandler {
   
        public XmlObjectValidator() {
            final String methodName = "XmlObjectValidator";
            if (logger.isDebugEnabled()) {
                logger.debug("constructing new " + methodName); 
            }   
        }
        
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(exception.getMessage());
        }
        
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(exception.getMessage());
        }
        
        public void warning(SAXParseException exception) {
        	logger.warn("WARNING!: " + exception.toString());   
        }

    }
    
}
