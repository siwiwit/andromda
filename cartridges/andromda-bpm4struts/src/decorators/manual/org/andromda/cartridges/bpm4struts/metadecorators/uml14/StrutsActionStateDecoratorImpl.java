package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ActionState
 *
 *
 */
public class StrutsActionStateDecoratorImpl extends StrutsActionStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsActionStateDecoratorImpl(org.omg.uml.behavioralelements.activitygraphs.ActionState metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionStateDecorator ...

    // ------------- relations ------------------
    protected Collection handleGetTriggerTransitions()
    {
        return metaObject.getOutgoing();
    }

    protected ModelElement handleGetJsp()
    {
        UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        Collection umlClasses = model.getCore().getUmlClass().refAllOfType();

        for (Iterator iterator = umlClasses.iterator(); iterator.hasNext();)
        {
            Classifier undercoratedClassifier = (Classifier) iterator.next();
            DecoratorBase classifier = decoratedElement(undercoratedClassifier);

            if (classifier instanceof StrutsViewDecorator)
            {
                StrutsViewDecorator view = (StrutsViewDecorator)classifier;
                // todo: find a more stable way
                if (view.getActionState().getName().equalsIgnoreCase(metaObject.getName()))
                {
                    return undercoratedClassifier;
                }
            }
        }
        return null;
    }
    // ------------- validation ------------------
/*
    public void validate() throws DecoratorValidationException
    {
        System.out.println("StrutsActionStateDecoratorImpl.validate");

        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            validationError("Name may not be empty or only contain whitespace");

        // the name of the action state must be unique in the use-case state machine
        final ActivityGraphDecorator stateMachine =
                (ActivityGraphDecorator) DecoratorBase.decoratedElement(metaObject.getContainer().getStateMachine());

        final Collection actionStates = stateMachine.getActionStates();
        int nameCount = 0;
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            ActionState actionState = (ActionState) iterator.next();
            if (name.equals(actionState.getName()))
                nameCount++;
        }

        if (nameCount > 1)
            validationError("There are " + nameCount + " action states found with this name, please give unique names");

        // there must be at least one incoming transition
        if (metaObject.getIncoming().isEmpty())
            validationError("Miracle action coming out of nowhere. " +
                    "There must be at least one transition going into this action state");

        // there must be at least one outgoing transition
        if (metaObject.getOutgoing().isEmpty())
            validationError("Black hole action: there must be at least one transition going " +
                    "out of this action state, you might consider using a final state.");

        // if more than one outgoing transition, they must all have triggers
        Collection outgoing = metaObject.getOutgoing();
        if (outgoing.size() > 1)
        {
            for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
            {
                Transition transition = (Transition) iterator.next();
                if (transition.getTrigger() == null)
                {
                    validationError("If an action state has more than 1 outgoing transition, " +
                            "they must all have triggers.");
                    break;
                }
            }
        }
    }
*/
}
