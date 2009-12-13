package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.ActorFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ActorFacade.
 *
 * @see org.andromda.metafacades.uml.ActorFacade
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ActorFacadeLogicImpl(
        final org.eclipse.uml2.Actor metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }

    /**
     * @return generalizedActors
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedActors()
     */
    protected List handleGetGeneralizedActors()
    {
        final List generalizedActors = new ArrayList();

        final Collection parentActors = this.getGeneralizations();
        for (final Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedActors.add(object);
            }
        }
        return generalizedActors;
    }

    /**
     *
     * @return generalizedByActors
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedByActors()
     */
    protected List handleGetGeneralizedByActors()
    {
        final List generalizedByActors = new ArrayList();

        final Collection specializedActors = this.getSpecializations();
        for (final Iterator iterator = specializedActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedByActors.add(object);
            }
        }
        return generalizedByActors;
    }
}