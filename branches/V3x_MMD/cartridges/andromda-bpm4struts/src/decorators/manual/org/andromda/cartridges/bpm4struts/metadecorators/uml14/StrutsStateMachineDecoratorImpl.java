package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;

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

    // ------------- relations ------------------
    protected ModelElement handleGetUseCaseContext()
    {
        return (UseCase)getContext();
    }

    public Set getForwardTransitions()
    {
        final Collection actionStates = getActionStates();
        final Set forwardTransitions = new LinkedHashSet();

        // 1. collect all the transition incoming to the action states
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            final ActionState actionState = (ActionState) actionStateIterator.next();
            final Collection incomingTransitions = actionState.getIncoming();
            for (Iterator transitionIterator = incomingTransitions.iterator(); transitionIterator.hasNext();)
            {
                final Transition transition = (Transition) transitionIterator.next();
                forwardTransitions.add(transition);
            }
        }
        // 1. done ---------------------------------------------------

        // 2. take all the targets of the final states in this state machine (lookup in workflow)
        final UseCase contextUseCase = (UseCase)getContext();
        // find this use-case in the workflow (as a state)
        final State useCaseState = MetaDecoratorUtil.findUseCaseInWorkflow(contextUseCase); // todo

        forwardTransitions.addAll(useCaseState.getOutgoing());
        // 2. done ------------------------------------------------------------------------------

        return forwardTransitions;
    }

    public Set getChoiceTransitions()
    {
        final Collection choices = getChoicePseudostates();
        final Set choiceTransitions = new LinkedHashSet();

        for (Iterator choiceIterator = choices.iterator(); choiceIterator.hasNext();)
        {
            final Pseudostate pseudostate = (Pseudostate) choiceIterator.next();
            final Collection outgoingTransitions = pseudostate.getOutgoing();
            for (Iterator transitionIterator = outgoingTransitions.iterator(); transitionIterator.hasNext();)
            {
                final Transition transition = (Transition) transitionIterator.next();
                choiceTransitions.add(transition);
            }
        }
        return choiceTransitions;
    }

    public void validate() throws DecoratorValidationException
    {
        // there must be one and only one initial state

        // this state machine should be owned by a use-case
    }

}
