package org.andromda.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Repository;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * A Maven2 plugin to run AndroMDA.
 *
 * @author Chad Brandon
 * @goal run
 * @phase generate-sources
 */
public class AndroMDAMojo
    extends AbstractMojo
{
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/andromda.xml"
     * @required
     */
    private String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List propertyFiles;

    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * The path to the mappings within the plugin.
     */
    private static final String MAPPINGS_PATH = "META-INF/andromda/mappings";

    /**
     * Whether or not a last modified check should be performed before running AndroMDA again.
     *
     * @parameter expression="false"
     * @required
     */
    private boolean lastModifiedCheck;

    /**
     * The directory to which the build source is located (any generated source).
     *
     * @parameter expression="${project.build.directory}/src/main/java"
     */
    private String buildSourceDirectory;

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final Configuration configuration = this.getConfiguration();
            boolean execute = true;
            if (this.lastModifiedCheck)
            {
                final Repository[] repositories = configuration.getRepositories();
                int repositoryCount = repositories.length;
                for (int ctr = 0; ctr < repositoryCount; ctr++)
                {
                    final Repository repository = repositories[ctr];
                    if (repository != null)
                    {
                        final Model[] models = repository.getModels();
                        final int modelCount = models.length;
                        for (int ctr2 = 0; ctr2 < modelCount; ctr2++)
                        {
                            final Model model = models[ctr2];
                            execute =
                                ResourceUtils.modifiedAfter(
                                    model.getLastModified(),
                                    new File(this.buildSourceDirectory));
                            if (execute)
                            {
                                break;
                            }
                        }
                    }
                }
            }
            if (execute)
            {
                final AndroMDA andromda = AndroMDA.newInstance();
                andromda.run(configuration);
                andromda.shutdown();
                final File buildSourceDirectory =
                    this.buildSourceDirectory != null ? new File(this.buildSourceDirectory) : null;
                if (buildSourceDirectory != null)
                {
                    this.project.addCompileSourceRoot(buildSourceDirectory.toString());
                }
            }
            else
            {
                this.getLog().info("Files are up-to-date, skipping AndroMDA execution");
            }
        }
        catch (Throwable throwable)
        {
            String message = "Error running AndroMDA";
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
            throw new MojoExecutionException(message, throwable);
        }
    }

    /**
     * Creates the Configuration instance from the {@link #configurationUri}
     * @return the configuration instance
     * @throws MalformedURLException if the URL is invalid.
     */
    private Configuration getConfiguration()
        throws IOException
    {
        final URL uri = ResourceUtils.toURL(this.configurationUri);
        final String contents = this.replaceProperties(ResourceUtils.getContents(uri));
        final Configuration configuration = Configuration.getInstance(contents);
        final URL mappingsUrl = ResourceUtils.getResource(MAPPINGS_PATH);
        if (mappingsUrl != null)
        {
            configuration.addMappingsSearchLocation(mappingsUrl.toString());
        }
        return configuration;
    }

    protected Properties getProperties()
        throws IOException
    {
        // System properties
        Properties properties = new Properties(System.getProperties());

        properties.put(
            "settings",
            this.settings);

        // Project properties
        properties.putAll(this.project.getProperties());
        for (final Iterator iterator = propertyFiles.iterator(); iterator.hasNext();)
        {
            final String propertiesFile = (String)iterator.next();

            final Properties projectProperties = PropertyUtils.loadPropertyFile(
                    new File(propertiesFile),
                    true,
                    true);

            properties.putAll(projectProperties);
        }
        return properties;
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(final String string)
        throws IOException
    {
        final Properties properties = this.getProperties();
        final StringReader stringReader = new StringReader(string);
        InterpolationFilterReader reader = new InterpolationFilterReader(stringReader, properties, "${", "}");
        reader.reset();
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(project),
                "${",
                "}");
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(settings),
                "${",
                "}");
        final String contents = ResourceUtils.getContents(reader);
        return contents;
    }
}