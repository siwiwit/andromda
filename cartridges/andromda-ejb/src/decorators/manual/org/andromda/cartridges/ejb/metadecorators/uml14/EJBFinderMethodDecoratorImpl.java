package org.andromda.cartridges.ejb.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Parameter;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Operation
 *
 *
 */
public class EJBFinderMethodDecoratorImpl extends EJBFinderMethodDecorator
{

	private static final String TAGGED_VALUE_EJB_QUERY = "@andromda.ejb.query";	

    // ---------------- constructor -------------------------------
    
    public EJBFinderMethodDecoratorImpl (org.omg.uml.foundation.core.Operation metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EJBFinderMethodDecorator ...

    public java.lang.String getQuery() {
		
		//first see if there is a query stored as a tagged value
		String queryString = 
			StringUtils.trimToNull(
				this.findTaggedValue(TAGGED_VALUE_EJB_QUERY));

		//if there wasn't any stored query, create one by default.
		if (StringUtils.isEmpty(queryString)) {
			queryString = 
				"SELECT DISTINCT OBJECT(c) FROM " 
					+ this.getOwner().getName() + " as c";
			if (this.getParameters().size() > 0) {
				queryString = queryString + " WHERE";
				Collection parameters = this.getParameters();
				if (parameters != null && parameters.isEmpty()) {
					Iterator parameterIt = parameters.iterator();
					for (int ctr = 0; parameterIt.hasNext(); ctr++) {
						Parameter param = (Parameter)parameterIt.next();
						queryString = queryString 
							+ " c." + param.getName() 
							+ " = ?" + ctr; 
						if (parameters.size() != ctr) {
							queryString = queryString + " AND";
						}
					}
				}
			} 
		}
		return queryString;
    }

    public java.lang.String getViewType() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------
    
}
