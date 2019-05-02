/* Copyright (c) 2018 JAX-DB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.openjax.ext.maven.mojo.GeneratorMojo;
import org.openjax.ext.maven.mojo.MojoUtil;
import org.openjax.ext.maven.mojo.SourceInput;
import org.openjax.ext.net.URLs;
import org.jaxdb.sqlx.SQL;
import org.jaxdb.vendor.DBVendor;
import org.xml.sax.SAXException;

@Mojo(name="sqlx2sql", defaultPhase=LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution=ResolutionScope.TEST)
@Execute(goal="sqlx2sql")
public final class Sqlx2SqlMojo extends GeneratorMojo {
  @Parameter(property="rename")
  private String rename;

  @Parameter(property="vendor", required=true)
  private String vendor;

  @SourceInput
  @Parameter(property="schemas", required=true)
  private List<String> schemas;

  @Parameter(defaultValue="${localRepository}")
  private ArtifactRepository localRepository;

  @Override
  public void execute(final Configuration configuration) throws MojoExecutionException, MojoFailureException {
    try {
      final ArtifactHandler artifactHandler = new DefaultArtifactHandler("jar");
      final File[] classpathFiles = MojoUtil.getExecutionClasspash(project, execution, (PluginDescriptor)this.getPluginContext().get("pluginDescriptor"), localRepository, artifactHandler);
      for (final URL schema : configuration.getSourceInputs("schemas"))
        SQL.sqlx2sql(DBVendor.valueOf(vendor), schema, new File(configuration.getDestDir(), rename != null ? MojoUtil.getRenamedFileName(schema, rename) : URLs.getShortName(schema) + ".sql"), classpathFiles);
    }
    catch (final DependencyResolutionRequiredException | IOException | SAXException e) {
      throw new MojoExecutionException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }
}