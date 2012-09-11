package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestResult;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.maven.plugin.AndroMDAMojo;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Provides the ability to compare cartridge output with existing output.
 *
 * @phase generate-test-sources
 * @goal test
 * @requiresDependencyResolution test
 * @description runs AndroMDA Cartridge tests
 * @author Chad Brandon
 * @author Bob Fields
 */
public class CartridgeTestMojo
    extends AbstractCartridgeTestMojo
{
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (!this.skip)
        {
            final File expectedOutputArchive = this.expectedOutputArchive;
            if (!expectedOutputArchive.exists() || !expectedOutputArchive.isFile())
            {
                if (this.testFailureIgnore)
                {
                    this.getLog().error("The path specifying the expectedOutputArchive '" +
                            this.expectedOutputArchive + "' must be a file");
                }
                else
                {
                    throw new MojoExecutionException("The path specifying the expectedOutputArchive '" +
                            this.expectedOutputArchive + "' must be a file");
                }
            }
            final File expectedOutputDir = this.expectedDirectory;
            if (!expectedOutputDir.exists())
            {
                expectedOutputDir.mkdirs();
            }
            else if (!expectedOutputDir.isDirectory())
            {
                throw new MojoExecutionException("The path specifying the expectedDirectory '" +
                    this.expectedDirectory + "' must be a directory");
            }
            else if (!this.lastModifiedCheck)
            {
                try
                {
                    FileUtils.cleanDirectory(expectedOutputDir);
                }
                catch (IOException ex)
                {
                    throw new MojoExecutionException("Could not clean the expectedDirectory '" +
                            this.expectedDirectory + "'");
                }
            }

            final File actualOutputDir = this.actualDirectory;
            if (!actualOutputDir.exists())
            {
                actualOutputDir.mkdirs();
            }
            else if (!actualOutputDir.isDirectory())
            {
                throw new MojoExecutionException("The path specifying the actualOutputDir '" +
                    this.actualDirectory + "' must be a directory");
            }
            else if (!this.lastModifiedCheck)
            {
                try
                {
                    FileUtils.cleanDirectory(actualOutputDir);
                }
                catch (IOException ex)
                {
                    throw new MojoExecutionException("Could not clean the actualOutputDir '" +
                            this.actualDirectory + "'");
                }
            }

            try
            {
                this.getLog().info("-----------------------------------------------------------------------------");
                this.getLog().info("          A n d r o M D A   C a r t r i d g e   T e s t   S u i t e          ");
                this.getLog().info("-----------------------------------------------------------------------------");
                this.getLog().info("configurationUri=" + this.configurationUri);
                this.getLog().info("actualDirectory=" + this.actualDirectory);
                this.getLog().info("expectedDirectory=" + this.expectedDirectory);
                this.getLog().info("expectedOutputArchive=" + this.expectedOutputArchive);
                this.getLog().info("lastModifiedCheck=" + this.lastModifiedCheck);
                if (this.testFailureIgnore)
                {
                    this.getLog().info("testFailureIgnore=" + this.testFailureIgnore);
                }

                // - change scope of test dependencies to runtime
                // TODO Add test dependencies required to compile all of the generated source code, and run testCompile for all cartridges
                this.changeScopeForTestDependencies();
                // TODO Clear the error list, carried over from a previous cartridge run failure.

                // - first run AndroMDA with the test configuration
                final AndroMDAMojo andromdaMojo = new AndroMDAMojo();
                andromdaMojo.setConfigurationUri(this.configurationUri);
                andromdaMojo.setProject(this.project);
                andromdaMojo.setSettings(this.settings);
                andromdaMojo.setPropertyFiles(this.propertyFiles);
                andromdaMojo.setLastModifiedCheck(this.lastModifiedCheck);
                andromdaMojo.setModelOutputHistory(new File(this.actualDirectory, ".."));
                // TODO This causes the build output expected directory to compile when running javadocs.
                //andromdaMojo.setBuildSourceDirectory(this.actualDirectory);
                andromdaMojo.execute();

                // - unpack the expected output archive
                this.unpack(
                    expectedOutputArchive,
                    this.expectedDirectory);

                final CartridgeTest cartridgeTest = CartridgeTest.instance();
                cartridgeTest.setActualOutputPath(this.actualDirectory.getAbsolutePath());
                cartridgeTest.setExpectedOutputPath(this.expectedDirectory.getAbsolutePath());
                cartridgeTest.setBinarySuffixes(this.binaryOutputSuffixes);

                final CartridgeTestFormatter formatter = new CartridgeTestFormatter();

                // - set the report location
                final File report = new File(this.reportDirectory, this.project.getArtifactId() + ".txt");
                formatter.setReportFile(report);
                formatter.setTestFailureIgnore(this.testFailureIgnore);
                final TestResult result = new TestResult();
                result.addListener(formatter);
                final Test suite = CartridgeTest.suite();
                formatter.startTestSuite(this.project.getName());
                suite.run(result);
                this.getLog().info("");
                this.getLog().info("Results:");
                this.getLog().info(formatter.endTestSuite(suite));
                cartridgeTest.shutdown();

                if (result.failureCount() > 0 || result.errorCount() > 0)
                {
                    if (this.testFailureIgnore)
                    {
                        this.getLog().error("There are test failures, failureCount=" + result.failureCount() + " errorCount=" + result.errorCount()
                                + ", Cartridge=" + this.project.getArtifactId());
                    }
                    else
                    {
                        throw new MojoExecutionException("There are test failures, failureCount=" + result.failureCount() + " errorCount=" + result.errorCount());
                    }
                }
            }
            catch (final Throwable throwable)
            {
                if (throwable instanceof MojoExecutionException && !this.testFailureIgnore)
                {
                    throw (MojoExecutionException)throwable;
                }
                else if (this.testFailureIgnore)
                {
                    this.getLog().error("An error occured while testing cartridge '" +
                        this.project.getArtifactId() + '\'',
                        ExceptionUtils.getRootCause(throwable));
                }
                else
                {
                    throw new MojoExecutionException("An error occured while testing cartridge '" +
                            this.project.getArtifactId() + '\'',
                            ExceptionUtils.getRootCause(throwable));
                }
            }
        }
        else
        {
            this.getLog().info("Skipping cartridge tests");
        }
    }
}