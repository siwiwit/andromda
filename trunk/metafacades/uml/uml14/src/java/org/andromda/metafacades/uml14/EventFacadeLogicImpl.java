package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.Transition;

/**
 * MetafacadeLogic implementation.
 * 
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
    extends EventFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EventFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.Event metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    protected Object handleGetTransition()
    {
        Transition eventTransition = null;

        Collection allTransitions = UML14MetafacadeUtils.getModel()
            .getStateMachines().getTransition().refAllOfType();
        for (Iterator iterator = allTransitions.iterator(); iterator.hasNext()
            && eventTransition == null;)
        {
            Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getTrigger()))
            {
                eventTransition = transition;
            }
        }

        return eventTransition;
    }

    protected Object handleGetActionState()
    {
        ActionState eventState = null;

        Collection allActionStates = UML14MetafacadeUtils.getModel()
            .getActivityGraphs().getActionState().refAllOfType();
        for (Iterator iterator = allActionStates.iterator(); iterator.hasNext()
            && eventState == null;)
        {
            ActionState actionState = (ActionState)iterator.next();
            if (actionState.getDeferrableEvent().contains(metaObject))
            {
                eventState = actionState;
            }
        }

        return eventState;
    }

    public Object getValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getActionState();
        }

        return validationOwner;
    }

}
