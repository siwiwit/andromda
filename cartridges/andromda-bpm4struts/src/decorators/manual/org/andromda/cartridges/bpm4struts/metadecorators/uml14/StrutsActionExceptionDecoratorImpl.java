package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsActionExceptionDecoratorImpl extends StrutsActionExceptionDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsActionExceptionDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionExceptionDecorator ...

    public java.lang.String getMessageKey()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_KEY);
    }

    public java.lang.String getExceptionType()
    {
        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_TYPE);
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return type;
    }

    public java.lang.String getPath()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_PATH);
    }

    // ------------- relations ------------------

    /**
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetServlet()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd = (AssociationEndDecorator)iterator.next();
            final ClassifierDecorator otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER))
                return otherEnd.getMetaObject();
        }
        return null;
    }

    // ------------------------------------------------------------

    public void validate() throws DecoratorValidationException
    {
        // must be associated to a controller class
    }
}
