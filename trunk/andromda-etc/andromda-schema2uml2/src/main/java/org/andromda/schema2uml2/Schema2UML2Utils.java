package org.andromda.schema2uml2;

import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities for the Schema2UML2 tool.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
class Schema2UML2Utils
{
    /**
     * Constructs the entire type name from the
     * given name and length.
     *
     * @param name the name of the type
     * @param length the length of the type.
     * @param decimalPlaces the number of decimal places specified for the type
     * @return the type name with the length.
     */
    static String constructTypeName(
        final String name,
        final String length,
        final String decimalPlaces)
    {
        final StringBuilder buffer = new StringBuilder();
        if (name != null)
        {
            buffer.append(name);

            if (!name.matches(".+\\(.+\\)"))
            {
                if (StringUtils.isNotBlank(length))
                {
                    buffer.append('(').append(length);
                    if (StringUtils.isNotBlank(decimalPlaces))
                    {
                        buffer.append(',').append(decimalPlaces);
                    }
                    buffer.append(')');
                }
            }

        }
        return buffer.toString();
    }
}