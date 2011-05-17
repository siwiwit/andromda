package org.andromda.cartridges.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ModelElementFacade;

/**
 * Transform class for the EJB cartridge.
 *
 * @author Richard Kunze
 * @author Chad Brandon
 */
public class EJBScriptHelper
{
    /**
     * Create a comma separated list of attributes.
     * <p>
     * This method can be used to generated e.g. argument lists for constructors, method calls etc.
     *
     * @param attributes   a collection of {@link AttributeFacade} objects
     * @param includeTypes if <code>true</code>, the type names of the attributes are included.
     * @param includeNames if <code>true</code>, the names of the attributes are included
     * @return attributes
     */
    public String getAttributesAsList(Collection attributes, boolean includeTypes, boolean includeNames)
    {
        if (!includeNames && !includeTypes || attributes == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (final Iterator it = attributes.iterator(); it.hasNext();)
        {
            AttributeFacade attr = (AttributeFacade)it.next();
            sb.append(separator);
            separator = ", ";
            if (includeTypes)
            {
                sb.append(attr.getType().getFullyQualifiedName());
                sb.append(' ');
            }
            if (includeNames)
            {
                sb.append(attr.getName());
            }
        }
        return sb.toString();
    }

    /**
     * Filter a list of model elements by visibility.
     *
     * @param list       the original list
     * @param visibility the visibility - "public" "protected", "private" or the empty string (for package visibility)
     * @return a list with all elements from the original list that have a matching visibility.
     */
    public Collection filterByVisibility(Collection list, String visibility)
    {
        Collection<ModelElementFacade> result = new ArrayList<ModelElementFacade>(list.size());
        for (ModelElementFacade elem : (Iterable<ModelElementFacade>) list)
        {
            if (visibility.equals(elem.getVisibility()))
            {
                result.add(elem);
            }
        }
        return result;
    }

}
