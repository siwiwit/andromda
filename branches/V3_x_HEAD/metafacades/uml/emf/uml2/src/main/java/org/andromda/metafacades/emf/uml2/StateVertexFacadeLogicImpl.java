package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;

import org.eclipse.uml2.Element;
import org.eclipse.uml2.StateMachine;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.StateVertexFacade.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
    extends StateVertexFacadeLogic
{
    public StateVertexFacadeLogicImpl(
        final org.eclipse.uml2.Vertex metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getOutgoing()
     */
    protected java.util.Collection handleGetOutgoing()
    {
        ArrayList outList = new ArrayList();
        outList.addAll(this.metaObject.getOutgoings());
        return outList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getIncoming()
     */
    protected java.util.Collection handleGetIncoming()
    {
        ArrayList inList = new ArrayList();
        inList.addAll(this.metaObject.getIncomings());
        return inList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getContainer()
     */
    protected java.lang.Object handleGetContainer()
    {
        //TODO: What's this ?
        return this.metaObject.getContainer().getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getPartition()
     */
    protected java.lang.Object handleGetPartition()
    {
        return this.metaObject.getContainer();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getStateMachine()
     */
    protected java.lang.Object handleGetStateMachine()
    {
        Element owner = this.metaObject;
        while (!(owner instanceof StateMachine))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}