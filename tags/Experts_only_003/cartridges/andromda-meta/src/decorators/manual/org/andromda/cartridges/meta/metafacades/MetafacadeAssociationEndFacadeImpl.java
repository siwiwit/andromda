package org.andromda.cartridges.meta.metafacades;

import org.andromda.core.mapping.Mappings;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 *
 *
 */
public class MetafacadeAssociationEndFacadeImpl
    extends MetafacadeAssociationEndFacade
{
    // ---------------- constructor -------------------------------

    public MetafacadeAssociationEndFacadeImpl(
        org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class MetafacadeAssociationEndFacade ...

    public java.lang.String getGetterSetterTypeName()
    {
        // if many, then list or collection
        if (isOne2Many() || isMany2Many())
        {
            Mappings lm = getLanguageMappings();
            return isOrdered()
                ? lm.getTo("datatype.List")
                : lm.getTo("datatype.Collection");
        }

        // If single element, then return the type.
        // However, return the interface type, not the
        // implementation class type!
        MetafacadeFacade otherMetafacade =
            (MetafacadeFacade) (getOtherEnd().getType());
        return otherMetafacade.getFullyQualifiedInterfaceName();
    }

    // ------------- relations ------------------

}
