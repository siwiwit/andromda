package org.andromda.cartridges.ejb.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.metadecorators.uml14.AssociationDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DependencyDecorator;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

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

		Iterator iter = this.getClientDependency().iterator();
		while (iter.hasNext()) {
			DependencyDecorator dep =
				(DependencyDecorator) decoratedElement((Dependency) iter
					.next());
			if (EJBProfile
				.STEREOTYPE_PRIMARY_KEY
				.equals(dep.getStereotypeName())) {
				Collection allAttrib =
					getInstanceAttributes(
						(ClassifierDecorator) dep
							.getSupplier()
							.iterator()
							.next());
				Collection publicAttrib = new ArrayList();
				for (Iterator i = allAttrib.iterator(); i.hasNext();) {
					Attribute att = (Attribute) i.next();
					if ("public".equals(getVisibility(att))) {
						publicAttrib.add(att);
					}
				}
				return publicAttrib;
			}
		}
		// No PK dependency found - try a PK attribute
		Attribute attr = this.getPrimaryKeyAttribute();
		if (attr != null) {
			Collection retval = new ArrayList(1);
			retval.add(attr);
			return retval;
		}

		// Still nothing found - recurse up the inheritance tree
		EJBEntityDecorator decorator =
			(EJBEntityDecorator) decoratedElement(this.getSuperclass());
		return decorator.getPrimaryKeyFields();
	}

	/**
	 * Gets the non-static attributes of the specified Classifier object.
	 * 
	 * @param object
	 *            Classifier object
	 * @return Collection of
	 *         org.andromda.core.metadecorators.AttributeDecorator
	 */
	private Collection getInstanceAttributes(ClassifierDecorator classifier) {
		Collection attributes = new ArrayList();
		for (Iterator i = classifier.getFeature().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Attribute && !isStatic(((Attribute) o))) {
				attributes.add(o);
			}
		}
		return attributes;
	}

	/**
	 * Check if <code>feature</code> is declared static.
	 * 
	 * @param feature
	 *            the structural feature to check
	 * @return <code>true</code> if <code>feature</code> is static, <code>false</code>
	 *         else
	 */
	private boolean isStatic(Feature feature) {

		return ScopeKindEnum.SK_CLASSIFIER.equals(feature.getOwnerScope());
	}

	/**
	 * Convert the visibility of <code>feature</code> into a string usable in
	 * Java source code if it is one of the predefined constants in
	 * {@link VisibilityKindEnum}. If the visibility is not one of these
	 * constants, this methods returns the string representation of the
	 * visibility. Note: This method really belongs into
	 * {@link UMLStaticHelper}
	 * 
	 * @param element
	 *            a UML class, attribute or operation
	 * @return a string representing the visibility
	 */
	private String getVisibility(ModelElement element) {
		VisibilityKind vis = element.getVisibility();
		if (VisibilityKindEnum.VK_PUBLIC.equals(vis)) {
			return "public";
		} else if (VisibilityKindEnum.VK_PACKAGE.equals(vis)) {
			return "";
		} else if (VisibilityKindEnum.VK_PROTECTED.equals(vis)) {
			return "protected";
		} else if (VisibilityKindEnum.VK_PRIVATE.equals(vis)) {
			return "private";
		} else {
			return vis.toString();
		}
	}

	// ------------- relations ------------------
	
	
	/**
	 * Gets all associations that define relations to other entities.
	 * <p>This method returns the {@linkplain AssociationEnd 
	 * source association end} for all associations that define 
	 * a container managed relation for this entity. 
	 * 
	 * <p>The returned collection includes both 
	 * <em>direct relations</em> and <em>inherited relations</em>. A 
	 * <em>direct relation</em> is an association of this entity 
	 * with some other class matching the following criteria:</p>
	 * <ul>
	 * <li>The class at the other side of the association is 
	 * stereotyped &lt;&lt;Entity&gt;&gt;
	 * <li>The association is navigable from this entity to 
	 * the other side.
	 * </ul>
	 * <p>An <em>inherited relation</em> is an asscoiated from an  
	 * abstract super type of <code>sourceClass</code> matching 
	 * the following criteria:
	 * <ul>
	 * <li>the inheritance path from from this entity to this abstract
	 * super type, including this super type itself, consists only of 
	 * abstract classes with stereotype &lt;&lt;Entity&gt;&gt;
	 * <li>The class at the other side of the association is 
	 * stereotyped &lt;&lt;Entity&gt;&gt;
	 * <li>The association is navigable from this abstract super type of 
	 * this entity to the other side.
	 * </ul>
	 * 
	 * <p>Relations must match the following integrity 
	 * constraint:</p>
	 * <ul>
	 * <li>The &lt;&lt;Entity&gt;&gt; at the target end is not abstract
	 * </ul>
	 * <p>The integrity constraint is necessary because the target of 
	 * a container managed relation in the EJB framework must be a 
	 * concrete entity bean &mdash; there is no such thing as an 
	 * "abstract entity bean" in the EJB specification. 
	 * It is possible, however, to generate and compile code for this case, 
	 * an error will only show up at deploy time. In order to catch 
	 * this kind of error at the earliest possible stage, 
	 * this method checks the integrity constraint and 
	 * throws an exception if it is violated.</p>
	 * 
	 * @return a collection of {@link DecoratorAssociationEnd} objects. 
	 * @throw IllegalStateException if a relation violates the integrity constraint
	 */
	public java.util.Collection getAllEntityRelations() {

		// Only concrete entities may have EJB relations. Return 
		// an empty collection for everything else
		if (this.isAbstract()) {
			return Collections.EMPTY_LIST;
		}
		
		Collection result = new ArrayList();
		result.addAll(getEntityRelations());
		
		Classifier classifier = this.getSuperclass();
		while (classifier != null && 
			   classifier instanceof EJBEntityDecorator && 
			   classifier.isAbstract()) {
			   	
			EJBEntityDecorator entity = (EJBEntityDecorator)classifier;			
	        result.add(entity.getEntityRelations());
	        classifier = this.getSuperclass();
		}
		return result;
	}
	
	/**
	 * Get the associations that define relations to other entities.
	 * This is similar to {@link  #findEntityRelationsForSource()}, but gets
	 * only relations that are defined directly on <code>sourceType</code> and
	 * it returns relations even if <code>sourceType</code> is an abstract entity.  
	 * @throw IllegalStateException if a relation violates the 
	 * integrity constraint
	 */
	public java.util.Collection getEntityRelations() {
		GeneralizableElement entity;
		
		Collection result = new ArrayList();
		
		Iterator i = this.getAssociationEnds().iterator();
		while (i.hasNext()) {
			EJBAssociationEndDecorator assoc =
				(EJBAssociationEndDecorator) i.next();
			Classifier target = assoc.getOtherEnd().getParticipant();

			if (target instanceof EJBEntityDecorator && assoc.getOtherEnd().isNavigable()) {
				// Check the integrity constraint
				String generateCmr = 
					((AssociationDecorator)assoc.getOtherEnd().getAssociation()).findTaggedValue(
						EJBProfile.TAGGEDVALUE_GENERATE_CMR);
				if (target.isAbstract() && !"false".equalsIgnoreCase(generateCmr)) {
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

}
