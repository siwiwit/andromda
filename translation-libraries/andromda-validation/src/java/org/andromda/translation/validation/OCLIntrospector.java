package org.andromda.translation.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Dynamically invokes operation and property calls on specified
 * <strong>elements</code>.
 * 
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class OCLIntrospector
{

    private static final Logger logger = Logger
        .getLogger(OCLIntrospector.class);

    /**
     * Invokes the given <code>feature</code> on the <code>element</code>.
     * Its expected that the feature is either an operation or a property.
     */
    public static Object invoke(Object element, String feature)
    {
        Object result = null;
        try
        {
            feature = StringUtils.trimToEmpty(feature);
            if (OCLPatterns.isOperation(feature))
            {
                result = invoke(element, feature, null);
            }
            else
            {
                result = getNestedProperty(element, feature);
            }
        }
        catch (NullPointerException ex)
        {
            // ignore (the result will just be null)
        }
        catch (OCLIntrospectorException throwable)
        {
            // Dont catch our own exceptions.
            // Otherwise get Exception/Cause chain which
            // can hide the original exception.
            throw throwable;
        }
        catch (Throwable throwable)
        {
            final String message = "Error invoking feature '" + feature
                + "' on element '" + element + "'";
            Throwable cause = ExceptionUtils.getRootCause(throwable);
            // If cause is an OCLIntrospector throw that exception
            // rather than creating a new one.
            if (cause instanceof OCLIntrospectorException)
            {
                // Wrap exception again to prevent redundant
                // error messages as the stack unwinds.
                throw (OCLIntrospectorException)cause;
            }
            logger.error(message);
            throw new OCLIntrospectorException(cause);
        }
        return result;
    }

    /**
     * Invokes the given <code>feature</code> on the specified
     * <code>element</code> taking the given <code>arguments</code>. If
     * <code>arguments</code> is null its expected that the feature is an
     * empty operation.
     */
    public static Object invoke(
        Object element,
        String feature,
        Object[] arguments)
    {
        Object result = null;
        try
        {
            // check for parenthesis
            int parenIndex = feature.indexOf('(');
            if (parenIndex != -1)
            {
                feature = feature.substring(0, parenIndex).trim();
            }
            result = invokeMethod(element, feature, arguments);
        }
        catch (NullPointerException ex)
        {
            // ignore (the result will just be null)
        }
        catch (Throwable throwable)
        {
            final String message = "Error invoking feature '" + feature
                + "' on element '" + element + "' with arguments '"
                + StringUtils.join(arguments, ',') + "'";
            throwable = ExceptionUtils.getRootCause(throwable);
            logger.error(message);
            throw new OCLIntrospectorException(throwable);
        }
        return result;
    }

    private static Object getNestedProperty(Object element, String propertyName)
        throws Exception
    {
        Object property = null;

        if (element != null && propertyName != null
            && propertyName.length() > 0)
        {
            int dotIndex = propertyName.indexOf('.');
            if (dotIndex == -1)
            {
                property = getProperty(element, propertyName);
            }
            else
            {
                if (dotIndex >= propertyName.length())
                {
                    throw new Exception("Malformed property call --> '"
                        + propertyName + "'");
                }
                Object nextInstance = getProperty(element, propertyName
                    .substring(0, dotIndex));
                property = getNestedProperty(nextInstance, propertyName
                    .substring(dotIndex + 1));
            }
        }
        return property;
    }

    /**
     * Gets the value of the property with <code>propertyName</code> on the
     * given <code>element</code>.
     * 
     * @param element the element from which to retrieve the property.
     * @param propertyName the name of the property
     * @return the resulting property value
     * @throws InvocationTargetException if an exception occurs during
     *         invocation
     * @throws IllegalAccessException if an illegal access exception occurs
     *         during invocation.
     */
    private static Object getProperty(Object element, String propertyName)
        throws InvocationTargetException,
            IllegalAccessException
    {
        Object property = null;
        if (element != null || propertyName != null
            || propertyName.length() > 0)
        {
            Method method = getMethod("get", element, propertyName);
            if (method == null)
            {
                method = getMethod("is", element, propertyName);
            }
            if (method == null)
            {
                throw new OCLIntrospectorException("No property named '"
                    + propertyName + "', found on element '" + element + "'");
            }
            property = method.invoke(element, (Object[])null);
        }
        return property;
    }

    /**
     * Retrieves the method from the given <code>element</code> and the given
     * <code>propertyName</code> by capitalizing the <code>propertyName</code>
     * 
     * @param prefix the prefix (either 'get' or 'is')
     * @param element the element from which to retrieve the moethod
     * @param propertyName the name of the property
     * @return the retrieved Method.
     */
    private static Method getMethod(
        String prefix,
        Object element,
        String propertyName)
    {
        Method method = null;
        try
        {
            method = element.getClass().getMethod(
                prefix + StringUtils.capitalize(propertyName),
                (Class[])null);
        }
        catch (NoSuchMethodException ex)
        {
            // ignore
        }
        return method;
    }

    private static Object invokeMethod(
        Object element,
        String methodName,
        Object[] arguments) throws Exception
    {
        Object property = null;

        if (element != null && methodName != null && methodName.length() > 0)
        {
            Class[] argumentTypes = getObjectTypes(arguments);

            Method method = element.getClass().getMethod(
                methodName,
                argumentTypes);
            property = method.invoke(element, arguments);
        }

        return property;
    }

    private static Class[] getObjectTypes(Object[] objects)
    {
        Class[] objectTypes = null;

        if (objects != null)
        {
            objectTypes = new Class[objects.length];
            for (int ctr = 0; ctr < objects.length; ctr++)
            {
                Object object = objects[ctr];
                if (object != null)
                {
                    objectTypes[ctr] = object.getClass();
                }
            }
        }
        return objectTypes;
    }
}
