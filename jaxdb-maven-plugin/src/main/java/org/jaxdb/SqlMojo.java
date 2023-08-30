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
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.xml.transform.TransformerException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jaxdb.vendor.DbVendor;
import org.xml.sax.SAXException;

abstract class SqlMojo<P extends Produce<?,?,?>,T> extends JaxDbMojo<P> {
  @Parameter(property="driverClassName")
  private String driverClassName;

  @Parameter(property="dbUrl")
  private String dbUrl;

  @Parameter(property="rename")
  String rename;

  @Parameter(property="vendor")
  private String vendor;

  final Reserve<T> getReserve(final URL schema) throws IOException, SAXException, TransformerException {
    Reserve<T> reserve = schemaToReserve().get(schema);
    if (reserve == null) {
      final Thread thread = Thread.currentThread();
      thread.setContextClassLoader(new URLClassLoader(new URL[] {new URL("file", "", project.getBuild().getOutputDirectory() + "/")}, thread.getContextClassLoader()));
      schemaToReserve().put(schema, reserve = newReserve(schema));
    }

    return reserve;
  }

  final void executeStaged(final JaxDbMojo<?>.Configuration configuration) throws Exception {
    if (vendor == null || vendor.length() == 0)
      throw new MojoExecutionException("The parameter 'vendor' is required for goal " + execution.getGoal() + "@" + execution.getExecutionId());

    final DbVendor dbVendor = DbVendor.valueOf(vendor);
    if (dbVendor == null)
      throw new MojoExecutionException("The parameter <vendor>" + vendor + "</vendor> does not match supported vendors: " + Arrays.toString(DbVendor.values()));

    final LinkedHashSet<URL> schemas = configuration.getSchemas();
    if (schemas.size() > 0) {
      for (final URL schema : schemas) { // [S]
        final Reserve<T> reserve = getReserve(schema);
        final String sqlName = reserve.get(schema, rename);
        final File sqlFile = new File(configuration.getDestDir(), sqlName).getAbsoluteFile();

        makeSql(reserve, dbVendor, sqlFile);

        if (dbUrl != null) {
          if (driverClassName == null)
            throw new MojoExecutionException("The parameter <driverClassName> is required for <dbUrl>" + dbUrl + "</dbUrl>");

          Class.forName(driverClassName);
          try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            if (!DbVendor.valueOf(connection.getMetaData()).equals(dbVendor))
              throw new MojoExecutionException("The parameters <vendor>" + vendor + "</vendor> and <dbUrl>" + dbUrl + "</dbUrl> specify different DB vendors");

            loadSql(connection, reserve.obj);
          }
        }
        else if (driverClassName != null) {
          throw new MojoExecutionException("The parameter <dbUrl> is required for <driverClassName>" + driverClassName + "</driverClassName>");
        }
      }
    }
  }

  abstract HashMap<URL,Reserve<T>> schemaToReserve();
  abstract Reserve<T> newReserve(URL schema) throws IOException, SAXException, TransformerException;
  abstract void makeSql(Reserve<? extends T> reserve, DbVendor dbVendor, File sqlFile) throws Exception;
  abstract void loadSql(Connection connection, T reserve) throws Exception;
}