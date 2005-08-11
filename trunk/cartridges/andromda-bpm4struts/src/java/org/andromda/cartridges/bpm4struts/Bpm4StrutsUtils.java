package org.andromda.cartridges.bpm4struts;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Contains utilities for bpm4struts.
 *
 * @author Wouter Zoons
 */
public final class Bpm4StrutsUtils
{
    /**
     * Creates and returns a List from an <code>enumeration</code>.
     *
     * @param enumeration the enumeration from which to create the List.
     * @return the new List.
     */
    public static List listEnumeration(Enumeration enumeration)
    {
        return Collections.list(enumeration);
    }

    private static final Pattern VALIDATOR_TAGGEDVALUE_PATTERN = Pattern.compile(
        "\\w+(\\(\\w+=[^,)]*(,\\w+=[^,)]*)*\\))?");

    /**
     * Reads the validator arguments from the the given tagged value.
     *
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorArgs(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException(
                "Illegal validator tagged value (this tag is used to specify custom validators " +
                    "and might look like myValidator(myVar=myArg,myVar2=myArg2), perhaps you wanted to use " +
                    "@andromda.struts.view.field.format?): " + validatorTaggedValue);
        }

        final List validatorArgs = new ArrayList();

        // only keep what is between parentheses (if any)
        int left = validatorTaggedValue.indexOf('(');
        if (left > -1)
        {
            final int right = validatorTaggedValue.indexOf(')');
            validatorTaggedValue = validatorTaggedValue.substring(left + 1, right);

            final String[] pairs = validatorTaggedValue.split(",");
            for (int i = 0; i < pairs.length; i++)
            {
                final String pair = pairs[i];
                final int equalsIndex = pair.indexOf('=');
                // it's possible the argument is the empty string
                if (equalsIndex < pair.length() - 1)
                {
                    validatorArgs.add(pair.substring(equalsIndex + 1));
                }
                else
                {
                    validatorArgs.add("");
                }
            }
        }
        return validatorArgs;
    }

    /**
     * Reads the validator variable names from the the given tagged value.
     *
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorVars(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
        }

        final List validatorVars = new ArrayList();

        // only keep what is between parentheses (if any)
        int left = validatorTaggedValue.indexOf('(');
        if (left > -1)
        {
            int right = validatorTaggedValue.indexOf(')');
            validatorTaggedValue = validatorTaggedValue.substring(left + 1, right);

            final String[] pairs = validatorTaggedValue.split(",");
            for (int i = 0; i < pairs.length; i++)
            {
                final String pair = pairs[i];
                final int equalsIndex = pair.indexOf('=');
                validatorVars.add(pair.substring(0, equalsIndex));
            }
        }
        return validatorVars;
    }

    /**
     * Parses the validator name for a tagged value.
     *
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static String parseValidatorName(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
        }

        final int leftParen = validatorTaggedValue.indexOf('(');
        return (leftParen == -1) ? validatorTaggedValue : validatorTaggedValue.substring(0, leftParen);
    }

    /**
     * Sorts a collection of Manageable entities according to their 'manageableName' property.
     * Returns a new collection.
     */
    public static Collection sortManageables(Collection collection)
    {
        final List sorted = new ArrayList(collection);
        Collections.sort(sorted, new ManageableEntityComparator());
        return sorted;
    }

    /**
     * Converts the argument into a web file name, this means: all lowercase
     * characters and words are separated with dashes.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         web file name
     */
    public static String toWebFileName(final String string)
    {
        return StringUtilsHelper.separate(string, "-").toLowerCase();
    }

    /**
     * Given a specific type and format patterns for both date and time, this method returns the pattern
     * to use for the argument type.
     *
     * @param type can be <code>null</code>, should be the fully qualified model name
     * @param dateFormat can be <code>null</code>
     * @param timeFormat can be <code>null</code>
     * @return <code>null</code> if the argument type is not a date nor a time type
     */
    public static String getFormatPattern(String type, String dateFormat, String timeFormat)
    {
        String formatPattern = null;

        if (UMLProfile.DATETIME_TYPE_NAME.equals(type))
        {
            formatPattern = StringUtils.trimToEmpty(dateFormat + " " + timeFormat);
        }
        else if (UMLProfile.DATE_TYPE_NAME.equals(type))
        {
            formatPattern = StringUtils.trimToEmpty(dateFormat);
        }
        else if (UMLProfile.TIME_TYPE_NAME.equals(type))
        {
            formatPattern = StringUtils.trimToEmpty(timeFormat);
        }

        return formatPattern;
    }

    private final static class ManageableEntityComparator
        implements Comparator
    {
        public int compare(
            Object left,
            Object right)
        {
            final ManageableEntity leftEntity = (ManageableEntity)left;
            final ManageableEntity rightEntity = (ManageableEntity)right;
            return StringUtils.trimToEmpty(leftEntity.getName()).compareTo(
                StringUtils.trimToEmpty(rightEntity.getName()));
        }
    }
}
