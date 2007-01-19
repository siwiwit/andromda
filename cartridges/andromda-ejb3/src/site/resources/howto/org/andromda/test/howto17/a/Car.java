// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: Entity.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

/**
 * Autogenerated POJO EJB3 implementation class for Car.
 *
 * Add any manual implementation within this class.  This class will NOT
 * be overwritten with incremental changes.
 *
 * 
 *
 */

@javax.persistence.Entity
// Uncomment to enable seam component name
// @org.jboss.seam.annotations.Name("car")
// Uncomment to set specific component scope type
//@org.jboss.seam.annotations.Scope(org.jboss.seam.ScopeType.CONVERSATION)
@javax.persistence.Table(name = "CAR")
// Uncomment to enable entity listener for Car
// @javax.persistence.EntityListeners({org.andromda.howto2.rental.CarListener.class})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL)
@javax.persistence.NamedQueries
({
    @javax.persistence.NamedQuery(name = "Car.findAll", query = "select car from Car AS car"),
    @javax.persistence.NamedQuery(name = "Car.findByType", query = "from Car as car where car.type = :type")
})
public class Car
    extends org.andromda.howto2.rental.CarEmbeddable
    implements java.io.Serializable
{

    /**
     * The serial version UID of this class required for serialization.
     */
    private static final long serialVersionUID = 8776337268494473937L;

    // --------------- constructors -----------------
    
    /**
     * Default Car constructor
     */
    public Car()
    { 
        super();
    }
    
    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this/super class to the
     * values provided by the parameters.
     *
     */
    public Car(java.lang.String serial, java.lang.String name, org.andromda.howto2.rental.CarType type) 
    {
        super(serial, name, type);
    }
    
    /**
     * Constructor with all POJO attribute values and CMR relations.
     *
     * @param serial Value for the serial property
     * @param name Value for the name property
     * @param type Value for the type property
     * @param owner Value for the owner relation role
     */
    public Car(java.lang.String serial, java.lang.String name, org.andromda.howto2.rental.CarType type, org.andromda.howto2.rental.Person owner)
    {
        super(serial, name, type, owner);
    }
    

    // -------------- Entity Methods -----------------
    
    /**
     * 
     */
    @javax.persistence.Transient
    public boolean isRented()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public static boolean allCarsAreRented()
    {
        // TODO put your implementation here.
        return false;
    }


    // --------------- Lifecycle callbacks -----------------

}