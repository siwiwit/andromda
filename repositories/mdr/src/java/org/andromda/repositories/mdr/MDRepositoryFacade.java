package org.andromda.repositories.mdr;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;

/**
 * Implements an AndroMDA object model repository by using the <a
 * href="http://mdr.netbeans.org">NetBeans MetaDataRepository </a>.
 * 
 * @author <A HREF="httplo://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class MDRepositoryFacade implements RepositoryFacade
{	
	private static Logger logger = Logger.getLogger(MDRepositoryFacade.class);
	
    protected final static String META_PACKAGE = "UML";
    
    private ModelAccessFacade modelFacade = null;

    static {
        // configure MDR to use an in-memory storage implementation
        System.setProperty(
            "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
            "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
        // set the logging so output does not go to standard out
        System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");
    }

    protected URL metaModelURL;
    protected URL modelURL;
    protected RefPackage model;

    /**
     * Constructs a Facade around a netbeans MDR (MetaDataRepository).
     */
    public MDRepositoryFacade()
    {
        String metamodelUri = "/M2_DiagramInterchangeModel.xml";
        // the default metamodel is now UML 1.4 plus UML 2.0 diagram extensions
        metaModelURL =
            MDRepositoryFacade.class.getResource(metamodelUri);

        if (metaModelURL == null) {
            throw new RepositoryFacadeException("Could not find meta model --> ' " 
                + metamodelUri + "'");   
        }
        
        modelURL = null;
        model = null;
    }

    /**
     * Opens the repository and prepares it to read in models.
     * <p>
     * All the file reads are done within the context of a transaction this
     * seems to speed up the processing.
     * </p>
     * 
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
        MDRManager.getDefault().getDefaultRepository().beginTrans(true);
    }

    /**
     * Closes the repository and reclaims all resources.
     * <p>
     * This should only be called after all the models has been read and all
     * querys have completed.
     * </p>
     * 
     * @see org.andromda.core.repository.RepositoryFacade#close()
     */
    public void close()
    {
        MDRManager.getDefault().getDefaultRepository().endTrans(true);
        this.model = null;
    }

    /**
     * @see org.andromda.core.common.RepositoryFacade#readModel(java.net.URL,
     *      java.lang.String[])
     */
    public void readModel(URL modelURL, String[] moduleSearchPath)
    {
        final String methodName = "MDRepositoryFacade.readModel";
        ExceptionUtils.checkNull(methodName, "modelURL", modelURL);
        
    	if (logger.isDebugEnabled())
    		logger.debug("creating repository");

        this.modelURL = modelURL;

        MDRepository repository =
            MDRManager.getDefault().getDefaultRepository();

        try
        {
            MofPackage metaModel = loadMetaModel(metaModelURL, repository);

            this.model = loadModel(modelURL, moduleSearchPath, metaModel, repository);
        }
        catch (Throwable th) 
        {
            String errMsg = "Error performing " + methodName;
            throw new RepositoryFacadeException(errMsg, th);
        }

        if (logger.isDebugEnabled())
        	logger.debug("created repository");
    }
    
    /**
     * The default XMI version if none is specified.
     */
    private static final String DEFAULT_XMI_VERSION = "1.2";
    
    /**
     * The default encoding if none is specified
     */
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object,
     *      java.lang.String, java.lang.String)
     */
    public void writeModel(Object model, String outputLocation, String xmiVersion)
    {
        this.writeModel(model, outputLocation, xmiVersion, null);
    }
     
    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object,
     *      java.lang.String, java.lang.String)
     */
    public void writeModel(Object model, String outputLocation, String xmiVersion, String encoding)
    {
        final String methodName = "MDRepositoryFacade.writeMode";
        ExceptionUtils.checkNull(methodName, "model", model);
        ExceptionUtils.checkNull(methodName, "outputLocation", outputLocation);
        ExceptionUtils.checkAssignable(methodName, RefPackage.class, "model", model.getClass());
        if (StringUtils.isEmpty(xmiVersion)) 
        {
            xmiVersion = DEFAULT_XMI_VERSION;
        }
        if (StringUtils.isEmpty(encoding))
        {
            encoding = DEFAULT_ENCODING;
        }
        try  
        {          
	        FileOutputStream outputStream = new FileOutputStream(outputLocation);
	        XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter();
	        xmiWriter.getConfiguration().setEncoding("UTF-8");	        
	        xmiWriter.write(outputStream, outputLocation, (RefPackage)model, xmiVersion); 
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing MDRepositoryFacade.writeModel";
            logger.error(errMsg, th);
            throw new RepositoryFacadeException(errMsg, th);
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#getLastModified()
     */
    public long getLastModified()
    {
        long lastModified = 0;

        try
        {
            URLConnection urlConnection = modelURL.openConnection();
            lastModified = urlConnection.getLastModified();
        }
        catch (IOException ioe)
        {
            // log and then eat the exception
            logger.error(ioe);
        }

        return lastModified;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#getModel()
     */
    public ModelAccessFacade getModel()
    {
        if (this.modelFacade == null) 
        {
            try 
            {    
            	this.modelFacade = 
                    (ModelAccessFacade)
        	        ComponentContainer.instance().findComponent(
        	            ModelAccessFacade.class);
                if (this.modelFacade == null) {
                    throw new RepositoryFacadeException(
                        "Could not find implementation for the component --> '" 
                        + ModelAccessFacade.class + "'");
                }
            	this.modelFacade.setModel(this.model);
            } 
            catch (Throwable th) 
            {
            	String errMsg = "Error performing MDRepositoryFacade.getModel";
                logger.error(errMsg, th);
                throw new RepositoryFacadeException(errMsg, th);
            }
        }
        return this.modelFacade;
    }

    /**
     * Loads a metamodel into the repository.
     * 
     * @param repository MetaDataRepository
     * @return MofPackage for newly loaded metamodel
     * @exception CreationFailedException
     * @exception IOException
     * @exception MalformedXMIException
     */
    private static MofPackage loadMetaModel(
        URL metaModelURL,
        MDRepository repository)
        throws CreationFailedException, IOException, MalformedXMIException
    {
    	if (logger.isDebugEnabled()) 
    		logger.debug("creating MetaModel using URL --> '" + metaModelURL + "'");

        // Use the metaModelURL as the name for the repository extent.
        // This ensures we can load mutiple metamodels without them colliding.
        ModelPackage metaModelExtent =
            (ModelPackage) repository.getExtent(metaModelURL.toExternalForm());

        if (metaModelExtent == null)
        {
            metaModelExtent =
                (ModelPackage) repository.createExtent(
                    metaModelURL.toExternalForm());
        }

        MofPackage metaModelPackage =
            findPackage(META_PACKAGE, metaModelExtent);

        if (metaModelPackage == null)
        {
            XMIReader xmiReader =
                XMIReaderFactory.getDefault().createXMIReader();
            xmiReader.read(metaModelURL.toExternalForm(),metaModelExtent);

            // locate the UML package definition that was just loaded in
            metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
        }

        if (logger.isDebugEnabled())
        	logger.debug("created MetaModel");
        return metaModelPackage;
    }

    /**
     * Loads a model into the repository and validates the model against the
     * given metaModel.
     * 
     * @param modelURL url of model
     * @param repository netbeans MDR
     * @param metaModel meta model of model
     * @return populated model
     * @exception CreationFailedException unable to create model in repository
     */
    private static RefPackage loadModel(
        URL modelURL,
        String[] moduleSearchPath,
        MofPackage metaModel,
        MDRepository repository)
        throws CreationFailedException
    {
    	if (logger.isDebugEnabled())
    		logger.debug("creating model");
    	
        RefPackage model = repository.getExtent("MODEL");
        if (model != null)
        {
        	if (logger.isDebugEnabled())
        		logger.debug("deleting existing model");
            model.refDelete();
        }

        if (logger.isDebugEnabled())
        	logger.debug("creating model extent");
        
        model = repository.createExtent("MODEL", metaModel);
        
        if (logger.isDebugEnabled()) 
        	logger.debug("created model extent");

        XMIReader xmiReader =
        	XMIReaderFactory.getDefault().createXMIReader(
        			new MDRXmiReferenceResolver(
        					new RefPackage[] { model }, moduleSearchPath));

        if (logger.isDebugEnabled())
        	logger.debug("reading model XMI --> '" + modelURL + "'");

        try
        {
            xmiReader.read(modelURL.toExternalForm(), model);
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing MDRepository.loadModel";
            logger.error(errMsg, th);
            throw new RepositoryFacadeException(errMsg, th);
        }
        if (logger.isDebugEnabled()) 
        	logger.debug("read XMI and created model");
        return model;
    }

    /**
     * Searches a meta model for the specified package.
     * 
     * @param packageName name of package for which to search
     * @param metaModel meta model to search
     * @return MofPackage
     */
    private static MofPackage findPackage(
        String packageName,
        ModelPackage metaModel)
    {
        for (Iterator it = metaModel.getMofPackage().refAllOfClass().iterator();
            it.hasNext();
            )
        {
            javax.jmi.model.ModelElement temp =
                (javax.jmi.model.ModelElement) it.next();

            if (temp.getName().equals(packageName))
            {
                return (MofPackage) temp;
            }
        }

        return null;
    }
}
