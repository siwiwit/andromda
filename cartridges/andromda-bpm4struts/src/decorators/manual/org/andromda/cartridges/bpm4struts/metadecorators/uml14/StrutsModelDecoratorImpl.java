package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.*;
import org.andromda.core.common.StringUtilsHelper;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;

import java.util.*;


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
            beanName = StringUtilsHelper.lowerCaseFirstLetter(getName());
        }
        return beanName;
    }

    public Set getInputFields()
    {
        final Set inputFields = new LinkedHashSet();
        final Collection views = getJsps();
        for (Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            StrutsViewDecorator view = (StrutsViewDecorator) iterator.next();
            inputFields.addAll(view.getInputFields());
        }
        return inputFields;
    }

    public Set getResetInputFields()
    {
        final Set resetInputFields = new LinkedHashSet();
        final Collection views = getJsps();
        for (Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            StrutsViewDecorator view = (StrutsViewDecorator) iterator.next();
            resetInputFields.addAll(view.getResetInputFields());
        }
        return resetInputFields;
    }

    public String getValidationMethodName()
    {
        return "validate" + StringUtilsHelper.upperCaseFirstLetter(getFormBeanName());
    }

    // ------------- relations ------------------

    protected Collection handleGetServlets()
    {
        final Collection controllerClasses = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd = (AssociationEndDecorator)iterator.next();
            final ClassifierDecorator otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_CONTROLLER))
                controllerClasses.add(otherEnd.getMetaObject());
        }
        return controllerClasses;
    }

    protected Collection handleGetJsps()
    {
        final Collection views = new LinkedList();
        for (Iterator dependencyIterator=getDependencies().iterator(); dependencyIterator.hasNext();)
        {
            Object element = dependencyIterator.next();
            Dependency dep = (Dependency)((DependencyDecorator) element).getMetaObject();
            ModelElement supplier = (ModelElement) dep.getSupplier().iterator().next();
            ModelElementDecorator supplierDecorator = (ModelElementDecorator)decoratedElement(supplier);
            if (supplierDecorator instanceof ClassifierDecorator && supplierDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW))
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
