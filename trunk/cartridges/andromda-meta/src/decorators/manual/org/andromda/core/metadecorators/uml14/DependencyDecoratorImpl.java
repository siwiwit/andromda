package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.core.ModelElement;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Dependency
 *
 *
 */
public class DependencyDecoratorImpl extends DependencyDecorator
{
    // ---------------- constructor -------------------------------

    public DependencyDecoratorImpl(
        org.omg.uml.foundation.core.Dependency metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class DependencyDecorator ...

    public ModelElement handleGetTargetType()
    {
        return (ModelElement) metaObject.getSupplier().iterator().next();
    }

    // ------------- relations ------------------

}
