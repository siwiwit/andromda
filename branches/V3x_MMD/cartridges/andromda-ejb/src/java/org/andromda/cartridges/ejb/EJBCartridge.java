package org.andromda.cartridges.ejb;

import java.util.Properties;

import org.andromda.cartridges.ejb.metadecorators.uml14.EJBEntityDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBFinderMethodDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBPrimaryKeyDecoratorImpl;
import org.andromda.cartridges.ejb.metadecorators.uml14.EJBAssociationEndDecoratorImpl;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;

/**
 * Overrides the default androMDA cartridge in order
 * to register its decorators.
 */
public class EJBCartridge extends DefaultAndroMDACartridge {

	public void init(Properties velocityProperties) throws Exception {
		super.init(velocityProperties);
		
		DecoratorFactory df = DecoratorFactory.getInstance();
		String oldNamespace = df.getActiveNamespace();
		df.setActiveNamespace (getDescriptor().getCartridgeName());

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
		

		df.setActiveNamespace (oldNamespace);
	}
}
