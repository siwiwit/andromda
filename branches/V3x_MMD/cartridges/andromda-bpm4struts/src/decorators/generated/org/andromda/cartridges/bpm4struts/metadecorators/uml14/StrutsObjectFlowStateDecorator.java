package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
 *
 *
 */
public abstract class StrutsObjectFlowStateDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState  metaObject;

    public StrutsObjectFlowStateDecorator (org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
    public java.util.Collection getParameter()
    {
        return metaObject.getParameter ();
    }

    // from org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
    public org.omg.uml.foundation.core.Classifier getType()
    {
        return metaObject.getType ();
    }

    // from org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
    public boolean isSynch()
    {
        return metaObject.isSynch ();
    }

    // from org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
    public void setSynch(boolean p0)
    {
        metaObject.setSynch (p0);
    }

    // from org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
    public void setType(org.omg.uml.foundation.core.Classifier p0)
    {
        metaObject.setType (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public java.util.Collection getDeferrableEvent()
    {
        return metaObject.getDeferrableEvent ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public org.omg.uml.behavioralelements.commonbehavior.Action getDoActivity()
    {
        return metaObject.getDoActivity ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public org.omg.uml.behavioralelements.commonbehavior.Action getEntry()
    {
        return metaObject.getEntry ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public org.omg.uml.behavioralelements.commonbehavior.Action getExit()
    {
        return metaObject.getExit ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public java.util.Collection getInternalTransition()
    {
        return metaObject.getInternalTransition ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public org.omg.uml.behavioralelements.statemachines.StateMachine getStateMachine()
    {
        return metaObject.getStateMachine ();
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public void setDoActivity(org.omg.uml.behavioralelements.commonbehavior.Action p0)
    {
        metaObject.setDoActivity (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public void setEntry(org.omg.uml.behavioralelements.commonbehavior.Action p0)
    {
        metaObject.setEntry (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public void setExit(org.omg.uml.behavioralelements.commonbehavior.Action p0)
    {
        metaObject.setExit (p0);
    }

    // from org.omg.uml.behavioralelements.statemachines.State
    public void setStateMachine(org.omg.uml.behavioralelements.statemachines.StateMachine p0)
    {
        metaObject.setStateMachine (p0);
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
