package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class StateFacadeLogicImpl
       extends StateFacadeLogic
       implements org.andromda.metafacades.uml.StateFacade
{
    // ---------------- constructor -------------------------------
    
    public StateFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.State metaObject)
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
    public Object handleGetActivityGraph()
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
