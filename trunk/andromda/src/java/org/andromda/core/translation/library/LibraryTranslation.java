package org.andromda.core.translation.library;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.translation.Translator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The LibraryTranslation object which is the intermediary object between the Library and the child Translation
 * instances.
 *
 * @author Chad Brandon
 */
public class LibraryTranslation
{
    private static Logger logger = Logger.getLogger(LibraryTranslation.class);

    /**
     * The parent library to which this LibraryTranslation belongs.
     */
    private Library library;

    /**
     * After processing by the CartridgeTemplate engine, will contain the processed translation.
     */
    private Translation translation;

    /**
     * The name of this library translation instance.
     */
    private String name;

    /**
     * Gets the name of this LibraryTranslation.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The path to the template.
     */
    private String template;

    /**
     * Gets the path to the template for this instance.
     *
     * @return String
     */
    public String getTemplate()
    {
        return template;
    }

    /**
     * Sets the path to the template.
     *
     * @param template
     */
    public void setTemplate(String template)
    {
        this.template = template;
    }

    /**
     * Returns the Library that this LibraryTranslation belongs too.
     *
     * @return Library
     */
    public Library getLibrary()
    {
        return library;
    }

    /**
     * Sets the {@link Library} to which this LibraryInstance belongs.
     *
     * @param library
     */
    public void setLibrary(Library library)
    {
        this.library = library;
    }

    /**
     * The name given to the variable containing the context element.
     */
    private String variable;

    /**
     * Gets the variable name which is made available to the translation template.
     *
     * @return the variable name.
     */
    public String getVariable()
    {
        return this.variable;
    }

    /**
     * Sets the variable name which is made available to the translation template.
     *
     * @param variable the variable name.
     */
    public void setVariable(String variable)
    {
        this.variable = variable;
    }

    /**
     * The Translator implementation to use. This is required.
     */
    private String translatorClass;

    /**
     * Sets the Translator class that will perform the translation processing.
     *
     * @param translatorClass the class of the translator.
     */
    public void setTranslator(String translatorClass)
    {
        this.translatorClass = translatorClass;
        ComponentContainer.instance().unregisterComponent(translatorClass);
        ComponentContainer.instance().registerComponentType(translatorClass);
    }

    /**
     * Gets the Translator instance that will perform processing of the template.
     *
     * @return Translator
     */
    public Translator getTranslator()
    {
        final String methodName = "LibraryTranslation.getTranslator";
        Translator translator = (Translator) ComponentContainer.instance().findComponent(this.translatorClass,
                Translator.class);
        if (translator == null)
        {
            throw new LibraryException(methodName + " - a translator implementation must be defined, " + " please check your translator library --> '" + this.library.getResource() + "'");
        }
        return translator;
    }

    /**
     * Calls the handlerMethod from a translation fragment. Each handle method must take a java.lang.String as the first
     * argument (the body of the fragment from the translation template) and a java.lang.Object for the second argument
     * (the node being parsed that we may need to retrieve any additional information from).
     *
     * @param name the name of the fragment to retrieve.
     * @param node the node Object which from the parsed expression.
     * @param kind the kind of the translation fragment to handle.
     */
    public void handleTranslationFragment(String name, String kind, Object node)
    {
        final String methodName = "LibraryTranslation.handleTranslationFragment";
        ExceptionUtils.checkNull(methodName, "node", node);
        if (this.translation != null && this.getTranslator() != null)
        {
            String translation = this.getTranslationFragment(name, kind);
            Fragment fragment = this.translation.getFragment(name);
            if (fragment != null)
            {
                String handlerMethod = fragment.getHandlerMethod();
                if (StringUtils.isNotEmpty(handlerMethod))
                {
                    Class[] argTypes = new Class[]{java.lang.String.class, java.lang.Object.class};

                    try
                    {

                        Method method = this.getTranslator().getClass().getMethod(handlerMethod, argTypes);

                        // add the translation as the first arg
                        Object[] args = new Object[]{translation, node};

                        method.invoke(this.getTranslator(), args);
                    }
                    catch (NoSuchMethodException ex)
                    {
                        String errMsg = "the translator '" + this.getTranslator().getClass() + "' must implement the method '" + handlerMethod + "'" + StringUtils.join(
                                argTypes, ",") + "'" + " in order to handle processing of the fragment --> '" + name + "'";
                        logger.error(errMsg);
                    }
                    catch (Exception ex)
                    {
                        String errMsg = "Error performing " + methodName;
                        throw new LibraryException(errMsg, ex);
                    }
                }
            }
        }
    }

    /**
     * Gets the current "translated" value of this fragmentName for resulting from the last processTranslation method
     *
     * @param name the name of the fragment to retrieve.
     * @param kind the kind or type of fragment to retrieve (this is the based on the expression type: body, inv, post,
     *             pre, etc).
     * @return String the value of the translated fragment or null of one wasn't found with the specified name.
     */
    public String getTranslationFragment(String name, String kind)
    {
        String fragment = null;
        if (this.translation != null)
        {
            fragment = this.translation.getTranslated(name, kind);
        }
        return fragment;
    }

    /**
     * The processed translation template as a Reader.
     *
     * @param translationInput
     */
    protected void setTranslation(Reader translationInput)
    {
        final String methodName = "LibraryTranslation.setTranslation";
        ExceptionUtils.checkNull(methodName, "translationInput", translationInput);
        try
        {
            this.translation = (Translation) XmlObjectFactory.getInstance(Translation.class).getObject(
                    translationInput);
            this.translation.setLibraryTranslation(this);
        }
        catch (Exception ex)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, ex);
            throw new LibraryException(errMsg, ex);
        }
    }

    /**
     * Processes the template belonging to this LibraryTranslation and returns the Translation objects. If
     * <code>template</code> hasn't been set (i.e. is null, then this method won't do anything but return a null
     * value).
     *
     * @param templateContext any key/value pairs that should be passed to the TemplateEngine while processing the
     *                        translation template.
     * @return Translation the Translation created from the processing the translation template.
     */
    public Translation processTranslation(Map templateContext)
    {
        final String methodName = "LibraryTranslation.processTranslation";
        logger.debug("processing translation template --> '" + this.getTemplate() + "'" + "' with templateContext --> '" + templateContext + "'");
        if (this.getTemplate() != null)
        {
            if (templateContext == null)
            {
                templateContext = new HashMap();
            }
            this.getLibrary().populateTemplateContext(templateContext);

            try
            {
                TemplateEngine engine = this.getLibrary().getTemplateEngine();

                StringWriter output = new StringWriter();
                engine.processTemplate(this.getTemplate(), templateContext, output);
                String outputString = output.toString();
                BufferedReader input = new BufferedReader(new StringReader(outputString));
                if (logger.isDebugEnabled())
                {
                    logger.debug("processed output --> '" + outputString + "'");
                }
                // load Reader into the translation
                this.setTranslation(input);
            }
            catch (Exception ex)
            {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, ex);
                throw new LibraryException(errMsg, ex);
            }
        }
        return this.translation;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}