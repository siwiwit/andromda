package org.andromda.cartridges.meta;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities for the AndroMDA meta cartridge.
 *
 * @author Chad Brandon
 */
public class MetaCartridgeUtils
{
    /**
     * Sorts model elements by their fully qualified name.
     *
     * @param modelElements the collection of model elements to sort.
     * @return the sorted collection.
     */
    public static Collection<ModelElementFacade> sortByFullyQualifiedName(Collection<ModelElementFacade> modelElements)
    {
        List<ModelElementFacade> sortedElements = null;
        if (modelElements != null)
        {
            sortedElements = new ArrayList<ModelElementFacade>(modelElements);
            Collections.sort(
                sortedElements,
                new FullyQualifiedNameComparator());
        }
        return sortedElements;
    }

    /**
     * Used to sort operations by <code>fullyQualifiedName</code>.
     */
    final static class FullyQualifiedNameComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        /**
         *
         */
        FullyQualifiedNameComparator()
        {
            this.collator.setStrength(Collator.PRIMARY);
        }

        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(
            final Object objectA,
            final Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade b = (ModelElementFacade)objectB;

            return this.collator.compare(
                a.getFullyQualifiedName(),
                b.getFullyQualifiedName());
        }
    }

    /**
     * Retrieves the fully qualified constraint name given the constraint (this includes the
     * full name of the context element and the constraint to which it applies).
     *
     * @param constraint the constraint of which to retrieve the name.
     * @return the fully qualified name.
     */
    public static String getFullyQualifiedConstraintName(final ConstraintFacade constraint)
    {
        final StringBuilder name = new StringBuilder();
        if (constraint != null)
        {
            final ModelElementFacade contextElement = constraint.getContextElement();
            final String contextElementName =
                contextElement != null ? contextElement.getFullyQualifiedName(true) : null;
            if (StringUtils.isNotBlank(contextElementName))
            {
                name.append(contextElementName.trim());
                name.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
            }
            name.append(constraint.getName());
        }
        return name.toString();
    }
}