package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities for use with EJB metafacades.
 *
 * @author Chad Brandon
 */
class EJBMetafacadeUtils
{

    /**
     * Gets all create methods for the given <code>classifier</code>.
     * @param classifier 
     * @param follow if true, all super type create methods are also retrieved
     * @return Collection of create methods found.
     */
    static Collection getCreateMethods(ClassifierFacade classifier, boolean follow)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        Collection retval = new ArrayList();
        ClassifierFacade entity = classifier;
        do
        {
            Collection ops = entity.getOperations();
            for (final Iterator i = ops.iterator(); i.hasNext();)
            {
                OperationFacade op = (OperationFacade)i.next();
                if (op.hasStereotype(EJBProfile.STEREOTYPE_CREATE_METHOD))
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
     * @param classifier
     * @return the interface name.
     */
    static String getHomeInterfaceName(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
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
     * Gets the view type for the passed in <code>classifier</code>. Returns 'local' if the model element has the entity
     * stereotype, also checks the ejb tagged value and if there is no value defined, returns 'remote'.
     * @param classifier
     * @return String the view type name.
     */
    static String getViewType(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String viewType = "local";
        if (classifier.hasStereotype(EJBProfile.STEREOTYPE_SERVICE) || classifier.hasStereotype(EJBProfile.STEREOTYPE_SERVICE_ELEMENT))
        {
            String viewTypeValue = (String)classifier.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_VIEWTYPE);
            // if the view type wasn't found, search all super classes
            if (StringUtils.isEmpty(viewTypeValue))
            {
                viewType = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_VIEWTYPE) !=
                                null;
                    }
                });
            }
            if (StringUtils.isNotBlank(viewTypeValue))
            {
                viewType = viewTypeValue;
            }
            else
            {
                viewType = "remote";
            }
        }
        return viewType.toLowerCase();
    }

    /**
     * Gets all the inherited instance attributes, excluding the instance attributes directory from this
     * <code>classifier</code>.
     * @param classifier the ClassifierFacade from which to retrieve the inherited attributes.
     * @return a list of ordered attributes.
     */
    static List getInheritedInstanceAttributes(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
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
     * Gets all instance attributes including those instance attributes belonging to the <code>classifier</code> and any
     * inherited ones.
     *
     * @param classifier the ClassifierFacade from which to retrieve the instance attributes.
     * @return the list of all instance attributes.
     */
    static List getAllInstanceAttributes(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        List retval = getInheritedInstanceAttributes(classifier);
        retval.addAll(classifier.getInstanceAttributes());
        return retval;
    }

    /**
     * Gets all environment entries for the specified <code>classifier</code>. If <code>follow</code> is true, then a
     * search up the inheritance hierarchy will be performed and all super type environment entries will also be
     * retrieved.
     *
     * @param classifier the classifier from which to retrieve the env-entries
     * @param follow     true/false on whether or not to 'follow' the inheritance hierarchy when retrieving the
     *                   env-entries.
     * @return the collection of environment entries
     */
    static Collection getEnvironmentEntries(ClassifierFacade classifier, boolean follow)
    {
        ExceptionUtils.checkNull("classifer", classifier);

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
                return ((AttributeFacade)object).hasStereotype(EJBProfile.STEREOTYPE_ENV_ENTRY);
            }
        });

        return attributes;
    }

    /**
     * Gets all constants for the specified <code>classifier</code>. If <code>follow</code> is true, then a search up
     * the inheritance hierarchy will be performed and all super type constants will also be retrieved.
     *
     * @param classifier the classifier from which to retrieve the constants
     * @param follow     true/false on whether or not to 'follow' the inheritance hierarchy when retrieving the
     *                   constants.
     * @return the collection of environment entries
     */
    static Collection getConstants(ClassifierFacade classifier, boolean follow)
    {
        ExceptionUtils.checkNull("classifer", classifier);

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
                return !((AttributeFacade)object).hasStereotype(EJBProfile.STEREOTYPE_ENV_ENTRY);
            }
        });

        return attributes;
    }

    /**
     * Returns true/false based on whether or not synthetic or auto generated create methods should be allowed.
     *
     * @param classifier the entity or session EJB.
     * @return true/false
     */
    static boolean allowSyntheticCreateMethod(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        return !classifier.isAbstract() && classifier.findTaggedValue(
                EJBProfile.TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD) == null;
    }

}