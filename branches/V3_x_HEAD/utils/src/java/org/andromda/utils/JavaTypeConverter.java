package org.andromda.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

/**
 * A utility object used by the code generator when it needs to convert an object
 * from one data type to another.
 *
 * @author Joel Kozikowski
 */
public class JavaTypeConverter
{
    public JavaTypeConverter()
    {
    }

    private ArrayList javaTypeConversionIgnoreList = new ArrayList();

    /** 
     * Specifies a list of one or more fully qualified java types that should be ignored 
     * whenever a type conversion is done.  See Spring namespace property "javaTypeConversionIgnoreList" 
     */
    public void setJavaTypeConversionIgnoreList(String commaSeparatedIgnoreList)
    {
        javaTypeConversionIgnoreList = new ArrayList();
        if (commaSeparatedIgnoreList != null)
        {
            String[] strList = StringUtils.split(commaSeparatedIgnoreList, ", ");
            javaTypeConversionIgnoreList.addAll(Arrays.asList(strList));
        }
        
    }
    
    private static class ConversionEntry
    {
        public String sourceType;
        public String targetType;
        public String conversionPattern;

        public ConversionEntry(
            String sourceType,
            String targetType,
            String conversionPattern)
        {
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.conversionPattern = conversionPattern;
        }
    }

    private static ConversionEntry[] conversionTable =
        {
            new ConversionEntry("int", "java.lang.Integer", "new java.lang.Integer({0})"),
            new ConversionEntry("int", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("int", "long", "(long){0}"), new ConversionEntry("int", "double", "(double){0}"),
            new ConversionEntry("int", "float", "(float){0}"), new ConversionEntry("int", "boolean", "({0} != 0)"),
            new ConversionEntry("java.lang.Integer", "int", "{0}.intValue()"),
            new ConversionEntry("java.lang.Integer", "java.lang.String", "{0}.toString()"),
            new ConversionEntry("java.lang.String", "int", "Integer.valueOf({0}).intValue()"),
            new ConversionEntry("java.lang.String", "java.lang.Integer", "Integer.valueOf({0})"),
            
            new ConversionEntry("long", "java.lang.Long", "new java.lang.Long({0})"),
            new ConversionEntry("long", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("long", "int", "(int){0}"), new ConversionEntry("long", "double", "(double){0}"),
            new ConversionEntry("long", "float", "(float){0}"), new ConversionEntry("long", "boolean", "({0} != 0)"),
            new ConversionEntry("java.lang.Long", "long", "{0}.longValue()"),
            new ConversionEntry("java.lang.Long", "java.lang.String", "{0}.toString()"),
            new ConversionEntry("java.lang.String", "long", "Long.valueOf({0}).longValue()"),
            new ConversionEntry("java.lang.String", "java.lang.Long", "Long.valueOf({0})"),
            
            new ConversionEntry("double", "java.lang.Double", "new java.lang.Double({0})"),
            new ConversionEntry("double", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("double", "int", "(int){0}"), new ConversionEntry("double", "long", "(long){0}"),
            new ConversionEntry("double", "float", "(float){0}"),
            new ConversionEntry("java.lang.Double", "double", "{0}.doubleValue()"),
            new ConversionEntry("java.lang.Double", "java.lang.String", "{0}.toString()"),
            new ConversionEntry("java.lang.String", "double", "Double.valueOf({0}).doubleValue()"),
            new ConversionEntry("java.lang.String", "java.lang.Double", "Double.valueOf({0})"),
            
            new ConversionEntry("float", "java.lang.Float", "new java.lang.Float({0})"),
            new ConversionEntry("float", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("float", "int", "(int){0}"), new ConversionEntry("float", "long", "(long){0}"),
            new ConversionEntry("float", "double", "(double){0}"),
            new ConversionEntry("java.lang.Float", "float", "{0}.floatValue()"),
            new ConversionEntry("java.lang.Float", "java.lang.String", "{0}.toString()"),
            new ConversionEntry("java.lang.String", "float", "Float.valueOf({0}).floatValue()"),
            new ConversionEntry("java.lang.String", "java.lang.Float", "Float.valueOf({0})"),
            
            new ConversionEntry("boolean", "java.lang.Boolean", "new java.lang.Boolean({0})"),
            new ConversionEntry("boolean", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("boolean", "int", "({0} ? 1 : 0)"),
            new ConversionEntry("boolean", "long", "({0} ? 1 : 0)"),
            new ConversionEntry("java.lang.Boolean", "boolean", "{0}.booleanValue()"),
            new ConversionEntry("java.lang.Boolean", "java.lang.String", "{0}.toString()"),
            new ConversionEntry("java.lang.String", "boolean", "Boolean.valueOf({0}).booleanValue()"),
            new ConversionEntry("java.lang.String", "java.lang.Boolean", "Boolean.valueOf({0})"),
            
            new ConversionEntry("java.util.Date", "java.lang.String",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format({0})"),
            new ConversionEntry("java.sql.Timestamp", "java.lang.String",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format({0})"),
            new ConversionEntry("java.sql.Time", "java.lang.String",
                "new java.text.SimpleDateFormat(\"HH:mm:ssZ\").format({0})"),
            new ConversionEntry("java.sql.Date", "java.lang.String",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd\").format({0})"),
            new ConversionEntry("java.lang.String", "java.util.Date",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse({0})"),
            new ConversionEntry("java.lang.String", "java.sql.Timestamp",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse({0})"),
            new ConversionEntry("java.lang.String", "java.sql.Time",
                "new java.text.SimpleDateFormat(\"HH:mm:ssZ\").parse({0})"),
            new ConversionEntry("java.lang.String", "java.sql.Date",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse({0})"),
            new ConversionEntry("java.sql.Timestamp", "java.util.Date", "{0}"),
            new ConversionEntry("java.sql.Time", "java.util.Date", "{0}"),
            new ConversionEntry("java.sql.Date", "java.util.Date", "{0}"),
            new ConversionEntry("java.util.Date", "java.sql.Timestamp", "new java.sql.Timestamp({0}.getTime())"),
            new ConversionEntry("java.util.Date", "java.sql.Time", "new java.sql.Time({0}.getTime())"),
            new ConversionEntry("java.util.Date", "java.sql.Date", "new java.sql.Date({0}.getTime())"),
            new ConversionEntry("java.util.Date", "long", "{0}.getTime()"),
            new ConversionEntry("long", "java.util.Date", "new java.util.Date({0})")
        };

    /**
     * Attemps to code a type conversion from the specified source value to the target
     * type that can be used on the right hand side of an assignment. If such a conversion
     * exists, the Java code fragment to do that will be returned. If no such conversion exists,
     * null is returned.
     * @param sourceType The fully qualified data type of the source value
     * @param sourceValue The actual source value (usually a variable name)
     * @param targetType The fully qualified data type of the target value
     * @return The converted value, or null if no conversion can be done.
     */
    public String typeConvert(
        String sourceType,
        String sourceValue,
        String targetType)
    {
        String convertedValue;

        if (javaTypeConversionIgnoreList.contains(sourceType) || javaTypeConversionIgnoreList.contains(targetType))
        {
            convertedValue = null;
        }
        else if (sourceType.equals(targetType))
        {
            convertedValue = sourceValue;
        }
        else
        {
            convertedValue = null;

            for (int i = 0; i < conversionTable.length && convertedValue == null; i++)
            {
                if (conversionTable[i].sourceType.equals(sourceType) &&
                    conversionTable[i].targetType.equals(targetType))
                {
                    convertedValue =
                        MessageFormat.format(
                            conversionTable[i].conversionPattern,
                            new Object[] {sourceValue});
                } // if
            } // for

            if (convertedValue == null && sourceType.startsWith("java.lang."))
            {
                // If source is a primative wrapper, try to convert
                // to the base primative first...
                String primativeSource = sourceType.substring(10).toLowerCase();
                if (primativeSource.equals("integer"))
                {
                    primativeSource = "int";
                }
                String interimValue = typeConvert(
                        sourceType,
                        sourceValue,
                        primativeSource);
                if (interimValue != null)
                {
                    convertedValue = typeConvert(
                            primativeSource,
                            interimValue,
                            targetType);
                }
            }

            if (convertedValue == null && targetType.startsWith("java.lang."))
            {
                // One last try - if target is a primative wrapper, try to convert
                // to the base primative first...
                String primativeTarget = targetType.substring(10).toLowerCase();
                if (primativeTarget.equals("integer"))
                {
                    primativeTarget = "int";
                }
                String interimValue = typeConvert(
                        sourceType,
                        sourceValue,
                        primativeTarget);
                if (interimValue != null)
                {
                    convertedValue = typeConvert(
                            primativeTarget,
                            interimValue,
                            targetType);
                }
            }
        }

        return convertedValue;
    }

}