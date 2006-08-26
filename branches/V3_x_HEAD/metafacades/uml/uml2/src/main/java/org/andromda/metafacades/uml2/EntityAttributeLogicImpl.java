package org.andromda.metafacades.uml2;

import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EntityAttribute.
 *
 * @see org.andromda.metafacades.uml.EntityAttribute
 */
public class EntityAttributeLogicImpl
    extends EntityAttributeLogic
{

    public EntityAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    protected java.lang.String handleGetColumnLength()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    protected java.lang.String handleGetColumnName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetJdbcMappings()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcType()
     */
    protected java.lang.String handleGetJdbcType()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetSqlMappings()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlType()
     */
    protected java.lang.String handleGetSqlType()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#isIdentifier()
     */
    protected boolean handleIsIdentifier()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#isUnique()
     */
    protected boolean handleIsUnique()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnIndex()
     */
    protected java.lang.String handleGetColumnIndex()
    {
        // TODO: put your implementation here.
        return null;
    }

    protected boolean handleIsTransient()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getUniqueGroup()
     */
    protected String handleGetUniqueGroup() {
        final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE_GROUP);
        return group != null ? StringUtils.trimToEmpty(group) : null;
    }
}