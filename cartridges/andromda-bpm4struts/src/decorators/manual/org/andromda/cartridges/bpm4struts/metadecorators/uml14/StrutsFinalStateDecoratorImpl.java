package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.FinalState
 *
 *
 */
public class StrutsFinalStateDecoratorImpl extends StrutsFinalStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsFinalStateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.FinalState metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsFinalStateDecorator ...

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // this state may not be owned by a composite state
        if (getContainer() != null)
            throw new DecoratorValidationException(this, "This state must be owned by a state machine, not a composite state");

        // the name of this final state must correspond with a transition in a workflow, going out of the use-case
        // that is represented by its container state machine
        // todo
    }
}
