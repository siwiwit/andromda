package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metadecorators.uml14.*;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;

import java.util.*;


/**
 * A Struts model is the form bean that encapsulates all the request or session parameters. *
 * <p/>
 * Metaclass decorator implementation for $decoratedMetacName
 */
public class StrutsFormBeanDecoratorImpl extends StrutsFormBeanDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsFormBeanDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsFormBeanDecorator ...

    public java.lang.String getFormName()
    {
        String beanName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_FORM_BEAN_NAME);
        if (beanName == null)
        {
            beanName = StringUtilsHelper.lowerCaseFirstLetter(getName());
        }
        return beanName;
    }

    // ------------- relations ------------------

    protected Collection handleGetFields()
    {
        final Set fields = new LinkedHashSet();
        final Collection jsps = getJsps();
        for (Iterator jspIterator = jsps.iterator(); jspIterator.hasNext();)
        {
            StrutsJspDecorator jsp = (StrutsJspDecorator) jspIterator.next();
            Collection messages = jsp.getMessages();
            for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
            {
                StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
                fields.addAll(messageDecorator.getParameters());
            }
        }
        return fields;
    }

    protected Collection handleGetResetFields()
    {
        final Set fields = new LinkedHashSet();
        final Collection jsps = getJsps();
        for (Iterator jspIterator = jsps.iterator(); jspIterator.hasNext();)
        {
            StrutsJspDecorator jsp = (StrutsJspDecorator) jspIterator.next();
            Collection messages = jsp.getMessages();
            for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
            {
                StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
                fields.addAll(messageDecorator.getResetInputParameters());
            }
        }
        return fields;
    }

    /**
     *
     *
     */
    public java.util.Collection handleGetServlets()
    {
        final Collection servletClasses = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd = (AssociationEndDecorator)iterator.next();
            final ClassifierDecorator otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER))
                servletClasses.add(otherEnd.getMetaObject());
        }
        return servletClasses;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetJsps()
    {
        final Collection views = new LinkedList();
        for (Iterator dependencyIterator=getDependencies().iterator(); dependencyIterator.hasNext();)
        {
            Object element = dependencyIterator.next();
            Dependency dep = (Dependency)((DependencyDecorator) element).getMetaObject();
            ModelElement supplier = (ModelElement) dep.getSupplier().iterator().next();
            ModelElementDecorator supplierDecorator = (ModelElementDecorator)decoratedElement(supplier);
            if (supplierDecorator instanceof StrutsJspDecorator)
                views.add(supplier);
        }
        return views;
    }

    // ------------- validation ------------------

    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // one or more controller classes must be associated to this model
        final Collection servlets = getServlets();
        if (servlets.size() == 0)
            throw new DecoratorValidationException(this, "A form bean needs to be associated to at least 1 servlet");
*/

    }

}
