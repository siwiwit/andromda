package org.andromda.maven;

import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.AndroMDA;
import org.andromda.core.AndroMDAServer;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.jelly.MavenJellyContext;


/**
 * This task is used with the AndroMDA Maven plugin.
 *
 * @author Chad Brandon
 * @see org.andromda.core.engine.ModelProcessor
 */
public class AndroMDARunner
{
    /**
     * The URI to the configuration;
     */
    private String configurationUri;

    /**
     * Sets the URI to the configuration file.
     *
     * @param configurationUri
     */
    public void setConfigurationUri(final String configurationUri)
    {
        this.configurationUri = configurationUri;
    }

    /**
     * Stores the search location for mapping files.
     */
    private String mappingsSearchLocation;

    /**
     * Sets the mappings search location.
     *
     * @param MappingsSearchLocation
     */
    public void setMappingsSearchLocation(final String mappingsSearchLocation)
    {
        this.mappingsSearchLocation = mappingsSearchLocation;
    }

    /**
     *  Runs AndroMDA.
     */
    public String run()
    {
        // - since maven doesn't fail on exceptions on this method (for some reason)
        //   we'll save the error message and if present call ant fail from within
        //   the plugin jelly.
        String message = null;
        Thread.currentThread().setContextClassLoader(AndroMDARunner.class.getClassLoader());
        try
        {
            final AndroMDA andromda = AndroMDA.newInstance();
            andromda.run(this.getConfiguration());
            andromda.shutdown();
        }
        catch (Throwable throwable)
        {
            final Throwable cause = ExceptionUtils.getCause(throwable);
            if (cause != null)
            {
                throwable = cause;
            }
            if (throwable instanceof FileNotFoundException)
            {
                message = "No configuration could be loaded from --> '" + configurationUri + "'";
            }
            else if (throwable instanceof MalformedURLException)
            {
                message = "Configuration is not a valid URI --> '" + configurationUri + "'";
            }
            throwable.printStackTrace();
            message = throwable.toString();
        }
        finally
        {
            // Set the context class loader back ot its system class loaders
            // so that any processes running after won't be trying to use
            // the ContextClassLoader for this class.
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
        }
        return message;
    }

    /**
     * Creates the Configuration instance from the {@link #configurationUri}
     * @return the configuration instance
     * @throws MalformedURLException if the URL is invalid.
     */
    private Configuration getConfiguration()
        throws MalformedURLException
    {
        final URL uri = new URL(this.configurationUri);
        final Configuration configuration =
            Configuration.getInstance(
                this.replaceProperties(ResourceUtils.getContents(uri)));
        configuration.addMappingsSearchLocation(this.mappingsSearchLocation);
        return configuration;
    }

    /**
     * The server instance.
     */
    private AndroMDAServer server = AndroMDAServer.newInstance();

    /**
     * Starts the AndroMDA server instance listening
     * for requests.
     *
     * @throws MalformedURLException
     */
    public void startServer()
        throws MalformedURLException
    {
        this.server.start(this.getConfiguration());
    }

    /**
     * Stops the AndroMDA server instance.
     */
    public void stopServer()
        throws MalformedURLException
    {
        this.server.stop(this.getConfiguration());
    }

    /**
     * The maven jelly context.
     */
    private MavenJellyContext context;

    /**
     * Sets the maven jelly context for this instance.
     */
    public void setContext(MavenJellyContext context)
    {
        this.context = context;
    }

    /**
     * Gets all property names.
     *
     * @return the property names.
     */
    public String[] getPropertyNames()
    {
        final Collection properties = new ArrayList();
        for (JellyContext context = this.context; context != null; context = context.getParent())
        {
            CollectionUtils.addAll(
                properties,
                context.getVariableNames());
        }
        return (String[])properties.toArray(new String[0]);
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(String string)
    {
        final String[] names = this.getPropertyNames();
        if (names != null && names.length > 0)
        {
            for (int ctr = 0; ctr < names.length; ctr++)
            {
                String value = null;
                final String name = names[ctr];
                final String property = "${" + name + "}";
                Object object = this.context.getVariable(name);
                if (object instanceof String)
                {
                    value = (String)object;
                }
                else if (object instanceof Expression)
                {
                    value = ((Expression)object).getExpressionText();
                }
                if (value != null)
                {
                    string = StringUtils.replace(string, property, value);
                }
            }
        }

        // remove any left over property references
        string = AndroMDAMavenUtils.removePropertyReferences(string);
        return string;
    }
}