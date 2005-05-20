package org.andromda.core.metafacade;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Merger;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Namespaces;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The Metafacade mapping class. Used to map <code>metafacade</code> objects to <code>metamodel</code> objects.
 *
 * @author Chad Brandon
 * @see MetafacadeMapping
 * @see org.andromda.core.common.XmlObjectFactory
 */
public class MetafacadeMappings
{
    /**
     * Contains the mappings XML used for mapping Metafacades.
     */
    private static final String METAFACADES_URI = "META-INF/andromda-metafacades.xml";

    /**
     * Holds the references to the child MetafacadeMapping instances.
     */
    private final Collection mappings = new ArrayList();

    /**
     * Holds the namespace MetafacadeMappings. This are child MetafacadeMappings keyed by namespace name.
     */
    private final Map namespaceMetafacadeMappings = new HashMap();

    /**
     * Holds the resource path from which this MetafacadeMappings object was loaded.
     */
    private URL resource;

    /**
     * Contains references to properties populated in the Namespaces.
     */
    private final Map propertyReferences = new HashMap();

    /**
     * Property references keyed by namespace, these are populated on the first call to getPropertyReferences below.
     */
    private final Map namespacePropertyReferences = new HashMap();

    /**
     * The shared static instance.
     */
    private static final MetafacadeMappings instance = new MetafacadeMappings();

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
        return instance;
    }

    /**
     * Returns a new configured instance of this MetafacadeMappings configured from the mappings configuration URI.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return MetafacadeMappings the configured MetafacadeMappings instance.
     */
    protected static final MetafacadeMappings getInstance(final URL mappingsUri)
    {
        final String methodName = "MetafacadeMappings.getInstance";
        ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
        final XmlObjectFactory factory = XmlObjectFactory.getInstance(MetafacadeMappings.class);
        MetafacadeMappings mappings = (MetafacadeMappings)factory.getObject(mappingsUri);

        // after we've gotten the initial instance we can merge the file
        // since we know the namespace
        String mappingsContents = ResourceUtils.getContents(mappingsUri);
        mappingsContents = Merger.instance().getMergedString(
                mappingsContents,
                mappings.getNamespace());
        mappings = (MetafacadeMappings)factory.getObject(mappingsContents);
        mappings.resource = mappingsUri;

        // indicate this mappings instance has been fully initialized
        mappings.initialized = true;
        return mappings;
    }

    private boolean initialized = false;

    /**
     * Indicates if this MetafacadeMappings instance has been fully initialized.
     *
     * @return true/false
     */
    boolean isInitialized()
    {
        return initialized;
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
    public void setNamespace(final String namespace)
    {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * Whether or not these mappings are shared across all namespaces.
     */
    private boolean shared = false;

    /**
     * Gets whether or not this set of <code>metafacade</code> mappings is shared across all namespaces. By default
     * mappings are <strong>NOT </strong> shared.
     *
     * @return Returns the shared.
     */
    public boolean isShared()
    {
        return shared;
    }

    /**
     * Sets whether or not this set of <code>metafacade</code> mappings is shared across all namespaces.
     *
     * @param shared The shared to set.
     */
    public void setShared(final boolean shared)
    {
        this.shared = shared;
    }

    /**
     * Adds a MetafacadeMapping instance to the set of current mappings.
     *
     * @param mapping the MetafacadeMapping instance.
     */
    public void addMapping(final MetafacadeMapping mapping)
    {
        final String methodName = "MetafacadeMappings.addMapping";
        ExceptionUtils.checkNull(methodName, "mapping", mapping);

        final String mappingClassName = mapping.getMappingClassName();
        ExceptionUtils.checkEmpty(methodName, "mapping.mappingClassName", mappingClassName);
        ExceptionUtils.checkNull(
            methodName,
            "mapping.metafacadeClass",
            mapping.getMetafacadeClass());
        mapping.setMetafacadeMappings(this);

        // find any mappings that match, if they do we add the properties
        // from that mapping to the existing matched mapping (so we only
        // have one mapping containing properties that can be 'OR'ed together.
        final MetafacadeMapping foundMapping =
            (MetafacadeMapping)CollectionUtils.find(
                this.mappings,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return mapping.match((MetafacadeMapping)object);
                    }
                });
        if (foundMapping != null)
        {
            foundMapping.addMappingPropertyGroup(mapping.getMappingProperties());
        }
        else
        {
            this.mappings.add(mapping);
            mappingsByMetafacadeClass.put(
                this.getMetafacadeInterface(mapping.getMetafacadeClass()),
                mapping);
        }
    }

    /**
     * Gets the class of the metafacade interface that belongs to the given <code>metafacadeClass</code>.
     *
     * @return the metafacade interface Class.
     */
    public Class getMetafacadeInterface(final Class metafacadeClass)
    {
        Class metafacadeInterface = null;
        if (metafacadeClass != null)
        {
            metafacadeInterface = metafacadeClass;
            List interfaces = ClassUtils.getAllInterfaces(metafacadeClass);
            if (interfaces != null && !interfaces.isEmpty())
            {
                metafacadeInterface = (Class)interfaces.iterator().next();
            }
        }
        return metafacadeInterface;
    }

    /**
     * Stores mappings by the metafacade class so that we can retrieve the inherited metafacade classes.
     */
    private final Map mappingsByMetafacadeClass = new HashMap();

    /**
     * Copies all data from <code>mappings<code> to this instance.
     *
     * @param mappings the mappings to add
     */
    private final void copyMappings(final MetafacadeMappings mappings)
    {
        final String methodName = "MetafacadeMappings.copyMappings";
        ExceptionUtils.checkNull(methodName, "mappings", mappings);

        // the namespace is always the default namespace
        this.setNamespace(Namespaces.DEFAULT);
        Iterator mappingIterator = mappings.mappings.iterator();
        while (mappingIterator.hasNext())
        {
            this.addMapping((MetafacadeMapping)mappingIterator.next());
        }
        Map propertyRefs = mappings.propertyReferences;
        if (propertyRefs != null && !propertyRefs.isEmpty())
        {
            this.propertyReferences.putAll(propertyRefs);
        }
        this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
    }

    /**
     * <p/> Retrieves the MetafacadeMapping belonging to the unique
     * <code>key</code> created from the <code>mappingObject</code>'s
     * class, <code>context</code> and given <code>stereotypes</code>. It's
     * <strong>IMPORTANT </strong> to note that contexts have a higher priority
     * than stereotypes. This allows us to retrieve mappings based on the
     * following combinations:
     * <ul>
     * <li>A single stereotype no context</li>
     * <li>A single stereotype with a context</li>
     * <li>metafacade properties no context</li>
     * <li>metafacade properties with a context</code>
     * <li>multiple stereotypes no context</li>
     * <li>multiple stereotypes with a context</li>
     * </ul>
     * </p>
     * <p/> NOTE: mapping properties are inherited from super metafacades.
     * </p>
     *
     * @param mappingObject an instance of the class to which the mapping
     *        applies.
     * @param stereotypes the stereotypes to check.
     * @param rootContext the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hiearchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    protected MetafacadeMapping getMapping(
        final Object mappingObject,
        final String context,
        final Collection stereotypes)
    {
        MetafacadeMapping mapping = this.getMapping(null, mappingObject, context, stereotypes);
        if (mapping == null)
        {
            final Collection hierarchy = this.getMappingObjectHierarchy(mappingObject);
            if (hierarchy != null && !hierarchy.isEmpty())
            {
                for (Iterator hierarchyIterator = hierarchy.iterator(); hierarchyIterator.hasNext() && mapping == null;)
                {
                    mapping = this.getMapping((String)hierarchyIterator.next(), mappingObject, context, stereotypes);
                }
            }
        }

        // load the inherited property references
        this.loadInheritedPropertyReferences(mapping);
        return mapping;
    }

    /**
     * The cache containing the hierachies for each mapping object so that
     * we don't need to retrieve more than once.
     */
    private final Map mappingObjectHierachyCache = new HashMap();

    /**
     * Retrieves the hiearchy of class names. of the given <code>mappingObject</code>.
     *
     * @param mappingObject the object from which to retrieve the hierarchy.
     * @return a list containing all inherited class names.
     */
    protected List getMappingObjectHierarchy(final Object mappingObject)
    {
        List hierachy = (List)this.mappingObjectHierachyCache.get(mappingObject);
        if (hierachy == null)
        {
            // we construct the mapping object name from the interface 
            // (using the implementation name pattern).
            final String pattern = this.getPropertyValue(MetafacadeProperties.METACLASS_IMPLEMENTATION_NAME_PATTERN);
            if (StringUtils.isNotBlank(pattern))
            {
                hierachy = new ArrayList();
                hierachy.addAll(ClassUtils.getAllInterfaces(mappingObject.getClass()));
                if (hierachy != null)
                {
                    CollectionUtils.transform(
                        hierachy,
                        new Transformer()
                        {
                            public Object transform(Object object)
                            {
                                String name = ((Class)object).getName();
                                if (pattern != null)
                                {
                                    name = pattern.replaceAll("\\{0\\}", name);
                                }
                                return name;
                            }
                        });
                }
                this.mappingObjectHierachyCache.put(mappingObject, hierachy);
            }
        }
        return hierachy;
    }

    /**
     * <p/> Stores the mappings which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the mapping
     * is being processed by the {@link #getMapping(Object, String, Collection)}
     * operation. We store these "in process" mappings in order to keep track of
     * the mappings currently being evaluated so we avoid stack over flow errors
     * {@link #getMapping(Object, String, Collection)}when finding mappings
     * that are mapped to super metafacade properties.
     * </p>
     * <p/> Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMappings = new ArrayList();

    /**
     * <p/> Stores the metafacades which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the
     * metafacade being processed by the {@link #getMapping(Object, String,
     * Collection)}operation. We store these "in process" metafacades in order
     * to keep track of the metafacades currently being evaluated so we avoid
     * stack over flow errors {@link #getMapping(Object, String, Collection)}when
     * finding metafacades that are mapped to super metafacade properties.
     * </p>
     * <p/> Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMetafacades = new ArrayList();

    /**
     * <p/> Retrieves the MetafacadeMapping belonging to the unique
     * <code>key</code> created from the <code>mappingObject</code>'s
     * class, <code>context</code> and given <code>stereotypes</code>. It's
     * <strong>IMPORTANT </strong> to note that contexts have a higher priority
     * than stereotypes. This allows us to retrieve mappings based on the
     * following combinations:
     * <ul>
     * <li>A single stereotype no context</li>
     * <li>A single stereotype with a context</li>
     * <li>metafacade properties no context</li>
     * <li>metafacade properties with a context</li>
     * <li>multiple stereotypes no context</li>
     * <li>multiple stereotypes with a context</li>
     * </ul>
     * </p>
     * <p/> NOTE: mapping properties are inherited from super metafacades.
     * </p>
     *
     * @param mappingClassName the name of the mapping class to use instead of
     *        the actual class name taken from the <code>mappingObject</code>.
     *        If null then the class name from the <code>mappingObject</code>
     *        is used.
     * @param mappingObject an instance of the class to which the mapping
     *        applies.
     * @param stereotypes the stereotypes to check.
     * @param rootContext the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hiearchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    private final MetafacadeMapping getMapping(
        final String mappingClassName,
        final Object mappingObject,
        final String context,
        final Collection stereotypes)
    {
        final String metaclassName = mappingClassName != null ? mappingClassName : mappingObject.getClass().getName();

        // verfiy we can at least find the meta class, so we don't perform the rest of
        // the search for nothing
        final boolean validMetaclass =
            CollectionUtils.find(
                this.mappings,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((MetafacadeMapping)object).getMappingClassName().equals(metaclassName);
                    }
                }) != null;
        MetafacadeMapping mapping = null;
        if (validMetaclass)
        {
            final boolean emptyStereotypes = stereotypes == null || stereotypes.isEmpty();

            // first try to find the mapping by context and stereotypes
            if (context != null && !emptyStereotypes)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                boolean valid = false;
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasContext() &&
                                    mapping.hasStereotypes() && !mapping.hasMappingProperties())
                                {
                                    valid =
                                        getContextHierarchy(context).contains(mapping.getContext()) &&
                                        stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // check for context and metafacade properties
            if (mapping == null && context != null)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    mapping.hasContext() && mapping.hasMappingProperties() &&
                                    !inProcessMappings.contains(mapping) && inProcessMetafacades.isEmpty())
                                {
                                    if (getContextHierarchy(context).contains(mapping.getContext()))
                                    {
                                        inProcessMappings.add(mapping);
                                        final MetafacadeBase metafacade =
                                            MetafacadeFactory.getInstance().createMetafacade(mappingObject, mapping);
                                        inProcessMetafacades.add(metafacade);

                                        // reset the "in process" mappings
                                        inProcessMappings.clear();
                                        valid = MetafacadeUtils.propertiesValid(metafacade, mapping);
                                    }
                                }
                                return valid;
                            }
                        });
            }

            // check just the context alone
            if (mapping == null && context != null)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                boolean valid = false;
                                MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasContext() &&
                                    !mapping.hasStereotypes() && !mapping.hasMappingProperties())
                                {
                                    valid = getContextHierarchy(context).contains(mapping.getContext());
                                }
                                return valid;
                            }
                        });
            }

            // check only stereotypes
            if (mapping == null && !emptyStereotypes)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                boolean valid = false;
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasStereotypes() &&
                                    !mapping.hasContext() && !mapping.hasMappingProperties())
                                {
                                    valid = stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // now check for metafacade properties
            if (mapping == null)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    !mapping.hasContext() && mapping.hasMappingProperties() &&
                                    !inProcessMappings.contains(mapping) && inProcessMetafacades.isEmpty())
                                {
                                    inProcessMappings.add(mapping);
                                    final MetafacadeBase metafacade =
                                        MetafacadeFactory.getInstance().createMetafacade(mappingObject, mapping);
                                    inProcessMetafacades.add(metafacade);

                                    // reset the "in process" mappings
                                    inProcessMappings.clear();
                                    valid = MetafacadeUtils.propertiesValid(metafacade, mapping);
                                }
                                return valid;
                            }
                        });
            }

            // finally find the mapping with just the class
            if (mapping == null)
            {
                mapping =
                    (MetafacadeMapping)CollectionUtils.find(
                        this.mappings,
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                return metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasContext() &&
                                !mapping.hasStereotypes() && !mapping.hasMappingProperties();
                            }
                        });
            }
        }

        // if it's still null, try with the parent
        if (mapping == null && this.parent != null)
        {
            mapping = this.parent.getMapping(metaclassName, mappingObject, context, stereotypes);
        }

        // reset the "in process" metafacades
        this.inProcessMetafacades.clear();
        return mapping;
    }

    /**
     * <p/>
     * Loads all property references into the given <code>mapping</code> inherited from any super metafacade of the
     * given mapping's metafacade. </p>
     *
     * @param mapping the MetafacadeMapping to which we'll add the inherited property references.
     * @param context the context from which the property references are inherited.
     * @return The MetafacadeMapping with all loaded property references.
     */
    private final void loadInheritedPropertyReferences(final MetafacadeMapping mapping)
    {
        if (mapping != null)
        {
            final Class[] interfaces = this.getInterfacesReversed(mapping.getMetafacadeClass().getName());
            if (interfaces != null && interfaces.length > 0)
            {
                for (int ctr = 0; ctr < interfaces.length; ctr++)
                {
                    final Class metafacadeClass = interfaces[ctr];
                    final MetafacadeMapping contextMapping =
                        (MetafacadeMapping)this.mappingsByMetafacadeClass.get(metafacadeClass);
                    if (contextMapping != null)
                    {
                        // add all property references
                        mapping.addPropertyReferences(contextMapping.getPropertyReferences());
                    }
                }
            }
        }
    }

    /**
     * The cache containing the hierachies for each context so that we don't need to retrieve more than once.
     */
    private final Map contextHierachyCache = new HashMap();

    /**
     * Retrieves all inherited contexts (including the root <code>context</code>) from the given <code>context</code>
     * and returns a list containing all of them.  Note that the visibilty of this operation is protected to improve
     * inner class access performance.
     *
     * @param context the root contexts
     * @return a list containing all inherited contexts
     */
    protected final List getContextHierarchy(final String context)
    {
        List contexts = (List)this.contextHierachyCache.get(context);
        if (contexts == null)
        {
            contexts = this.getInterfaces(context);
            if (contexts != null)
            {
                CollectionUtils.transform(
                    contexts,
                    new Transformer()
                    {
                        public Object transform(Object object)
                        {
                            return ((Class)object).getName();
                        }
                    });
            }
            this.contextHierachyCache.put(context, contexts);
        }
        return contexts;
    }

    /**
     * The cache of interfaces for the given className in reversed order.
     */
    private final Map reversedInterfaceArrayCache = new HashMap();

    /**
     * Gets the interfaces for the given <code>className</code> in reveresed order.
     *
     * @param className the name of the class for which to retrieve the interfaces
     * @return the array containing the reversed interfaces.
     */
    private final Class[] getInterfacesReversed(final String className)
    {
        Class[] interfaces = (Class[])this.reversedInterfaceArrayCache.get(className);
        if (interfaces == null)
        {
            interfaces = (Class[])this.getInterfaces(className).toArray(new Class[0]);
            if (interfaces != null && interfaces.length > 0)
            {
                CollectionUtils.reverseArray(interfaces);
            }
            this.reversedInterfaceArrayCache.put(className, interfaces);
        }
        return interfaces;
    }

    /**
     * Retrieves all interfaces for the given <code>className</code> (including the interface for <code>className</code>
     * itself).
     *
     * @param context the root context
     * @return a list containing all context interfaces ordered from the root down.
     */
    private final List getInterfaces(final String className)
    {
        final List interfaces = new ArrayList();
        if (StringUtils.isNotEmpty(className))
        {
            final Class contextClass = ClassUtils.loadClass(className);
            interfaces.addAll(ClassUtils.getAllInterfaces(contextClass));
            interfaces.add(0, contextClass);
        }
        return interfaces;
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
     * Adds a language mapping reference. This are used to populate metafacade impl classes with mapping files (such as
     * those that map from model types to Java, JDBC, SQL types, etc). If its added here as opposed to each child
     * MetafacadeMapping, then the reference will apply to all mappings.
     *
     * @param reference    the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addPropertyReference(
        final String reference,
        final String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }

    /**
     * Returns all property references for this MetafacadeMappings by <code>namespace</code> (these include all default
     * mapping references as well).
     *
     * @param namespace the namespace to search
     */
    public Map getPropertyReferences(final String namespace)
    {
        Map propertyReferences = (Map)namespacePropertyReferences.get(namespace);
        if (propertyReferences == null)
        {
            // first load the property references from
            // the mappings
            propertyReferences = new HashMap();
            propertyReferences.putAll(this.propertyReferences);
            MetafacadeMappings metafacades = this.getNamespaceMappings(namespace);
            if (metafacades != null)
            {
                propertyReferences.putAll(metafacades.propertyReferences);
            }
            this.namespacePropertyReferences.put(namespace, propertyReferences);
        }

        return propertyReferences;
    }

    /**
     * <p/> Attempts to get the MetafacadeMapping identified by the given
     * <code>mappingClass</code>,<code>context</code> and
     * <code>stereotypes<code>, from the mappings for the given <code>namespace</code>. If it can <strong>not</strong>
     * be found, it will search the default mappings and return that instead. </p>
     * <p/>
     * <strong>IMPORTANT:</strong> The <code>context</code> will take precedence over any <code>stereotypes</code> with
     * the mapping. </p>
     *
     * @param mappingClass the class name of the meta object for the mapping we are trying to find.
     * @param namespace    the namespace (i.e. a cartridge, name, etc.)
     * @param context      to which the mapping applies (note this takes precendence over stereotypes).
     * @param stereotypes  collection of sterotype names.  We'll check to see if the mapping for the given
     *                     <code>mappingClass</code> is defined for it.
     */
    public MetafacadeMapping getMetafacadeMapping(
        final Object mappingObject,
        final String namespace,
        final String context,
        final Collection stereotypes)
    {
        final String methodName = "MetafacadeMappings.getMetafacadeMapping";
        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug(
                "performing '" + methodName + "' with mappingObject '" + mappingObject + "', stereotypes '" +
                stereotypes + "', namespace '" + namespace + "' and context '" + context + "'");
        }

        final MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        MetafacadeMapping mapping = null;

        // first try the namespace mappings
        if (mappings != null)
        {
            mapping = mappings.getMapping(mappingObject, context, stereotypes);
        }

        // if we've found a namespace mapping, try to get any shared mappings
        // that this namespace mapping may extend and copy over any property
        // references from the shared mapping to the namespace mapping.
        if (mapping != null)
        {
            final Map propertyReferences = mapping.getPropertyReferences();
            final MetafacadeMapping defaultMapping = this.getMapping(mappingObject, context, stereotypes);
            if (defaultMapping != null)
            {
                Map defaultPropertyReferences = defaultMapping.getPropertyReferences();
                MetafacadeImpls metafacadeClasses = MetafacadeImpls.instance();
                final Class metafacadeInterface =
                    metafacadeClasses.getMetafacadeClass(mapping.getMetafacadeClass().getName());
                final Class defaultMetafacadeInterface =
                    metafacadeClasses.getMetafacadeClass(defaultMapping.getMetafacadeClass().getName());
                if (defaultMetafacadeInterface.isAssignableFrom(metafacadeInterface))
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
            if (this.getLogger().isDebugEnabled())
            {
                this.getLogger().debug("namespace mapping not found --> finding default");
            }
            mapping = this.getMapping(mappingObject, context, stereotypes);
        }

        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug("found mapping --> '" + mapping + "'");
        }
        return mapping;
    }

    /**
     * Gets the MetafacadeMappings instance belonging to the <code>namespace</code>.
     *
     * @param namespace the namespace name to check.
     * @return the found MetafacadeMappings.
     */
    private final MetafacadeMappings getNamespaceMappings(final String namespace)
    {
        return (MetafacadeMappings)this.namespaceMetafacadeMappings.get(namespace);
    }

    /**
     * Stores the parent metafacade mappings (if any).
     */
    private MetafacadeMappings parent;

    /**
     * Adds another MetafacadeMappings instance to the namespace metafacade mappings of this instance.
     *
     * @param namespace the namespace name to which the <code>mappings</code> will belong.
     * @param mappings  the MetafacadeMappings instance to add.
     */
    private final void addNamespaceMappings(
        final String namespace,
        final MetafacadeMappings mappings)
    {
        if (mappings != null)
        {
            mappings.parent = this;
            this.namespaceMetafacadeMappings.put(namespace, mappings);
        }
    }

    /**
     * Discover all metafacade mapping files on the class path. You need to call this anytime you want to find another
     * metafacade library that may have been made available.
     */
    public void discoverMetafacades()
    {
        final String methodName = "MetafacadeMappings.discoverMetafacadeMappings";
        final URL[] uris = ResourceFinder.findResources(METAFACADES_URI);
        if (uris == null || uris.length == 0)
        {
            this.getLogger().error("ERROR!! No metafacades found, please check your classpath");
        }
        else
        {
            AndroMDALogger.info("-- discovering metafacades --");
            try
            {
                // will store all namespaces (other than default)
                final Collection namespaces = new ArrayList();
                final Map discoveredMappings = new HashMap();
                for (int ctr = 0; ctr < uris.length; ctr++)
                {
                    final MetafacadeMappings mappings = MetafacadeMappings.getInstance(uris[ctr]);
                    final String namespace = mappings.getNamespace();
                    if (StringUtils.isEmpty(namespace))
                    {
                        throw new MetafacadeMappingsException(
                            methodName + " no 'namespace' has been set for metafacades --> '" + mappings.getResource() +
                            "'");
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

                    discoveredMappings.put(mappings.getNamespace(), mappings);
                }
                // list out the discovered mappings
                for (final Iterator iterator = discoveredMappings.values().iterator(); iterator.hasNext();)
                {
                    final MetafacadeMappings mappings = (MetafacadeMappings)iterator.next();
                    // construct the found informational based
                    // on whether or not the mappings are shared.
                    final StringBuffer foundMessage = new StringBuffer("found");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" shared");
                    }
                    foundMessage.append(" metafacades --> '" + mappings.getNamespace() + "'");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" - adding to '" + Namespaces.DEFAULT + "' namespace");
                    }
                    AndroMDALogger.info(foundMessage);                    
                }
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing " + methodName;
                this.getLogger().error(errMsg, th);
                throw new MetafacadeMappingsException(errMsg, th);
            }
            if (StringUtils.isEmpty(this.namespace))
            {
                String errMsg =
                    "No shared metafacades " + "found, please check your classpath, at least " +
                    "one set of metafacades must be marked as 'shared'";
                throw new MetafacadeMappingsException(errMsg);
            }
        }
    }

    /**
     * Gets the defaultMetafacadeClass, first looks for it in the namespace mapping, if it can't find it it then takes
     * the default mappings, setting.
     *
     * @return Returns the defaultMetafacadeClass.
     */
    public Class getDefaultMetafacadeClass(final String namespace)
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
    public void setDefaultMetafacadeClass(final String defaultMetafacadeClass)
    {
        try
        {
            this.defaultMetafacadeClass = ClassUtils.loadClass(StringUtils.trimToEmpty(defaultMetafacadeClass));
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing MetafacadeMappings.setDefaultMetafacadeClass";
            throw new MetafacadeMappingsException(errMsg, th);
        }
    }

    /**
     * Caches all properties values for this mapping (this includes
     * properties properties from the parent as well.
     */
    private Map propertyValues = null;

    /**
     * Retrieves the value of the property by
     * the properties <code>name</code> if one
     * can be found, otherwise returns null.
     *
     * @param name the name of the property who's value
     *        we'll retrieve.
     * @return the property value or null if one doesn't exist.
     */
    private final String getPropertyValue(final String name)
    {
        if (this.propertyValues == null)
        {
            this.propertyValues = this.getPropertyReferences(this.getNamespace());
            if (parent != null)
            {
                this.propertyValues.putAll(parent.getPropertyReferences(this.getNamespace()));
            }
        }
        return ObjectUtils.toString(this.propertyValues.get(name));
    }

    /**
     * Performs shutdown procedures for the factory. This should be called <strong>ONLY</code> when {@link
     * MetafacadeFactory#shutdown()}is called.
     */
    final void shutdown()
    {
        this.mappings.clear();
        this.namespaceMetafacadeMappings.clear();
        this.propertyReferences.clear();
        this.namespacePropertyReferences.clear();
        this.mappingsByMetafacadeClass.clear();
        this.contextHierachyCache.clear();
        this.reversedInterfaceArrayCache.clear();
        if (this.propertyValues != null)
        {
            this.propertyValues.clear();
        }
    }

    /**
     * Returns the logger instance to be used for logging within this class.
     *
     * @return the plugin logger
     */
    private final Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.namespace);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}