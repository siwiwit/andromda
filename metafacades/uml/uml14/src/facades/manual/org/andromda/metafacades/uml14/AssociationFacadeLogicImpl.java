package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.metadecorators.uml14.AssociationEndDecorator;


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
     * @see org.andromda.core.metadecorators.uml14.AssociationDecorator#handleGetAssociationEnds()
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return metaObject.getConnection();
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getName()
     */
    public String getRelationName() {
        Collection ends = this.getAssociationEnds();
        Iterator endIt = ends.iterator();
        AssociationEndDecorator firstEnd = (AssociationEndDecorator)endIt.next();
        AssociationEndDecorator secondEnd = (AssociationEndDecorator)endIt.next();
        String relationName = MetafacadeUtils.toRelationName(firstEnd.getName(), secondEnd.getName(), "-");
        return relationName;
    }

}
