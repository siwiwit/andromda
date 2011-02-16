// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.reserveCar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController
 */
public class CustomerReservesCarControllerImpl extends CustomerReservesCarController
{
    /**
     * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#searchForReservations(org.apache.struts.action.ActionMapping, SearchForReservationsForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchForReservations(ActionMapping mapping, SearchForReservationsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setReservations(reservationsDummyList);
        // this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        form.setComfortClassValueList(new Object[] {"comfortClass-1", "comfortClass-2", "comfortClass-3", "comfortClass-4", "comfortClass-5"});
        form.setComfortClassLabelList(form.getComfortClassValueList());
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#reserve(org.apache.struts.action.ActionMapping, ReserveForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void reserve(ActionMapping mapping, ReserveForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // setting a date
        form.setReservationDateAsDate(new Date());
        // this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        form.setComfortClassValueList(new Object[] {"comfortClass-1", "comfortClass-2", "comfortClass-3", "comfortClass-4", "comfortClass-5"});
        form.setComfortClassLabelList(form.getComfortClassValueList());
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#deleteReservation(org.apache.struts.action.ActionMapping, DeleteReservationForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void deleteReservation(ActionMapping mapping, DeleteReservationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setIdReservation("idReservation-test");
    }

    /**
     * This dummy variable is used to populate the "reservations" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection reservationsDummyList =
        Arrays.asList(new ReservationsDummy("reservationDate-1", "comfortClass-1", "idReservation-1"),
            new ReservationsDummy("reservationDate-2", "comfortClass-2", "idReservation-2"),
            new ReservationsDummy("reservationDate-3", "comfortClass-3", "idReservation-3"),
            new ReservationsDummy("reservationDate-4", "comfortClass-4", "idReservation-4"),
            new ReservationsDummy("reservationDate-5", "comfortClass-5", "idReservation-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ReservationsDummy implements Serializable
    {
        private static final long serialVersionUID = -2085538380562710068L;

        private String reservationDate = null;
        private String comfortClass = null;
        private String idReservation = null;

        public ReservationsDummy(String reservationDate, String comfortClass, String idReservation)
        {
            this.reservationDate = reservationDate;
            this.comfortClass = comfortClass;
            this.idReservation = idReservation;
        }
        
        public void setReservationDate(String reservationDate)
        {
            this.reservationDate = reservationDate;
        }

        public String getReservationDate()
        {
            return this.reservationDate;
        }
        
        public void setComfortClass(String comfortClass)
        {
            this.comfortClass = comfortClass;
        }

        public String getComfortClass()
        {
            return this.comfortClass;
        }
        
        public void setIdReservation(String idReservation)
        {
            this.idReservation = idReservation;
        }

        public String getIdReservation()
        {
            return this.idReservation;
        }
        
    }
}
