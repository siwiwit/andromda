package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @see org.andromda.cartridges.hibernate.metafacades.SpringQueryOperation Metaclass facade implementation.
 */
public class SpringQueryOperationLogicImpl extends SpringQueryOperationLogic
{
    // ---------------- constructor -------------------------------

    public SpringQueryOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateFinderMethod#getQuery()
     */
    protected String handleGetQuery()
    {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = this.getTranslatedQuery();

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this.findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            queryString = (String) value;
            if (queryString != null)
            {
                // remove any excess whitespace
                queryString = queryString.replaceAll("[$\\s]+", " ");
            }
        }

        // if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString))
        {
            String variableName = StringUtils.uncapitalize(this.getOwner().getName());
            queryString = "from " + this.getOwner().getFullyQualifiedName() + " as " + variableName;
            if (this.getArguments().size() > 0)
            {
                queryString = queryString + " where";
                Collection arguments = this.getArguments();
                if (arguments != null && !arguments.isEmpty())
                {
                    Iterator argumentIt = arguments.iterator();
                    for (int ctr = 0; argumentIt.hasNext(); ctr++)
                    {
                        ParameterFacade argument = (ParameterFacade) argumentIt.next();
                        String parameter = "?";
                        if (this.isUseNamedParameters())
                        {
                            parameter = ":" + argument.getName();
                        }
                        queryString = queryString + " " + variableName + "." + argument.getName() + " = " + parameter;
                        if (argumentIt.hasNext())
                        {
                            queryString = queryString + " and";
                        }
                    }
                }
            }
        }
        return queryString;
    }

    /**
     * Stores the translated query so that its only translated once.
     */
    private String translatedQuery = null;

    /**
     * Retrieves the translated query.
     */
    private String getTranslatedQuery()
    {
        if (this.translatedQuery == null)
        {
            this.translatedQuery = super.getQuery("query.Hibernate-QL");
        }
        return this.translatedQuery;
    }

    /**
     * Stores whether or not named parameters should be used in hibernate queries.
     */
    private static final String USE_NAMED_PARAMETERS = "hibernateQueryUseNamedParameters";

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateFinderMethod#isUseNamedParameters()
     */
    protected boolean handleIsUseNamedParameters()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(USE_NAMED_PARAMETERS))).booleanValue() || StringUtils.isNotBlank(
                this.getTranslatedQuery());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#isCriteriaFinder()
     */
    protected boolean handleIsCriteriaFinder()
    {
        return (getCriteriaArgument() != null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#getCriteriaArgument()
     */
    protected ParameterFacade handleGetCriteriaArgument()
    {
        Collection parameters = getParameters();
        for (Iterator iter = parameters.iterator(); iter.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade) iter.next();
            ClassifierFacade type = parameter.getType();
            if (type.hasStereotype(UMLProfile.STEREOTYPE_CRITERIA))
            {
                return parameter;
            }
        }
        return null;
    }
}