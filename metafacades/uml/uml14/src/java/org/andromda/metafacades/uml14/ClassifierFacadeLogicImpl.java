package org.andromda.metafacades.uml14;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;

import java.util.Collection;
import java.util.Iterator;

/**
 * Metaclass facade implementation.
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
    implements org.andromda.metafacades.uml.ClassifierFacade
{
    // ---------------- constructor -------------------------------

    public ClassifierFacadeLogicImpl(
        org.omg.uml.foundation.core.Classifier metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getOperations()
     */
    protected java.util.Collection handleGetOperations()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof Operation;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getAttributes()
     */
    protected java.util.Collection handleGetAttributes()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof Attribute;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getAssociationEnds()
     */
    protected java.util.Collection handleGetAssociationEnds()
    {
        return UMLMetafacadeUtils.getCorePackage().getAParticipantAssociation()
            .getAssociation(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitiveType()
     */
    protected boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive,
        // otherwise it isn't
        return this.getWrapperMappings() != null
            && this.getWrapperMappings().containsFrom(
                this.getFullyQualifiedName());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
    protected boolean handleIsArrayType()
    {
        return this.getFullyQualifiedName(true).endsWith(this.getArraySuffix());
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     * 
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this
            .getConfiguredProperty(UMLMetafacadeProperties.ARRAY_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    protected String handleGetWrapperName()
    {
        String wrapperName = null;
        if (this.getWrapperMappings() != null)
        {
            if (this.getWrapperMappings().containsFrom(
                this.getFullyQualifiedName()))
            {
                wrapperName = this.getWrapperMappings().getTo(
                    this.getFullyQualifiedName());
            }
        }
        return wrapperName;
    }

    /**
     * Gets the mappings from primitive types to wrapper types. Some languages
     * have primitives (i.e., Java) and some languages don't, so therefore this
     * property is optional.
     * 
     * @return the wrapper mappings
     */
    protected Mappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        Mappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = Mappings.getInstance(uri);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '"
                    + uri + "'";
                logger.error(errMsg, th);
                // don't throw the exception
            }
        }
        else
        {
            mappings = (Mappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.DATE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    protected boolean handleIsStringType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    protected Collection handleGetAttributes(boolean follow)
    {
        Collection attributes = this.getAttributes();
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            attributes.addAll(superClass.getAttributes());
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    protected String handleGetOperationCallFromAttributes()
    {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");

        for (Iterator iterator = getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();

            sb.append(separator);
            String typeName = attribute.getType().getFullyQualifiedName();
            sb.append(typeName);
            sb.append(" ");
            sb.append(attribute.getName());
            separator = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    protected Collection handleGetStaticAttributes()
    {
        return new FilteredCollection(this.getAttributes())
        {
            public boolean evaluate(Object object)
            {
                return ((AttributeFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    protected java.util.Collection handleGetInstanceAttributes()
    {
        return new FilteredCollection(this.getAttributes())
        {
            public boolean evaluate(Object object)
            {
                return !((AttributeFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    protected java.util.Collection handleGetProperties()
    {
        Collection properties = this.getAttributes();
        class ConnectingEndTransformer
            implements Transformer
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        }
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(
            connectingEnds,
            new ConnectingEndTransformer());
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).isNavigable();
            }
        }
        CollectionUtils.filter(connectingEnds, new NavigableFilter());
        properties.addAll(connectingEnds);
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    protected Collection handleGetAbstractions()
    {
        return new FilteredCollection(this.metaObject.getClientDependency())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof Abstraction;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    protected boolean handleIsDataType()
    {
        return DataType.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    protected boolean handleIsInterface()
    {
        return Interface.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    protected Object handleGetNonArray()
    {
        ClassifierFacade nonArrayType = this;
        if (this.getFullyQualifiedName().indexOf(this.getArraySuffix()) != -1)
        {
            nonArrayType = (ClassifierFacade)this.getRootPackage()
                .findModelElement(
                    StringUtils.replace(this.getFullyQualifiedName(true), this
                        .getArraySuffix(), ""));
        }
        return nonArrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    protected Object handleGetArray()
    {
        ClassifierFacade arrayType = this;
        String name = this.getFullyQualifiedName(true);
        if (name.indexOf(this.getArraySuffix()) == -1)
        {
            name = name + this.getArraySuffix();
            arrayType = (ClassifierFacade)this.getRootPackage()
                .findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#addAttribute(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    protected void handleAddAttribute(
        String name,
        String fullyQualifiedType,
        String visibility)
    {
        CorePackage corePackage = UMLMetafacadeUtils.getCorePackage();
        Attribute attribute = corePackage.getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UMLMetafacadeUtils
            .getVisibilityKind(visibility));
        this.metaObject.getFeature().add(attribute);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    protected boolean handleIsEnumeration()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    protected String handleGetJavaNullString()
    {
        String javaNullString = null;
        if (isPrimitive())
        {
            if (UMLMetafacadeUtils.isType(this, UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNullString = "false";
            }
            else
            {
                javaNullString = "0";
            }
        }
        else
        {
            javaNullString = "null";
        }
        return javaNullString;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    protected Collection handleGetStaticOperations()
    {
        return new FilteredCollection(this.getOperations())
        {
            public boolean evaluate(Object object)
            {
                return ((OperationFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    protected Collection handleGetInstanceOperations()
    {
        return new FilteredCollection(this.getOperations())
        {
            public boolean evaluate(Object object)
            {
                return !((OperationFacade)object).isStatic();
            }
        };
    }
}