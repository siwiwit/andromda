package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Base class for all metaclass decorators.
 */
public class DecoratorBase
{
    private   Object metaObject;
    protected Logger logger;

    public DecoratorBase(Object metaObject)
    {
        this.metaObject = metaObject;
    }

    /**
     * Validates that this decorator's meta object is in a valid state.
     * <p>
     * Classes that extend this base class may choose the override this method
     * to check whether it is in a valid state.
     * 
     * @throws DecoratorValidationException
     */
    public void validate() throws DecoratorValidationException
    {
    }

    /**
     * Returns a collection of decorators for a collection
     * of metaobjects. Contacts the DecoratorFactory to manufacture
     * the proper decorators.
     * @see DecoratorFactory
     *
     * @param metaobjects the objects to decorate
     * @return Collection of DecoratorBase-derived objects
     */
    public Collection decoratedElements(Collection metaobjects)
    {
        if (metaobjects == null)
        {
            return null;   // a decorated null is still a null! :-)
        }
		Collection metafacades = DecoratorFactory.getInstance().createDecoratorObjects(
				metaobjects, 
				this.getContext());
		class MetafacadeContextTransformer implements Transformer {
			public Object transform(Object object) {
				DecoratorBase metafacade = (DecoratorBase)object;
				// keep passing the context along from the 
				// very first one (i.e. the first metafacade)
				metafacade.setContext(getContext());
				
				if (logger.isDebugEnabled())
					logger.debug("set context as --> '" 
						+ metafacade.getContext() 
						+ "'");
				
				return metafacade;
			}
		}
		CollectionUtils.transform(
			metafacades, 
			new MetafacadeContextTransformer());
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
	protected String getContext() {
		if (StringUtils.isEmpty(this.context)) {
			this.context = this.getClass().getName();
		}
		return this.context;
	}
	
	/**
	 * Sets the <code>context<code> for this metafacade
	 * 
	 * @param context the context class to set
	 */
	private void setContext(String context) {
		this.context = StringUtils.trimToEmpty(context);
	}
    
    /**
     * Stores the property context for this Metafacade
     */
    private String propertyNamespace = null;
    
    /**
     * Gets the current property context for this metafacade.
     * This is the context in which properties for this metafacade
     * are stored.
     * 
     * @return String
     */
    protected String getPropertyNamespace() {
    	if (StringUtils.isEmpty(this.propertyNamespace)) {
    		this.propertyNamespace = this.getContext() + ":property";
    	}
    	return this.propertyNamespace;
    }
    
	/**
	 * Stores the namespace for this metafacade
	 */
	private String namespace = null;
	
	/**
	 * Gets the current namespace for this metafacade
	 * 
	 * @param namespace
	 */
	protected String getNamespace() {
		return this.namespace;
	}
	
	/**
	 * Sets the namespace for this metafacade.
	 * 
	 * @param namespace
	 */
	protected void setNamespace(String namespace) {
		this.namespace = namespace;
	}
    
    /**
     * Gets a configured property from the container.  Note
     * that the configured property must be registered first.
     * 
     * @param property the property name
     * @return Object the configured property instance (mappings, etc)
     */
    protected Object getConfiguredProperty(String property) {
    	return DecoratorFactory.getInstance().getRegisteredProperty(
    			this.getPropertyNamespace(),
				property);		
    }
    
    /**
     * Registers a configured property with the container.
     * 
     * @param property the name of the property.
     * @param value the value of the configured instance.
     */
    protected void registerConfiguredProperty(String property, Object value) {
    	DecoratorFactory.getInstance().registerProperty(
			this.getPropertyNamespace(), 
			property, 
			value);
    }

    /**
     * Returns one decorator for a particular metaobject. Contacts
     * the DecoratorFactory to manufacture the proper decorator.
     *
     * @see DecoratorFactory
     * @param metaObject the object to decorate
     * @return DecoratorBase the decorator
     */
    public DecoratorBase decoratedElement(ModelElement metaObject)
    {
		DecoratorBase metafacade = null;
		if (metaObject != null) {
			metafacade = 
				DecoratorFactory.getInstance().createDecoratorObject(
					metaObject, 
					this.getContext());
			// keep passing the context along from the 
			// very first one (i.e. the first metafacade)
			if (StringUtils.isNotEmpty(this.context)) {
				metafacade.setContext(this.getContext());
				if (logger.isDebugEnabled())
					logger.debug("set context as --> '" 
						+ metafacade.getContext() 
						+ "'");
			}
		}
		return metafacade;
    }

    /**
     * Package-local setter, called by decorator factory.
     * Sets the logger to use inside the decorator's code.
     * @param l the logger to set
     */
    void setLogger(Logger l)
    {
        logger = l;
    }
}
