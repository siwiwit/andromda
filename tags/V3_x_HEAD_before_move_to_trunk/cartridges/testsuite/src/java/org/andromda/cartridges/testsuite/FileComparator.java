package org.andromda.cartridges.testsuite;

import java.io.File;

import junit.framework.TestCase;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.io.FileUtils;


/**
 * Compares two files. It checks if both file do exist and if the contents of
 * both files are equal.
 *
 * @author Chad Brandon
 */
public class FileComparator
    extends TestCase
{
    private File expectedFile;
    private File actualFile;
    private boolean binary;

    /**
     * Constructs a new instance of the FileComparator.
     *
     * @param testName the name of the test to run
     * @param expectedFile the location of the expected file
     * @param actualFile the location of the actual file.
     * @param binary whether or not the file is binary, if it is binary contents
     *        of the binary are not compared as Strings but as binary files.
     */
    public FileComparator(
        String testName,
        File expectedFile,
        File actualFile,
        boolean binary)
    {
        super();
        this.setName(testName);
        this.expectedFile = expectedFile;
        this.actualFile = actualFile;
        this.binary = binary;
    }

    public void testEquals()
    {
        assertTrue(
            "expected file <" + expectedFile.getPath() + "> doesn't exist",
            expectedFile.exists());
        assertTrue(
            "actual file <" + actualFile.getPath() + "> doesn't exist",
            actualFile.exists());
        this.testContentsEqual();
    }

    /**
     * Loads both the <code>actual</code> and <code>expected</code> files
     * and tests the contents for equality.
     */
    protected void testContentsEqual()
    {
        try
        {
            String actualContents = ResourceUtils.getContents(actualFile.toURI().toURL());
            String expectedContents = ResourceUtils.getContents(expectedFile.toURI().toURL());
            String message = "actual file <" + actualFile + "> does not match\n";
            if (this.binary)
            {
                assertTrue(
                    message,
                    FileUtils.contentEquals(
                        expectedFile,
                        actualFile));
            }
            else
            {
                assertEquals(
                    message,
                    expectedContents.trim(),
                    actualContents.trim());
            }
        }
        catch (final Throwable throwable)
        {
            fail(throwable.toString());
        }
    }

    /**
     * Gets the actual file being compared.
     *
     * @return the file being compared.
     */
    public File getActualFile()
    {
        return this.actualFile;
    }

    /**
     * Gets the file expected file (i.e. the file that
     * the actual file is compared against).
     *
     * @return the expected file.
     */
    public File getExpectedFile()
    {
        return this.expectedFile;
    }
}