package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

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
    public String getTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getFullyQualifiedName());
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String getActionPath()
    {
        return getActivityGraph().getFirstAction().getActionPath();
    }

    public String getFullFormBeanPath()
    {
        return '/' + getFormBeanPackageName().replace('.','/') + '/' + StringUtilsHelper.toJavaClassName(getName()) + "Form";
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

    public String getPackagePath()
    {
        return '/' + getPackageName().replace('.','/');
    }

    // ------------- relations ------------------
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
        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsActivityGraph)
                return obj;
        }
        return null;
    }

    protected Collection handleGetUsers()
    {
        final Collection users = new LinkedList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getType();
            if (classifier instanceof StrutsUser)
                users.add(classifier);
        }

        return users;
    }

    protected Collection handleGetAllUsers()
    {
        final Collection users = new LinkedList();
        final Collection allActors = getModel().getAllActors();

        for (Iterator actorIterator = allActors.iterator(); actorIterator.hasNext();)
        {
            Object actor = shieldedElement(actorIterator.next());
            if (actor instanceof StrutsUser)
                users.add(actor);
        }
        return users;
    }

    protected Collection handleGetPages()
    {
        final Collection pages = new LinkedList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pages.add(actionState);
        }
        return pages;
    }

    protected Collection handleGetAllUseCases()
    {
        return getModel().getAllUseCases();
    }

    protected Object handleGetController()
    {
        return getActivityGraph().getController();
    }

    protected Collection handleGetFormFields()
    {
        final Map formFieldsMap = new HashMap();
        final Collection transitions = getActivityGraph().getTransitions();
        for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                Collection parameters = trigger.getParameters();
                for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                {
                    ParameterFacade parameter = (ParameterFacade) parameterIterator.next();
                    formFieldsMap.put(parameter.getName(), parameter);
                }
            }
        }
        return formFieldsMap.values();
    }
}
