package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

import org.andromda.core.uml14.UMLProfile;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class EntityDecoratorImpl extends EntityDecorator
{

    // ---------------- constructor -------------------------------
    
    public EntityDecoratorImpl (org.omg.uml.foundation.core.Classifier metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityDecorator ...

    // ------------- relations ------------------
    
    /**
     * @see org.andromda.core.metadecorators.uml14.EntityDecorator#getFinders
     */
	public java.util.Collection getFinders() {
		final String methodName = "EntityDecoratorImpl.getFinders";
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName);
		
		Collection operations = this.getOperations();
		MetafacadeUtils.filterByStereotype(
				operations, 
				UMLProfile.STEREOTYPE_FINDER_METHOD);
		
		if (logger.isDebugEnabled())
			logger.debug("completed " + methodName + 
					" with finders --> '" + operations + "'");

		return operations;
    }	
    
    /**
     * @see org.andromda.core.metadecorators.uml14.EntityDecorator#getIdentifiers()
     */
    public java.util.Collection getIdentifiers() {
    	final String methodName = "EntityFacadeImpl.getIdentifiers";
    	if (logger.isDebugEnabled())
    		logger.debug("performing " + methodName);
    	
    	Collection attributes = this.getAttributes();		
    	MetafacadeUtils.filterByStereotype(
    			attributes, 
    			UMLProfile.STEREOTYPE_IDENTIFIER);

    	if (logger.isDebugEnabled())
    		logger.debug("completed " + methodName + 
    				" with identifiers --> '" + attributes + "'");

    	return attributes;
    }	
    
    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityDecorator#hasIdentifiers()
     */
    public boolean hasIdentifiers() {
    	return this.getIdentifiers() != null && !this.getIdentifiers().isEmpty();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.EntityDecorator#getTableName()
     */
    public String getTableName() {
		return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
				this, 
				UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
				this.getMaxSqlNameLength());
    }
    
	/**
	 * SQL type specific mappings property reference.
	 */
	private static final String MAX_SQL_NAME_LENGTH = "maxSqlNameLength";
	
	/**
	 * Sets the maximum lenght to which a persistent SQL
	 * name may be.
	 * 
	 * @param maxSqlNameLength the maximum length a SQL name 
	 *        may be.
	 */
	public void setMaxSqlNameLength(String maxSqlNameLength) {
		try {
			// register the mappings 
			this.registerConfiguredProperty(
					MAX_SQL_NAME_LENGTH, 
					Short.valueOf(maxSqlNameLength));
		} catch (Throwable th) {
			String errMsg = "Error setting '" 
				+ MAX_SQL_NAME_LENGTH + "' --> '" 
				+ maxSqlNameLength + "'";
			logger.error(errMsg, th);
			//don't throw the exception
		}
	}
	
	/**
	 * Gets the maximum name length SQL names may be 
	 */
	protected Short getMaxSqlNameLength() {
		return (Short)this.getConfiguredProperty(MAX_SQL_NAME_LENGTH);
	}
    
    // ------------------------------------------------------------

}
