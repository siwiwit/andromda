package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


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

    protected Collection handleGetControllerClasses()
    {
        final Collection controllerClasses = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            Classifier participant = associationEnd.getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER).booleanValue())
                controllerClasses.add(participant);
        }
        return controllerClasses;
    }

    public String getFormBeanAbstractClassName()
    {
        return MetaDecoratorUtil.toJavaClassName(getName()) + Bpm4StrutsProfile.DEFAULT_ABSTRACT_CLASS_SUFFIX;
    }

    public String getFormBeanImplementationClassName()
    {
        return MetaDecoratorUtil.toJavaClassName(getName()) + Bpm4StrutsProfile.DEFAULT_IMPLEMENTATION_CLASS_SUFFIX;
    }

    public java.lang.String getFormBeanName()
    {
        String beanName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_FORM_BEAN_NAME);
        if (beanName == null)
        {
            beanName = MetaDecoratorUtil.toJavaMethodName(getName());
        }
        return beanName;
    }

    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name must be unique

        // if the name is specified using a tagged value that name must also be unique

        // one or more controller classes must be associated to this model

    }
}
