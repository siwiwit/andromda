package org.andromda.core.common;

import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;


/**
 * Provides wild card matching on file paths (i.e. Cartridge.java will match <code>*.java</code>, etc).
 *
 * @author Chad Brandon
 */
public class PathMatcher
{
    /**
     * A forward slash.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * A double star within a pattern.
     */
    private static final String DOUBLE_STAR = "**/*";

    /**
     * A tripple star within a pattern.
     */
    private static final String TRIPPLE_STAR = "***";

    /**
     * Provides matching of simple wildcards. (i.e. '*.java' etc.)
     *
     * @param path the path to match against.
     * @param pattern the pattern to check if the path matches.
     * @return true if the <code>path</code> matches the given <code>pattern</code>, false otherwise.
     */
    public static boolean wildcardMatch(
        String path,
        String pattern)
    {
        ExceptionUtils.checkNull(
            "path",
            path);
        ExceptionUtils.checkNull(
            "pattern",
            pattern);

        // - remove any starting slashes, as they interfere with the matching
        if (path.startsWith(FORWARD_SLASH))
        {
            path =
                path.substring(
                    1,
                    path.length());
        }
        path = path.trim();
        pattern = pattern.trim();
        boolean matches;
        pattern =
            StringUtils.replace(
                pattern,
                ".",
                "\\.");
        boolean matchAll = pattern.indexOf(DOUBLE_STAR) != -1 && pattern.indexOf(TRIPPLE_STAR) == -1;
        if (matchAll)
        {
            String replacement = ".*";
            if (path.indexOf(FORWARD_SLASH) == -1)
            {
                replacement = ".*";
            }
            pattern =
                StringUtils.replaceOnce(
                    pattern,
                    DOUBLE_STAR,
                    replacement);
        }
        pattern =
            StringUtils.replace(
                pattern,
                "*",
                ".*");
        try
        {
            matches = path.matches(pattern);
        }
        catch (final PatternSyntaxException exception)
        {
            matches = false;
        }
        if (!matchAll)
        {
            matches =
                matches &&
                StringUtils.countMatches(
                    pattern,
                    FORWARD_SLASH) == StringUtils.countMatches(
                    path,
                    FORWARD_SLASH);
        }
        return matches;
    }
}