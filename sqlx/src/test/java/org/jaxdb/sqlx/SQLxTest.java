/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb.sqlx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.Schemas;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxsb.runtime.Bindings;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.libj.util.ArrayUtil;
import org.libj.util.ClassLoaders;
import org.openjax.xml.sax.XmlPreview;
import org.openjax.xml.sax.XmlPreviewParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public abstract class SQLxTest {
  private static final Logger logger = LoggerFactory.getLogger(SQLxTest.class);
  private static final File sourcesJaxSbDestDir = new File("target/generated-test-sources/jaxsb");
  static final File resourcesDestDir = new File("target/generated-test-resources/jaxdb");
  static final File testClassesDir = new File("target/test-classes");
//  private static final File[] classpath;

  static {
//    final File[] testClassPath = ClassLoaders.getTestClassPath();
//    classpath = testClassPath != null ? ArrayUtil.concat(ClassLoaders.getClassPath(), testClassPath) : ClassLoaders.getClassPath();
  }

  private static $Database to$Database(final URL sqlxFile) throws IOException, SAXException {
    try {
      return ($Database)Bindings.parse(sqlxFile);
    }
    catch (final Throwable t) {
      final File sqlxTempDir = new File(FileUtil.getTempDir(), "sqlx");
      // FIXME: Files.deleteAllOnExit() is not working!
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            FileUtil.deleteAll(sqlxTempDir.toPath());
          }
          catch (final IOException e) {
            if (logger.isInfoEnabled()) logger.info(e.getMessage(), e);
          }
        }
      });

      final XmlPreview preview = XmlPreviewParser.parse(sqlxFile);
      SqlJaxSBLoader.xsd2jaxsb(sqlxTempDir, sqlxTempDir, preview.getImports().get(preview.getRootElement().getNamespaceURI()));

      final URLClassLoader classLoader = new URLClassLoader(ArrayUtil.concat(URLs.toURL(ClassLoaders.getClassPath()), sqlxTempDir.toURI().toURL()), ClassLoader.getSystemClassLoader());
      return ($Database)Bindings.parse(sqlxFile, classLoader);
    }
  }

  public static void createXSDs(final String name) throws IOException, TransformerException {
    final URL ddlxUrl = ClassLoader.getSystemClassLoader().getResource(name + ".ddlx");
    assertNotNull(ddlxUrl);
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddlx2xsd(ddlxUrl, destFile);
    SqlJaxSBLoader.xsd2jaxsb(sourcesJaxSbDestDir, testClassesDir, destFile.toURI().toURL());
  }

  public static void createSql(final Connection connection, final String name) throws IOException, SAXException, SQLException {
    final DbVendor vendor = DbVendor.valueOf(connection.getMetaData());
    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(name, sqlx);
    SqlJaxSBLoader.sqlx2sql(vendor, to$Database(sqlx), new File(resourcesDestDir, name + "-" + vendor + ".sql"));
  }

  public static int[] loadData(final Connection connection, final String name) throws IOException, SAXException, SQLException, TransformerException {
    final DDLx ddlx = new DDLx(ClassLoader.getSystemClassLoader().getResource(name + ".ddlx"));
    Schemas.truncate(connection, ddlx.getMergedSchema().getTable());
    final URL sqlx = ClassLoader.getSystemClassLoader().getResource(name + ".sqlx");
    assertNotNull(name, sqlx);
    return SQL.INSERT(connection, ($Database)Bindings.parse(sqlx));
  }
}