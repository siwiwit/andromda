package org.andromda.metafacades.uml2;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{

    public FrontEndEventLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    protected java.lang.Object handleGetControllerCall()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    protected java.lang.Object handleGetAction()
    {
        // TODO: add your implementation here!
        return null;
    }

    public List handleGetControllerCalls()
    {
        // TODO Auto-generated method stub
        return null;
    }

}