package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.StateMachine;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndActivityGraph.
 *
 * @see org.andromda.metafacades.uml.FrontEndActivityGraph
 * @author Bob Fields
 */
public class FrontEndActivityGraphLogicImpl
    extends FrontEndActivityGraphLogic
{
    private static final long serialVersionUID = -8207083324441641141L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActivityGraphLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getUseCase() instanceof FrontEndUseCase
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getUseCase() instanceof FrontEndUseCase;
    }

    /**
     * Retrieves the usecase that owns this activity.
     *
     * @see org.andromda.metafacades.emf.uml2.ActivityGraphFacadeLogic#handleGetUseCase()
     */
    protected Object handleGetUseCase()
    {
        Object useCase = super.handleGetUseCase();
        if (useCase == null)
        {
            useCase =
                this.getModel().findUseCaseWithTaggedValueOrHyperlink(
                    UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY,
                    this.getName());
        }
        return useCase;
    }

    /**
     * @return getInitialStates().getOutgoings()
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getInitialAction()
     */
    protected Object handleGetInitialAction()
    {
        Object firstAction = null;
        final Collection initialStates = this.getInitialStates();
        if (!initialStates.isEmpty())
        {
            final PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            final Collection outgoing = initialState.getOutgoings();
            firstAction = outgoing.isEmpty() ? null : outgoing.iterator().next();
        }
        return firstAction;
    }

    /**
     * @return getOwnedMembers() instanceof Class
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getController()
     */
    protected Object handleGetController()
    {
        // Take the first class inside the FSM
        Class controller = null;
        for (Iterator it = ((StateMachine)this.metaObject).getOwnedMembers().iterator();
            it.hasNext() && controller == null;)
        {
            Object next = it.next();
            if (next instanceof Class)
            {
                controller = (Class)next;
            }
        }
        return controller;
    }
}