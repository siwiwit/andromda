package org.andromda.cartridges.meta;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.metadecorators.uml14.DecoratorBase;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;
import org.andromda.core.simpleuml.SimpleOOHelper;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;

/**
 * Script helper for the metaclass decorator cartridge.
 */
public class MetaScriptHelper extends SimpleOOHelper
{
    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is
     * connected to cl via a dependency.
     * 
     * @param cl the source classifier
     * @return a decorator for the metaclass object
     */
    public DecoratorBase getMetaclass(GeneralizableElement cl)
    {
        for (Iterator iter = cl.getClientDependency().iterator();
            iter.hasNext();
            )
        {
            Object element = iter.next();
            if ((element instanceof Dependency)
                && !(element instanceof Abstraction))
            {
                Dependency dep = (Dependency) element;
                ModelElement supplier =
                    (ModelElement) dep.getSupplier().iterator().next();
                Collection stereotypes = supplier.getStereotype();
                if (stereotypes != null)
                {
                    String stereotypeName =
                        ((Stereotype) stereotypes.iterator().next())
                            .getName();
                    if (stereotypeName.equals("metaclass"))
                    {
                        return DecoratorFactory
                            .getInstance()
                            .createDecoratorObject(
                            supplier);
                    }
                }
            }
        }
        GeneralizableElement superclass = getGeneralization(cl);
        return (superclass != null) ? getMetaclass(superclass) : null;
    }


    /**
     * Method to help with debugging the templates.
     * 
     * @param o an Object to print to stdout
     */
    public void println(Object o)
    {
        System.out.println(o.toString());
    }
}
