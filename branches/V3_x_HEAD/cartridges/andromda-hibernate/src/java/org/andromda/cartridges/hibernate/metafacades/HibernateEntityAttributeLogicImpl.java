package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.andromda.cartridges.hibernate.HibernateUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
{
    // ---------------- constructor -------------------------------
    public HibernateEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof HibernateEntity)
        {
            HibernateEntity entity = (HibernateEntity)this.getOwner();
            if (entity.isHibernateInheritanceClass() && entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }

    /**
     * Override to provide java specific handling of the default value.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String fullyQualifiedName = StringUtils.trimToEmpty(type.getFullyQualifiedName());
            if ("java.lang.String".equals(fullyQualifiedName))
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ")";
            }
        }
        return defaultValue;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isContainsEmbeddedObject()
     */
    protected boolean handleIsContainsEmbeddedObject()
    {
        boolean returnValue = false;
        if (this.getType() instanceof HibernateEmbeddedValue)
        {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#concatColumnName(java.lang.String,
     *      java.lang.String)
     */
    protected String handleConcatColumnName(
        java.lang.String prefix,
        java.lang.String name)
    {
        String returnValue = name;
        if (StringUtils.isNotBlank(prefix))
        {
            returnValue = prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name;

            // handle maxSqlNameLength
            Short maxSqlNameLength =
                Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
            returnValue = EntityMetafacadeUtils.ensureMaximumNameLength(returnValue, maxSqlNameLength);
        }
        return returnValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isLazy()
     */
    protected boolean handleIsLazy()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : false;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#getFormula()
     */
    protected String handleGetFormula()
    {
        return (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_FORMULA);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isInsertEnabled()
     */
    protected boolean handleIsInsertEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isUpdateEnabled()
     */
    protected boolean handleIsUpdateEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    private boolean isXmlPersistenceActive()
    {
       return HibernateUtils.isXmlPersistenceActive((String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION),
                                                    (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE));
    }

    
    private boolean persistIDAsAttribute() 
    {
        boolean persistAsAttribute = true;
        String prop = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE_ID_AS_ATTRIBUTE);
        if (prop != null && prop.equalsIgnoreCase("false"))
            persistAsAttribute = false;
        
        return persistAsAttribute;
    }
    
    
    protected String handleGetXmlTagName() 
    {
        String tagName = null;
        
        if (isXmlPersistenceActive())
        {
            tagName = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_XML_TAG_NAME);

            if (tagName == null)
            {
                if (this.isIdentifier() && this.persistIDAsAttribute())
                    tagName = "@" + this.getName();
                else
                    tagName = this.getName();
            }

        }
        return (StringUtils.isBlank(tagName)) ? null : tagName;
    }
    
}