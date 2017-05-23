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

package org.safris.rdb.dmlx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.xml.transform.TransformerException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.MixedTest;
import org.safris.commons.lang.Strings;
import org.safris.commons.util.Hexadecimal;
import org.safris.commons.util.Random;
import org.safris.commons.xml.XMLException;
import org.safris.rdb.ddlx.runner.Derby;
import org.safris.rdb.ddlx.runner.MySQL;
import org.safris.rdb.ddlx.runner.Oracle;
import org.safris.rdb.ddlx.runner.PostgreSQL;
import org.safris.rdb.ddlx.runner.SQLite;
import org.safris.rdb.ddlx.runner.VendorRunner;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, SQLite.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class TypesTest extends DMLxTest {
  private static void createTypeData(final OutputStream out) throws IOException {
    final String[] values = new String[] {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    out.write("<Types xmlns=\"dmlx.types\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n  xsi:schemaLocation=\"dmlx.types types.xsd\">\n".getBytes());
    for (int i = 0; i < 1000; i++) {
      final String bigintType = (Math.random() < .5 ? "-" : "") + Random.numeric((int)(Math.random() * 18) + 1);
      final String binaryType = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String blobType = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String booleanType = String.valueOf(Math.random() < .5);
      final String clobType = Strings.getRandomAlphaNumericString((int)(Math.random() * 255));
      final String dateType = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String datetimeType = LocalDateTime.of(2010 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String timeType = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String charType = Math.random() < .2 ? Strings.getRandomAlphaString((int)(Math.random() * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) + "." + String.valueOf((int)(Math.random() * 255)) : Math.random() < .2 ? datetimeType : Math.random() < .2 ? dateType : timeType;
      final String decimalType = BigDecimal.valueOf(((Math.random() - .5) * Math.pow(2, (int)(Math.random() * 64) - 1))).setScale(10, BigDecimal.ROUND_DOWN).toString();
      final String floatType = String.valueOf((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 16) - 1)));
      final String doubleType = String.valueOf((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 32) - 1)));
      final String intType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 32) - 1))));
      final String smallintType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 16) - 1))));
      final String tinyintType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 8) - 1))));
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
        out.write(("    smallintType=\"" + smallintType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    tinyintType=\"" + tinyintType + "\"\n").getBytes());
      if (Math.random() < .9)
        out.write(("    timeType=\"" + timeType + "\"").getBytes());
      out.write(("/>\n").getBytes());
    }

    out.write("</Types>".getBytes());
    out.flush();
  }

  @BeforeClass
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public static void createSchema() throws IOException, TransformerException {
    createSchemas("types");
  }

  @BeforeClass
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public static void createData() throws IOException {
    resourcesDestDir.mkdirs();
    try (final OutputStream out = new FileOutputStream(new File(resourcesDestDir, "types.dmlx"))) {
      createTypeData(out);
    }
  }

  @Test
  public void testTypes(final Connection connection) throws IOException, SQLException, XMLException {
    testData(connection, "types");
  }
}