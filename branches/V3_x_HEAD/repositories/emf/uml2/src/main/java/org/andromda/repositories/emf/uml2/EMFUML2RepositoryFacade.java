package org.andromda.repositories.emf.uml2;

import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.andromda.metafacades.emf.uml2.UMLModelAccessFacade;
import org.andromda.repositories.emf.EMFRepositoryFacade;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.UML2Package;
import org.eclipse.uml2.util.UML2Resource;


/**
 * Implements an AndroMDA object model repository by using the <a href="http://www.eclipse.org/uml2/">Eclipse
 * UML2 API set </a>.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 */
public class EMFUML2RepositoryFacade
    extends EMFRepositoryFacade
{
    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(EMFUML2RepositoryFacade.class);

    /**
     * Perform required registrations for EMF/UML2.
     *
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Registering resource factories");
        }
        final Map extensionToFactoryMap = this.resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();

        // - we need to perform these registrations in order to load a UML2 model into EMF 
        //   see: http://dev.eclipse.org/viewcvs/indextools.cgi/%7Echeckout%7E/uml2-home/faq.html#6
        this.resourceSet.getPackageRegistry().put(
            UML2Package.eNS_URI,
            UML2Package.eINSTANCE);
        extensionToFactoryMap.put(
            Resource.Factory.Registry.DEFAULT_EXTENSION,
            UML2Resource.Factory.INSTANCE);

        // - populate the load options
        final Map loadOptions = this.getLoadOptions();
        loadOptions.put(
            UML2Resource.OPTION_DISABLE_NOTIFY,
            Boolean.FALSE);
        loadOptions.put(
            XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,
            Boolean.TRUE);
    }
    
    /**
     * Overrridden to check that the model is of the correct type.
     * 
     * @see org.andromda.repositories.emf.EMFRepositoryFacade#readModel(java.lang.String)
     */
    protected void readModel(final String uri)
    {
        super.readModel(uri);
        // Just to be sure there is a valid "model" inside
        EObject modelPackage =
            (EObject)EcoreUtil.getObjectByType(
            		model.getContents(),
                EcorePackage.eINSTANCE.getEObject());
        if (!(modelPackage instanceof Model))
        {
            throw new RepositoryFacadeException("Model '" + uri + "' is not a valid EMF UML2 model");
        }
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
                    (ModelAccessFacade)ComponentContainer.instance().newComponent(
                        UMLModelAccessFacade.class,
                        ModelAccessFacade.class);
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
        }
        if (this.model != null)
        {
            this.modelFacade.setModel(this.model);
        }
        else
        {
            this.modelFacade = null;
        }
        return this.modelFacade;
    }
}