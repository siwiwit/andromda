package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.omg.uml.behavioralelements.statemachines.*;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.commonbehavior.CallAction;
import org.omg.uml.foundation.core.Classifier;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
 */
public class StrutsActionLogicImpl
        extends StrutsActionLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
{
    private Transition transition = null;

    private final Collection effectTransitions = new HashSet();
    private final Collection forwardActionTransitions = new HashSet();
    private final Collection decisionTransitions = new HashSet();

    // ---------------- constructor -------------------------------
    
    public StrutsActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
        this.transition = (Transition)metaObject;

        collectTransitions(transition);
    }

    private void collectTransitions(Transition transition)
    {
        if (transition.getEffect()!=null && !effectTransitions.contains(transition))
        {
            effectTransitions.add(transition);
        }

        StateVertex target = transition.getTarget();
        Collection outcomes = target.getOutgoing();

        if ( (target instanceof Pseudostate) && (outcomes.size() > 1) )
        {
            decisionTransitions.add(transition);
        }

        if (target instanceof FinalState || target instanceof ActionState)
        {
            forwardActionTransitions.add(transition);
        }
        else
        {
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                Transition outcome = (Transition) iterator.next();
                collectTransitions(outcome);
            }
        }
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsAction ...

    public String getActionName()
    {
        return '/' + StringUtilsHelper.toJavaClassName(transition.getSource().getName() + ' ' + transition.getTrigger().getName());
    }

    private ClassifierFacade getContextClass()
    {
        Classifier contextClass = (Classifier)transition.getStateMachine().getContext();
        return (ClassifierFacade)shieldedElement(contextClass);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionType()
     */
    public java.lang.String getActionType()
    {
        ClassifierFacade context = getContextClass();
        return context.getPackageName() + '.' + StringUtilsHelper.toJavaClassName(getActionName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getFormBeanType()()
     */
    public java.lang.String getFormBeanType()
    {
        ClassifierFacade context = getContextClass();
        return context.getPackageName() + '.' + StringUtilsHelper.toJavaClassName(getActionName()) + "'Form";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionPath()()
     */
    public java.lang.String getActionPath()
    {
        return '/' + getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionRoles()
     */
    public java.lang.String getActionRoles()
    {
        Collection users = getUsers();

        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }

        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    public String getFormBeanPackageName()
    {
        return getController().getPackageName();
    }

    public String getActionPackageName()
    {
        return getController().getPackageName();
    }

    public String getActionClassName()
    {
        return StringUtilsHelper.toJavaClassName(getActionName());
    }

    public String getFormBeanClassName()
    {
        return getActionClassName() + "Form";
    }

    public Transition getActionTransition()
    {
        return transition;
    }

    public String getFormName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String getFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    public boolean isFormAction()
    {
        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE);
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_FORM;
        }
        return Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(type);
    }

    public boolean isHyperlinkAction()
    {
        return !isFormAction();
    }

    public String getFullPathName()
    {
        return '/' + getActionType().replace('.', '/');
    }

    public String getTriggerKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getTriggerValue());
    }

    public String getTriggerValue()
    {
        Event trigger = transition.getTrigger();
        return StringUtilsHelper.toPhrase(trigger.getName());
    }

    public boolean hasSuccesMessage()
    {
        return null != findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
    }

    public String getSuccessMessageKey()
    {
        return getTriggerKey() + ".success";
    }

    public String getSuccessMessageValue()
    {
        return '[' + getTriggerValue() + "] succesfully executed on " + getInput().getTitleValue();
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getUsers()
     */
    public java.util.Collection handleGetUsers()
    {
        Collection users = new LinkedHashSet();

        UseCase useCase = (UseCase)transition.getStateMachine().getNamespace();
        final Collection associationEnds = ((ClassifierFacade)shieldedElement(useCase)).getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
            final ClassifierFacade otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd instanceof StrutsUser)
            {
                StrutsUser user = (StrutsUser)otherEnd;
                users.add(user);
                users.addAll(user.getGeneralizedUsers());
            }
        }

        return users;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getInput()
     */
    public java.lang.Object handleGetInput()
    {
        return transition.getSource();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getExceptionHandlers()
     */
    public java.util.Collection handleGetExceptionHandlers()
    {
        final Collection exceptionHandlers = new LinkedList();
         final Collection associationEnds = getContextClass().getAssociationEnds();
         for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
         {
             final AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
             final ClassifierFacade otherEnd = associationEnd.getOtherEnd().getType();
             if (otherEnd instanceof StrutsJsp)
                 exceptionHandlers.add(otherEnd);
         }
         return exceptionHandlers;
    }

    protected Object handleGetController()
    {
        return getContextClass();
    }

    protected Collection handleGetForwardActionTransitions()
    {
        return forwardActionTransitions;
    }

    protected Collection handleGetDecisionTransitions()
    {
        return decisionTransitions;
    }

    protected Collection handleGetEffectTransitions()
    {
        return effectTransitions;
    }

    protected Collection handleGetActionParameters()
    {
        Action effect = transition.getEffect();

        if (effect instanceof CallAction)
        {
            CallAction callAction = (CallAction)effect;
            return callAction.getOperation().getParameter();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }
}
