package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;
import org.andromda.core.common.StringUtilsHelper;

import java.util.*;


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

    public String getValidWhen()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_VALIDWHEN);
    }

    public boolean isRequired()
    {
        final String requiredValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED);
        return makeBoolean(requiredValue);
    }

    private boolean makeBoolean(String string)
    {
        return !( "false".equalsIgnoreCase(string) || "no".equalsIgnoreCase(string) ||
                  "0".equalsIgnoreCase(string) || "off".equalsIgnoreCase(string) );
    }

    public boolean isResetField()
    {
        return false;
    }

    public String getMessageKey()
    {
        return StringUtilsHelper.separate(getJsp().getName() + '.' +getName(), ".").toLowerCase();
    }

    public String getMessageValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(getName(), " "));
    }

    public String getValidatorMsgKey()
    {
        return StringUtilsHelper.separate(getJsp().getFormBean().getFormBeanName() + '.' +getName(), ".").toLowerCase();
    }

    public String getValidatorFormat()
    {
        final String format = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_FORMAT);
        return (format == null) ? null : format.trim();
    }

    public Collection getValidatorTypes()
    {
        final String type = getType().getFullyQualifiedName();
        final String format = getValidatorFormat();

        final Collection validatorTypes = new LinkedList();

        if (isRequired()) validatorTypes.add("required");

        if (format != null)
        {
            final boolean isRangeFormat = isRangeFormat(format);
            if (isValidatorByte(type)) validatorTypes.add("byte");
            else if (isValidatorShort(type)) validatorTypes.add("short");
            else if (isValidatorLong(type)) validatorTypes.add("long");
            else if (isValidatorDate(type)) validatorTypes.add("date");
            else if (isValidatorInteger(type))
            {
                validatorTypes.add("integer");
                if (isRangeFormat) validatorTypes.add("intRange");
            }
            else if (isValidatorFloat(type))
            {
                validatorTypes.add("float");
                if (isRangeFormat) validatorTypes.add("floatRange");
            }
            else if (isValidatorDouble(type))
            {
                validatorTypes.add("double");
                if (isRangeFormat) validatorTypes.add("doubleRange");
            }
            else if (isValidatorString(type))
            {
                if (isEmailFormat(format)) validatorTypes.add("email");
                else if (isCreditCardFormat(format)) validatorTypes.add("creditCard");
                else if (isMinLengthFormat(format)) validatorTypes.add("minlength");
                else if (isMaxLengthFormat(format)) validatorTypes.add("maxlength");
                else if (isPatternFormat(format)) validatorTypes.add("mask");
            }
        }
        return validatorTypes;
    }

    public Collection getValidatorArgs(String validatorType)
    {
        final Collection args = new LinkedList();

        if ( "intRange".equals(validatorType) || "floatRange".equals(validatorType) || "doubleRange".equals(validatorType) )
        {
            args.add("${var:min}");
            args.add("${var:max}");
        }
        else if ( "minlength".equals(validatorType) )
        {
            args.add("${var:minlength}");
        }
        else if ( "maxlength".equals(validatorType) )
        {
            args.add("${var:maxlength}");
        }
        return args;
    }

    public Collection getValidatorVars()
    {
        final Collection vars = new LinkedList();

        final String type = getType().getFullyQualifiedName();
        final String format = getValidatorFormat();
        if (format != null)
        {
            final boolean isRangeFormat = isRangeFormat(format);

            if (isRangeFormat && (isValidatorInteger(type) || isValidatorFloat(type) || isValidatorDouble(type)))
            {
                vars.add(Arrays.asList(new Object[] { "min", getRangeStart(format) }));
                vars.add(Arrays.asList(new Object[] { "max", getRangeEnd(format) }));
            }
            else if (isValidatorString(type))
            {
                if (isMinLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[] { "minlength", getMinLengthValue(format) }));
                }
                else if (isMaxLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[] { "maxlength", getMaxLengthValue(format) }));
                }
                else if (isPatternFormat(format))
                {
                    vars.add(Arrays.asList(new Object[] { "mask", getPatternValue(format) }));
                }
            }
            else if (isValidatorDate(type))
            {
                if (isStrictDateFormat(format))
                {
                    vars.add(Arrays.asList(new Object[] { "datePatternStrict", getDateFormat(format) }));
                }
                else
                {
                    vars.add(Arrays.asList(new Object[] { "datePattern", getDateFormat(format) }));
                }
            }
        }
        return vars;
    }

    public abstract String getFieldType();


    private boolean isValidatorByte(String type)
    {
        return "byte".equals(type) || "java.lang.Byte".equals(type);
    }

    private boolean isValidatorShort(String type)
    {
        return "short".equals(type) || "java.lang.Short".equals(type);
    }

    private boolean isValidatorInteger(String type)
    {
        return "int".equals(type) || "java.lang.Integer".equals(type);
    }

    private boolean isValidatorLong(String type)
    {
        return "long".equals(type) || "java.lang.Long".equals(type);
    }

    private boolean isValidatorFloat(String type)
    {
        return "float".equals(type) || "java.lang.Float".equals(type);
    }

    private boolean isValidatorDouble(String type)
    {
        return "double".equals(type) || "java.lang.Double".equals(type);
    }

    private boolean isValidatorDate(String type)
    {
        return "java.util.Date".equals(type) || "java.sql.Date".equals(type);
    }

    private boolean isValidatorString(String type)
    {
        return "java.lang.String".equals(type);
    }

    private boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isCreditCardFormat(String format)
    {
        return "creditcard".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isRangeFormat(String format)
    {
        return "range".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isPatternFormat(String format)
    {
        return "pattern".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isStrictDateFormat(String format)
    {
        return "strict".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isMinLengthFormat(String format)
    {
        return "minlength".equalsIgnoreCase(getToken(format,0,2));
    }

    private boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(getToken(format,0,2));
    }

    private String getRangeStart(String format)
    {
        return getToken(format,1,3);
    }

    private String getRangeEnd(String format)
    {
        return getToken(format,2,4);
    }

    private String getDateFormat(String format)
    {
        return (isStrictDateFormat(format)) ? getToken(format,1,3) : getToken(format,0,2);
    }

    private String getMinLengthValue(String format)
    {
        return getToken(format,1,3);
    }

    private String getMaxLengthValue(String format)
    {
        return getToken(format,1,3);
    }

    private String getPatternValue(String format)
    {
        return getToken(format,1,3);
    }

    private String getToken(String string, int index, int limit)
    {
        String[] tokens = string.split("[\\s]", limit);
        return (index >= tokens.length) ? null : tokens[index];
    }
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
            throw new DecoratorValidatorException(this, "Name may not be empty or only contain whitespace");

        // it must be associated to a single View
        final Collection views = getAssociatedViews();
        if (views.size() != 1)
            throw new DecoratorValidatorException(this, "One and only one JSP may be associated with this input field");
*/
    }
}
