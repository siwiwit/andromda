package org.andromda.core.metafacade;

/**
 * Fake metafacade number 2 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade1Impl
    extends MetafacadeBase
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade1Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
}
