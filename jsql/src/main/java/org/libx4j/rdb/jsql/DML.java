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

import java.io.InputStream;
import java.io.Reader;
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
import org.libx4j.rdb.jsql.Select;

@SuppressWarnings("hiding")
public final class DML {

  /** Ordering Specification **/

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V ASC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(operator.Ordering.ASC, dataType));
  }

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V DESC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(operator.Ordering.DESC, dataType));
  }

  /** START Cast **/

  public static Cast.BIGINT CAST(final kind.BIGINT<Long> a) {
    return new Cast.BIGINT(a);
  }

  public static Cast.BINARY CAST(final kind.BINARY<byte[]> a) {
    return new Cast.BINARY(a);
  }

  public static Cast.BLOB CAST(final kind.BLOB<InputStream> a) {
    return new Cast.BLOB(a);
  }

  public static Cast.CHAR CAST(final kind.Textual<?> a) {
    return new Cast.CHAR(a);
  }

  public static Cast.BOOLEAN CAST(final kind.BOOLEAN<Boolean> a) {
    return new Cast.BOOLEAN(a);
  }

  public static Cast.CLOB CAST(final kind.CLOB<Reader> a) {
    return new Cast.CLOB(a);
  }

  public static Cast.DATE CAST(final kind.DATE<LocalDate> a) {
    return new Cast.DATE(a);
  }

  public static Cast.DATETIME CAST(final kind.DATETIME<LocalDateTime> a) {
    return new Cast.DATETIME(a);
  }

  public static Cast.DECIMAL CAST(final kind.DECIMAL<BigDecimal> a) {
    return new Cast.DECIMAL(a);
  }

  public static Cast.DOUBLE CAST(final kind.DOUBLE<Double> a) {
    return new Cast.DOUBLE(a);
  }

  public static Cast.FLOAT CAST(final kind.FLOAT<Float> a) {
    return new Cast.FLOAT(a);
  }

  public static Cast.INT CAST(final kind.INT<Integer> a) {
    return new Cast.INT(a);
  }

  public static Cast.SMALLINT CAST(final kind.SMALLINT<Short> a) {
    return new Cast.SMALLINT(a);
  }

  public static Cast.TINYINT CAST(final kind.TINYINT<Byte> a) {
    return new Cast.TINYINT(a);
  }

  public static Cast.TIME CAST(final kind.TIME<LocalTime> a) {
    return new Cast.TIME(a);
  }

  /** END Cast **/

  /** START ComparisonPredicate **/

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN EQ(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.NE, a, b);
  }

  public static type.BOOLEAN NE(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static type.BOOLEAN NE(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static type.BOOLEAN NE(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN NE(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.NE, a, b);
  }

  public static type.BOOLEAN NE(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.NE, a, b);
  }

  public static type.BOOLEAN NE(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LT, a, b);
  }

  public static type.BOOLEAN LT(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static type.BOOLEAN LT(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static type.BOOLEAN LT(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LT(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LT, a, b);
  }

  public static type.BOOLEAN LT(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LT, a, b);
  }

  public static type.BOOLEAN LT(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GT, a, b);
  }

  public static type.BOOLEAN GT(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static type.BOOLEAN GT(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static type.BOOLEAN GT(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GT(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GT, a, b);
  }

  public static type.BOOLEAN GT(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GT, a, b);
  }

  public static type.BOOLEAN GT(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN LTE(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final kind.Numeric<?> a, final Number b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final Number a, final kind.Numeric<?> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final kind.Numeric<?> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final kind.Temporal<Temporal> a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final kind.Temporal<Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final Temporal a, final kind.Temporal<Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final kind.Temporal<Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(operator.Logical.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final kind.CHAR<String> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final String a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.CHAR<String> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final E a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.ENUM<E> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.ENUM<E> a, final E b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final E a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<E>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.ENUM<E> a, final kind.CHAR<String> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.CHAR<String> a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final kind.ENUM<E> a, final String b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <E extends Enum<?> & EntityEnum>type.BOOLEAN GTE(final String a, final kind.ENUM<E> b) {
    return new ComparisonPredicate<String>(operator.Logical.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final kind.Textual<Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final kind.BOOLEAN<Boolean> a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Textual>(operator.Logical.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final kind.BOOLEAN<Boolean> a, final boolean b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final boolean a, final kind.BOOLEAN<Boolean> b) {
    return new ComparisonPredicate<Boolean>(operator.Logical.GTE, a, b);
  }

  /** END ComparisonPredicate **/

  /** SELECT **/

  @SafeVarargs
  public static <T>Select.ARRAY._SELECT<type.ARRAY<T>> SELECT(final kind.ARRAY<? extends T> ... entities) {
    return new SelectImpl.ARRAY.SELECT<type.ARRAY<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.BIGINT._SELECT<type.BIGINT> SELECT(final kind.BIGINT<Long> ... entities) {
    return new SelectImpl.BIGINT.SELECT<type.BIGINT>(false, entities);
  }

  @SafeVarargs
  public static Select.BIGINT.UNSIGNED._SELECT<type.BIGINT.UNSIGNED> SELECT(final kind.BIGINT.UNSIGNED<BigInteger> ... entities) {
    return new SelectImpl.BIGINT.UNSIGNED.SELECT<type.BIGINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.BINARY._SELECT<type.BINARY> SELECT(final kind.BINARY<byte[]> ... entities) {
    return new SelectImpl.BINARY.SELECT<type.BINARY>(false, entities);
  }

  @SafeVarargs
  public static Select.BLOB._SELECT<type.BLOB> SELECT(final kind.BLOB<InputStream> ... entities) {
    return new SelectImpl.BLOB.SELECT<type.BLOB>(false, entities);
  }

  @SafeVarargs
  public static Select.BOOLEAN._SELECT<type.BOOLEAN> SELECT(final kind.BOOLEAN<Boolean> ... entities) {
    return new SelectImpl.BOOLEAN.SELECT<type.BOOLEAN>(false, entities);
  }

  @SafeVarargs
  public static Select.CHAR._SELECT<type.CHAR> SELECT(final kind.CHAR<String> ... entities) {
    return new SelectImpl.CHAR.SELECT<type.CHAR>(false, entities);
  }

  @SafeVarargs
  public static Select.CLOB._SELECT<type.CLOB> SELECT(final kind.CLOB<Reader> ... entities) {
    return new SelectImpl.CLOB.SELECT<type.CLOB>(false, entities);
  }

  @SafeVarargs
  public static <T>Select.DataType._SELECT<type.DataType<T>> SELECT(final kind.DataType<? extends T> ... entities) {
    return new SelectImpl.DataType.SELECT<type.DataType<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.DATE._SELECT<type.DATE> SELECT(final kind.DATE<LocalDate> ... entities) {
    return new SelectImpl.DATE.SELECT<type.DATE>(false, entities);
  }

  @SafeVarargs
  public static Select.DATETIME._SELECT<type.DATETIME> SELECT(final kind.DATETIME<LocalDateTime> ... entities) {
    return new SelectImpl.DATETIME.SELECT<type.DATETIME>(false, entities);
  }

  @SafeVarargs
  public static Select.DECIMAL._SELECT<type.DECIMAL> SELECT(final kind.DECIMAL<BigDecimal> ... entities) {
    return new SelectImpl.DECIMAL.SELECT<type.DECIMAL>(false, entities);
  }

  @SafeVarargs
  public static Select.DECIMAL.UNSIGNED._SELECT<type.DECIMAL.UNSIGNED> SELECT(final kind.DECIMAL.UNSIGNED<BigDecimal> ... entities) {
    return new SelectImpl.DECIMAL.UNSIGNED.SELECT<type.DECIMAL.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.DOUBLE._SELECT<type.DOUBLE> SELECT(final kind.DOUBLE<Double> ... entities) {
    return new SelectImpl.DOUBLE.SELECT<type.DOUBLE>(false, entities);
  }

  @SafeVarargs
  public static Select.DOUBLE.UNSIGNED._SELECT<type.DOUBLE.UNSIGNED> SELECT(final kind.DOUBLE.UNSIGNED<Double> ... entities) {
    return new SelectImpl.DOUBLE.UNSIGNED.SELECT<type.DOUBLE.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends type.Entity>Select.Entity._SELECT<T> SELECT(final T ... entities) {
    return new SelectImpl.Entity.SELECT<T>(false, entities);
  }

  @SafeVarargs
  public static <T extends Enum<?> & EntityEnum>Select.ENUM._SELECT<type.ENUM<T>> SELECT(final kind.ENUM<? extends T> ... entities) {
    return new SelectImpl.ENUM.SELECT<type.ENUM<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.FLOAT._SELECT<type.FLOAT> SELECT(final kind.FLOAT<Float> ... entities) {
    return new SelectImpl.FLOAT.SELECT<type.FLOAT>(false, entities);
  }

  @SafeVarargs
  public static Select.FLOAT.UNSIGNED._SELECT<type.FLOAT.UNSIGNED> SELECT(final kind.FLOAT.UNSIGNED<Float> ... entities) {
    return new SelectImpl.FLOAT.UNSIGNED.SELECT<type.FLOAT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.INT._SELECT<type.INT> SELECT(final kind.INT<Integer> ... entities) {
    return new SelectImpl.INT.SELECT<type.INT>(false, entities);
  }

  @SafeVarargs
  public static Select.INT.UNSIGNED._SELECT<type.INT.UNSIGNED> SELECT(final kind.INT.UNSIGNED<Long> ... entities) {
    return new SelectImpl.INT.UNSIGNED.SELECT<type.INT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends Number>Select.LargeObject._SELECT<type.LargeObject<T>> SELECT(final kind.LargeObject<? extends T> ... entities) {
    return new SelectImpl.LargeObject.SELECT<type.LargeObject<T>>(false, entities);
  }

  @SafeVarargs
  public static <T extends Number>Select.Numeric._SELECT<type.Numeric<T>> SELECT(final kind.Numeric<? extends T> ... entities) {
    return new SelectImpl.Numeric.SELECT<type.Numeric<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.SMALLINT._SELECT<type.SMALLINT> SELECT(final kind.SMALLINT<Short> ... entities) {
    return new SelectImpl.SMALLINT.SELECT<type.SMALLINT>(false, entities);
  }

  @SafeVarargs
  public static Select.SMALLINT.UNSIGNED._SELECT<type.SMALLINT.UNSIGNED> SELECT(final kind.SMALLINT.UNSIGNED<Integer> ... entities) {
    return new SelectImpl.SMALLINT.UNSIGNED.SELECT<type.SMALLINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static <T extends java.time.temporal.Temporal>Select.Temporal._SELECT<type.Temporal<T>> SELECT(final kind.Temporal<? extends T> ... entities) {
    return new SelectImpl.Temporal.SELECT<type.Temporal<T>>(false, entities);
  }

  @SafeVarargs
  public static <T>Select.Textual._SELECT<type.Textual<T>> SELECT(final kind.Textual<? extends T> ... entities) {
    return new SelectImpl.Textual.SELECT<type.Textual<T>>(false, entities);
  }

  @SafeVarargs
  public static Select.TIME._SELECT<type.TIME> SELECT(final kind.TIME<LocalTime> ... entities) {
    return new SelectImpl.TIME.SELECT<type.TIME>(false, entities);
  }

  @SafeVarargs
  public static Select.TINYINT._SELECT<type.TINYINT> SELECT(final kind.TINYINT<Byte> ... entities) {
    return new SelectImpl.TINYINT.SELECT<type.TINYINT>(false, entities);
  }

  @SafeVarargs
  public static Select.TINYINT.UNSIGNED._SELECT<type.TINYINT.UNSIGNED> SELECT(final kind.TINYINT.UNSIGNED<Short> ... entities) {
    return new SelectImpl.TINYINT.UNSIGNED.SELECT<type.TINYINT.UNSIGNED>(false, entities);
  }

  @SafeVarargs
  public static Select.Entity._SELECT<Subject<?>> SELECT(final kind.Subject<?> ... entities) {
    return new SelectImpl.Entity.SELECT<Subject<?>>(false, entities);
  }

  public static final class SELECT {
    @SafeVarargs
    public static <T>Select.ARRAY._SELECT<type.ARRAY<T>> DISTINCT(final kind.ARRAY<? extends T> ... entities) {
      return new SelectImpl.ARRAY.SELECT<type.ARRAY<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.BIGINT._SELECT<type.BIGINT> DISTINCT(final kind.BIGINT<Long> ... entities) {
      return new SelectImpl.BIGINT.SELECT<type.BIGINT>(true, entities);
    }

    @SafeVarargs
    public static Select.BIGINT.UNSIGNED._SELECT<type.BIGINT.UNSIGNED> DISTINCT(final kind.BIGINT.UNSIGNED<BigInteger> ... entities) {
      return new SelectImpl.BIGINT.UNSIGNED.SELECT<type.BIGINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.BINARY._SELECT<type.BINARY> DISTINCT(final kind.BINARY<byte[]> ... entities) {
      return new SelectImpl.BINARY.SELECT<type.BINARY>(true, entities);
    }

    @SafeVarargs
    public static Select.BLOB._SELECT<type.BLOB> DISTINCT(final kind.BLOB<InputStream> ... entities) {
      return new SelectImpl.BLOB.SELECT<type.BLOB>(true, entities);
    }

    @SafeVarargs
    public static Select.BOOLEAN._SELECT<type.BOOLEAN> DISTINCT(final kind.BOOLEAN<Boolean> ... entities) {
      return new SelectImpl.BOOLEAN.SELECT<type.BOOLEAN>(true, entities);
    }

    @SafeVarargs
    public static Select.CHAR._SELECT<type.CHAR> DISTINCT(final kind.CHAR<String> ... entities) {
      return new SelectImpl.CHAR.SELECT<type.CHAR>(true, entities);
    }

    @SafeVarargs
    public static Select.CLOB._SELECT<type.CLOB> DISTINCT(final kind.CLOB<Reader> ... entities) {
      return new SelectImpl.CLOB.SELECT<type.CLOB>(true, entities);
    }

    @SafeVarargs
    public static <T>Select.DataType._SELECT<type.DataType<T>> DISTINCT(final kind.DataType<? extends T> ... entities) {
      return new SelectImpl.DataType.SELECT<type.DataType<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.DATE._SELECT<type.DATE> DISTINCT(final kind.DATE<LocalDate> ... entities) {
      return new SelectImpl.DATE.SELECT<type.DATE>(true, entities);
    }

    @SafeVarargs
    public static Select.DATETIME._SELECT<type.DATETIME> DISTINCT(final kind.DATETIME<LocalDateTime> ... entities) {
      return new SelectImpl.DATETIME.SELECT<type.DATETIME>(true, entities);
    }

    @SafeVarargs
    public static Select.DECIMAL._SELECT<type.DECIMAL> DISTINCT(final kind.DECIMAL<BigDecimal> ... entities) {
      return new SelectImpl.DECIMAL.SELECT<type.DECIMAL>(true, entities);
    }

    @SafeVarargs
    public static Select.DECIMAL.UNSIGNED._SELECT<type.DECIMAL.UNSIGNED> DISTINCT(final kind.DECIMAL.UNSIGNED<BigDecimal> ... entities) {
      return new SelectImpl.DECIMAL.UNSIGNED.SELECT<type.DECIMAL.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.DOUBLE._SELECT<type.DOUBLE> DISTINCT(final kind.DOUBLE<Double> ... entities) {
      return new SelectImpl.DOUBLE.SELECT<type.DOUBLE>(true, entities);
    }

    @SafeVarargs
    public static Select.DOUBLE.UNSIGNED._SELECT<type.DOUBLE.UNSIGNED> DISTINCT(final kind.DOUBLE.UNSIGNED<Double> ... entities) {
      return new SelectImpl.DOUBLE.UNSIGNED.SELECT<type.DOUBLE.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends type.Entity>Select.Entity._SELECT<T> DISTINCT(final T ... entities) {
      return new SelectImpl.Entity.SELECT<T>(true, entities);
    }

    @SafeVarargs
    public static <T extends Enum<?> & EntityEnum>Select.ENUM._SELECT<type.ENUM<T>> DISTINCT(final kind.ENUM<? extends T> ... entities) {
      return new SelectImpl.ENUM.SELECT<type.ENUM<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.FLOAT._SELECT<type.FLOAT> DISTINCT(final kind.FLOAT<Float> ... entities) {
      return new SelectImpl.FLOAT.SELECT<type.FLOAT>(true, entities);
    }

    @SafeVarargs
    public static Select.FLOAT.UNSIGNED._SELECT<type.FLOAT.UNSIGNED> DISTINCT(final kind.FLOAT.UNSIGNED<Float> ... entities) {
      return new SelectImpl.FLOAT.UNSIGNED.SELECT<type.FLOAT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.INT._SELECT<type.INT> DISTINCT(final kind.INT<Integer> ... entities) {
      return new SelectImpl.INT.SELECT<type.INT>(true, entities);
    }

    @SafeVarargs
    public static Select.INT.UNSIGNED._SELECT<type.INT.UNSIGNED> DISTINCT(final kind.INT.UNSIGNED<Long> ... entities) {
      return new SelectImpl.INT.UNSIGNED.SELECT<type.INT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends Number>Select.LargeObject._SELECT<type.LargeObject<T>> DISTINCT(final kind.LargeObject<? extends T> ... entities) {
      return new SelectImpl.LargeObject.SELECT<type.LargeObject<T>>(true, entities);
    }

    @SafeVarargs
    public static <T extends Number>Select.Numeric._SELECT<type.Numeric<T>> DISTINCT(final kind.Numeric<? extends T> ... entities) {
      return new SelectImpl.Numeric.SELECT<type.Numeric<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.SMALLINT._SELECT<type.SMALLINT> DISTINCT(final kind.SMALLINT<Short> ... entities) {
      return new SelectImpl.SMALLINT.SELECT<type.SMALLINT>(true, entities);
    }

    @SafeVarargs
    public static Select.SMALLINT.UNSIGNED._SELECT<type.SMALLINT.UNSIGNED> DISTINCT(final kind.SMALLINT.UNSIGNED<Integer> ... entities) {
      return new SelectImpl.SMALLINT.UNSIGNED.SELECT<type.SMALLINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static <T extends java.time.temporal.Temporal>Select.Temporal._SELECT<type.Temporal<T>> DISTINCT(final kind.Temporal<? extends T> ... entities) {
      return new SelectImpl.Temporal.SELECT<type.Temporal<T>>(true, entities);
    }

    @SafeVarargs
    public static <T>Select.Textual._SELECT<type.Textual<T>> DISTINCT(final kind.Textual<? extends T> ... entities) {
      return new SelectImpl.Textual.SELECT<type.Textual<T>>(true, entities);
    }

    @SafeVarargs
    public static Select.TIME._SELECT<type.TIME> DISTINCT(final kind.TIME<LocalTime> ... entities) {
      return new SelectImpl.TIME.SELECT<type.TIME>(true, entities);
    }

    @SafeVarargs
    public static Select.TINYINT._SELECT<type.TINYINT> DISTINCT(final kind.TINYINT<Byte> ... entities) {
      return new SelectImpl.TINYINT.SELECT<type.TINYINT>(true, entities);
    }

    @SafeVarargs
    public static Select.TINYINT.UNSIGNED._SELECT<type.TINYINT.UNSIGNED> DISTINCT(final kind.TINYINT.UNSIGNED<Short> ... entities) {
      return new SelectImpl.TINYINT.UNSIGNED.SELECT<type.TINYINT.UNSIGNED>(true, entities);
    }

    @SafeVarargs
    public static Select.Entity._SELECT<Subject<?>> DISTINCT(final kind.Subject<?> ... entities) {
      return new SelectImpl.Entity.SELECT<Subject<?>>(true, entities);
    }
  }

  /** CASE **/

  public static final class CASE {
    public static <T>Case.search.WHEN<T> WHEN(final Condition<T> condition) {
      return new CaseImpl.Search.WHEN<T>(null, condition);
    }
  }

  public static Case.simple.CASE<byte[]> CASE(final type.BINARY binary) {
    return new CaseImpl.Simple.CASE<byte[],type.BINARY>(binary);
  }

  public static Case.simple.CASE<Boolean> CASE(final type.BOOLEAN bool) {
    return new CaseImpl.Simple.CASE<Boolean,type.BOOLEAN>(bool);
  }

  public static <T extends Temporal>Case.simple.CASE<T> CASE(final type.Temporal<T> temporal) {
    return new CaseImpl.Simple.CASE<T,type.Temporal<T>>(temporal);
  }

  public static <T>Case.simple.CASE<T> CASE(final type.Textual<T> textual) {
    return new CaseImpl.Simple.CASE<T,type.Textual<T>>(textual);
  }

  public static <T extends Number>Case.simple.CASE<T> CASE(final type.Numeric<T> numeric) {
    return new CaseImpl.Simple.CASE<T,type.Numeric<T>>(numeric);
  }

  /** DELETE **/

  public static Update._SET UPDATE(final type.Entity entity) {
    return new UpdateImpl.UPDATE(entity);
  }

  public static Update.UPDATE UPDATE(final type.Entity entity, final type.Entity ... entities) {
    return new UpdateImpl.UPDATE(Arrays.splice(entities, 0, 0, entity));
  }

  public static Update.UPDATE UPDATE(final Collection<? extends type.Entity> entities) {
    return new UpdateImpl.UPDATE(entities.toArray(new type.Entity[entities.size()]));
  }

  public static Delete._DELETE DELETE(final type.Entity entity) {
    return new DeleteImpl.DELETE(entity);
  }

  public static Delete.DELETE DELETE(final type.Entity entity, final type.Entity ... entities) {
    return new DeleteImpl.DELETE(Arrays.splice(entities, 0, 0, entity));
  }

  public static Delete.DELETE DELETE(final Collection<? extends type.Entity> entities) {
    return new DeleteImpl.DELETE(entities.toArray(new type.Entity[entities.size()]));
  }

  public static Delete.DELETE DELETE(final type.Entity entity, final List<type.Entity> entities) {
    return new DeleteImpl.DELETE(entities.toArray(new type.Entity[entities.size()]));
  }

  /** INSERT **/

  public static <E extends type.Entity>Insert._INSERT<E> INSERT(final E entity) {
    return new InsertImpl.INSERT<E>(kind.Entity.class, entity);
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <E extends type.Entity>Insert.INSERT<E> INSERT(final E entity, final E ... entities) {
    return new InsertImpl.INSERT<E>(kind.Entity.class, Arrays.splice(entities, 0, 0, entity));
  }

  public static <E extends type.Entity>Insert.INSERT<E> INSERT(final List<E> entities) {
    return new InsertImpl.INSERT<E>(kind.Entity.class, entities.toArray(new type.Entity[entities.size()]));
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<?>>Insert._INSERT<DataType> INSERT(final DataType column, final DataType ... columns) {
    return new InsertImpl.INSERT<DataType>(kind.DataType.class, Arrays.splice(columns, 0, 0, column));
  }

  public static Insert.INSERT<?> INSERT(final $dmlx_data data) {
    return new InsertImpl.INSERT<Subject<?>>(kind.Subject.class, Entities.toEntities(data));
  }

  /** String Functions **/

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final kind.CHAR<String> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final kind.CHAR<String> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final CharSequence b, final kind.CHAR<String> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final kind.CHAR<String> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final CharSequence c, final kind.CHAR<String> d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final CharSequence b, final kind.CHAR<String> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final kind.CHAR<String> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final CharSequence c, final kind.CHAR<String> d, final CharSequence e) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final kind.ENUM<?> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final kind.ENUM<?> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final CharSequence b, final kind.ENUM<?> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final kind.ENUM<?> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final CharSequence c, final kind.ENUM<?> d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final CharSequence b, final kind.ENUM<?> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final kind.ENUM<?> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final CharSequence c, final kind.ENUM<?> d, final CharSequence e) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final kind.ENUM<?> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final kind.ENUM<?> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final CharSequence b, final kind.ENUM<?> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final CharSequence c, final kind.ENUM<?> d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final kind.ENUM<?> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final kind.ENUM<?> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final CharSequence b, final kind.ENUM<?> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final CharSequence c, final kind.ENUM<?> d, final CharSequence e) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final kind.CHAR<String> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final kind.CHAR<String> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final CharSequence b, final kind.CHAR<String> c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final kind.CHAR<String> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final CharSequence c, final kind.CHAR<String> d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final CharSequence b, final kind.CHAR<String> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final kind.CHAR<String> c, final CharSequence d) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final CharSequence c, final kind.CHAR<String> d, final CharSequence e) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final kind.CHAR<String> a, final CharSequence b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.CHAR<String> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final kind.ENUM<?> a, final CharSequence b) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final kind.ENUM<?> b, final CharSequence c) {
    return new type.CHAR().wrapper(new expression.String(operator.String.CONCAT, a, b, c));
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

  public static type.TINYINT SIGN(final kind.Numeric<?> a) {
    return new type.TINYINT(1).wrapper(new function.Sign(a));
  }

  public static final type.FLOAT ROUND(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.DOUBLE ROUND(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.DOUBLE ROUND(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.DECIMAL ROUND(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Round(a, 0));
  }

  public static final type.DECIMAL.UNSIGNED ROUND(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final kind.FLOAT<Float> a, final int scale) {
    return new type.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.FLOAT.UNSIGNED<Float> a, final int scale) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.DOUBLE ROUND(final kind.DOUBLE<Double> a, final int scale) {
    return new type.DOUBLE().wrapper(new function.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.DOUBLE.UNSIGNED<Double> a, final int scale) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final kind.TINYINT<Byte> a, final int scale) {
    return new type.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.TINYINT.UNSIGNED<Short> a, final int scale) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final kind.SMALLINT<Short> a, final int scale) {
    return new type.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final kind.SMALLINT.UNSIGNED<Integer> a, final int scale) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final kind.INT<Integer> a, final int scale) {
    return new type.FLOAT().wrapper(new function.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.INT.UNSIGNED<Long> a, final int scale) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.DOUBLE ROUND(final kind.BIGINT<Long> a, final int scale) {
    return new type.DOUBLE().wrapper(new function.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final kind.BIGINT.UNSIGNED<BigInteger> a, final int scale) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.DECIMAL ROUND(final kind.DECIMAL<BigDecimal> a, final int scale) {
    return new type.DECIMAL().wrapper(new function.Round(a, scale));
  }

  public static final type.DECIMAL.UNSIGNED ROUND(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final int scale) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Round(a, scale));
  }

  public static final type.FLOAT ABS(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.DOUBLE ABS(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT ABS(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT ABS(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.FLOAT ABS(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.DOUBLE ABS(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.DECIMAL ABS(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Abs(a));
  }

  public static final type.DECIMAL.UNSIGNED ABS(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Abs(a));
  }

  public static final type.INT FLOOR(final kind.FLOAT<Float> a) {
    return new type.INT(10).wrapper(new function.Floor(a));
  }

  public static final type.INT.UNSIGNED FLOOR(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.INT.UNSIGNED(10).wrapper(new function.Floor(a));
  }

  public static final type.BIGINT FLOOR(final kind.DOUBLE<Double> a) {
    return new type.BIGINT(19).wrapper(new function.Floor(a));
  }

  public static final type.BIGINT.UNSIGNED FLOOR(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.BIGINT.UNSIGNED(20).wrapper(new function.Floor(a));
  }

  public static final type.TINYINT FLOOR(final kind.TINYINT<Byte> a) {
    return new type.TINYINT().wrapper(new function.Floor(a));
  }

  public static final type.TINYINT.UNSIGNED FLOOR(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.TINYINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final type.SMALLINT FLOOR(final kind.SMALLINT<Short> a) {
    return new type.SMALLINT().wrapper(new function.Floor(a));
  }

  public static final type.SMALLINT.UNSIGNED FLOOR(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.SMALLINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final type.INT FLOOR(final kind.INT<Integer> a) {
    return new type.INT().wrapper(new function.Floor(a));
  }

  public static final type.INT.UNSIGNED FLOOR(final kind.INT.UNSIGNED<Long> a) {
    return new type.INT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final type.BIGINT FLOOR(final kind.BIGINT<Long> a) {
    return new type.BIGINT().wrapper(new function.Floor(a));
  }

  public static final type.BIGINT.UNSIGNED FLOOR(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.BIGINT.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final type.DECIMAL FLOOR(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Floor(a));
  }

  public static final type.DECIMAL.UNSIGNED FLOOR(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Floor(a));
  }

  public static final type.INT CEIL(final kind.FLOAT<Float> a) {
    return new type.INT(10).wrapper(new function.Ceil(a));
  }

  public static final type.INT.UNSIGNED CEIL(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.INT.UNSIGNED(10).wrapper(new function.Ceil(a));
  }

  public static final type.BIGINT CEIL(final kind.DOUBLE<Double> a) {
    return new type.BIGINT(19).wrapper(new function.Ceil(a));
  }

  public static final type.BIGINT.UNSIGNED CEIL(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.BIGINT.UNSIGNED(20).wrapper(new function.Ceil(a));
  }

  public static final type.TINYINT CEIL(final kind.TINYINT<Byte> a) {
    return new type.TINYINT().wrapper(new function.Ceil(a));
  }

  public static final type.TINYINT.UNSIGNED CEIL(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.TINYINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final type.SMALLINT CEIL(final kind.SMALLINT<Short> a) {
    return new type.SMALLINT().wrapper(new function.Ceil(a));
  }

  public static final type.SMALLINT.UNSIGNED CEIL(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.SMALLINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final type.INT CEIL(final kind.INT<Integer> a) {
    return new type.INT().wrapper(new function.Ceil(a));
  }

  public static final type.INT.UNSIGNED CEIL(final kind.INT.UNSIGNED<Long> a) {
    return new type.INT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final type.BIGINT CEIL(final kind.BIGINT<Long> a) {
    return new type.BIGINT().wrapper(new function.Ceil(a));
  }

  public static final type.BIGINT.UNSIGNED CEIL(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.BIGINT.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final type.DECIMAL CEIL(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Ceil(a));
  }

  public static final type.DECIMAL.UNSIGNED CEIL(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Ceil(a));
  }

  public static final type.FLOAT SQRT(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.DOUBLE SQRT(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.DOUBLE SQRT(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.DECIMAL SQRT(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Sqrt(a));
  }

  public static final type.DECIMAL.UNSIGNED SQRT(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Sqrt(a));
  }

  public static final type.FLOAT EXP(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.DOUBLE EXP(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT EXP(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT EXP(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT EXP(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.DOUBLE EXP(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.DECIMAL EXP(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Exp(a));
  }

  public static final type.DECIMAL.UNSIGNED EXP(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Exp(a));
  }

  public static final type.FLOAT LN(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.DOUBLE LN(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT LN(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT LN(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT LN(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.DOUBLE LN(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.DECIMAL LN(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Ln(a));
  }

  public static final type.DECIMAL.UNSIGNED LN(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Ln(a));
  }

  public static final type.FLOAT LOG2(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.DOUBLE LOG2(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT LOG2(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT LOG2(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT LOG2(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.DOUBLE LOG2(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.DECIMAL LOG2(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Log2(a));
  }

  public static final type.DECIMAL.UNSIGNED LOG2(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log2(a));
  }

  public static final type.FLOAT LOG10(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.DOUBLE LOG10(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT LOG10(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT LOG10(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT LOG10(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.DOUBLE LOG10(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.DECIMAL LOG10(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Log10(a));
  }

  public static final type.DECIMAL.UNSIGNED LOG10(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log10(a));
  }

  public static final type.FLOAT SIN(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.DOUBLE SIN(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT SIN(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT SIN(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT SIN(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.DOUBLE SIN(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.DECIMAL SIN(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Sin(a));
  }

  public static final type.DECIMAL.UNSIGNED SIN(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Sin(a));
  }

  public static final type.FLOAT ASIN(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.DOUBLE ASIN(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT ASIN(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT ASIN(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT ASIN(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.DOUBLE ASIN(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.DECIMAL ASIN(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Asin(a));
  }

  public static final type.DECIMAL.UNSIGNED ASIN(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Asin(a));
  }

  public static final type.FLOAT COS(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.DOUBLE COS(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT COS(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT COS(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT COS(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.DOUBLE COS(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.DECIMAL COS(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Cos(a));
  }

  public static final type.DECIMAL.UNSIGNED COS(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Cos(a));
  }

  public static final type.FLOAT ACOS(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.DOUBLE ACOS(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT ACOS(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT ACOS(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT ACOS(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.DOUBLE ACOS(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.DECIMAL ACOS(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Acos(a));
  }

  public static final type.DECIMAL.UNSIGNED ACOS(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Acos(a));
  }

  public static final type.FLOAT TAN(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.DOUBLE TAN(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT TAN(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT TAN(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT TAN(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.DOUBLE TAN(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.DECIMAL TAN(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Tan(a));
  }

  public static final type.DECIMAL.UNSIGNED TAN(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Tan(a));
  }

  public static final type.FLOAT ATAN(final kind.FLOAT<Float> a) {
    return new type.FLOAT().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final kind.FLOAT.UNSIGNED<Float> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.DOUBLE ATAN(final kind.DOUBLE<Double> a) {
    return new type.DOUBLE().wrapper(new function.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final kind.DOUBLE.UNSIGNED<Double> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT ATAN(final kind.TINYINT<Byte> a) {
    return new type.FLOAT().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final kind.TINYINT.UNSIGNED<Short> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT ATAN(final kind.SMALLINT<Short> a) {
    return new type.FLOAT().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final kind.SMALLINT.UNSIGNED<Integer> a) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.FLOAT ATAN(final kind.INT<Integer> a) {
    return new type.FLOAT().wrapper(new function.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final kind.INT.UNSIGNED<Long> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.DOUBLE ATAN(final kind.BIGINT<Long> a) {
    return new type.DOUBLE().wrapper(new function.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final kind.BIGINT.UNSIGNED<BigInteger> a) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan(a));
  }

  public static final type.DECIMAL ATAN(final kind.DECIMAL<BigDecimal> a) {
    return new type.DECIMAL().wrapper(new function.Atan(a));
  }

  public static final type.DECIMAL.UNSIGNED ATAN(final kind.DECIMAL.UNSIGNED<BigDecimal> a) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan(a));
  }

  /** End Math Functions (1 parameter) **/

  /** Start Math Functions (2 parameter) **/

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final kind.FLOAT.UNSIGNED<Float> POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.FLOAT<Float> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final long b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final double b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final int b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final int b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.FLOAT<Float> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final float b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final int b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.DOUBLE<Double> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.INT<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT POW(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new function.Pow(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.FLOAT<Float> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final long b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final double b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final int b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final int b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.FLOAT<Float> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final float b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final int b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.DOUBLE<Double> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.INT<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT MOD(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new function.Mod(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.FLOAT<Float> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final long b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final double b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final int b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final int b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.FLOAT<Float> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final float b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final int b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.DOUBLE<Double> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.INT<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT LOG(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new function.Log(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.FLOAT<Float> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final long b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final double b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final int b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final int b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.FLOAT<Float> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final float b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final int b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final double b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.DOUBLE<Double> a, final long b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.INT<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new function.Atan2(a, b));
  }

  /** End Math Functions (2 parameters) **/

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal ADD(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new expression.Temporal(operator.ArithmeticPlusMinus.PLUS, a, interval));
  }

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal SUB(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new expression.Temporal(operator.ArithmeticPlusMinus.MINUS, a, interval));
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal PLUS(final Temporal a, final Interval interval) {
    return ADD(a, interval);
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal MINUS(final Temporal a, final Interval interval) {
    return SUB(a, interval);
  }

  /** Start Numeric Expressions **/

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.SMALLINT<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT.UNSIGNED<Long> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final kind.TINYINT<Byte> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final byte a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.FLOAT<Float> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT ADD(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.TINYINT<Byte> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final long b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final double b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Byte b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.TINYINT<Byte> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final short a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.TINYINT<Byte> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.FLOAT<Float> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DOUBLE<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.TINYINT.UNSIGNED<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Byte b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Short b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Byte b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Byte b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Short b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Integer b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED ADD(final UNSIGNED.Byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.SMALLINT<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.DOUBLE<Double> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final short a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.INT<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT ADD(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.SMALLINT<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT.UNSIGNED<Long> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final kind.TINYINT<Byte> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final byte a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.FLOAT<Float> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT SUB(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.TINYINT<Byte> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final long b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final double b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.TINYINT<Byte> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final short a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.TINYINT<Byte> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.FLOAT<Float> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.TINYINT.UNSIGNED<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Byte b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Byte b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Short b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Integer b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.TINYINT.UNSIGNED SUB(final UNSIGNED.Byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.SMALLINT<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.DOUBLE<Double> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final short a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.INT<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT SUB(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.SMALLINT<Short> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT.UNSIGNED<Long> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final kind.TINYINT<Byte> a, final byte b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final byte a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.FLOAT<Float> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT MUL(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.TINYINT<Byte> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final long b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final double b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.TINYINT<Byte> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final short a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.TINYINT<Byte> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.FLOAT<Float> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.TINYINT.UNSIGNED<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Integer b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Byte b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Byte b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Short b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Integer b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.TINYINT.UNSIGNED MUL(final UNSIGNED.Byte a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.TINYINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.SMALLINT<Short> a, final int b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.DOUBLE<Double> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT<Short> a, final short b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final short a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.SMALLINT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.INT<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.INT<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.INT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.BIGINT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.MULTIPLY, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.FLOAT<Float> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT.UNSIGNED<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT.UNSIGNED<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Double b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.Double a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.INT.UNSIGNED<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.INT<Integer> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT<Integer> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DOUBLE<Double> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.DOUBLE<Double> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.TINYINT<Byte> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.BIGINT<Long> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.FLOAT<Float> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.FLOAT<Float> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT<Integer> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final long b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final long a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final double b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final double a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.Long b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.Long a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.SMALLINT<Short> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final kind.DOUBLE<Double> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.FLOAT<Float> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.INT<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.INT<Integer> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.Short b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Float b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.FLOAT<Float> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.FLOAT<Float> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Float b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.FLOAT)(a instanceof kind.Numeric.UNSIGNED ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.TINYINT<Byte> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.TINYINT<Byte> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DOUBLE<Double> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.DOUBLE<Double> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.BIGINT<Long> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT<Long> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.FLOAT<Float> a, final kind.DECIMAL<BigDecimal> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.FLOAT<Float> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED && b instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.DECIMAL<BigDecimal> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.DECIMAL<BigDecimal> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return (type.DECIMAL)(a instanceof kind.Numeric.UNSIGNED ? new type.DECIMAL.UNSIGNED() : new type.DECIMAL()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DOUBLE.UNSIGNED<Double> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.DOUBLE.UNSIGNED<Double> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return (type.DOUBLE)(a instanceof kind.Numeric.UNSIGNED ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Byte b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Short b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Integer b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final UNSIGNED.Long b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final float b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final int b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.FLOAT.UNSIGNED<Float> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.FLOAT.UNSIGNED<Float> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.DOUBLE<Double> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.DOUBLE<Double> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.TINYINT.UNSIGNED<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.DECIMAL.UNSIGNED<BigDecimal> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.DECIMAL.UNSIGNED<BigDecimal> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final kind.SMALLINT<Short> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.SMALLINT<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.SMALLINT<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final UNSIGNED.BigDecimal b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DECIMAL.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final kind.BIGINT.UNSIGNED<BigInteger> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT.UNSIGNED<BigInteger> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.INT<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final kind.INT.UNSIGNED<Long> a, final UNSIGNED.Double b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE.UNSIGNED().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.TINYINT<Byte> a, final kind.BIGINT<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.BIGINT<Long> a, final kind.TINYINT<Byte> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT.UNSIGNED<Short> a, final kind.INT<Integer> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.INT<Integer> a, final kind.TINYINT.UNSIGNED<Short> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.SMALLINT.UNSIGNED<Integer> a, final double b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final kind.INT.UNSIGNED<Long> a, final long b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final kind.SMALLINT.UNSIGNED<Integer> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final kind.INT.UNSIGNED<Long> b) {
    return new type.DOUBLE().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.FLOAT<Float> a, final kind.TINYINT<Byte> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final kind.TINYINT<Byte> a, final kind.FLOAT<Float> b) {
    return new type.FLOAT().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final kind.SMALLINT<Short> a, final BigDecimal b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final kind.SMALLINT<Short> b) {
    return new type.DECIMAL().wrapper(new expression.Numeric(operator.Arithmetic.DIVIDE, a, b));
  }

  /** End Numeric Expressions **/

  /** Start Aggregates **/

  public static type.INT COUNT() {
    return new type.INT(10).wrapper(expression.Count.STAR);
  }

  public static type.INT COUNT(final type.DataType<?> dataType) {
    return new type.INT(10).wrapper(new expression.Count(dataType, false));
  }

  public final static class COUNT {
    public static type.INT DISTINCT(final type.DataType<?> dataType) {
      return new type.INT(10).wrapper(new expression.Count(dataType, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric SUM(final Numeric a) {
    return (Numeric)a.clone().wrapper(new expression.Set("SUM", a, false));
  }

  public static final class SUM {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new expression.Set("SUM", a, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric AVG(final Numeric a) {
    return (Numeric)a.clone().wrapper(new expression.Set("AVG", a, false));
  }

  public static final class AVG {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new expression.Set("AVG", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MAX(final DataType a) {
    return (DataType)a.clone().wrapper(new expression.Set("MAX", a, false));
  }

  public static final class MAX {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new expression.Set("MAX", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MIN(final DataType a) {
    return (DataType)a.clone().wrapper(new expression.Set("MIN", a, false));
  }

  public static final class MIN {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new expression.Set("MIN", a, true));
    }
  }

  /** End Aggregates **/

  private static class NOW extends type.DATETIME {
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

  private static final type.DATETIME NOW = new NOW();

  public static type.DATETIME NOW() {
    return NOW;
  }

  private static class PI extends type.DOUBLE {
    protected PI() {
      super();
      this.wrapper(new function.Pi());
    }

    @Override
    public final PI clone() {
      return new PI();
    }
  }

  private static final type.DOUBLE PI = new PI();

  public static type.DOUBLE PI() {
    return PI;
  }

  /** Condition **/

  @SafeVarargs
  public static type.BOOLEAN AND(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(operator.Boolean.AND, a, b, conditions);
  }

  public static type.BOOLEAN AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(operator.Boolean.AND, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static type.BOOLEAN AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(operator.Boolean.AND, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static type.BOOLEAN AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(operator.Boolean.AND, array[0], array[1], Arrays.subArray(array, 2));
  }

  @SafeVarargs
  public static type.BOOLEAN OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(operator.Boolean.OR, a, b, conditions);
  }

  public static type.BOOLEAN OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(operator.Boolean.OR, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static type.BOOLEAN OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(operator.Boolean.OR, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static type.BOOLEAN OR(final Collection<Condition<?>> conditions) {
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

  public static <T>ALL<T> ALL(final Select.untyped.SELECT<? extends Subject<T>> subQuery) {
    return new ALL<T>(subQuery);
  }

  public static <T>ANY<T> ANY(final Select.untyped.SELECT<? extends Subject<T>> subQuery) {
    return new ANY<T>(subQuery);
  }

  public static <T>SOME<T> SOME(final Select.untyped.SELECT<? extends Subject<T>> subQuery) {
    return new SOME<T>(subQuery);
  }

  public static Predicate BETWEEN(final kind.Numeric<?> dataType, final kind.Numeric<?> a, final kind.Numeric<?> b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final kind.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final kind.TIME<LocalTime> a, final kind.TIME<LocalTime> b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.Numeric<?> dataType, final kind.Numeric<?> a, final Number b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final kind.TIME<LocalTime> a, final LocalTime b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, a, (type.TIME)type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Numeric<?> dataType, final Number a, final kind.Numeric<?> b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, type.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final kind.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final kind.Textual<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), b, true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATETIME<LocalDateTime> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATETIME<LocalDateTime> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final kind.DATETIME<LocalDateTime> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATE<LocalDate> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final kind.DATE<LocalDate> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final kind.DATETIME<LocalDateTime> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATE<LocalDate> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final kind.DATE<LocalDate> b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, true);
  }

  public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final LocalTime a, final kind.TIME<LocalTime> b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, (type.TIME)type.DataType.wrap(a), b, true);
  }

  public static <T extends Number>Predicate BETWEEN(final kind.Numeric<?> dataType, final T a, final T b) {
    return new BetweenPredicates.NumericBetweenPredicate(dataType, (type.Numeric<? extends T>)type.DataType.wrap(a), (type.Numeric<? extends T>)type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final String b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final Enum<?> b) {
    return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final LocalDateTime b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final LocalDate b) {
    return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), true);
  }

  public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final LocalTime a, final LocalTime b) {
    return new BetweenPredicates.TimeBetweenPredicate(dataType, (type.TIME)type.DataType.wrap(a), (type.TIME)type.DataType.wrap(b), true);
  }

  public static Predicate LIKE(final kind.CHAR<String> a, final CharSequence b) {
    return new LikePredicate(a, true, b);
  }

  public static <T>Predicate IN(final kind.DataType<T> a, final Collection<T> b) {
    return new InPredicate(a, true, b);
  }

  @SafeVarargs
  public static <T>Predicate IN(final kind.DataType<T> a, final T ... b) {
    return new InPredicate(a, true, b);
  }

  public static <T>Predicate IN(final kind.DataType<T> a, final Select.untyped.SELECT<? extends type.DataType<T>> b) {
    return new InPredicate(a, true, b);
  }

  public static Predicate EXISTS(final Select.untyped.SELECT<?> subQuery) {
    return new ExistsPredicate(subQuery);
  }

  public static final class NOT {
    public static Predicate BETWEEN(final kind.Numeric<?> dataType, final kind.Numeric<?> a, final kind.Numeric<?> b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final kind.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final kind.TIME<LocalTime> a, final kind.TIME<LocalTime> b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, a, b, false);
    }

    public static Predicate BETWEEN(final kind.Numeric<?> dataType, final kind.Numeric<?> a, final Number b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final kind.Textual<?> a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final kind.DATE<LocalDate> a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATETIME<LocalDateTime> a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final kind.DATE<LocalDate> a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, a, type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final kind.TIME<LocalTime> a, final LocalTime b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, a, (type.TIME)type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Numeric<?> dataType, final Number a, final kind.Numeric<?> b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final kind.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final kind.Textual<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final kind.DATETIME<LocalDateTime> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final kind.DATE<LocalDate> b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final LocalTime a, final kind.TIME<LocalTime> b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, (type.TIME)type.DataType.wrap(a), b, false);
    }

    public static Predicate BETWEEN(final kind.Numeric<?> dataType, final Number a, final Number b) {
      return new BetweenPredicates.NumericBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final String a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final String b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.Textual<?> dataType, final Enum<?> a, final Enum<?> b) {
      return new BetweenPredicates.TextualBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDateTime a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATETIME<LocalDateTime> dataType, final LocalDate a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDateTime a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final LocalDateTime b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.DATE<LocalDate> dataType, final LocalDate a, final LocalDate b) {
      return new BetweenPredicates.TemporalBetweenPredicate(dataType, type.DataType.wrap(a), type.DataType.wrap(b), false);
    }

    public static Predicate BETWEEN(final kind.TIME<LocalTime> dataType, final LocalTime a, final LocalTime b) {
      return new BetweenPredicates.TimeBetweenPredicate(dataType, (type.TIME)type.DataType.wrap(a), (type.TIME)type.DataType.wrap(b), false);
    }

    public static Predicate LIKE(final kind.CHAR<String> a, final String b) {
      return new LikePredicate(a, false, b);
    }

    public static <T>Predicate IN(final kind.DataType<T> a, final Collection<T> b) {
      return new InPredicate(a, false, b);
    }

    @SafeVarargs
    public static <T>Predicate IN(final kind.DataType<T> a, final T ... b) {
      return new InPredicate(a, false, b);
    }

    public static <T>Predicate IN(final kind.DataType<T> a, final Select.untyped.SELECT<? extends type.DataType<T>> b) {
      return new InPredicate(a, false, b);
    }
  }

  public static final class IS {
    public static final class NOT {
      public static Predicate NULL(final kind.DataType<?> dataType) {
        return new NullPredicate(dataType, false);
      }
    }

    public static Predicate NULL(final kind.DataType<?> dataType) {
      return new NullPredicate(dataType, true);
    }
  }

  private DML() {
  }
}