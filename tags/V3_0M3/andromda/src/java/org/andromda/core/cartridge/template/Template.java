package org.andromda.core.cartridge.template;

import java.io.File;

import org.andromda.core.cartridge.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class implements the <code>&lt;template&gt;</code> tag in a cartridge
 * descriptor file.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Anthony Mowers
 * @author Chad Brandon
 */
public class Template
    extends Resource
{
    /**
     * The default constructor used by the XmlObjectFactory to instantiate the
     * template configuration.
     */
    public Template()
    {
        this.supportedModelElements = new ModelElements();
    }

    /**
     * Tells us whether output files should be generated if this template does
     * not produce any output.
     * 
     * @param generateEmptyFiles generate files for empty output yes/no
     */
    public void setGenerateEmptyFiles(boolean generateEmptyFiles)
    {
        this.generateEmptyFiles = generateEmptyFiles;
    }

    /**
     * Tells us whether output files are generated by this template if the
     * template produces empty output.
     * 
     * @return boolean
     */
    public boolean isGenerateEmptyFiles()
    {
        return generateEmptyFiles;
    }

    /**
     * Returns the fully qualified output file, this means:
     * <ul>
     * <li>the output pattern has been translated</li>
     * <li>the output dir name has been prepended</li>
     * </ul>
     * 
     * @param modelElementName name of the element from the model
     * @param packageName name of the package from the model in which the class
     *        is contained
     * @param directory the directory as a File.
     * @return File absolute directory.
     */
    public File getOutputLocation(
        String modelElementName,
        String packageName,
        File directory)
    {
        File file = null;
        // if singleFileOutput is set to true, then
        // just use the output pattern as the file to
        // output to, otherwise we replace using message format.
        if (this.isOutputToSingleFile())
        {
            file = super.getOutputLocation(new String[]
            {
                this.getOutputPattern(),
            }, directory);
        }
        else
        {
            file = super.getOutputLocation(new String[]
            {
                StringUtils.trimToEmpty(packageName).replace(
                    '.',
                    File.separatorChar),
                modelElementName,
            }, directory);
        }
        return file;
    }

    /**
     * Tells us the model elements that are supported by this template (i.e.
     * will be processed by this template)
     * 
     * @return ModelElements all the model elements that should be processed by
     *         thsi template
     * @see org.andromda.core.cartridge.template.ModelElements
     */
    public ModelElements getSupportedModeElements()
    {
        final String methodName = "Template.getModelElements";
        if (this.supportedModelElements == null)
        {
            throw new TemplateException(methodName
                + " - supportedModelElements is null!");
        }
        return this.supportedModelElements;
    }

    /**
     * Sets the model elements that are suported by this template.
     * 
     * @param supportedModelElements the ModelElements instance.
     * @see org.andromda.core.cartridge.template.ModelElements
     */
    public void setSupportedModelElements(ModelElements supportedModelElements)
    {
        this.supportedModelElements = supportedModelElements;
    }

    /**
     * If output to single file is <code>true</code> then all model elements
     * found by the processor (i.e. all those having matching modelElements)
     * will be output to one file.
     * 
     * @return Returns the outputToSingleFile.
     */
    public boolean isOutputToSingleFile()
    {
        return outputToSingleFile;
    }

    /**
     * @param outputToSingleFile The outputToSingleFile to set.
     */
    public void setOutputToSingleFile(boolean outputToSingleFile)
    {
        this.outputToSingleFile = outputToSingleFile;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    private ModelElements supportedModelElements = null;
    private boolean generateEmptyFiles;
    private boolean outputToSingleFile = false;
}