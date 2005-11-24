package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.andromdapp.eclipse.ClasspathWriter;
import org.andromda.maven.plugin.andromdapp.eclipse.ProjectWriter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.xml.Xpp3Dom;


/**
 * Writes the necessary .classpath and .project files
 * for a new eclipse application.
 *
 * @goal eclipse
 * @phase validate
 * @author Chad Brandon
 */
public class EclipseMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${session}"
     */
    private MavenSession session;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Defines the POMs to include when generating the eclipse files.
     *
     * @parameter
     */
    private String[] includes = new String[] {"*/pom.xml"};

    /**
     * Defines the POMs to exclude when generating the eclipse files.
     *
     * @parameter
     */
    private String[] excludes = new String[0];

    /**
     * The name of the variable that will store the maven repository location.
     *
     * @parameter expression="${repository.variable.name}
     */
    private String repositoryVariableName = "M2_REPO";

    /**
     * Artifact factory, needed to download source jars for inclusion in classpath.
     *
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * Artifact resolver, needed to download source jars for inclusion in classpath.
     *
     * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
     * @required
     * @readonly
     */
    private ArtifactResolver artifactResolver;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final List projects = this.collectProjects();
            if (!projects.isEmpty())
            {
                final ProjectWriter projectWriter = new ProjectWriter(this.project,
                        this.getLog());
                projectWriter.write();
                final ClasspathWriter classpathWriter = new ClasspathWriter(this.project,
                        this.getLog());
                classpathWriter.write(
                    this.collectProjects(),
                    this.repositoryVariableName,
                    this.artifactFactory,
                    this.artifactResolver,
                    this.localRepository);
            }
        }
        catch (Throwable throwable)
        {
            throw new MojoExecutionException("Error creating eclipse configuration", throwable);
        }
    }

    /**
     * Collects all projects from all POMs within the current project.
     *
     * @return all collection Maven project instances.
     *
     * @throws MojoExecutionException
     */
    private List collectProjects()
        throws Exception
    {
        final List projects = new ArrayList();
        MavenProjectBuilder projectBuilder;
        try
        {
            projectBuilder = (MavenProjectBuilder)this.session.getContainer().lookup(MavenProjectBuilder.ROLE);
        }
        catch (ComponentLookupException exception)
        {
            throw new MojoExecutionException("Cannot get a MavenProjectBuilder instance", exception);
        }

        final List poms = this.getPoms();
        for (ListIterator iterator = poms.listIterator(); iterator.hasNext();)
        {
            final File pom = (File)iterator.next();
            try
            {
                final MavenProject project =
                    projectBuilder.build(
                        pom,
                        this.session.getLocalRepository(),
                        new DefaultProfileManager(this.session.getContainer()));
                project.getCompileSourceRoots().addAll(this.getExtraSourceDirectories(project));
                if (this.getLog().isDebugEnabled())
                {
                    this.getLog().debug("Adding project " + project.getId());
                }
                projects.add(project);
            }
            catch (ProjectBuildingException exception)
            {
                throw new MojoExecutionException("Error loading " + pom, exception);
            }
        }

        return projects;
    }

    /**
     * The artifact id for the multi source plugin.
     */
    private static final String MULTI_SOURCE_PLUGIN_ARTIFACT_ID = "andromda-multi-source-plugin";

    /**
     * Retrieves any additional source directories which are defined within the andromda-multi-source-plugin.
     *
     * @param project the maven project from which to retrieve the extra source directories.
     * @return the list of extra source directories.
     */
    private List getExtraSourceDirectories(final MavenProject project)
    {
        final List sourceDirectories = new ArrayList();
        final Build build = project.getBuild();
        if (build != null)
        {
            final PluginManagement pluginManagement = build.getPluginManagement();
            if (pluginManagement != null && !pluginManagement.getPlugins().isEmpty())
            {
                Plugin multiSourcePlugin = null;
                for (final Iterator iterator = pluginManagement.getPlugins().iterator(); iterator.hasNext();)
                {
                    final Plugin plugin = (Plugin)iterator.next();
                    if (MULTI_SOURCE_PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId()))
                    {
                        multiSourcePlugin = plugin;
                        break;
                    }
                }
                if (multiSourcePlugin != null && !multiSourcePlugin.getExecutions().isEmpty())
                {
                    // - there should only be one execution so we get the first
                    final PluginExecution execution =
                        (PluginExecution)multiSourcePlugin.getExecutions().iterator().next();
                    final Xpp3Dom configuration = (Xpp3Dom)execution.getConfiguration();
                    if (configuration != null && configuration.getChildCount() > 0)
                    {
                        final Xpp3Dom directories = configuration.getChild(0);
                        if (directories != null)
                        {
                            final int childCount = directories.getChildCount();
                            if (childCount > 0)
                            {
                                final Xpp3Dom[] children = directories.getChildren();
                                for (int ctr = 0; ctr < childCount; ctr++)
                                {
                                    final Xpp3Dom child = children[ctr];
                                    if (child != null)
                                    {
                                        final String directoryValue = child.getValue();
                                        if (directoryValue != null)
                                        {
                                            sourceDirectories.add(
                                                ResourceUtils.normalizePath(
                                                    ObjectUtils.toString(project.getBasedir()) + '/' +
                                                    directoryValue.trim()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return sourceDirectories;
    }

    /**
     * Retrieves all the POMs for the given project.
     *
     * @return all poms found.
     */
    private List getPoms()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.project.getBasedir());
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();

        List poms = new ArrayList();

        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            poms.add(new File(
                    this.project.getBasedir(),
                    scanner.getIncludedFiles()[ctr]));
        }

        return poms;
    }
}