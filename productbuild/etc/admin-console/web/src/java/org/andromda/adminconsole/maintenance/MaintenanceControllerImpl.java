package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.db.*;
import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;

/**
 * @see org.andromda.adminconsole.maintenance.MaintenanceController
 */
public class MaintenanceControllerImpl extends MaintenanceController
{
    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTableData
     */
    public final void loadTableData(ActionMapping mapping, org.andromda.adminconsole.maintenance.LoadTableDataForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        MetaDataSession metadataSession = getMetaDataSession(request);

        Table table = (Table)metadataSession.getTables().get( form.getTable() );
        if (table == null)
        {
            table = metadataSession.getCurrentTable();
            if (table == null)
                throw new IllegalArgumentException("Table could not be located: "+form.getTable());
        }

        metadataSession.setCurrentTable( table );
        form.setTableData( table.findAllRows() );

        form.setTableValueList( getMetaDataSession(request).getTableNames().toArray() );
    }

    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTables
     */
    public final void loadTables(ActionMapping mapping, org.andromda.adminconsole.maintenance.LoadTablesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);

        Database database = DatabaseFactory.create(
                loginSession.getUrl(), loginSession.getSchema(),
                loginSession.getUser(), loginSession.getPassword());
        
        Table[] tables = database.getTables(new TableType[]{TableType.TABLE});

        Map tableMap = new LinkedHashMap();
        for (int i = 0; i < tables.length; i++)
        {
            Table table = tables[i];
            tableMap.put(table.getName(), table);
        }

        Object[] tableNames = tableMap.keySet().toArray();
        Arrays.sort(tableNames);

        MetaDataSession metadataSession = getMetaDataSession(request);
        metadataSession.setTables( tableMap );
        metadataSession.setTableNames( Arrays.asList(tableNames) );

        if (tables.length < 1)
        {
            throw new Exception("No tables could be found");
        }
        else
        {
            metadataSession.setCurrentTable( tables[0] );
        }

    }

    public void registerLoginInformation(ActionMapping mapping, RegisterLoginInformationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);

        loginSession.setUrl( StringUtils.trim(form.getUrl()) );
        loginSession.setSchema( StringUtils.trimToNull(form.getSchema()) );
        loginSession.setUser( StringUtils.trimToEmpty(form.getUser()) );
        loginSession.setPassword( StringUtils.trimToEmpty(form.getPassword()) );
    }

    public void loadConfigurator(ActionMapping mapping, LoadConfiguratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        AdminConsoleConfigurator configurator = null;

        try
        {
            InputStream instream = Thread.currentThread().getContextClassLoader().getResourceAsStream(AdminConsoleConfigurator.FILE_NAME);
            Reader reader = new InputStreamReader(instream);
            configurator = new AdminConsoleConfigurator(reader);
            reader.close();
            instream.close();
        }
        catch (Exception e)
        {
            configurator = new AdminConsoleConfigurator();
        }

        getDatabaseLoginSession(request).setConfigurator(configurator);
    }

    public void insertRow(ActionMapping mapping, InsertRowForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Object[] parameters = form.getParametersAsArray();

        Table table = getMetaDataSession(request).getCurrentTable();

        if (table==null)
        {
            throw new Exception("Unable to insert row in table: current table not specified");
        }
        else
        {
            table.insertRow(parameters);
        }
    }
}
