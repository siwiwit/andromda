package org.andromda.cartridges.ejb.metadecorators.uml14;




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
    		EJBAssociationEndDecorator aggregateEnd = source;
    		if (!aggregateEnd.isAggregation() && !aggregateEnd.isComposition()) {
    			aggregateEnd  = target;
    		}
    		if (aggregateEnd.isAggregation()) {
    			seperator = "-has-";
    		} else if (aggregateEnd.isComposition()) {
    			seperator = "-consists-of-";
    		} else if (!(target.isNavigable() && source.isNavigable())) {
    			seperator = "->";
    		} else {
    			seperator = "-";
    		}
    	}
    	
    	boolean srcIsAggregate = source.isAggregation() || source.isComposition();
    	boolean targetIsAggregate = target.isAggregation() || target.isComposition();

    	// Generate the names. Swap sides if necessary.
    	if ((source.isNavigable() && !target.isNavigable())
		|| (!srcIsAggregate && targetIsAggregate)
		|| (!srcIsAggregate 
				&& source.isNavigable()
				&& target.isNavigable()
				&& source.getType().getName().compareTo(
						target.getType().getName())
				> 0)) {
    		return target.getType().getName()
			+ seperator
			+ source.getName();
    	} else {
    		return source.getName()
			+ seperator
			+ target.getType().getName();
    	}
    }
    
    /**
     * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBAssociationEndDecorator#getRelationType()
     */
    public String getRelationType() {
    	String targetType;
	    if (this.isMany2Many() || this.isOne2Many()) {
	    	targetType = "java.util.Collection"; 
	    } else {
	    	targetType = this.getType().getFullyQualifiedName();
	    }
	    return targetType;
    }
    
    // ------------- relations ------------------
    
}
