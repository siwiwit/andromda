package org.andromda.modules.xmilink.uml20;

import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLAttributeExportStrategy;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 25.10.2004
 */
public class UML20PropertyExportStrategy
        extends UMLAttributeExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Property20",
                UML20PropertyExportStrategy.class);
    }

}
