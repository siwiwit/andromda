package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.TransitionFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate
 */
public class StrutsPseudostateLogicImpl
    extends StrutsPseudostateLogic
{
    // ---------------- constructor -------------------------------

    public StrutsPseudostateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsPseudostate ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate#getActionMethodName()()
     */
    protected java.lang.String handleGetActionMethodName()
    {
        final String methodName = getName();
        return (methodName == null) ? "a" + System.currentTimeMillis() : StringUtilsHelper.lowerCamelCaseName(methodName);
    }

    protected Collection handleGetContainerActions()
    {
        Collection actionSet = new HashSet();
        ActivityGraphFacade activityGraphFacade = this.getActivityGraph();

        if (activityGraphFacade instanceof StrutsActivityGraph)
        {
            StrutsActivityGraph activityGraph = (StrutsActivityGraph) activityGraphFacade;
            UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                Collection actions = ((StrutsUseCase)useCase).getActions();
                for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    StrutsAction action = (StrutsAction) actionIterator.next();
                    Collection transitions = action.getTransitions();
                    for (Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                    {
                        TransitionFacade transition = (TransitionFacade) transitionIterator.next();
                        if (this.equals(transition.getTarget()))
                        {
                            actionSet.add(action);
                        }
                    }
                }
            }
        }
        return actionSet;
    }
}
