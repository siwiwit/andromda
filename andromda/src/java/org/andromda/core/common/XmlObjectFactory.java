package org.andromda.core.common;

import java.net.URL;

import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;

/**
 * Creates and returns Objects based on the a set of 
 * Apache Digester rules.
 * 
 * @author Chad Brandon
 */
public class XmlObjectFactory {

	private static Logger logger = Logger.getLogger(XmlObjectFactory.class);
	
	private static final String RULES_SUFFIX = "-Rules.xml";

	/**
	 * Returns a new instance of the object specified in 
	 * the rulesXml
	 * 
	 * @param objectClass the class for the object we are instantiating
	 *        (the expected rules will be passed on this objectClass name)
	 * @param objectXml the digester XML that configures the object.
	 * 
	 * @return the configured Object
	 */
	public static Object getInstance(Class objectClass, URL objectXml) {
		final String methodName = "XmlObjectFactory.getInstance";
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName
				+ " with objectXml (" + objectXml+ ")");
		
		ExceptionUtils.checkNull(methodName, "objectXml", objectXml);
		ExceptionUtils.checkNull(methodName, "objectClass", objectClass);
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		URL objectRulesXml =
			XmlObjectFactory.class.getResource(
				'/' + objectClass.getName().replace('.', '/') + RULES_SUFFIX);
		Object object = null;
		try {
			object = getObject(objectRulesXml, objectXml, loader);
		} catch (Exception ex) {
			String errMsg = "Could not load XmlOjbect from configuration file --> '"
				+ objectXml + "'";
			logger.error(errMsg, ex);
			throw new XmlObjectFactoryException(errMsg, ex);	
		}
		return object;
	}

	/**
	 * Returns a configured Object based on the objectXml configuration file
	 * 
	 * @param objectRulesXml the path to the XML resource config file that contains
	 *        the rules about the Object configuration file
	 * @param objectXml the path to the Object XML config file.
	 * @param loader the ClassLoader to use with Digester.
	 * @return Object the created instance.
	 */
	private static Object getObject(URL objectRulesXml, URL objectXml, ClassLoader loader) {
		final String methodName = "XmlObjectFactory.getObject";
		if (logger.isDebugEnabled())
			logger.debug("performing "+ methodName
				+ " with objectRulesXml ("
				+ objectRulesXml + ") and objectXml ("
				+ objectXml + ") and loader (" + loader + ")");
		
		ExceptionUtils.checkNull(methodName, "objectRulesXml", objectRulesXml);
		ExceptionUtils.checkNull(methodName, "objectXml", objectXml);

		Object object = null;
		try {
			object =
				(Object) DigesterLoader.load(objectRulesXml, loader, objectXml);
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new XmlObjectFactoryException(errMsg, ex);	
		}
		return object;
	}
}
