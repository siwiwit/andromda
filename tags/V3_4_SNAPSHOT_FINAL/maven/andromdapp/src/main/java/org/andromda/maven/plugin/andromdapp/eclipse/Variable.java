package org.andromda.maven.plugin.andromdapp.eclipse;

/**
 * Represents a variable that can be set in the eclipse configuration (i.e.
 * to map from an environment variable to an eclipse variable during classpath
 * generation, etc).
 *
 * @author Chad Brandon
 */
public class Variable
{
    private String name;

    private String value;

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return value
     */
    public String getValue()
    {
        return value;
    }
}
