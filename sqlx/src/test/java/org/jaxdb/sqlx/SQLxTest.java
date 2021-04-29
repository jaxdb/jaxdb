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
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.Schemas;
import org.jaxdb.sqlx_0_4.Database;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.Schema;
import org.jaxdb.www.sqlx_0_4.xLygluGCXAA.$Database;
import org.jaxsb.runtime.Bindings;
import org.libj.io.FileUtil;
import org.libj.jci.CompilationException;
import org.libj.lang.Identifiers;
import org.libj.net.URLs;
import org.libj.util.ArrayUtil;
import org.libj.util.ClassLoaders;
import org.libj.util.function.Throwing;
import org.openjax.jaxb.xjc.JaxbUtil;
import org.openjax.xml.sax.XmlPreview;
import org.openjax.xml.sax.XmlPreviewParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class SQLxTest {
  private static final Logger logger = LoggerFactory.getLogger(SQLxTest.class);
  private static final File sourcesXsbDestDir = new File("target/generated-test-sources/jaxsb");
  private static final File sourcesJaxbDestDir = new File("target/generated-test-sources/jaxb");
  static final File resourcesDestDir = new File("target/generated-test-resources/jaxdb");
  static final File testClassesDir = new File("target/test-classes");
//  private static final File[] classpath;

  static {
//    final File[] testClassPath = ClassLoaders.getTestClassPath();
//    classpath = testClassPath != null ? ArrayUtil.concat(ClassLoaders.getClassPath(), testClassPath) : ClassLoaders.getClassPath();
  }

  private static $Database to$Database(final URL sqlxFile) throws IOException, SAXException, URISyntaxException {
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
            logger.info(e.getMessage(), e);
          }
        }
      });

      final XmlPreview preview = XmlPreviewParser.parse(sqlxFile);
      SqlXsbLoader.xsd2xsb(sqlxTempDir, sqlxTempDir, preview.getImports().get(preview.getRootElement().getNamespaceURI()).toURI());

      final URLClassLoader classLoader = new URLClassLoader(ArrayUtil.concat(URLs.toURL(ClassLoaders.getClassPath()), sqlxTempDir.toURI().toURL()), ClassLoader.getSystemClassLoader());
      return ($Database)Bindings.parse(sqlxFile, classLoader);
    }
  }

  @SuppressWarnings("unchecked")
  private static Database toDatabase(final URL sqlxFile) throws IOException, SAXParseException, UnmarshalException, URISyntaxException {
    final XmlPreview preview = XmlPreviewParser.parse(sqlxFile);
    final QName rootElement = preview.getRootElement();
    Class<Database> bindingClass;
    try {
      bindingClass = (Class<Database>)Class.forName(rootElement.getLocalPart() + ".sqlx." + Identifiers.toClassCase(rootElement.getLocalPart()));
    }
    catch (final ClassNotFoundException e) {
      final File sqlxTempDir = new File(FileUtil.getTempDir(), "sqlx");
      // FIXME: Files.deleteAllOnExit() is not working!
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          FileUtil.deleteAll(sqlxTempDir.toPath());
        }
        catch (final IOException e12) {
          Throwing.rethrow(e12);
        }
      }));
      sqlxTempDir.deleteOnExit();
      final File tempDir = new File(sqlxTempDir, rootElement.getLocalPart());
      try {
        SqlJaxbLoader.xsd2jaxb(tempDir, tempDir, preview.getImports().get(preview.getRootElement().getNamespaceURI()).toURI());
        final URLClassLoader classLoader = new URLClassLoader(ArrayUtil.concat(URLs.toURL(ClassLoaders.getClassPath()), tempDir.toURI().toURL()), ClassLoader.getSystemClassLoader());
        bindingClass = (Class<Database>)Class.forName(rootElement.getLocalPart() + ".sqlx." + Identifiers.toClassCase(rootElement.getLocalPart()), true, classLoader);
      }
      catch (final ClassNotFoundException | CompilationException | JAXBException e1) {
        throw new UnsupportedOperationException(e1);
      }
    }

    return JaxbUtil.parse(bindingClass, bindingClass.getClassLoader(), sqlxFile, false);
  }

  public static void createXSDs(final String name) throws CompilationException, IOException, JAXBException, TransformerException, URISyntaxException {
    final URI ddlx = ClassLoader.getSystemClassLoader().getResource(name + ".ddlx").toURI();
    assertNotNull(ddlx);
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    SQL.ddlx2sqlXsd(ddlx, destFile);
    SqlXsbLoader.xsd2xsb(sourcesXsbDestDir, testClassesDir, destFile.toURI());
    SqlJaxbLoader.xsd2jaxb(sourcesJaxbDestDir, testClassesDir, destFile.toURI());
  }

  public static void createSql(final Connection connection, final String name) throws IOException, SAXException, SQLException, URISyntaxException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    SqlXsbLoader.sqlx2sql(vendor, to$Database(sqlx), new File(resourcesDestDir, name + "-" + vendor + ".sql"));
  }

  public static int[] loadData(final Connection connection, final String name) throws IOException, SAXException, SQLException {
    final Schema schema;
    try (final InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(name + ".ddlx")) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(name, sqlx);
    return SQL.INSERT(connection, ($Database)Bindings.parse(sqlx));
  }
}