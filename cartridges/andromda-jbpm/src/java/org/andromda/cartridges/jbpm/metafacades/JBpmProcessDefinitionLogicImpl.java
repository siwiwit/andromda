package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.cartridges.jbpm.JBpmProfile;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition
 */
public class JBpmProcessDefinitionLogicImpl
    extends JBpmProcessDefinitionLogic
{

    public JBpmProcessDefinitionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected List handleGetStates()
    {
        final List states = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final JBpmState state = (JBpmState)actionStateIterator.next();
                if (!state.isTaskNode())
                {
                    states.add(state);
                }
            }
        }

        return states;
    }

    protected List handleGetTaskNodes()
    {
        final List taskNodes = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final JBpmState state = (JBpmState)actionStateIterator.next();
                if (state.isTaskNode())
                {
                    taskNodes.add(state);
                }
            }
        }

        return taskNodes;
    }

    protected boolean handleIsBusinessProcess()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_BUSINESS_PROCESS) && (getFirstActivityGraph() != null); 
    }

    protected boolean handleIsActivityPresent()
    {
        return getFirstActivityGraph() != null;
    }

    protected List handleGetSwimlanes()
    {
        final List swimlanes = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            swimlanes.addAll(graph.getPartitions());
        }

        return swimlanes;
    }

    protected Object handleGetStartState()
    {
        Object startState = null;

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            startState = graph.getInitialState();
        }

        return startState;
    }

    protected List handleGetEndStates()
    {
        final List endStates = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            endStates.addAll(graph.getFinalStates());
        }

        return endStates;
    }

    protected List handleGetDecisions()
    {
        final List decisions = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isDecisionPoint())
                {
                    decisions.add(pseudostate);
                }
            }
        }

        return decisions;
    }

    protected List handleGetForks()
    {
        final List forks = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isFork())
                {
                    forks.add(pseudostate);
                }
            }
        }

        return forks;
    }

    protected List handleGetJoins()
    {
        final List joins = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isJoin())
                {
                    joins.add(pseudostate);
                }
            }
        }

        return joins;
    }
}