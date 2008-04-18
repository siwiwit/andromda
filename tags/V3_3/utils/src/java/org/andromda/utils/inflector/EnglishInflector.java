package org.andromda.utils.inflector;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Language utility for transforming English words.
 * See also <a href="http://www.csse.monash.edu.au/~damian/papers/HTML/Plurals.html">
 * http://www.csse.monash.edu.au/~damian/papers/HTML/Plurals.html</a>
 *
 * @author maetl@coretxt.net.nz
 * @author wouter@andromda.org
 */
public class EnglishInflector
{
    /**
     * Converts an English word to plural form
     *
     * @param word an English word
     * @return the pluralization of the argument English word, or the argument in case it is <code>null</code>
     */
    public static String pluralize(String word)
    {
        if (word == null) return null;

        final Map rules = EnglishInflector.getPluralRules();
        for (Iterator ruleIterator = rules.entrySet().iterator(); ruleIterator.hasNext();)
        {
            final Map.Entry rule = (Map.Entry)ruleIterator.next();
            final String pattern = rule.getKey().toString();
            final String replace = rule.getValue().toString();
            if (word.matches(pattern))
            {
                return word.replaceFirst(pattern, replace);
            }
        }
        return word.replaceFirst("([\\w]+)([^s])$", "$1$2s");
    }

    /**
     * Returns map of plural patterns
     */
    private static Map getPluralRules()
    {
        final Map rules = new HashMap();
        rules.put("(\\w+)(x|ch|ss|sh)$", "$1$2es");
        rules.put("(\\w+)([^aeiou])y$", "$1$2ies");
        rules.put("(\\w*)(f)$", "$1ves");
        rules.put("(\\w*)(fe)$", "$1ves");
        rules.put("(\\w+)(sis)$", "$1ses");
        rules.put("(\\w*)person$", "$1people");
        rules.put("(\\w*)child$", "$1children");
        rules.put("(\\w*)series$", "$1series");
        rules.put("(\\w*)foot$", "$1feet");
        rules.put("(\\w*)tooth$", "$1teeth");
        rules.put("(\\w*)bus$", "$1buses");
        rules.put("(\\w*)man$", "$1men");
        return rules;
    }

    /**
     * Converts an English word to singular form
     *
     * @param word an English word
     * @return the singularization of the argument English word, or the argument in case it is <code>null</code>
     */
    public static String singularize(String word)
    {
        if (word == null) return null;

        final Map rules = EnglishInflector.getSingularRules();
        for (Iterator ruleIterator = rules.entrySet().iterator(); ruleIterator.hasNext();)
        {
            final Map.Entry rule = (Map.Entry)ruleIterator.next();
            final String pattern = rule.getKey().toString();
            final String replace = rule.getValue().toString();
            if (word.matches(pattern))
            {
                return word.replaceFirst(pattern, replace);
            }
        }
        return word.replaceFirst("([\\w]+)s$", "$1");
    }

    /**
     * Returns map of singular patterns
     */
    private static Map getSingularRules()
    {
        final Map rules = new HashMap();
        rules.put("(\\w+)(x|ch|ss)es$", "$1$2");
        rules.put("(\\w+)([^aeiou])ies", "$1$2y");
        rules.put("(\\w+)([^l])ves", "$1$2fe");
        rules.put("(\\w+)([ll])ves", "$1$2f");
        rules.put("(\\w+)(ses)$", "$1sis");
        rules.put("(\\w*)people$", "$1person");
        rules.put("(\\w*)children$", "$1child");
        rules.put("(\\w*)series$", "$1series");
        rules.put("(\\w*)feet$", "$1foot");
        rules.put("(\\w*)teeth$", "$1tooth");
        rules.put("(\\w*)buses$", "$1bus");
        rules.put("(\\w*)men$", "$1man");
        return rules;
    }

}

