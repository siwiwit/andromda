package org.andromda.core.translation.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.TranslationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a translation XML template found within a translation library.
 *
 * @author Chad Brandon
 */
public class Translation
{
    private String name;
    private final Map<String, Fragment> fragments = new LinkedHashMap<String, Fragment>();
    private final Collection<String> ignorePatterns = new ArrayList<String>();

    /**
     * The library translation to which this translation belongs.
     */
    private LibraryTranslation libraryTranslation;

    /**
     * Gets the LibraryTranslation to which this Translation belongs.
     *
     * @return LibraryTranslation
     */
    protected LibraryTranslation getLibraryTranslation()
    {
        // should never happen, but it doesn't hurt to be safe
        if (this.libraryTranslation == null)
        {
            throw new LibraryException("Translation.getLibraryTranslation - libraryTranslation can not be null");
        }
        return libraryTranslation;
    }

    /**
     * Sets the LibraryTranslation to which this Translation belongs.
     *
     * @param translation the LibraryTranslation to which this Translation belongs.
     */
    protected void setLibraryTranslation(final LibraryTranslation translation)
    {
        libraryTranslation = translation;
    }

    /**
     * Gets the fragment matching (using regular expressions) the specified name.
     *
     * @param name the name of the fragment to retrieve.
     * @return Fragment
     */
    protected Fragment getFragment(final String name)
    {
        Fragment fragment = null;
        // search through the names and the first name that matches
        // one of the names return the value of that name.
        for (String nextName : fragments.keySet())
        {
            if (name.matches(nextName))
            {
                fragment = fragments.get(nextName);
            }
        }

        // if the fragment is null, and the name isn't in an ignorePattern
        // element, then give an error
        if (fragment == null && !this.isIgnorePattern(name))
        {
            // TODO: make this work correctly with unsupported functions.

            /*
             * logger.error("ERROR! expression fragment '" + name + "' is not
             * currently supported --> add a <fragment/> with " + " a name that
             * matches this expression to your translation file " + "'" +
             * this.getLibraryTranslation().getFile() + "' to enable support");
             */
        }
        return fragment;
    }

    /**
     * Adds a new Translation fragment to the Translation.
     *
     * @param fragment new Translation fragment
     */
    public void addFragment(final Fragment fragment)
    {
        ExceptionUtils.checkNull(
            "fragment",
            fragment);
        fragment.setTranslation(this);
        this.fragments.put(
            fragment.getName(),
            fragment);
    }

    /**
     * Gets the name of this Translation.
     *
     * @return String
     */
    protected String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    protected void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Adds an <code>ignorePattern</code> to the Collection of ignorePatterns.
     *
     * @param ignorePattern the pattern to ignore.
     */
    public void addIgnorePattern(final String ignorePattern)
    {
        this.ignorePatterns.add(StringUtils.trimToEmpty(ignorePattern));
    }

    /**
     * Checks to see if the pattern is an ignore pattern. What this means is that if if this pattern matches on a
     * regular expression found in the collection of ignore patterns then the TranslationLibrary won't complain if it
     * doesn't match a fragment name.
     *
     * @param pattern
     * @return boolean <code>true</code> if its an ignore pattern, <code>false</code> otherwise.
     */
    public boolean isIgnorePattern(String pattern)
    {
        boolean isIgnorePattern = false;
        pattern = StringUtils.trimToEmpty(pattern);
        // search through the ignorePatterns and see if one
        // of them matches the passed in pattern.
        for (String nextIgnorePattern : this.ignorePatterns)
        {
            isIgnorePattern = pattern.matches(StringUtils.trimToEmpty(nextIgnorePattern));
            if (isIgnorePattern)
            {
                break;
            }
        }
        return isIgnorePattern;
    }

    /**
     * Gets the "translated" value of this Fragment if it exists. That is, it retrieves the fragment body for the name
     * of this fragment and replaces any fragment references with other fragment bodies (if they exist)
     *
     * @param name the name of the fragment.
     * @param kind the kind of the fragment.
     * @return String the translated body of the fragment kind.
     */
    protected String getTranslated(
        String name,
        String kind)
    {
        // clean the strings first
        name = StringUtils.trimToEmpty(name);
        kind = StringUtils.trimToEmpty(kind);

        ExceptionUtils.checkEmpty(
            "name",
            name);

        Fragment fragment = this.getFragment(name);
        String translated = "";
        if (fragment != null)
        {
            translated = fragment.getKind(kind);
            String begin = "fragment{";
            int beginLength = begin.length();
            String end = "}";
            for (int beginIndex = translated.indexOf(begin); beginIndex != -1;
                beginIndex = translated.indexOf(begin))
            {
                String fragmentName = translated.substring(
                        beginIndex + beginLength,
                        translated.length());
                int endIndex = fragmentName.indexOf(end);
                if (endIndex != -1)
                {
                    fragmentName = fragmentName.substring(
                            0,
                            endIndex);
                }
                StringBuilder toReplace = new StringBuilder(begin);
                toReplace.append(fragmentName);
                toReplace.append(end);
                translated =
                    StringUtils.replace(
                        translated,
                        toReplace.toString(),
                        this.getTranslated(
                            fragmentName,
                            kind));
            }
        }
        return TranslationUtils.removeExtraWhitespace(translated);
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}