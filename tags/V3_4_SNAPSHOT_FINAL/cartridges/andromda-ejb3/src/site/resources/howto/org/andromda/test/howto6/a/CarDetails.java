// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.andromda.test.howto6.a;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class CarDetails
    implements Serializable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2523674736578380328L;

    public CarDetails()
    {
        this.name = null;
        this.serial = null;
        this.ownerName = null;
        this.year = null;
        this.timesRented = 0;
    }

    public CarDetails(String name, String serial, String ownerName, Date year, int timesRented)
    {
        this.name = name;
        this.serial = serial;
        this.ownerName = ownerName;
        this.year = year;
        this.timesRented = timesRented;
    }

    /**
     * Copies constructor from other CarDetails
     *
     * @param otherBean, cannot be <code>null</code>
     * @throws java.lang.NullPointerException if the argument is <code>null</code>
     */
    public CarDetails(CarDetails otherBean)
    {
        this(otherBean.getName(), otherBean.getSerial(), otherBean.getOwnerName(), otherBean.getYear(), otherBean.getTimesRented());
    }

    /**
     * Copies all properties from the argument value object into this value object.
     */
    public void copy(CarDetails otherBean)
    {
        this.setName(otherBean.getName());
        this.setSerial(otherBean.getSerial());
        this.setOwnerName(otherBean.getOwnerName());
        this.setYear(otherBean.getYear());
        this.setTimesRented(otherBean.getTimesRented());
    }

    private String name;

    /**
     *
     */
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String serial;

    /**
     *
     */
    public String getSerial()
    {
        return this.serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    private String ownerName;

    /**
     *
     */
    public String getOwnerName()
    {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    private Date year;

    /**
     *
     */
    public Date getYear()
    {
        return this.year;
    }

    public void setYear(Date year)
    {
        this.year = year;
    }

    private int timesRented;

    /**
     *
     */
    public int getTimesRented()
    {
        return this.timesRented;
    }

    public void setTimesRented(int timesRented)
    {
        this.timesRented = timesRented;
    }
}