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

package org.jaxdb.ddlx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$BigintCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$CharCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$DecimalCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$DoubleCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$FloatCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IntCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$RangeOperator;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$SmallintCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$TinyintCheck;
import org.openjax.xml.datatype.HexBinary;

public class dt {
  public abstract static class Column<T extends Serializable> implements Serializable {
    final T value;

    public Column(final T value) {
      this.value = value;
    }

    public T get() {
      return value;
    }

    @Override
    public String toString() {
      return value == null ? null : value.toString();
    }
  }

  public static class BOOLEAN extends Column<java.lang.Boolean> {
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

  public static class BIGINT extends Column<Long> {
    static $Bigint addCheck(final $Bigint column, final $BigintCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Bigint.Max$(Long.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Bigint.Min$(Long.valueOf(check.getValue$().text())));
      else {
        final $Bigint.Check typedCheck = new $Bigint.Check();
        typedCheck.setValue$(new $BigintCheck.Value$(Long.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Bigint.Check.Operator$($Bigint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Bigint addCheck(final $Bigint column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Bigint.Max$(Long.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Bigint.Min$(Long.valueOf(check.getValue$().text())));
      else {
        final $Bigint.Check typedCheck = new $Bigint.Check();
        typedCheck.setValue$(new $BigintCheck.Value$(Long.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Bigint.Check.Operator$($Bigint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    public static String print(final BIGINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static BIGINT parse(final String string) {
      return string == null ? null : new BIGINT(string);
    }

    public BIGINT(final Long value) {
      super(value);
    }

    public BIGINT(final String value) {
      super(value == null ? null : Long.valueOf(value));
    }
  }

  public static class BINARY extends Column<String> {
    public static String print(final BINARY binding) {
      return binding == null ? null : binding.toString();
    }

    public static BINARY parse(final String string) {
      return string == null ? null : new BINARY(string);
    }

    public BINARY(final HexBinary value) {
      super(value == null ? null : value.toString());
    }

    public BINARY(final String value) {
      super(value);
    }
  }

  public static class BLOB extends Column<String> {
    public static String print(final BLOB binding) {
      return binding == null ? null : binding.toString();
    }

    public static BLOB parse(final String string) {
      return string == null ? null : new BLOB(string);
    }

    public BLOB(final HexBinary value) {
      super(value == null ? null : value.toString());
    }

    public BLOB(final String value) {
      super(value);
    }
  }

  public static class CHAR extends Column<String> {
    static $Char addCheck(final $Char column, final $CharCheck check) {
      final $Char.Check typedCheck = new $Char.Check();
      typedCheck.setValue$(new $CharCheck.Value$(check.getValue$().text()));
      typedCheck.setOperator$(new $Char.Check.Operator$($Char.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
      column.setCheck(typedCheck);

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Char addCheck(final $Char column, final $CheckReference check) {
      final $Char.Check typedCheck = new $Char.Check();
      typedCheck.setValue$(new $CharCheck.Value$(check.getValue$().text()));
      typedCheck.setOperator$(new $Char.Check.Operator$($Char.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
      column.setCheck(typedCheck);

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

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

  public static class CLOB extends Column<String> {
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

  public static class DATE extends Column<LocalDate> {
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

  public static class DATETIME extends Column<LocalDateTime> {
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

  public static class DECIMAL extends Column<BigDecimal> {
    static $Decimal addCheck(final $Decimal column, final $DecimalCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text())) {
        column.setMax$(new $Decimal.Max$(check.getValue$().text()));
      }
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text())) {
        column.setMin$(new $Decimal.Min$(check.getValue$().text()));
      }
      else {
        final $Decimal.Check typedCheck = new $Decimal.Check();
        typedCheck.setValue$(new $DecimalCheck.Value$(check.getValue$().text()));
        typedCheck.setOperator$(new $Decimal.Check.Operator$($Decimal.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Decimal addCheck(final $Decimal column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text())) {
        column.setMax$(new $Decimal.Max$(new BigDecimal(check.getValue$().text())));
      }
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text())) {
        column.setMin$(new $Decimal.Min$(new BigDecimal(check.getValue$().text())));
      }
      else {
        final $Decimal.Check typedCheck = new $Decimal.Check();
        typedCheck.setValue$(new $DecimalCheck.Value$(new BigDecimal(check.getValue$().text())));
        typedCheck.setOperator$(new $Decimal.Check.Operator$($Decimal.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

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
      return value == null ? null : value.stripTrailingZeros().toPlainString();
    }
  }

  public static class DOUBLE extends Column<java.lang.Double> {
    static $Double addCheck(final $Double column, final $DoubleCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Double.Max$(Double.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Double.Min$(Double.valueOf(check.getValue$().text())));
      else {
        final $Double.Check typedCheck = new $Double.Check();
        typedCheck.setValue$(new $DoubleCheck.Value$(Double.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Double.Check.Operator$($Double.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Double addCheck(final $Double column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Double.Max$(Double.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Double.Min$(Double.valueOf(check.getValue$().text())));
      else {
        final $Double.Check typedCheck = new $Double.Check();
        typedCheck.setValue$(new $DoubleCheck.Value$(Double.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Double.Check.Operator$($Double.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

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

  public static class ENUM extends Column<String> {
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

  public static class FLOAT extends Column<java.lang.Float> {
    static $Float addCheck(final $Float column, final $FloatCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Float.Max$(Float.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Float.Min$(Float.valueOf(check.getValue$().text())));
      else {
        final $Float.Check typedCheck = new $Float.Check();
        typedCheck.setValue$(new $FloatCheck.Value$(Float.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Float.Check.Operator$($Float.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Float addCheck(final $Float column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Float.Max$(Float.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Float.Min$(Float.valueOf(check.getValue$().text())));
      else {
        final $Float.Check typedCheck = new $Float.Check();
        typedCheck.setValue$(new $FloatCheck.Value$(Float.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Float.Check.Operator$($Float.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

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

  public static class INT extends Column<Integer> {
    static $Int addCheck(final $Int column, final $IntCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Int.Max$(Integer.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Int.Min$(Integer.valueOf(check.getValue$().text())));
      else {
        final $Int.Check typedCheck = new $Int.Check();
        typedCheck.setValue$(new $IntCheck.Value$(Integer.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Int.Check.Operator$($Int.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Int addCheck(final $Int column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Int.Max$(Integer.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Int.Min$(Integer.valueOf(check.getValue$().text())));
      else {
        final $Int.Check typedCheck = new $Int.Check();
        typedCheck.setValue$(new $IntCheck.Value$(Integer.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Int.Check.Operator$($Int.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    public static String print(final INT binding) {
      return binding == null ? null : binding.toString();
    }

    public static INT parse(final String string) {
      return string == null ? null : new INT(string);
    }

    public INT(final Integer value) {
      super(value);
    }

    public INT(final String value) {
      super(value == null ? null : Integer.valueOf(value));
    }
  }

  public static class SMALLINT extends Column<Short> {
    static $Smallint addCheck(final $Smallint column, final $SmallintCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Smallint.Max$(Short.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Smallint.Min$(Short.valueOf(check.getValue$().text())));
      else {
        final $Smallint.Check typedCheck = new $Smallint.Check();
        typedCheck.setValue$(new $SmallintCheck.Value$(Short.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Smallint.Check.Operator$($Smallint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Smallint addCheck(final $Smallint column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Smallint.Max$(Short.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Smallint.Min$(Short.valueOf(check.getValue$().text())));
      else {
        final $Smallint.Check typedCheck = new $Smallint.Check();
        typedCheck.setValue$(new $SmallintCheck.Value$(Short.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Smallint.Check.Operator$($Smallint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    public static String print(final SMALLINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static SMALLINT parse(final String string) {
      return string == null ? null : new SMALLINT(string);
    }

    public SMALLINT(final Short value) {
      super(value);
    }

    public SMALLINT(final String value) {
      super(value == null ? null : Short.valueOf(value));
    }
  }

  public static class TIME extends Column<LocalTime> {
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

  public static class TINYINT extends Column<Byte> {
    static $Tinyint addCheck(final $Tinyint column, final $TinyintCheck check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Tinyint.Max$(Byte.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Tinyint.Min$(Byte.valueOf(check.getValue$().text())));
      else {
        final $Tinyint.Check typedCheck = new $Tinyint.Check();
        typedCheck.setValue$(new $TinyintCheck.Value$(Byte.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Tinyint.Check.Operator$($Tinyint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    static $Tinyint addCheck(final $Tinyint column, final $CheckReference check) {
      if ($RangeOperator.lte.text().equals(check.getOperator$().text()))
        column.setMax$(new $Tinyint.Max$(Byte.valueOf(check.getValue$().text())));
      else if ($RangeOperator.gte.text().equals(check.getOperator$().text()))
        column.setMin$(new $Tinyint.Min$(Byte.valueOf(check.getValue$().text())));
      else {
        final $Tinyint.Check typedCheck = new $Tinyint.Check();
        typedCheck.setValue$(new $TinyintCheck.Value$(Byte.valueOf(check.getValue$().text())));
        typedCheck.setOperator$(new $Tinyint.Check.Operator$($Tinyint.Check.Operator$.Enum.valueOf(check.getOperator$().text())));
        column.setCheck(typedCheck);
      }

      // TODO: Implement OR.
      if (check.getAnd() != null)
        addCheck(column, check.getAnd());

      return column;
    }

    public static String print(final TINYINT binding) {
      return binding == null ? null : binding.toString();
    }

    public static TINYINT parse(final String string) {
      return string == null ? null : new TINYINT(string);
    }

    public TINYINT(final Byte value) {
      super(value);
    }

    public TINYINT(final String value) {
      super(value == null ? null : Byte.valueOf(value));
    }
  }
}