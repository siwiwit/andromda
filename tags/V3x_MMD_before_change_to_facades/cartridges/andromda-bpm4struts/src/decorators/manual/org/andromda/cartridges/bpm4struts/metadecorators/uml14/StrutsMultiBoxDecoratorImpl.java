package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class StrutsMultiBoxDecoratorImpl extends StrutsMultiBoxDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsMultiBoxDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsMultiBoxDecorator ...

    public String getFieldType()
    {
        return "multibox";
    }
    // ------------- relations ------------------

}
