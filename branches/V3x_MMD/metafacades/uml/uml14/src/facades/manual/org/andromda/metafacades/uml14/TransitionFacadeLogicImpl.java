package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.Transition;


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

    protected Object handleGetEffect()
    {
        return ((Transition)metaObject).getEffect();
    }

    protected Object handleGetSource()
    {
        return ((Transition)metaObject).getTarget();
    }

    protected Object handleGetTarget()
    {
        return ((Transition)metaObject).getTarget();
    }

    protected Object handleGetTrigger()
    {
        return ((Transition)metaObject).getTrigger();
    }
}
