package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class UseCaseFacadeLogicImpl
       extends UseCaseFacadeLogic
       implements org.andromda.metafacades.uml.UseCaseFacade
{
    // ---------------- constructor -------------------------------
    
    public UseCaseFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.UseCase metaObject, String context)
    {
        super (metaObject, context);
    }
    
    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class UseCaseDecorator ...

    protected Collection handleGetStateMachines()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ActionState;
            }
        };

        return getSubGraphs(filter);
    }

    protected Collection handleGetActivityGraphs()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ActionState;
            }
        };

        return getSubGraphs(filter);
    }

    private Collection getSubGraphs(CollectionFilter collectionFilter)
    {
        return filter(metaObject.getOwnedElement(), collectionFilter);
    }

    private Collection filter(Collection collection, CollectionFilter collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.accept(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    // ------------- relations ------------------
    
}
