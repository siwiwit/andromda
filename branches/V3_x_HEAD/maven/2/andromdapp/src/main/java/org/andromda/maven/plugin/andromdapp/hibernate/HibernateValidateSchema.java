package org.andromda.maven.plugin.andromdapp.hibernate;

import org.andromda.core.common.ResourceWriter;
import org.andromda.core.common.Constants;

import java.util.Map;
import java.util.List;
import java.util.Random;
import java.io.File;

public class HibernateValidateSchema
    extends HibernateSchemaManagement
{
    protected String getExecutionClassName()
    {
        return "SchemaValidator";
    }

    protected String getExecutionOuputPath(final Map options)
    {
        return null;
    }

    private static final String HIBERNATE_PROPERTIES_TEMP_DIRECTORY =
        Constants.TEMPORARY_DIRECTORY + "andromdapp/hibernate-schema-validate";

    protected void addArguments(final Map options, final List arguments) throws Exception
    {
        final String driverClass = this.getRequiredProperty(
                options,
                "jdbcDriver");
        final String connectionUrl = this.getRequiredProperty(
                options,
                "jdbcConnectionUrl");
        final String username = this.getRequiredProperty(
                options,
                "jdbcUsername");
        final String password = this.getRequiredProperty(
                options,
                "jdbcPassword");
        final StringBuffer contents = new StringBuffer();
        contents.append("hibernate.connection.driver_class=" + driverClass + "\n");
        contents.append("hibernate.connection.url=" + connectionUrl + "\n");
        contents.append("hibernate.connection.username=" + username + "\n");
        contents.append("hibernate.connection.password=" + password + "\n");
        final File temporaryProperitesFile =
            new File(HIBERNATE_PROPERTIES_TEMP_DIRECTORY, new Random().nextDouble() + "");
        temporaryProperitesFile.deleteOnExit();
        ResourceWriter.instance().writeStringToFile(
            contents.toString(),
            temporaryProperitesFile.toString());
        arguments.add("--properties=" + temporaryProperitesFile);
    }
}
