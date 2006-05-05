package org.andromda.metafacades.emf.uml2;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.DependencyFacade.
 *
 * @see org.andromda.metafacades.uml.DependencyFacade
 */
public class DependencyFacadeLogicImpl
    extends DependencyFacadeLogic
{
    public DependencyFacadeLogicImpl(
        org.eclipse.uml2.Dependency metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Gets the name in the following manner. <ol> <li>If the dependency has a name return it.</li> <li>If the
     * dependency does <strong>NOT </strong> have a name, get the target element's and return its name
     * uncapitalized.</li> </ol>
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String handleGetName()
    {
        String name = super.handleGetName();
        if (StringUtils.isBlank(name) && this.getTargetElement() != null)
        {
            name = StringUtils.uncapitalize(this.getTargetElement().getName());
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getTargetElement()
     */
    protected java.lang.Object handleGetTargetElement()
    {
        return this.metaObject.getTargets().toArray()[0];
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSourceElement()
     */
    protected java.lang.Object handleGetSourceElement()
    {
        return this.metaObject.getClients().toArray()[0];
    }
}