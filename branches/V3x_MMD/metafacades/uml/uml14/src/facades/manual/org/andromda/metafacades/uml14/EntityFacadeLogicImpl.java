package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.uml14.UMLProfile;
import org.andromda.metafacades.uml.EntityAttributeFacade;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class EntityFacadeLogicImpl
       extends EntityFacadeLogic
       implements org.andromda.metafacades.uml.EntityFacade
{
    // ---------------- constructor -------------------------------
    
    public EntityFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityFacade ...

    // ------------- relations ------------------
    
    /**
     * @see org.andromda.metafacades.uml14.EntityFacade#getFinders
     */
    public java.util.Collection getFinders() {
        final String methodName = "EntityFacadeImpl.getFinders";
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
     * @see org.andromda.metafacades.uml14.EntityFacade#getIdentifiers()
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
     * @see edu.duke.dcri.mda.model.metafacade.EntityFacade#hasIdentifiers()
     */
    public boolean hasIdentifiers() {
        return this.getIdentifiers() != null && !this.getIdentifiers().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml14.EntityFacade#getTableName()
     */
    public String getTableName() {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                this, 
                UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                this.getMaxSqlNameLength());
    }
    
    /**
     * @see org.andromda.metafacades.uml14.EntityFacade#getAttributesAsList(boolean, boolean)
     */
    public String getAttributesAsList(boolean withTypeNames, boolean withIdentifiers) {
        StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");
        
        Collection attributes = this.getAttributes();
        
        if (attributes != null && !attributes.isEmpty()) {
            Iterator attributeIt = attributes.iterator();
            while (attributeIt.hasNext()) {
                EntityAttributeFacade attribute = 
                    (EntityAttributeFacade)attributeIt.next();
                if (withIdentifiers || !attribute.isIdentifier()) {
                    buffer.append(separator);
                    if (withTypeNames) {
                        String typeName = attribute.getType().getFullyQualifiedName();
                        buffer.append(typeName);
                        buffer.append(" ");
                        buffer.append(attribute.getName());    
                    } else {
                        buffer.append(attribute.getGetterName());
                        buffer.append("()");
                    }
                    separator = ", ";
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
    
    /**
     * SQL type specific mappings property reference.
     */
    private final static String MAX_SQL_NAME_LENGTH = "maxSqlNameLength";
    
    /**
     * Sets the maximum lenght to which a persistent SQL
     * name may be.
     * 
     * @param maxSqlNameLength the maximum length a SQL name 
     *        may be.
     */
    public void setMaxSqlNameLength(Short maxSqlNameLength) {
        this.registerConfiguredProperty(MAX_SQL_NAME_LENGTH, maxSqlNameLength);
    }
    
    /**
     * Gets the maximum name length SQL names may be 
     */
    public Short getMaxSqlNameLength() {
        return (Short)this.getConfiguredProperty(MAX_SQL_NAME_LENGTH);
    }

}
