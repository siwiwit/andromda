package org.andromda.cartridges.ejb.metadecorators.uml14;

/**
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 */
public class EJBAssociationEndDecoratorImpl extends EJBAssociationEndDecorator {

	public EJBAssociationEndDecoratorImpl(org.omg.uml.foundation.core.AssociationEnd metaObject) {
		super(metaObject);
	}

	/**
	 * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBAssociationEndDecorator#getRelationType()
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
