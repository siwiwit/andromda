package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecoratorImpl;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.UmlClass;

import java.util.Collection;
import java.util.Iterator;


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

    public UseCase getUseCase()
    {
        final String useCaseName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE);
        final UmlPackage model = MetaDecoratorUtil.getModel(metaObject);

        final Collection allUseCases = model.getUseCases().getUseCase().refAllOfType();
        for (Iterator iterator = allUseCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            if (useCaseName.equalsIgnoreCase(useCase.getName()))
                return useCase;
        }

        return null;
    }

    public String getDispatchParameter()
    {
        String parameter = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_PARAMETER);
        if (parameter == null)
            parameter = Bpm4StrutsProfile.TAGGED_VALUE_ACTION_DEFAULT_PARAMETER;
        return parameter;
    }

    protected ModelElement handleGetFormBean()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            ClassifierDecorator participant = new ClassifierDecoratorImpl(associationEnd.getParticipant());
            if (participant.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                return participant;
        }
        return null;
    }

    public String getControllerAbstractClassName()
    {
        return
            MetaDecoratorUtil.toJavaClassName(metaObject.getName()) +
                Bpm4StrutsProfile.DEFAULT_ABSTRACT_CONTROLLER_CLASS_SUFFIX;
    }

    public String getControllerImplementationClassName()
    {
        return
            MetaDecoratorUtil.toJavaClassName(metaObject.getName()) +
                Bpm4StrutsProfile.DEFAULT_IMPLEMENTATION_CONTROLLER_CLASS_SUFFIX;
    }

    public java.lang.String getControllerName()
    {
        String actionPath = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_PATH);
        if (actionPath == null)
        {
            actionPath = MetaDecoratorUtil.toJavaMethodName(metaObject.getName());
        }
        return actionPath;
    }

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = metaObject.getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(metaObject, "Name may not be empty or only contain whitespace");

        // the name must be unique
        final UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        final Collection classes = model.getCore().getUmlClass().refAllOfType();
        classes.remove(metaObject);
        // check that we don't accidentally generate the same names from more action states
        // if the name is specified using a tagged value that name must also be unique
        final String controllerName = getControllerName();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();)
        {
            UmlClass umlClass = (UmlClass) iterator.next();
            if (controllerName.equalsIgnoreCase('/'+MetaDecoratorUtil.toJavaMethodName(umlClass.getName())))
                if (hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER).booleanValue())
                    throw new DecoratorValidationException(metaObject, "There is another controller class in the model which generates a name clash with this one, please change one of the class names (The package namespace is not important)");
        }

        // there must be a tagged value pointing to a use-case
        final String useCaseName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE);
        if ( (useCaseName==null) || (useCaseName.trim().length()==0) )
            throw new DecoratorValidationException(metaObject, "Missing tagged value to point to use-case ("+Bpm4StrutsProfile.TAGGED_VALUE_USE_CASE+")");

        // this use-case must exist
        if (getUseCase() == null)
            throw new DecoratorValidationException(metaObject, "This controller points to a non-existing use-case ("+useCaseName+")");

        // at most a single Model may be associated to this controller
        int modelCount = 0;
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            ClassifierDecorator participant = new ClassifierDecoratorImpl(associationEnd.getParticipant());
            if (participant.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
            {
                if (modelCount > 1)
                    throw new DecoratorValidationException(metaObject, "At most one model class may be associated with a controller class");
                modelCount++;
            }
        }
    }
}
