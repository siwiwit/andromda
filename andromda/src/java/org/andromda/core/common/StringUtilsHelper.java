package org.andromda.core.common;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * A utility object for doing string manipulation operations that are commonly
 * needed by the code generation templates.
 *
 * @author Matthias Bohlen
 * @author Chris Shaw
 * @author Chad Brandon
 */
public class StringUtilsHelper {

    /**
     * <p>Capitalizes a string.  That is, it returns "Hamburger" when
     * eating a "hamburger".</p>
     *
     * @deprecated - use upperCaseFirstLetter
     *
     * @param s the input string
     * @return String the output string
     */
    public static String capitalize(String s) {
        return StringUtils.capitalize(s);
    }

    /**
    * <p>Capitalizes a string. That is, it returns "HamburgerStall"
    * when receiving a "hamburgerStall".</p>
    *
    * @param s the input string
    * @return String the output string.
    */
    public static String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
    * <p>Removes the capitalization of a string. That is, it returns
    * "hamburgerStall" when receiving a "HamburgerStall".</p>
    *
    * @param s the input string
    * @return String the output string.
    */
    public static String lowerCaseFirstLetter(String s) {
    	return StringUtils.uncapitalize(s);
    }


    /**
     * <p>Converts a string following the Java naming conventions to a
     * database attribute name.  For example convert customerName to
     * CUSTOMER_NAME.</p>
     *
     * @param string the string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
    public static String toDatabaseAttributeName(String string, String separator) {
    	ExceptionUtils.checkEmpty("toDatabaseAttributeName", "string", string);

        StringBuffer databaseAttributeName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(
                lowerCaseFirstLetter(string));

        for (char character = iter.first(); character != CharacterIterator.DONE;
                character = iter.next()) {

            if (Character.isUpperCase(character)) {
                databaseAttributeName.append(separator);
            }

            character = Character.toUpperCase(character);
            databaseAttributeName.append(character);
        }

        return databaseAttributeName.toString();
    }

    /**
     * <p>Returns a consistent name for a relation, independent from
     * the end of the relation one is looking at.</p>
     *
     * <p>In order to guarantee consistency with relation names, they
     * must appear the same whichever angle (ie entity) that you come
     * from.  For example, if you are at Customer end of a
     * relationship to an Address then your relation may appear with
     * the name Customer-Address.  But if you are in the Address
     * entity looking at the Customer then you will get an error
     * because the relation will be called Address-Customer.  A simple
     * way to guarantee that both ends of the relationship have the
     * same name is merely to use alphabetical ordering.</p>
     *
     * @param roleName       name of role in relation
     * @param targetRoleName name of target role in relation
     * @param separator      character used to separate words
     * @return uniform mapping name (in alphabetical order)
     */
    public static String toRelationName(String roleName, String targetRoleName,
            String separator) {
        if (roleName.compareTo(targetRoleName) <= 0) {
            return (roleName + separator + targetRoleName);
        }

        return (targetRoleName + separator + roleName);
    }

    /**
     * <p>Replaces a given suffix of the source string with a new one.
     * If the suffix isn't present, the string is returned
     * unmodified.</p>
     *
     * @param src the <code>String</code> for which the suffix should be replaced
     * @param suffixOld a <code>String</code> with the suffix that should be replaced
     * @param suffixNew a <code>String</code> with the new suffix
     * @return a <code>String</code> with the given suffix replaced or
     *         unmodified if the suffix isn't present
     */
    public static String replaceSuffix(String src, String suffixOld, String suffixNew) {
        if (src.endsWith(suffixOld)) {
            return src.substring(0, src.length()-suffixOld.length())+suffixNew;
        }

        return src;
    }

    /**
     * <p>Checks if a given type name is a Java primitive type.</p>
     *
     * @param name a <code>String</code> with the name of the type
     * @return <code>true</code> if <code>name</code> is a Java
     *         primitive type; <code>false</code> if not
     */
    public static boolean isPrimitiveType(String name) {
        return (   "void".equals(name)
                || "char".equals(name)
                || "byte".equals(name)
                || "short".equals(name)
                || "int".equals(name)
                || "long".equals(name)
                || "float".equals(name)
                || "double".equals(name)
                || "boolean".equals(name) );
    }

    /**
     * <p>Returns the type class name for a Java primitive.</p>
     *
     * @param name a <code>String</code> with the name of the type
     * @return a <code>String</code> with the name of the
     *         corresponding java.lang wrapper class if
     *         <code>name</code> is a Java primitive type;
     *         <code>false</code> if not
     */
    public static String getPrimitiveClassName(String name) {
        if (!isPrimitiveType(name)) {
            return null;
        }

        if ("void".equals(name)) {
            return null;
        }
        if ("char".equals(name)) {
            return "java.lang.Character";
        }
        if ("int".equals(name)) {
            return "java.lang.Integer";
        }

        return "java.lang."+upperCaseFirstLetter(name);
    }

    /**
     * <p>Returns the argument string as a Java class name according the Sun coding conventions.</p>
     * <p>Non word characters be removed and the letter following such a character will be uppercased.</p>
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a Java class
     */
    public static String toJavaClassName(String string)
    {
        if ( (string == null) ||  (string.trim().length() == 0) )
            return string;

        final String[] parts = split(string);
        final StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            conversionBuffer.append(parts[i].substring(0,1).toUpperCase());
            conversionBuffer.append(parts[i].substring(1).toUpperCase());
        }
        return conversionBuffer.toString();
    }

    /**
     * <p>Returns the argument string as a Java method name according the Sun coding conventions.</p>
     * <p>Non word characters be removed and the letter following such a character will be uppercased.</p>
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a Java method
     */
    public static String toJavaMethodName(String string)
    {
        return lowerCaseFirstLetter(toJavaClassName(string));
    }

    public static String toWebFileName(String string)
    {
        return separate(string, "-").toLowerCase();
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and replaces each of those
     * sequences by a hyphen '-'.
     */
    public static String separate(String string, String separator)
    {
        final String[] parts = split(string);
        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            buffer.append(parts[i]).append(separator);
        }
        return buffer.append(parts[parts.length - 1]).toString();
    }

    /**
     * Splits at each sequence of non-word characters.
     */
    private static String[] split(String string)
    {
        if ( (string == null) ||  (string.trim().length() == 0) )
            return new String[0];

        Matcher matcher = null;

        // surround capital sequences with spaces
        Pattern capitalSequencePattern = Pattern.compile("[(A-Z)+]");
        matcher = capitalSequencePattern.matcher(string);
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<matcher.groupCount(); i++)
        {
            buffer.append(' ' + matcher.group(i) + ' ');
        }

        // split on all non-word characters
        return buffer.toString().split("[^\\W+]");
    }
}
