// license-header java merge-point
// Generated by andromda-jsf cartridge (controllers\Controller.java.vsl) DO NOT EDIT!
package org.andromda.cartridges.jsf.tests.exceptions;

import java.io.Serializable;
import java.lang.reflect.Method;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.andromda.presentation.jsf.JsfUtils;

/**
 * 
 */
public abstract class Controller
    implements Serializable{
    private static final long serialVersionUID = 1L;

    private static final Log logger = LogFactory.getLog(Controller.class);

    /**
     * Resets all the "isSet" flags on the forms to false.
     */
    protected void resetFormSetFlags()
    {
        // Resets all the "isSet" flags on the forms to false.
    }

    /**
     * 
     * This method is called when 'submit' is triggered in the view 'enter info'.
     * It can be safely overridden in descendant classes.
     */
    protected void _enterInfo_submit()
    {
        //this method can be overridden
    }

    /**
     * @return enterInfoSubmit
     */
    public String enterInfoSubmit()
    {
        return enterInfoSubmit((javax.faces.event.FacesEvent)null);
    }

    /**
     * @param event
     */
    public void enterInfoSubmit(final ActionEvent event)
    {
        this.enterInfoSubmit((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     */
    public void enterInfoSubmit(final javax.faces.event.ValueChangeEvent event)
    {
        this.enterInfoSubmit((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     * @return enterInfoSubmit
     */
    public String enterInfoSubmit(final javax.faces.event.FacesEvent event)
    {
        String forward = null;
        final Object currentForm = this.resolveVariable("form");
        try
        {
            //trigger method execution
            _enterInfo_submit();

            forward = _doSomething();
            final FacesMessage.Severity messageSeverity = this.getMaximumMessageSeverity();
            if (messageSeverity != null && FacesMessage.SEVERITY_ERROR.getOrdinal() <= messageSeverity.getOrdinal())
            {
                this.setForm("form", currentForm, false);
            }
            if (event != null)
            {
                ExceptionsActivityViewPopulator.populateFormAndViewVariables(this.getContext(), null);
            }
        }
        catch (final Throwable throwable)
        {
            this.setForm("form", currentForm, false);
            // - set the forward to null so that we stay on the current view
            forward = null;
            try
            {
                // - the exception is re-thrown by the exception handler and handled by the catch below if it can't get a messageKey
                //   (no reason to check for presence of messageKey)
                this.addErrorMessage(org.andromda.presentation.jsf.Messages.get(
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().handleException(throwable),
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().getMessageArguments(throwable)));
            }
            catch (Throwable exception)
            {
                logger.error(exception.getMessage(),exception);
                this.addExceptionMessage(exception);
            }
        }
        return forward;
    }

    /**
     * 
     * It can be safely overridden in descendant classes,
     * you should return the forward unless you want to change the default flow.
     * @param forward
     * @return forward
     */
    protected String _doSomething( String forward)
    {
        //this method can be overridden
        return forward;
    }

    /**
     * 
     * @return forward
     * @throws Throwable
     */
    private String _doSomething()
        throws Throwable
    {
        String forward = null;
        forward = "exceptions-activity-show-something";
        forward = _doSomething( forward);
        return forward;
    }

    /**
     * 
     * This method is called when 'submit2' is triggered in the view 'show something'.
     * It can be safely overridden in descendant classes.
     */
    protected void _showSomething_submit2()
    {
        //this method can be overridden
    }

    /**
     * @return showSomethingSubmit2
     */
    public String showSomethingSubmit2()
    {
        return showSomethingSubmit2((javax.faces.event.FacesEvent)null);
    }

    /**
     * @param event
     */
    public void showSomethingSubmit2(final ActionEvent event)
    {
        this.showSomethingSubmit2((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     */
    public void showSomethingSubmit2(final javax.faces.event.ValueChangeEvent event)
    {
        this.showSomethingSubmit2((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     * @return showSomethingSubmit2
     */
    public String showSomethingSubmit2(final javax.faces.event.FacesEvent event)
    {
        String forward = null;
        final Object currentForm = this.resolveVariable("form");
        try
        {
            //trigger method execution
            _showSomething_submit2();

            forward = _stopUsecase();
            final FacesMessage.Severity messageSeverity = this.getMaximumMessageSeverity();
            if (messageSeverity != null && FacesMessage.SEVERITY_ERROR.getOrdinal() <= messageSeverity.getOrdinal())
            {
                this.setForm("form", currentForm, false);
            }
            if (event != null)
            {
                ExceptionsActivityViewPopulator.populateFormAndViewVariables(this.getContext(), null);
            }
        }
        catch (final Throwable throwable)
        {
            this.setForm("form", currentForm, false);
            // - set the forward to null so that we stay on the current view
            forward = null;
            try
            {
                // - the exception is re-thrown by the exception handler and handled by the catch below if it can't get a messageKey
                //   (no reason to check for presence of messageKey)
                this.addErrorMessage(org.andromda.presentation.jsf.Messages.get(
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().handleException(throwable),
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().getMessageArguments(throwable)));
            }
            catch (Throwable exception)
            {
                logger.error(exception.getMessage(),exception);
                this.addExceptionMessage(exception);
            }
        }
        return forward;
    }

    /**
     * 
     * It can be safely overridden in descendant classes,
     * you should return the forward unless you want to change the default flow.
     * @param forward
     * @return forward
     */
    protected String _stopUsecase( String forward)
    {
        //this method can be overridden
        return forward;
    }

    /**
     * 
     * @return forward
     * @throws Throwable
     */
    private String _stopUsecase()
        throws Throwable
    {
        String forward = null;
        forward = (($targetUseCase.controller.fullyQualifiedName)this.resolveVariable("$targetUseCase.controller.beanName")).${targetUseCase.controllerAction}();
        forward = _stopUsecase( forward);
        return forward;
    }

    /**
     * This method is called when the use case 'Exceptions Activity' starts.
     * It can be safely overridden in descendant classes.
     */
    protected void _exceptionsActivity_started()
    {
        //this method can be overridden
    }

    /**
     * @return exceptionsActivity
     */
    public String exceptionsActivity()
    {
        return exceptionsActivity((javax.faces.event.FacesEvent)null);
    }

    /**
     * @param event
     */
    public void exceptionsActivity(final ActionEvent event)
    {
        this.exceptionsActivity((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     */
    public void exceptionsActivity(final javax.faces.event.ValueChangeEvent event)
    {
        this.exceptionsActivity((javax.faces.event.FacesEvent)event);
    }

    /**
     * @param event
     * @return exceptionsActivity
     */
    public String exceptionsActivity(final javax.faces.event.FacesEvent event)
    {
        String forward = null;
        final Object currentForm = this.resolveVariable("form");
        try
        {
            //trigger method execution
            _exceptionsActivity_started();

            forward = _startUsecase();
            final FacesMessage.Severity messageSeverity = this.getMaximumMessageSeverity();
            if (messageSeverity != null && FacesMessage.SEVERITY_ERROR.getOrdinal() <= messageSeverity.getOrdinal())
            {
                this.setForm("form", currentForm, false);
            }
            if (event != null)
            {
                ExceptionsActivityViewPopulator.populateFormAndViewVariables(this.getContext(), null);
            }
        }
        catch (final Throwable throwable)
        {
            this.setForm("form", currentForm, false);
            // - set the forward to null so that we stay on the current view
            forward = null;
            try
            {
                // - the exception is re-thrown by the exception handler and handled by the catch below if it can't get a messageKey
                //   (no reason to check for presence of messageKey)
                this.addErrorMessage(org.andromda.presentation.jsf.Messages.get(
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().handleException(throwable),
                    org.andromda.presentation.jsf.PatternMatchingExceptionHandler.instance().getMessageArguments(throwable)));
            }
            catch (Throwable exception)
            {
                logger.error(exception.getMessage(),exception);
                this.addExceptionMessage(exception);
            }
        }
        return forward;
    }

    /**
     * 
     * It can be safely overridden in descendant classes,
     * you should return the forward unless you want to change the default flow.
     * @param forward
     * @return forward
     */
    protected String _startUsecase( String forward)
    {
        //this method can be overridden
        return forward;
    }

    /**
     * 
     * @return forward
     * @throws Throwable
     */
    private String _startUsecase()
        throws Throwable
    {
        String forward = null;
        forward = "exceptions-activity-enter-info";
        forward = _startUsecase( forward);
        return forward;
    }

    /**
     * Gets the current faces context.  This object is the point
     * from which to retrieve any request, session, etc information.
     *
     * @return the JSF faces context instance.
     */
    public FacesContext getContext()
    {
        return FacesContext.getCurrentInstance();
    }

    /**
     * A helper method that gets the current request from the faces
     * context.
     *
     * @return the current request instance.
     */
    protected javax.servlet.http.HttpServletRequest getRequest()
    {
        return (javax.servlet.http.HttpServletRequest)this.getContext().getExternalContext().getRequest();
    }

    /**
     * A helper method that gets the current reponse from the faces
     * context.
     *
     * @return the current response instance.
     */
    protected javax.servlet.http.HttpServletResponse getResponse()
    {
        return (javax.servlet.http.HttpServletResponse)this.getContext().getExternalContext().getResponse();
    }

    /**
     * A helper method that gets the current session from the faces
     * context.
     *
     * @param create If the create parameter is true, create (if necessary) and return a
     *        session instance associated with the current request. If the create
     *        parameter is false return any existing session instance associated with the
     *        current request, or return null if there is no such session.
     * @return the current session instance.
     */
    protected javax.servlet.http.HttpSession getSession(final boolean create)
    {
        return (javax.servlet.http.HttpSession)this.getContext().getExternalContext().getSession(create);
    }

    /**
     * Attempts to resolve the variable, given, the <code>name</code> of
     * the variable using the faces context variable resolver instance.
     * @param name
     * @return the resolved variable or null if not found.
     */
    @SuppressWarnings("deprecation")
    protected Object resolveVariable(final String name)
    {
        org.apache.myfaces.trinidad.context.RequestContext adfContext = org.apache.myfaces.trinidad.context.RequestContext.getCurrentInstance();
        Object variable = adfContext.getPageFlowScope().get(name);
        // - if we couldn't get the variable from the regular ADF context, see if
        //   the session contains an ADF context with the variable
        if (variable == null)
        {
            final javax.servlet.http.HttpSession session = this.getSession(false);
            if (session != null)
            {
                final org.andromda.presentation.jsf.AdfFacesContextWrapper contextWrapper =
                    (org.andromda.presentation.jsf.AdfFacesContextWrapper)session.getAttribute("AndroMDAADFContext");
                adfContext = contextWrapper != null ? contextWrapper.getCurrentInstance() : null;
            }
            variable = adfContext != null ? adfContext.getPageFlowScope().get(name) : null;
        }
        // - finally try resolving it in the standard JSF manner
        if (variable == null)
        {
            final FacesContext context = this.getContext();
            variable = context != null ? context.getApplication().getVariableResolver().resolveVariable(context, name) : null;
        }
        return variable;
    }

    private void setForm(final String formKey, final Object form, boolean includeInSession)
    {
        final org.andromda.presentation.jsf.AdfFacesContextWrapper contextWrapper = new org.andromda.presentation.jsf.AdfFacesContextWrapper();
        contextWrapper.getCurrentInstance().getPageFlowScope().put(formKey, form);
        if (includeInSession)
        {
            this.setSessionAttribute(formKey, form);
            // - add this temporary ADF context to the session so that we can retrieve from a view populator if required
            this.getSession(false).setAttribute("AndroMDAADFContext", contextWrapper);
        }
    }

    /**
     * Finds the root cause of the given <code>throwable</code> and
     * adds the message taken from that cause to the faces context messages.
     *
     * @param throwable the exception information to add.
     */
    protected final void addExceptionMessage(
        Throwable throwable)
    {
        String message = null;
        final Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null)
        {
            message = rootCause.toString();
        }
        if (message == null || message.trim().length() == 0)
        {
            message = throwable.toString();
        }
        this.addErrorMessage(message);
    }

    /**
     * Adds the given error <code>message</code> to the current faces context.
     *
     * @param message the message to add.
     */
    protected void addErrorMessage(final String message)
    {
        this.addMessage(FacesMessage.SEVERITY_ERROR, message);
    }

    /**
     * Adds the given warning <code>message</code> to the current faces context.
     *
     * @param message the message to add.
     */
    protected void addWarningMessage(final String message)
    {
        this.addMessage(FacesMessage.SEVERITY_WARN, message);
    }

    /**
     * Adds the given info <code>message</code> to the current faces context.
     *
     * @param message the message to add.
     */
    protected void addInfoMessage(final String message)
    {
        this.addMessage(FacesMessage.SEVERITY_INFO, message);
    }

    /**
     * Adds the given fatal <code>message</code> to the current faces context.
     *
     * @param message the message to add.
     */
    protected void addFatalMessage(final String message)
    {
        this.addMessage(FacesMessage.SEVERITY_FATAL, message);
    }

    /**
     * Adds a message to the faces context (which will show up on your view when using the
     * lt;h:messages/gt; tag).
     *
     * @param severity the severity of the message
     * @param message the message to add.
     */
    protected void addMessage(final FacesMessage.Severity severity, final String message)
    {
        final FacesMessage facesMessage = new FacesMessage(severity, message, message);
        final Object form = this.resolveVariable("form");
        if (form != null)
        {
            try
            {
                final Method method = form.getClass().getMethod(
                    "addJsfMessages",
                    new Class[]{FacesMessage.class});
                method.invoke(form, new Object[]{facesMessage});
            }
            catch (final Exception exception)
            {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Sets the messages title to use on the next view.
     *
     * @param messagesTitle the title to use.
     */
    protected void setMessagesTitle(final String messagesTitle)
    {
        final Object form = this.resolveVariable("form");
        if (form != null)
        {
            try
            {
                final Method method = form.getClass().getMethod(
                    "setJsfMessagesTitle",
                    new Class[]{String.class});
                method.invoke(form, new Object[]{messagesTitle});
            }
            catch (final Exception exception)
            {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Gets the maximum severity of the messages stored in the current form.
     *
     * @return the maximum message severity.
     */
    protected FacesMessage.Severity getMaximumMessageSeverity()
    {
        FacesMessage.Severity maximumSeverity = null;
        final Object form = this.resolveVariable("form");
        if (form != null)
        {
            try
            {
                final Method method = form.getClass().getMethod(
                    "getMaximumMessageSeverity",
                    (Class[])null);
                maximumSeverity = (FacesMessage.Severity)method.invoke(form, (Object[])null);
            }
            catch (final Exception exception)
            {
                throw new RuntimeException(exception);
            }
        }
        return maximumSeverity;
    }

    /**
     * Copies all matching properties from the <code>fromForm</code> to the given
     * <code>toForm</code> overriding any previously set values.
     * @param fromForm
     * @param toForm
     */
    protected void copyForm(final Object fromForm, final Object toForm)
    {
        org.andromda.presentation.jsf.FormPopulator.populateForm(fromForm, toForm, true);
    }
    /**
     * Retrieves the current action form while making sure the form is of the given
     * <code>type</code>.  If the action form is found, but not of the given type, null will
     * be returned.
     *
     * @param type the type of form to retrieve.
     * @return the found form.
     */
    protected Object getCurrentActionForm(final Class<?> type)
    {
        Object form = this.getCurrentActionForm();
        if (!type.isInstance(form))
        {
            form = null;
        }
        return form;
    }

    /**
     * Retrieves the current action form instance.
     *
     * @return the current action form instance.
     */
    protected Object getCurrentActionForm()
    {
        return this.resolveVariable("form");
    }

    /**
     * The name of the request attribute that stores the attributes from the current action event.
     */
    private static final String ACTION_EVENT_ATTRIBUTES = "actionEventAttributes";

    /**
     * This method just captures the event attributes and sets them into the request
     * so that we can retrieve in controller action operation and use to populate form.
     *
     * @param event the action event.
     */
    public void action(ActionEvent event)
    {
        this.setRequestAttribute(ACTION_EVENT_ATTRIBUTES, event.getComponent().getAttributes());
    }

    /**
     * @param name
     * @param object
     */
    protected void setRequestAttribute(final String name, final Object object)
    {
        JsfUtils.setAttribute(this.getContext().getExternalContext().getRequest(), name, object);
    }

    /**
     * @param name
     * @return RequestAttribute
     */
    protected Object getRequestAttribute(final String name)
    {
        return JsfUtils.getAttribute(this.getContext().getExternalContext().getRequest(), name);
    }

    /**
     * @param name
     * @param object
     */
    protected void setSessionAttribute(final String name, final Object object)
    {
        JsfUtils.setAttribute(this.getContext().getExternalContext().getSession(false), name, object);
    }

    /**
     * @param name
     * @return SessionAttribute
     */
    protected Object getSessionAttribute(final String name)
    {
        return JsfUtils.getAttribute(this.getContext().getExternalContext().getSession(false), name);
    }

}