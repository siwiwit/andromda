package org.andromda.core.uml14;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Extends the UMLDefaultHelper with a set of operations that are useful
 * for exploring the dynamic parts of UML v1.4 based object models.
 *
 * @author <a href="mailto:draftdog@users.sourceforge.net">Wouter Zoons</a>
 */
public class UMLDynamicHelper extends UMLDefaultHelper
{
    /**
     * Returns a collection containing all the activity graphs found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActivityGraph</code>.
     *
     * @return the ActivityGraph instances found in the UML model
     */
    public Collection getAllActivityGraphs()
    {
        return model.getActivityGraphs().getActivityGraph().refAllOfType();
    }

    /**
     * Returns a collection containing all the state machines found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.StateMachine</code>.
     *
     * @return the StateMachine instances found in the UML model
     */
    public Collection getAllStateMachines()
    {
        return model.getStateMachines().getStateMachine().refAllOfType();
    }

    /**
     * Returns a collection containing all the use-cases found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.usecases.UseCase</code>.
     *
     * @return the UseCase instances found in the UML model
     */
    public Collection getAllUseCases()
    {
        return model.getUseCases().getUseCase().refAllOfType();
    }

    /**
     * Returns a collection containing all the action states found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActionState</code>.
     *
     * @return the ActionState instances found in the UML model
     */
    public Collection getAllActionStates()
    {
        return model.getActivityGraphs().getActionState().refAllOfType();
    }

    /**
     * Returns a collection containing all the states found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.State</code>.
     *
     * @return the State instances found in the UML model
     */
    public Collection getAllStates()
    {
        return model.getStateMachines().getState().refAllOfType();
    }

    /**
     * Returns a collection containing all the state machines found associated
     * with the argument use-case.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.StateMachine</code>.
     *
     * @param useCase the use-case to query, may not be <code>null</code>
     * @return the ActivityGraph instances found associated with the argument use-case
     */
    public Collection getStateMachines(UseCase useCase)
    {
        return filter(useCase.getOwnedElement(), stateMachineFilter);
    }

    /**
     * Returns the state machine (such as an activity graph) to which the argument state vertex belongs.
     *
     * @param stateVertex may not be <code>null</code>
     * @return the associated state machine
     */
    public StateMachine getStateMachineContext(StateVertex stateVertex)
    {
        return stateVertex.getContainer().getStateMachine();
    }

    /**
     * Returns the collection of FinalState instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.FinalState</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the FinalState instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.FinalState
     */
    public Collection getFinalStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, finalStateFilter);
    }

    /**
     * Returns the collection of Transition instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the Transition instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Transition
     */
    public Collection getTransitions(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, transitionFilter);
    }

    /**
     * Returns the collection of Pseudostate instances of kind 'initial'
     * found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the collection of initial states found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getInitialStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, initialStateFilter);
    }

    /**
     * Returns the collection of ObjectFlowState instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the FinalState instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
     */
    public Collection getObjectFlowStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, objectFlowStateFilter);
    }

    public Collection getGuardedTransitions(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, guardedTransitionFilter);
    }

    public Collection getTriggeredTransitions(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, triggeredTransitionFilter);
    }

    /**
     * Returns a Collection containing the Pseudostate instances of kind 'choice'
     * that are model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the Pseudostate instances of kind 'choice'
     *    found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getChoices(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, choicePseudostateFilter);
    }

    /**
     * Returns a Collection containing the action states that are
     * model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActionState</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the ActionState instances found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.activitygraphs.ActionState
     */
    public Collection getActionStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, actionStateFilter);
    }

    /**
     * Returns a collection of vertices that are contained in the argument
     * StateMachine.
     * <p>
     * The CollectionFilter decides which vertices are being filtered out.
     *
     * @param stateMachine The graph where the look for vertices, may not be <code>null</code>
     * @param collectionFilter the filter that decides which vertices to ignore, may not be <code>null</code>
     * @return A Collection containing only
     *    <code>org.omg.uml.behavioralelements.statemachines.StateVertex</code> instances.
     * @see org.omg.uml.behavioralelements.statemachines.StateVertex
     */
    public Collection getSubvertices(StateMachine stateMachine, CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) stateMachine.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
    }

    /**
     * Filters the specified collection using the argument filter.
     *
     * @param collection The collection to filter, may not be <code>null</code>
     * @param collectionFilter The filter to apply, may not be <code>null</code>
     * @return A subset of the argument collection, filtered out as desired
     */
    public Collection filter(Collection collection, CollectionFilter collectionFilter)
    {
        final LinkedList filteredCollection = new LinkedList();
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

    /**
     * A filter used to keep only decision points.
     *
     * @see #isDecisionPoint(Object object)
     */
    public final CollectionFilter decisionPointsFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isDecisionPoint(object);
            }
        };

    /**
     * A filter used to keep only merge points.
     *
     * @see #isMergePoint(Object object)
     */
    public final CollectionFilter mergePointsFilter =
        new CollectionFilter() {
            public boolean accept(Object object)
            {
                return isMergePoint(object);
            }
        };

    /**
     * A filter used to keep only ObjectFlowState instances.
     */
    public final CollectionFilter objectFlowStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isObjectFlowState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'initial'.
     */
    public final CollectionFilter initialStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isInitialState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'choice'.
     */
    public final CollectionFilter choicePseudostateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isChoice(object);
            }
        };

    /**
     * A filter used to keep only ActionStates.
     */
    public final CollectionFilter actionStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isActionState(object);
            }
        };

    /**
     * A filter used to keep only FinalStates.
     */
    public final CollectionFilter finalStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isFinalState(object);
            }
        };

    /**
     * A filter used to keep only Transitions.
     */
    public final CollectionFilter transitionFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isTransition(object);
            }
        };

    /**
     * A filter used to keep only StateMachines.
     */
    public final CollectionFilter stateMachineFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isStateMachine(object);
            }
        };

    /**
     * A filter used to keep only transitions with a guard.
     */
    public final CollectionFilter guardedTransitionFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isGuardedTransition(object);
            }
        };

    /**
     * A filter used to keep only transitions with a trigger.
     */
    public final CollectionFilter triggeredTransitionFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isTriggeredTransition(object);
            }
        };

    /**
     * Returns the first transition from the argument State that is incoming into another State,
     * there may be several <i>hops</code> (such as merge points) in between.
     *
     * @param state a State, may not be <code>null</code>
     * @return the last transition before entering a new state
     */
    public Transition getNextStateTransition(State state)
    {
        return getNextStateTransitionForStateVertex(state);
    }

    /**
     * Returns the first transition from the argument State that is incoming into another State,
     * there may be several <i>hops</code> (such as merge points) in between.
     *
     * @param pseudostate a Pseudostate, may not be <code>null</code>
     * @return the last transition before entering a new state
     */
    public Transition getNextStateTransition(Pseudostate pseudostate)
    {
        return getNextStateTransitionForStateVertex(pseudostate);
    }

    /**
     * Returns the first transition from the argument Transition that is incoming into another State,
     * there may be several <i>hops</code> (such as merge points) in between.
     * <p>
     * If the argument transition targets a State, itself will be returned.
     *
     * @param transition a Transition, may not be <code>null</code>
     * @return the last transition before entering a new state
     */
    public Transition getNextStateTransition(Transition transition)
    {
        Transition nextTransition = null;
        StateVertex target = transition.getTarget();

        if (isState(target))
        {
            nextTransition = transition;
        }
        else
        {
            nextTransition = getNextStateTransition((Pseudostate)target);
        }

        return nextTransition;
    }

    /**
     * Returns the first transition from the argument StateVertex that is incoming into another State,
     * there may be several <i>hops</code> (such as merge points) in between.
     *
     * @param stateVertex a state vertex, may not be <code>null</code>
     * @return the last transition before entering a new state
     */
    private Transition getNextStateTransitionForStateVertex(StateVertex stateVertex)
    {
        Transition nextStateTransition = null;

        Collection outgoing = stateVertex.getOutgoing();
        if (outgoing.size() > 0)
        {
            Transition transition = (Transition)outgoing.iterator().next();
            StateVertex target = transition.getTarget();

            if (isState(target) || isDecisionPoint(target))
            {
                nextStateTransition = transition;
            }
            else
            {
                nextStateTransition = getNextStateTransition((Pseudostate)target);
            }
        }

        return nextStateTransition;
    }

    /**
     * Returns the state entered after following the argument transition, there may be several other transitions
     * before the state is actually entered.
     *
     * @param transition a Transition, may not be <code>null</code>
     * @return the first State which is entered when following the argument transition
     */
    public State getStateTarget(Transition transition)
    {
        StateVertex stateVertex = transition.getTarget();

        if (isState(stateVertex))
        {
            return (State)stateVertex;
        }
        else
        {
            Transition nextTransition = getNextStateTransition((Pseudostate)stateVertex);
            return (nextTransition == null) ? null : getStateTarget(nextTransition);
        }
    }

    /**
     * Gets the collection of transitions with a guard that are outgoing to the argument state.
     *
     * @param state a State, may not be <code>null</code>
     * @return a Collection containing Transitions that have a guard, never <code>null</code>
     */
    public Collection getNextGuardedTransitions(State state)
    {
        return getNextGuardedTransitionsForStateVertex(state);
    }

    /**
     * Gets the collection of transitions with a guard that are outgoing to the argument pseudostate.
     *
     * @param pseudostate a Pseudostate, may not be <code>null</code>
     * @return a Collection containing Transitions that have a guard, never <code>null</code>
     */
    public Collection getNextGuardedTransitions(Pseudostate pseudostate)
    {
        return getNextGuardedTransitionsForStateVertex(pseudostate);
    }

    /**
     * Gets the collection of transitions with a guard that are outgoing to the argument state-vertex.
     *
     * @param stateVertex a StateVertex, may not be <code>null</code>
     * @return a Collection containing Transitions that have a guard, never <code>null</code>
     */
    private Collection getNextGuardedTransitionsForStateVertex(StateVertex stateVertex)
    {
        Collection transitions = null;

        Transition transition = getNextStateTransitionForStateVertex(stateVertex);
        Guard guard = transition.getGuard();

        if (guard == null)
        {
            StateVertex target = transition.getTarget();
            if (isDecisionPoint(target))
            {
                transitions = target.getOutgoing();
            }
            else
            {
                transitions = Collections.EMPTY_LIST;
            }
        }
        else
        {
            transitions = Collections.singleton(transition);
        }

        return transitions;
    }

    /**
     * Gets the collection of transitions with a trigger that are outgoing to the argument state.
     *
     * @param state a State, may not be <code>null</code>
     * @return a Collection containing Transitions that have a trigger, never <code>null</code>
     */
    public Collection getNextTriggeredTransitions(State state)
    {
        return getNextTriggeredTransitionForStateVertex(state);
    }

    /**
     * Gets the collection of transitions with a trigger that are outgoing to the argument pseudostate.
     *
     * @param pseudostate a Pseudostate, may not be <code>null</code>
     * @return a Collection containing Transitions that have a trigger, never <code>null</code>
     */
    public Collection getNextTriggeredTransitions(Pseudostate pseudostate)
    {
        return getNextTriggeredTransitionForStateVertex(pseudostate);
    }

    /**
     * Gets the collection of transitions with a trigger that are outgoing to the argument state-vertex.
     *
     * @param stateVertex a StateVertex, may not be <code>null</code>
     * @return a Collection containing Transitions that have a trigger, never <code>null</code>
     */
    private Collection getNextTriggeredTransitionForStateVertex(StateVertex stateVertex)
    {
        Collection transitions = null;

        Transition transition = getNextStateTransitionForStateVertex(stateVertex);
        Event trigger = transition.getTrigger();

        if (trigger == null)
        {
            StateVertex target = transition.getTarget();
            if (isDecisionPoint(target))
            {
                transitions = target.getOutgoing();
            }
            else
            {
                transitions = Collections.EMPTY_LIST;
            }
        }
        else
        {
            transitions = Collections.singleton(transition);
        }

        return transitions;
    }


    /**
     * Returns <code>true</code> if the argument is a Transition instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Transition instance, <code>false</code>
     *    in any other case.
     */
    public boolean isTransition(Object object)
    {
        return (object instanceof Transition);
    }

    /**
     * Returns <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     *    in any other case.
     */
    public boolean isActivityGraph(Object object)
    {
        return (object instanceof ActivityGraph);
    }

    /**
     * Returns <code>true</code> if the argument is an ActionState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActionState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isActionState(Object object)
    {
        return (object instanceof ActionState);
    }

    /**
     * Returns <code>true</code> if the argument is a State instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a State instance, <code>false</code>
     *    in any other case.
     */
    public boolean isState(Object object)
    {
        return (object instanceof State);
    }

    /**
     * Returns <code>true</code> if the argument is a FinalState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a FinalState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isFinalState(Object object)
    {
        return (object instanceof FinalState);
    }

    /**
     * Returns <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isObjectFlowState(Object object)
    {
        return (object instanceof ObjectFlowState);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'choice', <code>false</code> in any other case.
     * <p>
     * Please note that as well decision points as merges are represented using
     * a choice pseudostate. Their difference lies in the number of incoming and
     * outgoing transitions.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public boolean isChoice(Object object)
    {
        return PseudostateKindEnum.PK_CHOICE.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', it has
     * multiple incoming transitions, but only a single outgoing transition.
     * <p>
     * Such a pseudostate would be used as a merge state in a UML diagram.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 incoming transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public boolean isMergePoint(Object object)
    {
        boolean isMergePoint = false;

        if (isChoice(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            isMergePoint = true;
            isMergePoint = isMergePoint && (pseudostate.getIncoming().size() > 1);
            isMergePoint = isMergePoint && (pseudostate.getOutgoing().size() == 1);
        }

        return isMergePoint;
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', it has
     * multiple outgoing transition, but only a single incoming transition.
     * <p>
     * Such a pseudostate would be used as a decision point in a UML diagram.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 outgoing transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public boolean isDecisionPoint(Object object)
    {
        boolean isDecisionPoint = false;

        if (isChoice(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            isDecisionPoint = true;
            isDecisionPoint = isDecisionPoint && (pseudostate.getIncoming().size() == 1);
            isDecisionPoint = isDecisionPoint && (pseudostate.getOutgoing().size() > 1);
        }

        return isDecisionPoint;
    }

    /**
     * Returns a Collection containing the decision points that are
     * model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the decision points found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     * @see #isDecisionPoint(Object object)
     */
    public Collection getDecisionPoints(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, decisionPointsFilter);
    }

    /**
     * Returns a Collection containing the merge points that are
     * model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the merge points found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     * @see #isMergePoint(Object object)
     */
    public Collection getMergePoints(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, mergePointsFilter);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'initial', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public boolean isInitialState(Object object)
    {
        return PseudostateKindEnum.PK_INITIAL.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'join', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'join', <code>false</code> in any other case.
     */
    public boolean isJoin(Object object)
    {
        return PseudostateKindEnum.PK_JOIN.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'fork', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'fork', <code>false</code> in any other case.
     */
    public boolean isFork(Object object)
    {
        return PseudostateKindEnum.PK_FORK.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Transition instance
     * with a trigger.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Transition instance
     *    with a trigger, <code>false</code> in any other case.
     */
    public boolean isTriggeredTransition(Object object)
    {
        return ( (object instanceof Transition) && (((Transition)object).getTrigger() != null) );
    }

    /**
     * Returns <code>true</code> if the argument is a Transition instance
     * with a guard.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Transition instance
     *    with a guard, <code>false</code> in any other case.
     */
    public boolean isGuardedTransition(Object object)
    {
        return ( (object instanceof Transition) && (((Transition)object).getGuard() != null) );
    }
    /**
     * Returns <code>true</code> if the argument is a StateVertex instance
     * of kind 'fork', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a StateVertex instance
     *    of kind 'fork', <code>false</code> in any other case.
     */
    public boolean isStateVertex(Object object)
    {
        return (object instanceof StateVertex);
    }

    /**
     * Returns <code>true</code> if the argument is a StateMachine,
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a StateMachine,
     *    <code>false</code> in any other case.
     */
    public boolean isStateMachine(Object object)
    {
        return (object instanceof StateMachine);
    }

    /**
     * Returns <code>true</code> if the argument is a UseCase,
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a UseCase,
     *    <code>false</code> in any other case.
     */
    public boolean isUseCase(Object object)
    {
        return (object instanceof UseCase);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    <code>false</code> in any other case.
     */
    public boolean isPseudostate(Object object)
    {
        return (object instanceof Pseudostate);
    }

    /**
     * Returns the kind of Pseudostate the argument is, if the argument is no
     * Pseudostate instance this method will return <code>null</code>.
     * <p>
     * In short, possible return values are
     * <ul>
     *    <li>PseudostateKindEnum.PK_CHOICE
     *    <li>PseudostateKindEnum.PK_DEEP_HISTORY
     *    <li>PseudostateKindEnum.PK_FORK
     *    <li>PseudostateKindEnum.PK_INITIAL
     *    <li>PseudostateKindEnum.PK_JOIN
     *    <li>PseudostateKindEnum.PK_JUNCTION
     *    <li>PseudostateKindEnum.PK_SHALLOW_HISTORY
     *    <li><code>null</code>
     * </ul>
     * @param object an argument to test, may be <code>null</code>
     * @return the pseudostate kind, or <code>null</code>
     */
    protected PseudostateKind getPseudostateKind(Object object)
    {
        return isPseudostate(object) ? ((Pseudostate) object).getKind() : null;
    }

}
