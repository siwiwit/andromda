package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.ManageableEntityAttribute;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ManageableEntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd
 */
public class ManageableEntityAssociationEndLogicImpl
    extends ManageableEntityAssociationEndLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityAssociationEndLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableIdentifier()
     */
    @Override
    protected ManageableEntityAttribute handleGetManageableIdentifier()
    {
        ManageableEntityAttribute manageableIdentifier = null;

        final ClassifierFacade classifierFacade = this.getType();
        if (classifierFacade instanceof ManageableEntity)
        {
            final ManageableEntity entity = (ManageableEntity)classifierFacade;
            manageableIdentifier = entity.getManageableIdentifier();
        }

        return manageableIdentifier;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#isDisplay()
     */
    @Override
    protected boolean handleIsDisplay()
    {
        // we always display association ends
        return true;
    }
}
