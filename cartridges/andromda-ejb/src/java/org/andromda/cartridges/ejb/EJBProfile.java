package org.andromda.cartridges.ejb;

import org.andromda.core.uml14.UMLProfile;

/**
 * The EJB profile. Contains
 * the profile information (tagged values, and stereotypes) 
 * for the EJB cartridge.
 * 
 * @author Chad Brandon
 */
public class EJBProfile extends UMLProfile {
	
	/* ----------------- Stereotypes -------------------- */
	
	/* ----------------- Tagged Values -------------------- */
	
	public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";	
	
	public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
	
}
