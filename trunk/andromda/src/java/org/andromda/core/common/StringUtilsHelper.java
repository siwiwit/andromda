package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility object for doing string manipulation operations that are commonly
 * needed by the code generation templates.
 * 
 * @author Matthias Bohlen
 * @author Chris Shaw
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class StringUtilsHelper
    extends StringUtils
{

    /**
     * <p>
     * Capitalizes a string. That is, it returns "HamburgerStall" when receiving
     * a "hamburgerStall".
     * </p>
     * 
     * @param s the input string
     * @return String the output string.
     */
    public static String upperCaseFirstLetter(String s)
    {
        return capitalize(s);
    }

    /**
     * <p>
     * Removes the capitalization of a string. That is, it returns
     * "hamburgerStall" when receiving a "HamburgerStall".
     * </p>
     * 
     * @param s the input string
     * @return String the output string.
     */
    public static String lowerCaseFirstLetter(String s)
    {
        return uncapitalize(s);
    }

    /**
     * <p>
     * Replaces a given suffix of the source string with a new one. If the
     * suffix isn't present, the string is returned unmodified.
     * </p>
     * 
     * @param src the <code>String</code> for which the suffix should be
     *        replaced
     * @param suffixOld a <code>String</code> with the suffix that should be
     *        replaced
     * @param suffixNew a <code>String</code> with the new suffix
     * @return a <code>String</code> with the given suffix replaced or
     *         unmodified if the suffix isn't present
     */
    public static String replaceSuffix(
        String src,
        String suffixOld,
        String suffixNew)
    {
        if (src.endsWith(suffixOld))
        {
            return src.substring(0, src.length() - suffixOld.length())
                + suffixNew;
        }
        return src;
    }

    /**
     * <p>
     * Returns the argument string as a Java class name according the Sun coding
     * conventions.
     * </p>
     * <p>
     * Non word characters be removed and the letter following such a character
     * will be uppercased.
     * </p>
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         Java class
     */
    public static String toJavaClassName(String string)
    {
        if (StringUtils.isEmpty(string))
            return string;

        final String[] parts = splitAtCapitalization(string);
        final StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].length() < 2)
            {
                conversionBuffer.append(parts[i].toUpperCase());
            }
            else
            {
                conversionBuffer.append(parts[i].substring(0, 1).toUpperCase());
                conversionBuffer.append(parts[i].substring(1));
            }
        }
        return conversionBuffer.toString();
    }

    /**
     * Removes the last occurance of the oldValue found within the string.
     * 
     * @param string the String to remove the <code>value</code> from.
     * @param value the value to remove.
     * @return String the resulting string.
     */
    public static String removeLastOccurrence(String string, String value)
    {
        if (string != null && value != null)
        {
            StringBuffer buf = new StringBuffer();
            int index = string.lastIndexOf(value);
            if (index != -1)
            {
                buf.append(string.substring(0, index));
                buf.append(string.substring(index + value.length(), string
                    .length()));
                string = buf.toString();
            }
        }
        return string;
    }

    /**
     * <p>
     * Returns the argument string as a Java method name according the Sun
     * coding conventions.
     * </p>
     * <p>
     * Non word characters be removed and the letter following such a character
     * will be uppercased.
     * </p>
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         Java method
     */
    public static String toJavaMethodName(String string)
    {
        return lowerCaseFirstLetter(toJavaClassName(string));
    }

    /**
     * Converts the argument into a web file name, this means: all lowercase
     * characters and words are separated with dashes.
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         web file name
     */
    public static String toWebFileName(String string)
    {
        return separate(string, "-").toLowerCase();
    }

    /**
     * Converts the argument into a message key in a properties resource bundle,
     * all lowercase characters, words are separated by dots.
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         message key
     */
    public static String toResourceMessageKey(String string)
    {
        return separate(string, ".").toLowerCase();
    }

    /**
     * Converts into a string suitable as a human readable phrase, First
     * character is uppercase (the rest is left unchanged), words are separated
     * by a space.
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         human readable phrase
     */
    public static String toPhrase(String string)
    {
        return upperCaseFirstLetter(separate(string, " "));
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and
     * replaces each of those sequences by a hyphen '-'.
     */
    public static String separate(String string, String separator)
    {
        if ((string == null) || (string.trim().length() == 0))
            return string;

        final String[] parts = splitAtCapitalization(string);
        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            if (parts[i].trim().length() > 0)
                buffer.append(parts[i]).append(separator);
        }
        return buffer.append(parts[parts.length - 1]).toString();
    }

    /**
     * Splits at each sequence of non-word characters. <p/>Sequences of capitals
     * will be left untouched.
     */
    private static String[] splitAtCapitalization(String string)
    {
        Pattern capitalSequencePattern = Pattern.compile("[A-Z]+");
        Matcher matcher = capitalSequencePattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(sb, ' ' + matcher.group());
        }
        matcher.appendTail(sb);

        // split on all non-word characters: make sure we send the good parts
        return sb.toString().split("[\\W+]");
    }

    /**
     * Suffixes each line with the argument suffix.
     * 
     * @param multiLines A String, optionally containing many lines
     * @param suffix The suffix to append to the end of each line
     * @return String The input String with the suffix appended at the end of
     *         each line
     */
    public static String suffixLines(String multiLines, String suffix)
    {
        final String[] lines = multiLines.split("\n");
        final StringBuffer linesBuffer = new StringBuffer();
        for (int i = 0; i < lines.length; i++)
        {
            String line = lines[i];
            linesBuffer.append(line);
            linesBuffer.append(suffix);
            linesBuffer.append("\n");
        }
        return linesBuffer.toString();
    }

    /**
     * Converts any multi-line String into a version that is suitable to be
     * included as-is in properties resource bundle.
     * 
     * @param multiLines A String, optionally containing many lines
     * @return String The input String with a backslash appended at the end of
     *         each line
     */
    public static String toResourceMessage(String multiLines)
    {
        String resourceMessage = null;

        if (multiLines != null)
        {
            final String suffix = "\\";
            multiLines = suffixLines(multiLines, ' ' + suffix).trim();
            while (multiLines.endsWith(suffix))
            {
                multiLines = multiLines.substring(
                    0,
                    multiLines.lastIndexOf(suffix)).trim();
            }
            resourceMessage = multiLines;
        }

        return resourceMessage;
    }

    /**
     * Takes an english word as input and prefixes it with 'a ' or 'an '
     * depending on the first character of the argument String.
     * <p>
     * The characters 'a', 'e', 'i' and 'o' will yield the 'an' predicate while
     * all the others will yield the 'a' predicate.
     * </p>
     * 
     * @param word the word needing the predicate
     * @return the argument prefixed with the predicate
     * @todo: this method could be implemented with better logic, for example to
     *        support 'an r' and 'a rattlesnake'
     */
    public static String prefixWithAPredicate(String word)
    {
        final StringBuffer formattedBuffer = new StringBuffer();

        formattedBuffer.append("a ");
        formattedBuffer.append(word);

        char firstChar = word.charAt(0);
        switch (firstChar)
        {
            case 'a' : // fall-through
            case 'e' : // fall-through
            case 'i' : // fall-through
            case 'o' :
                formattedBuffer.insert(1, 'n');
                break;
            default :
        }

        return formattedBuffer.toString();
    }

    /**
     * Converts multiline text into a single line, normalizing whitespace in the
     * process. This means whitespace characters will not follow each other
     * directly.
     * <p>
     * The resulting String will be trimmed.
     * <p>
     * <p>
     * If the input String is null the return value will be null too.
     * </p>
     * 
     * @param multiLine A multiline String, may be null
     * @return The argument in a single line
     */
    public static String toSingleLine(String multiLine)
    {
        return (multiLine == null) ? null : multiLine
            .replaceAll("[$\\s]+", " ").trim();
    }
}