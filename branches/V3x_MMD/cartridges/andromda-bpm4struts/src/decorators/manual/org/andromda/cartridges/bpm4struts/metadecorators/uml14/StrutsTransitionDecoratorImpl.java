package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;


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
        return metaObject.getGuard().getName();
    }

    public String getTriggerName()
    {
        return metaObject.getTrigger().getName();
    }

    public StateVertex getFinalTarget()
    {
        Transition transition = metaObject;
        StateVertex target = transition.getTarget();

        while ( MetaDecoratorUtil.isMergePoint(target) )
        {
            transition = (Transition)target.getOutgoing().iterator().next();
            target = transition.getTarget();
        }

        return target;
    }

    public Integer getGuardValue()
    {
        return new Integer(getGuardName().hashCode());
    }

    // ------------- relations ------------------

}
