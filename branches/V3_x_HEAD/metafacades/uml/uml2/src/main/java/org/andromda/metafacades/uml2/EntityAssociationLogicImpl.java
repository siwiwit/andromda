package org.andromda.metafacades.uml2;



/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EntityAssociation.
 *
 * @see org.andromda.metafacades.uml.EntityAssociation
 */
public class EntityAssociationLogicImpl
    extends EntityAssociationLogic
{

    public EntityAssociationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getTableName()
     */
    protected java.lang.String handleGetTableName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getSchema()
     */
    protected String handleGetSchema()
    {
        // TODO: put your implementation here.
        return null;
    }

    protected boolean handleIsEntityAssociation() {
        // TODO Auto-generated method stub
        return false;
    }
}