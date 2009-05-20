package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.TransitionFacade;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 * @author Bob Fields
 */
public class StateVertexFacadeLogicImpl
        extends StateVertexFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateVertexFacadeLogicImpl(StateVertex metaObject,
                                      String context)
    {
        super(metaObject, context);
    }

    protected StateMachine handleGetStateMachine()
    {
        // throws NullPointer if metaObject has no Container... Need to check for null on return.
        if (metaObject.getContainer()==null)
        {
            return null;
        }
        return metaObject.getContainer().getStateMachine();
    }

    protected CompositeState handleGetContainer()
    {
        return metaObject.getContainer();
    }

    protected Collection handleGetIncomings()
    {
        return metaObject.getIncoming();
    }

    protected Collection handleGetOutgoings()
    {
        return metaObject.getOutgoing();
    }

    /**
     * @return getStateMachine
     */
    public Object handleGetValidationOwner()
    {
        return getStateMachine();
    }

    protected Partition handleGetPartition()
    {
        Partition thePartition = null;

        final StateMachine stateMachine = metaObject.getContainer().getStateMachine();
        if (stateMachine instanceof ActivityGraph)
        {
            final ActivityGraph activityGraph = (ActivityGraph)stateMachine;
            final Collection<Partition> partitions = activityGraph.getPartition();
            for (final Iterator<Partition> partitionIterator = partitions.iterator(); partitionIterator.hasNext() && thePartition == null;)
            {
                final Partition partition = (Partition)partitionIterator.next();
                if (partition.getContents().contains(metaObject))
                {
                    thePartition = partition;
                }
            }
        }

        return thePartition;
    }

    /**
     * @return Outgoings FrontEndActions
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    //@Override
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoings = getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoings.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }
        // TODO StrutsFinalStateLogicImpl uses getIncomings INSTEAD OF getOutgoings - why?
        /*final Collection<TransitionFacade> incomings = getIncomings();
        for (final Iterator<TransitionFacade> iterator = incomings.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }*/
        return actions;
    }
}