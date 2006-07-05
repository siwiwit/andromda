package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.TransitionFacade;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.CallOperationAction;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.Transition;
import org.eclipse.uml2.UseCase;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{
    public FrontEndEventLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // Be careful. Should return true only when it has an owning transition
        // contained in frontend usecase
        // from UML1.4: return this.getTransition() instanceof FrontEndForward;
        // Causes stack overflow...
        Element owner = (Element)this.metaObject;
        if (!(owner.getOwner() instanceof Transition))
        {
            return false;
        }
        while (owner != null)
        {
            if (owner instanceof UseCase)
            {
                if (this.shieldedElement(owner) instanceof FrontEndUseCase)
                {
                    return true;
                }
            }
            owner = owner.getOwner();
        }
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    protected Object handleGetControllerCall()
    {
        // To find the called operation of this activity
        // We return the operation of the first CallOperationAction node in it.
        // Note: It's the same implementation than CallEvent.getOperationCall()
        Activity activity = (Activity)this.metaObject;
        Collection nodes = activity.getNodes();
        Operation calledOperation = null;
        for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext() && calledOperation == null;)
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

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    protected Object handleGetAction()
    {
        FrontEndAction action = null;
        TransitionFacade transition = this.getTransition();
        if (transition instanceof FrontEndAction)
        {
            action = (FrontEndAction)transition;
        }
        return action;
    }
}