package org.safris.xdb.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.safris.commons.lang.Strings;
import org.safris.commons.util.Hexadecimal;
import org.safris.commons.util.Random;

public class Ge {
  public static void main(final String[] args) {
    for (int i = 0; i < 1000; i++) {
      final String bigInt = Random.numeric(18);
      final String binary = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String blob = new Hexadecimal(Strings.getRandomAlphaNumericString(255).getBytes()).toString().toUpperCase();
      final String bool = String.valueOf(Math.random() < .5);
      final String ch = Strings.getRandomAlphaNumericString(255);
      final String clob = Strings.getRandomAlphaNumericString(255);
      final String date = LocalDate.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28)).format(DateTimeFormatter.ISO_DATE);
      final String dateTime = LocalDateTime.of(2000 + (int)(Math.random() * 100), 1 + (int)(Math.random() * 12), 1 + (int)(Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      final String decimal = (int)(Math.random() * 10000000) + "." +  (int)(Math.random() * 1000);
      final String flt = String.valueOf(Long.valueOf(Random.numeric(8)) / 13d);
      final String dbl = String.valueOf(Long.valueOf(Random.numeric(10)) / 13d);
      final String lng = String.valueOf((int)(Math.random() * 65536 * 65536));
      final String mediumInt = String.valueOf((int)(Math.random() * 65536));
      final String smallInt = String.valueOf((int)(Math.random() * 255));
      final String time = LocalTime.of((int)(Math.random() * 23), (int)(Math.random() * 59), (int)(Math.random() * 59)).format(DateTimeFormatter.ISO_LOCAL_TIME);
      final String[] values = new String[] {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
      final String enm = values[(int)(Math.random() * values.length)];
      System.out.println("<Type\n  typeBigint=\"" + bigInt + "\"\n  typeBinary=\"" + binary + "\"\n  typeBlob=\"" + blob + "\"\n  typeBoolean=\"" + bool + "\"\n  typeChar=\"" + ch + "\"\n  typeClob=\"" + clob + "\"\n  typeDate=\"" + date + "\"\n  typeDatetime=\"" + dateTime + "\"\n  typeDecimal=\"" + decimal + "\"\n  typeDouble=\"" + dbl + "\"\n  typeEnum=\"" + enm + "\"\n  typeFloat=\"" + flt + "\"\n  typeLong=\"" + lng + "\"\n  typeMediumint=\"" + mediumInt + "\"\n  typeSmallint=\"" + smallInt + "\"\n  typeTime=\"" + time + "\"/>");
    }
  }
}