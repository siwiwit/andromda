package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Util;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Guard
 *
 *
 */
public class StrutsGuardDecoratorImpl extends StrutsGuardDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsGuardDecoratorImpl(org.omg.uml.behavioralelements.statemachines.Guard metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsGuardDecorator ...

    public java.lang.String getGuardMethodName()
    {
        return Util.toJavaMethodName(this);
    }

    // ------------- relations ------------------

}
