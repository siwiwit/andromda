package org.andromda.cartridges.ejb.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DependencyDecorator;
import org.andromda.core.metadecorators.uml14.OperationDecorator;
import org.andromda.core.uml14.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * Metaclass decorator implementation for
 * org.omg.uml.foundation.core.Classifier.
 * 
 * @author Chad Brandon
 */
public class EJBEntityDecoratorImpl extends EJBEntityDecorator {
	// ---------------- constructor -------------------------------

	public EJBEntityDecoratorImpl(
		org.omg.uml.foundation.core.Classifier metaObject) {
		super(metaObject);
	}

	// -------------------- business methods ----------------------

	// concrete business methods that were declared
	// abstract in class EJBEntityDecorator ...

	/**
	 * Find the primary key fields for <code>obj</code>. If <code>obj</code>
	 * is of type {@link Classifier}or {@link UMLClassifier}, the primary key
	 * type is determined as follows:
	 * <ol>
	 * <li>If <code>obj</code> has a dependency with stereotype
	 * &lt;&lt;PrimaryKey&gt;&gt;, the primary key fields are the public
	 * attributes of the supplier of this dependency.
	 * <li>Otherwise, if <code>obj</code> has an attribute with stereotype
	 * &lt;&lt;PrimaryKey&gt;&gt;, the primary key field is this attribute
	 * <li>If there is neither a dependency nor an attribute with the
	 * &lt;&lt;PrimaryKey&gt;&gt; stereotype, recurse up the inheritance tree.
	 * <li>Finally, if no explict primary key type can be found, return an
	 * empty list
	 * </ol>
	 * <p>
	 * If <code>obj</code> is any other type, an empty list is returned.
	 * 
	 * @param obj
	 *            the entity to check
	 * @return a collection of {@link Attribute}objects
	 */
	public Collection getPrimaryKeyFields() {

		Iterator iter = this.getDependencies().iterator();
		while (iter.hasNext()) {
			DependencyDecorator dep =
				(DependencyDecorator) iter.next();
			if (EJBProfile
				.STEREOTYPE_PRIMARY_KEY
				.equals(dep.getStereotypeName())) {
				Collection allAttrib =
					dep.getTargetType().getInstanceAttributes();
				Collection publicAttrib = new ArrayList();
				for (Iterator i = allAttrib.iterator(); i.hasNext();) {
					AttributeDecorator att = (AttributeDecorator) i.next();
					if ("public".equals(att.getVisibility())) {
						publicAttrib.add(att);
					}
				}
				return publicAttrib;
			}
		}
		// No PK dependency found - try a PK attribute
		AttributeDecorator attr = this.getPrimaryKeyAttribute();
		if (attr != null) {
			Collection retval = new ArrayList(1);
			retval.add(attr);
			return retval;
		}

		// Still nothing found - recurse up the inheritance tree
		EJBEntityDecorator decorator =
			(EJBEntityDecorator) this.getSuperclass();
		return decorator.getPrimaryKeyFields();
	}

	/**
	 * Gets all associations that define relations to other entities.
	 * <p>
	 * This method returns the {@linkplain AssociationEnd source association
	 * end} for all associations that define a container managed relation for
	 * this entity.
	 * 
	 * <p>
	 * The returned collection includes both <em>direct relations</em> and
	 * <em>inherited relations</em>. A <em>direct relation</em> is an
	 * association of this entity with some other class matching the following
	 * criteria:
	 * </p>
	 * <ul>
	 * <li>The class at the other side of the association is stereotyped
	 * &lt;&lt;Entity&gt;&gt;
	 * <li>The association is navigable from this entity to the other side.
	 * </ul>
	 * <p>
	 * An <em>inherited relation</em> is an asscoiated from an abstract super
	 * type of <code>sourceClass</code> matching the following criteria:
	 * <ul>
	 * <li>the inheritance path from from this entity to this abstract super
	 * type, including this super type itself, consists only of abstract
	 * classes with stereotype &lt;&lt;Entity&gt;&gt;
	 * <li>The class at the other side of the association is stereotyped
	 * &lt;&lt;Entity&gt;&gt;
	 * <li>The association is navigable from this abstract super type of this
	 * entity to the other side.
	 * </ul>
	 * 
	 * <p>
	 * Relations must match the following integrity constraint:
	 * </p>
	 * <ul>
	 * <li>The &lt;&lt;Entity&gt;&gt; at the target end is not abstract
	 * </ul>
	 * <p>
	 * The integrity constraint is necessary because the target of a container
	 * managed relation in the EJB framework must be a concrete entity bean
	 * &mdash; there is no such thing as an "abstract entity bean" in the EJB
	 * specification. It is possible, however, to generate and compile code for
	 * this case, an error will only show up at deploy time. In order to catch
	 * this kind of error at the earliest possible stage, this method checks
	 * the integrity constraint and throws an exception if it is violated.
	 * </p>
	 * 
	 * @return a collection of {@link DecoratorAssociationEnd}objects. @throw
	 *         IllegalStateException if a relation violates the integrity
	 *         constraint
	 */
	public java.util.Collection getAllEntityRelations() {

		// Only concrete entities may have EJB relations. Return
		// an empty collection for everything else
		if (this.isAbstract()) {
			return Collections.EMPTY_LIST;
		}

		Collection result = new ArrayList();
		System.out.println("before getEntityRelations()");
		result.addAll(getEntityRelations());

		System.out.println("after getEntityRelations()");
		ClassifierDecorator classifier = this.getSuperclass();
		while (classifier != null
			&& classifier instanceof EJBEntityDecorator
			&& classifier.isAbstract()) {

			EJBEntityDecorator entity = (EJBEntityDecorator) classifier;
			System.out.println("after (EJBEntityDecorator) classifier");
			result.add(entity.getEntityRelations());
			classifier = this.getSuperclass();
		}
		return result;
	}
	
	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecorator#getViewType()
	 */
	public String getViewType() {
		if (UMLProfile.STEREOTYPE_ENTITY.equals(
			StringUtils.trimToEmpty(this.getStereotypeName()))) {
			return "local";
		}

		return "remote";
	}

	/**
	 * Get the associations that define relations to other entities. This is
	 * similar to {@link  #findEntityRelationsForSource()}, but gets only
	 * relations that are defined directly on <code>sourceType</code> and it
	 * returns relations even if <code>sourceType</code> is an abstract
	 * entity. @throw IllegalStateException if a relation violates the
	 * integrity constraint
	 */
	public java.util.Collection getEntityRelations() {

		Collection result = new ArrayList();

		Iterator i = this.getAssociationEnds().iterator();
		while (i.hasNext()) {
			EJBAssociationEndDecorator assoc =
				(EJBAssociationEndDecorator) i.next();
			ClassifierDecorator target = assoc.getOtherEnd().getType();
			if (target instanceof EJBEntityDecorator
				&& assoc.getOtherEnd().isNavigable()) {
				// Check the integrity constraint
				String generateCmr =
					assoc.getOtherEnd().getAssociation().findTaggedValue(
					  EJBProfile.TAGGEDVALUE_GENERATE_CMR);
				if (target.isAbstract()
					&& !"false".equalsIgnoreCase(generateCmr)) {
					throw new IllegalStateException(
						"Relation '"
							+ assoc.getRelationName()
							+ "' has the abstract target '"
							+ target.getName()
							+ "'. Abstract targets are not allowed in EJB.");
				} else {
					result.add(assoc);
				}
			}
		}
		return result;
	}
	
	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecorator#getAllInstanceAttributes()
	 */
	public List getAllInstanceAttributes() {
		List retval = this.getInheritedInstanceAttributes();
		retval.addAll(this.getInstanceAttributes());
		return retval;		
	}
	
	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecorator#getInheritedInstanceAttributes()
	 */
	public List getInheritedInstanceAttributes() {
		EJBEntityDecorator current = (EJBEntityDecorator)this.getSuperclass();
		if (current == null) {
			return new ArrayList();
		} else {
			List retval = current.getInheritedInstanceAttributes();
			return retval;
		}
	}
	
	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecorator#getCreateMethods(boolean)
	 */
	public Collection getCreateMethods(boolean all) {
		Collection retval = new ArrayList();
		EJBEntityDecorator entity = null;
		do {
			Collection ops = this.getOperations();
			for (Iterator i = ops.iterator(); i.hasNext();) {
				OperationDecorator op = (OperationDecorator) i.next();
				if (StringUtils.trimToEmpty(op.getStereotypeName()).equals(
					EJBProfile.STEREOTYPE_CREATE_METHOD)) {
					retval.add(op);
				}
			}
			if (all) {
				entity = (EJBEntityDecorator)this.getSuperclass();
			} else {
				break;
			}
		} while (entity != null);
		return retval;
	}

	/**
	 * @see org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecorator#getSelectMethods(boolean)
	 */
	public Collection getSelectMethods(boolean all) {
		Collection retval = new ArrayList();
		EJBEntityDecorator entity = null;
		do {
			Collection ops = this.getOperations();
			for (Iterator i = ops.iterator(); i.hasNext();) {
				OperationDecorator op = (OperationDecorator) i.next();
				if (StringUtils.trimToEmpty(op.getStereotypeName()).equals(
					EJBProfile.STEREOTYPE_SELECT_METHOD)) {
					retval.add(op);
				}
			}
			if (all) {
				entity = (EJBEntityDecorator)this.getSuperclass();
			} else {
				break;
			}
		} while (entity != null);
		return retval;
	}
	

}
