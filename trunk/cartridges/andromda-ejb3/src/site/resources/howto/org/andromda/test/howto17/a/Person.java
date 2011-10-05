// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: EntityEmbeddable.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

/**
 * Autogenerated POJO EJB class for Person containing the
 * bulk of the entity implementation.
 *
 * This is autogenerated by AndroMDA using the EJB3
 * cartridge.
 *
 * DO NOT MODIFY this class.
 *
 *
 *
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "PERSON")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL)
@javax.persistence.NamedQueries
({
    @javax.persistence.NamedQuery(name = "Person.findAll", query = "from Person as person"),
    @javax.persistence.NamedQuery(name = "Person.findByName", query = "from Person as person where person.name = :name"),
    @javax.persistence.NamedQuery(name = "Person.findByNameOrBirthDate", query = "from org.andromda.howto2.rental.Person as person where person.name = :name or person.birthDate = :birthDate")
})
public class Person
    implements java.io.Serializable
{
    private static final long serialVersionUID = -709937363639956936L;

    // ----------- Attribute Definitions ------------

    private String name;
    private java.util.Date birthDate;
    private java.lang.Long id;


    // --------- Relationship Definitions -----------

    private java.util.SortedSet<org.andromda.howto2.rental.Car> cars = new java.util.TreeSet<org.andromda.howto2.rental.Car>();

    // ---- Manageable Display Attributes (Transient) -----

    private java.util.Collection<String> carsLabels;

    // --------------- Constructors -----------------

    /**
     * Default empty constructor
     */
    public Person()
    {
        // default null constructor
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this class to the values provided by
     * the parameters.
     *
     * @param name Value for the name property
     * @param birthDate Value for the birthDate property
     */
    public Person(String name, java.util.Date birthDate)
    {
        setName(name);
        setBirthDate(birthDate);
    }

    /**
     * Constructor with all POJO attribute values and CMR relations.
     *
     * @param name Value for the name property
     * @param birthDate Value for the birthDate property
     * @param cars Value for the cars relation role
     */
    public Person(String name, java.util.Date birthDate, java.util.SortedSet<org.andromda.howto2.rental.Car> cars)
    {
        setName(name);
        setBirthDate(birthDate);

        setCars(cars);
    }


    // -------- Attribute Accessors ----------

    /**
     * Get the name property.
     *
     * @return String The value of name
     */
    @javax.persistence.Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 50)
    public String getName()
    {
        return name;
    }

    /**
     * Set the name property.
     * @param value the new value
     */
    public void setName(String value)
    {
        this.name = value;
    }

    /**
     * Get the birthDate property.
     *
     * @return java.util.Date The value of birthDate
     */
    @javax.persistence.Column(name = "BIRTH_DATE", nullable = false, insertable = true, updatable = true)
    public java.util.Date getBirthDate()
    {
        return birthDate;
    }

    /**
     * Set the birthDate property.
     * @param value the new value
     */
    public void setBirthDate(java.util.Date value)
    {
        this.birthDate = value;
    }

    /**
     * Get the id property.
     *
     * @return java.lang.Long The value of id
     */
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @javax.persistence.Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getId()
    {
        return id;
    }

    /**
     * Set the id property.
     * @param value the new value
     */
    public void setId(java.lang.Long value)
    {
        this.id = value;
    }


    // ------------- Relations ------------------

    /**
     * Get the cars Collection
     *
     * @return java.util.SortedSet<org.andromda.howto2.rental.Car>
     */
    @javax.persistence.OneToMany(mappedBy = "owner")
    @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL)
    @org.hibernate.annotations.Sort(type = org.hibernate.annotations.SortType.NATURAL)
    public java.util.SortedSet<org.andromda.howto2.rental.Car> getCars()
    {
        return this.cars;
    }

    /**
     * Set the cars
     *
     * @param cars
     */
    public void setCars (java.util.SortedSet<org.andromda.howto2.rental.Car> cars)
    {
        this.cars = cars;
    }

    // -------- Manageable Attribute Display -----------

    /**
     * Get the carsLabels
     *
     * @return java.util.Collection<String>
     */
    @javax.persistence.Transient
    public java.util.Collection<String> getCarsLabels()
    {
        return this.carsLabels;
    }

    /**
     * Set the carsLabels
     *
     * @param carsLabels
     */
    public void setCarsLabels (java.util.Collection<String> carsLabels)
    {
        this.carsLabels = carsLabels;
    }

    // -------- Common Methods -----------

    /**
     * Indicates if the argument is of the same type and all values are equal.
     *
     * @param object The target object to compare with
     * @return boolean True if both objects a 'equal'
     */
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Person))
        {
            return false;
        }
        final Person that = (Person)object;
        if (this.getId() == null || that.getId() == null || !this.getId().equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object
     *
     * @return int The hash code value
     */
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (getId() == null ? 0 : getId().hashCode());

        return hashCode;
    }

    /**
     * Returns a String representation of the object
     *
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Person(=");
        sb.append("name: ");
        sb.append(getName());
        sb.append(", birthDate: ");
        sb.append(getBirthDate());
        sb.append(", id: ");
        sb.append(getId());
        sb.append(")");
        return sb.toString();
    }
}
