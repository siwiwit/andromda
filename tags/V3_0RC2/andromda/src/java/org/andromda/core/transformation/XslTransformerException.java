package org.andromda.core.transformation;

public class XslTransformerException
    extends RuntimeException
{
    /**
     * Constructs an instance of TransformerException.
     *
     * @param parent the parent throwable
     */
    public XslTransformerException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TransformerException.
     *
     * @param message any message to give
     */
    public XslTransformerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TransformerException.
     *
     * @param message any message to give.
     * @param parent the parent throwab.e
     */
    public XslTransformerException(String message, Throwable parent)
    {
        super(message, parent);
    }
}