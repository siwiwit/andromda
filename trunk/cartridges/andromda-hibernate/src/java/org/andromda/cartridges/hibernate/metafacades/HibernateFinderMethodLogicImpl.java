package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;

/**
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodFacade
 *      Metaclass facade implementation.
 */
public class HibernateFinderMethodLogicImpl
    extends HibernateFinderMethodLogic
    implements
    org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod
{
    // ---------------- constructor -------------------------------

    public HibernateFinderMethodLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#getQuery()
     */
    public String handleGetQuery()
    {

        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = super.getQuery("query.Hibernate-QL");

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            queryString = (String)value;
            if (queryString != null)
            {
                // remove any excess whitespace
                queryString = queryString.replaceAll("[$\\s]+", " ");
            }
        }

        //if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString))
        {
            String variableName = StringUtils.uncapitalize(this.getOwner().getName());
            queryString = "from " + this.getOwner().getFullyQualifiedName()
                + " as " + variableName;
            if (this.getArguments().size() > 0)
            {
                queryString = queryString + " where";
                Collection parameters = this.getArguments();
                if (parameters != null && !parameters.isEmpty())
                {
                    Iterator parameterIt = parameters.iterator();
                    for (int ctr = 0; parameterIt.hasNext(); ctr++)
                    {
                        Object test = parameterIt.next();
                        ParameterFacade param = (ParameterFacade)test;
                        queryString = queryString + " " 
                            + variableName + "." + param.getName()
                            + " = ?";
                        if (parameterIt.hasNext())
                        {
                            queryString = queryString + " and";
                        }
                    }
                }
            }
        }
        return queryString;
    }

}