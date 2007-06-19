package org.andromda.cartridges.support.webservice.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
import org.apache.commons.codec.binary.Base64;


/**
 * Interceptor for accessing a specific port of a web service.
 */
public class Axis2PortClientInterceptor
    extends org.springframework.beans.factory.support.AbstractBeanDefinition
    implements MethodInterceptor,
        InitializingBean
{
    protected final Log logger = LogFactory.getLog(Axis2PortClientInterceptor.class);
    private final Object preparationMonitor = new Object();
    private String username;

    /**
     * Set the username to connect to the service.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Return the username to connect to the service.
     */
    public String getUsername()
    {
        return username;
    }

    private String password;

    /**
     * Set the password to connect to the service.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Return the password to connect to the service.
     */
    public String getPassword()
    {
        String password = this.password;
        if (password != null && this.getBase64Password())
        {
            password = new String(Base64.decodeBase64(password.getBytes()));
        }
        return password;
    }

    private boolean base64Password;

    /**
     * Sets a flag indicating whether or not the password given is in Base64.
     *
     * @param base64Password true/false
     */
    public void setBase64Password(boolean base64Password)
    {
        this.base64Password = base64Password;
    }

    /**
     * Gets the flag indicating whether or not the password given is in Base64.
     *
     * return true/false
     */
    public boolean getBase64Password()
    {
        return this.base64Password;
    }

    private long timeout;

    /**
     * Sets the timeout of the client in seconds.
     *
     * @param timeout
     */
    public void setTimeout(final long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Gets the timeout of the client in seconds.
     *
     * @return the timeout.
     */
    public long getTimeout()
    {
        return this.timeout;
    }

    private Class serviceInterface;

    /**
     * Set the interface of the service that this factory should create a proxy for.
     */
    public void setServiceInterface(Class serviceInterface)
    {
        if (serviceInterface != null && !serviceInterface.isInterface())
        {
            throw new IllegalArgumentException("serviceInterface must be an interface");
        }
        this.serviceInterface = serviceInterface;
    }

    /**
     * Return the interface of the service that this factory should create a proxy for.
     */
    public Class getServiceInterface()
    {
        return serviceInterface;
    }

    private String wsdlUrl;

    /**
     * Returns the URL to the WSDL for the service.
     *
     * @return the wsdlUrl
     */
    public String getWsdlUrl()
    {
        return wsdlUrl;
    }

    /**
     * Sets the WSDL URL for the service.
     *
     * @param wsdlUrl the wsdlUrl to set
     */
    public void setWsdlUrl(String wsdlUrl)
    {
        this.wsdlUrl = wsdlUrl;
    }

    public String portAddress;

    /**
     * Returns the port address (if not using a port address in the WSDL).
     *
     * @return the portAddress
     */
    public String getPortAddress()
    {
        return portAddress;
    }

    /**
     * Sets the port address (if to use one other than the one in the WSDL).
     *
     * @param portAddress the portAddress to set
     */
    public void setPortAddress(String portAddress)
    {
        this.portAddress = portAddress;
    }

    /**
     * Prepares the JAX-RPC service and port if the "lazyInit"
     * isn't false.
     */
    public void afterPropertiesSet()
    {
        this.prepare();
    }

    private WebServiceClient client;

    /**
     * Create and initialize the service for the specified WSDL.
     */
    public void prepare()
    {
        synchronized (this.preparationMonitor)
        {
            if (this.client == null)
            {
                this.client =
                    new WebServiceClient(
                        this.getWsdlUrl(),
                        this.getPortAddress(),
                        this.getServiceInterface(),
                        this.getUsername(),
                        this.getPassword());
                this.client.setTimeout(this.getTimeout());
            }
        }
    }

    /**
     * Translates the method invocation into a JAX-RPC service invocation.
     * Uses traditional RMI stub invocation if a JAX-RPC port stub is available;
     * falls back to JAX-RPC dynamic calls else.
     * @see #getPortStub()
     * @see org.springframework.remoting.rmi.RmiClientInterceptorUtils
     * @see #performJaxRpcCall
     */
    public Object invoke(MethodInvocation invocation)
        throws Throwable
    {
        if (AopUtils.isToStringMethod(invocation.getMethod()))
        {
            return "Axis2 proxy for WSDL [" + this.getWsdlUrl() + "] of service [" +
            this.getServiceInterface().getName() + "]";
        }
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "Invoking operation '" + invocation.getMethod().getName() + "' on " +
                this.getServiceInterface().getName());
        }
        try
        {
            try
            {
                return this.client.invokeBlocking(
                    invocation.getMethod().getName(),
                    invocation.getArguments());
            }
            finally
            {
                this.client.cleanup();
            }
        }
        catch (WebServiceClientException exception)
        {
            throw RmiClientInterceptorUtils.convertRmiAccessException(
                invocation.getMethod(),
                exception,
                this.getServiceInterface().getName());
        }
    }
}