// license-header java merge-point
// Generated by andromda-jsf cartridge (forms\Form.java.vsl) DO NOT EDIT!
package org.andromda.cartridges.jsf.tests.deferringoperations;

import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * This form encapsulates the fields that are used in the execution of the
 * <code>testPageToPage</code> method, which is located on the
 * <code>org.andromda.cartridges.jsf.tests.deferringoperations.Controller</code> controller.
 *
 * 
 *
 * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#testPageToPage(TestPageToPageForm)
 */
public interface TestPageToPageForm
{
    /**
     * Gets the ValueChangeEvent (if any) that is associated with this form.
     *
     * @return the faces ValueChangeEvent associated to this form.
     */
    public ValueChangeEvent getValueChangeEvent();

    /**
     * Gets the ActionEvent (if any) that is associated with this form.
     *
     * @return the faces ActionEvent associated to this form.
     */
    public ActionEvent getActionEvent();

    /**
     * Sets the event (if any) that is associated with this form.
     *
     * @param event the faces event to associate to this form.
     */
    public void setEvent(FacesEvent event);

    /**
     * 
     * @return decisionTestParam
     */
    public int getDecisionTestParam();

    /**
     * 
     * @param decisionTestParam
     */
    public void setDecisionTestParam(int decisionTestParam);

}