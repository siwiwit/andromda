package org.andromda.cartridges.webservice.metafacades;

import java.text.MessageFormat;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.webservice.metafacades.WSDLType.
 * 
 * @see org.andromda.cartridges.webservice.metafacades.WSDLType
 */
public class WSDLTypeLogicImpl
    extends WSDLTypeLogic
    implements org.andromda.cartridges.webservice.metafacades.WSDLType
{
    // ---------------- constructor -------------------------------

    public WSDLTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType()
     */
    public java.lang.String handleGetSchemaType()
    {
        return this.getSchemaType(true, true);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType(boolean,
     *      boolean)
     */
    public java.lang.String handleGetSchemaType(
        boolean withPrefix,
        boolean preserveArray)
    {
        StringBuffer schemaType = new StringBuffer();
        String modelName = this.getFullyQualifiedName(true);
        if (this.getSchemaTypeMappings() != null)
        {
            String namespacePrefix = this.getNamespacePrefix() + ':';

            String mappedValue = this.getSchemaTypeMappings().getTo(modelName);
            if (!mappedValue.equals(modelName))
            {
                schemaType.append(mappedValue);
            }
            else
            {
                if (withPrefix)
                {
                    schemaType.append(namespacePrefix);
                }
                if (this.isArrayType())
                {
                    ClassifierFacade nonArray = this.getNonArray();
                    if (WSDLType.class.isAssignableFrom(nonArray.getClass()))
                    {
                        schemaType.append(((WSDLType)nonArray).getQName());
                    }
                }
                else
                {
                    schemaType.append(this.getQName());
                }
            }
            // remove any array '[]' suffix
            schemaType = new StringBuffer(schemaType.toString().replaceAll(
                "\\[\\]",
                ""));
            if (preserveArray && this.isArrayType())
            {
                int insertIndex = namespacePrefix.length();
                if (!schemaType.toString().startsWith(namespacePrefix))
                {
                    if (withPrefix)
                    {
                        // add the prefix for any normal XSD types
                        // that may not have been set above
                        schemaType.insert(0, namespacePrefix);
                    }
                    else
                    {
                        // since we aren't adding the prefix, set
                        // the correct insert index
                        insertIndex = 0;
                    }
                }
                schemaType.insert(insertIndex, ARRAY_NAME_PREFIX);
            }
            if (withPrefix
                && !schemaType.toString().startsWith(namespacePrefix))
            {
                schemaType.insert(0, "xsd:");
            }
        }
        return schemaType.toString();
    }

    private final static String ARRAY_NAME_PREFIX = "ArrayOf";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic#getArrayName()
     */
    public String handleGetArrayName()
    {
        StringBuffer name = new StringBuffer(StringUtils.trimToEmpty(
            this.getQName()).replaceAll("\\[\\]", ""));
        name.insert(0, ARRAY_NAME_PREFIX);
        return name.toString();
    }
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getQName()
     */
    public String handleGetQName()
    {
        return MessageFormat.format(
            this.getQualfiedNameLocalPartPattern(),
            new String[]
            {
                StringUtils.trimToEmpty(this.getName())
            });
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespace()
     */
    public java.lang.String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = StringUtils.reverseDelimited(
                packageName,
                WebServiceLogicImpl.NAMESPACE_DELIM);
        }
        return MessageFormat.format(this.getNamespacePattern(), new String[]
        {
            StringUtils.trimToEmpty(packageName)
        });
    }

    /**
     * Gets the schemaType mappings that have been set for this schema type.
     * 
     * @return the Mappings instance.
     */
    public Mappings getSchemaTypeMappings()
    {
        final String propertyName = "schemaTypeMappingsUri";
        Object property = this.getConfiguredProperty(propertyName);
        Mappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try 
            {
                mappings = Mappings.getInstance((String)property);
                this.registerConfiguredProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName
                    + "' --> '" + uri + "'";
                logger.error(errMsg, th);
                //don't throw the exception
            }  
        }
        else 
        {
            mappings = (Mappings)property;
        }
        return mappings;
    }
    
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic#handleGetNamespacePrefix()
     */
    public String handleGetNamespacePrefix()
    {
        return (String)this
            .getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PREFIX);
    }
    
    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this service.
     */
    protected String getQualfiedNameLocalPartPattern()
    {
        return (String)this
            .getConfiguredProperty(WebServiceLogicImpl.QNAME_LOCAL_PART_PATTERN);
    }

    /**
     * Gets the <code <namespacePattern</code> for this type.
     */
    protected String getNamespacePattern()
    {
        return (String)this
            .getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PATTERN);
    }
    
    /**
     * Gets whether or not <code>reverseNamespace</code> is true/false for
     * this type.
     * 
     * @return boolean true/false
     */
    protected boolean isReverseNamespace()
    {
        return Boolean.valueOf(
            String.valueOf(this
                .getConfiguredProperty(WebServiceLogicImpl.REVERSE_NAMESPACE)))
            .booleanValue();
    }
}