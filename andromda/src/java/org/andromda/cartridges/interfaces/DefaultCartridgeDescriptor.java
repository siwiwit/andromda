package org.andromda.cartridges.interfaces;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A default implementation of the CartridgeDescriptor interface.
 * 
 * @see org.andromda.cartridges.interfaces.CartridgeDescriptor
 * @see org.andromda.common.XmlObjectFactory
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class DefaultCartridgeDescriptor implements CartridgeDescriptor
{
    private static Logger logger = Logger.getLogger(DefaultCartridgeDescriptor.class);
    
    private String cartridgeName;
    private Map properties = new HashMap();
    private List templates = new ArrayList();
    private Map templateObjects = new HashMap();
    private URL definitionURL;
    private String cartridgeClassName = null;
    private TemplateEngine templateEngine = null;
    
    /**
     * Returns a new configured instance of this DefaultCartridgeDescriptor as 
     * a CartridgeDescriptor configured from the CartridgeDescriptor configuration URI string.
     * 
     * @param cartridgeDescriptorUri the URI to the XML type cartridgeDescriptor configuration file.
     * @return CartridgeDescriptor the configured CartridgeDescriptor instance.
     * @throws MalformedURLException when the cartridgeDescriptorUri is invalid (not a valid URL).
     */
    public static CartridgeDescriptor getInstance(String cartridgeDescriptorUri) throws MalformedURLException 
    {
        final String methodName = "DefaultCartridgeDescriptor.getInstance";
        cartridgeDescriptorUri = StringUtils.trimToEmpty(cartridgeDescriptorUri);
        ExceptionUtils.checkEmpty(methodName, "cartridgeDescriptorUri", cartridgeDescriptorUri);
        CartridgeDescriptor cartridgeDescriptor = getInstance(new URL(cartridgeDescriptorUri));
        return cartridgeDescriptor;
    }
    
    /**
     * Returns a new configured instance of this DefaultCartridgeDescriptor as
     *  a CartridgeDescriptor configured from the cartridgeDescriptor configuration URI.
     * 
     * @param cartridgeDescriptorUri the URI to the XML cartridgeDescriptor configuration file.
     * @return CartridgeDescriptor the configured CartridgeDescriptor instance.
     */
    public static CartridgeDescriptor getInstance(URL cartridgeDescriptorUri) 
    {
        final String methodName = "DefaultCartridgeDescriptor.getInstance";
        ExceptionUtils.checkNull(methodName, "cartridgeDescriptorUri", cartridgeDescriptorUri);
        DefaultCartridgeDescriptor cartridgeDescriptor = 
            (DefaultCartridgeDescriptor)XmlObjectFactory.getInstance(
                CartridgeDescriptor.class).getObject(cartridgeDescriptorUri);
        cartridgeDescriptor.definitionURL = cartridgeDescriptorUri;
        return cartridgeDescriptor;
    }


    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getCartridgeName()
     * @return String
     */
    public String getCartridgeName()
    {
        return cartridgeName;
    }

    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getProperties()
     */
    public Map getProperties()
    {
        return properties;
    }

    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getTemplateConfigurations()
     */
    public List getTemplateConfigurations()
    {
        return templates;
    }

    /**
     * Adds a property to the list of properties of this cartridge. Properties
     * may be used to designate implementations for architectural aspects.
     * 
     * @param propertyName the name of the property
     * @param propertyValue the value of the property
     */
    public void addProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
    }
    
    /**
     * Sets the name of the cartridge.
     * @param cartridgeName The cartridgeName to set
     */
    public void setCartridgeName(String cartridgeName)
    {
        this.cartridgeName = cartridgeName;
    }
    
    /**
     * Adds an item to the list of defined template configurations.
     * 
     * @param templateConfiguration the new configuration to add
     */
    public void addTemplateConfiguration(TemplateConfiguration templateConfiguration)
    {
        templates.add(templateConfiguration);
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getDefinitionURL()
     */
    public URL getDefinitionURL()
    {
        return this.definitionURL;
    }
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#setDefinitionURL(java.net.URL)
     */
    public void setDefinitionURL(URL url)
    {
        this.definitionURL = url;
    }

    /**
     * Returns the cartridgeClassName.
     * @return String
     */
    public String getCartridgeClassName()
    {
        return cartridgeClassName;
    }

    /**
     * Sets the cartridgeClassName.
     * @param cartridgeClassName The cartridgeClassName to set
     */
    public void setCartridgeClassName(String cartridgeClassName)
    {
        this.cartridgeClassName = cartridgeClassName;
    }

    /**
     * Adds an item to the list of defined macro libraries.
     * 
     * @param libraryName String the name of the library
     */
    public void addMacroLibrary(String libraryName)
    {
        this.templateEngine.addMacroLibrary(libraryName);
    }
    
    /**
     * Adds the <code>templateObject</code> and makes
     * it available to the template.
     * 
     * @param templateObject
     */
    public void addTemplateObject(TemplateObject templateObject) 
    {
        final String methodName = "DefaultCartridgeDescriptor.addTemplateObject";
        ExceptionUtils.checkNull(methodName, "templateObject", templateObject);
        templateObject.setResource(this.getDefinitionURL());
        templateObject.setNamespace(this.getCartridgeName());
        this.templateObjects.put(
            templateObject.getName(), 
            templateObject.getTemplateObject());
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getTemplateObjects()
     */
    public Map getTemplateObjects() 
    {
        return this.templateObjects;
    }
    
    /**
     * This currently contains the default template engine,
     * but templateengines should be moved into their own
     * module and then a META-INF/service properties file should be used instead
     * of hard coding this value.  Then depending on what is on
     * the classpath can be set as the default template engine
     */
    private static final String DEFAULT_TEMPLATE_ENGINE =
        "org.andromda.core.templateengine.VelocityTemplateEngine";

    /**
     * Sets the template engine class for this cartridge.
     * 
     * @param templateEngineClassName
     */
    public void setTemplateEngineClass(String templateEngineClassName) {
        final String methodName = "DefaultCartridgeDescriptor.setTemplateEngine";
        try {
            this.templateEngine = 
                (TemplateEngine)
                    ClassUtils.loadClass(
                        templateEngineClassName).newInstance();
        } catch (Throwable th) {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new CartridgeException(errMsg, th);
        }
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getTemplateEngine()
     */
    public TemplateEngine getTemplateEngine() {
        final String methodName = "DefaultCartridgeDescriptor.getTemplateEngine";
        if (this.templateEngine == null) {
            try {
                this.templateEngine = 
                    (TemplateEngine)
                        ClassUtils.loadClass(
                            DEFAULT_TEMPLATE_ENGINE).newInstance();
            } catch (Throwable th) {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new CartridgeException(errMsg, th);
            }
        }
        return this.templateEngine;
    }
    
}
