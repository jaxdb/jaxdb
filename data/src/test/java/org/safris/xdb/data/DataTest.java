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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.safris.commons.lang.Resources;
import org.safris.commons.lang.Strings;
import org.safris.commons.logging.Logging;
import org.safris.commons.test.LoggableTest;
import org.safris.commons.util.Collections;
import org.safris.commons.util.Hexadecimal;
import org.safris.commons.util.Random;
import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.schema.VendorClassRunner;
import org.safris.xdb.schema.VendorIntegration;
import org.safris.xdb.schema.VendorTest;
import org.safris.xdb.schema.vendor.Derby;
import org.safris.xdb.schema.vendor.MySQL;
import org.safris.xdb.schema.vendor.PostgreSQL;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.generator.Generator;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

@RunWith(VendorClassRunner.class)
@VendorTest(Derby.class)
@VendorIntegration({MySQL.class, PostgreSQL.class})
public class DataTest extends LoggableTest {
  static {
    Logging.setLevel(Level.FINE);
  }

  private static final File sourcesDestDir = new File("target/generated-test-sources/xsb");
  private static final File resourcesDestDir = new File("target/generated-test-resources/xdb");

  private static void testData(final Connection connection, final String name, final boolean loadData) throws IOException, SQLException, TransformerException, XMLException {
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
      final String bigintType = (Math.random() < .5 ? "-" : "") + Random.numeric((int)(Math.random() * 18) + 1);
      final String binaryType = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String blobType = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String booleanType = String.valueOf(Math.random() < .5);
      final String clobType = Strings.getRandomAlphaNumericString((int)(Math.random() * 255));
      final String dateType = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String datetimeType = LocalDateTime.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String timeType = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String charType = Math.random() < .2 ? Strings.getRandomAlphaString((int)(Math.random() * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) + "." + String.valueOf((int)(Math.random() * 255)) : Math.random() < .2 ? datetimeType : Math.random() < .2 ? dateType : timeType;
      final String decimalType = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String floatType = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String doubleType = String.valueOf((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 8)));
      final String intType = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 10))));
      final String mediumintType = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 5))));
      final String smallintType = String.valueOf((int)((Math.random() - .5) * Math.pow(10, (int)(Math.random() * 3))));
      final String enumType = values[(int)(Math.random() * values.length)];

      out.write("  <Type\n".getBytes());
      if (Math.random() < .9)
        out.write(("    bigintType=\"" + bigintType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    binaryType=\"" + binaryType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    blobType=\"" + blobType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    booleanType=\"" + booleanType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    charType=\"" + charType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    clobType=\"" + clobType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    dateType=\"" + dateType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    datetimeType=\"" + datetimeType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    doubleType=\"" + doubleType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    decimalType=\"" + decimalType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    enumType=\"" + enumType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    floatType=\"" + floatType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    intType=\"" + intType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    mediumintType=\"" + mediumintType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    smallintType=\"" + smallintType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    timeType=\"" + timeType + "\"").getBytes());
      out.write(("/>\n").getBytes());
    }

    out.write("</Types>".getBytes());
  }

  @Test
  public void testClassicModels(final Connection connection) throws IOException, SQLException, TransformerException, XMLException {
    testData(connection, "classicmodels", true);
  }

  @Test
  public void testWorld(final Connection connection) throws IOException, SQLException, TransformerException, XMLException {
    testData(connection, "world", false);
  }

  @Test
  public void testTypes(final Connection connection) throws IOException, SQLException, TransformerException, XMLException {
    resourcesDestDir.mkdirs();
    try (final OutputStream out = new FileOutputStream(new File(resourcesDestDir, "types.xdd"))) {
      createTypeData(out);
    }

    testData(connection, "types", true);
  }
}