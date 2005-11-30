package org.andromda.maven.plugin.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.andromdapp.hibernate.HibernateCreateSchema;
import org.andromda.maven.plugin.andromdapp.hibernate.HibernateDropSchema;
import org.andromda.maven.plugin.andromdapp.hibernate.HibernateUpdateSchema;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Provides the ability to drop database schemas.
 *
 * @goal schema
 * @requiresDependencyResolution runtime
 * @author Chad Brandon
 */
public class SchemaMojo
    extends AbstractMojo
{
    /**
     * The schema task to execute (create, drop, update)
     *
     * @parameter expression="${tasks}"
     * @required
     */
    private String tasks;

    /**
     * The type of the create schema task to execute.
     *
     * @parameter expression="hibernate"
     * @required
     */
    private String taskType;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Any property files that should be loaded into the schema properties.
     *
     * @parameter
     */
    private String[] propertyFiles;

    /**
     * The options that can be passed to the schema task.
     *
     * @parameter
     */
    private Properties properties = new Properties();

    /**
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    private ArtifactFactory factory;

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
    private ArtifactRepository localRepository;

    /**
     * The name of the JDBC driver class.
     *
     * @parameter
     * @required
     */
    private String jdbcDriver;

    /**
     * The JDBC connection URL.
     *
     * @parameter
     * @required
     */
    private String jdbcConnectionUrl;

    /**
     * The JDBC username for the database.
     *
     * @parameter
     * @required
     */
    private String jdbcUsername;

    /**
     * The JDBC password for the database.
     *
     * @parameter
     * @required
     */
    private String jdbcPassword;

    /**
     * The jar containing the JDBC driver.
     *
     * @parameter
     * @required
     */
    private String jdbcDriverJar;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            AndroMDALogger.initialize();
            if (this.tasks == null || this.tasks.length() == 0)
            {
                throw new MojoExecutionException("The schema 'tasks' must be specified");
            }
            final Map tasksMap = (Map)SchemaMojo.tasksCache.get(this.taskType);
            if (tasksMap == null)
            {
                throw new MojoExecutionException("'" + taskType + "' is not a valid task type, valid task types are: " +
                    tasksMap.keySet());
            }

            final String[] tasks = this.tasks.split(",");
            final int numberOfTasks = tasks.length;
            for (int ctr = 0; ctr < numberOfTasks; ctr++)
            {
                final String task = tasks[ctr].trim();
                if (this.propertyFiles != null)
                {
                    final int numberOfPropertyFiles = propertyFiles.length;
                    for (int ctr2 = 0; ctr2 < numberOfPropertyFiles; ctr2++)
                    {
                        final URL propertyFileUri = ResourceUtils.toURL(propertyFiles[ctr2]);
                        if (propertyFileUri != null)
                        {
                            final InputStream stream = propertyFileUri.openStream();
                            this.properties.load(stream);
                            stream.close();
                        }
                    }
                }

                final List classpathElements = new ArrayList(this.project.getRuntimeClasspathElements());
                classpathElements.addAll(this.getProvidedClasspathElements());
                this.initializeClasspathFromClassPathElements(classpathElements);
                final Class type = (Class)tasksMap.get(task);
                if (type == null)
                {
                    throw new MojoExecutionException("'" + task + "' is not a valid task, valid types are: " +
                        tasksMap.keySet());
                }
                final SchemaManagement schemaManagement = (SchemaManagement)ClassUtils.newInstance(type);
                final Connection connection = this.getConnection();
                this.executeSql(connection, schemaManagement.execute(connection, this.properties));
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("An error occured while attempting to create the schema", throwable);
        }
    }

    /**
     * Sets the current context class loader from the given runtime classpath elements.
     *
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     */
    protected void initializeClasspathFromClassPathElements(final List classpathFiles)
        throws MalformedURLException
    {
        if (classpathFiles != null && classpathFiles.size() > 0)
        {
            final URL[] classpathUrls = new URL[classpathFiles.size()];

            for (int ctr = 0; ctr < classpathFiles.size(); ++ctr)
            {
                final File file = new File((String)classpathFiles.get(ctr));
                if (this.getLog().isDebugEnabled())
                {
                    getLog().debug("adding to classpath '" + file + "'");
                }
                classpathUrls[ctr] = file.toURL();
            }

            final URLClassLoader loader =
                new URLClassLoader(classpathUrls,
                    Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    /**
     * The class loader containing the jdbc driver.
     */
    private ClassLoader jdbcDriverJarLoader = null;

    /**
     * Sets the current context class loader from the given <code>jdbcDriverJar</code>
     *
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     */
    protected ClassLoader getJdbcDriverJarLoader()
        throws MalformedURLException
    {
        if (this.jdbcDriverJarLoader == null)
        {
            jdbcDriverJarLoader =
                new URLClassLoader(
                    new URL[] {new File(this.jdbcDriverJar).toURL()},
                    Thread.currentThread().getContextClassLoader());
        }
        return jdbcDriverJarLoader;
    }

    /**
     * Adds any dependencies with a scope of 'provided' to the current project with a scope
     * of runtime.
     * @throws ArtifactNotFoundException
     * @throws ArtifactResolutionException
     */
    protected List getProvidedClasspathElements()
        throws ArtifactResolutionException, ArtifactNotFoundException
    {
        final List classpathElements = new ArrayList();
        final List dependencies = this.project.getDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            for (final Iterator iterator = dependencies.iterator(); iterator.hasNext();)
            {
                final Dependency dependency = (Dependency)iterator.next();
                if (Artifact.SCOPE_PROVIDED.equals(dependency.getScope()))
                {
                    final String file = this.getDependencyFile(dependency);
                    if (file != null)
                    {
                        classpathElements.add(file);
                    }
                }
            }
        }
        return classpathElements;
    }

    /**
     * Adds a dependency to the current project's dependencies.
     *
     * @param dependency
     * @throws ArtifactNotFoundException
     * @throws ArtifactResolutionException
     */
    private String getDependencyFile(final Dependency dependency)
        throws ArtifactResolutionException, ArtifactNotFoundException
    {
        String file = null;
        if (dependency != null)
        {
            final Artifact artifact =
                this.factory.createArtifact(
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion(),
                    null,
                    dependency.getType());

            this.artifactResolver.resolve(
                artifact,
                project.getRemoteArtifactRepositories(),
                this.localRepository);
            file = artifact.getFile() != null ? artifact.getFile().toString() : null;
        }
        return file;
    }

    /**
     * Retrieves a database connection, given the appropriate database information.
     *
     * @return the retrieved connection.
     * @throws Exception
     */
    protected Connection getConnection()
        throws Exception
    {
        Driver driver = (Driver)this.getJdbcDriverJarLoader().loadClass(this.jdbcDriver).newInstance();
        final Properties properties = new Properties();
        properties.setProperty(
            "user",
            this.jdbcUsername);
        properties.setProperty(
            "password",
            this.jdbcPassword);

        // - need to connect this way since we can't use the driver manager when not using
        //   the system class loader
        return driver.connect(
            this.jdbcConnectionUrl,
            properties);
    }
    
    /**
     * The statement end character.
     */
    private static final String STATEMENT_END = ";";
    
    /**
     * Executes the SQL contained with the file located at the <code>sqlPath</code>.
     * 
     * @param connection the connection used to execute the SQL.
     * @param sqlPath the path to the SQL file.
     * @throws Exception
     */
    public void executeSql(
        final Connection connection,
        final String sqlPath)
        throws Exception
    {

        if (sqlPath != null && sqlPath.length() > 0)
        {
            final URL sqlUrl = ResourceUtils.toURL(sqlPath);
            if (sqlUrl != null)
            {
                this.successes = 0;
                this.failures = 0;
                final Statement statement = connection.createStatement();
                final InputStream stream = sqlUrl.openStream();
                final BufferedReader resourceInput = new BufferedReader(new InputStreamReader(stream));
                StringBuffer sql = new StringBuffer();
                for (String line = resourceInput.readLine(); line != null; line = resourceInput.readLine())
                {
                    if (line.startsWith("//"))
                    {
                        continue;
                    }
                    if (line.startsWith("--"))
                    {
                        continue;
                    }
                    sql.append(line);
                    if (line.endsWith(STATEMENT_END))
                    {
                        this.executeSql(
                            statement,
                            sql.toString().replaceAll(
                                STATEMENT_END,
                                ""));
                        sql = new StringBuffer();
                    }
                    sql.append("\n");
                }
                resourceInput.close();
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                }
            }
            this.getLog().info(" Executed script: " + sqlPath);
            final String count = String.valueOf((this.successes + this.failures)).toString();
            this.getLog().info(" " + count + "  SQL statements executed");
            this.getLog().info(" Failures: " + this.failures);
            this.getLog().info(" Successes: " + this.successes);
        }
    }
    
    /**
     * Stores the count of statements that were executed successfully.
     */
    private int successes;

    /**
     * Stores the count of statements that failed.
     */
    private int failures;
    
    /**
     * Executes the given <code>sql</code>, using the given <code>statement</code>.
     *
     * @param statement the statement to use to execute the SQL.
     * @param sql the SQL to execute.
     * @throws SQLException
     */
    private void executeSql(
        final Statement statement,
        final String sql)
    {
        this.getLog().info(sql.trim());
        try
        {
            statement.execute(sql.toString());
            this.successes++;
        }
        catch (final SQLException exception)
        {
            this.failures++;
            this.getLog().warn(exception.toString());
        }
    }

    /**
     * Stores the task types.
     */
    private static final Map tasksCache = new LinkedHashMap();

    static
    {
        // - initialize the hibernat taks types
        final Map hibernateTasks = new LinkedHashMap();
        tasksCache.put(
            "hibernate",
            hibernateTasks);
        hibernateTasks.put(
            "create",
            HibernateCreateSchema.class);
        hibernateTasks.put(
            "drop",
            HibernateDropSchema.class);
        hibernateTasks.put(
            "update",
            HibernateUpdateSchema.class);
    }
}