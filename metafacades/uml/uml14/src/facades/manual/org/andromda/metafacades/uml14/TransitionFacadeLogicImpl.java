package org.andromda.metafacades.uml14;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class TransitionFacadeLogicImpl
       extends TransitionFacadeLogic
       implements org.andromda.metafacades.uml.TransitionFacade
{
    // ---------------- constructor -------------------------------
    
    public TransitionFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.Transition metaObject, String context)
    {
        super (metaObject, context);
    }
}
