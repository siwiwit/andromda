package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.omg.uml.behavioralelements.statemachines.CallEvent;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
 */
public class StrutsActionStateLogicImpl
        extends StrutsActionStateLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
{
    // ---------------- constructor -------------------------------
    
    public StrutsActionStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionState ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getActionMethodName()()
     */
    public java.lang.String getActionMethodName()
    {
        return StringUtilsHelper.toJavaMethodName(getName());
    }

    // ------------- relations ------------------

    public Collection handleGetControllerCalls()
    {
        final Collection controllerCalls = new LinkedList();
        final Collection deferrableEvents = getDeferrableEvents();
        for (Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();)
        {
            EventFacade event = (EventFacade) iterator.next();
            if (event instanceof CallEvent)
            {
                controllerCalls.add(((CallEvent)event).getOperation());
            }
        }
        return controllerCalls;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getForward()
     */
    public java.lang.Object handleGetForward()
    {
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (! (transition instanceof StrutsExceptionHandler))
                return transition;
        }
        return null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getExceptions()
     */
    public java.util.Collection handleGetExceptions()
    {
        final Map exceptionsMap = new HashMap(4);
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (transition instanceof StrutsExceptionHandler)
            {
                exceptionsMap.put( ((StrutsExceptionHandler)transition).getExceptionKey(), transition);
            }
        }
        return exceptionsMap.values();
    }

}
