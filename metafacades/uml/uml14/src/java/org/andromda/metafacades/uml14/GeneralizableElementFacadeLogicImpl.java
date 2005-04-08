package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.omg.uml.foundation.core.Generalization;

/**
 * MetafacadeLogic implementation.
 * 
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 */
public class GeneralizableElementFacadeLogicImpl
    extends GeneralizableElementFacadeLogic
{
    // ---------------- constructor -------------------------------

    public GeneralizableElementFacadeLogicImpl(
        org.omg.uml.foundation.core.GeneralizableElement metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()()
     */
    public java.util.Collection handleGetAllGeneralizations()
    {
        Collection generalizations = new ArrayList();
        for (GeneralizableElementFacade element = this.getGeneralization(); element != null; element = element
            .getGeneralization())
        {
            generalizations.add(element);
        }
        return generalizations;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    public java.lang.Object handleGetGeneralization()
    {
        Object parent = null;
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations != null)
        {
            Iterator iterator = generalizations.iterator();
            if (iterator.hasNext())
            {
                parent = ((Generalization)iterator.next()).getParent();
            }
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    protected Collection handleGetGeneralizations()
    {
        Collection parents = new HashSet();
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations != null && !generalizations.isEmpty())
        {
            Iterator iterator = generalizations.iterator();
            if (iterator.hasNext())
            {
                parents.add(((Generalization)iterator.next()).getParent());
            }
        }
        return parents;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSpecializations()
     */
    public Collection handleGetSpecializations()
    {
        Collection specializations = new ArrayList(UML14MetafacadeUtils
            .getCorePackage().getAParentSpecialization().getSpecialization(
                this.metaObject));
        CollectionUtils.transform(specializations, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((Generalization)object).getChild();
            }
        });
        return specializations;
    }
}
