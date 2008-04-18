package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.IncludeFacade.
 *
 * @see org.andromda.metafacades.uml.IncludeFacade
 */
public class IncludeFacadeLogicImpl
    extends IncludeFacadeLogic
{

    public IncludeFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.Include metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getAddition()
     */
    protected java.lang.Object handleGetAddition()
    {
        return metaObject.getAddition();
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getBase()
     */
    protected java.lang.Object handleGetBase()
    {
    		return metaObject.getBase();
    }

}