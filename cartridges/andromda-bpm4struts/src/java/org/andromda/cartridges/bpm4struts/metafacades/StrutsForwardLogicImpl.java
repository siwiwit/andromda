package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
 */
public class StrutsForwardLogicImpl
        extends StrutsForwardLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
{
    // ---------------- constructor -------------------------------

    public StrutsForwardLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsForward ...

    protected String handleGetGuardName()
    {
        final GuardFacade guard = getGuard();
        return (guard == null) ? null : guard.getName();
    }

    protected boolean handleIsTargettingActionState()
    {
        return getTarget() instanceof StrutsActionState;
    }

    protected boolean handleIsTargettingFinalState()
    {
        return getTarget() instanceof StrutsFinalState;
    }

    protected boolean handleIsTargettingDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade) target).isDecisionPoint();
    }

    protected boolean handleIsTargettingPage()
    {
        return getTarget() instanceof StrutsJsp;
    }

    protected java.lang.String handleGetForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(resolveName());
    }

    protected java.lang.String handleGetForwardPath()
    {
        final StateVertexFacade target = getTarget();
        if (isTargettingPage())
        {
            return ((StrutsJsp) target).getFullPath() + ".jsp";
        }
        else if (isTargettingFinalState())
        {
            return ((StrutsFinalState) target).getFullPath() + ".do";
        }
        else
            return null;
    }

    protected String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(resolveName());
    }

    protected String handleGetTargetNameKey()
    {
        if (isTargettingPage())
        {
            return ((StrutsJsp) getTarget()).getTitleKey();
        }
        else if (isTargettingFinalState())
        {
            return ((StrutsFinalState) getTarget()).getTargetUseCase().getTitleKey();
        }
        return null;
    }

    private String resolveName()
    {
        String forwardName = null;
        //trigger
        final EventFacade trigger = getTrigger();
        if (trigger != null) forwardName = trigger.getName();
        //name
        if (forwardName == null) forwardName = getName();
        //target
        if (forwardName == null) forwardName = getTarget().getName();
        // else
        if (forwardName == null) forwardName = "unknown";
        // return
        return forwardName;
    }

    protected boolean handleIsExitingPage()
    {
        return getSource() instanceof StrutsJsp;
    }

    protected boolean handleIsSuccessMessagesPresent()
    {
        return getSuccessMessages().isEmpty() == false;
    }

    protected boolean handleIsWarningMessagesPresent()
    {
        return getWarningMessages().isEmpty() == false;
    }

    protected String handleGetMessageKey()
    {
        String messageKey = getStrutsActivityGraph().getUseCase().getName();
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    private Map getMessages(String messageKey, String taggedValue)
    {
        Collection taggedValues = findTaggedValues(taggedValue);

        Map messages = new LinkedHashMap();

        for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String value = (String) iterator.next();
            messages.put(messageKey + value.hashCode(), value);
        }

        return messages;
    }

    protected Map handleGetSuccessMessages()
    {
        return getMessages(getMessageKey() + ".success.", Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
    }

    protected Map handleGetWarningMessages()
    {
        return getMessages(getMessageKey() + ".warning.", Bpm4StrutsProfile.TAGGED_VALUE_ACTION_WARNING_MESSAGE);
    }

    protected java.util.Collection handleGetForwardParameters()
    {
        final EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetDecisionTrigger()
    {
        return getTrigger();
    }

    protected Object handleGetStrutsActivityGraph()
    {
        Object graph = getSource().getActivityGraph();
        return (graph instanceof StrutsActivityGraph) ? graph : null;
    }

    protected Collection handleGetActions()
    {
        Collection actions = null;

        if (this instanceof StrutsAction) // @todo this is not so nice because StrutsAction extends StrutsForward, solution would be to override in StrutsAction
        {
            actions = Collections.singletonList(this);
        }
        else
        {
            StateVertexFacade vertex = getSource();
            if (vertex instanceof StrutsJsp)
            {
                StrutsJsp jsp = (StrutsJsp)vertex;
                actions = jsp.getActions();
            }
            else if (vertex instanceof StrutsActionState)
            {
                StrutsActionState actionState = (StrutsActionState)vertex;
                actions = actionState.getContainerActions();
            }
            else if (vertex instanceof PseudostateFacade)
            {
                PseudostateFacade pseudostate = (PseudostateFacade)vertex;
                if (pseudostate.isInitialState())
                {
                    actions = Collections.EMPTY_LIST;
                }
                else
                {
                    actions = new ArrayList();
                    Collection incomingForwards = pseudostate.getIncoming();
                    for (Iterator forwardIterator = incomingForwards.iterator(); forwardIterator.hasNext();)
                    {
                        StrutsForward forward = (StrutsForward) forwardIterator.next();
                        actions.addAll(forward.getActions());
                    }
                }
            }
        }
        return actions;
    }
}
