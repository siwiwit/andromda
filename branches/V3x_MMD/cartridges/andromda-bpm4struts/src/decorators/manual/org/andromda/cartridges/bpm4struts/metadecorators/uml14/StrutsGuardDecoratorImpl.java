package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;


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

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");
    }
}
