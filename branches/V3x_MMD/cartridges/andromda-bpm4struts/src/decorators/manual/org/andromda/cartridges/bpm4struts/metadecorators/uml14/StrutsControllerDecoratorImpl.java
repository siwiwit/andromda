package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Util;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsControllerDecoratorImpl extends StrutsControllerDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsControllerDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsControllerDecorator ...

    public java.lang.String getControllerClassName()
    {
        return Util.toJavaClassName(this);
    }

    public java.lang.String getControllerName()
    {
        String actionPath = Util.findTagValue(this, Util.TAG_ACTION_PATH);
        if (actionPath == null)
        {
            actionPath = '/' + Util.toJavaMethodName(this);
        }
        return actionPath;
    }

    // ------------- relations ------------------

}
