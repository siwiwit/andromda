package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

import java.util.Collections;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
 */
public class StrutsForwardLogicImpl
        extends StrutsForwardLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
{
    // ---------------- constructor -------------------------------
    
    public StrutsForwardLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsForward ...

    public java.lang.String getForwardName()
    {
        final EventFacade trigger = getTrigger();
        final String name = (trigger == null) ? getTarget().getName() : trigger.getName();
        return StringUtilsHelper.toResourceMessageKey(name);
    }

    public java.lang.String getForwardPath()
    {
        final StateVertexFacade target = getTarget();
        if (target instanceof StrutsJsp)
        {
            return ((StrutsJsp)target).getFullPath();
        }
        else if (target instanceof StrutsFinalState)
        {
            return ((StrutsFinalState)target).getFullPath();
        }
        else
            return null;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward#getForwardParameters()
     */
    public java.util.Collection handleGetForwardParameters()
    {
        EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }
}
