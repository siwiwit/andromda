package org.andromda.core.metadecorators.uml14;

import org.andromda.core.common.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class AttributeDecoratorImpl extends AttributeDecorator
{
    // ---------------- constructor -------------------------------

    public AttributeDecoratorImpl(
        org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AttributeDecorator ...

    public java.lang.String getGetterName()
    {
        return "get"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public java.lang.String getSetterName()
    {
        return "set"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public String getDefaultValue()
    {
        return metaObject.getInitialValue().getBody();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeDecorator#handleGetType()
     */
    protected ModelElement handleGetType()
    {
        return metaObject.getType();
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#handleGetOwner()
     */
    public ModelElement handleGetOwner() 
	{
    	return this.metaObject.getOwner();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isReadOnly()
     */
    public boolean isReadOnly()
	{
    	return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeDecorator#isStatic()
     */
    public boolean isStatic() 
	{
    	return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeDecorator#findTaggedValue(java.lang.String, boolean)
     */
    public String findTaggedValue(String name, boolean follow) 
	{
    	name = StringUtils.trimToEmpty(name);
    	String value = findTaggedValue(name);
    	if (follow) {
	    	ClassifierDecorator type = (ClassifierDecorator)this.getType();
	    	while (value == null && type != null) {
	    		value = type.findTaggedValue(name);
	    		type = (ClassifierDecorator)type.getSuperclass();
	    	}
    	}
    	return value;
    }

    // ------------- relations ------------------

}
