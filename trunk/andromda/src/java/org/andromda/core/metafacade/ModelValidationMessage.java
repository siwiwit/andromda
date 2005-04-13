package org.andromda.core.metafacade;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Stores the validation messages that are stored up to be output at the end model processing.
 *
 * @author Chad Brandon
 */
public class ModelValidationMessage
{
    /**
     * Constructs a new instance of MetafacadeValidationMessage taking a <code>metafacade</code> instance and a
     * <code>message</code> indicating what has been violated.
     *
     * @param metafacadeClass  the Class of the metafacade being validated.
     * @param modelElementName the name of the model element being validated.
     * @param message          the message to to communitate about the validation.
     */
    public ModelValidationMessage(MetafacadeBase metafacade, String message)
    {
        final String constructorName = "MetafacadeValidationMessage";
        ExceptionUtils.checkNull(constructorName, "metafacade", metafacade);
        ExceptionUtils.checkEmpty(constructorName, "message", message);
        this.metafacade = metafacade;
        this.message = message;
    }

    private String message;

    /**
     * @return Returns the message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Stores the actual metafacade to which this validation message applies.
     */
    private MetafacadeBase metafacade;

    /**
     * Stores the metafacade name which is only constructed the very first time.
     */
    private String metafacadeName = null;

    /**
     * Gets the name of the metafacade to which this validation message applies.
     *
     * @return Returns the metafacade.
     */
    public String getMetafacadeName()
    {
        if (this.metafacadeName == null)
        {
            final String seperator = String.valueOf(this.metafacade.getConfiguredProperty(
                    MetafacadeProperties.METAFACADE_NAMESPACE_SCOPE_OPERATOR));
            StringBuffer name = new StringBuffer();
            for (MetafacadeBase metafacade = this.metafacade; metafacade != null; metafacade = (MetafacadeBase) metafacade.getValidationOwner())
            {
                if (StringUtils.isNotBlank(metafacade.getValidationName()))
                {
                    String validationName = metafacade.getValidationName();
                    if (metafacade.getValidationOwner() != null)
                    {
                        // remove package if we have an owner
                        validationName = validationName.replaceAll(".*" + seperator, "");
                    }
                    if (name.length() > 0)
                    {
                        name.insert(0, seperator);
                    }
                    name.insert(0, validationName);
                }
            }
            this.metafacadeName = name.toString();
        }
        return metafacadeName;
    }

    /**
     * Stores the metafacade class displayed within the message, this is only retrieved the very first time.
     */
    private Class metafacadeClass = null;

    /**
     * Gets the class of the metafacade to which this validation message applies.
     *
     * @return the metafacade Class.
     */
    public Class getMetafacadeClass()
    {
        if (metafacadeClass == null)
        {
            this.metafacadeClass = this.metafacade.getClass();
            List interfaces = ClassUtils.getAllInterfaces(this.metafacade.getClass());
            if (interfaces != null && !interfaces.isEmpty())
            {
                this.metafacadeClass = (Class)interfaces.iterator().next();
            }
        }
        return this.metafacadeClass;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer toString = new StringBuffer();
        toString.append("[");
        toString.append(this.getMetafacadeName());
        toString.append("]");
        toString.append(":");
        toString.append(this.message);
        return toString.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object)
    {
        boolean equals = object != null && ModelValidationMessage.class == object.getClass();
        if (equals)
        {
            ModelValidationMessage message = (ModelValidationMessage) object;
            equals = message.toString().equals(this.toString());
        }
        return equals;
    }
}