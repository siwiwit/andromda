package org.andromda.cartridges.ejb;

import java.util.Properties;

import org.andromda.cartridges.ejb.metadecorators.uml14.EJBAssociationEndDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBFinderMethodDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBPrimaryKeyDecoratorImpl;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;

/**
 * Overrides the default androMDA cartridge in order
 * to register its decorators.
 */
public class EJBCartridge extends DefaultAndroMDACartridge {

	public void init(Properties velocityProperties) throws Exception {
		super.init(velocityProperties);
		
		DecoratorFactory decoratorFactory = DecoratorFactory.getInstance();
		String oldNamespace = decoratorFactory.getActiveNamespace();
		decoratorFactory.setActiveNamespace (getDescriptor().getCartridgeName());

		decoratorFactory.registerDecoratorClass(
			"org.omg.uml.foundation.core.Operation$Impl",
			EJBProfile.STEREOTYPE_FINDER_METHOD,
			EJBFinderMethodDecoratorImpl.class.getName());
		
		decoratorFactory.registerDecoratorClass(
			"org.omg.uml.foundation.core.Attribute$Impl",
			EJBProfile.STEREOTYPE_PRIMARY_KEY,
			EJBPrimaryKeyDecoratorImpl.class.getName());
		
		decoratorFactory.registerDecoratorClass(
			"org.omg.uml.foundation.core.UmlClass$Impl",
			EJBEntityDecoratorImpl.class.getName());

		decoratorFactory.registerDecoratorClass(
			"org.omg.uml.foundation.core.Interface$Impl",
			EJBEntityDecoratorImpl.class.getName());
		
		decoratorFactory.registerDecoratorClass(
			"org.omg.uml.foundation.core.AssociationEnd$Impl",
			EJBAssociationEndDecoratorImpl.class.getName());
		

		decoratorFactory.setActiveNamespace (oldNamespace);
	}
}
