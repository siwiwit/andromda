package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
 */
public class StrutsJspLogicImpl
        extends StrutsJspLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
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
        ClassifierFacade classifier = (ClassifierFacade) getActivityGraph().getContextElement();
        return classifier.getPackageName();
    }

    public String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getUseCase().getName() + ' ' + getName());
    }

    public String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    public String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    public String handleGetDocumentationValue()
    {
        return StringUtilsHelper.toResourceMessage(getDocumentation(""));
    }

    public String handleGetFullPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/');
    }
    // ------------- relations ------------------

    protected Object handleGetUseCase()
    {
        final ActivityGraphFacade graph = getActivityGraph();
        if (graph instanceof StrutsActivityGraph)
        {
            return ((StrutsActivityGraph) graph).getUseCase();
        }
        return null;
    }

    protected Collection handleGetActions()
    {
        final Collection actions = new LinkedList();
        final Collection outgoing = getOutgoing();

        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsAction)
                actions.add(object);
        }

        return actions;
    }

    public StrutsForward getForward()
    {
        return (StrutsForward)shieldedElement(getOutgoing().iterator().next());
    }

    protected Collection handleGetPageVariables()
    {
        final Map variablesMap = new HashMap();

        final Collection incoming = getIncoming();
        for (Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
                collectByName(trigger.getParameters(), variablesMap);
        }

/*
        final Collection actions = getActions();
        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            collectByName(action.getActionParameters(), variablesMap);
        }
*/

        return variablesMap.values();
    }

    private void collectByName(Collection modelElements, Map elementMap)
    {
        for (Iterator iterator = modelElements.iterator(); iterator.hasNext();)
        {
            ModelElementFacade modelElement = (ModelElementFacade) iterator.next();
            elementMap.put(modelElement.getName(), modelElement);
        }
    }

    protected Collection handleGetIncomingActions()
    {
        final Collection incomingActionsList = new LinkedList();
        collectIncomingActions(this, new HashSet(), incomingActionsList);
        return incomingActionsList;
    }

    public int handleGetTabCount()
    {
        if (this.isTabbed())
        {
            final Collection actions = this.getActions();
            int maxValue = 0;

            for (Iterator iterator = actions.iterator(); iterator.hasNext();)
            {
                StrutsAction action = (StrutsAction)iterator.next();
                StrutsTrigger trigger = action.getActionTrigger();

                if (trigger != null)    // should never be null (OCL validation rules)
                {
                    maxValue = Math.max(maxValue, trigger.getTabIndex());
                }
            }
            return maxValue + 1;    // we add one because we're counting from [1..n]
        }
        return 0;
    }

    public Collection handleGetTabActions(int index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("Minimum tab-index value is zero");

        if (index >= this.getTabCount())
            throw new IndexOutOfBoundsException("Maximum tab-index value is the number of available tabs minus one");

        final Collection actions = this.getActions();
        final Collection tabActions = new LinkedList();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            StrutsTrigger trigger = action.getActionTrigger();

            if (trigger != null)    // should never be null (OCL validation rules)
            {
                if (trigger.getTabIndex() == index)
                {
                    tabActions.add(action);
                }
            }
        }

        return tabActions;
    }

    public boolean handleIsTabbed()
    {
        final Collection actions = this.getActions();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            Object actionObject = iterator.next();
            if (actionObject instanceof StrutsAction)
            {
                StrutsAction action = (StrutsAction)actionObject;
                StrutsTrigger trigger = action.getActionTrigger();

                if (trigger != null)    // should never be null (OCL validation rules)
                {
                    if (trigger.isTabbed()) return true;
                }
            }
        }
        return false;
    }

    public Collection handleGetNonTabActions()
    {
        final Collection nonTabbedActions = new LinkedList();
        final Collection actions = this.getActions();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            Object actionObject = iterator.next();
            if (actionObject instanceof StrutsAction)
            {
                StrutsAction action = (StrutsAction)actionObject;
                StrutsTrigger trigger = action.getActionTrigger();

                if (trigger != null)    // should never be null (OCL validation rules)
                {
                    if (!trigger.isTabbed()) nonTabbedActions.add(action);
                }
            }
        }
        return nonTabbedActions;
    }

    public Map handleGetTabMap()
    {
        final Map tabMap = new LinkedHashMap();

        final int tabCount = this.getTabCount();
        for (int i = 0; i < tabCount; i++)
        {
            final Collection tabActions = this.getTabActions(i);
            tabMap.put(String.valueOf(i), tabActions);
        }

        return tabMap;
    }

    public String handleGetTabName(int tabIndex)
    {
        final Collection tabActions = this.getTabActions(tabIndex);
        final StringBuffer buffer = new StringBuffer();

        boolean needsSeparator = false;
        for (Iterator iterator = tabActions.iterator(); iterator.hasNext();)
        {
            if (needsSeparator) buffer.append( " / " );
            StrutsAction action = (StrutsAction) iterator.next();
            buffer.append( action.getActionTrigger().getTriggerValue() );
            needsSeparator = true;
        }

        if (buffer.length() == 0)
        {
            buffer.append(String.valueOf(tabIndex));
        }
        return buffer.toString();
    }

    private void collectIncomingActions(StateVertexFacade stateVertex, Collection processedTransitions, Collection actions)
    {
        final Collection incomingTransitions = stateVertex.getIncoming();
        for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
        {
            TransitionFacade incomingTransition = (TransitionFacade) iterator.next();
            collectIncomingActions(incomingTransition, processedTransitions, actions);
        }
    }

    private void collectIncomingActions(TransitionFacade transition, Collection processedTransitions, Collection actions)
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
                    Collection finalStates = getUseCase().getFinalStates();
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
                Collection incomingTransitions = transition.getSource().getIncoming();
                for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
                {
                    TransitionFacade incomingTransition = (TransitionFacade) iterator.next();
                    collectIncomingActions(incomingTransition, processedTransitions, actions);
                }
            }
        }
    }
}
