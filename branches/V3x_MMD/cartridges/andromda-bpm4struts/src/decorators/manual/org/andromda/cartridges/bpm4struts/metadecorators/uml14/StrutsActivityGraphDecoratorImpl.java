package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Util;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ActivityGraph
 *
 *
 */
public class StrutsActivityGraphDecoratorImpl extends StrutsActivityGraphDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsActivityGraphDecoratorImpl(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActivityGraphDecorator ...

    public java.util.Set getActionStates()
    {
        return Util.getSubvertices(this, Util.ACTIONSTATE_FILTER);
    }

    public org.omg.uml.behavioralelements.statemachines.Pseudostate getInitialState()
    {
        final Collection initialStates = Util.getSubvertices(this, Util.INITIALSTATE_FILTER);
        return ((initialStates == null) || (initialStates.isEmpty()))
            ? null : (Pseudostate) initialStates.iterator().next();
    }

    public java.util.Set getPossibleChoiceValues()
    {
        final Collection choices = Util.getSubvertices(this, Util.CHOICEPSEUDOSTATE_FILTER);
        final Set choiceValues = new LinkedHashSet();

        for (Iterator choiceIterator = choices.iterator(); choiceIterator.hasNext();)
        {
            Pseudostate pseudostate = (Pseudostate) choiceIterator.next();
            Collection outgoingTransitions = pseudostate.getOutgoing();
            for (Iterator transitionIterator = outgoingTransitions.iterator(); transitionIterator.hasNext();)
            {
                Transition transition = (Transition) transitionIterator.next();
                choiceValues.add(transition.getGuard().getName());
            }
        }

        return choiceValues;
    }

    // ------------- relations ------------------

}
