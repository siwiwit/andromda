package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metadecorators.uml14.*;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * A view in Struts is represented using a page, typically a JSP page. *
 * <p/>
 * Metaclass decorator implementation for $decoratedMetacName
 */
public class StrutsJspDecoratorImpl extends StrutsJspDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsJspDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsJspDecorator ...
    public String getTitleKey()
    {
        return getKey() + ".title";
    }

    public String getTitleValue()
    {
        return StringUtilsHelper.upperCaseFirstLetter(StringUtilsHelper.separate(metaObject.getName(), " "));
    }

    public String getKey()
    {
        return StringUtilsHelper.separate(metaObject.getName(), ".").toLowerCase();
    }

    public java.lang.String getFullPathName()
    {
        return '/' + getFormBean().getPackageName().replace('.','/') + '/' + metaObject.getName();
    }

    public java.lang.String getFullPackageName()
    {
        return getFormBean().getPackageName() + '.' + metaObject.getName();
    }

    public boolean hasForms()
    {
        Collection messages = getMessages();
        for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
        {
            StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
            if (messageDecorator.isForm()) return true;
        }
        return false;
    }

    // ------------- relations ------------------

    protected Collection handleGetForms()
    {
        final Collection forms = new LinkedList();
        final Collection messages = getMessages();
        for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
        {
            StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
            if (messageDecorator.isForm())
                forms.add(messageDecorator.getMetaObject());
        }
        return forms;
    }

    protected Collection handleGetHyperlinks()
    {
        final Collection hyperlinks = new LinkedList();
        final Collection messages = getMessages();
        for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
        {
            StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
            if (messageDecorator.isHyperlink())
                hyperlinks.add(messageDecorator.getMetaObject());
        }
        return hyperlinks;
    }

    protected Collection handleGetParagraphs()
    {
        final Collection paragraphs = new LinkedList();
        final Collection messages = getMessages();
        for (Iterator messageIterator = messages.iterator(); messageIterator.hasNext();)
        {
            StrutsMessageDecorator messageDecorator = (StrutsMessageDecorator) messageIterator.next();
            if (messageDecorator.isParagraph())
                paragraphs.add(messageDecorator.getMetaObject());
        }
        return paragraphs;
    }

    /**
     *
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetServlet()
    {
        final StrutsActivityGraphDecorator activityGraph = (StrutsActivityGraphDecorator)getActionState().getActivityGraph();
        final StrutsUseCaseDecorator useCase = activityGraph.getUseCase();
        final Collection controllers = getFormBean().getServlets();
        for (Iterator iterator = controllers.iterator(); iterator.hasNext();)
        {
            StrutsServletDecorator controller = (StrutsServletDecorator) iterator.next();
            if (controller.getUseCase().getMetaObject() == useCase.getMetaObject())
                return controller.getMetaObject();
        }
        return null;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetFormBean()
    {
        Collection supplierDependencies = getSupplierDependencies();
        for (Iterator dependencyIterator=supplierDependencies.iterator(); dependencyIterator.hasNext();)
        {
            Object element = dependencyIterator.next();
            Dependency dep = (Dependency) element;
            ModelElement client = (ModelElement) dep.getClient().iterator().next();
            ModelElementDecorator clientDecorator = (ModelElementDecorator)decoratedElement(client);
            if (clientDecorator instanceof StrutsFormBeanDecorator)
                return client;
        }
        return null;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public org.omg.uml.foundation.core.ModelElement handleGetActionState()
    {
        final String actionStateName = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_STATE);
        final Collection actionStates = getPossibleActionStates();
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            ActionStateDecorator actionStateDecorator = (ActionStateDecorator) actionStateIterator.next();
            ActionState actionState = (ActionState)actionStateDecorator.getMetaObject();
            if (actionStateName.equalsIgnoreCase(actionState.getName()))
                return actionState;
        }
        return null;
    }

    // ------------------------------------------------------------

    /**
     *
     *
     */
    public java.util.Collection handleGetMessages()
    {
        final Collection messages = new LinkedList();
        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndDecorator associationEnd = (AssociationEndDecorator)iterator.next();
            final ClassifierDecorator otherEnd = associationEnd.getOtherEnd().getType();
            if (otherEnd.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_MESSAGE))
                messages.add(otherEnd.getMetaObject());
        }
        return messages;
    }

    // ------------------------------------------------------------

    private Collection getSupplierDependencies()
    {
        final UmlPackage model = MetaDecoratorUtil.getModel(metaObject);
        final Collection allDependencies = model.getCore().getDependency().refAllOfType();
        final Collection supplierDependencies = new LinkedList();

        for (Iterator iterator = allDependencies.iterator(); iterator.hasNext();)
        {
            Dependency dependency = (Dependency) iterator.next();
            Object supplier = dependency.getSupplier().iterator().next();
            if (metaObject.equals(supplier))
                supplierDependencies.add(dependency);
        }
        return supplierDependencies;
    }

    /**
     * Returns a collection of all the action states in the activity graphs for the use-case context.
     */
    private Collection getPossibleActionStates()
    {
        final Collection graphs = new LinkedList();

        // collect all the graphs
        final Collection servlets = getFormBean().getServlets();
        for (Iterator iterator = servlets.iterator(); iterator.hasNext();)
        {
            StrutsServletDecorator servlet = (StrutsServletDecorator) iterator.next();
            graphs.addAll(servlet.getUseCase().metaObject.getOwnedElement());
        }

        // collect all the action states from these graphs
        final Collection actionStates = new LinkedList();
        for (Iterator iterator = graphs.iterator(); iterator.hasNext();)
        {
            ModelElement modelElement = (ModelElement) iterator.next();
            if (modelElement instanceof ActivityGraph)
            {
                ActivityGraphDecorator activityGraph = (ActivityGraphDecorator) decoratedElement(modelElement);
                actionStates.addAll(activityGraph.getActionStates());
            }
        }
        return actionStates;
    }

}
