package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.ValueSpecification;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{
    public AttributeLinkFacadeLogicImpl(AttributeLink metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    protected java.lang.Object handleGetAttribute()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwningInstance());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    protected java.lang.Object handleGetValue()
    {
        final Object value;

        final List values = this.metaObject.getValues();

        if (values == null || values.isEmpty())
        {
            value = null;
        }
        else if (values.get(0) instanceof ValueSpecification)
        {
            value = InstanceFacadeLogicImpl.createInstanceFor((ValueSpecification)values.get(0));
        }
        else
        {
            value = values.get(0);
        }

        return value;
    }
}