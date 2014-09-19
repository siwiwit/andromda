package org.andromda.core.translation;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Stores the translated expression,
 *
 * @author Chad Brandon
 */
public class Expression
{
    /**
     * The resulting translated expression.
     */
    private StringBuffer translatedExpression;

    /**
     * Creates a new instance of this Expression object
     *
     * @param originalExpression the expression that will be translated.
     */
    public Expression(final String originalExpression)
    {
        ExceptionUtils.checkEmpty("originalExpression", originalExpression);
        this.originalExpression = StringUtils.trimToEmpty(originalExpression);
        this.translatedExpression = new StringBuffer();
    }

    /**
     * Appends the value of the value of the <code>object</code>'s toString result to the current translated expression
     * String Buffer.
     *
     * @param object the object to append.
     */
    public void appendToTranslatedExpression(final Object object)
    {
        this.translatedExpression.append(object);
    }

    /**
     * Appends a space character to the current translated expression String Buffer.
     */
    public void appendSpaceToTranslatedExpression()
    {
        this.translatedExpression.append(' ');
    }

    /**
     * Replaces the regular expression <code>pattern</code> with the replacement within the translated expression
     * buffer.
     *
     * @param pattern     the regular expression pattern to replace
     * @param replacement the replacement string.
     */
    public void replaceInTranslatedExpression(
        final String pattern,
        final String replacement)
    {
        this.translatedExpression =
            new StringBuffer(this.getTranslatedExpression().replaceAll(pattern, replacement));
    }

    /**
     * Performs replacement of the value of the <code>object</code>'s toString result at the start and end positions of
     * the buffer containing the Expression.
     *
     * @param position the position at which to insert
     * @param object   the
     * @see StringBuffer#insert(int,String)
     */
    public void insertInTranslatedExpression(
        final int position,
        final Object object)
    {
        this.translatedExpression.insert(position, object);
    }

    /**
     * Returns the expression after translation.
     *
     * @return String
     */
    public String getTranslatedExpression()
    {
        return TranslationUtils.removeExtraWhitespace(this.translatedExpression.toString());
    }

    /**
     * The original expression before translation
     */
    private String originalExpression;

    /**
     * Returns the expression before translation.
     *
     * @return String
     */
    public String getOriginalExpression()
    {
        return TranslationUtils.removeExtraWhitespace(this.originalExpression);
    }

    /**
     * The element to which the expression applies.
     */
    private String contextElement;

    /**
     * Returns the element which is the context of this expression.
     *
     * @return String the context element element.
     */
    public String getContextElement()
    {
        final String methodName = "Expression.getContextElement";
        if (this.contextElement == null)
        {
            throw new ExpressionException(methodName + " - contextElement can not be null");
        }
        return this.contextElement;
    }

    /**
     * The kind of the expression that was translated.
     */
    private String kind;

    /**
     * Returns the Kind of this Expression (inv, post, or pre)
     *
     * @return String returns the Kind of this translation
     */
    public String getKind()
    {
        final String methodName = "Expression.getKind";
        if (this.contextElement == null)
        {
            throw new ExpressionException(methodName + " - kind can not be null");
        }
        return this.kind;
    }

    /**
     * The name of the expression.
     */
    private String name;

    /**
     * Gets the name of the expression.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Sets the context element (the element to which the expression applies: the element declared after the
     * <code>context</code>)
     *
     * @param contextElement the name of the element which is the context element.
     */
    public void setContextElement(final String contextElement)
    {
        this.contextElement = contextElement;
    }

    /**
     * Sets the "kind" of the expression (i.e, "pre", "post", "inv", etc.)
     *
     * @param kind the kind to set.
     */
    public void setKind(final String kind)
    {
        this.kind = kind;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}