package org.andromda.translation.ocl.parser;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * Retrieves information from the OCL parser exceptions in a more user friendly format.
 */
public class OclParserException
        extends RuntimeException
{
    private static final long serialVersionUID = 3340016550152777823L;

    private StringBuffer messageBuffer;
    private int errorLine;
    private int errorColumn;

    /**
     * Constructs an instance of OclParserException.
     *
     * @param message
     */
    public OclParserException(String message)
    {
        super();
        if (StringUtils.isNotBlank(message))
        {
            extractErrorPosition(message);
        }
    }

    /**
     * @see Throwable#getMessage()
     */
    public String getMessage()
    {
        int position = 0;
        if (this.errorLine != -1)
        {
            String message = "line: " + errorLine + ' ';
            this.messageBuffer.insert(0, message);
            position = message.length();
        }
        if (this.errorColumn != -1)
        {
            String message = "column: " + errorColumn + ' ';
            this.messageBuffer.insert(position, message);
            position = position + message.length();
        }
        this.messageBuffer.insert(position, "--> ");
        return this.messageBuffer.toString();
    }

    /**
     * Extracts the error positioning from exception message, if possible. Assumes SableCC detail message format: "["
     * <line>"," <col>"]" <error message>.
     *
     * @param message the message to extract.
     */
    private void extractErrorPosition(String message)
    {
        this.messageBuffer = new StringBuffer();
        if (StringUtils.isNotBlank(message) && message.charAt(0) == '[')
        {
            // Positional data seems to be available
            StringTokenizer tokenizer = new StringTokenizer(message.substring(1), ",]");

            try
            {
                this.errorLine = Integer.parseInt(tokenizer.nextToken());
                this.errorColumn = Integer.parseInt(tokenizer.nextToken());

                this.messageBuffer.append(tokenizer.nextToken("").substring(2));
            }
            catch (NumberFormatException ex)
            {
                // No positional information
                this.messageBuffer.append(message);
                this.errorLine = -1;
                this.errorColumn = -1;
            }
        }
        else
        {
            // No positional information
            this.messageBuffer.append(message);
            this.errorLine = -1;
            this.errorColumn = -1;
        }
    }
}