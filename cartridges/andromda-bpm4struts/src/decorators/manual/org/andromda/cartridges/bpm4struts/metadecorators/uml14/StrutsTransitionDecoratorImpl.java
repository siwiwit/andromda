package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.PseudostateDecorator;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Transition
 *
 *
 */
public class StrutsTransitionDecoratorImpl extends StrutsTransitionDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsTransitionDecoratorImpl(org.omg.uml.behavioralelements.statemachines.Transition metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTransitionDecorator ...

    public String getTriggerName()
    {
        String triggerName = null;
        Event trigger = getTrigger();

        if (trigger != null)
        {
            triggerName = trigger.getName();
        }

        if (triggerName == null)
        {
            triggerName = getTarget().getName();
        }

        return triggerName;
    }

    public StateVertex getFinalTarget()
    {
        Transition transition = this;
        StateVertex target = transition.getTarget();

        boolean isMergePoint = true;
        while ((target instanceof Pseudostate) && (isMergePoint))
        {
            PseudostateDecorator pseudostate = (PseudostateDecorator) DecoratorBase.decoratedElement(target);
            isMergePoint = pseudostate.isMergePoint().booleanValue();

            if (isMergePoint)
            {
                transition = (Transition) target.getOutgoing().iterator().next();
                target = transition.getTarget();
            }
        }

        return target;//(StateVertex)DecoratorBase.decoratedElement(target);
    }

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
/*
        // if outgoing from a choice pseudostate this transition must have a guard
        StateVertex source = getSource();
        if (source instanceof PseudostateDecorator)
        {
            PseudostateDecorator sourceDecorator = (PseudostateDecorator)source;
            if (sourceDecorator.isChoice().booleanValue())
                if (getGuard() == null)
                    throw new DecoratorValidationException(this,
                        "Transitions going out of a choice pseudostate (decision point) must have a guard");
        }
        else if (source instanceof ActionState)
        {
            if ( source.getOutgoing().size() > 1 && getTrigger() == null )
                throw new DecoratorValidationException(this,
                    "If there are two transitions or more going out of an action state they must each have a trigger");
        }
*/
    }

}
