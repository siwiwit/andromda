package org.andromda.core.uml14;

/**
 * Contains the common UML AndroMDA profile.  That is, it contains
 * elements "common" to all AndroMDA compoents (tagged values, and stereotypes).
 * 
 * @author Chad Brandon
 */
public class UMLProfile {

	/* ----------------- Stereotypes -------------------- */
	
	/**
	 * Represents a persistent entity.
	 */
	public static final String STEREOTYPE_ENTITY = "Entity";
	
	/**
	 * Represents a finder method on an entity.
	 */
	public static final String STEREOTYPE_FINDER_METHOD = "FinderMethod";
	
	/**
	 * Represents the primary key of an entity.
	 */
	public static final String STEREOTYPE_PRIMARY_KEY = "PrimaryKey";
	
	/**
	 * Represents a service.
	 */
	public static final String STEREOTYPE_SERVICE = "Service";
	
	/* ----------------- Tagged Values -------------------- */
	
	/**
	 * Represents documentation stored as a tagged value
	 */
	public static final String TAGGEDVALUE_DOCUMENTATION = "documentation";
	
}
