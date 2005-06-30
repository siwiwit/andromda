package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmAction.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction
 */
public class JBpmActionLogicImpl
    extends JBpmActionLogic
{

    public JBpmActionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getTransition() instanceof JBpmTransition;
    }

    /**
     * We override this method in order to be able to return the call-event's operation name
     * when the event's name itself has not been specified.
     */
    public String getName()
    {
        String name = super.getName();

        if (StringUtils.isBlank(name))
        {
            final ModelElementFacade operation = getOperation();
            if (operation != null)
            {
                name = operation.getName();
            }
        }

        return name;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isBeforeSignal()
     */
    protected boolean handleIsBeforeSignal()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_BEFORE_SIGNAL);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isAfterSignal()
     */
    protected boolean handleIsAfterSignal()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_AFTER_SIGNAL);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isNodeEnter()
     */
    protected boolean handleIsNodeEnter()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_NODE_ENTER);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isNodeLeave()
     */
    protected boolean handleIsNodeLeave()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_NODE_LEAVE);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isTask()
     */
    protected boolean handleIsTask()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isTimer()
     */
    protected boolean handleIsTimer()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_TIMER);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isBlocking()
     */
    protected boolean handleIsBlocking()
    {
        return false;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getDueDate()
     */
    protected java.lang.String handleGetDueDate()
    {
        return null;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getClazz()
     */
    protected java.lang.String handleGetClazz()
    {
        String clazz = null;

        if (isAssignment())
        {
            final OperationFacade handler = this.getOperation();

            if (handler instanceof JBpmHandler)
            {
                final StringBuffer clazzBuffer = new StringBuffer();
                final String packageName = handler.getOwner().getPackageName();
                clazzBuffer.append(packageName);
                if (StringUtils.isNotBlank(packageName))
                {
                    clazzBuffer.append('.');
                }
                clazzBuffer.append(((JBpmHandler)handler).getClassName());
                clazz = clazzBuffer.toString();
            }
        }

        return clazz;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getConfigType()
     */
    protected java.lang.String handleGetConfigType()
    {
        // @todo
        return null;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isAssignment()
     */
    protected boolean handleIsAssignment()
    {
        return this.getOperation() != null;
    }
}