package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metadecorators.uml14.*;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;

import java.util.*;


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

    private Collection getSupplierDependencies()
    {
        final UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        final Collection allDependencies = model.getCore().getDependency().refAllOfType();
        final Collection supplierDependencies = new LinkedList();

        for (Iterator iterator = allDependencies.iterator(); iterator.hasNext();)
        {
            Dependency dependency = (Dependency) iterator.next();
            Object supplier = dependency.getSupplier().iterator().next();
            if (metaObject.equals(supplier))
                supplierDependencies.add(dependency);
        }
        return supplierDependencies;
    }

    /**
     * Returns a new collection containing all the model classes associated with this one.
     */
    private Collection getAssociatedModelClasses()
    {
        final Collection associatedClasses = new LinkedList();
        Collection supplierDependencies = getSupplierDependencies();
        for (Iterator dependencyIterator=supplierDependencies.iterator(); dependencyIterator.hasNext();)
        {
            Object element = dependencyIterator.next();
            Dependency dep = (Dependency) element;
            ModelElement client = (ModelElement) dep.getClient().iterator().next();
            ModelElementDecorator clientDecorator = (ModelElementDecorator)decoratedElement(client);
            if (clientDecorator instanceof ClassifierDecorator && clientDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL))
                associatedClasses.add(clientDecorator);
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
            graphs.addAll(controller.getUseCase().metaObject.getOwnedElement());
        }

        // collect all the action states from these graphs
        final Collection actionStates = new LinkedList();
        for (Iterator iterator = graphs.iterator(); iterator.hasNext();)
        {
            ModelElement modelElement = (ModelElement) iterator.next();
            if (modelElement instanceof ActivityGraph)
            {
                ActivityGraphDecorator activityGraph = (ActivityGraphDecorator) decoratedElement(modelElement);
                actionStates.addAll(activityGraph.getActionStates());
            }
        }
        return actionStates;
    }

    // todo: this method in fact returns transitions -- Wouter Zoons
    public Collection getTriggers()
    {
        return decoratedElements(getActionState().metaObject.getOutgoing());
    }

    public Map getTriggerMessages()
    {
        Map triggerMessages = new TreeMap();
        String keyPrefix = StringUtilsHelper.separate(getName(), ".") + '.';
        Collection triggers = getTriggers();
        for (Iterator iterator = triggers.iterator(); iterator.hasNext();)
        {
            StrutsTransitionDecorator trigger = (StrutsTransitionDecorator) iterator.next();
            String key = (keyPrefix + StringUtilsHelper.separate(trigger.getTriggerName(), ".")).toLowerCase();
            String val = StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(trigger.getTriggerName(), " "));
            triggerMessages.put(key, val);
        }
        return triggerMessages;
    }

    public String getTitleMessageKey()
    {
        return (StringUtilsHelper.separate(getName(), ".") + ".title").toLowerCase();
    }

    public String getTitleMessageValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(getName(), " "));
    }

    public String getFullPathName()
    {
        return '/' + getFormBean().getPackageName().replace('.','/') + '/' + getName();
    }

    // ------------- relations ------------------
    public ModelElement handleGetActionState()
    {
        final String actionStateName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE);
        final Collection actionStates = getPossibleActionStates();
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            ActionStateDecorator actionStateDecorator = (ActionStateDecorator) actionStateIterator.next();
            ActionState actionState = (ActionState)actionStateDecorator.getMetaObject();
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
            return ((ClassifierDecorator) models.iterator().next()).getMetaObject();
    }


    protected ModelElement handleGetServlet()
    {
        final StrutsActivityGraphDecorator activityGraph = (StrutsActivityGraphDecorator)getActionState().getActivityGraph();
        final UseCase useCase = activityGraph.getUseCase().metaObject;
        final Collection controllers = getFormBean().getServlets();

        for (Iterator iterator = controllers.iterator(); iterator.hasNext();)
        {
            StrutsControllerDecorator controller = (StrutsControllerDecorator) iterator.next();
//            UseCase controllerUseCase = controller.getUseCase();
//            if (controllerUseCase == useCase)   // .equals throws a ClassCastException ??!! identity will do too: todo

                return controller.getMetaObject();
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
            AttributeDecorator attribute = (AttributeDecorator) iterator.next();
            if (attribute instanceof StrutsInputFieldDecorator)
            {
                inputFields.add(attribute.getMetaObject());
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
            AttributeDecorator attribute = (AttributeDecorator) iterator.next();
            if (attribute instanceof StrutsInputFieldDecorator)
            {
                ClassifierDecorator classifierDecorator = attribute.getType();
                final String fqType = classifierDecorator.getFullyQualifiedName();
                if ("boolean".equals(fqType))
                {
                    resetInputFields.add(attribute.getMetaObject());
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
