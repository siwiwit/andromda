// license-header java merge-point
// Generated by andromda-jsf cartridge (flow\UseCaseViewPopulator.java.vsl) DO NOT EDIT!
package org.andromda.cartridges.jsf.tests.constraints.controllers.duplicateoperationnames;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Provides the ability to populate any view in the duplicate operation names
 */
public final class DuplicateOperationNamesViewPopulator
{
    /**
     * Attempts to populate the current view using the appropriate view populator.
     *
     * @param facesContext the current faces context.
     * @param form the form to pass to the populator.
     */
    public static void populateFormAndViewVariables(final FacesContext facesContext, Object form)
    {
        populateFormAndViewVariables(facesContext, form, null);
    }

    /**
     * Populates the view using the appropriate view populator.
     *
     * @param facesContext the current faces context.
     * @param form the form to pass to the populator.
     * @param viewPath the path used to get the appropriate populator.
     */
    public static void populateFormAndViewVariables(final FacesContext facesContext, Object form, String viewPath)
    {
        try
        {
            final String viewId = (viewPath != null && viewPath.trim().length() > 0) ? viewPath : getViewId(facesContext);
            final Class<?> populator = populators.get(viewId);
            if (populator != null)
            {
                final Method method = populator.getMethod(
                    "populateFormAndViewVariables",
                    new Class[]{FacesContext.class, Object.class});
                method.invoke(populator, new Object[]{facesContext, form});
           }
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Populates the view using the appropriate view populator.
     *
     * @param facesContext the current faces context.
     * @return viewId.
     */
    protected static String getViewId(final FacesContext facesContext)
    {
        UIViewRoot view  = facesContext.getViewRoot();
        return view != null ? view.getViewId() : null;
    }

    /**
     * Stores the view populators by path.
     */
    private static final Map<String, Class<?>> populators = new HashMap<String, Class<?>>();

    static
    {
    }
}