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
import org.lib4j.lang.Resource;
import org.lib4j.lang.Resources;
import org.lib4j.util.JavaIdentifiers;
import org.lib4j.xml.jaxb.JaxbUtil;
import org.libx4j.rdb.ddlx.Schemas;
import org.libx4j.rdb.ddlx_0_9_8.xLzgluGCXYYJc.Schema;
import org.libx4j.rdb.sqlx_0_9_8.Database;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Bindings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SQLxTest {
  private static final File sourcesDestDir = new File("target/generated-test-sources/jaxb");
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

  public static void createSchemas(final String name) throws CompilationException, IOException, JAXBException, TransformerException {
    final URL ddlx = Resources.getResource(name + ".ddlx").getURL();
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddlx2sqlx(ddlx, destFile);
    SQL.createJaxBindings(destFile.toURI().toURL(), sourcesDestDir, testClassesDir, classpath);
  }

  public static void createSql(final Connection connection, final String name) throws IOException, SAXException, SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final URL sqlx = Resources.getResource("rdb/" + name + ".sqlx").getURL();
    SQL.sqlx2sql(vendor, sqlx, new File(resourcesDestDir, name + "-" + vendor + ".sql"), classpath);
  }

  public static int[] loadData(final Connection connection, final String name) throws ClassNotFoundException, IOException, SAXException, SQLException {
    final Schema schema;
    try (final InputStream in = Resources.getResource(name + ".ddlx").getURL().openStream()) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.truncate(connection, Schemas.flatten(schema).getTable());

    Resource resource = Resources.getResource("rdb/" + name + ".sqlx");
    if (resource == null)
      resource = Resources.getResource(name + ".sqlx");

    final URL sqlx = resource.getURL();
    final Database database = (Database)JaxbUtil.parse(Class.forName(name + ".sqlx." + JavaIdentifiers.toClassCase(name)), sqlx, false);

    return SQL.INSERT(connection, database);
  }
}