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
     * Returns a decorator for a metaobject, depending on its <code>metaclass</code> and 
     * (optionally) its sterotype and <code>contextName</code>.  This is useful when you
     * want an element to use a certain type of facade (instead of a previous registered facade).
     * For example: with an <em>Entity</em>, you may want all attributes to be created
     * as <em>EntityAttributes</em> but it wouldn't make sense to stereotype each attribute 
     * as an <em>EntityAttribute</em> (since you know that this attributes falls within the 
     * context of an <em>Entity</em>.
     * @param metaobject the meta model element.
     * @param contextName the name of the context the meta 
     *                    model element is registered under.
     * @return
     */
    private DecoratorBase createDecoratorObject(ModelElement metaobject, String contextName) {
    	
    	String methodName = "DecoratorBase.createDecoratorObject";
    	
    	ExceptionUtils.checkNull(
    			"DecoratorBase.createDecoratorObject",
				"metaobject",
				metaobject);

    	//if the metaobject ALREADY IS a Decorator
    	//return the metaobject since we don't want to try and create a
    	//Decorator from a Decorator.
    	if (metaobject instanceof DecoratorBase)
    	{
    		return (DecoratorBase) metaobject;
    	}
    	
    	DecoratorBase result = null;
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
    		
    		result =
    			(DecoratorBase) ConstructorUtils.invokeConstructor(
    					metafacadeClass,
						metaobject);
    		
    		this.populatePropertyReferences(result,
    				mappings.getPropertyReferences(this.getActiveNamespace()));
    		
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
    	result.setLogger(internalGetLogger());

    	return result;
    }
    
    /**
     * Returns a decorator for a metaobject, depending on its
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
    protected void populatePropertyReferences(DecoratorBase metafacade, Collection propertyReferences) {
    	
    	// only add the property once per context
    	final String methodName = "MetafacadeFactory.populatePropertyReferences";
    	ExceptionUtils.checkNull(methodName, "propertyReferences", propertyReferences);
    	
    	Iterator referenceIt = propertyReferences.iterator();		
    	while (referenceIt.hasNext()) {
    		String reference = (String)referenceIt.next();

    		// ensure that each property is only set once per context
    		// for performance reasons
    		if (!this.isPropertyRegistered(metafacade.getPropertyNamespace(), reference)) {
    			String value =   
    				Namespaces.instance().findNamespaceProperty(
    						this.getActiveNamespace(), reference);
    			
    			if (internalGetLogger().isDebugEnabled())
    				internalGetLogger().debug("setting namespace property '" 
    					+ reference + "' with value '" 
						+ value + "' in namespace '" 
						+ this.getActiveNamespace() + "'");
    			
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
