package org.andromda.cartridges.ejb;

/**
 * The EJB profile. Contains
 * the profile information (tagged values, and stereotypes) 
 * for the EJB cartridge.
 * 
 * @author Chad Brandon
 */
public class EJBProfile {
	
	private EJBProfile() {}
	
	/* ----------------- Stereotypes -------------------- */
	
	/**
	 * Represents the persistent entity (an Entity EJB).
	 */
	public static final String STEREOTYPE_ENTITY = "Entity";
	
	/**
	 * Represents a finder method.
	 */
	public static final String STEREOTYPE_FINDER_METHOD = "FinderMethod";
	
	/**
	 * Represents the primary key of an entity.
	 */
	public static final String STEREOTYPE_PRIMARY_KEY = "PrimaryKey";
	
	/**
	 * Represents a service (a Session EJB).
	 */
	public static final String STEREOTYPE_SERVICE = "Service";
	
	/* ----------------- Tagged Values -------------------- */
	
	public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";	
	
	public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
	
}
