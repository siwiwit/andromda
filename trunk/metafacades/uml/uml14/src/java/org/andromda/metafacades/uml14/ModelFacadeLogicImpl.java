package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * Metaclass facade implementation.
 */
public class ModelFacadeLogicImpl
        extends ModelFacadeLogic
        implements org.andromda.metafacades.uml.ModelFacade
{
    // ---------------- constructor -------------------------------

    public ModelFacadeLogicImpl(org.omg.uml.UmlPackage metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelDecorator ...

    // ------------- relations ------------------

    protected Object handleGetRootPackage()
    {
        Collection rootPackages =
                metaObject.getModelManagement().getModel().refAllOfType();
        return rootPackages.iterator().next();
    }

    // ------------------------------------------------------------

    protected Collection handleGetAllUseCases()
    {
        return metaObject.getUseCases().getUseCase().refAllOfType();
    }

    protected Collection handleGetAllActors()
    {
        return metaObject.getUseCases().getActor().refAllOfType();
    }

    protected Collection handleGetAllActionStates()
    {
        return metaObject.getActivityGraphs().getActionState().refAllOfType();
    }

    protected Collection handleGetAllFinalStates()
    {
        return metaObject.getStateMachines().getFinalState().refAllOfType();
    }

    protected Collection handleGetAllActivityGraphs()
    {
        return metaObject.getActivityGraphs().getActivityGraph().refAllOfType();
    }

    protected Collection handleGetAllTransitions()
    {
        return metaObject.getStateMachines().getTransition().refAllOfType();
    }

    protected Collection handleGetAllOperations()
    {
        return metaObject.getCore().getOperation().refAllOfType();
    }
}
