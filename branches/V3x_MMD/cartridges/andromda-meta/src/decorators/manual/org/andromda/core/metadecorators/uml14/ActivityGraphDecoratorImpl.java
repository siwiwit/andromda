package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.behavioralelements.usecases.UseCase;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.activitygraphs.ActivityGraph
 *
 *
 */
public class ActivityGraphDecoratorImpl extends ActivityGraphDecorator
{
    // ---------------- constructor -------------------------------

    public ActivityGraphDecoratorImpl (org.omg.uml.behavioralelements.activitygraphs.ActivityGraph metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ActivityGraphDecorator ...

    // ------------- relations ------------------

   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetUseCaseContainer()
    {
        ModelElement context = getContext();
        return (context instanceof UseCase) ? context : null;
    }

    // ------------------------------------------------------------

}
