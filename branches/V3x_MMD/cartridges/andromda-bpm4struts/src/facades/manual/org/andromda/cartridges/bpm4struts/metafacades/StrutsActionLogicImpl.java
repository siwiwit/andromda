package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

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
    private Collection effectTransitions = null;
    private Collection forwardActionTransitions = null;
    private Collection decisionTransitions = null;

    // ---------------- constructor -------------------------------
    
    public StrutsActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    private void initializeCollections()
    {
        effectTransitions = new HashSet();
        forwardActionTransitions = new HashSet();
        decisionTransitions = new HashSet();
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

        if (transition.getEffect()!=null && !effectTransitions.contains(transition))
        {
            effectTransitions.add(transition);
        }

        StateVertexFacade target = transition.getTarget();
        Collection outcomes = target.getOutgoing();

        if (target instanceof PseudostateFacade)
        {
            if ( ((PseudostateFacade)target).isDecisionPoint() )
                decisionTransitions.add(transition);
        }

        if (target instanceof FinalStateFacade || target instanceof ActionStateFacade)
        {
            forwardActionTransitions.add(transition);
        }
        else
        {
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

    private ClassifierFacade getContextClass()
    {
        ModelElementFacade contextElement = getSource().getActivityGraph().getContextElement();
        return (contextElement instanceof ClassifierFacade)
            ? (ClassifierFacade)contextElement
            : null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionType()
     */
    public java.lang.String getActionType()
    {
        ClassifierFacade context = getContextClass();
        return context.getPackageName() + getActionName().replace('/','.');
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getFormBeanType()()
     */
    public java.lang.String getFormBeanType()
    {
        ClassifierFacade context = getContextClass();
        return context.getPackageName() + '.' + StringUtilsHelper.toJavaClassName(getActionName()) + "Form";
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
        final Collection users = getUsers();
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
        return !Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(type);
    }

    public boolean isHyperlinkAction()
    {
        return !isFormAction();
    }

    public String getFormFullPathName()
    {
        return getActionFullPathName() + "Form";
    }

    public String getActionFullPathName()
    {
        return '/' + getActionType().replace('.', '/');
    }

    public String getTriggerKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getActionClassName());
    }

    public String getTriggerValue()
    {
        EventFacade trigger = getTrigger();
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

    public String getEffectMethodName()
    {
        ActionFacade effect = getEffect();

        if (effect instanceof CallActionFacade)
        {
            CallActionFacade callAction = (CallActionFacade)effect;
            return callAction.getOperation().getName();
        }
        else
        {
            return null;
        }
    }

    public String getDecisionMethodName()
    {
        return (isTargettingDecisionPoint()) ? StringUtilsHelper.toJavaMethodName(getActionClassName()) : null;
    }

    public String getTriggerMethodName()
    {
        EventFacade event = getTrigger();
        if (event instanceof CallEventFacade)
        {
            CallEventFacade callEvent = (CallEventFacade)event;
            return callEvent.getOperation().getName();
        }
        return null;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getUsers()
     */
    public java.util.Collection handleGetUsers()
    {
        Collection users = new LinkedHashSet();
        UseCaseFacade useCase = getActivityGraph().getUseCase();
        final Collection associationEnds = useCase.getAssociationEnds();
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
        return getSource();
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
        if (forwardActionTransitions == null)
        {
            initializeCollections();
        }
        return forwardActionTransitions;
    }

    protected Collection handleGetDecisionTransitions()
    {
        if (decisionTransitions == null)
        {
            initializeCollections();
        }
        return decisionTransitions;
    }

    protected Collection handleGetEffectTransitions()
    {
        if (effectTransitions == null)
        {
            initializeCollections();
        }
        return effectTransitions;
    }

    protected Collection handleGetActionParameters()
    {
        ActionFacade effect = getEffect();

        if (effect instanceof CallActionFacade)
        {
            CallActionFacade callAction = (CallActionFacade)effect;
            return callAction.getOperation().getParameters();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }

    protected Object handleGetActivityGraph()
    {
        return (StrutsActivityGraph)getSource().getActivityGraph();
    }

    public boolean isUseCaseStart()
    {
        return getSource().getIncoming().size() == 0;
    }
}
