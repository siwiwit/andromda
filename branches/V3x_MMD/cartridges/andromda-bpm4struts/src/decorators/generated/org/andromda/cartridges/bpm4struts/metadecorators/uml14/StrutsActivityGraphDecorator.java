package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.activitygraphs.ActivityGraph
 *
 *
 */
public abstract class StrutsActivityGraphDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.activitygraphs.ActivityGraph
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.activitygraphs.ActivityGraph  metaObject;

    public StrutsActivityGraphDecorator (org.omg.uml.behavioralelements.activitygraphs.ActivityGraph metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.util.Set getActionStates();
    
   /**
    *
    */
    public abstract org.omg.uml.behavioralelements.statemachines.Pseudostate getInitialState();
    
   /**
    *
    */
    public abstract java.util.Set getPossibleChoiceValues();
    
    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.activitygraphs.ActivityGraph
    public java.util.Collection getPartition()
    {
        return metaObject.getPartition ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public org.omg.uml.foundation.core.ModelElement getContext()
    {
        return metaObject.getContext ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public java.util.Collection getSubmachineState()
    {
        return metaObject.getSubmachineState ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public org.omg.uml.behavioralelements.statemachines.State getTop()
    {
        return metaObject.getTop ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public java.util.Collection getTransitions()
    {
        return metaObject.getTransitions ();
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public void setContext(org.omg.uml.foundation.core.ModelElement p0)
    {
        metaObject.setContext (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.StateMachine
    public void setTop(org.omg.uml.behavioralelements.statemachines.State p0)
    {
        metaObject.setTop (p0);
    }

}
