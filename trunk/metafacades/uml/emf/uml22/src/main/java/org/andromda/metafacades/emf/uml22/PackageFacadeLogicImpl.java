package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.internal.impl.BehaviorImpl;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.ComponentImpl;
import org.eclipse.uml2.uml.internal.impl.NodeImpl;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.PackageFacade.
 *
 * @see org.andromda.metafacades.uml.PackageFacade
 * @author Bob Fields
 */
public class PackageFacadeLogicImpl
    extends PackageFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public PackageFacadeLogicImpl(
        final Package metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(PackageFacadeLogicImpl.class);

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#findModelElement(String)
     */
    @Override
    protected ModelElementFacade handleFindModelElement(
        final String fullyQualifiedName)
    {
        Object modelElement = null;
        if (PackageFacadeLogicImpl.logger.isDebugEnabled())
        {
            PackageFacadeLogicImpl.logger.debug("Looking for >> " + fullyQualifiedName);
        }
        modelElement =
            UmlUtilities.findByFullyQualifiedName(
                this.metaObject.eResource().getResourceSet(),
                fullyQualifiedName,
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
                true);
        if (modelElement==null)
        {
            // Try again with fullyQualified PSM name, this may come from a taggedValue or some freeform entry.
            modelElement =
                UmlUtilities.findByFullyQualifiedName(
                    this.metaObject.eResource().getResourceSet(),
                    fullyQualifiedName,
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
                    false);
        }
        if (PackageFacadeLogicImpl.logger.isDebugEnabled())
        {
            PackageFacadeLogicImpl.logger.debug("Found: '" + modelElement + '\'');
        }
        return (ModelElementFacade)this.shieldedElement(modelElement);
    }

    /**
     * Returns Andromda class types, not necessarily all UML2 Class/Classifier type objects.
     * Returns Classes, not UseCases, Activities, Behaviors, Components, Nodes.
     * @see org.andromda.metafacades.uml.PackageFacade#getClasses()
     */
    @Override
    protected Collection<Class> handleGetClasses()
    {
        final List<Class> classes = new ArrayList<Class>();
        for (Element element : this.metaObject.getOwnedElements())
        {
            // Behavior Activity is mapped to EventFacade, unlike UML2 it does not inherit from Class so it must be excluded.
            // TODO Fix inheritance hierarchy of EventFacade
            if ((element instanceof ClassImpl)
                && !(element instanceof BehaviorImpl || element instanceof ComponentImpl || element instanceof NodeImpl || element instanceof StereotypeImpl))
            {
                classes.add((Class)element);
            }
        }
        return classes;
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getSubPackages()
     */
    @Override
    protected Collection<Package> handleGetSubPackages()
    {
        return this.metaObject.getNestedPackages();
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getModelElements()
     */
    @Override
    protected Collection<? extends ModelElementFacade> handleGetModelElements()
    {
        return CollectionUtils.collect(UmlUtilities.findModel(this.metaObject)
                .allOwnedElements(), UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getOwnedElements()
     */
    @Override
    protected Collection<? extends ModelElementFacade> handleGetOwnedElements()
    {
        return CollectionUtils.collect(
            this.metaObject.getOwnedMembers(),
            UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.PackageFacadeLogic#handleGetTablePrefix()
     */
    @Override
    protected String handleGetTablePrefix()
    {
        // TODO Auto-generated method stub
        return "";
    }
}
