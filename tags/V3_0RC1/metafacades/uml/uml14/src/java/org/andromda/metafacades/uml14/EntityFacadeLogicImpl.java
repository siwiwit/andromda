package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EntityQueryOperationFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;

/**
 * Metaclass facade implementation.
 */
public class EntityFacadeLogicImpl
    extends EntityFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EntityFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    public void initialize()
    {
        super.initialize();
        // if there are no identifiers on this entity, create and add one.
        // enumeration don't have identifiers since they are not entities
        if (!this.isIdentifiersPresent() && this.isAllowDefaultIdentifiers())
        {
            this.createIdentifier();
        }
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getQueryOperations()
     */
    protected java.util.Collection handleGetQueryOperations()
    {
        return this.getQueryOperations(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getQueryOperations(boolean)
     */
    protected java.util.Collection handleGetQueryOperations(boolean follow)
    {
        Collection queryOperations = this.getOperations();

        MetafacadeUtils.filterByType(
            queryOperations,
            EntityQueryOperationFacade.class);

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (EntityFacade.class.isAssignableFrom(superClass.getClass()))
            {
                EntityFacade entity = (EntityFacade)superClass;
                queryOperations.addAll(entity.getQueryOperations());
            }
        }
        return queryOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers()
     */
    protected java.util.Collection handleGetIdentifiers()
    {
        return this.getIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers(boolean)
     */
    protected java.util.Collection handleGetIdentifiers(boolean follow)
    {
        Collection identifiers = EntityMetafacadeUtils.getIdentifiers(
            this,
            follow);
        return identifiers;
    }

    /**
     * Creates an identifier from the default identifier properties specified
     * within a namespace.
     */
    private void createIdentifier()
    {
        // first check if the foreign identifier flag is set, and
        // let those taken precedence if so
        if (!this.checkForAndAddForeignIdentifiers())
        {
            this.createIdentifier(this.getDefaultIdentifier(), this
                .getDefaultIdentifierType(), this
                .getDefaultIdentifierVisibility());
        }
    }

    /**
     * Creates a new identifier and adds it to the underlying meta model
     * classifier instance.
     * 
     * @param name the name to give the identifier
     * @param type the type to give the identifier
     * @param visibility the visibility to give the identifier
     */
    private void createIdentifier(String name, String type, String visibility)
    {
        // only create the identifier if an identifer with the name doesn't
        // already exist
        if (!UMLMetafacadeUtils.attributeExists(this.metaObject, name))
        {
            Attribute identifier = UMLMetafacadeUtils
                .createAttribute(
                    name,
                    type,
                    visibility,
                    String
                        .valueOf(this
                            .getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)));

            identifier.getStereotype().add(
                UMLMetafacadeUtils
                    .findOrCreateStereotype(UMLProfile.STEREOTYPE_IDENTIFIER));

            ((Classifier)this.metaObject).getFeature().add(identifier);
        }
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#isIdentifiersPresent()
     */
    protected boolean handleIsIdentifiersPresent()
    {
        Collection identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getTableName()
     */
    protected String handleGetTableName()
    {
        String tableNamePrefix = StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty(UMLMetafacadeProperties.TABLE_NAME_PREFIX)));
        return EntityMetafacadeUtils
            .getSqlNameFromTaggedValue(
                tableNamePrefix,
                this,
                UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                this.getMaxSqlNameLength(),
                this
                    .getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getOperationCallFromAttributes(boolean)
     */
    protected String handleGetOperationCallFromAttributes(
        boolean withIdentifiers)
    {
        return this.getOperationCallFromAttributes(withIdentifiers, false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getOperationCallFromAttributes(boolean,
     *      boolean)
     */
    protected String handleGetOperationCallFromAttributes(
        boolean withIdentifiers,
        boolean follow)
    {
        StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");

        Collection attributes = this.getAttributes();

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (EntityFacade.class.isAssignableFrom(superClass.getClass()))
            {
                EntityFacade entity = (EntityFacade)superClass;
                attributes.addAll(entity.getAttributes());
            }
        }

        if (attributes != null && !attributes.isEmpty())
        {
            Iterator attributeIt = attributes.iterator();
            while (attributeIt.hasNext())
            {
                EntityAttributeFacade attribute = (EntityAttributeFacade)attributeIt
                    .next();
                if (withIdentifiers || !attribute.isIdentifier())
                {
                    buffer.append(separator);
                    String typeName = attribute.getType()
                        .getFullyQualifiedName();
                    buffer.append(typeName);
                    buffer.append(" ");
                    buffer.append(attribute.getName());
                    separator = ", ";
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getAttributeTypeList(boolean,
     *      boolean)
     */
    protected String handleGetAttributeTypeList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeTypeList(this.getAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributeNameList(boolean,
     *      boolean)
     */
    protected String handleGetAttributeNameList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeNameList(this.getAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getRequiredAttributeTypeList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredAttributeTypeList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeTypeList(this.getRequiredAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getRequiredAttributeNameList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredAttributeNameList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeNameList(this.getRequiredAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * Constructs a comma seperated list of attribute type names from the passed
     * in collection of <code>attributes</code>.
     * 
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute types.
     */
    private String getAttributeTypeList(Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(attributes, new Closure()
        {
            public void execute(Object object)
            {
                list.append(((AttributeFacade)object).getType()
                    .getFullyQualifiedName());
                list.append(comma);
            }
        });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * Constructs a comma seperated list of attribute names from the passed in
     * collection of <code>attributes</code>.
     * 
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute names.
     */
    private String getAttributeNameList(Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(attributes, new Closure()
        {
            public void execute(Object object)
            {
                list.append(((AttributeFacade)object).getName());
                list.append(comma);
            }
        });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#isChild()
     */
    protected boolean handleIsChild()
    {
        return CollectionUtils.find(this.getAssociationEnds(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd()
                    .isComposition();
            }
        }) != null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getParentEnd()
     */
    protected Object handleGetParentEnd()
    {
        Object parentEnd = null;
        AssociationEndFacade end = (AssociationEndFacade)CollectionUtils.find(
            this.getAssociationEnds(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd()
                        .isComposition();
                }
            });
        if (end != null)
        {
            parentEnd = end.getOtherEnd();
        }
        return parentEnd;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getChildren()
     */
    protected Collection handleGetChildEnds()
    {
        Collection childEnds = new FilteredCollection(this.getAssociationEnds())
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).isComposition();
            }
        };
        CollectionUtils.transform(childEnds, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        });
        return childEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getBusinessOperations()
     */
    protected Collection handleGetBusinessOperations()
    {
        final Collection businessOperations = this.getOperations();
        MetafacadeUtils.filterByNotType(
            businessOperations,
            EntityQueryOperationFacade.class);
        return businessOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getEntityReferences()
     */
    protected Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && EntityFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributes(boolean,
     *      boolean)
     */
    protected Collection handleGetAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow);
        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = true;
                if (!withIdentifiers
                    && EntityAttributeFacade.class.isAssignableFrom(object
                        .getClass()))
                {
                    valid = !((EntityAttributeFacade)object).isIdentifier();
                }
                return valid;
            }
        });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getRequiredAttributes(boolean,
     *      boolean)
     */
    protected Collection handleGetRequiredAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(
            follow,
            withIdentifiers);
        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                valid = ((AttributeFacade)object).isRequired();
                if (valid
                    && !withIdentifiers
                    && EntityAttributeFacade.class.isAssignableFrom(object
                        .getClass()))
                {
                    valid = !((EntityAttributeFacade)object).isIdentifier();
                }
                return valid;
            }
        });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getRequiredProperties(boolean,
     *      boolean)
     */
    protected Collection handleGetRequiredProperties(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection properties = this.getProperties();
        if (follow)
        {
            CollectionUtils.forAllDo(
                this.getAllGeneralizations(),
                new Closure()
                {
                    public void execute(Object object)
                    {
                        properties.addAll(((ClassifierFacade)object)
                            .getProperties());
                    }
                });
        }
        CollectionUtils.filter(properties, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                if (AttributeFacade.class.isAssignableFrom(object.getClass()))
                {
                    valid = ((AttributeFacade)object).isRequired();
                    if (valid
                        && !withIdentifiers
                        && EntityAttributeFacade.class.isAssignableFrom(object
                            .getClass()))
                    {
                        valid = !((EntityAttributeFacade)object).isIdentifier();
                    }
                }
                else if (AssociationEndFacade.class.isAssignableFrom(object
                    .getClass()))
                {
                    valid = ((AssociationEndFacade)object).isRequired();
                }
                return valid;
            }
        });
        return properties;
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    protected Short handleGetMaxSqlNameLength()
    {
        return Short
            .valueOf((String)this
                .getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * Returns true/false on whether or not default identifiers are allowed
     */
    private boolean isAllowDefaultIdentifiers()
    {
        return Boolean
            .valueOf(
                (String)this
                    .getConfiguredProperty(UMLMetafacadeProperties.ALLOW_DEFAULT_IDENTITIFIERS))
            .booleanValue();
    }

    /**
     * Gets the name of the default identifier.
     */
    private String getDefaultIdentifier()
    {
        return (String)this
            .getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER);
    }

    /**
     * Gets the name of the default identifier type.
     */
    private String getDefaultIdentifierType()
    {
        return (String)this
            .getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER_TYPE);
    }

    /**
     * Gets the default identifier visibility.
     */
    private String getDefaultIdentifierVisibility()
    {
        return (String)this
            .getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER_VISIBILITY);
    }

    /**
     * Checks to see if this entity has any associations where the foreign
     * identifier flag may be set, and if so creates and adds identifiers just
     * like the foreign entity to this entity.
     * 
     * @return true if any identifiers were added, false otherwise
     */
    private boolean checkForAndAddForeignIdentifiers()
    {
        boolean identifiersAdded = false;
        EntityAssociationEndFacade end = this.getForeignIdentifierEnd();
        if (end != null
            && EntityFacade.class.isAssignableFrom(end.getType().getClass()))
        {
            EntityFacade foreignEntity = (EntityFacade)end.getOtherEnd()
                .getType();
            Collection identifiers = EntityMetafacadeUtils.getIdentifiers(
                foreignEntity,
                true);
            for (Iterator identifierIterator = identifiers.iterator(); identifierIterator
                .hasNext();)
            {
                AttributeFacade identifier = (AttributeFacade)identifierIterator
                    .next();
                this.createIdentifier(identifier.getName(), identifier
                    .getType().getFullyQualifiedName(true), identifier
                    .getVisibility());
                identifiersAdded = true;
            }
        }
        return identifiersAdded;
    }

    /**
     * Override to filter out any association ends that point to model elements
     * other than other entities.
     * 
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    public Collection handleGetAssociationEnds()
    {
        final Collection associationEnds = this.shieldedElements(super
            .handleGetAssociationEnds());
        CollectionUtils.filter(associationEnds, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd().getType() instanceof EntityFacade;
            }
        });
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml14.EntityFacadeLogic#handleIsUsingForeignIdentifier()
     */
    protected boolean handleIsUsingForeignIdentifier()
    {
        return this.getForeignIdentifierEnd() != null;
    }

    /**
     * Gets the association end that is flagged as having the foreign identifier
     * set (or null if none is).
     */
    private EntityAssociationEndFacade getForeignIdentifierEnd()
    {
        return (EntityAssociationEndFacade)CollectionUtils.find(this
            .getAssociationEnds(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                if (object != null
                    && EntityAssociationEndFacade.class.isAssignableFrom(object
                        .getClass()))
                {
                    EntityAssociationEndFacade end = (EntityAssociationEndFacade)object;
                    valid = end.isForeignIdentifier();
                }
                return valid;
            }
        });
    }
}