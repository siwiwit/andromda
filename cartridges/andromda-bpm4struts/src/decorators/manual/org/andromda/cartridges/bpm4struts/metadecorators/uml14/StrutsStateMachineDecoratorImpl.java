package org.andromda.cartridges.bpm4struts.metadecorators.uml14;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.StateMachine
 *
 *
 */
public class StrutsStateMachineDecoratorImpl extends StrutsStateMachineDecorator
{
    // ---------------- constructor -------------------------------
    
    public StrutsStateMachineDecoratorImpl (org.omg.uml.behavioralelements.statemachines.StateMachine metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsStateMachineDecorator ...

    public java.util.Set getStates() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    public org.omg.uml.behavioralelements.statemachines.Pseudostate getInitialState() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------
    
}
