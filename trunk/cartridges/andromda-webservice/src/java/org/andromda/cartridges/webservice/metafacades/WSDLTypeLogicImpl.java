package org.andromda.cartridges.webservice.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.webservice.WebServiceProfile;
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
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#isEnumeration()
     */
    public boolean handleIsEnumeration()
    {
        return this.hasStereotype(WebServiceProfile.STEREOTYPE_ENUMERATION);
    }

    /**
     * Schema type mappings property reference.
     */
    private static final String SCHEMA_TYPE_MAPPINGS_URI = "schemaTypeMappingsUri";

    /**
     * Allows the MetafacadeFactory to populate the schemaType mappings for this
     * model element.
     * 
     * @param mappingUri the URI of the schemaType mappings resource.
     */
    public void setSchemaTypeMappingsUri(String mappingUri)
    {
        try
        {
            // register the mappings with the component container.
            this.registerConfiguredProperty(SCHEMA_TYPE_MAPPINGS_URI, Mappings
                .getInstance(mappingUri));
        }
        catch (Throwable th)
        {
            String errMsg = "Error setting '" + SCHEMA_TYPE_MAPPINGS_URI
                + "' --> '" + mappingUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
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
        return (Mappings)this.getConfiguredProperty(SCHEMA_TYPE_MAPPINGS_URI);
    }

    /**
     * Sets the <code>namespacePrefix</code> for the WSDLs type.
     * 
     * @param namespacePrefix the namespace prefix to use for these types.
     */
    public void setNamespacePrefix(String namespacePrefix)
    {
        this.registerConfiguredProperty(
            WebServiceLogicImpl.NAMESPACE_PREFIX,
            StringUtils.trimToEmpty(namespacePrefix));
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
     * Sets the <code>qualifiedNameLocalPartPattern</code> for this service.
     * 
     * @param qualifiedNameLocalPartPattern the name prefix to use for these
     *        types.
     */
    public void setQualifiedNameLocalPartPattern(
        String qualifiedNameLocalPartPattern)
    {
        this.registerConfiguredProperty(
            WebServiceLogicImpl.QNAME_LOCAL_PART_PATTERN,
            StringUtils.trimToEmpty(qualifiedNameLocalPartPattern));
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
     * Sets the <code>namespacePattern</code> for the WSDLs type.
     * 
     * @param namespacePattern the name prefix to use for this types.
     */
    public void setNamespacePattern(String namespacePattern)
    {
        this.registerConfiguredProperty(
            WebServiceLogicImpl.NAMESPACE_PATTERN,
            StringUtils.trimToEmpty(namespacePattern));
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
     * Sets the <code>reverseNamespace</code> for this service.
     * 
     * @param reverseNamespace whether or not to reverse the ordering of the
     *        namespace.
     */
    public void setReverseNamespace(String reverseNamespace)
    {
        this.registerConfiguredProperty(
            WebServiceLogicImpl.REVERSE_NAMESPACE,
            StringUtils.trimToEmpty(reverseNamespace));
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