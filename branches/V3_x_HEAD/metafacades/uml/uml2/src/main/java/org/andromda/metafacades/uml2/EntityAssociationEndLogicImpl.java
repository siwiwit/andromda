package org.andromda.metafacades.uml2;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.EntityAssociationEnd
 */
public class EntityAssociationEndLogicImpl
    extends EntityAssociationEndLogic
{

    public EntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnName()
     */
    protected java.lang.String handleGetColumnName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeySuffix()
     */
    protected java.lang.String handleGetForeignKeySuffix()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isForeignIdentifier()
     */
    protected boolean handleIsForeignIdentifier()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyConstraintName()
     */
    protected java.lang.String handleGetForeignKeyConstraintName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnIndex()
     */
    protected java.lang.String handleGetColumnIndex()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getSqlType()
     */
    protected java.lang.String handleGetSqlType()
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
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isIdentifiersPresent()
     */
    protected boolean handleIsIdentifiersPresent() {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }
}