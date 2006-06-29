package org.andromda.metafacades.emf.uml2;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.UseCaseFacade.
 *
 * @see org.andromda.metafacades.uml.UseCaseFacade
 */
public class UseCaseFacadeLogicImpl
    extends UseCaseFacadeLogic
{
    public UseCaseFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getFirstActivityGraph()
     */
    protected java.lang.Object handleGetFirstActivityGraph()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtensionPoints()
     */
    protected java.util.Collection handleGetExtensionPoints()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtends()
     */
    protected java.util.Collection handleGetExtends()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getIncludes()
     */
    protected Collection handleGetIncludes() 
    {
    		// TODO Auto-generated method stub
    		return null;
    	}
}