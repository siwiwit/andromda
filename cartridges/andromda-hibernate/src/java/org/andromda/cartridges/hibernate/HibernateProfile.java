package org.andromda.cartridges.hibernate;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The Hibernate profile. Contains the profile information (tagged values, and stereotypes) for the Hibernate
 * cartridge.
 *
 * @author Chad Brandon
 * @author Carlos Cuenca
 */
public class HibernateProfile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /* ----------------- Tagged Values -------------------- */

    /**
     * Tag attached to entities, attributes, and/or association ends to specify the
     * name of the XML node to generate when v3 XML Persistence is activated.
     */
    public static final String TAGGEDVALUE_HIBERNATE_XML_TAG_NAME = profile.get("HIBERNATE_XML_TAG_NAME");

    /**
     * Tag attached to association ends to determine if the associated entity should be embed
     * in as XML when v3 XML Persistence is activated.
     */
    public static final String TAGGEDVALUE_HIBERNATE_XML_EMBED = profile.get("HIBERNATE_XML_EMBED");

    /**
     * Stores the hibernate generator class.
     */
    public static final String TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS = profile.get("HIBERNATE_GENERATOR_CLASS");

    /**
     * Stores a hibernate query string
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = profile.get("HIBERNATE_QUERY");

    /**
     * Define whether the marked finder will use named parameters or positional parameters.
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS =
        profile.get("HIBERNATE_USE_NAMED_PARAMETERS");

    /**
     * Stores the viewtype of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = profile.get("EJB_VIEWTYPE");

    /**
     * Stores the EJB service transaction type.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = profile.get("EJB_TRANSACTION_TYPE");

    /**
     * Stores the aggregation kind (lazy/eager) of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = profile.get("HIBERNATE_LAZY");

    /**
     * Support for hibernate inheritance strategy, supported values are <ul> <li>class : one table per base class</li>
     * <li>subclass : one table per subclass</li> <li>concrete : one table per class, subclasses may implement subclass
     * or joined-subclass</li> <li>union-subclass: generate per concrete class with union-subclass mapping</li> <li>
     * interface : generate interface and put attributes etc on subclasses</li> </ul> See  Hibernate documentation for
     * specific details.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile.get("HIBERNATE_INHERITANCE");

    /**
     * Defines outer join fetching on many to one and one to one associations
     */
    public static final String TAGGEDVALUE_HIBERNATE_OUTER_JOIN = profile.get("HIBERNATE_OUTER_JOIN");

    /**
     * Defines if a query within a finder method should use the cache
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE = profile.get("HIBERNATE_USE_QUERY_CACHE");

    /**
     * Defines the cache type for the Entity
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_CACHE = profile.get("HIBERNATE_ENTITY_CACHE");

    /**
     * Defines if the entity will limit the SQL insert statement to properties with values
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_INSERT =
        profile.get("HIBERNATE_ENTITY_DYNAMIC_INSERT");

    /**
     * Defines if the entity will limit the SQL update statements to properties that are modified
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_UPDATE =
        profile.get("HIBERNATE_ENTITY_DYNAMIC_UPDATE");

    /**
     * Defines the cache type for an association
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE = profile.get("HIBERNATE_ASSOCIATION_CACHE");

    /**
     * Defines if the entity has a proxy
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROXY = profile.get("HIBERNATE_PROXY");

    /**
     * Defines the maximum number of objects that will be created in memory
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS =
        profile.get("HIBERNATE_EHCACHE_MAX_ELEMENTS");

    /**
     * Defines if elements are eternal.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL = profile.get("HIBERNATE_EHCACHE_ETERNAL");

    /**
     * Defines the time to idle for an element before it expires.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE =
        profile.get("HIBERNATE_EHCACHE_TIME_TO_IDLE");

    /**
     * Defines the time to live for an element before it expires.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE =
        profile.get("HIBERNATE_EHCACHE_TIME_TO_LIVE");

    /**
     * Defines if elements can overflow to disk.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK =
        profile.get("HIBERNATE_EHCACHE_OVERFLOW_TO_DISK");

    /**
     * Defines if the cache for this entity is to be distributed.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITYCACHE_DISTRIBUTED =
        profile.get("HIBERNATE_ENTITYCACHE_DISTRIBUTED");

    /**
     * Defines if the cache for this association is to be distributed.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED =
        profile.get("HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED");

    /**
     * Defines the association collection type
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_COLLECTION_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_COLLECTION_TYPE");

    /**
     * Defines the association sort type.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_SORT_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_SORT_TYPE");

    /**
     * Defines the association order by columns names.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS =
        profile.get("HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS");

    /**
     * Defines the association where clause.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_WHERE_CLAUSE =
        profile.get("HIBERNATE_ASSOCIATION_WHERE_CLAUSE");

    /**
     * Defines the index column for hibernate indexed collections
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX = profile.get("HIBERNATE_ASSOCIATION_INDEX");

    /**
     * Defines the index column type for hibernate indexed collections
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_INDEX_TYPE");

    /**
     * Defines the tagged value for hibernate version property on entities
     */
    public static final String TAGGEDVALUE_HIBERNATE_VERSION_PROPERTY = profile.get("HIBERNATE_VERSION_PROPERTY");

    /**
     * Defines the tagged value for hibernate cascade on an association end
     */
    public static final String TAGGEDVALUE_HIBERNATE_CASCADE = profile.get("HIBERNATE_CASCADE");

    /**
     * Stores sql formula for an attribute.
     */
    public static final String TAGGEDVALUE_HIBERNATE_FORMULA = profile.get("HIBERNATE_FORMULA");

    /**
    * Column name of the dicriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_COLUMN =
        profile.get("ENTITY_DISCRIMINATOR_COLUMN");

    /**
    * Column type of the discriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_TYPE = profile.get("ENTITY_DISCRIMINATOR_TYPE");

    /**
    * Value of the class for the discriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_VALUE = profile.get("ENTITY_DISCRIMINATOR_VALUE");

    /**
     * Specifies whether a mapped column should be included in SQL INSERT statements.
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT = profile.get("HIBERNATE_PROPERTY_INSERT");

    /**
     * Specifies whether a mapped column should be included in SQL UPDATE statements.
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE = profile.get("HIBERNATE_PROPERTY_UPDATE");

}