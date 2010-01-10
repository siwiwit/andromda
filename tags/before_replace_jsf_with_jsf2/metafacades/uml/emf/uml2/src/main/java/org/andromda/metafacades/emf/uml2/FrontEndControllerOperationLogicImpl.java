package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndPseudostate;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateVertexFacade;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndControllerOperation.
 *
 * @see org.andromda.metafacades.uml.FrontEndControllerOperation
 */
public class FrontEndControllerOperationLogicImpl
    extends FrontEndControllerOperationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerOperationLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Override to return the owner's package as the package name.
     *
     * @see org.andromda.metafacades.emf.uml2.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        final ClassifierFacade owner = this.getOwner();
        return owner != null ? owner.getPackageName() : "";
    }

    /**
     * @return getOwner() instanceof FrontEndController
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isOwnerIsController()
     */
    protected boolean handleIsOwnerIsController()
    {
        return this.getOwner() instanceof FrontEndController;
    }

    /**
     * @return formFields
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getFormFields()
     */
    protected List handleGetFormFields()
    {
        final Map formFieldsMap = new LinkedHashMap();

        // for quick lookup we use a hashset for the argument names, we only
        // consider parameters with a name
        // which is also present in this set
        final Set argumentNames = new LinkedHashSet();
        final Collection arguments = this.getArguments();
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final ModelElementFacade element = (ModelElementFacade)argumentIterator.next();
            argumentNames.add(element.getName());
        }

        // - get all actions deferring to this operation
        final List deferringActions = this.getDeferringActions();
        for (final Iterator iterator = deferringActions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();

            // store the action parameters
            final List actionFormFields = action.getFormFields();
            for (final Iterator fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext();)
            {
                final FrontEndParameter parameter = (FrontEndParameter)fieldIterator.next();
                final String name = parameter.getName();

                // - only add if the parameter is an action parameter and its an
                // argument of this operation
                if (parameter.getAction() != null && argumentNames.contains(name))
                {
                    formFieldsMap.put(
                        name,
                        parameter);
                }
            }

            // get all forwards and overwrite when we find a table (or add when
            // not yet present)
            final List forwards = action.getActionForwards();
            for (final Iterator forwardIterator = forwards.iterator(); forwardIterator.hasNext();)
            {
                final FrontEndForward forward = (FrontEndForward)forwardIterator.next();

                // - only consider forwards directly entering a view
                if (forward.isEnteringView())
                {
                    final List viewVariables = forward.getForwardParameters();
                    for (final Iterator variableIterator = viewVariables.iterator(); variableIterator.hasNext();)
                    {
                        final FrontEndParameter viewVariable = (FrontEndParameter)variableIterator.next();
                        final String name = viewVariable.getName();
                        if (argumentNames.contains(name))
                        {
                            if (!formFieldsMap.containsKey(name) || viewVariable.isTable())
                            {
                                formFieldsMap.put(
                                    name,
                                    viewVariable);
                            }
                        }
                    }
                }
            }
        }

        // since all arguments need to be present we add those that haven't yet
        // been stored in the map
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final FrontEndParameter argument = (FrontEndParameter)argumentIterator.next();
            final String name = argument.getName();
            if (!formFieldsMap.containsKey(name))
            {
                formFieldsMap.put(
                    name,
                    argument);
            }
        }
        return new ArrayList(formFieldsMap.values());
    }

    /**
     * @return getOwner().getUseCase().getActivityGraph()
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getActivityGraph()
     */
    protected Object handleGetActivityGraph()
    {
        Object graph = null;

        final ClassifierFacade owner = this.getOwner();
        if (owner instanceof FrontEndController)
        {
            final FrontEndController controller = (FrontEndController)owner;
            final FrontEndUseCase useCase = controller.getUseCase();
            if (useCase != null)
            {
                graph = useCase.getActivityGraph();
            }
        }
        return graph;
    }

    /**
     * @return getActivityGraph().getActionStates().getControllerCalls().getContainerActions()
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getDeferringActions()
     */
    protected List handleGetDeferringActions()
    {
        final Collection deferringActions = new LinkedHashSet();

        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final Object actionStateObject = actionStateIterator.next();
                if (actionStateObject instanceof FrontEndActionState)
                {
                    final FrontEndActionState actionState = (FrontEndActionState)actionStateObject;
                    final Collection controllerCalls = actionState.getControllerCalls();
                    for (final Iterator controllerCallIterator = controllerCalls.iterator();
                        controllerCallIterator.hasNext();)
                    {
                        final OperationFacade operation = (OperationFacade)controllerCallIterator.next();
                        if (this.equals(operation))
                        {
                            deferringActions.addAll(actionState.getContainerActions());
                        }
                    }
                }
            }

            final Collection transitions = graph.getTransitions();
            for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                final FrontEndForward transition = (FrontEndForward)transitionIterator.next();
                final EventFacade event = transition.getTrigger();
                if (event instanceof FrontEndEvent)
                {
                    final FrontEndEvent trigger = (FrontEndEvent)event;
                    final FrontEndControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        // we have two types of controller calls: the ones in
                        // action states and the ones for decisions
                        final StateVertexFacade source = transition.getSource();
                        if (source instanceof FrontEndActionState)
                        {
                            final FrontEndActionState sourceActionState = (FrontEndActionState)source;
                            deferringActions.addAll(sourceActionState.getContainerActions());
                        }

                        // test for decision
                        final StateVertexFacade target = transition.getTarget();
                        if (target instanceof FrontEndPseudostate)
                        {
                            final FrontEndPseudostate targetPseudoState = (FrontEndPseudostate)target;
                            if (targetPseudoState.isDecisionPoint())
                            {
                                deferringActions.addAll(targetPseudoState.getContainerActions());
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(deferringActions);
    }

    /**
     * @return isAllArgumentsHaveFormFields
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isAllArgumentsHaveFormFields()
     */
    protected boolean handleIsAllArgumentsHaveFormFields()
    {
        final Collection arguments = this.getArguments();
        final Collection deferringActions = this.getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (final Iterator iterator = arguments.iterator(); iterator.hasNext() && allArgumentsHaveFormFields;)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            final String parameterName = parameter.getName();
            final ClassifierFacade parameterType = parameter.getType();
            final String parameterTypeName = parameterType != null ? parameterType.getFullyQualifiedName() : "";

            boolean actionMissingField = false;
            for (final Iterator actionIterator = deferringActions.iterator();
                actionIterator.hasNext() && !actionMissingField;)
            {
                final FrontEndAction action = (FrontEndAction)actionIterator.next();
                final Collection actionFormFields = action.getFormFields();

                boolean fieldPresent = false;
                for (final Iterator fieldIterator = actionFormFields.iterator();
                    fieldIterator.hasNext() && !fieldPresent;)
                {
                    final ParameterFacade field = (ParameterFacade)fieldIterator.next();
                    final ClassifierFacade fieldType = field.getType();
                    final String fieldTypeName = fieldType != null ? fieldType.getFullyQualifiedName() : "";
                    if (parameterName.equals(field.getName()) && parameterTypeName.equals(fieldTypeName))
                    {
                        fieldPresent = true;
                    }
                }
                actionMissingField = !fieldPresent;
            }
            allArgumentsHaveFormFields = !actionMissingField;
        }
        return allArgumentsHaveFormFields;
    }
}