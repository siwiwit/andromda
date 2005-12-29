package org.andromda.utils;

import junit.framework.TestCase;

public class JavaTypeConverterTest 
    extends TestCase
{

    public static final class ExpectedResult
    {
        public String fromType;
        public String toType;
        public String expected;
        
        public ExpectedResult(
            String fromType,
            String toType,
            String expected)
        {
            this.fromType = fromType;
            this.toType = toType;
            this.expected = expected;
        }
    }
    
    
    final static ExpectedResult[] expected =
    {
        new ExpectedResult("int", "int", "sourceVal"),
        new ExpectedResult("int", "long", "(long)sourceVal"),
        new ExpectedResult("int", "double", "(double)sourceVal"),
        new ExpectedResult("int", "float", "(float)sourceVal"),
        new ExpectedResult("int", "boolean", "(sourceVal != 0)"),
        new ExpectedResult("int", "java.lang.Integer", "new java.lang.Integer(sourceVal)"),
        new ExpectedResult("int", "java.lang.Long", "new java.lang.Long((long)sourceVal)"),
        new ExpectedResult("int", "java.lang.Double", "new java.lang.Double((double)sourceVal)"),
        new ExpectedResult("int", "java.lang.Float", "new java.lang.Float((float)sourceVal)"),
        new ExpectedResult("int", "java.lang.Boolean", "new java.lang.Boolean((sourceVal != 0))"),
        new ExpectedResult("int", "java.util.Date", null),
        new ExpectedResult("int", "java.sql.Timestamp", null),
        new ExpectedResult("int", "java.sql.Time", null),
        new ExpectedResult("int", "java.sql.Date", null),
        new ExpectedResult("int", "java.lang.String", "java.lang.String.valueOf(sourceVal)"),
        new ExpectedResult("int", "com.example.UserClass", null),
        new ExpectedResult("long", "int", "(int)sourceVal"),
        new ExpectedResult("long", "long", "sourceVal"),
        new ExpectedResult("long", "double", "(double)sourceVal"),
        new ExpectedResult("long", "float", "(float)sourceVal"),
        new ExpectedResult("long", "boolean", "(sourceVal != 0)"),
        new ExpectedResult("long", "java.lang.Integer", "new java.lang.Integer((int)sourceVal)"),
        new ExpectedResult("long", "java.lang.Long", "new java.lang.Long(sourceVal)"),
        new ExpectedResult("long", "java.lang.Double", "new java.lang.Double((double)sourceVal)"),
        new ExpectedResult("long", "java.lang.Float", "new java.lang.Float((float)sourceVal)"),
        new ExpectedResult("long", "java.lang.Boolean", "new java.lang.Boolean((sourceVal != 0))"),
        new ExpectedResult("long", "java.util.Date", "new java.util.Date(sourceVal)"),
        new ExpectedResult("long", "java.sql.Timestamp", null),
        new ExpectedResult("long", "java.sql.Time", null),
        new ExpectedResult("long", "java.sql.Date", null),
        new ExpectedResult("long", "java.lang.String", "java.lang.String.valueOf(sourceVal)"),
        new ExpectedResult("long", "com.example.UserClass", null),
        new ExpectedResult("double", "int", "(int)sourceVal"),
        new ExpectedResult("double", "long", "(long)sourceVal"),
        new ExpectedResult("double", "double", "sourceVal"),
        new ExpectedResult("double", "float", "(float)sourceVal"),
        new ExpectedResult("double", "boolean", null),
        new ExpectedResult("double", "java.lang.Integer", "new java.lang.Integer((int)sourceVal)"),
        new ExpectedResult("double", "java.lang.Long", "new java.lang.Long((long)sourceVal)"),
        new ExpectedResult("double", "java.lang.Double", "new java.lang.Double(sourceVal)"),
        new ExpectedResult("double", "java.lang.Float", "new java.lang.Float((float)sourceVal)"),
        new ExpectedResult("double", "java.lang.Boolean", null),
        new ExpectedResult("double", "java.util.Date", null),
        new ExpectedResult("double", "java.sql.Timestamp", null),
        new ExpectedResult("double", "java.sql.Time", null),
        new ExpectedResult("double", "java.sql.Date", null),
        new ExpectedResult("double", "java.lang.String", "java.lang.String.valueOf(sourceVal)"),
        new ExpectedResult("double", "com.example.UserClass", null),
        new ExpectedResult("float", "int", "(int)sourceVal"),
        new ExpectedResult("float", "long", "(long)sourceVal"),
        new ExpectedResult("float", "double", "(double)sourceVal"),
        new ExpectedResult("float", "float", "sourceVal"),
        new ExpectedResult("float", "boolean", null),
        new ExpectedResult("float", "java.lang.Integer", "new java.lang.Integer((int)sourceVal)"),
        new ExpectedResult("float", "java.lang.Long", "new java.lang.Long((long)sourceVal)"),
        new ExpectedResult("float", "java.lang.Double", "new java.lang.Double((double)sourceVal)"),
        new ExpectedResult("float", "java.lang.Float", "new java.lang.Float(sourceVal)"),
        new ExpectedResult("float", "java.lang.Boolean", null),
        new ExpectedResult("float", "java.util.Date", null),
        new ExpectedResult("float", "java.sql.Timestamp", null),
        new ExpectedResult("float", "java.sql.Time", null),
        new ExpectedResult("float", "java.sql.Date", null),
        new ExpectedResult("float", "java.lang.String", "java.lang.String.valueOf(sourceVal)"),
        new ExpectedResult("float", "com.example.UserClass", null),
        new ExpectedResult("boolean", "int", "(sourceVal ? 1 : 0)"),
        new ExpectedResult("boolean", "long", "(sourceVal ? 1 : 0)"),
        new ExpectedResult("boolean", "double", null),
        new ExpectedResult("boolean", "float", null),
        new ExpectedResult("boolean", "boolean", "sourceVal"),
        new ExpectedResult("boolean", "java.lang.Integer", "new java.lang.Integer((sourceVal ? 1 : 0))"),
        new ExpectedResult("boolean", "java.lang.Long", "new java.lang.Long((sourceVal ? 1 : 0))"),
        new ExpectedResult("boolean", "java.lang.Double", null),
        new ExpectedResult("boolean", "java.lang.Float", null),
        new ExpectedResult("boolean", "java.lang.Boolean", "new java.lang.Boolean(sourceVal)"),
        new ExpectedResult("boolean", "java.util.Date", null),
        new ExpectedResult("boolean", "java.sql.Timestamp", null),
        new ExpectedResult("boolean", "java.sql.Time", null),
        new ExpectedResult("boolean", "java.sql.Date", null),
        new ExpectedResult("boolean", "java.lang.String", "java.lang.String.valueOf(sourceVal)"),
        new ExpectedResult("boolean", "com.example.UserClass", null),
        new ExpectedResult("java.lang.Integer", "int", "sourceVal.intValue()"),
        new ExpectedResult("java.lang.Integer", "long", "(long)sourceVal.intValue()"),
        new ExpectedResult("java.lang.Integer", "double", "(double)sourceVal.intValue()"),
        new ExpectedResult("java.lang.Integer", "float", "(float)sourceVal.intValue()"),
        new ExpectedResult("java.lang.Integer", "boolean", "(sourceVal.intValue() != 0)"),
        new ExpectedResult("java.lang.Integer", "java.lang.Integer", "sourceVal"),
        new ExpectedResult("java.lang.Integer", "java.lang.Long", "new java.lang.Long((long)sourceVal.intValue())"),
        new ExpectedResult("java.lang.Integer", "java.lang.Double", "new java.lang.Double((double)sourceVal.intValue())"),
        new ExpectedResult("java.lang.Integer", "java.lang.Float", "new java.lang.Float((float)sourceVal.intValue())"),
        new ExpectedResult("java.lang.Integer", "java.lang.Boolean", "new java.lang.Boolean((sourceVal.intValue() != 0))"),
        new ExpectedResult("java.lang.Integer", "java.util.Date", null),
        new ExpectedResult("java.lang.Integer", "java.sql.Timestamp", null),
        new ExpectedResult("java.lang.Integer", "java.sql.Time", null),
        new ExpectedResult("java.lang.Integer", "java.sql.Date", null),
        new ExpectedResult("java.lang.Integer", "java.lang.String", "sourceVal.toString()"),
        new ExpectedResult("java.lang.Integer", "com.example.UserClass", null),
        new ExpectedResult("java.lang.Long", "int", "(int)sourceVal.longValue()"),
        new ExpectedResult("java.lang.Long", "long", "sourceVal.longValue()"),
        new ExpectedResult("java.lang.Long", "double", "(double)sourceVal.longValue()"),
        new ExpectedResult("java.lang.Long", "float", "(float)sourceVal.longValue()"),
        new ExpectedResult("java.lang.Long", "boolean", "(sourceVal.longValue() != 0)"),
        new ExpectedResult("java.lang.Long", "java.lang.Integer", "new java.lang.Integer((int)sourceVal.longValue())"),
        new ExpectedResult("java.lang.Long", "java.lang.Long", "sourceVal"),
        new ExpectedResult("java.lang.Long", "java.lang.Double", "new java.lang.Double((double)sourceVal.longValue())"),
        new ExpectedResult("java.lang.Long", "java.lang.Float", "new java.lang.Float((float)sourceVal.longValue())"),
        new ExpectedResult("java.lang.Long", "java.lang.Boolean", "new java.lang.Boolean((sourceVal.longValue() != 0))"),
        new ExpectedResult("java.lang.Long", "java.util.Date", "new java.util.Date(sourceVal.longValue())"),
        new ExpectedResult("java.lang.Long", "java.sql.Timestamp", null),
        new ExpectedResult("java.lang.Long", "java.sql.Time", null),
        new ExpectedResult("java.lang.Long", "java.sql.Date", null),
        new ExpectedResult("java.lang.Long", "java.lang.String", "sourceVal.toString()"),
        new ExpectedResult("java.lang.Long", "com.example.UserClass", null),
        new ExpectedResult("java.lang.Double", "int", "(int)sourceVal.doubleValue()"),
        new ExpectedResult("java.lang.Double", "long", "(long)sourceVal.doubleValue()"),
        new ExpectedResult("java.lang.Double", "double", "sourceVal.doubleValue()"),
        new ExpectedResult("java.lang.Double", "float", "(float)sourceVal.doubleValue()"),
        new ExpectedResult("java.lang.Double", "boolean", null),
        new ExpectedResult("java.lang.Double", "java.lang.Integer", "new java.lang.Integer((int)sourceVal.doubleValue())"),
        new ExpectedResult("java.lang.Double", "java.lang.Long", "new java.lang.Long((long)sourceVal.doubleValue())"),
        new ExpectedResult("java.lang.Double", "java.lang.Double", "sourceVal"),
        new ExpectedResult("java.lang.Double", "java.lang.Float", "new java.lang.Float((float)sourceVal.doubleValue())"),
        new ExpectedResult("java.lang.Double", "java.lang.Boolean", null),
        new ExpectedResult("java.lang.Double", "java.util.Date", null),
        new ExpectedResult("java.lang.Double", "java.sql.Timestamp", null),
        new ExpectedResult("java.lang.Double", "java.sql.Time", null),
        new ExpectedResult("java.lang.Double", "java.sql.Date", null),
        new ExpectedResult("java.lang.Double", "java.lang.String", "sourceVal.toString()"),
        new ExpectedResult("java.lang.Double", "com.example.UserClass", null),
        new ExpectedResult("java.lang.Float", "int", "(int)sourceVal.floatValue()"),
        new ExpectedResult("java.lang.Float", "long", "(long)sourceVal.floatValue()"),
        new ExpectedResult("java.lang.Float", "double", "(double)sourceVal.floatValue()"),
        new ExpectedResult("java.lang.Float", "float", "sourceVal.floatValue()"),
        new ExpectedResult("java.lang.Float", "boolean", null),
        new ExpectedResult("java.lang.Float", "java.lang.Integer", "new java.lang.Integer((int)sourceVal.floatValue())"),
        new ExpectedResult("java.lang.Float", "java.lang.Long", "new java.lang.Long((long)sourceVal.floatValue())"),
        new ExpectedResult("java.lang.Float", "java.lang.Double", "new java.lang.Double((double)sourceVal.floatValue())"),
        new ExpectedResult("java.lang.Float", "java.lang.Float", "sourceVal"),
        new ExpectedResult("java.lang.Float", "java.lang.Boolean", null),
        new ExpectedResult("java.lang.Float", "java.util.Date", null),
        new ExpectedResult("java.lang.Float", "java.sql.Timestamp", null),
        new ExpectedResult("java.lang.Float", "java.sql.Time", null),
        new ExpectedResult("java.lang.Float", "java.sql.Date", null),
        new ExpectedResult("java.lang.Float", "java.lang.String", "sourceVal.toString()"),
        new ExpectedResult("java.lang.Float", "com.example.UserClass", null),
        new ExpectedResult("java.lang.Boolean", "int", "(sourceVal.booleanValue() ? 1 : 0)"),
        new ExpectedResult("java.lang.Boolean", "long", "(sourceVal.booleanValue() ? 1 : 0)"),
        new ExpectedResult("java.lang.Boolean", "double", null),
        new ExpectedResult("java.lang.Boolean", "float", null),
        new ExpectedResult("java.lang.Boolean", "boolean", "sourceVal.booleanValue()"),
        new ExpectedResult("java.lang.Boolean", "java.lang.Integer", "new java.lang.Integer((sourceVal.booleanValue() ? 1 : 0))"),
        new ExpectedResult("java.lang.Boolean", "java.lang.Long", "new java.lang.Long((sourceVal.booleanValue() ? 1 : 0))"),
        new ExpectedResult("java.lang.Boolean", "java.lang.Double", null),
        new ExpectedResult("java.lang.Boolean", "java.lang.Float", null),
        new ExpectedResult("java.lang.Boolean", "java.lang.Boolean", "sourceVal"),
        new ExpectedResult("java.lang.Boolean", "java.util.Date", null),
        new ExpectedResult("java.lang.Boolean", "java.sql.Timestamp", null),
        new ExpectedResult("java.lang.Boolean", "java.sql.Time", null),
        new ExpectedResult("java.lang.Boolean", "java.sql.Date", null),
        new ExpectedResult("java.lang.Boolean", "java.lang.String", "sourceVal.toString()"),
        new ExpectedResult("java.lang.Boolean", "com.example.UserClass", null),
        new ExpectedResult("java.util.Date", "int", null),
        new ExpectedResult("java.util.Date", "long", "sourceVal.getTime()"),
        new ExpectedResult("java.util.Date", "double", null),
        new ExpectedResult("java.util.Date", "float", null),
        new ExpectedResult("java.util.Date", "boolean", null),
        new ExpectedResult("java.util.Date", "java.lang.Integer", null),
        new ExpectedResult("java.util.Date", "java.lang.Long", "new java.lang.Long(sourceVal.getTime())"),
        new ExpectedResult("java.util.Date", "java.lang.Double", null),
        new ExpectedResult("java.util.Date", "java.lang.Float", null),
        new ExpectedResult("java.util.Date", "java.lang.Boolean", null),
        new ExpectedResult("java.util.Date", "java.util.Date", "sourceVal"),
        new ExpectedResult("java.util.Date", "java.sql.Timestamp", "new java.sql.Timestamp(sourceVal.getTime())"),
        new ExpectedResult("java.util.Date", "java.sql.Time", "new java.sql.Time(sourceVal.getTime())"),
        new ExpectedResult("java.util.Date", "java.sql.Date", "new java.sql.Date(sourceVal.getTime())"),
        new ExpectedResult("java.util.Date", "java.lang.String", "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format(sourceVal)"),
        new ExpectedResult("java.util.Date", "com.example.UserClass", null),
        new ExpectedResult("java.sql.Timestamp", "int", null),
        new ExpectedResult("java.sql.Timestamp", "long", null),
        new ExpectedResult("java.sql.Timestamp", "double", null),
        new ExpectedResult("java.sql.Timestamp", "float", null),
        new ExpectedResult("java.sql.Timestamp", "boolean", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.Integer", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.Long", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.Double", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.Float", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.Boolean", null),
        new ExpectedResult("java.sql.Timestamp", "java.util.Date", "sourceVal"),
        new ExpectedResult("java.sql.Timestamp", "java.sql.Timestamp", "sourceVal"),
        new ExpectedResult("java.sql.Timestamp", "java.sql.Time", null),
        new ExpectedResult("java.sql.Timestamp", "java.sql.Date", null),
        new ExpectedResult("java.sql.Timestamp", "java.lang.String", "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format(sourceVal)"),
        new ExpectedResult("java.sql.Timestamp", "com.example.UserClass", null),
        new ExpectedResult("java.sql.Time", "int", null),
        new ExpectedResult("java.sql.Time", "long", null),
        new ExpectedResult("java.sql.Time", "double", null),
        new ExpectedResult("java.sql.Time", "float", null),
        new ExpectedResult("java.sql.Time", "boolean", null),
        new ExpectedResult("java.sql.Time", "java.lang.Integer", null),
        new ExpectedResult("java.sql.Time", "java.lang.Long", null),
        new ExpectedResult("java.sql.Time", "java.lang.Double", null),
        new ExpectedResult("java.sql.Time", "java.lang.Float", null),
        new ExpectedResult("java.sql.Time", "java.lang.Boolean", null),
        new ExpectedResult("java.sql.Time", "java.util.Date", "sourceVal"),
        new ExpectedResult("java.sql.Time", "java.sql.Timestamp", null),
        new ExpectedResult("java.sql.Time", "java.sql.Time", "sourceVal"),
        new ExpectedResult("java.sql.Time", "java.sql.Date", null),
        new ExpectedResult("java.sql.Time", "java.lang.String", "new java.text.SimpleDateFormat(\"HH:mm:ssZ\").format(sourceVal)"),
        new ExpectedResult("java.sql.Time", "com.example.UserClass", null),
        new ExpectedResult("java.sql.Date", "int", null),
        new ExpectedResult("java.sql.Date", "long", null),
        new ExpectedResult("java.sql.Date", "double", null),
        new ExpectedResult("java.sql.Date", "float", null),
        new ExpectedResult("java.sql.Date", "boolean", null),
        new ExpectedResult("java.sql.Date", "java.lang.Integer", null),
        new ExpectedResult("java.sql.Date", "java.lang.Long", null),
        new ExpectedResult("java.sql.Date", "java.lang.Double", null),
        new ExpectedResult("java.sql.Date", "java.lang.Float", null),
        new ExpectedResult("java.sql.Date", "java.lang.Boolean", null),
        new ExpectedResult("java.sql.Date", "java.util.Date", "sourceVal"),
        new ExpectedResult("java.sql.Date", "java.sql.Timestamp", null),
        new ExpectedResult("java.sql.Date", "java.sql.Time", null),
        new ExpectedResult("java.sql.Date", "java.sql.Date", "sourceVal"),
        new ExpectedResult("java.sql.Date", "java.lang.String", "new java.text.SimpleDateFormat(\"yyyy-MM-dd\").format(sourceVal)"),
        new ExpectedResult("java.sql.Date", "com.example.UserClass", null),
        new ExpectedResult("java.lang.String", "int", "Integer.valueOf(sourceVal).intValue()"),
        new ExpectedResult("java.lang.String", "long", "Long.valueOf(sourceVal).longValue()"),
        new ExpectedResult("java.lang.String", "double", "Double.valueOf(sourceVal).doubleValue()"),
        new ExpectedResult("java.lang.String", "float", "Float.valueOf(sourceVal).floatValue()"),
        new ExpectedResult("java.lang.String", "boolean", "Boolean.valueOf(sourceVal).booleanValue()"),
        new ExpectedResult("java.lang.String", "java.lang.Integer", "Integer.valueOf(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.lang.Long", "Long.valueOf(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.lang.Double", "Double.valueOf(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.lang.Float", "Float.valueOf(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.lang.Boolean", "Boolean.valueOf(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.util.Date", "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.sql.Timestamp", "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.sql.Time", "new java.text.SimpleDateFormat(\"HH:mm:ssZ\").parse(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.sql.Date", "new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse(sourceVal)"),
        new ExpectedResult("java.lang.String", "java.lang.String", "sourceVal"),
        new ExpectedResult("java.lang.String", "com.example.UserClass", null),
        new ExpectedResult("com.example.UserClass", "int", null),
        new ExpectedResult("com.example.UserClass", "long", null),
        new ExpectedResult("com.example.UserClass", "double", null),
        new ExpectedResult("com.example.UserClass", "float", null),
        new ExpectedResult("com.example.UserClass", "boolean", null),
        new ExpectedResult("com.example.UserClass", "java.lang.Integer", null),
        new ExpectedResult("com.example.UserClass", "java.lang.Long", null),
        new ExpectedResult("com.example.UserClass", "java.lang.Double", null),
        new ExpectedResult("com.example.UserClass", "java.lang.Float", null),
        new ExpectedResult("com.example.UserClass", "java.lang.Boolean", null),
        new ExpectedResult("com.example.UserClass", "java.util.Date", null),
        new ExpectedResult("com.example.UserClass", "java.sql.Timestamp", null),
        new ExpectedResult("com.example.UserClass", "java.sql.Time", null),
        new ExpectedResult("com.example.UserClass", "java.sql.Date", null),
        new ExpectedResult("com.example.UserClass", "java.lang.String", null),
        new ExpectedResult("com.example.UserClass", "com.example.UserClass", "sourceVal")
    };

    
    public void testTypeConvert() 
    {
        JavaTypeConverter converter = new JavaTypeConverter();
        for (int ndxTest = 0; ndxTest < expected.length; ndxTest++)
        {
            String result = converter.typeConvert(expected[ndxTest].fromType, "sourceVal", expected[ndxTest].toType);
            assertEquals("Converting " + expected[ndxTest].fromType + " to " + expected[ndxTest].toType, expected[ndxTest].expected, result);
        } // for
    }

    
    public void testTypeConvertWithIgnore() 
    {
        JavaTypeConverter converter = new JavaTypeConverter();
        converter.setJavaTypeConversionIgnoreList("java.util.Date, java.sql.Timestamp");
        
        for (int ndxTest = 0; ndxTest < expected.length; ndxTest++)
        {
            String result = converter.typeConvert(expected[ndxTest].fromType, "sourceVal", expected[ndxTest].toType);
            if (expected[ndxTest].fromType.equals("java.util.Date") ||
                expected[ndxTest].fromType.equals("java.sql.Timestamp") ||
                expected[ndxTest].toType.equals("java.util.Date") ||
                expected[ndxTest].toType.equals("java.sql.Timestamp")
                )
            {
                assertNull("Converting " + expected[ndxTest].fromType + " to " + expected[ndxTest].toType + " should have been null, was: " + result, result);
            }
            else
            {
                assertEquals("Converting " + expected[ndxTest].fromType + " to " + expected[ndxTest].toType, expected[ndxTest].expected, result);
            }
        } // for
    }
    
    /**
     * This method generates the "expected" source array, so it can be cut and
     * pasted from the console output to this class, AFTER is has been examined
     * by the programmer for accuracy.
     */
    static public void genExpectedSource()
    {
        final String[] knownTypes = 
        {
            "int", "long", "double", "float", "boolean",
            "java.lang.Integer", "java.lang.Long", "java.lang.Double", "java.lang.Float", "java.lang.Boolean",
            "java.util.Date", "java.sql.Timestamp", "java.sql.Time", "java.sql.Date",
            "java.lang.String", "com.example.UserClass"
        };
        
        JavaTypeConverter converter = new JavaTypeConverter();
        
        boolean firstOut = false;
        for (int ndxFrom = 0; ndxFrom < knownTypes.length; ndxFrom++)
        {
            for (int ndxTo = 0; ndxTo < knownTypes.length; ndxTo++)
            {
               if (firstOut == false)
               {
                   firstOut = true;
               }
               else
               {
                   System.out.println(",");
               }
               System.out.print("        new ExpectedResult(\"");
               System.out.print(knownTypes[ndxFrom]);
               System.out.print("\", \"");
               System.out.print(knownTypes[ndxTo]);
               System.out.print("\", ");
               String result = converter.typeConvert(knownTypes[ndxFrom], "sourceVal", knownTypes[ndxTo]);
               if (result != null)
               {
                  System.out.print("\"");
                  System.out.print(result);
                  System.out.print("\"");
               }
               else
               {
                  System.out.print("null");                   
               }
               System.out.print(")");
            } // for ndxTo
        } // for ndxFrom
    }
    
    public static void main(String[] args) {
//        Uncomment to re-generated the "expected" if major mods are made        
//        JavaTypeConverterTest.genExpectedSource();
        
        junit.textui.TestRunner.run(JavaTypeConverterTest.class);
    }
}
