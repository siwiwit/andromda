package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class AssociationFacadeLogicImpl
       extends AssociationFacadeLogic
       implements org.andromda.metafacades.uml.AssociationFacade
{
    // ---------------- constructor -------------------------------
    
    public AssociationFacadeLogicImpl (org.omg.uml.foundation.core.UmlAssociation metaObject)
    {
        super (metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml14.AssociationFacade#handleGetAssociationEnds()
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return metaObject.getConnection();
    }
    
    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacade#getName()
     */
    public String getRelationName() {
        Collection ends = this.getAssociationEnds();
        Iterator endIt = ends.iterator();
        AssociationEndFacade firstEnd = (AssociationEndFacade)endIt.next();
        AssociationEndFacade secondEnd = (AssociationEndFacade)endIt.next();
        String relationName = MetafacadeUtils.toRelationName(firstEnd.getName(), secondEnd.getName(), "-");
        return relationName;
    }

}