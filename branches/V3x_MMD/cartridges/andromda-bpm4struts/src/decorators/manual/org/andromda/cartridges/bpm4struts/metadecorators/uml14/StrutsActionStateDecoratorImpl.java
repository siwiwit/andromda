package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.StateMachineDecorator;
import org.andromda.core.metadecorators.uml14.StateMachineDecoratorImpl;
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
    public String getDispatchMethodName()
    {
        return MetaDecoratorUtil.toJavaMethodName(metaObject.getName());
    }

    public Integer getTriggerTransitionCount()
    {
        return new Integer(metaObject.getOutgoing().size());
    }

    public Transition getFirstTriggerTransition()
    {
        return (getTriggerTransitionCount().intValue() > 0)
            ? (Transition)metaObject.getOutgoing().iterator().next() : null;
    }

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // this state may not be owned by a composite state
        if (metaObject.getContainer() != null)
            throw new DecoratorValidationException(metaObject, "This state must be owned by a state machine, not a composite state");

        // the name must not be empty
        final String name = metaObject.getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(metaObject, "Name may not be empty or only contain whitespace");

        // the name of the action state must be unique in the use-case state machine
        final StateMachineDecorator stateMachine = new StateMachineDecoratorImpl(metaObject.getStateMachine());
        Collection actionStates = stateMachine.getActionStates();
        actionStates.remove(metaObject);
        // check that we don't accidentally generate the same names from more action states
        final String dispatchMethodName = getDispatchMethodName();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            ActionState actionState = (ActionState) iterator.next();
            if (dispatchMethodName.equals(MetaDecoratorUtil.toJavaMethodName(actionState.getName())))
                throw new DecoratorValidationException(metaObject, "There is another action state in the same state machine which generates a name clash with this one, please change one of the action state\'s names");
        }

        // there must be at least one incoming transition
        if (metaObject.getIncoming().isEmpty())
            throw new DecoratorValidationException(metaObject, "There must be at least one transition going into this action state");

        // there must be at least one outgoing transition
        if (metaObject.getOutgoing().isEmpty())
            throw new DecoratorValidationException(metaObject, "There must be at least one transition going out of this action state");
    }
}
