package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;

import java.util.Collections;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
 */
public class StrutsUserLogicImpl
        extends StrutsUserLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
{
    // ---------------- constructor -------------------------------
    
    public StrutsUserLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
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

    public String getMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    public String getMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    // ------------- relations ------------------

    public java.util.Collection handleGetGeneralizedByUsers()
    {
/*
        final Collection parentActors = new LinkedList();
        final Collection generalizations = getGeneralization();

        for (Iterator iterator = generalizations.iterator(); iterator.hasNext();)
        {
            Generalization generalization = (Generalization) iterator.next();
            GeneralizableElement parent = generalization.getParent();
            MetafacadeBase decoratedParent = shieldedElement(parent);
            if (decoratedParent instanceof StrutsUser)
                parentActors.add(parent);
        }

        return parentActors;
*/
        return Collections.EMPTY_LIST;
    }

    public java.util.Collection handleGetGeneralizedUsers()
    {
/*
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
*/
        return Collections.EMPTY_LIST;
    }

}
