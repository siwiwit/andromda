package org.andromda.metafacades.uml2;

import org.andromda.metafacades.uml.GeneralizableElementFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.GeneralizableElementFacade.
 *
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 */
public class GeneralizableElementFacadeLogicImpl
    extends GeneralizableElementFacadeLogic
{

    public GeneralizableElementFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationList()
     */
    protected java.lang.String handleGetGeneralizationList()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#findTaggedValue(java.lang.String, boolean)
     */
    protected java.lang.Object handleFindTaggedValue(java.lang.String tagName, boolean follow)
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    protected java.lang.Object handleGetGeneralization()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getSpecializations()
     */
    protected java.util.Collection handleGetSpecializations()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    protected java.util.Collection handleGetGeneralizations()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationLinks()
     */
    protected java.util.Collection handleGetGeneralizationLinks()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllSpecializations()
     */
    protected java.util.Collection handleGetAllSpecializations()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()
     */
    protected java.util.Collection handleGetAllGeneralizations()
    {
        // TODO: add your implementation here!
        return null;
    }

    protected Object handleGetGeneralizationRoot()
    {
        GeneralizableElementFacade generalizableElement = this;

        while (generalizableElement.getGeneralization() != null)
        {
            generalizableElement = generalizableElement.getGeneralization();
        }

        return generalizableElement;
    }
}