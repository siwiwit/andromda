package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.behavioralelements.usecases.UseCase
 *
 *
 */
public abstract class StrutsUseCaseDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.behavioralelements.usecases.UseCase
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.behavioralelements.usecases.UseCase  metaObject;

    public StrutsUseCaseDecorator (org.omg.uml.behavioralelements.usecases.UseCase metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
    // from org.omg.uml.behavioralelements.usecases.UseCase
    public java.util.Collection getExtend()
    {
        return metaObject.getExtend ();
    }

    // from org.omg.uml.behavioralelements.usecases.UseCase
    public java.util.Collection getExtensionPoint()
    {
        return metaObject.getExtensionPoint ();
    }

    // from org.omg.uml.behavioralelements.usecases.UseCase
    public java.util.Collection getInclude()
    {
        return metaObject.getInclude ();
    }

    // from org.omg.uml.foundation.core.Classifier
    public java.util.List getFeature()
    {
        return metaObject.getFeature ();
    }

    // from org.omg.uml.foundation.core.Classifier
    public java.util.Collection getPowertypeRange()
    {
        return metaObject.getPowertypeRange ();
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public java.util.Collection getGeneralization()
    {
        return metaObject.getGeneralization ();
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public boolean isAbstract()
    {
        return metaObject.isAbstract ();
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public boolean isLeaf()
    {
        return metaObject.isLeaf ();
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public boolean isRoot()
    {
        return metaObject.isRoot ();
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public void setAbstract(boolean p0)
    {
        metaObject.setAbstract (p0);
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public void setLeaf(boolean p0)
    {
        metaObject.setLeaf (p0);
    }

    // from org.omg.uml.foundation.core.GeneralizableElement
    public void setRoot(boolean p0)
    {
        metaObject.setRoot (p0);
    }

    // from org.omg.uml.foundation.core.Namespace
    public java.util.Collection getOwnedElement()
    {
        return metaObject.getOwnedElement ();
    }

}
