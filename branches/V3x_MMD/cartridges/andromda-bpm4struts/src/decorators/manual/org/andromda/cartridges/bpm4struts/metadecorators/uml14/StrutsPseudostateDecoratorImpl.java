package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Pseudostate
 *
 *
 */
public class StrutsPseudostateDecoratorImpl extends StrutsPseudostateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsPseudostateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsPseudostateDecorator ...

    public String getImplementationMethodName()
    {
        return MetaDecoratorUtil.toJavaMethodName(metaObject);
    }

    public String getAbstractMethodName()
    {
        return getImplementationMethodName() + "Abstract";
    }
    // ------------- relations ------------------

}
