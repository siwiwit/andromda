package org.andromda.translation.ocl.validation;

import org.apache.commons.lang.BooleanUtils;


/**
 * Used to translate OCL expressions to their corresponding Java expressions.
 */
public final class OCLExpressions
{
    /**
     * @param first
     * @param second
     * @return first.equals(second)
     */
    public static boolean equal(
            final Object first,
            final Object second)
    {
        return (first == null) ? (second == null) : first.equals(second);
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final boolean first,
            final boolean second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final int first,
            final int second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final short first,
            final short second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final byte first,
            final byte second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final char first,
            final char second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final float first,
            final float second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final double first,
            final double second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final long first,
            final long second)
    {
        return first == second;
    }

    /**
     * @param first
     * @param second
     * @return first == second
     */
    public static boolean equal(
            final Object first,
            final boolean second)
    {
        return (second) ? Boolean.TRUE.equals(first) || BooleanUtils.toBoolean(String.valueOf(first))
                : Boolean.FALSE.equals(first) || !BooleanUtils.toBoolean(String.valueOf(first));
    }

    /**
     * @param first
     * @param second
     * @return equal(second, first)
     */
    public static boolean equal(
            final boolean first,
            final Object second)
    {
        return equal(second, first);
    }

    /**
     * @param first
     * @param second
     * @return !first.equals(second)
     */
    public static boolean notEqual(
            final Object first,
            final Object second)
    {
        return first == null ? second != null : !first.equals(second);
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final boolean first,
            final boolean second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return equal
     */
    public static boolean equal(
            final Object first,
            final long second)
    {
        boolean equal = first instanceof Comparable;
        if (equal)
        {
            final Comparable comparable = (Comparable) first;
            if (comparable instanceof Integer)
            {
                equal = comparable.compareTo(Integer.valueOf((int) second)) > 0;
            } 
            else
            {
                equal = comparable.equals(Long.valueOf(second));
            }
        }
        return equal;
    }

    /**
     * @param first
     * @param second
     * @return equal
     */
    public static boolean equal(
            final Object first,
            final int second)
    {
        boolean equal = first instanceof Comparable;
        if (equal)
        {
            final Comparable comparable = (Comparable) first;
            equal = comparable.equals(Integer.valueOf(second));
        }
        return equal;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final int first,
            final int second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final short first,
            final short second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final byte first,
            final byte second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final char first,
            final char second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final float first,
            final float second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final double first,
            final double second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return first != second
     */
    public static boolean notEqual(
            final long first,
            final long second)
    {
        return first != second;
    }

    /**
     * @param first
     * @param second
     * @return !equal(first, second)
     */
    public static boolean notEqual(
            final Object first,
            final boolean second)
    {
        return !equal(first, second);
    }

    /**
     * @param first
     * @param second
     * @return notEqual(second, first)
     */
    public static boolean notEqual(
            final boolean first,
            final Object second)
    {
        return notEqual(second, first);
    }

    /**
     * @param first
     * @param second
     * @return first.compareTo(second) < 0
     */
    public static boolean less(
            final Comparable first,
            final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) < 0;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final int first,
            final int second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final short first,
            final short second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final byte first,
            final byte second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final char first,
            final char second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final float first,
            final float second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final double first,
            final double second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first < second
     */
    public static boolean less(
            final long first,
            final long second)
    {
        return first < second;
    }

    /**
     * @param first
     * @param second
     * @return first.compareTo(second) <= 0
     */
    public static boolean lessOrEqual(
            final Comparable first,
            final Comparable second)
    {
        return first == null ? second == null : (first.compareTo(second) <= 0);
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, (Comparable) second)
     */
    public static boolean lessOrEqual(
            final Object first,
            final Object second)
    {
        boolean lessOrEqual = first instanceof Comparable && second instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual((Comparable) first, (Comparable) second);
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final int first,
            final int second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final short first,
            final short second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final byte first,
            final byte second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final char first,
            final char second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final float first,
            final float second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, Float.valueOf(second))
     */
    public static boolean lessOrEqual(
            final Object first,
            final float second)
    {
        boolean lessOrEqual = first instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual((Comparable) first, Float.valueOf(second));
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual(new Float(first), (Comparable) second)
     */
    public static boolean lessOrEqual(
            final float first,
            final Object second)
    {
        boolean lessOrEqual = second instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual(new Float(first), (Comparable) second);
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final double first,
            final double second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, Double.valueOf(second))
     */
    public static boolean lessOrEqual(
            final Object first,
            final double second)
    {
        boolean lessOrEqual = first instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual((Comparable) first, Double.valueOf(second));
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual(Double.valueOf(first), (Comparable) second)
     */
    public static boolean lessOrEqual(
            final double first,
            final Object second)
    {
        boolean lessOrEqual = second instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual(Double.valueOf(first), (Comparable) second);
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return first <= second
     */
    public static boolean lessOrEqual(
            final long first,
            final long second)
    {
        return first <= second;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, Long.valueOf(second))
     */
    public static boolean lessOrEqual(
            final Object first,
            final long second)
    {
        boolean lessOrEqual = first instanceof Comparable;
        if (lessOrEqual)
        {
            if (first instanceof Integer)
            {
                lessOrEqual = lessOrEqual((Comparable) first, Integer.valueOf((int) second));
            } 
            else
            {
                lessOrEqual = lessOrEqual((Comparable) first, Long.valueOf(second));
            }
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, Integer.valueOf(second))
     */
    public static boolean lessOrEqual(
            final Object first,
            final int second)
    {
        boolean lessOrEqual = first instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual((Comparable) first, Integer.valueOf(second));
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual((Comparable) first, Short.valueOf(second))
     */
    public static boolean lessOrEqual(
            final Object first,
            final short second)
    {
        boolean lessOrEqual = first instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual((Comparable) first, Short.valueOf(second));
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual = lessOrEqual(Long.valueOf(first), (Comparable) second)
     */
    public static boolean lessOrEqual(
            final long first,
            final Object second)
    {
        boolean lessOrEqual = second instanceof Comparable;
        if (lessOrEqual)
        {
            if (second instanceof Integer)
            {
                lessOrEqual = lessOrEqual(Integer.valueOf((int) first), (Comparable) second);
            } 
            else
            {
                lessOrEqual = lessOrEqual(Long.valueOf(first), (Comparable) second);
            }
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual(new Integer(first), (Comparable) second)
     */
    public static boolean lessOrEqual(
            final int first,
            final Object second)
    {
        boolean lessOrEqual = second instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual(new Integer(first), (Comparable) second);
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return lessOrEqual(new Short(first), (Comparable) second)
     */
    public static boolean lessOrEqual(
            final short first,
            final Object second)
    {
        boolean lessOrEqual = second instanceof Comparable;
        if (lessOrEqual)
        {
            lessOrEqual = lessOrEqual(new Short(first), (Comparable) second);
        }
        return lessOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return first.compareTo(second) > 0
     */
    public static boolean greater(
            final Comparable first,
            final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) > 0;
    }

    /**
     * @param first
     * @param second
     * @return comparable.compareTo(Long.valueOf(second)) > 0
     */
    public static boolean greater(
            final Object first,
            final long second)
    {
        boolean greater = first instanceof Comparable;
        if (greater)
        {
            final Comparable comparable = (Comparable) first;
            if (comparable instanceof Integer)
            {
                greater = comparable.compareTo(Integer.valueOf((int) second)) > 0;
            } else
            {
                greater = comparable.compareTo(Long.valueOf(second)) > 0;
            }
        }
        return greater;
    }

    /**
     * @param first
     * @param second
     * @return comparable.compareTo(Long.valueOf(second)) > 0
     */
    public static boolean greater(
            final Object first,
            final int second)
    {
        boolean greater = first instanceof Comparable;
        if (greater)
        {
            final Comparable comparable = (Comparable) first;
            if (comparable instanceof Integer)
            {
                greater = comparable.compareTo(Integer.valueOf(second)) > 0;
            } else
            {
                greater = comparable.compareTo(Long.valueOf(second)) > 0;
            }
        }
        return greater;
    }

    /**
     * @param first
     * @param second
     * @return first > second
     */
    public static boolean greater(
            final byte first,
            final byte second)
    {
        return first > second;
    }

    /**
     * @param first
     * @param second
     * @return first > second
     */
    public static boolean greater(
            final char first,
            final char second)
    {
        return first > second;
    }

    /**
     * @param first
     * @param second
     * @return first > second
     */
    public static boolean greater(
            final float first,
            final float second)
    {
        return first > second;
    }

    /**
     * @param first
     * @param second
     * @return first > second
     */
    public static boolean greater(
            final double first,
            final double second)
    {
        return first > second;
    }

    /**
     * @param first
     * @param second
     * @return first > second
     */
    public static boolean greater(
            final long first,
            final long second)
    {
        return first > second;
    }

    /**
     * @param first
     * @param second
     * @return first.compareTo(second) >= 0
     */
    public static boolean greaterOrEqual(
            final Comparable first,
            final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) >= 0;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, Long.valueOf(second))
     */
    public static boolean greaterOrEqual(
            final Object first,
            final long second)
    {
        boolean greaterOrEqual = first instanceof Comparable;
        if (greaterOrEqual)
        {
            if (first instanceof Integer)
            {
                greaterOrEqual = greaterOrEqual((Comparable) first, Integer.valueOf((int) second));
            } else
            {
                greaterOrEqual = greaterOrEqual((Comparable) first, Long.valueOf(second));
            }
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, Integer.valueOf(second))
     */
    public static boolean greaterOrEqual(
            final Object first,
            final int second)
    {
        boolean greaterOrEqual = first instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual((Comparable) first, Integer.valueOf(second));
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, Short.valueOf(second))
     */
    public static boolean greaterOrEqual(
            final Object first,
            final short second)
    {
        boolean greaterOrEqual = first instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual((Comparable) first, Short.valueOf(second));
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual(Long.valueOf(first), (Comparable) second)
     */
    public static boolean greaterOrEqual(
            final long first,
            final Object second)
    {
        boolean greaterOrEqual = second instanceof Comparable;
        if (greaterOrEqual)
        {
            if (second instanceof Integer)
            {
                greaterOrEqual = greaterOrEqual(Integer.valueOf((int) first), (Integer) second);
            } else
            {
                greaterOrEqual = greaterOrEqual(Long.valueOf(first), (Comparable) second);
            }
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual(Integer.valueOf(first), (Comparable) second)
     */
    public static boolean greaterOrEqual(
            final int first,
            final Object second)
    {
        boolean greaterOrEqual = second instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual(Integer.valueOf(first), (Comparable) second);
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual(Short.valueOf(first), (Comparable) second)
     */
    public static boolean greaterOrEqual(
            final short first,
            final Object second)
    {
        boolean greaterOrEqual = second instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual(Short.valueOf(first), (Comparable) second);
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, (Comparable) second)
     */
    public static boolean greaterOrEqual(
            final Object first,
            final Object second)
    {
        boolean greaterOrEqual = first instanceof Comparable && second instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual((Comparable) first, (Comparable) second);
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, Double.valueOf(second))
     */
    public static boolean greaterOrEqual(
            final Object first,
            final double second)
    {
        boolean greaterOrEqual = first instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual((Comparable) first, Double.valueOf(second));
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual((Comparable) first, Float.valueOf(second))
     */
    public static boolean greaterOrEqual(
            final Object first,
            final float second)
    {
        boolean greaterOrEqual = first instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual((Comparable) first, Float.valueOf(second));
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual(new Double(first), (Comparable) second
     */
    public static boolean greaterOrEqual(
            final double first,
            final Object second)
    {
        boolean greaterOrEqual = second instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual(new Double(first), (Comparable) second);
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return greaterOrEqual(new Float(first), (Comparable) second)
     */
    public static boolean greaterOrEqual(
            final float first,
            final Object second)
    {
        boolean greaterOrEqual = second instanceof Comparable;
        if (greaterOrEqual)
        {
            greaterOrEqual = greaterOrEqual(new Float(first), (Comparable) second);
        }
        return greaterOrEqual;
    }

    /**
     * @param first
     * @param second
     * @return first >= second
     */
    public static boolean greaterOrEqual(
            final byte first,
            final byte second)
    {
        return first >= second;
    }

    /**
     * @param first
     * @param second
     * @return first >= second
     */
    public static boolean greaterOrEqual(
            final char first,
            final char second)
    {
        return first >= second;
    }

    /**
     * @param first
     * @param second
     * @return first >= second
     */
    public static boolean greaterOrEqual(
            final double first,
            final double second)
    {
        return first >= second;
    }

    /**
     * @param first
     * @param second
     * @return first >= second
     */
    public static boolean greaterOrEqual(
            final long first,
            final long second)
    {
        return first >= second;
    }
}