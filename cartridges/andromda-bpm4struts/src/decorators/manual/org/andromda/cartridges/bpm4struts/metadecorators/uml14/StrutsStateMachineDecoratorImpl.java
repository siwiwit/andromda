package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.common.CollectionFilter;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;
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
public class StrutsStateMachineDecoratorImpl extends StrutsStateMachineDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsStateMachineDecoratorImpl(org.omg.uml.behavioralelements.statemachines.StateMachine metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsStateMachineDecorator ...

    public Pseudostate getInitialState()
    {
        final Collection initialStates = getInitialStates();
        return (initialStates.isEmpty()) ? null : (Pseudostate)initialStates.iterator().next();
    }

    // ------------- relations ------------------
    public Set getForwardTransitionNames()
    {
        final Collection actionStates = getActionStates();
        final Set forwardTransitionNames = new LinkedHashSet();

        // 1. collect all the transitions incoming to the action states
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            final ActionState actionState = (ActionState) actionStateIterator.next();
            final Collection incomingTransitions = actionState.getIncoming();
            for (Iterator transitionIterator = incomingTransitions.iterator(); transitionIterator.hasNext();)
            {
                StrutsTransitionDecorator transition = (StrutsTransitionDecorator)DecoratorBase.decoratedElement((Transition)transitionIterator.next());
                String triggerName = transition.getTriggerName();
                if (triggerName == null)
                    triggerName = transition.getFinalTarget().getName();
                forwardTransitionNames.add(triggerName);
            }
        }
        // 1. done ---------------------------------------------------

        // 2. take all the targets of the final states in this state machine (lookup in workflow)
        final UseCase contextUseCase = (UseCase)getContext();
        if (contextUseCase != null)
        {
            // find this use-case in the workflow (as a state)
            final StrutsUseCaseDecorator useCaseDecorator =
                (StrutsUseCaseDecorator)DecoratorBase.decoratedElement(contextUseCase);
            final State useCaseState = useCaseDecorator.findAsWorkflowState();

            if (useCaseState!=null)
            {
                final Collection outgoingTransitions = DecoratorBase.decoratedElements(useCaseState.getOutgoing());
                for (Iterator iterator = outgoingTransitions.iterator(); iterator.hasNext();)
                {
                    final StrutsTransitionDecorator transition = (StrutsTransitionDecorator) iterator.next();
                    String triggerName = transition.getTriggerName();
                    if (triggerName == null)
                        triggerName = transition.getFinalTarget().getName();
                    forwardTransitionNames.add(triggerName);
                }
            }
        }
        // 2. done ------------------------------------------------------------------------------

        return forwardTransitionNames;
    }

    protected ModelElement handleGetInitialState()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_INITIAL.equals(((Pseudostate)(object)).getKind()));
            }
        };
        final Collection initialStates = getSubvertices(filter);
        return (initialStates.isEmpty()) ? null : (ModelElement)initialStates.iterator().next();
    }

    protected Collection handleGetDecisionPoints()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate) &&
                    ( (PseudostateKindEnum.PK_CHOICE.equals(((Pseudostate)(object)).getKind())) ||
                      (PseudostateKindEnum.PK_JUNCTION.equals(((Pseudostate)(object)).getKind())) );
            }
        };
        return getSubvertices(filter);
    }

    public Set getOutgoingDecisionPointTransitions()
    {
        final Collection decisions = getDecisionPoints();
        final Collection choiceTransitions = new LinkedHashSet();

        for (Iterator choiceIterator = decisions.iterator(); choiceIterator.hasNext();)
        {
            final Pseudostate pseudostate = (Pseudostate) choiceIterator.next();
            final Collection outgoingTransitions = pseudostate.getOutgoing();
            for (Iterator transitionIterator = outgoingTransitions.iterator(); transitionIterator.hasNext();)
            {
                final Transition transition = (Transition) transitionIterator.next();
                choiceTransitions.add(transition);
            }
        }
        return new LinkedHashSet(DecoratorBase.decoratedElements(choiceTransitions));
    }

    protected ModelElement handleGetUseCase()
    {
        return (UseCase)getNamespace();
    }

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/* ENDLESS LOOP


        // there must be one and only one initial state
        final Collection initialStates = getInitialStates();
        if (initialStates.size() == 0)
            throw new DecoratorValidationException(this, "No initial state could be located in this state machine");

        if (initialStates.size() > 1)
            throw new DecoratorValidationException(this, "More than one initial state could be located in this state machine, only one is allowed");
*/
    }

}
