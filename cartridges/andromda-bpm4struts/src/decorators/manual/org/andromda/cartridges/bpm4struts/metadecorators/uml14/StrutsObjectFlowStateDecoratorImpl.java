package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.StateMachineDecorator;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
 *
 *
 */
public class StrutsObjectFlowStateDecoratorImpl extends StrutsObjectFlowStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsObjectFlowStateDecoratorImpl(org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsObjectFlowStateDecorator ...

    // ------------- relations ------------------

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name must be unique for object flow states in the use-case
        final StateMachineDecorator stateMachine = (StateMachineDecorator) getStateMachine();
        final Collection objectFlowStates = stateMachine.getObjectFlowStates();
        int nameCount = 0;
        for (Iterator iterator = objectFlowStates.iterator(); iterator.hasNext();)
        {
            ObjectFlowState objectFlowState = (ObjectFlowState) iterator.next();
            if (name.equals(objectFlowState.getName()))
                nameCount++;
        }

        if (nameCount > 1)
            throw new DecoratorValidationException(this,
                "There are " + nameCount + " object flow states found with this names, please give unique names");

        // there must be at least one incoming transition
        if (getIncoming().isEmpty())
            throw new DecoratorValidationException(this,
                "Miracle state coming out of nowhere. There must be at least one transition going into this object flow state");

        // there must be at least one outgoing transition
        if (getOutgoing().isEmpty())
            throw new DecoratorValidationException(this,
                "Black hole state: there must be one and only one transition going out of this object flow state, " +
                "you might consider using a final state.");

        // there must be only one outgoing transition
        if (getOutgoing().size() > 1)
            throw new DecoratorValidationException(this,
                "There must be one and only one transition going out of this object flow state, " +
                "you might consider using a final state.");
*/
    }
}
