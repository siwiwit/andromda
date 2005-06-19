package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
        extends StrutsUseCaseLogic
{
    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(normalizeMessages() ? getTitleValue() : getName()) + ".title";
    }

    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetOnlineHelpKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No use-case documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected String handleGetActionPath()
    {
        String actionPath = null;

        final StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            final StrutsAction action = graph.getFirstAction();
            if (action != null)
            {
                actionPath = action.getActionPath();
            }
        }
        return actionPath;
    }

    protected String handleGetActionPathRoot()
    {
        String actionPathRoot = null;

        final StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            final StrutsAction action = graph.getFirstAction();
            if (action != null)
            {
                actionPathRoot = action.getActionPathRoot();
            }
        }
        return actionPathRoot;
    }

    protected String handleGetActionRoles()
    {
        final Collection users = getUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        boolean first = true;
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                rolesBuffer.append(',');
            }
            StrutsUser strutsUser = (StrutsUser)userIterator.next();
            rolesBuffer.append(strutsUser.getName());
        }
        return rolesBuffer.toString();
    }

    public Collection getOperations()
    {
        return Collections.EMPTY_LIST;
    }

    protected java.lang.Object handleGetActivityGraph()
    {
        ActivityGraphFacade activityGraph = null;

        /*
         * In case there is a tagged value pointing to an activity graph, and this graph is found,
         * then return it.
         */
        final Object activity = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_USECASE_ACTIVITY);
        if (activity != null)
        {
            String activityName = String.valueOf(activity.toString());
            activityGraph = getModel().findActivityGraphByName(activityName);
        }

        /*
         * Otherwise just take the first one in this use-case's namespace.
         */
        if (activityGraph == null)
        {
            Collection ownedElements = getOwnedElements();
            for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                if (obj instanceof StrutsActivityGraph)
                    return obj;
            }
        }

        return activityGraph;
    }

    /**
     * @return those users directly associated to this use-case, the returned collection only contains StrutsUser
     *  instances
     */
    private Collection associatedUsers()
    {
        final Collection usersList = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
            final ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsUser)
                usersList.add(classifier);
        }

        return usersList;
    }

    protected List handleGetUsers()
    {
        final Collection allUsersList = new HashSet();
        final Collection associatedUsers = associatedUsers();
        for (Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            final StrutsUser user = (StrutsUser)iterator.next();
            collectUsers(user, allUsersList);
        }
        return new ArrayList(allUsersList);
    }

    protected List handleGetAllUsers()
    {
        List allUsers = new ArrayList();
        Collection allActors = getModel().getAllActors();

        for (Iterator actorIterator = allActors.iterator(); actorIterator.hasNext();)
        {
            Object actorObject = actorIterator.next();
            if (actorObject instanceof StrutsUser)
            {
                allUsers.add(actorObject);
            }
        }
        return allUsers;
    }

    /**
     * Recursively collects all users generalizing the argument user, in the specified collection.
     */
    private void collectUsers(StrutsUser user, Collection users)
    {
        if (!users.contains(user))
        {
            users.add(user);

            final Collection childUsers = user.getGeneralizedByActors();
            for (Iterator iterator = childUsers.iterator(); iterator.hasNext();)
            {
                final StrutsUser childUser = (StrutsUser)iterator.next();
                collectUsers(childUser, users);
            }
        }
    }

    protected List handleGetPages()
    {
        List pagesList = null;

        StrutsActivityGraph graph = getActivityGraph();
        if (graph == null)
        {
            pagesList = Collections.EMPTY_LIST;
        }
        else
        {
            pagesList = new ArrayList(
                    getModel().getAllActionStatesWithStereotype(graph, Bpm4StrutsProfile.STEREOTYPE_VIEW));
        }
        return pagesList;
    }

    protected List handleGetAllPages()
    {
        final List pagesList = new ArrayList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = actionStateIterator.next();
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected List handleGetAllUseCases()
    {
        final List useCases = new ArrayList();

        for (Iterator useCaseIterator = getModel().getAllUseCases().iterator(); useCaseIterator.hasNext();)
        {
            Object object = useCaseIterator.next();
            if (object instanceof StrutsUseCase)
                useCases.add(object);
        }
        return useCases;
    }

    protected Object handleGetController()
    {
        StrutsActivityGraph graph = getActivityGraph();
        return (graph == null) ? null : graph.getController();
    }

    protected List handleGetFormFields()
    {
        final List formFields = new ArrayList(); // parameter names are supposed to be unique

        final Collection pages = getPages();
        for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp)pageIterator.next();
            Collection variables = jsp.getPageVariables();
            for (Iterator variableIterator = variables.iterator(); variableIterator.hasNext();)
            {
                formFields.add(variableIterator.next());
            }
            Collection parameters = jsp.getAllActionParameters();
            for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
            {
                formFields.add(parameterIterator.next());
            }
        }
        return formFields;
    }

    protected boolean handleIsValidationRequired()
    {
        final Collection allPages = this.getAllPages();
        for (Iterator iterator = allPages.iterator(); iterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp)iterator.next();
            if (jsp.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsApplicationValidationRequired()
    {
        final Collection useCases = this.getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase)iterator.next();
            if (useCase.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected List handleGetActions()
    {
        Collection actions = new HashSet();

        Collection pages = getPages();
        for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp)pageIterator.next();
            actions.addAll(jsp.getActions());
        }

        StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            StrutsAction action = graph.getFirstAction();
            if (action != null) actions.add(action);
        }

        return new ArrayList(actions);
    }

    protected List handleGetPageVariables()
    {
        final Map pageVariableMap = new HashMap();

        /**
         * page variables can occur twice or more in the usecase in case their
         * names are the same for different forms, storing them in a map
         * solves this issue because those names do not have the action-name prefix
         */
        final Collection pages = getPages();
        for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            final StrutsJsp jsp = (StrutsJsp)pageIterator.next();
            final Collection variables = jsp.getPageVariables();
            for (Iterator variableIterator = variables.iterator(); variableIterator.hasNext();)
            {
                final StrutsParameter variable = (StrutsParameter)variableIterator.next();
                pageVariableMap.put(variable.getName(), variable);
            }
        }
        return new ArrayList(pageVariableMap.values());
    }

    protected boolean handleIsSecured()
    {
        return !getUsers().isEmpty();
    }

    protected boolean handleIsApplicationUseCase()
    {
        return hasStereotype(Bpm4StrutsProfile.STEREOTYPE_APPLICATION);
    }

    protected boolean handleIsCyclic()
    {
        boolean selfTargetting = false;

        final StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            final Collection finalStates = graph.getFinalStates();
            for (Iterator finalStateIterator = finalStates.iterator();
                 finalStateIterator.hasNext() && !selfTargetting;)
            {
                final StrutsFinalState finalState = (StrutsFinalState)finalStateIterator.next();
                if (this.equals(finalState.getTargetUseCase()))
                {
                    selfTargetting = true;
                }
            }
        }

        return selfTargetting;
    }

    protected List handleGetReferencingFinalStates()
    {
        final List referencingFinalStates = new ArrayList(this.getModel().findFinalStatesWithNameOrHyperlink(this));
        return referencingFinalStates;
    }

    protected String handleGetCssFileName()
    {
        return getPackagePath() + '/' + StringUtilsHelper.toWebFileName(getName()) + ".css";
    }

    protected TreeNode handleGetApplicationHierarchyRoot()
    {
        final UseCaseNode root = new UseCaseNode(this);
        createHierarchy(root);
        return root;
    }

    protected TreeNode handleGetHierarchyRoot()
    {
        UseCaseNode hierarchy = null;

        Collection allUseCases = getAllUseCases();
        for (Iterator useCaseIterator = allUseCases.iterator(); useCaseIterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase)useCaseIterator.next();
            if (useCase.isApplicationUseCase())
            {
                UseCaseNode root = (UseCaseNode)useCase.getApplicationHierarchyRoot();
                hierarchy = findNode(root, this);
            }
        }
        return hierarchy;
    }

    /**
     * Recursively creates a hierarchy of use-cases, starting with the argument use-case as the root. This is primarily
     * meant to build a set of menu items.
     */
    private void createHierarchy(UseCaseNode root)
    {
        StrutsUseCase useCase = (StrutsUseCase)root.getUserObject();

        StrutsActivityGraph graph = useCase.getActivityGraph();
        if (graph != null)
        {
            Collection finalStates = graph.getFinalStates();
            for (Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
            {
                StrutsFinalState finalState = (StrutsFinalState)finalStateIterator.next();
                StrutsUseCase targetUseCase = finalState.getTargetUseCase();
                if (targetUseCase != null)
                {
                    UseCaseNode useCaseNode = new UseCaseNode(targetUseCase);
                    if (isNodeAncestor(root, useCaseNode) == false)
                    {
                        root.add(useCaseNode);
                        createHierarchy(useCaseNode);
                    }
                }
            }
        }
    }

    /**
     * <code>true</code> if the argument ancestor node is actually an ancestor of the first node.
     *
     * <em>Note: DefaultMutableTreeNode's isNodeAncestor does not work because of its specific impl.</em>
     */
    private boolean isNodeAncestor(UseCaseNode node, UseCaseNode ancestorNode)
    {
        boolean ancestor = false;

        if (node.getUseCase().equals(ancestorNode.getUseCase()))
        {
            ancestor = true;
        }
        while (!ancestor && node.getParent() != null)
        {
            node = (UseCaseNode)node.getParent();
            if (isNodeAncestor(node, ancestorNode))
            {
                ancestor = true;
            }
        }
        return ancestor;
    }

    /**
     * Given a root use-case, finds the node in the hierarchy that represent the argument StrutsUseCase node.
     */
    private UseCaseNode findNode(UseCaseNode root, StrutsUseCase useCase)
    {
        UseCaseNode useCaseNode = null;

        final List nodeList = Collections.list(root.breadthFirstEnumeration());
        for (Iterator nodeIterator = nodeList.iterator(); nodeIterator.hasNext() && useCaseNode == null;)
        {
            UseCaseNode node = (UseCaseNode)nodeIterator.next();
            if (useCase.equals(node.getUserObject()))
            {
                useCaseNode = node;
            }
        }
        return useCaseNode;
    }

    public final static class UseCaseNode
            extends DefaultMutableTreeNode
    {
        public UseCaseNode(StrutsUseCase useCase)
        {
            super(useCase);
        }

        public StrutsUseCase getUseCase()
        {
            return (StrutsUseCase)getUserObject();
        }
    }

    private boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    protected Map handleGetAllMessages()
    {
        final boolean normalize = normalizeMessages();
        final Map messages = (normalize) ? (Map)new TreeMap() : (Map)new LinkedHashMap();

        if (isApplicationUseCase())
        {
            final List useCases = getAllUseCases();
            for (int i = 0; i < useCases.size(); i++)
            {
                // USECASE
                final StrutsUseCase useCase = (StrutsUseCase)useCases.get(i);
                messages.put(useCase.getTitleKey(), useCase.getTitleValue());
                messages.put(useCase.getOnlineHelpKey(), useCase.getOnlineHelpValue());

                final List pages = useCase.getPages();
                for (int j = 0; j < pages.size(); j++)
                {
                    // PAGE
                    final StrutsJsp page = (StrutsJsp)pages.get(j);
                    messages.put(page.getTitleKey(), page.getTitleValue());
                    messages.put(page.getMessageKey(), page.getMessageValue());
                    messages.put(page.getOnlineHelpKey(), page.getOnlineHelpValue());
                    messages.put(page.getDocumentationKey(), page.getDocumentationValue());

                    final List pageVariables = page.getPageVariables();
                    for (int k = 0; k < pageVariables.size(); k++)
                    {
                        // PAGE-VARIABLE
                        final StrutsParameter parameter = (StrutsParameter)pageVariables.get(k);

                        messages.put(parameter.getMessageKey(), parameter.getMessageValue());
/*
                        if (normalize)
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                        }
                        else
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(page.getTitleKey() + '.' + parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(page.getTitleKey() + '.' + parameter.getMessageKey(),
                                    parameter.getMessageValue());
                        }
*/

                        // TABLE
                        if (parameter.isTable())
                        {
                            final Collection columnNames = parameter.getTableColumnNames();
                            for (Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
                            {
                                final String columnName = (String)columnNameIterator.next();
                                messages.put(parameter.getTableColumnMessageKey(columnName),
                                        parameter.getTableColumnMessageValue(columnName));
                            }
                        }
                    }

                    final List actions = useCase.getActions();
                    for (int k = 0; k < actions.size(); k++)
                    {
                        // ACTION
                        final StrutsAction action = (StrutsAction)actions.get(k);

                        // TRIGGER
                        final StrutsTrigger trigger = action.getActionTrigger();
                        if (trigger != null)
                        {
                            // only add these when a trigger is present, otherwise it's no use having them
                            messages.put(action.getOnlineHelpKey(), action.getOnlineHelpValue());
                            messages.put(action.getDocumentationKey(), action.getDocumentationValue());

                            // the regular trigger messages
                            messages.put(trigger.getTitleKey(), trigger.getTitleValue());
                            messages.put(trigger.getNotAllowedTitleKey(), trigger.getNotAllowedTitleValue());
                            messages.put(trigger.getResetMessageKey(), trigger.getResetMessageValue());
                            messages.put(trigger.getResetNotAllowedTitleKey(), trigger.getResetNotAllowedTitleValue());
                            messages.put(trigger.getResetTitleKey(), trigger.getResetTitleValue());
                            // this one is the same as doing: action.getMessageKey()
                            messages.put(trigger.getTriggerKey(), trigger.getTriggerValue());

                            // IMAGE LINK
                            if (action.isImageLink())
                            {
                                messages.put(action.getImageMessageKey(), action.getImagePath());
                            }
                        }

                        // FORWARDS
                        final List transitions = action.getTransitions();
                        for (int l = 0; l < transitions.size(); l++)
                        {
                            final StrutsForward forward = (StrutsForward)transitions.get(l);
                            messages.putAll(forward.getSuccessMessages());
                            messages.putAll(forward.getWarningMessages());
                        }

                        // ACTION PARAMETERS
                        final List parameters = action.getActionParameters();
                        for (int l = 0; l < parameters.size(); l++)
                        {
                            final StrutsParameter parameter = (StrutsParameter)parameters.get(l);
                            messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                            messages.put(parameter.getOnlineHelpKey(), parameter.getOnlineHelpValue());
                            messages.put(parameter.getDocumentationKey(), parameter.getDocumentationValue());
                            messages.put(parameter.getTitleKey(), parameter.getTitleValue());

                            if (parameter.getValidWhen() != null)
                            {
                                // this key needs to be fully qualified since the valid when value can be different
                                final String completeKeyPrefix = (normalize)
                                        ? useCase.getTitleKey() + '.' +
                                            page.getMessageKey() + '.' +
                                            action.getMessageKey() + '.' +
                                            parameter.getMessageKey()
                                        : parameter.getMessageKey();
                                messages.put(completeKeyPrefix + "_validwhen",
                                        "{0} is only valid when " + parameter.getValidWhen());
                            }

                            if (parameter.getOptionCount() > 0)
                            {
                                final List optionKeys = parameter.getOptionKeys();
                                final List optionValues = parameter.getOptionValues();

                                for (int m = 0; m < optionKeys.size(); m++)
                                {
                                    messages.put(optionKeys.get(m), optionValues.get(m));
                                    messages.put(optionKeys.get(m) + ".title", optionValues.get(m));
                                }
                            }
                        }

                        // EXCEPTION FORWARDS
                        final List exceptions = action.getActionExceptions();

                        if (normalize)
                        {
                            if (exceptions.isEmpty())
                            {
                                messages.put("exception.occurred", "{0}");
                            }
                            else
                            {
                                for (int l = 0; l < exceptions.size(); l++)
                                {
                                    final StrutsExceptionHandler exception = (StrutsExceptionHandler)exceptions.get(l);
                                    messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(), "{0}");
                                }
                            }
                        }
                        else
                        {
                            if (exceptions.isEmpty())
                            {
                                if (!action.isUseCaseStart())
                                {
                                    messages.put(action.getMessageKey() + ".exception", "{0} (java.lang.Exception)");
                                }
                            }
                            else
                            {
                                for (int l = 0; l < exceptions.size(); l++)
                                {
                                    final StrutsExceptionHandler exception = (StrutsExceptionHandler)exceptions.get(l);
                                    // we construct the key using the action message too because the exception can
                                    // belong to more than one action (therefore it cannot return the correct value
                                    // in .getExceptionKey())
                                    messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(),
                                            "{0} (" + exception.getExceptionType() + ")");
                                }
                            }
                        }
                    }
                }
            }
        }

        return messages;
    }
}
