package org.andromda.cartridges.ejb.metadecorators.uml14;

import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 *
 *
 */
public class EJBAssociationEndDecoratorImpl extends EJBAssociationEndDecorator
{
    // ---------------- constructor -------------------------------
    
    public EJBAssociationEndDecoratorImpl (org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EJBAssociationEndDecorator ...

    public java.lang.String getRelationName() {
    	EJBAssociationEndDecorator source = this;
    	EJBAssociationEndDecorator target = 
    		(EJBAssociationEndDecorator)source.getOtherEnd();
    	
    	// Get the seperator
    	String seperator = source.getAssociation().getName();
    	if (seperator != null) {
    		seperator = seperator.trim();
    		if (seperator.length() == 0) {
    			seperator = null;
    		} else {
    			seperator = "-" + seperator.replace(' ', '-') + "-";
    		}
    	}        
    	if (seperator == null) {
    		AggregationKind aggregation = source.getAggregation();
    		if (AggregationKindEnum.AK_NONE.equals(aggregation)) {
    			aggregation = target.getAggregation();
    		}
    		if (AggregationKindEnum.AK_AGGREGATE.equals(aggregation)) {
    			seperator = "-has-";
    		} else if (AggregationKindEnum.AK_COMPOSITE.equals(aggregation)) {
    			seperator = "-consists-of-";
    		} else if (!(target.isNavigable() && source.isNavigable())) {
    			seperator = "->";
    		} else {
    			seperator = "-";
    		}
    	}
    	
    	boolean srcIsAggregate =
    		source.getAggregation() != null
			&& !AggregationKindEnum.AK_NONE.equals(source.getAggregation());
    	boolean targetIsAggregate =
    		target.getAggregation() != null
			&& !AggregationKindEnum.AK_NONE.equals(target.getAggregation());

    	// Generate the names. Swap sides if necessary.
    	if ((source.isNavigable() && !target.isNavigable())
		|| (!srcIsAggregate && targetIsAggregate)
		|| (!srcIsAggregate 
				&& source.isNavigable()
				&& target.isNavigable()
				&& source.getParticipant().getName().compareTo(
						target.getParticipant().getName())
				> 0)) {
    		return target.getParticipant().getName()
			+ seperator
			+ source.getName();
    	} else {
    		return source.getName()
			+ seperator
			+ target.getParticipant().getName();
    	}
    }
    
    // ------------- relations ------------------
    
}
