package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;



/**
 * Metaclass decorator implementation for org.omg.uml.foundation.core.UmlAssociation
 *
 * @author Chad Brandon
 */
public class AssociationDecoratorImpl extends AssociationDecorator
{
    // ---------------- constructor -------------------------------
    
    public AssociationDecoratorImpl (org.omg.uml.foundation.core.UmlAssociation metaObject)
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

    // ------------------------------------------------------------

}
