package org.andromda.cartridges.hibernate.metafacades;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument
 */
public class HibernateFinderMethodArgumentLogicImpl
    extends HibernateFinderMethodArgumentLogic
{
    // ---------------- constructor -------------------------------

    public HibernateFinderMethodArgumentLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument#getQueryArgumentNameSetter()
     */
    protected java.lang.String handleGetQueryArgumentNameSetter()
    {
        StringBuffer setterName = new StringBuffer("setParameter");
        if (this.getType().isCollectionType())
        {
            setterName.append("List");
        }
        return setterName.toString();
    }
}