package org.andromda.core.metadecorators.uml14;

import org.andromda.core.uml14.UMLProfile;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 *
 *
 */
public class EntityAssociationEndDecoratorImpl extends EntityAssociationEndDecorator
{
    // ---------------- constructor -------------------------------
    
    public EntityAssociationEndDecoratorImpl (org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityAssociationEndDecorator ...

	/**
	 * @see edu.duke.dcri.mda.model.metafacade.EntityAssociationEndFacade#getColumnName()()
	 */
	public java.lang.String getColumnName() {
		return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
			this, 
			UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
			((EntityDecoratorImpl)this.getType()).getMaxSqlNameLength(),
			this.getForeignKeySuffix());
    }	

	/**
	 * The foreign key suffix.
	 */
	private static final String FOREIGN_KEY_SUFFIX = "foreignKeySuffix";
	
	/**
	 * Sets the suffix for foreign keys.
	 * 
	 * @param foreignKeySuffix the suffix for foreign keys 
	 * (i.e. '_FK').
	 */
	public void setForeignKeySuffix(String foreignKeySuffix) {
		try {
			// register the mappings 
			this.registerConfiguredProperty(
					FOREIGN_KEY_SUFFIX, 
					foreignKeySuffix);
		} catch (Throwable th) {
			String errMsg = "Error setting '" 
				+ FOREIGN_KEY_SUFFIX + "' --> '" 
				+ foreignKeySuffix + "'";
			logger.error(errMsg, th);
			//don't throw the exception
		}
	}
	
	/**
	 * Gets the maximum name length SQL names may be 
	 */
	protected String getForeignKeySuffix() {
		return (String)this.getConfiguredProperty(FOREIGN_KEY_SUFFIX );
	}
	

    // ------------- relations ------------------
    
}
