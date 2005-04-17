package org.andromda.cartridges.spring.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntity.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity
 */
public class SpringManageableEntityLogicImpl
    extends SpringManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    public SpringManageableEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.lang.String handleGetCrudDaoName()
    {
        return getName() + "CrudDao";
    }

    protected java.lang.String handleGetFullyQualifiedCrudDaoName()
    {
        return getCrudPackageName() + '.' + getCrudDaoName();
    }

    protected String handleGetCrudDaoFullPath()
    {
        return getFullyQualifiedCrudDaoName().replace('.', '/');
    }

    protected String handleGetCrudDaoBaseName()
    {
        return getCrudDaoName() + "Base";
    }

    protected String handleGetFullyQualifiedCrudDaoBaseName()
    {
        return getCrudPackageName() + '.' + getCrudDaoBaseName();
    }

    protected String handleGetCrudDaoBaseFullPath()
    {
        return getFullyQualifiedCrudDaoBaseName().replace('.', '/');
    }

    protected String handleGetCrudServiceBaseName()
    {
        return getCrudServiceName() + "Base";
    }

    protected String handleGetFullyQualifiedCrudServiceBaseName()
    {
        return getCrudPackageName() + '.' + getCrudServiceBaseName();
    }

    protected String handleGetCrudServiceBaseFullPath()
    {
        return getFullyQualifiedCrudServiceBaseName().replace('.', '/');
    }
}