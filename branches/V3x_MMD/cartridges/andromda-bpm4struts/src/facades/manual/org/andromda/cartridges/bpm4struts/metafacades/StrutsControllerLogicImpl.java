package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsController
{
    // ---------------- constructor -------------------------------
    
    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsController ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperClassName()()
     */
    public java.lang.String getControllerHelperClassName()
    {
        return getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperPackageName()()
     */
    public java.lang.String getControllerHelperPackageName()
    {
        return getPackageName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperType()()
     */
    public java.lang.String getControllerHelperType()
    {
        return getFullyQualifiedName();
    }

    public java.lang.String getFullPathName()
    {
        return '/' + getFullyQualifiedName().replace('.','/');
    }

    public java.util.Collection getDecisionChoices()
    {
        Collection choices = new HashSet();

        Collection actions = getActions();
        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            if (action.isTargettingDecisionPoint())
            {
                Collection outcomes = action.getTarget().getOutgoing();
                for (Iterator outcomeIterator = outcomes.iterator(); outcomeIterator.hasNext();)
                {
                    StrutsTransition transition = (StrutsTransition) outcomeIterator.next();
                    choices.add(transition.getGuardName());
                }
            }
        }
        return choices;
    }

    // ------------- relations ------------------

    protected Collection handleGetActions()
    {
        final Collection actions = new LinkedList();

        Collection useCases = getModel().getAllUseCases();
        for (Iterator useCaseIterator = useCases.iterator(); useCaseIterator.hasNext();)
        {
            Object obj = useCaseIterator.next();
            if (obj instanceof StrutsUseCase)
            {
                StrutsUseCase useCase = (StrutsUseCase)obj;
                StrutsActivityGraph graph = useCase.getActivityGraph();
                if (graph.getContextElement().equals(this))
                {
                    Collection actionStates = graph.getActionStates();
                    for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
                    {
                        Object jsp = (Object) actionStateIterator.next();
                        if (jsp instanceof StrutsJsp)
                        {
                            actions.addAll(((StrutsJsp)jsp).getOutgoing());
                        }
                    }
                }
            }
        }
        return actions;
    }

}
