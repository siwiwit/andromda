package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Filters;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.util.UML2Resource;


/**
 * Model access facade implementation for the EMF/UML2 metafacades
 *
 * @author Steve Jerman
 * @author Chad Brandon
 */
public class UMLModelAccessFacade
    implements ModelAccessFacade
{
    /**
     * Protected to improve performance.
     */
    protected Filters modelPackages = new Filters();

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(java.lang.Object)
     */
    public void setModel(final Object model)
    {
        ExceptionUtils.checkNull(
            "model",
            model);
        ExceptionUtils.checkAssignable(
            UML2Resource.class,
            "modelElement",
            model.getClass());
        this.model = (UML2Resource)model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModel()
     */
    public Object getModel()
    {
        return this.model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(java.lang.Object)
     */
    public String getName(final Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getName();
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(final Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        final ModelElementFacade modelElementFacade = (ModelElementFacade)modelElement;
        final StringBuffer packageName = new StringBuffer(modelElementFacade.getPackageName(true));

        // - if the model element is a package then the package name will be the
        // name
        // of the package with its package name
        if (modelElement instanceof PackageFacade)
        {
            final String name = modelElementFacade.getName();
            if (StringUtils.isNotBlank(name))
            {
                packageName.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
                packageName.append(name);
            }
        }
        return packageName.toString();
    }

    /**
     * The actual underlying model instance.
     */
    private UML2Resource model;

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setPackageFilter(org.andromda.core.configuration.Filters)
     */
    public void setPackageFilter(final Filters modelPackages)
    {
        this.modelPackages = modelPackages;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(final Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);

        Collection stereotypeNames = Collections.EMPTY_LIST;
        if (modelElement instanceof Element)
        {
            Element element = (Element)modelElement;
            stereotypeNames = UmlUtilities.getStereotypeNames(element);
        }
        else if (modelElement instanceof ModelElementFacade)
        {
            stereotypeNames = ((ModelElementFacade)modelElement).getStereotypeNames();
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(java.lang.String)
     */
    public Collection findByStereotype(final String name)
    {
        final ArrayList elements = new ArrayList();
        for (TreeIterator iterator = UmlUtilities.findModel(this.model).eAllContents(); iterator.hasNext();)
        {
            final EObject object = (EObject)iterator.next();
            if (object instanceof NamedElement)
            {
                final NamedElement element = (NamedElement)object;
                if (UmlUtilities.containsStereotype(
                        element,
                        name))
                {
                    elements.add(MetafacadeFactory.getInstance().createMetafacade(element));
                }
            }
        }
        return elements;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModelElements()
     */
    public Collection getModelElements()
    {
        final ArrayList elements = new ArrayList();

        for (final Iterator iterator = UmlUtilities.findModel(this.model).eAllContents(); iterator.hasNext();)
        {
            final EObject object = (EObject)iterator.next();
            if (object instanceof NamedElement)
            {
                elements.add(object);
            }
        }

        // Don't forget to transform properties
        CollectionUtils.transform(elements, UmlUtilities.ELEMENT_TRANSFORMER);

        final Collection metafacades;

        if (elements.isEmpty())
        {
            metafacades = Collections.EMPTY_LIST;
        }
        else
        {
            metafacades = MetafacadeFactory.getInstance().createMetafacades(elements);
            this.filterMetafacades(metafacades);
        }

        return metafacades;
    }

    /**
     * Filters out those metafacades which <strong>should </strong> be
     * processed.
     *
     * @param metafacades the Collection of metafacades.
     */
    private void filterMetafacades(final Collection metafacades)
    {
        if (this.modelPackages != null && !this.modelPackages.isEmpty())
        {
            CollectionUtils.filter(
                metafacades,
                new Predicate()
                {
                    public boolean evaluate(final Object metafacade)
                    {
                        boolean valid = false;
                        if (metafacade instanceof MetafacadeBase)
                        {
                            final ModelElementFacade modelElementFacade = (ModelElementFacade)metafacade;
                            final StringBuffer packageName = new StringBuffer(modelElementFacade.getPackageName(true));

                            // - if the model element is a package then the package
                            // name will be the name
                            // of the package with its package name
                            if (metafacade instanceof PackageFacade)
                            {
                                final String name = modelElementFacade.getName();
                                if (StringUtils.isNotBlank(name))
                                {
                                    packageName.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
                                    packageName.append(name);
                                }
                            }
                            valid = UMLModelAccessFacade.this.modelPackages.isApply(packageName.toString());
                        }
                        return valid;
                    }
                });
        }
    }
}