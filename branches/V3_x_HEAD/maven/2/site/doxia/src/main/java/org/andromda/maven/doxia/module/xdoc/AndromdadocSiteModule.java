package org.andromda.maven.doxia.module.xdoc;

/*
 * Copyright 2004-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.doxia.site.module.SiteModule;

/**
 * @version $Id: AndromdadocSiteModule.java,v 1.1.2.2 2007-01-09 03:20:41 vancek Exp $
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
