package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.jmi.reflect.RefPackage;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ModelFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.modelmanagement.Model;

/**
 * Contains a UML model, follows the ModelFacade interface
 * and can therefore be processed by AndroMDA.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class UMLModelFacade implements ModelFacade
{

    private static Logger logger = Logger.getLogger(UMLModelFacade.class);
    
    private UmlPackage model;

    public UMLModelFacade (RefPackage topNode)
    {
        this.model = (UmlPackage)topNode;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getModel()
     */
    public Object getModel()
    {
        return model;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getName(java.lang.Object)
     */
    public String getName(Object modelElement)
    {
        final String methodName = "UMLModelFacade.getName";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName, 
            ModelElementDecorator.class, 
            "modelElement", 
            modelElement.getClass());
       
        return ((ModelElementDecorator)modelElement).getName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(Object modelElement)
    {      
        final String methodName = "UMLModelFacade.getStereotypeNames";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName, 
            ModelElementDecorator.class, 
            "modelElement", 
            modelElement.getClass());
       
        return ((ModelElementDecorator)modelElement).getPackageName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        final String methodName = "UMLModelFacade.getStereotypeNames";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ModelElement m = (ModelElement) modelElement;
        Collection stereoTypeNames = new Vector();

        Collection stereotypes = m.getStereotype();
        for (Iterator i = stereotypes.iterator(); i.hasNext(); )
        {
            ModelElement stereotype = (ModelElement) i.next();
            stereoTypeNames.add(stereotype.getName());
        }

        return stereoTypeNames;
    }

    /**
     * @see edu.duke.dcri.mda.model.ModelFacade#findModelElementsByStereotype(java.lang.String)
     */
    public Collection findModelElementsByStereotype(String stereotype) {
        final String methodName = "UMLModelFacade.findModelElementsByStereotype";
        Collection modelElements = new ArrayList();
        stereotype = StringUtils.trimToEmpty(stereotype);
        if (StringUtils.isNotEmpty(stereotype)) {
            if (this.model != null) {
                Collection underlyingElements = 
                    model.getCore().getModelElement().refAllOfType();
                if (underlyingElements != null || !underlyingElements.isEmpty()) {
                    Iterator elementIt = underlyingElements.iterator();
                    while (elementIt.hasNext()) {
                        ModelElement element = (ModelElement)elementIt.next();
                        Collection stereotypeNames = this.getStereotypeNames(element);
                        if (stereotypeNames != null && stereotypeNames.contains(stereotype)) {
                            modelElements.add(element);
                        }
                    }
                }
                if (logger.isDebugEnabled())
                    logger.debug("completed " + methodName 
                        + " with " + modelElements.size() + " modelElements");
            }
        }
        return modelElements;
    }

}
