package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;

import java.util.Collection;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Pseudostate
 *
 *
 */
public class StrutsPseudostateDecoratorImpl extends StrutsPseudostateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsPseudostateDecoratorImpl(org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsPseudostateDecorator ...

    // from org.omg.uml.behavioralelements.statemachines.StateVertex
    public Collection getOutgoing()
    {
        return DecoratorBase.decoratedElements(metaObject.getOutgoing());
    }
    // ------------- relations ------------------

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();

        if (isDecisionPoint().booleanValue())
        {
            // name cannot be null
            if ((name == null) || (name.trim().length() == 0))
                throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

            // must have at least 2 outgoing transitions
            final Collection outgoing = getOutgoing();
            if (outgoing.size() < 2)
                throw new DecoratorValidationException(this,
                    "A decision point needs at least two outgoing transitions");

            for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
            {
                Transition transition = (Transition) iterator.next();
                if (transition.getGuard() == null)
                    throw new DecoratorValidationException(this,
                        "All transitions going out of a choice pseudostate (decision point) must have a guard");
            }
        }
*/

/* CLASSCASTEXCEPTION
        // the name must be unique for pseudo states in the use-case
        // the name of the action state must be unique in the use-case state machine
        final StateMachineDecorator stateMachine = (StateMachineDecorator) getContainer().getStateMachine();
        final Collection pseudostates = stateMachine.getPseudostates();
        int nameCount = 0;
        for (Iterator iterator = pseudostates.iterator(); iterator.hasNext();)
        {
            Pseudostate pseudostate = (Pseudostate) iterator.next();
            if (name.equals(pseudostate.getName()))
                nameCount++;
        }

        if (nameCount > 1)
            throw new DecoratorValidationException(this,
                "There are " + nameCount + " pseudo states found with this names, please give unique names");
*/

    }
}
