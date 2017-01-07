/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.jar.JarFile;
import java.util.logging.Level;

import javax.xml.transform.TransformerException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.io.Files;
import org.safris.commons.io.JarFiles;
import org.safris.commons.lang.Resource;
import org.safris.commons.lang.Resources;
import org.safris.commons.logging.Logging;
import org.safris.commons.net.URLs;
import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.test.LoggableTest;
import org.safris.commons.util.Collections;
import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.generator.Generator;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

@SuppressWarnings("unused")
public class DataTest extends LoggableTest {
  static {
    Logging.setLevel(Level.FINE);
  }

  private static Connection connection;

  public static Connection createConnection() throws ClassNotFoundException, IOException, SQLException {
    final File db = new File("target/generated-test-resources/test-db");
    if (db.exists() && !Files.deleteAll(db.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    final Resource resource = Resources.getResource("test-db");
    if (URLs.isJar(resource.getURL())) {
      final JarFile jarFile = new JarFile(URLs.getParentJar(resource.getURL()).getPath());
      final String path = URLs.getPathInJar(resource.getURL());
      JarFiles.extract(jarFile, path, db.getParentFile());
    }
    else {
      Files.copy(new File(resource.getURL().getPath()), db);
    }

    return new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath()));
  }

  @BeforeClass
  public static void create() throws ClassNotFoundException, IOException, SQLException {
    connection = createConnection();
  }

  @Test
  public void testData() throws ClassNotFoundException, IOException, SQLException, TransformerException, XMLException {
    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final File destFile = new File("target/generated-test-resources/xdb/classicmodels.xsd");
    Datas.createXSD(xds, destFile);

    final File destDir = new File("target/generated-test-sources/xsb");
    final GeneratorContext generatorContext = new GeneratorContext(destDir, true, true);
    new Generator(generatorContext, java.util.Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), Collections.asCollection(HashSet.class, NamespaceURI.getInstance("http://xdb.safris.org/xdd.xsd"), NamespaceURI.getInstance("http://commons.safris.org/xml/datatypes.xsd")), null).generate();
    try (final URLClassLoader classLoader = new URLClassLoader(new URL[] {destDir.toURI().toURL()})) {
      Class.forName("classicmodels.xdd.xe", true, classLoader);
    }

    final URL xdd = Resources.getResource("classicmodels.xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    Datas.loadData(connection, data);
  }

  @AfterClass
  public static void destroy() {
    new File("derby.log").deleteOnExit();
  }
}