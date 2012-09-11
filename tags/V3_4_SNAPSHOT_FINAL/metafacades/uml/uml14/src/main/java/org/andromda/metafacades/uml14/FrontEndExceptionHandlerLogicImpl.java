package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndExceptionHandler.
 *
 * @see org.andromda.metafacades.uml.FrontEndExceptionHandler
 * @author Bob Fields
 */
public class FrontEndExceptionHandlerLogicImpl
    extends FrontEndExceptionHandlerLogic
{
    private static final long serialVersionUID = -4149131092786433784L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndExceptionHandlerLogicImpl(
        Object metaObject,
        String context)
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