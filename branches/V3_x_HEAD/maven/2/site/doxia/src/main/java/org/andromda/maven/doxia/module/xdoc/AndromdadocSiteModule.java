package org.andromda.maven.doxia.module.xdoc;

/*
 * Based on The Apache Software Foundation XdocModule
 */

import org.apache.maven.doxia.site.module.SiteModule;

/**
 * @version $Id: AndromdadocSiteModule.java,v 1.1.2.3 2007-01-10 00:21:13 vancek Exp $
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
