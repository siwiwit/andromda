package org.andromda.metafacades.emf.uml2;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FinalStateFacade.
 *
 * @see org.andromda.metafacades.uml.FinalStateFacade
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public FinalStateFacadeLogicImpl(
        final org.eclipse.uml2.FinalState metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}