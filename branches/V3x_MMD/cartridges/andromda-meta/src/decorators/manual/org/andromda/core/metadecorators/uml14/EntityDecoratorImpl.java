package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class EntityDecoratorImpl extends EntityDecorator
{
    private final static String PRIMARY_KEY = "PrimaryKey";
    private final static String FINDER_METHOD = "FinderMethod";

    // ---------------- constructor -------------------------------
    
    public EntityDecoratorImpl (org.omg.uml.foundation.core.Classifier metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityDecorator ...

    // ------------- relations ------------------
    
    public Collection getFinderMethods() {

		class FinderMethodFilter implements Predicate {
			public boolean evaluate(Object obj) {
				boolean valid = false;    				
				if (obj != null && obj.equals(FINDER_METHOD)) {
					valid = true;
				}
				return valid;
			}
		}
		
		Collection finderMethods = this.getOperations();
    	CollectionUtils.filter(
    		finderMethods, 
			new FinderMethodFilter());
    
    	return decoratedElements(finderMethods);
    }
    
   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetPrimaryKeyAttribute()
    {
        for (Iterator i = getAttributes().iterator(); i.hasNext();)
        {
            AttributeDecorator attribute = (AttributeDecorator) i.next();
            if (attribute.getStereotype().equals(PRIMARY_KEY))
            {
                return attribute;
            }
        }

        return null;
    }

    // ------------------------------------------------------------

}
