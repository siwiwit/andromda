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
    public String getFullPath()
    {
        final String name = getName();

        if (name != null)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                UseCaseFacade useCase = (UseCaseFacade) iterator.next();
                if (useCase instanceof StrutsUseCase)
                    return ((StrutsUseCase)useCase).getActionPath();
            }
        }

        return "";
    }
}
