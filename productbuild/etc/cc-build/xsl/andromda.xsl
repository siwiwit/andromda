<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:lxslt="http://xml.apache.org/xslt">

    <xsl:output method="html"/>

    <xsl:template match="/">
        <html>
        <head>
          <style type="text/css">
.white { color:#FFFFFF }

.index { background-color:#FFFFFF }
.index-passed { color:#004400 }
.index-failed { color:#FF0000; font-weight:bold }
.index-header { font-weight:bold }

.link { font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; text-decoration:none; }

.tab-table { margin: 0em 0em 0.5em 0em; }
.tabs { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; font-weight:bold; padding: 0em 2em; background-color:#EEEEEE; }
.tabs-link { color:#000000; text-decoration:none; }
.tabs-link:visited { color:#000000; text-decoration:none; }
.tabs-selected { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; font-weight:bold; padding: 0em 2em; }
.tabs-selected { border: inset; }

.header-title { font-family:arial,helvetica,sans-serif; font-size:12pt; color:#000000; font-weight:bold; }
.header-label { font-weight:bold; }
.header-data { font-family:arial,helvetica,sans-serif; font-size:10pt; color:#000000; }

.modifications-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }
.modifications-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }
.modifications-oddrow { background-color:#CCCCCC }
.modifications-evenrow { background-color:#FFFFCC }

.changelists-oddrow { background-color:#CCCCCC }
.changelists-evenrow { background-color:#FFFFCC }
.changelists-file-spacer { background-color:#FFFFFF }
.changelists-file-evenrow { background-color:#EEEEEE }
.changelists-file-oddrow { background-color:#FFFFEE }
.changelists-file-header { background-color:#666666; font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FFFFFF; }

.compile-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }
.compile-error-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FF0000; }
.compile-warn-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#CC9900; }
.compile-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }

.distributables-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }
.distributables-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }
.distributables-oddrow { background-color:#CCCCCC }

.unittests-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }
.unittests-oddrow { background-color:#CCCCCC }
.unittests-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }
.unittests-error { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#901090; }
.unittests-failure { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FF0000; }

.checkstyle-oddrow { background-color:#CCCCCC }
.checkstyle-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }
.checkstyle-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }
          </style>
        </head>

        <table align="center" cellpadding="2" cellspacing="0" border="0" width="98%">

            <xsl:if test="cruisecontrol/build/@error">
                <tr><td class="header-title">BUILD FAILED</td></tr>
                <tr><td class="header-data">
                    <span class="header-label">Ant Error Message:&#160;</span>
                    <xsl:value-of select="cruisecontrol/build/@error"/>
                </td></tr>
            </xsl:if>

            <xsl:if test="not (cruisecontrol/build/@error)">
                <tr><td class="header-title">BUILD COMPLETE&#160;-&#160;
                    <xsl:value-of select="cruisecontrol/info/property[@name='label']/@value"/>
                </td></tr>
            </xsl:if>

            <tr><td class="header-data">
                <span class="header-label">Date of build:&#160;</span>
                <xsl:value-of select="cruisecontrol/info/property[@name='builddate']/@value"/>
            </td></tr>
            <tr><td class="header-data">
                <span class="header-label">Time to build:&#160;</span>
                <xsl:value-of select="cruisecontrol/build/@time"/>
            </td></tr>
        </table>

        <xsl:variable name="mavengoal" select="/cruisecontrol/build/mavengoal"/>
        <xsl:variable name="maven.messages" select="$mavengoal/message"/>
        <xsl:variable name="maven.error.messages" select="$mavengoal/message[@priority='error']"/>
        <xsl:variable name="maven.warn.messages" select="$mavengoal/message[@priority='warn']"/>
        <xsl:variable name="maven.info.messages" select="$mavengoal/message[@priority='info']"/>

        <xsl:if test="count($maven.error.messages) > 0">
			<HR/><H2>Errors</H2>
            <table align="center" cellpadding="2" cellspacing="0" border="0" width="98%">
                 <!-- Style download notifications first -->
                 <tr class="compile-sectionheader">
                     <td>Error Messages</td>
                 </tr>
                 <xsl:apply-templates select="$mavengoal/message[@priority='error']"/>
            </table>
        </xsl:if>		

        <HR/><H2>Modifications</H2>
        <table align="center" cellpadding="2" cellspacing="0" border="0" width="98%">
        <xsl:variable name="modification.list" select="cruisecontrol/modifications/modification"/>
        <xsl:apply-templates select="$modification.list">
            <xsl:sort select="date" order="descending" data-type="text" />
        </xsl:apply-templates>
        </table>
		
        </html>
    </xsl:template>

    <xsl:template match="mavengoal">
       <tr class="compile-sectionheader">
       	<td>
           <xsl:value-of select="@name"/>
        </td>
       </tr>
       <tr>
       	<td>
           <xsl:apply-templates select="message[@priority='error']"/>
        </td>
       </tr>
    </xsl:template>

    <xsl:template match="message[@priority='error']">
         <tr>
           <td> 
    	  <span class="compile-error-data">
        <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
        </span>
           </td>
         </tr>
    </xsl:template>

    <xsl:template match="message[@priority='warn']">
    	  <span class="compile-data">
        <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
        </span>
    </xsl:template>

    <xsl:variable name="tasklist" select="/cruisecontrol/build//target/task"/>
    <xsl:variable name="javac.tasklist" select="$tasklist[@name='Javac'] | $tasklist[@name='javac'] | $tasklist[@name='compilewithwalls']"/>
    <xsl:variable name="ejbjar.tasklist" select="$tasklist[@name='EjbJar'] | $tasklist[@name='ejbjar']"/>

    <!-- P4 changelist template
    <modification type="p4" revision="15">
       <revision>15</revision>
       <user>non</user>
       <client>non:all</client>
       <date>2002/05/02 10:10:10</date>
       <file action="add">
          <filename>myfile</filename>
          <revision>10</revision>
       </file>
    </modification>
    -->
    <xsl:template match="modification[@type='p4']">
        <tr valign="top">
            <xsl:if test="position() mod 2=0">
                <xsl:attribute name="class">changelists-oddrow</xsl:attribute>
            </xsl:if>
            <xsl:if test="position() mod 2!=0">
                <xsl:attribute name="class">changelists-evenrow</xsl:attribute>
            </xsl:if>
            <td class="modifications-data">
                <xsl:value-of select="revision"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="user"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="client"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="date"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="comment"/>
            </td>
        </tr>

        <xsl:if test="count(file) > 0">
            <tr valign="top">
                <xsl:if test="position() mod 2=0">
                    <xsl:attribute name="class">changelists-oddrow</xsl:attribute>
                </xsl:if>
                <xsl:if test="position() mod 2!=0">
                    <xsl:attribute name="class">changelists-evenrow</xsl:attribute>
                </xsl:if>
                <td class="modifications-data" colspan="6">
                    <table align="right" cellpadding="1" cellspacing="0" border="0" width="95%">
                        <tr>
                            <td class="changelists-file-header" colspan="3">
                                &#160;Files affected by this changelist:&#160;
                                (<xsl:value-of select="count(file)"/>)
                            </td>
                        </tr>
                        <xsl:apply-templates select="file"/>
                    </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <!-- used by P4 -->
    <xsl:template match="file">
        <tr valign="top" >
            <xsl:if test="position() mod 2=0">
                <xsl:attribute name="class">changelists-file-oddrow</xsl:attribute>
            </xsl:if>
            <xsl:if test="position() mod 2!=0">
                <xsl:attribute name="class">changelists-file-evenrow</xsl:attribute>
            </xsl:if>

            <td class="changelists-file-spacer">
                &#160;
            </td>

            <td class="modifications-data">
                <b>
                    <xsl:value-of select="@action"/>
                </b>
            </td>
            <td class="modifications-data" width="100%">
                <xsl:value-of select="filename"/>&#160;
                <xsl:value-of select="revision"/>
            </td>
        </tr>
    </xsl:template>

    <!-- Modifications template -->
    <xsl:template match="modification[file]">
        <tr>
            <xsl:if test="position() mod 2=0">
                <xsl:attribute name="class">modifications-oddrow</xsl:attribute>
            </xsl:if>
            <xsl:if test="position() mod 2!=0">
                <xsl:attribute name="class">modifications-evenrow</xsl:attribute>
            </xsl:if>

            <td class="modifications-data">
                <xsl:value-of select="file/@action"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="user"/>
            </td>
            <td class="modifications-data">
                <xsl:if test="file/project">
                    <xsl:value-of select="file/project"/>
                    <xsl:value-of select="system-property('file.separator')"/>
                </xsl:if>
                <xsl:value-of select="file/filename"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="comment"/>
            </td>
        </tr>
    </xsl:template>

    <!-- Up to version 2.1.6 the modification set format did not
         include the file node -->
    <xsl:template match="modification">
        <tr>
            <xsl:if test="position() mod 2=0">
                <xsl:attribute name="class">modifications-oddrow</xsl:attribute>
            </xsl:if>
            <xsl:if test="position() mod 2!=0">
                <xsl:attribute name="class">modifications-evenrow</xsl:attribute>
            </xsl:if>

            <td class="modifications-data">
                <xsl:value-of select="@type"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="user"/>
            </td>
            <td class="modifications-data">
                <xsl:if test="project">
                    <xsl:value-of select="project"/>
                    <xsl:value-of select="system-property('file.separator')"/>
                </xsl:if>
                <xsl:value-of select="filename"/>
            </td>
            <td class="modifications-data">
                <xsl:value-of select="comment"/>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>

