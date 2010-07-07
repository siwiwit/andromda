package org.andromda.utils.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.utils.beans.SortCriteria.Ordering;
import org.andromda.utils.beans.comparators.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * Provides bean sorting capabilities.
 *
 * <p>
 * Sorts passed in Collections and returns
 * sorted Lists.  Performs sorting for any
 * Class that has an associated Comparator implementation defined.  If
 * the Collection that is passed in, is not an instance of List, it will create
 * a new ArrayList with the contents of the passed in Collection, before
 * sorting occurs (since sorting can only be done on a java.util.List).
 * </p>
 *
 * @author Chad Brandon
 */
public class BeanSorter
{
    /**
     * Performs sorting of the collection by one property to
     * sort with more than one property see {@link #sort(java.util.Collection, SortCriteria[])}.
     *
     * @see #sort(java.util.Collection, SortCriteria[])
     *
     * @param <T> Collection type
     * @param beans the Collection of PersistentObjects to sort
     * @param sortBy the property to sort by (i.e. firstName, etc). Can
     *        be a nested property such as 'person.address.street'.
     * @param ordering the ordering of the sorting (either {@link Ordering#ASCENDING}
     *        or {@link Ordering#DESCENDING})
     * @return the sorted List
     */
    public static <T> List<T> sort(
        final Collection<T> beans,
        final String sortBy,
        final Ordering ordering)
    {
        return sort(
            beans,
            new SortCriteria[] {new SortCriteria(
                    sortBy,
                    ordering)});
    }

    /**
     * Performs sorting of the collection by one property to
     * sort with more than one property see {@link #sort(java.util.Collection, SortCriteria[])}.
     *
     * @see #sort(java.util.Collection, SortCriteria[])
     *
     * @param <T> Collection type
     * @param beans the Collection of PersistentObjects to sort
     * @param sortBy the property to sort by (i.e. firstName, etc). Can
     *        be a nested property such as 'person.address.street'.
     * @param nullsFirst a flag indicating whether or not null values should be sorted to the beginning
     *        or the ending of the list.
     * @return the sorted List
     */
    public static <T> List<T> sort(
        final Collection<T> beans,
        final String sortBy,
        final boolean nullsFirst)
    {
        return sort(
            beans,
            new SortCriteria[] {new SortCriteria(
                    sortBy,
                    nullsFirst)});
    }

    /**
     * Performs sorting of the collection by one property to
     * sort with more than one property see {@link #sort(java.util.Collection, SortCriteria[])}.
     *
     * @see #sort(java.util.Collection, SortCriteria[])
     *
     * @param <T> Collection type
     * @param beans the Collection of PersistentObjects to sort
     * @param sortBy the property to sort by (i.e. firstName, etc). Can
     *        be a nested property such as 'person.address.street'.
     * @param ordering the ordering of the sorting (either {@link Ordering#ASCENDING}
     *        or {@link Ordering#DESCENDING})
     * @param nullsFirst a flag indicating whether or not null values should be sorted to the beginning
     *        or the ending of the list.
     * @return the sorted List
     */
    public static <T> List<T> sort(
        final Collection<T> beans,
        final String sortBy,
        final Ordering ordering,
        final boolean nullsFirst)
    {
        return sort(
            beans,
            new SortCriteria[] {new SortCriteria(
                    sortBy,
                    ordering,
                    nullsFirst)});
    }

    /**
     * <p>
     * Sorts the passed in Collection and returns
     * a sorted List.  Performs SQL like sorting for any
     * Class that has an associated Comparator implementation defined.  If
     * the Collection that is passed in, is not an instance of List, it will create
     * a new ArrayList with the contents of the passed in Collection, before
     * sorting occurs.  Since sorting can only be done on a java.util.List.
     * </p>
     *
     * @param <T> Collection type
     * @param beans the Collection of PersistentObjects to sort
     * @param sortBy an array of SortCriteria.  This array of SortCriteria
     * specifies which attributes to sort by.
     * Attributes to sort by, MUST be simple attributes
     * (i.e. name, type, etc, they can not be complex objects, but the properties can be
     * nested simple types within associated beans).
     *
     * @return List the sorted List
     */
    public static <T> List<T> sort(
        final Collection<T> beans,
        final SortCriteria[] sortBy)
    {
        ExceptionUtils.checkNull(
            "beans",
            beans);
        ExceptionUtils.checkNull(
            "sortBy",
            sortBy);

        if (sortBy.length == 0)
        {
            throw new IllegalArgumentException("sortBy must contain at least one value by which to sort");
        }

        List<T> sorted = new ArrayList<T>(beans);
        
        int sortByNum = sortBy.length;

        // - use the Comparator chain to provide SQL like sorting of properties
        final ComparatorChain chain = new ComparatorChain();
        for (int ctr = 0; ctr < sortByNum; ctr++)
        {
            final SortCriteria orderBy = sortBy[ctr];
            chain.addComparator(new BeanComparator(orderBy));
        }

        Collections.sort(
            sorted,
            chain);

        return sorted;
    }
}