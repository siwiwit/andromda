package org.andromda.core.metadecorators.uml14;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.usecases.UseCase
 *
 *
 */
public class UseCaseDecoratorImpl extends UseCaseDecorator
{
    // ---------------- constructor -------------------------------

    public UseCaseDecoratorImpl(org.omg.uml.behavioralelements.usecases.UseCase metaObject)
    {
        super(metaObject);
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
