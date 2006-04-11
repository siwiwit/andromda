package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.core.metafacade.MetafacadeFactory;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.UmlClass;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class PackageFacadeLogicImpl
       extends PackageFacadeLogic
       implements org.andromda.metafacades.uml.PackageFacade
{
    // ---------------- constructor -------------------------------
    
    public PackageFacadeLogicImpl (org.omg.uml.modelmanagement.UmlPackage metaObject)
    {
        super (metaObject);
    }
    /**
     * @see org.andromda.metafacades.uml14.PackageDecorator#handleGetClasses()
     */
    public java.util.Collection handleGetClasses()
    {
        return new FilteredCollection(metaObject.getOwnedElement())
        {
            protected boolean accept(Object object)
            {
                return object instanceof UmlClass;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml14.PackageDecorator#handleGetSubPackages()
     */
    protected Collection handleGetSubPackages()
    {
        return new FilteredCollection(metaObject.getOwnedElement())
        {
            protected boolean accept(Object object)
            {
                return object instanceof UmlPackage;
            }
        };
    }
    
    /**
     * @see org.andromda.metafacades.uml14.PackageDecorator#handleGetModelElements()
     */
    protected Collection handleGetModelElements() {
        return ((UmlPackage)MetafacadeFactory.getInstance().getModel().getModel())
            .getCore()
            .getModelElement()
            .refAllOfType();
    }

    // ------------------------------------------------------------

}