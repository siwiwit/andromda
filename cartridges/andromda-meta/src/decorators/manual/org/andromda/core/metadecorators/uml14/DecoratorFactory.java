package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.core.metafacade.MetafacadeMapping;
import org.andromda.core.metafacade.MetafacadeMappings;
import org.andromda.core.metafacade.MetafacadeMappingsException;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

public class DecoratorFactory
{
    private static DecoratorFactory factory = new DecoratorFactory();

    private String activeNamespace;
    private org.omg.uml.UmlPackage model;

    /**
     * Caches the registered properties used
     * within metafacades.
     */
    private Map registeredProperties = new HashMap();

    // just to make sure that nobody instantiates it
    private DecoratorFactory()
    {
        MetafacadeMappings.instance().discoverMetafacades();
    }

    /**
     * Returns the decorator factory singleton.
     * @return the only instance
     */
    public static DecoratorFactory getInstance()
    {
        return factory;
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge
     * have their own namespace for decorator registrations.
     *
     * @param namespaceName the name of the namespace
     */
    public void setActiveNamespace(String namespaceName)
    {
        this.activeNamespace = namespaceName;
    }

    /**
     * Returns the name of the active namespace.
     *
     * @return String the namespace name
     */
    public String getActiveNamespace()
    {
        return this.activeNamespace;
    }

    /**
     * Returns a metafacade for a metaobject, depending on its <code>metaclass</code> and
     * (optionally) its sterotype and <code>contextName</code>.
     * @param metaobject the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @return the new metafacade
     */
    protected DecoratorBase createDecoratorObject(ModelElement metaobject, String contextName) {

    	String methodName = "DecoratorFactory.createMetafacade";

    	ExceptionUtils.checkNull(
    			methodName,
				"metaobject",
				metaobject);

    	//if the metaobject ALREADY IS a Decorator
    	//return the metaobject since we don't want to try and create a
    	//Decorator from a Decorator.
    	if (metaobject instanceof DecoratorBase)
    	{
    		return (DecoratorBase) metaobject;
    	}

    	DecoratorBase metafacade = null;
    	Class metaobjectClass = null;
    	Class metafacadeClass = null;
    	try {

    		metaobjectClass = metaobject.getClass();
    		String metaobjectClassName = metaobjectClass.getName();

    		MetafacadeMappings mappings =
    			MetafacadeMappings.instance();

			Collection stereotypeNames =
				this.getStereotypeNames(metaobject);
			MetafacadeMapping mapping = null;
			if (metafacadeClass == null) {
				stereotypeNames =
					this.getStereotypeNames(metaobject);
				if (this.internalGetLogger().isDebugEnabled())
					this.internalGetLogger().debug("metaobject stereotype names --> '"
							+ stereotypeNames + "'");
				mapping = mappings.getMetafacadeMapping(
						metaobjectClassName,
						stereotypeNames,
						this.getActiveNamespace(),
						contextName);
				if (mapping != null) {
					metafacadeClass = mapping.getMetafacadeClass();
				} else {
					// get the default since no mapping was found.
					metafacadeClass = mappings.getDefaultMetafacadeClass(this.activeNamespace);
					if (this.internalGetLogger().isDebugEnabled())
						this.internalGetLogger().debug("Meta object model class '"
								+ metaobjectClass
								+ "' has no corresponding meta facade class, default is being used --> '"
								+ metafacadeClass + "'");
				}
			}

			if (metafacadeClass == null) {
				throw new MetafacadeMappingsException(methodName
						+ " metafacadeClass was not retrieved from mappings"
						+ " or specified as an argument in this method for metaobject --> '"
						+ metaobject + "'");
			}

    		if (internalGetLogger().isDebugEnabled())
    			if (internalGetLogger().isDebugEnabled())
    				internalGetLogger().debug(
    						"lookupDecoratorClass: "
    						+ metaobjectClassName
							+ " -> "
							+ metafacadeClass);


    		metafacade =
    			(DecoratorBase) ConstructorUtils.invokeConstructor(
    					metafacadeClass,
						metaobject);

				// set this namespace to the metafacade's namespace
    		metafacade.setNamespace(this.getActiveNamespace());
			this.populatePropertyReferences(
				metafacade,
				mappings.getPropertyReferences(this.getActiveNamespace()));

			// now populate any context property references (if
			// we have any)
			if (mapping != null) {
 				this.populatePropertyReferences(
 					metafacade,
 					mapping.getPropertyReferences());
			}

            // validate the meta-facade
            metafacade.validate();

    	} catch (Throwable th) {
    		String errMsg = "Failed to construct a meta facade of type '"
    			+ metafacadeClass
				+ "' with metaobject of type --> '"
				+ metaobjectClass + "'";
    		internalGetLogger().error(errMsg, th);
    		throw new MetafacadeException(errMsg, th);
    	}

    	// make sure that the decorator has a proper logger associated
    	// with it.
    	metafacade.setLogger(internalGetLogger());

    	return metafacade;
    }

    /**
     * Returns a metafacade for a metaobject, depending on its
     * metaclass and (optionally) its stereotype.
     *
     * @param metaobject the model element
     * @return DecoratorBase the decorator object (not yet attached to metaclass object)
     */
    public DecoratorBase createDecoratorObject(ModelElement metaobject)
    {
    	return this.createDecoratorObject(metaobject, null);
    }

    /**
     * Populates the metafacade with the values retrieved from the property references
     * in the <code>propertyuReferences</code> Map.
     * They are keyed by the <code>context</code> member of an an instance of
     * this class.
     *
     * @param propertyReferences the collection of property references to add.
     */
	/**
	 * Populates the metafacade with the values retrieved from the property references
	 * found in the <code>propertyReferences</code> collection.
	 *
	 * @param propertyReferences the collection of property references which we'll populate.
	 */
	protected void populatePropertyReferences(DecoratorBase metafacade, Collection propertyReferences) {

		// only add the property once per context
		final String methodName = "DecoratorFactory.populatePropertyReferences";
		ExceptionUtils.checkNull(methodName, "propertyReferences", propertyReferences);

		Iterator referenceIt = propertyReferences.iterator();
		while (referenceIt.hasNext()) {
			String reference = (String)referenceIt.next();

			// ensure that each property is only set once per context
			// for performance reasons
			if (!this.isPropertyRegistered(
					metafacade.getPropertyNamespace(),
					reference)) {
				String value =
					Namespaces.instance().findNamespaceProperty(
							this.getActiveNamespace(), reference);

				if (this.internalGetLogger().isDebugEnabled())
					this.internalGetLogger().debug("setting context property '"
							+ this.getActiveNamespace() + "' with value '"
							+ value + "'");

				if (value != null) {
					try {
						PropertyUtils.setProperty(metafacade, reference, value);
					} catch (Exception ex) {
						String errMsg = "Error setting property '" + reference
							+ "' on metafacade --> '" + metafacade + "'";
						this.internalGetLogger().error(errMsg, ex);
						//don't throw the exception
					}
				}
			}
		}
	}

    /**
     * Returns a metafacade for each metaobject, contained within the <code>metaobjects</code>
     * collection depending on its <code>metaclass</code> and
     * (optionally) its sterotype and <code>contextName</code>.
     * @param metaobjects the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @return
     */
	protected Collection createDecoratorObjects(Collection metaobjects, String contextName) {
		Collection metafacades = new ArrayList();
		if (metaobjects != null && !metaobjects.isEmpty()) {
			Iterator metaobjectIt = metaobjects.iterator();
			while (metaobjectIt.hasNext()) {
				metafacades.add(createDecoratorObject((ModelElement)metaobjectIt.next(), contextName));
			}
		}
		return metafacades;
	}

    /**
     * Create a decorator for the whole model itself.
     *
     * @param model the model as a package
     * @return a decorator for the model
     */
    public ModelDecorator createDecoratorObject(UmlPackage model)
    {
        return new ModelDecoratorImpl(model);
    }

    /**
     * Return the names of the stereotype for the <code>modelElement</code>.
     * @param modelElement the model element
     * @return String the stereotype name or null if the element has no stereotype
     */
    private Collection getStereotypeNames(ModelElement modelElement)
    {
    	Collection stereotypeNames = new ArrayList();

    	Collection stereotypes = modelElement.getStereotype();
    	for (Iterator stereotypeIt = stereotypes.iterator();
    	stereotypeIt.hasNext();) {
    		ModelElement stereotype = (ModelElement) stereotypeIt.next();
    		stereotypeNames.add(StringUtils.trimToEmpty(stereotype.getName()));
    	}
    	return stereotypeNames;
    }

    /**
     * @return the model
     */
    public org.omg.uml.UmlPackage getModel()
    {
        return model;
    }

    /**
     * @param model the model
     */
    public void setModel(org.omg.uml.UmlPackage model)
    {
        this.model = model;
    }

    private Logger internalGetLogger()
    {
        if (!"core".equals(activeNamespace))
            return Logger.getLogger(
                "org.andromda.cartridges." + activeNamespace);
        return Logger.getRootLogger();
    }

    /**
     * Registers a property with the specified <code>name</code>
     * in the given <code>namespace</code>, having the specified <code>value</code>.
     * @param namespace the namespace in which the property is stored.
     * @param name the name of the property
     * @param value the value of the property.
     */
    protected void registerProperty(String namespace, String name, Object value) {
    	final String methodName = "DecoratorFactory.registerProperty";
    	ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
    	ExceptionUtils.checkEmpty(methodName, "name", name);
    	ExceptionUtils.checkNull(methodName, "value", value);

    	Map propertyNamespace = (Map)this.registeredProperties.get(namespace);
    	if (propertyNamespace != null) {
    		propertyNamespace.put(name, value);
    	} else {
    		propertyNamespace = new HashMap();
    		propertyNamespace.put(name, value);
			this.registeredProperties.put(namespace, propertyNamespace);
		}
    }

    /**
     * Returns true if this property is registered, false otherwise.
     * @param namespace
     * @param name
     * @return boolean
     */
    protected boolean isPropertyRegistered(String namespace, String name) {
    	boolean registered = false;
    	Map propertyNamespace = (Map)this.registeredProperties.get(namespace);
    	if (propertyNamespace != null) {
    		registered = propertyNamespace.containsKey(name);
    	}
    	return registered;
    }

    /**
     * Returns true if this property is registered, false otherwise.
     * @param namespace the namespace of the property to check.
     * @param name the name of the property to check.
     * @return boolean
     */
    protected Object getRegisteredProperty(String namespace, String name) {
    	final String methodName = "DecoratorFactory.getRegisteredProperty";
    	Object registeredProperty = null;
    	Map propertyNamespace = (Map)this.registeredProperties.get(namespace);
    	if (propertyNamespace == null) {
    		throw new MetafacadeException(methodName +
    			" - no properties registered under namespace --> '" + namespace + "'");
    	} else {
    		registeredProperty = propertyNamespace.get(name);
    		if (registeredProperty == null) {
    			throw new MetafacadeException(methodName
    				+ " - no property '"
    				+ name
					+ "' registered under namespace '" + namespace + "'");
    		}
    	}
    	return registeredProperty;
    }


}
