package org.andromda.metafacades.emf.uml2;

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
     * @return hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_EXCEPTION)
     * @see org.andromda.metafacades.uml.FrontEndExceptionHandler#isFrontEndException()
     */
    protected boolean handleIsFrontEndException()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_EXCEPTION);
    }
}