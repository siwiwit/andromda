package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.omg.uml.UmlPackage;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ModelFacadeLogicImpl
       extends ModelFacadeLogic
       implements org.andromda.metafacades.uml.ModelFacade
{
    // ---------------- constructor -------------------------------
    
    public ModelFacadeLogicImpl (org.omg.uml.UmlPackage metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelDecorator ...

    // ------------- relations ------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelDecorator#handleGetRootPackage()
     */
    protected Object handleGetRootPackage()
    {
        Collection rootPackages =
            metaObject.getModelManagement().getModel().refAllOfType();
        return rootPackages.iterator().next();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelDecorator#getMetaObject()
     */
    public UmlPackage getMetaObject()
    {
        return metaObject;
    }

    // ------------------------------------------------------------

}
