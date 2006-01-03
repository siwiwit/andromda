package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * <p/>
 * Represents an EJB association end. </p> 
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade
 */
public class EJB3AssociationEndFacadeLogicImpl
    extends EJB3AssociationEndFacadeLogic
{

    /**
     * Represents the EJB3 <code>ALL</code> cascade option and fully qualified representation.
     */
    private static final String ENTITY_CASCADE_ALL = "ALL";
    private static final String ENTITY_CASCADE_ALL_FQN = "javax.persistence.CascadeType.ALL";
    
    /**
     * Represents the EJB3 <code>PERSIST</code> cascade option.
     */
    private static final String ENTITY_CASCADE_PERSIST = "PERSIST";
    private static final String ENTITY_CASCADE_PERSIST_FQN = "javax.persistence.CascadeType.PERSIST";
    
    /**
     * Represents the EJB3 <code>MERGE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_MERGE = "MERGE";
    private static final String ENTITY_CASCADE_MERGE_FQN = "javax.persistence.CascadeType.MERGE";
    
    /**
     * Represents the EJB3 <code>REMOVE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REMOVE = "REMOVE";
    private static final String ENTITY_CASCADE_REMOVE_FQN = "javax.persistence.CascadeType.REMOVE";

    /**
     * Represents the EJB3 <code>REFRESH</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REFRESH = "REFRESH";
    private static final String ENTITY_CASCADE_REFRESH_FQN = "javax.persistence.CascadeType.REFRESH";
    
    /**
     * Represents the value used to represents NO cascade option.
     */
    private static final String ENTITY_CASCADE_NONE = "NONE";
    
    /**
     * Stores the cascade map of fully qualified cascade types
     */
    private static final Hashtable cascadeTable = new Hashtable();
    
    static
    {
        cascadeTable.put(ENTITY_CASCADE_ALL, ENTITY_CASCADE_ALL_FQN);
        cascadeTable.put(ENTITY_CASCADE_PERSIST, ENTITY_CASCADE_PERSIST_FQN);
        cascadeTable.put(ENTITY_CASCADE_MERGE, ENTITY_CASCADE_MERGE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REMOVE, ENTITY_CASCADE_REMOVE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REFRESH, ENTITY_CASCADE_REFRESH_FQN);
    }
    
    // ---------------- constructor -------------------------------
	
    public EJB3AssociationEndFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade#getRelationType()
     */
    protected java.lang.String handleGetRelationType()
    {
        String targetType;
        if (this.isMany2Many() || this.isOne2Many())
        {
            targetType = "java.util.Collection";
        }
        else
        {
            targetType = this.getOtherEnd().getType().getFullyQualifiedName();
        }
        return targetType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade#getFetchType()
     * 
     * This method is always called on the target association end.
     * If a fetch type tagged value is not found on the target end, then compare the association
     * relationship from the source end to indicate the default fetch types.
     * <ul>
     *     <li>One-2-Many and Many-2-Many defaults to LAZY loading</li>
     *     <li>Many-2-One and One-2-One default to EAGER loading</li>
     * </ul>
     */
	protected String handleGetFetchType() 
	{
		String fetchType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);

		if (StringUtils.isBlank(fetchType))
		{
			if (this.getOtherEnd().isOne2Many() || this.getOtherEnd().isMany2Many())
			{
				fetchType = EJB3Globals.FETCH_TYPE_LAZY;
			}
			else
			{
				fetchType = EJB3Globals.FETCH_TYPE_EAGER;
			}
		}
		
		return fetchType;
	}

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsEager()
     */
    protected boolean handleIsEager()
    {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_EAGER))
            {
                isEager = true;
            }
        }
        return isEager;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsLazy()
     */
    protected boolean handleIsLazy()
    {
        boolean isLazy = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_LAZY))
            {
                isLazy = true;
            }
        }
        return isLazy;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsOwning()
     */
    protected boolean handleIsOwning()
    {
        boolean owning = false;
        if (this.isAggregation() || this.isComposition()) 
        {
            owning = true;
        }
        return owning;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsOptional()
     */
    protected boolean handleIsOptional()
    {
        boolean optional = true;
        String optionalString = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_OPTIONAL);

        if (StringUtils.isBlank(optionalString))
        {
            optionalString = 
                String.valueOf(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_ASSOCIATION_OPTIONAL));
        } 
        optional = Boolean.valueOf(optionalString).booleanValue();
        return optional;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetOrderByClause()
     */
    protected String handleGetOrderByClause()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_ORDERBY);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetColumnDefinition()
     */
    protected String handleGetColumnDefinition()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
    }

    /**
     * Returns true if the tagged name exists for this association end.
     * 
     * @param name The tagged name to lookup.
     * @return boolean True if the tagged name exists.  False otherwise.
     * 
     * @see org.andromda.cartridges.ejb3.metafacades.
     *          EJB3AssociationEndFacadeLogic#handleHasTaggedValue(java.lang.String)
     */
    protected boolean handleHasTaggedValue(String name)
    {
        boolean exists = false;
        if (StringUtils.isNotBlank(name)) 
        {
            // trim to remove leading/trailing spaces
            name = StringUtils.trimToEmpty(name);
            
            // loop over tagged values and matche the argument tagged value name
            for (final Iterator iter = this.getTaggedValues().iterator(); iter.hasNext(); ) 
            {
                final TaggedValueFacade taggedValue = (TaggedValueFacade)iter.next();
                
                // return with true on the first match found
                if (name.equals(taggedValue.getName())) 
                {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCascadeType()
     */
    protected String handleGetCascadeType()
    {
        String cascade = null;
        final Collection taggedValues = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE);
        if (taggedValues != null && !taggedValues.isEmpty())
        {
            StringBuffer buf = null;
            for (final Iterator iter = taggedValues.iterator(); iter.hasNext(); )
            {
                if (buf == null) 
                {
                    buf = new StringBuffer();
                }
                else
                {
                    buf.append(", ");
                }
                final String value = (String)iter.next();
                if (StringUtils.isNotBlank(value))
                {
                    buf.append(cascadeTable.get(value));
                }
            }
            cascade = buf.toString();
        }
        else if (this.isChild())
        {
            cascade = (String)cascadeTable.get(ENTITY_CASCADE_REMOVE);
            
            if (this.getOtherEnd() != null)
            {
                if (this.getOtherEnd().isComposition())
                {
                    if (StringUtils.isBlank(this.getCompositionCascadeType()))
                    {
                        if (this.getType() instanceof EJB3EntityFacade)
                        {
                            EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                            cascade = (entity.getDefaultCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                    null : (String)cascadeTable.get(entity.getDefaultCascadeType()));
                        }
                    }
                    else
                    {
                        cascade = (this.getCompositionCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                null : (String)cascadeTable.get(this.getCompositionCascadeType()));
                    }
                }
                else if (this.getOtherEnd().isAggregation())
                {
                    if (StringUtils.isBlank(this.getAggregationCascadeType()))
                    {
                        if (this.getType() instanceof EJB3EntityFacade)
                        {
                            EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                            cascade = (entity.getDefaultCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                    null : (String)cascadeTable.get(entity.getDefaultCascadeType()));
                        }
                    }
                    else
                    {
                        cascade = (this.getAggregationCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ?
                                null : (String)cascadeTable.get(this.getAggregationCascadeType()));
                    }
                }
            }
        }
        else if (this.isComposition())
        {
            /**
             * On the composite side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propogation - overriding a default cascade 
             * value
             */
            cascade = null;
        }
        else if (this.isAggregation())
        {
            /**
             * On the aggregation side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propogation - overriding a default cascade
             * value
             */
            cascade = null;
        }
        return cascade;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCompositionCascadeType()
     */
    protected String handleGetCompositionCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_COMPOSITE_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetAggregationCascadeType()
     */
    protected String handleGetAggregationCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_AGGREGATION_CASCADE)));
    }

}