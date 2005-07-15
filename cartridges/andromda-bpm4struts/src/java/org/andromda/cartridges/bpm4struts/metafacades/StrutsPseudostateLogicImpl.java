package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate
 */
public class StrutsPseudostateLogicImpl
    extends StrutsPseudostateLogic
{
    public StrutsPseudostateLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof StrutsActivityGraph;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate#getActionMethodName()()
     */
    protected java.lang.String handleGetActionMethodName()
    {
        final String methodName = getName();
        return (methodName == null) ?
            "a" + System.currentTimeMillis() : StringUtilsHelper.lowerCamelCaseName(methodName);
    }

    /**
     * Overrideen since StrutsAction does not extend FrontEndAction
     *
     * @see org.andromda.metafacades.uml.FrontEndPseudostate#getContainerActions()
     */
    public List getContainerActions()
    {
        final Collection actionSet = new HashSet();
        final StateMachineFacade graphContext = getStateMachine();

        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade useCase = ((ActivityGraphFacade)graphContext).getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                Collection actions = ((StrutsUseCase)useCase).getActions();
                for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    StrutsAction action = (StrutsAction)actionIterator.next();
                    Collection transitions = action.getTransitions();
                    for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
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
        return new ArrayList(actionSet);
    }
}
