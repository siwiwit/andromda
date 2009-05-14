package org.andromda.core.metafacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.namespace.BaseNamespaceComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * The Metafacade mapping class. Used to map <code>metafacade</code> objects
 * to <code>metamodel</code> objects.
 *
 * @author Chad Brandon
 * @author Bob Fields
 * @see MetafacadeMapping
 * @see org.andromda.core.common.XmlObjectFactory
 */
public class MetafacadeMappings
    extends BaseNamespaceComponent
    implements Serializable
{
    /**
     * Holds the references to the child MetafacadeMapping instances.
     */
    private final Collection<MetafacadeMapping> mappings = new ArrayList<MetafacadeMapping>();

    /**
     * Holds the namespace MetafacadeMappings. This are child MetafacadeMappings
     * keyed by namespace name.
     */
    private final Map<String, MetafacadeMappings> namespaceMetafacadeMappings = new HashMap<String, MetafacadeMappings>();

    /**
     * The default meta facade to use when there isn't a mapping found.
     */
    private Class defaultMetafacadeClass = null;

    /**
     * Constructs a new instance of this class.
     *
     * @return MetafacadeMappings
     */
    public static MetafacadeMappings newInstance()
    {
        return new MetafacadeMappings();
    }

    /**
     * Adds a MetafacadeMapping instance to the set of current mappings.
     *
     * @param mapping the MetafacadeMapping instance.
     */
    public void addMapping(final MetafacadeMapping mapping)
    {
        ExceptionUtils.checkNull(
            "mapping",
            mapping);
        ExceptionUtils.checkNull(
            "mapping.metafacadeClass",
            mapping.getMetafacadeClass());
        mapping.setMetafacadeMappings(this);

        // find any mappings that match, if they do we add the properties
        // from that mapping to the existing matched mapping (so we only
        // have one mapping containing properties that can be 'OR'ed together).
        final MetafacadeMapping foundMapping =
            this.findMapping(
                new Condition()
                {
                    public boolean evaluate(final MetafacadeMapping object)
                    {
                        return mapping.match(object);
                    }
                });
        if (foundMapping != null)
        {
            foundMapping.addMappingPropertyGroup(mapping.getMappingProperties());
        }
        else
        {
            this.mappings.add(mapping);
            this.mappingsByMetafacadeClass.put(
                this.getMetafacadeInterface(mapping.getMetafacadeClass()),
                mapping);
        }
    }

    /**
     * Gets the class of the metafacade interface that belongs to the given
     * <code>metafacadeClass</code>.
     * @param metafacadeClass 
     *
     * @return the metafacade interface Class.
     */
    public Class getMetafacadeInterface(final Class metafacadeClass)
    {
        Class metafacadeInterface = null;
        if (metafacadeClass != null)
        {
            metafacadeInterface = metafacadeClass;
            final List interfaces = ClassUtils.getAllInterfaces(metafacadeClass);
            if (interfaces != null && !interfaces.isEmpty())
            {
                metafacadeInterface = (Class)interfaces.iterator().next();
            }
        }
        return metafacadeInterface;
    }

    /**
     * Stores mappings by the metafacade class so that we can retrieve the
     * inherited metafacade classes.
     */
    private final Map<Class, MetafacadeMapping> mappingsByMetafacadeClass = new HashMap<Class, MetafacadeMapping>();

    /**
     * Copies all data from <code>mappings<code> to this instance.
     *
     * @param mappings the mappings to add
     */
    private void copyMappings(final MetafacadeMappings mappings)
    {
        ExceptionUtils.checkNull(
            "mappings",
            mappings);
        for (final Iterator<MetafacadeMapping> iterator = mappings.mappings.iterator(); iterator.hasNext();)
        {
            final MetafacadeMapping mapping = (MetafacadeMapping)iterator.next();
            this.addMapping(mapping);
        }
        final Collection<String> propertyReferences = mappings.getPropertyReferences();
        if (propertyReferences != null && !propertyReferences.isEmpty())
        {
            this.propertyReferences.addAll(propertyReferences);
        }
        this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
    }

    /**
     * Contains references to properties populated in the Namespaces.
     */
    private final Collection<String> propertyReferences = new LinkedHashSet<String>();

    /**
     * Gets all property references defined in this mappings instance.
     *
     * @return the map of property references (names and values).
     */
    public Collection<String> getPropertyReferences()
    {
        return this.propertyReferences;
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
     * @param context the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hierarchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    protected MetafacadeMapping getMapping(
        final Object mappingObject,
        final String context,
        final Collection<String> stereotypes)
    {
        MetafacadeMapping mapping = this.getMapping(
                null,
                mappingObject,
                context,
                stereotypes);
        if (mapping == null)
        {
            final Collection hierarchy = this.getMappingObjectHierarchy(mappingObject);
            if (hierarchy != null && !hierarchy.isEmpty())
            {
                for (final Iterator iterator = hierarchy.iterator(); iterator.hasNext() && mapping == null;)
                {
                    mapping =
                        this.getMapping(
                            (String)iterator.next(),
                            mappingObject,
                            context,
                            stereotypes);
                }
            }
        }
        return mapping;
    }

    /**
     * The cache containing the hierarchies for each mapping object so that we
     * don't need to retrieve more than once.
     */
    private final Map mappingObjectHierarchyCache = new HashMap();

    /**
     * The pattern used for substituting the package name.
     */
    private static final String METAFACADE_PACKAGE_REPLACE_PATTERN = "\\{0\\}";

    /**
     * The pattern used for substituting the metafacade name.
     */
    private static final String METAFACADE_NAME_REPLACE_PATTERN = "\\{1\\}";

    /**
     * Retrieves the hierarchy of class names of the given
     * <code>mappingObject</code>.
     *
     * @param mappingObject the object from which to retrieve the hierarchy.
     * @return a list containing all inherited class names.
     */
    protected List getMappingObjectHierarchy(final Object mappingObject)
    {
        List hierarchy = (List)this.mappingObjectHierarchyCache.get(mappingObject);
        if (hierarchy == null)
        {
            // - we construct the mapping object name from the metafacade interface
            //  (using the underlying UML implementation name pattern).
            final String pattern = this.getMetaclassPattern();
            if (StringUtils.isNotBlank(pattern))
            {
                hierarchy = new ArrayList<Class>(ClassUtils.getAllInterfaces(mappingObject.getClass()));
                for (final ListIterator iterator = hierarchy.listIterator(); iterator.hasNext();)
                {
                    final Class metafacadeInterface = (Class)iterator.next();
                    final String packageName = ClassUtils.getPackageName(metafacadeInterface);
                    final String name = ClassUtils.getShortClassName(metafacadeInterface);

                    // - replace references {0} with the package name and
                    //   references of {1} with the name of the class
                    final String metafacadeImplementationName =
                        pattern != null
                        ? pattern.replaceAll(
                            METAFACADE_PACKAGE_REPLACE_PATTERN,
                            packageName).replaceAll(
                            METAFACADE_NAME_REPLACE_PATTERN,
                            name) : metafacadeInterface.getName();
                    iterator.set(metafacadeImplementationName);
                }
                this.mappingObjectHierarchyCache.put(
                    mappingObject,
                    hierarchy);
            }
        }
        return hierarchy;
    }

    /**
     * <p>
     * Stores the mappings which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the mapping
     * is being processed by the {@link #getMapping(Object, String, Collection)}
     * operation. We store these "in process" mappings in order to keep track of
     * the mappings currently being evaluated so we avoid stack over flow errors
     * {@link #getMapping(Object, String, Collection)}when finding mappings
     * that are mapped to super metafacade properties.
     * </p>
     * <p>
     * Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMappings = new ArrayList();

    /**
     * <p>
     * Stores the metafacades which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the
     * metafacade being processed by the {@link #getMapping(Object, String,
     * Collection)}operation. We store these "in process" metafacades in order
     * to keep track of the metafacades currently being evaluated so we avoid
     * stack over flow errors {@link #getMapping(Object, String, Collection)}when
     * finding metafacades that are mapped to super metafacade properties.
     * </p>
     * <p>
     * Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMetafacades = new ArrayList();

    /**
     * <p>
     * Retrieves the MetafacadeMapping belonging to the unique <code>key</code>
     * created from the <code>mappingObject</code>'s class,
     * <code>context</code> and given <code>stereotypes</code>. It's
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
     * <p>
     * NOTE: mapping properties are inherited from super metafacades.
     * </p>
     *
     * @param mappingClassName the name of the mapping class to use instead of
     *        the actual class name taken from the <code>mappingObject</code>.
     *        If null then the class name from the <code>mappingObject</code>
     *        is used.
     * @param mappingObject an instance of the class to which the mapping
     *        applies.
     * @param stereotypes the stereotypes to check.
     * @param context the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hierarchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    private MetafacadeMapping getMapping(
        final String mappingClassName,
        final Object mappingObject,
        final String context,
        final Collection<String> stereotypes)
    {
        final String metaclassName = mappingClassName != null ? mappingClassName : mappingObject.getClass().getName();

        // - Verify we can at least find the meta class, so we don't perform the
        //   rest of the search for nothing
        final boolean validMetaclass =
            this.findMapping(
                new Condition()
                {
                    public boolean evaluate(final MetafacadeMapping mapping)
                    {
                        return mapping.getMappingClassName().equals(metaclassName);
                    }
                }) != null;
        MetafacadeMapping mapping = null;
        if (validMetaclass)
        {
            final boolean emptyStereotypes = stereotypes == null || stereotypes.isEmpty();

            // - first try to find the mapping by context and stereotypes
            if (context != null && !emptyStereotypes)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasContext() &&
                                    mapping.hasStereotypes() && !mapping.hasMappingProperties())
                                {
                                    valid =
                                        getContextHierarchy(context).contains(mapping.getContext()) &&
                                        stereotypes!=null && stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // - check for context and metafacade properties
            if (mapping == null && context != null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    mapping.hasContext() && mapping.hasMappingProperties() &&
                                    (!inProcessMappings.contains(mapping)))
                                {
                                    if (getContextHierarchy(context).contains(mapping.getContext()))
                                    {
                                        inProcessMappings.add(mapping);
                                        final MetafacadeBase metafacade =
                                            MetafacadeFactory.getInstance().createMetafacade(
                                                mappingObject,
                                                mapping);
                                        inProcessMetafacades.add(metafacade);

                                        // reset the "in process" mappings
                                        inProcessMappings.clear();
                                        valid =
                                            MetafacadeUtils.propertiesValid(
                                                metafacade,
                                                mapping);
                                    }
                                }
                                return valid;
                            }
                        });
            }

            // - check just the context alone
            if (mapping == null && context != null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                boolean valid = false;
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
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasStereotypes() &&
                                    !mapping.hasContext() && !mapping.hasMappingProperties())
                                {
                                    valid = stereotypes!=null && stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // - now check for metafacade properties
            if (mapping == null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    !mapping.hasContext() && mapping.hasMappingProperties() &&
                                    (!inProcessMappings.contains(mapping)))
                                {
                                    inProcessMappings.add(mapping);
                                    final MetafacadeBase metafacade =
                                        MetafacadeFactory.getInstance().createMetafacade(
                                            mappingObject,
                                            mapping);
                                    inProcessMetafacades.add(metafacade);

                                    // reset the "in process" mappings
                                    inProcessMappings.clear();
                                    valid =
                                        MetafacadeUtils.propertiesValid(
                                            metafacade,
                                            mapping);
                                }
                                return valid;
                            }
                        });
            }

            // - finally find the mapping with just the class
            if (mapping == null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final MetafacadeMapping mapping)
                            {
                                return metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasContext() &&
                                !mapping.hasStereotypes() && !mapping.hasMappingProperties();
                            }
                        });
            }
        }

        // - if it's still null, try with the parent
        if (mapping == null && this.getParent() != null)
        {
            mapping =
                this.getParent().getMapping(
                    metaclassName,
                    mappingObject,
                    context,
                    stereotypes);
        }

        // - reset the "in process" metafacades
        this.inProcessMetafacades.clear();
        return mapping;
    }

    /**
     * Finds the first mapping in the internal {@link #mappings} collection that
     * matches the given condition.
     *
     * @param condition the condition
     * @return the found mapping instance
     */
    private MetafacadeMapping findMapping(final Condition condition)
    {
        MetafacadeMapping found = null;
        for (final Iterator<MetafacadeMapping> iterator = this.mappings.iterator(); iterator.hasNext();)
        {
            final MetafacadeMapping mapping = (MetafacadeMapping)iterator.next();
            if (condition.evaluate(mapping))
            {
                found = mapping;
                break;
            }
        }
        return found;
    }

    /**
     * Provides a means to evaluate whether or not a condition is true.
     */
    static interface Condition
    {
        public boolean evaluate(final MetafacadeMapping mapping);
    }

    /**
     * <p>
     * Loads all property references into the given <code>mapping</code>
     * inherited from any super metafacade of the given mapping's metafacade.
     * </p>
     *
     * @param mapping the MetafacadeMapping to which we'll add the inherited
     *        property references.
     */
    private void loadInheritedPropertyReferences(final MetafacadeMapping mapping)
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
     * The cache containing the hierarchies for each context so that we don't
     * need to retrieve more than once.
     */
    private final Map contextHierarchyCache = new HashMap();

    /**
     * Retrieves all inherited contexts (including the root <code>context</code>)
     * from the given <code>context</code> and returns a list containing all
     * of them. Note that the visibility of this operation is protected to
     * improve inner class access performance.
     *
     * @param context the root contexts
     * @return a list containing all inherited contexts
     */
    protected final List getContextHierarchy(final String context)
    {
        List contexts = (List)this.contextHierarchyCache.get(context);
        if (contexts == null)
        {
            contexts = ClassUtils.getInterfaces(context);
            if (contexts != null)
            {
                for (final ListIterator iterator = contexts.listIterator(); iterator.hasNext();)
                {
                    iterator.set(((Class)iterator.next()).getName());
                }
            }
            this.contextHierarchyCache.put(
                context,
                contexts);
        }
        return contexts;
    }

    /**
     * The cache of interfaces for the given className in reversed order.
     */
    private final Map<String, Class[]> reversedInterfaceArrayCache = new HashMap<String, Class[]>();

    /**
     * Gets the interfaces for the given <code>className</code> in reverse
     * order.
     *
     * @param className the name of the class for which to retrieve the
     *        interfaces
     * @return the array containing the reversed interfaces.
     */
    private Class[] getInterfacesReversed(final String className)
    {
        Class[] interfaces = (Class[])this.reversedInterfaceArrayCache.get(className);
        if (interfaces == null)
        {
            interfaces = ClassUtils.getInterfacesReversed(className);
            this.reversedInterfaceArrayCache.put(
                className,
                interfaces);
        }
        return interfaces;
    }

    /**
     * Adds a language mapping reference. This are used to populate metafacade
     * impl classes with mapping files (such as those that map from model types
     * to Java, JDBC, SQL types, etc). If its added here as opposed to each
     * child MetafacadeMapping, then the reference will apply to all mappings.
     *
     * @param reference the name of the reference.
     */
    public void addPropertyReference(final String reference)
    {
        this.propertyReferences.add(reference);
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
     * @param mappingObject the meta object for the mapping we are trying to find.
     * @param namespace the namespace (i.e. a cartridge, name, etc.)
     * @param context to which the mapping applies (note this takes precedence over stereotypes).
     * @param stereotypes collection of stereotype names.  We'll check to see if the mapping for the given
     *                    <code>mappingClass</code> is defined for it.
     * @return mapping 
     */
    public MetafacadeMapping getMetafacadeMapping(
        final Object mappingObject,
        final String namespace,
        final String context,
        final Collection<String> stereotypes)
    {
        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug(
                "performing 'MetafacadeMappings.getMetafacadeMapping' with mappingObject '" + mappingObject +
                "', stereotypes '" + stereotypes + "', namespace '" + namespace + "' and context '" + context + "'");
        }

        MetafacadeMapping mapping = null;

        final MetafacadeMappings mappings = this.getNamespaceMappings(namespace);

        // first try the namespace mappings
        if (mappings != null)
        {
            // - set the parent namespace
            mappings.parentNamespace = this.getNamespace();
            mapping =
                mappings.getMapping(
                    mappingObject,
                    context,
                    stereotypes);
        }

        // - if we've found a namespace mapping, try to get any shared mappings
        //   that this namespace mapping may extend and copy over any property
        //   references from the shared mapping to the namespace mapping.
        if (mapping != null)
        {
            final Collection<String> propertyReferences = mapping.getPropertyReferences();
            final MetafacadeMapping defaultMapping = this.getMapping(
                    mappingObject,
                    context,
                    stereotypes);
            if (defaultMapping != null)
            {
                Collection<String> defaultPropertyReferences = defaultMapping.getPropertyReferences();
                final Class metafacadeInterface =
                    this.metafacadeClasses.getMetafacadeClass(mapping.getMetafacadeClass().getName());
                final Class defaultMetafacadeInterface =
                    this.metafacadeClasses.getMetafacadeClass(defaultMapping.getMetafacadeClass().getName());
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
            mapping =
                this.getMapping(
                    mappingObject,
                    context,
                    stereotypes);
        }

        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug("found mapping --> '" + mapping + "'");
        }
        return mapping;
    }

    /**
     * Gets the MetafacadeMappings instance belonging to the
     * <code>namespace</code>.
     *
     * @param namespace the namespace name to check.
     * @return the found MetafacadeMappings.
     */
    private MetafacadeMappings getNamespaceMappings(final String namespace)
    {
        return (MetafacadeMappings)this.namespaceMetafacadeMappings.get(namespace);
    }

    /**
     * Stores the possible parents of this metafacade mappings instance (i.e. mappings for uml-1.4, emf-uml2, etc).
     */
    private Map<String, MetafacadeMappings> parents = new HashMap<String, MetafacadeMappings>();

    /**
     * Retrieves the appropriate parent based on the current {@link #getNamespace()}.
     *
     * @return the parent metafacade mappings.
     */
    private MetafacadeMappings getParent()
    {
        return (MetafacadeMappings)this.parents.get(this.parentNamespace);
    }

    /**
     * Adds a MetafacadeMappings instance to the namespace metafacade mappings
     * of this instance.
     *
     * @param namespace the namespace name to which the <code>mappings</code>
     *        will belong.
     * @param mappings the MetafacadeMappings instance to add.
     */
    private void addNamespaceMappings(
        final String namespace,
        final MetafacadeMappings mappings)
    {
        if (mappings != null)
        {
            // - set the parent by its namespace (the parent is different depending on the current metafacade model namespace)
            mappings.parents.put(
                this.getNamespace(),
                this);
            this.namespaceMetafacadeMappings.put(
                namespace,
                mappings);
        }
    }

    /**
     * Initializes this mappings instance, which includes discovery of all
     * metafacade mappings instances on the classpath.
     */
    public void initialize()
    {
        final List<String> modelTypeNamespaces = new ArrayList<String>();
        final Collection<MetafacadeMappings> metafacades = ComponentContainer.instance().findComponentsOfType(MetafacadeMappings.class);
        for (final Iterator<MetafacadeMappings> iterator = metafacades.iterator(); iterator.hasNext();)
        {
            final MetafacadeMappings mappings = (MetafacadeMappings)iterator.next();
            final String namespace = mappings.getNamespace();
            if (MetafacadeUtils.isMetafacadeModelPresent(namespace))
            {
                modelTypeNamespaces.add(namespace);
            }
        }

        final String[] modelNamespaces = (String[])modelTypeNamespaces.toArray(new String[0]);
        MetafacadeImpls.instance().discover(modelNamespaces);
        this.initializeMappings(modelNamespaces);
    }

    /**
     * Registers all namespace properties in the shared {@link MetafacadeFactory} instance.
     */
    final void registerAllProperties()
    {
        // - register all namespace property references defined in the descriptors
        final Namespaces namespaces = Namespaces.instance();
        for (final Iterator<Namespace> iterator = namespaces.getNamespaces().iterator(); iterator.hasNext();)
        {
            final String mappingsNamespace = ((Namespace)iterator.next()).getName();

            // - add the default mappings
            final Collection<MetafacadeMapping> mappings = new ArrayList<MetafacadeMapping>(this.mappings);
            final MetafacadeMappings metafacadeMappings = this.getNamespaceMappings(mappingsNamespace);

            // - add all the references from the default namespace
            final Collection<String> propertyReferences = new ArrayList<String>(this.propertyReferences);

            // - if we have namespace mappings, add them
            if (metafacadeMappings != null)
            {
                mappings.addAll(metafacadeMappings.mappings);
                propertyReferences.addAll(metafacadeMappings.propertyReferences);
            }

            for (final Iterator<MetafacadeMapping> mappingIterator = mappings.iterator(); mappingIterator.hasNext();)
            {
                final MetafacadeMapping mapping = (MetafacadeMapping)mappingIterator.next();
                final String metafacadeInterface =
                    this.metafacadeClasses.getMetafacadeClass(mapping.getMetafacadeClass().getName()).getName();

                // - first register the references defined globally in the
                // descriptor for each interface
                // in the hierarchy
                final Class[] interfaces = this.getInterfacesReversed(metafacadeInterface);
                for (int ctr = 0; ctr < interfaces.length; ctr++)
                {
                    this.registerProperties(
                        mappingsNamespace,
                        propertyReferences,
                        interfaces[ctr].getName());
                }

                // - next register the references defined only within each mapping
                // - remember to first load the inherited property references
                //   into the mapping
                this.loadInheritedPropertyReferences(mapping);
                this.registerProperties(
                    mappingsNamespace,
                    mapping.getPropertyReferences(),
                    metafacadeInterface);
            }
        }
    }

    /**
     * The name of the metaclass pattern.
     */
    private String metaclassPattern;

    /**
     * First attempts to retrieve the metaclass pattern from this instance, and
     * if not found, attempts to retrieve it from the parent instance (since the
     * parent instance should always have been set at least once from a shared
     * metafacades instance).
     *
     * @return the metaclass pattern.
     */
    private String getMetaclassPattern()
    {
        if (this.metaclassPattern == null && this.getParent() != null)
        {
            this.metaclassPattern = this.getParent().metaclassPattern;
        }
        return this.metaclassPattern;
    }

    /**
     * Sets the pattern of the metaclass implementations based on a metaclass
     * interface name. This should only be set on a metafacade mappings
     * instances that is marked as shared.
     *
     * @param metaclassPattern the pattern for the meta classes.
     */
    public void setMetaclassPattern(final String metaclassPattern)
    {
        this.metaclassPattern = metaclassPattern;
    }

    /**
     * Initializes all the metafacade mapping instances under the appropriate model type (defined
     * in the <code>modelTypes</code> collection.
     *
     * @param metafacadeModelNamespaces a list of each namespace containing a metafacade model facade implementation.
     */
    private void initializeMappings(final String[] metafacadeModelNamespaces)
    {
        ExceptionUtils.checkNull(
            "modelTypes",
            metafacadeModelNamespaces);
        final Collection metafacades = ComponentContainer.instance().findComponentsOfType(MetafacadeMappings.class);

        // - we need to load up the allMetafacadeMappingInstances before we do
        //   anything else
        for (final Iterator iterator = metafacades.iterator(); iterator.hasNext();)
        {
            final MetafacadeMappings mappings = (MetafacadeMappings)iterator.next();
            for (final Iterator<MetafacadeMapping> mappingIterator = mappings.mappings.iterator(); mappingIterator.hasNext();)
            {
                final MetafacadeMapping mapping = (MetafacadeMapping)mappingIterator.next();
                if (mapping.isMappingClassNamePresent())
                {
                    MetafacadeMappings.allMetafacadeMappingInstances.put(
                        mapping.getMetafacadeClass(),
                        mapping.getMappingClassName());
                }
            }
        }

        final List<String> modelNamespaces = new ArrayList<String>(Arrays.asList(metafacadeModelNamespaces));
        try
        {
            final Namespaces namespaces = Namespaces.instance();
            final int numberOfModelTypes = metafacadeModelNamespaces.length;
            for (int ctr = 0; ctr < numberOfModelTypes; ctr++)
            {
                final String modelNamespace = metafacadeModelNamespaces[ctr];
                if (modelNamespace != null)
                {
                    // - remove the current model type so that we don't keep out the namespace
                    //   that stores the metafacade model
                    modelNamespaces.remove(modelNamespace);

                    MetafacadeMappings modelMetafacadeMappings =
                        (MetafacadeMappings)this.modelMetafacadeMappings.get(modelNamespace);
                    if (modelMetafacadeMappings == null)
                    {
                        modelMetafacadeMappings = MetafacadeMappings.newInstance();

                        // - set the namespace
                        modelMetafacadeMappings.setNamespace(modelNamespace);
                        this.modelMetafacadeMappings.put(
                            modelNamespace,
                            modelMetafacadeMappings);
                    }

                    for (final Iterator<MetafacadeMappings> iterator = metafacades.iterator(); iterator.hasNext();)
                    {
                        final MetafacadeMappings mappings = (MetafacadeMappings)iterator.next();
                        final String namespace = mappings.getNamespace();

                        if (!modelNamespaces.contains(namespace))
                        {
                            // - if we have 'shared' mappings or only a single set available, they are copied
                            //   to this mappings instance.
                            if (namespaces.isShared(namespace) || metafacades.size() == 1)
                            {
                                // - copy over any 'shared' mappings to this root instance
                                modelMetafacadeMappings.copyMappings(mappings);

                                // - set the metaclass pattern from the 'shared' or single
                                //   instance of metafacades
                                final String metaclassPattern = mappings.metaclassPattern;
                                if (metaclassPattern != null && metaclassPattern.trim().length() > 0)
                                {
                                    modelMetafacadeMappings.setMetaclassPattern(mappings.metaclassPattern);
                                }
                            }
                            else
                            {
                                // add all others as namespace mappings
                                modelMetafacadeMappings.addNamespaceMappings(
                                    namespace,
                                    mappings);
                            }
                        }
                    }

                    // - add the metafacade model namespace back
                    modelNamespaces.add(modelNamespace);
                    if (modelMetafacadeMappings.getNamespace() == null ||
                        modelMetafacadeMappings.getNamespace().trim().length() == 0)
                    {
                        throw new MetafacadeMappingsException(
                            "No shared metafacades found, please check your classpath, at least " +
                            "one set of metafacades must be marked as 'shared'");
                    }
                    if (modelMetafacadeMappings.metaclassPattern == null ||
                        modelMetafacadeMappings.metaclassPattern.trim().length() == 0)
                    {
                        throw new MetafacadeMappingsException("At least one set of metafacades marked as shared " +
                            "must have the 'metaclassPattern' attribute defined");
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeMappingsException(throwable);
        }
    }

    /**
     * Stores all metafacade mapping instances
     */
    private static Map<Class, String> allMetafacadeMappingInstances = new HashMap<Class, String>();

    /**
     * Stores every metafacade mapping instance, this is used from
     * {@link MetafacadeUtils#getInheritedMappingClassName(MetafacadeMapping)}.
     *
     * @return all metafacade mapping instances.
     */
    static Map<Class, String> getAllMetafacadeMappingInstances()
    {
        return allMetafacadeMappingInstances;
    }

    /**
     * The shared metafacade impls instance.
     */
    private MetafacadeImpls metafacadeClasses = MetafacadeImpls.instance();

    /**
     * Stores the metafacadeMapping instances by model type.
     */
    private Map<String, MetafacadeMappings> modelMetafacadeMappings = new LinkedHashMap<String, MetafacadeMappings>();

    /**
     * Should be used used instead of "this", retrieves the appropriate
     * metafacade mappings instance based on the current model type.
     *
     * @param metafacadeModelNamespace the namespace that contains a metafacade model facade implementation.
     * @return the {@link MetafacadeMappings} instance.
     */
    public MetafacadeMappings getModelMetafacadeMappings(final String metafacadeModelNamespace)
    {
        final MetafacadeMappings instance =
            (MetafacadeMappings)this.modelMetafacadeMappings.get(metafacadeModelNamespace);
        if (instance == null)
        {
            throw new MetafacadeMappingsException("Namespace '" + metafacadeModelNamespace +
                "' is not a registered metafacade model namespace");
        }
        return instance;
    }

    /**
     * Stores the namespace of the parent mappings.
     */
    private String parentNamespace;

    /**
     * Gets the defaultMetafacadeClass, first looks for it in the namespace
     * mapping, if it can't find it it then takes the default mappings, setting.
     * @param namespace mapping to check for defaultMetafacadeClass
     * @return Returns the defaultMetafacadeClass.
     */
    final Class getDefaultMetafacadeClass(final String namespace)
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
     * Sets the default metafacade class to use if no other is found for the
     * mapping class.
     *
     * @param defaultMetafacadeClass the default metafacade class.
     */
    public void setDefaultMetafacadeClass(final String defaultMetafacadeClass)
    {
        try
        {
            this.defaultMetafacadeClass = ClassUtils.loadClass(StringUtils.trimToEmpty(defaultMetafacadeClass));
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeMappingsException(throwable);
        }
    }

    /**
     * Retrieves all child {@link MetafacadeMapping} instances belonging to this
     * metafacade mappings instance.
     *
     * @return the collection of {@link MetafacadeMapping} instances
     */
    protected Collection<MetafacadeMapping> getMappings()
    {
        return this.mappings;
    }

    /**
     * Registers the defined property references properties in the metafacade
     * factory.
     *
     * @param propertyReferences the property references to register.
     * @param metafacadeName the name of the metafacade under which to register
     *        the properties.
     * @param namespace the namespace of the property reference.
     */
    final void registerProperties(
        final String namespace,
        final Collection<String> propertyReferences,
        final String metafacadeName)
    {
        final MetafacadeFactory factory = MetafacadeFactory.getInstance();
        for (final Iterator iterator = propertyReferences.iterator(); iterator.hasNext();)
        {
            final String reference = (String)iterator.next();
            final String value = Namespaces.instance().getPropertyValue(
                    namespace,
                    reference);
            if (value != null)
            {
                if (this.getLogger().isDebugEnabled())
                {
                    this.getLogger().debug(
                        "setting context property '" + reference + "' with value '" + value + "' for namespace '" +
                        namespace + "' on metafacade '" + metafacadeName + "'");
                }
            }
            factory.registerProperty(
                namespace,
                metafacadeName,
                reference,
                value);
        }
    }

    /**
     * Performs shutdown procedures for the factory. This should be called
     * <strong>ONLY</code> when {@link MetafacadeFactory#shutdown()}is called.
     */
    final void shutdown()
    {
        this.mappings.clear();
        this.inProcessMappings.clear();
        this.inProcessMetafacades.clear();
        this.namespaceMetafacadeMappings.clear();
        this.propertyReferences.clear();
        this.mappingObjectHierarchyCache.clear();
        this.mappingsByMetafacadeClass.clear();
        this.contextHierarchyCache.clear();
        this.reversedInterfaceArrayCache.clear();
        for (final Iterator iterator = this.modelMetafacadeMappings.values().iterator(); iterator.hasNext();)
        {
            final MetafacadeMappings metafacadeMappings = (MetafacadeMappings)iterator.next();
            metafacadeMappings.shutdown();
        }
        this.modelMetafacadeMappings.clear();
    }

    /**
     * Returns the logger instance to be used for logging within this class.
     *
     * @return the plugin logger
     */
    private Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.getNamespace());
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getNamespace() + "]";
    }
}