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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.DDLxTest;
import org.openjax.rdb.ddlx.GeneratorExecutionException;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.standard.jci.CompilationException;
import org.openjax.standard.util.Hexadecimal;
import org.openjax.standard.util.Strings;
import org.openjax.standard.xml.api.ValidationException;

public abstract class TypesTest extends SQLxTest {
  private static final String name = "types";

  public static class Test extends SQLxTest {
    @org.junit.Test
    public void testCreate() throws CompilationException, IOException, JAXBException, TransformerException {
      createXSDs(name);
      final File destDir = new File("target/test-classes/rdb");
      destDir.mkdirs();
      try (final OutputStreamWriter out = new FileWriter(new File(destDir, "types.sqlx"))) {
        createTypeData(out);
      }
    }
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends TypesTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends TypesTest {
  }

  private static void createTypeData(final OutputStreamWriter out) throws IOException {
    final String[] values = {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    out.write("<types xmlns=\"sqlx.types\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n  xsi:schemaLocation=\"sqlx.types types.xsd\">\n  <insert>\n");
    for (int i = 0; i < 1000; ++i) {
      final String bigintType = (Math.random() < .5 ? "-" : "") + Strings.getRandomNumeric((int)(Math.random() * 18) + 1);
      final String binaryType = new Hexadecimal(Strings.getRandomAlphaNumeric(255).getBytes()).toString().toUpperCase();
      final String blobType = new Hexadecimal(Strings.getRandomAlphaNumeric(255).getBytes()).toString().toUpperCase();
      final String booleanType = String.valueOf(Math.random() < .5);
      final String clobType = Strings.getRandomAlphaNumeric((int)(Math.random() * 255));
      final String dateType = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String datetimeType = LocalDateTime.of(2010 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String timeType = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String charType = Math.random() < .2 ? Strings.getRandomAlpha((int)(Math.random() * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) + "." + String.valueOf((int)(Math.random() * 255)) : Math.random() < .2 ? datetimeType : Math.random() < .2 ? dateType : timeType;
      final String decimalType = BigDecimal.valueOf(((Math.random() - .5) * Math.pow(2, (int)(Math.random() * 64) - 1))).setScale(10, RoundingMode.FLOOR).toString();
      final String floatType = String.valueOf((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 16) - 1)));
      final String doubleType = String.valueOf((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 32) - 1)));
      final String intType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 32) - 1))));
      final String smallintType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 16) - 1))));
      final String tinyintType = String.valueOf((int)((Math.random() - .5) * (Math.pow(2, (int)(Math.random() * 8) - 1))));
      final String enumType = values[(int)(Math.random() * values.length)];

      out.write("    <type");
      if (Math.random() < .9)
        out.write(("\n      bigintType=\"" + bigintType + "\""));
      if (Math.random() < .9)
        out.write(("\n      binaryType=\"" + binaryType + "\""));
      if (Math.random() < .9)
        out.write(("\n      blobType=\"" + blobType + "\""));
      if (Math.random() < .9)
        out.write(("\n      booleanType=\"" + booleanType + "\""));
      if (Math.random() < .9)
        out.write(("\n      charType=\"" + charType + "\""));
      if (Math.random() < .9)
        out.write(("\n      clobType=\"" + clobType + "\""));
      if (Math.random() < .9)
        out.write(("\n      dateType=\"" + dateType + "\""));
      if (Math.random() < .9)
        out.write(("\n      datetimeType=\"" + datetimeType + "\""));
      if (Math.random() < .9)
        out.write(("\n      doubleType=\"" + doubleType + "\""));
      if (Math.random() < .9)
        out.write(("\n      decimalType=\"" + decimalType + "\""));
      if (Math.random() < .9)
        out.write(("\n      enumType=\"" + enumType + "\""));
      if (Math.random() < .9)
        out.write(("\n      floatType=\"" + floatType + "\""));
      if (Math.random() < .9)
        out.write(("\n      intType=\"" + intType + "\""));
      if (Math.random() < .9)
        out.write(("\n      smallintType=\"" + smallintType + "\""));
      if (Math.random() < .9)
        out.write(("\n      tinyintType=\"" + tinyintType + "\""));
      if (Math.random() < .9)
        out.write(("\n      timeType=\"" + timeType + "\""));
      out.write(("/>\n"));
    }

    out.write("  </insert>\n</types>");
    out.flush();
  }

  @org.junit.Test
  public void testLoadData(final Connection connection) throws GeneratorExecutionException, IOException, SQLException, ValidationException {
    DDLxTest.recreateSchema(connection, name);
    assertEquals(1000, loadData(connection, name).length);
  }
}