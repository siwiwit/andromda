package org.andromda.cartridges.ejb.metadecorators.uml14;

/**
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 */
public class EJBAssociationEndDecoratorImpl extends EJBAssociationEndDecorator {

	public EJBAssociationEndDecoratorImpl(org.omg.uml.foundation.core.AssociationEnd metaObject) {
		super(metaObject);
	}

	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBAssociationEndDecorator#getRelationName()
	 */
	public java.lang.String getRelationName() {
		String relationName;
		EJBAssociationEndDecorator target =
			(EJBAssociationEndDecorator) this.getOtherEnd();

		// Get the seperator
		String seperator = this.getAssociation().getName();
		if (seperator != null) {
			seperator = seperator.trim();
			if (seperator.length() == 0) {
				seperator = null;
			} else {
				seperator = "-" + seperator.replace(' ', '-') + "-";
			}
		}
		if (seperator == null) {
			EJBAssociationEndDecorator aggregateEnd = this;
			if (!aggregateEnd.isAggregation()
				&& !aggregateEnd.isComposition()) {
				aggregateEnd = target;
			}
			if (aggregateEnd.isAggregation()) {
				seperator = "-has-";
			} else if (aggregateEnd.isComposition()) {
				seperator = "-consists-of-";
			} else if (!(target.isNavigable() && this.isNavigable())) {
				seperator = "->";
			} else {
				seperator = "-";
			}
		}

		boolean srcIsAggregate =
			this.isAggregation() || this.isComposition();
		boolean targetIsAggregate =
			target.isAggregation() || target.isComposition();

		// Generate the names. Swap sides if necessary.
		if ((this.isNavigable() && !target.isNavigable())
			|| (!srcIsAggregate && targetIsAggregate)
			|| (!srcIsAggregate
				&& this.isNavigable()
				&& target.isNavigable()
				&& this.getName().compareTo(target.getName()) > 0)) {
			relationName = 
				target.getName() + seperator + this.getName();
		} else {
			relationName = 
				this.getName() + seperator + target.getName();
		}
		return relationName;
	}

	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBAssociationEndDecorator#getRelationType()
	 */
	public String getRelationType() {
		String targetType;
		if (this.isMany2Many() || this.isOne2Many()) {
			targetType = "java.util.Collection";
		} else {
			targetType = this.getOtherEnd().getType().getFullyQualifiedName();
		}
		return targetType;
	}

	// ------------- relations ------------------

}
