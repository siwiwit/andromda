package org.andromda.cartridges.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ModelElementDecorator;
import org.andromda.core.uml14.UMLStaticHelper;
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
public class EJBScriptHelper extends UMLStaticHelper {
    
    /** Check if <code>attr</code> is read only.
     * 
     * @param attr the attribute to check
     * @return <code>true</code> if <code>attr</code> is read only, 
     * <code>false</code> else
     */
    public boolean isReadOnly(Attribute attr) {
        return ChangeableKindEnum.CK_FROZEN.equals(attr.getChangeability());
    }
    
    /** Check if <code>assoc</code> is read only.
     * 
     * @param assoc the association end to check
     * @return <code>true</code> if <code>assoc</code> is read only, 
     * <code>false</code> else
     */
    public boolean isReadOnly(AssociationEnd assoc) {
        return ChangeableKindEnum.CK_FROZEN.equals(assoc.getChangeability());
    }
    
    /** Check if <code>feature</code> is declared static.
     * 
     * @param feature the structural feature to check
     * @return <code>true</code> if <code>feature</code> is static, 
     * <code>false</code> else
     */
    public boolean isStatic(Feature feature) {
        return ScopeKindEnum.SK_CLASSIFIER.equals(feature.getOwnerScope());
    }

    /** 
     * Get all attributes for <code>type</code>. The returned 
     * list includes the attributes that are 
     * inherited from super classes. The list is contains the
     * inherited attributes first, followed by the attributes 
     * defined in this class.
     * @param type the type to get the attributes for
     * @return a List of {@link Attribute} objects
     */ 
    public List getAllInstanceAttributes(Object type) {
        List retval = getInheritedInstanceAttributes(type);
        retval.addAll(getInstanceAttributes(type));
        return retval;
    }
    
    /** 
     * Get all inherited attributes for <code>type</code>.
     * The attributes are grouped by the class that defines 
     * the attributes, with attributes from the most removed super class first. 
     * @param type the type to get the inherited attributes for
     * @return a List of {@link Attribute} objects
     */ 
    public List getInheritedInstanceAttributes(Object type) {
        GeneralizableElement current = getGeneralization(type);
        if (current == null) {
            return new ArrayList();
        } else {
            List retval = getInheritedInstanceAttributes(current);
            retval.addAll(getInstanceAttributes(current));
            return retval;
        }
    }

    /**
     * Gets the non-static attributes of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Attribute
     */
    public Collection getInstanceAttributes(Object object)
    {
        if ((object == null) || !(object instanceof Classifier))
        {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection attributes = new ArrayList(); 
        for (Iterator i = classifier.getFeature().iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Attribute && !isStatic((Attribute)o)) {
                 attributes.add(o);
            }
        }
        return attributes;
    }

    /**
     * Gets the static attributes of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Attribute
     */
    public Collection getStaticAttributes(Object object)
    {
        if ((object == null) || !(object instanceof Classifier))
        {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection attributes = new ArrayList(); 
        for (Iterator i = classifier.getFeature().iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Attribute && isStatic((Attribute)o)) {
                 attributes.add(o);
            }
        }
        return attributes;
    }    

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
