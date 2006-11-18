package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Abstraction;
import org.eclipse.uml2.AssociationClass;
import org.eclipse.uml2.DataType;
import org.eclipse.uml2.Enumeration;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.PrimitiveType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ClassifierFacade.
 *
 * @see org.andromda.metafacades.uml.ClassifierFacade
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
    public ClassifierFacadeLogicImpl(
        final org.eclipse.uml2.Classifier metaObject,
        final String context)
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
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitive()
     */
    protected boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive,
        // otherwise it isn't
        return this.getWrapperMappings() != null &&
        this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName());
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    protected java.lang.String handleGetOperationCallFromAttributes()
    {
        final StringBuffer call = new StringBuffer();
        String separator = "";
        call.append("(");
        for (final Iterator iterator = this.getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();

            call.append(separator);
            String typeName = attribute.getType().getFullyQualifiedName();
            call.append(typeName);
            call.append(" ");
            call.append(attribute.getName());
            separator = ", ";
        }
        call.append(")");
        return call.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    protected java.util.Collection handleGetProperties()
    {
        return this.getProperties(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllProperties()
     */
    public Collection handleGetAllProperties()
    {
        return this.getProperties(true);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllRequiredConstructorParameters()
     */
    public Collection handleGetAllRequiredConstructorParameters()
    {
        final Collection allRequiredConstructorParameters = new ArrayList();

        final Collection generalizations = this.getGeneralizations();
        for (Iterator parents = generalizations.iterator(); parents.hasNext();)
        {
            final Object parent = parents.next();
            if (parent instanceof ClassifierFacade)
            {
                allRequiredConstructorParameters.addAll(
                    ((ClassifierFacade)parent).getAllRequiredConstructorParameters());
            }
        }

        allRequiredConstructorParameters.addAll(this.getRequiredConstructorParameters());

        return allRequiredConstructorParameters;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getRequiredConstructorParameters()
     */
    public Collection handleGetRequiredConstructorParameters()
    {
        final Collection requiredConstructorParameters = new ArrayList();

        final Collection properties = this.getProperties();
        for (Iterator propertyIterator = properties.iterator(); propertyIterator.hasNext();)
        {
            final Object property = propertyIterator.next();
            if (property instanceof AttributeFacade)
            {
                final AttributeFacade attribute = (AttributeFacade)property;
                if (attribute.isRequired() || attribute.isReadOnly())
                {
                    requiredConstructorParameters.add(attribute);
                }
            }
            else if (property instanceof AssociationEndFacade)
            {
                final AssociationEndFacade associationEnd = (AssociationEndFacade)property;
                if (associationEnd.isRequired() || associationEnd.isReadOnly())
                {
                    requiredConstructorParameters.add(associationEnd);
                }
            }
        }

        return requiredConstructorParameters;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDataType()
     */
    protected boolean handleIsDataType()
    {
        return this.metaObject instanceof DataType;
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.COLLECTION_TYPE_NAME);
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
     * Gets the mappings from primitive types to wrapper types. Some languages
     * have primitives (i.e., Java) and some languages don't, so therefore this
     * property is optional.
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (final Throwable throwable)
            {
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                this.logger.error(
                    errMsg,
                    throwable);

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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.DATE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isInterface()
     */
    protected boolean handleIsInterface()
    {
        return this.metaObject instanceof Interface;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    protected String handleGetJavaNullString()
    {
        String javaNullString;
        if (this.isPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                    this,
                    UMLProfile.BOOLEAN_TYPE_NAME))
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    protected boolean handleIsStringType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    protected boolean handleIsEnumeration()
    {
        return (this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION)) || (this.metaObject instanceof Enumeration);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    protected String handleGetArrayName()
    {
        return this.getName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getFullyQualifiedArrayName()
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
        final StringBuffer buffer = new StringBuffer();

        // class name
        buffer.append(this.getName());

        // generalizations
        for (final Iterator iterator = this.getAllGeneralizations().iterator(); iterator.hasNext();)
        {
            ClassifierFacade classifier = (ClassifierFacade)iterator.next();
            buffer.append(classifier.getName());
        }

        // declared fields
        for (final Iterator iterator = this.getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();
            buffer.append(attribute.getName());
            buffer.append(attribute.getVisibility());
            buffer.append(attribute.getType().getName());
        }

        // operations
        for (final Iterator iter = this.getOperations().iterator(); iter.hasNext();)
        {
            OperationFacade operation = (OperationFacade)iter.next();
            buffer.append(operation.getName());
            buffer.append(operation.getVisibility());
            buffer.append(operation.getReturnType().getName());
            for (final Iterator iterator = operation.getArguments().iterator(); iterator.hasNext();)
            {
                final ParameterFacade parameter = (ParameterFacade)iterator.next();
                buffer.append(parameter.getName());
                buffer.append(parameter.getType().getName());
            }
        }
        final String signature = buffer.toString();

        Long serialVersionUID = new Long(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(hashBytes.length, 8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = new Long(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            final String errMsg = "Error performing ModelElementFacadeImpl.getSerialVersionUID";
            this.logger.error(
                errMsg,
                exception);
        }
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Default UID for " + this.metaObject.getQualifiedName() + " is " + serialVersionUID);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSerialVersionUID()
     */
    protected java.lang.Long handleGetSerialVersionUID()
    {
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Starting get serial UID");
        }
        Long serialVersionUID;
        String serialVersionString = UmlUtilities.getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.valueOf(serialVersionString);
        }
        else
        {
            serialVersionUID = this.calculateDefaultSUID();
        }
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("SerialVersionUID for " + this.metaObject.getQualifiedName() + " is " + serialVersionUID);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isClobType()
     */
    protected boolean handleIsClobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.CLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBooleanType()
     */
    protected boolean handleIsBooleanType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isTimeType()
     */
    protected boolean handleIsTimeType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.TIME_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    protected java.util.Collection handleGetAttributes(final boolean follow)
    {
        return this.shieldedElements(UmlUtilities.getAttributes(
                this.metaObject,
                follow));
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#findAttribute(java.lang.String)
     */
    protected org.andromda.metafacades.uml.AttributeFacade handleFindAttribute(final java.lang.String name)
    {
        return (AttributeFacade)CollectionUtils.find(
            this.getAttributes(),
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final AttributeFacade attribute = (AttributeFacade)object;
                    return StringUtils.trimToEmpty(attribute.getName()).equals(name);
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    protected java.util.Collection handleGetProperties(final boolean follow)
    {
        final List properties = new ArrayList();
        if (follow && !this.getGeneralizations().isEmpty())
        {
            for (Iterator iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
            {
                final Object generalization = iterator.next();
                if (generalization instanceof ClassifierFacade)
                {
                    properties.addAll(((ClassifierFacade)generalization).getAllProperties());
                }
            }
        }
        properties.addAll(this.getAttributes(false));
        properties.addAll(this.getNavigableConnectingEnds(false));
        return properties;
    }

    protected Collection handleGetOperations()
    {
        final Collection operations;

        if (this.metaObject instanceof org.eclipse.uml2.Class)
        {
            operations = ((org.eclipse.uml2.Class)this.metaObject).getOwnedOperations();
        }
        else if (this.metaObject instanceof org.eclipse.uml2.Interface)
        {
            operations = new LinkedHashSet(((org.eclipse.uml2.Interface)this.metaObject).getOwnedOperations());
        }
        else
        {
            operations = Collections.EMPTY_LIST;
        }

        return operations;
    }

    /**
     * Note: if this instance represents an actual class we resolve any realized interfaces recursively, in case this
     * instance represents an interface we return only the owned operations.
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    protected java.util.Collection handleGetImplementationOperations()
    {
        final Collection operations;

        if (this.metaObject instanceof org.eclipse.uml2.Class)
        {
            operations = new LinkedHashSet(((org.eclipse.uml2.Class)this.metaObject).getOwnedOperations());

            final Collection dependencies = new FilteredCollection(this.metaObject.getClientDependencies())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof Abstraction;
                }
            };

            for (Iterator abstractionIterator = dependencies.iterator(); abstractionIterator.hasNext();)
            {
                final Abstraction abstraction = (Abstraction)abstractionIterator.next();
                final List suppliers = abstraction.getSuppliers();
                for (int i = 0; i < suppliers.size(); i++)
                {
                    final Object supplierObject = suppliers.get(i);
                    if (supplierObject instanceof Interface)
                    {
                        operations.addAll(resolveInterfaceOperationsRecursively((Interface)supplierObject));
                    }
                }
            }
        }
        else if (this.metaObject instanceof org.eclipse.uml2.Interface)
        {
            operations = new LinkedHashSet(((org.eclipse.uml2.Interface)this.metaObject).getOwnedOperations());
        }
        else
        {
            operations = Collections.EMPTY_LIST;
        }

        return operations;
    }


    private static Collection resolveInterfaceOperationsRecursively(Interface classifier)
    {
        final Collection operations = new LinkedHashSet(classifier.getOwnedOperations());   // preserve ordering

        final List generals = classifier.getGenerals();
        for (int i = 0; i < generals.size(); i++)
        {
            final Object generalObject = generals.get(i);
            if (generalObject instanceof Interface)
            {
                operations.addAll(resolveInterfaceOperationsRecursively((Interface)generalObject));
            }
        }

        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    protected java.util.Collection handleGetAttributes()
    {
        return UmlUtilities.getAttributes(this.metaObject, false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    protected java.util.List handleGetAssociationEnds()
    {
        return UmlUtilities.getAssociationEnds(this.metaObject, false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    protected Object handleGetNonArray()
    {
        ClassifierFacade nonArrayType = this;

        String arraySuffix = this.getArraySuffix();

        if (this.getFullyQualifiedName().indexOf(arraySuffix) != -1)
        {
            PackageFacade rootPF = this.getRootPackage();
            String fullQualifiedName = this.getFullyQualifiedName(true);

            if (this.logger.isDebugEnabled())
            {
                this.logger.debug(
                    "Looking for non-array type of element " + fullQualifiedName + " with array suffix " + arraySuffix +
                    ", root: " + rootPF);
                this.logger.debug("Metaobject: " + this.metaObject + " its model is : " + this.metaObject.getModel());
            }
            nonArrayType =
                (ClassifierFacade)rootPF.findModelElement(StringUtils.replace(
                        fullQualifiedName,
                        arraySuffix,
                        ""));
        }
        return nonArrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    protected Object handleGetArray()
    {
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Entered getArray for " + this.metaObject);
        }
        ClassifierFacade arrayType = this;
        if (this.metaObject instanceof PrimitiveType)
        {
            String name = this.getFullyQualifiedName(true);
            if (name.indexOf(this.getArraySuffix()) == -1)
            {
                name = name + this.getArraySuffix();
                arrayType = (ClassifierFacade)this.getRootPackage().findModelElement(name);
            }
        }
        else
        {
            arrayType = null;
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    protected java.util.Collection handleGetStaticAttributes()
    {
        Collection attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AttributeFacade)object).isStatic();
                }
            });

        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    protected java.util.Collection handleGetInstanceAttributes()
    {
        Collection attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return !((AttributeFacade)object).isStatic();
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    protected java.util.Collection handleGetStaticOperations()
    {
        Collection operations = this.getOperations();
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            });
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    protected java.util.Collection handleGetInstanceOperations()
    {
        Collection operations = this.getOperations();
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            });
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    protected java.util.Collection handleGetAbstractions()
    {
        final Collection dependencies = new ArrayList(this.metaObject.getClientDependencies());
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object instanceof Abstraction;
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    protected java.util.Collection handleGetNavigableConnectingEnds()
    {
        final Collection connectingEnds = new ArrayList(this.getAssociationEnds());
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).isNavigable();
                }
            });
        return connectingEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds(boolean)
     */
    protected Collection handleGetNavigableConnectingEnds(final boolean follow)
    {
        final Collection connectingEnds = this.shieldedElements(UmlUtilities.getAssociationEnds(
                    this.metaObject,
                    follow));
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).isNavigable();
                }
            });
        return connectingEnds;
    }

    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    protected Collection handleGetInterfaceAbstractions()
    {
        final Collection interfaceAbstractions = new LinkedHashSet();
        if (this.getAbstractions() != null)
        {
            for (Iterator abstractionIterator = this.getAbstractions().iterator(); abstractionIterator.hasNext();)
            {
                final DependencyFacade abstraction = (DependencyFacade)abstractionIterator.next();
                final ModelElementFacade element = abstraction.getTargetElement();

                if (element instanceof ClassifierFacade)
                {
                    final ClassifierFacade classifier = (ClassifierFacade)element;
                    if (classifier.isInterface())
                    {
                        interfaceAbstractions.add(classifier);
                    }
                }
            }
        }

        return interfaceAbstractions;
    }

    protected String handleGetImplementedInterfaceList()
    {
        final String interfaceList;

        final Collection interfaces = this.getInterfaceAbstractions();
        if (interfaces.isEmpty())
        {
            interfaceList = "";
        }
        else
        {
            final StringBuffer list = new StringBuffer();
            for (final Iterator iterator = interfaces.iterator(); iterator.hasNext();)
            {
                final ModelElementFacade element = (ModelElementFacade)iterator.next();
                list.append(element.getFullyQualifiedName());
                if (iterator.hasNext())
                {
                    list.append(", ");
                }
            }
            interfaceList = list.toString();
        }

        return interfaceList;
    }

/*    protected Object handleFindTaggedValue(final String tagName, final boolean follow)
    {
       // TODO: This methode has been overriden. Why ?
        return null;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO: This methode has been overriden. Why ?
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO: This methode has been overriden. Why ?
        return false;
    }

    protected void handleCopyTaggedValues(final ModelElementFacade element)
    {
        // TODO: This methode has been overriden. Why ?
    }

    protected Object handleGetTemplateParameter(final String parameterName)
    {
        // TODO: This methode has been overriden. Why ?
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO: This methode has been overriden. Why ?
        return null;
    }*/

    /**
     * @see org.andromda.metafacades.emf.uml2.ClassifierFacadeLogic#handleIsAssociationClass()
     */
    protected boolean handleIsAssociationClass()
    {
        // TODO: Check it's working.
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    protected Collection handleGetAssociatedClasses()
    {
        final Set associatedClasses = new LinkedHashSet();

        final List associationEnds = this.getAssociationEnds();
        for (int i = 0; i < associationEnds.size(); i++)
        {
            final AssociationEndFacade associationEndFacade = (AssociationEndFacade)associationEnds.get(i);
            associatedClasses.add(associationEndFacade.getOtherEnd().getType());
        }

        return associatedClasses;
    }

    protected Collection handleGetAllAssociatedClasses()
    {
        final Set associatedClasses = new LinkedHashSet();
        associatedClasses.addAll(this.getAssociatedClasses());
        for (Iterator parentIterator = this.getGeneralizations().iterator(); parentIterator.hasNext();)
        {
            final ClassifierFacade parent = (ClassifierFacade)parentIterator.next();
            associatedClasses.addAll(parent.getAllAssociatedClasses());
        }

        return associatedClasses;
    }

    protected Object handleGetSuperClass()
    {
        final GeneralizableElementFacade superClass = this.getGeneralization();
        return superClass instanceof ClassifierFacade ? superClass : null;
    }
 
    protected boolean handleIsEmbeddedValue()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_EMBEDDED_VALUE);
    }
}