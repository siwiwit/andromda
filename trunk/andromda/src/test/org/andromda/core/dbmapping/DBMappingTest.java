package org.andromda.core.dbmapping;

import java.io.File;

import junit.framework.TestCase;

/**
 * <p>Test for reading the XML-formatted database mapping files.</p>
 * 
 * @author Matthias Bohlen
 *
 */
public class DBMappingTest extends TestCase
{

    /**
     * Constructor for DBMappingTest.
     * @param arg0
     */
    public DBMappingTest(String arg0)
    {
        super(arg0);
    }

    private Mappings fMappings;
    private JAXBDbMappingTable fMappingTable;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // the current input file relative to the baseDir
        File inFile = new File("simple2ejb", "TypeMapping.xml");

        fMappingTable = new JAXBDbMappingTable();
        fMappingTable.read(inFile);
    }

    public void testUnmarshal() throws Exception
    {
        // just check for first element
        Mapping m = (Mapping) fMappings.getMappings().get(0);
        assertEquals("java.lang.String", m.getTypes().get(0));
        assertEquals("String", m.getTypes().get(1));
        assertEquals("VARCHAR", m.getJdbcType().getName());
        assertEquals("VARCHAR({0})", m.getSqlType().getPattern());
        assertEquals("255", m.getSqlType().getDefaultLength());
    }

    public void testMappingTable() throws Exception
    {
        assertEquals("INTEGER", fMappingTable.getJDBCType("int"));
        assertEquals("VARCHAR", fMappingTable.getJDBCType("String"));
        assertEquals(
            "VARCHAR",
            fMappingTable.getJDBCType("java.lang.String"));
        assertEquals(
            "DECIMAL",
            fMappingTable.getJDBCType("java.math.BigDecimal"));
        assertEquals(
            "** MISSING JDBC type mapping for my.undefined.Datatype",
            fMappingTable.getJDBCType("my.undefined.Datatype"));

        assertEquals(
            "VARCHAR(125)",
            fMappingTable.getSQLType("java.lang.String", "125"));

        assertEquals(
            "DECIMAL(10)",
            fMappingTable.getSQLType("java.math.BigDecimal", "10"));
        assertEquals(
            "DECIMAL()",
            fMappingTable.getSQLType("java.math.BigDecimal", ""));

        assertEquals(
            "DATE",
            fMappingTable.getSQLType("java.util.Date", "10"));

        assertEquals(
            "INTEGER(9)",
            fMappingTable.getSQLType("java.lang.Integer", ""));
        assertEquals(
            "INTEGER(3)",
            fMappingTable.getSQLType("java.lang.Integer", "3"));

        assertEquals(
            "** MISSING SQL type mapping for my.undefined.Datatype",
            fMappingTable.getSQLType("my.undefined.Datatype", "3"));
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        fMappings = null;
    }

}
