package org.andromda.cartridges.ejb.metafacades;


/**
 * <p>
 *  Represents an EJB association end.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class EJBAssociationEndFacadeLogicImpl
       extends EJBAssociationEndFacadeLogic
       implements org.andromda.cartridges.ejb.metafacades.EJBAssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public EJBAssociationEndFacadeLogicImpl (org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super (metaObject);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBAssociationEndDecorator#getRelationType()
     */
    public String getRelationType() {
        String targetType;
        if (this.isMany2Many() || this.isOne2Many()) {
            targetType = "java.util.Collection";
        } else {
            targetType = this.getOtherEnd().getType().getFullyQualifiedName();
        }
        return targetType;
    }

    // ------------- relations ------------------

}
