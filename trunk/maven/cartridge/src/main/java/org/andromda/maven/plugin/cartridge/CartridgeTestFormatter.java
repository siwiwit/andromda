package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import org.apache.commons.io.FileUtils;

/**
 * Formats the cartridge test results into the correct format.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class CartridgeTestFormatter
    implements TestListener
{
    /**
     * Stores the report file location.
     */
    private File reportFile;

    /**
     * Stores the contents of the report.
     */
    private StringWriter report;

    /**
     * The print writer for the report.
     */
    private PrintWriter reportWriter;

    /**
     * Ignore test failures, just show failure messages
     */
    private boolean testFailureIgnore = false;

    /**
     *
     */
    public CartridgeTestFormatter()
    {
        this.report = new StringWriter();
        this.reportWriter = new PrintWriter(this.report);
    }

    /**
     * Sets the file that will contain the report results (i.e.
     * the results of the cartridge test run).
     *
     * @param reportFile the file to which the report output will be written.
     */
    public void setReportFile(final File reportFile)
    {
        this.reportFile = reportFile;
    }

    /**
     * Keeps track of the number of errors.
     */
    private int numberOfErrors = 0;

    /**
     * @see junit.framework.TestListener#addError(junit.framework.Test, Throwable)
     */
    public void addError(
        Test test,
        Throwable throwable)
    {
        this.numberOfErrors++;
        this.collectFailure(
            test,
            throwable);
    }

    /**
     * Keeps track of the number of failures.
     */
    private int numberOfFailures = 0;

    /**
     * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
     */
    public void addFailure(
        Test test,
        AssertionFailedError failure)
    {
        this.numberOfFailures++;
        this.collectFailure(
            test,
            failure);
    }

    /**
     * @see junit.framework.TestListener#endTest(junit.framework.Test)
     */
    public void endTest(Test test)
    {
    }

    /**
     * Keeps track of the number of tests run.
     */
    private int numberOfTests = 0;

    /**
     * @see junit.framework.TestListener#startTest(junit.framework.Test)
     */
    public void startTest(Test test)
    {
        this.numberOfTests++;
    }

    /**
     * Stores the start time of the test suite
     */
    private long startTime = 0;

    /**
     * Stores the new line separator.
     */
    private static final String newLine = System.getProperty("line.separator");

    /**
     * The testsuite started.
     * @param name
     */
    public void startTestSuite(final String name)
    {
        startTime = System.currentTimeMillis();
        this.reportWriter.println("-------------------------------------------------------------------------------");
        this.reportWriter.println(name + " Test Suite");
        this.reportWriter.println("-------------------------------------------------------------------------------");
    }

    /**
     * Adds a failure to the current <code>failures</code>
     * collection (these are rendered at the end of test suite
     * execution).
     *
     * @param test the actual test.
     * @param throwable the failure information.
     */
    private void collectFailure(
        Test test,
        Throwable throwable)
    {
        this.failures.add(new Failure(
                test,
                throwable));
    }

    private Collection<Failure> failures = new ArrayList<Failure>();

    /**
     * Signifies the test suite ended and returns the summary of the
     * test.
     *
     * @param test the test suite being run.
     * @return the test summary.
     */
    String endTestSuite(Test test)
    {
        final double elapsed = ((System.currentTimeMillis() - this.startTime) / 1000.0);
        final StringBuilder summary = new StringBuilder("Tests: " + String.valueOf(this.numberOfTests) + ", ");
        summary.append("Failures: ").append(String.valueOf(this.numberOfFailures)).append(", ");
        summary.append("Errors: ").append(String.valueOf(this.numberOfErrors)).append(", ");
        summary.append("Time elapsed: ").append(elapsed).append(" sec");
        summary.append(newLine);
        summary.append(newLine);
        this.reportWriter.print(summary);
        if (this.numberOfFailures > 0)
        {
            this.reportWriter.println("Failures: " + this.numberOfFailures);
            this.reportWriter.println("-------------------------------------------------------------------------------");
            int ctr = 1;
            for (final Iterator iterator = this.failures.iterator(); iterator.hasNext(); ctr++)
            {
                final Failure failure = (Failure)iterator.next();
                final Throwable information = failure.information;
                if (information instanceof AssertionFailedError)
                {
                    FileComparator comparator = (FileComparator)failure.test;
                    this.reportWriter.println(ctr + ") " + comparator.getActualFile());
                }
            }
            this.reportWriter.println("-------------------------------------------------------------------------------");
            this.reportWriter.println();
        }

        for (final Failure failure : this.failures)
        {
            FileComparator comparator = (FileComparator) failure.test;
            final Throwable information = failure.information;
            if (information instanceof AssertionFailedError)
            {
                this.reportWriter.println("FAILURE: " + comparator.getActualFile());
                this.reportWriter.println(failure.information.getMessage());
            }
            else
            {
                this.reportWriter.println("ERROR:");
                information.printStackTrace(this.reportWriter);
            }
            this.reportWriter.println();
        }

        if (this.reportFile != null)
        {
            try
            {
                FileUtils.writeStringToFile(this.reportFile, report.toString());
            }
            catch (IOException exception)
            {
                if (isTestFailureIgnore())
                {
                    this.reportWriter.println(exception);
                }
                else
                {
                    throw new RuntimeException(exception);
                }
            }
        }
        return summary.toString();
    }

    /**
     * Stores the information about a test failure.
     */
    private static final class Failure
    {
        Test test;
        Throwable information;

        Failure(
            final Test test,
            final Throwable information)
        {
            this.test = test;
            this.information = information;
        }
    }

    /**
     * @return the testFailureIgnore
     */
    public boolean isTestFailureIgnore()
    {
        return this.testFailureIgnore;
    }

    /**
     * @param testFailureIgnore the testFailureIgnore to set
     */
    public void setTestFailureIgnore(boolean testFailureIgnore)
    {
        this.testFailureIgnore = testFailureIgnore;
    }
}