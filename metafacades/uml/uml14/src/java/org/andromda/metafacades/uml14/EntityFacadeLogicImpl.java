package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;
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
    implements org.andromda.metafacades.uml.EntityFacade
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
        // if there are no identfiers on this entity,
        // create and add one.
        if (!this.hasIdentifiers() && this.isAllowDefaultIdentifiers())
        {
            this.createIdentifier();
        }
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders
     */
    public java.util.Collection handleGetFinders()
    {
        return this.getFinders(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders(boolean)
     */
    public java.util.Collection handleGetFinders(boolean follow)
    {
        Collection finders = this.getOperations();

        MetafacadeUtils.filterByStereotype(
            finders,
            UMLProfile.STEREOTYPE_FINDER_METHOD);

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                EntityFacade entity = (EntityFacade)superClass;
                finders.addAll(entity.getFinders(follow));
            }
        }
        return finders;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers()
     */
    public java.util.Collection handleGetIdentifiers()
    {
        return this.getIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers(boolean)
     */
    public java.util.Collection handleGetIdentifiers(boolean follow)
    {
        Collection identifiers = this.getAttributes();
        MetafacadeUtils.filterByStereotype(
            identifiers,
            UMLProfile.STEREOTYPE_IDENTIFIER);

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && identifiers.isEmpty() && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                EntityFacade entity = (EntityFacade)superClass;
                identifiers.addAll(entity.getIdentifiers(follow));
            }
        }
        return identifiers;
    }

    /**
     * Creates an identifier from the default identifier properties specified
     * within a namespace.
     */
    private void createIdentifier()
    {
        Attribute identifier = UMLMetafacadeUtils.createAttribute(this
            .getDefaultIdentifier(), this.getDefaultIdentifierType(), this
            .getDefaultIdentifierVisibility());

        identifier.getStereotype().add(
            UMLMetafacadeUtils
                .findOrCreateStereotype(UMLProfile.STEREOTYPE_IDENTIFIER));

        ((Classifier)this.metaObject).getFeature().add(identifier);
    }

    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityFacade#hasIdentifiers()
     */
    public boolean handleHasIdentifiers()
    {
        Collection identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getTableName()
     */
    public String handleGetTableName()
    {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
            this.getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributesAsList(boolean,
     *      boolean)
     */
    public String handleGetAttributesAsList(
        boolean withTypeNames,
        boolean withIdentifiers)
    {
        return this.getAttributesAsList(withTypeNames, withIdentifiers, false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributesAsList(boolean,
     *      boolean, boolean)
     */
    public String handleGetAttributesAsList(
        boolean withTypeNames,
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
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
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
                    if (withTypeNames)
                    {
                        String typeName = attribute.getType()
                            .getFullyQualifiedName();
                        buffer.append(typeName);
                        buffer.append(" ");
                        buffer.append(attribute.getName());
                    }
                    else
                    {
                        buffer.append(attribute.getGetterName());
                        buffer.append("()");
                    }
                    separator = ", ";
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#isChild()
     */
    public boolean handleIsChild()
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
     * @see org.andromda.metafacades.uml.EntityFacade#getParent()
     */
    public Object handleGetParent()
    {
        Object parent = null;
        AssociationEndFacade parentEnd = (AssociationEndFacade)CollectionUtils
            .find(this.getAssociationEnds(), new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd()
                        .isComposition();
                }
            });
        if (parentEnd != null)
        {
            parent = parentEnd.getOtherEnd().getType();
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getChildren()
     */
    public Collection handleGetChildren()
    {
        Collection parentEnds = new FilteredCollection(this
            .getAssociationEnds())
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).isComposition();
            }
        };
        CollectionUtils.transform(parentEnds, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        });
        return parentEnds;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getBusinessOperations()
     */
    public Collection handleGetBusinessOperations() 
    {
        return new FilteredCollection(this.getOperations()) 
        {
            public boolean evaluate(Object object)
            {
                return !((ModelElementFacade)object).hasStereotype(
                    UMLProfile.STEREOTYPE_FINDER_METHOD);
            }
        };
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getEntityReferences()
     */
    public Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getDependencies())
        {
            public boolean evaluate(Object object)
            {
                return ((DependencyFacade)object)
                    .hasStereotype(UMLProfile.STEREOTYPE_ENTITY_REF);
            }
        };
    }

    /**
     * SQL type specific mappings property reference.
     */
    private final static String MAX_SQL_NAME_LENGTH = "maxSqlNameLength";

    /**
     * Sets the maximum lenght to which a persistent SQL name may be.
     * 
     * @param maxSqlNameLength the maximum length a SQL name may be.
     */
    public void setMaxSqlNameLength(Short maxSqlNameLength)
    {
        this.registerConfiguredProperty(MAX_SQL_NAME_LENGTH, maxSqlNameLength);
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    public Short handleGetMaxSqlNameLength()
    {
        return (Short)this.getConfiguredProperty(MAX_SQL_NAME_LENGTH);
    }

    /**
     * Name of the allow default identifiers property.
     */
    private final static String ALLOW_DEFAULT_IDENTIFIERS = "allowDefaultIdentifiers";

    /**
     * Sets whether or not to allow default identifier creation.
     * 
     * @param allowDefaultIdentifiers true/false on whether to allow default
     *        identifiers.
     */
    public void setAllowDefaultIdentifiers(String allowDefaultIdentifiers)
    {
        this.registerConfiguredProperty(ALLOW_DEFAULT_IDENTIFIERS, Boolean
            .valueOf(StringUtils.trimToEmpty(allowDefaultIdentifiers)));
    }

    /**
     * Returns true/false on whether or not default identifiers are allowed
     */
    private boolean isAllowDefaultIdentifiers()
    {
        return ((Boolean)this.getConfiguredProperty(ALLOW_DEFAULT_IDENTIFIERS))
            .booleanValue();
    }

    /**
     * The default identifier name.
     */
    private final static String DEFAULT_IDENTIFIER = "defaultIdentifier";

    /**
     * Sets the name to give a default identifier.
     * 
     * @param defaultIdentifier the default identifier.
     */
    public void setDefaultIdentifier(String defaultIdentifier)
    {
        this.registerConfiguredProperty(DEFAULT_IDENTIFIER, defaultIdentifier);
    }

    /**
     * Gets the name of the default identifier.
     */
    public String getDefaultIdentifier()
    {
        return (String)this.getConfiguredProperty(DEFAULT_IDENTIFIER);
    }

    /**
     * The default identifier type.
     */
    private final static String DEFAULT_IDENTIFIER_TYPE = "defaultIdentifierType";

    /**
     * Sets the default identifier type name.
     * 
     * @param defaultIdentifierType the default identifier type.
     */
    public void setDefaultIdentifierType(String defaultIdentifierType)
    {
        this.registerConfiguredProperty(
            DEFAULT_IDENTIFIER_TYPE,
            defaultIdentifierType);
    }

    /**
     * Gets the name of the default identifier type.
     */
    private String getDefaultIdentifierType()
    {
        return (String)this.getConfiguredProperty(DEFAULT_IDENTIFIER_TYPE);
    }

    /**
     * The default identifier visibility.
     */
    private final static String DEFAULT_IDENTIFIER_VISIBILITY = "defaultIdentifierVisibility";

    /**
     * Sets the default identifier visibility.
     * 
     * @param defaultIdentifierVisibility the default identifier visibility.
     */
    public void setDefaultIdentifierVisibility(
        String defaultIdentifierVisibility)
    {
        this.registerConfiguredProperty(
            DEFAULT_IDENTIFIER_VISIBILITY,
            defaultIdentifierVisibility);
    }

    /**
     * Gets the default identifier visibility.
     */
    private String getDefaultIdentifierVisibility()
    {
        return (String)this
            .getConfiguredProperty(DEFAULT_IDENTIFIER_VISIBILITY);
    }
}