package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public abstract class StrutsInputFieldDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.foundation.core.Attribute
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.foundation.core.Attribute  metaObject;

    public StrutsInputFieldDecorator (org.omg.uml.foundation.core.Attribute metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.Boolean isReadOnly();
    
   /**
    *
    */
    public abstract java.lang.Integer getMaximumLength();
    
   /**
    *
    */
    public abstract java.lang.Boolean isRequired();
    
    // ------------- relations ------------------
    
        
    protected abstract org.omg.uml.foundation.core.ModelElement handleGetJsp();

   /**
    *
    */
    public org.omg.uml.foundation.core.Classifier getJsp()
    {
        return (org.omg.uml.foundation.core.Classifier) decoratedElement(handleGetJsp());
    }

    // ------------------------------------------------------------


    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.foundation.core.Attribute
    public org.omg.uml.foundation.core.AssociationEnd getAssociationEnd()
    {
        return metaObject.getAssociationEnd ();
    }

    // from org.omg.uml.foundation.core.Attribute
    public org.omg.uml.foundation.datatypes.Expression getInitialValue()
    {
        return metaObject.getInitialValue ();
    }

    // from org.omg.uml.foundation.core.Attribute
    public void setAssociationEnd(org.omg.uml.foundation.core.AssociationEnd p0)
    {
        metaObject.setAssociationEnd (p0);
    }

    // from org.omg.uml.foundation.core.Attribute
    public void setInitialValue(org.omg.uml.foundation.datatypes.Expression p0)
    {
        metaObject.setInitialValue (p0);
    }

    // from org.omg.uml.foundation.core.Feature
    public org.omg.uml.foundation.core.Classifier getOwner()
    {
        return metaObject.getOwner ();
    }

    // from org.omg.uml.foundation.core.Feature
    public org.omg.uml.foundation.datatypes.ScopeKind getOwnerScope()
    {
        return metaObject.getOwnerScope ();
    }

    // from org.omg.uml.foundation.core.Feature
    public void setOwner(org.omg.uml.foundation.core.Classifier p0)
    {
        metaObject.setOwner (p0);
    }

    // from org.omg.uml.foundation.core.Feature
    public void setOwnerScope(org.omg.uml.foundation.datatypes.ScopeKind p0)
    {
        metaObject.setOwnerScope (p0);
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public org.omg.uml.foundation.datatypes.ChangeableKind getChangeability()
    {
        return metaObject.getChangeability ();
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public org.omg.uml.foundation.datatypes.Multiplicity getMultiplicity()
    {
        return metaObject.getMultiplicity ();
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public org.omg.uml.foundation.datatypes.OrderingKind getOrdering()
    {
        return metaObject.getOrdering ();
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public org.omg.uml.foundation.datatypes.ScopeKind getTargetScope()
    {
        return metaObject.getTargetScope ();
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public org.omg.uml.foundation.core.Classifier getType()
    {
        return metaObject.getType ();
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public void setChangeability(org.omg.uml.foundation.datatypes.ChangeableKind p0)
    {
        metaObject.setChangeability (p0);
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public void setMultiplicity(org.omg.uml.foundation.datatypes.Multiplicity p0)
    {
        metaObject.setMultiplicity (p0);
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public void setOrdering(org.omg.uml.foundation.datatypes.OrderingKind p0)
    {
        metaObject.setOrdering (p0);
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public void setTargetScope(org.omg.uml.foundation.datatypes.ScopeKind p0)
    {
        metaObject.setTargetScope (p0);
    }

    // from org.omg.uml.foundation.core.StructuralFeature
    public void setType(org.omg.uml.foundation.core.Classifier p0)
    {
        metaObject.setType (p0);
    }

}
