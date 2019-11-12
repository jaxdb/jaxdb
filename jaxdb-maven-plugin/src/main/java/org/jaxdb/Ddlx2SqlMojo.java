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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jaxdb.ddlx.Generator;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.ddlx.StatementBatch;
import org.jaxdb.vendor.DBVendor;
import org.libj.net.URLs;
import org.openjax.maven.mojo.FilterParameter;
import org.openjax.maven.mojo.FilterType;
import org.openjax.maven.mojo.GeneratorMojo;
import org.openjax.maven.mojo.MojoUtil;
import org.xml.sax.SAXException;

@Mojo(name="ddlx2sql", defaultPhase=LifecyclePhase.GENERATE_RESOURCES)
@Execute(goal="ddlx2sql")
public final class Ddlx2SqlMojo extends GeneratorMojo {
  private static final HashMap<String,File> schemaToSql = new HashMap<>();

  @Parameter(property="rename")
  private String rename;

  @Parameter(property="vendor", required=true)
  private String vendor;

  @FilterParameter(FilterType.URL)
  @Parameter(property="schemas", required=true)
  private List<String> schemas;

  @Override
  public void execute(final Configuration configuration) throws MojoExecutionException, MojoFailureException {
    try {
      for (final String schema : new LinkedHashSet<>(schemas)) {
        final URL url = new URL(schema);
        final StatementBatch statementBatch = Generator.createDDL(url, DBVendor.valueOf(vendor));
        final File file = new File(configuration.getDestDir(), rename != null ? MojoUtil.getRenamedFileName(url, rename) : URLs.getShortName(url) + ".sql");
        statementBatch.writeOutput(file);
        schemaToSql.put(schema, file);
      }
    }
    catch (final GeneratorExecutionException | IOException | SAXException e) {
      throw new MojoExecutionException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }
}