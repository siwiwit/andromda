package org.andromda.cartridges.interfaces;

import org.andromda.core.common.CodeGenerationContext;

/**
 * Interface between an AndroMDA code generator cartridge
 * and the generator's core.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public interface IAndroMDACartridge
{

    /**
     * Returns the descriptor data of this particular cartridge.
     * 
     * @return ICartridgeDescriptor
     */
    public ICartridgeDescriptor getDescriptor();

    /**
     * Sets the descriptor data of this particular cartridge. Used by cartridge
     * manager.
     * 
     * @param d the new cartridge descriptor
     * 
     */
    public void setDescriptor(ICartridgeDescriptor d);

    /**
     * Generates code for one model element.
     * 
     * @param context the code generation context
     * @param modelElement the model element to generate code for
     * @param stereotypeName the name of the stereotype that selected the model
     * element
     * @throws CartridgeException if something goes wrong
     */
    public void processModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
        throws CartridgeException;
}
