package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Util;


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

    public java.lang.String getStateMethodName()
    {
        return Util.toJavaMethodName(this);
    }

    // ------------- relations ------------------

}
