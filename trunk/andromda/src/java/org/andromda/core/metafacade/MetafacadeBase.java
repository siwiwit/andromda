package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Base class for all metafacades.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MetafacadeBase
{
    private Object metaObject;
    protected Logger logger;

    public MetafacadeBase(
        Object metaObject,
        String context)
    {
        this.metaObject = metaObject;
        this.context = context;
    }

    /**
     * Retrieves the <code>owner</code> of this metafacade (for example: an
     * operation owns its parameters, a class owns its attributes).
     * <p>
     * By default <code>null</code> is returned, however this method is
     * overridden by subclasses which have a <code>parent</code> or
     * <code>owner</code>. This is used to give the model validation messages
     * more context as to where the validation error occurred.
     * </p>
     * 
     * @return the owner of this metafacade.
     */
    public Object getValidationOwner()
    {
        return null;
    }

    /**
     * Retrieves the <code>name</code> of this metafacade used within the
     * validation messages.
     * <p>
     * By default <code>null</code> is returned, however this method is
     * overridden by subclasses model elements that do have a name.
     * </p>
     * 
     * @return the owner of this metafacade.
     */
    public String getValidationName()
    {
        return null;
    }

    private boolean initialized = false;

    /**
     * Sets the flag indicating this metafacade has been initlized.
     */
    void setInitialized()
    {
        this.initialized = true;
    }

    /**
     * Indicates if this metafacade has been initialized.
     * 
     * @return true/false
     */
    boolean isInitialized()
    {
        return this.initialized;
    }

    /**
     * Validates that this facade's meta object is in a valid state.
     * <p>
     * Validate is called during metafacade creation by the factory. In the
     * lifecycle of a metafacade it is validated only once, this is enforced by
     * the caching within the metafacade factory.
     * 
     * @see MetafacadeFactory#createMetafacade(Object, String, Class)
     * @param validationMessages any messages generated during validation.
     */
    public final void validate(Collection validationMessages)
    {
        this.validateInvariants(validationMessages);
    }

    /**
     * <p>
     * The logic of modeled OCL invariants from derived metafacades will be
     * generated into this method and validation messages created and collected
     * into the <code>messages</code> collection. This method is called by
     * validate #validate().
     * </p>
     * <p>
     * By default this method is empty.
     * </p>
     */
    public void validateInvariants(Collection messages)
    {
        // By default this does nothing
    }

    /**
     * A lifecycle method, providing the ability for sub classes to take any
     * action after the factory has completely initialized a metafacade, but
     * before it has been validated for completeness.
     */
    public void initialize()
    {
        // By default this does nothing
    }

    /**
     * Returns one facade for a particular metaObject. Contacts the
     * MetafacadeFactory to manufacture the proper metafacade. In certain cases
     * <code>metaObject</code> can also be a metafacade instance; in that case
     * the actual meta model element is retrieved from the metafacade and a
     * metafacade is constructed from that.
     * 
     * @see MetafacadeFactory
     * @param metaObject the underlying meta model element. A metafacade is
     *        created for each.
     * @return MetafacadeBase the facade
     */
    protected MetafacadeBase shieldedElement(Object metaObject)
    {
        MetafacadeBase metafacade = null;
        if (metaObject != null)
        {
            metafacade = MetafacadeFactory.getInstance().createMetafacade(
                metaObject,
                this.getContext());
            // pass the context on to the metafacade created within this
            // metafacade
            metafacade.setMetafacadeContext(this.getContext());
        }
        return metafacade;
    }

    /**
     * Returns a collection of facades for a collection of metaobjects. Contacts
     * the MetafacadeFactory to manufacture the proper facades.
     * 
     * @see MetafacadeFactory
     * @param metaobjects the objects to decorate
     * @return Collection of MetafacadeBase-derived objects
     */
    protected Collection shieldedElements(Collection metaobjects)
    {
        Collection metafacades = new ArrayList();
        if (metaobjects != null)
        {
            metafacades.addAll(metaobjects);
            CollectionUtils.transform(metafacades, new Transformer()
            {
                public Object transform(Object object)
                {
                    return MetafacadeBase.this.shieldedElement(object);
                }
            });
        }
        return metafacades;
    }

    /**
     * Stores the context for this metafacade
     */
    private String context = null;

    /**
     * Gets the context for this metafacade.
     * 
     * @return the context name.
     */
    String getContext()
    {
        return this.context;
    }

    /**
     * Sets the context for this metafacade.  This is used to 
     * pass the context along from a metafacade specializing this
     * metafacade (since we use delegate inheritance between
     * shared and non-shared metafacades), as well as to pass 
     * the context to a metafacade being created within another.  
     * 
     * @param context the metafacade interface name representing the context.
     * @see MetafacadeMapping#isContextRoot()
     * @see MetafacadeFactory#createMetafacade(Object, String, Class)
     */
    public void setMetafacadeContext(String context)
    {
        this.context = StringUtils.trimToEmpty(context);
    }

    /**
     * Stores the property context for this metafacade.
     */
    private String propertyNamespace = null;

    /**
     * Gets the current property context for this metafacade. This is the
     * context in which properties for this metafacade are stored.
     * 
     * @return the property namespace name
     */
    String getPropertyNamespace()
    {
        if (StringUtils.isEmpty(this.propertyNamespace))
        {
            this.propertyNamespace = this.constructPropertyNamespace(this
                .getContext());
        }
        return this.propertyNamespace;
    }

    /**
     * Constructs a property namespace name with the <code>context</code>
     * argument and the current <code>namespace</code> of this metafacade.
     * 
     * @param context the context name with which to construct the name.
     * @return the new property namespace name
     */
    private String constructPropertyNamespace(String context)
    {
        StringBuffer propertyNamespace = new StringBuffer();
        propertyNamespace.append(this.getNamespace());
        propertyNamespace.append(":");
        propertyNamespace.append(context);
        return propertyNamespace.toString();
    }

    /**
     * Stores the namespace for this metafacade
     */
    private String namespace = null;

    /**
     * Gets the current namespace for this metafacade
     * 
     * @return String
     */
    String getNamespace()
    {
        return this.namespace;
    }

    /**
     * Sets the namespace for this metafacade.
     * 
     * @param namespace
     */
    void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * Returns true or false depending on whether the <code>property</code> is
     * registered or not.
     * 
     * @param property the name of the property to check.
     * @return true/false on whether or not its regisgterd.
     */
    protected boolean isConfiguredProperty(String property)
    {
        return MetafacadeFactory.getInstance().isPropertyRegistered(
            this.getPropertyNamespace(),
            property);
    }

    /**
     * Gets a configured property from the container. Note that the configured
     * property must be registered first.
     * 
     * @param property the property name
     * @return Object the configured property instance (mappings, etc)
     */
    protected Object getConfiguredProperty(String property)
    {
        return MetafacadeFactory.getInstance().getRegisteredProperty(
            this.getPropertyNamespace(),
            property);
    }

    /**
     * Attempts to set the property with <code>name</code> having the
     * specified <code>value</code> on this metafacade.
     */
    protected void setProperty(String name, Object value)
    {
        MetafacadeFactory.getInstance().registerProperty(
            this.getPropertyNamespace(),
            name,
            value);
    }

    /**
     * Gets the current meta model object for this metafacade. This is used from
     * {@link MetafacadeFactory}when attempting to construct a metafacade from
     * a metafacade. This allows us to get the meta object for this metafacade
     * so that the meta object can be used instead.
     * 
     * @return the underlying model's meta object instance.
     */
    Object getMetaObject()
    {
        return this.metaObject;
    }

    /**
     * Package-local setter, called by facade factory. Sets the logger to use
     * inside the facade's code.
     * 
     * @param logger the logger to set
     */
    void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * The flag indicating whether or not this metafacade is a context root.
     */
    private boolean contextRoot = false;

    /**
     * Sets whether or not this metafacade represents a contextRoot. If it does
     * represent a context root, then {@link #getMetafacadeContext()}returns
     * the metafacade interface for this metafacade, otherwise the regular
     * <code>context</code> is returned.
     * 
     * @param contextRoot
     */
    void setContextRoot(boolean contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * Gets the <code>context</code> for this metafacade. This is either the
     * <code>contextRoot</code> (if one exists), or the regular
     * <code>context</code>.
     * 
     * @return the metafacade's context.
     * @see #setContextRoot(String)
     */
    public String getMetafacadeContext()
    {
        String metafacadeContext = this.context;
        if (this.contextRoot)
        {
            metafacadeContext = MetafacadeImpls.instance().getMetafacadeClass(
                this.getClass().getName()).getName();
        }
        return metafacadeContext;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object)
    {
        if (object instanceof MetafacadeBase)
        {
            MetafacadeBase that = (MetafacadeBase)object;
            return this.metaObject.equals(that.metaObject);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return metaObject.hashCode();
    }
}