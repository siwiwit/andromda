package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.StateVertex;

import java.util.Collection;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
       extends StateVertexFacadeLogic
       implements org.andromda.metafacades.uml.StateVertexFacade
{
    // ---------------- constructor -------------------------------
    
    public StateVertexFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.StateVertex metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetActivityGraph()
    {
        StateVertex stateVertex = (StateVertex)metaObject;
        return stateVertex.getContainer().getStateMachine();
    }

    protected Collection handleGetIncoming()
    {
        return ((StateVertex)metaObject).getIncoming();
    }

    protected Collection handleGetOutgoing()
    {
        return ((StateVertex)metaObject).getOutgoing();
    }
}
