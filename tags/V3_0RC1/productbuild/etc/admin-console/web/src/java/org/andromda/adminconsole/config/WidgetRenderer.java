package org.andromda.adminconsole.config;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class WidgetRenderer implements Serializable
{
    public static String renderTextfield(String parameterName, Object value, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<input type=\"text\" name=\"");
        buffer.append(parameterName);
        buffer.append("\" value=\"");
        buffer.append(value);
        buffer.append('\"');
        if (readOnly)
        {
            buffer.append(" readonly=\"readonly\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append("/>");

        return buffer.toString();
    }

    private static boolean isTrue(Object object)
    {
        boolean isTrue;

        if (object instanceof Boolean) isTrue = ((Boolean)object).booleanValue();
        else if (object instanceof String) isTrue = StringUtils.isNotBlank((String)object);
        else isTrue = (object!=null);

        return isTrue;
    }

    public static String renderCheckbox(String parameterName, Object value, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<input type=\"checkbox\" name=\"");
        buffer.append(parameterName);
        buffer.append("\" value=\"true\"");
        if (isTrue(value))
        {
            buffer.append(" checked=\"checked\"");
        }
        if (readOnly)
        {
            buffer.append(" disabled=\"disabled\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append("/>");

        return buffer.toString();
    }

    public static String renderSelect(String parameterName, Object value, Object[] values, Object[] labels, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<select name=\"");
        buffer.append(parameterName);
        buffer.append('\"');
        if (readOnly)
        {
            buffer.append(" disabled=\"disabled\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append('>');

        if (values != null)
        {
            buffer.append("<option value=\"\"></option>");
            for (int i = 0; i < values.length; i++)
            {
                buffer.append("<option");
                if (values[i].equals(value)) buffer.append(" selected=\"selected\"");
                buffer.append(" value=\"");
                buffer.append(values[i]);
                buffer.append("\">");
                buffer.append(labels[i]);
                buffer.append("</option>");
            }
        }
        buffer.append("</select>");

        return buffer.toString();
    }

}
