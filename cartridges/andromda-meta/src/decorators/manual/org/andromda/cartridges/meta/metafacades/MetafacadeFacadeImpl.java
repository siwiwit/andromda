package org.andromda.cartridges.meta.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DependencyDecorator;
import org.andromda.core.metadecorators.uml14.ModelElementDecorator;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class MetafacadeFacadeImpl extends MetafacadeFacade
{
    // ---------------- constructor -------------------------------

    public MetafacadeFacadeImpl(
        org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class MetafacadeFacade ...

    public java.lang.String getImplSuperclass()
    {
        String taggedValue = null;
        ModelElementDecorator med = this;
        do
        {
            // try to find this tagged value
            taggedValue =
                med.findTaggedValue(
                    MetaProfile.TAGGEDVALUE_METAFACADE_BASECLASS);
            if (taggedValue != null)
            {
                // return if found
                return taggedValue;
            }

            // if not found, walk up in the package hierarchy
            med = med.getPackage();
        }
        while (med != null);

        return getLanguageMappings().getTo("datatype.Object");
    }

    // ------------- relations ------------------

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt;&gt; that is
     * connected to the metaobject via a dependency. If no metaclass is
     * directly connected, the method walks up the supertype hierarchy.
     *
     * @return the metaclass object
     */
    public org.omg.uml.foundation.core.ModelElement handleGetMetaclass()
    {
        // delegate to recursive method
        return getMetaclass(this);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is
     * connected to cl via a dependency.
     * 
     * @param cl the source classifier
     * @return the metaclass object
     */
    private org.omg.uml.foundation.core.ModelElement getMetaclass(
        ClassifierDecorator cl)
    {
        for (Iterator iter = cl.getDependencies().iterator(); iter.hasNext();)
        {
            DependencyDecorator dep = (DependencyDecorator) iter.next();
            ClassifierDecorator target = dep.getTargetType();
            Collection stereotypes = target.getStereotypeNames();
            if (stereotypes != null)
            {
                String stereotypeName =
                    (String) stereotypes.iterator().next();
                if (stereotypeName
                    .equals(MetaProfile.STEREOTYPE_METACLASS))
                {
                    return target.getMetaObject();
                }
            }
        }

        ClassifierDecorator superclass = cl.getSuperclass();
        return (superclass != null) ? getMetaclass(superclass) : null;
    }

// ------------------------------------------------------------

}
