package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class StrutsListDecoratorImpl extends StrutsListDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsListDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsListDecorator ...

    public java.lang.Integer getSize()
    {
        final String size = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_SIZE);
        if (size == null)
        {
            return null;
        }
        else
        {
            try
            {
                return new Integer(size);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
    }

    public String getWidgetType()
    {
        return "select";
    }

    public boolean isMultiple()
    {
        return true;
    }

    public java.lang.String getBackingPropertyName()
    {
        return getName() + "BackingList";
    }

    // ------------- relations ------------------
}
