package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.behavioralelements.statemachines.State;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class StateFacadeLogicImpl
    extends StateFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public StateFacadeLogicImpl(State metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.StateFacadeLogic#handleGetDeferrableEvents()
     */
    protected Collection handleGetDeferrableEvents()
    {
        return metaObject.getDeferrableEvent();
    }
}