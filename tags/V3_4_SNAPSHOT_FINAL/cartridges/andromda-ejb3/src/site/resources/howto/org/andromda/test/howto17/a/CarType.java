// license-header java merge-point
/* Autogenerated by AndroMDA (Enumeration.vsl) - do not edit */
package org.andromda.howto2.rental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Autogenerated enumeration CarType.
 *
 *
 */
public enum CarType implements Serializable
{
    /**
     *
     */
    SEDAN,

    /**
     *
     */
    LIFTBACK,

    /**
     *
     */
    WAGON;

    /**
     * Return the CarType from a string value
     * @return CarType enum object
     */
    public static CarType fromString(String value)
    {
        return valueOf(value);
    }

    /**
     * Return a Collection of all literal values for this enumeration
     * @return Collection literal values
     */
    public static Collection literals()
    {
        final Collection<String> literals = new ArrayList<String>(values().length);
        for (int i = 0; i < values().length; i++)
        {
            literals.add(values()[i].name());
        }
        return literals;
    }
}
