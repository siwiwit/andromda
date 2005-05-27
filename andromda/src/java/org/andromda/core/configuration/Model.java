package org.andromda.core.configuration;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores the processing information for each model that AndroMDA will process.
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
    private boolean lastModifiedCheck = false;

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
    private ModelPackages packages = new ModelPackages();

    /**
     * Stores the information about what packages should/shouldn't be processed.
     *
     * @return Returns the packages.
     */
    public ModelPackages getPackages()
    {
        return this.packages;
    }

    /**
     * Adds a model package that indicates what should/shouldn't
     * be processed.
     * 
     * @param packages the packages to process.
     */
    public void addPackage(final ModelPackage modelPackage)
    {
        this.packages.addPackage(modelPackage);
    }
    
    /**
     * The URL to the model.
     */
    private URL uri;

    /**
     * The URL of the model.
     *
     * @return Returns the uri.
     */
    public URL getUri()
    {
        return uri;
    }
    
    /**
     * Sets the URL to the actual model file.
     * @param uri the model URL.
     */
    public void setUri(final String uri) throws Exception
    {
        try
        {
            this.uri = new URL(uri);
        }
        catch (final Throwable throwable)
        {
            throw new ConfigurationException(throwable);
        }
        try
        {
            // Get around the fact the URL won't be released until the JVM
            // has been terminated, when using the 'jar' uri protocol.
            this.uri.openConnection().setDefaultUseCaches(false);
        }
        catch (final IOException exception)
        {
            // ignore the exception
        }
    }

    /**
     * The locations in which to search for module.
     */
    private final Collection moduleSearchLocations = new ArrayList();

    /**
     * Adds a module search location (these are the locations
     * in which a search for module is performed).
     *
     * @param location a file location.
     */
    public void addModuleSearchLocation(final String location)
    {
        this.moduleSearchLocations.add(location);
    }

    /**
     * Gets the module searach location for this configuration instance.
     *
     * @return the module search locations.
     */
    public String[] getModuleSearchLocations()
    {
        return (String[])this.moduleSearchLocations.toArray(new String[0]);
    }

    /**
     * Gets the time as a <code>long</code> when this model was last modified. If it can not be determined
     * <code>0</code> is returned.
     *
     * @return the time this model was last modified
     */
    public long getLastModified()
    {
        long lastModified;
        try
        {
            URLConnection uriConnection = uri.openConnection();
            lastModified = uriConnection.getLastModified();
            uriConnection = null;
        }
        catch (Exception ex)
        {
            lastModified = 0;
        }
        return lastModified;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String toString = super.toString();
        if (this.uri != null)
        {
            toString = this.uri.toString();
        }
        return toString;
    }
}