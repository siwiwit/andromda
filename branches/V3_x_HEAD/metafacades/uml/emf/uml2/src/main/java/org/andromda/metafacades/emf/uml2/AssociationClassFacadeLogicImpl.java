package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    public AssociationClassFacadeLogicImpl(
        final org.eclipse.uml2.AssociationClass metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationClassFacade#getConnectionAssociationEnds()
     */
    protected java.util.Collection handleGetConnectionAssociationEnds()
    {
        // TODO: This has to be tested.
        // TODO: It may need to be transformed like other properties.
        return this.metaObject.getMemberEnds();
    }
}