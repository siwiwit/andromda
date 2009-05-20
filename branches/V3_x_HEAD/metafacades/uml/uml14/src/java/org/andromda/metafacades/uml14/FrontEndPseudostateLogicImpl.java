package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndPseudostate.
 *
 * @see org.andromda.metafacades.uml.FrontEndPseudostate
 * @author Bob Fields
 */
public class FrontEndPseudostateLogicImpl
    extends FrontEndPseudostateLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndPseudostateLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndPseudostate#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof FrontEndActivityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndPseudostate#getContainerActions()
     */
    @Override
    protected List<FrontEndAction> handleGetContainerActions()
    {
        final Collection<FrontEndAction> actionSet = new LinkedHashSet<FrontEndAction>();
        final StateMachineFacade graphContext = getStateMachine();

        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade useCase = ((ActivityGraphFacade)graphContext).getUseCase();

            if (useCase instanceof FrontEndUseCase)
            {
                for (
                    final Iterator actionIterator = ((FrontEndUseCase)useCase).getActions().iterator();
                    actionIterator.hasNext();)
                {
                    final FrontEndAction action = (FrontEndAction)actionIterator.next();
                    final Collection<FrontEndForward> transitions = action.getTransitions();
                    for (final Iterator<FrontEndForward> transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                    {
                        TransitionFacade transition = (TransitionFacade)transitionIterator.next();
                        if (this.equals(transition.getTarget()))
                        {
                            actionSet.add(action);
                        }
                    }
                }
            }
        }
        return new ArrayList<FrontEndAction>(actionSet);
    }
}