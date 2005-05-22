package org.andromda.core.configuration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A configurable namespace object. These are passed to Plugin instances (Cartridges, etc.).
 *
 * @author Chad Brandon
 */
public class Namespace
{    
    /**
     * The namespace name.
     */
    private String name;

    /**
     * Returns name of this Namespace. Will correspond to a Plugin name (or it can be be 'default' if we want it's
     * settings to be used everywhere).
     *
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of this Namespace.
     *
     * @param name The name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }
    
    /**
     * Stores the collected properties
     */
    private final Map properties = new LinkedHashMap();

    /**
     * Adds a property to this Namespace object. A property must correspond to a java bean property name on a Plugin in
     * order for it to be set during processing. Otherwise the property will just be ignored.
     *
     * @param property the property to add to this namespace.
     */
    public void addProperty(final Property property)
    {
        if (property != null)
        {
            this.properties.put(property.getName(), property);
        }
    }

    /**
     * Retrieves the property with the specified name.
     *
     * @param name the name of the property.
     * 
     * @return the property
     */
    public Property getProperty(final String name)
    {
        return (Property)this.properties.get(name);
    }
    
    /**
     * Gets all namespaces belonging to this namespaces instance.
     * 
     * @return all namespaces.
     */
    public Collection getProperties()
    {
        return this.properties.values();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}