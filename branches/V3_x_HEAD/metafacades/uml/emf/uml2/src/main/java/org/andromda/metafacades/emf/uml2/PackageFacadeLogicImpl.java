package org.andromda.metafacades.emf.uml2;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.PackageFacade.
 *
 * @see org.andromda.metafacades.uml.PackageFacade
 */
public class PackageFacadeLogicImpl
    extends PackageFacadeLogic
{
    public PackageFacadeLogicImpl(
        final org.eclipse.uml2.Package metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#findModelElement(java.lang.String)
     */
    protected org.andromda.metafacades.uml.ModelElementFacade handleFindModelElement(
        final java.lang.String fullyQualifiedName)
    {
        Object modelElement = null;
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Looking for >> " + fullyQualifiedName);
        }
        modelElement =
            UmlUtilities.findByFullyQualifiedName(
                this.metaObject.eResource().getResourceSet(),
                fullyQualifiedName,
                MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                true);
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Found: '" + modelElement + "'");
        }
        return (ModelElementFacade)this.shieldedElement(modelElement);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getClasses()
     */
    protected java.util.Collection handleGetClasses()
    {
        return new FilteredCollection(this.metaObject.getOwnedElements())
            {
                public boolean evaluate(final Object object)
                {
                    return object instanceof org.eclipse.uml2.Class;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getSubPackages()
     */
    protected java.util.Collection handleGetSubPackages()
    {
        return this.metaObject.getNestedPackages();
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getModelElements()
     */
    protected java.util.Collection handleGetModelElements()
    {
        return CollectionUtils.transformedCollection(
            this.metaObject.getModel().getOwnedMembers(),
            UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getOwnedElements()
     */
    protected java.util.Collection handleGetOwnedElements()
    {
        return CollectionUtils.transformedCollection(
            this.metaObject.getOwnedMembers(),
            UmlUtilities.ELEMENT_TRANSFORMER);
    }
}