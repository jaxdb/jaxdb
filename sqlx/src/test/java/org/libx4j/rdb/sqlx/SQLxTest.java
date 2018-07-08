/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.sqlx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.lib4j.jci.CompilationException;
import org.lib4j.lang.ClassLoaders;
import org.lib4j.xml.ValidationException;
import org.libx4j.rdb.ddlx.Schemas;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.Schema;
import org.libx4j.rdb.sqlx_0_9_9.xLzgluGCXYYJc.$Database;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Bindings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SQLxTest {
  private static final File sourcesXsbDestDir = new File("target/generated-test-sources/xsb");
  private static final File sourcesJaxbDestDir = new File("target/generated-test-sources/jaxb");
  protected static final File resourcesDestDir = new File("target/generated-test-resources/rdb");
  protected static final File testClassesDir = new File("target/test-classes");
  private static final File[] classpath;

  static {
    final URL[] mainClasspath = ClassLoaders.getClassPath();
    final URL[] testClasspath = ClassLoaders.getTestClassPath();
    classpath = new File[mainClasspath.length + testClasspath.length];
    for (int i = 0; i < mainClasspath.length; i++)
      classpath[i] = new File(mainClasspath[i].getFile());

    for (int i = 0; i < testClasspath.length; i++)
      classpath[i + mainClasspath.length] = new File(testClasspath[i].getFile());
  }

  public static void createXSDs(final String name) throws CompilationException, IOException, JAXBException, TransformerException {
    final URL ddlx = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx");
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddlx2sqlx(ddlx, destFile);
    SQL.xsd2xsb(sourcesXsbDestDir, testClassesDir, destFile.toURI().toURL());
    SQL.xsd2jaxb(sourcesJaxbDestDir, testClassesDir, destFile.toURI().toURL());
  }

  public static void createSql(final Connection connection, final String name) throws IOException, SAXException, SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final URL sqlx = Thread.currentThread().getContextClassLoader().getResource("rdb/" + name + ".sqlx");
    SqlXsb.sqlx2sql(vendor, sqlx, new File(resourcesDestDir, name + "-" + vendor + ".sql"), classpath);
  }

  public static int[] loadData(final Connection connection, final String name) throws IOException, SQLException, ValidationException {
    final Schema schema;
    try (final InputStream in = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx").openStream()) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    URL url = Thread.currentThread().getContextClassLoader().getResource("rdb/" + name + ".sqlx");
    if (url == null)
      url = Thread.currentThread().getContextClassLoader().getResource(name + ".sqlx");

    return SqlXsb.INSERT(connection, ($Database)Bindings.parse(url));
  }
}