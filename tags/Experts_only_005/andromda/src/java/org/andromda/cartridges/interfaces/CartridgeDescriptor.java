package org.andromda.cartridges.interfaces;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Describes the capabilities of an AndroMDA cartridge.
 * 
 * @author  <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public interface CartridgeDescriptor {
    
    /**
     * Returns the name of this cartridge.
     * @return String the name
     */
    public String getCartridgeName();

    /**
     * Returns the stereotypes which are supported by this cartridge.
     * @return List the stereotypes
     */
    public List getSupportedStereotypes();

    /**
     * Returns the property values which are set for this cartridge. Example:
     * @andromda.persistence="ejb".
     * 
     * @return List the properties
     */
    public Map getProperties();
    
    /**
     * Returns the list of templates configured in this cartridge.
     * 
     * @return List the template list
     * @see TemplateConfiguration
     */
    public List getTemplateConfigurations();

    /**
     * Returns the list of macro libraries with commonly used
     * scripting engine macros.
     * 
     * @return List the list of macros
     */
    public List getMacroLibraries();
    
    /**
     * Returns the Map of template objects made available to
     * the templates.  (i.e. stringUtils of type org.apache.commons.lang.StringUtils
     * can be defined in the cartridge as a template object and made
     * available to the template at processing time).
     * @return the Map of template objects keyed by name.
     */
    public Map getTemplateObjects();
        
    /**
     * Gets the URL where this descriptor data came from.
     * 
     * @return URL
     */
    public URL getDefinitionURL();

    /**
     * Sets the URL where this descriptor data came from.
     * 
     * @param url
     */
    public void setDefinitionURL(URL url);
    
    /**
     * Returns the cartridge class name. This is used by cartridges that have an
     * own main class (other than DefaultAndroMDACartridge).
     * 
     * @return String
     */
    public String getCartridgeClassName();
}
