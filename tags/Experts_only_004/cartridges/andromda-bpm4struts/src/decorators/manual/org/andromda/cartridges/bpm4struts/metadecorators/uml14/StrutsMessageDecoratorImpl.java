package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Metaclass decorator implementation for $decoratedMetacName
 */
public class StrutsMessageDecoratorImpl extends StrutsMessageDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsMessageDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsMessageDecorator ...

    public java.lang.String getValidationMethodName()
    {
        return "validate" + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public java.lang.String getFormName()
    {
        String beanName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_FORM_BEAN_NAME);
        if (beanName == null)
        {
            beanName = StringUtilsHelper.lowerCaseFirstLetter(getName());
        }
        return beanName;
    }

    public boolean isForm()
    {
        return getInputParameters().isEmpty() == false;
    }

    public boolean isHyperlink()
    {
        return getInputParameters().isEmpty() == true;
    }

    public boolean isParagraph()
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return false;
    }

    // ------------- relations ------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetTriggerTransitions()
    {
        Collection transitions = getJsp().getActionState().handleGetTriggerTransitions();
        if (transitions.size() > 1)
        {
            final Collection taggedValueTransitions = new LinkedList();
            Collection taggedValues = getTaggedValues(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TRIGGER);
            for (Iterator taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext();)
            {
                taggedValueTransitions.add(findTriggerByName(transitions, taggedValueIterator.next().toString()));
            }
            transitions = taggedValueTransitions;
        }
        return transitions;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetParameters()
    {
        return handleGetAttributes();
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetResetInputParameters()
    {
        final Collection resetInputParameters = new LinkedList();
        final Collection allParameters = getParameters();
        for (Iterator parameterIterator = allParameters.iterator(); parameterIterator.hasNext();)
        {
            StrutsParameterDecorator parameterDecorator = (StrutsParameterDecorator) parameterIterator.next();
            if (parameterDecorator.isResetField())
                resetInputParameters.add(parameterDecorator.getMetaObject());
        }
        return resetInputParameters;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetInputParameters()
    {
        final Collection resetInputParameters = new LinkedList();
        final Collection allParameters = getParameters();
        for (Iterator parameterIterator = allParameters.iterator(); parameterIterator.hasNext();)
        {
            StrutsParameterDecorator parameterDecorator = (StrutsParameterDecorator) parameterIterator.next();
            if (parameterDecorator instanceof StrutsInputParameterDecorator)
                resetInputParameters.add(parameterDecorator.getMetaObject());
        }
        return resetInputParameters;
    }

    // ------------------------------------------------------------

    protected ModelElement handleGetJsp()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd = (AssociationEndDecorator)iterator.next();
            final ClassifierDecorator otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW))
                return otherEnd.getMetaObject();
        }
        return null;
    }

    // ------------------------------------------------------------

    private Collection getTaggedValues(String tagName)
    {
        final Collection taggedValues = getTaggedValues();
        for (Iterator taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext();)
        {
            TaggedValue taggedValue = (TaggedValue) taggedValueIterator.next();
            if (taggedValue != null)
            {
                if (StringUtils.trimToEmpty(taggedValue.getName()).equals(tagName))
                {
                    return taggedValue.getDataValue();
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    private StrutsTransitionDecorator findTriggerByName(Collection triggerTransitions, String name)
    {
        for (Iterator triggerIterator = triggerTransitions.iterator(); triggerIterator.hasNext();)
        {
            StrutsTransitionDecorator transitionDecorator = (StrutsTransitionDecorator) triggerIterator.next();
            if (name.equalsIgnoreCase(transitionDecorator.getTriggerName()))
                return transitionDecorator;
        }
        return null;
    }

}
