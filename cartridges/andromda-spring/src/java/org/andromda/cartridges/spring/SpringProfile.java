package org.andromda.cartridges.spring;

import org.andromda.core.common.Profile;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Spring profile. Contains the profile information (tagged values, and
 * stereotypes) for the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Peter Friese
 */
public class SpringProfile extends UMLProfile {

    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */

    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores whether a criteria search attribute may be nullable.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_NULLABLE = profile
            .get("HIBERNATE_CRITERIA_NULLABLE");

    /**
     * Stores a criteria search attribute path.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_ATTRIBUTE = profile
            .get("HIBERNATE_CRITERIA_ATTRIBUTE");

    /**
     * Stores a criteria search comparator.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_COMPARATOR = profile
            .get("HIBERNATE_CRITERIA_COMPARATOR");

    /**
     * Stores a hibernate matchmode.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_MATCHMODE = profile
            .get("HIBERNATE_CRITERIA_MATCHMODE");

    /**
     * Stores a hibernate query.
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = profile
            .get("HIBERNATE_QUERY");

    /**
     * Stores the hibernate inheritance use for entities.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile
            .get("HIBERNATE_INHERITANCE");

    /**
     * Defines the remoting type for spring services.
     */
    public static final String TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE = profile
            .get("SPRING_SERVICE_REMOTING_TYPE");

    /**
     * Defines the remote port for spring services.
     */
    public static final String TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT = profile
            .get("SPRING_SERVICE_REMOTE_PORT");

    /* ----------------- Tagged Values Values ------------- */

    /**
     * The "like" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LIKE = profile
            .get("LIKE_COMPARATOR");

    /**
     * The "equals" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_EQUAL = profile
            .get("EQUAL_COMPARATOR");

    /**
     * The "greater of even" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL = profile
            .get("GREATER_THAN_OR_EQUAL_COMPARATOR");

    /**
     * The "greater" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_GREATER = profile
            .get("GREATER_THAN_COMPARATOR");

    /**
     * The "less of even" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL = profile
            .get("LESS_THAN_OR_EQUAL_COMPARATOR");

    /**
     * The "less" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LESS = profile
            .get("LESS_THAN_COMPARATOR");

    /**
     * The {@link net.sf.hibernate.expression.MatchMode#ANYWHERE} match mode.
     */
    public static final Object TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE = profile.get("MATCHMODE_ANYWHERE");

    /**
     * The {@link net.sf.hibernate.expression.MatchMode#END} match mode.
     */
    public static final Object TAGGEDVALUEVALUE_MATCHMODE_END = profile.get("MATCHMODE_END");

    /**
     * The {@link net.sf.hibernate.expression.MatchMode#EXACT} match mode.
     */
    public static final Object TAGGEDVALUEVALUE_MATCHMODE_EXACT = profile.get("MATCHMODE_EXACT");

    /**
     * The {@link net.sf.hibernate.expression.MatchMode#START} match mode.
     */
    public static final Object TAGGEDVALUEVALUE_MATCHMODE_START = profile.get("MATCHMODE_START");

}