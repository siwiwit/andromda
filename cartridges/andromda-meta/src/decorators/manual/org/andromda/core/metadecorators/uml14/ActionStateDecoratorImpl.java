package org.andromda.core.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ActionState
 *
 *
 */
public class ActionStateDecoratorImpl extends ActionStateDecorator
{
    // ---------------- constructor -------------------------------

    public ActionStateDecoratorImpl(org.omg.uml.behavioralelements.activitygraphs.ActionState metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ActionStateDecorator ...

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
