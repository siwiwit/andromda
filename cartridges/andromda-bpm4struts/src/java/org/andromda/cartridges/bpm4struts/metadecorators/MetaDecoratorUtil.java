package org.andromda.cartridges.bpm4struts.metadecorators;

import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

public final class MetaDecoratorUtil
{
    public static UmlPackage getModel(ModelElement modelElement)
    {
        return (UmlPackage)modelElement.refOutermostPackage();
    }
}
