package org.andromda.repositories.emf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ResourceFinder;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;


/**
 * An abtract EMF {@link RepositoryFacade} instance that should be extended by any repository wishing to load EMF models.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 */
public abstract class EMFRepositoryFacade
    implements RepositoryFacade
{
    /**
     * Stores the resources (i.e. models) loaded into EMF.
     */
    protected final ResourceSet resourceSet = new ResourceSetImpl();
    
    protected ModelAccessFacade modelFacade = null;

    /**
     * Stores the actual loaded model.
     */
    protected Resource model;

    /**
     * The options for loading the model.
     */
    private Map loadOptions = new HashMap();

    /**
     * Gets the current load options.
     *
     * @return the load options.
     */
    protected Map getLoadOptions()
    {
        return this.loadOptions;
    }

    /**
     * Reads the model with the given <code>uri</code> into the EModelElement
     * instance.
     *
     * @param uri the URI to the model
     * @return the model element instance.
     */
    protected void readModel(final String uri)
    {
        try
        {
           model = resourceSet.createResource(EMFRepositoryFacadeUtils.createUri(uri));
            if (model == null)
            {
                throw new RepositoryFacadeException("'" + uri + "' is an invalid model");
            }
            model.load(this.getLoadOptions());
            EcoreUtil.resolveAll(model);
        }
        catch (final Exception exception)
        {
            throw new RepositoryFacadeException(exception);
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public abstract void open();

    /**
     * @see org.andromda.core.repository.RepositoryFacade#close()
     */
    public void close()
    {
    }

    /**
     * The path to any modules found on the classpath.
     */
    private static final String MODULES_PATH = "META-INF/emf/modules";

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.lang.String[], java.lang.String[])
     */
    public void readModel(
        String[] modelUris,
        String[] moduleSearchPaths)
    {
        if (modelUris == null || modelUris.length == 0)
        {
            throw new RepositoryFacadeException("No model specified.");
        }
        final List moduleSearchPathList = new ArrayList();
        if (moduleSearchPaths != null)
        {
            moduleSearchPathList.addAll(Arrays.asList(moduleSearchPaths));
        }

        // - first add the default module search paths maps that are found on the classpath
        final URL[] classpathSearchPaths = ResourceFinder.findResources(MODULES_PATH);

        if (classpathSearchPaths != null)
        {
            final int numberOfClasspathSearchPaths = classpathSearchPaths.length;
            for (int ctr = 0; ctr < numberOfClasspathSearchPaths; ctr++)
            {
                final URL classpathSearchPath = classpathSearchPaths[ctr];
                if (classpathSearchPath != null)
                {
                    moduleSearchPathList.add(classpathSearchPath.toString());
                }
            }
        }
        resourceSet.setURIConverter(new EMFURIConverter(moduleSearchPathList));
        if (modelUris != null && modelUris.length > 0)
        {
            final int numberOfModelUris = modelUris.length;
            for (int ctr = 0; ctr < numberOfModelUris; ctr++)
            {
                this.readModel(modelUris[ctr]);
            }
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.io.InputStream[], java.lang.String[], java.lang.String[])
     */
    public void readModel(
        InputStream[] stream,
        String[] modelUri,
        String[] moduleSearchPaths)
    {
        this.readModel(
            modelUri,
            moduleSearchPaths);
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object, java.lang.String, java.lang.String, java.lang.String)
     */
    public void writeModel(
        Object model,
        String location,
        String version,
        String encoding)
    {
        this.writeModel(
            model,
            location,
            "");
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object, java.lang.String, java.lang.String)
     */
    public void writeModel(
        Object model,
        String location,
        String version)
    {
        final org.eclipse.emf.ecore.EModelElement element = (org.eclipse.emf.ecore.EModelElement)model;
        final Resource resource = element.eResource();
        final URI uri = URI.createURI(location);
        resource.setURI(uri);
        try
        {
            resource.save(null);
        }
        catch (IOException exception)
        {
            throw new RepositoryFacadeException("Could not save model", exception);
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#clear()
     */
    public void clear()
    {
        model = null;
    }
}