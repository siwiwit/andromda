package org.andromda.metafacades.emf.uml2;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.BindingFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.translation.ocl.ExpressionKinds;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.uml2.Abstraction;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Constraint;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Deployment;
import org.eclipse.uml2.DirectedRelationship;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Implementation;
import org.eclipse.uml2.Manifestation;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Permission;
import org.eclipse.uml2.Realization;
import org.eclipse.uml2.StateMachine;
import org.eclipse.uml2.Substitution;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.TemplateableElement;
import org.eclipse.uml2.Usage;
import org.eclipse.uml2.VisibilityKind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ModelElementFacade.
 *
 * @see org.andromda.metafacades.uml.ModelElementFacade
 */
public class ModelElementFacadeLogicImpl
    extends ModelElementFacadeLogic
{
    static XMIHelperImpl xmiHelper = new XMIHelperImpl();

    public ModelElementFacadeLogicImpl(
        final org.eclipse.uml2.Element metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    protected java.lang.String handleGetVisibility()
    {
        if (this.metaObject instanceof NamedElement)
        {
            final NamedElement element = (NamedElement)this.metaObject;
            final VisibilityKind kind = element.getVisibility();
            String visibility = null;
            if (kind.equals(VisibilityKind.PACKAGE_LITERAL))
            {
                visibility = "package";
            }
            if (kind.equals(VisibilityKind.PRIVATE_LITERAL))
            {
                visibility = "private";
            }
            if (kind.equals(VisibilityKind.PROTECTED_LITERAL))
            {
                visibility = "protected";
            }
            if (kind.equals(VisibilityKind.PUBLIC_LITERAL))
            {
                visibility = "public";
            }
            final TypeMappings languageMappings = this.getLanguageMappings();
            if (languageMappings != null)
            {
                visibility = languageMappings.getTo(visibility);
            }
            return visibility;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    protected java.lang.String handleGetPackagePath()
    {
        return StringUtils.replace(
            this.getPackageName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected java.lang.String handleGetName()
    {
        // In UML2, model elements need not have a name,
        // only when they are an instance of NamedElement.
        if (this.metaObject instanceof NamedElement)
        {
            NamedElement namedElement = (NamedElement) this.metaObject;
            return namedElement.getName();

        }
        return "";
    }

    /**
     * Gets the appropriate namespace property for retrieve the namespace scope
     * operation (dependng on the given <code>modelName</code> flag.
     *
     * @param modelName
     *            whether or not the scope operation for the model should be
     *            retrieved as oppposed to the mapped scope operator.
     * @return the scope operator.
     */
    private String getNamespaceScope(boolean modelName)
    {
        return modelName
            ? MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR
            : ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    protected java.lang.String handleGetPackageName()
    {
        return UmlUtilities.getPackageName(this.metaObject, this.getNamespaceScope(false), false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    protected java.lang.String handleGetFullyQualifiedName()
    {
        return this.getFullyQualifiedName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    protected java.lang.String handleGetFullyQualifiedNamePath()
    {
        return StringUtils.replace(
            this.getFullyQualifiedName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetLanguageMappings()
    {
        final String propertyName = UMLMetafacadeProperties.LANGUAGE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                mappings.setArraySuffix(this.getArraySuffix());
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                this.logger.error(
                    errMsg,
                    th);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    protected java.util.Collection handleGetStereotypeNames()
    {
        return UmlUtilities.getStereotypeNames(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    protected java.lang.String handleGetId()
    {
        return xmiHelper.getID(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#isConstraintsPresent()
     */
    protected boolean handleIsConstraintsPresent()
    {
        return this.getConstraints() != null && !this.getConstraints().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    protected java.lang.Object handleFindTaggedValue(final java.lang.String name)
    {
        Collection taggedValues = this.findTaggedValues(name);
        return taggedValues.isEmpty() ? null : taggedValues.iterator().next();
    }

    /**
     * Assumes no stereotype inheritance
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(java.lang.String)
     */
    protected boolean handleHasStereotype(final java.lang.String stereotypeName)
    {
        return UmlUtilities.containsStereotype(
            this.metaObject,
            stereotypeName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    protected java.lang.String handleGetDocumentation(final java.lang.String indent)
    {
        return this.getDocumentation(
            indent,
            64);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    protected java.lang.String handleGetFullyQualifiedName(boolean modelName)
    {
        String fullName = StringUtils.trimToEmpty(this.getName());
        final String packageName = this.getPackageName(true);
        final String metafacadeNamespaceScopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        if (StringUtils.isNotBlank(packageName))
        {
            fullName = packageName + metafacadeNamespaceScopeOperator + fullName;
        }
        if (!modelName)
        {
            final TypeMappings languageMappings = this.getLanguageMappings();
            if (languageMappings != null)
            {
                fullName = StringUtils.trimToEmpty(languageMappings.getTo(fullName));

                // now replace the metafacade scope operators
                // with the mapped scope operators
                final String namespaceScopeOperator =
                    String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR));
                fullName = StringUtils.replace(
                        fullName,
                        metafacadeNamespaceScopeOperator,
                        namespaceScopeOperator);
            }
        }

        if (this.isTemplateParametersPresent() &&
            BooleanUtils.toBoolean(
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuffer buffer = new StringBuffer();

            // add the name we've constructed so far
            buffer.append(fullName);

            // start the parameter list
            buffer.append("<");

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            final Collection templateParameters = this.getTemplateParameters();
            for (Iterator parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement =
                    ((TemplateParameterFacade)parameterIterator.next()).getParameter();

                if (modelElement instanceof ParameterFacade)
                {
                    buffer.append(((ParameterFacade)modelElement).getType().getFullyQualifiedName());
                }
                else
                {
                    buffer.append(modelElement.getFullyQualifiedName());
                }

                if (parameterIterator.hasNext())
                {
                    buffer.append(", ");
                }
            }

            // we're finished listing the parameters
            buffer.append(">");

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }

        return fullName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
     *      int)
     */
    protected java.lang.String handleGetDocumentation(
        final java.lang.String indent,
        final int lineLength)
    {
        return this.getDocumentation(
            indent,
            lineLength,
            true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    protected boolean handleHasExactStereotype(final java.lang.String stereotypeName)
    {
    	return this.getStereotypeNames().contains(StringUtils.trimToEmpty(stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(java.lang.String,
     *      java.lang.String)
     */
    protected java.lang.String handleTranslateConstraint(
        final java.lang.String name,
        final java.lang.String translation)
    {
        String translatedExpression = "";
        ConstraintFacade constraint =
            (ConstraintFacade)CollectionUtils.find(
                this.getConstraints(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        final ConstraintFacade constraint = (ConstraintFacade)object;
                        return StringUtils.trimToEmpty(constraint.getName()).equals(StringUtils.trimToEmpty(name));
                    }
                });

        if (constraint != null)
        {
            translatedExpression = constraint.getTranslation(translation);
        }
        return translatedExpression;
    }

    /**
     * Private helper that translates all the expressions contained in the
     * <code>constraints</code>, and returns an array of the translated
     * expressions.
     *
     * @param constraints
     *            the constraints to translate
     * @param translation
     *            the translation to transate <code>to</code>.
     * @return String[] the translated expressions, or null if no constraints
     *         were found
     */
    private String[] translateConstraints(
        final Collection constraints,
        final String translation)
    {
        String[] translatedExpressions = null;
        if (constraints != null && !constraints.isEmpty())
        {
            translatedExpressions = new String[constraints.size()];
            Iterator constraintIt = constraints.iterator();
            for (int ctr = 0; constraintIt.hasNext(); ctr++)
            {
                ConstraintFacade constraint = (ConstraintFacade)constraintIt.next();
                translatedExpressions[ctr] = constraint.getTranslation(translation);
            }
        }
        return translatedExpressions;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String,
     *      java.lang.String)
     */
    protected java.lang.String[] handleTranslateConstraints(
        final java.lang.String kind,
        final java.lang.String translation)
    {
        Collection constraints = this.getConstraints();
        CollectionUtils.filter(
            constraints,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    ConstraintFacade constraint = (ConstraintFacade)object;
                    return UMLMetafacadeUtils.isConstraintKind(
                        constraint.getBody(),
                        kind);
                }
            });
        return this.translateConstraints(
            constraints,
            translation);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String)
     */
    protected java.lang.String[] handleTranslateConstraints(final java.lang.String translation)
    {
        return this.translateConstraints(
            this.getConstraints(),
            translation);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(java.lang.String)
     */
    protected java.util.Collection handleGetConstraints(final java.lang.String kind)
    {
        return CollectionUtils.select(
            this.getConstraints(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    if (object instanceof ConstraintFacade)
                    {
                        ConstraintFacade constraint = (ConstraintFacade)object;
                        return ((ExpressionKinds.BODY.equals(kind) && constraint.isBodyExpression()) ||
                        (ExpressionKinds.DEF.equals(kind) && constraint.isDefinition()) ||
                        (ExpressionKinds.INV.equals(kind) && constraint.isInvariant()) ||
                        (ExpressionKinds.PRE.equals(kind) && constraint.isPreCondition()) ||
                        (ExpressionKinds.POST.equals(kind) && constraint.isPostCondition()));
                    }
                    return false;
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(java.lang.String)
     */
    protected java.util.Collection handleFindTaggedValues(java.lang.String name)
    {
        final Collection values = new ArrayList();

        // only search a tagged value when it actually has a name
        if (StringUtils.isNotBlank(name))
        {
            // trim the name, we don't want leading/trailing spaces
            name = StringUtils.trimToEmpty(name);

            // loop over the tagged values
            final Collection taggedValues = this.getTaggedValues();
            for (final Iterator taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext();)
            {
                final TaggedValueFacade taggedValue = (TaggedValueFacade)taggedValueIterator.next();

                // does this name match the argument tagged value name ?
                if (UmlUtilities.doesTagValueNameMatch(name, taggedValue.getName()))
                {
                    // 'tagged values' cam have arrays of strings as well as
                    // strings as values.
                    Object value = taggedValue.getValue();
                    if (value instanceof Collection)
                    {
                        values.addAll((Collection) taggedValue.getValue());
                    }
                    else
                    {
                        values.add(taggedValue.getValue());
                    }
                }
            }
        }
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
     *      int, boolean)
     */
    protected java.lang.String handleGetDocumentation(
        final java.lang.String indent,
        int lineLength,
        final boolean htmlStyle)
    {
        final StringBuffer documentation = new StringBuffer();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection comments = this.metaObject.getOwnedComments();
        if (comments != null && !comments.isEmpty())
        {
            for (final Iterator commentIterator = comments.iterator(); commentIterator.hasNext();)
            {
                final Comment comment = (Comment)commentIterator.next();
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                if (StringUtils.isEmpty(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.toString());
                }
                documentation.append(StringUtils.trimToEmpty(commentString));
                documentation.append(SystemUtils.LINE_SEPARATOR);
            }
        }

        // if there still isn't anything, try a tagged value
        if (StringUtils.isEmpty(documentation.toString()))
        {
            documentation.append(
                StringUtils.trimToEmpty((String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION)));
        }

        return StringUtilsHelper.format(
            StringUtils.trimToEmpty(documentation.toString()),
            indent,
            lineLength,
            htmlStyle);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName(boolean)
     */
    protected java.lang.String handleGetPackageName(final boolean modelName)
    {
        String packageName = this.getPackageName();
        if (modelName)
        {
            packageName =
                StringUtils.replace(
                    packageName,
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTaggedValues()
     */
    protected java.util.Collection handleGetTaggedValues()
    {
        return UmlUtilities.getTaggedValue(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    protected java.lang.Object handleGetPackage()
    {
        return this.metaObject.getNearestPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    protected java.lang.Object handleGetRootPackage()
    {
        // Be careful here, UML2 Model is mapped to a PackageFacade -
        // RootPackage
        return this.metaObject.getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    protected java.util.Collection handleGetTargetDependencies()
    {
        ArrayList dependencies = new ArrayList();
        dependencies.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(
                DirectedRelationship.class,
                this.metaObject.getModel()));
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                	DirectedRelationship relation = (DirectedRelationship) object;
                	if(isAUml14Dependency(relation))
                	{
                		// we only check first, see dependency facade for more detail.
                		return ModelElementFacadeLogicImpl.this.metaObject.equals(relation.getTargets().get(0));
                	}
                	return false;
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    protected java.lang.Object handleGetModel()
    {
        // Be careful here, Model Facade is mapped to resource
        return this.metaObject.getModel().eResource();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    protected java.util.Collection handleGetStereotypes()
    {
        return this.metaObject.getAppliedStereotypes();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    protected java.util.Collection handleGetConstraints()
    {
    	ArrayList constraints = new ArrayList();
    	constraints.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(Constraint.class, this.metaObject.getModel()));

    	CollectionUtils.filter(
    			constraints,
    			new Predicate()
    			{
    				public boolean evaluate(final Object object)
    				{
    					Constraint constraint = (Constraint) object;
    					return constraint.getConstrainedElements().contains(ModelElementFacadeLogicImpl.this.metaObject);
    				}
    			});
    	return constraints;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    protected java.util.Collection handleGetSourceDependencies()
    {
        // A more efficient implmentation of this would have been to use getClientDependencies() and getTemplateBindings()
    	// But it would have required the same filtering
    	// This way, the code is the "same" as getTargettingDependencies
        ArrayList dependencies = new ArrayList();
        dependencies.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(
                DirectedRelationship.class,
                this.metaObject.getModel()));
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                	DirectedRelationship relation = (DirectedRelationship) object;
                	if(isAUml14Dependency(relation))
                	{
                		// we only check first, see dependency facade for more detail.
                		return ModelElementFacadeLogicImpl.this.metaObject.equals(relation.getSources().get(0));
                	}
                	return false;
                }
            });
        return dependencies;
    }

    /**
     * This function test if the given relation is a dependency in UML1.4 sense of term.
     * @param relation: The relation to test
     * @return
     */
    static boolean isAUml14Dependency(DirectedRelationship relation)
    {
    	// this ensure that this relation is either a dependency or a template binding
    	boolean isAUml14Dependency = (relation instanceof Dependency) || (relation instanceof TemplateBinding);

    	// but we don't want subclass of dependency
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Abstraction); // present in uml 1.4 (but filter in uml14 facade)
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Deployment);
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Implementation);
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Manifestation);
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Permission); // present in uml 1.4
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Realization);
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Substitution);
    	isAUml14Dependency = isAUml14Dependency && !(relation instanceof Usage);// present in uml 1.4

    	return isAUml14Dependency;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStateMachineContext()
     */
    protected java.lang.Object handleGetStateMachineContext()
    {
        // TODO: What should this method return ?
    	// As I've seen in uml1.4 impl, it should return the statemachine which this element is the context for.
    	// Let's say for UML2: Return the owner if the latter is a StateMachine
    	StateMachine stateMachine = null;
        Element owner = this.metaObject.getOwner();
        if(owner instanceof StateMachine)
        {
        	stateMachine = (StateMachine) owner;
        }
        return stateMachine;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    public String getValidationName()
    {
        final StringBuffer validationName = new StringBuffer();
        final Object seperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        for (NamedElement namespace = (NamedElement)this.metaObject.getOwner(); namespace != null;
            namespace = (NamedElement)namespace.getOwner())
        {
            if (validationName.length() == 0)
            {
                validationName.append(namespace.getName());
            }
            else
            {
                validationName.insert(
                    0,
                    seperator);
                validationName.insert(
                    0,
                    namespace.getName());
            }
        }
        if (validationName.length() > 0)
        {
            validationName.append(seperator);
        }
        if (StringUtils.isNotEmpty(this.getName()))
        {
            validationName.append(this.getName());
        }
        else
        {
            validationName.append(this.getConfiguredProperty(UMLMetafacadeProperties.UNDEFINED_NAME));
        }
        return validationName.toString();
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        final Collection dependencies = new ArrayList(this.getSourceDependencies());
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof BindingFacade;
                }
            });
        return !dependencies.isEmpty();
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO: Be sure it works with RSM / MD11.5
        final Collection params = this.getTemplateParameters();
        return params != null && !params.isEmpty();
    }

    protected void handleCopyTaggedValues(final ModelElementFacade element)
    {
        // TODO What to do with this ?
    }

    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO: Be sure it works with RSM / MD11.5
        TemplateParameterFacade templateParameter = null;
        if (StringUtils.isNotEmpty(parameterName))
        {
            parameterName = StringUtils.trimToEmpty(parameterName);
            final Collection parameters = this.getTemplateParameters();
            if (parameters != null && !parameters.isEmpty())
            {
                for (final Iterator iterator = parameters.iterator(); iterator.hasNext();)
                {
                    final TemplateParameterFacade currentTemplateParameter = (TemplateParameterFacade)iterator.next();
                    if (currentTemplateParameter.getParameter() != null)
                    {
                        final ModelElementFacade parameter = currentTemplateParameter.getParameter();

                        // there should not be two template parameters with the same parameter name, but nothing
                        // prevents the model from allowing that.  So return the first instance if found.
                        if (parameterName.equals(parameter.getName()))
                        {
                            templateParameter = currentTemplateParameter;
                            break;
                        }
                    }
                }
            }
        }

        return templateParameter;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO: Be sure it works with RSM / MD11.5
        Collection templateParameters = new ArrayList();
        if (this.metaObject instanceof TemplateableElement)
        {
            TemplateableElement templateableElement = (TemplateableElement)this.metaObject;
            TemplateSignature templateSignature = templateableElement.getOwnedTemplateSignature();
            if (templateSignature != null)
            {
                templateParameters.addAll(templateSignature.getParameters());
            }
        }
        return templateParameters;
    }
}