/* Copyright (c) 2019 JAX-DB
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
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jaxdb.vendor.DBVendor;
import org.libj.net.URIs;
import org.openjax.maven.mojo.MojoUtil;
import org.xml.sax.SAXException;

abstract class SqlMojo<P extends Produce<?>,T> extends JaxDbMojo<P> {
  @Parameter(property="dbUrl")
  private String dbUrl;

  @Parameter(property="rename")
  private String rename;

  @Parameter(property="vendor")
  private String vendor;

  final void executeStaged(final JaxDbMojo<?>.Configuration configuration) throws Exception {
    if (vendor == null || vendor.length() == 0)
      throw new MojoExecutionException("The parameter 'vendor' is required for goal " + execution.getGoal() + "@" + execution.getExecutionId());

    final DBVendor dbVendor = DBVendor.valueOf(vendor);
    if (dbVendor == null)
      throw new MojoExecutionException("The parameter <vendor>" + vendor + "</vendor> does not match supported vendors: " + Arrays.toString(DBVendor.values()));

    for (final URI schema : configuration.getSchemas()) {
      Reserve<T> reserve = schemaToReserve().get(schema);
      File sqlFile = null;
      if (reserve == null) {
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] {new URL("file", "", project.getBuild().getOutputDirectory() + "/")}, Thread.currentThread().getContextClassLoader()));
        schemaToReserve().put(schema, reserve = newReserve(schema));
      }
      else {
        sqlFile = reserve.renameToFile.get(rename);
      }

      if (sqlFile == null) {
        sqlFile = new File(configuration.getDestDir(), rename == null ? URIs.getSimpleName(schema) + ".sql" : MojoUtil.getRenamedFileName(schema.toString(), rename)).getAbsoluteFile();
        makeSql(reserve, dbVendor, sqlFile);
        reserve.renameToFile.put(rename, sqlFile);
      }

      if (dbUrl != null) {
        dbVendor.loadDriver();
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
          if (!DBVendor.valueOf(connection.getMetaData()).equals(dbVendor))
            throw new MojoExecutionException("The parameters <vendor>" + vendor + "</vendor> and <dbUrl>" + dbUrl + "</dbUrl> specify different DB vendors");

          getLog().info("Loading in DB: " + dbVendor);
          loadSql(connection, reserve.obj);
        }
      }
    }
  }

  abstract HashMap<URI,Reserve<T>> schemaToReserve();
  abstract Reserve<T> newReserve(URI schema) throws IOException, SAXException;
  abstract void makeSql(Reserve<? extends T> reserve, DBVendor dbVendor, File sqlFile) throws Exception;
  abstract void loadSql(Connection connection, T reserve) throws Exception;
}