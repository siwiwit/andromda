package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.statemachines.Guard
 *
 *
 */
public abstract class StrutsGuardDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.statemachines.Guard
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.statemachines.Guard  metaObject;

    public StrutsGuardDecorator (org.omg.uml.behavioralelements.statemachines.Guard metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.String getGuardMethodName();
    
    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.statemachines.Guard
    public org.omg.uml.foundation.datatypes.BooleanExpression getExpression()
    {
        return metaObject.getExpression ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Guard
    public org.omg.uml.behavioralelements.statemachines.Transition getTransition()
    {
        return metaObject.getTransition ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Guard
    public void setExpression(org.omg.uml.foundation.datatypes.BooleanExpression p0)
    {
        metaObject.setExpression (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Guard
    public void setTransition(org.omg.uml.behavioralelements.statemachines.Transition p0)
    {
        metaObject.setTransition (p0);
    }

}
