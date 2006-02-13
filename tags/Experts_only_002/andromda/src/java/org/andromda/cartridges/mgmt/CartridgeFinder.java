package org.andromda.cartridges.mgmt;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.interfaces.CartridgeXmlParser;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.cartridges.interfaces.IAndroMDACartridge;
import org.andromda.cartridges.interfaces.ICartridgeDescriptor;
import org.andromda.core.common.ResourceFinder;
import org.apache.log4j.Logger;

/**
 * Finds AndroMDA cartridges on the classpath.
 *
 * @author    <a href="mailto:aslak.hellesoy@netcom.no">Aslak Helles�y</a>
 * @author    <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author    Chad Brandon
 * @since     April 1, 2003
 * @version   $Revision: 1.3.2.1 $
 */
public class CartridgeFinder
{
	private static Logger logger = Logger.getLogger(CartridgeFinder.class);
	
    private final static String resourceName =
        "META-INF/andromda-cartridge.xml";

    private static List cartridges = null;

    /**
     * Returns a List of ICartridgeDescriptor objects
     *
     * @return a <code>List<code> of cartriges
     */
    public static List findCartridges() throws IOException
    {
        if (cartridges == null)
        {
            cartridges = new ArrayList();

            CartridgeXmlParser parser = new CartridgeXmlParser();

            URL cartridgeUris[]= ResourceFinder.findResources(resourceName);

            for (int ctr = 0; ctr < cartridgeUris.length; ctr++)
            {
            	URL cartridgeUri = cartridgeUris[ctr];
                ICartridgeDescriptor cDescriptor =
                    parser.parse(cartridgeUri.openStream());

                if (cDescriptor != null)
                {
                    cDescriptor.setDefinitionURL(cartridgeUri);
                    IAndroMDACartridge cartridge =
                        instantiateCartridge(cDescriptor);
                    cartridges.add(cartridge);
                }
                else
                {
					logger.error("Could not parse cartridge descriptor --> '" + cartridgeUri + "'");
                }
            }
        }

        // some debugging output
        for (Iterator iter = cartridges.iterator(); iter.hasNext();)
        {
            IAndroMDACartridge element =
                (IAndroMDACartridge) iter.next();
			logger.info("cartridge found --> '" + element.getDescriptor().getCartridgeName() + "'");

        }
        return cartridges;
    }
    
    /**
     * Instantiates a cartridge from a descriptor.
     * @param cDescriptor the cartridge descriptor
     * @return IAndroMDACartridge
     */
    private static IAndroMDACartridge instantiateCartridge(ICartridgeDescriptor cd)
	{
    	String className = cd.getCartridgeClassName();
    	if (className == null)
    		className = DefaultAndroMDACartridge.class.getName();
    	try
		{
    		Class cl = Class.forName(className);
    		IAndroMDACartridge ac = (IAndroMDACartridge) cl.newInstance();
    		ac.setDescriptor(cd);
    		return ac;
    	}
    	catch (ClassNotFoundException e)
		{
    		logger.error(e);
    	}
    	catch (InstantiationException e)
		{
    		logger.error(e);
    	}
    	catch (IllegalAccessException e)
		{
    		logger.error(e);
    	}
    	return null;
    }

    public static void resetFoundCartridges()
    {
        cartridges = null;
    }
}