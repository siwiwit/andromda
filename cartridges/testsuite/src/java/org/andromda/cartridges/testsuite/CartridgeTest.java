package org.andromda.cartridges.testsuite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is the main class of the cartridge test suite for AndroMDA. The test
 * checks for a list of expected files that a file with the same name and the
 * same package was generated by AndroMDA and that the APIs of the expected file
 * and the generated file are equal. <code>CartridgeTest</code> acts as the
 * test director which creates the list of files to be compared. The actual API
 * comparison is carried out by instances of {@link JavaSourceComparator}.
 * 
 * @author Ralf Wirdemann
 * @author Chad Brandon
 */
public class CartridgeTest
    extends TestCase
{

    /**
     * Points to the directory were the expected files are stored which will be
     * compared to the generated ones.
     */
    public static final String DRAFT_DIR_PROPERTY = "compare.dir";

    /**
     * Points to the directory were the generated files are located.
     */
    public static final String OUT_DIR_PROPERTY = "output.dir";

    public CartridgeTest(
        String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        // Add the tests which compares all existing draft files against the
        // generated ones. This makes sure that for each draft an appropriate
        // files was generated.
        int noOfTests = addTests(getDirectory(DRAFT_DIR_PROPERTY), suite);

        File outDir = getDirectory(OUT_DIR_PROPERTY);
        List expectedFiles = new ArrayList();
        createListOfExpectedFiles(outDir, expectedFiles);

        if (noOfTests < expectedFiles.size())
        {
            // The generator has generated more files than drafts do exist.
            // In order to find out which one was additionally generated
            // compare each generated file to the appropriate complement in the
            // draft directory. The testcase will report if the draft doesn't
            // exist
            addTests(getDirectory(OUT_DIR_PROPERTY), suite);
        }

        return suite;
    }

    private static int addTests(File dir, TestSuite suite)
    {
        int noOfAddedTests = 0;
        List expectedFiles = new ArrayList();
        createListOfExpectedFiles(dir, expectedFiles);
        Iterator iterator = expectedFiles.iterator();
        while (iterator.hasNext())
        {
            File expectedFile = (File)iterator.next();
            File acutalFile = getComplementFile(expectedFile);
            if (expectedFile.getName().endsWith(".java"))
            {
                suite.addTest(new JavaSourceComparator(
                    "testAPIEquals",
                    expectedFile,
                    acutalFile));
            }
            else if (expectedFile.getName().endsWith(".xml"))
            {
                suite.addTest(new XMLComparator(
                    "testXMLEquals",
                    expectedFile,
                    acutalFile));
            }
            noOfAddedTests++;
        }
        return noOfAddedTests;
    }

    private static File getComplementFile(File f)
    {
        String complement;
        String fn = f.getPath();
        String draftDir = getDirectoryName(DRAFT_DIR_PROPERTY);
        String outDir = getDirectoryName(OUT_DIR_PROPERTY);

        if (f.getPath().startsWith(draftDir))
        {
            complement = fn.substring(draftDir.length(), fn.length());
            complement = outDir + complement;
        }
        else
        {
            complement = fn.substring(outDir.length(), fn.length());
            complement = draftDir + complement;
        }
        return new File(complement);
    }

    private static String getDirectoryName(String propertyKey)
    {
        String dirName = System.getProperty(propertyKey);
        if (dirName == null)
        {
            throw new RuntimeException("system property <" + propertyKey
                + "> not set");
        }

        // Replace the path-separator character in the given directory name
        // by the path-separator character used by the actual system
        char ch = dirName.indexOf('\\') != -1 ? '\\' : '/';
        dirName = dirName.replace(ch, File.separatorChar);
        return dirName;
    }

    private static File getDirectory(String propertyKey)
    {
        String dirName = getDirectoryName(propertyKey);
        File dir = new File(dirName);
        if (!dir.exists() || !dir.isDirectory())
        {
            throw new RuntimeException("directory <" + dirName
                + "> doesn't exist");
        }
        return dir;
    }

    private static void createListOfExpectedFiles(File dir, List fileList)
    {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            File f = files[i];
            if (!f.isDirectory())
            {
                if (f.getName().endsWith(".java")
                    || f.getName().endsWith(".xml"))
                {
                    fileList.add(f);
                }
            }
            else
            {
                createListOfExpectedFiles(f, fileList);
            }
        }
    }
}