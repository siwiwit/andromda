package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;


/**
 * Metaclass decorator implementation for $decoratedMetacName
 */
public class StrutsParameterDecoratorImpl extends StrutsParameterDecorator
{
    // ---------------- constructor -------------------------------
    public StrutsParameterDecoratorImpl(org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsParameterDecorator ...
    public String getKey()
    {
        return StringUtilsHelper.separate(getMessage().getJsp().getName() + '.' +metaObject.getName(), ".").toLowerCase();
    }

    public String getValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(metaObject.getName(), " "));
    }

    public String getTitleKey()
    {
        return getKey() + ".title";
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(metaObject.getName(), " "));
    }

    public boolean isRequired()
    {
        final String requiredValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED);
        return makeBoolean(requiredValue);
    }

    public boolean isResetField()
    {
        return false;
    }

    // ------------- relations ------------------

    /**
     *
     *
     */
    protected ModelElement handleGetMessage()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected ModelElement handleGetStrutsParameterDecorator()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected Collection handleGetSupplierParameters()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // ------------------------------------------------------------

    private boolean makeBoolean(String string)
    {
        return !( "false".equalsIgnoreCase(string) || "no".equalsIgnoreCase(string) ||
                "0".equalsIgnoreCase(string) || "off".equalsIgnoreCase(string) );
    }


}
