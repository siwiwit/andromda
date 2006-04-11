package org.andromda.maven.plugin.andromdapp.eclipse;

import java.io.File;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.XMLWriter;


/**
 * Writes the Eclipse .classpath files.
 *
 * @author Chad Brandon
 */
public class ClasspathWriter
    extends EclipseWriter
{
    public ClasspathWriter(
        final MavenProject project,
        final Log logger)
    {
        super(project, logger);
    }

    /**
     * Writes the .classpath file for eclipse.
     *
     * @param projects the list of projects from which the .classpath will get its dependencies.
     * @param repositoryVariableName the name of the maven repository variable.
     * @param artifactFactory the factory for constructing artifacts.
     * @param artifactResolver the artifact resolver.
     * @param localRepository the local repository instance.
     * @param artifactMetadataSource
     * @param classpathArtifactTypes the artifacts types that are allowed in the classpath file.
     * @param remoteRepositories the list of remote repository instances.
     * @param resolveTransitiveDependencies whether or not dependencies shall be transitively resolved.
     * @param merge anything extra (not auto-generated), that should be "merged" into the generated .classpath
     * @throws Exception
     */
    public void write(
        final List projects,
        final String repositoryVariableName,
        final ArtifactFactory artifactFactory,
        final ArtifactResolver artifactResolver,
        final ArtifactRepository localRepository,
        final ArtifactMetadataSource artifactMetadataSource,
        final Set classpathArtifactTypes,
        final List remoteRepositories,
        final boolean resolveTransitiveDependencies,
        final String merge)
        throws Exception
    {
        final String rootDirectory = ResourceUtils.normalizePath(this.project.getBasedir().toString());
        final File classpathFile = new File(rootDirectory, ".classpath");
        final FileWriter fileWriter = new FileWriter(classpathFile);
        final XMLWriter writer = new PrettyPrintXMLWriter(fileWriter);

        writer.startElement("classpath");

        final Set projectArtifactIds = new LinkedHashSet();
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            final Artifact projectArtifact =
                artifactFactory.createArtifact(
                    project.getGroupId(),
                    project.getArtifactId(),
                    project.getVersion(),
                    null,
                    project.getPackaging());
            projectArtifactIds.add(projectArtifact.getId());
        }

        final Set allArtifacts = new LinkedHashSet();
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            for (final Iterator sourceIterator = project.getCompileSourceRoots().iterator(); sourceIterator.hasNext();)
            {
                final String sourceRoot = ResourceUtils.normalizePath((String)sourceIterator.next());
                if (new File(sourceRoot).isDirectory())
                {
                    String sourceRootPath = StringUtils.replace(
                            sourceRoot,
                            rootDirectory,
                            "");
                    if (sourceRootPath.startsWith("/"))
                    {
                        sourceRootPath = sourceRootPath.substring(
                                1,
                                sourceRootPath.length());
                        this.writeClasspathEntry(
                            writer,
                            "src",
                            sourceRootPath);
                    }
                }
            }

            final Set artifacts = project.createArtifacts(
                    artifactFactory,
                    null,
                    null);

            // - get the direct dependencies
            for (final Iterator artifactIterator = artifacts.iterator(); artifactIterator.hasNext();)
            {
                final Artifact artifact = (Artifact)artifactIterator.next();

                // - don't attempt to resolve the artifact if its part of the project (we
                //   infer this if it has the same id has one of the projects or is in
                //   the same groupId).
                if (!projectArtifactIds.contains(artifact.getId()) &&
                    !project.getGroupId().equals(artifact.getGroupId()))
                {
                    artifactResolver.resolve(
                        artifact,
                        project.getRemoteArtifactRepositories(),
                        localRepository);
                    allArtifacts.add(artifact);
                }
                else
                {
                    allArtifacts.add(artifact);
                }
            }
        }

        // - remove the project artifacts
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            final Artifact projectArtifact = project.getArtifact();
            if (projectArtifact != null)
            {
                for (final Iterator artifactIterator = allArtifacts.iterator(); artifactIterator.hasNext();)
                {
                    final Artifact artifact = (Artifact)artifactIterator.next();
                    final String projectId = projectArtifact.getArtifactId();
                    final String projectGroupId = projectArtifact.getGroupId();
                    final String artifactId = artifact.getArtifactId();
                    final String groupId = artifact.getGroupId();
                    if (artifactId.equals(projectId) && groupId.equals(projectGroupId))
                    {
                        artifactIterator.remove();
                    }
                }
            }
        }

        // - now we resolve transitively, if we have the flag on
        if (resolveTransitiveDependencies)
        {
            final Artifact rootProjectArtifact =
                artifactFactory.createArtifact(
                    this.project.getGroupId(),
                    this.project.getArtifactId(),
                    this.project.getVersion(),
                    null,
                    this.project.getPackaging());

            final OrArtifactFilter filter = new OrArtifactFilter();
            filter.add(new ScopeArtifactFilter(Artifact.SCOPE_COMPILE));
            filter.add(new ScopeArtifactFilter(Artifact.SCOPE_PROVIDED));
            final ArtifactResolutionResult result =
                artifactResolver.resolveTransitively(
                    allArtifacts,
                    rootProjectArtifact,
                    localRepository,
                    remoteRepositories,
                    artifactMetadataSource,
                    filter);

            allArtifacts.clear();
            allArtifacts.addAll(result.getArtifacts());
        }

        final List allArtifactPaths = new ArrayList(allArtifacts);
        for (final ListIterator iterator = allArtifactPaths.listIterator(); iterator.hasNext();)
        {
            final Artifact artifact = (Artifact)iterator.next();
            if (classpathArtifactTypes.contains(artifact.getType()))
            {
                final File artifactFile = artifact.getFile();
                final String path =
                    StringUtils.replace(
                        ResourceUtils.normalizePath(artifactFile.toString()),
                        ResourceUtils.normalizePath(localRepository.getBasedir()),
                        repositoryVariableName);
                iterator.set(path);
            }
            else
            {
                iterator.remove();
            }
        }

        // - sort the paths
        Collections.sort(allArtifactPaths);

        for (final Iterator iterator = allArtifactPaths.iterator(); iterator.hasNext();)
        {
            final String path = (String)iterator.next();
            if (path.startsWith(repositoryVariableName))
            {
                this.writeClasspathEntry(
                    writer,
                    "var",
                    path);
            }
            else
            {
                this.writeClasspathEntry(
                    writer,
                    "lib",
                    path);
            }
        }

        this.writeClasspathEntry(
            writer,
            "con",
            "org.eclipse.jdt.launching.JRE_CONTAINER");

        String outputPath =
            StringUtils.replace(
                ResourceUtils.normalizePath(this.project.getBuild().getOutputDirectory()),
                rootDirectory,
                "");
        if (outputPath.startsWith("/"))
        {
            outputPath = outputPath.substring(
                    1,
                    outputPath.length());
        }
        this.writeClasspathEntry(
            writer,
            "output",
            outputPath);

        if (StringUtils.isNotBlank(merge))
        {
            writer.writeMarkup(merge);
        }
        writer.endElement();

        logger.info("Classpath file written --> '" + classpathFile + "'");
        IOUtil.close(fileWriter);
    }

    /**
     * Writes a classpathentry with the given <code>kind</code> and <code>path</code> values.
     *
     * @param writer the XML writer with which to write.
     * @param kind the kind of the classpath entry.
     * @param path the path of the classpath entry.
     */
    private void writeClasspathEntry(
        final XMLWriter writer,
        final String kind,
        final String path)
    {
        writer.startElement("classpathentry");
        writer.addAttribute(
            "kind",
            kind);
        writer.addAttribute(
            "path",
            path);
        writer.endElement();
    }
}