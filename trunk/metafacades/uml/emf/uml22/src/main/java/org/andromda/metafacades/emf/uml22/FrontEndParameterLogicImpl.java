package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 */
public class FrontEndParameterLogicImpl
    extends FrontEndParameterLogic
{
    private static final long serialVersionUID = -5932754222510758357L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndParameterLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    @Override
    protected boolean handleIsControllerOperationArgument()
    {
        return this.getControllerOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    @Override
    protected OperationFacade handleGetControllerOperation()
    {
        return this.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
    @Override
    protected Object handleGetView()
    {
        Object view = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction)
            {
                final FrontEndAction action = (FrontEndAction)transition;
                view = action.getInput();
            }
            else if (transition instanceof FrontEndForward)
            {
                final FrontEndForward forward = (FrontEndForward)transition;
                if (forward.isEnteringView())
                {
                    view = forward.getTarget();
                }
            }
        }
        return view;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    @Override
    protected boolean handleIsActionParameter()
    {
        final FrontEndAction action = this.getAction();
        return action != null && action.getParameters().contains(this.THIS());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction()
    {
        FrontEndAction actionObject = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction)
            {
                actionObject = (FrontEndAction)transition;
            }
        }
        return actionObject;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
    @Override
    protected boolean handleIsTable()
    {
        boolean isTable = false;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            isTable = isMany() || type.isCollectionType() || type.isArrayType();
            if (isTable)
            {
                final String tableTaggedValue = ObjectUtils.toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_IS_TABLE));
                isTable =
                    StringUtils.isNotBlank(tableTaggedValue) ? Boolean.valueOf(tableTaggedValue.trim()) : true;
                if (!isTable)
                {
                    isTable = !this.getTableColumnNames().isEmpty();
                }
            }
        }
        return isTable && this.getOperation() == null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumnNames()
     */
    @Override
    protected Collection<String> handleGetTableColumnNames()
    {
        final Collection<String> tableColumnNames = new LinkedHashSet<String>();
        final Collection<Object> taggedValues = this.findTaggedValues(UMLProfile.TAGGEDVALUE_PRESENTATION_TABLE_COLUMNS);
        if (!taggedValues.isEmpty())
        {
            for (final Object value : taggedValues)
            {
                final String taggedValue = StringUtils.trimToNull(String.valueOf(value));
                if (taggedValue != null)
                {
                    final String[] properties = taggedValue.split("[,\\s]+");
                    for (int ctr = 0; ctr < properties.length; ctr++)
                    {
                        final String property = properties[ctr];
                        tableColumnNames.add(property);
                    }
                }
            }
        }

        // - if we have no table column names explicitly defined, use the table
        // attribute names.
        if (tableColumnNames.isEmpty())
        {
            tableColumnNames.addAll(this.getTableAttributeNames());
        }
        return tableColumnNames;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    @Override
    protected Collection<String> handleGetTableColumns()
    {
        final Collection<String> tableColumns = new ArrayList(this.getNonArrayAttributes());
        final Collection<String> tableColumnNames = this.getTableColumnNames();
        CollectionUtils.filter(
            tableColumns,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final ModelElementFacade attribute = (ModelElementFacade)object;
                    final String attributeName = attribute.getName();
                    return attributeName != null && tableColumnNames.contains(attributeName);
                }
            });
        return tableColumns;
    }

    /**
     * Gets all attributes for an array type that has a corresponding non-array
     * type.
     *
     * @return the collection of attributes.
     */
    private Collection<AttributeFacade> getNonArrayAttributes()
    {
        final Collection<AttributeFacade> nonArrayAttributes = new ArrayList<AttributeFacade>();
        final ClassifierFacade type = this.getType();
        if (type != null && (type.isArrayType() || isMany()))
        {
            final ClassifierFacade nonArrayType = type.getNonArray();
            if (nonArrayType != null)
            {
                nonArrayAttributes.addAll(nonArrayType.getAttributes(true));
            }
        }
        return nonArrayAttributes;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableAttributeNames()
     */
    @Override
    protected Collection<String> handleGetTableAttributeNames()
    {
        final Collection<String> tableAttributeNames = new ArrayList<String>();
        for (AttributeFacade attribute : this.getNonArrayAttributes())
        {
            tableAttributeNames.add(attribute.getName());
        }
        return tableAttributeNames;
    }
}
