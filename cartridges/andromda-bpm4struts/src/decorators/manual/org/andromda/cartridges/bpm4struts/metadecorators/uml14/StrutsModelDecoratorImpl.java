package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsModelDecoratorImpl extends StrutsModelDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsModelDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsModelDecorator ...

    public java.lang.String getFormBeanName()
    {
        String beanName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_FORM_BEAN_NAME);
        if (beanName == null)
        {
            beanName = getName();
        }
        return beanName;
    }

    public Set getInputFields()
    {
        final Set inputFields = new LinkedHashSet();
        final Collection views = getJsps();
        for (Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            Classifier view = (Classifier) iterator.next();
            StrutsViewDecorator viewDecorator = (StrutsViewDecorator) DecoratorBase.decoratedElement(view);
            inputFields.addAll(viewDecorator.getInputFields());
        }
        return inputFields;
    }

    public Set getResetInputFields()
    {
        final Set resetInputFields = new LinkedHashSet();
        final Collection views = getJsps();
        for (Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            Classifier view = (Classifier) iterator.next();
            StrutsViewDecorator viewDecorator = (StrutsViewDecorator) DecoratorBase.decoratedElement(view);
            resetInputFields.addAll(viewDecorator.getResetInputFields());
        }
        return resetInputFields;
    }
    // ------------- relations ------------------

    protected Collection handleGetServlets()
    {
        final Collection controllerClasses = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndDecorator associationEnd =
                (AssociationEndDecorator) DecoratorBase.decoratedElement((AssociationEnd) iterator.next());
            Classifier participant = associationEnd.getOtherEnd().getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator) DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER).booleanValue())
                controllerClasses.add(participant);
        }
        return controllerClasses;
    }

    protected Collection handleGetJsps()
    {
        final Collection views = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndDecorator associationEnd =
                (AssociationEndDecorator) DecoratorBase.decoratedElement((AssociationEnd) iterator.next());
            Classifier participant = associationEnd.getOtherEnd().getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator) DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW).booleanValue())
                views.add(participant);
        }
        return views;
    }

    // ------------- validation ------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // one or more controller classes must be associated to this model
        final Collection servlets = getServlets();
        if (servlets.size() == 0)
            throw new DecoratorValidationException(this, "A form bean needs to be associated to at least 1 servlet");

    }
}
