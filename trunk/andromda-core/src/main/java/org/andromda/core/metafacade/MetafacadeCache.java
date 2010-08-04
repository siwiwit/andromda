package org.andromda.core.metafacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A global cache for metafacades. Used by the {@link MetafacadeFactory}when constructing or retrieving metafacade
 * instances. If the cache constains the metafacade it should not be constructed again.
 *
 * @author Chad Brandon
 */
public final class MetafacadeCache
    implements Serializable
{
    /**
     * Constructs a new instance of this class.
     *
     * @return the new instance
     */
    public static MetafacadeCache newInstance()
    {
        return new MetafacadeCache();
    }

    private MetafacadeCache()
    {
        // don't allow instantiation
    }

    /**
     * The namespace to which the cache currently applies
     */
    private String namespace;

    /**
     * Sets the namespace to which the cache currently applies.
     *
     * @param namespace the current namespace.
     */
    public final void setNamespace(final String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * The cache for already created metafacades.
     */
    private final Map<Object, Map<Class, Map<String, MetafacadeBase>>> metafacadeCache = new HashMap<Object, Map<Class, Map<String, MetafacadeBase>>>();

    /**
     * <p/>
     * Returns the metafacade from the metafacade cache. The Metafacades are cached first by according to its
     * <code>mappingObject</code>, next the <code>metafacadeClass</code>, and finally by the current namespace. </p>
     * <p/>
     * Metafacades must be cached in order to keep track of the state of its validation. If we keep creating a new one
     * each time, we can never tell whether or not a metafacade has been previously validated. Not to mention tremendous
     * performance gains. </p>
     *
     * @param mappingObject   the object to which the mapping applies
     * @param metafacadeClass the class of the metafacade.
     * @return MetafacadeBase stored in the cache.
     */
    public final MetafacadeBase get(
        final Object mappingObject,
        final Class metafacadeClass)
    {
        MetafacadeBase metafacade = null;
        final Map<Class, Map<String, MetafacadeBase>> namespaceMetafacadeCache = this.metafacadeCache.get(mappingObject);
        if (namespaceMetafacadeCache != null)
        {
            final Map<String, MetafacadeBase> metafacadeCache = namespaceMetafacadeCache.get(metafacadeClass);
            if (metafacadeCache != null)
            {
                metafacade = metafacadeCache.get(this.namespace);
            }
        }
        return metafacade;
    }

    /**
     * Adds the <code>metafacade</code> to the cache according to first <code>mappingObject</code>, second the
     * <code>metafacade</code>, and finally by the current <code>namespace</code>.
     *
     * @param mappingObject the mappingObject for which to cache the metafacade.
     * @param metafacade the metafacade to cache.
     */
    public final void add(
        final Object mappingObject,
        final MetafacadeBase metafacade)
    {
        Map<Class, Map<String, MetafacadeBase>> namespaceMetafacadeCache = this.metafacadeCache.get(mappingObject);
        if (namespaceMetafacadeCache == null)
        {
            namespaceMetafacadeCache = new HashMap<Class, Map<String, MetafacadeBase>>();
            this.metafacadeCache.put(mappingObject, namespaceMetafacadeCache);
        }
        Map<String, MetafacadeBase> metafacadeCache = namespaceMetafacadeCache.get(metafacade.getClass());
        if (metafacadeCache == null)
        {
            metafacadeCache = new HashMap<String, MetafacadeBase>();
            namespaceMetafacadeCache.put(
                metafacade.getClass(),
                metafacadeCache);
        }
        metafacadeCache.put(this.namespace, metafacade);
    }

    /**
     * Clears the cache of any metafacades
     */
    public final void clear()
    {
        this.metafacadeCache.clear();
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return this.metafacadeCache.toString();
    }
}