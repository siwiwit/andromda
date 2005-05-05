package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
 */
public class StrutsJspLogicImpl
        extends StrutsJspLogic
{
    // ---------------- constructor -------------------------------

    public StrutsJspLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsJsp ...

    // from org.andromda.metafacades.uml.ModelElementFacade
    public String getPackageName()
    {
        String packageName = null;

        ActivityGraphFacade graph = getActivityGraph();
        if (graph != null)
        {
            UseCaseFacade graphUseCase = graph.getUseCase();
            if (graphUseCase instanceof StrutsUseCase)
            {
                StrutsUseCase useCase = (StrutsUseCase)graphUseCase;
                if (useCase != null)
                {
                    packageName = useCase.getPackageName();
                }
            }
        }
        return packageName;
    }

    protected String handleGetMessageKey()
    {
        String messageKey = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            messageKey = StringUtilsHelper.toResourceMessageKey(useCase.getName() + ' ' + getName());
        }
        return messageKey;
    }

    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return (value == null) ? "" : value;
    }

    protected String handleGetOnlineHelpKey()
    {
        return getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No page documentation has been specified" : value);
        buffer.append(crlf);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected String handleGetFullPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(StringUtils.trimToEmpty(getName()))).replace(
                '.', '/');
    }

    protected boolean handleIsValidationRequired()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsDateFieldPresent()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isDateFieldPresent())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsCalendarRequired()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    // ------------- relations ------------------

    protected List handleGetAllActionParameters()
    {
        final List actionParameters = new ArrayList();
        final Collection actions = getActions();
        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction)iterator.next();
            actionParameters.addAll(action.getActionParameters());
        }
        return actionParameters;
    }

    protected Object handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final ActivityGraphFacade graph = getActivityGraph();
        if (graph instanceof StrutsActivityGraph)
        {
            useCase = ((StrutsActivityGraph)graph).getUseCase();
            if (useCase != null && !StrutsUseCase.class.isAssignableFrom(useCase.getClass()))
            {
                useCase = null;
            }
        }
        return useCase;
    }

    protected List handleGetActions()
    {
        final List actions = new ArrayList();
        final Collection outgoing = getOutgoing();

        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsAction)
                actions.add(object);
        }

        return actions;
    }

    protected List handleGetNonActionForwards()
    {
        final List actions = new ArrayList();
        final Collection outgoing = getOutgoing();

        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (!(object instanceof StrutsAction))
            {
                actions.add(object);
            }
        }
        return actions;
    }

    public Object handleGetForward()
    {
        return (StrutsForward)shieldedElement(getOutgoing().iterator().next());
    }

    protected List handleGetPageVariables()
    {
        final Map variablesMap = new HashMap();

        final Collection incoming = getIncoming();
        for (Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade)iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
                collectByName(trigger.getParameters(), variablesMap);
        }

        return new ArrayList(variablesMap.values());
    }

    /**
     * Iterates over the model elements and maps their name on their instance in the argument map.
     */
    private void collectByName(Collection modelElements, Map elementMap)
    {
        for (Iterator iterator = modelElements.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade modelElement = (ModelElementFacade)iterator.next();
            elementMap.put(modelElement.getName(), modelElement);
        }
    }

    protected List handleGetIncomingActions()
    {
        final List incomingActionsList = new ArrayList();
        collectIncomingActions(this, new HashSet(), incomingActionsList);
        return incomingActionsList;
    }

    /**
     * Collects all actions that are entering the argument state vertex.
     *
     * @param stateVertex the statevertex to process
     * @param processedTransitions the transitions that have already been processed
     * @param actions the actions collected so far
     */
    private void collectIncomingActions(StateVertexFacade stateVertex, Collection processedTransitions,
                                        Collection actions)
    {
        final Collection incomingTransitions = stateVertex.getIncoming();
        for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
        {
            final TransitionFacade incomingTransition = (TransitionFacade)iterator.next();
            collectIncomingActions(incomingTransition, processedTransitions, actions);
        }
    }

    /**
     * Collects all actions that are possibly traversing the argument transitions.
     *
     * @param transition the transition to process
     * @param processedTransitions the transitions that have already been processed
     * @param actions the actions collected so far
     */
    private void collectIncomingActions(TransitionFacade transition, Collection processedTransitions,
                                        Collection actions)
    {
        if (!processedTransitions.contains(transition))
        {
            processedTransitions.add(transition);
            if (transition instanceof StrutsAction)
            {
                actions.add(transition);

/*  @todo: TEMPORARILY COMMENTED OUT -- needs verification that isCaseStart() forms are not populated, but I think they are
                if (((StrutsAction)transition).isUseCaseStart())
                {
                    Collection finalStates = getUseCase().getFinalStates();// todo: test usecase for null
                    for (Iterator iterator = finalStates.iterator(); iterator.hasNext();)
                    {
                        FinalStateFacade finalState = (FinalStateFacade) iterator.next();
                        collectIncomingActions(finalState, processedTransitions, actions);
                    }
                }
*/
            }
            else
            {
                final Collection incomingTransitions = transition.getSource().getIncoming();
                for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
                {
                    final TransitionFacade incomingTransition = (TransitionFacade)iterator.next();
                    collectIncomingActions(incomingTransition, processedTransitions, actions);
                }
            }
        }
    }

    protected String handleGetCssFileName()
    {
        return getFullPath() + ".css";
    }

    protected List handleGetNonTableActions()
    {
        final List nonTableActions = new ArrayList();

        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            if (!action.isTableLink())
            {
                nonTableActions.add(action);
            }
        }

        return nonTableActions;
    }

    protected List handleGetTables()
    {
        final List tables = new ArrayList();

        final List pageVariables = getPageVariables();
        for (int i = 0; i < pageVariables.size(); i++)
        {
            final StrutsParameter pageVariable = (StrutsParameter)pageVariables.get(i);
            if (pageVariable.isTable())
            {
                tables.add(pageVariable);
            }
        }

        return tables;
    }
}
