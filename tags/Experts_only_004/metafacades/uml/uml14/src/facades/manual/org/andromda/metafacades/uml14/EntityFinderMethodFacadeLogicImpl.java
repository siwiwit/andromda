package org.andromda.metafacades.uml14;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class EntityFinderMethodFacadeLogicImpl
       extends EntityFinderMethodFacadeLogic
       implements org.andromda.metafacades.uml.EntityFinderMethodFacade
{
    // ---------------- constructor -------------------------------
    
    public EntityFinderMethodFacadeLogicImpl (org.omg.uml.foundation.core.Operation metaObject)
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
