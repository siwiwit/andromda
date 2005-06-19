package org.andromda.metafacades.uml14;

/**
 * Metaclass facade implementation.
 */
public class ActionStateFacadeLogicImpl
        extends ActionStateFacadeLogic
{
    public ActionStateFacadeLogicImpl(org.omg.uml.behavioralelements.activitygraphs.ActionState metaObject,
                                      String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetEntry()
    {
        return metaObject.getEntry();
    }
}
