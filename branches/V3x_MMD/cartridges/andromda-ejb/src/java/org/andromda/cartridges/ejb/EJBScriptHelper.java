package org.andromda.cartridges.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ModelElementDecorator;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 * Transform class for the EJB cartridge.
 * @author Richard Kunze
 */
public class EJBScriptHelper {

    /** Create a comma seperated list of attributes.
     * This method can be used to generated e.g. argument lists for 
     * constructors, method calls etc.
     * @param attributes a collection of {@link Attribute} objects
     * @param includeTypes if <code>true</code>, the type names of 
     * the attributes are included.
     * @param includeNames if <code>true</code>, the 
     * names of the attributes are included
     * 
     * @author richard
     */
    public String getAttributesAsList(Collection attributes,
    		boolean includeTypes,
			boolean includeNames) {
    	if (!includeNames && !includeTypes || attributes == null) {
    		return "";
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	String separator = "";

    	for (Iterator it = attributes.iterator(); it.hasNext();) {
    		AttributeDecorator attr = (AttributeDecorator)it.next();
    		sb.append(separator);
    		separator = ", ";
    		if (includeTypes) {
    			sb.append(attr.getType().getFullyQualifiedName());
    			sb.append(" ");
    		}
    		if (includeNames) {
    			sb.append(attr.getName());
    		}
    	}
    	return sb.toString();
    }
    
    /** Filter a list of model elements by visibility
     * @param list the original list
     * @param visibility the visibility - "public" "protected", "private" or the
     * empty string (for package visibility)
     * @return a list with all elements from the original list that have 
     * a matching visibility.
     */
    public Collection filterByVisibility(Collection list, String visibility) { 
        Collection retval = new ArrayList(list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ModelElementDecorator elem = (ModelElementDecorator)iter.next();
            if (visibility.equals(elem.getVisibility().toString())) {
                retval.add(elem);
            }
        }
        return retval;
    }
    
}
