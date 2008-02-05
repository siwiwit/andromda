package org.andromda.maven.doxia.module.xdoc;

/*
 * Based on The Apache Software Foundation XdocModule
 */

import org.apache.maven.doxia.module.site.SiteModule;

/**
 * @version $Id: AndromdadocSiteModule.java,v 1.1.2.4 2008-02-05 00:59:01 carloslcuenca Exp $
 * 
 * Based taken from Apache Foundation Doxia Project.
 * 
 * @plexus.component role="org.apache.maven.doxia.site.module.SiteModule" role-hint="andromdadoc"
 */
public class AndromdadocSiteModule
    implements SiteModule
{
    public String getSourceDirectory()
    {
        return "axdoc";
    }

    public String getExtension()
    {
        return "xml";
    }

    public String getParserId()
    {
        return "andromdadoc";
    }
}
