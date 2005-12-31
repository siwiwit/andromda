package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.ClassUtils;
import org.andromda.maven.plugin.andromdapp.script.ScriptClassGenerator;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Allows for the {@link ScriptClassGenerator} mojo to be invoked.
 * on one or more given classes.
 *
 * @author Chad Brandon
 * @goal instrument-scripts
 * @phase compile
 */
public class ScriptClassGeneratorMojo
    extends AbstractMojo
{
    /**
     * Defines the java files who's classes will be instrumented.
     *
     * @required
     * @parameter
     */
    private Location[] locations;

    /**
     * Defines the fully qualified class name of the script wrapper implementation.
     *
     * @parameter
     * @required
     */
    private String scriptWrapper;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The java file extension
     */
    private static final String JAVA_EXTENSION = ".java";

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final ScriptClassGenerator generator = ScriptClassGenerator.getInstance(this.scriptWrapper);
            if (this.locations != null)
            {
                this.initializeClassLoader(this.project.getRuntimeClasspathElements());
                for (int ctr = 0; ctr < locations.length; ctr++)
                {
                    final Location location = locations[ctr];
                    String rootPath = location.getRootPath();
                    for (final Iterator iterator = location.getPaths().iterator(); iterator.hasNext();)
                    {
                        final String path = (String)iterator.next();
                        final int extensionIndex = path.lastIndexOf(JAVA_EXTENSION);
                        if (extensionIndex != -1)
                        {
                            final String className = path.substring(
                                    0,
                                    extensionIndex).replaceAll(
                                    "\\\\|/",
                                    "\\.");
                            this.getLog().info("injecting script wrapper: " + className);
                            generator.modifyClass(
                                rootPath,
                                ClassUtils.loadClass(className));
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Failed to inject script wrappers", throwable);
        }
    }

    /**
     * Sets the current context class loader from the given runtime classpath elements.
     *
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     */
    protected void initializeClassLoader(final List classpathFiles)
        throws MalformedURLException
    {
        final Set classpathUrls = new LinkedHashSet();
        classpathUrls.add(new File(this.project.getBuild().getOutputDirectory()).toURL());
        if (classpathFiles != null && classpathFiles.size() > 0)
        {
            for (int ctr = 0; ctr < classpathFiles.size(); ++ctr)
            {
                final File file = new File((String)classpathFiles.get(ctr));
                if (this.getLog().isDebugEnabled())
                {
                    getLog().debug("adding to classpath '" + file + "'");
                }
                classpathUrls.add(file.toURL());
            }
        }
        final URLClassLoader loader =
            new URLClassLoader((URL[])classpathUrls.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
    }
}