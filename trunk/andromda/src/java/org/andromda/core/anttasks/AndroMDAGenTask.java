package org.andromda.core.anttasks;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.Model;
import org.andromda.core.ModelProcessor;
import org.andromda.core.ModelProcessorException;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.core.common.ModelPackage;
import org.andromda.core.common.ModelPackages;
import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * This class wraps the AndroMDA model processor so that AndroMDA can be used as
 * an Ant task. Represents the <code>&lt;andromda&gt;</code> custom task which
 * can be called from an Ant build script.
 * </p>
 * 
 * @see org.andromda.core.ModelProcessor
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author <a href="http://www.amowers.com">Anthony Mowers </a>
 * @author Chad Brandon
 */
public class AndroMDAGenTask
    extends MatchingTask
{
    /**
     * Initialize the context class loader.
     */
    static
    {
        initializeContextClassLoader();
    }

    /**
     * the base directory
     */
    private File baseDir = null;

    /**
     * check the last modified date on files. defaults to true
     */
    private boolean lastModifiedCheck = true;

    /**
     * A ModelPackages object which specify whether or not packages should be
     * processed.
     */
    private ModelPackages packages = new ModelPackages();

    private RepositoryConfiguration repositoryConfiguration = null;

    /**
     * Stores the path elements pointing to the mapping files
     */
    private Path mappingsSearchPath = null;

    private Collection models = new ArrayList();

    /**
     * Temporary list of properties from the &lt;namespace&gt; subtask. Will be
     * transferred to the Namespaces instance before execution starts.
     */
    private Collection namespaces = new ArrayList();

    /**
     * Adds a namespace for a Plugin. Namespace objects are used to configure
     * Plugins.
     * 
     * @param namespace a Namespace to add to this
     */
    public void addNamespace(Namespace namespace)
    {
        namespaces.add(namespace);
    }

    /**
     * <p>
     * Sets the base directory from which the object model files are read. This
     * defaults to the base directory of the ant project if not provided.
     * </p>
     * 
     * @param dir a <code>File</code> with the path to the base directory
     */
    public void setBasedir(File dir)
    {
        baseDir = dir;
    }

    /**
     * <p>
     * Turns on/off last modified checking for generated files. If checking is
     * turned on, overwritable files are regenerated only when the model is
     * newer than the file to be generated. By default, it is on.
     * </p>
     * 
     * @param lastmod set the modified check, yes or no?
     */
    public void setLastModifiedCheck(boolean lastmod)
    {
        this.lastModifiedCheck = lastmod;
    }

    private String cartridgeFilter;

    /**
     * <p>
     * Sets the current cartridge filter. This is a comma seperated list of
     * namespaces (matching cartridges names) that should be processed.
     * </p>
     * <p>
     * If this filter is defined, then any cartridge names found in this list
     * <strong>will be processed </strong>, while any other discovered
     * cartridges <strong>will not be processed </strong>.
     * </p>
     * 
     * @param cartridgeFilter a comma seperated list of cartridge names to be
     *        processed.
     * @see org.andromda.core.ModelProcessor#setCartridgeFilter(String)
     */
    public void setCartridgeFilter(String cartridgeFilter)
    {
        this.cartridgeFilter = cartridgeFilter;
    }

    /**
     * <p>
     * Starts the generation of source code from an object model.
     * </p>
     * <p>
     * This is the main entry point of the application when running Ant. It is
     * called by ant whenever the surrounding task is executed (which could be
     * multiple times).
     * </p>
     * 
     * @throws BuildException if something goes wrong
     */
    public void execute() throws BuildException
    {
        // initialize before the execute as well in case we
        // want to execute more than once
        initializeContextClassLoader();
        try
        {
            this.initializeMappings();
            this.initializeNamespaces();

            DirectoryScanner scanner;
            String[] list;

            if (baseDir == null)
            {
                // We directly change the user variable, because it
                // shouldn't lead to problems
                baseDir = this.getProject().resolveFile(".");
            }

            String[] moduleSearchPath = this.createRepository()
                .createModuleSearchPath().list();

            Model[] models;
            // if the model is specified explicitly
            // then we create the Model instances from the
            // ModelConfiguration instances.
            if (!this.models.isEmpty())
            {
                models = new Model[this.models.size()];
                Iterator modelIt = this.models.iterator();
                for (int ctr = 0; modelIt.hasNext(); ctr++)
                {
                    ModelConfiguration modelConfig = (ModelConfiguration)modelIt
                        .next();
                    if (modelConfig.getUrl() != null)
                    {
                        models[ctr] = new Model(
                            modelConfig.getUrl(),
                            this.packages,
                            this.lastModifiedCheck,
                            moduleSearchPath);
                    }
                }
            }
            else
            {
                // find the files/directories
                scanner = getDirectoryScanner(baseDir);

                // get a list of files to work on
                list = scanner.getIncludedFiles();

                if (list.length > 0)
                {
                    models = new Model[list.length];
                    for (int ctr = 0; ctr < list.length; ++ctr)
                    {
                        File inFile = new File(baseDir, list[ctr]);
                        try
                        {
                            models[ctr] = new Model(
                                inFile.toURL(),
                                this.packages,
                                this.lastModifiedCheck,
                                moduleSearchPath);
                        }
                        catch (MalformedURLException mfe)
                        {
                            throw new BuildException(
                                "Malformed model URI --> '" + inFile + "'");
                        }
                    }
                }
                else
                {
                    throw new BuildException("Could not find any model input!");
                }
            }

            // set the cartridge filter
            ModelProcessor.instance().setCartridgeFilter(this.cartridgeFilter);

            // pass the loaded model(s) to the ModelProcessor
            ModelProcessor.instance().process(models);
        }
        catch (ModelProcessorException th)
        {
            // This is "recognised" problem and will have been
            // recorded. So just throw a new build exception.
            throw new BuildException(th.getMessage());
        }
        catch (Throwable th)
        {
            ExceptionRecorder.instance().record(
                "Unexpected Exception",
                th,
                "AndroMDAGenTask");
            throw new BuildException(th.getMessage());
        }
        finally
        {
            // Set the context class loader back ot its system class loaders
            // so that any processes running after won't be trying to use
            // the ContextClassLoader for this class.
            Thread.currentThread().setContextClassLoader(
                ClassLoader.getSystemClassLoader());
        }
    }

    /**
     * Set the context class loader so that any classes using it (the
     * contextContextClassLoader) have access to the correct loader.
     */
    private static void initializeContextClassLoader()
    {
        Thread.currentThread().setContextClassLoader(
            AndroMDAGenTask.class.getClassLoader());
    }

    /**
     * This method would normally be unnecessary. It is here because of a bug in
     * ant. Ant calls addNamespace() before the Namespace javabean is fully
     * initialized. So we kept the javabeans in an ArrayList that we have to
     * copy into the Namespaces instance.
     */
    private void initializeNamespaces()
    {
        CollectionUtils.forAllDo(this.namespaces, new Closure()
        {
            public void execute(Object object)
            {
                Namespace namespace = (Namespace)object;
                Namespaces.instance().addNamespace(namespace);
            }
        });
    }
    
    /**
     * Creates and returns a repository configuration object. This enables an
     * ANT build script to use the &lt;repository&gt; ant subtask to configure
     * the model repository used by AndroMDA during code generation.
     * 
     * @return RepositoryConfiguration
     */
    public RepositoryConfiguration createRepository()
    {
        if (repositoryConfiguration == null)
        {
            repositoryConfiguration = new RepositoryConfiguration(getProject());
        }
        return repositoryConfiguration;
    }

    /**
     * Adds a new model configuration object. This enables an Ant build script
     * to use the <code>&lt;model&gt;</code> within the
     * <code>&lt;andromda&gt;</code> task, which allows multiple models to be
     * processed.
     * 
     * @param model a model to process.
     */
    public void addModel(ModelConfiguration model)
    {
        this.models.add(model);
    }

    /**
     * Specifies whether or not AndroMDA should process all packages.
     * 
     * @param processAllModelPackages true/false on whether or not to process
     *        all packages.
     * @see #addModelPackage(org.andromda.core.common.ModelPackage)
     * @see org.andromda.core.ModelProcessor#setProcessAllModelPackages(boolean)
     */
    public void setProcessAllModelPackages(boolean processAllModelPackages)
    {
        ModelProcessor.instance().setProcessAllModelPackages(
            processAllModelPackages);
    }

    /**
     * Adds the <code>packageName</code>. If processAllModelPackages is set
     * to true, then all packageNames added will be skipped during processing.
     * If processAllModelPackages is set to false, then all packages specified
     * by package names are the only packages that will be processed.
     * 
     * @param modelPackage the ModelPackage that should/shouldn't be processed.
     * @see setProcessAllModelPackages(boolean)
     */
    public void addModelPackage(ModelPackage modelPackage)
    {
        this.packages.addPackage(modelPackage);
    }

    /**
     * Sets <code>xmlValidation</code> to be true/false. This defines whether
     * XML resources loaded by AndroMDA (such as plugin descriptors) should be
     * validated. Sometimes underlying parsers don't support XML Schema
     * validation and in that case, we want to be able to turn it off.
     * 
     * @param validating true/false on whether we should validate XML resources
     *        used by AndroMDA
     */
    public void setXmlValidation(boolean xmlValidation)
    {
        XmlObjectFactory.setDefaultValidating(xmlValidation);
    }

    /**
     * Sets <code>loggingConfigurationUri</code> for AndroMDA.
     * 
     * @param the URI to the external logging configuration file.
     * @see ModelProcessor#setLoggingConfigurationUri(String)
     */
    public void setLoggingConfigurationUri(String loggingConfigurationUri)
    {
        ModelProcessor.instance().setLoggingConfigurationUri(
            loggingConfigurationUri);
    }

    /**
     * Sets <code>failOnModelValidationErrors</code> to be true/false. This
     * defines whether processing should fail if model validation errors are
     * present, default is <code>true</code>.
     * 
     * @param failOnModelValidationErrors true/false on whether AndroMDA should
     *        fail when model validation errors occurr.
     */
    public void setFailOnModelValidationErrors(
        boolean failOnModelValidationErrors)
    {
        ModelProcessor.instance().setFailOnValidationErrors(
            failOnModelValidationErrors);
    }

    /**
     * Sets <code>modelValidation</code> to be true/false. This defines
     * whether model validation should occur when AndroMDA processes model(s).
     * 
     * @param modelValidation true/false on whether model validation should be
     *        performed or not.
     * @see org.andromda.core.ModelProcessor#setModelValidation(boolean)
     * @see org.andromda.core.metafacade.MetafacadeFactory#setModelValidation(boolean)
     */
    public void setModelValidation(boolean modelValidation)
    {
        ModelProcessor.instance().setModelValidation(modelValidation);
    }

    /**
     * Handles the nested &lt;mappingSearchPath&gt; element. The user can
     * specify his/her own search path for mapping files. These mapping files
     * will then we loaded into memory and can be referenced by logical name.
     * 
     * @return Path the mapping search path
     */
    public Path createMappingsSearchPath()
    {
        if (mappingsSearchPath == null)
        {
            mappingsSearchPath = new Path(this.getProject());
        }
        return mappingsSearchPath;
    }

    /**
     * Loads all mappings from the specified mapping seach path. If the path
     * points to a directory the directory contents will be loaded, otherwise
     * just the mapping itself will be loaded.
     */
    private void initializeMappings()
    {
        String[] mappingsPaths = this.createMappingsSearchPath().list();
        Collection mappingsLocations = new ArrayList();
        if (mappingsLocations != null)
        {
            for (int ctr = 0; ctr < mappingsPaths.length; ctr++)
            {
                File mappingsPath = new File(mappingsPaths[ctr]);
                if (mappingsPath.isDirectory())
                {
                    File[] mappingsFiles = mappingsPath.listFiles();
                    if (mappingsFiles != null)
                    {
                        for (int ctr2 = 0; ctr2 < mappingsFiles.length; ctr2++)
                        {
                            mappingsLocations.add(mappingsFiles[ctr2]);
                        }
                    }
                }
                else
                {
                    mappingsLocations.add(mappingsPath);
                }
            }
            Iterator mappingsLocationIt = mappingsLocations.iterator();
            while (mappingsLocationIt.hasNext())
            {
                try
                {
                    Mappings
                        .addLogicalMappings(Mappings
                            .getInstance(((File)mappingsLocationIt.next())
                                .toURL()));
                }
                catch (Throwable th)
                {
                    // ignore the exception (probably means its a file
                    // other than a mapping and in that case we don't care
                }
            }
        }
    }
}