package org.andromda.andromdapp;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Converter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Some utilities for dealing with the AndroMDApp generator.
 *
 * @author Chad Brandon
 */
public class AndroMDAppUtils
{
    /**
     * The delimiter for separating patterns.
     */
    private static final String COMMA = ",";

    /**
     * Converts a comma delimited string to an array of Strings.
     *
     * @param string to convert.
     * @return the resulting array or null if the string was null.
     */
    public static String[] stringToArray(final String string)
    {
        return string != null ? string.split(COMMA) : null;
    }

    /**
     * Attempts to convert the given <code>value</code> to the given
     * <code>type</code> (if the type is specified), otherwise does nothing and
     * returns the value unchanged.
     *
     * @param value the value to convert.
     * @param type the type to convert it to.
     * @return the converted, or unconverted depending on whether it needed
     *         to be converted.
     */
    public static Object convert(
        final Object value,
        final String type)
    {
        Object object = value;
        if (StringUtils.isNotBlank(type))
        {
            try
            {
                final Class typeClass = ClassUtils.getClassLoader().loadClass(type);

                // - handle booleans differently, since we want to be able to
                // convert 'yes/no', 'on/off', etc
                // to boolean values
                if (typeClass == Boolean.class)
                {
                    object = BooleanUtils.toBooleanObject(ObjectUtils.toString(value));
                }
                else
                {
                    object = Converter.convert(
                            value,
                            typeClass);
                }
            }
            catch (final ClassNotFoundException exception)
            {
                // - ignore
            }
        }
        return object;
    }
}