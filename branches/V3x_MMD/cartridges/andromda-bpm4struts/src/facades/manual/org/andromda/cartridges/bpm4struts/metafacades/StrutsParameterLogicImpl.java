package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
 */
public class StrutsParameterLogicImpl
        extends StrutsParameterLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
{
    // ---------------- constructor -------------------------------
    
    public StrutsParameterLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsParameter ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getGetterName()()
     */
    public java.lang.String getGetterName()
    {
        return "get" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getSetterName()()
     */
    public java.lang.String getSetterName()
    {
        return "set" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()()
     */
    public java.lang.String getResetValue()
    {
        final String type = getFullyQualifiedName();

        if ("boolean".equals(type)) return "false";
        else if (getType().isPrimitiveType()) return "0";
        else return "null";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#mustReset()()
     */
    public boolean mustReset()
    {
        final String type = getFullyQualifiedName();
        return Boolean.class.getName().equals(type) || "boolean".equals(type);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageKey()()
     */
    public java.lang.String getMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getNameSpace().getName() + ' ' + getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageValue()()
     */
    public java.lang.String getMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleKey()()
     */
    public java.lang.String getTitleKey()
    {
        return getMessageKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleValue()()
     */
    public java.lang.String getTitleValue()
    {
        return getMessageValue();
    }

    public String getWidgetType()
    {
        final String fieldType = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE);

        if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_TEXTBLOCK.equalsIgnoreCase(fieldType))
        {
            return "textarea";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_CHOICE.equalsIgnoreCase(fieldType))
        {
            return "radio";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_OPTION.equalsIgnoreCase(fieldType))
        {
            return "checkbox";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
        {
            return "select";
        }
        else
        {
            return "text";
        }
    }

    public boolean isMultiple()
    {
        try
        {
            Class clazz = Class.forName(getType().getFullyQualifiedName());
            if (clazz.isArray()) return true;
            Class collectionInterface = Class.forName("java.util.Collection");
            return collectionInterface.isAssignableFrom(clazz);
        }
        catch(Exception exception)
        {
            return false;
        }
    }

    public boolean hasBackingList()
    {
        return "select".equals(getWidgetType()) && isMultiple();
    }

    public String getBackingListName()
    {
        return getName() + "BackingList";
    }
    
    // ------------- relations ------------------

}
