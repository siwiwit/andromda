package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    public static Collection decoratedElements(Collection metaobjects)
    {
        if (metaobjects == null)
        {
            return null;   // a decorated null is still a null! :-)
        }
        ArrayList result = new ArrayList(metaobjects.size());
        DecoratorFactory df = DecoratorFactory.getInstance();

        for (Iterator iter = metaobjects.iterator(); iter.hasNext();)
        {
            ModelElement element = (ModelElement) iter.next();
            result.add(df.createDecoratorObject(element));
        }
        return result;
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
    public static DecoratorBase decoratedElement(ModelElement metaObject)
    {
        if (metaObject == null)
        {
            return null;   // a decorated null is still a null! :-)
        }
        return DecoratorFactory.getInstance().createDecoratorObject(
            metaObject);
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
