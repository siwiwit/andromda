package org.andromda.persistence.hibernate;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.UserType;

/**
 * <p>
 * A hibernate user type which converts a Blob into a byte[] and back again. 
 * </p>
 */
public class HibernateByteBlobType
    implements UserType
{
    /**
     * @see net.sf.hibernate.UserType#sqlTypes()
     */
    public int[] sqlTypes()
    {
        return new int[]
        {
            Types.BLOB
        };
    }

    /**
     * @see net.sf.hibernate.UserType#returnedClass()
     */
    public Class returnedClass()
    {
        return byte[].class;
    }

    /**
     * @see net.sf.hibernate.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y)
    {
        return (x == y)
            || (x != null && y != null && java.util.Arrays.equals(
                (byte[])x,
                (byte[])y));
    }

    /**
     * @see net.sf.hibernate.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws SQLException
    {
        Blob blob = rs.getBlob(names[0]);
        return blob.getBytes(1, (int)blob.length());
    }

    /**
     * @see net.sf.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws SQLException
    {
        byte[] b = (byte[])value;
        st.setBinaryStream(index, new ByteArrayInputStream(b), b.length);
    }

    /**
     * @see net.sf.hibernate.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value)
    {
        if (value == null)
            return null;

        byte[] bytes = (byte[])value;
        byte[] result = new byte[bytes.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);

        return result;
    }

    /**
     * @see net.sf.hibernate.UserType#isMutable()
     */
    public boolean isMutable()
    {
        return true;
    }
}