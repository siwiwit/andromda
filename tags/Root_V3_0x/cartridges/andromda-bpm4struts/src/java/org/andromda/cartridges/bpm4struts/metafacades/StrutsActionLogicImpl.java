package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
 */
public class StrutsActionLogicImpl
        extends StrutsActionLogic
{
    /**
     * All action states that make up this action, this includes all possible action states traversed
     * after a decision point too.
     */
    private Collection actionStates = null;

    /**
     * All transitions leading into either a page or final state that originated from a call to this action.
     */
    private Map actionForwards = null;

    /**
     * All transitions leading into a decision point that originated from a call to this action.
     */
    private Collection decisionTransitions = null;

    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection transitions = null;

    public StrutsActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Initializes all action states, action forwards, decision transitions and transitions in one shot, so that they
     * can be queried more effiencently later on.
     */
    private void initializeCollections()
    {
        actionStates = new HashSet();
        actionForwards = new HashMap();
        decisionTransitions = new HashSet();
        transitions = new HashSet();
        collectTransitions(this, transitions);
    }

    /**
     * Recursively collects all action states, action forwards, decision transitions and transitions.
     *
     * @param transition the current transition that is being processed
     * @param processedTransitions the set of transitions already processed
     */
    private void collectTransitions(TransitionFacade transition, Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if ((target instanceof StrutsJsp) || (target instanceof StrutsFinalState))
        {
            if (!actionForwards.containsKey(transition.getTarget()))
            {
                actionForwards.put(transition.getTarget(), transition);
            }
        }
        else if ((target instanceof PseudostateFacade) && ((PseudostateFacade)target).isDecisionPoint())
        {
            decisionTransitions.add(transition);
            final Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade)iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
        else if (target instanceof StrutsActionState)
        {
            actionStates.add(target);
            final StrutsForward forward = ((StrutsActionState)target).getForward();
            if (forward != null)
            {
                collectTransitions(forward, processedTransitions);
            }
        }
        else    // all the rest is ignored but outgoing transitions are further processed
        {
            final Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade)iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
    }

    protected String handleGetActionName()
    {
        return getFormBeanName();
    }

    protected String handleGetActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp)source).getFullPath() : "";
    }

    protected boolean handleIsFormPost()
    {
        return !isHyperlink();
    }

    protected boolean handleIsTableLink()
    {
        return getTableLinkParameter() != null;
    }

    protected Object handleGetTableLinkParameter()
    {
        StrutsParameter tableLinkParameter = null;

        final String tableLinkName = getTableLinkName();
        if (tableLinkName != null)
        {
            final StrutsJsp page = getInput();
            if (page != null)
            {
                final List tables = page.getTables();
                for (int i = 0; i < tables.size() && tableLinkParameter == null; i++)
                {
                    StrutsParameter table = (StrutsParameter)tables.get(i);
                    if (tableLinkName.equals(table.getName()))
                    {
                        tableLinkParameter = table;
                    }
                }
            }
        }

        return tableLinkParameter;
    }

    protected List handleGetTableNonColumnFormParameters()
    {
        List tableNonColumnActionParameters = null;

        final StrutsParameter table = getTableLinkParameter();
        if (table != null)
        {
            final Map tableNonColumnActionParametersMap = new LinkedHashMap(4);
            final Collection columnNames = table.getTableColumnNames();
            final List formActions = table.getTableFormActions();
            for (int i = 0; i < formActions.size(); i++)
            {
                final StrutsAction action = (StrutsAction)formActions.get(i);
                for (int j = 0; j < action.getActionParameters().size(); j++)
                {
                    final StrutsParameter parameter = (StrutsParameter)action.getActionParameters().get(j);
                    if (!columnNames.contains(parameter.getName()))
                    {
                        tableNonColumnActionParametersMap.put(parameter.getName(), parameter);
                    }
                }
            }

            tableNonColumnActionParameters = new ArrayList(tableNonColumnActionParametersMap.values());
        }

        return tableNonColumnActionParameters;
    }

    protected String handleGetTableLinkName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1) ? tableLink : tableLink.substring(0, columnOffset);
            }
        }

        return tableLink;
    }

    protected String handleGetTableLinkColumnName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1 || columnOffset == tableLink.length() - 1) ?
                        null : tableLink.substring(columnOffset + 1);
            }
        }

        return tableLink;
    }

    protected boolean handleIsHyperlink()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(
                value == null ? null : value.toString());
    }

    protected boolean handleIsImageLink()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_IMAGE.equalsIgnoreCase(
                value == null ? null : value.toString());
    }

    protected String handleGetImagePath()
    {
        return getPackagePath() + '/' + StringUtilsHelper.toWebFileName(getActionClassName()) + ".gif";
    }

    protected java.lang.String handleGetActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    protected String handleGetActionPathRoot()
    {
        String actionPathRoot = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            StringBuffer buffer = new StringBuffer();

            String prefix = String.valueOf(getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_ACTION_PATH_PREFIX));

            ModelElementFacade useCasePackage = useCase.getPackage();
            if (useCasePackage != null)
            {
                prefix = prefix.replaceAll("\\{0\\}", useCasePackage.getPackagePath());
            }

            buffer.append(prefix);
            buffer.append('/');
            buffer.append(StringUtilsHelper.upperCamelCaseName(useCase.getName()));

            actionPathRoot = buffer.toString();
        }
        return actionPathRoot;
    }

    protected String handleGetActionScope()
    {
        return "request";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionRoles()
     */
    protected java.lang.String handleGetActionRoles()
    {
        final Collection users = getRoleUsers();
        final StringBuffer roles = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            roles.append(((ModelElementFacade)userIterator.next()).getName());
            if (userIterator.hasNext())
            {
                roles.append(",");
            }
        }
        return roles.toString();
    }

    /**
     * Returns a collection containing StrutsUser instances representing the roles
     * authorized to call this action. If this action starts the use-case that use-case's users
     * are returned, otherwise it will return the users associated to the use-cases targetted by this
     * action (which may be none at all)
     */
    private Collection getRoleUsers()
    {
        final Collection roleUsers = new ArrayList();

        if (isUseCaseStart())
        {
            final StrutsUseCase useCase = getUseCase();
            if (useCase != null)
            {
                roleUsers.addAll(useCase.getUsers());
            }
        }
        else
        {
            for (Iterator iterator = getActionForwards().iterator(); iterator.hasNext();)
            {
                final TransitionFacade transition = (TransitionFacade)iterator.next();
                if (transition.getTarget() instanceof StrutsFinalState)
                {
                    final StrutsUseCase useCase = ((StrutsFinalState)transition.getTarget()).getTargetUseCase();
                    if (useCase != null)
                    {
                        roleUsers.addAll(useCase.getUsers());
                    }
                }
            }
        }

        return roleUsers;
    }

    protected String handleGetActionClassName()
    {
        String name = null;
        final StateVertexFacade source = getSource();

        if (source instanceof PseudostateFacade)
        {
            final PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
            {
                final StrutsUseCase useCase = getUseCase();
                if (useCase != null)
                {
                    name = useCase.getName();
                }
            }
        }
        else
        {
            final EventFacade trigger = getTrigger();
            final String suffix = (trigger == null) ? getTarget().getName() : trigger.getName();
            name = getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.upperCamelCaseName(name);
    }

    protected String handleGetActionType()
    {
        return getPackageName() + '.' + getActionClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return getActionClassName() + "FormImpl";
    }

    protected String handleGetFormBeanName()
    {
        String formBeanName = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            String useCaseName = useCase.getName();
            formBeanName = StringUtilsHelper.lowerCamelCaseName(useCaseName) + getActionClassName() + "Form";
        }
        return formBeanName;
    }

    protected String handleGetFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    /**
     * Overrides the one from StrutsForward. This one incorporates the name of the originating page to avoid conflicts.
     */
    public String handleGetMessageKey()
    {
        String messageKey = super.handleGetMessageKey() + ' ';
        messageKey += isExitingPage() ? getInput().getName() : "";
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    /**
     * Overrides the method defined in the facade parent of StrutsAction, this is done because actions (transitions) are
     * not directly contained in a UML namespace.
     */
    public String getPackageName()
    {
        String packageName = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            packageName = useCase.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isResettable()
     */
    protected boolean handleIsResettable()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_RESETTABLE);
        return isTrue(value == null ? null : value.toString());
    }

    /**
     * Convenient method to detect whether or not a String instance represents a boolean <code>true</code> value.
     */
    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) || "on".equalsIgnoreCase(string) ||
                "1".equalsIgnoreCase(string);
    }

    protected boolean handleIsUseCaseStart()
    {
        StateVertexFacade source = getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade)source).isInitialState();
    }

    protected String handleGetFullActionPath()
    {
        return getPackagePath() + '/' + getActionClassName();
    }

    protected String handleGetFullTilePath()
    {
        return isUseCaseStart() ?
                "empty-file" : getPackagePath() + '/' + StringUtilsHelper.toWebFileName(getActionClassName());
    }

    /**
     * We override this method here to make sure the actions end-up in the same package as their use-case. A transition
     * (this class' parent type) does not have a real package as we need it here.
     */
    public String getPackagePath()
    {
        String packagePath = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            packagePath = '/' + useCase.getPackagePath();
        }
        return packagePath;
    }

    protected String handleGetFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/');
    }

    protected boolean handleIsValidationRequired()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter)iterator.next();
            if (parameter.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsDateFieldPresent()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter)iterator.next();
            if (parameter.isDate())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsCalendarRequired()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter)iterator.next();
            if (parameter.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected String handleGetFormBeanPackageName()
    {
        return getPackageName();
    }

    protected String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    protected String handleGetDocumentationKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null) ?
                getMessageKey() + ".is.an.action.without.trigger" : getActionTrigger().getTriggerKey()) +
                ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    protected String handleGetOnlineHelpKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null) ?
                getMessageKey() + ".is.an.action.without.trigger" : getActionTrigger().getTriggerKey()) +
                ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No action documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected List handleGetActionForwards()
    {
        if (actionForwards == null) initializeCollections();
        return new ArrayList(actionForwards.values());
    }

    protected List handleGetDecisionTransitions()
    {
        if (decisionTransitions == null) initializeCollections();
        return new ArrayList(decisionTransitions);
    }

    protected List handleGetActionStates()
    {
        if (actionStates == null) initializeCollections();
        return new ArrayList(actionStates);
    }

    protected List handleGetActionExceptions()
    {
        final Collection exceptions = new HashSet();
        final Collection actionStates = getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState)iterator.next();
            exceptions.addAll(actionState.getExceptions());
        }

        return new ArrayList(exceptions);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getInput()
     */
    protected java.lang.Object handleGetInput()
    {
        Object input = null;
        ModelElementFacade source = getSource();
        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW))
            {
                input = source;
            }
        }
        return input;
    }

    protected Object handleGetController()
    {
        return getStrutsActivityGraph().getController();
    }

    protected Object handleGetActionTrigger()
    {
        return this.getTrigger();
    }

    protected List handleGetActionFormFields()
    {
        final Map formFieldMap = new HashMap();

        /**
         * for useCaseStart actions we need to detect all usecases forwarding to the one belonging to this action
         * if there are any parameters in those requests we need to have them included in this action's form
         */
        if (isUseCaseStart())
        {
            StrutsUseCase useCase = getUseCase();
            if (useCase != null)
            {
                Collection finalStates = useCase.getReferencingFinalStates();
                for (Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
                {
                    StrutsFinalState finalState = (StrutsFinalState)finalStateIterator.next();
                    Collection actions = finalState.getActions();
                    for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                    {
                        StrutsAction action = (StrutsAction)actionIterator.next();
                        Collection parameters = action.getActionParameters();
                        for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                        {
                            StrutsParameter parameter = (StrutsParameter)parameterIterator.next();
                            formFieldMap.put(parameter.getName(), parameter);
                        }
                    }
                }
            }
        }

        // if any action encountered by the execution of the complete action-graph path emits a forward
        // containing one or more parameters they need to be included as a form field too
        Collection actionStates = getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState)iterator.next();
            StrutsForward forward = actionState.getForward();
            if (forward != null)
            {
                Collection forwardParameters = forward.getForwardParameters();
                for (Iterator parameterIterator = forwardParameters.iterator(); parameterIterator.hasNext();)
                {
                    StrutsParameter forwardParameter = (StrutsParameter)parameterIterator.next();
                    formFieldMap.put(forwardParameter.getName(), forwardParameter);
                }
            }
        }

        // add page variables for all pages/final-states targetted
        // also add the fields of the target page's actions (for preloading)
        Collection forwards = getActionForwards();
        for (Iterator iterator = forwards.iterator(); iterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)iterator.next();
            StateVertexFacade target = forward.getTarget();
            if (target instanceof StrutsJsp)
            {
                StrutsJsp jsp = (StrutsJsp)target;
                Collection pageVariables = jsp.getPageVariables();
                for (Iterator pageVariableIterator = pageVariables.iterator(); pageVariableIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade)pageVariableIterator.next();
                    formFieldMap.put(facade.getName(), facade);
                }
                Collection allActionParameters = jsp.getAllActionParameters();
                for (Iterator actionParameterIterator = allActionParameters.iterator();
                     actionParameterIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade)actionParameterIterator.next();
                    formFieldMap.put(facade.getName(), facade);
                }
            }
            else if (target instanceof StrutsFinalState)
            {
                // only add these if there is no parameter recorded yet with the same name
                Collection forwardParameters = forward.getForwardParameters();
                for (Iterator forwardParameterIterator = forwardParameters.iterator();
                     forwardParameterIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade)forwardParameterIterator.next();
                    if (!formFieldMap.containsKey(facade.getName()))
                    {
                        formFieldMap.put(facade.getName(), facade);
                    }
                }
            }
        }

        // we do the action parameters in the end because they are allowed to overwrite existing properties
        Collection actionParameters = getActionParameters();
        for (Iterator actionParameterIterator = actionParameters.iterator(); actionParameterIterator.hasNext();)
        {
            ModelElementFacade facade = (ModelElementFacade)actionParameterIterator.next();
            formFieldMap.put(facade.getName(), facade);
        }

        return new ArrayList(formFieldMap.values());
    }

    protected List handleGetDeferredOperations()
    {
        final Collection deferredOperations = new LinkedHashSet();

        final StrutsController controller = getController();
        if (controller != null)
        {
            final List actionStates = getActionStates();
            for (int i = 0; i < actionStates.size(); i++)
            {
                final StrutsActionState actionState = (StrutsActionState)actionStates.get(i);
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List transitions = getDecisionTransitions();
            for (int i = 0; i < transitions.size(); i++)
            {
                final StrutsForward forward = (StrutsForward)transitions.get(i);
                final StrutsTrigger trigger = forward.getDecisionTrigger();
                if (trigger != null)
                {
                    deferredOperations.add(trigger.getControllerCall());
                }
            }
        }
        return new ArrayList(deferredOperations);
    }

    protected List handleGetActionParameters()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : new ArrayList(trigger.getParameters());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTargetPages()
     */
    protected List handleGetTargetPages()
    {
        Collection targetPages = new HashSet();

        Collection forwards = getActionForwards();
        for (Iterator forwardIterator = forwards.iterator(); forwardIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)forwardIterator.next();
            if (forward.isEnteringPage())
            {
                targetPages.add(forward.getTarget());
            }
        }

        return new ArrayList(targetPages);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#getTransitions()
     */
    protected List handleGetTransitions()
    {
        if (transitions == null)
        {
            initializeCollections();
        }
        return new ArrayList(transitions);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getStyleId()
     */
    protected String handleGetStyleId()
    {
        String styleId = null;

        StrutsTrigger trigger = getActionTrigger();
        if (trigger != null)
        {
            String triggerName = trigger.getName();
            styleId = StringUtilsHelper.lowerCamelCaseName(triggerName);
        }
        return styleId;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isRedirect()
     */
    protected boolean handleIsRedirect()
    {
        String redirect = (String)this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_ACTION_REDIRECT);
        Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_REDIRECT);
        if (value != null)
        {
            redirect = (String)value;
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(redirect)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getResettableActionParameters()
     */
    protected List handleGetResettableActionParameters()
    {
        return new ArrayList(new FilteredCollection(this.getActionParameters())
        {
            public boolean evaluate(Object object)
            {
                return object != null && ((StrutsParameter)object).isShouldReset();
            }
        });
    }

    /**
     * The "session" action form scope.
     */
    private static final String FORM_SCOPE_SESSION = "session";

    /**
     * The "request" action form scope.
     */
    private static final String FORM_SCOPE_REQUEST = "request";

    /**
     * The "none" action form scope.
     */
    private static final String FORM_SCOPE_NONE = "none";

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getFormScope()
     */
    protected String handleGetFormScope()
    {
        String actionFormScope = String.valueOf(this.getConfiguredProperty(
                Bpm4StrutsGlobals.PROPERTY_ACTION_FORM_SCOPE));
        Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_SCOPE);
        if (value != null)
        {
            actionFormScope = String.valueOf(value);
        }
        return StringUtils.trimToEmpty(actionFormScope);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeSession()
     */
    protected boolean handleIsFormScopeSession()
    {
        return this.getFormScope().equalsIgnoreCase(FORM_SCOPE_SESSION);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeRequest()
     */
    protected boolean handleIsFormScopeRequest()
    {
        return this.getFormScope().equalsIgnoreCase(FORM_SCOPE_REQUEST);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeNone()
     */
    protected boolean handleIsFormScopeNone()
    {
        return this.getFormScope().equalsIgnoreCase(FORM_SCOPE_NONE);
    }
}