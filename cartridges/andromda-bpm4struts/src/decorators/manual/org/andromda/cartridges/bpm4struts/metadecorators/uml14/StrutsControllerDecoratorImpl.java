package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.UseCaseDecorator;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsControllerDecoratorImpl extends StrutsControllerDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsControllerDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsControllerDecorator ...
    public String getServletName()
    {
        String actionPath = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_PATH);
        if (actionPath == null)
        {
            actionPath = getName();
        }
        return actionPath;
    }

    public String getDispatchParameter()
    {
        String parameter = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_PARAMETER);
        if (parameter == null)
            parameter = Bpm4StrutsProfile.TAGGED_VALUE_ACTION_DEFAULT_PARAMETER;
        return parameter;
    }

    // ------------- relations ------------------

    protected ModelElement handleGetUseCase()
    {
        final String useCaseName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE);
        final UmlPackage model = MetaDecoratorUtil.getModel(this);

        final Collection allUseCases = model.getUseCases().getUseCase().refAllOfType();
        for (Iterator iterator = allUseCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            UseCaseDecorator useCaseDecorator = (UseCaseDecorator) DecoratorBase.decoratedElement(useCase);

            if (useCaseDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_USECASE).booleanValue() &&
                useCaseName.equalsIgnoreCase(useCase.getName()))
            {
                return useCase;
            }
        }
        return null;
    }

    protected ModelElement handleGetFormBean()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd =
                (AssociationEndDecorator) DecoratorBase.decoratedElement((AssociationEnd) iterator.next());
            final Classifier participant = associationEnd.getOtherEnd().getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                return participant;
        }
        return null;
    }

    protected Collection handleGetExceptionHandlers()
    {
        final Collection exceptionHandlers = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd =
                (AssociationEndDecorator) DecoratorBase.decoratedElement((AssociationEnd) iterator.next());
            final Classifier participant = associationEnd.getOtherEnd().getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_EXCEPTION).booleanValue())
                exceptionHandlers.add(participant);
        }
        return exceptionHandlers;
    }
    // ------------- validation ------------------

    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name must be unique
        final UmlPackage model = MetaDecoratorUtil.getModel(this);
        final Collection classes = model.getCore().getUmlClass().refAllOfType();
        // check that we don't accidentally generate the same names from more action states
        // if the name is specified using a tagged value that name must also be unique
        final String controllerName = getServletName();

        boolean controllerNameFound = false;
        for (Iterator iterator = classes.iterator(); iterator.hasNext();)
        {
            Object classObject = iterator.next();
            if (classObject instanceof StrutsControllerDecorator)
            {
                StrutsControllerDecorator controllerDecorator = (StrutsControllerDecorator) classObject;
                if (controllerName.equals(controllerDecorator.getServletName()))
                {
                    if (controllerNameFound)
                        throw new DecoratorValidationException(this,
                            "There is another controller class in the model which generates a name clash with this one, please change one of the class names (The package namespace is not important)");
                    else
                        controllerNameFound = true;
                }
            }
        }

        // there must be a tagged value pointing to a use-case
        final String useCaseName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE);
        if ((useCaseName == null) || (useCaseName.trim().length() == 0))
            throw new DecoratorValidationException(this,
                "Missing tagged value to point to use-case (" + Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE + ")");

        // this use-case must exist
        if (getUseCase() == null)
            throw new DecoratorValidationException(this,
                "This controller points to a non-existing use-case (" + useCaseName + ")");

        // at most a single Model may be associated to this controller
        int modelCount = 0;
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndDecorator associationEnd = (AssociationEndDecorator) iterator.next();
            Classifier participant = associationEnd.getOtherEnd().getParticipant();

            if (participant instanceof StrutsModelDecorator)
            {
                ClassifierDecorator participantDecorator = (ClassifierDecorator) participant;
                if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                {
                    if (modelCount > 1)
                        throw new DecoratorValidationException(this,
                            "At most one model class may be associated with a controller class");
                    modelCount++;
                }
            }
        }
*/
    }
}
