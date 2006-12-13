package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestResult;

import org.andromda.cartridges.testsuite.CartridgeTest;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.maven.plugin.AndroMDAMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides the ability to compare cartridge output with existing output.
 *
 * @phase test
 * @goal test
 * @requiresDependencyResolution test
 * @description runs AndroMDA Cartridge tests
 * @author Chad Brandon
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
            final File expectedOutputArchive = new File(this.expectedOutputArchive);
            if (!expectedOutputArchive.exists() || !expectedOutputArchive.isFile())
            {
                throw new MojoExecutionException("The path specifying the expectedOutputArchive '" +
                    this.expectedOutputArchive + "' must be a file");
            }

            try
            {
                this.getLog().info("-----------------------------------------------------------------------------");
                this.getLog().info("          A n d r o M D A   C a r t r i d g e   T e s t   S u i t e          ");
                this.getLog().info("-----------------------------------------------------------------------------");

                // - add the cartridge test dependencies (any dependencies of the cartridge test plugin)
                this.addCartridgeTestDependencies();

                // - first run AndroMDA with the test configuration
                final AndroMDAMojo andromdaMojo = new AndroMDAMojo();
                andromdaMojo.setConfigurationUri(this.configurationUri);
                andromdaMojo.setProject(this.project);
                andromdaMojo.setSettings(this.settings);
                andromdaMojo.setPropertyFiles(this.propertyFiles);
                andromdaMojo.execute();

                // - unpack the expected output archive
                this.unpack(
                    expectedOutputArchive,
                    new File(this.expectedDirectory));

                final CartridgeTest cartridgeTest = CartridgeTest.instance();
                cartridgeTest.setActualOutputPath(this.actualDirectory);
                cartridgeTest.setExpectedOutputPath(this.expectedDirectory);
                cartridgeTest.setBinarySuffixes(this.binaryOutputSuffixes);

                final CartridgeTestFormatter formatter = new CartridgeTestFormatter();

                // - set the report location
                final File report = new File(this.reportDirectory, this.project.getArtifactId() + ".txt");
                formatter.setReportFile(report);
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
                    throw new MojoExecutionException("Test are some test failures");
                }
            }
            catch (final Throwable throwable)
            {
                if (throwable instanceof MojoExecutionException)
                {
                    throw (MojoExecutionException)throwable;
                }
                throw new MojoExecutionException("An error occured while testing cartridge '" +
                    this.project.getArtifactId() + "'",
                    ExceptionUtils.getRootCause(throwable));
            }
        }
        else
        {
            this.getLog().info("Skipping cartridge tests");
        }
    }

    /**
     * Unpacks the expected archive file to the expected directory
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(
        final File file,
        final File location)
        throws MojoExecutionException
    {
        final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            final UnArchiver unArchiver;
            unArchiver = this.archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            location.mkdirs();
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, throwable);
            }
        }
    }
}