package org.andromda.core.metadecorators.uml14;

import org.andromda.core.uml14.UMLProfile;
import org.apache.commons.lang.StringUtils;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Operation
 *
 *
 */
public class EntityFinderMethodDecoratorImpl extends EntityFinderMethodDecorator
{
    // ---------------- constructor -------------------------------
    
    public EntityFinderMethodDecoratorImpl (org.omg.uml.foundation.core.Operation metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityFinderMethodDecorator ...

    public java.lang.String getQuery() {
		//first see if there is a query stored as a tagged value
		String queryString =
			StringUtils.trimToNull(
				this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_QUERY));
		return queryString;
    }

    // ------------- relations ------------------
    
}
