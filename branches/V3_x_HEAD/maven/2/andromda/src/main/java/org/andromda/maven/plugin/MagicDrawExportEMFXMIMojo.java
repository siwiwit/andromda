package org.andromda.maven.plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Repository;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.commons.lang.StringUtils;

/**
 * Exports the MagicDraw project file to EMF XMI
 * (requires valid MagicDraw installation in MD_HOME, but
 * only if target files are not up-to-date) 
 *
 * @goal export2emf
 * @phase generate-sources
 * @author Jens Vagts
 */
public class MagicDrawExportEMFXMIMojo
    extends AbstractAndroMDAMojo
{
	/**
	 * Name of the environment variable pointing to the MagicDraw home directory 
	 */
	private final String MD_HOME = "MD_HOME";
	
    /**
     * The home/root directory of the magicdraw installation.
     *
     * @parameter expression="${magicDrawHome}"
     */
    private String magicDrawHome;
    
    /**
     * @see org.andromda.maven.plugin.AbstractAndroMDAMojo#execute(org.andromda.core.configuration.Configuration)
     */
    public void execute(final Configuration configuration)
        throws MojoExecutionException
    {
        try
        {
        	//export each file (uri) of each model in each repository 
        	final Repository[] repositories = configuration.getRepositories();
        	if (repositories == null || repositories.length == 0) {
        		getLog().info("No repositories for export in configuration defined.");
        		return;
        	}
            int repositoryCount = repositories.length;
            for (int ctr = 0; ctr < repositoryCount; ctr++)
            {
                final Repository repository = repositories[ctr];
                if (repository != null)
                {
                    final Model[] models = repository.getModels();
                    final int modelCount = models.length;
                    for (int ctr2 = 0; ctr2 < modelCount; ctr2++)
                    {
                        final Model model = models[ctr2];
	        			if ("emf-uml2".equals(model.getType()))
	        			{
							String[] uris = model.getUris();
							for (int u = 0; u < uris.length; u++)
							{
								exportFile(uris[u]);
							}						
	        			}
					}
	        	}
			}
        	
        }
        catch (Throwable throwable)
        {
            throw new MojoExecutionException("Error exporting MagicDraw project file to EMF XMI", throwable);
        }
    }

    private void exportFile(String dest) throws Exception {
    	final String UML2EXT = ".uml2";
    	final String MDEXT = ".xml.zip";
    	
    	//get the source file name from the destination name (we expect xml.zip)
    	if (!dest.endsWith(UML2EXT))
    	{
    		getLog().warn("Ignoring model file " + dest + ", since it seems not to be of type 'uml2'");
    		return;
    	}
    	
    	String source = StringUtils.replace(dest, UML2EXT, MDEXT);
    	File destFile = new File(new URI(ResourceUtils.normalizePath(dest)));
    	File sourceFile = new File(new URI(ResourceUtils.normalizePath(source)));
    	if (sourceFile == null || !sourceFile.exists())
    	{
    		throw new MojoExecutionException("Model file [" + source + "] does not exist");
    	}
    	if (destFile == null || !destFile.exists())
    	{
    		getLog().debug("No old model file [" + dest + "] existing");
    	}
    	else
    	{
    		if (getLog().isDebugEnabled())
    		{
    			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			getLog().debug("- MagicDraw model file ["+sourceFile.getName()+"] date = " + formatter.format(new Date(sourceFile.lastModified())));
    			getLog().debug("- EMF model file ["+destFile.getName()+"] date = " + formatter.format(new Date(destFile.lastModified())));
    		}
    		if (destFile.lastModified() >= sourceFile.lastModified())
    		{
	    		getLog().info("Model file [" + dest + "] is up-to-date");
	    		return;
    		}
    	}

    	//check for valid magic draw installation
    	checkForMagicDraw();
    	
    	//perform the export via MagicDraw
    	getLog().info("Exporting model file [" + source + "] ...");
    	String command = "\"" + magicDrawHome
    			+ File.separator + "plugins"
    			+ File.separator + "com.nomagic.magicdraw.emfuml2export"
    			+ File.separator + "exportEMFXMI" + exportExt + "\""
    			+ " project_file=" + sourceFile.getPath()
    			+ " destination_dir=" + sourceFile.getParent()
    			+ " load_all_modules=true";
    	Process process = Runtime.getRuntime().exec(command);
    	
    	//since at least the windows version forks the magicdraw process,
    	//we have to synchronize via input stream reading
    	InputStream is = process.getInputStream();
    	final byte[] buf = new byte[128];
    	int length;
    	while ((length = is.read(buf)) > 0)
    	{
    		getLog().info(new String(buf, 0, length));
    	}
    	process.waitFor();    	 
    	process.destroy();
    	int err = process.exitValue();
    	if (err != 0)
    	{
    		throw new MojoExecutionException("MagicDraw export returned error code " + err);
    	}
    	getLog().info("Successfully exported model file.");
    }
    
    /**
     * only check once for magic draw installation
     */
	private boolean checkedMagicDraw = false;
	
	/**
	 * The export executeable file extension (.exe for Windows, nothing for *ix) 
	 */
	private String exportExt = "";

	private void checkForMagicDraw() throws MojoExecutionException
	{
		if (!checkedMagicDraw)
		{
	    	if (magicDrawHome == null)
	    	{
	    		magicDrawHome = System.getenv(MD_HOME);
	    	}
	    	
	    	if (magicDrawHome == null)
	    	{
	    		throw new MojoExecutionException("MagicDraw home directory not defined: please define either a configuration variable \"magicDrawHome\" in your pom or the environment variable \""+ MD_HOME + "\"!");
	    	}
	    	
	    	File home = new File(magicDrawHome);
	    	if (home == null || !home.exists())
	    	{
	    		throw new MojoExecutionException("Invalid MagicDraw home directory specified: " + magicDrawHome);
	    	}

	    	//check for running os
	    	String os = System.getProperty("os.name");
	    	if (os.indexOf("Windows") != -1)
	    	{
	    		exportExt = ".exe";
	    	}

	    	checkedMagicDraw = true;
		}
	}
}