package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;
import org.andromda.core.metadecorators.uml14.AttributeDecorator;
import org.andromda.core.metadecorators.uml14.ClassifierDecorator;
import org.andromda.core.metadecorators.uml14.DependencyDecorator;
import org.andromda.core.metadecorators.uml14.ModelElementDecorator;
import org.andromda.core.metadecorators.uml14.OperationDecorator;

/**
 *
 * @since 28.02.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
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

    public java.lang.String getImplSuperclassName()
    {
        String taggedValue =
            findTaggedValueUpstairs(
                MetaProfile.TAGGEDVALUE_METAFACADE_BASECLASS);
        return (taggedValue != null)
            ? taggedValue
            : getLanguageMappings().getTo("datatype.Object");
    }

    /**
     * Finds a tagged value on the current element or on a package
     * in the hierarchy above it.
     * 
     * @param taggedValueName the name of the tagged value
     * @return the value of the tagged value
     */
    private String findTaggedValueUpstairs(String taggedValueName)
    {
        String taggedValue = null;
        ModelElementDecorator med = this;
        do
        {
            // try to find this tagged value
            taggedValue = med.findTaggedValue(taggedValueName);
            if (taggedValue != null)
            {
                // return if found
                return taggedValue;
            }

            // if not found, walk up in the package hierarchy
            med = med.getPackage();
        }
        while (med != null);
        return null; // not found
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
        for (Iterator iter = cl.getDependencies().iterator();
            iter.hasNext();
            )
        {
            DependencyDecorator dep = (DependencyDecorator) iter.next();
            ClassifierDecorator target = dep.getTargetType();
            Collection stereotypes = target.getStereotypeNames();
            if (stereotypes != null && stereotypes.size() > 0)
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

    /* (non-Javadoc)
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getInterfacePackageName()
     */
    public String getInterfacePackageName()
    {
        String taggedValue =
            findTaggedValueUpstairs(
                MetaProfile.TAGGEDVALUE_METAFACADE_INTERFACEPACKAGE);
        // if the tagged value is not set, return the same package
        // as the current package.
        return (taggedValue != null) ? taggedValue : getPackageName();
    }

    /* (non-Javadoc)
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getFullyQualifiedInterfaceName()
     */
    public String getFullyQualifiedInterfaceName()
    {
        return getInterfacePackageName() + "." + getInterfaceName();
    }

    /* (non-Javadoc)
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getInterfaceName()
     */
    public String getInterfaceName()
    {
        return "I" + getName();
    }

    /* (non-Javadoc)
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getMethodDataForPSM(boolean)
     */
    public Collection getMethodDataForPSM(boolean includeSuperclasses)
    {
        HashMap map = new HashMap();
        internalGetMethodDataForPSM(map, this);
        if (includeSuperclasses)
        {
            for (ClassifierDecorator cd = getSuperclass();
                cd != null;
                cd = cd.getSuperclass())
            {
                internalGetMethodDataForPSM(map, (MetafacadeFacade) cd);
            }
        }
        ArrayList result = new ArrayList(map.values());
        Collections.sort(result);
        return result;
    }

    private static void internalGetMethodDataForPSM(HashMap map, MetafacadeFacade cd)
    {
        final String fullyQualifiedInterfaceName =
            cd.getFullyQualifiedInterfaceName();

        // translate UML attributes to getter methods
        for (Iterator iter = cd.getAttributes().iterator();
            iter.hasNext();
            )
        {
            AttributeDecorator att = (AttributeDecorator) iter.next();
            final MethodData md =
                new MethodData(
                    fullyQualifiedInterfaceName,
                    "public",
                    false,
                    att.getType().getFullyQualifiedName(),
                    att.getGetterName(),
                    att.getDocumentation("    "));
            map.put(md.buildCharacteristicKey(), md);
        }

        // translate UML operations to methods
        for (Iterator iter = cd.getOperations().iterator();
            iter.hasNext();
            )
        {
            OperationDecorator op = (OperationDecorator) iter.next();
            final UMLOperationData md =
                new UMLOperationData(fullyQualifiedInterfaceName, op);
            map.put(md.buildCharacteristicKey(), md);
        }

        // translate UML associations to getter methods
        for (Iterator iter = cd.getAssociationEnds().iterator();
            iter.hasNext();
            )
        {
            AssociationEndDecorator ae =
                (AssociationEndDecorator) iter.next();
            AssociationEndDecorator otherEnd = ae.getOtherEnd();
            if (otherEnd.isNavigable())
            {
                final MethodData md =
                    new MethodData(
                        fullyQualifiedInterfaceName,
                        "public",
                        false,
                        ae.getGetterSetterTypeName(),
                        otherEnd.getGetterName(),
                        otherEnd.getDocumentation("    "));
                map.put(md.buildCharacteristicKey(), md);
            }
        }
    }

    // ------------------------------------------------------------

}
