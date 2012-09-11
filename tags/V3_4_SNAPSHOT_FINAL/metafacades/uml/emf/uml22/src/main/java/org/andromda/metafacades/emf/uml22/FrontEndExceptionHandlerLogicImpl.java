package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndExceptionHandler.
 *
 * @see org.andromda.metafacades.uml.FrontEndExceptionHandler
 */
public class FrontEndExceptionHandlerLogicImpl
    extends FrontEndExceptionHandlerLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndExceptionHandlerLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndExceptionHandler#isFrontEndException()
     */
    @Override
    protected boolean handleIsFrontEndException()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_EXCEPTION);
    }
}
