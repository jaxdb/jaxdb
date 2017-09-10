/* Copyright (c) 2014 lib4j
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

package org.libx4j.rdb.jsql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lib4j.lang.Arrays;
import org.libx4j.rdb.dmlx.xe.$dmlx_data;

@SuppressWarnings("hiding")
public final class DML {

  /** Ordering Specification **/

  @SuppressWarnings("unchecked")
  public static <V extends data.DataType<T>,T>V ASC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(operator.Ordering.ASC, dataType));
  }

  @SuppressWarnings("unchecked")
  public static <V extends data.DataType<T>,T>V DESC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(operator.Ordering.DESC, dataType));
  }

  /** START Cast **/

  public static Cast.BIGINT CAST(final type.BIGINT a) {
    return new Cast.BIGINT(a);
  }

  public static Cast.BIGINT CAST(final type.BIGINT.UNSIGNED a) {
    return new Cast.BIGINT(a);
  }

  public static Cast.BINARY CAST(final type.BINARY a) {
    return new Cast.BINARY(a);
  }

  public static Cast.BLOB CAST(final type.BLOB a) {
    return new Cast.BLOB(a);
  }

  public static Cast.CHAR CAST(final type.Textual<?> a) {
    return new Cast.CHAR(a);
  }

  public static Cast.BOOLEAN CAST(final type.BOOLEAN a) {
    return new Cast.BOOLEAN(a);
  }

  public static Cast.CLOB CAST(final type.CLOB a) {
    return new Cast.CLOB(a);
  }

  public static Cast.DATE CAST(final type.DATE a) {
    return new Cast.DATE(a);
  }

  public static Cast.DATETIME CAST(final type.DATETIME a) {
    return new Cast.DATETIME(a);
  }

  public static Cast.DECIMAL CAST(final type.DECIMAL a) {
    return new Cast.DECIMAL(a);
  }

  public static Cast.DECIMAL CAST(final type.DECIMAL.UNSIGNED a) {
    return new Cast.DECIMAL(a);
  }

  public static Cast.DOUBLE CAST(final type.DOUBLE a) {
    return new Cast.DOUBLE(a);
  }

  public static Cast.DOUBLE CAST(final type.DOUBLE.UNSIGNED a) {
    return new Cast.DOUBLE(a);
  }

  public static Cast.FLOAT CAST(final type.FLOAT a) {
    return new Cast.FLOAT(a);
  }

  public static Cast.FLOAT CAST(final type.FLOAT.UNSIGNED a) {
    return new Cast.FLOAT(a);
  }

  public static Cast.INT CAST(final type.INT a) {
    return new Cast.INT(a);
  }

  public static Cast.INT CAST(final type.INT.UNSIGNED a) {
    return new Cast.INT(a);
  }

  public static Cast.SMALLINT CAST(final type.SMALLINT a) {
    return new Cast.SMALLINT(a);
  }

  public static Cast.SMALLINT CAST(final type.SMALLINT.UNSIGNED a) {
    return new Cast.SMALLINT(a);
  }

  public static Cast.TINYINT CAST(final type.TINYINT a) {
    return new Cast.TINYINT(a);
  }

  public static Cast.TINYINT CAST(final type.TINYINT.UNSIGNED a) {
    return new Cast.TINYINT(a);
  }

  public static Cast.TIME CAST(final type.TIME a) {
    return new Cast.TIME(a);
  }

  /** END Cast **/

  /** START ComparisonPredicate **/

  public static <Number extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN EQ(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN EQ(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static data.BOOLEAN EQ(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static data.BOOLEAN EQ(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static data.BOOLEAN EQ(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN EQ(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN EQ(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.EQ, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN EQ(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.EQ, a, b);
  }

  public static data.BOOLEAN EQ(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.EQ, a, b);
  }

  public static data.BOOLEAN EQ(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN NE(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN NE(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static data.BOOLEAN NE(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static data.BOOLEAN NE(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static data.BOOLEAN NE(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN NE(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN NE(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.NE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN NE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.NE, a, b);
  }

  public static data.BOOLEAN NE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.NE, a, b);
  }

  public static data.BOOLEAN NE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LT(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LT(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static data.BOOLEAN LT(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static data.BOOLEAN LT(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static data.BOOLEAN LT(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LT(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN LT(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LT, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN LT(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LT, a, b);
  }

  public static data.BOOLEAN LT(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LT, a, b);
  }

  public static data.BOOLEAN LT(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GT(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GT(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static data.BOOLEAN GT(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static data.BOOLEAN GT(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static data.BOOLEAN GT(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GT(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN GT(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GT, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN GT(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GT, a, b);
  }

  public static data.BOOLEAN GT(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GT, a, b);
  }

  public static data.BOOLEAN GT(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LTE(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LTE(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static data.BOOLEAN LTE(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static data.BOOLEAN LTE(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static data.BOOLEAN LTE(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN LTE(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN LTE(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LTE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN LTE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LTE, a, b);
  }

  public static data.BOOLEAN LTE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LTE, a, b);
  }

  public static data.BOOLEAN LTE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GTE(final Number a, final type.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<Temporal> a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GTE(final Temporal a, final type.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static data.BOOLEAN GTE(final type.CHAR a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static data.BOOLEAN GTE(final type.CHAR a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static data.BOOLEAN GTE(final String a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.CHAR a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final E a, final type.CHAR b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.ENUM<E> a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final E a, final type.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.ENUM<E> a, final type.CHAR b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.CHAR a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final type.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>data.BOOLEAN GTE(final String a, final type.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN GTE(final type.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GTE, a, b);
  }

  public static <Textual extends Comparable<?>>data.BOOLEAN GTE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GTE, a, b);
  }

  public static data.BOOLEAN GTE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GTE, a, b);
  }

  public static data.BOOLEAN GTE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GTE, a, b);
  }

  /** END ComparisonPredicate **/

  /** SELECT **/

  @SafeVarargs
  public static <T>Select.ARRAY._SELECT<data.ARRAY<T>> SELECT(final type.ARRAY<? extends T> ... entities) {
    return new SelectImpl.ARRAY.SELECT<data.ARRAY<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.BIGINT._SELECT<data.BIGINT> SELECT(final type.BIGINT ... entities) {
    return new SelectImpl.BIGINT.SELECT<data.BIGINT>(false, entities);
  }

  @SafeVarargs
  public static Select.BIGINT.UNSIGNED._SELECT<data.BIGINT.UNSIGNED> SELECT(final type.BIGINT.UNSIGNED ... entities) {
    return new SelectImpl.BIGINT.UNSIGNED.SELECT<data.BIGINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.BINARY._SELECT<data.BINARY> SELECT(final type.BINARY ... entities) {
    return new SelectImpl.BINARY.SELECT<data.BINARY>(false, entities);
  }

  @SafeVarargs
  public static Select.BLOB._SELECT<data.BLOB> SELECT(final type.BLOB ... entities) {
    return new SelectImpl.BLOB.SELECT<data.BLOB>(false, entities);
  }

  @SafeVarargs
  public static Select.BOOLEAN._SELECT<data.BOOLEAN> SELECT(final type.BOOLEAN ... entities) {
    return new SelectImpl.BOOLEAN.SELECT<data.BOOLEAN>(false, entities);
  }

  @SafeVarargs
  public static Select.CHAR._SELECT<data.CHAR> SELECT(final type.CHAR ... entities) {
    return new SelectImpl.CHAR.SELECT<data.CHAR>(false, entities);
  }

  @SafeVarargs
  public static Select.CLOB._SELECT<data.CLOB> SELECT(final type.CLOB ... entities) {
    return new SelectImpl.CLOB.SELECT<data.CLOB>(false, entities);
  }

  @SafeVarargs
  public static <T>Select.DataType._SELECT<data.DataType<T>> SELECT(final type.DataType<? extends T> ... entities) {
    return new SelectImpl.DataType.SELECT<data.DataType<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.DATE._SELECT<data.DATE> SELECT(final type.DATE ... entities) {
    return new SelectImpl.DATE.SELECT<data.DATE>(false, entities);
  }

  @SafeVarargs
  public static Select.DATETIME._SELECT<data.DATETIME> SELECT(final type.DATETIME ... entities) {
    return new SelectImpl.DATETIME.SELECT<data.DATETIME>(false, entities);
  }

  @SafeVarargs
  public static Select.DECIMAL._SELECT<data.DECIMAL> SELECT(final type.DECIMAL ... entities) {
    return new SelectImpl.DECIMAL.SELECT<data.DECIMAL>(false, entities);
  }

  @SafeVarargs
  public static Select.DECIMAL.UNSIGNED._SELECT<data.DECIMAL.UNSIGNED> SELECT(final type.DECIMAL.UNSIGNED ... entities) {
    return new SelectImpl.DECIMAL.UNSIGNED.SELECT<data.DECIMAL.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.DOUBLE._SELECT<data.DOUBLE> SELECT(final type.DOUBLE ... entities) {
    return new SelectImpl.DOUBLE.SELECT<data.DOUBLE>(false, entities);
  }

  @SafeVarargs
  public static Select.DOUBLE.UNSIGNED._SELECT<data.DOUBLE.UNSIGNED> SELECT(final type.DOUBLE.UNSIGNED ... entities) {
    return new SelectImpl.DOUBLE.UNSIGNED.SELECT<data.DOUBLE.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends data.Entity>Select.Entity._SELECT<T> SELECT(final T ... entities) {
    return new SelectImpl.Entity.SELECT<T>(false, entities);
  }

  @SafeVarargs
  public static <T extends Enum<?> & EntityEnum>Select.ENUM._SELECT<data.ENUM<T>> SELECT(final type.ENUM<? extends T> ... entities) {
    return new SelectImpl.ENUM.SELECT<data.ENUM<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.FLOAT._SELECT<data.FLOAT> SELECT(final type.FLOAT ... entities) {
    return new SelectImpl.FLOAT.SELECT<data.FLOAT>(false, entities);
  }

  @SafeVarargs
  public static Select.FLOAT.UNSIGNED._SELECT<data.FLOAT.UNSIGNED> SELECT(final type.FLOAT.UNSIGNED ... entities) {
    return new SelectImpl.FLOAT.UNSIGNED.SELECT<data.FLOAT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.INT._SELECT<data.INT> SELECT(final type.INT ... entities) {
    return new SelectImpl.INT.SELECT<data.INT>(false, entities);
  }

  @SafeVarargs
  public static Select.INT.UNSIGNED._SELECT<data.INT.UNSIGNED> SELECT(final type.INT.UNSIGNED ... entities) {
    return new SelectImpl.INT.UNSIGNED.SELECT<data.INT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends Number>Select.Numeric._SELECT<data.Numeric<T>> SELECT(final type.Numeric<? extends T> ... entities) {
    return new SelectImpl.Numeric.SELECT<data.Numeric<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.SMALLINT._SELECT<data.SMALLINT> SELECT(final type.SMALLINT ... entities) {
    return new SelectImpl.SMALLINT.SELECT<data.SMALLINT>(false, entities);
  }

  @SafeVarargs
  public static Select.SMALLINT.UNSIGNED._SELECT<data.SMALLINT.UNSIGNED> SELECT(final type.SMALLINT.UNSIGNED ... entities) {
    return new SelectImpl.SMALLINT.UNSIGNED.SELECT<data.SMALLINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends java.time.temporal.Temporal>Select.Temporal._SELECT<data.Temporal<T>> SELECT(final type.Temporal<? extends T> ... entities) {
    return new SelectImpl.Temporal.SELECT<data.Temporal<T>>(false, entities);
  }

  @SafeVarargs
  public static <T extends Comparable<?>>Select.Textual._SELECT<data.Textual<T>> SELECT(final type.Textual<? extends T> ... entities) {
    return new SelectImpl.Textual.SELECT<data.Textual<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.TIME._SELECT<data.TIME> SELECT(final type.TIME ... entities) {
    return new SelectImpl.TIME.SELECT<data.TIME>(false, entities);
  }

  @SafeVarargs
  public static Select.TINYINT._SELECT<data.TINYINT> SELECT(final type.TINYINT ... entities) {
    return new SelectImpl.TINYINT.SELECT<data.TINYINT>(false, entities);
  }

  @SafeVarargs
  public static Select.TINYINT.UNSIGNED._SELECT<data.TINYINT.UNSIGNED> SELECT(final type.TINYINT.UNSIGNED ... entities) {
    return new SelectImpl.TINYINT.UNSIGNED.SELECT<data.TINYINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.Entity._SELECT<data.Subject<?>> SELECT(final type.Subject<?> ... entities) {
    return new SelectImpl.Entity.SELECT<data.Subject<?>>(false, entities);
  }

  public static final class SELECT {
    @SafeVarargs
    public static <T>Select.ARRAY._SELECT<data.ARRAY<T>> DISTINCT(final type.ARRAY<? extends T> ... entities) {
      return new SelectImpl.ARRAY.SELECT<data.ARRAY<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.BIGINT._SELECT<data.BIGINT> DISTINCT(final type.BIGINT ... entities) {
      return new SelectImpl.BIGINT.SELECT<data.BIGINT>(true, entities);
    }

    @SafeVarargs
    public static Select.BIGINT.UNSIGNED._SELECT<data.BIGINT.UNSIGNED> DISTINCT(final type.BIGINT.UNSIGNED ... entities) {
      return new SelectImpl.BIGINT.UNSIGNED.SELECT<data.BIGINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.BINARY._SELECT<data.BINARY> DISTINCT(final type.BINARY ... entities) {
      return new SelectImpl.BINARY.SELECT<data.BINARY>(true, entities);
    }

    @SafeVarargs
    public static Select.BLOB._SELECT<data.BLOB> DISTINCT(final type.BLOB ... entities) {
      return new SelectImpl.BLOB.SELECT<data.BLOB>(true, entities);
    }

    @SafeVarargs
    public static Select.BOOLEAN._SELECT<data.BOOLEAN> DISTINCT(final type.BOOLEAN ... entities) {
      return new SelectImpl.BOOLEAN.SELECT<data.BOOLEAN>(true, entities);
    }

    @SafeVarargs
    public static Select.CHAR._SELECT<data.CHAR> DISTINCT(final type.CHAR ... entities) {
      return new SelectImpl.CHAR.SELECT<data.CHAR>(true, entities);
    }

    @SafeVarargs
    public static Select.CLOB._SELECT<data.CLOB> DISTINCT(final type.CLOB ... entities) {
      return new SelectImpl.CLOB.SELECT<data.CLOB>(true, entities);
    }

    @SafeVarargs
    public static <T>Select.DataType._SELECT<data.DataType<T>> DISTINCT(final type.DataType<? extends T> ... entities) {
      return new SelectImpl.DataType.SELECT<data.DataType<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.DATE._SELECT<data.DATE> DISTINCT(final type.DATE ... entities) {
      return new SelectImpl.DATE.SELECT<data.DATE>(true, entities);
    }

    @SafeVarargs
    public static Select.DATETIME._SELECT<data.DATETIME> DISTINCT(final type.DATETIME ... entities) {
      return new SelectImpl.DATETIME.SELECT<data.DATETIME>(true, entities);
    }

    @SafeVarargs
    public static Select.DECIMAL._SELECT<data.DECIMAL> DISTINCT(final type.DECIMAL ... entities) {
      return new SelectImpl.DECIMAL.SELECT<data.DECIMAL>(true, entities);
    }

    @SafeVarargs
    public static Select.DECIMAL.UNSIGNED._SELECT<data.DECIMAL.UNSIGNED> DISTINCT(final type.DECIMAL.UNSIGNED ... entities) {
      return new SelectImpl.DECIMAL.UNSIGNED.SELECT<data.DECIMAL.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.DOUBLE._SELECT<data.DOUBLE> DISTINCT(final type.DOUBLE ... entities) {
      return new SelectImpl.DOUBLE.SELECT<data.DOUBLE>(true, entities);
    }

    @SafeVarargs
    public static Select.DOUBLE.UNSIGNED._SELECT<data.DOUBLE.UNSIGNED> DISTINCT(final type.DOUBLE.UNSIGNED ... entities) {
      return new SelectImpl.DOUBLE.UNSIGNED.SELECT<data.DOUBLE.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends data.Entity>Select.Entity._SELECT<T> DISTINCT(final T ... entities) {
      return new SelectImpl.Entity.SELECT<T>(true, entities);
    }

    @SafeVarargs
    public static <T extends Enum<?> & EntityEnum>Select.ENUM._SELECT<data.ENUM<T>> DISTINCT(final type.ENUM<? extends T> ... entities) {
      return new SelectImpl.ENUM.SELECT<data.ENUM<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.FLOAT._SELECT<data.FLOAT> DISTINCT(final type.FLOAT ... entities) {
      return new SelectImpl.FLOAT.SELECT<data.FLOAT>(true, entities);
    }

    @SafeVarargs
    public static Select.FLOAT.UNSIGNED._SELECT<data.FLOAT.UNSIGNED> DISTINCT(final type.FLOAT.UNSIGNED ... entities) {
      return new SelectImpl.FLOAT.UNSIGNED.SELECT<data.FLOAT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.INT._SELECT<data.INT> DISTINCT(final type.INT ... entities) {
      return new SelectImpl.INT.SELECT<data.INT>(true, entities);
    }

    @SafeVarargs
    public static Select.INT.UNSIGNED._SELECT<data.INT.UNSIGNED> DISTINCT(final type.INT.UNSIGNED ... entities) {
      return new SelectImpl.INT.UNSIGNED.SELECT<data.INT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends Number>Select.Numeric._SELECT<data.Numeric<T>> DISTINCT(final type.Numeric<? extends T> ... entities) {
      return new SelectImpl.Numeric.SELECT<data.Numeric<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.SMALLINT._SELECT<data.SMALLINT> DISTINCT(final type.SMALLINT ... entities) {
      return new SelectImpl.SMALLINT.SELECT<data.SMALLINT>(true, entities);
    }

    @SafeVarargs
    public static Select.SMALLINT.UNSIGNED._SELECT<data.SMALLINT.UNSIGNED> DISTINCT(final type.SMALLINT.UNSIGNED ... entities) {
      return new SelectImpl.SMALLINT.UNSIGNED.SELECT<data.SMALLINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends java.time.temporal.Temporal>Select.Temporal._SELECT<data.Temporal<T>> DISTINCT(final type.Temporal<? extends T> ... entities) {
      return new SelectImpl.Temporal.SELECT<data.Temporal<T>>(true, entities);
    }

    @SafeVarargs
    public static <T extends Comparable<?>>Select.Textual._SELECT<data.Textual<T>> DISTINCT(final type.Textual<? extends T> ... entities) {
      return new SelectImpl.Textual.SELECT<data.Textual<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.TIME._SELECT<data.TIME> DISTINCT(final type.TIME ... entities) {
      return new SelectImpl.TIME.SELECT<data.TIME>(true, entities);
    }

    @SafeVarargs
    public static Select.TINYINT._SELECT<data.TINYINT> DISTINCT(final type.TINYINT ... entities) {
      return new SelectImpl.TINYINT.SELECT<data.TINYINT>(true, entities);
    }

    @SafeVarargs
    public static Select.TINYINT.UNSIGNED._SELECT<data.TINYINT.UNSIGNED> DISTINCT(final type.TINYINT.UNSIGNED ... entities) {
      return new SelectImpl.TINYINT.UNSIGNED.SELECT<data.TINYINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.Entity._SELECT<data.Subject<?>> DISTINCT(final type.Subject<?> ... entities) {
      return new SelectImpl.Entity.SELECT<data.Subject<?>>(true, entities);
    }
  }

  /** CASE **/

  public static final class CASE {
    public static <T>Case.search.WHEN<T> WHEN(final Condition<T> condition) {
      return new CaseImpl.Search.WHEN<T>(null, condition);
    }
  }

  public static Case.simple.CASE<byte[]> CASE(final data.BINARY binary) {
    return new CaseImpl.Simple.CASE<byte[],data.BINARY>(binary);
  }

  public static Case.simple.CASE<Boolean> CASE(final data.BOOLEAN bool) {
    return new CaseImpl.Simple.CASE<Boolean,data.BOOLEAN>(bool);
  }

  public static <T extends Temporal>Case.simple.CASE<T> CASE(final data.Temporal<T> temporal) {
    return new CaseImpl.Simple.CASE<T,data.Temporal<T>>(temporal);
  }

  public static <T extends Comparable<?>>Case.simple.CASE<T> CASE(final data.Textual<T> textual) {
    return new CaseImpl.Simple.CASE<T,data.Textual<T>>(textual);
  }

  public static <T extends Number>Case.simple.CASE<T> CASE(final data.Numeric<T> numeric) {
    return new CaseImpl.Simple.CASE<T,data.Numeric<T>>(numeric);
  }

  /** DELETE **/

  public static Update._SET UPDATE(final data.Entity entity) {
    return new UpdateImpl.UPDATE(entity);
  }

  public static Update.UPDATE UPDATE(final data.Entity entity, final data.Entity ... entities) {
    return new UpdateImpl.UPDATE(Arrays.splice(entities, 0, 0, entity));
  }

  public static Update.UPDATE UPDATE(final Collection<? extends data.Entity> entities) {
    return new UpdateImpl.UPDATE(entities.toArray(new data.Entity[entities.size()]));
  }

  public static Delete._DELETE DELETE(final data.Entity entity) {
    return new DeleteImpl.DELETE(entity);
  }

  public static Delete.DELETE DELETE(final data.Entity entity, final data.Entity ... entities) {
    return new DeleteImpl.DELETE(Arrays.splice(entities, 0, 0, entity));
  }

  public static Delete.DELETE DELETE(final Collection<? extends data.Entity> entities) {
    return new DeleteImpl.DELETE(entities.toArray(new data.Entity[entities.size()]));
  }

  public static Delete.DELETE DELETE(final data.Entity entity, final List<data.Entity> entities) {
    return new DeleteImpl.DELETE(entities.toArray(new data.Entity[entities.size()]));
  }

  /** INSERT **/

  public static <E extends data.Entity>Insert._INSERT<E> INSERT(final E entity) {
    return new InsertImpl.INSERT<E>(type.Entity.class, entity);
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <E extends data.Entity>Insert.INSERT<E> INSERT(final E entity, final E ... entities) {
    return new InsertImpl.INSERT<E>(type.Entity.class, Arrays.splice(entities, 0, 0, entity));
  }

  public static <E extends data.Entity>Insert.INSERT<E> INSERT(final List<E> entities) {
    return new InsertImpl.INSERT<E>(type.Entity.class, entities.toArray(new data.Entity[entities.size()]));
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <DataType extends data.DataType<?>>Insert._INSERT<DataType> INSERT(final DataType column, final DataType ... columns) {
    return new InsertImpl.INSERT<DataType>(type.DataType.class, Arrays.splice(columns, 0, 0, column));
  }

  public static Insert.INSERT<?> INSERT(final $dmlx_data data) {
    return new InsertImpl.INSERT<data.Subject<?>>(type.Subject.class, Entities.toEntities(data));
  }

  /** String Functions **/

  public static data.CHAR CONCAT(final type.CHAR a, final type.CHAR b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final type.CHAR b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c, final CharSequence d) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static data.CHAR CONCAT(final type.CHAR a, final CharSequence b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static data.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c) {
    return new data.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  /** Start UnsignedNumber(s) **/

  public static final UNSIGNED.Float UNSIGNED(final float value) {
    return new UNSIGNED.Float(value);
  }

  public static final UNSIGNED.Double UNSIGNED(final double value) {
    return new UNSIGNED.Double(value);
  }

  public static final UNSIGNED.BigDecimal UNSIGNED(final BigDecimal value) {
    return new UNSIGNED.BigDecimal(value);
  }

  public static final UNSIGNED.Byte UNSIGNED(final short value) {
    return new UNSIGNED.Byte(value);
  }

  public static final UNSIGNED.Short UNSIGNED(final int value) {
    return new UNSIGNED.Short(value);
  }

  public static final UNSIGNED.Integer UNSIGNED(final long value) {
    return new UNSIGNED.Integer(value);
  }

  public static final UNSIGNED.Long UNSIGNED(final BigInteger value) {
    return new UNSIGNED.Long(value);
  }

  /** End UnsignedNumber(s) **/

  /** Start Math Functions (1 parameter) **/

  public static data.TINYINT SIGN(final type.Numeric<?> a) {
    return new data.TINYINT(1).wrapper(new function.Sign(a));
  }

  public static final data.FLOAT ROUND(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.DOUBLE ROUND(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Round(a, 0));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT ROUND(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT ROUND(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT ROUND(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.DOUBLE ROUND(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Round(a, 0));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.DECIMAL ROUND(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Round(a, 0));
  }

  public static final data.DECIMAL.UNSIGNED ROUND(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final data.FLOAT ROUND(final type.FLOAT a, final int scale) {
    return new data.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.FLOAT.UNSIGNED a, final int scale) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.DOUBLE ROUND(final type.DOUBLE a, final int scale) {
    return new data.DOUBLE().wrapper(new function.Round(a, scale));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.DOUBLE.UNSIGNED a, final int scale) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT ROUND(final type.TINYINT a, final int scale) {
    return new data.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.TINYINT.UNSIGNED a, final int scale) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT ROUND(final type.SMALLINT a, final int scale) {
    return new data.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT.UNSIGNED ROUND(final type.SMALLINT.UNSIGNED a, final int scale) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT ROUND(final type.INT a, final int scale) {
    return new data.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.INT.UNSIGNED a, final int scale) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.DOUBLE ROUND(final type.BIGINT a, final int scale) {
    return new data.DOUBLE().wrapper(new function.Round(a, scale));
  }

  public static final data.DOUBLE.UNSIGNED ROUND(final type.BIGINT.UNSIGNED a, final int scale) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.DECIMAL ROUND(final type.DECIMAL a, final int scale) {
    return new data.DECIMAL().wrapper(new function.Round(a, scale));
  }

  public static final data.DECIMAL.UNSIGNED ROUND(final type.DECIMAL.UNSIGNED a, final int scale) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final data.FLOAT ABS(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT.UNSIGNED ABS(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.DOUBLE ABS(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Abs(a));
  }

  public static final data.DOUBLE.UNSIGNED ABS(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT ABS(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT.UNSIGNED ABS(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT ABS(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT.UNSIGNED ABS(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.FLOAT ABS(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Abs(a));
  }

  public static final data.DOUBLE.UNSIGNED ABS(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.DOUBLE ABS(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Abs(a));
  }

  public static final data.DOUBLE.UNSIGNED ABS(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.DECIMAL ABS(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Abs(a));
  }

  public static final data.DECIMAL.UNSIGNED ABS(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final data.INT FLOOR(final type.FLOAT a) {
    return new data.INT(10).wrapper(new function.Floor(a));
  }

  public static final data.INT.UNSIGNED FLOOR(final type.FLOAT.UNSIGNED a) {
    return new data.INT.UNSIGNED(10).wrapper(new function.Floor(a));
  }

  public static final data.BIGINT FLOOR(final type.DOUBLE a) {
    return new data.BIGINT(19).wrapper(new function.Floor(a));
  }

  public static final data.BIGINT.UNSIGNED FLOOR(final type.DOUBLE.UNSIGNED a) {
    return new data.BIGINT.UNSIGNED(20).wrapper(new function.Floor(a));
  }

  public static final data.TINYINT FLOOR(final type.TINYINT a) {
    return new data.TINYINT().wrapper(new function.Floor(a));
  }

  public static final data.TINYINT.UNSIGNED FLOOR(final type.TINYINT.UNSIGNED a) {
    return new data.TINYINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final data.SMALLINT FLOOR(final type.SMALLINT a) {
    return new data.SMALLINT().wrapper(new function.Floor(a));
  }

  public static final data.SMALLINT.UNSIGNED FLOOR(final type.SMALLINT.UNSIGNED a) {
    return new data.SMALLINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final data.INT FLOOR(final type.INT a) {
    return new data.INT().wrapper(new function.Floor(a));
  }

  public static final data.INT.UNSIGNED FLOOR(final type.INT.UNSIGNED a) {
    return new data.INT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final data.BIGINT FLOOR(final type.BIGINT a) {
    return new data.BIGINT().wrapper(new function.Floor(a));
  }

  public static final data.BIGINT.UNSIGNED FLOOR(final type.BIGINT.UNSIGNED a) {
    return new data.BIGINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final data.DECIMAL FLOOR(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Floor(a));
  }

  public static final data.DECIMAL.UNSIGNED FLOOR(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final data.INT CEIL(final type.FLOAT a) {
    return new data.INT(10).wrapper(new function.Ceil(a));
  }

  public static final data.INT.UNSIGNED CEIL(final type.FLOAT.UNSIGNED a) {
    return new data.INT.UNSIGNED(10).wrapper(new function.Ceil(a));
  }

  public static final data.BIGINT CEIL(final type.DOUBLE a) {
    return new data.BIGINT(19).wrapper(new function.Ceil(a));
  }

  public static final data.BIGINT.UNSIGNED CEIL(final type.DOUBLE.UNSIGNED a) {
    return new data.BIGINT.UNSIGNED(20).wrapper(new function.Ceil(a));
  }

  public static final data.TINYINT CEIL(final type.TINYINT a) {
    return new data.TINYINT().wrapper(new function.Ceil(a));
  }

  public static final data.TINYINT.UNSIGNED CEIL(final type.TINYINT.UNSIGNED a) {
    return new data.TINYINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final data.SMALLINT CEIL(final type.SMALLINT a) {
    return new data.SMALLINT().wrapper(new function.Ceil(a));
  }

  public static final data.SMALLINT.UNSIGNED CEIL(final type.SMALLINT.UNSIGNED a) {
    return new data.SMALLINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final data.INT CEIL(final type.INT a) {
    return new data.INT().wrapper(new function.Ceil(a));
  }

  public static final data.INT.UNSIGNED CEIL(final type.INT.UNSIGNED a) {
    return new data.INT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final data.BIGINT CEIL(final type.BIGINT a) {
    return new data.BIGINT().wrapper(new function.Ceil(a));
  }

  public static final data.BIGINT.UNSIGNED CEIL(final type.BIGINT.UNSIGNED a) {
    return new data.BIGINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final data.DECIMAL CEIL(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Ceil(a));
  }

  public static final data.DECIMAL.UNSIGNED CEIL(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final data.FLOAT SQRT(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT.UNSIGNED SQRT(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.DOUBLE SQRT(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Sqrt(a));
  }

  public static final data.DOUBLE.UNSIGNED SQRT(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT SQRT(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT.UNSIGNED SQRT(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT SQRT(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT.UNSIGNED SQRT(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT SQRT(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final data.DOUBLE.UNSIGNED SQRT(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.DOUBLE SQRT(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Sqrt(a));
  }

  public static final data.DOUBLE.UNSIGNED SQRT(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.DECIMAL SQRT(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Sqrt(a));
  }

  public static final data.DECIMAL.UNSIGNED SQRT(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final data.FLOAT EXP(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT.UNSIGNED EXP(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.DOUBLE EXP(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Exp(a));
  }

  public static final data.DOUBLE.UNSIGNED EXP(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT EXP(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT.UNSIGNED EXP(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT EXP(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT.UNSIGNED EXP(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT EXP(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Exp(a));
  }

  public static final data.DOUBLE.UNSIGNED EXP(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.DOUBLE EXP(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Exp(a));
  }

  public static final data.DOUBLE.UNSIGNED EXP(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.DECIMAL EXP(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Exp(a));
  }

  public static final data.DECIMAL.UNSIGNED EXP(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final data.FLOAT LN(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT.UNSIGNED LN(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.DOUBLE LN(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Ln(a));
  }

  public static final data.DOUBLE.UNSIGNED LN(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT LN(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT.UNSIGNED LN(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT LN(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT.UNSIGNED LN(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT LN(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Ln(a));
  }

  public static final data.DOUBLE.UNSIGNED LN(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.DOUBLE LN(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Ln(a));
  }

  public static final data.DOUBLE.UNSIGNED LN(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.DECIMAL LN(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Ln(a));
  }

  public static final data.DECIMAL.UNSIGNED LN(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final data.FLOAT LOG2(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT.UNSIGNED LOG2(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.DOUBLE LOG2(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Log2(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG2(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT LOG2(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT.UNSIGNED LOG2(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT LOG2(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT.UNSIGNED LOG2(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT LOG2(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Log2(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG2(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.DOUBLE LOG2(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Log2(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG2(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.DECIMAL LOG2(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Log2(a));
  }

  public static final data.DECIMAL.UNSIGNED LOG2(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final data.FLOAT LOG10(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT.UNSIGNED LOG10(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.DOUBLE LOG10(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Log10(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG10(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT LOG10(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT.UNSIGNED LOG10(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT LOG10(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT.UNSIGNED LOG10(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT LOG10(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Log10(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG10(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.DOUBLE LOG10(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Log10(a));
  }

  public static final data.DOUBLE.UNSIGNED LOG10(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.DECIMAL LOG10(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Log10(a));
  }

  public static final data.DECIMAL.UNSIGNED LOG10(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final data.FLOAT SIN(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT.UNSIGNED SIN(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.DOUBLE SIN(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Sin(a));
  }

  public static final data.DOUBLE.UNSIGNED SIN(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT SIN(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT.UNSIGNED SIN(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT SIN(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT.UNSIGNED SIN(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT SIN(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Sin(a));
  }

  public static final data.DOUBLE.UNSIGNED SIN(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.DOUBLE SIN(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Sin(a));
  }

  public static final data.DOUBLE.UNSIGNED SIN(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.DECIMAL SIN(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Sin(a));
  }

  public static final data.DECIMAL.UNSIGNED SIN(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final data.FLOAT ASIN(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT.UNSIGNED ASIN(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.DOUBLE ASIN(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Asin(a));
  }

  public static final data.DOUBLE.UNSIGNED ASIN(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT ASIN(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT.UNSIGNED ASIN(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT ASIN(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT.UNSIGNED ASIN(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT ASIN(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Asin(a));
  }

  public static final data.DOUBLE.UNSIGNED ASIN(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.DOUBLE ASIN(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Asin(a));
  }

  public static final data.DOUBLE.UNSIGNED ASIN(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.DECIMAL ASIN(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Asin(a));
  }

  public static final data.DECIMAL.UNSIGNED ASIN(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final data.FLOAT COS(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT.UNSIGNED COS(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.DOUBLE COS(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Cos(a));
  }

  public static final data.DOUBLE.UNSIGNED COS(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT COS(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT.UNSIGNED COS(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT COS(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT.UNSIGNED COS(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT COS(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Cos(a));
  }

  public static final data.DOUBLE.UNSIGNED COS(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.DOUBLE COS(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Cos(a));
  }

  public static final data.DOUBLE.UNSIGNED COS(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.DECIMAL COS(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Cos(a));
  }

  public static final data.DECIMAL.UNSIGNED COS(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final data.FLOAT ACOS(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT.UNSIGNED ACOS(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.DOUBLE ACOS(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Acos(a));
  }

  public static final data.DOUBLE.UNSIGNED ACOS(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT ACOS(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT.UNSIGNED ACOS(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT ACOS(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT.UNSIGNED ACOS(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT ACOS(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Acos(a));
  }

  public static final data.DOUBLE.UNSIGNED ACOS(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.DOUBLE ACOS(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Acos(a));
  }

  public static final data.DOUBLE.UNSIGNED ACOS(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.DECIMAL ACOS(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Acos(a));
  }

  public static final data.DECIMAL.UNSIGNED ACOS(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final data.FLOAT TAN(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT.UNSIGNED TAN(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.DOUBLE TAN(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Tan(a));
  }

  public static final data.DOUBLE.UNSIGNED TAN(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT TAN(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT.UNSIGNED TAN(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT TAN(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT.UNSIGNED TAN(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT TAN(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Tan(a));
  }

  public static final data.DOUBLE.UNSIGNED TAN(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.DOUBLE TAN(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Tan(a));
  }

  public static final data.DOUBLE.UNSIGNED TAN(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.DECIMAL TAN(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Tan(a));
  }

  public static final data.DECIMAL.UNSIGNED TAN(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final data.FLOAT ATAN(final type.FLOAT a) {
    return new data.FLOAT().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT.UNSIGNED ATAN(final type.FLOAT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.DOUBLE ATAN(final type.DOUBLE a) {
    return new data.DOUBLE().wrapper(new function.Atan(a));
  }

  public static final data.DOUBLE.UNSIGNED ATAN(final type.DOUBLE.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT ATAN(final type.TINYINT a) {
    return new data.FLOAT().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT.UNSIGNED ATAN(final type.TINYINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT ATAN(final type.SMALLINT a) {
    return new data.FLOAT().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT.UNSIGNED ATAN(final type.SMALLINT.UNSIGNED a) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.FLOAT ATAN(final type.INT a) {
    return new data.FLOAT().wrapper(new function.Atan(a));
  }

  public static final data.DOUBLE.UNSIGNED ATAN(final type.INT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.DOUBLE ATAN(final type.BIGINT a) {
    return new data.DOUBLE().wrapper(new function.Atan(a));
  }

  public static final data.DOUBLE.UNSIGNED ATAN(final type.BIGINT.UNSIGNED a) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final data.DECIMAL ATAN(final type.DECIMAL a) {
    return new data.DECIMAL().wrapper(new function.Atan(a));
  }

  public static final data.DECIMAL.UNSIGNED ATAN(final type.DECIMAL.UNSIGNED a) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan(a));
  }

  /** End Math Functions (1 parameter) **/

  /** Start Math Functions (2 parameter) **/

  public static final data.FLOAT POW(final type.FLOAT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DOUBLE a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.TINYINT b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final int b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.FLOAT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.INT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final long b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final double b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final int b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.SMALLINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.TINYINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.INT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.FLOAT a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final type.SMALLINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.BIGINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.SMALLINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final float b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final int a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.DOUBLE a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.INT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.TINYINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final type.BIGINT a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT.UNSIGNED POW(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.DOUBLE POW(final long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.FLOAT a, final type.TINYINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT POW(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.DECIMAL POW(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DOUBLE a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.TINYINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final int b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.FLOAT a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.INT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.INT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final long b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final double b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final int b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.SMALLINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.INT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.FLOAT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.SMALLINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.BIGINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.SMALLINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final float b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final int a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.DOUBLE a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.INT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.TINYINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final type.BIGINT a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT.UNSIGNED MOD(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.DOUBLE MOD(final long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.FLOAT a, final type.TINYINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT MOD(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.DECIMAL MOD(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.INT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DOUBLE a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.TINYINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final int b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.FLOAT a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.INT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.INT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final long b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final double b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final int b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.SMALLINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.TINYINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.INT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.FLOAT a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.SMALLINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.BIGINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.SMALLINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final float b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final int a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.DOUBLE a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.INT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.TINYINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final type.BIGINT a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT.UNSIGNED LOG(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.DOUBLE LOG(final long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.FLOAT a, final type.TINYINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT LOG(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.DECIMAL LOG(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.INT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DOUBLE a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.TINYINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final int b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.FLOAT a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.INT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.INT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final long b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final double b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final int b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.SMALLINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.BIGINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.TINYINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final BigDecimal b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.INT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.FLOAT a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.SMALLINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.BIGINT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.SMALLINT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final float b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final int b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final int a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final double b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.DOUBLE a, final long b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.INT a, final long b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.TINYINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final type.BIGINT a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT.UNSIGNED ATAN2(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.DOUBLE ATAN2(final long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.FLOAT a, final type.TINYINT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final data.FLOAT ATAN2(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final data.DECIMAL ATAN2(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  /** End Math Functions (2 parameters) **/

  @SuppressWarnings("unchecked")
  public static <Temporal extends data.Temporal<T>,T extends java.time.temporal.Temporal>Temporal ADD(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new expression.Temporal(operator.ArithmeticPlusMinus.PLUS, a, interval));
  }

  @SuppressWarnings("unchecked")
  public static <Temporal extends data.Temporal<T>,T extends java.time.temporal.Temporal>Temporal SUB(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new expression.Temporal(operator.ArithmeticPlusMinus.MINUS, a, interval));
  }

  public static <Temporal extends data.Temporal<T>,T extends java.time.temporal.Temporal>Temporal PLUS(final Temporal a, final Interval interval) {
    return ADD(a, interval);
  }

  public static <Temporal extends data.Temporal<T>,T extends java.time.temporal.Temporal>Temporal MINUS(final Temporal a, final Interval interval) {
    return SUB(a, interval);
  }

  /** Start Numeric Expressions **/

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final type.TINYINT a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.SMALLINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final type.TINYINT.UNSIGNED a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DOUBLE a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final type.TINYINT a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final byte a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.TINYINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.FLOAT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT ADD(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.INT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.TINYINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final long b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final double b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.TINYINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final short a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.TINYINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.FLOAT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.TINYINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.SMALLINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DOUBLE a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DOUBLE.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DOUBLE a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.TINYINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED ADD(final UNSIGNED.Byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.SMALLINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final int a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.DOUBLE a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final short a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.SMALLINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.INT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.TINYINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.BIGINT a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT ADD(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final type.INT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DOUBLE ADD(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.BIGINT ADD(final long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.FLOAT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.FLOAT ADD(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.DECIMAL ADD(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final type.TINYINT a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.SMALLINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final type.TINYINT.UNSIGNED a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DOUBLE a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final type.TINYINT a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final byte a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.TINYINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.FLOAT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT SUB(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.INT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.TINYINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final long b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final double b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.TINYINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final short a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.TINYINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.FLOAT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.TINYINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.SMALLINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.TINYINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.TINYINT.UNSIGNED SUB(final UNSIGNED.Byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.SMALLINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final int a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.DOUBLE a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final short a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.SMALLINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.SMALLINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.INT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.TINYINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.BIGINT a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT SUB(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final type.INT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DOUBLE SUB(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.BIGINT SUB(final long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.FLOAT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.FLOAT SUB(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.DECIMAL SUB(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final type.TINYINT a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.SMALLINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final type.TINYINT.UNSIGNED a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DOUBLE a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final type.TINYINT a, final byte b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final byte a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.TINYINT a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.FLOAT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT MUL(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.INT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.TINYINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final long b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final double b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.TINYINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final short a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.TINYINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.FLOAT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.TINYINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.TINYINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.SMALLINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.TINYINT.UNSIGNED a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final short a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.TINYINT.UNSIGNED MUL(final UNSIGNED.Byte a, final type.TINYINT.UNSIGNED b) {
    return new data.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.SMALLINT a, final int b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final int a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.DOUBLE a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT a, final short b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final short a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.SMALLINT a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.SMALLINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.INT a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.INT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.TINYINT a, final type.BIGINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.BIGINT a, final type.TINYINT b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.INT MUL(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final type.INT.UNSIGNED a, final long b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DOUBLE MUL(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.BIGINT MUL(final long a, final type.INT.UNSIGNED b) {
    return new data.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.FLOAT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT MUL(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.DECIMAL MUL(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.FLOAT b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT.UNSIGNED a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.INT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.INT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.INT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DOUBLE a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.DOUBLE b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.TINYINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.BIGINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.FLOAT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.FLOAT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final long b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final long a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final double b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final double a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.TINYINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.TINYINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.SMALLINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final type.DOUBLE b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.FLOAT b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.INT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.INT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.FLOAT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.FLOAT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT.UNSIGNED a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.TINYINT.UNSIGNED b) {
    return (data.FLOAT)(a instanceof type.Numeric.UNSIGNED ? new data.FLOAT.UNSIGNED() : new data.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.TINYINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.TINYINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DOUBLE a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.DOUBLE b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.BIGINT a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.FLOAT a, final type.DECIMAL b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.FLOAT b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED && b instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (data.DECIMAL)(a instanceof type.Numeric.UNSIGNED ? new data.DECIMAL.UNSIGNED() : new data.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final type.TINYINT.UNSIGNED b) {
    return (data.DOUBLE)(a instanceof type.Numeric.UNSIGNED ? new data.DOUBLE.UNSIGNED() : new data.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT.UNSIGNED a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Short b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final float b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final int b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final float a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final int a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.DOUBLE a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.DOUBLE b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.TINYINT.UNSIGNED a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.TINYINT.UNSIGNED b) {
    return new data.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final type.BIGINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.TINYINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.TINYINT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.INT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.TINYINT a, final type.BIGINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.BIGINT a, final type.TINYINT b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT.UNSIGNED a, final type.INT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.INT a, final type.TINYINT.UNSIGNED b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final double b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final type.INT.UNSIGNED a, final long b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final double a, final type.SMALLINT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DOUBLE DIV(final long a, final type.INT.UNSIGNED b) {
    return new data.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.FLOAT a, final type.TINYINT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.FLOAT DIV(final type.TINYINT a, final type.FLOAT b) {
    return new data.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final type.SMALLINT a, final BigDecimal b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final data.DECIMAL DIV(final BigDecimal a, final type.SMALLINT b) {
    return new data.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  /** End Numeric Expressions **/

  /** Start Aggregates **/

  public static data.INT COUNT() {
    return new data.INT(10).wrapper(expression.Count.STAR);
  }

  public static data.INT COUNT(final data.DataType<?> dataType) {
    return new data.INT(10).wrapper(new expression.Count(dataType, false));
  }

  public final static class COUNT {
    public static data.INT DISTINCT(final data.DataType<?> dataType) {
      return new data.INT(10).wrapper(new expression.Count(dataType, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends data.Numeric<T>,T extends java.lang.Number>Numeric SUM(final Numeric a) {
    return (Numeric)a.clone().wrapper(new expression.Set("SUM", a, false));
  }

  public static final class SUM {
    @SuppressWarnings("unchecked")
    public static <Numeric extends data.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new expression.Set("SUM", a, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends data.Numeric<T>,T extends java.lang.Number>Numeric AVG(final Numeric a) {
    return (Numeric)a.clone().wrapper(new expression.Set("AVG", a, false));
  }

  public static final class AVG {
    @SuppressWarnings("unchecked")
    public static <Numeric extends data.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new expression.Set("AVG", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends data.DataType<T>,T>DataType MAX(final DataType a) {
    return (DataType)a.clone().wrapper(new expression.Set("MAX", a, false));
  }

  public static final class MAX {
    @SuppressWarnings("unchecked")
    public static <DataType extends data.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new expression.Set("MAX", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends data.DataType<T>,T>DataType MIN(final DataType a) {
    return (DataType)a.clone().wrapper(new expression.Set("MIN", a, false));
  }

  public static final class MIN {
    @SuppressWarnings("unchecked")
    public static <DataType extends data.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new expression.Set("MIN", a, true));
    }
  }

  /** End Aggregates **/

  private static class NOW extends data.DATETIME {
    protected NOW() {
      super(10);
      this.wrapper(new function.Temporal("NOW") {
        @Override
        protected Object evaluate(final Set<Evaluable> visited) {
          return LocalDateTime.now();
        }
      });
    }

    @Override
    public final NOW clone() {
      return new NOW();
    }
  }

  private static final data.DATETIME NOW = new NOW();

  public static data.DATETIME NOW() {
    return NOW;
  }

  private static class PI extends data.DOUBLE {
    protected PI() {
      super();
      this.wrapper(new function.Pi());
    }

    @Override
    public final PI clone() {
      return new PI();
    }
  }

  private static final data.DOUBLE PI = new PI();

  public static data.DOUBLE PI() {
    return PI;
  }

  /** Condition **/

  @SafeVarargs
  public static data.BOOLEAN AND(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(operator.Boolean.AND, a, b, conditions);
  }

  public static data.BOOLEAN AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(operator.Boolean.AND, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static data.BOOLEAN AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(operator.Boolean.AND, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static data.BOOLEAN AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(operator.Boolean.AND, array[0], array[1], Arrays.subArray(array, 2));
  }

  @SafeVarargs
  public static data.BOOLEAN OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(operator.Boolean.OR, a, b, conditions);
  }

  public static data.BOOLEAN OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(operator.Boolean.OR, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static data.BOOLEAN OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(operator.Boolean.OR, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static data.BOOLEAN OR(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(operator.Boolean.OR, array[0], array[1], Arrays.subArray(array, 2));
  }

  /** Predicate **/

  protected static final class ALL<T> extends QuantifiedComparisonPredicate<T> {
    protected ALL(final Select.untyped.SELECT<?> subQuery) {
      super("ALL", subQuery);
    }
  }

  protected static final class ANY<T> extends QuantifiedComparisonPredicate<T> {
    protected ANY(final Select.untyped.SELECT<?> subQuery) {
      super("ANY", subQuery);
    }
  }

  protected static final class SOME<T> extends QuantifiedComparisonPredicate<T> {
    protected SOME(final Select.untyped.SELECT<?> subQuery) {
      super("SOME", subQuery);
    }
  }

  public static <T>ALL<T> ALL(final Select.untyped.SELECT<? extends data.Subject<T>> subQuery) {
    return new ALL<T>(subQuery);
  }

  public static <T>ANY<T> ANY(final Select.untyped.SELECT<? extends data.Subject<T>> subQuery) {
    return new ANY<T>(subQuery);
  }

  public static <T>SOME<T> SOME(final Select.untyped.SELECT<? extends data.Subject<T>> subQuery) {
    return new SOME<T>(subQuery);
  }

  public static Predicate BETWEEN(final type.Numeric<?> dataType, final type.Numeric<?> a, final type.Numeric<?> b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final type.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.TIME dataType, final type.TIME a, final type.TIME b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.Numeric<?> dataType, final type.Numeric<?> a, final Number b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.TIME dataType, final type.TIME a, final LocalTime b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, a, (data.TIME)data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Numeric<?> dataType, final Number a, final type.Numeric<?> b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, data.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final type.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final type.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final type.DATETIME b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final type.DATETIME b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final type.DATETIME b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final type.DATE b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final type.DATE b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final type.DATETIME b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final type.DATE b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final type.DATE b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final type.TIME dataType, final LocalTime a, final type.TIME b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, (data.TIME)data.DataType.wrap(a), b, true);
  }

  public static <T extends Number>Predicate BETWEEN(final type.Numeric<?> dataType, final T a, final T b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, (data.Numeric<? extends T>)data.DataType.wrap(a), (data.Numeric<? extends T>)data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final type.TIME dataType, final LocalTime a, final LocalTime b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, (data.TIME)data.DataType.wrap(a), (data.TIME)data.DataType.wrap(b), true);
  }

  public static Predicate LIKE(final type.CHAR a, final CharSequence b) {
    return new LikePredicate(a, true, b);
  }

  public static <T>Predicate IN(final type.DataType<T> a, final Collection<T> b) {
    return new InPredicate(a, true, b);
  }

  @SafeVarargs
  public static <T>Predicate IN(final type.DataType<T> a, final T ... b) {
    return new InPredicate(a, true, b);
  }

  public static <T>Predicate IN(final type.DataType<T> a, final Select.untyped.SELECT<? extends data.DataType<T>> b) {
    return new InPredicate(a, true, b);
  }

  public static Predicate EXISTS(final Select.untyped.SELECT<?> subQuery) {
    return new ExistsPredicate(subQuery);
  }

  public static final class NOT {
    public static Predicate BETWEEN(final type.Numeric<?> dataType, final type.Numeric<?> a, final type.Numeric<?> b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final type.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.TIME dataType, final type.TIME a, final type.TIME b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final type.Numeric<?> dataType, final type.Numeric<?> a, final Number b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final type.Textual<?> a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATETIME a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final type.DATE a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATETIME a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final type.DATE a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.TIME dataType, final type.TIME a, final LocalTime b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, a, (data.TIME)data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Numeric<?> dataType, final Number a, final type.Numeric<?> b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final type.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final type.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final type.DATETIME b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final type.DATE b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.TIME dataType, final LocalTime a, final type.TIME b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, (data.TIME)data.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final type.Numeric<?> dataType, final Number a, final Number b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final String a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.Textual<?> dataType, final Enum<?> a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDateTime a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATETIME dataType, final LocalDate a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDateTime a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.DATE dataType, final LocalDate a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, data.DataType.wrap(a), data.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final type.TIME dataType, final LocalTime a, final LocalTime b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, (data.TIME)data.DataType.wrap(a), (data.TIME)data.DataType.wrap(b), false);
    }

    public static Predicate LIKE(final type.CHAR a, final String b) {
      return new LikePredicate(a, false, b);
    }

    public static <T>Predicate IN(final type.DataType<T> a, final Collection<T> b) {
      return new InPredicate(a, false, b);
    }

    @SafeVarargs
    public static <T>Predicate IN(final type.DataType<T> a, final T ... b) {
      return new InPredicate(a, false, b);
    }

    public static <T>Predicate IN(final type.DataType<T> a, final Select.untyped.SELECT<? extends data.DataType<T>> b) {
      return new InPredicate(a, false, b);
    }
  }

  public static final class IS {
    public static final class NOT {
      public static Predicate NULL(final type.DataType<?> dataType) {
        return new NullPredicate(dataType, false);
      }
    }

    public static Predicate NULL(final type.DataType<?> dataType) {
      return new NullPredicate(dataType, true);
    }
  }

  private DML() {
  }
}