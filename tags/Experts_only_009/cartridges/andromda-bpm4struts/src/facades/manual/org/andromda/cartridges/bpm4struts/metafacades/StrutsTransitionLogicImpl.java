package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition
 */
public class StrutsTransitionLogicImpl
        extends StrutsTransitionLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition
{
    private Transition transition = null;

    // ---------------- constructor -------------------------------
    
    public StrutsTransitionLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
        this.transition = (Transition)metaObject;
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTransition ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getForwardName()()
     */
    public java.lang.String getForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getForwardPath()()
     */
    public java.lang.String getForwardPath()
    {
        return ('/' + getPackageName() + '/' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getMethodName()()
     */
    public java.lang.String getMethodName()
    {
        return StringUtilsHelper.toJavaMethodName(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getParametersAsList(boolean withTypeNames)()
     */
    public java.lang.String getParametersAsList(boolean withTypeNames)
    {
        final Collection parameters = getEventParameters();

        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = parameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            buffer.append(parameter.getFullyQualifiedName());
            buffer.append(' ');
            buffer.append(parameter.getName());
            if (iterator.hasNext())
                buffer.append(',');
        }

        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getGuardName()()
     */
    public java.lang.String getGuardName()
    {
        return transition.getGuard().getName();
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getEventParameters()
     */
    public java.util.Collection handleGetEventParameters()
    {
        return transition.getEffect().getActualArgument();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTransition#getTargetOutcomes()
     */
    public java.util.Collection handleGetTargetOutcomes()
    {
        return transition.getTarget().getOutgoing();
    }

}
