package org.andromda.core.metadecorators.uml14;

import org.andromda.core.mapping.Mappings;
import org.andromda.core.uml14.UMLProfile;
import org.apache.commons.lang.StringUtils;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class EntityAttributeDecoratorImpl extends EntityAttributeDecorator
{
    // ---------------- constructor -------------------------------
    
    public EntityAttributeDecoratorImpl (org.omg.uml.foundation.core.Attribute metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityAttributeDecorator ...

    public java.lang.String getColumnName() {
		return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
				this, 
				UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
				((EntityDecoratorImpl)this.getOwner()).getMaxSqlNameLength());
    }

	protected String getColumnLength() {
		return this.findTaggedValue(
			UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
    }	
	
	/**
	 * @see edu.duke.dcri.mda.model.metafacade.EntityAttributeDecorator#isIdentifier()()
	 */
	public boolean isIdentifier() {
		return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }	

	/**
	 * @see edu.duke.dcri.mda.model.metafacade.EntityAttributeDecorator#getSqlType()()
	 */
	public java.lang.String getSqlType() {
		String value = null;
		if (this.getSqlMappings() != null) {
			ClassifierDecorator type = this.getType();
			String typeName = type.getFullyQualifiedName(true);
			String columnLength = this.getColumnLength();
			value = this.getSqlMappings().getTo(typeName);
			if (StringUtils.isBlank(value)) {
				logger.error("ERROR! missing SQL type mapping for model type '" 
						+ typeName 
						+ "' --> please adjust your model or SQL type mappings '"
						+ this.getSqlMappings().getResource() + "' accordingly");
			} 
			if (StringUtils.isNotEmpty(columnLength)) {
				char beginChar = '(';
				char endChar = ')';
				int beginIndex = value.indexOf(beginChar);
				int endIndex = value.indexOf(endChar);
				if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex) {
					String replacement = value.substring(beginIndex, endIndex) + endChar;
					value = StringUtils.replace(
						value, 
						replacement, 
						beginChar + columnLength + endChar);
				}
			}
		}
		return value;
    }	

	/**
	 * @see edu.duke.dcri.mda.model.metafacade.EntityAttributeDecorator#getLanguageToSqlType()()
	 */
	public java.lang.String getJdbcType() {
		String value = null;
		if (this.getJdbcMappings() != null) {
			ClassifierDecorator type = this.getType();
			if (type != null) {
				String typeName = type.getFullyQualifiedName(true);
				value = this.getJdbcMappings().getTo(typeName);
				if (StringUtils.isBlank(value)) {
					logger.error("ERROR! missing JDBC type mapping for model type '" 
							+ typeName 
							+ "' --> please adjust your model or JDBC type mappings '"
							+ this.getJdbcMappings().getResource() + "' accordingly");
				}
			}
		}

		return value;
    }	
	
	/**
	 * SQL type specific mappings property reference.
	 */
	private static final String SQL_MAPPINGS = "sqlMappings";
	
	/**
	 * Allows the MetafacadeFactory to populate 
	 * the language mappings for this model element.
	 * 
	 * @param mappingUri the URI of the language mappings resource.
	 */
	public void setSqlMappings(String mappingUri) {
		try {
			// register the mappings 
			this.registerConfiguredProperty(
				SQL_MAPPINGS, 
				Mappings.getInstance(mappingUri));
		} catch (Throwable th) {
			String errMsg = "Error setting '" 
				+ SQL_MAPPINGS + "' --> '" 
				+ mappingUri + "'";
			logger.error(errMsg, th);
			//don't throw the exception
		}
	}
	
	/**
	 * Gets the SQL mappings that have been set 
	 * for this entity attribute.
	 * @return the SQL Mappings instance.
	 */
	protected Mappings getSqlMappings() {
		return (Mappings)this.getConfiguredProperty(SQL_MAPPINGS);
	}
	
	/**
	 * JDBC type specific mappings property reference.
	 */
	private static final String JDBC_MAPPINGS = "jdbcMappings";
	
	/**
	 * Allows the MetafacadeFactory to populate 
	 * the language mappings for this model element.
	 * 
	 * @param mappingUri the URI of the language mappings resource.
	 */
	public void setJdbcMappings(String mappingUri) {
		try {
			// register the mappings 
			this.registerConfiguredProperty(
					JDBC_MAPPINGS, 
					Mappings.getInstance(mappingUri));
		} catch (Throwable th) {
			String errMsg = "Error setting '" 
				+ SQL_MAPPINGS + "' --> '" 
				+ mappingUri + "'";
			logger.error(errMsg, th);
			//don't throw the exception
		}
	}
	
	/**
	 * Gets the JDBC mappings.
	 */
	protected Mappings getJdbcMappings() {
		return (Mappings)this.getConfiguredProperty(JDBC_MAPPINGS);
	}

    // ------------- relations ------------------
    
}
