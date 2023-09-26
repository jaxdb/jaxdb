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

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.TestSpec;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.junit.runner.RunWith;
import org.libj.lang.Hexadecimal;
import org.libj.lang.Strings;
import org.xml.sax.SAXException;

public abstract class TypesTest extends SQLxTest {
  private static final String name = "types";

  public static class Test extends SQLxTest {
    @org.junit.Test
    public void testCreate() throws IOException, TransformerException {
      createXSDs(name);
      final File destDir = new File("target/test-classes/jaxdb");
      destDir.mkdirs();
      try (final OutputStreamWriter out = new FileWriter(new File(destDir, "types.sqlx"))) {
        createTypeData(out);
      }
    }
  }

  @RunWith(DBTestRunner.class)
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends TypesTest {
  }

  @RunWith(DBTestRunner.class)
  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends TypesTest {
  }

  private static void createTypeData(final OutputStreamWriter out) throws IOException {
    final String[] values = {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    out.write("<types xmlns=\"urn:jaxdb:sqlx:types\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n  xsi:schemaLocation=\"urn:jaxdb:sqlx:types types.xsd\">\n\n");
    for (int i = 0; i < 1000; ++i) { // [N]
      final String bigintType = (Math.random() < .5 ? "-" : "") + Strings.getRandomNumeric((int)(Math.random() * 18) + 1);
      final String binaryType = new Hexadecimal(Strings.getRandomAlphaNumeric(255).getBytes()).toString().toUpperCase();
      final String blobType = new Hexadecimal(Strings.getRandomAlphaNumeric(255).getBytes()).toString().toUpperCase();
      final String booleanType = String.valueOf(Math.random() < .5);
      final String clobType = Strings.getRandomAlphaNumeric((int)(Math.random() * 255));
      final String decimalType = BigDecimal.valueOf(((Math.random() - .5) * (1 << (int)(Math.random() * 64) - 1))).setScale(10, RoundingMode.FLOOR).toString();
      final String dateType = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String datetimeType = LocalDateTime.of(2010 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String timeType = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String charType = Math.random() < .2 ? Strings.getRandomAlpha((int)(Math.random() * 255)) : Math.random() < .2 ? String.valueOf((int)((Math.random() - .5) * 255)) : Math.random() < .2 ? (int)((Math.random() - .5) * 255) + "." + (int)(Math.random() * 255) : Math.random() < .2 ? datetimeType : Math.random() < .2 ? dateType : timeType;
      final String floatType = String.valueOf((Math.random() - .5) * (StrictMath.pow(2, (int)(Math.random() * 16) - 1)));
      final String doubleType = String.valueOf((Math.random() - .5) * (StrictMath.pow(2, (int)(Math.random() * 32) - 1)));
      final String intType = String.valueOf((int)((Math.random() - .5) * (StrictMath.pow(2, (int)(Math.random() * 32) - 1))));
      final String smallintType = String.valueOf((int)((Math.random() - .5) * (StrictMath.pow(2, (int)(Math.random() * 16) - 1))));
      final String tinyintType = String.valueOf((int)((Math.random() - .5) * (StrictMath.pow(2, (int)(Math.random() * 8) - 1))));
      final String enumType = values[(int)(Math.random() * values.length)];

      out.write("    <type");
      if (Math.random() < .9)
        out.write("\n      bigint_type=\"" + bigintType + "\"");
      if (Math.random() < .9)
        out.write("\n      binary_type=\"" + binaryType + "\"");
      if (Math.random() < .9)
        out.write("\n      blob_type=\"" + blobType + "\"");
      if (Math.random() < .9)
        out.write("\n      boolean_type=\"" + booleanType + "\"");
      if (Math.random() < .9)
        out.write("\n      char_type=\"" + charType + "\"");
      if (Math.random() < .9)
        out.write("\n      clob_type=\"" + clobType + "\"");
      if (Math.random() < .9)
        out.write("\n      date_type=\"" + dateType + "\"");
      if (Math.random() < .9)
        out.write("\n      datetime_type=\"" + datetimeType + "\"");
      if (Math.random() < .9)
        out.write("\n      double_type=\"" + doubleType + "\"");
      if (Math.random() < .9)
        out.write("\n      decimal_type=\"" + decimalType + "\"");
      if (Math.random() < .9)
        out.write("\n      enum_type=\"" + enumType + "\"");
      if (Math.random() < .9)
        out.write("\n      float_type=\"" + floatType + "\"");
      if (Math.random() < .9)
        out.write("\n      int_type=\"" + intType + "\"");
      if (Math.random() < .9)
        out.write("\n      smallint_type=\"" + smallintType + "\"");
      if (Math.random() < .9)
        out.write("\n      tinyint_type=\"" + tinyintType + "\"");
      if (Math.random() < .9)
        out.write("\n      time_type=\"" + timeType + "\"");
      out.write("/>\n");
    }

    out.write("</types>");
    out.flush();
  }

  @org.junit.Test
  @TestSpec(order = 1)
  public void testLoadData(final Connection connection) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(connection, name);
    assertEquals(1000, loadData(connection, name).length);
  }

  @org.junit.Test
  @TestSpec(order = 2)
  public void testCreateSql(final Connection connection) throws IOException, SAXException, SQLException {
    createSql(connection, name);
  }
}