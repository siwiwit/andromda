package org.andromda.samples.carrental.inventory.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.andromda.samples.carrental.inventory.InventoryService;
import org.andromda.samples.carrental.inventory.InventoryServiceHome;
import org.andromda.samples.carrental.inventory.InventoryServiceUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 *
 *
 */
public class ListCarsAction extends Action
{

    /**
     * @see Action#perform(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException
    {
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();

        CarCreationForm myForm = (CarCreationForm) form;

        try
        {
            InventoryServiceHome ish = InventoryServiceUtil.getHome();
            InventoryService is = ish.create();

            Collection carTypeData = is.searchAllCarTypes();
            myForm.setExistingCarTypes(carTypeData);

            Collection carAndTypeData = is.searchAllCars();
            myForm.setExistingCars(carAndTypeData);

            is.remove();

            return (mapping.findForward("success"));
        }
        catch (Exception e)
        {
            throw new ServletException(e.getMessage());
            // or ... return (mapping.findForward("failure"));
        }
    }
}
