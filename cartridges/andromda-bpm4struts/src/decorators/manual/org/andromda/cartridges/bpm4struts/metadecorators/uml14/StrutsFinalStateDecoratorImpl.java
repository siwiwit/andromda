package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.FinalState
 *
 *
 */
public class StrutsFinalStateDecoratorImpl extends StrutsFinalStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsFinalStateDecoratorImpl(org.omg.uml.behavioralelements.statemachines.FinalState metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsFinalStateDecorator ...

    // ------------- relations ------------------

    public ModelElement handleGetUseCase()
    {
        final String name = getName();
        UmlPackage model = MetaDecoratorUtil.getModel(this.metaObject);
        Collection useCases = model.getUseCases().getUseCase().refAllOfType();

        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            if (useCase.getName().equalsIgnoreCase(name))
                return useCase;
        }

        return null;
    }

    public void validate() throws DecoratorValidationException
    {
/*
        // the name of this final state must correspond with a transition in a workflow, going out of the use-case
        // that is represented by its container state machine (only if there is more than one outgoing transition)
        StateMachine stateMachineMetaObject = getStateMachine();

        if (stateMachineMetaObject instanceof StrutsStateMachineDecorator)
        {
            StrutsStateMachineDecorator stateMachine = (StrutsStateMachineDecorator)stateMachineMetaObject;
            StrutsUseCaseDecorator useCase = (StrutsUseCaseDecorator)stateMachine.getUseCaseContext();

            if (!useCase.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_USECASE).booleanValue())
                return; // ignore this state vertex

            State state = useCase.findAsWorkflowState();

            if (state != null)
            {
                final Collection outgoing = state.getOutgoing();
                final Collection finalStates = stateMachine.getFinalStates();

                int finalStateCount = finalStates.size();

                // the name cannot be empty and must match a transition if there are more than one
                if (finalStateCount > 1)
                {
                    final String name = getName();
                    if ((name == null) || (name.trim().length() == 0))
                        throw new DecoratorValidationException(this, "In the workflow Name may not be empty or only contain whitespace");

                    boolean nameFound = false;
                    for (Iterator iterator = outgoing.iterator(); (iterator.hasNext() && !nameFound);)
                    {
                        Transition transition = (Transition) iterator.next();
                        if (name.equalsIgnoreCase(transition.getName()))
                            nameFound = true;
                    }

                    if (!nameFound)
                        throw new DecoratorValidationException(this,
                            "Unable to locate an outgoing transition from "+state.getName()+" with name "+name);
                }
            }
        }
*/
    }
}

