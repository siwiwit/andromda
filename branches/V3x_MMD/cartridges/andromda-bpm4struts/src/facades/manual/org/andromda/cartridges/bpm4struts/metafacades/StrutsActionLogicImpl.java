package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;


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

    // ---------------- constructor -------------------------------
    
    public StrutsActionLogicImpl(java.lang.Object metaObject)
    {
        super(metaObject);
        this.transition = (Transition)metaObject;
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsAction ...

    private String getActionName()
    {
        return transition.getSource().getName() + ' ' + transition.getTrigger().getName();
    }

    private ClassifierFacade getContextClass()
    {
        Classifier contextClass = (Classifier)transition.getStateMachine().getContext();
        return (ClassifierFacade)shieldedElement(contextClass);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getType()()
     */
    public java.lang.String getType()
    {
        ClassifierFacade context = getContextClass();
        return context.getPackageName() + '.' + StringUtilsHelper.toJavaClassName(getActionName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getForwardNamesToPathsMap()()
     */
    public java.util.Map getForwardNamesToPathsMap()
    {
// TODO: put your implementation here.

// Dummy return value, just that the file compiles
        return null;
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
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getPath()()
     */
    public java.lang.String getPath()
    {
        return '/' + StringUtilsHelper.toJavaClassName(getActionName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getRoles()()
     */
    public java.lang.String getRoles()
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

}
