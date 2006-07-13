package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.Association;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Feature;
import org.eclipse.uml2.Generalization;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.LiteralInteger;
import org.eclipse.uml2.LiteralString;
import org.eclipse.uml2.LiteralUnlimitedNatural;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.MultiplicityElement;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.TypedElement;
import org.eclipse.uml2.UML2Package;
import org.eclipse.uml2.ValueSpecification;
import org.eclipse.uml2.util.UML2Resource;


/**
 * Contains utilities for the Eclipse/UML2 metafacades.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class UmlUtilities
{
    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(UmlUtilities.class);

    /**
     * A transformer which transforms each uml2 properties in a attribute or an
     * association end.
     */
    protected static final Transformer PropertyTransformer =
        new Transformer()
        {
            public Object transform(Object element)
            {
                if (element instanceof Property)
                {
                    Property property = (Property)element;
                    if (property instanceof AssociationEnd || property instanceof Attribute)
                    {
                        return property;
                    }
                    if (property.getAssociation() != null)
                    {
                        return new AssociationEndImpl(property);
                    }

                    return new AttributeImpl(property);
                }

                return element;
            }
        };

    /**
     * List all meta objects instances of a given meta class It's a way to
     * achieve refAllOfType method in a JMI implementation. Please take care of the
     * fact that properties are not transformed here.
     *
     * @param metaClass
     *            The meta class we're looking for its instances
     * @param model
     *            The model where we're searching
     * @return a list of objects owned by model, instance of metaClass
     */
    public static List getAllMetaObjectsInstanceOf(
        final java.lang.Class metaClass,
        final Model model)
    {
        LinkedList metaObjects = new LinkedList();
        for (Iterator it = model.eAllContents(); it.hasNext();)
        {
            Object metaObject = it.next();
            if (metaClass.isInstance(metaObject))
            {
                metaObjects.add(metaObject);
            }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("getAllMetaObjectsInstanceOf class: " + metaClass + ". Found: " + metaObjects.size());
        }
        return metaObjects;
    }

    /**
     * Get the comments for a UML Element. This will be a string with each
     * comment separated by a 2 newlines.
     *
     * @param element
     * @return concatenated string
     */
    public static String getComment(final Element element)
    {
        String commentString = "";
        EList comments = element.getOwnedComments();

        for (Iterator iterator = comments.iterator(); iterator.hasNext();)
        {
            final Comment comment = (Comment)iterator.next();
            if (!commentString.equalsIgnoreCase(""))
            {
                commentString = commentString + "\n\n";
            }
            commentString = commentString.concat(comment.getBody());
        }
        return cleanText(commentString);
    }

    /**
     * Gets read of all excess whitespace.
     *
     * @param text
     *            the text from which to remove the white space.
     * @return the cleaned text.
     */
    public static String cleanText(String text)
    {
        text = text.replaceAll(
                "[\\t\\n]*",
                "");
        text = text.replaceAll(
                "\\s+",
                " ");

        return text;
    }

    /**
     * Retrieves the name of the type for the given <code>element</code>.
     *
     * @deprecated - it is not used. Do we need to keep it ?
     * @param element
     *            the element for which to retrieve the type.
     * @return the type name.
     */
    public static String getType(final TypedElement element)
    {
        final Type elementType = element.getType();
        String type = elementType.getName();
        if (type != null && type.trim().length() > 0)
        {
            if (element instanceof MultiplicityElement)
            {
                MultiplicityElement multiplicity = (MultiplicityElement)element;
                if (multiplicity.isMultivalued())
                {
                    type = type + "[";
                    if (multiplicity.getLower() > 0)
                    {
                        type = type + multiplicity.getLower() + "..";
                    }
                    if (multiplicity.getUpper() == -1)
                    {
                        type = type + "*]";
                    }
                    else
                    {
                        type = type + multiplicity.getUpper() + "]";
                    }
                }
            }
        }
        return type;
    }

    /**
     * Return the Superclass for this class/interface. If more than one
     * superclass is defined an error will be logged to the console.
     *
     * @param classifier
     *            the classifier instance.
     * @return the super class or null if it could not be found.
     */
    public static Classifier getSuperclass(final Classifier classifier)
    {
        final List superClasses = classifier.getGenerals();
        if (superClasses.size() > 1)
        {
            throw new MetafacadeException("Error: found " + superClasses.size() + " generalizations for " +
                classifier.getQualifiedName());
        }
        Classifier superClass = null;
        if (superClasses.size() == 1)
        {
            final Object generalization = superClasses.get(0);
            if (generalization instanceof Classifier)
            {
                superClass = (Classifier)generalization;
            }
            else
            {
                throw new MetafacadeException("Error: generalization for " + classifier.getQualifiedName() + " is a " +
                    superClass.getClass().getName());
            }
        }
        return superClass;
    }

    /**
     * Gets a collection containing all of the attributes for this
     * class/interface. Superclass properties will included if
     * <code>follow</code> is true. Overridden properties will be omitted.
     *
     * @param umlClassifer
     *            the UML class instance from which to retrieve all properties
     * @param follow
     *            whether or not the inheritance hierarchy should be followed
     * @return all retrieved attributes.
     */
    public static List getAttributes(
        final Classifier umlClassifier,
        final boolean follow)
    {
        List attributes = new ArrayList();
        List candidates = new ArrayList();
        candidates.addAll(umlClassifier.getOwnedMembers());
        if (follow)
        {
            candidates.addAll(umlClassifier.getInheritedMembers());
        }
        for (Iterator it = candidates.iterator(); it.hasNext();)
        {
            Object nextCandidate = it.next();
            if (nextCandidate instanceof Property)
            {
                Property property = (Property)nextCandidate;

                if (property.getAssociation() == null)
                {
                    // property is valid. Add it.
                    if (isRedefinitionContainedIn(
                            property,
                            attributes))
                    {
                        attributes.add(property);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("Attribute found for " + umlClassifier.getName() + ": " + property.getName());
                        }
                    }
                }
            }
        }

        CollectionUtils.transform(
            attributes,
            PropertyTransformer);
        return attributes;
    }

    /**
     * UML 2 has its own way to describe redefinition and inheritance But for
     * Java code generation, names must be "unique"
     *
     * @param feature
     * @param collection
     * @return
     */
    private static boolean isRedefinitionContainedIn(
        final Feature feature,
        final List collection)
    {
        boolean valid = true;
        for (Iterator it = collection.iterator(); it.hasNext() && valid;)
        {
            Property concurrent = (Property)it.next();
            if (concurrent.getName().equals(feature.getName()))
            {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Gets a collection containing all of the associationEnds for this
     * class/interface. Superclass properties will included if
     * <code>follow</code> is true. Overridden properties will be omitted.
     *
     * cejeanne: Changed the way association end are found.
     *
     * @param umlClassiifer
     *            the UML class instance from which to retrieve all properties
     * @param follow
     *            whether or not the inheritance hierarchy should be followed
     * @return all retrieved attributes.
     */
    public static List getAssociationEnds(
        final Classifier umlClassifier,
        final boolean follow)
    {
        List associationEnds = new ArrayList();
        List allProperties = getAllMetaObjectsInstanceOf(
                Property.class,
                umlClassifier.getModel());
        for (Iterator it = allProperties.iterator(); it.hasNext();)
        {
            Property property = (Property)it.next();
            if (property.getAssociation() != null)
            {
                boolean concernUmlClassifier = false;

                // okay, it's an association end
                if (property.getType().equals(umlClassifier))
                {
                    concernUmlClassifier = true;
                }
                else if (follow)
                {
                    // check about superclasses
                    for (Classifier superClass = getSuperclass(umlClassifier);
                        superClass != null && (!concernUmlClassifier); superClass = getSuperclass(superClass))
                    {
                        if (property.getType().equals(superClass))
                        {
                            concernUmlClassifier = true;
                        }
                    }
                }
                if (concernUmlClassifier)
                {
                    // property is valid. Add it.
                    if (isRedefinitionContainedIn(
                            property,
                            associationEnds))
                    {
                        associationEnds.add(property);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(
                                "AssociationEnd found for " + umlClassifier.getName() + ": " + property.getName());
                        }
                    }
                }
            }
        }

        CollectionUtils.transform(
            associationEnds,
            PropertyTransformer);
        return associationEnds;
    }

    /**
     * Attempts to retrieve the attribute for the given
     * <code>umlClass/umlInterface</code> having the given <code>name</code>.
     *
     * @deprecated - it's not used any more. Do we need to keep it ?
     * @param umlClassifier
     *            the name of the UML class.
     * @param name
     *            the name of the attribute.
     * @return the attribute or null if not found.
     */
    public static Property getAttribute(
        final Classifier umlClassifier,
        final String name)
    {
        Property attribute = null;
        final Collection properties = getAttributes(
                umlClassifier,
                true);
        for (final Iterator iterator = properties.iterator(); iterator.hasNext();)
        {
            final Property property = (Property)iterator.next();
            if (property.getAssociation() == null && property.getName().equalsIgnoreCase(name))
            {
                attribute = property;
                break;
            }
        }
        return attribute;
    }

    /**
     * Return a collection containing all of the methods (UML operations) for
     * this class/interface. Superclass properties will included if
     * <code>follow</code> is true. Overridden methods will be omitted.
     *
     *
     * @param umlClassifier
     *            the UML class instance.
     * @param follow
     *            whether or not to follow the inheritance hierarchy
     * @return the collection of operations.
     */
    public static Collection getOperations(
        final Classifier umlClassifier,
        final boolean follow)
    {
        ArrayList classOperations = new ArrayList();

        // - first add all the class properties
        if (!(umlClassifier == null))
        {
            Iterator local = null;
            Iterator other = null;
            if (umlClassifier instanceof Class)
            {
                Class ca = (Class)umlClassifier;
                local = ca.getOwnedOperations().iterator();
                other = ca.getInheritedMembers().iterator();
            }

            if (umlClassifier instanceof Interface)
            {
                Interface intfc = (Interface)umlClassifier;
                local = intfc.getOwnedOperations().iterator();
                other = intfc.getInheritedMembers().iterator();
            }

            while (local.hasNext())
            {
                classOperations.add(local.next());
            }
            if (follow)
            {
                // then interate through all the inherited members adding those
                // not already
                // defined (ie overridden)
                while (other.hasNext())
                {
                    NamedElement el = (NamedElement)other.next();
                    if (el instanceof Operation)
                    {
                        Iterator classIter = classOperations.iterator();
                        boolean defined = false;
                        while (classIter.hasNext())
                        {
                            Operation testMeth = (Operation)classIter.next();
                            if (testMeth.getName().equalsIgnoreCase(el.getName()))
                            {
                                defined = true;
                            }
                        }
                        if (!defined)
                        {
                            classOperations.add(el);
                        }
                    }
                }
            }
        }
        return classOperations;
    }

    /**
     * Gets the operation with the given <code>name</code> for the given
     * <code>umlClass</code>.
     *
     * @deprecated - It's not used any more.
     * @param umlClass
     *            the UML class instance.
     * @param name
     *            the name of the operation.
     * @return the operation instance or null
     */
    public static Operation getOperation(
        final Classifier umlClass,
        final String name)
    {
        Operation foundOperation = null;
        final Collection operations = getOperations(
                umlClass,
                true);
        for (final Iterator iterator = operations.iterator(); iterator.hasNext();)
        {
            final Operation operation = (Operation)iterator.next();
            if (operation.getName().equalsIgnoreCase(name))
            {
                foundOperation = operation;
                break;
            }
        }
        return foundOperation;
    }

    /**
     * Retrieves all specializations of the given <code>classifier</code>
     * instance.
     *
     * @param classifier
     *            the classifier from which to retrieve the specializations.
     * @return all specializations.
     */
    public static List getSpecializations(final Classifier classifier)
    {
        final List specials = new ArrayList();
        for (final TreeIterator iterator = EcoreUtil.getRootContainer(classifier).eAllContents(); iterator.hasNext();)
        {
            final EObject object = (EObject)iterator.next();
            if (object instanceof Generalization)
            {
                final Generalization generalization = (Generalization)object;
                if (generalization.getGeneral().equals(classifier))
                {
                    specials.add(generalization.getSpecific());
                }
                iterator.prune();
            }
        }
        return specials;
    }

    /**
     * Retrieves the names of the stereotypes for the given <code>element</code>
     *
     * @param element
     *            the element for which to retrieve the stereotypes.
     * @return all stereotype names
     */
    public static List getStereotypeNames(final Element element)
    {
        final Collection stereotypes = element.getAppliedStereotypes();
        final List names = new ArrayList();
        if (stereotypes != null)
        {
            for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
            {
                final Stereotype stereotype = (Stereotype)iterator.next();
                names.add(stereotype.getName());
            }
        }
        return names;
    }

    /**
     * Indicates whether or not the given <code>element</code> contains a
     * stereotype with the given <code>name</code>.
     *
     * @param element
     *            the element instance.
     * @param name
     *            the name of the element
     * @return true/false
     */
    public static boolean containsStereotype(
        final Element element,
        final String name)
    {
        if (name == null || StringUtils.isEmpty(name))
        {
            return true;
        }
        boolean result = false;
        Collection names = getStereotypeNames(element);
        for (Iterator iterator = names.iterator(); iterator.hasNext();)
        {
            final String stereotypeName = (String)iterator.next();
            if (stereotypeName.equals(name))
            {
                result = true;
            }
        }
        if (logger.isDebugEnabled())
        {
            if (element instanceof NamedElement)
            {
                logger.debug(((NamedElement)element).getQualifiedName() + " has stereotype:" + name + " : " + result);
            }
            else
            {
                logger.debug(element.toString() + " has stereotype:" + name + " : " + result);
            }
        }
        return result;
    }

    /**
     * @deprecated old way to handle tag values
     * Note: The uml profile define it as "AndroMdaTags" and not "AndroMDATags"
     * Stores the tagged values that may be applied to an element.
     */
    private static final String TAGGED_VALUES_STEREOTYPE = "AndroMdaTags";

    /**
     * Retrieves the TagDefinitions for the given element. Note that this is either the TagName/TagValues on the
     *  TAGGED_VALUES_STEREOTYPE stereotype or the values for any Stereotypes that have the value property.
     * @deprecated old way to handle tag values
     * @param element the element from which to retrieve the tagged values.
     * @return the collection of {@TagDefinition} instances.
     */
    public static Collection getAndroMDATags(Element element)
    {
        final Collection tags = new ArrayList();
        final Stereotype tagStereotype = findAppliedStereotype(
                element,
                TAGGED_VALUES_STEREOTYPE);
        if (tagStereotype != null)
        {
            List tagNames = (List)element.getValue(
                    tagStereotype,
                    "TagName");
            List tagValues = (List)element.getValue(
                    tagStereotype,
                    "TagValue");
            for (int ctr = 0; ctr < tagValues.size(); ctr++)
            {
                tags.add(new TagDefinitionImpl(
                        tagNames.get(ctr).toString(),
                        tagValues.get(ctr)));
            }
        }
        for (final Iterator iterator = getStereotypeNames(element).iterator(); iterator.hasNext();)
        {
            final String name = (String)iterator.next();
            if (!name.equalsIgnoreCase(TAGGED_VALUES_STEREOTYPE))
            {
                final Stereotype stereotype = findAppliedStereotype(
                        element,
                        name);
                if (element.hasValue(
                        stereotype,
                        "value"))
                {
                    final Object value = element.getValue(
                            stereotype,
                            "value");
                    tags.add(new TagDefinitionImpl(
                            name,
                            value));
                }
            }
        }
        return tags;
    }

    /**
     * Retrieves the TagDefinitions for the given element.
     *
     * @param element
     *            the element from which to retrieve the tagged values.
     * @return the collection of {@TagDefinition} instances.
     */
    public static Collection getTaggedValue(final NamedElement element)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Searching Tagged Values for " + element.getName());
        }
        final Collection tags = getAndroMDATags(element);
        if (logger.isDebugEnabled())
        {
            logger.debug("AndroMDATags found until here: " + tags.size());
        }
        final Collection stereotypes = element.getAppliedStereotypes();
        for (Iterator stereoIt = stereotypes.iterator(); stereoIt.hasNext();)
        {
            Stereotype stereo = (Stereotype)stereoIt.next();
            if (logger.isDebugEnabled())
            {
                logger.debug("Proceeding stereotype: " + stereo.getName());
            }
            for (Iterator tvIt = getAttributes(
                        stereo,
                        true).iterator(); tvIt.hasNext();)
            {
                String tagName = ((Property)tvIt.next()).getName();
                if (!tagName.startsWith("base$"))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Checking presence of: " + tagName + ": " + element.hasValue(
                                stereo,
                                tagName));
                    }
                    if (element.hasValue(
                            stereo,
                            tagName))
                    {
                        // Restore tag name
                        String androMDATagName = "@" + tagName.replace(
                                '_',
                                '.');

                        // Obtain its value
                        Object tagValue = element.getValue(
                                stereo,
                                tagName);
                        if (tagValue instanceof Collection)
                        {
                            Collection tagValues = (Collection)tagValue;
                            if (!tagValues.isEmpty())
                            {
                                Collection tagValuesInString =
                                    CollectionUtils.collect(
                                        tagValues,
                                        new Transformer()
                                        {
                                            public Object transform(Object object)
                                            {
                                                return object.toString();
                                            }
                                        });
                                TagDefinition tagDefinition = new TagDefinitionImpl(androMDATagName, tagValuesInString);
                                if (logger.isDebugEnabled())
                                {
                                    logger.debug("Tagged Value found: " + tagDefinition);
                                }
                                tags.add(tagDefinition);
                            }
                        }
                        else
                        {
                            TagDefinition tagDefinition = new TagDefinitionImpl(androMDATagName,
                                    tagValue.toString());
                            if (logger.isDebugEnabled())
                            {
                                logger.debug("Tagged Value found: " + tagDefinition);
                            }
                            tags.add(tagDefinition);
                        }
                    }
                }
            }
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("Found " + tags.size() + " tagged values for " + element.getName());
        }

        return tags;
    }

    /**
     * Attempts to find the applied stereotype with the given name on the given
     * <code>element</code>. First tries to find it with the fully qualified
     * name, and then tries it with just the name.
     *
     * @param name
     *            the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findAppliedStereotype(
        final Element element,
        final String name)
    {
        Stereotype foundStereotype = element.getAppliedStereotype(name);
        if (foundStereotype == null)
        {
            final Set stereotypes = element.getAppliedStereotypes();
            if (stereotypes != null)
            {
                for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
                {
                    final Stereotype stereotype = (Stereotype)iterator.next();
                    if (stereotype.getName().equals(name))
                    {
                        foundStereotype = stereotype;
                        break;
                    }
                }
            }
        }
        return foundStereotype;
    }

    /**
     * Attempts to find the applicable stereotype with the given name on the
     * given <code>element</code>. First tries to find it with the fully
     * qualified name, and then tries it with just the name.
     *
     * @param name
     *            the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findApplicableStereotype(
        final Element element,
        final String name)
    {
        Stereotype foundStereotype = element.getApplicableStereotype(name);
        if (foundStereotype == null)
        {
            final Set stereotypes = element.getApplicableStereotypes();
            if (stereotypes != null)
            {
                for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
                {
                    final Stereotype stereotype = (Stereotype)iterator.next();
                    if (stereotype.getName().equals(name))
                    {
                        foundStereotype = stereotype;
                        break;
                    }
                }
            }
        }
        return foundStereotype;
    }

    /**
     * Retrieves the serial version UID by reading the tagged value
     * {@link UMLProfile#TAGGEDVALUE_SERIALVERSION_UID} of the
     * <code>classifier</code>.
     *
     * @param classifier
     *            the classifier to be inspected.
     * @return the serial version UID of the classifier. Returns
     *         <code>null</code> if the tagged value cannot be found.
     */
    final static String getSerialVersionUID(final ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull(
            "classifer",
            classifier);
        String serialVersionString = (String)classifier.findTaggedValue(UMLProfile.TAGGEDVALUE_SERIALVERSION_UID);
        return StringUtils.trimToNull(serialVersionString);
    }

    /**
     * Gets the opposite end of the given <code>associationEnd</code> if the
     * property is indeed an association end, otherwise returns null.
     *
     * @param associationEnd
     *            the association end from which to retrieve the opposite end.
     * @return the opposite association end or null.
     */
    final static AssociationEnd getOppositeAssociationEnd(final Property associationEnd)
    {
        Object opposite = null;
        Association association = associationEnd.getAssociation();

        if (association != null)
        {
            Collection ends = association.getMemberEnds();
            for (final Iterator endIterator = ends.iterator(); endIterator.hasNext();)
            {
                final Object end = endIterator.next();
                if (end != null && !associationEnd.equals(end))
                {
                    opposite = end;
                    break;
                }
            }
        }

        return new AssociationEndImpl((Property)opposite);
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param model
     *            The model to search.
     * @param name
     *            the name of the model element to find.
     * @return the found model element.
     */
    static Object findByPredicate(
        final ResourceSet resourceSet,
        final Predicate pred)
    {
        Object modelElement = null;
        for (final Iterator iterator = resourceSet.getResources().iterator();
            iterator.hasNext() && modelElement == null;)
        {
            final Resource resource = (Resource)iterator.next();
            final Package model =
                (Package)EcoreUtil.getObjectByType(
                    resource.getContents(),
                    UML2Package.eINSTANCE.getPackage());
            if (model != null)
            {
                for (final TreeIterator elementIterator = model.eAllContents();
                    elementIterator.hasNext() && modelElement == null;)
                {
                    final Object object = elementIterator.next();
                    if (pred.evaluate(object))
                    {
                        modelElement = object;
                    }
                }
            }
        }

        return modelElement;
    }

    /**
     * Find the Model of a ressource (UML2 Model)
     */
    public static Model findModel(final UML2Resource resource)
    {
        Model model = (Model)EcoreUtil.getObjectByType(
                resource.getContents(),
                EcorePackage.eINSTANCE.getEObject());
        if (logger.isDebugEnabled())
        {
            logger.debug("Model found: " + model);
        }
        return model;
    }

    /**
     * Constructs the package name for the given <code>metaObject</code>,
     * seperating the package name by the given <code>separator</code>.
     *
     * @param metaObject
     *            the Model Element
     * @param separator
     *            the PSM namespace separator, ignored if <code>modelName</code> is <code>true</code>
     * @param modelName
     *            true/false on whether or not to get the model package name
     *            instead of the PSM package name.
     * @return the package name.
     */
    static String getPackageName(
        final NamedElement metaObject,
        final String separator,
        final boolean modelName)
    {
        final StringBuffer buffer = new StringBuffer();

        final String usedSeparator = modelName ? MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR : separator;

        for (Namespace namespace = metaObject.getNamespace();
            (namespace instanceof Package) && !(namespace instanceof Model); namespace = namespace.getNamespace())
        {
            if (buffer.length() != 0)
            {
                buffer.insert(0, usedSeparator);
            }

            buffer.insert(0, namespace.getName());
        }

        return buffer.toString();
    }

    /**
     * Returns the package name of the closest ancestor that is an instance of <code>NamedElement</code>. If no such
     * ancestor exists the empty String is returned.
     * <p/>
     * If the argument would be an instance of <code>NamedElement</code> then this method returns that object's
     * package name.
     *
     * @see #getPackageName(org.eclipse.uml2.NamedElement, String, boolean)
     */
    static String getPackageName(
        final Element metaObject,
        final String separator,
        final boolean modelName)
    {
        final String packageName;

        if (metaObject instanceof NamedElement)
        {
            packageName = getPackageName((NamedElement)metaObject, separator, modelName);
        }
        else if (metaObject.getOwner() == null)
        {
            packageName = "";
        }
        else
        {
            packageName = getPackageName(metaObject.getOwner(), separator, modelName);
        }

        return packageName;
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param rs
     *            the resource set to search in
     * @param name
     *            the name to find.
     * @return the found model element.
     */
    static Object findByName(
        final ResourceSet rs,
        final String name)
    {
        Object modelElement = null;
        if (StringUtils.isNotBlank(name))
        {
            modelElement =
                findByPredicate(
                    rs,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            if (object instanceof NamedElement)
                            {
                                return StringUtils.trimToEmpty(((NamedElement)object).getName()).equals(name);
                            }
                            return false;
                        }
                    });
        }
        return modelElement;
    }

    /**
     * Finds a given model element in the model having the specified
     * <code>fullyQualifiedName</code>. If the model element can <strong>NOT
     * </strong> be found, <code>null</code> will be returned instead.
     *
     * @param rs
     *            the resource set to search in
     * @param fullyQualifiedName
     *            the fully qualified name of the element to search for.
     * @param separator
     *            the PSM separator used for qualifying the name (example ".").
     * @param modelName
     *            a flag indicating whether or not a search shall be performed
     *            using the fully qualified model name or fully qualified PSM
     *            name.
     * @return the found model element
     */
    static Object findByFullyQualifiedName(
        final ResourceSet rs,
        final String fullyQualifiedName,
        final String separator,
        final boolean modelName)
    {
        Object modelElement;
        modelElement =
            findByPredicate(
                rs,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        if (object instanceof NamedElement)
                        {
                            NamedElement element = (NamedElement)object;
                            StringBuffer fullName = new StringBuffer(getPackageName(
                                        element,
                                        separator,
                                        modelName));
                            String name = element.getName();
                            if (StringUtils.isNotBlank(name))
                            {
                                String namespaceSeparator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
                                if (!modelName)
                                {
                                    namespaceSeparator = separator;
                                }
                                fullName.append(namespaceSeparator);
                                fullName.append(name);
                            }
                            return fullName.toString().equals(fullyQualifiedName);
                        }
                        return false;
                    }
                });
        return modelElement;
    }

    /**
     * Multiplicity can be expressed as Value. String, integer... This method
     * parse it. MD11.5 uses string, and RSM integers.
     *
     * @param multValue a ValueSpecification, which need to be parsed
     * @return the parsed intrger
     */
    static int parseMultiplicity(final ValueSpecification multValue)
    {
        int value = 1;
        if (multValue != null)
        {
            if (multValue instanceof LiteralInteger)
            {
                LiteralInteger litInt = (LiteralInteger)multValue;
                value = litInt.getValue();
            }
            else if (multValue instanceof LiteralUnlimitedNatural)
            {
                LiteralUnlimitedNatural litInt = (LiteralUnlimitedNatural)multValue;
                value = litInt.getValue();
            }

            else if (multValue instanceof LiteralString)
            {
                LiteralString litStr = (LiteralString)multValue;
                String multString = litStr.getValue();
                if (multString.equals("*"))
                {
                    value = MultiplicityElement.UNLIMITED_UPPER_BOUND;
                }
                else
                {
                    value = Integer.parseInt(multString);
                }
            }
            else
            {
                logger.error("Unable to parse this value as multiplicity: " + multValue);
            }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Parsing multiplicity: intValue = " + value + " value: " + multValue);
        }
        return value;
    }
}