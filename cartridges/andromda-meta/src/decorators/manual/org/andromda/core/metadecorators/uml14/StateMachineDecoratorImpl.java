package org.andromda.core.metadecorators.uml14;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.StateMachine
 *
 *
 */
public class StateMachineDecoratorImpl extends StateMachineDecorator
{
    // ---------------- constructor -------------------------------

    public StateMachineDecoratorImpl(org.omg.uml.behavioralelements.statemachines.StateMachine metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StateMachineDecorator ...

    public java.util.Collection getActionStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ActionState;
            }
        };

        return getSubvertices(filter);
    }

    public java.util.Collection getChoicePseudostates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_CHOICE.equals(((Pseudostate)(object)).getKind()));
            }
        };

        return getSubvertices(filter);
    }

    public java.util.Collection getFinalStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof FinalState;
            }
        };

        return getSubvertices(filter);
    }

    public java.util.Collection getInitialStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_INITIAL.equals(((Pseudostate)(object)).getKind()));
            }
        };

        return getSubvertices(filter);
    }

    public java.util.Collection getObjectFlowStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ObjectFlowState;
            }
        };

        return getSubvertices(filter);
    }

    protected Collection getSubvertices(CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) metaObject.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
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
