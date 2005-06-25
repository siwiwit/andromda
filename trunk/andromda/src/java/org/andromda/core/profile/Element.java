package org.andromda.core.profile;


/**
 * Stores information about a profile element.
 *
 * @author Chad Brandon
 */
public class Element
{
    /**
     * The name of the element
     */
    private String name;

    /**
     * Gets the name of the element.
     *
     * @return the element name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the element.
     *
     * @param name the element's name.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * The value of the element.
     */
    private String value;

    /**
     * Gets the value of this element.
     *
     * @return the value of this element.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value of this element.
     *
     * @param value that value of this element.
     */
    public void setValue(final String value)
    {
        this.value = value;
    }
}