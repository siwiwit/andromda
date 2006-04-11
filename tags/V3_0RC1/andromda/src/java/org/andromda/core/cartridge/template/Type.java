package org.andromda.core.cartridge.template;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

/**
 * Represents the &lt;type/&gt; element nested within the &lt;modelElement/&gt;
 * element.
 * 
 * @see ModelElement
 * @author Chad Brandon
 */
public class Type
{
    private String name;
    private final Collection properties = new ArrayList();

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return StringUtils.trimToEmpty(name);
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the properties.
     */
    public Collection getProperties()
    {
        return properties;
    }

    /**
     * Adds a property having the given <code>name</code> and
     * <code>value</code>. The <code>value</code> is what the property must
     * be in order to be collected.
     * 
     * @param name the name of the property
     * @param value the value the property must be
     */
    public void addProperty(String name, String value)
    {
        this.properties.add(new Property(name, value));
    }

    /**
     * Stores and provides access to the type's &lt;property/&gt; elements.
     */
    class Property
    {
        private String name;
        private String value;

        Property(
            String name,
            String value)
        {
            this.name = StringUtils.trimToEmpty(name);
            this.value = value;
        }

        /**
         * Gets the value of the <code>name</code> attribute on the
         * <code>property</code> element.
         * 
         * @return the name
         */
        String getName()
        {
            return StringUtils.trimToEmpty(this.name);
        }

        /**
         * Gets the value of the <code>value</code> attribute defined on the
         * <code>property</code> element.
         * 
         * @return the value
         */
        String getValue()
        {
            return StringUtils.trimToEmpty(this.value);
        }
    }
}