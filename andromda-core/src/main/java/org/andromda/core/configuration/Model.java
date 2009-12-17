package org.andromda.core.configuration;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Stores the model information for each model that AndroMDA will process.
 *
 * @author Chad Brandon
 */
public class Model
    implements Serializable
{
    /**
     * Stores whether or not a last modified check
     * should be performed.
     */
    private boolean lastModifiedCheck;

    /**
     * Whether or not to perform a last modified check on the model.
     *
     * @return Returns the lastModifiedCheck.
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * Sets whether or not to perform a last modified check when processing the model. If
     * <code>true</code> the model will be checked for a timestamp before processing occurs.
     *
     * @param lastModifiedCheck true/false
     */
    public void setLastModifiedCheck(final boolean lastModifiedCheck)
    {
        this.lastModifiedCheck = lastModifiedCheck;
    }

    /**
     * Stores the informationj about what packages should and shouldn't
     * be processed.
     */
    private Filters packages = new Filters();

    /**
     * Sets the processAll flag on the internal model packages instance
     * of this model.
     *
     * @param processAllPackages whether or not all packages should be processed by default.
     */
    public void setProcessAllPackages(final boolean processAllPackages)
    {
        packages.setApplyAll(processAllPackages);
    }

    /**
     * Stores the information about what packages should/shouldn't be processed.
     *
     * @return Returns the packages.
     */
    public Filters getPackages()
    {
        return this.packages;
    }

    /**
     * Sets the model packages for this model.  This indicates what
     * packages should and should not be processed from this model.
     *
     * @param packages the packages to process.
     */
    public void setPackages(final Filters packages)
    {
        this.packages = packages;
    }

    /**
     * Stores the informationj about what constraints should and shouldn't
     * be enforced.
     */
    private Filters constraints = new Filters();

    /**
     * Sets the applyAll flag on the internal filters instance
     * of this model.
     *
     * @param enforceAllConstraints whether or not all constraints should be enforced by default.
     */
    public void setEnforceAllConstraints(final boolean enforceAllConstraints)
    {
        this.constraints.setApplyAll(enforceAllConstraints);
    }

    /**
     * Stores the information about what constraints should/shouldn't be enforced.
     *
     * @return Returns the constraints instance.
     */
    public Filters getConstraints()
    {
        return this.constraints;
    }

    /**
     * Sets the constraints for this model.  This indicates what
     * constraints should and should not be processed from this model.
     *
     * @param constraints the packages to process.
     */
    public void setConstraints(final Filters constraints)
    {
        this.constraints = constraints;
    }

    /**
     * The URL to the model.
     */
    private List<URL> uris = new ArrayList<URL>();

    /**
     * Caches the urisAsStrings value (so we don't need
     * to do the conversion more than once).
     */
    private String[] urisAsStrings;

    /**
     * All URIs that make up the model.
     *
     * @return Returns the uri.
     */
    public String[] getUris()
    {
        if (this.urisAsStrings == null)
        {
            final int uriNumber = uris.size();
            this.urisAsStrings = new String[uriNumber];
            for (int ctr = 0; ctr < uriNumber; ctr++)
            {
                urisAsStrings[ctr] = uris.get(ctr).toString();
            }
        }
        return this.urisAsStrings;
    }

    /**
     * Adds the location as a URI to one of the model files.
     *
     * @param uri the URI to the model.
     */
    public void addUri(final String uri)
    {
        try
        {
            final URL url = ResourceUtils.toURL(uri);
            if (url == null)
            {
                throw new ConfigurationException("Model could not be loaded from invalid path --> '" + uri + '\'');
            }
            try
            {
                // - Get around the fact the URL won't be released until the JVM
                //   has been terminated, when using the 'jar' uri protocol.
                url.openConnection().setDefaultUseCaches(false);
            }
            catch (final IOException exception)
            {
                // - ignore the exception
            }
            this.uris.add(url);
        }
        catch (final Throwable throwable)
        {
            throw new ConfigurationException(throwable);
        }
    }

    /**
     * Stores the transformations for this Configuration instance.
     */
    private final Collection<Transformation> transformations = new ArrayList<Transformation>();

    /**
     * Adds a transformation to this configuration instance.
     *
     * @param transformation the transformation instance to add.
     */
    public void addTransformation(final Transformation transformation)
    {
        this.transformations.add(transformation);
    }

    /**
     * Gets the transformations belonging to this configuration.
     *
     * @return the array of {@link Transformation} instances.
     */
    public Transformation[] getTransformations()
    {
        return this.transformations.toArray(new Transformation[this.transformations.size()]);
    }

    /**
     * The locations in which to search for module.
     */
    private final Collection<Location> moduleSearchLocations = new ArrayList<Location>();

    /**
     * Adds a module search location (these are the locations
     * in which a search for module is performed).
     *
     * @param location a location path.
     * @see #addModuleSearchLocation(String)
     */
    public void addModuleSearchLocation(final Location location)
    {
        this.moduleSearchLocations.add(location);
    }

    /**
     * Adds a module search location path (a location
     * without a pattern defined).
     *
     * @param path a location path.
     * @see #addModuleSearchLocation(Location)
     */
    public void addModuleSearchLocation(final String path)
    {
        if (path != null)
        {
            final Location location = new Location();
            location.setPath(path);
            this.moduleSearchLocations.add(location);
        }
    }

    /**
     * The type of model (i.e. uml-1.4, uml-2.0, etc).
     */
    private String type;

    /**
     * Gets the type of the model (i.e. the type of metamodel this
     * model is based upon).
     *
     * @return Returns the type.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the type of model (i.e. the type of metamodel this model
     * is based upon).
     *
     * @param type The type to set.
     */
    public void setType(final String type)
    {
        this.type = type;
    }

    /**
     * Gets the module searach locations for this model instance.
     *
     * @return the module search locations.
     * @see #getModuleSearchLocationPaths()
     */
    public Location[] getModuleSearchLocations()
    {
        return this.moduleSearchLocations.toArray(new Location[this.moduleSearchLocations.size()]);
    }

    /**
     * Stores the path for each module search location in this configuration.
     */
    private String[] moduleSearchLocationPaths;

    /**
     * Gets all found module search location paths for this model instance.
     *
     * @return the module search location paths.
     * @see #getModuleSearchLocations()
     */
    public String[] getModuleSearchLocationPaths()
    {
        if (this.moduleSearchLocationPaths == null)
        {
            final Collection<String> paths = new ArrayList<String>();
            for (final Location location : this.moduleSearchLocations)
            {
                final URL[] resources = location.getResources();
                final int resourceNumber = resources.length;
                for (int ctr = 0; ctr < resourceNumber; ctr++)
                {
                    paths.add(resources[ctr].toString());
                }
                paths.add(location.getPath());
            }
            this.moduleSearchLocationPaths = paths.toArray(new String[paths.size()]);
        }
        return this.moduleSearchLocationPaths;
    }

    /**
     * Stores all resources including all resources found within the module search locations
     * as well as a resource for the {@link #getUris()}.
     */
    private URL[] moduleSearchLocationResources;

    /**
     * Gets the accumulation of all files found when combining the contents
     * of all module search location paths and their patterns by which they
     * are filtered as well as the model URI.
     *
     * @return all module search location files.
     */
    public URL[] getModuleSearchLocationResources()
    {
        if (this.moduleSearchLocationResources == null)
        {
            final Collection<URL> allResources = new ArrayList<URL>();
            final Location[] locations = this.getModuleSearchLocations();
            for (final Location location : locations)
            {
                final URL[] resources = location.getResources();
                allResources.addAll(Arrays.asList(resources));
            }
            this.moduleSearchLocationResources = allResources.toArray(new URL[allResources.size()]);
        }
        return this.moduleSearchLocationResources;
    }

    /**
     * Gets the time of the latest modified uri of the model as a <code>long</code>.
     * If it can not be determined <code>0</code> is returned.
     *
     * @return the time this model was last modified
     */
    public long getLastModified()
    {
        long lastModifiedTime = 0;
        for (final URL url : uris)
        {
            final long modifiedTime = ResourceUtils.getLastModifiedTime(url);
            if (modifiedTime > lastModifiedTime)
            {
                lastModifiedTime = modifiedTime;
            }
        }
        return lastModifiedTime;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String toString = super.toString();
        final String key = this.getKey();
        if (StringUtils.isNotBlank(key))
        {
            toString = key;
        }
        return toString;
    }

    /**
     * Stores the last modified times for each model at the time
     * {@link #isChanged()} is called.
     */
    private static final Map<String, Map<String, Long>> modelModifiedTimes = new HashMap<String, Map<String, Long>>();

    /**
     * The unique key that identifies this model.
     */
    private String key;

    /**
     * Creates the unique key that identifies this model
     * (its made up of a list of all the uris for this model
     * concatinated).
     *
     * @return the unique key
     */
    private String getKey()
    {
        if (StringUtils.isBlank(this.key))
        {
            final StringBuffer buffer = new StringBuffer();
            for (final Iterator<URL> iterator = this.uris.iterator(); iterator.hasNext();)
            {
                final URL uri = iterator.next();
                buffer.append(uri.getFile());
                if (iterator.hasNext())
                {
                    buffer.append(", ");
                }
            }
            this.key = buffer.toString();
        }
        return this.key;
    }

    /**
     * The repository to which this model belongs.
     */
    private Repository repository;

    /**
     * Gets the repository to which this model belongs.
     *
     * @return the repository to which this model belongs.
     */
    public Repository getRepository()
    {
        return this.repository;
    }

    /**
     * Sets the repository to which this model belongs.
     *
     * @param repository the repository configuration to which this model belongs.
     */
    void setRepository(final Repository repository)
    {
        this.repository = repository;
    }

    /**
     * Indicates whether or not the given <code>model</code>
     * has changed since the previous call to this method.
     *
     * @return true/false
     */
    public boolean isChanged()
    {
        boolean changed = this.getUris().length > 0;
        if (changed)
        {
            final String modelKey = this.getKey();
            Map<String, Long> lastModifiedTimes = modelModifiedTimes.get(modelKey);

            // - load up the last modified times (from the model and all its modules)
            //   if they haven't been loaded yet
            if (lastModifiedTimes != null)
            {
                final long modelLastModified = lastModifiedTimes.get(modelKey);
                changed = this.getLastModified() > modelLastModified;
                if (!changed)
                {
                    // - check to see if any of the modules have changed if the model hasn't changed
                    final URL[] resources = this.getModuleSearchLocationResources();
                    for (final URL resource : resources)
                    {
                        final Long lastModified = lastModifiedTimes.get(resource.getFile());
                        if (lastModified != null)
                        {
                            // - when we find the first modified module, break out
                            if (ResourceUtils.getLastModifiedTime(resource) > lastModified)
                            {
                                changed = true;
                                break;
                            }
                        }
                    }
                }
            }

            // - if our model (or modules) have changed re-load the last modified times
            if (changed)
            {
                this.loadLastModifiedTimes();
            }
        }
        return changed;
    }

    /**
     * Loads (or re-loads) the last modified times from the
     * {@link #getUris()} and the modules found on the module search path.
     */
    private void loadLastModifiedTimes()
    {
        final String modelKey = this.getKey();
        Map<String, Long> lastModifiedTimes = modelModifiedTimes.get(modelKey);
        if (lastModifiedTimes == null)
        {
            lastModifiedTimes = new HashMap<String, Long>();
        }
        else
        {
            lastModifiedTimes.clear();
        }
        final URL[] resources = this.getModuleSearchLocationResources();
        for (final URL resource : resources)
        {
            lastModifiedTimes.put(
                resource.getFile(),
                    ResourceUtils.getLastModifiedTime(resource));
        }

        // - add the model key last so it overwrites any invalid ones
        //   we might have picked up from adding the module search location files.
        lastModifiedTimes.put(
                modelKey,
                this.getLastModified());

        modelModifiedTimes.put(
            modelKey,
            lastModifiedTimes);
    }

    /**
     * Clears out the current last modified times.
     */
    static void clearLastModifiedTimes()
    {
        modelModifiedTimes.clear();
    }
}