package org.andromda.cartridges.bpm4struts.metafacades;

import org.omg.uml.UmlPackage;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsModel
 */
public class StrutsModelLogicImpl
        extends StrutsModelLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsModel
{
    private UmlPackage model = null;

    // ---------------- constructor -------------------------------
    
    public StrutsModelLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
        this.model = (UmlPackage)metaObject;
    }
    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsModel#getUsers()
     */
    public java.util.Collection handleGetUsers()
    {
        final Collection users = new LinkedList();
        final Collection allActors = model.getUseCases().getActor().refAllOfType();

        for (Iterator actorIterator = allActors.iterator(); actorIterator.hasNext();)
        {
            Object actor = shieldedElement(actorIterator.next());
            if (actor instanceof StrutsUser)
                users.add(actor);
        }
        return users;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsModel#getPages()
     */
    public java.util.Collection handleGetPages()
    {
        final Collection pages = new LinkedList();
        final Collection allActionStates = model.getUseCases().getActor().refAllOfType();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pages.add(actionState);
        }
        return pages;
    }

}
