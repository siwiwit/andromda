package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
 * @author Bob Fields
 */
public class StrutsJspLogicImpl
    extends StrutsJspLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsJspLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJspLogic#getPackageName()
     */
    public String getPackageName()
    {
        String packageName = null;

        final StateMachineFacade graphContext = getStateMachine();
        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade graphUseCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (graphUseCase instanceof StrutsUseCase)
            {
                final StrutsUseCase useCase = (StrutsUseCase)graphUseCase;
                packageName = useCase.getPackageName();
            }
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return StringUtils.replace(
            this.getPackageName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    protected String handleGetMessageKey()
    {
        final StringBuilder messageKey = new StringBuilder();

        if (!normalizeMessages())
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

    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        return (!this.isDocumentationPresent()) ? "" 
            : StringUtilsHelper.toResourceMessage(this.getDocumentation(""));
    }

    protected String handleGetOnlineHelpKey()
    {
        return getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final StringBuilder buffer = new StringBuilder();

        buffer.append(!this.isDocumentationPresent() ? "No page documentation has been specified" 
            : StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false)));
        buffer.append(crlf);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected String handleGetOnlineHelpPagePath()
    {
        return this.getFullPath() + "_help";
    }

    protected String handleGetOnlineHelpActionPath()
    {
        final StringBuilder buffer = new StringBuilder();

        if (StringUtils.isNotBlank(this.getPackagePath()))
        {
            buffer.append('/');
            buffer.append(this.getPackagePath());
        }
        buffer.append('/');
        buffer.append(StringUtilsHelper.upperCamelCaseName(this.getName()));
        buffer.append("Help");

        return buffer.toString();
    }

    protected String handleGetFullPath()
    {
        return '/' +
            (getPackageName() + '.' + Bpm4StrutsUtils.toWebFileName(StringUtils.trimToEmpty(getName()))).replace(
                '.', '/');
    }

    protected boolean handleIsValidationRequired()
    {
        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsDateFieldPresent()
    {
        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isDateFieldPresent())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsCalendarRequired()
    {
        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            if (action.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Overridden since StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndView#getAllActionParameters()
     */
    public List getAllActionParameters()
    {
        final List actionParameters = new ArrayList();
        final Collection actions = getActions();
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)iterator.next();
            actionParameters.addAll(action.getActionParameters());
        }
        return actionParameters;
    }

    /**
     * Overridden because StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    public List getActions()
    {
        final List actions = new ArrayList();
        final Collection outgoings = this.getOutgoings();

        for (final Iterator iterator = outgoings.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof StrutsAction)
                actions.add(object);
        }

        return actions;
    }

    protected List handleGetNonActionForwards()
    {
        final List actions = new ArrayList();
        final Collection outgoings = getOutgoings();

        for (final Iterator iterator = outgoings.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (!(object instanceof StrutsAction))
            {
                actions.add(object);
            }
        }
        return actions;
    }

    protected List handleGetPageVariables()
    {
        return this.getVariables();
    }

    protected List handleGetIncomingActions()
    {
        final List incomingActionsList = new ArrayList();
        collectIncomingActions(this, new LinkedHashSet(), incomingActionsList);
        return incomingActionsList;
    }

    /**
     * Collects all actions that are entering the argument state vertex.
     *
     * @param stateVertex          the statevertex to process
     * @param processedTransitions the transitions that have already been processed
     * @param actions              the actions collected so far
     */
    private void collectIncomingActions(
        StateVertexFacade stateVertex,
        Collection processedTransitions,
        Collection actions)
    {
        final Collection incomingTransitions = stateVertex.getIncomings();
        for (final Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
        {
            final TransitionFacade incomingTransition = (TransitionFacade)iterator.next();
            collectIncomingActions(incomingTransition, processedTransitions, actions);
        }
    }

    /**
     * Collects all actions that are possibly traversing the argument transitions.
     *
     * @param transition           the transition to process
     * @param processedTransitions the transitions that have already been processed
     * @param actions              the actions collected so far
     */
    private void collectIncomingActions(
        TransitionFacade transition,
        Collection processedTransitions,
        Collection actions)
    {
        if (!processedTransitions.contains(transition))
        {
            processedTransitions.add(transition);
            if (transition instanceof StrutsAction)
            {
                actions.add(transition);

/*  @todo: TEMPORARILY COMMENTED OUT -- needs verification that isCaseStart() forms are not populated, but I think they are
                if (((StrutsAction)transition).isUseCaseStart())
                {
                    Collection finalStates = getUseCase().getFinalStates();// todo: test usecase for null
                    for (final Iterator iterator = finalStates.iterator(); iterator.hasNext();)
                    {
                        FinalStateFacade finalState = (FinalStateFacade) iterator.next();
                        collectIncomingActions(finalState, processedTransitions, actions);
                    }
                }
*/
            }
            else
            {
                final Collection incomingTransitions = transition.getSource().getIncomings();
                for (final Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
                {
                    final TransitionFacade incomingTransition = (TransitionFacade)iterator.next();
                    collectIncomingActions(incomingTransition, processedTransitions, actions);
                }
            }
        }
    }

    protected String handleGetCssFileName()
    {
        return getFullPath() + ".css";
    }

    protected List handleGetNonTableActions()
    {
        final List nonTableActions = new ArrayList();

        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            if (!action.isTableLink())
            {
                nonTableActions.add(action);
            }
        }

        return nonTableActions;
    }

    private boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
}