// license-header java merge-point
// Generated by andromda-jsf cartridge (flow\ActionForward.java.vsl) DO NOT EDIT!
package org.andromda.cartridges.jsf.tests.finalstates.webpage;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.andromda.presentation.jsf.FacesContextUtils;
import org.andromda.presentation.jsf.UseCaseForwards;

/**
 * This servlet is used to allow controller operation execution through
 * a URL call.
 */
public class FinalStatesWebPage
    extends HttpServlet
{
    /**  */
    private static final long serialVersionUID = -4200629904955041261L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(
        final HttpServletRequest request,
        final HttpServletResponse response)
        throws ServletException, IOException
    {
        // - we need to resolve the controller differently since we're outside of the faces servlet
        Controller controller =
            (Controller)FacesContextUtils.resolveVariable(
                request, response,
                "controller");
        final String forwardPath = UseCaseForwards.getPath(controller.finalStatesWebPage());
        if(forwardPath != null){
            FinalStatesWebPageViewPopulator.populateFormAndViewVariables(
                FacesContextUtils.getFacesContext(request, response),
                null, forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(
        final HttpServletRequest request,
        final HttpServletResponse response)
        throws ServletException, IOException
    {
        this.doGet(request, response);
    }
}