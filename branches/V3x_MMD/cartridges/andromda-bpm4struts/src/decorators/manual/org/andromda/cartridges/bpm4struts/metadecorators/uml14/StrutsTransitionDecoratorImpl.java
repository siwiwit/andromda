package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.PseudostateDecoratorImpl;
import org.andromda.core.metadecorators.uml14.PseudostateDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
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

    public StrutsTransitionDecoratorImpl (org.omg.uml.behavioralelements.statemachines.Transition metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTransitionDecorator ...

    public String getGuardName()
    {
        return getGuard().getName();
    }

    public String getTriggerName()
    {
        return getTrigger().getName();
    }

    public StateVertex getFinalTarget()
    {
        Transition transition = this;
        StateVertex target = transition.getTarget();

        boolean isMergePoint = true;
        while ( (target instanceof Pseudostate) && (isMergePoint) )
        {
            PseudostateDecorator pseudostate = (PseudostateDecorator)DecoratorBase.decoratedElement(target);
            isMergePoint = pseudostate.isMergePoint().booleanValue();

            if (isMergePoint)
            {
                transition = (Transition)target.getOutgoing().iterator().next();
                target = transition.getTarget();
            }
        }

        return target;
    }

    public Integer getGuardValue()
    {
        return new Integer(getGuardName().hashCode());
    }

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // if outgoing from a choice pseudostate this transition must have a guard

        // if outgoing from an action state or object flow state this transition must have a trigger
        // (unless the container state machine is a workflow)
    }

}
