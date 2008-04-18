package org.andromda.cartridges.spring.metafacades;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.cartridges.spring.SpringUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * @see org.andromda.cartridges.hibernate.metafacades.SpringQueryOperation Metaclass facade implementation.
 */
public class SpringQueryOperationLogicImpl
        extends SpringQueryOperationLogic
{

    public SpringQueryOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateFinderMethod#getQuery()
     */
    protected String handleGetQuery()
    {
        return this.getQuery((SpringEntity)null);
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
        boolean useNamedParameters = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(USE_NAMED_PARAMETERS)))
                .booleanValue()
                || StringUtils.isNotBlank(this.getTranslatedQuery());

        return SpringMetafacadeUtils.getUseNamedParameters(this, useNamedParameters);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#isCriteriaFinder()
     */
    protected boolean handleIsCriteriaFinder()
    {
        return this.getCriteriaArgument() != null;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#getCriteriaArgument()
     */
    protected ParameterFacade handleGetCriteriaArgument()
    {
        ParameterFacade foundParameter = null;
        for (final Iterator iterator = this.getParameters().iterator(); iterator.hasNext();)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            final ClassifierFacade type = parameter.getType();
            if (type != null && type.hasStereotype(UMLProfile.STEREOTYPE_CRITERIA))
            {
                foundParameter = parameter;
                break;
            }
        }
        return foundParameter;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#getQuery(org.andromda.cartridges.spring.metafacades.SpringEntity)
     */
    protected String handleGetQuery(SpringEntity entity)
    {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = this.getTranslatedQuery();

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this.findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            queryString = (String)value;
            if (queryString != null)
            {
                // remove any excess whitespace
                queryString = queryString.replaceAll("[$\\s]+", " ");
            }
        }

        // if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString))
        {
            ModelElementFacade owner;
            if (entity == null)
            {
                owner = this.getOwner();
            }
            else
            {
                owner = entity;
            }
            String variableName = StringUtils.uncapitalize(owner.getName());
            queryString = "from " + owner.getFullyQualifiedName() + " as " + variableName;
            if (this.getArguments().size() > 0)
            {
                queryString = queryString + " where";
                Collection arguments = this.getArguments();
                if (arguments != null && !arguments.isEmpty())
                {
                    final Iterator iterator = arguments.iterator();
                    for (int ctr = 0; iterator.hasNext(); ctr++)
                    {
                        ParameterFacade argument = (ParameterFacade)iterator.next();
                        final ClassifierFacade type = argument.getType();
                        if (type != null)
                        {
                            final String parameterName = argument.getName();
                            if (type != null && type.isEmbeddedValue())
                            {
                                for (final Iterator attributeIterator = type.getAttributes(true).iterator(); attributeIterator.hasNext();)
                                {
                                    final AttributeFacade attribute = (AttributeFacade)attributeIterator.next();
                                    String parameter = "?";
                                    if (this.isUseNamedParameters())
                                    {
                                        parameter = ":" + SpringUtils.concatNamesCamelCase(Arrays.asList(new String[]{parameterName, attribute.getName()}));
                                    }
                                    queryString = queryString + " " + variableName + "." + parameterName + "." + attribute.getName() + " = " + parameter;
                                    if (attributeIterator.hasNext())
                                    {
                                        queryString = queryString + " and";
                                    }
                                }
                            }
                            else
                            {
                                String parameter = "?";
                                if (this.isUseNamedParameters())
                                {
                                    parameter = ":" + parameterName;
                                }
                                queryString = queryString + " " + variableName + "." + parameterName + " = " + parameter;
                                if (iterator.hasNext())
                                {
                                    queryString = queryString + " and";
                                }
                            }
                        }
                    }
                }
            }
        }
        return queryString;
    }
}