package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.ModelElementDecorator;
import org.andromda.core.metadecorators.uml14.PseudostateDecorator;
import org.andromda.core.common.StringUtilsHelper;
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
        Event trigger = metaObject.getTrigger();
        return (trigger == null) ? metaObject.getTarget().getName() : trigger.getName();
    }

    public ModelElementDecorator getFinalTarget()
    {
        Transition transition = metaObject;
        StateVertex target = transition.getTarget();

        boolean isMergePoint = true;
        while ((target instanceof Pseudostate) && (isMergePoint))
        {
            PseudostateDecorator pseudostate = (PseudostateDecorator) decoratedElement(target);
            isMergePoint = pseudostate.isMergePoint();

            if (isMergePoint)
            {
                transition = (Transition) target.getOutgoing().iterator().next();
                target = transition.getTarget();
            }
        }

        return (ModelElementDecorator)decoratedElement(target);
    }

    // ------------- relations ------------------

    public String getForwardName()
    {
        return StringUtilsHelper.separate(getTriggerName(), ".").toLowerCase();
    }

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
