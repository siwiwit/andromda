package org.andromda.core.repository;


/**
 * An exception thrown whenever an error is encountered while performing RepositoryFacade processing.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 * @author Bob Fields
 */
public final class RepositoryFacadeException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;
    /**
     * Constructor for the RepositoryFacadeException object
     *
     * @param message describes cause of the exception
     */
    public RepositoryFacadeException(String message)
    {
        super(message);
    }

    /**
     * Constructor for the RepositoryFacadeException object
     *
     * @param parent describes cause of the exception
     */
    public RepositoryFacadeException(final Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructor for the RepositoryFacadeException object
     *
     * @param message describes cause of the exception
     * @param cause original exception that caused this exception
     */
    public RepositoryFacadeException(
        String message,
        Throwable cause)
    {
        super(message,
            getRootCause(cause));
    }

    private static Throwable getRootCause(final Throwable throwable)
    {
        Throwable cause = throwable;
        if (cause.getCause() != null)
        {
            cause = cause.getCause();
            cause = getRootCause(cause);
        }
        return cause;
    }
}