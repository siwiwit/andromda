package org.andromda.cartridges.bpm4struts;

import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;

import java.util.Properties;

public class Bpm4StrutsCartridge extends DefaultAndroMDACartridge {

    public void init(Properties velocityProperties) throws Exception {
        super.init(velocityProperties);

        DecoratorFactory df = DecoratorFactory.getInstance();
        String oldNamespace = df.getActiveNamespace();
        df.setActiveNamespace (getDescriptor().getCartridgeName());
/*
        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Operation$Impl",
            EJBProfile.STEREOTYPE_FINDER_METHOD,
            EJBFinderMethodDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            EJBProfile.STEREOTYPE_PRIMARY_KEY,
            EJBPrimaryKeyDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Classifier$Impl",
            EJBProfile.STEREOTYPE_ENTITY,
            EJBEntityDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.AssociationEnd$Impl",
            null,
            EJBAssociationEndDecoratorImpl.class.getName());

*/
        df.setActiveNamespace (oldNamespace);
    }
}