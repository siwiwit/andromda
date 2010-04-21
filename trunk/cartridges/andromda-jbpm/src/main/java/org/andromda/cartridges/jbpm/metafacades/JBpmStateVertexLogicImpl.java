package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex
 * @author Bob Fields
 */
public class JBpmStateVertexLogicImpl
    extends JBpmStateVertexLogic
{

    public JBpmStateVertexLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#isContainedInBusinessProcess()
     */
    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getStateMachine()!= null 
                && this.getStateMachine() instanceof ActivityGraphFacade
                && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodeClassName()
     */
    protected java.lang.String handleGetNodeClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + "Node";
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodePackageName()
     */
    protected java.lang.String handleGetNodePackageName()
    {
        return (this.getProcessDefinition() == null) ? null : this.getProcessDefinition().getPackageName();
    }

    protected Object handleGetProcessDefinition()
    {
        Object processDefinition = null;

        final StateMachineFacade stateMachine = this.getStateMachine();
        if (stateMachine instanceof ActivityGraphFacade)
        {
            processDefinition = ((ActivityGraphFacade)stateMachine).getUseCase();
        }

        return (processDefinition instanceof JBpmProcessDefinition) ? processDefinition : null;
    }
}