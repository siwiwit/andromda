package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.statemachines.Event
 *
 *
 */
public abstract class StrutsEventDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.statemachines.Event
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.statemachines.Event  metaObject;

    public StrutsEventDecorator (org.omg.uml.behavioralelements.statemachines.Event metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.statemachines.Event
    public java.util.List getParameter()
    {
        return metaObject.getParameter ();
    }

}
