package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Criterion;
import org.andromda.adminconsole.db.Database;
import org.andromda.adminconsole.db.DatabaseFactory;
import org.andromda.adminconsole.db.Expression;
import org.andromda.adminconsole.db.RowData;
import org.andromda.adminconsole.db.Table;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        MetaDataSession metaDataSession = getMetaDataSession(request);

        if (form.getName() != null && metaDataSession.getTableNames().contains(form.getName()) == false)
        {
            throw new Exception("Trying to load unknown table (contact your System Administrator): "+form.getName());
        }

        Table table = null;

        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);
        List tableNames = metaDataSession.getTableNames();

        if (form.getName() == null)
        {
            // try to reload the current table
            table = metaDataSession.getCurrentTable();

            if (table == null)
            {
                // take the first known table (there is at least one otherwise loadTableNames
                // would have thrown an exception
                table = loginSession.getDatabase().findTable((String)tableNames.iterator().next());
            }
        }
        else
        {
            table = loginSession.getDatabase().findTable(form.getName());
        }

        if (table != null)
        {
            metaDataSession.setCurrentTable( table );

            AdminConsoleConfigurator configurator = loginSession.getConfigurator();
            TableConfiguration tableConfiguration = configurator.getTableConfiguration(table.getName());
            metaDataSession.setCurrentTableData( table.findAllRows( tableConfiguration.getMaxListSize() ) );
        }
    }

    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTables
     */
    public void loadTableNames(ActionMapping mapping, LoadTableNamesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);
        AdminConsoleConfigurator configurator = loginSession.getConfigurator();

        // create a link to the database
        Database database = DatabaseFactory.create(
                loginSession.getUrl(), loginSession.getSchema(),
                loginSession.getUser(), loginSession.getPassword());

        // register this database connection  to the session
        loginSession.setDatabase(database);

        boolean allowUnconfiguredTables = configurator.isUnconfiguredTablesAvailable();
        List knownTableNames = loginSession.getConfigurator().getKnownDatabaseTableNames();

        List tableNames = Arrays.asList(database.getAllTableNames());

        if (allowUnconfiguredTables == false)
        {
            tableNames = new ArrayList(CollectionUtils.intersection(tableNames, knownTableNames));
        }

        Collections.sort(tableNames);

        MetaDataSession metadataSession = getMetaDataSession(request);
        metadataSession.setTableNames( tableNames );

        if (tableNames.size() > 0)
        {
            Table currentTable = database.findTable((String)tableNames.get(0));
            metadataSession.setCurrentTable( currentTable );
        }
        else
        {
            throw new Exception("No tables could be found");
        }
    }

    public void registerLoginInformation(ActionMapping mapping, RegisterLoginInformationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);
        AdminConsoleConfigurator configurator = loginSession.getConfigurator();

        if ( configurator.isArbitraryUrlAllowed() == false)
        {
            List knownUrls = configurator.getKnownDatabaseUrls();
            boolean validUrl = false;
            for (int i = 0; i < knownUrls.size() && !validUrl; i++)
            {
                String url = ((AdminConsoleConfigurator.DbUrl) knownUrls.get(i)).getValue();
                if (url != null && url.equals(form.getUrl()))
                {
                    validUrl = true;
                }
            }
            if (validUrl == false)
            {
                throw new Exception("You are not allowed to login into this database (contact your System Administrator): "+form.getUrl());
            }
        }

        loginSession.setUrl( StringUtils.trim(form.getUrl()) );
        loginSession.setSchema( StringUtils.trimToNull(form.getSchema()) );
        loginSession.setUser( StringUtils.trimToEmpty(form.getUser()) );
        loginSession.setPassword( StringUtils.trimToEmpty(form.getPassword()) );
    }

    public void loadConfigurator(ActionMapping mapping, LoadConfiguratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getDatabaseLoginSession(request).setConfigurator(new AdminConsoleConfigurator());
    }

    private final static String COOKIE_NAME = "admin.console";
    private final static String COOKIE_VALUE_SEPARATOR = "::::";

    public void loadPreferences(ActionMapping mapping, LoadPreferencesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Cookie[] cookies = request.getCookies();

        boolean cookieLoaded = false;
        if (cookies != null)
        {
            for (int i = 0; i < cookies.length && !cookieLoaded; i++)
            {
                Cookie cookie = cookies[i];
                if (COOKIE_NAME.equals(cookie.getName()))
                {
                    String value = cookie.getValue();
                    int separatorIndex = value.indexOf(COOKIE_VALUE_SEPARATOR);

                    if (separatorIndex != -1)
                    {
                        String user = value.substring(0, separatorIndex);
                        String url = value.substring(separatorIndex + COOKIE_VALUE_SEPARATOR.length());

                        form.setUser(user);
                        form.setUrl(url);
                    }
                    cookieLoaded = true;
                }
            }
        }
    }

    public void storePreferences(ActionMapping mapping, StorePreferencesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Cookie cookie = new Cookie(COOKIE_NAME, form.getUser() + COOKIE_VALUE_SEPARATOR + form.getUrl());
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }

    public void deleteRows(ActionMapping mapping, DeleteRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // the list of rows selected for deletion
        Object[] rowNumbers = form.getSelectedRowsAsArray();

        // has something been selected for deletion ?
        if (rowNumbers == null || rowNumbers.length==0)
        {
            return;
        }

        // the current metadata
        MetaDataSession metaDataSession = getMetaDataSession(request);
        List tableData = metaDataSession.getCurrentTableData();

        // if the table has a primary key use it to identify the row, otherwise use all columns
        Table table = metaDataSession.getCurrentTable();
        final Column[] identityColumns =
                (table.getPrimaryKeyColumnCount() > 0) ? table.getPrimaryKeyColumns() : table.getColumns();

        // collect all selected rows in a list
        List selectedData = new ArrayList();
        for (int i = 0; i < rowNumbers.length; i++)
        {
            int rowNumber = Integer.parseInt((String)rowNumbers[i]);
            selectedData.add( tableData.get(rowNumber) );
        }

        for (int i = 0; i < selectedData.size(); i++)
        {
            RowData rowData = (RowData) selectedData.get(i);
            // find out how this row is uniquely identified in its table
            Criterion identityCriterion = createCriterion(identityColumns, rowData);
            // delete this row
            table.deleteRow(identityCriterion);
        }
    }

    public void updateRows(ActionMapping mapping, UpdateRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // the list of rows selected for update
        Object[] rowNumbers = form.getSelectedRowsAsArray();

        // was something selected for update ?
        if (rowNumbers == null || rowNumbers.length==0)
        {
            return;
        }

        // the current metadata
        MetaDataSession metaDataSession = getMetaDataSession(request);
        List tableData = metaDataSession.getCurrentTableData();

        // if the table has a primary key use it to identify the row, otherwise use all columns
        Table table = metaDataSession.getCurrentTable();
        final Column[] identityColumns =
                (table.getPrimaryKeyColumnCount() > 0) ? table.getPrimaryKeyColumns() : table.getColumns();

        // get the columns we might need to set
        Column[] tableColumns = table.getColumns();

        // this object represents the new data we will persist
        RowData newRowData = new RowData();

        // get the configurator to find out which column is updateable
        AdminConsoleConfigurator configurator = getDatabaseLoginSession(request).getConfigurator();

        // loop over the rows the user selected for update
        for (int i = 0; i < rowNumbers.length; i++)
        {
            int rowNumber = Integer.parseInt((String)rowNumbers[i]);
            RowData rowData = (RowData) tableData.get(rowNumber);

            for (int j = 0; j < tableColumns.length; j++)
            {
                Column column = tableColumns[j];
                ColumnConfiguration columnConfiguration = configurator.getColumnConfiguration(table.getName(), column.getName());

                // only record the parameter when it is allowed for update
                if (columnConfiguration.getUpdateable())
                {
                    String parameter = request.getParameter(rowNumber + ":" + column.getName());
                    newRowData.put(column.getName(), parameter); // @todo type conversion ???
                }
            }

            // create the criterion used to uniquely identify the selected row in its table
            Criterion identityCriterion = createCriterion(identityColumns, rowData);

            // update the old row by setting only the updateable fields
            table.updateRow(newRowData, identityCriterion);
        }

        metaDataSession.setCurrentTableData(table.findAllRows());
    }

    private Criterion createCriterion(Column[] columns, RowData rowData)
    {
        Criterion criterion = null;

        if (columns!=null && columns.length>0)
        {
            criterion = Expression.equal(columns[0], rowData.get(columns[0].getName()));

            for (int i = 1; i < columns.length; i++)
            {
                criterion = Expression.and( criterion, Expression.equal(columns[i], rowData.get(columns[i].getName())) );
            }
        }
        return criterion;
    }

    public void insertRow(ActionMapping mapping, InsertRowForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        RowData rowData = new RowData();

        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry parameterPair = (Map.Entry) iterator.next();
            String parameterName = (String)parameterPair.getKey();
            String[] parameterValues = (String[])parameterPair.getValue();
            rowData.put(parameterName, StringUtils.trimToNull(parameterValues[0]));  // minimum array length is 1
        }

        Table table = getMetaDataSession(request).getCurrentTable();

        if (table==null)
        {
            throw new Exception("Unable to insert row in table: current table not specified");
        }
        else
        {
            table.insertRow(rowData);
            getMetaDataSession(request).setCurrentTableData(table.findAllRows());
        }
    }

    public void searchTableData(ActionMapping mapping, SearchTableDataForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        MetaDataSession metaDataSession = getMetaDataSession(request);
        Table table = metaDataSession.getCurrentTable();

        if (table==null)
        {
            throw new Exception("Unable to find rows in table: current table not specified");
        }

        Map parameterMap = request.getParameterMap();

        if (parameterMap.size() == 0)
        {
            metaDataSession.setCurrentTableData( table.findAllRows() );
        }
        else
        {
            Criterion criterion = null;

            Column[] columns = table.getColumns();
            for (int i = 0; i < columns.length; i++)
            {
                Column column = columns[i];
                String parameter = request.getParameter(column.getName());

/*
                // if column is a foreign key column then we need to find its display name and search on its value
                if (column.isForeignKeyColumn())
                {
                    ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) column;
                    foreignKeyColumn.getImportedKeyColumn().getTable().get
                    ColumnConfiguration configuration = getDatabaseLoginSession(request).getConfigurator().getConfiguration(column);
                    if (configuration.get)
                }
*/
                // only included those parameters that contain an actual value
                if (StringUtils.isNotBlank(parameter))
                {
                    Criterion lastCriterion =
                            (form.getExactMatches())
                                ? Expression.equal(column, parameter)
                                : Expression.like(column, '%' + parameter + '%');
                    criterion =  (criterion == null) ? lastCriterion : Expression.and(criterion, lastCriterion);
                }
            }

            List currentTableData = (criterion == null) ? table.findAllRows() : table.findRows(criterion);
            metaDataSession.setCurrentTableData( currentTableData );
        }
    }

    public void deleteUnreferencedRows(ActionMapping mapping, DeleteUnreferencedRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {

    }
}
