package org.andromda.core.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.CompositeState;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.State
 *
 *
 */
public class StateDecoratorImpl extends StateDecorator
{
    // ---------------- constructor -------------------------------

    public StateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.State metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StateDecorator ...

    // ------------- relations ------------------

    /**
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetActivityGraph()
    {
        StateMachine stateMachine = null;
        CompositeState compositeState = metaObject.getContainer();

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

    
}
