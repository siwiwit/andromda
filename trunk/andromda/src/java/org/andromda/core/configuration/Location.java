package org.andromda.core.configuration;

import java.io.Serializable;

import java.net.URL;

import java.util.List;
import java.util.ListIterator;

import org.andromda.core.common.ResourceUtils;


/**
 * Represents a location within a module search or mappings search.
 * @author Chad Brandon
 */
public class Location
    implements Serializable
{
    /**
     * The path of the location.
     */
    private String path;

    /**
     * The patterns (a comma seperated list) to
     * include in the path search
     */
    private String patterns;

    /**
     * Gets the path to this location.
     *
     * @return Returns the path to this location.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path to this location.
     *
     * @param path The path to this location.
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Gets the patterns to include in this location.
     *
     * @return Returns the patterns.
     */
    public String getPatterns()
    {
        return patterns;
    }

    /**
     * Sets the patterns to include in this location.
     *
     * @param patterns The patterns to set.
     */
    public void setPatterns(String patterns)
    {
        this.patterns = patterns;
    }

    /**
     * Gets all files that are valid for this location.  It takes into
     * consideration the given patterns.  If the location is an actual
     * file, the an array containing that single file is returned.
     *
     * @return the valid files.
     */
    public URL[] getResources()
    {
        URL[] resources;
        final URL url = ResourceUtils.toURL(this.path);
        if (url != null)
        {
            if (ResourceUtils.isFile(url))
            {
                resources = new URL[] {url};
            }
            else
            {
                String[] patterns = this.patterns != null ? this.patterns.split(PATTERN_DELIMITER) : new String[0];
                final List paths = ResourceUtils.getDirectoryContents(
                        url,
                        true,
                        patterns);
                for (final ListIterator iterator = paths.listIterator(); iterator.hasNext();)
                {
                    final URL resource = ResourceUtils.toURL((String)iterator.next());
                    if (resource != null)
                    {
                        iterator.set(resource);
                    }
                    else
                    {
                        iterator.remove();
                    }
                }
                resources = (URL[])paths.toArray(new URL[0]);
            }
        }
        else
        {
            resources = new URL[0];
        }
        return resources;
    }

    /**
     * The delimiter for seperating location patterns.
     */
    private static final String PATTERN_DELIMITER = ",";
}