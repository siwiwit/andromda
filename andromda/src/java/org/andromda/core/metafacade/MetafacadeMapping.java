package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Profile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A meta facade mapping class. This class is a child of
 * {@link MetafacadeMappings}.
 * 
 * @author Chad Brandon
 */
public class MetafacadeMapping
{
    /**
     * The meta facade for which this mapping applies.
     */
    private Class metafacadeClass = null;

    /**
     * Gets the metafacadeClass for this mapping.
     * 
     * @return Returns the metafacadeClass.
     */
    public Class getMetafacadeClass()
    {
        return metafacadeClass;
    }

    /**
     * Sets the metafacadeClassName for this mapping.
     * 
     * @param metafacadeClassName The name of the metafaacde class to set.
     */
    public void setMetafacadeClassName(String metafacadeClassName)
    {
        try
        {
            this.metafacadeClass = ClassUtils.loadClass(StringUtils
                .trimToEmpty(metafacadeClassName));
        }
        catch (Throwable th)
        {
            throw new MetafacadeMappingsException(th);
        }
    }

    /**
     * The name of the mapping class for which this mapping applies. The
     * {@link #context},{@link #stereotypes}and this name make up the
     * identifying key for this mapping.
     */
    private String mappingClassName = null;

    /**
     * Gets the name of the metaobject class used for this mapping.
     * 
     * @return Returns the mappingClassName.
     */
    protected String getMappingClassName()
    {
        return this.mappingClassName;
    }

    /**
     * The name of the metaobject class to use for this mapping.
     * 
     * @param mappingClassName The mappingClassName to set.
     */
    public void setMappingClassName(String mappingClassName)
    {
        this.mappingClassName = StringUtils.trimToEmpty(mappingClassName);
    }

    /**
     * Whether or not this mapping represents a <code>contextRoot</code>.
     */
    private boolean contextRoot = false;

    /**
     * <p>
     * Gets whether or not this mapping represents a <code>contextRoot</code>,
     * by default a mapping is <strong>NOT </strong> a contextRoot. You'll want
     * to specify this as true when other metafacades need to be created within
     * the context of this metafacade.
     * </p>
     * 
     * @return Returns the contextRoot.
     */
    public boolean isContextRoot()
    {
        return contextRoot;
    }

    /**
     * Sets the name of the <code>contextRoot</code> for this mapping.
     * 
     * @param contextRoot The contextRoot to set.
     * @see #isContextRoot()
     */
    public void setContextRoot(boolean contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * The stereotypes to which this mapping applies (all stereotypes must be
     * present for this mapping to apply).
     */
    private final List stereotypes = new ArrayList();

    /**
     * Adds a <code>stereotype</code> to the stereotypes.
     * 
     * @param stereotype
     */
    public void addStereotype(String stereotype)
    {
        this.stereotypes.add(Profile.instance().get(stereotype));
    }

    /**
     * Gets the stereotypes which apply to this mapping.
     * 
     * @return the names of the stereotypes
     */
    List getStereotypes()
    {
        return this.stereotypes;
    }

    /**
     * Indicates whether or not this mapping has any stereotypes defined.
     * 
     * @return true/false
     */
    boolean hasStereotypes()
    {
        return !this.stereotypes.isEmpty();
    }

    /**
     * Used to hold references to language mapping classes.
     */
    private final Map propertyReferences = new HashMap();

    /**
     * Adds a mapping property reference. These are used to populate metafacade
     * impl classes with mapping files, etc. The property reference applies to
     * the given mapping.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     * @see MetafacadeMappings#addPropertyReference(String, String)
     */
    public void addPropertyReference(String reference, String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }

    /**
     * Returns all mapping references for this MetafacadeMapping instance.
     */
    public Map getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * Used to hold the properties that should apply to the mapping element.
     */
    private PropertyGroup mappingProperties = null;

    /**
     * Adds a mapping property. This are used to narrow the metafacade to which
     * the mapping can apply. The properties must exist and must evaluate to the
     * specified value if given for the mapping to match.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addMappingProperty(String name, String value)
    {
        if (this.mappingProperties == null)
        {
            this.mappingProperties = new PropertyGroup();
            // we add the mapping properties to the mappingPropertyGroups
            // collection only once
            this.mappingPropertyGroups.add(this.mappingProperties);
        }
        this.mappingProperties.addProperty(new Property(name, value));
    }

    /**
     * Stores a collection of all property groups added through
     * {@link #addPropertyGroup(Collection)}. These are property groups added
     * from other mappings that return true when executing
     * {@link #match(MetafacadeMapping)}.
     */
    private final Collection mappingPropertyGroups = new ArrayList();

    /**
     * Adds the <code>propertyGroup</code> to the existing mapping property
     * groups within this mapping.
     * 
     * @param mappingProperties the collection of mapping properties to add to
     *        the mapping properties within this mappings instance.
     */
    void addMappingPropertyGroup(PropertyGroup propertyGroup)
    {
        this.mappingPropertyGroups.add(propertyGroup);
    }

    /**
     * Returns all mapping property groups for this MetafacadeMapping instance.
     */
    Collection getMappingPropertyGroups()
    {
        return this.mappingPropertyGroups;
    }

    /**
     * Gets the mapping properties associated this this mapping directly
     * (contained within a {@link PropertyGroup}instance).
     * 
     * @return the mapping property group.
     */
    PropertyGroup getMappingProperties()
    {
        return this.mappingProperties;
    }

    /**
     * Indicates whether or not this mapping contains any mapping properties.
     * 
     * @return true/false
     */
    boolean hasMappingProperties()
    {
        return this.mappingProperties != null
            && !this.mappingProperties.getProperties().isEmpty();
    }

    /**
     * Adds all <code>propertyReferences</code> to the property references
     * contained in this MetafacadeMapping instance.
     * 
     * @param propertyReferences the property references to add.
     */
    public void addPropertyReferences(Map propertyReferences)
    {
        if (propertyReferences != null)
        {
            this.propertyReferences.putAll(propertyReferences);
        }
    }

    /**
     * The key used to uniquely identify this mapping.
     */
    private String key;

    /**
     * Gets the unique key that identifies this mapping.
     */
    protected String getKey()
    {
        final MetafacadeMappings parent = this.getMetafacadeMappings();
        // we keep constructing the key until the parent MetafacadeMappings
        // instance is fully initialized becaues we don't know if this
        // instance is fully initialized until that point.
        if ((this.key == null && this.mappingClassName != null)
            || parent == null || !parent.isInitialized())
        {
            key = MetafacadeUtils.constructKey(
                this.mappingClassName,
                this.mappings.getNamespace());
            key = MetafacadeUtils.constructKey(
                key,
                this.context,
                this.stereotypes);
            if (this.hasMappingProperties())
            {
                Iterator mappingPropertyIterator = this
                    .getMappingPropertyGroups().iterator();
                while (mappingPropertyIterator.hasNext())
                {
                    PropertyGroup group = (PropertyGroup)mappingPropertyIterator
                        .next();
                    key = MetafacadeUtils.constructKey(key, group.toString());
                }
            }
        }
        return key;
    }

    /**
     * The context to which this mapping applies.
     */
    private String context = "";

    /**
     * Sets the context to which this mapping applies.
     * 
     * @param context The metafacade context name to set.
     */
    public void setContext(String context)
    {
        this.context = StringUtils.trimToEmpty(context);
    }

    /**
     * Gets the context to which this mapping applies.
     * 
     * @return the name of the context
     */
    String getContext()
    {
        return this.context;
    }

    /**
     * Indicates whether or not this mapping has a context.
     * 
     * @return true/false
     */
    boolean hasContext()
    {
        return StringUtils.isNotEmpty(this.context);
    }

    /**
     * The "parent" metafacade mappings;
     */
    private MetafacadeMappings mappings;

    /**
     * Sets the metafacade mappings instance to which this particular mapping
     * belongs. (i.e. the parent) Note, that this is populated during the call
     * to {@link MetafacadeMappings#addMapping(MetafacadeMapping)}.
     * 
     * @param mappings the MetacadeMappings instance to which this mapping
     *        belongs.
     */
    void setMetafacadeMappings(MetafacadeMappings mappings)
    {
        this.mappings = mappings;
    }

    /**
     * Gets the "parent" MetafacadeMappings instance to which this mapping
     * belongs.
     * 
     * @return the parent metafacade mappings instance.
     */
    MetafacadeMappings getMetafacadeMappings()
    {
        return this.mappings;
    }

    /**
     * Indicates whether or not the <code>mapping</code> matches this mapping.
     * It matches on the following:
     * <ul>
     * <li>metafacadeClass</li>
     * <li>mappingClassName</li>
     * <li>stereotypes</li>
     * </ul>
     */
    boolean match(MetafacadeMapping mapping)
    {
        return mapping != null
            && this.getMetafacadeClass().equals(mapping.getMetafacadeClass())
            && this.getStereotypes().equals(mapping.getStereotypes())
            && this.getMappingClassName().equals(mapping.getMappingClassName())
            && this.getContext().equals(mapping.getContext());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Represents a group of properties. Properties within a group are evaluated
     * within an 'AND' expression. PropertyGroups are evaluated together as an
     * 'OR' expressions (i.e. you 'OR' property groups together, and 'AND'
     * properties together).
     * 
     * @see MetafacadeMappings#addMapping(MetafacadeMapping)
     */
    static class PropertyGroup
    {
        private final Collection properties = new ArrayList();

        /**
         * Adds a property to the internal collection of properties.
         * 
         * @param property the property to add to this group.
         */
        void addProperty(Property property)
        {
            this.properties.add(property);
        }

        /**
         * Gets the currently internal collection of properties.
         * 
         * @return the properties collection.
         */
        Collection getProperties()
        {
            return this.properties;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            StringBuffer toString = new StringBuffer();
            Iterator propertyIterator = this.properties.iterator();
            char seperator = ':';
            while (propertyIterator.hasNext())
            {
                Property property = (Property)propertyIterator.next();
                toString.append(property.getName());
                if (StringUtils.isNotEmpty(property.getValue()))
                {
                    toString.append(seperator);
                    toString.append(property.getValue());
                }
                if (propertyIterator.hasNext())
                {
                    toString.append(seperator);
                }
            }
            return toString.toString();
        }
    }

    /**
     * Stores and provides access to the mapping element's nested
     * &lt;property/&gt;.
     */
    static class Property
    {
        private String name;
        private String value;

        Property(
            String name,
            String value)
        {
            this.name = StringUtils.trimToEmpty(name);
            this.value = value;
        }

        /**
         * Gets the value of the <code>name</code> attribute on the
         * <code>property</code> element.
         * 
         * @return the name
         */
        String getName()
        {
            return StringUtils.trimToEmpty(this.name);
        }

        /**
         * Gets the value of the <code>value</code> attribute defined on the
         * <code>property</code> element.
         * 
         * @return the value
         */
        String getValue()
        {
            return StringUtils.trimToEmpty(this.value);
        }
    }
}