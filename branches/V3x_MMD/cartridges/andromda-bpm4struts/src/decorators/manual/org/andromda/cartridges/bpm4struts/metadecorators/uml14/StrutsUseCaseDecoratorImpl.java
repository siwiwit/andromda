package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.UmlPackage;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.usecases.UseCase
 *
 *
 */
public class StrutsUseCaseDecoratorImpl extends StrutsUseCaseDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsUseCaseDecoratorImpl (org.omg.uml.behavioralelements.usecases.UseCase metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsUseCaseDecorator ...

    public State findAsWorkflowState()
    {
        if (hasStereotype(Bpm4StrutsProfile.STEREOTYPE_USECASE).booleanValue())
        {
            final String name = getName();
            final UmlPackage model = MetaDecoratorUtil.getModel(this);
            final Collection allStates = model.getStateMachines().getState().refAllOfType();

            for (Iterator iterator = allStates.iterator(); iterator.hasNext();)
            {
                State state = (State) iterator.next();
                if (name.equalsIgnoreCase(state.getName()))
                    return state;
            }
        }
        return null;
    }

    // ------------- relations ------------------
    protected ModelElement handleGetStateMachine()
    {
        final Collection ownedElements = getOwnedElement();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object ownedElement = iterator.next();
            if (ownedElement instanceof StateMachine)
            {
                return (StateMachine)ownedElement;
            }
        }
        return null;
    }

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");
    }
}
