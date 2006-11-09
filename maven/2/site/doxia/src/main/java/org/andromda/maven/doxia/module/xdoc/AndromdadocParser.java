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

import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.parser.AbstractParser;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.StructureSink;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.StringUtils;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parse an xdoc model and emit events into the specified doxia
 * Sink.
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id: AndromdadocParser.java,v 1.1.2.1 2006-09-14 08:10:39 vancek Exp $
 * 
 * @plexus.component role="org.apache.maven.doxia.parser.Parser" role-hint="andromdadoc"
 */
public class AndromdadocParser
    extends AbstractParser
{
    public void parse( Reader reader, Sink sink )
        throws ParseException
    {
        try
        {
            XmlPullParser parser = new MXParser();

            parser.setInput( reader );

            parseXdoc( parser, sink );
        }
        catch ( Exception ex )
        {
            throw new ParseException( "Error parsing the model.", ex );
        }
    }

    public void parseXdoc( XmlPullParser parser, Sink sink )
        throws Exception
    {
        int eventType = parser.getEventType();

        while ( eventType != XmlPullParser.END_DOCUMENT )
        {
            if ( eventType == XmlPullParser.START_TAG )
            {
                if ( parser.getName().equals( "document" ) )
                {
                    //Do nothing
                }
                else if ( parser.getName().equals( "title" ) )
                {
                    sink.title();
                }
                else if ( parser.getName().equals( "author" ) )
                {
                    sink.author();
                }
                else if ( parser.getName().equals( "body" ) )
                {
                    sink.body();
                }
                else if ( parser.getName().equals( "section" ) )
                {
                    sink.anchor( parser.getAttributeValue( null, "name" ) );
                    sink.anchor_();
                    
                    sink.section1();

                    sink.sectionTitle1();

                    sink.text( parser.getAttributeValue( null, "name" ) );

                    sink.sectionTitle1_();
                }
                else if ( parser.getName().equals( "subsection" ) )
                {
                    sink.anchor( parser.getAttributeValue( null, "name" ) );
                    sink.anchor_();
                    
                    sink.section2();

                    sink.sectionTitle2();

                    sink.text( parser.getAttributeValue( null, "name" ) );

                    sink.sectionTitle2_();
                }
                else if ( parser.getName().equals( "p" ) )
                {
                    final String styleClass = parser.getAttributeValue( null, "class" );
                    this.writeParagraph( sink, styleClass );
                }
                else if ( parser.getName().equals( "source" ) )
                {
                    sink.verbatim( true );
                }
                else if ( parser.getName().equals( "ul" ) )
                {
                    sink.list();
                }
                else if ( parser.getName().equals( "ol" ) )
                {
                    sink.numberedList( Sink.NUMBERING_DECIMAL );
                }
                else if ( parser.getName().equals( "li" ) )
                {
                    final String styleClass = parser.getAttributeValue( null, "class" );
                    this.writeListItem( sink, styleClass );
                }
                else if ( parser.getName().equals( "properties" ) )
                {
                    sink.head();
                }
                else if ( parser.getName().equals( "b" ) )
                {
                    sink.bold();
                }
                else if ( parser.getName().equals( "i" ) )
                {
                    sink.italic();
                }
                else if ( parser.getName().equals( "a" ) )
                {
                    final String styleClass = parser.getAttributeValue( null, "class" );
                    final String target = parser.getAttributeValue( null, "target" );
                    final String href = parser.getAttributeValue( null, "href" );
                    
                    if ( StringUtils.isNotEmpty( href ) )
                    {
                        this.writeLink( sink, styleClass, target, href );
                    }
                    else
                    {
                        final String name = parser.getAttributeValue( null, "name" );
                        if ( StringUtils.isNotEmpty( name ) )
                        {
                            this.writeAnchor( sink, styleClass, name );
                        }
                        else
                        {
                            handleRawText( sink, parser );
                        }
                    }
                }
                else if ( parser.getName().equals( "macro" ) )
                {
                    String macroId = parser.getAttributeValue( null, "id" );

                    int count = parser.getAttributeCount();

                    Map parameters = new HashMap();

                    for ( int i = 1; i < count; i++ )
                    {
                        parameters.put( parser.getAttributeName( i ), parser.getAttributeValue( i ) );
                    }

                    MacroRequest request = new MacroRequest( parameters );

                    executeMacro( macroId, request, sink );
                }

                // ----------------------------------------------------------------------
                // Tables
                // ----------------------------------------------------------------------

                else if ( parser.getName().equals( "table" ) )
                {
                    sink.table();
                }
                else if ( parser.getName().equals( "tr" ) )
                {
                    sink.tableRow();
                }
                else if ( parser.getName().equals( "th" ) )
                {
                    final String width = parser.getAttributeValue( null, "width" );
                    final String styleClass = parser.getAttributeValue( null, "class" );
                    this.writeTableHeaderCell( sink, width, styleClass );
                }
                else if ( parser.getName().equals( "td" ) )
                {
                    String width = parser.getAttributeValue( null, "width" );
                    String styleClass = parser.getAttributeValue( null, "class" );
                    this.writeTableCell( sink, width, styleClass );
                }
                else
                {
                    handleRawText( sink, parser );
                }
            }
            else if ( eventType == XmlPullParser.END_TAG )
            {
                if ( parser.getName().equals( "document" ) )
                {
                    //Do nothing
                }
                else if ( parser.getName().equals( "title" ) )
                {
                    sink.title_();
                }
                else if ( parser.getName().equals( "author" ) )
                {
                    sink.author_();
                }
                else if ( parser.getName().equals( "body" ) )
                {
                    sink.body_();
                }
                else if ( parser.getName().equals( "p" ) )
                {
                    sink.paragraph_();
                }
                else if ( parser.getName().equals( "source" ) )
                {
                    sink.verbatim_();
                }
                else if ( parser.getName().equals( "ul" ) )
                {
                    sink.list_();
                }
                else if ( parser.getName().equals( "ol" ) )
                {
                    sink.numberedList_();
                }
                else if ( parser.getName().equals( "li" ) )
                {
                    sink.listItem_();
                }
                else if ( parser.getName().equals( "properties" ) )
                {
                    sink.head_();
                }
                else if ( parser.getName().equals( "b" ) )
                {
                    sink.bold_();
                }
                else if ( parser.getName().equals( "i" ) )
                {
                    sink.italic_();
                }
                else if ( parser.getName().equals( "a" ) )
                {
                    // TODO: Note there will be badness if link_ != anchor != </a>
                    sink.link_();
                }

                // ----------------------------------------------------------------------
                // Tables
                // ----------------------------------------------------------------------

                else if ( parser.getName().equals( "table" ) )
                {
                    sink.table_();
                }
                else if ( parser.getName().equals( "tr" ) )
                {
                    sink.tableRow_();
                }
                else if ( parser.getName().equals( "th" ) )
                {
                    sink.tableHeaderCell_();
                }
                else if ( parser.getName().equals( "td" ) )
                {
                    sink.tableCell_();
                }

                // ----------------------------------------------------------------------
                // Sections
                // ----------------------------------------------------------------------

                else if ( parser.getName().equals( "section" ) )
                {
                    sink.section1_();
                }
                else if ( parser.getName().equals( "subsection" ) )
                {
                    sink.section2_();
                }
                else
                {
                    sink.rawText( "</" );

                    sink.rawText( parser.getName() );

                    sink.rawText( ">" );
                }

                // ----------------------------------------------------------------------
                // Sections
                // ----------------------------------------------------------------------
            }
            else if ( eventType == XmlPullParser.TEXT )
            {
                sink.text( parser.getText() );
            }

            eventType = parser.next();
        }
    }

    private void handleRawText( Sink sink, XmlPullParser parser )
    {
        sink.rawText( "<" );

        sink.rawText( parser.getName() );

        int count = parser.getAttributeCount();

        for ( int i = 0; i < count; i++ )
        {
            sink.rawText( " " );

            sink.rawText( parser.getAttributeName( i ) );

            sink.rawText( "=" );

            sink.rawText( "\"" );

            sink.rawText( parser.getAttributeValue( i ) );

            sink.rawText( "\"" );
        }

        sink.rawText( ">" );
    }
    
    private void writeParagraph( Sink sink, String styleClass )
    {
        if ( styleClass != null )
        {
            sink.rawText( "<p class=\"" + styleClass + "\">" );
        }
        else
        {
            sink.paragraph();
        }
    }
    
    private void writeListItem( Sink sink, String styleClass )
    {
        if ( StringUtils.isNotEmpty( styleClass ) )
        {
            sink.rawText( "<li class=\"" + styleClass + "\">" );
        }
        else
        {
            sink.listItem();
        }
    }
    
    private void writeLink( Sink sink, String styleClass, String target, String href )
    {
        if ( StringUtils.isNotEmpty( styleClass ) )
        {
            sink.rawText( "<a " );
            
            if ( StringUtils.isNotEmpty( styleClass ) )
            {
                sink.rawText( "class=\"" + styleClass + "\" " );
            }
            
            if ( StringUtils.isNotEmpty( target ) )
            {
                sink.rawText( "target=\"" + target + "\" " );
            }
            
            sink.rawText( "href=\"" + href + "\">" );
        }
        else
        {
            sink.link( href );
        }
    }

    private void writeAnchor( Sink sink, String styleClass, String name )
    {
        if ( StringUtils.isNotEmpty( name ) )
        {
            sink.rawText( "<a name=\"" + name + "\" " );
            
            if ( StringUtils.isNotEmpty( styleClass ) )
            {
                sink.rawText( "class=\"" + styleClass + "\"" );
            }
            else
            {
                final String id = StructureSink.linkToKey( name );
                sink.rawText( "id=\"" + id + "\"" );
            }
            sink.rawText( ">" );
        }
        else
        {
            sink.anchor( name );
        }
    }
    
    private void writeTableHeaderCell( Sink sink, String width, String styleClass )
    {
        if ( StringUtils.isNotEmpty( styleClass ) || StringUtils.isNotEmpty( width ) )
        {
            sink.rawText( "<th " );
            
            if ( StringUtils.isNotEmpty( styleClass ) )
            {
                sink.rawText( " class=\"" + styleClass + "\"" );
            }
    
            if ( StringUtils.isNotEmpty( width ) )
            {
                sink.rawText( "width=\"" + width + "\"" );
            }
            sink.rawText( ">" );
        }
        else
        {
            sink.tableHeaderCell();
        }
    }
    
    private void writeTableCell( Sink sink, String width, String styleClass )
    {
        if ( StringUtils.isNotEmpty( styleClass ) || StringUtils.isNotEmpty( width ) )
        {
            sink.rawText( "<td " );
            
            if ( StringUtils.isNotEmpty( styleClass ) )
            {
                sink.rawText( " class=\"" + styleClass + "\"" );
            }
    
            if ( StringUtils.isNotEmpty( width ) )
            {
                sink.rawText( "width=\"" + width + "\"" );
            }
            sink.rawText( ">" );
        }
        else
        {
            sink.tableCell();
        }
    }
}
