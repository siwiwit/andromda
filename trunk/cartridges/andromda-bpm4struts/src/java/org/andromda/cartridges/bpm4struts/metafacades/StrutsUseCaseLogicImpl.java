package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;

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
    private Object activityGraph = null;
    private Collection allServices = null;
    private Collection allUseCases = null;
    private Collection allUsers = null;
    private Object controller = null;
    private Collection formFields = null;
    private Collection pages = null;
    private Collection allPages = null;
    private Collection users = null;
    private Collection finalStates = null;
    private String actionRoles = null;

    // ---------------- constructor -------------------------------
    
    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    public String getTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String getActionPath()
    {
        return getActivityGraph().getFirstAction().getActionPath();
    }

    public String getActionPathRoot()
    {
        return getActivityGraph().getFirstAction().getActionPathRoot();
    }

    public String getFullFormBeanPath()
    {
        return '/' + getFormBeanPackageName().replace('.', '/') + '/' + StringUtilsHelper.toJavaClassName(getName()) + "Form";
    }

    public String getFormBeanName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String getFormBeanClassName()
    {
        return StringUtilsHelper.toJavaClassName(getName()) + "Form";
    }

    public String getFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    public String getFormBeanPackageName()
    {
        return getController().getPackageName();
    }

    public String getActionRoles()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionRoles != null) return actionRoles;

        final Collection users = getAllUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return (actionRoles = StringUtilsHelper.separate(rolesBuffer.toString(), ","));
    }

    // ------------- relations ------------------
    protected Collection handleGetAllServices()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && allServices != null) return allServices;

        // find all controller dependencies on <<Service>> classes
        final Collection useCases = getAllUseCases();
        final Collection services = new HashSet();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase) iterator.next();
            services.addAll(useCase.getController().getServices());
        }
        return allServices = services;
    }

    public java.lang.Object handleGetActivityGraph()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && activityGraph != null) return activityGraph;

        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsActivityGraph)
                return activityGraph = obj;
        }
        return null;
    }

    protected Collection handleGetUsers()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && users != null) return users;

        final Collection usersList = new LinkedList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsUser)
                usersList.add(classifier);
        }

        return users = usersList;
    }

    protected Collection handleGetAllUsers()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && allUsers != null) return allUsers;

        final Collection allUsersList = new HashSet();
        final Collection associatedUsers = getUsers();
        for (Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            StrutsUser user = (StrutsUser) iterator.next();
            collectUsers(user, allUsersList);
        }
        return allUsers = allUsersList;
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
        if (Bpm4StrutsProfile.ENABLE_CACHE && pages != null) return pages;

        final Collection pagesList = new LinkedList();
        final Collection allActionStates = getActivityGraph().getActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pages = pagesList;
    }

    protected Collection handleGetAllPages()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && allPages != null) return allPages;

        final Collection pagesList = new LinkedList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return allPages = pagesList;
    }

    protected Collection handleGetAllUseCases()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && allUseCases != null) return allUseCases;

        final Collection useCases = new LinkedList();

        for (Iterator iterator = getModel().getAllUseCases().iterator(); iterator.hasNext();)
        {
            Object object = (Object) iterator.next();
            if (object instanceof StrutsUseCase)
                useCases.add(object);
        }
        return allUseCases = useCases;
    }

    protected Object handleGetController()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && controller != null) return controller;
        return controller = getActivityGraph().getController();
    }

    protected Collection handleGetFormFields()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && formFields != null) return formFields;

        final Map formFieldsMap = new HashMap();
        final Collection transitions = getActivityGraph().getTransitions();
        for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
        {
            Object transitionObject = iterator.next();
            if (transitionObject instanceof StrutsAction)
            {
                Collection parameters = ((StrutsAction) transitionObject).getActionFormFields();
                for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                {
                    ParameterFacade parameter = (ParameterFacade) parameterIterator.next();
                    formFieldsMap.put(parameter.getName(), parameter);
                }
            }
        }
        return formFields = formFieldsMap.values();
    }

    protected Collection handleGetFinalStates()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && finalStates != null) return finalStates;

        final Collection finalStatesList = new LinkedList();
        final Collection allFinalStates = getModel().getAllFinalStates();

        for (Iterator iterator = allFinalStates.iterator(); iterator.hasNext();)
        {
            ModelElementFacade modelElement = (ModelElementFacade) iterator.next();
            if (getName().equalsIgnoreCase(modelElement.getName()))
                finalStatesList.add(modelElement);
        }

        return finalStates = finalStatesList;
    }
}
