package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity
 */
public class StrutsManageableEntityLogicImpl
        extends StrutsManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String internalGetNamespaceProperty()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

    public StrutsManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetFormBeanType()
    {
        return getManageablePackageName() + internalGetNamespaceProperty() + getFormBeanClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return getName() + "Form";
    }

    protected String handleGetFormBeanFullPath()
    {
        return getFormBeanType().replace(internalGetNamespaceProperty(), "/");
    }

    protected java.lang.String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetPageTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".page.title";
    }

    protected java.lang.String handleGetPageTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetListName()
    {
        return "manageableList";
    }

    protected java.lang.String handleGetListGetterName()
    {
        return "getManageableList";
    }

    protected java.lang.String handleGetListSetterName()
    {
        return "setManageableList";
    }

    protected String handleGetPageName()
    {
        return getName().toLowerCase() + "-crud.jsp";
    }

    protected String handleGetPageFullPath()
    {
        return '/' + getManageablePackagePath() + '/' + getPageName();
    }

    protected java.lang.String handleGetActionPath()
    {
        return '/' + getName() + "/Manage";
    }

    protected java.lang.String handleGetActionParameter()
    {
        return "crud";
    }

    protected java.lang.String handleGetFormBeanName()
    {
        return "manage" + getName() + "Form";
    }

    protected java.lang.String handleGetActionType()
    {
        return getManageablePackageName() + internalGetNamespaceProperty() + getActionClassName();
    }

    protected java.lang.String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".exception";
    }

    protected java.lang.String handleGetExceptionPath()
    {
        return getPageFullPath();
    }

    protected java.lang.String handleGetActionFullPath()
    {
        return '/' + getActionType().replace(internalGetNamespaceProperty(), "/");
    }

    protected java.lang.String handleGetActionClassName()
    {
        return "Manage" + getName();
    }

    protected boolean handleIsPreload()
    {
        return isCreate() || isRead() || isUpdate() || isDelete();
    }
}