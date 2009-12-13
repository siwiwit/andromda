package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.OperationFacade;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.ActivityNode;
import org.eclipse.uml2.CallOperationAction;
import org.eclipse.uml2.Operation;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.CallEventFacade. UML1.4 Event is mapped to UML2
 * Activity (because UML2 Event doesn't contain parameters)
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl extends CallEventFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public CallEventFacadeLogicImpl(final Activity metaObject,
            final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    @Override
    protected OperationFacade handleGetOperation()
    {
        final List<OperationFacade> operations = this.getOperations();
        return operations.isEmpty() ? null : operations.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperations()
     */
    @Override
    public List<Operation> handleGetOperations()
    {
        // We get every operation from each CallOperationAction instance.
        final List<Operation> operations = new ArrayList();
        Collection<ActivityNode> nodes = this.metaObject.getNodes();
        for (final Iterator<ActivityNode> iterator = nodes.iterator(); iterator.hasNext();)
        {
            final Object nextNode = iterator.next();
            if (nextNode instanceof CallOperationAction)
            {
                final Operation operation = ((CallOperationAction)nextNode).getOperation();
                if (operation != null)
                {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

}