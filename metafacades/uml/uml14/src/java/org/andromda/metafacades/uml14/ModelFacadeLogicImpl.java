package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Metaclass facade implementation.
 */
public class ModelFacadeLogicImpl
        extends ModelFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ModelFacadeLogicImpl(org.omg.uml.UmlPackage metaObject, String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetRootPackage()
    {
        Collection rootPackages = metaObject.getModelManagement().getModel().refAllOfType();
        return rootPackages.iterator().next();
    }

    protected Collection handleGetAllActors()
    {
        return metaObject.getUseCases().getActor().refAllOfType();
    }

    protected Collection handleGetAllActionStates()
    {
        return metaObject.getActivityGraphs().getActionState().refAllOfType();
    }

    protected Collection handleGetAllUseCases()
    {
        return metaObject.getUseCases().getUseCase().refAllOfType();
    }

    protected Collection handleGetAllClasses()
    {
        return metaObject.getCore().getUmlClass().refAllOfType();
    }

    // ------------------------------------------------------------

    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findUseCaseWithTaggedValueOrHyperlink(tag, value));
    }

    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (ClassifierFacade)shieldedElement(UML14MetafacadeUtils.findClassWithTaggedValueOrHyperlink(tag, value));
    }

    protected ActivityGraphFacade handleFindActivityGraphByName(String name)
    {
        return (ActivityGraphFacade)shieldedElement(UML14MetafacadeUtils.findFirstActivityGraphWithName(name));
    }

    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(String name, String stereotypeName)
    {
        return (ActivityGraphFacade)shieldedElement(UML14MetafacadeUtils.findFirstActivityGraphWithNameAndStereotype(
                name, stereotypeName));
    }

    protected UseCaseFacade handleFindUseCaseByName(String name)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findFirstUseCaseWithName(name));
    }

    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(String name, String stereotypeName)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findFirstUseCaseWithNameAndStereotype(name,
                stereotypeName));
    }

    protected Collection handleFindFinalStatesWithNameOrHyperlink(UseCaseFacade useCase)
    {
        UseCase useCaseMetaClass = UML14MetafacadeUtils.getMetaClass(useCase);
        return shieldedElements(UML14MetafacadeUtils.findFinalStatesWithNameOrHyperlink(useCaseMetaClass));
    }

    protected Collection handleGetAllActionStatesWithStereotype(ActivityGraphFacade activityGraph,
                                                                String stereotypeName)
    {
        ActivityGraph activityGraphMetaClass = UML14MetafacadeUtils.getMetaClass(activityGraph);

        CompositeState compositeState = (CompositeState)activityGraphMetaClass.getTop();
        return filter(compositeState.getSubvertex(), new ActionStateWithStereotypeFilter(stereotypeName));
    }

    private class ActionStateWithStereotypeFilter
            implements Predicate
    {
        private String stereotypeName = null;

        public ActionStateWithStereotypeFilter(String stereotypeName)
        {
            this.stereotypeName = stereotypeName;
        }

        public boolean evaluate(Object o)
        {
            return (o instanceof ActionState) && UML14MetafacadeUtils.isStereotypePresent((ModelElement)o,
                    stereotypeName);
        }
    }

    private Collection filter(Collection collection, Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.evaluate(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

}
