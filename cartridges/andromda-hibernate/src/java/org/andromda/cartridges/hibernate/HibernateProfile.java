package org.andromda.cartridges.hibernate;

import org.andromda.core.uml14.UMLProfile;

/**
 * The EJB profile. Contains
 * the profile information (tagged values, and stereotypes) 
 * for the EJB cartridge.
 * 
 * @author Chad Brandon
 */
public class HibernateProfile extends UMLProfile {
	
	/* ----------------- Stereotypes -------------------- */
	
	
	/* ----------------- Tagged Values -------------------- */
	
	public static final String TAGGEDVALUE_HIBERNATE_QUERY = "@andromda.hibernate.query";	
	
}
