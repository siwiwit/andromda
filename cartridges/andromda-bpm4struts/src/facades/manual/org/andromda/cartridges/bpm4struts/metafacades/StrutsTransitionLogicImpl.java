package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition
 */
public class StrutsTransitionLogicImpl
        extends StrutsTransitionLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition
{
    private Transition transition = null;

    // ---------------- constructor -------------------------------
    
    public StrutsTransitionLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
        this.transition = (Transition)metaObject;
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTransition ...

    public String getPackageName()
    {
        ClassifierFacade classifier = (ClassifierFacade)getSource().getActivityGraph().getContextElement();
        return classifier.getPackageName();
    }

    // from org.andromda.metafacades.uml.ModelElementFacade
    private String getTransitionName()
    {
        EventFacade trigger = getTrigger();
        return (trigger == null) ? getTarget().getName() : trigger.getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getForwardName()()
     */
    public java.lang.String getForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(getTransitionName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getForwardPath()()
     */
    public java.lang.String getForwardPath()
    {
        StateVertexFacade target = getTarget();
        String forwardPath = null;

        if (target instanceof StrutsJsp)
        {
            StrutsJsp jsp = (StrutsJsp)target;
            forwardPath = ('/' + getPackageName() + '/' + StringUtilsHelper.toWebFileName(jsp.getName())).replace('.', '/') + ".jsp";
        }
        else if (target instanceof StrutsFinalState)
        {
            StrutsFinalState finalState = (StrutsFinalState)target;
            forwardPath = finalState.getTargetUseCase().getActivityGraph().getInitialAction().getActionName() + ".do";
        }
        else
        {
            forwardPath = getTransitionName();
        }

        return forwardPath;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getMethodName()()
     */
    public java.lang.String getMethodName()
    {
        return StringUtilsHelper.toJavaMethodName(getTransitionName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getParametersAsList(boolean withTypeNames)()
     */
    public java.lang.String getParametersAsList(boolean withTypeNames)
    {
        Collection parameters = null;

        ActionFacade effect = getEffect();
        if (effect instanceof CallActionFacade)
        {
            CallActionFacade callAction = (CallActionFacade)effect;
            parameters = callAction.getOperation().getParameters();
        }
        else
        {
            parameters = Collections.EMPTY_LIST;
        }

        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = parameters.iterator(); iterator.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade) iterator.next();
            if (withTypeNames)
            {
                buffer.append(parameter.getType().getFullyQualifiedName());
                buffer.append(' ');
            }
            buffer.append(parameter.getName());
            if (iterator.hasNext())
                buffer.append(',');
        }

        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getGuardName()()
     */
    public java.lang.String getGuardName()
    {
        return transition.getGuard().getName().toUpperCase();
    }

    public boolean isTargettingJsp()
    {
        return getTarget() instanceof StrutsJsp;
    }

    public boolean isTargettingUseCase()
    {
        return getTarget() instanceof StrutsFinalState;
    }

    public boolean isTargettingDecisionPoint()
    {
        StateVertexFacade target = getTarget();
        if (target instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade)target;
            return pseudostate.isDecisionPoint();
        }
        return false;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getEventParameters()
     */
    public java.util.Collection handleGetEventParameters()
    {
        return transition.getEffect().getActualArgument();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getTargetOutcomes()
     */
    public java.util.Collection handleGetTargetOutcomes()
    {
        return transition.getTarget().getOutgoing();
    }

}
