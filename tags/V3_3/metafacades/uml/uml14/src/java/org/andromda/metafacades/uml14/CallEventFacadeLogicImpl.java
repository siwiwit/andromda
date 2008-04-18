package org.andromda.metafacades.uml14;

import java.util.Collections;
import java.util.List;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl
    extends CallEventFacadeLogic
{
    public CallEventFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.CallEvent metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    public java.lang.Object handleGetOperation()
    {
        return metaObject.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml14.CallEventFacade#getOperations()
     */
    protected List handleGetOperations()
    {
        final Object operation = this.getOperation();
        return operation == null ? Collections.EMPTY_LIST : Collections.singletonList(operation);
    }
}