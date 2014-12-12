package org.andromda.core.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * A simple class providing the ability to manipulate properties on java bean objects.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public final class Introspector
{
    /**
     * The shared instance.
     */
    private static Introspector instance = null;

    /**
     * Gets the shared instance.
     *
     * @return the shared introspector instance.
     */
    public static Introspector instance()
    {
        if (instance == null)
        {
            instance = new Introspector();
        }
        return instance;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(Introspector.class);

    /**
     * <p> Indicates whether or not the given <code>object</code> contains a
     * valid property with the given <code>name</code> and <code>value</code>.
     * </p>
     * <p>
     * A valid property means the following:
     * <ul>
     * <li>It exists on the object</li>
     * <li>It is not null on the object</li>
     * <li>If its a boolean value, then it evaluates to <code>true</code></li>
     * <li>If value is not null, then the property matches the given </code>.value</code></li>
     * </ul>
     * All other possibilities return <code>false</code>
     * </p>
     *
     * @param object the object to test for the valid property.
     * @param name the name of the property for which to test.
     * @param value the value to evaluate against.
     * @return true/false
     */
    public boolean containsValidProperty(
        final Object object,
        final String name,
        final String value)
    {
        boolean valid;

        try
        {
            final Object propertyValue = this.getProperty(
                    object,
                    name);
            valid = propertyValue != null;

            // if valid is still true, and the propertyValue
            // is not null
            if (valid)
            {
                // if it's a collection then we check to see if the
                // collection is not empty
                if (propertyValue instanceof Collection)
                {
                    valid = !((Collection)propertyValue).isEmpty();
                }
                else
                {
                    final String valueAsString = String.valueOf(propertyValue);
                    if (StringUtils.isNotBlank(value))
                    {
                        valid = valueAsString.equals(value);
                    }
                    else if (propertyValue instanceof Boolean)
                    {
                        valid = Boolean.valueOf(valueAsString);
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            valid = false;
        }
        return valid;
    }

    /**
     * Sets the property having the given <code>name</code> on the <code>object</code>
     * with the given <code>value</code>.
     *
     * @param object the object on which to set the property.
     * @param name the name of the property to populate.
     * @param value the value to give the property.
     */
    public void setProperty(
        final Object object,
        final String name,
        final Object value)
    {
        this.setNestedProperty(
            object,
            name,
            value);
    }

    /**
     * The delimiter used for separating nested properties.
     */
    private static final char NESTED_DELIMITER = '.';

    /**
     * Attempts to set the nested property with the given
     * name of the given object.
     * @param object the object on which to populate the property.
     * @param name the name of the object.
     * @param value the value to populate.
     */
    private void setNestedProperty(
        final Object object,
        String name,
        final Object value)
    {
        if (object != null && StringUtils.isNotBlank(name))
        {
            final int dotIndex = name.indexOf(NESTED_DELIMITER);
            if (dotIndex >= name.length())
            {
                throw new IntrospectorException("Invalid property call --> '" + name + '\'');
            }
            String[] names = name.split("\\" + NESTED_DELIMITER);
            Object objectToPopulate = object;
            for (int ctr = 0; ctr < names.length; ctr++)
            {
                name = names[ctr];
                if (ctr == names.length - 1)
                {
                    break;
                }
                objectToPopulate = this.internalGetProperty(
                        objectToPopulate,
                        name);
            }
            this.internalSetProperty(
                objectToPopulate,
                name,
                value);
        }
    }

    /**
     * Attempts to retrieve the property with the given <code>name</code> on the <code>object</code>.
     *
     * @param object the object to which the property belongs.
     * @param name the name of the property
     * @return the value of the property.
     */
    public final Object getProperty(
        final Object object,
        final String name)
    {
        Object result;

        try
        {
            result = this.getNestedProperty(
                    object,
                    name);
        }
        catch (final NullPointerException ex)
        {
            return "null";
        }
        catch (final StackOverflowError ex)
        {
            return "StackOverflowError";
        }
        catch (final IntrospectorException throwable)
        {
            // Don't catch our own exceptions.
            // Otherwise get Exception/Cause chain which
            // can hide the original exception.
            throw throwable;
        }
        catch (Throwable throwable)
        {
            throwable = ExceptionUtils.getRootCause(throwable);

            // If cause is an IntrospectorException re-throw that exception
            // rather than creating a new one.
            if (throwable instanceof IntrospectorException)
            {
                throw (IntrospectorException)throwable;
            }
            throw new IntrospectorException(throwable);
        }
        return result;
    }

    /**
     * Gets a nested property, that is it gets the properties
     * separated by '.'.
     *
     * @param object the object from which to retrieve the nested property.
     * @param name the name of the property
     * @return the property value or null if one couldn't be retrieved.
     */
    private Object getNestedProperty(
        final Object object,
        final String name)
    {
        Object property = null;
        if (object != null && StringUtils.isNotBlank(name))
        {
            int dotIndex = name.indexOf(NESTED_DELIMITER);
            if (dotIndex == -1)
            {
                property = this.internalGetProperty(
                        object,
                        name);
            }
            else
            {
                if (dotIndex >= name.length())
                {
                    throw new IntrospectorException("Invalid property call --> '" + name + '\'');
                }
                final Object nextInstance = this.internalGetProperty(
                        object,
                        name.substring(
                            0,
                            dotIndex));
                property = getNestedProperty(
                        nextInstance,
                        name.substring(dotIndex + 1));
            }
        }
        return property;
    }

    /**
     * Gets the writable method for the property.
     *
     * @param object the object from which to retrieve the property method.
     * @param name the name of the property.
     * @return the property method or null if one wasn't found.
     */
    private Method getWriteMethod(
        final Object object,
        final String name)
    {
        final PropertyDescriptor descriptor = getPropertyDescriptor(object.getClass(), name);
        return descriptor != null ? descriptor.getWriteMethod() : null;
    }

    /**
     * Indicates if the <code>object</code> has a property that
     * is <em>readable</em> with the given <code>name</code>.
     *
     * @param object the object to check.
     * @param name the property to check for.
     * @return this.getReadMethod(object, name) != null
     */
    public boolean isReadable(
        final Object object,
        final String name)
    {
        return this.getReadMethod(
            object,
            name) != null;
    }

    /**
     * Indicates if the <code>object</code> has a property that
     * is <em>writable</em> with the given <code>name</code>.
     *
     * @param object the object to check.
     * @param name the property to check for.
     * @return this.getWriteMethod(object, name) != null
     */
    public boolean isWritable(
        final Object object,
        final String name)
    {
        return this.getWriteMethod(
            object,
            name) != null;
    }

    /**
     * Gets the readable method for the property.
     *
     * @param object the object from which to retrieve the property method.
     * @param name the name of the property.
     * @return the property method or null if one wasn't found.
     */
    private Method getReadMethod(
        final Object object,
        final String name)
    {
        final PropertyDescriptor descriptor = getPropertyDescriptor(object.getClass(), name);
        return descriptor != null ? descriptor.getReadMethod() : null;
    }

    /**
     * The cache of property descriptors.
     */
    private final Map<Class, Map<String, PropertyDescriptor>> propertyDescriptorsCache = new ConcurrentHashMap<Class, Map<String, PropertyDescriptor>>();

    /**
     * The pattern for property names.
     */
    private Pattern propertyNamePattern = Pattern.compile("\\p{Lower}\\p{Upper}.*");

    /**
     * Retrieves the property descriptor for the given type and name of
     * the property.
     *
     * @param type the Class of which we'll attempt to retrieve the property
     * @param name the name of the property.
     * @return the found property descriptor
     */
    private PropertyDescriptor getPropertyDescriptor(
        final Class type,
        final String name)
    {
        PropertyDescriptor propertyDescriptor = null;
        Map<String, PropertyDescriptor> classPropertyDescriptors = this.propertyDescriptorsCache.get(type);
        if (classPropertyDescriptors == null)
        {
            classPropertyDescriptors = new HashMap<String, PropertyDescriptor>();
        }
        else
        {
            propertyDescriptor = classPropertyDescriptors.get(name);
        }

        if (propertyDescriptor == null)
        {
            try
            {
                final PropertyDescriptor[] descriptors =
                    java.beans.Introspector.getBeanInfo(type).getPropertyDescriptors();
                final int descriptorNumber = descriptors.length;
                for (PropertyDescriptor descriptor : descriptors)
                {
                    // - handle names that start with a lowercased letter and have an uppercase as the second letter
                    final String compareName =
                        propertyNamePattern.matcher(name).matches() ? StringUtils.capitalize(name) : name;
                    if (descriptor.getName().equals(compareName))
                    {
                        propertyDescriptor = descriptor;
                        break;
                    }
                }
                if (propertyDescriptor == null && name.indexOf(NESTED_DELIMITER) != -1)
                {
                    int dotIndex = name.indexOf(NESTED_DELIMITER);
                    if (dotIndex >= name.length())
                    {
                        throw new IntrospectorException("Invalid property call --> '" + name + '\'');
                    }
                    final PropertyDescriptor nextInstance =
                        this.getPropertyDescriptor(
                            type,
                            name.substring(
                                0,
                                dotIndex));
                    propertyDescriptor =
                        this.getPropertyDescriptor(
                            nextInstance.getPropertyType(),
                            name.substring(dotIndex + 1));
                }
            }
            catch (final IntrospectionException exception)
            {
                throw new IntrospectorException(exception);
            }
            classPropertyDescriptors.put(
                name,
                propertyDescriptor);
            this.propertyDescriptorsCache.put(
                type,
                classPropertyDescriptors);
        }
        return propertyDescriptor;
    }

    /**
     * Attempts to get the value of the property with <code>name</code> on the
     * given <code>object</code> (throws an exception if the property
     * is not readable on the object).
     *
     * @param object the object from which to retrieve the property.
     * @param name the name of the property
     * @return the resulting property value
     */
    private Object internalGetProperty(
        final Object object,
        final String name)
    {
    	return internalGetProperty(object, name, new HashMap<Object, String>());
    }

    /**
     * Attempts to get the value of the property with <code>name</code> on the
     * given <code>object</code> (throws an exception if the property
     * is not readable on the object).
     *
     * @param object the object from which to retrieve the property.
     * @param name the name of the property
     * @param evaluatingObjects prevents stack-over-flow by storing the objects that are currently being evaluated.
     * @return the resulting property value
     */
    private Object internalGetProperty(
        final Object object,
        final String name,
        final Map<Object, String> evaluatingObjects)
    {
        Object property = null;

        // - prevent stack overflows by checking to make sure
        //   we aren't entering any circular evaluations
        final Object value = evaluatingObjects.get(object);
        if (value == null || !value.equals(name))
        {
            evaluatingObjects.put(
                object,
                name);
            if (object != null || StringUtils.isNotBlank(name))
            {
                final Method method = this.getReadMethod(
                        object,
                        name);
                if (method == null)
                {
                    throw new IntrospectorException("No readable property named '" + name + "', exists on object '" +
                        object + '\'');
                }
                try
                {
                    property = method.invoke(
                            object,
                            (Object[])null);
                }
                catch (Throwable throwable)
                {
                    if (throwable.getCause() != null)
                    {
                        throwable = throwable.getCause();
                    }
                    // At least output the location where the error happened, not the entire stack trace.
                    StackTraceElement[] trace = throwable.getStackTrace();
                    String location = " AT " + trace[0].getClassName() + '.' + trace[0].getMethodName() + ':' + trace[0].getLineNumber();
                    if (throwable.getMessage()!=null)
                    {
                        location += ' ' + throwable.getMessage();
                    }
                    logger.error("Introspector " + throwable + " invoking " + object + " METHOD " + method + " WITH " + name + location);
                    // Unrecoverable errors may result in infinite loop, in particular StackOverflowError
                    if (throwable instanceof Exception)
                    {
                        throw new IntrospectorException(throwable);
                    }
                }
            }
        }
        return property;
    }

    /**
     * Attempts to sets the value of the property with <code>name</code> on the
     * given <code>object</code> (throws an exception if the property
     * is not writable on the object).
     *
     * @param object the object from which to retrieve the property.
     * @param name the name of the property to set.
     * @param value the value of the property to set.
     */
    private void internalSetProperty(
        final Object object,
        final String name,
        Object value)
    {
        if (object != null || (StringUtils.isNotBlank(name)))
        {
            Class expectedType;
            if (value != null && object != null)
            {
                final PropertyDescriptor descriptor = this.getPropertyDescriptor(
                        object.getClass(),
                        name);
                if (descriptor != null)
                {
                    expectedType = this.getPropertyDescriptor(
                            object.getClass(),
                            name).getPropertyType();
                    value = Converter.convert(
                            value,
                            expectedType);
                }
            }
            final Method method = this.getWriteMethod(
                    object,
                    name);
            if (method == null)
            {
                throw new IntrospectorException("No writeable property named '" + name + "', exists on object '" +
                    object + '\'');
            }
            try
            {
                method.invoke(
                    object,
                    value);
            }
            catch (final Throwable throwable)
            {
                throw new IntrospectorException(throwable);
            }
        }
    }

    /**
     * Shuts this instance down and reclaims
     * any resources used by this instance.
     */
    public void shutdown()
    {
        this.propertyDescriptorsCache.clear();
        Introspector.instance = null;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(" [propertyDescriptorsCache=").append(this.propertyDescriptorsCache)
                .append(", propertyNamePattern=").append(this.propertyNamePattern).append("]");
        return builder.toString();
    }
}
