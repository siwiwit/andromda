package org.andromda.cartridges.database.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.FilteredCollection;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.TableFacade.
 * 
 * @see org.andromda.cartridges.database.metafacades.TableFacade
 */
public class TableLogicImpl
    extends TableLogic
    implements org.andromda.cartridges.database.metafacades.Table
{
    // ---------------- constructor -------------------------------

    public TableLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableFacade#getForeignKeyConstraintAssociationEnds()
     */
    public java.util.Collection handleGetForeignKeyConstraintAssociationEnds()
    {
        return new FilteredCollection(this.getAssociationEnds())
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;
                AssociationEndFacade otherEnd = end.getOtherEnd();
                EntityFacade entity = (EntityFacade)otherEnd.getType();
                return end.isMany2One() || entity.isChild();
            }
        };
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableFacade#hasForeignKeyConstraints()
     */
    public boolean handleHasForeignKeyConstraints()
    {
        boolean hasForeignKeyConstraints = false;
        Collection associationEnds = this.getAssociationEnds();
        if (associationEnds != null && !associationEnds.isEmpty())
        {
            Iterator associationEndIt = associationEnds.iterator();
            while (associationEndIt.hasNext())
            {
                AssociationEndFacade end = (AssociationEndFacade)associationEndIt
                    .next();
                if (end != null)
                {
                    hasForeignKeyConstraints = end.getOtherEnd().isNavigable()
                        && (end.isMany2Many() || end.isOne2One() || end
                            .isOne2Many());
                    if (hasForeignKeyConstraints)
                    {
                        break;
                    }
                }
            }
        }
        return hasForeignKeyConstraints;
    }

}