package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;

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

    public Collection getTriggerNames()
    {
        final Collection triggers = DecoratorBase.decoratedElements(getActionState().getOutgoing());
        final Collection triggerNames = new LinkedList();
        for (Iterator iterator = triggers.iterator(); iterator.hasNext();)
        {
            StrutsTransitionDecorator transition = (StrutsTransitionDecorator)iterator.next();
            triggerNames.add(transition.getTriggerName());
        }
        return triggerNames;
    }

    public String getViewName()
    {
        return this.getFullyQualifiedJspFilename();
    }

    // ------------- relations ------------------
    public ActionState getActionState()
    {
        final String actionStateName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE);
        final UmlPackage model = MetaDecoratorUtil.getModel(this);

        final Collection allActionStates = model.getUseCases().getUseCase().refAllOfType();
        for (Iterator iterator = allActionStates.iterator(); iterator.hasNext();)
        {
            ActionState actionState = (ActionState) iterator.next();
            if (actionStateName.equalsIgnoreCase(actionState.getName()))
                return actionState;
        }

        return null;
    }


    protected ModelElement handleGetModel()
    {
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            Classifier participant = associationEnd.getParticipant();
            ClassifierDecorator participantDecorator = (ClassifierDecorator) DecoratorBase.decoratedElement(participant);
            if (participantDecorator.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MODEL).booleanValue())
                return participant;
        }
        return null;
    }

    /**
     *
     */
    public java.util.Collection handleGetInputFields()
    {
        final Collection attributes = getAttributes();
        final Collection inputFields = new LinkedList();

        for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            Attribute attribute = (Attribute) iterator.next();
            AttributeDecorator attributeDecorator = (AttributeDecorator)DecoratorBase.decoratedElement(attribute);
            if (attributeDecorator instanceof StrutsInputFieldDecorator)
            {
                inputFields.add(attribute);
            }
        }

        return inputFields;
    }

    protected Collection handleGetResetInputFields()
    {
        final Collection attributes = getAttributes();
        final Collection resetInputFields = new LinkedList();

        for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            Attribute attribute = (Attribute) iterator.next();
            AttributeDecorator attributeDecorator = (AttributeDecorator)DecoratorBase.decoratedElement(attribute);
            if (attributeDecorator instanceof StrutsInputFieldDecorator)
            {
                ClassifierDecorator classifierDecorator = (ClassifierDecorator)DecoratorBase.decoratedElement(attributeDecorator.getType());
                final String fqType = classifierDecorator.getFullyQualifiedName();
                if ("boolean".equals(fqType))
                {
                    resetInputFields.add(attribute);
                }
            }
        }

        return resetInputFields;
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
