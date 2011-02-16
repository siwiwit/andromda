package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.uml2.uml.AssociationClass;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for AssociationClassFacadeLogicImpl
     * @param metaObject
     * @param context
     * @see org.andromda.metafacades.uml.AssociationClassFacade
     */
    public AssociationClassFacadeLogicImpl(
        final AssociationClass metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationClassFacade#getConnectionAssociationEnds()
     */
    @Override
    protected Collection<AssociationEndFacade> handleGetConnectionAssociationEnds()
    {
        return CollectionUtils.collect(
                this.metaObject.getMemberEnds(),
                UmlUtilities.ELEMENT_TRANSFORMER);
    }
}
