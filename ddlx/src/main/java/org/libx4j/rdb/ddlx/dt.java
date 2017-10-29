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

package org.libx4j.rdb.ddlx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_decimal;
import org.libx4j.rdb.ddlx.xe.$ddlx_double;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_rangeOperator;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_tinyint;

public class dt {
  public static abstract class DataType<T> implements Serializable {
    private static final long serialVersionUID = 5744263596183372559L;
    protected final T value;

    public DataType(final T value) {
      this.value = value;
    }

    public T get() {
      return this.value;
    }

    @Override
    public String toString() {
      return value == null ? null : value.toString();
    }
  }

  public static class BOOLEAN extends DataType<java.lang.Boolean> {
    private static final long serialVersionUID = -4485533995297797429L;

    public static String print(final BOOLEAN binding) {
      return binding == null ? null : binding.toString();
    }

    public static BOOLEAN parse(final String string) {
      return string == null ? null : new BOOLEAN(string);
    }

    public BOOLEAN(final java.lang.Boolean value) {
      super(value);
    }

    public BOOLEAN(final String value) {
      super(value == null ? null : java.lang.Boolean.valueOf(value));
    }
  }

  public static class BIGINT extends DataType<BigInteger> {
    private static final long serialVersionUID = -7869441789524610043L;

    protected static $ddlx_bigint addCheck(final $ddlx_bigint column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_bigint._max$(new BigInteger(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_bigint._min$(new BigInteger(check._value(0).text())));
      else {
        final $ddlx_bigint._check typedCheck = new $ddlx_bigint._check();
        typedCheck._condition$(new $ddlx_bigint._check._condition$(new BigInteger(check._value(0).text())));
        typedCheck._operator$(new $ddlx_bigint._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final BIGINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static BIGINT parse(final String string) {
      return string == null ? null : new BIGINT(string);
    }

    public BIGINT(final BigInteger value) {
      super(value);
    }

    public BIGINT(final String value) {
      super(value == null ? null : new BigInteger(value));
    }
  }

  public static class BINARY extends DataType<String> {
    private static final long serialVersionUID = -6480289821282094847L;

    public static String print(final BINARY binding) {
      return binding == null ? null : binding.toString();
    }

    public static BINARY parse(final String string) {
      return string == null ? null : new BINARY(string);
    }

    public BINARY(final String value) {
      super(value);
    }
  }

  public static class BLOB extends DataType<String> {
    private static final long serialVersionUID = 363574745916397965L;

    public static String print(final BLOB binding) {
      return binding == null ? null : binding.toString();
    }

    public static BLOB parse(final String string) {
      return string == null ? null : new BLOB(string);
    }

    public BLOB(final String value) {
      super(value);
    }
  }

  public static class CHAR extends DataType<String> {
    private static final long serialVersionUID = 4342711843352764121L;

    protected static $ddlx_char addCheck(final $ddlx_char column, final $ddlx_check check) {
      final $ddlx_char._check typedCheck = new $ddlx_char._check();
      typedCheck._condition$(new $ddlx_char._check._condition$(check._value(0).text()));
      typedCheck._operator$(new $ddlx_char._check._operator$(check._operator(0).text()));
      column._check(typedCheck);

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final CHAR binding) {
      return binding == null ? null : binding.toString();
    }

    public static CHAR parse(final String string) {
      return string == null ? null : new CHAR(string);
    }

    public CHAR(final String value) {
      super(value);
    }
  }

  public static class CLOB extends DataType<String> {
    private static final long serialVersionUID = -4971755608584963685L;

    public static String print(final CLOB binding) {
      return binding == null ? null : binding.toString();
    }

    public static CLOB parse(final String string) {
      return string == null ? null : new CLOB(string);
    }

    public CLOB(final String value) {
      super(value);
    }
  }

  public static class DATE extends DataType<LocalDate> {
    private static final long serialVersionUID = -2990133263070615612L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    public static String print(final DATE binding) {
      return binding == null ? null : binding.toString();
    }

    public static DATE parse(final String string) {
      return string == null ? null : new DATE(string);
    }

    public DATE(final LocalDate value) {
      super(value);
    }

    public DATE(final String value) {
      super(value == null ? null : LocalDate.parse(value, FORMATTER));
    }

    @Override
    public String toString() {
      return value == null ? null : value.format(FORMATTER);
    }
  }

  public static class DATETIME extends DataType<LocalDateTime> {
    private static final long serialVersionUID = -6612981768174021637L;
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

    public static Timestamp toTimestamp(final LocalDateTime dateTime) {
      return dateTime == null ? null : Timestamp.valueOf(dateTime.format(FORMATTER));
    }

    public static String print(final DATETIME binding) {
      return binding == null ? null : binding.toString();
    }

    public static DATETIME parse(final String string) {
      return string == null ? null : new DATETIME(string);
    }

    public DATETIME(final LocalDateTime value) {
      super(value);
    }

    public DATETIME(final String value) {
      super(value == null ? null : LocalDateTime.parse(value, FORMATTER));
    }

    @Override
    public String toString() {
      return value == null ? null : value.format(FORMATTER);
    }
  }

  public static class DECIMAL extends DataType<BigDecimal> {
    private static final long serialVersionUID = -7880579934877572719L;

    protected static $ddlx_decimal addCheck(final $ddlx_decimal column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_decimal._max$(new BigDecimal(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_decimal._min$(new BigDecimal(check._value(0).text())));
      else {
        final $ddlx_decimal._check typedCheck = new $ddlx_decimal._check();
        typedCheck._condition$(new $ddlx_decimal._check._condition$(new BigDecimal(check._value(0).text())));
        typedCheck._operator$(new $ddlx_decimal._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final DECIMAL binding) {
      return binding == null ? null : binding.toString();
    }

    public static DECIMAL parse(final String string) {
      return string == null ? null : new DECIMAL(string);
    }

    public DECIMAL(final BigDecimal value) {
      super(value);
    }

    public DECIMAL(final String value) {
      super(value == null ? null : new BigDecimal(value));
    }

    @Override
    public String toString() {
      return value == null ? null : value.stripTrailingZeros().toString();
    }
  }

  public static class DOUBLE extends DataType<java.lang.Double> {
    private static final long serialVersionUID = 8510411838107614004L;

    protected static $ddlx_double addCheck(final $ddlx_double column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_double._max$(Double.valueOf(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_double._min$(Double.valueOf(check._value(0).text())));
      else {
        final $ddlx_double._check typedCheck = new $ddlx_double._check();
        typedCheck._condition$(new $ddlx_double._check._condition$(Double.valueOf(check._value(0).text())));
        typedCheck._operator$(new $ddlx_double._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final DOUBLE binding) {
      return binding == null ? null : binding.toString();
    }

    public static DOUBLE parse(final String string) {
      return string == null ? null : new DOUBLE(string);
    }

    public DOUBLE(final java.lang.Double value) {
      super(value);
    }

    public DOUBLE(final String value) {
      super(value == null ? null : java.lang.Double.valueOf(value));
    }
  }

  public static class ENUM extends DataType<String> {
    private static final long serialVersionUID = 5895800099722941093L;

    public static String print(final ENUM binding) {
      return binding == null ? null : binding.toString();
    }

    public static ENUM parse(final String string) {
      return string == null ? null : new ENUM(string);
    }

    public ENUM(final String value) {
      super(value);
    }
  }

  public static class FLOAT extends DataType<java.lang.Float> {
    private static final long serialVersionUID = 8510411838107614004L;

    protected static $ddlx_float addCheck(final $ddlx_float column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_float._max$(Float.valueOf(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_float._min$(Float.valueOf(check._value(0).text())));
      else {
        final $ddlx_float._check typedCheck = new $ddlx_float._check();
        typedCheck._condition$(new $ddlx_float._check._condition$(Float.valueOf(check._value(0).text())));
        typedCheck._operator$(new $ddlx_float._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final FLOAT binding) {
      return binding == null ? null : binding.toString();
    }

    public static FLOAT parse(final String string) {
      return string == null ? null : new FLOAT(string);
    }

    public FLOAT(final java.lang.Float value) {
      super(value);
    }

    public FLOAT(final String value) {
      super(value == null ? null : java.lang.Float.valueOf(value));
    }
  }

  public static class INT extends DataType<Long> {
    private static final long serialVersionUID = -7869441789524610043L;

    protected static $ddlx_int addCheck(final $ddlx_int column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_int._max$(new BigInteger(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_int._min$(new BigInteger(check._value(0).text())));
      else {
        final $ddlx_int._check typedCheck = new $ddlx_int._check();
        typedCheck._condition$(new $ddlx_int._check._condition$(new BigInteger(check._value(0).text())));
        typedCheck._operator$(new $ddlx_int._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final INT binding) {
      return binding == null ? null : binding.toString();
    }

    public static INT parse(final String string) {
      return string == null ? null : new INT(string);
    }

    public INT(final Long value) {
      super(value);
    }

    public INT(final String value) {
      super(value == null ? null : Long.valueOf(value));
    }
  }

  public static class SMALLINT extends DataType<java.lang.Integer> {
    private static final long serialVersionUID = -7869441789524610043L;

    protected static $ddlx_smallint addCheck(final $ddlx_smallint column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_smallint._max$(new BigInteger(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_smallint._min$(new BigInteger(check._value(0).text())));
      else {
        final $ddlx_smallint._check typedCheck = new $ddlx_smallint._check();
        typedCheck._condition$(new $ddlx_smallint._check._condition$(new BigInteger(check._value(0).text())));
        typedCheck._operator$(new $ddlx_smallint._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final SMALLINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static SMALLINT parse(final String string) {
      return string == null ? null : new SMALLINT(string);
    }

    public SMALLINT(final java.lang.Integer value) {
      super(value);
    }

    public SMALLINT(final String value) {
      super(value == null ? null : new java.lang.Integer(value));
    }
  }

  public static class TIME extends DataType<LocalTime> {
    private static final long serialVersionUID = 7396289524599140702L;
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

    public static String print(final TIME binding) {
      return binding == null ? null : binding.toString();
    }

    public static TIME parse(final String string) {
      return string == null ? null : new TIME(string);
    }

    public TIME(final LocalTime value) {
      super(value);
    }

    public TIME(final String value) {
      super(value == null ? null : LocalTime.parse(value, FORMATTER));
    }

    @Override
    public String toString() {
      return value == null ? null : value.format(FORMATTER);
    }
  }

  public static class TINYINT extends DataType<Short> {
    private static final long serialVersionUID = -7869441789524610043L;

    protected static $ddlx_tinyint addCheck(final $ddlx_tinyint column, final $ddlx_check check) {
      if ($ddlx_rangeOperator.lte.text().equals(check._operator(0).text()))
        column._max$(new $ddlx_tinyint._max$(new BigInteger(check._value(0).text())));
      else if ($ddlx_rangeOperator.gte.text().equals(check._operator(0).text()))
        column._min$(new $ddlx_tinyint._min$(new BigInteger(check._value(0).text())));
      else {
        final $ddlx_tinyint._check typedCheck = new $ddlx_tinyint._check();
        typedCheck._condition$(new $ddlx_tinyint._check._condition$(new BigInteger(check._value(0).text())));
        typedCheck._operator$(new $ddlx_tinyint._check._operator$(check._operator(0).text()));
        column._check(typedCheck);
      }

      // TODO: Implement OR.
      if (check._and() != null && check._and().size() > 0)
        addCheck(column, check._and(0));

      return column;
    }

    public static String print(final TINYINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static TINYINT parse(final String string) {
      return string == null ? null : new TINYINT(string);
    }

    public TINYINT(final Short value) {
      super(value);
    }

    public TINYINT(final String value) {
      super(value == null ? null : Short.valueOf(value));
    }
  }
}