package org.andromda.cartridges.database.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.cartridges.database.DatabaseProfile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.Table.
 *
 * @see org.andromda.cartridges.database.metafacades.Table
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
     * @see org.andromda.cartridges.database.metafacades.Table#getInitialLoadSize()
     */
    protected int handleGetDummyLoadSize()
    {
        /*
         * comment out until enumeration support is added. for enumerations we
         * always have a fixed size if (isEnumeration()) { return
         * getLiterals().size(); }
         */

        int dummyLoadSize = 0;
        float dummyLoadMultiplier = 0;

        // first get the initial load size for this table
        try
        {
            final String initialLoadSizeString = (String)this
                .findTaggedValue(DatabaseProfile.TAGGEDVALUE_DUMMYLOAD_SIZE);
            if (initialLoadSizeString != null)
            {
                dummyLoadSize = Integer.parseInt(initialLoadSizeString);
            }
        }
        catch (Exception e)
        {
            // do nothing, let the 'finally' clause handle it
        }
        finally
        {
            if (dummyLoadSize <= 0)
            {
                dummyLoadSize = DatabaseProfile.DUMMY_LOAD_SIZE_DEFAULT;
            }
        }

        // if a multiplier has been specified apply it on the load size
        try
        {
            final String multiplierString = (String)getConfiguredProperty(DatabaseGlobals.DUMMYLOAD_MULTIPLIER);
            if (multiplierString != null)
            {
                dummyLoadMultiplier = Float.parseFloat(multiplierString);
            }
        }
        catch (Exception e)
        {
            // do nothing, let the 'finally' clause handle it
        }
        finally
        {
            if (dummyLoadMultiplier <= 0)
            {
                dummyLoadMultiplier = DatabaseGlobals.DUMMYLOAD_MULTIPLIER_DEFAULT;
            }
        }

        return (int)(dummyLoadSize * dummyLoadMultiplier);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getForeignKeyColumns()
     */
    protected java.util.Collection handleGetForeignKeyColumns()
    {
        Collection foreignKeyColumns = new ArrayList();

        Collection associationEnds = this.getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade end = (AssociationEndFacade)iterator.next();
            if (ForeignKeyColumn.class.isAssignableFrom(end.getClass()))
            {
                ForeignKeyColumn column = (ForeignKeyColumn)end;
                ForeignKeyColumn otherEnd = (ForeignKeyColumn)end.getOtherEnd();
                boolean thisSideNavigable = column.isNavigable()
                    && !otherEnd.isNavigable();
                boolean aggregationPresent = end.isAggregation()
                    || end.isComposition();
                boolean otherEndAggregationPresent = otherEnd.isAggregation()
                    || otherEnd.isComposition();
                boolean one2One = end.isOne2One()
                    && ((aggregationPresent && !otherEnd.isForeignIdentifier())
                    || column.isForeignIdentifier()
                    || (!otherEndAggregationPresent && thisSideNavigable));
                if ((otherEnd.isNavigable() && end.isMany2One()) || one2One)
                {
                    if (column.isForeignIdentifier())
                    {
                        Column columntest = (Column)this.getIdentifierForeignKeyColumns().iterator().next();
                        foreignKeyColumns.add(columntest);
                    }
                    else
                    {
                        foreignKeyColumns.add(otherEnd);
                    }
                }
            }
        }

        return foreignKeyColumns;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getPrimaryKeyColumns()
     */
    protected Object handleGetPrimaryKeyColumn()
    {
        Collection identifiers = this.getIdentifiers();
        return identifiers.isEmpty() ? null : identifiers.iterator().next();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getImportingTables()
     */
    protected java.util.Collection handleGetImportingTables()
    {
        Collection importingTables = new HashSet();

        Collection associationEnds = this.getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade)iterator
                .next();
            if (associationEnd.isOne2Many())
            {
                importingTables.add(associationEnd.getOtherEnd().getType());
            }
        }
        return importingTables;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getImportedTables()
     */
    protected java.util.Collection handleGetImportedTables()
    {
        Collection importedTables = new HashSet();

        Collection foreignKeyColumns = this.getForeignKeyColumns();
        for (Iterator iterator = foreignKeyColumns.iterator(); iterator
            .hasNext();)
        {
            ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn)iterator
                .next();
            importedTables.add(foreignKeyColumn.getTable());
        }

        return importedTables;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#isForeignKeyColumnsPresent()
     */
    protected boolean handleIsForeignKeyColumnsPresent()
    {
        return !this.getForeignKeyColumns().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getPrimaryKeyConstraintName()
     */
    protected String handleGetPrimaryKeyConstraintName()
    {
        return DatabaseMetafacadeUtils
            .toSqlIdentifierName(
                this
                    .getConfiguredProperty(DatabaseGlobals.PRIMARY_KEY_CONSTRAINT_PREFIX),
                this,
                this.getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getNonForeignKeyColumns()
     */
    protected Collection handleGetNonForeignKeyColumns()
    {
        return getAttributes();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getIdentifierForeignKeyColumns()
     */
    protected Collection handleGetIdentifierForeignKeyColumns()
    {
        Collection columns = null;
        EntityAssociationEndFacade end =
            (EntityAssociationEndFacade)CollectionUtils.find(
                this.getAssociationEnds(),
                new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    if (EntityAssociationEndFacade.class
                        .isAssignableFrom(object.getClass()))
                    {
                        valid = ((EntityAssociationEndFacade)object)
                            .isForeignIdentifier();
                    }
                    return valid;
                }
            });
        if (end != null && EntityAssociationEndFacade.class.isAssignableFrom(end.getClass()))
        {
            columns = ((Table)end.getType()).getIdentifiers();
        }
        return columns;
    }
}
