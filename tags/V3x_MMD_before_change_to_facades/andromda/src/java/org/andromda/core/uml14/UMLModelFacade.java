package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.jmi.reflect.RefPackage;

import org.andromda.core.common.ModelFacade;
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
     * @see org.andromda.core.common.ModelFacade#getModelElements()
     */
    public Collection getModelElements()
    {
        return model.getCore().getModelElement().refAllOfType();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getName(java.lang.Object)
     */
    public String getName(Object modelElement)
    {
        return ((ModelElement)modelElement).getName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(Object modelElement)
    {
        ModelElement m = (ModelElement) modelElement;
        String packageName = "";

        for (ModelElement namespace = m.getNamespace();
            (namespace instanceof
            org.omg.uml.modelmanagement.UmlPackage)
             && !
            (namespace instanceof Model);
            namespace = namespace.getNamespace())
        {
            packageName = "".equals(packageName) ?
                namespace.getName()
                 : namespace.getName() + "." + packageName;
        }

        return packageName;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
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

}
