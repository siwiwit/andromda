package org.andromda.cartridges.ejb.metadecorators.uml14;

import java.util.Collection;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.metadecorators.uml14.AttributeDecorator;

/**
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute.
 * 
 * @author Chad Brandon
 */
public class EJBPrimaryKeyDecoratorImpl extends EJBPrimaryKeyDecorator {
	// ---------------- constructor -------------------------------

	public EJBPrimaryKeyDecoratorImpl(
		org.omg.uml.foundation.core.Attribute metaObject) {
		super(metaObject);
	}

	// -------------------- business methods ----------------------

	// concrete business methods that were declared
	// abstract in class PrimaryKeyDecorator ...

	/**
	 * Check if the primary key for <code>class</code> has been specified
	 * with a dependency to an external class.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return <code>true</code> if the primary key has ben specified by a
	 *         dependency, <code>false</code> if it has been specified by an
	 *         attribute marked as &lt;&lt;PrimaryKey&gt;&/gt;
	 */
	public boolean isComplex() {
		return getSimplePkField() == null;
	}

	/**
	 * If this <code>object</code> does not have a complex primary key, get
	 * the (unqiue) attribute that is used as the primary key.
	 * 
	 * @param object
	 *            the class to check
	 * @return the attribute used as primary key, or <code>null</code> if
	 *         there is none or the class has a complex primary key.
	 */
	private AttributeDecorator getSimplePkField() {
		AttributeDecorator primaryKey = null;
		Collection primaryKeys =
			((EJBEntityDecorator) this.getOwner()).getIdentifiers();
		if (primaryKeys.size() == 1) {
			AttributeDecorator pkField =
				(AttributeDecorator) primaryKeys.iterator().next();
			if (pkField.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER)) {
				primaryKey = pkField;
			}
		}
		return primaryKey;
	}

	// ------------- relations ------------------

}
