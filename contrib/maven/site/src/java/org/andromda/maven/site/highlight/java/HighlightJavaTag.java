package org.andromda.maven.site.highlight.java;


import org.andromda.maven.site.highlight.AbstractHighlightTag;

import java.io.StringReader;

public class HighlightJavaTag extends AbstractHighlightTag implements JavaHighlightStyles
{
    private String keywordClass = null;
    private String stringLiteralClass = null;
    private String numericLiteralClass = null;
    private String commentClass = null;

    protected void highlight(String text)
    {
        JavaHighlighter highlighter = new JavaHighlighter(this);
        highlighter.highlight(new StringReader(text), getWriter());
    }

    public String getCommentClass()
    {
        return commentClass;
    }

    public void setCommentClass(String commentClass)
    {
        this.commentClass = commentClass;
    }

    public String getKeywordClass()
    {
        return keywordClass;
    }

    public void setKeywordClass(String keywordClass)
    {
        this.keywordClass = keywordClass;
    }

    public String getStringLiteralClass()
    {
        return stringLiteralClass;
    }

    public void setStringLiteralClass(String stringLiteralClass)
    {
        this.stringLiteralClass = stringLiteralClass;
    }

    public String getNumericLiteralClass()
    {
        return numericLiteralClass;
    }

    public void setNumericLiteralClass(String numericLiteralClass)
    {
        this.numericLiteralClass = numericLiteralClass;
    }

}
