package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.core.common.StringUtilsHelper;

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
    public String getFullPathName()
    {
        return '/' + StringUtilsHelper.toJavaClassName(getName());
    }

    public String getTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getFullyQualifiedName());
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String getFullPath()
    {
        return getPackageName().replace('.','/') + '/' + StringUtilsHelper.toJavaClassName(getName());
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
    // ------------- relations ------------------

    public java.lang.Object handleGetActivityGraph()
    {
        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof ActivityGraphFacade)
                return obj;
        }
        return null;
    }

    protected Collection handleGetUsers()
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
                formFieldsMap.put(trigger.getName(), trigger);
            }
        }
        return formFieldsMap.values();
    }
}
