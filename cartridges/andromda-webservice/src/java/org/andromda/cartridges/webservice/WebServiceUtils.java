package org.andromda.cartridges.webservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ServiceFacade;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

/**
 * Contains utilities used within the WebService cartridge.
 * 
 * @author Chad Brandon
 */
public class WebServiceUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     * 
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        final Collection allRoles = new HashSet();
        CollectionUtils.forAllDo(services,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object != null && ServiceFacade.class.isAssignableFrom(object.getClass()))
                    {
                        allRoles.addAll(((ServiceFacade)object).getAllRoles());
                    }                    
                }
            });
        return allRoles;
    }
}
