package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.HTMLParagraph;
import org.andromda.core.uml14.UMLProfile;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
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

    /* (non-Javadoc)
    * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#hasStereotype(String)
    */
    public Boolean hasStereotype(String stereotypeName)
    {
        if (stereotypeName == null) return Boolean.FALSE;

        final Collection stereotypes = metaObject.getStereotype();
        for (Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
        {
            Stereotype stereotype = (Stereotype) iterator.next();
            if (stereotypeName.equals(stereotype.getName()))
                return Boolean.TRUE;
        }

        return Boolean.FALSE;
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
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getDocumentation(java.lang.String)
     */
    public java.lang.String getDocumentation(String indent) {
    	if (StringUtils.isEmpty(indent)) {
    		indent = " ";
    	}
    	StringBuffer documentation = new StringBuffer();
    	Collection comments = this.metaObject.getComment();
    	if (comments != null && !comments.isEmpty()) {
    		Iterator commentIt = comments.iterator();
    		while (commentIt.hasNext()) {
    			Comment comment = (Comment)commentIt.next();
    			String commentString = StringUtils.trimToEmpty(comment.getBody());
    			//if there isn't anything in the body, try the name
    			if (StringUtils.isEmpty(commentString)) {
    				commentString = StringUtils.trimToEmpty(comment.getName());
    			}
    			// if there still isn't anything, try a tagged value 
    			if (StringUtils.isEmpty(commentString)) {
    				commentString = StringUtils.trimToEmpty(
    					this.findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION));
    			}
    			documentation.append(
    					StringUtils.trimToEmpty(commentString));
    		}
    	}
    	try {
    		String star = "* ";
    		String newLine = "\n";
    		String startParaTag = "<p>";
    		String endParaTag = "</p>";
    		Collection paragraphs =
    			new HTMLAnalyzer().htmlToParagraphs(documentation.toString());
    		if (paragraphs != null && !paragraphs.isEmpty()) {
    			documentation = new StringBuffer();
    			Iterator paragraphIt = paragraphs.iterator();
    			for (int ctr = 0; paragraphIt.hasNext(); ctr++) {
    				HTMLParagraph paragraph = (HTMLParagraph)paragraphIt.next();
    				documentation.append(indent + star + startParaTag + newLine);
    				Collection lines = paragraph.getLines();
    				if (lines != null && !lines.isEmpty()) {
    					Iterator lineIt = lines.iterator();
    					while (lineIt.hasNext()) {
    						documentation.append(indent + star + lineIt.next() + newLine);
    					}
    				}
    				documentation.append(indent + star + endParaTag);
    			}
    		} else {
    			documentation.append(indent + star);
    		}
    	} catch (Throwable th) {
    		String errMsg = "Error performing getDocumentationParagraphs";
    		logger.error(errMsg, th);
    	}
    	return documentation.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getName()
     */
    public String getName()
    {
        return metaObject.getName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getMetaObject()
     */
    public ModelElement getMetaObject()
    {
        return metaObject;
    }

}
