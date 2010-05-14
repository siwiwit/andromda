package org.andromda.maven.plugin.cartridge.site;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;

/**
 * Abstract parent class used by site mojos to perform helper functions like copy
 * and unpack.
 * 
 * @author vancek
 *
 */
public abstract class AbstractSiteMojo
    extends AbstractMojo
{
    /**
     * To look up Archiver/UnArchiver implementations
     * 
     * @component role="org.codehaus.plexus.archiver.manager.ArchiverManager"
     * @required
     * @readonly
     */
    protected ArchiverManager archiverManager;
    
    /**
     * Does the actual copy of the file and logging.
     * 
     * @param sourceFile represents the file to copy.
     * @param destFile file name of destination file.
     * 
     * @throws MojoExecutionException with a message if an error occurs.
     */
    public void copyFile(File sourceFile, File destFile)
        throws MojoExecutionException
    {
        try
        {
            this.getLog().info("Copying " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
            FileUtils.copyFile(sourceFile, destFile);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error copying file from " + sourceFile + " to " + destFile, e);
        }
    }
    
    /**
     * Unpacks the archive file.
     * 
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(File file, File location)
        throws MojoExecutionException
    {
        final String archiveExt = FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            location.mkdirs();
            UnArchiver unArchiver;
            unArchiver = archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
        }
        catch (NoSuchArchiverException e)
        {
            throw new MojoExecutionException("Unknown archiver type", e);
        }
        catch (ArchiverException e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("Error unpacking file: " + 
                    file + " to: " + location + "\r\n" + e.toString(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("Error unpacking file: " + 
                    file.getAbsolutePath() + " to: " + location + "\r\n" + e.toString(), e);
        }
    }
}
