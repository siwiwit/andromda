package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class StrutsCheckBoxDecoratorImpl extends StrutsCheckBoxDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsCheckBoxDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------
    public boolean isResetField()
    {
        return true;
    }

    public String getWidgetType()
    {
        return "checkbox";
    }

    // concrete business methods that were declared
    // abstract in class StrutsCheckBoxDecorator ...

    // ------------- relations ------------------

}
