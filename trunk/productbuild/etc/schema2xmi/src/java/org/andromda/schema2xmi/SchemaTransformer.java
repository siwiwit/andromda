package org.andromda.schema2xmi;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.andromda.core.ModelProcessorException;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;

/**
 * Performs the transformation of database schema to XMI.
 * 
 * @author Chad Brandon
 */
public class SchemaTransformer
{

    private final static Logger logger = Logger
        .getLogger(SchemaTransformer.class);

    private RepositoryFacade repository = null;

    /**
     * The JDBC driver class
     */
    private String jdbcDriver = null;

    /**
     * The JDBC schema user.
     */
    private String jdbcUser = null;

    /**
     * The JDBC schema password.
     */
    private String jdbcPassword = null;

    /**
     * The JDBC connection URL.
     */
    private String jdbcConnectionUrl = null;

    /**
     * The name of the package in which the name of the elements will be
     * created.
     */
    private String packageName = null;
    
    /**
     * The regular expression pattern to match on when deciding 
     * what table names to add to the transformed XMI.
     */
    private String tableNamePattern = null;

    /**
     * Constructs a new instance of this SchemaTransformer.
     */
    public SchemaTransformer(
        String jdbcDriver,
        String jdbcConnectionUrl,
        String jdbcUser,
        String jdbcPassword)
    {
        final String constructorName = "SchemaTransformer";

        ExceptionUtils.checkEmpty(constructorName, "jdbcDriver", jdbcDriver);
        ExceptionUtils.checkEmpty(
            constructorName,
            "jdbcConnectionUrl",
            jdbcConnectionUrl);
        ExceptionUtils.checkEmpty(constructorName, "jdbcUser", jdbcUser);
        ExceptionUtils
            .checkEmpty(constructorName, "jdbcPassword", jdbcPassword);

        this.jdbcDriver = jdbcDriver;
        this.jdbcConnectionUrl = jdbcConnectionUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
        this.jdbcConnectionUrl = jdbcConnectionUrl;

        this.repository = (RepositoryFacade)ComponentContainer.instance()
            .findComponent(RepositoryFacade.class);

        if (repository == null)
        {
            throw new ModelProcessorException("No Repository could be found, "
                + "please make sure you have a "
                + RepositoryFacade.class.getName()
                + " instance on your classpath");
        }
        repository.open();
    }

    /**
     * Transforms the Schema file and writes it to the location given by
     * <code>outputLocation</code>. The <code>inputModel</code> must be a
     * valid URL, otherwise an exception will be thrown.
     * 
     * @param inputModel the location of the input model to start with (if there
     *        is one)
     * @param outputLocation The location to where the transformed output will
     *        be written.
     */
    public void transform(String inputModel, String outputLocation)
    {
        outputLocation = StringUtils.trimToEmpty(outputLocation);
        if (outputLocation == null)
        {
            throw new IllegalArgumentException(
                "'outputLocation' can not be null");
        }
        Connection connection = null;
        try
        {
            this.repository.readModel(new URL(inputModel), null);
            Class.forName(this.jdbcDriver);
            connection = DriverManager.getConnection(
                this.jdbcConnectionUrl,
                this.jdbcUser,
                this.jdbcPassword);
            repository.writeModel(transform(connection), outputLocation, "1.2");
        }
        catch (Throwable th)
        {
            throw new SchemaTransformerException(th);
        }
        finally
        {

            repository.close();
        }
    }

    /**
     * Sets the name of the package to which the model elements will be created.
     * 
     * @param packageName The packageName to set.
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    /**
     * Sets the regular expression pattern to match on when deciding 
     * what table names to add to the transformed XMI.
     * 
     * @param tableNamePattern The tableNamePattern to set.
     */
    public void setTableNamePattern(String tableNamePattern)
    {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * Performs the actual translation of the Schema to the XMI and returns the
     * object model.
     */
    private Object transform(Connection connection) throws Exception
    {
        UmlPackage umlPackage = (UmlPackage)this.repository.getModel()
            .getModel();

        ModelManagementPackage modelManagementPackage = umlPackage
            .getModelManagement();

        // A given XMI file can contain multiptle models.
        // Use the first model in the XMI file
        Model model = (Model)(modelManagementPackage.getModel().refAllOfType()
            .iterator().next());

        // create the package on the model
        org.omg.uml.modelmanagement.UmlPackage leafPackage = this
            .createPackage(umlPackage.getModelManagement(), model);
        this.createClasses(connection, umlPackage.getModelManagement()
            .getCore(), leafPackage);

        return umlPackage;
    }

    /**
     * Creates the actual model package from the class <code>packageName</code>
     * using the given <code>modelManagementPackage</code> and places it on
     * the <code>model</code> and returns the last leaf package.
     * 
     * @param modelManagementPackage from which we retrieve the UmlPackageClass
     *        to create a UmlPackage.
     * @param modelPackage the root UmlPackage
     */
    protected org.omg.uml.modelmanagement.UmlPackage createPackage(
        ModelManagementPackage modelManagementPackage,
        org.omg.uml.modelmanagement.UmlPackage modelPackage)
    {
        this.packageName = StringUtils.trimToEmpty(this.packageName);
        String[] packages = this.packageName.split("\\.");
        if (packages != null && packages.length > 0)
        {
            for (int ctr = 0; ctr < packages.length; ctr++)
            {
                org.omg.uml.modelmanagement.UmlPackage umlPackage = modelManagementPackage
                    .getUmlPackage().createUmlPackage(
                        packages[ctr],
                        VisibilityKindEnum.VK_PUBLIC,
                        false,
                        false,
                        false,
                        false);
                modelPackage.getOwnedElement().add(umlPackage);
                modelPackage = umlPackage;
            }
        }
        return modelPackage;
    }

    /**
     * Creates all classes from the tables found in the schema.
     * 
     * @param connection the Connection used to retrieve the schema metadata.
     * @param corePackage the CorePackage instance we use to create the classes.
     * @param modelPackage the package which the classes are added.
     */
    protected void createClasses(
        Connection connection,
        CorePackage corePackage,
        org.omg.uml.modelmanagement.UmlPackage modelPackage)
        throws SQLException
    {

        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tableRs = metadata.getTables(null, null, null, new String[]
        {
            "TABLE",
        });
        while (tableRs.next())
        {
            String name = tableRs.getString("TABLE_NAME");
            if (StringUtils.isNotBlank(this.tableNamePattern))
            {
                if (name.matches(this.tableNamePattern))
                {
                   if (logger.isInfoEnabled())
                       logger.info("adding table --> '" + name + "' to model");
                   UmlClass umlClass = this.createClass(corePackage, name);
                   modelPackage.getOwnedElement().add(umlClass);
                }              
            }
            else 
            {
                UmlClass umlClass = this.createClass(corePackage, name); 
                modelPackage.getOwnedElement().add(umlClass);
            }
        }
    }
    
    /**
     * Creates and returns a UmlClass with the given <code>name</code> 
     * using the <code>corePackage</code> to create it.
     * @param corePackage used to create the class.
     * @param name the name to create the class with
     * @return the UmlClass
     */
    private UmlClass createClass(CorePackage corePackage, String name)
    {
        return corePackage.getUmlClass().createUmlClass(
            toClassName(name),
            VisibilityKindEnum.VK_PUBLIC,
            false,
            false,
            false,
            false,
            false);
    }
    
    /**
     * Turns a table name into a model element class name.
     * 
     * @param name the table name.
     * @return the new class name.
     */
    private String toClassName(String name)
    {
        StringBuffer buffer = new StringBuffer();
        String[] tokens = name.split("_");
        if (tokens != null && tokens.length > 0)
        {
            for (int ctr = 0; ctr < tokens.length; ctr++)
            {
                buffer.append(StringUtils.capitalize(tokens[ctr].toLowerCase()));
            }
        } 
        else 
        {
            buffer.append(StringUtils.capitalize(name.toLowerCase()));
        }
        return buffer.toString();    
    }
    
}