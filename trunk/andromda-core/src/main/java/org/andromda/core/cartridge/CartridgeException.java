package org.andromda.core.cartridge;

/**
 * This exception is thrown when a special situation is encountered within an AndroMDA cartridge.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class CartridgeException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;

    /**
     * Constructor for CartridgeException.
     */
    public CartridgeException()
    {
        super();
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param message the exception message
     */
    public CartridgeException(String message)
    {
        super(message);
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param message the exception message
     * @param parent the parent exception
     */
    public CartridgeException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param parent the parent exception
     */
    public CartridgeException(Throwable parent)
    {
        super(parent);
    }
}
