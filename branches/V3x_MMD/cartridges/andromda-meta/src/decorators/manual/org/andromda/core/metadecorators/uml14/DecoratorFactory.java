package org.andromda.core.metadecorators.uml14;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.andromda.core.common.ExceptionUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

public class DecoratorFactory
{
    private static DecoratorFactory factory = new DecoratorFactory();

    private HashMap namespaces = new HashMap();
    private String activeNamespace;
    private org.omg.uml.UmlPackage model;

    // just to make sure that nobody instantiates it
    private DecoratorFactory()
    {
        registerCoreDecoratorClasses();
    }

    /**
     *
     */
    private void registerCoreDecoratorClasses()
    {
        setActiveNamespace("core");
        registerDecoratorClass(
            "org.omg.uml.modelmanagement.UmlPackage$Impl",
            PackageDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.modelmanagement.Model$Impl",
            PackageDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlClass$Impl",
            ClassifierDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.DataType$Impl",
            ClassifierDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.Interface$Impl",
            ClassifierDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.AssociationEnd$Impl",
            AssociationEndDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlAssociation$Impl",
            AssociationEndDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.Dependency$Impl",
            DependencyDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.TaggedValue$Impl",
            TaggedValueDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.Operation$Impl",
            OperationDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            AttributeDecoratorImpl.class.getName());
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
        HashMap namespace = (HashMap) namespaces.get(namespaceName);
        if (namespace == null)
        {
            namespace = new HashMap();
            namespaces.put(namespaceName, namespace);
        }
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
     * Registers a decorator class for a given metaclass and (optionally)
     * a stereotype.
     *
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @param decoratorClassName the FQCN of the decorator class
     */
    public void registerDecoratorClass(
        String umlMetaClassName,
        String stereotypeName,
        String decoratorClassName)
    {
        registerDecoratorClass(
            umlMetaClassName,
            null,
            stereotypeName,
            decoratorClassName);
    }

    /**
     * Registers a decorator class for a given metaclass.
     *
     * @param umlMetaClassName the FQCN of the metaclass
     * @param decoratorClassName the FQCN of the decorator class
     */
    public void registerDecoratorClass(
        String umlMetaClassName,
        String decoratorClassName)
    {
        registerDecoratorClass(
            umlMetaClassName,
            null,
            null,
            decoratorClassName);
    }

    /**
     * Registers a decorator class for a given metaclass and (optionally)
     * a context name and a stereotype.
     *
     * @param umlMetaClassName
     * @param contextName
     * @param stereotypeName
     * @param decoratorClassName
     */
    private void registerDecoratorClass(
        String umlMetaClassName,
        String contextName,
        String stereotypeName,
        String decoratorClassName)
    {
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);

        DecoratorMappingRuleList ruleList =
            (DecoratorMappingRuleList) namespace.get(umlMetaClassName);
        if (ruleList == null) // no mapping for this metaclass yet
        {
            ruleList = new DecoratorMappingRuleList();
        }

        ruleList.addRule(
            new DecoratorMappingRule(
                contextName,
                stereotypeName,
                decoratorClassName));

        namespace.put(umlMetaClassName, ruleList);
    }

    /**
     * Looks up a registered decorator class name for a metaclass name and
     * (optionally) a stereotype.
     *
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @return the FQCN of the decorator class to be instantiated
     */
    String lookupDecoratorClass(
        String umlMetaClassName,
        String stereotypeName)
    {
        return lookupDecoratorClass(umlMetaClassName, null, stereotypeName);
    }

    /**
     * Looks up a registered decorator class name for a metaclass name and
     * (optionally) a context name and a stereotype.
     *
     * @param umlMetaClassName the FQCN of the metaclass
     * @param contextName the name of the context (may be null)
     * @param stereotypeName the stereotype name (may be null)
     * @return the FQCN of the decorator class to be instantiated
     */
    String lookupDecoratorClass(
        String umlMetaClassName,
        String contextName,
        String stereotypeName)
    {
        // first, lookup in active namespace
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);
        String decoratorClassName =
            internalLookupDecoratorClass(
                namespace,
                umlMetaClassName,
                contextName,
                stereotypeName);
        if (decoratorClassName != null)
        {
        	if (internalGetLogger().isDebugEnabled())
	            internalGetLogger().debug(
	                "lookupDecoratorClass: "
	                    + umlMetaClassName
	                    + " -> "
	                    + decoratorClassName);
            return decoratorClassName;
        }

        // if not found, lookup in core namespace
        namespace = (HashMap) namespaces.get("core");
        decoratorClassName =
            internalLookupDecoratorClass(
                namespace,
                umlMetaClassName,
                contextName,
                stereotypeName);
        if (internalGetLogger().isDebugEnabled())
	        internalGetLogger().debug(
	            "lookupDecoratorClass: "
	                + umlMetaClassName
	                + " -> "
	                + decoratorClassName);
        return decoratorClassName;
    }

    /**
     * Internal helper class for lookup. Called twice, once with the
     * active namespace, once more with the "core" namespace.
     *
     * @param namespace the namespace to search
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @return the FQCN of the decorator class to be instantiated
     */
    private String internalLookupDecoratorClass(
        HashMap namespace,
        String umlMetaClassName,
        String contextName,
        String stereotypeName)
    {
        DecoratorMappingRuleList ruleList =
            (DecoratorMappingRuleList) namespace.get(umlMetaClassName);
        if (ruleList == null) // no mapping for this metaclass yet
        {
            return null;
        }
        return ruleList.findDecorator(contextName, stereotypeName);
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
    	String stereotypeName = getStereotypeName(metaobject);
    	String decoratorClassName =
    		lookupDecoratorClass(
    				metaobject.getClass().getName(),
					contextName,
					stereotypeName);
    	DecoratorBase result;

    	if (decoratorClassName == null)
    	{
    		// if no special decorator is registered, simply
    		// return a decorator for a standard model element.
    		result = new ModelElementDecoratorImpl(metaobject);
    	}
    	else
    	{
    		try
			{
    			Class dynamicClass = Class.forName(decoratorClassName);
    			Constructor constructor =
    				findConstructor(dynamicClass, metaobject.getClass());

    			Object[] constructorParams = { metaobject };

    			result =
    				(DecoratorBase) constructor.newInstance(
    						constructorParams);

    			result.validate();
    		}
    		catch (Exception e)
			{
    			internalGetLogger().error(e);
    			return null;
    		}
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
     * Finds the right constructor to create an object of class
     * <code>dynamicClass</code>, using a parameter of type
     * <code>parameterClass</code>.
     *
     * @param dynamicClass class in which the constructor should be found
     * @param parameterClass type of parameter that the constructor should accept
     * @return the appropriate constructor or null
     */
    private Constructor findConstructor(
        Class dynamicClass,
        Class parameterClass)
    {
        Constructor[] c = dynamicClass.getConstructors();
        for (int i = 0; i < c.length; i++)
        {
            Class[] ptypes = c[i].getParameterTypes();
            if (ptypes.length == 1
                && ptypes[0].isAssignableFrom(parameterClass))
            {
                return c[i];
            }
        }
        return null;
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
     * Return the name of the first stereotype attached to a given
     * model element.
     * @param modelElement the model element
     * @return String the stereotype name or null if the element has no stereotype
     */
    private String getStereotypeName(ModelElement modelElement)
    {
        Collection stereotypes = modelElement.getStereotype();
        for (Iterator i = stereotypes.iterator(); i.hasNext();)
        {
            ModelElement stereotype = (ModelElement) i.next();
            return stereotype.getName();
        }

        return null;
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

    // ----------- these methods support unit testing ---------------

    int getNamespaceCount()
    {
        return namespaces.size();
    }

    int getDecoratorCount()
    {
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);
        return namespace.size();
    }
}
