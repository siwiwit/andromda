package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.andromda.adminconsole.db.PrimaryKeyColumn;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ColumnImpl extends DatabaseObject implements Column
{
    private String name = null;
    private boolean nullable = false;
    private Class type = null;
    private int sqlType = 0;
    private int size = 0;
    private String remarks = null;
    private int ordinalPosition = 0;

    private Table table = null;

    public ColumnImpl(Table table, String name, int sqlType)
    {
        this.table = table;
        this.name = name;
        this.sqlType = sqlType;
        this.loadMetaData();
    }

    /**
     * Convenient accessor to the catalog.
     */
    protected String getCatalog()
    {
        return getTable().getDatabase().getCatalog();
    }

    /**
     * Convenient accessor to the schema.
     */
    protected String getSchema()
    {
        return getTable().getDatabase().getSchema();
    }

    /**
     * Convenient accessor to the databse metadata.
     */
    protected DatabaseMetaData getMetaData()
    {
        return getTable().getDatabase().getMetaData();
    }

    public Table getTable()
    {
        return table;
    }

    public String getName()
    {
        return name;
    }

    public boolean isNullable()
    {
        return nullable;
    }

    public Class getType()
    {
        return type;
    }

    public int getSqlType()
    {
        return sqlType;
    }

    public int getSize()
    {
        return size;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public int getOrdinalPosition()
    {
        return ordinalPosition;
    }

    public boolean isBooleanType()
    {
        return Boolean.class.isAssignableFrom(getType());
    }

    public boolean isNumericType()
    {
        Class type = getType();
        return Integer.class.isAssignableFrom(type) ||
                Long.class.isAssignableFrom(type) ||
                Short.class.isAssignableFrom(type) ||
                Double.class.isAssignableFrom(type) ||
                Float.class.isAssignableFrom(type);
    }

    public boolean isStringType()
    {
        return String.class.isAssignableFrom(getType());
    }

    public boolean isForeignKeyColumn()
    {
        return this instanceof ForeignKeyColumn;
    }

    public boolean isPrimaryKeyColumn()
    {
        return this instanceof PrimaryKeyColumn;
    }

    /**
     * Loads the metadata information for this column
     */
    private void loadMetaData()
    {
        try
        {
            ResultSet resultSet = null;

            // TYPE
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (resultSet.next())
                {
                    type = toJavaType(resultSet.getInt("DATA_TYPE"));
                    remarks = resultSet.getString("REMARKS");
                    ordinalPosition = resultSet.getInt("ORDINAL_POSITION") - 1;
                    size = resultSet.getInt("COLUMN_SIZE");
                    nullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                }
                else
                    throw new RuntimeException("Unable to retrieve column metadata: " + name);
            }
            finally
            {
                close(resultSet);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
    }

    /**
     * Converts an SQL type to a Java class
     */
    protected Class toJavaType(int type)
    {
        switch (type)
        {
            case Types.VARCHAR:
                return java.lang.String.class;
            case Types.BIGINT:
                return java.lang.Long.class;
            case Types.BINARY:
                return java.lang.Boolean.class;
            case Types.BIT:
                return java.lang.Boolean.class;
            case Types.BOOLEAN:
                return java.lang.Boolean.class;
            case Types.CHAR:
                return java.lang.Character.class;
            case Types.DATE:
                return java.util.Date.class;
            case Types.DECIMAL:
                return java.lang.Integer.class;
            case Types.DOUBLE:
                return java.lang.Double.class;
            case Types.FLOAT:
                return java.lang.Float.class;
            case Types.INTEGER:
                return java.lang.Integer.class;
            case Types.JAVA_OBJECT:
                return java.lang.Object.class;
            case Types.LONGVARCHAR:
                return java.lang.String.class;
            case Types.NUMERIC:
                return java.lang.Integer.class;
            case Types.REAL:
                return java.lang.Double.class;
            case Types.SMALLINT:
                return java.lang.Short.class;
            case Types.TIME:
                return java.util.Date.class;
            case Types.TIMESTAMP:
                return java.util.Date.class;
            case Types.TINYINT:
                return java.lang.Short.class;
            case Types.OTHER:
                return java.lang.Object.class;
            default:
                throw new IllegalArgumentException("Unsupported SQL Type: " + type);
        }
    }

    public String toString()
    {
        return getName();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ColumnImpl)) return false;

        final ColumnImpl column = (ColumnImpl) o;

        if (!name.equals(column.name)) return false;
        if (!table.equals(column.table)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = name.hashCode();
        result = 29 * result + table.hashCode();
        return result;
    }
}
