package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 *
 *
 */
public class AssociationEndDecoratorImpl extends AssociationEndDecorator
{
    // ---------------- constructor -------------------------------

    public AssociationEndDecoratorImpl(
        org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AssociationEndDecorator ...

    public org.omg.uml.foundation.core.ModelElement handleGetOtherEnd()
    {
        AssociationEnd otherEnd;

        Collection ends = metaObject.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();)
        {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!metaObject.equals(ae))
            {
                return ae;
            }
        }

        return null;
    }

    public java.lang.String getRoleName()
    {
        String roleName = metaObject.getName();
        if ((roleName == null) || (roleName.length() == 0))
        {
            roleName = "the" + metaObject.getParticipant().getName();
        }

        return roleName;
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getName()
     */
    public String getName() {
    	String name = super.getName();
    	//if name is empty, then get the name from the type
    	if (StringUtils.isEmpty(name)) {
    		ClassifierDecorator type = this.getType();
    		name = StringUtils.uncapitalize(
    				StringUtils.trimToEmpty(type.getName()));
    	}
    	return name;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getType()
     */
    public ModelElement handleGetType()
    {
        return metaObject.getParticipant();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isOne2Many()
     */
    public boolean isOne2Many()
    {
        return !isMany(metaObject) && isMany((AssociationEnd)getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isMany2Many()
     */
    public boolean isMany2Many()
    {
        return isMany(metaObject) && isMany((AssociationEnd)getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isOne2One()
     */
    public boolean isOne2One()
    {
        return !isMany(metaObject) && !isMany((AssociationEnd)getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isMany2One()
     */
    public boolean isMany2One()
    {
        return isMany(metaObject) && !isMany((AssociationEnd)getOtherEnd().getMetaObject());
    }

    static protected boolean isMany(AssociationEnd ae)
    {
        Multiplicity multiplicity = ae.getMultiplicity();
        if (multiplicity == null)
        {
            return false;  // no multiplicity means multiplicity "1"
        }
        
        Collection ranges = multiplicity.getRange();

        for (Iterator i = ranges.iterator(); i.hasNext();)
        {
            MultiplicityRange range = (MultiplicityRange) i.next();
            if (range.getUpper() > 1)
            {
                return true;
            }

            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0)
            {
                return true;
            }

        }

        return false;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isOrdered()
     */
    public boolean isOrdered()
	{
    	boolean ordered = false;
    	
    	OrderingKind ordering = metaObject.getOrdering();
    	//no ordering is 'unordered'
    	if (ordering != null) {
    		ordered = ordering.equals(OrderingKindEnum.OK_ORDERED);  		
    	}

		return ordered;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isAggregation()
     */
    public boolean isAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isComposition()
     */
    public boolean isComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(
            metaObject.getAggregation());
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isReadOnly()
     */
    public boolean isReadOnly() 
	{
    	return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isNavigable()
     */
    public boolean isNavigable()
    {
        return metaObject.isNavigable();
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getGetterName()
     */
    public java.lang.String getGetterName() {
    	return "get" + StringUtils.capitalize(this.getName());
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getSetterName()
     */
    public java.lang.String getSetterName() {
    	return "set" + StringUtils.capitalize(this.getName());	
    }
    
    // relations

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#handleGetAssociation()
     */
    protected org.omg.uml.foundation.core.ModelElement handleGetAssociation() {
    	return metaObject.getAssociation();
    }

}
