package org.andromda.core.common;

import java.util.Collection;

import org.andromda.core.metafacade.*;
import org.andromda.core.repository.RepositoryFacade;

/**
 * Conext passed from the core to a cartridge
 * when code has to be generated.
 * 
 * @since 28.07.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 *
 */
public class CodeGenerationContext
{
    private RepositoryFacade repository = null;
    private MetafacadeModel modelFacade = null;
    private boolean lastModifiedCheck = false;
    private Collection userProperties = null;
    private ModelPackages modelPackages;

    public CodeGenerationContext(
        RepositoryFacade rf,
        MetafacadeModel mf,
        boolean lastModifiedCheck,
		ModelPackages modelPackages,
		Collection userPropeties)
    {
        this.repository = rf;
        this.modelFacade = mf;
        this.lastModifiedCheck = lastModifiedCheck;
        this.modelPackages = modelPackages;
        this.userProperties = userPropeties;
    }

    /**
     * Returns the repository.
     * @return RepositoryFacade
     */
    public RepositoryFacade getRepository()
    {
        return repository;
    }

    /**
     * Sets the repository.
     * @param repository The repository to set
     */
    public void setRepository(RepositoryFacade repository)
    {
        this.repository = repository;
    }

    /**
     * Returns the model facade for this code generation step.
     * @return the model facade
     */
    public MetafacadeModel getModelFacade()
    {
        return modelFacade;
    }

    /**
     * Returns the lastModifiedCheck.
     * @return boolean
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * Sets the lastModifiedCheck.
     * @param lastModifiedCheck The lastModifiedCheck to set
     */
    public void setLastModifiedCheck(boolean lastModifiedCheck)
    {
        this.lastModifiedCheck = lastModifiedCheck;
    }

    /**
     * Returns the userProperties.
     * @return Collection
     */
    public Collection getUserProperties()
    {
        return userProperties;
    }

    /**
     * Sets the userProperties.
     * @param userProperties The userProperties to set
     */
    public void setUserProperties(Collection userProperties)
    {
        this.userProperties = userProperties;
    }
    
    
	/**
	 * Gets the model packages that should/shouldn't
	 * be processed.  
	 * 
	 * @return Returns the modelPackages.
	 */
	public ModelPackages getModelPackages() 
	{
		return this.modelPackages;
	}

	/**
	 * Sets modelPackages.
	 * 
	 * @param modelPackages The modelPackages to set.
	 */
	public void setModelPackages(ModelPackages modelPackages) 
	{
		this.modelPackages = modelPackages;
	}

}
