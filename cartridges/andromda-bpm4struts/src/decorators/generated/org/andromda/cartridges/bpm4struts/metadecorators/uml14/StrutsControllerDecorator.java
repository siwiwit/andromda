package org.andromda.cartridges.bpm4struts.metadecorators.uml14;


        /**
 *
 * Metaclass decorator for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public abstract class StrutsControllerDecorator
       extends    org.andromda.core.metadecorators.uml14.ModelElementDecoratorImpl
       implements org.omg.uml.foundation.core.Classifier
{
    // -------- link to decorated metaobject ----------
    
    protected org.omg.uml.foundation.core.Classifier  metaObject;

    public StrutsControllerDecorator (org.omg.uml.foundation.core.Classifier metaObject)
    {
        super (metaObject);
        this.metaObject = metaObject;
    }
    
    // --------------- attributes ---------------------


    // ---------------- real business methods ----------------------

   /**
    *
    */
    public abstract java.lang.String getControllerClassName();
    
   /**
    *
    */
    public abstract java.lang.String getControllerName();
    
    // ------------- relations ------------------
    

    // ---------------- decorating methods ----------------------
    
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
