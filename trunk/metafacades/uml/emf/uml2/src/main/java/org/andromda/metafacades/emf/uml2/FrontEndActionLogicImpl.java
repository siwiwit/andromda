package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndAction.
 *
 * @see org.andromda.metafacades.uml.FrontEndAction
 * @author Bob Fields
 */
public class FrontEndActionLogicImpl
    extends FrontEndActionLogic
{
    private static final long serialVersionUID = -34319354954787916L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActionLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getSource()
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    protected Object handleGetInput()
    {
        Object input = null;
        final ModelElementFacade source = this.getSource();
        if (source instanceof PseudostateFacade)
        {
            final PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source instanceof FrontEndView)
            {
                input = source;
            }
        }
        return input;
    }

    /**
     * @return getTrigger().getParameters()
     * @see org.andromda.metafacades.uml.FrontEndAction#getParameters()
     */
    protected List handleGetParameters()
    {
        final EventFacade trigger = this.getTrigger();
        return trigger == null ? Collections.emptyList() : new ArrayList(trigger.getParameters());
    }

    /**
     * @param name
     * @return foundParameters
     * @see org.andromda.metafacades.uml.FrontEndAction#findParameter(String)
     */
    protected ParameterFacade handleFindParameter(final String name)
    {
        return (ParameterFacade)CollectionUtils.find(
            this.getParameters(),
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final ParameterFacade parameter = (ParameterFacade)object;
                    return StringUtils.trimToEmpty(parameter.getName()).equals(name);
                }
            });
    }

    /**
     * @return deferredOperation
     * @see org.andromda.metafacades.uml.FrontEndAction#getDeferredOperations()
     */
    protected List handleGetDeferredOperations()
    {
        final Collection deferredOperations = new LinkedHashSet();
        final FrontEndController controller = this.getController();
        if (controller != null)
        {
            final List actionStates = this.getActionStates();
            for (int ctr = 0; ctr < actionStates.size(); ctr++)
            {
                final FrontEndActionState actionState = (FrontEndActionState)actionStates.get(ctr);
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List transitions = this.getDecisionTransitions();
            for (int ctr = 0; ctr < transitions.size(); ctr++)
            {
                final FrontEndForward forward = (FrontEndForward)transitions.get(ctr);
                final FrontEndEvent trigger = forward.getDecisionTrigger();
                if (trigger != null)
                {
                    deferredOperations.add(trigger.getControllerCall());
                }
            }
        }
        return new ArrayList(deferredOperations);
    }

    /**
     * @return decisionTransitions
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTransitions()
     */
    protected List handleGetDecisionTransitions()
    {
        if (this.decisionTransitions == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(this.decisionTransitions);
    }

    /**
     * @return getActionForwards().getTargets()
     * @see org.andromda.metafacades.uml.FrontEndAction#getTargetViews()
     */
    protected List handleGetTargetViews()
    {
        final Collection targetViews = new LinkedHashSet();
        final Collection forwards = this.getActionForwards();
        for (final Iterator iterator = forwards.iterator(); iterator.hasNext();)
        {
            final FrontEndForward forward = (FrontEndForward)iterator.next();
            if (forward.isEnteringView())
            {
                targetViews.add(forward.getTarget());
            }
        }
        return new ArrayList(targetViews);
    }

    /**
     * All action states that make up this action, this includes all possible
     * action states traversed after a decision point too.
     */
    private Collection actionStates = null;

    /**
     * All transitions leading into either a page or final state that originated
     * from a call to this action.
     */
    private Map actionForwards = null;

    /**
     * All transitions leading into a decision point that originated from a call
     * to this action.
     */
    private Collection decisionTransitions = null;

    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection transitions = null;

    /**
     * Initializes all action states, action forwards, decision transitions and
     * transitions in one shot, so that they can be queried more effiencently
     * later on.
     */
    private void initializeCollections()
    {
        this.actionStates = new LinkedHashSet();
        this.actionForwards = new LinkedHashMap();
        this.decisionTransitions = new LinkedHashSet();
        this.transitions = new LinkedHashSet();
        this.collectTransitions(
            (TransitionFacade)this.THIS(),
            this.transitions);
    }

    /**
     * Recursively collects all action states, action forwards, decision
     * transitions and transitions.
     *
     * @param transition
     *            the current transition that is being processed
     * @param processedTransitions
     *            the set of transitions already processed
     */
    private void collectTransitions(
        final TransitionFacade transition,
        final Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);
        final StateVertexFacade target = transition.getTarget();
        if (target instanceof FrontEndView || target instanceof FrontEndFinalState)
        {
            if (!this.actionForwards.containsKey(transition.getTarget()))
            {
                this.actionForwards.put(
                    transition.getTarget(),
                    transition);
            }
        }
        else if (target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint())
        {
            this.decisionTransitions.add(transition);
            final Collection outcomes = target.getOutgoings();
            for (final Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                final TransitionFacade outcome = (TransitionFacade)iterator.next();
                this.collectTransitions(
                    outcome,
                    processedTransitions);
            }
        }
        else if (target instanceof FrontEndActionState)
        {
            this.actionStates.add(target);
            final FrontEndForward forward = ((FrontEndActionState)target).getForward();
            if (forward != null)
            {
                this.collectTransitions(
                    forward,
                    processedTransitions);
            }
        }
        else// all the rest is ignored but outgoing transitions are further
        // processed
        {
            final Collection outcomes = target.getOutgoings();
            for (final Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                final TransitionFacade outcome = (TransitionFacade)iterator.next();
                this.collectTransitions(
                    outcome,
                    processedTransitions);
            }
        }
    }

    /**
     * @return actionStates
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    protected List handleGetActionStates()
    {
        if (this.actionStates == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(this.actionStates);
    }

    /**
     * @return transitions
     * @see org.andromda.metafacades.uml.FrontEndAction#getTransitions()
     */
    protected List handleGetTransitions()
    {
        if (this.transitions == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(this.transitions);
    }

    /**
     * @return actionForwards.values()
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionForwards()
     */
    protected List handleGetActionForwards()
    {
        if (this.actionForwards == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(this.actionForwards.values());
    }

    /**
     * @return getFrontEndActivityGraph().getController()
     * @see org.andromda.metafacades.uml.FrontEndAction#getController()
     */
    protected Object handleGetController()
    {
        final FrontEndActivityGraph graph = this.getFrontEndActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * Overridden because actions (transitions) are not directly contained in a
     * UML namespace.
     * @return getUseCase().getPackageName()
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String handleGetPackageName()
    {
        String packageName = null;

        final UseCaseFacade useCase = this.getUseCase();
        if (useCase != null)
        {
            packageName = useCase.getPackageName();
        }
        return packageName;
    }

    /**
     * @return getSource().isInitialState()
     * @see org.andromda.metafacades.uml.FrontEndAction#isUseCaseStart()
     */
    protected boolean handleIsUseCaseStart()
    {
        final StateVertexFacade source = this.getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade)source).isInitialState();
    }

    /**
     * @return getUseCase().getInterUseCaseParameters()
     * @see org.andromda.metafacades.uml.FrontEndAction#getFormFields()
     */
    protected List handleGetFormFields()
    {
        final Map formFieldMap = new LinkedHashMap();

        // - For an action that starts the use case, we need to detect all
        // usecases forwarding to the one
        // belonging to this action if there are any parameters in those
        // transitions we need to have
        // them included in this action's form
        if (this.isUseCaseStart())
        {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null)
            {
                final Collection finalStates = useCase.getReferencingFinalStates();
                for (final Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
                {
                    final Object finalStateObject = finalStateIterator.next();

                    // we need to test for the type because a non
                    // struts-use-case final state might accidently
                    // we linking to this use-case (for example: the user
                    // temporarily wants to disable code generation
                    // for a specific use-case and is not removing the
                    // final-state to use-case link(s))
                    if (finalStateObject instanceof FrontEndFinalState)
                    {
                        final FrontEndFinalState finalState = (FrontEndFinalState)finalStateObject;
                        final Collection parameters = finalState.getInterUseCaseParameters();
                        for (final Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                        {
                            final ParameterFacade parameter = (ParameterFacade)parameterIterator.next();
                            formFieldMap.put(
                                parameter.getName(),
                                parameter);
                        }
                    }
                }
            }
        }

        // if any action encountered by the execution of the complete
        // action-graph path emits a forward
        // containing one or more parameters they need to be included as a form
        // field too
        final Collection actionStates = this.getActionStates();
        for (final Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            final FrontEndActionState actionState = (FrontEndActionState)iterator.next();
            final FrontEndForward forward = actionState.getForward();
            if (forward != null)
            {
                final Collection forwardParameters = forward.getForwardParameters();
                for (final Iterator parameterIterator = forwardParameters.iterator(); parameterIterator.hasNext();)
                {
                    final ModelElementFacade forwardParameter = (ModelElementFacade)parameterIterator.next();
                    formFieldMap.put(
                        forwardParameter.getName(),
                        forwardParameter);
                }
            }
        }

        // add page variables for all pages/final-states targetted
        // also add the fields of the target page's actions (for preloading)
        final Collection forwards = this.getActionForwards();
        for (final Iterator iterator = forwards.iterator(); iterator.hasNext();)
        {
            final FrontEndForward forward = (FrontEndForward)iterator.next();
            final StateVertexFacade target = forward.getTarget();
            if (target instanceof FrontEndView)
            {
                final FrontEndView view = (FrontEndView)target;
                final Collection viewVariables = view.getVariables();
                for (final Iterator pageVariableIterator = viewVariables.iterator(); pageVariableIterator.hasNext();)
                {
                    final ModelElementFacade facade = (ModelElementFacade)pageVariableIterator.next();
                    formFieldMap.put(
                        facade.getName(),
                        facade);
                }
                final Collection allActionParameters = view.getAllFormFields();
                for (final Iterator actionParameterIterator = allActionParameters.iterator();
                    actionParameterIterator.hasNext();)
                {
                    // - don't allow existing parameters that are tables be
                    // overwritten (since they take
                    // precedence
                    final Object parameter = actionParameterIterator.next();
                    if (parameter instanceof FrontEndParameter)
                    {
                        FrontEndParameter variable = (FrontEndParameter)parameter;
                        final String name = variable.getName();
                        final Object existingParameter = formFieldMap.get(name);
                        if (existingParameter instanceof FrontEndParameter)
                        {
                            final FrontEndParameter existingVariable = (FrontEndParameter)existingParameter;
                            if (existingVariable.isTable())
                            {
                                variable = existingVariable;
                            }
                        }
                        formFieldMap.put(
                            name,
                            variable);
                    }
                }
            }
            else if (target instanceof FrontEndFinalState)
            {
                // only add these if there is no parameter recorded yet with the
                // same name
                final Collection forwardParameters = forward.getForwardParameters();
                for (final Iterator parameterIterator = forwardParameters.iterator(); parameterIterator.hasNext();)
                {
                    final ModelElementFacade facade = (ModelElementFacade)parameterIterator.next();
                    if (!formFieldMap.containsKey(facade.getName()))
                    {
                        formFieldMap.put(
                            facade.getName(),
                            facade);
                    }
                }
            }
        }

        // we do the action parameters in the end because they are allowed to
        // overwrite existing properties
        final Collection actionParameters = this.getParameters();
        for (final Iterator parameterIterator = actionParameters.iterator(); parameterIterator.hasNext();)
        {
            final Object parameter = parameterIterator.next();
            if (parameter instanceof FrontEndParameter)
            {
                final FrontEndParameter variable = (FrontEndParameter)parameter;
                formFieldMap.put(
                    variable.getName(),
                    variable);
            }
        }

        // - if we don't have any fields defined on this action and there are no
        // action forwards,
        // take the parameters from the deferred operations (since we would want
        // to stay on the same view)
        if (formFieldMap.isEmpty() && this.getActionForwards().isEmpty())
        {
            for (final Iterator iterator = this.getDeferredOperations().iterator(); iterator.hasNext();)
            {
                final OperationFacade operation = (OperationFacade)iterator.next();
                for (final Iterator parameterIterator = operation.getArguments().iterator();
                    parameterIterator.hasNext();)
                {
                    final ParameterFacade parameter = (ParameterFacade)parameterIterator.next();
                    formFieldMap.put(
                        parameter.getName(),
                        parameter);
                }
            }
        }
        return new ArrayList(formFieldMap.values());
    }
}