package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActionFacade;
import org.andromda.metafacades.uml.CallActionFacade;
import org.andromda.metafacades.uml.ClassifierFacade;

import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;


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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp#getFullPathName()()
     */
    public java.lang.String getFullPathName()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/') + ".jsp";
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
        return getOutgoing();
    }

    protected Collection handleGetVariables()
    {
        ActionFacade action = getEntry();

        if (action instanceof CallActionFacade)
        {
            CallActionFacade callAction = (CallActionFacade)action;
            return callAction.getOperation().getParameters();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }
}
