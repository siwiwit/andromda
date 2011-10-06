package org.andromda.metafacades.emf.uml22;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
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
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Implementation of AttributeLink.
 *
 * We extend Slot. We keep a reference to the original slot and we defer
 * almost all method calls to it.
 *
 * @author Wouter Zoons
 */
public class AttributeLinkImpl implements AttributeLink
{
    /**
     *
     */
    final Slot slot;

    /**
     * @param slotIn
     */
    AttributeLinkImpl(final Slot slotIn)
    {
        this.slot = slotIn;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof AttributeLinkImpl)
        {
            return this.slot.equals(((AttributeLinkImpl)object).slot);
        }
        /*if (object instanceof LinkEndImpl)
        {
            return this.slot.equals(((LinkEndImpl)object).slot);
        }*/
        return this.slot.equals(object);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.slot.hashCode();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.slot.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getOwningInstance()
     */
    public InstanceSpecification getOwningInstance()
    {
        return this.slot.getOwningInstance();
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#setOwningInstance(org.eclipse.uml2.uml.InstanceSpecification)
     */
    public void setOwningInstance(final InstanceSpecification instanceSpecification)
    {
        this.slot.setOwningInstance(instanceSpecification);
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getValues()
     */
    public EList<ValueSpecification> getValues()
    {
        return this.slot.getValues();
    }

    /**
     * @param string
     * @return slot.getValue(string, null)
     */
    public ValueSpecification getValue(final String string)
    {
        return this.slot.getValue(string, null);
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getValue(String, org.eclipse.uml2.uml.Type)
     */
    public ValueSpecification getValue(final String string, final Type type)
    {
        return this.slot.getValue(string, type);
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getValue(String, org.eclipse.uml2.uml.Type, boolean, org.eclipse.emf.ecore.EClass, boolean)
     */
    public ValueSpecification getValue(final String name, final Type type, final boolean ignoreCase, final EClass eClass, final boolean createOnDemand)
    {
        return this.slot.getValue(name, type, ignoreCase, eClass, createOnDemand);
    }

    /**
     * @param eClass
     * @return slot.createValue(null, null, eClass)
     */
    public ValueSpecification createValue(final EClass eClass)
    {
        return this.slot.createValue(null, null, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#createValue(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createValue(final String name, final Type type, final EClass eClass)
    {
        return this.slot.createValue(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getDefiningFeature()
     */
    public StructuralFeature getDefiningFeature()
    {
        return this.slot.getDefiningFeature();
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#setDefiningFeature(org.eclipse.uml2.uml.StructuralFeature)
     */
    public void setDefiningFeature(final StructuralFeature structuralFeature)
    {
        this.slot.setDefiningFeature(structuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(final EStructuralFeature eStructuralFeature, final boolean resolve)
    {
        return this.slot.eGet(eStructuralFeature, resolve);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, Object)
     */
    public void eSet(final EStructuralFeature eStructuralFeature, final Object object)
    {
        this.slot.eSet(eStructuralFeature, object);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature eStructuralFeature)
    {
        this.slot.eUnset(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature eStructuralFeature)
    {
        return this.slot.eIsSet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedElements()
     */
    public EList<Element> getOwnedElements()
    {
        return this.slot.getOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.slot.getOwner();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedComments()
     */
    public EList<Comment> getOwnedComments()
    {
        return this.slot.getOwnedComments();
    }

    /**
     * @param eClass
     * @return slot.createOwnedComment()
     */
    public Comment createOwnedComment(final EClass eClass)
    {
        return this.slot.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.slot.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(final DiagnosticChain diagnosticChain, final Map<Object, Object> context)
    {
        return this.slot.validateHasOwner(diagnosticChain, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(final DiagnosticChain diagnosticChain, final Map<Object, Object> context)
    {
        return this.slot.validateNotOwnSelf(diagnosticChain, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#allOwnedElements()
     */
    public EList<Element> allOwnedElements()
    {
        return this.slot.allOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.slot.mustBeOwned();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(final String string)
    {
        return this.slot.createEAnnotation(string);
    }

    /**
     * @param stereotype
     */
    public void apply(final Stereotype stereotype)
    {
        this.slot.applyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(final Stereotype stereotype)
    {
        return this.slot.applyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(final String string)
    {
        return this.slot.getApplicableStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotypes()
     */
    public EList<Stereotype> getApplicableStereotypes()
    {
        return this.slot.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(final String string)
    {
        return this.slot.getAppliedStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotype(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Stereotype getAppliedSubstereotype(final Stereotype stereotype, final String string)
    {
        return this.slot.getAppliedSubstereotype(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotypes()
     */
    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.slot.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(final Stereotype stereotype)
    {
        return this.slot.getAppliedSubstereotypes(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getModel()
     */
    public Model getModel()
    {
        return (Model) UmlUtilities.findModel(this.slot);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getNearestPackage()
     */
    public Package getNearestPackage()
    {
        return this.slot.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Object getValue(final Stereotype stereotype, final String string)
    {
        return this.slot.getValue(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(final Stereotype stereotype)
    {
        return this.slot.isStereotypeApplicable(stereotype);
    }

    /**
     * @param stereotype
     * @return slot.isStereotypeApplied(stereotype)
     */
    @Deprecated
    public boolean isApplied(final Stereotype stereotype)
    {
        return this.slot.isStereotypeApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(final Stereotype stereotype)
    {
        return this.slot.isStereotypeApplied(stereotype);
    }

    /**
     * @param stereotype
     * @return slot.isStereotypeRequired(stereotype)
     */
    @Deprecated
    public boolean isRequired(final Stereotype stereotype)
    {
        return this.slot.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(final Stereotype stereotype)
    {
        return this.slot.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#setValue(org.eclipse.uml2.uml.Stereotype, String, Object)
     */
    public void setValue(final Stereotype stereotype, final String string, final Object object)
    {
        this.slot.setValue(stereotype, string, object);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public boolean hasValue(final Stereotype stereotype, final String string)
    {
        return this.slot.hasValue(stereotype, string);
    }

    /**
     * @param stereotype
     */
    public void unapply(final Stereotype stereotype)
    {
        this.slot.unapplyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#unapplyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject unapplyStereotype(final Stereotype stereotype)
    {
        return this.slot.unapplyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#destroy()
     */
    public void destroy()
    {
        this.slot.destroy();
    }

    // TODO: Map getAppliedVersion?
    /*public String getAppliedVersion(Stereotype stereotype)
    {
        return this.slot.getAppliedVersion(stereotype);
    }*/

    /**
     * @see org.eclipse.uml2.uml.Element#addKeyword(String)
     */
    public boolean addKeyword(final String string)
    {
        return this.slot.addKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getKeywords()
     */
    public EList<String> getKeywords()
    {
        return this.slot.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasKeyword(String)
     */
    public boolean hasKeyword(final String string)
    {
        return this.slot.hasKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#removeKeyword(String)
     */
    public boolean removeKeyword(final String string)
    {
        return this.slot.removeKeyword(string);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList<EAnnotation> getEAnnotations()
    {
        return this.slot.getEAnnotations();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(final String string)
    {
        return this.slot.getEAnnotation(string);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList<Adapter> eAdapters()
    {
        return this.slot.eAdapters();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        return this.slot.eDeliver();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(final boolean deliver)
    {
        this.slot.eSetDeliver(deliver);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        return this.slot.eIsProxy();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        return this.slot.eClass();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        return this.slot.eContainer();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList<EObject> eContents()
    {
        return this.slot.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList<EObject> eCrossReferences()
    {
        return this.slot.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator<EObject> eAllContents()
    {
        return this.slot.eAllContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        return this.slot.eContainmentFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.slot.eContainingFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        return this.slot.eResource();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(final EStructuralFeature eStructuralFeature)
    {
        return this.slot.eGet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification notification)
    {
        this.slot.eNotify(notification);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        return this.slot.getRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(final EClass eClass)
    {
        return this.slot.getRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(final String qualifiedName)
    {
        return this.slot.getRequiredStereotype(qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        return this.slot.getRequiredStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        return this.slot.getSourceDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(final EClass eClass)
    {
        return this.slot.getSourceDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(final Stereotype stereotype)
    {
        return this.slot.getStereotypeApplication(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        return this.slot.getStereotypeApplications();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        return this.slot.getTargetDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(final EClass eClass)
    {
        return this.slot.getTargetDirectedRelationships(eClass);
    }

    /**
     * UML2 3.1 (Eclipse 3.6) only
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    public Object eInvoke(final EOperation operation, final EList<?> arguments) throws InvocationTargetException
    {
        return this.slot.eInvoke(operation, arguments);
    }
}
