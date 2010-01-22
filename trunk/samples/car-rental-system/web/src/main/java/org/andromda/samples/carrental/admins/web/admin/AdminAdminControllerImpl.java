// license-header java merge-point
package org.andromda.samples.carrental.admins.web.admin;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController
 */
public class AdminAdminControllerImpl extends AdminAdminController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#updateAdministrator(org.apache.struts.action.ActionMapping, UpdateAdministratorForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String updateAdministrator(ActionMapping mapping, UpdateAdministratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setAccountNo("accountNo-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setEmail("email-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setName("name-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#addAdministrator(org.apache.struts.action.ActionMapping, AddAdministratorForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String addAdministrator(ActionMapping mapping, AddAdministratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setAccountNo("accountNo-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setEmail("email-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setName("name-test");
        return null;
    }

}