package org.andromda.cartridges.hibernate;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Hibernate profile. Contains
 * the profile information (tagged values, and stereotypes)
 * for the Hibernate cartridge.
 *
 * @author Chad Brandon
 */
public class HibernateProfile extends UMLProfile
{

    /* ----------------- Stereotypes -------------------- */


    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGEDVALUE_HIBERNATE_QUERY = "@andromda.hibernate.query";

    public static final String TAGGEDVALUE_HIBERNATE_INDEX = "@andromda.hibernate.index";
    public static final String TAGGEDVALUE_HIBERNATE_UNIQUE = "@andromda.hibernate.unique";

    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";

}
