package org.andromda.metafacades.uml;

import java.util.ResourceBundle;

/**
 * Contains the common UML AndroMDA profile. That is, it contains elements
 * "common" to all AndroMDA components (tagged values, and stereotypes).
 * 
 * @author Chad Brandon
 */
public class UMLProfile
{
    // Get the resource
    static ResourceBundle umlProfile = ResourceBundle
        .getBundle(UMLProfile.class.getName());

    /* ----------------- Stereotypes -------------------- */

    /**
     * Represents a persistent entity.
     */
    public static final String STEREOTYPE_ENTITY = "ENTITY";

    /**
     * Represents a finder method on an entity.
     */
    public static final String STEREOTYPE_FINDER_METHOD = "FINDER_METHOD";

    /**
     * Represents the primary key of an entity.
     */
    public static final String STEREOTYPE_IDENTIFIER = "IDENTIFIER";

    /**
     * If an attribute has this stereotype, it is considered unique.
     */
    public static final String STEREOTYPE_UNIQUE = "UNIQUE";

    /**
     * Represents a service.
     */
    public static final String STEREOTYPE_SERVICE = "SERVICE";

    /**
     * Represents a value object.
     */
    public static final String STEREOTYPE_VALUE_OBJECT = "VALUE_OBJECT";

    /**
     * <p>
     * Represents a web service. Stereotype a class with this stereotype when
     * you want everything on the class to be exposed as a web service.
     * </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE = "WEBSERVICE";

    /**
     * <p>
     * Stereotype an operation on a <code>service</code> if you wish to expose
     * the operation.
     * </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE_OPERATION = "WEBSERVICE_OPERATION";

    /**
     * The base exception stereotype. If a model element is stereotyped with
     * this (or one of its specializations), then the exception can be generated
     * by a cartridge and a dependency to it from an operation will add a throws
     * clause.
     */
    public static final String STEREOTYPE_EXCEPTION = "EXCEPTION";

    /**
     * Represents an enumeration type.
     */
    public static final String STEREOTYPE_ENUMERATION = "ENUMERATION";

    /**
     * Represents exceptions thrown during normal application processing (such
     * as business exceptions). It extends the base exception stereotype.
     */
    public static final String STEREOTYPE_APPLICATION_EXCEPTION = "APPLICATION_EXCEPTION";
    
    /**
     * Represents unexpected exceptions that can occur during application
     * processing. This that a caller isn't expected to handle.
     */
    public static final String STEREOTYPE_UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";

    /**
     * Represents a reference to an exception model element. Model dependencies
     * to unstereotyped exception model elements can be stereotyped with this.
     * This allows the user to create a custom exception class since the
     * exception itself will not be generated but the references to it will be
     * (i.e. the throws clause within an operation).
     */
    public static final String STEREOTYPE_EXCEPTION_REF = "EXCEPTION_REF";

    /**
     * Used to indicate whether or not a parameter is nullable (since parameters
     * do <strong>NOT </strong> allow specification of multiplicity.
     */
    public static final String STEREOTYPE_NULLABLE = "NULLABLE";

    /**
     * Represents a role played by a user within a system.
     */
    public static final String STEREOTYPE_ROLE = "ROLE";

    /* ----------------- Tagged Values -------------------- */

    /**
     * Represents documentation stored as a tagged value
     */
    public static final String TAGGEDVALUE_DOCUMENTATION = umlProfile
        .getString("TAGGEDVALUE_DOCUMENTATION");

    /**
     * Represents a relational table name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TABLE = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_TABLE");

    /**
     * Represents a relational table column name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_COLUMN");

    /**
     * Represents a relational table column length
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH");

    /**
     * Represents a relational table column index name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX");

    /**
     * Stores the style of a web service (document, wrapped, rpc).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_STYLE = umlProfile
        .getString("TAGGEDVALUE_WEBSERVICE_STYLE");

    /**
     * Stores the use of a web service (literal, encoded).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_USE = umlProfile
        .getString("TAGGEDVALUE_WEBSERVICE_USE");

    /**
     * Stores the provider of the web service (RPC, EJB).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_PROVIDER = umlProfile
        .getString("TAGGEDVALUE_WEBSERVICE_PROVIDER");

    /**
     * Stores the name of the role (if it's different than the name of the actor
     * stereotyped as role)
     */
    public static final String TAGGEDVALUE_ROLE_NAME = umlProfile
        .getString("TAGGEDVALUE_ROLE_NAME");
    
    /**
     * Stores the service method transaction type.
     */
    public static final String TAGGEDVALUE_TRANSACTION_TYPE = umlProfile
        .getString("TAGGEDVALUE_TRANSACTION_TYPE");
}