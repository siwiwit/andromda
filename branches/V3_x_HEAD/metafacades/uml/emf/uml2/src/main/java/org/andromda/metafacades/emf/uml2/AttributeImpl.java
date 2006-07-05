package org.andromda.metafacades.emf.uml2;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
 * Implementation of Attribute.
 *
 * We extends Property We keep a reference to original property and we defer
 * almost all method call to it.
 *
 * @author Cédric Jeanneret
 */
public class AttributeImpl
    implements Attribute
{
    Property property;

    AttributeImpl(final Property p)
    {
        this.property = p;
    }

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

    public int hashCode()
    {
        return this.property.hashCode();
    }

    public String toString()
    {
        return this.property.toString();
    }

    /**
     * @param arg0
     */
    public void addKeyword(final String arg0)
    {
        this.property.addKeyword(arg0);
    }

    /**
     * @return
     */
    public List allNamespaces()
    {
        return this.property.allNamespaces();
    }

    /**
     * @return
     */
    public Set allOwnedElements()
    {
        return this.property.allOwnedElements();
    }

    /**
     * @param arg0
     */
    public void apply(final Stereotype arg0)
    {
        this.property.apply(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public ValueSpecification createDefaultValue(final EClass arg0)
    {
        return this.property.createDefaultValue(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public Dependency createDependency(final NamedElement arg0)
    {
        return this.property.createDependency(arg0);
    }

    /**
     * @return
     */
    public Deployment createDeployment()
    {
        return this.property.createDeployment();
    }

    /**
     * @param arg0
     * @return
     */
    public Deployment createDeployment(final EClass arg0)
    {
        return this.property.createDeployment(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public EAnnotation createEAnnotation(final String arg0)
    {
        return this.property.createEAnnotation(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public ValueSpecification createLowerValue(final EClass arg0)
    {
        return this.property.createLowerValue(arg0);
    }

    /**
     * @return
     */
    public StringExpression createNameExpression()
    {
        return this.property.createNameExpression();
    }

    /**
     * @param arg0
     * @return
     */
    public StringExpression createNameExpression(final EClass arg0)
    {
        return this.property.createNameExpression(arg0);
    }

    /**
     * @return
     */
    public Comment createOwnedComment()
    {
        return this.property.createOwnedComment();
    }

    /**
     * @param arg0
     * @return
     */
    public Comment createOwnedComment(final EClass arg0)
    {
        return this.property.createOwnedComment(arg0);
    }

    /**
     * @return
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        return this.property.createOwnedTemplateSignature();
    }

    /**
     * @param arg0
     * @return
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        return this.property.createOwnedTemplateSignature(arg0);
    }

    /**
     * @return
     */
    public Property createQualifier()
    {
        return this.property.createQualifier();
    }

    /**
     * @param arg0
     * @return
     */
    public Property createQualifier(final EClass arg0)
    {
        return this.property.createQualifier(arg0);
    }

    /**
     * @return
     */
    public TemplateBinding createTemplateBinding()
    {
        return this.property.createTemplateBinding();
    }

    /**
     * @param arg0
     * @return
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        return this.property.createTemplateBinding(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public ValueSpecification createUpperValue(final EClass arg0)
    {
        return this.property.createUpperValue(arg0);
    }

    /**
     *
     */
    public void destroy()
    {
        this.property.destroy();
    }

    /**
     * @return
     */
    public EList eAdapters()
    {
        return this.property.eAdapters();
    }

    /**
     * @return
     */
    public TreeIterator eAllContents()
    {
        return this.property.eAllContents();
    }

    /**
     * @return
     */
    public EClass eClass()
    {
        return this.property.eClass();
    }

    /**
     * @return
     */
    public EObject eContainer()
    {
        return this.property.eContainer();
    }

    /**
     * @return
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.property.eContainingFeature();
    }

    /**
     * @return
     */
    public EReference eContainmentFeature()
    {
        return this.property.eContainmentFeature();
    }

    /**
     * @return
     */
    public EList eContents()
    {
        return this.property.eContents();
    }

    /**
     * @return
     */
    public EList eCrossReferences()
    {
        return this.property.eCrossReferences();
    }

    /**
     * @return
     */
    public boolean eDeliver()
    {
        return this.property.eDeliver();
    }

    /**
     * @param arg0
     * @return
     */
    public Object eGet(final EStructuralFeature arg0)
    {
        return this.property.eGet(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @return
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
     * @return
     */
    public boolean eIsProxy()
    {
        return this.property.eIsProxy();
    }

    /**
     * @param arg0
     * @return
     */
    public boolean eIsSet(final EStructuralFeature arg0)
    {
        return this.property.eIsSet(arg0);
    }

    /**
     * @param arg0
     */
    public void eNotify(final Notification arg0)
    {
        this.property.eNotify(arg0);
    }

    /**
     * @return
     */
    public Resource eResource()
    {
        return this.property.eResource();
    }

    /**
     * @param arg0
     * @param arg1
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
     * @param arg0
     */
    public void eSetDeliver(final boolean arg0)
    {
        this.property.eSetDeliver(arg0);
    }

    /**
     * @param arg0
     */
    public void eUnset(final EStructuralFeature arg0)
    {
        this.property.eUnset(arg0);
    }

    /**
     * @return
     */
    public AggregationKind getAggregation()
    {
        return this.property.getAggregation();
    }

    /**
     * @param arg0
     * @return
     */
    public Stereotype getApplicableStereotype(final String arg0)
    {
        return this.property.getApplicableStereotype(arg0);
    }

    /**
     * @return
     */
    public Set getApplicableStereotypes()
    {
        return this.property.getApplicableStereotypes();
    }

    /**
     * @param arg0
     * @return
     */
    public Stereotype getAppliedStereotype(final String arg0)
    {
        return this.property.getAppliedStereotype(arg0);
    }

    /**
     * @return
     */
    public Set getAppliedStereotypes()
    {
        return this.property.getAppliedStereotypes();
    }

    /**
     * @param arg0
     * @return
     */
    public String getAppliedVersion(final Stereotype arg0)
    {
        return this.property.getAppliedVersion(arg0);
    }

    /**
     * @return
     */
    public Association getAssociation()
    {
        return this.property.getAssociation();
    }

    /**
     * @return
     */
    public Property getAssociationEnd()
    {
        return this.property.getAssociationEnd();
    }

    /**
     * @return
     */
    public Class getClass_()
    {
        return this.property.getClass_();
    }

    /**
     * @return
     */
    public EList getClientDependencies()
    {
        return this.property.getClientDependencies();
    }

    /**
     * @param arg0
     * @return
     */
    public Dependency getClientDependency(final String arg0)
    {
        return this.property.getClientDependency(arg0);
    }

    /**
     * @return
     */
    public DataType getDatatype()
    {
        return this.property.getDatatype();
    }

    /**
     * @return
     */
    public String getDefault()
    {
        return this.property.getDefault();
    }

    /**
     * @return
     */
    public ValueSpecification getDefaultValue()
    {
        return this.property.getDefaultValue();
    }

    /**
     * @param arg0
     * @return
     */
    public PackageableElement getDeployedElement(final String arg0)
    {
        return this.property.getDeployedElement(arg0);
    }

    /**
     * @return
     */
    public EList getDeployedElements()
    {
        return this.property.getDeployedElements();
    }

    /**
     * @param arg0
     * @return
     */
    public Deployment getDeployment(final String arg0)
    {
        return this.property.getDeployment(arg0);
    }

    /**
     * @return
     */
    public EList getDeployments()
    {
        return this.property.getDeployments();
    }

    /**
     * @param arg0
     * @return
     */
    public EAnnotation getEAnnotation(final String arg0)
    {
        return this.property.getEAnnotation(arg0);
    }

    /**
     * @return
     */
    public EList getEAnnotations()
    {
        return this.property.getEAnnotations();
    }

    /**
     * @return
     */
    public EList getEnds()
    {
        return this.property.getEnds();
    }

    /**
     * @param arg0
     * @return
     */
    public Classifier getFeaturingClassifier(final String arg0)
    {
        return this.property.getFeaturingClassifier(arg0);
    }

    /**
     * @return
     */
    public EList getFeaturingClassifiers()
    {
        return this.property.getFeaturingClassifiers();
    }

    /**
     * @return
     */
    public Set getKeywords()
    {
        return this.property.getKeywords();
    }

    /**
     * @return
     */
    public String getLabel()
    {
        return this.property.getLabel();
    }

    /**
     * @param arg0
     * @return
     */
    public String getLabel(final boolean arg0)
    {
        return this.property.getLabel(arg0);
    }

    /**
     * @return
     */
    public int getLower()
    {
        return this.property.getLower();
    }

    /**
     * @return
     */
    public ValueSpecification getLowerValue()
    {
        return this.property.getLowerValue();
    }

    /**
     * @return
     */
    public Model getModel()
    {
        return this.property.getModel();
    }

    /**
     * @return
     */
    public String getName()
    {
        return this.property.getName();
    }

    /**
     * @return
     */
    public StringExpression getNameExpression()
    {
        return this.property.getNameExpression();
    }

    /**
     * @return
     */
    public Namespace getNamespace()
    {
        return this.property.getNamespace();
    }

    /**
     * @return
     */
    public Package getNearestPackage()
    {
        return this.property.getNearestPackage();
    }

    /**
     * @return
     */
    public Property getOpposite()
    {
        return this.property.getOpposite();
    }

    /**
     * @return
     */
    public EList getOwnedComments()
    {
        return this.property.getOwnedComments();
    }

    /**
     * @return
     */
    public EList getOwnedElements()
    {
        return this.property.getOwnedElements();
    }

    /**
     * @return
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        return this.property.getOwnedTemplateSignature();
    }

    /**
     * @return
     */
    public Element getOwner()
    {
        return this.property.getOwner();
    }

    /**
     * @return
     */
    public Association getOwningAssociation()
    {
        return this.property.getOwningAssociation();
    }

    /**
     * @return
     */
    public TemplateParameter getOwningParameter()
    {
        return this.property.getOwningParameter();
    }

    /**
     * @return
     */
    public String getQualifiedName()
    {
        return this.property.getQualifiedName();
    }

    /**
     * @param arg0
     * @return
     */
    public Property getQualifier(final String arg0)
    {
        return this.property.getQualifier(arg0);
    }

    /**
     * @return
     */
    public EList getQualifiers()
    {
        return this.property.getQualifiers();
    }

    /**
     * @param arg0
     * @return
     */
    public RedefinableElement getRedefinedElement(final String arg0)
    {
        return this.property.getRedefinedElement(arg0);
    }

    /**
     * @return
     */
    public EList getRedefinedElements()
    {
        return this.property.getRedefinedElements();
    }

    /**
     * @return
     */
    public EList getRedefinedProperties()
    {
        return this.property.getRedefinedProperties();
    }

    /**
     * @param arg0
     * @return
     */
    public Property getRedefinedProperty(final String arg0)
    {
        return this.property.getRedefinedProperty(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public Classifier getRedefinitionContext(final String arg0)
    {
        return this.property.getRedefinitionContext(arg0);
    }

    /**
     * @return
     */
    public EList getRedefinitionContexts()
    {
        return this.property.getRedefinitionContexts();
    }

    /**
     * @return
     */
    public EList getSubsettedProperties()
    {
        return this.property.getSubsettedProperties();
    }

    /**
     * @param arg0
     * @return
     */
    public Property getSubsettedProperty(final String arg0)
    {
        return this.property.getSubsettedProperty(arg0);
    }

    /**
     * @return
     */
    public EList getTemplateBindings()
    {
        return this.property.getTemplateBindings();
    }

    /**
     * @return
     */
    public TemplateParameter getTemplateParameter()
    {
        return this.property.getTemplateParameter();
    }

    /**
     * @return
     */
    public Type getType()
    {
        return this.property.getType();
    }

    /**
     * @return
     */
    public int getUpper()
    {
        return this.property.getUpper();
    }

    /**
     * @return
     */
    public ValueSpecification getUpperValue()
    {
        return this.property.getUpperValue();
    }

    /**
     * @param arg0
     * @param arg1
     * @return
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
     * @return
     */
    public VisibilityKind getVisibility()
    {
        return this.property.getVisibility();
    }

    /**
     * @param arg0
     * @return
     */
    public boolean hasKeyword(final String arg0)
    {
        return this.property.hasKeyword(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @return
     */
    public boolean includesCardinality(final int arg0)
    {
        return this.property.includesCardinality(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public boolean includesMultiplicity(final MultiplicityElement arg0)
    {
        return this.property.includesMultiplicity(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public boolean isApplied(final Stereotype arg0)
    {
        return this.property.isApplied(arg0);
    }

    /**
     * @return
     */
    public boolean isComposite()
    {
        return this.property.isComposite();
    }

    /**
     * @param arg0
     * @return
     */
    public boolean isConsistentWith(final RedefinableElement arg0)
    {
        return this.property.isConsistentWith(arg0);
    }

    /**
     * @return
     */
    public boolean isDerived()
    {
        return this.property.isDerived();
    }

    /**
     * @return
     */
    public boolean isDerivedUnion()
    {
        return this.property.isDerivedUnion();
    }

    /**
     * @param arg0
     * @param arg1
     * @return
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
     * @return
     */
    public boolean isLeaf()
    {
        return this.property.isLeaf();
    }

    /**
     * @return
     */
    public boolean isMultivalued()
    {
        return this.property.isMultivalued();
    }

    /**
     * @return
     */
    public boolean isNavigable()
    {
        return this.property.isNavigable();
    }

    /**
     * @return
     */
    public boolean isOrdered()
    {
        return this.property.isOrdered();
    }

    /**
     * @return
     */
    public boolean isReadOnly()
    {
        return this.property.isReadOnly();
    }

    /**
     * @param arg0
     * @return
     */
    public boolean isRedefinitionContextValid(final RedefinableElement arg0)
    {
        return this.property.isRedefinitionContextValid(arg0);
    }

    /**
     * @param arg0
     * @return
     */
    public boolean isRequired(final Stereotype arg0)
    {
        return this.property.isRequired(arg0);
    }

    /**
     * @return
     */
    public boolean isStatic()
    {
        return this.property.isStatic();
    }

    /**
     * @return
     */
    public boolean isUnique()
    {
        return this.property.isUnique();
    }

    /**
     * @return
     */
    public int lower()
    {
        return this.property.lower();
    }

    /**
     * @return
     */
    public int lowerBound()
    {
        return this.property.lowerBound();
    }

    /**
     * @return
     */
    public boolean mustBeOwned()
    {
        return this.property.mustBeOwned();
    }

    /**
     * @return
     */
    public Property opposite()
    {
        return this.property.opposite();
    }

    /**
     * @return
     */
    public Set parameterableElements()
    {
        return this.property.parameterableElements();
    }

    /**
     * @return
     */
    public String qualifiedName()
    {
        return this.property.qualifiedName();
    }

    /**
     * @param arg0
     */
    public void removeKeyword(final String arg0)
    {
        this.property.removeKeyword(arg0);
    }

    /**
     * @return
     */
    public String separator()
    {
        return this.property.separator();
    }

    /**
     * @param arg0
     */
    public void setAggregation(final AggregationKind arg0)
    {
        this.property.setAggregation(arg0);
    }

    /**
     * @param arg0
     */
    public void setAssociation(final Association arg0)
    {
        this.property.setAssociation(arg0);
    }

    /**
     * @param arg0
     */
    public void setAssociationEnd(final Property arg0)
    {
        this.property.setAssociationEnd(arg0);
    }

    /**
     * @param arg0
     */
    public void setBooleanDefault(final boolean arg0)
    {
        this.property.setBooleanDefault(arg0);
    }

    /**
     * @param arg0
     */
    public void setDatatype(final DataType arg0)
    {
        this.property.setDatatype(arg0);
    }

    /**
     * @param arg0
     */
    public void setDefaultValue(final ValueSpecification arg0)
    {
        this.property.setDefaultValue(arg0);
    }

    /**
     * @param arg0
     */
    public void setIntegerDefault(final int arg0)
    {
        this.property.setIntegerDefault(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsDerived(final boolean arg0)
    {
        this.property.setIsDerived(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsDerivedUnion(final boolean arg0)
    {
        this.property.setIsDerivedUnion(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsLeaf(final boolean arg0)
    {
        this.property.setIsLeaf(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsOrdered(final boolean arg0)
    {
        this.property.setIsOrdered(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsReadOnly(final boolean arg0)
    {
        this.property.setIsReadOnly(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsStatic(final boolean arg0)
    {
        this.property.setIsStatic(arg0);
    }

    /**
     * @param arg0
     */
    public void setIsUnique(final boolean arg0)
    {
        this.property.setIsUnique(arg0);
    }

    /**
     * @param arg0
     */
    public void setLowerBound(final int arg0)
    {
        this.property.setLowerBound(arg0);
    }

    /**
     * @param arg0
     */
    public void setLowerValue(final ValueSpecification arg0)
    {
        this.property.setLowerValue(arg0);
    }

    /**
     * @param arg0
     */
    public void setName(final String arg0)
    {
        this.property.setName(arg0);
    }

    /**
     * @param arg0
     */
    public void setNameExpression(final StringExpression arg0)
    {
        this.property.setNameExpression(arg0);
    }

    /**
     * @param arg0
     */
    public void setNavigable(final boolean arg0)
    {
        this.property.setNavigable(arg0);
    }

    /**
     * @param arg0
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        this.property.setOwnedTemplateSignature(arg0);
    }

    /**
     * @param arg0
     */
    public void setOwningAssociation(final Association arg0)
    {
        this.property.setOwningAssociation(arg0);
    }

    /**
     * @param arg0
     */
    public void setOwningParameter(final TemplateParameter arg0)
    {
        this.property.setOwningParameter(arg0);
    }

    /**
     * @param arg0
     */
    public void setStringDefault(final String arg0)
    {
        this.property.setStringDefault(arg0);
    }

    /**
     * @param arg0
     */
    public void setTemplateParameter(final TemplateParameter arg0)
    {
        this.property.setTemplateParameter(arg0);
    }

    /**
     * @param arg0
     */
    public void setType(final Type arg0)
    {
        this.property.setType(arg0);
    }

    /**
     * @param arg0
     */
    public void setUnlimitedNaturalDefault(final int arg0)
    {
        this.property.setUnlimitedNaturalDefault(arg0);
    }

    /**
     * @param arg0
     */
    public void setUpperBound(final int arg0)
    {
        this.property.setUpperBound(arg0);
    }

    /**
     * @param arg0
     */
    public void setUpperValue(final ValueSpecification arg0)
    {
        this.property.setUpperValue(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
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
     * @param arg0
     */
    public void setVisibility(final VisibilityKind arg0)
    {
        this.property.setVisibility(arg0);
    }

    /**
     * @return
     */
    public Set subsettingContext()
    {
        return this.property.subsettingContext();
    }

    /**
     * @param arg0
     */
    public void unapply(final Stereotype arg0)
    {
        this.property.unapply(arg0);
    }

    /**
     * @return
     */
    public int upper()
    {
        return this.property.upper();
    }

    /**
     * @return
     */
    public int upperBound()
    {
        return this.property.upperBound();
    }

    /**
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
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
     * @param arg0
     * @param arg1
     * @return
     */
    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateVisibilityNeedsOwnership(
            arg0,
            arg1);
    }
}