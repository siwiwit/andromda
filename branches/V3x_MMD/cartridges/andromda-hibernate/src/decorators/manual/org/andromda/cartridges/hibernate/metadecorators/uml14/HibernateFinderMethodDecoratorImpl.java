package org.andromda.cartridges.hibernate.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.core.metadecorators.uml14.ParameterDecorator;
import org.apache.commons.lang.StringUtils;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Operation
 *
 *
 */
public class HibernateFinderMethodDecoratorImpl extends HibernateFinderMethodDecorator
{
    // ---------------- constructor -------------------------------
    
    public HibernateFinderMethodDecoratorImpl (org.omg.uml.foundation.core.Operation metaObject)
    {
        super (metaObject);
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.EntityFinderMethodDecorator#getQuery()
     */
    public String getQuery() {
    	
    	// first see if we can retrieve the query from the super class.
		String queryString = super.getQuery();
		
		// now see if there is a query stored as a tagged value
		if (StringUtils.isEmpty(queryString)) {
			queryString = this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
		}

		//if there wasn't any stored query, create one by default.
		if (StringUtils.isEmpty(queryString)) {
			queryString =
				"from c in class  "
					+ this.getOwner().getFullyQualifiedName()
					+ " as c";
			if (this.getParameters().size() > 0) {
				queryString = queryString + " where";
				Collection parameters = this.getParameters();
				if (parameters != null && !parameters.isEmpty()) {
					Iterator parameterIt = parameters.iterator();
					for (int ctr = 0; parameterIt.hasNext(); ctr++) {
						Object test = parameterIt.next();
						ParameterDecorator param = (ParameterDecorator) test;
						queryString =
							queryString
								+ " c."
								+ param.getName()
								+ " = ?"
								+ ctr;
						if (parameterIt.hasNext()) {
							queryString = queryString + " and";
						}
					}
				}
			}
		}
		return queryString;
    }
    
}
