package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.statemachines.Pseudostate
 *
 *
 */
public abstract class StrutsPseudostateDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.statemachines.Pseudostate
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.statemachines.Pseudostate  metaObject;

    public StrutsPseudostateDecorator (org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.Boolean isChoice();
    
    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.statemachines.Pseudostate
    public org.omg.uml.foundation.datatypes.PseudostateKind getKind()
    {
        return metaObject.getKind ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Pseudostate
    public void setKind(org.omg.uml.foundation.datatypes.PseudostateKind p0)
    {
        metaObject.setKind (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.StateVertex
    public org.omg.uml.behavioralelements.statemachines.CompositeState getContainer()
    {
        return metaObject.getContainer ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateVertex
    public java.util.Collection getIncoming()
    {
        return metaObject.getIncoming ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateVertex
    public java.util.Collection getOutgoing()
    {
        return metaObject.getOutgoing ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateVertex
    public void setContainer(org.omg.uml.behavioralelements.statemachines.CompositeState p0)
    {
        metaObject.setContainer (p0);
    }

}
