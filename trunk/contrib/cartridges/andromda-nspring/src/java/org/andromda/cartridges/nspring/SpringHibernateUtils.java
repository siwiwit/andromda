package org.andromda.cartridges.nspring;



/**
 * Contains utilities used within the Spring cartridge
 * when dealing with Hibernate.
 *
 * @author Chad Brandon
 */
public class SpringHibernateUtils
{
    /**
     * The version of Hibernate we're generating for.
     */
    private String hibernateVersion = "3";

    /**
     * Sets the version of Hibernate we're generating for.
     *
     * @param hibernateVersion the Hibernate version.
     */
    public void setHibernateVersion(final String hibernateVersion)
    {
        this.hibernateVersion = hibernateVersion;
    }

    /**
     * The Hibernate 2 version number (for determining the
     * correct package).
     */
    private static final String VERSION_2 = "2";

    /**
     * Gets the appropriate hibernate package name for the given
     * <code>version</code>.
     *
     * @return the base package name.
     */
    public String getBasePackage()
    {
        String packageName = null;
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName = "net.sf.hibernate";
        }
        else
        {
            packageName = "org.hibernate";
        }
        return packageName;
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     *
     * @return the Hibernate criterion package name.
     */
    public String getCriterionPackage()
    {
        final StringBuffer packageName = new StringBuffer();
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName.append(".expression");
        }
        else
        {
            packageName.append(".criterion");
        }
        packageName.insert(0, this.getBasePackage());
        return packageName.toString();
    }

    /**
     * Gets the appropriate Spring Hibernate package based on the given
     * <code>version</code>.
     *
     * @return the spring hibernate package.
     */
    public String getSpringHibernatePackage()
    {
        String packageName = null;
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName = "org.springframework.orm.hibernate";
        }
        else
        {
            packageName = "org.springframework.orm.hibernate3";
        }
        return packageName;
    }
    
    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getEagerFetchMode()
    {
        String fetchMode = null;
        if (VERSION_2.equals(this.hibernateVersion))
        {
            fetchMode = "EAGER";
        }
        else
        {
            fetchMode = "JOIN";
        }
        return fetchMode;
    }
    
    /**
     * Retrieves the fully qualified name of the class that retrieves the Hibernate
     * disjunction instance.
     * @return the fully qualified class name.
     */
    public String getDisjunctionClassName()
    {
        final StringBuffer className = new StringBuffer(this.getCriterionPackage() + '.');
        if (VERSION_2.equals(hibernateVersion))
        {
            className.append("Expression");
        }
        else
        {
            className.append("Restrictions");
        }
        return className.toString();
    }
    
    /**
     * Indicates whether or not version 3 is the one that is currently being used.
     * 
     * @return true/false
     */
    public boolean isVersion3()
    {
        return !VERSION_2.equals(hibernateVersion);
    }
}