package org.andromda.cartridges.bpm4struts.metafacades;

import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.andromda.core.metafacade.MetafacadeBase;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
 */
public class StrutsUserLogicImpl
        extends StrutsUserLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
{
    private Actor actor = null;

    // ---------------- constructor -------------------------------
    
    public StrutsUserLogicImpl(java.lang.Object metaObject)
    {
        super(metaObject);
        this.actor = (Actor)metaObject;
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsUser ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser#getRole()()
     */
    public java.lang.String getRole()
    {
        return getName().toLowerCase();
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser#getGeneralizedByUsers()
     */
    public java.util.Collection handleGetGeneralizedByUsers()
    {
        final Collection parentActors = new LinkedList();
        final Collection generalizations = actor.getGeneralization();

        for (Iterator iterator = generalizations.iterator(); iterator.hasNext();)
        {
            Generalization generalization = (Generalization) iterator.next();
            GeneralizableElement parent = generalization.getParent();
            MetafacadeBase decoratedParent = shieldedElement(parent);
            if (decoratedParent instanceof StrutsUser)
                parentActors.add(parent);
        }

        return parentActors;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser#getGeneralizedUsers()
     */
    public java.util.Collection handleGetGeneralizedUsers()
    {
        final Collection childActors = new LinkedList();
        final Collection generalizations = actor.getGeneralization();

        for (Iterator iterator = generalizations.iterator(); iterator.hasNext();)
        {
            Generalization generalization = (Generalization) iterator.next();
            GeneralizableElement child = generalization.getChild();
            MetafacadeBase decoratedChild = shieldedElement(child);
            if (decoratedChild instanceof StrutsUser)
                childActors.add(child);
        }

        return childActors;
    }

}
