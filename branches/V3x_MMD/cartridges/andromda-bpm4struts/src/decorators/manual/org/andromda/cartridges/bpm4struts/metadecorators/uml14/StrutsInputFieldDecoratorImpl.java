package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public abstract class StrutsInputFieldDecoratorImpl extends StrutsInputFieldDecorator
{
    // ---------------- constructor -------------------------------
    public StrutsInputFieldDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsInputFieldDecorator ...


    public java.lang.Boolean isRequired()
    {
        final String requiredValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED);
        return makeBoolean(requiredValue);
    }

    private Boolean makeBoolean(String string)
    {
        if ( string!=null &&
            !"true".equalsIgnoreCase(string) &&
            !"yes".equalsIgnoreCase(string) &&
            !"0".equals(string) )
        {
            return Boolean.FALSE;
        }
        else
        {
            return Boolean.TRUE;
        }
    }

    public String getMaskPattern()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_PATTERN);
    }

    public Boolean isResetField()
    {
        return Boolean.FALSE;
    }

    public abstract String getFieldType();

    // ------------- relations ------------------
    protected ModelElement handleGetView()
    {
        final ClassifierDecorator type = (ClassifierDecorator)DecoratorBase.decoratedElement(getType());
        final Collection associationEnds = type.getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            Classifier participant = associationEnd.getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW).booleanValue())
                return participant; // undecorated
        }
        return null;
    }

    /**
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetJsp()
    {
        final ClassifierDecorator owner = (ClassifierDecorator)DecoratorBase.decoratedElement(getOwner());
        final Collection associationEnds = owner.getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            Classifier participant = associationEnd.getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                return participant; // undecorated
        }
        return null;
    }

    // ------------------------------------------------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty

        // it must be associated to a single View
    }
}
