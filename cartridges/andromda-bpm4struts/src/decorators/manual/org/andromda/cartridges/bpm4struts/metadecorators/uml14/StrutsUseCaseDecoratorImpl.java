package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.ActionStateDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.usecases.UseCase
 *
 *
 */
public class StrutsUseCaseDecoratorImpl extends StrutsUseCaseDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsUseCaseDecoratorImpl(org.omg.uml.behavioralelements.usecases.UseCase metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsUseCaseDecorator ...

    // ------------- relations ------------------
    protected ModelElement handleGetAsWorkflowState()
    {
        if (hasStereotype(Bpm4StrutsProfile.STEREOTYPE_USECASE))
        {
            final String name = getName();
            final UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
            final Collection allStates = model.getStateMachines().getState().refAllOfType();

            for (Iterator iterator = allStates.iterator(); iterator.hasNext();)
            {
                State state = (State) iterator.next();
                if (name.equalsIgnoreCase(state.getName()))
                    return state;
            }
        }
        return null;
    }

    protected ModelElement handleGetActivityGraph()
    {
        final Collection ownedElements = metaObject.getOwnedElement();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object ownedElement = iterator.next();
            if (ownedElement instanceof StateMachine)
                return (ModelElement) ownedElement;
        }
        return null;
    }

    protected ModelElement handleGetServlet()
    {
        final String name = getName();
        UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        Collection classifiers = model.getCore().getClassifier().refAllOfType();

        for (Iterator iterator = classifiers.iterator(); iterator.hasNext();)
        {
            Classifier undecoratedClassifier = (Classifier) iterator.next();
            DecoratorBase classifier = decoratedElement(undecoratedClassifier);
            if (classifier instanceof StrutsControllerDecorator)
            {
                StrutsControllerDecorator controller = (StrutsControllerDecorator)classifier;
                if (controller.getUseCase().getName().equalsIgnoreCase(name))
                    return undecoratedClassifier;
            }
        }
        return null;
    }

    protected ModelElement handleGetWorkflow()
    {
        // find a workflow which has an action state with the same name
        ActionStateDecorator actionState = findUseCaseAsActionStateDecorator(this);

        if (actionState == null)
            return null;

        StrutsActivityGraphDecorator activityGraph = (StrutsActivityGraphDecorator)actionState.getActivityGraph();
        return activityGraph.getWorkflow().getMetaObject();
    }

    private ActionStateDecorator findUseCaseAsActionStateDecorator(StrutsUseCaseDecorator useCase)
    {
        final String useCaseName = (useCase==null) ? null : useCase.getName();

        if (useCaseName == null)
            return null;

        UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        Collection allActionStates = model.getActivityGraphs().getActionState().refAllOfType();

        for (Iterator iterator = allActionStates.iterator(); iterator.hasNext();)
        {
            ActionState actionState = (ActionState) iterator.next();
            DecoratorBase decoratedActionState = decoratedElement(actionState);
            if (decoratedActionState instanceof StrutsUseCaseDecorator)
            {
                StrutsActionStateDecorator strutsActionStateDecorator = (StrutsActionStateDecorator)decoratedActionState;
                if (useCaseName.equalsIgnoreCase(strutsActionStateDecorator.getName()))
                    return strutsActionStateDecorator;
            }
        }

        return null;
    }


    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // it must have at least one activity graph
        final Collection ownedElements = getOwnedElement();
        int activityGraphCount = 0;
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object ownedElement = iterator.next();
            if (ownedElement instanceof ActivityGraph)
            {
                activityGraphCount++;
            }
        }
        if (activityGraphCount != 1)
            throw new DecoratorValidationException(this, "You need to one and only one activity graph for this use-case");

        final State state = findAsWorkflowState();

        if (state != null)
        {
            final StateMachine stateMachineMetaObject = getStateMachine();

            if (stateMachineMetaObject instanceof StrutsStateMachineDecorator)
            {
                final StrutsStateMachineDecorator stateMachine = (StrutsStateMachineDecorator)stateMachineMetaObject;

                final Collection outgoing = state.getOutgoing();
                final Collection finalStates = stateMachine.getFinalStates();

                int stateExitCount = outgoing.size();
                int finalStateCount = finalStates.size();

                // there must be as many final states as there are outgoing transitions from the workflow state
                if (stateExitCount != finalStateCount)
                    throw new DecoratorValidationException(this,
                        "There are only "+finalStateCount+" final states, according to the parent workflow you would need to have "+stateExitCount);
            }
        }
*/

    }
}
