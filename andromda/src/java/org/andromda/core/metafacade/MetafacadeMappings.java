package org.andromda.core.metafacade;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Merger;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * The Metafacade mapping class. Used to map <code>metafacade</code> objects
 * to <code>metamodel</code> objects.
 * 
 * @see MetafacadeMapping
 * @see org.andromda.core.common.XmlObjectFactory
 * @author Chad Brandon
 */
public class MetafacadeMappings
{
    private Logger logger = Logger.getLogger(MetafacadeMappings.class);

    /**
     * Contains the mappings XML used for mapping Metafacades.
     */
    private static String METAFACADES_URI = "META-INF/andromda-metafacades.xml";

    /**
     * Holds the references to the child MetafacadeMapping instances.
     */
    private Map mappings = new HashMap();

    /**
     * Holds the namespace MetafacadeMappings. This are child MetafacadeMappings
     * keyed by namespace name.
     */
    private Map namespaceMetafacadeMappings = new HashMap();

    /**
     * Holds the resource path from which this MetafacadeMappings object was
     * loaded.
     */
    private URL resource;

    /**
     * Contains references to properties populated in the Namespaces.
     */
    private Map propertyReferences = new HashMap();

    /**
     * Property references keyed by namespace, these are populated on the first
     * call to getPropertyReferences below.
     */
    private Map namespacePropertyRefs = null;

    /**
     * The shared static instance.
     */
    private static MetafacadeMappings instance = null;

    /**
     * The default meta facade to use when there isn't a mapping found.
     */
    private Class defaultMetafacadeClass = null;

    /**
     * Gets the shared instance.
     * 
     * @return MetafacadeMappings
     */
    public static MetafacadeMappings instance()
    {
        if (instance == null)
        {
            instance = new MetafacadeMappings();
        }
        return instance;
    }

    /**
     * Returns a new configured instance of this MetafacadeMappings configured
     * from the mappings configuration URI.
     * 
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return MetafacadeMappings the configured MetafacadeMappings instance.
     */
    protected static MetafacadeMappings getInstance(URL mappingsUri)
    {
        final String methodName = "MetafacadeMappings.getInstance";
        ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
        XmlObjectFactory factory = XmlObjectFactory
            .getInstance(MetafacadeMappings.class);
        MetafacadeMappings mappings = (MetafacadeMappings)factory
            .getObject(mappingsUri);
        // after we've gotten the initial instance we can merge the file
        // since we know the namespace
        String mappingsContents = ResourceUtils.getContents(mappingsUri);
        mappingsContents = Merger.instance().getMergedString(
            mappingsContents,
            mappings.getNamespace());
        mappings = (MetafacadeMappings)factory.getObject(mappingsContents);
        mappings.resource = mappingsUri;
        return mappings;
    }

    /**
     * The namespace to which this MetafacadeMappings instance applies.
     */
    private String namespace = null;

    /**
     * @return Returns the namespace.
     */
    public String getNamespace()
    {
        final String methodName = "MetafacadeMappings.getNamespace";
        ExceptionUtils.checkEmpty(methodName, "namespace", this.namespace);
        return this.namespace;
    }

    /**
     * @param namespace The namepace to set.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * Whether or not these mappings are shared across all namespaces.
     */
    private boolean shared = false;

    /**
     * Gets whether or not this set of <code>metafacade</code> mappings is
     * shared across all namespaces. By default mappings are <strong>NOT
     * </strong> shared.
     * 
     * @return Returns the shared.
     */
    public boolean isShared()
    {
        return shared;
    }

    /**
     * Sets whether or not this set of <code>metafacade</code> mappings is
     * shared across all namespaces.
     * 
     * @param shared The shared to set.
     */
    public void setShared(boolean shared)
    {
        this.shared = shared;
    }

    /**
     * Adds a MetafacadeMapping instance to the set of current mappings.
     * 
     * @param mapping the MetafacadeMapping instance.
     */
    public void addMapping(MetafacadeMapping mapping)
    {
        final String methodName = "MetafacadeMappings.addMapping";
        ExceptionUtils.checkNull(methodName, "mapping", mapping);

        String mappingClassName = mapping.getMappingClassName();
        ExceptionUtils.checkEmpty(
            methodName,
            "mapping.mappingClassName",
            mappingClassName);
        ExceptionUtils.checkNull(methodName, "mapping.metafacadeClass", mapping
            .getMetafacadeClass());

        mapping.setMetafacadeMappings(this);

        String key = mapping.getKey();
        if (mapping.hasStereotypes())
        {
            // If the mapping has a stereotype we just add it directly to the
            // mappings Map of this MetafacadeMappings instance.
            this.mappings.put(key, mapping);
        }
        else
        {
            // Otherwise we place each mapping in another Map
            // keyed by context (if it has a context) or keyed by null, (if
            // it has no context). We then place this new Map in the mappings
            // Map of this MetafacadeMappings instance.
            Object object = this.mappings.get(key);
            Map mappingMap = null;
            if (object != null)
            {
                mappingMap = (Map)object;
            }
            else
            {
                mappingMap = new HashMap();
            }
            mappingMap.put(mapping.getContext(), mapping);
            this.mappings.put(key, mappingMap);
        }
    }

    /**
     * Copies all data from <code>mappings<code> to this
     * instance.
     * 
     * @param mappings the mappings to add
     */
    private void copyMappings(MetafacadeMappings mappings)
    {
        final String methodName = "MetafacadeMappings.copyMappings";
        ExceptionUtils.checkNull(methodName, "mappings", mappings);
        // the namespace is always the default namespace
        this.setNamespace(Namespaces.DEFAULT);
        Iterator keyIt = mappings.mappings.keySet().iterator();
        while (keyIt.hasNext())
        {
            String key = (String)keyIt.next();

            Object object = mappings.mappings.get(key);
            if (Map.class.isAssignableFrom(object.getClass()))
            {
                this.mappings.put(key, object);
            }
            else
            {
                this.addMapping((MetafacadeMapping)object);
            }
        }
        Map propertyRefs = mappings.propertyReferences;
        if (propertyRefs != null && !propertyRefs.isEmpty())
        {
            this.propertyReferences.putAll(propertyRefs);
        }
        this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
    }

    /**
     * Retrieves the MetafacadeMapping belonging to the unique <code>key</code>
     * created from the <code>mappingClass</code> and given
     * <code>stereotypes</code>. This allows us to retrieve mappings based on
     * single stereotypes or mappings having multiple stereotypes.
     * 
     * @param mappingClass the class name of the meta model object.
     * @param stereotypes the stereotypes to check.
     * @param context the context within the namespace for which the mapping
     *        applies
     * @return MetafacadeMapping
     */
    protected MetafacadeMapping getMapping(
        String mappingClass,
        Collection stereotypes,
        String context)
    {
        MetafacadeMapping mapping = null;
        String key = null;
        // loop through stereotypes and if we find a mapping
        // that matches when constructing a key, break
        // out of it with the mapping
        if (stereotypes != null && !stereotypes.isEmpty())
        {
            // stores the stereotypes that are being used
            // to check for matching mappings
            List verifiedStereotypes = new ArrayList();
            Iterator stereotypeIt = stereotypes.iterator();
            while (stereotypeIt.hasNext())
            {
                String stereotype = StringUtils
                    .trimToEmpty((String)stereotypeIt.next());
                // first check for a mapping that contains the single stereotype
                // (this gives us the 'OR' feature of matching on stereotypes)
                key = MetafacadeMappingsUtils.constructKey(
                    mappingClass,
                    stereotype);
                mapping = (MetafacadeMapping)this.mappings.get(key);
                if (mapping != null)
                {
                    break;
                }
                // if we didn't find a mapping containing a single stereotype
                // try a mapping having a combination of multiple stereotypes
                // (this gives us the 'AND' feature of matching on stereotypes)
                verifiedStereotypes.add(stereotype);
                key = MetafacadeMappingsUtils.constructKey(
                    mappingClass,
                    verifiedStereotypes);
                mapping = (MetafacadeMapping)this.mappings.get(key);
                if (mapping != null)
                {
                    break;
                }
            }
        }

        // try getting the mapping with the context since there
        // wasn't any mapping with a matching stereotype
        if (mapping == null && StringUtils.isNotEmpty(context))
        {
            // try constructing key that has the context
            key = mappingClass;
            Object object = this.mappings.get(key);
            if (object != null && Map.class.isAssignableFrom(object.getClass()))
            {
                Map mappingMap = (Map)object;
                mapping = this.getInheritedContextMapping(mappingMap, context);
            }
            else
            {
                mapping = (MetafacadeMapping)object;
            }
        }
        if (mapping == null)
        {
            if (logger.isDebugEnabled())
                logger.debug("could not find mapping for '" + key
                    + "' find default --> '" + mappingClass + "'");
            Map mappingMap = (Map)this.mappings.get(mappingClass);
            if (mappingMap != null)
            {
                // retrieve default mapping
                mapping = (MetafacadeMapping)mappingMap.get(null);
            }
        }
        return mapping;
    }

    /**
     * <p>
     * Allows us to get the correct inherited context mapping from the given
     * <code>context</code> contained in the given <code>mappings</code>.
     * </p>
     * <p>
     * For example: a Spring cartridge may have a <code>SpringEntity</code>
     * which extends the basic metafacades' <code>EntityFacade</code>. Within
     * the basic metafacades descriptor (i.e.
     * <code>andromda-metafacades.xml</code>), EntityFacade could be defined
     * as the context for an <code>EntityFacadeAttribute</code> mapping. This
     * method allows us to discover the fact that the <code>SpringEntity</code>
     * context inherits from the <code>EntityFacade</code> context, thereby
     * inheriting any properties of the mapping having the
     * <code>EntityFacade</code> as its context.
     * </p>
     * 
     * @param mappings the Map of MetafacadeMapping instances keyed by context.
     * @param context the <code>context</code> to begin with.
     * @return The MetafacadeMapping found having the correct
     *         <code>context</code>.
     */
    private MetafacadeMapping getInheritedContextMapping(
        Map mappings,
        String context)
    {
        MetafacadeMapping mapping = null;
        // save the property references from the orginal mapping so that
        // they aren't overridden by super contexts
        Class contextClass = ClassUtils.loadClass(context);
        List interfaces = new ArrayList(ClassUtils
            .getAllInterfaces(contextClass));
        interfaces.add(0, contextClass);
        if (interfaces != null && !interfaces.isEmpty())
        {
            Class[] interfaceArray = (Class[])interfaces.toArray(new Class[0]);
            CollectionUtils.reverseArray(interfaceArray);
            for (int ctr = 0; ctr < interfaceArray.length; ctr++)
            {
                contextClass = interfaceArray[ctr];
                MetafacadeMapping contextMapping = (MetafacadeMapping)mappings
                    .get(contextClass.getName());
                if (contextMapping != null)
                {
                    // Set the mapping to the latest contextMapping
                    mapping = contextMapping;
                    mapping.addPropertyReferences(contextMapping
                        .getPropertyReferences());
                }
            }
        }
        return mapping;
    }

    /**
     * Gets the resource that configured this instance.
     * 
     * @return URL to the resource.
     */
    protected URL getResource()
    {
        return this.resource;
    }

    /**
     * Adds a language mapping reference. This are used to populate metafacade
     * impl classes with mapping files (such as those that map from model types
     * to Java, JDBC, SQL types, etc). If its added here as opposed to each
     * child MetafacadeMapping, then the reference will apply to all mappings.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addPropertyReference(String reference, String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }

    /**
     * Returns all property references for this MetafacadeMappings by
     * <code>namespace</code> (these include all default mapping references as
     * well).
     * 
     * @param namespace the namespace to search
     */
    public Map getPropertyReferences(String namespace)
    {
        Map propertyReferences = null;
        if (this.namespacePropertyRefs == null)
        {
            this.namespacePropertyRefs = new HashMap();
        }
        else
        {
            propertyReferences = (Map)namespacePropertyRefs.get(namespace);
        }

        if (propertyReferences == null)
        {
            // first load the property references from
            // the mappings
            propertyReferences = new HashMap();
            propertyReferences.putAll(this.propertyReferences);
            MetafacadeMappings metafacades = this
                .getNamespaceMappings(namespace);
            if (metafacades != null)
            {
                propertyReferences.putAll(metafacades.propertyReferences);
            }
            this.namespacePropertyRefs.put(namespace, propertyReferences);
        }

        return propertyReferences;
    }

    /**
     * Gets all the child MetafacadeMapping instances for this
     * MetafacadeMappings by <code>namespace</code> (these include all child
     * mappings from the <code>default</code> mapping reference as well).
     * 
     * @param namespace the namespace of the mappings to retrieve.
     * @return Map the child mappings (MetafacadeMapping instances)
     */
    protected Map getMappings(String namespace)
    {
        MetafacadeMappings metafacades = this.getNamespaceMappings(namespace);
        if (metafacades != null)
        {
            this.mappings.putAll(metafacades.mappings);
        }
        return this.mappings;
    }

    /**
     * <p>
     * Attempts to get the MetafacadeMapping identified by the given
     * <code>mappingClass</code> and <code>stereotypes<code>, 
     * from the mappings for the given <code>namespace</code> within the 
     * specified <code>context</code>. If it can <strong>not</strong> 
     * be found, it will search the default mappings and return that instead.
     * </p>
     * 
     * @param mappingClass the class name of the meta object for the mapping
     *        we are trying to find.
     * @param stereotypes collection of sterotype names.  We'll check to see if 
     *        the mapping for the given <code>mappingClass</code> is defined for it.
     * @param namespace the namespace (i.e. a cartridge, name, etc.)
     * @param context the within the namespace
     */
    public MetafacadeMapping getMetafacadeMapping(
        String mappingClass,
        Collection stereotypes,
        String namespace,
        String context)
    {
        final String methodName = "MetafacadeMappings.getMetafacadeMapping";
        if (logger.isDebugEnabled())
            logger.debug("performing '" + methodName + "' with mappingClass '"
                + mappingClass + "', stereotypes '" + stereotypes
                + "', namespace '" + namespace + "' and context '" + context
                + "'");

        MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        MetafacadeMapping mapping = null;

        // first try the namespace mappings
        if (mappings != null)
        {
            mapping = mappings.getMapping(mappingClass, stereotypes, context);
        }

        // if we've found a namespace mapping, try to get any shared mappings
        // that this namespace mapping may extend and copy over any property
        // references from the shared mapping to the namespace mapping.
        if (mapping != null)
        {
            Map propertyReferences = mapping.getPropertyReferences();
            MetafacadeMapping defaultMapping = this.getMapping(
                mappingClass,
                stereotypes,
                context);
            if (defaultMapping != null)
            {
                Map defaultPropertyReferences = defaultMapping
                    .getPropertyReferences();
                MetafacadeImpls metafacadeClasses = MetafacadeImpls.instance();
                Class metafacadeInterface = metafacadeClasses
                    .getMetafacadeClass(mapping.getMetafacadeClass().getName());
                Class defaultMetafacadeInterface = metafacadeClasses
                    .getMetafacadeClass(defaultMapping.getMetafacadeClass()
                        .getName());
                if (defaultMetafacadeInterface
                    .isAssignableFrom(metafacadeInterface))
                {
                    mapping.addPropertyReferences(defaultPropertyReferences);
                    // add the namespace property references back so
                    // that the default ones don't override the
                    // namespace specific ones.
                    mapping.addPropertyReferences(propertyReferences);
                }
            }
        }

        // if the namespace mappings weren't found, try the default
        if (mapping == null)
        {
            if (logger.isDebugEnabled())
                logger.debug("namespace mapping not found --> finding default");
            mapping = this.getMapping(mappingClass, stereotypes, context);
        }

        if (logger.isDebugEnabled())
            logger.debug("found mapping --> '" + mapping + "'");
        return mapping;
    }

    /**
     * Gets the MetafacadeMappings instance belonging to the
     * <code>namespace</code>.
     * 
     * @param namespace the namespace name to check.
     * @return the found MetafacadeMappings.
     */
    private MetafacadeMappings getNamespaceMappings(String namespace)
    {
        return (MetafacadeMappings)this.namespaceMetafacadeMappings
            .get(namespace);
    }

    /**
     * Adds another MetafacadeMappings instance to the namespace metafacade
     * mappings of this instance.
     * 
     * @param namespace the namespace name to which the <code>mappings</code>
     *        will belong.
     * @param mappings the MetafacadeMappings instance to add.
     */
    private void addNamespaceMappings(
        String namespace,
        MetafacadeMappings mappings)
    {
        this.namespaceMetafacadeMappings.put(namespace, mappings);
    }

    /**
     * Discover all metafacade mapping files on the class path. You need to call
     * this anytime you want to find another metafacade library that may have
     * been made available.
     */
    public void discoverMetafacades()
    {
        final String methodName = "MetafacadeMappings.discoverMetafacadeMappings";
        URL uris[] = ResourceFinder.findResources(METAFACADES_URI);
        if (uris == null || uris.length == 0)
        {
            logger
                .error("ERROR!! No metafacades found, please check your classpath");
        }
        else
        {
            AndroMDALogger.info("-- discovering metafacades --");
            try
            {
                // will store all namespaces (other than default)
                Collection namespaces = new ArrayList();
                for (int ctr = 0; ctr < uris.length; ctr++)
                {
                    MetafacadeMappings mappings = MetafacadeMappings
                        .getInstance(uris[ctr]);
                    String namespace = mappings.getNamespace();
                    if (StringUtils.isEmpty(namespace))
                    {
                        throw new MetafacadeMappingsException(
                            methodName
                                + " no 'namespace' has been set for metafacades --> '"
                                + mappings.getResource() + "'");
                    }

                    // 'shared' mappings are copied
                    // to this shared mappings instance.
                    if (mappings.isShared())
                    {
                        // copy over any 'shared' mappings
                        this.copyMappings(mappings);
                    }
                    else
                    {
                        // add all others as namespace mappings
                        this.addNamespaceMappings(
                            mappings.getNamespace(),
                            mappings);
                        namespaces.add(mappings.getNamespace());
                    }
                    // construct the found informational based
                    // on whether or not the mappings are shared.
                    StringBuffer foundMessage = new StringBuffer("found");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" shared");
                    }
                    foundMessage.append(" metafacades --> '"
                        + mappings.getNamespace() + "'");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" - adding to '"
                            + Namespaces.DEFAULT + "' namespace");
                    }
                    AndroMDALogger.info(foundMessage);
                }
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new MetafacadeMappingsException(errMsg, th);
            }
            if (StringUtils.isEmpty(this.namespace))
            {
                String errMsg = "No shared metafacades "
                    + "found, please check your classpath, at least "
                    + "one set of metafacades must be marked as 'shared'";
                throw new MetafacadeMappingsException(errMsg);
            }
        }
    }

    /**
     * Gets the defaultMetafacadeClass, first looks for it in the namespace
     * mapping, if it can't find it it then takes the default mappings, setting.
     * 
     * @return Returns the defaultMetafacadeClass.
     */
    public Class getDefaultMetafacadeClass(String namespace)
    {
        Class defaultMetafacadeClass = null;
        MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        if (mappings != null)
        {
            defaultMetafacadeClass = mappings.defaultMetafacadeClass;
        }
        if (defaultMetafacadeClass == null)
        {
            defaultMetafacadeClass = this.defaultMetafacadeClass;
        }
        return defaultMetafacadeClass;
    }

    /**
     * @param defaultMetafacadeClass The defaultMetafacadeClass to set.
     */
    public void setDefaultMetafacadeClass(String defaultMetafacadeClass)
    {
        try
        {
            this.defaultMetafacadeClass = ClassUtils.loadClass(StringUtils
                .trimToEmpty(defaultMetafacadeClass));
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing MetafacadeMappings.setDefaultMetafacadeClass";
            throw new MetafacadeMappingsException(errMsg, th);
        }
    }

    /**
     * Performs shutdown procedures for the factory. This should be called
     * <strong>ONLY</code> when {@link MetafacadeFactory#shutdown()}is called.
     */
    void shutdown()
    {
        this.mappings.clear();
        this.namespaceMetafacadeMappings.clear();
        this.propertyReferences.clear();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}