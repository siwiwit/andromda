package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class StrutsTextAreaDecoratorImpl extends StrutsTextAreaDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsTextAreaDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTextAreaDecorator ...

    public java.lang.Integer getRowCount()
    {
        final String size = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_ROWS);
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

    public java.lang.Integer getColumnCount()
    {
        final String size = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_COLS);
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

    // ------------- relations ------------------

}
