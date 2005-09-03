package org.andromda.cartridges.bpm4jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.cartridges.bpm4jsf.BPM4JSFProfile;
import org.andromda.cartridges.bpm4jsf.BPM4JSFUtils;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFView.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView
 */
public class JSFViewLogicImpl
    extends JSFViewLogic
{
    public JSFViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + BPM4JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!this.isNormalizeMessages())
        {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null)
            {
                messageKey.append(StringUtilsHelper.toResourceMessageKey(useCase.getName()));
                messageKey.append('.');
            }
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(getName()));
        return messageKey.toString();
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return value == null ? "" : value;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return this.getMessageKey() + '.' + BPM4JSFGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getPath()
     */
    protected java.lang.String handleGetPath()
    {
        final StringBuffer path = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            path.append(packageName + '.');
        }
        path.append(BPM4JSFUtils.toWebResourceName(StringUtils.trimToEmpty(this.getName())).replace(
                '.',
                '/'));
        return '/' + path.toString().replace(
            '.',
            '/');
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getForwards()
     */
    protected List handleGetForwards()
    {
        final Set forwards = new LinkedHashSet();
        for (final Iterator iterator = this.getActions().iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            if (action != null && !action.isUseCaseStart())
            {
                forwards.addAll(action.getActionForwards());
            }
        }
        return new ArrayList(forwards);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#isTableLink()
     */
    protected List handleGetTables()
    {
        final List tables = new ArrayList();
        final List variables = this.getVariables();
        for (int ctr = 0; ctr < variables.size(); ctr++)
        {
            final JSFParameter variable = (JSFParameter)variables.get(ctr);
            if (variable.isTable())
            {
                tables.add(variable);
            }
        }
        return tables;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getActionForwards()
     */
    protected List handleGetActionForwards()
    {
        final List actionForwards = new ArrayList(this.getForwards());
        for (final Iterator iterator = actionForwards.iterator(); iterator.hasNext();)
        {
            if (!(iterator.next() instanceof JSFAction))
            {
                iterator.remove();
            }
        }
        return actionForwards;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFullyQuailifiedPopulator()
     */
    protected String handleGetFullyQualifiedPopulator()
    {
        final StringBuffer name = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            name.append(packageName);
            name.append('.');
        }
        name.append(this.getPopulator());
        return name.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getPopulator()
     */
    protected String handleGetPopulator()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFormActions()
     */
    protected List handleGetFormActions()
    {
        final List actions = new ArrayList(this.getActions());
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            final JSFAction action = (JSFAction)iterator.next();
            if (action.getFormFields().isEmpty())
            {
                iterator.remove();
            }
        }
        return actions;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#getPopulatorPath()
     */
    protected String handleGetPopulatorPath()
    {
        return this.getFullyQualifiedPopulator().replace(
            '.',
            '/');
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#isPopulatorRequired()
     */
    protected boolean handleIsPopulatorRequired()
    {
        return !this.getFormActions().isEmpty() || !this.getVariables().isEmpty();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#isPopulatorRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final JSFAction action = (JSFAction)actionIterator.next();
            if (action.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFView#isPopup()
     */
    protected boolean handleIsPopup()
    {
        return ObjectUtils.toString(this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_VIEW_TYPE)).equalsIgnoreCase(
            BPM4JSFGlobals.VIEW_TYPE_POPUP);
    }
}