package org.andromda.cartridges.interfaces;

import java.io.File;
import java.text.MessageFormat;

/**
 * This class implements the <code>&lt;template&gt;</code> tag
 * in a cartridge descriptor file.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Anthony Mowers
 */
public class TemplateConfiguration
{
    private ICartridgeDescriptor cartridgeDescriptor;

    /**
     * Constructor which is used to build default
     * template configurations when initializing the AndroMDAGenTask.
     * 
     * @param stereotype the name of the stereotype
     * @param sheet name of the style sheet file
     * @param outputPattern the pattern to build the output file name
     * @param outlet the output directory
     * @param overwrite yes/no whether output file should be overwritten
     */
    public TemplateConfiguration(
        ICartridgeDescriptor cartridgeDescriptor,
        String stereotype,
        String sheet,
        String outputPattern,
        String outlet,
        boolean overwrite)
    {
        this.cartridgeDescriptor = cartridgeDescriptor;
        this.stereotype = stereotype;
        this.sheet = sheet;
        this.outputPattern = outputPattern;
        this.outlet = outlet;
        this.overwrite = overwrite;
    }

    /**
     * Sets the class name of object that the
     * template code generation scripts will use
     * to access the object model.  The class must implement
     * the ScriptHelper interface.
     * 
     * <p> This is an optional parameter and if it is not set
     * it defaults to the default transform class or the
     * one which was configured using the 
     * <code>&lt;repository&gt;</code> tag. </p>
     *	
     * <p> By writing ones own transformer class some of the
     * more complicated code generation logic can be moved from
     * the code generation script and into the transformer
     * implementation. </p>
     * 
     * @see org.andromda.core.common.ScriptHelper
     * 
     * @param scriptHelperClassName
     */
    public void setTransformClassname(String scriptHelperClassName)
        throws ClassNotFoundException
    {
        transformClass = Class.forName(scriptHelperClassName);
    }

    /**
     * Returns the class of the transform object
     * that will be used to the code generation templates
     * to access the object model.
     * 
     * @return Class	
     */
    public Class getTransformClass()
    {
        return transformClass;
    }

    /**
    * Tells us the stereotype in the UML model that
    * should drive code generation with this template.
    * @param stereotype the name of the stereotype
    */
    public void setStereotype(String stereotype)
    {
        this.stereotype = stereotype;
    }

    /**
     * Tells us the stereotype in the UML model that
     * should drive code generation with this template.
     * @return String the name of the stereotype
     */
    public String getStereotype()
    {
        return stereotype;
    }

    /**
     * Tells us which Velocity stylesheet to use as a template.
     * @param sheet points to the script
     */
    public void setSheet(String sheet)
    {
        this.sheet = sheet;
    }

    /**
     * Tells us which Velocity stylesheet to use as a template.
     * @return File points to the script
     */
    public String getSheet()
    {
        return sheet;
    }

    /**
     * Sets the pattern that is used to build the
     * name of the output file.
     * @param outputPattern the pattern in java.text.MessageFormat syntax
     */
    public void setOutputPattern(String outputPattern)
    {
        this.outputPattern = outputPattern;
    }

    /**
     * Gets the pattern that is used to build the
     * name of the output file.
     * @return String the pattern in java.text.MessageFormat syntax
     */
    public String getOutputPattern()
    {
        return outputPattern;
    }

    /**
     * Sets the outlet where the output file that is generated from this
     * template should be placed,
     * @param outlet points to the outlet
     */
    public void setOutlet(String outlet)
    {
        this.outlet = outlet;
    }

    /**
     * Gets the outlet where the output file that is generated from this
     * template should be placed.
     * @return String the outlet alias name
     */
    public String getOutlet()
    {
        return outlet;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @param overwrite overwrite the file yes/no
     */
    public void setOverwrite(boolean overwrite)
    {
        this.overwrite = overwrite;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @return boolean
     */
    public boolean isOverwrite()
    {
        return overwrite;
    }

    /**
     * Returns the fully qualified output file, that means:
     * <ul>
     * <li>the output pattern has been translated</li>
     * <li>the output dir name has been prepended</li>
     * </ul>
     * 
     * @param inputClassName name of the class from the UML model
     * @param inputPackageName name of the package from the UML model 
     *                         in which the class is contained
     * @param oldict the dictionary where outlet names can be resolved to
     *               physical  directories
     * @return File absolute file
     */
    public File getFullyQualifiedOutputFile(
        String inputClassName,
        String inputPackageName,
        OutletDictionary oldict)
    {
        int dotIndex = sheet.indexOf(".");
        String sheetBaseName = sheet.substring(0, dotIndex);

        Object[] arguments =
            {
                inputPackageName.replace('.', File.separatorChar),
                inputClassName,
                sheetBaseName,
                getStereotype()};

        String outputFileName =
            MessageFormat.format(outputPattern, arguments);

        File physDir = oldict.lookupOutlet(
                cartridgeDescriptor.getCartridgeName(),
                outlet);
                
        return physDir == null ? null : new File(physDir, outputFileName);
    }

    /**
     * Just for debugging.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "TemplateConfiguration: "
            + stereotype
            + " "
            + sheet
            + " "
            + outputPattern
            + " "
            + outlet
            + " "
            + overwrite;
    }

    private String stereotype;
    private String sheet;
    private String outputPattern;
    private String outlet;
    private boolean overwrite;
    private Class transformClass;

}
