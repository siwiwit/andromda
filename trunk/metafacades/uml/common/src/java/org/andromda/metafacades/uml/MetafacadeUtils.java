package org.andromda.metafacades.uml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * A class containing utlities for metafacade manipulation.
 * 
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MetafacadeUtils
{

    /**
     * Checks to see if the element is the specified type and if so casts it to
     * the object and returns it, otherwise it returns null.
     * 
     * @param element the element to check.
     * @param type the Class type.
     * @return java.lang.Object
     */
    public static Object getElementAsType(Object element, Class type)
    {
        Object elementAsType = null;
        if (element != null && type != null)
        {
            Class elementClass = element.getClass();
            if (type.isAssignableFrom(elementClass))
            {
                elementAsType = element;
            }
        }
        return elementAsType;
    }

    /**
     * Filters out the model elements from the <code>modelElements</code>
     * collection that don't have the specified <code>stereotype</code>
     * 
     * @param modelElements the model elements to filter.
     * @param stereotype the stereotype that a model element must have in order
     *        to stay remain within the <code>modelElements</code> collection.
     */
    public static void filterByStereotype(
        Collection modelElements,
        final String stereotype)
    {
        if (StringUtils.isNotEmpty(stereotype))
        {
            CollectionUtils.filter(
                modelElements,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).hasStereotype(stereotype);
                    }
                });
        }
    }
    
    /**
     * Filters out the model elements from the <code>modelElements</code>
     * collection that are not of (or do not inherit from) 
     * the specified type <code>type</code>
     * 
     * @param modelElements the model elements to filter.
     * @param type the type of Class.
     */
    public static void filterByType(
        Collection modelElements,
        final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(modelElements,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return type.isAssignableFrom(object.getClass());
                }
            });
        }
    }
    
    /**
     * Filters out the model elements from the <code>modelElements</code>
     * collection that are of (or inherit from) 
     * the specified type <code>type</code>
     * 
     * @param modelElements the model elements to filter.
     * @param type the type of Class.
     */
    public static void filterByNotType(
        Collection modelElements,
        final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(modelElements,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return !type.isAssignableFrom(object.getClass());
                }
            });
        }
    }

    /**
     * <p/>Returns a consistent name for a relation, independent from the end
     * of the relation one is looking at.
     * </p>
     * <p/>In order to guarantee consistency with relation names, they must
     * appear the same whichever angle (ie entity) that you come from. For
     * example, if you are at Customer end of a relationship to an Address then
     * your relation may appear with the name Customer-Address. But if you are
     * in the Address entity looking at the Customer then you will get an error
     * because the relation will be called Address-Customer. A simple way to
     * guarantee that both ends of the relationship have the same name is merely
     * to use alphabetical ordering.
     * </p>
     * 
     * @param roleName name of role in relation
     * @param targetRoleName name of target role in relation
     * @param separator character used to separate words
     * @return uniform mapping name (in alphabetical order)
     */
    public static String toRelationName(
        String roleName,
        String targetRoleName,
        String separator)
    {
        if (roleName.compareTo(targetRoleName) <= 0)
        {
            return (roleName + separator + targetRoleName);
        }
        return (targetRoleName + separator + roleName);
    }
    
    /**
     * The <code>uppercase</code> role name mask.
     */
    public static final String MASK_UPPERCASE = "uppercase";
    
    /**
     * The <code>underscore</code> role name mask.
     */
    public static final String MASK_UNDERSCORE = "underscore";
    
    /**
     * The <code>upperunderscore</code> role name mask.
     */
    public static final String MASK_UPPERUNDERSCORE = "upperunderscore"; 

    /**
     * The <code>lowercase</code> role name mask.
     */
    public static final String MASK_LOWERCASE = "lowercase";
    
    /**
     * The <code>lowerunderscore</code> role name mask.
     */
    public static final String MASK_LOWERUNDERSCORE = "lowerunderscore";    

    /**
     * The <code>camelcase</code> role name mask.
     */
    public static final String MASK_CAMELCASE = "camelcase";

    /**
     * The <code>nospace</code> role name mask.
     */
    public static final String MASK_NOSPACE = "nospace";

    /**
     * The <code>none</code> role name mask.
     */
    public static final String MASK_NONE = "none";
    
    /**
     * Returns the name with the appropriate <code>mask</code>
     * applied.  The mask, must match one of the valid mask properties
     * or will be ignored.
     * @param name the name to be masked
     * @param mask the mask to apply
     * @return the masked name.
     */
    public static String getMaskedName(String name, String mask)
    {
        mask = StringUtils.trimToEmpty(mask);
        name = StringUtils.trimToEmpty(name);
        if (!mask.equalsIgnoreCase(MASK_NONE))
        {
            if (mask.equalsIgnoreCase(MASK_UPPERCASE))
            {
                name = name.toUpperCase();
            }
            else if (mask.equalsIgnoreCase(MASK_UNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_");
            }
            else if (mask.equalsIgnoreCase(MASK_UPPERUNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_").toUpperCase();
            }
            else if (mask.equalsIgnoreCase(MASK_LOWERCASE))
            {
                name = name.toLowerCase();
            }
            else if (mask.equalsIgnoreCase(MASK_LOWERUNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_").toLowerCase();
            }
            else if (mask.equalsIgnoreCase(MASK_CAMELCASE))
            {
                name = StringUtilsHelper.upperCamelCaseName(name
                    .toLowerCase());
            }
            else if (mask.equalsIgnoreCase(MASK_NOSPACE))
            {
                name = StringUtils.deleteWhitespace(name);
            }
        }
        return name;
    }

    private final static Map uniqueNames = new HashMap();

    /**
     * Registers the argument name and updates it if necessary so it is unique
     * amoung all the registered names so far.
     * 
     * @param name the name to register
     * @return the argument, possible transformed in case it was already
     *         registered
     */
    public static String createUniqueName(String name)
    {
        if (StringUtils.isBlank(name))
            return name;

        String uniqueName = null;
        if (uniqueNames.containsKey(name))
        {
            int collisionCount = ((Integer)uniqueNames.get(name)).intValue() + 1;
            String suffix = String.valueOf(collisionCount);

            uniqueNames.put(name, new Integer(collisionCount));

            uniqueName = name.substring(0, name.length() - suffix.length())
                + suffix;
        }
        else
        {
            uniqueName = name;
        }
        uniqueNames.put(uniqueName, new Integer(0));
        return uniqueName;
    }
}