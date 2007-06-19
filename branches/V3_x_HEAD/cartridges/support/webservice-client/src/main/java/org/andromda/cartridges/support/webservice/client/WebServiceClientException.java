package org.andromda.cartridges.support.webservice.client;

import java.lang.reflect.Method;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.xml.sax.InputSource;


/**
 * An exception thrown during an unexpected error during the {@link WebServiceClient}
 * execution.
 *
 * @author Chad Brandon
 */
public class WebServiceClientException
    extends RuntimeException
{
    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param parent
     */
    public WebServiceClientException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param message
     */
    public WebServiceClientException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param message
     * @param parent
     */
    public WebServiceClientException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}