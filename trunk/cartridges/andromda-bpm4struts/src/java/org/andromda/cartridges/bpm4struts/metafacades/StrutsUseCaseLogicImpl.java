package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLProfile;

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


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
        extends StrutsUseCaseLogic
{
    // ---------------- constructor -------------------------------

    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
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

    protected String handleGetFullFormBeanPath()
    {
        return '/' + getFormBeanPackageName().replace('.', '/') + '/' +
                StringUtilsHelper.upperCamelCaseName(getName()) + "Form";
    }

    protected String handleGetFormBeanName()
    {
        return StringUtilsHelper.uncapitalize(getFormBeanClassName());
    }

    protected String handleGetFormBeanClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName()) + "Form";
    }

    protected String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    protected String handleGetFormBeanPackageName()
    {
        return getPackageName();
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

    // ------------- relations ------------------
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

    private Collection associatedUsers()
    {
        final Collection usersList = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsUser)
                usersList.add(classifier);
        }

        return usersList;
    }

    protected Collection handleGetUsers()
    {
        final Collection allUsersList = new HashSet();
        final Collection associatedUsers = associatedUsers();
        for (Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            StrutsUser user = (StrutsUser)iterator.next();
            collectUsers(user, allUsersList);
        }
        return allUsersList;
    }

    protected Collection handleGetAllUsers()
    {
        Collection allUsers = new ArrayList();
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

    private void collectUsers(StrutsUser user, Collection users)
    {
        if (!users.contains(user))
        {
            users.add(user);

            Collection childUsers = user.getGeneralizedByUsers();
            for (Iterator iterator = childUsers.iterator(); iterator.hasNext();)
            {
                StrutsUser childUser = (StrutsUser)iterator.next();
                collectUsers(childUser, users);
            }
        }
    }

    protected Collection handleGetPages()
    {
        Collection pagesList = null;

        StrutsActivityGraph graph = getActivityGraph();
        if (graph == null)
        {
            pagesList = Collections.EMPTY_LIST;
        }
        else
        {
            pagesList = getModel().getAllActionStatesWithStereotype(graph, Bpm4StrutsProfile.STEREOTYPE_VIEW);
        }
        return pagesList;
    }

    protected Collection handleGetAllPages()
    {
        final Collection pagesList = new ArrayList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = actionStateIterator.next();
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected Collection handleGetAllUseCases()
    {
        final Collection useCases = new ArrayList();

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

    protected Collection handleGetFormFields()
    {
        final Collection formFields = new ArrayList(); // parameter names are supposed to be unique

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

    protected Collection handleGetActions()
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

        return actions;
    }

    private Collection pageVariables = null;

    protected Collection handleGetPageVariables()
    {
        if (pageVariables == null)
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
            pageVariables = pageVariableMap.values();
        }
        return pageVariables;
    }

    protected boolean handleIsSecured()
    {
        return !hasStereotype(UMLProfile.STEREOTYPE_UNSECURED);
    }

    protected boolean handleIsApplicationUseCase()
    {
        return hasStereotype(Bpm4StrutsProfile.STEREOTYPE_APPLICATION);
    }

    protected Collection handleGetReferencingFinalStates()
    {
        final Collection referencingFinalStates = getModel().findFinalStatesWithNameOrHyperlink(this);
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

    // DefaultMutableTreeNode's isNodeAncestor does not work because of its specific impl.
    private boolean isNodeAncestor(UseCaseNode node, UseCaseNode ancestorNode)
    {
        if (node.getUseCase().equals(ancestorNode.getUseCase()))
        {
            return true;
        }
        while (node.getParent() != null)
        {
            node = (UseCaseNode)node.getParent();
            if (isNodeAncestor(node, ancestorNode))
            {
                return true;
            }
        }
        return false;
    }

    private UseCaseNode findNode(UseCaseNode root, StrutsUseCase useCase)
    {
        UseCaseNode useCaseNode = null;

        List nodeList = Collections.list(root.breadthFirstEnumeration());
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
}
