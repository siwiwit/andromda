package org.andromda.metafacades.emf.uml2;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.AggregationKind;
import org.eclipse.uml2.Association;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.DataType;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Deployment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.MultiplicityElement;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.PackageableElement;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.RedefinableElement;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateParameter;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.ValueSpecification;
import org.eclipse.uml2.VisibilityKind;

/**
 * Implementation of AssociationEnd. We extend Property, we keep a reference to
 * the original property, and we defer almost all method calls to it.
 *
 * @author Cédric Jeanneret
 * @author Bob Fields
 */
public class AssociationEndImpl
    implements AssociationEnd
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AssociationEndImpl.class);

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * org.eclipse.uml2.Property
     */
    final Property property;

    /**
     * @param p
     */
    AssociationEndImpl(final Property p)
    {
        this.property = p;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof Attribute)
        {
            Property other = ((AttributeImpl)obj).property;
            return this.property.equals(other);
        }
        if (obj instanceof AssociationEnd)
        {
            Property other = ((AssociationEndImpl)obj).property;
            return this.property.equals(other);
        }
        return this.property.equals(obj);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.property.hashCode();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.property.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.Element#addKeyword(String)
     */
    public void addKeyword(final String arg0)
    {
        this.property.addKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#allNamespaces()
     */
    public List allNamespaces()
    {
        return this.property.allNamespaces();
    }

    /**
     * @see org.eclipse.uml2.Element#allOwnedElements()
     */
    public Set allOwnedElements()
    {
        return this.property.allOwnedElements();
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.Element#apply(Stereotype)
     */
    public void apply(final Stereotype arg0)
    {
        this.property.apply(arg0);
    }

    /**
     * @param arg0
     * @return this.property.createDefaultValue(eClass)
     */
    public ValueSpecification createDefaultValue(final EClass arg0)
    {
        return this.property.createDefaultValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createDependency(org.eclipse.uml2.NamedElement)
     */
    public Dependency createDependency(final NamedElement arg0)
    {
        return this.property.createDependency(arg0);
    }

    /**
     * @return this.property.createDeployment(null)
     */
    public Deployment createDeployment()
    {
        return this.property.createDeployment();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#createDeployment(org.eclipse.emf.ecore.EClass)
     */
    @Deprecated
    public Deployment createDeployment(final EClass arg0)
    {
        return this.property.createDeployment(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(final String arg0)
    {
        return this.property.createEAnnotation(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#createLowerValue(org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createLowerValue(final EClass arg0)
    {
        return this.property.createLowerValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createNameExpression()
     */
    public StringExpression createNameExpression()
    {
        return this.property.createNameExpression();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createNameExpression(org.eclipse.emf.ecore.EClass)
     */
    @Deprecated
    public StringExpression createNameExpression(final EClass arg0)
    {
        return this.property.createNameExpression(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.property.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment(org.eclipse.emf.ecore.EClass)
     */
    @Deprecated
    public Comment createOwnedComment(final EClass arg0)
    {
        return this.property.createOwnedComment(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature()
     * @deprecated
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        //return this.property.createOwnedTemplateSignature();
        logger.error("AssociationEndImpl.createOwnedTemplateSignature is UML14 only");
        return null;
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        //return this.property.createOwnedTemplateSignature(arg0);
        logger.error("AssociationEndImpl.createOwnedTemplateSignature(arg0) is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.Property#createQualifier()
     */
    public Property createQualifier()
    {
        return this.property.createQualifier();
    }

    /**
     * @see org.eclipse.uml2.Property#createQualifier(org.eclipse.emf.ecore.EClass)
     */
    public Property createQualifier(final EClass arg0)
    {
        return this.property.createQualifier(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding()
     * @deprecated
     */
    public TemplateBinding createTemplateBinding()
    {
        //return this.property.createTemplateBinding();
        logger.error("AssociationEndImpl.createTemplateBinding is UML14 only");
        return null;
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        //return this.property.createTemplateBinding(arg0);
        logger.error("AssociationEndImpl.createTemplateBinding(arg0) is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#createUpperValue(org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createUpperValue(final EClass arg0)
    {
        return this.property.createUpperValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#destroy()
     */
    public void destroy()
    {
        this.property.destroy();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        return this.property.eAdapters();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        return this.property.eAllContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        return this.property.eClass();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        return this.property.eContainer();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.property.eContainingFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        return this.property.eContainmentFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        return this.property.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        return this.property.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        return this.property.eDeliver();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(final EStructuralFeature arg0)
    {
        return this.property.eGet(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(
        final EStructuralFeature arg0,
        final boolean arg1)
    {
        return this.property.eGet(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        return this.property.eIsProxy();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature arg0)
    {
        return this.property.eIsSet(arg0);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification arg0)
    {
        this.property.eNotify(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        return this.property.eResource();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, Object)
     */
    public void eSet(
        final EStructuralFeature arg0,
        final Object arg1)
    {
        this.property.eSet(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(final boolean arg0)
    {
        this.property.eSetDeliver(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature arg0)
    {
        this.property.eUnset(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#getAggregation()
     */
    public AggregationKind getAggregation()
    {
        return this.property.getAggregation();
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(final String arg0)
    {
        return this.property.getApplicableStereotype(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotypes()
     */
    public Set getApplicableStereotypes()
    {
        return this.property.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(final String arg0)
    {
        return this.property.getAppliedStereotype(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotypes()
     */
    public Set getAppliedStereotypes()
    {
        return this.property.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedVersion(org.eclipse.uml2.Stereotype)
     */
    public String getAppliedVersion(final Stereotype arg0)
    {
        return this.property.getAppliedVersion(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#getAssociation()
     */
    public Association getAssociation()
    {
        return this.property.getAssociation();
    }

    /**
     * @see org.eclipse.uml2.Property#getAssociationEnd()
     */
    public Property getAssociationEnd()
    {
        return this.property.getAssociationEnd();
    }

    /**
     * @see org.eclipse.uml2.Property#getClass_()
     */
    public Class getClass_()
    {
        return this.property.getClass_();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getClientDependencies()
     */
    public EList getClientDependencies()
    {
        return this.property.getClientDependencies();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getClientDependency(String)
     */
    public Dependency getClientDependency(final String arg0)
    {
        return this.property.getClientDependency(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#getDatatype()
     */
    public DataType getDatatype()
    {
        return this.property.getDatatype();
    }

    /**
     * @see org.eclipse.uml2.Property#getDefault()
     */
    public String getDefault()
    {
        return this.property.getDefault();
    }

    /**
     * @see org.eclipse.uml2.Property#getDefaultValue()
     */
    public ValueSpecification getDefaultValue()
    {
        return this.property.getDefaultValue();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployedElement(String)
     */
    public PackageableElement getDeployedElement(final String arg0)
    {
        return this.property.getDeployedElement(arg0);
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployedElements()
     */
    public EList getDeployedElements()
    {
        return this.property.getDeployedElements();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployment(String)
     */
    public Deployment getDeployment(final String arg0)
    {
        return this.property.getDeployment(arg0);
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployments()
     */
    public EList getDeployments()
    {
        return this.property.getDeployments();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(final String arg0)
    {
        return this.property.getEAnnotation(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        return this.property.getEAnnotations();
    }

    /**
     * @see org.eclipse.uml2.ConnectableElement#getEnds()
     */
    public EList getEnds()
    {
        return this.property.getEnds();
    }

    /**
     * @see org.eclipse.uml2.Feature#getFeaturingClassifier(String)
     */
    public Classifier getFeaturingClassifier(final String arg0)
    {
        return this.property.getFeaturingClassifier(arg0);
    }

    /**
     * @see org.eclipse.uml2.Feature#getFeaturingClassifiers()
     */
    public EList getFeaturingClassifiers()
    {
        return this.property.getFeaturingClassifiers();
    }

    /**
     * @see org.eclipse.uml2.Element#getKeywords()
     */
    public Set getKeywords()
    {
        return this.property.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getLabel()
     */
    public String getLabel()
    {
        return this.property.getLabel();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getLabel(boolean)
     */
    public String getLabel(final boolean arg0)
    {
        return this.property.getLabel(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#getLower()
     */
    public int getLower()
    {
        return this.property.getLower();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#getLowerValue()
     */
    public ValueSpecification getLowerValue()
    {
        return this.property.getLowerValue();
    }

    /**
     * @see org.eclipse.uml2.Element#getModel()
     */
    public Model getModel()
    {
        return this.property.getModel();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getName()
     */
    public String getName()
    {
        return this.property.getName();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        return this.property.getNameExpression();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        return this.property.getNamespace();
    }

    /**
     * @see org.eclipse.uml2.Element#getNearestPackage()
     */
    public Package getNearestPackage()
    {
        return this.property.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.Property#getOpposite()
     */
    public Property getOpposite()
    {
        return this.property.getOpposite();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        return this.property.getOwnedComments();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        return this.property.getOwnedElements();
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#getOwnedTemplateSignature()
     * @deprecated
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        //return this.property.getOwnedTemplateSignature();
        logger.error("AssociationEndImpl.getOwnedTemplateSignature is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.property.getOwner();
    }

    /**
     * @see org.eclipse.uml2.Property#getOwningAssociation()
     */
    public Association getOwningAssociation()
    {
        return this.property.getOwningAssociation();
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#getOwningParameter()
     */
    public TemplateParameter getOwningParameter()
    {
        return this.property.getOwningParameter();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.property.getQualifiedName();
    }

    /**
     * @see org.eclipse.uml2.Property#getQualifier(String)
     */
    public Property getQualifier(final String arg0)
    {
        return this.property.getQualifier(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#getQualifiers()
     */
    public EList getQualifiers()
    {
        return this.property.getQualifiers();
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#getRedefinedElement(String)
     */
    public RedefinableElement getRedefinedElement(final String arg0)
    {
        return this.property.getRedefinedElement(arg0);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#getRedefinedElements()
     */
    public EList getRedefinedElements()
    {
        return this.property.getRedefinedElements();
    }

    /**
     * @see org.eclipse.uml2.Property#getRedefinedProperties()
     */
    public EList getRedefinedProperties()
    {
        return this.property.getRedefinedProperties();
    }

    /**
     * @see org.eclipse.uml2.Property#getRedefinedProperty(String)
     */
    public Property getRedefinedProperty(final String arg0)
    {
        return this.property.getRedefinedProperty(arg0);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#getRedefinitionContext(String)
     */
    public Classifier getRedefinitionContext(final String arg0)
    {
        return this.property.getRedefinitionContext(arg0);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#getRedefinitionContexts()
     */
    public EList getRedefinitionContexts()
    {
        return this.property.getRedefinitionContexts();
    }

    /**
     * @see org.eclipse.uml2.Property#getSubsettedProperties()
     */
    public EList getSubsettedProperties()
    {
        return this.property.getSubsettedProperties();
    }

    /**
     * @see org.eclipse.uml2.Property#getSubsettedProperty(String)
     */
    public Property getSubsettedProperty(final String arg0)
    {
        return this.property.getSubsettedProperty(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#getTemplateBindings()
     * @deprecated
     */
    public EList getTemplateBindings()
    {
        //return this.property.getTemplateBindings();
        logger.error("AssociationEndImpl.getTemplateBindings is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#getTemplateParameter()
     */
    public TemplateParameter getTemplateParameter()
    {
        return this.property.getTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.TypedElement#getType()
     */
    public Type getType()
    {
        return this.property.getType();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#getUpper()
     */
    public int getUpper()
    {
        return this.property.getUpper();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#getUpperValue()
     */
    public ValueSpecification getUpperValue()
    {
        return this.property.getUpperValue();
    }

    /**
     * @see org.eclipse.uml2.Element#getValue(org.eclipse.uml2.Stereotype, String)
     */
    public Object getValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.getValue(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        return this.property.getVisibility();
    }

    /**
     * @see org.eclipse.uml2.Element#hasKeyword(String)
     */
    public boolean hasKeyword(final String arg0)
    {
        return this.property.hasKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#hasValue(org.eclipse.uml2.Stereotype, String)
     */
    public boolean hasValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.hasValue(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#includesCardinality(int)
     */
    public boolean includesCardinality(final int arg0)
    {
        return this.property.includesCardinality(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#includesMultiplicity(org.eclipse.uml2.MultiplicityElement)
     */
    public boolean includesMultiplicity(final MultiplicityElement arg0)
    {
        return this.property.includesMultiplicity(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#isApplied(org.eclipse.uml2.Stereotype)
     */
    public boolean isApplied(final Stereotype arg0)
    {
        return this.property.isApplied(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#isComposite()
     */
    public boolean isComposite()
    {
        return this.property.isComposite();
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#isConsistentWith(org.eclipse.uml2.RedefinableElement)
     */
    public boolean isConsistentWith(final RedefinableElement arg0)
    {
        return this.property.isConsistentWith(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#isDerived()
     */
    public boolean isDerived()
    {
        return this.property.isDerived();
    }

    /**
     * @see org.eclipse.uml2.Property#isDerivedUnion()
     */
    public boolean isDerivedUnion()
    {
        return this.property.isDerivedUnion();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#isDistinguishableFrom(org.eclipse.uml2.NamedElement, org.eclipse.uml2.Namespace)
     */
    public boolean isDistinguishableFrom(
        final NamedElement arg0,
        final Namespace arg1)
    {
        return this.property.isDistinguishableFrom(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.property.isLeaf();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#isMultivalued()
     */
    public boolean isMultivalued()
    {
        return this.property.isMultivalued();
    }

    /**
     * @see org.eclipse.uml2.Property#isNavigable()
     */
    public boolean isNavigable()
    {
        return this.property.isNavigable();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.property.isOrdered();
    }

    /**
     * @see org.eclipse.uml2.StructuralFeature#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.property.isReadOnly();
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#isRedefinitionContextValid(org.eclipse.uml2.RedefinableElement)
     */
    public boolean isRedefinitionContextValid(final RedefinableElement arg0)
    {
        return this.property.isRedefinitionContextValid(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#isRequired(org.eclipse.uml2.Stereotype)
     */
    public boolean isRequired(final Stereotype arg0)
    {
        return this.property.isRequired(arg0);
    }

    /**
     * @see org.eclipse.uml2.Feature#isStatic()
     */
    public boolean isStatic()
    {
        return this.property.isStatic();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#isUnique()
     */
    public boolean isUnique()
    {
        return this.property.isUnique();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#lower()
     */
    public int lower()
    {
        return this.property.lower();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#lowerBound()
     */
    public int lowerBound()
    {
        return this.property.lowerBound();
    }

    /**
     * @see org.eclipse.uml2.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.property.mustBeOwned();
    }

    /**
     * @see org.eclipse.uml2.Property#opposite()
     */
    public Property opposite()
    {
        return this.property.opposite();
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#parameterableElements()
     * @deprecated
     */
    public Set parameterableElements()
    {
        //return this.property.parameterableElements();
        logger.error("AssociationEndImpl.property.parameterableElements is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.NamedElement#qualifiedName()
     */
    public String qualifiedName()
    {
        return this.property.qualifiedName();
    }

    /**
     * @see org.eclipse.uml2.Element#removeKeyword(String)
     */
    public void removeKeyword(final String arg0)
    {
        this.property.removeKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#separator()
     */
    public String separator()
    {
        return this.property.separator();
    }

    /**
     * @see org.eclipse.uml2.Property#setAggregation(org.eclipse.uml2.AggregationKind)
     */
    public void setAggregation(final AggregationKind arg0)
    {
        this.property.setAggregation(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setAssociation(org.eclipse.uml2.Association)
     */
    public void setAssociation(final Association arg0)
    {
        this.property.setAssociation(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setAssociationEnd(org.eclipse.uml2.Property)
     */
    public void setAssociationEnd(final Property arg0)
    {
        this.property.setAssociationEnd(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setBooleanDefault(boolean)
     */
    public void setBooleanDefault(final boolean arg0)
    {
        this.property.setBooleanDefault(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setDatatype(org.eclipse.uml2.DataType)
     */
    public void setDatatype(final DataType arg0)
    {
        this.property.setDatatype(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setDefaultValue(org.eclipse.uml2.ValueSpecification)
     */
    public void setDefaultValue(final ValueSpecification arg0)
    {
        this.property.setDefaultValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setIntegerDefault(int)
     */
    public void setIntegerDefault(final int arg0)
    {
        this.property.setIntegerDefault(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setIsDerived(boolean)
     */
    public void setIsDerived(final boolean arg0)
    {
        this.property.setIsDerived(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setIsDerivedUnion(boolean)
     */
    public void setIsDerivedUnion(final boolean arg0)
    {
        this.property.setIsDerivedUnion(arg0);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#setIsLeaf(boolean)
     */
    public void setIsLeaf(final boolean arg0)
    {
        this.property.setIsLeaf(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setIsOrdered(boolean)
     */
    public void setIsOrdered(final boolean arg0)
    {
        this.property.setIsOrdered(arg0);
    }

    /**
     * @see org.eclipse.uml2.StructuralFeature#setIsReadOnly(boolean)
     */
    public void setIsReadOnly(final boolean arg0)
    {
        this.property.setIsReadOnly(arg0);
    }

    /**
     * @see org.eclipse.uml2.Feature#setIsStatic(boolean)
     */
    public void setIsStatic(final boolean arg0)
    {
        this.property.setIsStatic(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setIsUnique(boolean)
     */
    public void setIsUnique(final boolean arg0)
    {
        this.property.setIsUnique(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setLowerBound(int)
     */
    public void setLowerBound(final int arg0)
    {
        this.property.setLowerBound(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setLowerValue(org.eclipse.uml2.ValueSpecification)
     */
    public void setLowerValue(final ValueSpecification arg0)
    {
        this.property.setLowerValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setName(String)
     */
    public void setName(final String arg0)
    {
        this.property.setName(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setNameExpression(org.eclipse.uml2.StringExpression)
     */
    public void setNameExpression(final StringExpression arg0)
    {
        this.property.setNameExpression(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setNavigable(boolean)
     */
    public void setNavigable(final boolean arg0)
    {
        this.property.setNavigable(arg0);
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.TemplateSignature)
     * @deprecated
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        //this.property.setOwnedTemplateSignature(arg0);
        logger.error("AssociationEndImpl.property.setOwnedTemplateSignature(arg0) is UML14 only");
    }

    /**
     * @see org.eclipse.uml2.Property#setOwningAssociation(org.eclipse.uml2.Association)
     */
    public void setOwningAssociation(final Association arg0)
    {
        this.property.setOwningAssociation(arg0);
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#setOwningParameter(org.eclipse.uml2.TemplateParameter)
     */
    public void setOwningParameter(final TemplateParameter arg0)
    {
        this.property.setOwningParameter(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setStringDefault(String)
     */
    public void setStringDefault(final String arg0)
    {
        this.property.setStringDefault(arg0);
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#setTemplateParameter(org.eclipse.uml2.TemplateParameter)
     */
    public void setTemplateParameter(final TemplateParameter arg0)
    {
        this.property.setTemplateParameter(arg0);
    }

    /**
     * @see org.eclipse.uml2.TypedElement#setType(org.eclipse.uml2.Type)
     */
    public void setType(final Type arg0)
    {
        this.property.setType(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#setUnlimitedNaturalDefault(int)
     */
    public void setUnlimitedNaturalDefault(final int arg0)
    {
        this.property.setUnlimitedNaturalDefault(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setUpperBound(int)
     */
    public void setUpperBound(final int arg0)
    {
        this.property.setUpperBound(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#setUpperValue(org.eclipse.uml2.ValueSpecification)
     */
    public void setUpperValue(final ValueSpecification arg0)
    {
        this.property.setUpperValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.Element#setValue(org.eclipse.uml2.Stereotype, String, Object)
     */
    public void setValue(
        final Stereotype arg0,
        final String arg1,
        final Object arg2)
    {
        this.property.setValue(
            arg0,
            arg1,
            arg2);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setVisibility(org.eclipse.uml2.VisibilityKind)
     */
    public void setVisibility(final VisibilityKind arg0)
    {
        this.property.setVisibility(arg0);
    }

    /**
     * @see org.eclipse.uml2.Property#subsettingContext()
     */
    public Set subsettingContext()
    {
        return this.property.subsettingContext();
    }

    /**
     * @see org.eclipse.uml2.Element#unapply(org.eclipse.uml2.Stereotype)
     */
    public void unapply(final Stereotype arg0)
    {
        this.property.unapply(arg0);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#upper()
     */
    public int upper()
    {
        return this.property.upper();
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#upperBound()
     */
    public int upperBound()
    {
        return this.property.upperBound();
    }

    /**
     * @see org.eclipse.uml2.Property#validateDerivedUnionIsDerived(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDerivedUnionIsDerived(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateDerivedUnionIsDerived(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateHasOwner(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#validateLowerEqLowerbound(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateLowerEqLowerbound(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateLowerEqLowerbound(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#validateLowerGe0(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateLowerGe0(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateLowerGe0(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateMultiplicityOfComposite(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateMultiplicityOfComposite(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateMultiplicityOfComposite(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateNavigablePropertyRedefinition(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNavigablePropertyRedefinition(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNavigablePropertyRedefinition(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateNavigableReadonly(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNavigableReadonly(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNavigableReadonly(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateNoName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNoName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNoName(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNotOwnSelf(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateOppositeIsOtherEnd(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateOppositeIsOtherEnd(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateOppositeIsOtherEnd(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateQualifiedName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateQualifiedName(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#validateRedefinitionConsistent(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateRedefinitionConsistent(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateRedefinitionConsistent(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.RedefinableElement#validateRedefinitionContextValid(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateRedefinitionContextValid(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateRedefinitionContextValid(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateSubsettingContext(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSubsettingContext(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateSubsettingContext(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.Property#validateSubsettingRules(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSubsettingRules(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateSubsettingRules(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#validateUpperEqUpperbound(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateUpperEqUpperbound(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperEqUpperbound(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#validateUpperGeLower(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateUpperGeLower(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperGeLower(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.MultiplicityElement#validateUpperGt0(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateUpperGt0(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperGt0(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateVisibilityNeedsOwnership(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    public Object eInvoke(EOperation arg0, EList arg1) throws InvocationTargetException
    {
        // UML2 3.1 stub
        return null;
    }
}
