package org.andromda.cartridges.testsuite;

import java.io.File;
import java.io.FileReader;

import org.custommonkey.xmlunit.XMLTestCase;

/**
 * Compares two XML-Files.
 * 
 * @author Ralf Wirdemann
 * @author Chad Brandon
 */
public class XMLComparator
    extends XMLTestCase
{

    private File expectedFile;
    private File actualFile;

    public XMLComparator(
        String s,
        File expectedFile,
        File actualFile)
    {
        super(s);
        setExpectedFile(expectedFile);
        setActualFile(actualFile);
    }

    public void testXMLEquals()
    {
        try
        {
            assertTrue("expected file <" + getExpectedFile().getPath()
                + "> doesn't exist", getExpectedFile().exists());
            assertTrue("actual file <" + getActualFile().getPath()
                + "> doesn't exist", getActualFile().exists());
            assertXMLEqual(
                new FileReader(expectedFile.getAbsolutePath()),
                new FileReader(actualFile.getAbsolutePath()));
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    private File getExpectedFile()
    {
        return expectedFile;
    }

    private void setExpectedFile(File expectedFile)
    {
        this.expectedFile = expectedFile;
    }

    private File getActualFile()
    {
        return actualFile;
    }

    private void setActualFile(File actualFile)
    {
        this.actualFile = actualFile;
    }
}