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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
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
import org.safris.commons.lang.Strings;
import org.safris.commons.logging.Logging;
import org.safris.commons.net.URLs;
import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.test.LoggableTest;
import org.safris.commons.util.Collections;
import org.safris.commons.util.Hexadecimal;
import org.safris.commons.util.Random;
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

  private static final File sourcesDestDir = new File("target/generated-test-sources/xsb");
  private static final File resourcesDestDir = new File("target/generated-test-resources/xdb");
  private static Connection connection;

  public static Connection createConnection() throws ClassNotFoundException, IOException, SQLException {
    final File db = new File("target/generated-test-resources/derby/test-db");
    if (db.exists() && !Files.deleteAll(db.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    final File testClasses = new File("target/test-classes/test-db");
    if (testClasses.exists() && !Files.deleteAll(testClasses.toPath()))
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

    new File(db, "tmp").mkdir();
    return new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath()));
  }

  @BeforeClass
  public static void create() throws ClassNotFoundException, IOException, SQLException {
    connection = createConnection();
  }

  private static void testData(final String name, final boolean loadData) throws IOException, ReflectiveOperationException, SQLException, TransformerException, XMLException {
    final URL xds = Resources.getResource(name + ".xds").getURL();
    final File destFile = new File(resourcesDestDir, name + ".xsd");
    Datas.createXSD(xds, destFile);

    final Set<NamespaceURI> excludes = Collections.asCollection(HashSet.class, NamespaceURI.getInstance("http://xdb.safris.org/xdd.xsd"), NamespaceURI.getInstance("http://commons.safris.org/xml/datatypes.xsd"));
    final GeneratorContext generatorContext = new GeneratorContext(sourcesDestDir, true, true, false, null, excludes);
    new Generator(generatorContext, java.util.Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), null).generate();

    if (loadData) {
      final URL xdd = Resources.getResource(name + ".xdd").getURL();
      final $xdd_data data;
      try (final InputStream in = xdd.openStream()) {
        data = ($xdd_data)Bindings.parse(new InputSource(in));
      }

      Datas.loadData(connection, data);
    }
  }

  private static void createTypeData(final OutputStream out) throws IOException {
    final String[] values = new String[] {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    out.write("<Types xmlns=\"xdd.types\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n  xsi:schemaLocation=\"xdd.types types.xsd\">\n".getBytes());
    for (int i = 0; i < 1000; i++) {
      final String bigInt = (Math.random() < .5 ? "-" : "") + Random.numeric((int)(Math.random() * 18) + 1);
      final String binary = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String blob = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String bool = String.valueOf(Math.random() < .5);
      final String clob = Strings.getRandomAlphaNumericString((int)(Math.random() * 255));
      final String date = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String dateTime = LocalDateTime.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String time = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String typeChar = Math.random() < .2 ? Strings.getRandomAlphaString((int)(Math.random() * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) + "." + String.valueOf((int)(Math.random() * 255)) : Math.random() < .2 ? dateTime : Math.random() < .2 ? date : time;
      final String decimal = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String flt = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String dbl = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String lng = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 10))));
      final String mediumInt = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 5))));
      final String tinyInt = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 3))));
      final String enm = values[(int)(Math.random() * values.length)];

      out.write("  <Type\n".getBytes());
      if (Math.random() < .9)
        out.write(("    typeBigint=\"" + bigInt + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeBinary=\"" + binary + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeBlob=\"" + blob + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeBoolean=\"" + bool + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeChar=\"" + typeChar + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeClob=\"" + clob + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeDate=\"" + date + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeDatetime=\"" + dateTime + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeDouble=\"" + dbl + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeEnum=\"" + enm + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeFloat=\"" + flt + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeLong=\"" + lng + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeMediumint=\"" + mediumInt + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeSmallint=\"" + tinyInt + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    typeTime=\"" + time + "\"").getBytes());
      out.write(("/>\n").getBytes());
    }

    out.write("</Types>".getBytes());
  }

  @Test
  public void testClassicModels() throws IOException, ReflectiveOperationException, SQLException, TransformerException, XMLException {
    testData("classicmodels", true);
  }

  @Test
  public void testWorld() throws IOException, ReflectiveOperationException, SQLException, TransformerException, XMLException {
    testData("world", false);
  }

  @Test
  public void testTypes() throws IOException, ReflectiveOperationException, SQLException, TransformerException, XMLException {
    resourcesDestDir.mkdirs();
    try (final OutputStream out = new FileOutputStream(new File(resourcesDestDir, "types.xdd"))) {
      createTypeData(out);
    }

    testData("types", true);
  }

  @AfterClass
  public static void destroy() throws SQLException {
    new File("derby.log").deleteOnExit();
    try {
      DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }
    catch (final SQLException e) {
      if (!"XJ015".equals(e.getSQLState()))
        throw e;
    }
  }
}