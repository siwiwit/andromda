package org.andromda.cartridges.bpm4struts.metadecorators.uml14;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.State
 *
 *
 */
public class StrutsSimpleStateDecoratorImpl extends StrutsSimpleStateDecorator
{
    // ---------------- constructor -------------------------------
    
    public StrutsSimpleStateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.State metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsSimpleStateDecorator ...

    public org.omg.uml.behavioralelements.usecases.UseCase getCorrespondingUseCase() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------
    
}
