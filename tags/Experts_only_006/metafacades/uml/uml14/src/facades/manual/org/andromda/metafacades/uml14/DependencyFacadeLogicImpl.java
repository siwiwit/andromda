package org.andromda.metafacades.uml14;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class DependencyFacadeLogicImpl
       extends DependencyFacadeLogic
       implements org.andromda.metafacades.uml.DependencyFacade
{
    // ---------------- constructor -------------------------------
    
    public DependencyFacadeLogicImpl (org.omg.uml.foundation.core.Dependency metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class DependencyDecorator ...

    public Object handleGetTargetType()
    {
        return metaObject.getSupplier().iterator().next();
    }

    // ------------- relations ------------------

}
