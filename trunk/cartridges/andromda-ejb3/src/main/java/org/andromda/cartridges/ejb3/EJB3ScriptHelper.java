package org.andromda.cartridges.ejb3;

import org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade;
import org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade;
import org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Transform class for the EJB3 cartridge.
 *
 * @author Richard Kunze
 * @author Chad Brandon
 * @author Vance Karimi
 */
public class EJB3ScriptHelper
{
    /**
     * Create a collection of String objects representing the argument names.
     * 
     * @param args A comma separated list of arguments
     * @return Collection A collection of of Strings representing the arguments
     */
    public Collection getArgumentsAsList(String args)
    {
    	StringTokenizer st = new StringTokenizer(args, ",");
    	Collection retval = new ArrayList(st.countTokens());
    	while (st.hasMoreTokens()) {
    		retval.add(st.nextToken().trim());
    	}
    	return retval;
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
        Collection retval = new ArrayList(list.size());
        for (final Iterator iter = list.iterator(); iter.hasNext();)
        {
            ModelElementFacade elem = (ModelElementFacade)iter.next();
            if (visibility.equals(elem.getVisibility().toString()))
            {
                retval.add(elem);
            }
        }
        return retval;
    }

    /**
     * Filter a list of EntityAttributes by removing all non-updatable attributes.
     * This filter currently removes all attributes that are of stereotype Version
     * It also removes identifier attributes for an entity with a composite primary key class
     * or if the identifier attribute have a specified generator.
     * 
     * @param list The original list
     * @param isCompositePKPresent True if entity has a composite primary key 
     * @return Collection A list of EntityAttributes from the original list that are updatable
     */
    public Collection filterUpdatableAttributes(Collection list, boolean isCompositePKPresent)
    {
    	Collection retval = new ArrayList(list.size());
    	for (final Iterator iter = list.iterator(); iter.hasNext(); ) 
    	{
    		EJB3EntityAttributeFacade attr = (EJB3EntityAttributeFacade)iter.next();
    		if (!attr.isVersion() && 
                ((isCompositePKPresent && !attr.isIdentifier()) || 
                 (!isCompositePKPresent && (attr.isIdentifier() && attr.isGeneratorTypeNone()) ||
                                !attr.isIdentifier())))
    		{
    			retval.add(attr);
    		}
    	}
    	return retval;
    }
    
    /**
     * Replaces all instances of the dot (.) in the name argument with an understore (_)
     * and returns the string response.
     *  
     * @param name The name, typically a fully qualified name with dot notation
     * @return The string with all dots replaced with underscore.
     */
    public String toUnderscoreName(String name)
    {
        String result = null;
        if (name != null)
        {
            result = StringUtils.replaceChars(name, '.', '_');
        }
        return result;
    }
    
    /**
     * Returns the comma separated list of interceptor classes.
     * 
     * @param interceptors The collection ModelElementFacade elements representing the interceptors
     * @param prepend Prefix any interceptors to the comma separated list
     * @return String containing the comma separated fully qualified class names
     */
    public String getInterceptorsAsList(Collection interceptors, String prepend)
    {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        
        if (StringUtils.isNotBlank(prepend))
        {
            sb.append(prepend);
            separator = ", ";
        }

        for (final Iterator it = interceptors.iterator(); it.hasNext();)
        {
            ModelElementFacade interceptor = (ModelElementFacade)it.next();
            sb.append(separator);
            separator = ", ";
            sb.append(interceptor.getFullyQualifiedName() + ".class");
        }
        return sb.toString();
    }
    
    /**
     * Reverses the <code>packageName</code>.
     *
     * @param packageName the package name to reverse.
     * @return the reversed package name.
     */
    public static String reversePackage(String packageName)
    {
        return StringUtils.reverseDelimited(packageName, EJB3Globals.NAMESPACE_DELIMITER);
    }
}
