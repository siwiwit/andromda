package org.andromda.core.metadecorators.uml14;




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
		//right now this method does nothing,
    	//eventually it will return an OCL query.
    	return null;
    }

    // ------------- relations ------------------
    
}
