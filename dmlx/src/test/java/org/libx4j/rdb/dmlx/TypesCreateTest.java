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

package org.libx4j.rdb.dmlx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.lib4j.jci.CompilationException;
import org.lib4j.lang.Strings;
import org.lib4j.util.Hexadecimal;
import org.lib4j.util.Random;

public class TypesCreateTest extends DMLxTest {
  private static void createTypeData(final OutputStream out) throws IOException {
    final String[] values = new String[] {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    out.write("<types xmlns=\"dmlx.types\"\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n  xsi:schemaLocation=\"dmlx.types types.xsd\">\n  <insert>\n".getBytes());
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

      out.write("    <type".getBytes());
      if (Math.random() < .9)
        out.write(("\n      bigintType=\"" + bigintType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      binaryType=\"" + binaryType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      blobType=\"" + blobType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      booleanType=\"" + booleanType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      charType=\"" + charType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      clobType=\"" + clobType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      dateType=\"" + dateType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      datetimeType=\"" + datetimeType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      doubleType=\"" + doubleType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      decimalType=\"" + decimalType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      enumType=\"" + enumType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      floatType=\"" + floatType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      intType=\"" + intType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      smallintType=\"" + smallintType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      tinyintType=\"" + tinyintType + "\"").getBytes());
      if (Math.random() < .9)
        out.write(("\n      timeType=\"" + timeType + "\"").getBytes());
      out.write(("/>\n").getBytes());
    }

    out.write("  </insert>\n</types>".getBytes());
    out.flush();
  }

  @Test
  public void testCreateSchema() throws CompilationException, IOException, JAXBException, TransformerException {
    createSchemas("types");
  }

  @Test
  public void testCreateData() throws IOException {
    resourcesDestDir.mkdirs();
    try (final OutputStream out = new FileOutputStream(new File(resourcesDestDir, "types.dmlx"))) {
      createTypeData(out);
    }
  }
}