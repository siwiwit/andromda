package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.common.StringUtilsHelper;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public abstract class StrutsInputFieldDecoratorImpl extends StrutsInputFieldDecorator
{
    // ---------------- constructor -------------------------------
    public StrutsInputFieldDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsInputFieldDecorator ...


    public java.lang.Boolean isRequired()
    {
        final String requiredValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED);
        return makeBoolean(requiredValue);
    }

    private Boolean makeBoolean(String string)
    {
        if (string != null &&
            !"true".equalsIgnoreCase(string) &&
            !"yes".equalsIgnoreCase(string) &&
            !"0".equals(string))
        {
            return Boolean.FALSE;
        }
        else
        {
            return Boolean.TRUE;
        }
    }

    public String getMaskPattern()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_PATTERN);
    }

    public Boolean isResetField()
    {
        return Boolean.FALSE;
    }

    public String getMessageKey()
    {
        return StringUtilsHelper.separate(getJsp().getName() + '.' +getName(), ".").toLowerCase();
    }

    public String getMessageValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(getName(), " "));
    }

    public String getValidationMsgKey()
    {
        return StringUtilsHelper.separate(getJsp().getFormBean().getFormBeanName() + '.' +getName(), ".").toLowerCase();
    }

    public abstract String getFieldType();

    // ------------- relations ------------------

    /**
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetJsp()
    {
        return getOwner().getMetaObject();
    }

    // ------------------------------------------------------------

    public void validate() throws DecoratorValidationException
    {
/*
        // the name must not be empty
        final String name = getName();
        if ((name == null) || (name.trim().length() == 0))
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // it must be associated to a single View
        final Collection views = getAssociatedViews();
        if (views.size() != 1)
            throw new DecoratorValidationException(this, "One and only one JSP may be associated with this input field");
*/
    }
}
