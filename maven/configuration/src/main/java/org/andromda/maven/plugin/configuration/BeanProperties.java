package org.andromda.maven.plugin.configuration;

import java.io.File;
import java.util.Properties;
import org.codehaus.plexus.util.introspection.ReflectionValueExtractor;

/**
 * Extends properties and allows the key to be retrieved from the given bean.
 *
 * @author Chad Brandon
 */
public class BeanProperties
    extends Properties
{
    private Object bean;

    /**
     * @param bean
     */
    public BeanProperties(final Object bean)
    {
        this.bean = bean;
    }

    /**
     * @see java.util.Dictionary#get(Object)
     */
    public Object get(Object key)
    {
        Object value = null;
        try
        {
            value = ReflectionValueExtractor.evaluate(
                    String.valueOf(key),
                    bean);
            // - convert file instances to strings
            if (value instanceof File)
            {
                value = ((File)value).getPath();
                this.put(key, value);
            }
        }
        catch (Exception exception)
        {
            // ignore
        }
        return value;
    }
}