package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.HTMLParagraph;
import org.andromda.core.mapping.Mappings;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.modelmanagement.Model;
  

/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ModelElementFacadeLogicImpl
       extends ModelElementFacadeLogic
       implements org.andromda.metafacades.uml.ModelElementFacade
{
    // ---------------- constructor -------------------------------
    
    public ModelElementFacadeLogicImpl (org.omg.uml.foundation.core.ModelElement metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetTaggedValues()
     */
    public java.util.Collection handleGetTaggedValues()
    {
        return metaObject.getTaggedValue();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
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

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    public java.lang.String getFullyQualifiedName(boolean modelName) 
    {
        String fullName = StringUtils.trimToEmpty(this.getName());
        
        String packageName = getPackageName();

        if (StringUtils.isNotEmpty(packageName)) 
        {
            fullName = packageName + "." + fullName;
        }

        if (!modelName) 
        {   
            if (this.getLanguageMappings() != null) 
            {
                fullName = StringUtils.deleteWhitespace(
                        this.getLanguageMappings().getTo(fullName));
            }
        }
        
        return fullName;
    }   

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public java.lang.String getFullyQualifiedName() 
    {
        return this.getFullyQualifiedName(false);
    }   

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    public String findTaggedValue(String name)
    {
        name = StringUtils.trimToEmpty(name);
        Collection taggedValues = this.getTaggedValues();
        String value = null;
        for (Iterator taggedValueIt = taggedValues.iterator(); 
             taggedValueIt.hasNext();) 
        {
            TaggedValueFacade taggedValue = 
                (TaggedValueFacade) taggedValueIt.next();
            String tagName = taggedValue.getName();
            if (tagName.equals(name)) 
            {
                value = taggedValue.getValue();
                break;
            }
        }

        return value;
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementFacade#hasStereotype(String)
     */
    public boolean hasStereotype(final String stereotypeName)
    {
        Collection stereotypes = this.getStereotypes();

        boolean hasStereotype = 
            StringUtils.isNotBlank(stereotypeName) &&
            stereotypes != null && 
            !stereotypes.isEmpty();

        if (hasStereotype) 
        {
            class StereotypeFilter implements Predicate 
            {
                public boolean evaluate(Object object) 
                {
                    boolean valid = false;
                    StereotypeFacade stereotype = (StereotypeFacade)object;
                    String name = StringUtils.trimToEmpty(stereotype.getName());
                    valid = stereotypeName.equals(name);
                    while (!valid && stereotype != null) 
                    {
                        stereotype = 
                            (StereotypeFacade)stereotype.getGeneralization();
                        valid = stereotype != null && 
                            StringUtils.trimToEmpty(stereotype.getName()).equals(stereotypeName);
                    }
                    return valid;
                }
            }
            hasStereotype = CollectionUtils.find(
                this.getStereotypes(), 
                new StereotypeFilter()) != null;
        }
        return hasStereotype;
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    public boolean hasExactStereotype(String stereotypeName) {
        return this.getStereotypeNames().contains(
                StringUtils.trimToEmpty(stereotypeName));        
    }
   
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
		StringBuffer visibility = new StringBuffer();
		VisibilityKind visibilityKind = metaObject.getVisibility();
		if (visibilityKind != null) {
			String visibilityString = visibilityKind.toString();
			visibility.append(
					visibilityString.substring(3, visibilityString.length()));
			if (this.getLanguageMappings() != null) {
				visibility = new StringBuffer(
					this.getLanguageMappings().getTo(visibility.toString()));
			}
		}
		return visibility.toString();
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    public java.util.Collection getStereotypeNames() {
        Collection stereotypeNames = new ArrayList();

        Collection stereotypes = metaObject.getStereotype();
        for (Iterator stereotypeIt = stereotypes.iterator(); 
             stereotypeIt.hasNext();) {
            ModelElement stereotype = (ModelElement) stereotypeIt.next();
            stereotypeNames.add(StringUtils.trimToEmpty(stereotype.getName()));
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return '/' + getPackageName().replace('.','/');
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    public String getDocumentation(String indent)
    {
        return getDocumentation(indent, 64);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int)
     */
    public String getDocumentation(String indent, int lineLength)
    {
        return getDocumentation(indent, lineLength, true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int, boolean)
     * 
     * TODO: the lineLength does not work yet
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        if (StringUtils.isEmpty(indent)) {
            indent = "";
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
            String newLine = "\n";
            String startParaTag = (htmlStyle) ? "<p>" : "";
            String endParaTag = (htmlStyle) ? "</p>" : "";
            Collection paragraphs =
                new HTMLAnalyzer().htmlToParagraphs(documentation.toString());
            if (paragraphs != null && !paragraphs.isEmpty()) {
                documentation = new StringBuffer();
                for (Iterator paragraphIt = paragraphs.iterator(); paragraphIt.hasNext();) {
                    HTMLParagraph paragraph = (HTMLParagraph)paragraphIt.next();
                    documentation.append(indent + startParaTag + newLine);
                    Collection lines = paragraph.getLines();
                    if (lines != null && !lines.isEmpty()) {
                        Iterator lineIt = lines.iterator();
                        while (lineIt.hasNext()) {
                            documentation.append(indent + lineIt.next() + newLine);
                        }
                    }
                    documentation.append(indent + endParaTag);
                    if (paragraphIt.hasNext()) {
                        documentation.append(newLine);
                    }
                }
            } else {
                documentation.append(indent);
            }
        } catch (Throwable th) {
            final String errMsg = 
                "Error performing ModelElementFacadeImpl.getDocumentation";
            logger.error(errMsg, th);
        }
        return documentation.toString();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementFacade#getName()
     */
    public String getName()
    {
        return metaObject.getName();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementFacade#getMetaObject()
     */
    public ModelElement getMetaObject()
    {
        return metaObject;
    }
    
    /**
     * Language specific mappings property reference.
     */
    private static final String LANGUAGE_MAPPINGS_URI = "languageMappingsUri";
    
    /**
     * Allows the MetaFacadeFactory to populate 
     * the language mappings for this model element.
     * 
     * @param mappingsUri the URI of the language mappings resource.
     */
    public void setLanguageMappingsUri(String mappingsUri) {
        try {
            this.registerConfiguredProperty(
            	LANGUAGE_MAPPINGS_URI, 
                Mappings.getInstance(mappingsUri));
        } catch (Throwable th) {
            String errMsg = "Error setting '" 
                + LANGUAGE_MAPPINGS_URI + "' --> '" 
                + mappingsUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    public Mappings getLanguageMappings() {
        return (Mappings)this.getConfiguredProperty(LANGUAGE_MAPPINGS_URI);
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementFacade#handleGetPackage()
     */
    protected Object handleGetPackage()
    {
        return metaObject.getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetRootPackage()
     */
    protected Object handleGetRootPackage() 
    {
        Collection rootPackages = 
            ((UmlPackage)MetafacadeFactory.getInstance().getModel().getModel())
                .getModelManagement().refAllPackages();
        return (ModelElement) rootPackages.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetDependencies()
     */
    protected java.util.Collection handleGetDependencies()
	{
    	return new FilteredCollection(metaObject.getClientDependency())
		{
    		protected boolean accept(Object object)
			{
    			return (object instanceof Dependency)
                    && !(object instanceof Abstraction);
			}
		};
	}
    
    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetStereotypes()
     */
    protected java.util.Collection handleGetStereotypes() {
    	return this.metaObject.getStereotype();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetNameSpace()
     */
    protected Object handleGetNameSpace()
    {
        return metaObject.getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetModel()
     */
    protected Object handleGetModel()
    {
        return MetafacadeFactory.getInstance().getModel().getModel();
    }
}
