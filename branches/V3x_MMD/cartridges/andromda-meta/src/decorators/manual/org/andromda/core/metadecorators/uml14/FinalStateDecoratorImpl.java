package org.andromda.core.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.CompositeState;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.FinalState
 *
 *
 */
public class FinalStateDecoratorImpl extends FinalStateDecorator
{
    // ---------------- constructor -------------------------------

    public FinalStateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.FinalState metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class FinalStateDecorator ...

    // ------------- relations ------------------

   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetStateMachine()
    {
        StateMachine stateMachine = null;
        CompositeState compositeState = getContainer();

        if (compositeState != null)
        {
            while (compositeState != null)
            {
                stateMachine = compositeState.getStateMachine();
                compositeState = compositeState.getContainer();
            }
        }
        else
        {
            stateMachine = compositeState.getStateMachine();
        }

        return stateMachine;
    }

    // ------------------------------------------------------------

}
