package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.StateMachineDecorator;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsViewDecoratorImpl extends StrutsViewDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsViewDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsViewDecorator ...

    /**
     * Returns a new collection containing all the classes associated with this one.
     */
    private Collection getAssociatedClasses()
    {
        final Collection associatedClasses = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndDecorator associationEnd =
                (AssociationEndDecorator) DecoratorBase.decoratedElement((AssociationEnd) iterator.next());
            associatedClasses.add(associationEnd.getOtherEnd().getParticipant());
        }
        return associatedClasses;
    }

    /**
     * Returns a new collection containing all the model classes associated with this one.
     */
    private Collection getAssociatedModelClasses()
    {
        final Collection associatedClasses = getAssociatedClasses();
        for (Iterator iterator = associatedClasses.iterator(); iterator.hasNext();)
        {
            Classifier classifier = (Classifier) iterator.next();
            ClassifierDecorator classifierDecorator = (ClassifierDecorator) DecoratorBase.decoratedElement(classifier);
            if (!classifierDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                iterator.remove();
        }
        return associatedClasses;
    }

    /**
     * Returns a collection of all the action states in the activity graphs for the use-case context.
     */
    private Collection getPossibleActionStates()
    {
        final Collection graphs = new LinkedList();

        // collect all the graphs
        final Collection controllers = getFormBean().getServlets();
        for (Iterator iterator = controllers.iterator(); iterator.hasNext();)
        {
            StrutsControllerDecorator controller = (StrutsControllerDecorator) iterator.next();
            graphs.addAll(controller.getUseCase().getOwnedElement());
        }

        // collect all the action states from these graphs
        final Collection actionStates = new LinkedList();
        for (Iterator iterator = graphs.iterator(); iterator.hasNext();)
        {
            ModelElement modelElement = (ModelElement) iterator.next();
            if (modelElement instanceof ActivityGraph)
            {
                StateMachineDecorator stateMachine = (StateMachineDecorator) DecoratorBase.decoratedElement(modelElement);
                actionStates.addAll(stateMachine.getActionStates());
            }
        }
        return actionStates;
    }

    public Collection getTriggers()
    {
        return DecoratorBase.decoratedElements(getActionState().getOutgoing());
    }

    // ------------- relations ------------------
    public ActionState getActionState()
    {
        final String actionStateName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE);
        final Collection actionStates = getPossibleActionStates();
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            ActionState actionState = (ActionState) actionStateIterator.next();
            if (actionStateName.equalsIgnoreCase(actionState.getName()))
                return actionState;
        }
        return null;
    }

    protected ModelElement handleGetFormBean()
    {
        final Collection models = getAssociatedModelClasses();
        if (models.isEmpty())
            return null;
        else
            return (ModelElement) models.iterator().next();
    }


    protected ModelElement handleGetServlet()
    {
        final StrutsStateMachineDecorator stateMachine = (StrutsStateMachineDecorator)DecoratorBase.decoratedElement(getActionState().getContainer().getStateMachine());
        final UseCase useCase = stateMachine.getUseCase();
        final Collection controllers = getFormBean().getServlets();

        for (Iterator iterator = controllers.iterator(); iterator.hasNext();)
        {
            StrutsControllerDecorator controller = (StrutsControllerDecorator) iterator.next();
/*
            UseCase controllerUseCase = controller.getUseCase();
            if (controllerUseCase == useCase)   // .equals throws a ClassCastException ??!! identity will do too
*/
                return controller.metaObject;
        }
        return null;
    }

    /**
     *
     */
    public java.util.Collection handleGetInputFields()
    {
        final Collection attributes = getAttributes();
        final Collection inputFields = new LinkedList();

        for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            Attribute attribute = (Attribute) iterator.next();
            AttributeDecorator attributeDecorator = (AttributeDecorator) DecoratorBase.decoratedElement(attribute);
            if (attributeDecorator instanceof StrutsInputFieldDecorator)
            {
                inputFields.add(attribute);
            }
        }

        return inputFields;
    }

    protected Collection handleGetResetInputFields()
    {
        final Collection attributes = getAttributes();
        final Collection resetInputFields = new LinkedList();

        for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            Attribute attribute = (Attribute) iterator.next();
            AttributeDecorator attributeDecorator = (AttributeDecorator) DecoratorBase.decoratedElement(attribute);
            if (attributeDecorator instanceof StrutsInputFieldDecorator)
            {
                ClassifierDecorator classifierDecorator = (ClassifierDecorator) DecoratorBase.decoratedElement(attributeDecorator.getType());
                final String fqType = classifierDecorator.getFullyQualifiedName();
                if ("boolean".equals(fqType))
                {
                    resetInputFields.add(attribute);
                }
            }
        }

        return resetInputFields;
    }

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // detect associated classes
        final Collection associatedClasses = getAssociatedClasses();
        if (associatedClasses.isEmpty())
            throw new DecoratorValidationException(this,
                "No classes are associated with this view class, you will need to associate a class with the " +
                Bpm4StrutsProfile.STEREOTYPE_MODEL + " stereotype");

        // must be associated to one form bean
        final Collection associatedModelClasses = getAssociatedModelClasses();
        if (associatedModelClasses.isEmpty())
        {
            if (associatedClasses.size() == 1)
                throw new DecoratorValidationException(this,
                    "You have a class associated with this view class, but it requires the " +
                    Bpm4StrutsProfile.STEREOTYPE_MODEL + " stereotype");
            else
                throw new DecoratorValidationException(this,
                    "You have classes associated with this view class, but one of them requires the " +
                    Bpm4StrutsProfile.STEREOTYPE_MODEL + " stereotype");
        }

        // check for tagged value
        final String actionStateName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE);
        if (actionStateName == null)
        {
            throw new DecoratorValidationException(this,
                "You will need to add the tagged value " + Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE + " to this class " +
                "pointing to an action state in the context use-case");
        }
        else
        {
            // making sure there is only one action state with the specified name
            final Collection actionStates = getPossibleActionStates();
            int actionStateCount = 0;
            for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
            {
                ActionState actionState = (ActionState) iterator.next();
                if (actionStateName.equals(actionState.getName()))
                    actionStateCount++;
            }

            switch (actionStateCount)
            {
                case 0:
                    final Set actionStateNames = new LinkedHashSet();
                    for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
                    {
                        ActionState actionState = (ActionState) iterator.next();
                        actionStateNames.add(actionState.getName());
                    }
                    throw new DecoratorValidationException(this,
                        "The action state specified by the tagged value " + Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE +
                        "could not be located. Allowed values are: " + actionStateNames);
                case 1:
                    break; // this is what we want
                default :
                    throw new DecoratorValidationException(this,
                        "A valid action state has been specified but there are " + actionStateCount +
                        " action states with that name for all controller use-case activity graphs, the name must be unique");
            }
        }
*/
    }
}
