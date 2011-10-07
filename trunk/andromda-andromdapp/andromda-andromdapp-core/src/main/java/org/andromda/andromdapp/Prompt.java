package org.andromda.andromdapp;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Represents a user prompt used by AndroMDApp.
 *
 * @author Chad Brandon
 */
public class Prompt
{
    /**
     * The unique prompt identifier.
     */
    private String id;

    /**
     * Gets the unique id of this prompt.
     *
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the unique id of this prompt.
     *
     * @param id The id to set.
     */
    public void setId(final String id)
    {
        this.id = id;
    }

    /**
     * Whether or not this prompt is required.
     */
    private boolean required = true;

    /**
     * Sets whether or not this prompt is required,
     * by default the prompt is <strong>required</strong>.
     *
     * @param required whether or not this prompt is required
     */
    public void setRequired(final boolean required)
    {
        this.required = required;
    }

    /**
     * Indicates whether or not this prompt is required.
     *
     * @return true/false
     */
    public boolean isRequired()
    {
        return this.required;
    }

    /**
     * Stores the actual text of the prompt.
     */
    private String text;

    /**
     * Gets the text of the prompt.
     *
     * @return Returns the text.
     */
    public String getText()
    {
        final StringBuilder text = new StringBuilder();
        if (this.text != null)
        {
            text.append(this.text);
        }
        if (!this.responses.isEmpty())
        {
            text.append(' ').append(this.getResponsesAsString());
        }
        text.append(": ");
        return text.toString();
    }

    /**
     * Sets the prompt text.
     *
     * @param text The text to set.
     */
    public void setText(final String text)
    {
        this.text = StringUtils.trim(text);
    }

    /**
     * Stores the possible responses of the prompt.
     */
    private final List<String> responses = new ArrayList<String>();

    /**
     * Adds a response to the possible responses.
     *
     * @param response the response to add.
     */
    public void addResponse(final String response)
    {
        if (StringUtils.isNotBlank(response))
        {
            this.responses.add(response.trim());
        }
    }

    /**
     * Indicates whether or not the given <code>response</code> is valid
     * according to the valid responses contained in this prompt instance.
     *
     * @param response the response to check.
     * @return true/false
     */
    public boolean isValidResponse(final String response)
    {
        return this.responses.contains(response) ||
            (this.responses.isEmpty() && (!this.isRequired() || (StringUtils.isNotBlank(response))));
    }

    /**
     * Gets the response object converted to the appropriate
     * type or just as it is (if no conversion took place or conversion
     * failed).
     *
     * @param response the response to convert.
     * @return the response as an object.
     */
    public Object getResponse(final Object response)
    {
        return AndroMDAppUtils.convert(
            response,
            this.responseType);
    }

    /**
     * Stores the response type.
     */
    private String responseType;

    /**
     * Sets the response type to use (i.e the fully qualified name of the
     * type to which it should be converted when placed into the the template context).
     *
     * @param responseType the fully qualified response type name.
     */
    public void setResponseType(final String responseType)
    {
        this.responseType = responseType;
    }

    /**
     * Gets the responses as a formatted string.
     *
     * @return the responses list as a string.
     */
    private String getResponsesAsString()
    {
        final StringBuilder responsesString = new StringBuilder("[");
        for (String response : this.responses)
        {
            responsesString.append(response).append(", ");
        }
        //remove last ", "
        if(!this.responses.isEmpty())
        {
            responsesString.delete(responsesString.length()-2, responsesString.length());
        }
        responsesString.append(']');
        return responsesString.toString();
    }

    /**
     * The conditions that apply to this prompt.
     */
    private final List<Condition> conditions = new ArrayList<Condition>();

    /**
     * Adds a condition to this prompt.
     *
     * @param condition the condition which must apply to this prompt.
     */
    public void addCondition(final Condition condition)
    {
        this.conditions.add(condition);
    }

    /**
     * Gets the conditions defined in this prompt.
     *
     * @return the conditions that are defined within this prompt.
     */
    public List<Condition> getConditions()
    {
        return this.conditions;
    }

    /**
     * The preconditions that must be valid for this prompt
     * in order for it to be executed.
     */
    private final List<Conditions> preconditions = new ArrayList<Conditions>();

    /**
     * Adds preconditions to this prompt.
     *
     * @param preconditions the preconditions to add.
     */
    public void addPreconditions(final Conditions preconditions)
    {
        this.preconditions.add(preconditions);
    }

    /**
     * Gets the preconditions for this prompt.
     *
     * @return the prompt preconditions.
     */
    public List<Conditions> getPreconditions()
    {
        return this.preconditions;
    }

    /**
     * Whether or not the value of the response should be
     * set to a boolean value of true.
     */
    private boolean setResponseAsTrue;

    /**
     * Whether or not the response should be set to a boolean value of <code>true</code>.
     *
     * @return Returns the setResponseAsTrue.
     */
    public boolean isSetResponseAsTrue()
    {
        return setResponseAsTrue;
    }

    /**
     * Sets whether or not the response should be set to a boolean value of true.
     *
     * @param setResponseAsBoolean The setResponseAsTrue to set.
     */
    public void setSetResponseAsTrue(final boolean setResponseAsBoolean)
    {
        this.setResponseAsTrue = setResponseAsBoolean;
    }
}
