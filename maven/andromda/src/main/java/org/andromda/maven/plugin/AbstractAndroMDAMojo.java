package org.andromda.maven.plugin;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.maven.plugin.configuration.AbstractConfigurationMojo;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

/**
 * The abstract AndroMDA Mojo.  This should be extended
 * by the Mojos that are used to run AndroMDA.
 *
 * @author Chad Brandon
 */
public abstract class AbstractAndroMDAMojo
    extends AbstractConfigurationMojo
{
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${project.basedir}/conf/andromda.xml"
     * @required
     */
    protected String configurationUri;

    /**
     * Do we allow the code generation to run multiple times? Yes for AndroMDA server,
     * no for all other cases unless overridden. This prevents multiple code generation
     * runs while creating site documentation, generate-sources phase can run more than 8 times
     * for each model when initiated by many of the reporting plugins.
     *
     * @parameter default-value="false"
     * @required
     */
    protected boolean allowMultipleRuns;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    protected List<String> propertyFiles;

    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    protected Settings settings;

    /**
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    protected ArtifactFactory factory;

    /**
     * The registered plugin implementations.
     *
     * @parameter expression="${project.build.plugins}"
     * @required
     * @readonly
     */
    protected List<Plugin> plugins;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    private static List<String> configurations = new ArrayList<String>();
    
    /**
     * Initialize configuration and execute plugin
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (this.allowMultipleRuns || !configurations.contains(this.configurationUri))
        {
            try
            {
                if (!this.allowMultipleRuns)
                {
                    configurations.add(this.configurationUri);
                }
                AndroMDALogger.initialize();
                final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
                if (configurationUri == null)
                {
                    throw new MojoExecutionException("Configuration could not be loaded from '" + this.configurationUri +
                            '\'');
                }
                // - setup the classpath
                this.addPluginDependencies(
                    Constants.ARTIFACT_ID,
                    Artifact.SCOPE_RUNTIME);
                this.initializeClasspathFromClassPathElements(this.project.getRuntimeClasspathElements());
                final Configuration configuration = this.getConfiguration(configurationUri);
                this.execute(configuration);
            }
            catch (Throwable throwable)
            {
                String message = "Error running AndroMDA";
                throwable = ExceptionUtils.getRootCause(throwable);
                if (throwable instanceof MalformedURLException || throwable instanceof FileNotFoundException)
                {
                    message = "Configuration is not valid '" + this.configurationUri + '\'';
                }
                throw new MojoExecutionException(message, throwable);
            }
        }
    }
    
    /**
     * Performs the execution of an AndroMDA service with the given 
     * <code>configuration</code>.
     * 
     * @param configuration the configuration to use for AndroMDA service execution
     * @throws Exception 
     */
    protected abstract void execute(final Configuration configuration) throws Exception;

    /**
     * @param configurationUri The configurationUri to set.
     */
    public void setConfigurationUri(String configurationUri)
    {
        this.configurationUri = configurationUri;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(MavenProject project)
    {
        this.project = project;
    }

    /**
     * Sets the current settings for this Mojo.
     *
     * @param settings The settings to set.
     */
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    /**
     * Sets the property files for this project.
     *
     * @param propertyFiles
     */
    public void setPropertyFiles(List<String> propertyFiles)
    {
        this.propertyFiles = propertyFiles;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getProject()
     */
    protected MavenProject getProject()
    {
        return this.project;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPropertyFiles()
     */
    protected List<String> getPropertyFiles()
    {
        return this.propertyFiles;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getSettings()
     */
    protected Settings getSettings()
    {
        return this.settings;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getFactory()
     */
    protected ArtifactFactory getFactory()
    {
        return this.factory;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPlugins()
     */
    protected List<Plugin> getPlugins()
    {
        return this.plugins;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getLocalRepository()
     */
    protected ArtifactRepository getLocalRepository()
    {
        return this.localRepository;
    }
}