package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;

import java.util.Collection;
import java.util.Iterator;


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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp#getPath()()
     */
    public java.lang.String getPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName()).replace('.', '/')) + ".jsp";
    }

    public boolean hasForms()
    {
        Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) actionIterator.next();
            if (action.isFormAction())
                return true;
        }
        return false;
    }

    public String getTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }
    // ------------- relations ------------------

    protected Collection handleGetActions()
    {
        return ((ActionState)metaObject).getOutgoing();
    }
}
