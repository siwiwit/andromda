package org.andromda.core.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.CompositeState;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
 *
 *
 */
public class ObjectFlowStateDecoratorImpl extends ObjectFlowStateDecorator
{
    // ---------------- constructor -------------------------------

    public ObjectFlowStateDecoratorImpl (org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ObjectFlowStateDecorator ...

    // ------------- relations ------------------

   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetStateMachine()
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

    // ------------------------------------------------------------

}
