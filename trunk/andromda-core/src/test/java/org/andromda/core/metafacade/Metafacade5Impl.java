package org.andromda.core.metafacade;


/**
 * Fake metafacade number 5 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade5Impl
    extends MetafacadeBase
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade5Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return false
     */
    public boolean isProperty()
    {
        return false;
    }
}