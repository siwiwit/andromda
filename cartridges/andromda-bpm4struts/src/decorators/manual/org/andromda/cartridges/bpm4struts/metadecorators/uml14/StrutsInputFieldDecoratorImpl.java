package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecoratorImpl;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class StrutsInputFieldDecoratorImpl extends StrutsInputFieldDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsInputFieldDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsInputFieldDecorator ...


    public java.lang.Boolean isReadOnly()
    {
        return null;
    }

    public java.lang.Integer getMaximumLength()
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    public java.lang.Boolean isRequired()
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------

    /**
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetJsp()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            Classifier participant = associationEnd.getParticipant();
            ClassifierDecorator participantDecorator = new ClassifierDecoratorImpl(participant);
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
