package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.usecases.UseCase;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.State
 *
 *
 */
public class StrutsSimpleStateDecoratorImpl extends StrutsSimpleStateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsSimpleStateDecoratorImpl(org.omg.uml.behavioralelements.statemachines.State metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsSimpleStateDecorator ...

    public org.omg.uml.behavioralelements.usecases.UseCase getCorrespondingUseCase()
    {
        final String name = getName();
        final UmlPackage model = MetaDecoratorUtil.getModel(this);
        final Collection allUseCases = model.getUseCases().getUseCase().refAllOfType();

        for (Iterator iterator = allUseCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCaseDecorator useCase = (StrutsUseCaseDecorator) DecoratorBase.decoratedElement((UseCase) iterator.next());
            if (name.equalsIgnoreCase(useCase.getName()))
                if (useCase.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_USECASE).booleanValue())
                    return useCase;
        }

        return null;
    }

    // ------------- relations ------------------

    // ------------- validation ------------------
    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name must be unique in the container state machine

        // there must be one and only one associated use-case represented by this state
*/
    }

}
