package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;


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
    
    public StrutsJspLogicImpl(java.lang.Object metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsJsp ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp#getPath()()
     */
    public java.lang.String getPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName()).replace('.', '/')) + Bpm4StrutsProfile.DEFAULT_JSP_PATH_EXTENSION;
    }

    // ------------- relations ------------------

}
