package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.statemachines.Transition
 *
 *
 */
public abstract class StrutsTransitionDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.statemachines.Transition
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.statemachines.Transition  metaObject;

    public StrutsTransitionDecorator (org.omg.uml.behavioralelements.statemachines.Transition metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.Boolean isGuarded();
    
    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.commonbehavior.Action getEffect()
    {
        return metaObject.getEffect ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.statemachines.Guard getGuard()
    {
        return metaObject.getGuard ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.statemachines.StateVertex getSource()
    {
        return metaObject.getSource ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.statemachines.StateMachine getStateMachine()
    {
        return metaObject.getStateMachine ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.statemachines.StateVertex getTarget()
    {
        return metaObject.getTarget ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public org.omg.uml.behavioralelements.statemachines.Event getTrigger()
    {
        return metaObject.getTrigger ();
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setEffect(org.omg.uml.behavioralelements.commonbehavior.Action p0)
    {
        metaObject.setEffect (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setGuard(org.omg.uml.behavioralelements.statemachines.Guard p0)
    {
        metaObject.setGuard (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setSource(org.omg.uml.behavioralelements.statemachines.StateVertex p0)
    {
        metaObject.setSource (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setStateMachine(org.omg.uml.behavioralelements.statemachines.StateMachine p0)
    {
        metaObject.setStateMachine (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setTarget(org.omg.uml.behavioralelements.statemachines.StateVertex p0)
    {
        metaObject.setTarget (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.Transition
    public void setTrigger(org.omg.uml.behavioralelements.statemachines.Event p0)
    {
        metaObject.setTrigger (p0);
    }

}
