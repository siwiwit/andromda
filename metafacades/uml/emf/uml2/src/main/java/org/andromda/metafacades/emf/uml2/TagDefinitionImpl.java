package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.VisibilityKind;


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
    private Collection values;

    /**
     * Constructor
     *
     * @param name
     * @param value a single String value
     */
    public TagDefinitionImpl(
        final String name,
        final Object value)
    {
        this.name = name;
        this.values = new ArrayList();
        this.values.add(value);
    }

    /**
     * Generalized constructor.
     *
     * @param object
     * @param object2
     */
    public TagDefinitionImpl(
        final String name,
        final Collection values)
    {
        this.name = name;
        this.values = values;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.TagDefinition#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.TagDefinition#getValue()
     */
    public Object getValue()
    {
        return this.values.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.TagDefinition#getValues()
     */
    public Collection getValues()
    {
        return this.values;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.TagDefinition#toString()
     */
    public String toString()
    {
        StringBuffer out = new StringBuffer(this.name + ": ");
        for (Iterator it = this.values.iterator(); it.hasNext();)
        {
            out.append(it.next());
            out.append(it.hasNext() ? ", " : ".");
        }
        return out.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#setName(java.lang.String)
     */
    public void setName(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#setVisibility(org.eclipse.uml2.VisibilityKind)
     */
    public void setVisibility(final VisibilityKind arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getClientDependencies()
     */
    public EList getClientDependencies()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getClientDependency(java.lang.String)
     */
    public Dependency getClientDependency(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#setNameExpression(org.eclipse.uml2.StringExpression)
     */
    public void setNameExpression(final StringExpression arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#createNameExpression(org.eclipse.emf.ecore.EClass)
     */
    public StringExpression createNameExpression(final EClass arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#createNameExpression()
     */
    public StringExpression createNameExpression()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#allNamespaces()
     */
    public List allNamespaces()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#isDistinguishableFrom(org.eclipse.uml2.NamedElement,
     *      org.eclipse.uml2.Namespace)
     */
    public boolean isDistinguishableFrom(
        final NamedElement arg0,
        final Namespace arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#separator()
     */
    public String separator()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#qualifiedName()
     */
    public String qualifiedName()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#validateNoName(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateNoName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#validateQualifiedName(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateQualifiedName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getLabel()
     */
    public String getLabel()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#getLabel(boolean)
     */
    public String getLabel(final boolean arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.NamedElement#createDependency(org.eclipse.uml2.NamedElement)
     */
    public Dependency createDependency(final NamedElement arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#getTemplateBindings()
     */
    public EList getTemplateBindings()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding(org.eclipse.emf.ecore.EClass)
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding()
     */
    public TemplateBinding createTemplateBinding()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#getOwnedTemplateSignature()
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.TemplateSignature)
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature()
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.TemplateableElement#parameterableElements()
     */
    public Set parameterableElements()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getOwner()
     */
    public Element getOwner()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#createOwnedComment(org.eclipse.emf.ecore.EClass)
     */
    public Comment createOwnedComment(final EClass arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateNotOwnSelf(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain,
     *      java.util.Map)
     */
    public boolean validateHasOwner(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#allOwnedElements()
     */
    public Set allOwnedElements()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#createEAnnotation(java.lang.String)
     */
    public EAnnotation createEAnnotation(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#isApplied(org.eclipse.uml2.Stereotype)
     */
    public boolean isApplied(final Stereotype arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#isRequired(org.eclipse.uml2.Stereotype)
     */
    public boolean isRequired(final Stereotype arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getApplicableStereotypes()
     */
    public Set getApplicableStereotypes()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getApplicableStereotype(java.lang.String)
     */
    public Stereotype getApplicableStereotype(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getAppliedStereotypes()
     */
    public Set getAppliedStereotypes()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getAppliedStereotype(java.lang.String)
     */
    public Stereotype getAppliedStereotype(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#apply(org.eclipse.uml2.Stereotype)
     */
    public void apply(final Stereotype arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#unapply(org.eclipse.uml2.Stereotype)
     */
    public void unapply(final Stereotype arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getValue(org.eclipse.uml2.Stereotype,
     *      java.lang.String)
     */
    public Object getValue(
        final Stereotype arg0,
        final String arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#setValue(org.eclipse.uml2.Stereotype,
     *      java.lang.String, java.lang.Object)
     */
    public void setValue(
        final Stereotype arg0,
        final String arg1,
        final Object arg2)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#hasValue(org.eclipse.uml2.Stereotype,
     *      java.lang.String)
     */
    public boolean hasValue(
        final Stereotype arg0,
        final String arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getModel()
     */
    public Model getModel()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getNearestPackage()
     */
    public Package getNearestPackage()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#destroy()
     */
    public void destroy()
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getAppliedVersion(org.eclipse.uml2.Stereotype)
     */
    public String getAppliedVersion(final Stereotype arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#getKeywords()
     */
    public Set getKeywords()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#hasKeyword(java.lang.String)
     */
    public boolean hasKeyword(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#addKeyword(java.lang.String)
     */
    public void addKeyword(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.uml2.Element#removeKeyword(java.lang.String)
     */
    public void removeKeyword(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(java.lang.String)
     */
    public EAnnotation getEAnnotation(final String arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(final EStructuralFeature arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature,
     *      boolean)
     */
    public Object eGet(
        final EStructuralFeature arg0,
        final boolean arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature,
     *      java.lang.Object)
     */
    public void eSet(
        final EStructuralFeature arg0,
        final Object arg1)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        // TODO Raccord de m�thode auto-g�n�r�
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(final boolean arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification arg0)
    {
        // TODO Raccord de m�thode auto-g�n�r�
    }
}