package org.andromda.metafacades.uml14;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
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

/**
 * Metaclass facade implementation.
 */
public class ClassifierFacadeLogicImpl
        extends ClassifierFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ClassifierFacadeLogicImpl(org.omg.uml.foundation.core.Classifier metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
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
        return UML14MetafacadeUtils.getCorePackage().getAParticipantAssociation().getAssociation(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitiveType()
     */
    protected boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive,
        // otherwise it isn't
        return this.getWrapperMappings() != null && this.getWrapperMappings().getMappings().containsFrom(
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
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    protected String handleGetWrapperName()
    {
        String wrapperName = null;
        if (this.getWrapperMappings() != null)
        {
            if (this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName()))
            {
                wrapperName = this.getWrapperMappings().getTo(this.getFullyQualifiedName());
            }
        }
        return wrapperName;
    }

    /**
     * Gets the mappings from primitive types to wrapper types. Some languages have primitives (i.e., Java) and some
     * languages don't, so therefore this property is optional.
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                logger.error(errMsg, th);
                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.BLOB_TYPE_NAME);
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
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization();
             superClass != null && follow; superClass = (ClassifierFacade)superClass.getGeneralization())
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
        CollectionUtils.transform(connectingEnds, new ConnectingEndTransformer());
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
            nonArrayType = (ClassifierFacade)this.getRootPackage().findModelElement(StringUtils.replace(
                    this.getFullyQualifiedName(true), this.getArraySuffix(), ""));
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
            arrayType = (ClassifierFacade)this.getRootPackage().findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#addAttribute(java.lang.String, java.lang.String,
            *      java.lang.String)
     */
    protected void handleAddAttribute(String name, String fullyQualifiedType, String visibility)
    {
        CorePackage corePackage = UML14MetafacadeUtils.getCorePackage();
        Attribute attribute = corePackage.getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UML14MetafacadeUtils.getVisibilityKind(visibility));
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

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#findAttribute()
     */
    protected AttributeFacade handleFindAttribute(final String name)
    {
        return (AttributeFacade)CollectionUtils.find(this.getAttributes(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                final AttributeFacade attribute = (AttributeFacade)object;
                return StringUtils.trimToEmpty(attribute.getName()).equals(name);
            }
        });
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    protected String handleGetArrayName()
    {
        return this.getName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml14.ClassifierFacade#getFullyQualifiedArrayName()
     */
    protected String handleGetFullyQualifiedArrayName()
    {
        return this.getFullyQualifiedName() + this.getArraySuffix();
    }

    /**
     * Calculates the serial version UID of this classifier based on the
     * signature of the classifier (name, visibility, attributes and methods).
     * The algorithm is inspired by
     * {@link java.io.ObjectStreamClass#getSerialVersionUID()}.
     *
     * The value should be stable as long as the classifier remains unchanged
     * and should change as soon as there is any change in the signature of the
     * classifier.
     *
     * @return the serial version UID of this classifier.
     */
    private Long calculateDefaultSUID()
    {
        StringBuffer sb = new StringBuffer();

        // class name
        sb.append(this.getName());
        // class modifiers (visibility)
        sb.append(this.getVisibility());

        // generalizations
        Collection allGeneralizations = this.getAllGeneralizations();
        for (Iterator iter = allGeneralizations.iterator(); iter.hasNext();)
        {
            ClassifierFacade classifier = (ClassifierFacade)iter.next();
            sb.append(classifier.getName());
        }

        // declared fields
        Collection attributes = this.getAttributes();
        for (Iterator iter = attributes.iterator(); iter.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iter.next();
            sb.append(attribute.getName());
            sb.append(attribute.getVisibility());
            sb.append(attribute.getType());
        }

        // operations
        Collection operations = this.getOperations();
        for (Iterator iter = operations.iterator(); iter.hasNext();)
        {
            OperationFacade operation = (OperationFacade)iter.next();
            sb.append(operation.getName());
            sb.append(operation.getVisibility());
            sb.append(operation.getReturnType());
            Collection parameters = operation.getParameters();
            for (Iterator iterator = parameters.iterator(); iterator.hasNext();)
            {
                ParameterFacade parameter = (ParameterFacade)iterator.next();
                sb.append(parameter.getName());
                sb.append(parameter.getVisibility());
                sb.append(parameter.getType());
            }
        }
        String signature = sb.toString();

        Long serialVersionUID = new Long(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--)
            {
                hash = (hash << 8) | (hashBytes[i] & 0xFF);
            }
            serialVersionUID = new Long(hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            final String errMsg = "Error performing ModelElementFacadeImpl.getSerialVersionUID";
            logger.error(errMsg, e);
        }
        return serialVersionUID;

    }

    /**
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogic#handleGetSerialVersionUID()
     */
    public Long handleGetSerialVersionUID()
    {
        Long serialVersionUID = null;
        String serialVersionString = UML14MetafacadeUtils
                .getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.valueOf(serialVersionString);
        }
        else
        {
            serialVersionUID = calculateDefaultSUID();
        }
        return serialVersionUID;
    }

}