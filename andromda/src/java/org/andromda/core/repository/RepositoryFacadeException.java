package org.andromda.core.repository;


/**
 * An exception thrown whenever an error is encountered while performing
 * processing RepositoryFacade processing.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 */
public final class RepositoryFacadeException
    extends RuntimeException
{

    /**
     * Constructor for the MetaDataReadException object
     */
    public RepositoryFacadeException()
    {
        super();
    }

    /**
     * Constructor for the MetaDataReadException object
     * 
     * @param message describes cause of the exception
     */
    public RepositoryFacadeException(
        String message)
    {
        super(message);
    }

    /**
     * Constructor for the MetaDataReadException object
     * 
     * @param message describes cause of the exception
     * @param cause original exception that caused this exception
     */
    public RepositoryFacadeException(
        String message,
        Throwable cause)
    {
        super(message + ": " + cause.getMessage());
        getRootCause(cause);
    }

    private static Throwable getRootCause(Throwable th)
    {
        Throwable cause = th;
        if (cause.getCause() != null)
        {
            cause = cause.getCause();
            cause = getRootCause(cause);
        }
        return cause;
    }
}