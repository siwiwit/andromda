// license-header java merge-point
// Generated by andromda-jsf cartridge (utils\MessagePhaseListener.java.vsl) DO NOT EDIT!
package org.andromda.presentation.jsf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.myfaces.trinidad.context.RequestContext;

/**
 * Used to pass messages to the current faces context (this allows messages to live beyond
 * a request, which is very useful when redirecting).
 *
 * @author Chad Brandon
 */
public class MessagePhaseListener
    extends AbstractPhaseListener
{
    private static final long serialVersionUID = 1L;

    private static final String ARGUMENT_PREFIX = "arg:";

    /**
     * @see org.andromda.presentation.jsf.AbstractPhaseListener#handleBeforePhase(PhaseEvent)
     */
    @Override
    protected void handleBeforePhase(PhaseEvent event)
    {
        final FacesContext context = event.getFacesContext();
        final UIViewRoot root = context.getViewRoot();
        for (final Iterator<String> iterator = context.getClientIdsWithMessages(); iterator.hasNext();)
        {
            final String clientId = iterator.next();
            if (clientId != null)
            {
                final UIComponent component = root.findComponent(clientId);
                final Collection<Object> arguments = new ArrayList<Object>();
                if (component != null)
                {
                    for (final Iterator<UIComponent> iterator2 = component.getChildren().iterator(); iterator2.hasNext();)
                    {
                        final Object child = iterator2.next();
                        if (child instanceof UIParameter)
                        {
                            final UIParameter parameter = (UIParameter)child;
                            if (parameter.getName() != null)
                            {
                                if (parameter.getName().startsWith(ARGUMENT_PREFIX))
                                {
                                    arguments.add(parameter.getValue());
                                }
                            }
                       }
                   }
               }
               for (final Iterator<FacesMessage> iterator2 = context.getMessages(clientId); iterator2.hasNext();)
               {
                   final FacesMessage facesMessage = iterator2.next();
                   final String messageKey = this.getMessageKey(facesMessage.getDetail());
                   final String newMessage = Messages.get(messageKey, null);
                   if (!newMessage.equals(messageKey))
                   {
                       facesMessage.setDetail(MessageFormat.format(newMessage, arguments.toArray(new Object[0])));
                   }
               }
            }
        }
        RequestContext requestContext = RequestContext.getCurrentInstance();
        final Object form =  requestContext != null ? requestContext.getPageFlowScope().get("form") : null;
        if (form != null)
        {
            try
            {
                final Collection<FacesMessage> messages = (Collection<FacesMessage>)PropertyUtils.getProperty(form, "jsfMessages");
                if (messages != null)
                {
                    for (final Iterator<FacesMessage> iterator = messages.iterator(); iterator.hasNext();)
                    {
                        FacesContext.getCurrentInstance().addMessage(null, iterator.next());
                    }
                    // - set the messages title to use (if we have a severity of error or higher)
                    final FacesMessage.Severity severity = context.getMaximumSeverity();
                    if (severity != null && severity.getOrdinal() >= FacesMessage.SEVERITY_ERROR.getOrdinal())
                    {
                        final Object request = context.getExternalContext().getRequest();
                        if (request instanceof HttpServletRequest)
                        {
                            String messagesTitle = (String)form.getClass().getMethod("getJsfMessagesTitle", (Class[])null).invoke(form, (Object[])null);
                            if (messagesTitle == null || messagesTitle.trim().length() == 0)
                            {
                                messagesTitle = Messages.get("errors.header", null);
                            }
                            ((HttpServletRequest)request).setAttribute(MESSAGES_TITLE, messagesTitle);
                        }
                    }
                    form.getClass().getMethod("clearJsfMessages", (Class[])null).invoke(form, (Object[])null);
                    form.getClass().getMethod("setJsfMessagesTitle",
                        new Class[]{String.class}).invoke(form, new Object[]{null});
                }
            }
            catch (final Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    /**
     * The name of the property storing the title to use for faces messages in the request scope
     */
    protected static final String MESSAGES_TITLE = "jsfMessagesTitle";

    /**
     * Empty method
     * @see org.andromda.presentation.jsf.AbstractPhaseListener#handleAfterPhase(PhaseEvent)
     */
    @Override
    protected void handleAfterPhase(PhaseEvent event)
    {
        // Empty method
    }

    /**
     * @see org.andromda.presentation.jsf.AbstractPhaseListener#getPhaseId()
     */
    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }

    private String getMessageKey(final String detail)
    {
        return detail != null ? detail.replaceAll(".*:", "").replace(".", "").trim().replaceAll("\\s+", ".").toLowerCase() : null;
    }
}