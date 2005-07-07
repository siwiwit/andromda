package org.andromda.metafacades.uml;

import org.andromda.core.profile.Profile;

/**
 * Contains the common UML AndroMDA profile. That is, it contains elements "common" to all AndroMDA components (tagged
 * values, stereotypes, and datatypes).
 *
 * @author Chad Brandon
 */
public class UMLProfile
{
    /* ----------------- Stereotypes -------------------- */

    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /**
     * Represents a criteria search.
     */
    public static final String STEREOTYPE_CRITERIA = profile.get("CRITERIA");

    /**
     * Represents a persistent entity.
     */
    public static final String STEREOTYPE_ENTITY = profile.get("ENTITY");

    /**
     * Represents a finder method on an entity.
     */
    public static final String STEREOTYPE_FINDER_METHOD = profile.get("FINDER_METHOD");

    /**
     * Represents the primary key of an entity.
     */
    public static final String STEREOTYPE_IDENTIFIER = profile.get("IDENTIFIER");

    /**
     * If an attribute has this stereotype, it is considered unique.
     */
    public static final String STEREOTYPE_UNIQUE = profile.get("UNIQUE");

    /**
     * Represents a service.
     */
    public static final String STEREOTYPE_SERVICE = profile.get("SERVICE");

    /**
     * Represents a value object.
     */
    public static final String STEREOTYPE_VALUE_OBJECT = profile.get("VALUE_OBJECT");

    /**
     * <p/>
     * Represents a web service. Stereotype a class with this stereotype when you want everything on the class to be
     * exposed as a web service. </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE = profile.get("WEBSERVICE");

    /**
     * <p/>
     * Stereotype an operation on a <code>service</code> if you wish to expose the operation. </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE_OPERATION = profile.get("WEBSERVICE_OPERATION");

    /**
     * The base exception stereotype. If a model element is stereotyped with this (or one of its specializations), then
     * the exception can be generated by a cartridge and a dependency to it from an operation will add a throws clause.
     */
    public static final String STEREOTYPE_EXCEPTION = profile.get("EXCEPTION");

    /**
     * Represents an enumeration type.
     */
    public static final String STEREOTYPE_ENUMERATION = profile.get("ENUMERATION");

    /**
     * Represents exceptions thrown during normal application processing (such as business exceptions). It extends the
     * base exception stereotype.
     */
    public static final String STEREOTYPE_APPLICATION_EXCEPTION = profile.get("APPLICATION_EXCEPTION");

    /**
     * Represents unexpected exceptions that can occur during application processing. This that a caller isn't expected
     * to handle.
     */
    public static final String STEREOTYPE_UNEXPECTED_EXCEPTION = profile.get("UNEXPECTED_EXCEPTION");

    /**
     * Represents a reference to an exception model element. Model dependencies to unstereotyped exception model
     * elements can be stereotyped with this. This allows the user to create a custom exception class since the
     * exception itself will not be generated but the references to it will be (i.e. the throws clause within an
     * operation).
     */
    public static final String STEREOTYPE_EXCEPTION_REF = profile.get("EXCEPTION_REF");

    /**
     * Used to indicate whether or not a parameter is nullable (since parameters do <strong>NOT </strong> allow
     * specification of multiplicity.
     */
    public static final String STEREOTYPE_NULLABLE = profile.get("NULLABLE");

    /**
     * Represents a manageable entity.
     */
    public static final String STEREOTYPE_MANAGEABLE = profile.get("MANAGEABLE");
    
    /**
     * Represents a "front end" use case (that is a use case used to model a presentation tier or "front end").
     */
    public static final String STEREOTYPE_FRONT_END_USECASE = profile.get("FRONT_END_USE_CASE");
    
    /**
     * Represents a "front end" use case that is the entry point to the presentation tier.
     */
    public static final String STEREOTYPE_FRONT_END_APPLICATION = profile.get("FRONT_END_APPLICATION");
    
    /**
     * Represents a "front end" view (that is it can represent a JSP page, etc).
     */
    public static final String STEREOTYPE_FRONT_END_VIEW = profile.get("FRONT_END_VIEW");
    
    /**
     * Represents an exception on a "front-end" view.
     */
    public static final String STEREOTYPE_FRONT_END_EXCEPTION = profile.get("FRONT_END_EXCEPTION");

    /* ----------------- Tagged Values -------------------- */

    /**
     * Represents documentation stored as a tagged value
     */
    public static final String TAGGEDVALUE_DOCUMENTATION = profile.get("DOCUMENTATION");
    
    /**
     * Represents a hyperlink stored as a tagged value.
     */
    public static final String TAGGEDVALUE_HYPERLINK = profile.get("HYPERLINK");

    /**
     * Represents a relational table name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TABLE = profile.get("PERSISTENCE_TABLE");

    /**
     * Represents a relational table column name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = profile.get("PERSISTENCE_COLUMN");

    /**
     * Represents a relational table column length
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = profile.get("PERSISTENCE_COLUMN_LENGTH");

    /**
     * Represents a relational table column index name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX = profile.get("PERSISTENCE_COLUMN_INDEX");
    
    /**
     * Indicates if a persistence type is immutable.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_IMMUTABLE = profile.get("PERSISTENCE_IMMUTABLE");

    /**
     * Used on an association end to indicate whether its owning entity should have its identifier also be the foreign
     * key to the related entity.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FOREIGN_IDENTIFIER = profile.get(
        "PERSISTENCE_FOREIGN_IDENTIFIER");
    
    /**
     * Used on an identifier to indicate whether or not the identifier is <em>assigned</em> (meaning
     * that the identifier is manually assigned instead of generated.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ASSIGNED_IDENTIFIER = profile.get(
        "PERSISTENCE_ASSIGNED_IDENTIFIER");

    /**
     * Used on an association end to denote to name of the foreign key constraint to use in the database.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME = profile.get(
        "PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME");
    
    /**
     * Used to assign the controller to the activity (when it can not be assigned explicity).
     */
    public static final String TAGGEDVALUE_PRESENTATION_CONTROLLER_USECASE = profile.get("PRESENTATION_CONTROLLER_USECASE");
    
    /**
     * Used to associate an activity to a use case (when it can not be assigned explicitly).
     */
    public static final String TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY = profile.get("PRESENTATION_USECASE_ACTIVITY");

    /**
     * Stores the style of a web service (document, wrapped, rpc).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_STYLE = profile.get("WEBSERVICE_STYLE");

    /**
     * Stores the use of a web service (literal, encoded).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_USE = profile.get("WEBSERVICE_USE");

    /**
     * Stores the provider of the web service (RPC, EJB).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_PROVIDER = profile.get("WEBSERVICE_PROVIDER");

    /**
     * Stores the name of the role (if it's different than the name of the actor stereotyped as role)
     */
    public static final String TAGGEDVALUE_ROLE_NAME = profile.get("ROLE_NAME");
    
    /**
     * Stores the serial version UID to be used for a class. If not specified, it will be calculated
     * based on the class signature.
     *
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogicImpl#handleGetSerialVersionUID()
     */
    public static final String TAGGEDVALUE_SERIALVERSION_UID = profile.get("SERIALVERSION_UID");

    /**
     * The attribute to use when referencing this table from another one.
     */
    public static final String TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME = profile.get("MANAGEABLE_DISPLAY_NAME");

    /**
     * The maximum number of records to load from the DB at the same time.
     */
    public static final String TAGGEDVALUE_MANAGEABLE_MAXIMUM_LIST_SIZE = profile.get("MANAGEABLE_MAXIMUM_LIST_SIZE");

    /**
     * The maximum number of records to show at the same time.
     */
    public static final String TAGGEDVALUE_MANAGEABLE_PAGE_SIZE = profile.get("MANAGEABLE_PAGE_SIZE");

    /**
     * Indicates whether or not the underlying entity keys should be resolved when referencing it.
     */
    public static final String TAGGEDVALUE_MANAGEABLE_RESOLVEABLE = profile.get("MANAGEABLE_RESOLVEABLE");

    /* ----------------- Data Types -------------------- */

    /**
     * Used to identify collection types in the model, any other type that will be identified as a collection must
     * specialize this type.
     */
    public static final String COLLECTION_TYPE_NAME = profile.get("COLLECTION_TYPE");

    /**
     * Used to identify a list type in the model, any other type that will be identified as a list must specialize this
     * type.
     */
    public static final String LIST_TYPE_NAME = profile.get("LIST_TYPE");

    /**
     * Used to identify a set type in the model, any other type that will be identified as a set must specialize this
     * type.
     */
    public static final String SET_TYPE_NAME = profile.get("SET_TYPE");

    /**
     * Used to identify date types in the model, any other type that will be identified as a date must specialize this
     * type.
     */
    public static final String DATE_TYPE_NAME = profile.get("DATE_TYPE");

    /**
     * Used to identify a boolean type in the model, any other type that will be identified as a boolean type must
     * specialize this type.
     */
    public static final String BOOLEAN_TYPE_NAME = profile.get("BOOLEAN_TYPE");

    /**
     * Used to identify a file type in the model, any other type that will be identified as a file type must specialize
     * this type.
     */
    public static final String FILE_TYPE_NAME = profile.get("FILE_TYPE");

    /**
     * Used to identify a Blob type in the model, any other type that will be identified as a Blob type must specialize
     * this type.
     */
    public static final String BLOB_TYPE_NAME = profile.get("BLOB_TYPE");

    /**
     * Used to identify a map type in the model, any other type that will be identified as a map type must specialize
     * this type.
     */
    public static final String MAP_TYPE_NAME = profile.get("MAP_TYPE");

    /**
     * Used to identify a string type in the model, any other type that will be identified as a string type must
     * specialize this type.
     */
    public static final String STRING_TYPE_NAME = profile.get("STRING_TYPE");
}