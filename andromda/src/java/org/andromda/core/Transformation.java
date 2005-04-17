package org.andromda.core;

import java.net.URL;

/**
 * Stores the information about a transformation. Transformations are applied to
 * the model(s) before actual model processing occurs.
 * 
 * @author Chad Brandon
 */
public class Transformation
{
    /**
     * Constructs a new instance of this Transformation.
     * 
     * @param url the URL to the transformation to apply.
     */
    public Transformation(URL url)
    {
        this.url = url;
    }

    /**
     * The URL location of the transformation.
     */
    private URL url;

    /**
     * The URL of the model.
     * 
     * @return Returns the url.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * Stores the optional output location.
     */
    private String outputLocation;

    /**
     * Sets the location to which the result of this transformation should be
     * written. This is optional, if this is unspecified then the result is not
     * written but just passed in memory to the processor.
     * 
     * @param outputLocation the location of the output to be written.
     */
    public void setOuputLocation(String outputLocation)
    {
        this.outputLocation = outputLocation;
    }

    /**
     * Gets the location to which the output of the transformation result will
     * be written.
     * 
     * @return the output location.
     */
    public String getOutputLocation()
    {
        return this.outputLocation;
    }
}
