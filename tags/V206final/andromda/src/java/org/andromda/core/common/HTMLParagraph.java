package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * A utility object useful for formatting html paragraph output.
 * 
 * <p> Represents a paragraph, made of lines. The whole
 * paragraph has a limit for the line width.
 * Words can be added, the class will reformat the
 * paragraph according to max. line width. </p>
 * 
 * @author Matthias Bohlen
 *
 */
public class HTMLParagraph
{
    private ArrayList lines = new ArrayList();
    private StringBuffer currentLine = new StringBuffer();
    private int maxLineWidth;

    /**
     * <p>Constructs an HTMLParagraph with a specified maximum line
     * width.</p>
     * @param lineWidth maximum line width
     */
    public HTMLParagraph(int lineWidth)
    {
        this.maxLineWidth = lineWidth;
    }

    /**
     * <p>Appends another word to this paragraph.</p>
     * @param word the word
     */
    public void appendWord(String word)
    {
        if ((currentLine.length() + word.length() + 1) > maxLineWidth)
        {
            nextLine();
        }
        currentLine.append(" ");
        currentLine.append(word);
    }

    /**
     * <p>Appends a bunch of words to the paragraph.</p>
     * @param text the text to add to the paragraph
     */
    public void appendText(String text)
    {
        if ((currentLine.length() + text.length() + 1) <= maxLineWidth)
        {
            currentLine.append(" ");
            currentLine.append(text);
            return;
        }

        StringTokenizer st = new StringTokenizer(text);
        while (st.hasMoreTokens())
        {
            appendWord(st.nextToken());
        }
    }

    /**
     * <p>Returns the lines in this paragraph.</p>
     * @return Collection the lines as collection of Strings
     */
    public Collection getLines()
    {
        if (currentLine.length() > 0)
        {
            nextLine();
        }
        
        return lines;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer st = new StringBuffer();
        for (Iterator it = getLines().iterator();  it.hasNext(); )
        {
            st.append((String)it.next());
            st.append("\n");
        }
        return st.toString();
    }

    private void nextLine()
    {
        lines.add(currentLine.toString());
        currentLine = new StringBuffer();
    }
}
