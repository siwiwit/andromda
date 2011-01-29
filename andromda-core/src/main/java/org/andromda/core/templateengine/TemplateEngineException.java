package org.andromda.core.templateengine;


/**
 * Any unchecked exception that will be thrown when any processing by a TemplateEngine occurs..
 */
public class TemplateEngineException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;

    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param parent the parent exception
     */
    public TemplateEngineException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param message the exception message
     */
    public TemplateEngineException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param message the exception message
     * @param parent the parent exception
     */
    public TemplateEngineException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}