package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.usecases.UseCase
 *
 *
 */
public class StrutsWorkflowDecoratorImpl extends StrutsWorkflowDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsWorkflowDecoratorImpl (org.omg.uml.behavioralelements.usecases.UseCase metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsWorkflowDecorator ...

    // ------------- relations ------------------

   /**
    *
    */
    public java.util.Collection handleGetUseCases()
    {
        final Collection useCases = new LinkedHashSet();
        Collection actionStates = getActivityGraph().getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionStateDecorator actionState = (StrutsActionStateDecorator) iterator.next();
            UseCase useCase= findActionStateAsUseCase(actionState);
            if (useCase != null)
                useCases.add(useCase);
        }
        return useCases;
    }

    private UseCase findActionStateAsUseCase(StrutsActionStateDecorator actionState)
    {
        final String actionStateName = (actionState==null) ? null : actionState.getName();

        if (actionStateName == null)
            return null;

        UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        Collection allUseCases = model.getUseCases().getUseCase().refAllOfType();

        for (Iterator iterator = allUseCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            DecoratorBase decoratedUseCase = decoratedElement(useCase);
            if (decoratedUseCase instanceof StrutsUseCaseDecorator)
            {
                StrutsUseCaseDecorator strutsUseCaseDecorator = (StrutsUseCaseDecorator)decoratedUseCase;
                if (actionStateName.equalsIgnoreCase(strutsUseCaseDecorator.getName()))
                    return useCase;
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
    // ------------------------------------------------------------

}
