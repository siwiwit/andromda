package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.uml14.UMLProfile;
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
    
    public Collection getFinders() {

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
     * @see org.andromda.core.metadecorators.uml14.EntityDecorator#getIdentifiers()
     */
    public java.util.Collection getIdentifiers() {
    	final String methodName = "EntityFacadeImpl.getIdentifiers";
    	if (logger.isDebugEnabled())
    		logger.debug("performing " + methodName);
    	
    	Collection attributes = this.getAttributes();
    		Collection associationEnds = this.getAssociationEnds();				
    	MetafacadeUtils.filterByStereotype(
    			attributes, 
    			UMLProfile.STEREOTYPE_PRIMARY_KEY);

    	if (logger.isDebugEnabled())
    		logger.debug("completed " + methodName + 
    				" with identifiers --> '" + attributes + "'");

    	return attributes;
    }	
    
    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityFacade#hasIdentifiers()
     */
    public boolean hasIdentifiers() {
    	return this.getIdentifiers() != null && !this.getIdentifiers().isEmpty();
    }
    
   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetPrimaryKeyAttribute()
    {
        for (Iterator i = getAttributes().iterator(); i.hasNext();)
        {
            AttributeDecorator attribute = (AttributeDecorator)i.next();
            if (attribute.hasStereotype(UMLProfile.STEREOTYPE_PRIMARY_KEY)) {
            	return attribute.getMetaObject();
            }
        }

        return null;
    }
    
    public String getTableName() {
    	// TODO: implement getTableName()
    	return null;
    }
    // ------------------------------------------------------------

}
