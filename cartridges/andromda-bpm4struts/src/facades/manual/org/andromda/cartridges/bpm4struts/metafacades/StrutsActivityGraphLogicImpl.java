package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.PseudostateFacade;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 */
public class StrutsActivityGraphLogicImpl
        extends StrutsActivityGraphLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
{
    // ---------------- constructor -------------------------------
    
    public StrutsActivityGraphLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph#getInitialAction()
     */
    protected java.lang.Object handleGetInitialAction()
    {
        Collection pseudostates = getPseudostates();
        for (Iterator iterator = pseudostates.iterator(); iterator.hasNext();)
        {
            PseudostateFacade pseudostate = (PseudostateFacade) iterator.next();
            if (pseudostate.isInitialState())
            {
                return pseudostate.getOutgoing().iterator().next();
            }
        }
        return null;
    }

    protected Object handleGetUseCase()
    {
        Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsUseCase)
            {
                StrutsUseCase useCase = (StrutsUseCase)obj;
                if (useCase.getOwnedElements().contains(this))
                {
                    return useCase;
                }
            }
        }
        return null;
    }
}
