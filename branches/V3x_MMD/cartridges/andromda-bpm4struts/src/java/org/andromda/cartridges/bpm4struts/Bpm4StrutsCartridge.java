package org.andromda.cartridges.bpm4struts;

import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsActionStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsControllerDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsEventDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsFinalStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsGuardDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsInputFieldDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsModelDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsObjectFlowStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsPseudostateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsSimpleStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsStateMachineDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsTransitionDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsUseCaseDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsViewDecoratorImpl;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;

import java.util.Properties;

public class Bpm4StrutsCartridge extends DefaultAndroMDACartridge {

    public void init(Properties velocityProperties) throws Exception {
        super.init(velocityProperties);

        DecoratorFactory df = DecoratorFactory.getInstance();
        String oldNamespace = df.getActiveNamespace();
        df.setActiveNamespace (getDescriptor().getCartridgeName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.ActionState$Impl",
            null,
            StrutsActionStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.core.foundation.Classifier$Impl",
            Bpm4StrutsProfile.STEREOTYPE_CONTROLLER,
            StrutsControllerDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Event$Impl",
            null,
            StrutsEventDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.FinalState$Impl",
            null,
            StrutsFinalStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Guard$Impl",
            null,
            StrutsGuardDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.core.foundation.Classifier$Impl",
            Bpm4StrutsProfile.STEREOTYPE_INPUTFIELD,
            StrutsInputFieldDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.core.foundation.Classifier$Impl",
            Bpm4StrutsProfile.STEREOTYPE_MODEL,
            StrutsModelDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState$Impl",
            null,
            StrutsObjectFlowStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Pseudostate$Impl",
            null,
            StrutsPseudostateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.State$Impl",
            null,
            StrutsSimpleStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.StateMachine$Impl",
            null,
            StrutsStateMachineDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.Transition$Impl",
            null,
            StrutsTransitionDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.usecases.UseCase$Impl",
            null,
            StrutsUseCaseDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.core.foundation.Classifier$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW,
            StrutsViewDecoratorImpl.class.getName());

        df.setActiveNamespace (oldNamespace);
    }
}