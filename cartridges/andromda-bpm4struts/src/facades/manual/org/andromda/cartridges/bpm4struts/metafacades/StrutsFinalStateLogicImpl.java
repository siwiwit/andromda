package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.UseCaseFacade;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 */
public class StrutsFinalStateLogicImpl
       extends StrutsFinalStateLogic
       implements org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
{
    // ---------------- constructor -------------------------------
    
    public StrutsFinalStateLogicImpl (java.lang.Object metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }
    // ------------- relations ------------------
    
	/**
	 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState#getTargetUseCase()
	 */
    public java.lang.Object handleGetTargetUseCase()
    {
        final String name = getName();

        if (name != null)
        {
            Collection allUseCases = getModel().getAllUseCases();
            for (Iterator iterator = allUseCases.iterator(); iterator.hasNext();)
            {
                UseCaseFacade useCaseFacade = (UseCaseFacade) iterator.next();
                if (name.equalsIgnoreCase(useCaseFacade.getName()))
                    return useCaseFacade;
            }
        }

        return null;
    }

}
