package org.andromda.cartridges.ejb.metadecorators.uml14;


/**
 *
 * Metaclass decorator for org.omg.uml.foundation.core.Operation
 *
 *
 */
public abstract class EJBFinderMethodDecorator
       extends    org.andromda.core.metadecorators.uml14.OperationDecoratorImpl
       implements org.omg.uml.foundation.core.Operation
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.foundation.core.Operation  metaObject;

    public EJBFinderMethodDecorator (org.omg.uml.foundation.core.Operation metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.String getQuery();
    
   /**
    *
    */
    public abstract java.lang.String getViewType();
    
    // ------------- relations ------------------
    

    // decorating methods not implemented here. 
    // they are already in org.andromda.core.metadecorators.uml14.OperationDecoratorImpl
    // or in a superclass of that.
}
