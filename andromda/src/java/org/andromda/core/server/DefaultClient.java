package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * The default AndroMDA {@link Client} instance.
 *
 * @author Chad Brandon
 */
public class DefaultClient
    implements Client
{
    /**
     * @see org.andromda.core.server.Client#run(org.andromda.core.configuration.Configuration)
     */
    public void run(final Configuration configuration)
        throws ConnectException
    {
        if (configuration != null && configuration.getServer() != null)
        {
            final org.andromda.core.configuration.Server serverConfiguration = configuration.getServer();
            if (serverConfiguration != null)
            {
                try
                {
                    Socket server = null;
                    ObjectOutputStream out = null;
                    ObjectInputStream objectInput = null;
                    final String host = serverConfiguration.getHost();
                    try
                    {
                        server = new Socket(
                                host,
                                serverConfiguration.getPort());
                        out = new ObjectOutputStream(server.getOutputStream());
                        objectInput = new ObjectInputStream(new DataInputStream(server.getInputStream()));
                    }
                    catch (final UnknownHostException exception)
                    {
                        throw new RuntimeException("Can't connect to host '" + host + "'");
                    }
                    out.writeObject(configuration);
                    final Object input = objectInput.readObject();
                    while (input == null);
                    if (input instanceof Throwable)
                    {
                        throw new ClientException((Throwable)input);
                    }
                    objectInput.close();
                    out.flush();
                    out.close();
                    server.close();
                }
                catch (final ConnectException exception)
                {
                    // just re-throw since we need to detect when
                    // we can't communicate with the server
                    throw exception;
                }
                catch (Throwable throwable)
                {
                    throw new ClientException(throwable);
                }
            }
        }
    }
}