package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * A class containing utlities for metafacade manipulation.
 * 
 * @author Chad Brandon
 */
public class MetafacadeUtils {

	/**
	 * Filters out the model elements from the <code>modelElements</code> collection
	 * that don't have the specified <code>stereotype</code>
	 * @param modelElements the model elements to filter.
	 * @param stereotype the stereotype that a model element must have
	 *        in order to stay remain within the <code>modelElements</code>
	 *        collection.
	 */
	public static void filterByStereotype(Collection modelElements, final String stereotype) {
		final String methodName = "MetafacadeUtils.filterByStereotype";
		ExceptionUtils.checkEmpty(methodName, "stereotype", stereotype);
		class StereotypeFilter implements Predicate {
			public boolean evaluate(Object object) {
				return ((ModelElementDecorator)object).hasStereotype(
						stereotype);
			}
		}	
		CollectionUtils.filter(modelElements, new StereotypeFilter());
	}
}
