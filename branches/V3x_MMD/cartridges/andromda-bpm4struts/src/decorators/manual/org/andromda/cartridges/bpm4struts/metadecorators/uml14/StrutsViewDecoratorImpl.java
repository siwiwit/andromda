package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecoratorImpl;
import org.omg.uml.foundation.core.AssociationEnd;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsViewDecoratorImpl extends StrutsViewDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsViewDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsViewDecorator ...

    public String getFullyQualifiedJspFilename()
    {
        String fileName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_JSP_FILENAME);
        if (fileName == null)
        {
            fileName = MetaDecoratorUtil.toWebFileName(getName());
        }
        return fileName;
    }

    // ------------- relations ------------------

    /**
     *
     */
    public java.util.Collection handleGetInputFields()
    {
        final Collection inputFields = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            ClassifierDecorator participant = new ClassifierDecoratorImpl(associationEnd.getParticipant());
            if (participant.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_INPUTFIELD).booleanValue())
                inputFields.add(participant);
        }
        return inputFields;
    }

    // ------------------------------------------------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");
    }
}
