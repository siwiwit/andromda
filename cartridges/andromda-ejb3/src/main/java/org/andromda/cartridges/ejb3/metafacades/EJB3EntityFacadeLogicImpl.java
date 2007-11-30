package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade;
import org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade;
import org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.ValueObject;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade
 */
public class EJB3EntityFacadeLogicImpl
    extends EJB3EntityFacadeLogic
{
    /**
     * The default entity association cascade property
     */
    public static final String ENTITY_DEFAULT_CASCADE = "entityDefaultCascade";
    
    /**
     * Stores the default entity inheritance strategy
     */
    private static final String ENTITY_INHERITANCE_STRATEGY = "entityInheritanceStrategy";
    
    /**
     * Stores the default entity discriminator type used in the 
     * inheritance annotation
     */
    private static final String ENTITY_DISCRIMINATOR_TYPE = "entityDiscriminatorType";
    
    /**
     * Stores the default entity discriminator column name used in 
     * the DiscriminatorColumn annotation
     */
    private static final String ENTITY_DISCRIMINATOR_COLUMN_NAME = "entityDiscriminatorColumnName";
    
    /**
     * The default view type accessability for an entity POJO bean
     */
    public static final String ENTITY_DEFAULT_VIEW_TYPE = "entityViewType";
    
    /**
     * Value for one table per root class
     */
    private static final String INHERITANCE_STRATEGY_TABLE_PER_CLASS = "TABLE_PER_CLASS";

    /**
     * Value for a single table for the hierarchy
     */
    private static final String INHERITANCE_STRATEGY_SINGLE_TABLE = "SINGLE_TABLE";

    /**
     * Value for joined subclass
     */
    private static final String INHERITANCE_STRATEGY_JOINED_SUBLCASS = "JOINED";

    /**
     * Stores the valid inheritance strategies
     */
    private static final Collection inheritanceStrategies = new ArrayList();
    
    static
    {
        inheritanceStrategies.add(INHERITANCE_STRATEGY_TABLE_PER_CLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_SINGLE_TABLE);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_JOINED_SUBLCASS);
    }
    
    /**
     * Value for string based discriminator type
     */
    public static final String DISCRIMINATORTYPE_STRING = "STRING";
    
    /**
     * Value for char based discriminator type
     */
    public static final String DISCRIMINATORTYPE_CHAR = "CHAR";
    
    /**
     * Value for integer based discriminator type
     */
    public static final String DISCRIMINATORTYPE_INTEGER = "INTEGER";
    
    /**
     * Stores the valid discriminator types
     */
    private static final Collection discriminatorTypes = new ArrayList();
    
    static
    {
        discriminatorTypes.add(DISCRIMINATORTYPE_STRING);
        discriminatorTypes.add(DISCRIMINATORTYPE_CHAR);
        discriminatorTypes.add(DISCRIMINATORTYPE_INTEGER);
    }
    
    /**
     * The property which stores the pattern defining the entity name.
     */
    public static final String ENTITY_NAME_PATTERN = "entityNamePattern";
    
    /**
     * The property which stores the pattern defining the entity 
     * implementation name.
     */
    public static final String ENTITY_IMPLEMENTATION_NAME_PATTERN = "entityImplementationNamePattern";
    
    /**
     * The property that stores the pattern defining the entity 
     * listener class name.
     */
    public static final String ENTITY_LISTENER_NAME_PATTERN = "entityListenerNamePattern";
    
    /**
     * The property that stores the pattern defining the entity 
     * embeddable super class name.
     */
    public static final String ENTITY_EMBEDDABLE_NAME_PATTERN = "entityEmbeddableNamePattern";

    /**
     * The property that stores the pattern defining the entity
     * composite primary key class name.
     */
    private static final String ENTITY_COMPOSITE_PRIMARY_KEY_NAME_PATTERN = "entityCompositePrimaryKeyNamePattern";
    
    /**
     * The property that stores the generic finders option
     */
    private static final String ENTITY_GENERIC_FINDERS = "entityGenericFinders";

    /**
     * The property that stores whether caching is enabled.
     */
    private static final String HIBERNATE_ENABLE_CACHE = "hibernateEnableCache";
    
    /**
     * The property that stores the hibernate entity cache value.
     */
    private static final String HIBERNATE_ENTITY_CACHE = "hibernateEntityCache";
    
    /**
     * The property that determines whether to use the default cache region for
     * entities and queries.
     */
    private static final String USE_DEFAULT_CACHE_REGION = "useDefaultCacheRegion";

    /**
     * The pattern used to construct the DAO implementation name.
     */
    private static final String DAO_IMPLEMENTATION_PATTERN = "daoImplementationNamePattern";

    /**
     * The pattern used to construct the DAO base name.
     */
    private static final String DAO_BASE_PATTERN = "daoBaseNamePattern";
    
    /**
     * The property which stores the pattern defining the DAO default exception name.
     */
    private static final String DAO_DEFAULT_EXCEPTION_NAME_PATTERN = "daoDefaultExceptionNamePattern";
    
    // ---------------- constructor -------------------------------

    public EJB3EntityFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------
    
    /**
     * This was meant to overrides the default implementation in EntityLogicImpl.java.
     * TODO: check - is it really required?
     * 
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getIdentifiers()
     */
    public Collection handleGetIdentifiers()
    {
        Collection identifiers = new ArrayList();
        for (final Iterator iter = this.getSourceDependencies().iterator(); iter.hasNext();)
        {
            final DependencyFacade dep = (DependencyFacade)iter.next();
            if (dep.hasStereotype(EJB3Profile.STEREOTYPE_IDENTIFIER))
            {
                identifiers = ((ClassifierFacade)dep.getTargetElement()).getInstanceAttributes();
                MetafacadeUtils.filterByStereotype(identifiers, EJB3Profile.STEREOTYPE_IDENTIFIER);
                return identifiers;
            }
        }

        // No PK dependency found - try a PK attribute
        if (this.getIdentifiers(true) != null && !this.getIdentifiers(true).isEmpty())
        {
            AttributeFacade attr = (AttributeFacade)this.getIdentifiers(true).iterator().next();
            identifiers.add(attr);
            return identifiers;
        }

        // Still nothing found - recurse up the inheritance tree
        EJB3EntityFacade decorator = (EJB3EntityFacade)this.getGeneralization();
        return decorator.getIdentifiers();
    }
    
    /**
     * This overrides the default implementation in EntityLogicImpl.java.
     * 
     * This provides the means to check super classes, even those modelled
     * as mapped superclasses, as well as entities.
     * 
     * Gets all identifiers for this entity. If 'follow' is true, and if
     * no identifiers can be found on the entity, a search up the
     * inheritance chain will be performed, and the identifiers from
     * the first super class having them will be used.
     * 
     * @param follow a flag indicating whether or not the inheritance hiearchy
     *        should be followed
     * @return the collection of identifiers.
     */
    public Collection getIdentifiers(boolean follow)
    {
        final Collection identifiers = new ArrayList(this.getAttributes());
        MetafacadeUtils.filterByStereotype(
            identifiers,
            UMLProfile.STEREOTYPE_IDENTIFIER);

        if (identifiers.isEmpty() && follow)
        {
            if (this.getGeneralization() instanceof EJB3EntityFacade)
            {
                return ((EJB3EntityFacade)this.getGeneralization()).getIdentifiers(follow);
            }
            else if (this.getGeneralization() instanceof EJB3MappedSuperclassFacade)
            {
                return ((EJB3MappedSuperclassFacade)this.getGeneralization()).getIdentifiers(follow);
            }
            else
            {
                return identifiers;
            }
        }
        else
        {
            return identifiers;
        }
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#isSyntheticCreateMethodAllowed()
     */
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
    	return EJB3MetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getAllEntityRelations()
     */
    protected java.util.Collection handleGetAllEntityRelations()
    {
        return this.getEntityRelations();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getJndiName()
     */
    protected java.lang.String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * Gets the <code>jndiNamePrefix</code> for this EJB.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getJndiNamePrefix()
    {
        String prefix = null;
        if (this.isConfiguredProperty(EJB3Globals.JNDI_NAME_PREFIX))
        {
            prefix = (String)this.getConfiguredProperty(EJB3Globals.JNDI_NAME_PREFIX);
        }
        return prefix;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getViewType()
     */
    protected java.lang.String handleGetViewType()
    {
    	return EJB3MetafacadeUtils.getViewType(this,
                String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_VIEW_TYPE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getAllInstanceAttributes()
     */
    protected java.util.List handleGetAllInstanceAttributes()
    {
        return EJB3MetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getInheritedInstanceAttributes()
     */
    protected java.util.List handleGetInheritedInstanceAttributes()
    {
        return EJB3MetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getHomeInterfaceName()
     */
    protected java.lang.String handleGetHomeInterfaceName()
    {
    	return EJB3MetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getValueDependencies()
     * 
     * NOTE: This is not required since ValueObject no longer exist and replaced with POJOs
     */
    protected java.util.Collection handleGetValueDependencies()
    {
        Collection dependencies = super.getSourceDependencies();
        CollectionUtils.filter(
                dependencies, 
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        boolean isValueRef = false;
                        if (object instanceof DependencyFacade)
                        {
                            DependencyFacade dep = (DependencyFacade)object;
                            isValueRef = dep.getStereotypeNames().contains(EJB3Profile.STEREOTYPE_VALUE_REF) 
                                && dep.getTargetElement().hasExactStereotype(EJB3Profile.STEREOTYPE_VALUE_OBJECT);
                        }
                        return isValueRef;
                    }
                });
        return dependencies;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getEntityRelations()
     */
    protected java.util.Collection handleGetEntityRelations()
    {
        Collection result = new ArrayList();
        for (final Iterator endIt = this.getAssociationEnds().iterator(); endIt.hasNext();)
        {
            final EJB3AssociationEndFacade associationEnd = (EJB3AssociationEndFacade)endIt.next();
            ClassifierFacade target = associationEnd.getOtherEnd().getType();
            if (target instanceof EJB3EntityFacade && associationEnd.getOtherEnd().isNavigable())
            {
                result.add(associationEnd);
            }
        }

        return result;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getCreateMethods(boolean)
     */
    protected java.util.Collection handleGetCreateMethods(boolean follow)
    {
    	return EJB3MetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getSelectMethods(boolean)
     */
    protected java.util.Collection handleGetSelectMethods(boolean follow)
    {
        Collection retval = new ArrayList();
        EJB3EntityFacade entity = null;
        do
        {
            Collection ops = this.getOperations();
            for (final Iterator i = ops.iterator(); i.hasNext();)
            {
                final OperationFacade op = (OperationFacade)i.next();
                if (op.hasStereotype(EJB3Profile.STEREOTYPE_SELECT_METHOD))
                {
                    retval.add(op);
                }
            }
            if (follow)
            {
                entity = (EJB3EntityFacade)this.getGeneralization();
            }
            else
            {
                break;
            }
        }
        while (entity != null);
        return retval;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getEnvironmentEntries(boolean)
     */
    protected java.util.Collection handleGetEnvironmentEntries(boolean follow)
    {
    	return EJB3MetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getConstants(boolean)
     */
    protected java.util.Collection handleGetConstants(boolean follow)
    {
        return EJB3MetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#isOperationPresent(java.lang.String)
     */
    protected boolean handleIsOperationPresent(java.lang.String op)
    {
        Collection collOps = this.getOperations();
        for (final Iterator it = collOps.iterator(); it.hasNext();)
        {
            final OperationFacade operation = (OperationFacade)it.next();
            if (operation.getName().equalsIgnoreCase(op))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#isAttributePresent(java.lang.String)
     */
    protected boolean handleIsAttributePresent(java.lang.String att)
    {
        Collection collAttrib = this.getAttributes(true);
        for (final Iterator it = collAttrib.iterator(); it.hasNext();)
        {
            final AttributeFacade attr = (AttributeFacade)it.next();
            if (attr.getName().equalsIgnoreCase(att))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#isIdentifierPresent(java.lang.String)
     */
    protected boolean handleIsIdentifierPresent(java.lang.String id)
    {
        Collection collIdentifier = this.getIdentifiers(true);
        for (final Iterator it = collIdentifier.iterator(); it.hasNext();)
        {
            final AttributeFacade attr = (AttributeFacade)it.next();
            if (attr.getName().equalsIgnoreCase(id))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacade#getSqlType()
     */
    protected java.lang.String handleGetSqlType()
    {
        String mpSql = this.getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI).getMappings().getName();
        if (mpSql.startsWith("Oracle"))
        {
            mpSql = "ORACLE";
        }
        return mpSql;
    }

    /**
     * Gets a Mappings instance from a property registered under the given <code>propertyName</code>.
     *
     * @param propertyName the property name to register under.
     * @return the Mappings instance.
     */
    private TypeMappings getMappingsProperty(final String propertyName)
    {
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (property instanceof String)
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
                logger.error(errMsg);
                // don't throw the exception
                ExceptionRecorder.instance().record(errMsg, th);
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }
    
    /**
     * @see org.andromda.metafacades.uml.Entity#getBusinessOperations()
     */
    public Collection getBusinessOperations()
    {
        Collection operations = super.getBusinessOperations();
        CollectionUtils.filter(operations, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean businessOperation = false;
                if (EJB3OperationFacade.class.isAssignableFrom(object.getClass()))
                {
                    businessOperation = ((EJB3OperationFacade)object).isBusinessOperation();
                }
                return businessOperation;
            }
        });
        return operations;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetEntityCompositePrimaryKeyName()
     */
    protected String handleGetEntityCompositePrimaryKeyName()
    {
        String compPKPattern =
            String.valueOf(this.getConfiguredProperty(ENTITY_COMPOSITE_PRIMARY_KEY_NAME_PATTERN));

        return MessageFormat.format(
            compPKPattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetEntityListenerName()
     */
    protected String handleGetEntityListenerName() 
	{
		String entityListenerPattern = (String)this.getConfiguredProperty(ENTITY_LISTENER_NAME_PATTERN);
		
		return MessageFormat.format(
			entityListenerPattern,
			new Object[] {StringUtils.trimToEmpty(this.getName())});
	}

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetEntityEmbeddableName()
     */
    protected String handleGetEntityEmbeddableName()
    {
        String embeddableSuperclassName = 
            (String)this.getConfiguredProperty(ENTITY_EMBEDDABLE_NAME_PATTERN);
        
        return MessageFormat.format(
            embeddableSuperclassName,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetEntityName()
     */
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this.getConfiguredProperty(ENTITY_NAME_PATTERN);

        return MessageFormat.format(
            entityNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedEntityCompositePrimaryKeyName()
     */
    protected String handleGetFullyQualifiedEntityCompositePrimaryKeyName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getEntityCompositePrimaryKeyName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetEntityImplementationName()
     */
    protected String handleGetEntityImplementationName()
    {
        String implNamePattern =
            String.valueOf(this.getConfiguredProperty(ENTITY_IMPLEMENTATION_NAME_PATTERN));

        return MessageFormat.format(
            implNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedEntityListenerName()
     */
    protected String handleGetFullyQualifiedEntityListenerName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getEntityListenerName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedEntityEmbeddableName()
     */
    protected String handleGetFullyQualifiedEntityEmbeddableName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getEntityEmbeddableName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getEntityName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedEntityImplementationName()
     */
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getEntityImplementationName(),
                null);
    }
    
    /**
     * Override the default table name definition to lookup the tagged value first.
     */
    public String getTableName()
    {
        String tableName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME);
        if (StringUtils.isBlank(tableName))
        {
            tableName = super.getTableName();
        }
        return tableName; 
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDefaultCascadeType()
     */
    protected String handleGetDefaultCascadeType()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDiscriminatorColumn()
     */
    protected String handleGetDiscriminatorColumn()
    {
        String discriminatorColumnName = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN);
        if (StringUtils.isBlank(discriminatorColumnName))
        {
            discriminatorColumnName = String.valueOf(this.getConfiguredProperty(ENTITY_DISCRIMINATOR_COLUMN_NAME));
        }
        return discriminatorColumnName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDiscriminatorColumnDefinition()
     */
    protected String handleGetDiscriminatorColumnDefinition()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_DEFINITION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDiscriminatorLength()
     */
    protected int handleGetDiscriminatorLength()
    {
        int length = 0;
        String lengthAsStr = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_LENGTH);
        if (StringUtils.isNotBlank(lengthAsStr))
        {
            length = NumberUtils.toInt(lengthAsStr);
        }
        return length;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDiscriminatorType()
     */
    protected String handleGetDiscriminatorType()
    {
        String discriminatorType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_TYPE);
        if (StringUtils.isBlank(discriminatorType))
        {
            discriminatorType = String.valueOf(this.getConfiguredProperty(ENTITY_DISCRIMINATOR_TYPE));
        }
        return discriminatorType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDiscriminatorValue()
     */
    protected String handleGetDiscriminatorValue()
    {
        String discriminatorValue = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_VALUE);
        if (StringUtils.isBlank(discriminatorValue))
        {
            discriminatorValue = StringUtils.substring(this.getEntityName(), 0, 1);
        }
        return discriminatorValue;
    }
    
    /**
     * Gets the default entity inhertance strategy from namespace.
     *
     * @return the default entity inheritance strategy.
     */
    private String getDefaultInheritanceStrategy()
    {
        return String.valueOf(this.getConfiguredProperty(ENTITY_INHERITANCE_STRATEGY));
    }

    /**
     * Return the inheritance tagged value for for given <code>entity</code>.
     *
     * @param the EJB3EntityFacade from which to retrieve the inheritance tagged value.
     * @return String inheritance tagged value.
     */
    private String getInheritance(EJB3EntityFacade entity)
    {
        String inheritance = null;
        if (entity != null)
        {
            Object value = entity.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_INHERITANCE);
            if (value != null)
            {
                inheritance = String.valueOf(value);
            }
        }
        return inheritance;
    }

    /**
     * Returns the super entity for this entity, if one exists by generalization. If a
     * generalization does NOT exist OR if it's not an instance of EJB3EntityFacade
     * then returns null.
     *
     * @return the super entity or null if one doesn't exist.
     */
    private EJB3EntityFacade getSuperEntity()
    {
        EJB3EntityFacade superEntity = null;
        if ((this.getGeneralization() != null) && this.getGeneralization() instanceof EJB3EntityFacade)
        {
            superEntity = (EJB3EntityFacade)this.getGeneralization();
        }
        return superEntity;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsInheritanceSingleTable()
     */
    protected boolean handleIsInheritanceSingleTable()
    {
        return this.getInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_SINGLE_TABLE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetInheritanceStrategy()
     */
    protected String handleGetInheritanceStrategy()
    {
        String inheritance = this.getInheritance(this);
        for (EJB3EntityFacade superEntity = this.getSuperEntity();
            (superEntity != null) && StringUtils.isBlank(inheritance); )
        {
            inheritance = superEntity.getInheritanceStrategy();
        }

        if (StringUtils.isBlank(inheritance) || !inheritanceStrategies.contains(inheritance))
        {
            inheritance = this.getDefaultInheritanceStrategy();
        }
        return inheritance;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsInheritanceTablePerClass()
     */
    protected boolean handleIsInheritanceTablePerClass()
    {
        return this.getInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_TABLE_PER_CLASS);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsInheritanceJoined()
     */
    protected boolean handleIsInheritanceJoined()
    {
        return this.getInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_JOINED_SUBLCASS);
    }

    /**
     * Returns true if this entity is a <code>root</code> entity and has no generalizations.
     * 
     * @return
     */
    private boolean isRoot()
    {
        final EJB3EntityFacade superEntity = this.getSuperEntity();
        return (superEntity == null && !this.isAbstract());
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsRequiresSpecializationMapping()
     */
    protected boolean handleIsRequiresSpecializationMapping()
    {
        return (this.isInheritanceSingleTable() || this.isInheritanceTablePerClass() || this.isInheritanceJoined())
                && !this.getSpecializations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsRequiresGeneralizationMapping()
     */
    protected boolean handleIsRequiresGeneralizationMapping()
    {
        return (this.getSuperEntity() != null && 
                (this.getSuperEntity().isInheritanceSingleTable() || 
                        this.getSuperEntity().isInheritanceTablePerClass() ||
                        this.getSuperEntity().isInheritanceJoined()));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsEmbeddableSuperclass()
     */
    protected boolean handleIsEmbeddableSuperclass()
    {
        boolean isEmbeddableSuperclass = this.hasStereotype(EJB3Profile.STEREOTYPE_MAPPED_SUPERCLASS);

        /**
         * Must the root class - Cannot have embeddable superclass in the middle of the hierarchy
         */
        return isEmbeddableSuperclass && isRoot();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsEmbeddableSuperclassGeneralizationExists()
     */
    protected boolean handleIsEmbeddableSuperclassGeneralizationExists()
    {
        return (this.getSuperEntity() != null && this.getSuperEntity().isEmbeddableSuperclass());
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetAttributesAsList(java.util.Collection, boolean, boolean, boolean)
     */
    protected String handleGetAttributesAsList(
            Collection attributes, 
            boolean includeTypes, 
            boolean includeNames,
            boolean includeAutoIdentifiers)
    {
        if (!includeNames && !includeTypes || attributes == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        String separator = "";

        for (final Iterator it = attributes.iterator(); it.hasNext();)
        {
            EJB3EntityAttributeFacade attr = (EJB3EntityAttributeFacade)it.next();
            /**
             * Do not include attributes that are assigned for optimistic lock value as a version
             */
            boolean isCompositePKPresent = this.isCompositePrimaryKeyPresent();
            if (!attr.isVersion())
            {
                /**
                 * Do not include identifier attributes for entities with a composite primary key
                 * or if includeAutoIdentifiers is false, do not include identifiers with auto generated values.
                 */
                if ((isCompositePKPresent && (includeAutoIdentifiers || !attr.isIdentifier())) || 
                    (!isCompositePKPresent && 
                            ((!includeAutoIdentifiers && attr.isIdentifier() && attr.isGeneratorTypeNone()) || 
                            (includeAutoIdentifiers && attr.isIdentifier()) ||
                            !attr.isIdentifier())))
                {
                    sb.append(separator);
                    separator = ", ";
                    if (includeTypes)
                    {
                        /**
                         * If attribute is a LOB and lob type is overriden, then use 
                         * overriding lob type.
                         */
                        if (attr.isLob() && StringUtils.isNotBlank(attr.getLobType()))
                        {
                            sb.append(attr.getLobType());
                        }
                        else
                        {
                            sb.append(attr.getType().getFullyQualifiedName());
                        }
                        sb.append(" ");
                    }
                    if (includeNames)
                    {
                        sb.append(attr.getName());
                    }
                }
            }
                
        }
        return sb.toString();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsGenericFinders()
     */
    protected boolean handleIsGenericFinders()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(ENTITY_GENERIC_FINDERS)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsCompositePrimaryKeyPresent()
     */
    protected boolean handleIsCompositePrimaryKeyPresent()
    {
        boolean isCompositePK = false;
        if (this.getIdentifiers().size() > 1)
        {
            isCompositePK = true;
        }
        return isCompositePK;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsListenerEnabled()
     */
    protected boolean handleIsListenerEnabled()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_LISTENER);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsFinderFindAllExists()
     */
    protected boolean handleIsFinderFindAllExists()
    {
        boolean finderExists = false;
        for (final Iterator iter = this.getQueryOperations().iterator(); iter.hasNext();)
        {
            final OperationFacade operation = (OperationFacade)iter.next();
            if (StringUtils.trimToEmpty(operation.getName()).equalsIgnoreCase("findAll"))
            {
                // Check for no finder arguments
                if (operation.getArguments().size() == 0)
                {
                    finderExists = true;
                    break;
                }
            }
        }
        return finderExists;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsFinderFindByPrimaryKeyExists()
     */
    protected boolean handleIsFinderFindByPrimaryKeyExists()
    {
        boolean finderExists = false;
        for (final Iterator iter = this.getQueryOperations().iterator(); iter.hasNext();)
        {
            final OperationFacade operation = (OperationFacade)iter.next();
            if (operation.getName().equalsIgnoreCase("findByPrimaryKey"))
            {
                finderExists = true;
                break;
            }
        }
        return finderExists;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsManageable()
     */
    protected boolean handleIsManageable()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_MANAGEABLE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetManageableDisplayAttribute()
     */
    protected Object handleGetManageableDisplayAttribute()
    {
        AttributeFacade displayAttribute = null;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = this.findAttribute(StringUtils.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection attributes = this.getAttributes(true);
        for (final Iterator attributeIterator = attributes.iterator();
            attributeIterator.hasNext() && displayAttribute == null;)
        {
            final EntityAttribute attribute = (EntityAttribute)attributeIterator.next();
            if (attribute.isUnique())
            {
                displayAttribute = attribute;
            }
        }

        if (displayAttribute == null)
        {
            if (!this.getIdentifiers().isEmpty())
            {
                displayAttribute = (EntityAttribute)this.getIdentifiers().iterator().next();
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = (EntityAttribute)attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetIdentifier()
     */
    protected Object handleGetIdentifier()
    {
        return (EJB3EntityAttributeFacade)this.getIdentifiers().iterator().next();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetCacheType()
     */
    protected String handleGetCacheType()
    {
        String cacheType = (String)findTaggedValue(EJB3Profile.TAGGEDVALUE_HIBERNATE_ENTITY_CACHE);
        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ENTITY_CACHE));
        }
        return StringUtils.trimToEmpty(cacheType);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsCacheEnabled()
     */
    protected boolean handleIsCacheEnabled()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(HIBERNATE_ENABLE_CACHE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsUseDefaultCacheRegion()
     */
    protected boolean handleIsUseDefaultCacheRegion()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(USE_DEFAULT_CACHE_REGION)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoName()
     */
    protected String handleGetDaoName()
    {
        return MessageFormat.format(
            getDaoNamePattern(),
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link #DAO_PATTERN}
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(EJB3Globals.DAO_PATTERN));
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedDaoName()
     */
    protected String handleGetFullyQualifiedDaoName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoImplementationName()
     */
    protected String handleGetDaoImplementationName()
    {
        return MessageFormat.format(
                getDaoImplementationNamePattern(),
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link #DAO_IMPLEMENTATION_PATTERN}
     *
     * @return the DAO implementation name pattern.
     */
    private String getDaoImplementationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_IMPLEMENTATION_PATTERN));
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedDaoImplementationName()
     */
    protected String handleGetFullyQualifiedDaoImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoImplementationName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoBaseName()
     */
    protected String handleGetDaoBaseName()
    {
        return MessageFormat.format(
                getDaoBaseNamePattern(),
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link #DAO_BASE_PATTERN}
     *
     * @return the DAO base name pattern.
     */
    private String getDaoBaseNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_BASE_PATTERN));
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedDaoBaseName()
     */
    protected String handleGetFullyQualifiedDaoBaseName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoBaseName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsDaoBusinessOperationsPresent()
     */
    protected boolean handleIsDaoBusinessOperationsPresent()
    {
        return this.getDaoBusinessOperations() != null && !this.getDaoBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoBusinessOperations()
     */
    protected Collection handleGetDaoBusinessOperations()
    {
        // operations that are not finders and static
        Collection finders = this.getQueryOperations();
        Collection operations = this.getOperations();

        Collection nonFinders = CollectionUtils.subtract(operations, finders);
        CollectionUtils.filter(
            nonFinders,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            }
        );
        return nonFinders;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetValueObjectReferences()
     */
    protected Collection handleGetValueObjectReferences()
    {
        return this.getValueObjectReferences(false);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetAllValueObjectReferences()
     */
    protected Collection handleGetAllValueObjectReferences()
    {
        return this.getValueObjectReferences(true);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsDaoImplementationRequired()
     */
    protected boolean handleIsDaoImplementationRequired()
    {
        return !this.getValueObjectReferences().isEmpty() || !this.getDaoBusinessOperations().isEmpty() ||
            !this.getQueryOperations(true).isEmpty();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoNoTransformationConstantName()
     */
    protected String handleGetDaoNoTransformationConstantName()
    {
        return EJB3Globals.TRANSFORMATION_CONSTANT_PREFIX + EJB3Globals.NO_TRANSFORMATION_CONSTANT_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetValueObjectReferences(boolean)
     */
    protected Collection handleGetValueObjectReferences(boolean follow)
    {
        final Collection sourceDependencies = new ArrayList(this.getSourceDependencies());
        if (follow)
        {
            for (
                GeneralizableElementFacade entity = this.getGeneralization(); entity != null;
                entity = entity.getGeneralization())
            {
                sourceDependencies.addAll(entity.getSourceDependencies());
            }
        }
        return new FilteredCollection(sourceDependencies)
            {
				private static final long serialVersionUID = -8193885902084039620L;

				public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    Object targetElement = ((DependencyFacade)object).getTargetElement();
                    if (targetElement instanceof ClassifierFacade)
                    {
                        ClassifierFacade element = (ClassifierFacade)targetElement;
                        valid = element.isDataType() || element instanceof ValueObject || 
                                    element instanceof EnumerationFacade;
                    }
                    return valid;
                }
            };
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetRoot()
     */
    protected Object handleGetRoot()
    {
        GeneralizableElementFacade generalization = this;
        for (
            ; generalization.getGeneralization() != null && generalization instanceof EJB3EntityFacade;
            generalization = generalization.getGeneralization())
            ;
        return generalization;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDefaultPersistenceContextUnitName()
     */
    protected String handleGetDefaultPersistenceContextUnitName()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.PERSISTENCE_CONTEXT_UNIT_NAME)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetDaoDefaultExceptionName()
     */
    protected String handleGetDaoDefaultExceptionName()
    {
        return MessageFormat.format(
                getDaoDefaultExceptionNamePattern(),
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link #DAO_DEFAULT_EXCEPTION_NAME_PATTERN}
     *
     * @return the DAO default exception name pattern.
     */
    private String getDaoDefaultExceptionNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_DEFAULT_EXCEPTION_NAME_PATTERN));
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetFullyQualifiedDaoDefaultExceptionName()
     */
    protected String handleGetFullyQualifiedDaoDefaultExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoDefaultExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsEntityImplementationRequired()
     */
    protected boolean handleIsEntityImplementationRequired()
    {
        return !this.getBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetInstanceAttributes(boolean, boolean)
     */
    protected Collection handleGetInstanceAttributes(
            boolean follow, 
            boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow, withIdentifiers);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
    			public boolean evaluate(Object object)
                {
                    boolean valid = true;
                    if (object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isStatic();
                    }
                    return valid;
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetInstanceAttributeNameList(boolean, boolean)
     */
    protected String handleGetInstanceAttributeNameList(boolean follow, boolean withIdentifiers)
    {
        return this.getNameList(this.getInstanceAttributes(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetInstanceAttributeTypeList(boolean, boolean)
     */
    protected String handleGetInstanceAttributeTypeList(boolean follow, boolean withIdentifiers)
    {
        return this.getTypeList(this.getInstanceAttributes(follow, withIdentifiers));
    }
    
    /**
     * Constructs a comma seperated list of attribute type names from the passed in collection of
     * <code>attributes</code>.
     *
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute types.
     */
    private String getTypeList(final Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            attributes,
            new Closure()
            {
                public void execute(final Object object)
                {
                    if (object instanceof AttributeFacade)
                    {
                        final AttributeFacade attribute = (AttributeFacade)object;
                        if (attribute.getType() != null)
                        {
                            list.append(attribute.getType().getFullyQualifiedName());
                            list.append(comma);
                        }
                    }
                    if (object instanceof AssociationEndFacade)
                    {
                        final AssociationEndFacade associationEnd = (AssociationEndFacade)object;
                        if (associationEnd.getType() != null)
                        {
                            list.append(associationEnd.getType().getFullyQualifiedName());
                            list.append(comma);
                        }
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * Constructs a comma seperated list of attribute names from the passed in collection of <code>attributes</code>.
     *
     * @param properties the properties to construct the list from.
     * @return the comma seperated list of attribute names.
     */
    private String getNameList(final Collection properties)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            properties,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof EntityAttribute)
                    {
                        list.append(((AttributeFacade)object).getName());
                        list.append(comma);
                    }
                    if (object instanceof EntityAssociationEnd)
                    {
                        list.append(((AssociationEndFacade)object).getName());
                        list.append(comma);
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsSecurityEnabled()
     */
    protected boolean handleIsSecurityEnabled()
    {
        return StringUtils.isNotBlank(this.getSecurityRealm());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetRolesAllowed()
     */
    protected String handleGetRolesAllowed()
    {
        StringBuffer rolesAllowed = null;
        String separator = "";
        
        for (final Iterator iter = this.getNonRunAsRoles().iterator(); iter.hasNext(); )
        {
            if (rolesAllowed == null)
            {
                rolesAllowed = new StringBuffer();
            }
            rolesAllowed.append(separator);
            Role role = (Role)iter.next();
            rolesAllowed.append('"');
            rolesAllowed.append(role.getName());
            rolesAllowed.append('"');
            separator = ", ";
        }
        return rolesAllowed != null ? rolesAllowed.toString() : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetSecurityRealm()
     */
    protected String handleGetSecurityRealm()
    {
        String securityRealm = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_REALM);
        if (StringUtils.isBlank(securityRealm))
        {
            securityRealm = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.SECURITY_REALM)));
        }
        return securityRealm;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetNonRunAsRoles()
     */
    protected Collection handleGetNonRunAsRoles()
    {
        Collection roles = this.getTargetDependencies();
        CollectionUtils.filter(
            roles, 
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null 
                            && dependency.getSourceElement() != null 
                            && dependency.getSourceElement() instanceof Role 
                            && !dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
                }
            });
        CollectionUtils.transform(
            roles, 
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((DependencyFacade)object).getSourceElement();
                }
            });
        final Collection allRoles = new LinkedHashSet(roles);
        // add all roles which are generalizations of this one
        CollectionUtils.forAllDo(
            roles, 
            new Closure()
            {
                public void execute(final Object object)
                {
                    allRoles.addAll(((Role)object).getAllSpecializations());
                }
            });
        return allRoles;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsUseQueryCache()
     */
    protected boolean handleIsUseQueryCache()
    {
        return BooleanUtils.toBoolean(
                String.valueOf(this.getConfiguredProperty(EJB3Globals.HIBERNATE_USER_QUERY_CACHE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleIsSeamComponent()
     */
    protected boolean handleIsSeamComponent()
    {
        return EJB3MetafacadeUtils.isSeamComponent(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetSeamComponentScopeType()
     */
    protected String handleGetSeamComponentScopeType()
    {
        return EJB3MetafacadeUtils.getSeamComponentScopeType(this, false);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityFacadeLogic#handleGetSeamComponentName()
     */
    protected String handleGetSeamComponentName()
    {
        return EJB3MetafacadeUtils.getSeamComponentName(this);
    }
}