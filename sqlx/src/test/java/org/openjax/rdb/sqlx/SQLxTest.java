/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.sqlx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.openjax.classic.jci.CompilationException;
import org.openjax.classic.util.ClassLoaders;
import org.openjax.classic.util.FastArrays;
import org.openjax.classic.xml.api.ValidationException;
import org.openjax.rdb.ddlx.Schemas;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.Schema;
import org.openjax.rdb.sqlx_0_9_9.xL0gluGCXYYJc.$Database;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.xsb.runtime.Bindings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SQLxTest {
  private static final File sourcesXsbDestDir = new File("target/generated-test-sources/xsb");
  private static final File sourcesJaxbDestDir = new File("target/generated-test-sources/jaxb");
  protected static final File resourcesDestDir = new File("target/generated-test-resources/rdb");
  protected static final File testClassesDir = new File("target/test-classes");
  private static final File[] classpath;

  static {
    final File[] testClassPath = ClassLoaders.getTestClassPath();
    classpath = testClassPath != null ? FastArrays.concat(ClassLoaders.getClassPath(), testClassPath) : ClassLoaders.getClassPath();
  }

  public static void createXSDs(final String name) throws CompilationException, IOException, JAXBException, TransformerException {
    final URL ddlx = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx");
    assertNotNull(ddlx);
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddlx2sqlx(ddlx, destFile);
    SQL.xsd2xsb(sourcesXsbDestDir, testClassesDir, destFile.toURI().toURL());
    SQL.xsd2jaxb(sourcesJaxbDestDir, testClassesDir, destFile.toURI().toURL());
  }

  public static void createSql(final Connection connection, final String name) throws IOException, SAXException, SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final URL sqlx = Thread.currentThread().getContextClassLoader().getResource("rdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    SqlXsb.sqlx2sql(vendor, sqlx, new File(resourcesDestDir, name + "-" + vendor + ".sql"), classpath);
  }

  public static int[] loadData(final Connection connection, final String name) throws IOException, SQLException, ValidationException {
    final Schema schema;
    try (final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name + ".ddlx")) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final URL sqlx = Thread.currentThread().getContextClassLoader().getResource("rdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    return SqlXsb.INSERT(connection, ($Database)Bindings.parse(sqlx));
  }
}