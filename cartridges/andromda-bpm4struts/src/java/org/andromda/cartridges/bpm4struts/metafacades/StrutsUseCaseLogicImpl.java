package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
        extends StrutsUseCaseLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
{
    // ---------------- constructor -------------------------------

    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    public String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
    }

    public String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String handleGetOnlineHelpKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".online.help";
    }

    public String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No use-case documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    public String handleGetActionPath()
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

    public String handleGetActionPathRoot()
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

    public String handleGetFullFormBeanPath()
    {
        return '/' + getFormBeanPackageName().replace('.', '/') + '/' + StringUtilsHelper.upperCamelCaseName(getName()) + "Form";
    }

    public String handleGetFormBeanName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String handleGetFormBeanClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName()) + "Form";
    }

    public String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    public String handleGetFormBeanPackageName()
    {
        return getPackageName();
    }

    public String handleGetActionRoles()
    {
        final Collection users = getAllUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    // ------------- relations ------------------
    public Collection getOperations()
    {
        return Collections.EMPTY_LIST;
    }

    protected Collection handleGetAllServices()
    {
        // find all controller dependencies on <<Service>> classes
        final Collection useCases = getAllUseCases();
        final Collection services = new HashSet();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase) iterator.next();
            services.addAll(useCase.getController().getServices());
        }
        return services;
    }

    public java.lang.Object handleGetActivityGraph()
    {
        /*
         * In case there is a tagged value pointing to an activity graph, and this graph is found,
         * then return it.
         */
        final Object activity = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USECASE_ACTIVITY);
        if (activity != null)
        {
            String activityName = String.valueOf(activity.toString());
            Collection activityGraphs = getModel().getAllActivityGraphs();
            for (Iterator iterator = activityGraphs.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                if (obj instanceof StrutsActivityGraph)
                {
                    StrutsActivityGraph activityGraph = (StrutsActivityGraph) obj;
                    if (activityName.equalsIgnoreCase(activityGraph.getName()))
                        return activityGraph;
                }
            }
        }

        /*
         * Otherwise just take the first one in this use-case's namespace.
         */
        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsActivityGraph)
                return obj;
        }

        /*
         * Nothing was found
         */
        return null;
    }

    protected Collection handleGetUsers()
    {
        final Collection usersList = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsUser)
                usersList.add(classifier);
        }

        return usersList;
    }

    protected Collection handleGetAllUsers()
    {
        final Collection allUsersList = new HashSet();
        final Collection associatedUsers = getUsers();
        for (Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            StrutsUser user = (StrutsUser) iterator.next();
            collectUsers(user, allUsersList);
        }
        return allUsersList;
    }

    private void collectUsers(StrutsUser user, Collection users)
    {
        if (!users.contains(user))
        {
            users.add(user);

            Collection childUsers = user.getGeneralizedByUsers();
            for (Iterator iterator = childUsers.iterator(); iterator.hasNext();)
            {
                StrutsUser childUser = (StrutsUser) iterator.next();
                collectUsers(childUser, users);
            }
        }
    }

    protected Collection handleGetPages()
    {
        final Collection pagesList = new ArrayList();
        StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            final Collection allActionStates = graph.getActionStates();
            for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
            {
                Object actionState = actionStateIterator.next();
                if (actionState instanceof StrutsJsp)
                    pagesList.add(actionState);
            }
        }
        return pagesList;
    }

    protected Collection handleGetAllPages()
    {
        final Collection pagesList = new ArrayList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected Collection handleGetAllUseCases()
    {
        final Collection useCases = new ArrayList();

        for (Iterator iterator = getModel().getAllUseCases().iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUseCase)
                useCases.add(object);
        }
        return useCases;
    }

    protected Object handleGetController()
    {
        StrutsActivityGraph graph = getActivityGraph();
        return (graph == null) ? null :graph.getController();
    }

    protected Collection handleGetFormFields()
    {
        final Collection formFields = new ArrayList(); // parameter names are supposed to be unique

        final Collection pages = getPages();
        for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp) pageIterator.next();
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

    protected Collection handleGetFinalStates()
    {
        final Collection finalStatesList = new ArrayList();
        final Collection allFinalStates = getModel().getAllFinalStates();

        for (Iterator iterator = allFinalStates.iterator(); iterator.hasNext();)
        {
            ModelElementFacade modelElement = (ModelElementFacade) iterator.next();
            if (getName().equalsIgnoreCase(modelElement.getName()))
                finalStatesList.add(modelElement);
        }

        return finalStatesList;
    }

    public boolean handleIsValidationRequired()
    {
        final Collection allPages = this.getAllPages();
        for (Iterator iterator = allPages.iterator(); iterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp) iterator.next();
            if (jsp.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsApplicationValidationRequired()
    {
        final Collection useCases = this.getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase) iterator.next();
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
            StrutsJsp jsp = (StrutsJsp) pageIterator.next();
            actions.addAll(jsp.getActions());
        }
        actions.add(getActivityGraph().getFirstAction());

        return actions;
    }

    private Collection pageVariables = null;

    protected Collection handleGetPageVariables()
    {
        if (pageVariables == null)
        {
            Map pageVariableMap = new HashMap();

            /**
             * page variables can occur twice or more in the usecase in case their
             * names are the same for different forms, storing them in a map
             * solves this issue because those names do not have the action-name prefix
             */
            Collection pages = getPages();
            for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
            {
                StrutsJsp jsp = (StrutsJsp) pageIterator.next();
                Collection variables = jsp.getPageVariables();
                for (Iterator variableIterator = variables.iterator(); variableIterator.hasNext();)
                {
                    StrutsParameter variable = (StrutsParameter) variableIterator.next();
                    pageVariableMap.put(variable.getName(), variable);
                }
            }
            pageVariables = pageVariableMap.values();
        }
        return pageVariables;
    }

    public boolean handleIsApplicationUseCase()
    {
        return hasStereotype(Bpm4StrutsProfile.STEREOTYPE_APPLICATION);
    }
}
