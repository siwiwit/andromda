package org.andromda.cartridges.bpm4struts;

import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsActionExceptionDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsActionStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsCheckBoxDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsComboBoxDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsControllerDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsEventDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsFinalStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsGuardDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsListDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsModelDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsMultiBoxDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsObjectFlowStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsPseudostateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsRadioButtonDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsSimpleStateDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsStateMachineDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsTextAreaDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsTextFieldDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsTransitionDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsUseCaseDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsViewDecoratorImpl;
import org.andromda.cartridges.bpm4struts.metadecorators.uml14.StrutsPasswordDecoratorImpl;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.core.metadecorators.uml14.DecoratorFactory;

import java.util.Properties;

public class Bpm4StrutsCartridge extends DefaultAndroMDACartridge {

    public void init(Properties velocityProperties) throws Exception {
        super.init(velocityProperties);

        DecoratorFactory df = DecoratorFactory.getInstance();
        String oldNamespace = df.getActiveNamespace();
        df.setActiveNamespace (getDescriptor().getCartridgeName());

        /**
         *
         *      behavioralelements
         *
         */
        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Event$Impl",
            null,
            StrutsEventDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Guard$Impl",
            null,
            StrutsGuardDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Pseudostate$Impl",
            null,
            StrutsPseudostateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.State$Impl",
            null,
            StrutsSimpleStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.statemachines.Transition$Impl",
            null,
            StrutsTransitionDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.ActionState$Impl",
            null,
            StrutsActionStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.FinalState$Impl",
            null,
            StrutsFinalStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState$Impl",
            null,
            StrutsObjectFlowStateDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.activitygraphs.ActivityGraph$Impl",
            null,
            StrutsStateMachineDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.behavioralelements.usecases.UseCase$Impl",
            null,
            StrutsUseCaseDecoratorImpl.class.getName());

        /**
         *
         *      core model elements
         *
         */
        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlClass$Impl",
            Bpm4StrutsProfile.STEREOTYPE_MODEL,
            StrutsModelDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlClass$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW,
            StrutsViewDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlClass$Impl",
            Bpm4StrutsProfile.STEREOTYPE_CONTROLLER,
            StrutsControllerDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.UmlClass$Impl",
            Bpm4StrutsProfile.STEREOTYPE_EXCEPTION,
            StrutsActionExceptionDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_TEXTFIELD,
            StrutsTextFieldDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_TEXTAREA,
            StrutsTextAreaDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_CHECKBOX,
            StrutsCheckBoxDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_RADIOBUTTON,
            StrutsRadioButtonDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_COMBOBOX,
            StrutsComboBoxDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_LIST,
            StrutsListDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_MULTIBOX,
            StrutsMultiBoxDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            Bpm4StrutsProfile.STEREOTYPE_VIEW_PASSWORD,
            StrutsPasswordDecoratorImpl.class.getName());

        df.registerDecoratorClass(
            "org.omg.uml.foundation.core.Attribute$Impl",
            null,
            StrutsTextFieldDecoratorImpl.class.getName());

        df.setActiveNamespace (oldNamespace);
    }
}