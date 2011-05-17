package org.andromda.metafacades.emf.uml22;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.VisibilityKind;


/**
 * Represents a TagDefinition metaclass (was needed because it doesn't exist in
 * the uml2 metamodel).
 *
 * @author Steve Jerman
 */
public class TagDefinitionImpl
    implements TagDefinition
{
    /**
     * The name of the tag.
     */
    private String name;

    /**
     * The value of the tag: collection of strings.
     */
    private Collection<Object> values;

    /**
     * Constructor
     *
     * @param nameIn
     * @param value a single String value
     */
    public TagDefinitionImpl(
        final String nameIn,
        final Object value)
    {
        this.name = nameIn;
        this.values = new ArrayList<Object>();
        this.values.add(value);
    }

    /**
     * Generalized constructor.
     * @param nameIn
     * @param valuesIn
     */
    public TagDefinitionImpl(
        final String nameIn,
        final Collection<Object> valuesIn)
    {
        this.name = nameIn;
        this.values = valuesIn;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TagDefinition#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TagDefinition#getValue()
     */
    public Object getValue()
    {
        return this.values.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TagDefinition#getValues()
     */
    public Collection<Object> getValues()
    {
        return this.values;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TagDefinition#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder(this.name).append(": ");
        for (Iterator<Object> it = this.values.iterator(); it.hasNext();)
        {
            out.append(it.next());
            out.append(it.hasNext() ? ", " : ".");
        }
        return out.toString();
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#setName(String)
     */
    public void setName(final String arg0)
    {
        this.name = arg0;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#setVisibility(org.eclipse.uml2.uml.VisibilityKind)
     */
    public void setVisibility(final VisibilityKind arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependencies()
     */
    public EList getClientDependencies()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String)
     */
    public Dependency getClientDependency(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#setNameExpression(org.eclipse.uml2.uml.StringExpression)
     */
    public void setNameExpression(final StringExpression arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @param arg0
     * @return null
     */
    public StringExpression createNameExpression(final EClass arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     */
    public StringExpression createNameExpression()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#allNamespaces()
     */
    public EList<Namespace> allNamespaces()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#isDistinguishableFrom(org.eclipse.uml2.uml.NamedElement,
     *      org.eclipse.uml2.uml.Namespace)
     */
    public boolean isDistinguishableFrom(
        final NamedElement arg0,
        final Namespace arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#separator()
     */
    public String separator()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     */
    public String qualifiedName()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @return false
     */
    public boolean validateNoName(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @return false
     */
    public boolean validateQualifiedName(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getLabel()
     */
    public String getLabel()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#getLabel(boolean)
     */
    public String getLabel(final boolean arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.NamedElement#createDependency(org.eclipse.uml2.uml.NamedElement)
     */
    public Dependency createDependency(final NamedElement arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getTemplateBindings()
     */
    public EList getTemplateBindings()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     * @return null
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     */
    public TemplateBinding createTemplateBinding()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getOwnedTemplateSignature()
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     * @see org.eclipse.uml2.uml.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.uml.TemplateSignature)
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @param arg0
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#createOwnedTemplateSignature()
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#parameterableElements()
     */
    public Set parameterableElements()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getOwner()
     */
    public Element getOwner()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     * @return null
     */
    public Comment createOwnedComment(final EClass arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateNotOwnSelf(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateHasOwner(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#allOwnedElements()
     */
    public EList<Element> allOwnedElements()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     * @return false
     */
    public boolean isApplied(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @param arg0
     * @return false
     */
    public boolean isRequired(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotypes()
     */
    public EList<Stereotype> getApplicableStereotypes()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotypes()
     */
    public EList<Stereotype> getAppliedStereotypes()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     */
    public void apply(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @param arg0
     */
    public void unapply(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns void
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getValue(org.eclipse.uml2.uml.Stereotype,
     *      String)
     */
    public Object getValue(
        final Stereotype arg0,
        final String arg1)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#setValue(org.eclipse.uml2.uml.Stereotype,
     *      String, Object)
     */
    public void setValue(
        final Stereotype arg0,
        final String arg1,
        final Object arg2)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#hasValue(org.eclipse.uml2.uml.Stereotype,
     *      String)
     */
    public boolean hasValue(
        final Stereotype arg0,
        final String arg1)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getModel()
     */
    public Model getModel()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getNearestPackage()
     */
    public Package getNearestPackage()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#destroy()
     */
    public void destroy()
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     * @param arg0
     * @return null
     */
    public String getAppliedVersion(final Stereotype arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#getKeywords()
     */
    public EList<String> getKeywords()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#hasKeyword(String)
     */
    public boolean hasKeyword(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#addKeyword(String)
     */
    public boolean addKeyword(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.uml2.uml.Element#removeKeyword(String)
     */
    public boolean removeKeyword(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(final String arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(final EStructuralFeature arg0)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature,
     *      boolean)
     */
    public Object eGet(
        final EStructuralFeature arg0,
        final boolean arg1)
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature,
     *      Object)
     */
    public void eSet(
        final EStructuralFeature arg0,
        final Object arg1)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature arg0)
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        // TODO Implement autogenerated method - returns null
        return null;
    }

    /**
     *
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        // TODO Implement autogenerated method - returns null
        return false;
    }

    /**
     *
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(final boolean arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     *
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification arg0)
    {
        // TODO Implement autogenerated method - returns null
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allOwningPackages()
     */
    public EList<Package> allOwningPackages()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createNameExpression(String, org.eclipse.uml2.uml.Type)
     */
    public StringExpression createNameExpression(String name, Type type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createUsage(org.eclipse.uml2.uml.NamedElement)
     */
    public Usage createUsage(NamedElement supplier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Dependency getClientDependency(String name, boolean ignoreCase, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetName()
     */
    public boolean isSetName()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetVisibility()
     */
    public boolean isSetVisibility()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetName()
     */
    public void unsetName()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetVisibility()
     */
    public void unsetVisibility()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasNoQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotype(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(String qualifiedName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#unapplyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject unapplyStereotype(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * UML2 3.1 (Eclipse 3.6) only
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    public Object eInvoke(EOperation arg0, EList<?> arg1) throws InvocationTargetException
    {
        return null; //this.property.eInvoke(arg0, arg1);
    }
}
