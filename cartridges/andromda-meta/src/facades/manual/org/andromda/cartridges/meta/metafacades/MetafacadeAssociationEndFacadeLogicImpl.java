package org.andromda.cartridges.meta.metafacades;

import org.andromda.core.mapping.Mappings;


/**
 * <p>
 *  Facade for use in the andromda-meta cartridge. It hides an
 *  association between two classifiers that each represent a
 * </p>
 * <p>
 *  > object.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class MetafacadeAssociationEndFacadeLogicImpl
       extends MetafacadeAssociationEndFacadeLogic
       implements org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public MetafacadeAssociationEndFacadeLogicImpl (org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class MetafacadeAssociationEndFacade ...

    public java.lang.String getGetterSetterTypeName()
    {
        // if many, then list or collection
        if (isOne2Many() || isMany2Many())
        {
            Mappings lm = getLanguageMappings();
            return isOrdered()
                ? lm.getTo("datatype.List")
                : lm.getTo("datatype.Collection");
        }

        String typeName = null;
        // If single element, then return the type.
        // However, return the interface type, not the
        // implementation class type!
        MetafacadeFacade otherMetafacade =
            (MetafacadeFacade) getOtherEnd().getType();
        if (otherMetafacade != null) {
            typeName = otherMetafacade.getFullyQualifiedInterfaceName();
        }
        return typeName;
    }
    
    /**
     * Language specific mappings property reference.
     */
    private static final String LANGUAGE_MAPPINGS = "languageMappings";
    
    /**
     * Allows the MetaFacadeFactory to populate 
     * the language mappings for this model element.
     * 
     * @param mappingUri the URI of the language mappings resource.
     */
    public void setLanguageMappings(String mappingUri) {
        try {
            Mappings mappings = Mappings.getInstance(mappingUri);
            // register the mappings with the component container.
            this.registerConfiguredProperty(LANGUAGE_MAPPINGS, mappings);
        } catch (Throwable th) {
            String errMsg = "Error setting '" 
                + LANGUAGE_MAPPINGS + "' --> '" 
                + mappingUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }
    
    /**
     * Gets the language mappings that have been
     * set for this model elemnt.
     * @return the Mappings instance.
     */
    protected Mappings getLanguageMappings() {
        return (Mappings)this.getConfiguredProperty(LANGUAGE_MAPPINGS);
    }

    // ------------- relations ------------------

}
