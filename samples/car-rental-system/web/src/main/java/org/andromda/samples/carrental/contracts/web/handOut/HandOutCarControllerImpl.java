// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.handOut;

import java.io.Serializable;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController
 */
public class HandOutCarControllerImpl extends HandOutCarController
{
    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#loadAvailableCars(org.apache.struts.action.ActionMapping, LoadAvailableCarsForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String loadAvailableCars(ActionMapping mapping, LoadAvailableCarsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setAvailableCars(availableCarsDummyList);
        // this property receives a default value, just to have the application running on dummy data
        form.setIdReservation("idReservation-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#searchForReservationsOfCustomer(org.apache.struts.action.ActionMapping, SearchForReservationsOfCustomerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchForReservationsOfCustomer(ActionMapping mapping, SearchForReservationsOfCustomerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setCustomerReservations(customerReservationsDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#saveSelectedCar(org.apache.struts.action.ActionMapping, SaveSelectedCarForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void saveSelectedCar(ActionMapping mapping, SaveSelectedCarForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setId("id-test");
    }

    /**
     * This dummy variable is used to populate the "availableCars" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection availableCarsDummyList =
        Arrays.asList(new AvailableCarsDummy("inventoryNo-1", "registrationNo-1", "id-1"),
            new AvailableCarsDummy("inventoryNo-2", "registrationNo-2", "id-2"),
            new AvailableCarsDummy("inventoryNo-3", "registrationNo-3", "id-3"),
            new AvailableCarsDummy("inventoryNo-4", "registrationNo-4", "id-4"),
            new AvailableCarsDummy("inventoryNo-5", "registrationNo-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class AvailableCarsDummy implements Serializable
    {
        private static final long serialVersionUID = 8707553970547007301L;

        private String inventoryNo = null;
        private String registrationNo = null;
        private String id = null;

        public AvailableCarsDummy(String inventoryNo, String registrationNo, String id)
        {
            this.inventoryNo = inventoryNo;
            this.registrationNo = registrationNo;
            this.id = id;
        }
        
        public void setInventoryNo(String inventoryNo)
        {
            this.inventoryNo = inventoryNo;
        }

        public String getInventoryNo()
        {
            return this.inventoryNo;
        }
        
        public void setRegistrationNo(String registrationNo)
        {
            this.registrationNo = registrationNo;
        }

        public String getRegistrationNo()
        {
            return this.registrationNo;
        }
        
        public void setId(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }
        
    }
    /**
     * This dummy variable is used to populate the "customerReservations" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection customerReservationsDummyList =
        Arrays.asList(new CustomerReservationsDummy("comfortClass-1", "reservationDate-1", "idReservation-1"),
            new CustomerReservationsDummy("comfortClass-2", "reservationDate-2", "idReservation-2"),
            new CustomerReservationsDummy("comfortClass-3", "reservationDate-3", "idReservation-3"),
            new CustomerReservationsDummy("comfortClass-4", "reservationDate-4", "idReservation-4"),
            new CustomerReservationsDummy("comfortClass-5", "reservationDate-5", "idReservation-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class CustomerReservationsDummy implements Serializable
    {
        private String comfortClass = null;
        private String reservationDate = null;
        private String idReservation = null;
        private static final long serialVersionUID = -8077662513546496380L;

        public CustomerReservationsDummy(String comfortClass, String reservationDate, String idReservation)
        {
            this.comfortClass = comfortClass;
            this.reservationDate = reservationDate;
            this.idReservation = idReservation;
        }
        
        public void setComfortClass(String comfortClass)
        {
            this.comfortClass = comfortClass;
        }

        public String getComfortClass()
        {
            return this.comfortClass;
        }
        
        public void setReservationDate(String reservationDate)
        {
            this.reservationDate = reservationDate;
        }

        public String getReservationDate()
        {
            return this.reservationDate;
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
