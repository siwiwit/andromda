package org.andromda.core.metadecorators.uml14;



/**
 * Metaclass decorator implementation for org.omg.uml.foundation.core.UmlAssociation
 *
 * @author Chad Brandon
 */
public class AssociationDecoratorImpl extends AssociationDecorator
{
    // ---------------- constructor -------------------------------
    
    public AssociationDecoratorImpl (org.omg.uml.foundation.core.UmlAssociation metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AssociationDecorator ...

    // ------------- relations ------------------
    
   /**
    * Since Magic Draw doesn't seem to set 'ordered' correctly 
    * (or MDR doesn't load the XMI correctly) name the association
    * end to associationEnds.  When this issue is fixed, the association
    * should be named back to 'connection'.
    */
    public java.util.Collection handleGetAssociationEnds()
    {
        // TODO: add your implementation here!
        return metaObject.getConnection();
    }

    // ------------------------------------------------------------

}
