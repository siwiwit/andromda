package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
 */
public class StrutsControllerOperationLogicImpl
        extends StrutsControllerOperationLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
{
    // ---------------- constructor -------------------------------

    public StrutsControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    public String handleGetInterfaceName()
    {
        return StringUtilsHelper.upperCamelCaseName(getOwner().getName()) +
                StringUtilsHelper.upperCamelCaseName(getName()) + "Interface";
    }

    public String handleGetInterfacePackageName()
    {
        return getOwner().getPackageName();
    }

    public String handleGetInterfaceFullPath()
    {
        return '/' + getInterfacePackageName().replace('.', '/') + '/' + getInterfaceName();
    }

    private Collection deferringActions = null;

    public java.util.Collection handleGetDeferringActions()
    {
        if (this.deferringActions == null)
        {
            Collection deferringActions = new HashSet();

            ClassifierFacade owner = getOwner();
            if (owner instanceof StrutsController)
            {
                StrutsController controller = (StrutsController) owner;
                StrutsActivityGraph graph = controller.getUseCase().getActivityGraph();

                Collection actionStates = graph.getActionStates();
                for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
                {
                    StrutsActionState actionState = (StrutsActionState) actionStateIterator.next();
                    Collection controllerCalls = actionState.getControllerCalls();
                    for (Iterator controllerCallIterator = controllerCalls.iterator(); controllerCallIterator.hasNext();)
                    {
                        OperationFacade operation = (OperationFacade) controllerCallIterator.next();
                        if (this.equals(operation))
                        {
                            deferringActions.addAll(actionState.getContainerActions());
                        }
                    }
                }

                Collection transitions = graph.getTransitions();
                for (Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                {
                    StrutsForward transition = (StrutsForward) transitionIterator.next();
                    EventFacade event = transition.getTrigger();
                    if (event instanceof StrutsTrigger)
                    {
                        StrutsTrigger trigger = (StrutsTrigger) event;
                        StrutsControllerOperation operation = trigger.getControllerCall();
                        if (this.equals(operation))
                        {
                            StateVertexFacade source = transition.getSource();
                            if (source instanceof StrutsActionState)
                            {
                                deferringActions.addAll(((StrutsActionState) source).getContainerActions());
                            }
                        }
                    }
                }
            }
            this.deferringActions = deferringActions;
        }
        return deferringActions;
    }

    protected Object handleGetController()
    {
        Object owner = getOwner();
        return (owner instanceof StrutsController) ? owner : null;
    }

    private Collection formFields = null;

    protected Collection handleGetFormFields()
    {
        if (formFields == null)
        {
            Collection fields = new HashSet();

            Collection actions = getDeferringActions();
            for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                StrutsAction action = (StrutsAction) actionIterator.next();
                fields.addAll(action.getAllFormFields());
            }
            formFields = fields;
        }
        return formFields;
    }
}
