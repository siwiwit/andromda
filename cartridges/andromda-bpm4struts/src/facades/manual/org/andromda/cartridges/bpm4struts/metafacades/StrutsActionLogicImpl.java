package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
 */
public class StrutsActionLogicImpl
        extends StrutsActionLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
{
    private Collection actionStates = null;
    private Collection actionForwards = null;
    private Collection decisionPoints = null;

    // ---------------- constructor -------------------------------
    
    public StrutsActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    private void initializeCollections()
    {
        actionStates = new HashSet();
        actionForwards = new HashSet();
        decisionPoints = new HashSet();
        collectTransitions(this, new HashSet());
    }

    private void collectTransitions(TransitionFacade transition, Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        else
        {
            processedTransitions.add(transition);
        }

        StateVertexFacade target = getTarget();

        if ( (target instanceof StrutsJsp) || (target instanceof StrutsFinalState) )
        {
            actionForwards.add(transition);
        }
        else if ( (target instanceof PseudostateFacade) && ((PseudostateFacade)target).isDecisionPoint() )
        {
            decisionPoints.add(target);
            Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade) iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
        else  if (target instanceof StrutsActionState)
        {
            actionStates.add(target);
            collectTransitions( ((StrutsActionState)target).getForward(), processedTransitions );
        }
        else    // all the rest is ignored but outgoing transitions are further processed
        {
            Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade) iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsAction ...

    public String getActionName()
    {
        String name = null;
        final StateVertexFacade source = getSource();

        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
                name = getActivityGraph().getUseCase().getName();
        }
        else
        {
            name = getSource().getName() + ' ' + getTrigger().getName();
        }
        return '/' + StringUtilsHelper.toJavaClassName(name);
    }

    public String getActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp)source).getFullPath() : "";
    }

    public boolean isFormPost()
    {
        return !isHyperlink();
    }

    public boolean isHyperlink()
    {
       return Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(
               findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE));
    }

    public boolean hasSuccessMessage()
    {
        return null != findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
    }

    private ClassifierFacade getContextClass()
    {
        ModelElementFacade contextElement = getSource().getActivityGraph().getContextElement();
        return (contextElement instanceof ClassifierFacade)
            ? (ClassifierFacade)contextElement
            : null;
    }

    public java.lang.String getActionPath()
    {
        return '/' + getActionClassName();
    }

    public java.lang.String getActionRoles()
    {
        final Collection users = getActivityGraph().getUseCase().getUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    public String getActionClassName()
    {
        return StringUtilsHelper.toJavaClassName(getName() + ' ' + getTriggerName());
    }

    private String getTriggerName()
    {
        EventFacade trigger = getTrigger();
        return (trigger == null) ? getTarget().getName() : trigger.getName();
    }

    public String getFormBeanClassName()
    {
        return getActionClassName() + "Form";
    }

    public String getFormBeanName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String getFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    public String getTriggerKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getActionClassName());
    }

    public String getTriggerValue()
    {
        return StringUtilsHelper.toPhrase(getTriggerName());
    }

    public String getSuccessMessageKey()
    {
        return getTriggerKey() + ".success";
    }

    public String getSuccessMessageValue()
    {
        return '[' + getTriggerValue() + "] succesfully executed on " + getInput().getTitleValue();
    }

    public String getPackageName()
    {
        return getContextClass().getPackageName();
    }
    // ------------- relations ------------------

    protected Collection handleGetActionForwards()
    {
        if (actionForwards == null) initializeCollections();
        return actionForwards;
    }

    protected Collection handleGetDecisionPoints()
    {
        if (decisionPoints == null) initializeCollections();
        return decisionPoints;
    }

    protected Collection handleGetActionStates()
    {
        if (actionStates == null) initializeCollections();
        return actionStates;
    }

    protected Collection handleGetActionExceptions()
    {
        final Collection exceptions = new HashSet();

        final Collection actionStates = getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState)iterator.next();
            exceptions.addAll(actionState.getExceptions());
        }

        return exceptions;
    }

    protected java.lang.Object handleGetInput()
    {
        return getSource();
    }

    protected Collection handleGetInputFields()
    {
        EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetActivityGraph()
    {
        return getSource().getActivityGraph();
    }
}
