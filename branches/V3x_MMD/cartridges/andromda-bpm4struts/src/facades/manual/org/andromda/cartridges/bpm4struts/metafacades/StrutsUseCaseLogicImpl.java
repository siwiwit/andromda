package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.core.common.StringUtilsHelper;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
        extends StrutsUseCaseLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
{
    // ---------------- constructor -------------------------------
    
    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    public String getFullPathName()
    {
        return '/' + StringUtilsHelper.toJavaClassName(getName());
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase#getActivityGraph()
     */
    public java.lang.Object handleGetActivityGraph()
    {
        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof ActivityGraphFacade)
                return obj;
        }
        return null;
    }

}
