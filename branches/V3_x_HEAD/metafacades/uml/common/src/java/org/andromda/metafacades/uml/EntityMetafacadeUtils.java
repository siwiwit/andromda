package org.andromda.metafacades.uml;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Utilities for dealing with entity metafacades
 *
 * @author Chad Brandon
 */
public class EntityMetafacadeUtils
{
    /**
     * <p/> Converts a string following the Java naming conventions to a
     * database attribute name. For example convert customerName to
     * CUSTOMER_NAME.
     * </p>
     *
     * @param modelElementName the string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
    public static String toSqlName(
        String modelElementName,
        Object separator)
    {
        ExceptionUtils.checkEmpty(
            "modelElementName",
            modelElementName);

        StringBuffer sqlName = new StringBuffer();
        StringCharacterIterator iterator = new StringCharacterIterator(StringUtils.uncapitalize(modelElementName));

        for (char character = iterator.first(); character != CharacterIterator.DONE; character = iterator.next())
        {
            if (Character.isUpperCase(character))
            {
                sqlName.append(separator);
            }
            character = Character.toUpperCase(character);
            sqlName.append(character);
        }
        return StringEscapeUtils.escapeSql(sqlName.toString());
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param prefix the optional prefix to add to the sql name (i.e. table name
     *        prefix, etc.).
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            prefix,
            element,
            name,
            nameMaxLength,
            null,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param suffix the optional suffix to add to the sql name (i.e. foreign
     *        key suffix, etc.)
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        String suffix,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            suffix,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            null,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param prefix the optional prefix to add to the sql name (i.e. table name
     *        prefix, etc.).
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param suffix the optional suffix to add to the sql name (i.e. foreign
     *        key suffix, etc.)
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        final ModelElementFacade element,
        String name,
        final Short nameMaxLength,
        String suffix,
        final Object separator)
    {
        if (element != null)
        {
            Object value = element.findTaggedValue(name);
            StringBuffer buffer = new StringBuffer(StringUtils.trimToEmpty((String)value));
            if (StringUtils.isEmpty(buffer.toString()))
            {
                // if we can't find the tagValue then use the
                // element name for the name
                buffer = new StringBuffer(toSqlName(
                            element.getName(),
                            separator));
                suffix = StringUtils.trimToEmpty(suffix);
                prefix = StringUtils.trimToEmpty(prefix);
                if (nameMaxLength != null)
                {
                    final short maxLength = (short)(nameMaxLength.shortValue() - suffix.length() - prefix.length());
                    buffer =
                        new StringBuffer(
                            EntityMetafacadeUtils.ensureMaximumNameLength(
                                buffer.toString(),
                                new Short(maxLength)));
                }
                if (StringUtils.isNotBlank(prefix))
                {
                    buffer.insert(
                        0,
                        prefix);
                }
                if (StringUtils.isNotBlank(suffix))
                {
                    buffer.append(suffix);
                }
            }
            name = buffer.toString();
        }
        return name;
    }

    /**
     * <p/> Trims the passed in value to the maximum name length.
     * </p>
     * If no maximum length has been set then this method does nothing.
     *
     * @param name the name length to check and trim if necessary
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @return String the string to be used as SQL type
     */
    public static String ensureMaximumNameLength(
        String name,
        Short nameMaxLength)
    {
        if (StringUtils.isNotEmpty(name) && nameMaxLength != null)
        {
            short max = nameMaxLength.shortValue();
            if (name.length() > max)
            {
                name = name.substring(
                        0,
                        max);
            }
        }
        return name;
    }

    /**
     * Gets all identifiers for an entity. If 'follow' is true, and if
     * no identifiers can be found on the entity, a search up the
     * inheritance chain will be performed, and the identifiers from
     * the first super class having them will be used.   If no
     * identifiers exist, a default identifier will be created if the
     * allowDefaultIdentifiers property is set to true.
     *
     * @param entity the entity for which to retrieve the identifiers
     * @param follow a flag indicating whether or not the inheritance hiearchy
     *        should be followed
     * @return the collection of identifiers.
     */
    public static Collection getIdentifiers(
        final Entity entity,
        final boolean follow)
    {
        final Collection identifiers = new ArrayList(entity.getAttributes());
        MetafacadeUtils.filterByStereotype(
            identifiers,
            UMLProfile.STEREOTYPE_IDENTIFIER);

        return identifiers.isEmpty() && follow && entity.getGeneralization() instanceof Entity
            ? getIdentifiers((Entity)entity.getGeneralization(), follow)
            : identifiers;
    }

    /**
     * Constructs a sql type name from the given <code>mappedName</code> and
     * <code>columnLength</code>.
     *
     * @param typeName the actual type name (usually retrieved from a mappings
     *        file, ie NUMBER(19).
     * @param columnLength the length of the column.
     * @return the new name co
     */
    public static String constructSqlTypeName(
        final String typeName,
        final String columnLength)
    {
        String value = typeName;
        if (StringUtils.isNotEmpty(typeName))
        {
            final char beginChar = '(';
            final char endChar = ')';
            final int beginIndex = value.indexOf(beginChar);
            final int endIndex = value.indexOf(endChar);
            if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex)
            {
                String replacement = value.substring(
                        beginIndex,
                        endIndex) + endChar;
                value = StringUtils.replace(
                        value,
                        replacement,
                        beginChar + columnLength + endChar);
            }
            else
            {
                value = value + beginChar + columnLength + endChar;
            }
        }
        return value;
    }
}