package org.andromda.core.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Dictionary of configurable Namespace objects.  
 * Namespace objects are used for configuring Plugin
 * instances.
 * 
 * @see org.andromda.core.common.Namespace
 * 
 * @author Chad Brandon
 */
public class Namespaces {
	
	private static Logger logger = Logger.getLogger(Namespaces.class);
	
	private final static Namespaces instance = new Namespaces();

	/**
	 * This is passed as the cartridge name for the findNamespaceProperty
	 * method if we wish to use a 'default' Namespace for Plugins.
	 * This is so we don't need to define a specific mapping for each Plugin
	 * if we don't want.  If a namespaceName exists with
	 * a specific Plugin name, then that will be used instead
	 * of the 'default'
	 */
	public static final String DEFAULT = "default";

	private Map namespaces;

	/**
	 * Constructs an instance of Namespaces.
	 */
	public Namespaces() {
		this.namespaces = new HashMap();
	}

	/**
	 * Returns the singleton instance of this Namespaces
	 * @return instance.
	 */
	public static Namespaces instance() {
		return instance;
	}

	/**
	 * Adds a mapping for a namespaces name to a physical directory.
	 * 
	 * @param namespace the Namespace to add to this instance.
	 */
	public void addNamespace(Namespace namespace) {
		namespaces.put(namespace.getName(), namespace);
	}

	/**
	 * Retrieves a property from the Namespace with the namespaceName.
	 * 
	 * @param namespaceName name of the Plugin to which the contexdt applies
	 * @param propertyName name of the namespace property to find.
	 * @return String the namespace property value.
	 */
	public String findNamespaceProperty(String namespaceName, String propertyName) {
		final String methodName = "Namespaces.findNamespaceProperty";
		ExceptionUtils.checkEmpty(methodName, "namespaceName", namespaceName);
		ExceptionUtils.checkEmpty(methodName, "propertyName", propertyName);
		
		Property property = null;
		
		Namespace namespace = (Namespace)namespaces.get(namespaceName);

		if (namespace != null) {
			property = namespace.getProperty(propertyName);
		}
		
		//since we couldn't find a Namespace for the specified cartridge,
		//try to lookup the default
		if (property == null) {
			if (logger.isDebugEnabled())
				logger.debug("no namespace with name '" 
					+ namespaceName + "' found, looking for " + DEFAULT);
			namespace = (Namespace)namespaces.get(DEFAULT);

			if (namespace != null) {
				property = namespace.getProperty(propertyName);
			}
		}

		String result = null;
		if (property == null) {
			logger.error("ERROR! No 'default' or '" 
				+ namespaceName + "' namespace defined for property '"
				+ propertyName + "' --> please define a namespace with" 
				+ " at least one of these names for property '" 
				+ propertyName + "' in your build file");
		} else {
			result = property.getValue();
		}

		return result;
	}
}
