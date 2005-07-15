package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(StringUtilsHelper.class);

    /**
     * <p/> Replaces a given suffix of the source string with a new one. If the
     * suffix isn't present, the string is returned unmodified.
     * </p>
     *
     * @param src       the <code>String</code> for which the suffix should be
     *                  replaced
     * @param suffixOld a <code>String</code> with the suffix that should be
     *                  replaced
     * @param suffixNew a <code>String</code> with the new suffix
     * @return a <code>String</code> with the given suffix replaced or
     *         unmodified if the suffix isn't present
     */
    public static String replaceSuffix(
        final String src,
        final String suffixOld,
        final String suffixNew)
    {
        if (src.endsWith(suffixOld))
        {
            return src.substring(0, src.length() - suffixOld.length()) + suffixNew;
        }
        return src;
    }

    /**
     * <p/> Returns the argument string as a camel cased name beginning with an
     * uppercased letter.
     * </p>
     * <p/> Non word characters be removed and the letter following such a
     * character will be uppercased.
     * </p>
     *
     * @param string any string
     * @return the string converted to a camel cased name beginning with a lower
     *         cased letter.
     */
    public static String upperCamelCaseName(final String string)
    {
        if (StringUtils.isEmpty(string))
        {
            return string;
        }

        final String[] parts = splitAtNonWordCharacters(string);
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
     * @param value  the value to remove.
     * @return String the resulting string.
     */
    public static String removeLastOccurrence(
        String string,
        final String value)
    {
        if (string != null && value != null)
        {
            StringBuffer buf = new StringBuffer();
            int index = string.lastIndexOf(value);
            if (index != -1)
            {
                buf.append(string.substring(0, index));
                buf.append(string.substring(
                    index + value.length(),
                    string.length()));
                string = buf.toString();
            }
        }
        return string;
    }

    /**
     * <p/> Returns the argument string as a camel cased name beginning with a
     * lowercased letter.
     * </p>
     * <p/> Non word characters be removed and the letter following such a
     * character will be uppercased.
     * </p>
     *
     * @param string any string
     * @return the string converted to a camel cased name beginning with a lower
     *         cased letter.
     */
    public static String lowerCamelCaseName(final String string)
    {
        return uncapitalize(upperCamelCaseName(string));
    }

    /**
     * Converts the argument into a message key in a properties resource bundle,
     * all lowercase characters, words are separated by dots.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         message key
     */
    public static String toResourceMessageKey(final String string)
    {
        return separate(
            StringUtils.trimToEmpty(string),
            ".").toLowerCase();
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
    public static String toPhrase(final String string)
    {
        return capitalize(separate(string, " "));
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and
     * replaces each of those sequences by the separator.
     */
    public static String separate(
        final String string,
        final String separator)
    {
        if (StringUtils.isBlank(string))
        {
            return string;
        }

        final String[] parts = splitAtNonWordCharacters(string);
        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            if (parts[i].trim().length() > 0)
            {
                buffer.append(parts[i]).append(separator);
            }
        }
        return buffer.append(parts[parts.length - 1]).toString();
    }

    /**
     * Splits at each sequence of non-word characters. <p/>Sequences of capitals
     * will be left untouched.
     */
    private static String[] splitAtNonWordCharacters(final String string)
    {
        Pattern capitalSequencePattern = Pattern.compile("[A-Z]+");
        Matcher matcher = capitalSequencePattern.matcher(StringUtils.trimToEmpty(string));
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
     * @param suffix     The suffix to append to the end of each line
     * @return String The input String with the suffix appended at the end of
     *         each line
     */
    public static String suffixLines(
        final String multiLines,
        final String suffix)
    {
        final String[] lines = StringUtils.trimToEmpty(multiLines).split(LINE_SEPARATOR);
        final StringBuffer linesBuffer = new StringBuffer();
        for (int i = 0; i < lines.length; i++)
        {
            String line = lines[i];
            linesBuffer.append(line);
            linesBuffer.append(suffix);
            linesBuffer.append(LINE_SEPARATOR);
        }
        return linesBuffer.toString();
    }

    /**
     * Converts any multi-line String into a version that is suitable to be
     * included as-is in properties resource bundle.
     *
     * @param multiLines A String, optionally containing many lines
     * @return String The input String with a backslash appended at the end of
     *         each line, or <code>null</code> if the input String was blank.
     */
    public static String toResourceMessage(String multiLines)
    {
        String resourceMessage = null;
        if (StringUtils.isNotBlank(multiLines))
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
     * depending on the first character of the argument String. <p/> The
     * characters 'a', 'e', 'i' and 'o' will yield the 'an' predicate while all
     * the others will yield the 'a' predicate.
     * </p>
     *
     * @param word the word needing the predicate
     * @return the argument prefixed with the predicate
     * @todo: this method could be implemented with better logic, for example to
     * support 'an r' and 'a rattlesnake'
     */
    public static String prefixWithAPredicate(final String word)
    {
        final StringBuffer formattedBuffer = new StringBuffer();

        formattedBuffer.append("a ");
        formattedBuffer.append(word);

        char firstChar = word.charAt(0);
        switch (firstChar)
        {
            case 'a': // fall-through
            case 'e': // fall-through
            case 'i': // fall-through
            case 'o':
                formattedBuffer.insert(1, 'n');
                break;
            default:
        }

        return formattedBuffer.toString();
    }

    /**
     * Converts multiline text into a single line, normalizing whitespace in the
     * process. This means whitespace characters will not follow each other
     * directly. <p/> The resulting String will be trimmed. <p/> <p/> If the
     * input String is null the return value will be an empty string.
     * </p>
     *
     * @param string A String, may be null
     * @return The argument in a single line
     */
    public static String toSingleLine(String string)
    {
        // remove anything that is greater than 1 space.
        return (string == null) ? "" : string.replaceAll("[$\\s]+", " ").trim();
    }

    /**
     * Linguistically pluralizes a singular noun. <p/>
     * <ul>
     * <li><code>noun</code> becomes <code>nouns</code></li>
     * <li><code>key</code> becomes <code>keys</code></li>
     * <li><code>word</code> becomes <code>words</code></li>
     * <li><code>property</code> becomes <code>properties</code></li>
     * <li><code>bus</code> becomes <code>busses</code></li>
     * <li><code>boss</code> becomes <code>bosses</code></li>
     * </ul>
     * <p/> Whitespace as well as <code>null</code> arguments will return an
     * empty String.
     * </p>
     *
     * @param singularNoun A singular noun to pluralize
     * @return The plural of the argument singularNoun
     */
    public static String pluralize(final String singularNoun)
    {
        String pluralNoun = trimToEmpty(singularNoun);

        int nounLength = pluralNoun.length();

        if (nounLength == 1)
        {
            pluralNoun = pluralNoun + 's';
        }
        else if (nounLength > 1)
        {
            char secondToLastChar = pluralNoun.charAt(nounLength - 2);

            if (pluralNoun.endsWith("y"))
            {
                switch (secondToLastChar)
                {
                    case 'a': // fall-through
                    case 'e': // fall-through
                    case 'i': // fall-through
                    case 'o': // fall-through
                    case 'u':
                        pluralNoun = pluralNoun + 's';
                        break;
                    default:
                        pluralNoun = pluralNoun.substring(0, nounLength - 1) + "ies";
                }
            }
            else if (pluralNoun.endsWith("s"))
            {
                switch (secondToLastChar)
                {
                    case 's':
                        pluralNoun = pluralNoun + "es";
                        break;
                    default:
                        pluralNoun = pluralNoun + "ses";
                }
            }
            else
            {
                pluralNoun = pluralNoun + 's';
            }
        }
        return pluralNoun;
    }

    /**
     * Formats the argument string without any indentiation, the text will be
     * wrapped at the default column.
     *
     * @see #format(String, String)
     */
    public static String format(final String plainText)
    {
        return format(plainText, "");
    }

    /**
     * Formats the given argument with the specified indentiation, wrapping the
     * text at a 64 column margin.
     *
     * @see #format(String, String, int)
     */
    public static String format(
        final String plainText,
        final String indentation)
    {
        return format(plainText, indentation, 64);
    }

    /**
     * Formats the given argument with the specified indentiation, wrapping the
     * text at the desired column margin. The returned String will not be suited
     * for display in HTML environments.
     *
     * @see #format(String, String, int, boolean)
     */
    public static String format(
        final String plainText,
        final String indentation,
        final int wrapAtColumn)
    {
        return format(plainText, indentation, wrapAtColumn, true);
    }

    /**
     * <p/>
     * Formats the given argument with the specified indentation, wrapping the
     * text at the desired column margin.
     * </p>
     * <p/>
     * When enabling <em>htmlStyle</em> the returned text will be suitable for
     * display in HTML environments such as JavaDoc, all newlines will be
     * replaced by paragraphs.
     * </p>
     * <p/>
     * This method trims the input text: all leading and trailing whitespace
     * will be removed.
     * </p>
     * <p/>
     * If for some reason this method would fail it will return the
     * <em>plainText</em> argument.
     * </p>
     *
     * @param plainText    the text to format, the empty string will be returned in
     *                     case this argument is <code>null</code>; long words will be
     *                     placed on a newline but will never be wrapped
     * @param indentation  the empty string will be used if this argument would
     *                     be <code>null</code>
     * @param wrapAtColumn does not take into account the length of the
     *                     indentation, needs to be stricly positive
     * @param htmlStyle    whether or not to make sure the returned string is
     *                     suited for display in HTML environments such as JavaDoc
     * @return a String instance which represents the formatted input, never
     *         <code>null</code>
     * @throws IllegalArgumentException when the <em>wrapAtColumn</em>
     *                                  argument is not strictly positive
     */
    public static String format(
        final String plainText,
        String indentation,
        final int wrapAtColumn,
        final boolean htmlStyle)
    {
        // - we cannot wrap at a column index less than 1
        if (wrapAtColumn < 1)
        {
            throw new IllegalArgumentException("Cannot wrap at column less than 1: " + wrapAtColumn);
        }

        // unspecified indentation will use the empty string
        if (indentation == null)
        {
            indentation = "";
        }

        // - null plaintext will yield the empty string
        if (StringUtils.isBlank(plainText))
        {
            return indentation;
        }

        final String lineSeparator = LINE_SEPARATOR;

        String format = null;

        try
        {
            // - this buffer will contain the formatted text
            final StringBuffer formattedText = new StringBuffer();

            // - we'll be reading lines from this reader
            final BufferedReader reader = new BufferedReader(new StringReader(plainText));

            String line = reader.readLine();

            // - test whether or not we reached the end of the stream
            while (line != null)
            {
                if (StringUtils.isNotBlank(line))
                {
                    // - in HTML mode we start each new line on a paragraph
                    if (htmlStyle)
                    {
                        formattedText.append(indentation);
                        formattedText.append("<p>");
                        formattedText.append(lineSeparator);
                    }

                    // - WordUtils.wrap never indents the first line so we do it
                    // here
                    formattedText.append(indentation);

                    // - append the wrapped text, the indentation is prefixed
                    // with a newline
                    formattedText.append(WordUtils.wrap(
                        line.trim(),
                        wrapAtColumn,
                        lineSeparator + indentation,
                        false));

                    // - in HTML mode we need to close the paragraph
                    if (htmlStyle)
                    {
                        formattedText.append(lineSeparator);
                        formattedText.append(indentation);
                        formattedText.append("</p>");
                    }
                }

                // - read the next line
                line = reader.readLine();

                // - only add a newline when the next line is not empty and some
                // string have already been added
                if (formattedText.length() > 0 && StringUtils.isNotBlank(line))
                {
                    formattedText.append(lineSeparator);
                }
            }

            // - close the reader as there is nothing more to read
            reader.close();

            // - set the return value
            format = formattedText.toString();
        }
        catch (final IOException ioException)
        {
            logger.error("Could not format text: " + plainText, ioException);
            format = plainText;
        }

        return format;
    }

    /**
     * The line separator.
     */
    private static final String LINE_SEPARATOR = "\n";

    /**
     * Gets the line separator.
     *
     * @return the line separator.
     */
    public static final String getLineSeparator()
    {
        // - for reasons of platform compatibility we do not use the 'line.separator' property
        //   since this will break the build on different platforms (for exampele
        //   when comparing cartridge output zips)
        return LINE_SEPARATOR;
    }
}