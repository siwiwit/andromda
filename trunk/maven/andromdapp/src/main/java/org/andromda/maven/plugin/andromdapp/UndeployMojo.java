package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Provides the undeployment of applications from a given directory.
 *
 * @goal undeploy
 * @author Chad Brandon
 */
public class UndeployMojo
    extends AppManagementMojo
{
    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (this.deployLocation.exists() && this.deployLocation.isDirectory())
        {
            try
            {
                final File deployFile = this.getDeployFile();
                this.getLog().info("Undeploying " + deployFile + " from " + this.deployLocation);
                if (deployFile.isDirectory())
                {
                    FileUtils.deleteDirectory(deployFile);
                }
                else
                {
                    deployFile.delete();
                }
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("An error occurred while attempting to undeploy artifact", throwable);
            }
        }
        else
        {
            this.getLog().warn(
                "Undeploy did not occur because the specified deployLocation '" + deployLocation +
                "' does not exist, or is not a directory");
        }
    }
}
