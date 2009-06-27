package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityAttribute.
 *
 * @see org.andromda.metafacades.uml.EntityAttribute
 */
public class EntityAttributeLogicImpl
    extends EntityAttributeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EntityAttributeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EntityAttributeLogicImpl.class);

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENTITY_PROPERTY_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    @Override
    protected String handleGetColumnLength()
    {
        return (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    @Override
    protected String handleGetColumnName()
    {
        final Short maxSqlNameLength =
            Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
        final String columnNamePrefix =
            this.isConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)
            ? ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)) : null;
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            columnNamePrefix,
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
            maxSqlNameLength,
            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcMappings()
     */
    @Override
    protected org.andromda.metafacades.uml.TypeMappings handleGetJdbcMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.JDBC_MAPPINGS_URI);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcType()
     */
    @Override
    protected String handleGetJdbcType()
    {
        String value = null;
        if (this.getJdbcMappings() != null)
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                final String typeName = type.getFullyQualifiedName(true);
                value = this.getJdbcMappings().getTo(typeName);
            }
        }

        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlMappings()
     */
    @Override
    protected org.andromda.metafacades.uml.TypeMappings handleGetSqlMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlType()
     */
    @Override
    protected String handleGetSqlType()
    {
        String value = null;
        if (this.getSqlMappings() != null)
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                String typeName = type.getFullyQualifiedName(true);

                // if its an enumeration, the sql type is the literal type
                if (type.isEnumeration())
                {
                    ClassifierFacade literalType = ((EnumerationFacade)type).getLiteralType();
                    if (literalType != null)
                    {
                        typeName = literalType.getFullyQualifiedName(true);
                    }
                }
                value = this.getSqlMappings().getTo(typeName);
                final String columnLength = this.getColumnLength();
                if (StringUtils.isNotEmpty(columnLength))
                {
                    value = EntityMetafacadeUtils.constructSqlTypeName(
                            value,
                            columnLength);
                }
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#isIdentifier()
     */
    @Override
    protected boolean handleIsIdentifier()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }

    /* super.isUnique() causes StackOverflowError
     * @see org.andromda.metafacades.uml.EntityAttribute#isUnique()
    @Override
    protected boolean handleIsUnique()
    {
        return super.isUnique() || this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }
    */

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnIndex()
     */
    // TODO: Duplicated method warning from ancestor
    @Override
    protected String handleGetColumnIndex()
    {
        final String index = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

    /**
     * Gets a Mappings instance from a property registered under the given
     * <code>propertyName</code>.
     *
     * @param propertyName
     *            the property name to register under.
     * @return the Mappings instance.
     */
    private TypeMappings getMappingsProperty(final String propertyName)
    {
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        if (property instanceof String)
        {
            String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                EntityAttributeLogicImpl.logger.error(
                    errMsg,
                    th);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    @Override
    protected boolean handleIsTransient()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_TRANSIENT);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getUniqueGroup()
     */
    @Override
    protected String handleGetUniqueGroup() {
        final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE_GROUP);
        return group != null ? StringUtils.trimToEmpty(group) : null;
    }
}
