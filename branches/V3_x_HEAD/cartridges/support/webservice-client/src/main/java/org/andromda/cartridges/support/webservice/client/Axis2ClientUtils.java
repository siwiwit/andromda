package org.andromda.cartridges.support.webservice.client;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.Base64;
import org.apache.axis2.databinding.typemapping.SimpleTypeMapper;
import org.apache.axis2.engine.ObjectSupplier;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities for Axis2 clients.
 *
 * @author Chad Brandon
 */
public class Axis2ClientUtils
{
    /**
     * Gets the appropriate OM element for the given method
     * @param definition the WSDL definition.
     * @param method the method corresponding to the OMElement to create.
     * @param arguments the arguments to pass to the method.
     * @return the constructed OMElement
     */
    public static OMElement getOperationOMElement(
        final Definition definition,
        final Method method,
        final Object[] arguments)
    {
        final Class serviceClass = method.getDeclaringClass();
        OMElement operationOMElement = null;
        try
        {
            final String operationName = method.getName();
            final String serviceName = serviceClass.getSimpleName();
            final Element operationElement = getElementByAttribute(
                    definition,
                    NAME,
                    operationName);
            final Schema operationSchema = Axis2ClientUtils.getElementSchemaByAttribute(
                    definition,
                    NAME,
                    operationName);
            operationOMElement =
                getOMElement(
                    definition,
                    operationSchema,
                    null,
                    null,
                    operationName);
            if (operationElement == null)
            {
                throw new RuntimeException("No operation with name '" + operationName + "' can be found on service: " +
                    serviceName);
            }
            final List argumentElements = Axis2ClientUtils.getElementsWithAttribute(
                    operationElement,
                    TYPE);
            final Class[] parameterTypes = method.getParameterTypes();
            if (argumentElements.size() != arguments.length)
            {
                throw new RuntimeException("Operation: " + operationName + " takes " + parameterTypes.length +
                    " argument(s), and 'arguments' only contains: " + arguments.length);
            }

            // - declare all the namespaces
            final Schema[] schemas = getSchemas(definition);
            for (int ctr = 0; ctr < schemas.length; ctr++)
            {
                final Schema schema = schemas[ctr];
                final String namespace = Axis2ClientUtils.getTargetNamespace(schema);
                final String prefix = getNamespacePrefix(
                        definition,
                        namespace);
                operationOMElement.declareNamespace(
                    namespace,
                    prefix);
            }

            // - add the argument children
            for (int ctr = 0; ctr < argumentElements.size(); ctr++)
            {
                final Element argument = (Element)argumentElements.get(ctr);
                final String argumentName = getAttributeValue(
                        argument,
                        NAME);
                final OMElement element = getOMElement(
                        definition,
                        operationSchema,
                        argument,
                        arguments[ctr],
                        argumentName);
                operationOMElement.addChild(element);
            }
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
        return operationOMElement;
    }

    /**
     * Constructs and OMElement from the given bean
     *
     * @param definition the WSDL definition
     * @param schema the current schema from which to retrieve the om element.
     * @param componentElement, the current componentElemnet of the WSDL definition.
     * @param bean the bean to introspect
     * @param namespace the namespace to add to each element.
     */
    public static OMElement getOMElement(
        final Definition definition,
        final Schema schema,
        final Element componentElement,
        final Object bean,
        final String elementName)
    {
        final OMFactory factory = OMAbstractFactory.getOMFactory();
        return getOMElement(
            definition,
            schema,
            componentElement,
            bean,
            elementName,
            factory,
            new ArrayList<Object>());
    }

    /**
     * Constructs and OMElement from the given bean
     *
     * @param definition the WSDL definition
     * @param schema the current schema from which to retrieve the om element.
     * @param componentElement, the current componentElemnet of the WSDL definition.
     * @param bean the bean to introspect
     * @param elementName the name of the element
     * @param factory the SOAP factory instance used to create the OMElement
     * @param evaluatingBeans the collection in which to keep the beans that are evaluating in order
     *        to prevent endless recursion.
     */
    private static OMElement getOMElement(
        final Definition definition,
        Schema schema,
        final Element componentElement,
        final Object bean,
        final String elementName,
        final OMFactory factory,
        final Collection<Object> evaluatingBeans)
    {
        final String componentElementName = componentElement != null ? componentElement.getAttribute(NAME) : null;
        if (StringUtils.isNotBlank(componentElementName))
        {
            final Schema currentSchema = Axis2ClientUtils.getElementSchemaByAttribute(
                    definition,
                    NAME,
                    componentElementName);
            if (currentSchema != null)
            {
                schema = currentSchema;
            }
        }
        final boolean qualified = isQualified(schema);
        final String namespace = Axis2ClientUtils.getTargetNamespace(schema);
        final String namespacePrefix = getNamespacePrefix(
                definition,
                namespace);

        final OMElement omElement =
            factory.createOMElement(
                elementName,
                qualified ? factory.createOMNamespace(
                    namespace,
                    namespacePrefix) : null);
        if (bean != null && evaluatingBeans != null && !evaluatingBeans.contains(bean))
        {
            evaluatingBeans.add(bean);
            final Element currentComponentElement =
                Axis2ClientUtils.getElementByAttribute(
                    definition,
                    NAME,
                    bean.getClass().getSimpleName());
            if (isSimpleType(bean))
            {
                omElement.addChild(factory.createOMText(SimpleTypeMapper.getStringValue(bean)));
            }
            else if (bean instanceof byte[])
            {
                omElement.addChild(factory.createOMText(Base64.encode((byte[])bean)));
            }
            else
            {
                final Class beanType = bean.getClass();
                if (beanType.isArray())
                {
                    final Element arrayElement = Axis2ClientUtils.getElementByAttribute(
                            componentElement,
                            NAME,
                            elementName);
                    final Element arrayTypeElement =
                        Axis2ClientUtils.getElementByAttribute(
                            definition,
                            NAME,
                            stripPrefix(arrayElement.getAttribute(TYPE)));
                    final String arrayComponentName = Axis2ClientUtils.getAttributeValueFromChildElement(
                            arrayTypeElement,
                            NAME,
                            0);
                    for (int ctr = 0; ctr < Array.getLength(bean); ctr++)
                    {
                        omElement.addChild(
                            getOMElement(
                                definition,
                                schema,
                                currentComponentElement,
                                Array.get(
                                    bean,
                                    ctr),
                                arrayComponentName,
                                factory,
                                evaluatingBeans));
                    }
                }
                try
                {
                    final java.util.Map properties = PropertyUtils.describe(bean);
                    for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
                    {
                        final String name = (String)iterator.next();
                        if (!CLASS.equals(name))
                        {
                            final Object value = properties.get(name);
                            if (value != null)
                            {
                                boolean addToDocument = true;
                                if (value.getClass().isArray())
                                {
                                    addToDocument = Array.getLength(value) > 0;
                                }
                                if (addToDocument)
                                {
                                    omElement.addChild(
                                        getOMElement(
                                            definition,
                                            schema,
                                            currentComponentElement,
                                            value,
                                            name,
                                            factory,
                                            evaluatingBeans));
                                }
                            }
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throwable.printStackTrace();
                    throw new RuntimeException(throwable);
                }
            }
            evaluatingBeans.remove(bean);
        }
        return omElement;
    }

    private static final String ELEMENT_FORM_DEFAULT = "elementFormDefault";
    private static final String QUALIFIED = "qualified";
    private static final String TARGET_NAMESPACE = "targetNamespace";

    private static boolean isQualified(Schema schema)
    {
        final String qualified = Axis2ClientUtils.getAttributeValue(
                schema.getElement(),
                ELEMENT_FORM_DEFAULT);
        return QUALIFIED.equalsIgnoreCase(qualified);
    }

    private static String getTargetNamespace(Schema schema)
    {
        return Axis2ClientUtils.getAttributeValue(
            schema.getElement(),
            TARGET_NAMESPACE);
    }

    private static String stripPrefix(String name)
    {
        return name.replaceAll(
            ".*:",
            "");
    }

    private static String getNamespacePrefix(
        final Definition definition,
        final String namespace)
    {
        String prefix = null;
        final Map namespaces = definition.getNamespaces();
        for (final Iterator iterator = namespaces.entrySet().iterator(); iterator.hasNext();)
        {
            final Map.Entry entry = (Map.Entry)iterator.next();
            if (entry.getValue().equals(namespace))
            {
                prefix = (String)entry.getKey();
            }
        }
        return prefix;
    }

    private static final String NAME = "name";
    private static final String TYPE = "type";

    private static Element getElementByAttribute(
        final Definition definition,
        final String attribute,
        final String value)
    {
        Element element = null;
        if (value != null)
        {
            final Types types = definition.getTypes();
            final Collection elements = types.getExtensibilityElements();
            for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                if (object instanceof Schema)
                {
                    final Schema schema = (Schema)object;
                    element =
                        getElementByAttribute(
                            schema.getElement(),
                            attribute,
                            value);
                    if (element != null)
                    {
                        break;
                    }
                }
            }
        }
        return element;
    }

    private static Schema[] getSchemas(final Definition definition)
    {
        final Collection<Schema> schemas = new ArrayList<Schema>();
        final Types types = definition.getTypes();
        final Collection elements = types.getExtensibilityElements();
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof Schema)
            {
                schemas.add((Schema)object);
            }
        }
        return schemas.toArray(new Schema[0]);
    }

    private static Element getElementByAttribute(
        final Schema schema,
        final String attribute,
        final String value)
    {
        return getElementByAttribute(
            schema.getElement(),
            attribute,
            value);
    }

    /**
     * Gets the schema that owns the element that has the given attribute with the given value.
     *
     * @param definition the WSDL definition.
     * @param attribute the name of the attribute to find.
     * @param value the value of the attribute.
     * @return the schema
     */
    private static Schema getElementSchemaByAttribute(
        final Definition definition,
        final String attribute,
        final String value)
    {
        final Types types = definition.getTypes();
        final Collection elements = types.getExtensibilityElements();
        Schema schema = null;
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof Schema)
            {
                final Element element = getElementByAttribute(
                        (Schema)object,
                        attribute,
                        value);
                if (element != null)
                {
                    schema = (Schema)object;
                    break;
                }
            }
        }
        return schema;
    }

    private static Element getElementByAttribute(
        final Element element,
        final String attribute,
        final String value)
    {
        Element found = null;
        if (element != null && value != null)
        {
            final String foundAttribute = element.getAttribute(attribute);
            if (value.equals(foundAttribute))
            {
                found = element;
            }
            else
            {
                final NodeList nodes = element.getChildNodes();
                for (int ctr = 0; ctr < nodes.getLength(); ctr++)
                {
                    final Node node = nodes.item(ctr);
                    if (node instanceof Element)
                    {
                        found =
                            getElementByAttribute(
                                (Element)node,
                                attribute,
                                value);
                        if (found != null)
                        {
                            break;
                        }
                    }
                }
            }
        }
        return found;
    }

    private static List<Element> getElementsWithAttribute(
        final Element element,
        final String attribute)
    {
        final List<Element> found = new ArrayList<Element>();
        if (element != null)
        {
            final String foundAttribute = element.getAttribute(attribute);
            if (StringUtils.isNotBlank(foundAttribute))
            {
                found.add(element);
            }
            final NodeList nodes = element.getChildNodes();
            for (int ctr = 0; ctr < nodes.getLength(); ctr++)
            {
                final Node node = nodes.item(ctr);
                if (node instanceof Element)
                {
                    found.addAll(getElementsWithAttribute(
                            (Element)node,
                            attribute));
                }
            }
        }
        return found;
    }

    private static String getAttributeValue(
        final Element element,
        final String attribute)
    {
        String value = null;
        Element found = getElementWithAttribute(
                element,
                attribute);
        if (found != null)
        {
            value = found.getAttribute(attribute);
        }
        return value;
    }

    private static String getAttributeValueFromChildElement(
        final Element element,
        final String attribute,
        int childIndex)
    {
        String value = null;
        if (element != null)
        {
            int elementIndex = 0;
            for (int ctr = 0; ctr < element.getChildNodes().getLength(); ctr++)
            {
                final Node node = element.getChildNodes().item(ctr);
                if (node instanceof Element)
                {
                    if (elementIndex == childIndex)
                    {
                        value =
                            getAttributeValue(
                                (Element)node,
                                attribute);
                    }
                    elementIndex++;
                }
            }
        }
        return value;
    }

    /**
     * Finds the first child element of the given <code>element</code> that has an attribute
     * matching the name of <code>attribute</code>.
     *
     * @param element the element to search.
     * @param attribute the name of the attribute to get.
     * @return the found element or null if not found.
     */
    private static Element getElementWithAttribute(
        final Element element,
        final String attribute)
    {
        Element found = null;
        if (element != null)
        {
            final NodeList nodes = element.getChildNodes();
            final String foundAttribute = element.getAttribute(attribute);
            if (StringUtils.isNotBlank(foundAttribute))
            {
                found = element;
            }
            if (found == null)
            {
                for (int ctr = 0; ctr < nodes.getLength(); ctr++)
                {
                    final Node node = nodes.item(ctr);
                    if (node instanceof Element)
                    {
                        found =
                            getElementWithAttribute(
                                (Element)node,
                                attribute);
                    }
                }
            }
        }
        return found;
    }

    private static final String CLASS = "class";

    /**
     * Deserializes the given <code>element</code> to the given <code>type</code>.
     *
     * @param element the XML OMElement
     * @param type the java type.
     * @param objectSupplier the "object supplier" used to construct objects from given classes.
     * @return the deserialized object.
     * @throws Exception
     */
    public static Object deserialize(
        OMElement element,
        final Class type,
        final ObjectSupplier objectSupplier) throws Exception
    {
        Object bean = null;
        if (isSimpleType(type))
        {
            bean =
                getSimpleTypeObject(
                    type,
                    element);
        }
        else if (type == byte[].class)
        {
            bean = Base64.decode(element.getText());
        }
        else
        {
            if (type.isArray())
            {
                final Collection<Object> elements = new ArrayList<Object>();
                for (final Iterator iterator = element.getChildElements(); iterator.hasNext();)
                {
                    OMElement omElement = (OMElement)iterator.next();
                    elements.add(deserialize(
                            omElement,
                            type.getComponentType(),
                            objectSupplier));
                }
                bean =
                    elements.toArray((Object[])Array.newInstance(
                            type.getComponentType(),
                            0));
            }
            else
            {
                try
                {
                    bean = objectSupplier.getObject(type);
                    final java.beans.PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(type);
                    for (int ctr = 0; ctr < descriptors.length; ctr++)
                    {
                        final java.beans.PropertyDescriptor descriptor = descriptors[ctr];
                        final String name = descriptor.getName();
                        if (!CLASS.equals(name))
                        {
                            final OMElement propertyElement = findElementByName(
                                    element,
                                    name);

                            if (propertyElement != null)
                            {
                                PropertyUtils.setProperty(
                                    bean,
                                    name,
                                    deserialize(
                                        propertyElement,
                                        descriptor.getPropertyType(),
                                        objectSupplier));
                            }
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new RuntimeException(throwable);
                }
            }
        }
        return bean;
    }

    /**
     * Finds an element having the given <code>name</code> from the child elements on the given
     * <code>element</code>.
     *
     * @param element the element to search.
     * @param name the name of the element to find.
     * @return the found element or null if one couldn't be found.
     */
    private static OMElement findElementByName(
        final OMElement element,
        final String name)
    {
        OMElement found = null;
        for (final Iterator iterator = element.getChildElements(); iterator.hasNext();)
        {
            final OMElement child = (OMElement)iterator.next();
            if (child.getLocalName().equals(name))
            {
                found = child;
                break;
            }
        }
        return found;
    }

    /**
     * First delegate to the Axis2 simple type mapper, if that says
     * its simple, it is, otherwise check to see if the type is an enumeration (typesafe
     * or Java5 version).
     *
     * @param bean the bean to check.
     * @return true/false
     */
    private static boolean isSimpleType(final Object bean)
    {
        return isSimpleType(bean != null ? bean.getClass() : null);
    }

    /**
     * First delegate to the Axis2 simple type mapper, if that says
     * its simple, it is, otherwise check to see if the type is an enumeration (typesafe
     * or Java5 version).
     *
     * @param type the type to check.
     * @return true/false
     */
    private static boolean isSimpleType(final Class type)
    {
        return java.util.Calendar.class.isAssignableFrom(type) ||
               java.util.Date.class.isAssignableFrom(type) ||
               SimpleTypeMapper.isSimpleType(type) ||
               isEnumeration(type);
    }

    private static final String VALUE_OF = "valueOf";

    /**
     * Constructs a type object for the given type and element.
     *
     * @param type the class type to construct.
     * @param element the element containing the value.
     * @return the constructed object or null if one couldn't be constructed.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Object getSimpleTypeObject(Class type, OMElement element)
        throws Exception
    {
        Object object = SimpleTypeMapper.getSimpleTypeObject(type, element);
        if (object == null && type != null && element != null)
        {
            if (type.isEnum())
            {
                object = type.getMethod(
                    VALUE_OF, new Class[]{java.lang.String.class}).invoke(
                        type, element.getText());
            }
            else
            {
                final Method fromMethod = getEnumerationFromMethod(type);
                if (fromMethod != null)
                {
                    object = fromMethod.invoke(type, new Object[]{element.getText()});
                }
            }
        }
        return object;
    }

    private static final String FROM = "from";

    /**
     * Indicates whether or not the given type represents an enumeration.
     *
     * @param type the type to check.
     * @return true/false
     */
    private static boolean isEnumeration(final Class type)
    {
        return isEnumeration(type, getEnumerationFromMethod(type));
    }

    /**
     * Indicates whether or not the given type represents an enumeration by checking
     * whether the type is an actual "enum" class or the "fromMethod" is not null.
     *
     * @param type the type to check.
     * @param the "from" method used to construct a typesafe enumeration from it's simple type.
     * @return true/false
     */
    private static boolean isEnumeration(final Class type, final Method fromMethod)
    {
        boolean enumeration = false;
        if (type != null)
        {
            enumeration = type.isEnum();
            if (!enumeration)
            {
                enumeration = fromMethod != null;
            }
        }
        return enumeration;
    }

    /**
     * Gets the "from" method for a type safe enumeration.
     *
     * @param type the type.
     * @return the "from" method (i.e. fromString, etc).
     */
    private static Method getEnumerationFromMethod(final Class type)
    {
        Method fromMethod = null;
        if (type != null)
        {
            // - check for the typesafe enum pattern
            for (final Method method : type.getMethods())
            {
                if (method.getName().startsWith(FROM))
                {
                    final Class[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1)
                    {
                        final Class parameterType = parameterTypes[0];
                        if (method.getName().equals(FROM + parameterType.getSimpleName()))
                        {
                            fromMethod = method;
                            break;
                        }
                    }
                }
            }
        }
        return fromMethod;
    }
}