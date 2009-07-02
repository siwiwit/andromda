package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.andromdapp.eclipse.ClasspathWriter;
import org.andromda.maven.plugin.andromdapp.eclipse.ProjectWriter;
import org.andromda.maven.plugin.andromdapp.eclipse.Variable;
import org.andromda.maven.plugin.andromdapp.utils.ProjectUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.xml.Xpp3Dom;


/**
 * Writes the necessary .classpath and .project files
 * for a new eclipse application.
 *
 * @goal eclipse
 * @phase generate-sources
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

    private static final String POM_FILE_NAME = "pom.xml";

    /**
     * Defines the POMs to include when generating the eclipse files.
     *
     * @parameter
     */
    private String[] includes = new String[] {"*/**/" + POM_FILE_NAME};

    /**
     * Defines the POMs to exclude when generating the eclipse files.
     *
     * @parameter expression="${exclude.poms}"
     */
    private String excludePoms;

    /**
     * Defines the POMs to exclude when generating the eclipse files.
     *
     * @parameter
     */
    private String[] excludes = new String[0];

    /**
     * Used to contruct Maven project instances from POMs.
     *
     * @component
     */
    private MavenProjectBuilder projectBuilder;

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
     * @parameter
     */
    protected Variable[] variables;

    /**
     * @component
     */
    private ArtifactMetadataSource artifactMetadataSource;

    /**
     * The artifact types which should be included in the generated Eclipse classpath.
     *
     * @parameter
     */
    private Set classpathArtifactTypes = new LinkedHashSet(Arrays.asList(new String[] {"jar","ejb"}));

    /**
     * Whether or not transitive dependencies shall be included in any resources (i.e. .classpath
     * that are generated by this mojo).
     *
     * @parameter expression="${resolveTransitiveDependencies}"
     */
    private boolean resolveTransitiveDependencies = true;

    /**
     * Allows non-generated configuration to be "merged" into the generated .classpath file.
     *
     * @parameter
     */
    private String classpathMerge;

    /**
     * Whether or not processing should be skipped (this is if you just want to force AndroMDA
     * not to run on your model).
     *
     * @parameter expression="${andromda.run.skip}"
     */
    private boolean skipProcessing = false;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        if (!this.skipProcessing)
        {
            try
            {
                final MavenProject rootProject = this.getRootProject();
                final ProjectWriter projectWriter = new ProjectWriter(rootProject,
                        this.getLog());
                projectWriter.write();
                final Map originalCompileSourceRoots = this.collectProjectCompileSourceRoots();
                final List projects = this.collectProjects();
                this.processCompileSourceRoots(projects);
                final ClasspathWriter classpathWriter = new ClasspathWriter(rootProject,
                        this.getLog());
                classpathWriter.write(
                    projects,
                    this.repositoryVariableName,
                    this.artifactFactory,
                    this.artifactResolver,
                    this.localRepository,
                    this.artifactMetadataSource,
                    this.classpathArtifactTypes,
                    this.project.getRemoteArtifactRepositories(),
                    this.resolveTransitiveDependencies,
                    this.variables,
                    this.classpathMerge);
                // - reset to the original source roots
                for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
                {
                    final MavenProject project = (MavenProject)iterator.next();
                    project.getCompileSourceRoots().clear();
                    project.getCompileSourceRoots().addAll((List)originalCompileSourceRoots.get(project));
                }
            }
            catch (Throwable throwable)
            {
                throw new MojoExecutionException("Error creating eclipse configuration", throwable);
            }
        }
    }

    /**
     * Collects all existing project compile source roots.
     *
     * @return a collection of collections
     */
    private Map collectProjectCompileSourceRoots()
        throws Exception
    {
        final Map sourceRoots = new LinkedHashMap();
        for (final Iterator iterator = this.collectProjects().iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            sourceRoots.put(project, new ArrayList(project.getCompileSourceRoots()));
        }
        return sourceRoots;
    }

    private List projects = new ArrayList();

    /**
     * Collects all projects from all POMs within the current project.
     *
     * @return all applicable Maven project instances.
     *
     * @throws MojoExecutionException
     */
    private List collectProjects()
        throws Exception
    {
        if (projects.isEmpty())
        {
            final List poms = this.getPoms();
            for (ListIterator iterator = poms.listIterator(); iterator.hasNext();)
            {
                final File pom = (File)iterator.next();
                try
                {
                    // - first attempt to get the existing project from the session
                    final MavenProject project = ProjectUtils.getProject(this.projectBuilder, this.session, pom, this.getLog());
                    if (project != null)
                    {
                        this.getLog().info("found project " + project.getId());
                        projects.add(project);
                    }
                    else
                    {
                        if (this.getLog().isWarnEnabled())
                        {
                            this.getLog().warn("Could not load project from pom: " + pom + " - ignoring");
                        }
                    }
                }
                catch (ProjectBuildingException exception)
                {
                    throw new MojoExecutionException("Error loading " + pom, exception);
                }
            }
        }
        return projects;
    }

    /**
     * Processes the project compile source roots (adds all appropriate ones to the projects)
     * so that they're avialable to the eclipse mojos.
     *
     * @param projects the projects to process.
     * @return the source roots.
     * @throws Exception
     */
    private void processCompileSourceRoots(final List projects)
        throws Exception
    {
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            final Set compileSourceRoots = new LinkedHashSet(project.getCompileSourceRoots());
            compileSourceRoots.addAll(this.getExtraSourceDirectories(project));
            final String testSourceDirectory = project.getBuild().getTestSourceDirectory();
            if (StringUtils.isNotBlank(testSourceDirectory))
            {
                compileSourceRoots.add(testSourceDirectory);
            }
            project.getCompileSourceRoots().clear();
            project.getCompileSourceRoots().addAll(compileSourceRoots);
        }
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
                final Xpp3Dom configuration = this.getConfiguration(multiSourcePlugin);
                if (configuration != null && configuration.getChildCount() > 0)
                {
                    final Xpp3Dom directories = configuration.getChild(0);
                    if (directories != null)
                    {
                        final int childCount = directories.getChildCount();
                        if (childCount > 0)
                        {
                            final String baseDirectory =
                                ResourceUtils.normalizePath(ObjectUtils.toString(project.getBasedir()) + '/');
                            final Xpp3Dom[] children = directories.getChildren();
                            for (int ctr = 0; ctr < childCount; ctr++)
                            {
                                final Xpp3Dom child = children[ctr];
                                if (child != null)
                                {
                                    String directoryValue = ResourceUtils.normalizePath(child.getValue());
                                    if (directoryValue != null)
                                    {
                                        if (!directoryValue.startsWith(baseDirectory))
                                        {
                                            directoryValue =
                                                ResourceUtils.normalizePath(baseDirectory + directoryValue.trim());
                                        }
                                        sourceDirectories.add(directoryValue);
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
     * Retrieves the appropriate configuration instance (first tries
     * to get the configuration from the plugin, then tries from the plugin's
     * executions.
     *
     * @param plugin the plugin from which the retrieve the configuration.
     * @return the plugin's configuration, or null if not found.
     */
    private Xpp3Dom getConfiguration(final Plugin plugin)
    {
        Xpp3Dom configuration = null;
        if (plugin != null)
        {
            if (plugin.getConfiguration() != null)
            {
                configuration = (Xpp3Dom)plugin.getConfiguration();
            }
            else
            {
                final List executions = plugin.getExecutions();
                if (executions != null && !executions.isEmpty())
                {
                    // - there should only be one execution so we get the first one
                    final PluginExecution execution = (PluginExecution)plugin.getExecutions().iterator().next();
                    configuration = (Xpp3Dom)execution.getConfiguration();
                }
            }
        }
        return configuration;
    }

    /**
     * Stores the root project.
     */
    private MavenProject rootProject;

    /**
     * Retrieves the root project (i.e. the root parent project)
     * for this project.
     *
     * @return the root project.
     * @throws MojoExecutionException
     */
    private MavenProject getRootProject()
        throws MojoExecutionException, ArtifactResolutionException, ArtifactNotFoundException
    {
        if (this.rootProject == null)
        {
            final MavenProject firstParent = this.project.getParent();
            File rootFile = this.project.getFile();
            if (firstParent != null && firstParent.getFile() != null )
            {
                for (this.rootProject = firstParent, rootFile = new File(rootFile.getParentFile().getParentFile(), POM_FILE_NAME);
                     this.rootProject.getParent() != null && this.rootProject.getParent().getFile() != null;
                     this.rootProject = this.rootProject.getParent(), rootFile = new File(rootFile.getParentFile().getParentFile(), POM_FILE_NAME))
                {
                    ;
                }
                // - if the project has no file defined, use the rootFile
                if (this.rootProject != null && this.rootProject.getFile() == null && rootFile.exists())
                {
                	this.rootProject.setFile(rootFile);
                }
            }
            else
            {
                this.rootProject = this.project;
            }
        }
        return this.rootProject;
    }

    /**
     * Retrieves all the POMs for the given project.
     *
     * @return all poms found.
     * @throws MojoExecutionException
     */
    private List getPoms()
        throws Exception
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.getRootProject().getBasedir());
        scanner.setIncludes(this.includes);

        final List<String> excludes = new ArrayList<String>(Arrays.asList(this.excludes));
        if (this.excludePoms != null)
        {
            excludes.addAll(Arrays.asList(excludePoms.split(",")));
        }
        scanner.setExcludes(excludes.toArray(new String[0]));
        scanner.scan();

        List poms = new ArrayList();

        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            final File file = new File(
                this.getRootProject().getBasedir(),
                scanner.getIncludedFiles()[ctr]);
            if (file.exists())
            {
                poms.add(file);
            }
        }

        return poms;
    }
}