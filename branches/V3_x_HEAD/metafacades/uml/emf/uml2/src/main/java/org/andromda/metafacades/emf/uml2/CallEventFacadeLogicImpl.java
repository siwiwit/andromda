package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.uml2.CallOperationAction;
import org.eclipse.uml2.Operation;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.CallEventFacade. UML1.4 Event are mapped to UML2
 * Activity (because UML2 Event doesn't contain parameter)
 * 
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl extends CallEventFacadeLogic
{

    public CallEventFacadeLogicImpl(final org.eclipse.uml2.Activity metaObject,
            final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    protected java.lang.Object handleGetOperation()
    {
        // To find the called operation of this activity
        // We return the operation of the first CallOperationAction node in it.

        Collection nodes = this.metaObject.getNodes();
        Operation calledOperation = null;
        for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext()
                && calledOperation == null;)
        {
            Object nextNode = nodesIt.next();
            if (nextNode instanceof CallOperationAction)
            {
                CallOperationAction callOperationAction = (CallOperationAction)nextNode;
                calledOperation = callOperationAction.getOperation();
            }
        }
        return calledOperation;
    }

}