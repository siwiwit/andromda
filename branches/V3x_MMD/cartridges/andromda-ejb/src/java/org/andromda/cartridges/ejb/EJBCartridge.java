package org.andromda.cartridges.ejb;

import java.util.Properties;

import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.ejb.EJBFinderMethodDecoratorImpl;
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
				"FinderMethod",
				EJBFinderMethodDecoratorImpl.class.getName());

		df.setActiveNamespace (oldNamespace);
	}
}
