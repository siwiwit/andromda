package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.TransitionFacade;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
 */
public class StrutsJspLogicImpl
        extends StrutsJspLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
{
    // ---------------- constructor -------------------------------
    
    public StrutsJspLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsJsp ...

    // from org.andromda.metafacades.uml.ModelElementFacade
    public String getPackageName()
    {
        ClassifierFacade classifier = (ClassifierFacade)getActivityGraph().getContextElement();
        return classifier.getPackageName();
    }

    public String getTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String getFullPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/') + ".jsp";
    }
    // ------------- relations ------------------

    protected Collection handleGetActions()
    {
        return getOutgoing();
    }

    protected Collection handleGetPageVariables()
    {
        final Collection variables = new LinkedList();
        final Collection incoming = getIncoming();
        for (Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
                variables.addAll( trigger.getParameters() );
        }
        return variables;
    }
}
