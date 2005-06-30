package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate
 */
public class JBpmPseudostateLogicImpl
    extends JBpmPseudostateLogic
{

    public JBpmPseudostateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getStateMachine() instanceof ActivityGraphFacade
                && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getClazz()
     */
    protected java.lang.String handleGetClazz()
    {
        String clazz = null;

        if (isDecisionPoint())
        {
            final StateMachineFacade stateMachine = getStateMachine();
            if (stateMachine instanceof ActivityGraphFacade)
            {
                final ActivityGraphFacade graph = (ActivityGraphFacade)stateMachine;
                final UseCaseFacade useCase = graph.getUseCase();
                if (useCase != null)
                {
                    final StringBuffer clazzBuffer = new StringBuffer();
                    if (StringUtils.isNotBlank(useCase.getPackageName()))
                    {
                        clazzBuffer.append(useCase.getPackageName());
                        clazzBuffer.append('.');
                    }
                    clazzBuffer.append(getClassName());
                    clazz = clazzBuffer.toString();
                }
            }
        }

        return clazz;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getClassName()
     */
    protected java.lang.String handleGetClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName());
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getSwimlane()
     */
    protected java.lang.Object handleGetSwimlane()
    {
        return this.getPartition();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getBeforeSignal()
     */
    protected java.util.List handleGetBeforeSignal()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getAfterSignal()
     */
    protected java.util.List handleGetAfterSignal()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getNodeEnter()
     */
    protected java.util.List handleGetNodeEnter()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getNodeLeave()
     */
    protected java.util.List handleGetNodeLeave()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    public boolean isCollect()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isSplit()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}