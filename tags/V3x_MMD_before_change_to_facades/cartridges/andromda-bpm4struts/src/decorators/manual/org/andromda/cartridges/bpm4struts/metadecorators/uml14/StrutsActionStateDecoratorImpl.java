package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.StateMachineDecorator;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ActionState
 *
 *
 */
public class StrutsActionStateDecoratorImpl extends StrutsActionStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsActionStateDecoratorImpl(org.omg.uml.behavioralelements.activitygraphs.ActionState metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionStateDecorator ...

    // ------------- relations ------------------
    protected Collection handleGetTriggerTransitions()
    {
        return getOutgoing();
    }

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name of the action state must be unique in the use-case state machine
        final StateMachineDecorator stateMachine = (StateMachineDecorator) DecoratorBase.decoratedElement(getContainer().getStateMachine());

        final Collection actionStates = stateMachine.getActionStates();
        int nameCount = 0;
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            ActionState actionState = (ActionState) iterator.next();
            if (name.equals(actionState.getName()))
                nameCount++;
        }

        if (nameCount > 1)
            throw new DecoratorValidationException(this,
                "There are " + nameCount + " action states found with this names, please give unique names");

        // there must be at least one incoming transition
        if (getIncoming().isEmpty())
            throw new DecoratorValidationException(this,
                "Miracle action coming out of nowhere. There must be at least one transition going into this action state");

        // there must be at least one outgoing transition
        if (getOutgoing().isEmpty())
            throw new DecoratorValidationException(this,
                "Black hole action: there must be at least one transition going out of this action state, " +
                "you might consider using a final state.");

        // if more than one outgoing transition, they must all have triggers
        Collection outgoing = getOutgoing();
        if (outgoing.size() > 1)
        {
            for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
            {
                Transition transition = (Transition) iterator.next();
                if (transition.getTrigger() == null)
                    throw new DecoratorValidationException(this,
                        "If an action state has more than 1 outgoing transition, they must all have triggers.");
            }
        }
*/
    }
}
