package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.omg.uml.behavioralelements.statemachines.Transition;


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
        return MetaDecoratorUtil.toJavaMethodName(metaObject);
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

}
