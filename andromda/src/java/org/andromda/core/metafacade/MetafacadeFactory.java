package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ModelFacade;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MetafacadeFactory
{
    private static MetafacadeFactory factory = new MetafacadeFactory();

    private String activeNamespace;
    private ModelFacade model;

    /**
     * Caches the registered properties used
     * within metafacades.
     */
    private Map registeredProperties = null;

    // constructor is private to make sure that nobody instantiates it
    private MetafacadeFactory()
    {
        registeredProperties = new HashMap();
        MetafacadeMappings.instance().discoverMetafacades();
    }

    /**
     * Returns the facade factory singleton.
     * @return the only instance
     */
    public static MetafacadeFactory getInstance()
    {
        return factory;
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge
     * have their own namespace for facade registrations.
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
    public MetafacadeBase createMetafacade(
        Object metaobject,
        String contextName)
    {
        return this.internalCreateMetafacade(
            metaobject,
            contextName,
            null);
    }

    /**
     * Internal helper method.
     * 
     * @param metaobject the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @param metafacadeClass if not null, it contains the name of the metafacade class to be used
     * @return the new metafacade 
     */
    private MetafacadeBase internalCreateMetafacade(
        Object metaobject,
        String contextName,
        Class metafacadeClass)
    {
        // TODO: the source code for this class looks complicated and has to be refactored.

        final String methodName = "MetafacadeFactory.createMetafacade";

        ExceptionUtils.checkNull(methodName, "metaobject", metaobject);

        //if the metaobject ALREADY IS a metafacade
        //return the metaobject since we don't want to try and create a
        //metafacade from a metafacade.
        if (metaobject instanceof MetafacadeBase)
        {
            return (MetafacadeBase)metaobject;
        }

        MetafacadeBase metafacade = null;
        Class metaobjectClass = null;
        try
        {
            metaobjectClass = metaobject.getClass();
            String metaobjectClassName = metaobjectClass.getName();

            MetafacadeMappings mappings = MetafacadeMappings.instance();

            Collection stereotypeNames =
                model.getStereotypeNames(metaobject);
            MetafacadeMapping mapping = null;

            if (metafacadeClass == null)
            {
                if (this.internalGetLogger().isDebugEnabled())
                    this.internalGetLogger().debug(
                        "metaobject stereotype names --> '"
                            + stereotypeNames
                            + "'");
                mapping =
                    mappings.getMetafacadeMapping(
                        metaobjectClassName,
                        stereotypeNames,
                        this.getActiveNamespace(),
                        contextName);
                if (mapping != null)
                {
                    metafacadeClass = mapping.getMetafacadeClass();
                }
                else
                {
                    // get the default since no mapping was found.
                    metafacadeClass =
                        mappings.getDefaultMetafacadeClass(
                            this.activeNamespace);
                    if (this.internalGetLogger().isDebugEnabled())
                        this.internalGetLogger().debug(
                            "Meta object model class '"
                                + metaobjectClass
                                + "' has no corresponding meta facade class, default is being used --> '"
                                + metafacadeClass
                                + "'");
                }
            }

            if (metafacadeClass == null)
            {
                throw new MetafacadeMappingsException(
                    methodName
                        + " metafacadeClass was not retrieved from mappings"
                        + " or specified as an argument in this method for metaobject --> '"
                        + metaobject
                        + "'");
            }

            if (internalGetLogger().isDebugEnabled())
                if (internalGetLogger().isDebugEnabled())
                    internalGetLogger().debug(
                        "lookupFacadeClass: "
                            + metaobjectClassName
                            + " -> "
                            + metafacadeClass);

            metafacade =
                (MetafacadeBase) ConstructorUtils.invokeConstructor(
                    metafacadeClass,
                    metaobject);
            
            // make sure that the facade has a proper logger associated
            // with it.
            metafacade.setLogger(internalGetLogger());

            // set this namespace to the metafacade's namespace
            metafacade.setNamespace(this.getActiveNamespace());
            this.populatePropertyReferences(
                metafacade,
                mappings.getPropertyReferences(this.getActiveNamespace()));

            // now populate any context property references (if
            // we have any)
            if (mapping != null)
            {
                this.populatePropertyReferences(
                    metafacade,
                    mapping.getPropertyReferences());
            } 
            // validate the meta-facade
            metafacade.validate();

        }
        catch (Throwable th)
        {
            String errMsg =
                "Failed to construct a meta facade of type '"
                    + metafacadeClass
                    + "' with metaobject of type --> '"
                    + metaobjectClass
                    + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }

        return metafacade;
    }

    /**
     * Returns a metafacade for a metaobject, depending on its
     * metaclass and (optionally) its stereotype.
     *
     * @param metaobject the model element
     * @return MetafacadeBase the facade object (not yet attached to metaclass object)
     */
    public MetafacadeBase createMetafacade(Object metaobject)
    {
        return this.internalCreateMetafacade(metaobject, null, null);
    }

    /**
     * Create a facade implementation object for a metaobject. The facade implementation
     * object must be found in a way that it implements the interface <code>interfaceName</code>.
     * 
     * @param interfaceName the name of the interface that the implementation object has to implement 
     * @param metaObject the metaobject for which a facade shall be created
     * @return MetafacadeBase the metafacade
     */
    public MetafacadeBase createFacadeImpl(
        String interfaceName,
        Object metaObject)
    {
        final String methodName = "MetafacadeFactory.createFacadeImpl";
        ExceptionUtils.checkEmpty(methodName, "interfaceName", interfaceName);
        ExceptionUtils.checkNull(methodName, "metaObject", metaObject);
        // TODO: this string processing is a temporary(!) hack(!) 
        // until we implement interface class to
        // implementation class name mapping in this factory, based on XML files
        // that define that mapping.

        // Example:
        // org.andromda.metafacades.uml.ClassifierFacade will be mapped to
        // org.andromda.core.metafacades.uml14.ClassifierFacadeLogicImpl

        String shortInterfaceName = ClassUtils.getShortClassName(interfaceName);
        String metafacadeClassName = 
        	"org.andromda.metafacades.uml14." + shortInterfaceName + "LogicImpl";
        // End of temporary(!) string hack.

        try
        {
            Class metafacadeClass = ClassUtils.loadClass(metafacadeClassName);
            
            MetafacadeBase metafacade = 
                this.internalCreateMetafacade(
                        metaObject,
                        null,
                        metafacadeClass);
            
            // Populate any property references that this facade impl might have 
            // (we need to do this so that any sub metafacade that inherit from 
            // one of these metafacades, can inherit its properties
            MetafacadeMappings metafacadeMappings = MetafacadeMappings.instance();
            Collection mappings = 
                metafacadeMappings.findMappingsWithMetafacadeClass(
                	this.getActiveNamespace(), 
                    metafacadeClass);
            Iterator mappingIt = mappings.iterator();
            while (mappingIt.hasNext()) {
            	MetafacadeMapping mapping = (MetafacadeMapping)mappingIt.next();
                this.populatePropertyReferences(metafacade, mapping.getPropertyReferences());
            }
            
            return metafacade;
        }
        catch (Throwable th)
        {
            String errMsg =
                "Failed to construct a meta facade of type '"
                    + metafacadeClassName
                    + "' with metaobject of type --> '"
                    + metaObject.getClass().getName()
                    + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }
    }

    /**
     * Populates the metafacade with the values retrieved from the property references
     * found in the <code>propertyReferences</code> collection.
     *
     * @param propertyReferences the collection of property references which we'll populate.
     */
    protected void populatePropertyReferences(
        MetafacadeBase metafacade,
        Collection propertyReferences)
    {

        // only add the property once per context
        final String methodName =
            "MetafacadeFactory.populatePropertyReferences";
        ExceptionUtils.checkNull(methodName, "metafacade", metafacade);
        ExceptionUtils.checkNull(
            methodName,
            "propertyReferences",
            propertyReferences);

        Iterator referenceIt = propertyReferences.iterator();
        while (referenceIt.hasNext())
        {
            String reference = (String) referenceIt.next();

            // ensure that each property is only set once per context
            // for performance reasons
            if (PropertyUtils.isWriteable(metafacade, reference) &&
                !this.isPropertyRegistered(
                 metafacade.getPropertyNamespace(),
                 reference))
            {
                Property property =
                    Namespaces.instance().findNamespaceProperty(
                        this.getActiveNamespace(),
                        reference);

                // don't attempt to set if the property is null, or it's set to ignore.
                if (property != null && !property.isIgnore())
                {
                    String value = property.getValue();
                    if (this.internalGetLogger().isDebugEnabled())
                        this.internalGetLogger().debug(
                            "setting context property '"
                                + reference
                                + "' with value '"
                                + value
                                + "' for namespace '" 
                                + this.getActiveNamespace() + "'");

                    if (value != null)
                    {
                       metafacade.setProperty(reference, value);
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
    protected Collection createMetafacades(
        Collection metaobjects,
        String contextName)
    {
        Collection metafacades = new ArrayList();
        if (metaobjects != null && !metaobjects.isEmpty())
        {
            Iterator metaobjectIt = metaobjects.iterator();
            while (metaobjectIt.hasNext())
            {
                metafacades.add(
                    internalCreateMetafacade(
                        metaobjectIt.next(),
                        contextName,
                        null));
            }
        }
        return metafacades;
    }

    /**
     * @return the model
     */
    public ModelFacade getModel()
    {
        return model;
    }

    /**
     * @param model the model
     */
    public void setModel(ModelFacade model)
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
     * in the given <code>namespace</code>>.
     * @param namespace the namespace in which the property is stored.
     * @param name the name of the property
     */
    protected void registerProperty(
        String namespace,
        String name,
        Object value)
    {
        final String methodName = "MetafacadeFactory.registerProperty";
        ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
        ExceptionUtils.checkEmpty(methodName, "name", name);
        ExceptionUtils.checkNull(methodName, "value", value);

        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
        if (propertyNamespace != null)
        {
            propertyNamespace.put(name, value);
        }
        else
        {
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
    protected boolean isPropertyRegistered(String namespace, String name)
    {
        boolean registered = false;
        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
        if (propertyNamespace != null)
        {
            registered = propertyNamespace.containsKey(name);
        }
        return registered;
    }

    /**
     * Gets the registered property registered under the <code>namespace</code>
     * with the <code>name</code>
     * @param namespace the namespace of the property to check.
     * @param name the name of the property to check.
     * @return boolean
     */
    protected Object getRegisteredProperty(String namespace, String name)
    {
        final String methodName = "MetafacadeFactory.getRegisteredProperty";
        Object registeredProperty = null;
        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
        if (propertyNamespace == null)
        {
            throw new MetafacadeException(
                methodName
                    + " - no properties registered under namespace '"
                    + namespace
                    + "', can't retrieve property --> '" 
                    + name + "'");
        }
        else
        {
            registeredProperty = propertyNamespace.get(name);
            if (registeredProperty == null)
            {
                throw new MetafacadeException(
                    methodName
                        + " - no property '"
                        + name
                        + "' registered under namespace '"
                        + namespace
                        + "'");
            }
        }
        return registeredProperty;
    }

}
