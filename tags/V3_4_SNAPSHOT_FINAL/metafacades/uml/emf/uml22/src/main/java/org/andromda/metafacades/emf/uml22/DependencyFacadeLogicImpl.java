package org.andromda.metafacades.emf.uml22;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.DependencyFacade.
 *
 * @see org.andromda.metafacades.uml.DependencyFacade
 */
public class DependencyFacadeLogicImpl
    extends DependencyFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public DependencyFacadeLogicImpl(
        final DirectedRelationship metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(DependencyFacadeLogicImpl.class);

    /**
     * Gets the name in the following manner.
     * <ol>
     * <li>If the dependency has a name return it.</li>
     * <li>If the dependency does <strong>NOT </strong> have a name, get the
     * target element's and return its name uncapitalized.</li>
     * </ol>
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
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
    @Override
    protected String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSetterName()
     */
    @Override
    protected String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getTargetElement()
     */
    @Override
    protected Object handleGetTargetElement()
    {
        Object transform = null;
        final List<Element> elist = this.metaObject.getTargets();
        if (elist == null || elist.isEmpty())
        {
            DependencyFacadeLogicImpl.LOGGER.error("DependencyFacade has no targets: " + this.metaObject.getSources().toString());
        }
        else
        {
            transform = UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getTargets().toArray()[0]);
        }
        return transform;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSourceElement()
     */
    @Override
    protected Object handleGetSourceElement()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getSources().toArray()[0]);
    }
}
