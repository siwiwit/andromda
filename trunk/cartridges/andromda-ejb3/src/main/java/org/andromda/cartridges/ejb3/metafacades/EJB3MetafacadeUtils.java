package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Contains utilities for use with EJB metafacades.
 *
 * @author Chad Brandon
 * @author Vance Karimi
 */
class EJB3MetafacadeUtils
{

    /**
     * Gets all create methods for the given <code>classifier</code>.
     *
     * @param classifier The classifier from which to retries the create methods
     * @param follow if true, all super type create methods are also retrieved
     * @return Collection of create methods found.
     */
    static Collection getCreateMethods(
            ClassifierFacade classifier, 
            boolean follow)
    {
        final String methodName = "EJBMetafacadeUtils.getCreateMethods";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        Collection retval = new ArrayList();
        ClassifierFacade entity = classifier;
        do
        {
            Collection ops = entity.getOperations();
            for (final Iterator i = ops.iterator(); i.hasNext();)
            {
                final OperationFacade op = (OperationFacade)i.next();
                if (op.hasStereotype(EJB3Profile.STEREOTYPE_CREATE_METHOD))
                {
                    retval.add(op);
                }
            }
            if (follow)
            {
                entity = (ClassifierFacade)entity.getGeneralization();
            }
        }
        while (follow && entity != null);
        return retval;
    }

    /**
     * Gets the interface name for the passed in <code>classifier</code>. Returns 'LocalHome' if the mode element has
     * the entity stereotype, returns 'Home' otherwise.
     *
     * @return the interface name.
     */
    static String getHomeInterfaceName(ClassifierFacade classifier)
    {
        final String methodName = "EJBMetafacadeUtils.getHomeInterfaceName";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        String homeInterfaceName;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
        {
            homeInterfaceName = classifier.getName() + "LocalHome";
        }
        else
        {
            homeInterfaceName = classifier.getName() + "Home";
        }
        return homeInterfaceName;
    }

    /**
     * Gets the view type for the passed in <code>classifier</code>. If no 
     * view type can be retrieved from the <code>classifier</code>, then the 
     * <code>defaultViewType</code> is returned.
     * 
     * If the model element has the entity stereotype, returns 'local'.
     * Otherwise (session ejb) checks the ejb tagged value and if there is 
     * no value defined, returns 'remote'.
     *
     * @param classifier The classifier to lookup the view type tagged value
     * @param defaultViewType The default view type if one is not found
     * @return String the view type name.
     */
    static String getViewType(
            ClassifierFacade classifier,
            String defaultViewType)
    {
        final String methodName = "EJBMetafacadeUtils.getViewType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        String viewType = (String)classifier.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (classifier.hasStereotype(EJB3Profile.STEREOTYPE_ENTITY))
        {
            if (StringUtils.isEmpty(viewType))
            {
                viewType = (StringUtils.isNotBlank(defaultViewType) ? defaultViewType : EJB3Globals.VIEW_TYPE_LOCAL);
            }
        }
        else if (classifier.hasStereotype(EJB3Profile.STEREOTYPE_SERVICE))
        {
            // if the view type wasn't found, search all super classes
            if (StringUtils.isEmpty(viewType))
            {
                viewType = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE) != null;
                    }
                });
            }
            if (StringUtils.isEmpty(viewType))
            {
                viewType = (StringUtils.isNotBlank(defaultViewType) ? defaultViewType : EJB3Globals.VIEW_TYPE_REMOTE);
            }
        }
        return viewType.toLowerCase();
    }

    /**
     * Gets all the inherited instance attributes, excluding the instance attributes directory from this
     * <code>classifier</code>.
     *
     * @param classifer the ClassifierFacade from which to retrieve the inherited attributes.
     * @return a list of ordered attributes.
     */
    static List getInheritedInstanceAttributes(ClassifierFacade classifier)
    {
        final String methodName = "EJBMetafacadeUtils.getInheritedInstanceAttributes";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        ClassifierFacade current = (ClassifierFacade)classifier.getGeneralization();
        if (current == null)
        {
            return new ArrayList();
        }
        List retval = getInheritedInstanceAttributes(current);

        if (current.getInstanceAttributes() != null)
        {
            retval.addAll(current.getInstanceAttributes());
        }
        return retval;
    }

    /**
     * Gets all instance attributes including those instance attributes belonging to the 
     * <code>classifier</code> and any inherited ones.
     *
     * @param classifier the ClassifierFacade from which to retrieve the instance attributes.
     * @return the list of all instance attributes.
     */
    static List getAllInstanceAttributes(ClassifierFacade classifier)
    {
        final String methodName = "EJBMetafacadeUtils.getAllInstanceAttributes";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        List retval = getInheritedInstanceAttributes(classifier);
        retval.addAll(classifier.getInstanceAttributes());
        return retval;
    }

    /**
     * Gets all environment entries for the specified <code>classifier</code>. If <code>follow</code> is true, then a
     * search up the inheritance hierachy will be performed and all super type environment entries will also be
     * retrieved.
     *
     * @param classifier the classifier from which to retrieve the env-entries
     * @param follow     true/false on whether or not to 'follow' the inheritance hierarchy when retrieving the
     *                   env-entries.
     * @return the collection of enviroment entries
     */
    static Collection getEnvironmentEntries(
            ClassifierFacade classifier, 
            boolean follow)
    {
        final String methodName = "EJBMetafacadeUtils.getEnvironmentEntries";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);

        Collection attributes = classifier.getStaticAttributes();

        if (follow)
        {
            for (classifier = (ClassifierFacade)classifier.getGeneralization();
                 classifier != null; classifier = (ClassifierFacade)classifier.getGeneralization())
            {
                attributes.addAll(classifier.getStaticAttributes());
            }
        }

        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return ((AttributeFacade)object).hasStereotype(EJB3Profile.STEREOTYPE_ENV_ENTRY);
            }
        });

        return attributes;
    }

    /**
     * Returns the transaction type for the specified <code>classifier</code>.
     * 
     * @param classifier the classifier from which to retrieve the transaction type.
     * @param defaultTransactionType the default transaction type if no tagged value is specified.
     * @return the transaction type as a String.
     */
    static String getTransactionType(ClassifierFacade classifier, String defaultTransactionType)
    {
        final String methodName = "EJBMetafacadeUtils.getTransactionType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        
        String transactionType = (String)classifier.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isNotBlank(transactionType))
        {
            transactionType = convertTransactionType(transactionType);
        }
        else
        {
            transactionType = defaultTransactionType;
        }
        return transactionType;
    }
    
    /**
     * Convert the transaction type from lower casing to upper casing.
     * This maintains reusable tagged value enumeration from EJB
     * implementation.
     * 
     * @param transType
     * @return
     */
    static String convertTransactionType(String transType)
    {
        final String methodName = "EJBMetafacadeUtils.convertTransactionType";
        ExceptionUtils.checkNull(methodName, "transType", transType);
        
        String type = null;
        if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_MANDATORY))
        {
            type = "MANDATORY";
        }
        else if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_NEVER))
        {
            type = "NEVER";
        }
        else if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_NOT_SUPPORTED))
        {
            transType = "NOT_SUPPORTED";
        }
        else if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_REQUIRED))
        {
            type = "REQUIRED";
        }
        else if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_REQUIRES_NEW))
        {
            type = "REQUIRES_NEW";
        }
        else if (StringUtils.equalsIgnoreCase(transType, EJB3Globals.TRANSACTION_TYPE_SUPPORTS))
        {
            type = "SUPPORTS";
        }
        return type;
    }
    
    /**
     * Gets all constants for the specified <code>classifier</code>. 
     * If <code>follow</code> is true, then a search up
     * the inheritance hierachy will be performed and all super 
     * type constants will also be retrieved.
     *
     * @param classifier the classifier from which to retrieve the constants
     * @param follow     true/false on whether or not to 'follow' the inheritance hierarchy when retrieving the
     *                   constants.
     * @return the collection of enviroment entries
     */
    static Collection getConstants(
            ClassifierFacade classifier, 
            boolean follow)
    {
        final String methodName = "EJBMetafacadeUtils.getConstants";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);

        Collection attributes = classifier.getStaticAttributes();

        if (follow)
        {
            for (classifier = (ClassifierFacade)classifier.getGeneralization();
                 classifier != null; classifier = (ClassifierFacade)classifier.getGeneralization())
            {
                attributes.addAll(classifier.getStaticAttributes());
            }
        }

        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return !((AttributeFacade)object).hasStereotype(EJB3Profile.STEREOTYPE_ENV_ENTRY);
            }
        });

        return attributes;
    }

    /**
     * Returns true/false based on whether or not synthetic or auto generated create methods should be allowed.
     *
     * @param classifier The entity or session bean.
     * @return true/false
     */
    static boolean allowSyntheticCreateMethod(ClassifierFacade classifier)
    {
        final String methodName = "EJBMetafacadeUtils.allowSyntheticCreateMethod";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        return !classifier.isAbstract() && classifier.findTaggedValue(
                EJB3Profile.TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD) == null;
    }

    /**
     * Creates a fully qualified name from the given <code>packageName</code>,
     * <code>name</code>, and <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name the name of the model element.
     * @param suffix the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(
        String packageName,
        String name,
        String suffix)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append('.');
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        return fullyQualifiedName.toString();
    }
}