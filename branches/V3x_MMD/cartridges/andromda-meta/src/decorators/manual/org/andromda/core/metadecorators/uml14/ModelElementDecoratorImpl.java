package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.ModelElement
 *
 *
 */
public class ModelElementDecoratorImpl extends ModelElementDecorator
{
    // ---------------- constructor -------------------------------

    public ModelElementDecoratorImpl(
        org.omg.uml.foundation.core.ModelElement metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelElementDecorator ...

    // ------------- relations ------------------

    /**
     *
     */
    public java.util.Collection handleGetTaggedValues()
    {
        return metaObject.getTaggedValue();
    }

    // ------------------------------------------------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getPackageName()
     */
    public String getPackageName()
    {
        String packageName = "";

        for (ModelElement namespace = metaObject.getNamespace();
            (namespace instanceof org.omg.uml.modelmanagement.UmlPackage)
                && !(namespace instanceof Model);
            namespace = namespace.getNamespace())
        {
            packageName =
                "".equals(packageName)
                    ? namespace.getName()
                    : namespace.getName() + "." + packageName;
        }

        return packageName;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String fullName = getName();
        String packageName = getPackageName();
        fullName =
            "".equals(packageName)
                ? fullName
                : packageName + "." + fullName;

        return fullName;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#findTaggedValue(java.lang.String)
     */
    public String findTaggedValue(String tagName)
    {
    	tagName = StringUtils.trimToEmpty(tagName);
        for (Iterator iter = metaObject.getTaggedValue().iterator();
            iter.hasNext();
            )
        {
            TaggedValue element = (TaggedValue) iter.next();
            if (element != null) 
            {
	            if (StringUtils.trimToEmpty(element.getName()).equals(tagName))
	            {
	                return (
	                    (TaggedValueDecorator) DecoratorFactory
	                        .getInstance()
	                        .createDecoratorObject(
	                        element))
	                    .getValue();
	            }
            }

        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getStereotypeName()
     */
    public String getStereotypeName()
    {
        Collection stereotypes = metaObject.getStereotype();
        if (stereotypes == null || stereotypes.size() == 0)
        {
            return null;
        }

        ModelElement stereotype =
            (ModelElement) stereotypes.iterator().next();
        return stereotype.getName();
    }
    
    // -------------------- business methods ----------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getVisibility()
     */
    public VisibilityKind getVisibility()
	{
    	VisibilityKind visibility = metaObject.getVisibility();
    	
    	if (VisibilityKindEnum.VK_PRIVATE.equals(visibility))
    	{
    		return JavaVisibilityEnum.PRIVATE;
    	}
    	else if (VisibilityKindEnum.VK_PROTECTED.equals(visibility))
    	{
    		return JavaVisibilityEnum.PROTECTED;
    	}
    	else if (VisibilityKindEnum.VK_PUBLIC.equals(visibility))
    	{
    		return JavaVisibilityEnum.PUBLIC;
    	}

    	return JavaVisibilityEnum.PACKAGE;
    }

}
