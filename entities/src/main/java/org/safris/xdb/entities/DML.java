/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.entities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collection;

import org.safris.commons.lang.Arrays;
import org.safris.commons.lang.Numbers;
import org.safris.xdb.entities.model.delete;
import org.safris.xdb.entities.model.expression;
import org.safris.xdb.entities.model.insert;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.entities.model.update;
import org.safris.xdb.xdd.xe.$xdd_data;

@SuppressWarnings("hiding")
public final class DML {

  /** Ordering Specification **/

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V ASC(final V dataType) {
    final V wrapper = (V)dataType.clone();
    wrapper.wrapper(new OrderingSpec(Operator.ASC, dataType));
    return wrapper;
  }

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V DESC(final V dataType) {
    final V wrapper = (V)dataType.clone();
    wrapper.wrapper(new OrderingSpec(Operator.DESC, dataType));
    return wrapper;
  }

  /** START Cast **/

  public static Cast.BIGINT CAST(final type.BIGINT a) {
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

  public static Cast.DOUBLE CAST(final type.DOUBLE a) {
    return new Cast.DOUBLE(a);
  }

  public static Cast.FLOAT CAST(final type.FLOAT a) {
    return new Cast.FLOAT(a);
  }

  public static Cast.INTEGER CAST(final type.INT a) {
    return new Cast.INTEGER(a);
  }

  public static Cast.MEDIUMINT CAST(final type.MEDIUMINT a) {
    return new Cast.MEDIUMINT(a);
  }

  public static Cast.SMALLINT CAST(final type.SMALLINT a) {
    return new Cast.SMALLINT(a);
  }

  public static Cast.TIME CAST(final type.TIME a) {
    return new Cast.TIME(a);
  }

  /** END Cast **/

  /** START ComparisonPredicate **/

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN EQ(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN EQ(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.EQ, a, b);
  }

  public static type.BOOLEAN EQ(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Textual>type.BOOLEAN EQ(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.EQ, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN NE(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN NE(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static type.BOOLEAN NE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.NE, a, b);
  }

  public static type.BOOLEAN NE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Textual>type.BOOLEAN NE(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.NE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LT(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LT(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static type.BOOLEAN LT(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.LT, a, b);
  }

  public static type.BOOLEAN LT(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Textual>type.BOOLEAN LT(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GT(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GT(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static type.BOOLEAN GT(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.GT, a, b);
  }

  public static type.BOOLEAN GT(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Textual>type.BOOLEAN GT(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GT, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN LTE(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN LTE(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.LTE, a, b);
  }

  public static type.BOOLEAN LTE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Textual>type.BOOLEAN LTE(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.LTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final type.Numeric<? extends Number> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final type.Numeric<? extends Number> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final Number a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final type.Numeric<? extends Number> a, final select.SELECT<? extends type.DataType<Number>> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final select.SELECT<? extends type.Numeric<? extends Number>> a, final type.Numeric<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final Number a, final select.SELECT<? extends type.Numeric<? super Number>> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final select.SELECT<? extends type.Numeric<? super Number>> a, final Number b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Number extends java.lang.Number>type.BOOLEAN GTE(final type.Numeric<? extends Number> a, final QuantifiedComparisonPredicate<? extends Number> b) {
    return new ComparisonPredicate<Number>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final type.Temporal<? extends Temporal> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final type.Temporal<? super Temporal> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final Temporal a, final type.Temporal<? super Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final type.Temporal<? extends Temporal> a, final select.SELECT<? extends type.Temporal<? extends Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final select.SELECT<? extends type.Temporal<? extends Temporal>> a, final type.Temporal<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final Temporal a, final select.SELECT<? extends type.Temporal<? super Temporal>> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final select.SELECT<? extends type.Temporal<? super Temporal>> a, final Temporal b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Temporal extends java.time.temporal.Temporal>type.BOOLEAN GTE(final type.Temporal<? super Temporal> a, final QuantifiedComparisonPredicate<? extends Temporal> b) {
    return new ComparisonPredicate<Temporal>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final type.Textual<? extends Textual> a, final type.Textual<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final type.Textual<?> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final Textual a, final type.Textual<?> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final type.BOOLEAN a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final type.BOOLEAN a, final boolean b) {
    return new ComparisonPredicate<Boolean>(Operator.GTE, a, b);
  }

  public static type.BOOLEAN GTE(final boolean a, final type.BOOLEAN b) {
    return new ComparisonPredicate<Boolean>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final type.Textual<? super Textual> a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final select.SELECT<? extends type.Textual<? super Textual>> a, final type.Textual<? super Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final Textual a, final select.SELECT<? extends type.Textual<? super Textual>> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final select.SELECT<? extends type.Textual<? super Textual>> a, final Textual b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  public static <Textual>type.BOOLEAN GTE(final type.Textual<? super Textual> a, final QuantifiedComparisonPredicate<? extends Textual> b) {
    return new ComparisonPredicate<Textual>(Operator.GTE, a, b);
  }

  /** END ComparisonPredicate **/

  /** SELECT **/

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final T ... entities) {
    return new Select.SELECT<T>(false, entities);
  }

  public static final class SELECT {
    @SafeVarargs
    public static final <T extends Subject<?>>select._SELECT<T> DISTINCT(final T ... entities) {
      return new Select.SELECT<T>(true, entities);
    }
  }

  /** CASE **/

  public static <T>expression.WHEN CASE_WHEN(final Condition<T> condition) {
    return new Case.CASE_WHEN(condition);
  }

  /** DELETE **/

  public static update.UPDATE_SET UPDATE(final Entity entity) {
    return new Update.UPDATE(entity);
  }

  public static delete.DELETE_WHERE DELETE(final Entity entity) {
    return new Delete.DELETE(entity);
  }

  /** INSERT **/

  @SafeVarargs
  public static <E extends Entity>insert.INSERT_SELECT<E> INSERT(final E ... entities) {
    return new Insert.INSERT<E>(entities);
  }

  public static insert.INSERT INSERT(final $xdd_data data) {
    return new Insert.INSERT<Entity>(Entities.toEntities(data));
  }

  /** String Functions **/

  public static type.CHAR CONCAT(final type.CHAR a, final type.CHAR b) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.CHAR b, final CharSequence c) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c, final CharSequence d) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b, final CharSequence c) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c, final CharSequence d) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c, final CharSequence d) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b, final CharSequence c) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b, final CharSequence c) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c, final CharSequence d) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b) {
    final type.CHAR wrapper = a.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c) {
    final type.CHAR wrapper = b.clone();
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b) {
    final type.CHAR wrapper = new type.CHAR(a.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c) {
    final type.CHAR wrapper = new type.CHAR(b.length(), false);
    wrapper.wrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  /** Math Functions **/

  public static type.FLOAT ABS(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.FLOAT a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.FLOAT FLOOR(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.FLOAT CEIL(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.FLOAT POW(final type.FLOAT x, final double y) {
    final type.FLOAT wrapper = x.clone();
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.FLOAT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT ROUND(final type.FLOAT a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.FLOAT SQRT(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.FLOAT SIN(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.FLOAT ASIN(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.FLOAT COS(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.FLOAT ACOS(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.FLOAT TAN(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT EXP(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.FLOAT LN(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.FLOAT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG2(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.FLOAT LOG10(final type.FLOAT a) {
    final type.FLOAT wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.DOUBLE ABS(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.DOUBLE a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.DOUBLE FLOOR(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.DOUBLE CEIL(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.DOUBLE POW(final type.DOUBLE x, final double y) {
    final type.DOUBLE wrapper = x.clone();
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE ROUND(final type.DOUBLE a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.DOUBLE SQRT(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.DOUBLE SIN(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.DOUBLE ASIN(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.DOUBLE COS(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.DOUBLE ACOS(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.DOUBLE TAN(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE EXP(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.DOUBLE LN(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG2(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.DOUBLE LOG10(final type.DOUBLE a) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.DECIMAL ABS(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.DECIMAL a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.DECIMAL FLOOR(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.DECIMAL CEIL(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.DECIMAL POW(final type.DECIMAL x, final double y) {
    final type.DECIMAL wrapper = x.clone();
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL MOD(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DECIMAL ROUND(final type.DECIMAL a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.DECIMAL SQRT(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.DECIMAL SIN(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.DECIMAL ASIN(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.DECIMAL COS(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.DECIMAL ACOS(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.DECIMAL TAN(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.DECIMAL ATAN(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL ATAN2(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DECIMAL EXP(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.DECIMAL LN(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DECIMAL LOG2(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.DECIMAL LOG10(final type.DECIMAL a) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.FLOAT ABS(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.SMALLINT a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.FLOAT FLOOR(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.FLOAT CEIL(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.FLOAT POW(final type.SMALLINT x, final double y) {
    final type.FLOAT wrapper = new type.FLOAT(x.unsigned());
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.SMALLINT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT ROUND(final type.SMALLINT a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.FLOAT SQRT(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.FLOAT SIN(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.FLOAT ASIN(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.FLOAT COS(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.FLOAT ACOS(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.FLOAT TAN(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.SMALLINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT EXP(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.FLOAT LN(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.SMALLINT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG2(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.FLOAT LOG10(final type.SMALLINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.FLOAT ABS(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.MEDIUMINT a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.FLOAT FLOOR(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.FLOAT CEIL(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.FLOAT POW(final type.MEDIUMINT x, final double y) {
    final type.FLOAT wrapper = new type.FLOAT(x.unsigned());
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT MOD(final type.MEDIUMINT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.FLOAT ROUND(final type.MEDIUMINT a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.FLOAT SQRT(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.FLOAT SIN(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.FLOAT ASIN(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.FLOAT COS(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.FLOAT ACOS(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.FLOAT TAN(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT ATAN2(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.FLOAT EXP(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.FLOAT LN(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG(final type.MEDIUMINT a, final long b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.FLOAT LOG2(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.FLOAT LOG10(final type.MEDIUMINT a) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned());
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.DOUBLE ABS(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.INT a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.DOUBLE FLOOR(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.DOUBLE CEIL(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.DOUBLE POW(final type.INT x, final double y) {
    final type.DOUBLE wrapper = new type.DOUBLE(x.unsigned());
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.INT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE ROUND(final type.INT a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.DOUBLE SQRT(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.DOUBLE SIN(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.DOUBLE ASIN(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.DOUBLE COS(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.DOUBLE ACOS(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.DOUBLE TAN(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.INT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(false);
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE EXP(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.DOUBLE LN(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.INT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG2(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.DOUBLE LOG10(final type.INT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  public static type.DOUBLE ABS(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Abs(a));
    return wrapper;
  }

  public static type.SMALLINT SIGN(final type.BIGINT a) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)1, false);
    wrapper.wrapper(new function.numeric.Sign(a));
    return wrapper;
  }

  public static type.DOUBLE FLOOR(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Floor(a));
    return wrapper;
  }

  public static type.DOUBLE CEIL(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Ceil(a));
    return wrapper;
  }

  public static type.DOUBLE POW(final type.BIGINT x, final double y) {
    final type.DOUBLE wrapper = new type.DOUBLE(x.unsigned());
    wrapper.wrapper(new function.numeric.Pow(x, y));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE MOD(final type.BIGINT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Mod(a, b));
    return wrapper;
  }

  public static type.DOUBLE ROUND(final type.BIGINT a, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Round(a, decimal));
    return wrapper;
  }

  public static type.DOUBLE SQRT(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Sqrt(a));
    return wrapper;
  }

  public static type.DOUBLE SIN(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Sin(a));
    return wrapper;
  }

  public static type.DOUBLE ASIN(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Asin(a));
    return wrapper;
  }

  public static type.DOUBLE COS(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Cos(a));
    return wrapper;
  }

  public static type.DOUBLE ACOS(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Acos(a));
    return wrapper;
  }

  public static type.DOUBLE TAN(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Tan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan(a));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE ATAN2(final type.BIGINT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Atan2(a, b));
    return wrapper;
  }

  public static type.DOUBLE EXP(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Exp(a));
    return wrapper;
  }

  public static type.DOUBLE LN(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Ln(a));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.DECIMAL b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG(final type.BIGINT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log(a, b));
    return wrapper;
  }

  public static type.DOUBLE LOG2(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log2(a));
    return wrapper;
  }

  public static type.DOUBLE LOG10(final type.BIGINT a) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned());
    wrapper.wrapper(new function.numeric.Log10(a));
    return wrapper;
  }

  /** Aggregate **/

  public static type.INT COUNT() {
    final type.INT wrapper = new type.INT((short)10, true);
    wrapper.wrapper(CountFunction.STAR);
    return wrapper;
  }

  public static type.INT COUNT(final type.DataType<?> dataType) {
    final type.INT wrapper = new type.INT((short)10, true);
    wrapper.wrapper(new CountFunction(dataType, false));
    return wrapper;
  }

  public final static class COUNT {
    public static type.INT DISTINCT(final type.DataType<?> dataType) {
      final type.INT wrapper = new type.INT((short)10, true);
      wrapper.wrapper(new CountFunction(dataType, true));
      return wrapper;
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric SUM(final Numeric a) {
    final Numeric wrapper = (Numeric)a.clone();
    wrapper.wrapper(new SetFunction("SUM", a, false));
    return wrapper;
  }

  public static final class SUM {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      final Numeric wrapper = (Numeric)a.clone();
      wrapper.wrapper(new SetFunction("SUM", a, true));
      return wrapper;
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric AVG(final Numeric a) {
    final Numeric wrapper = (Numeric)a.clone();
    wrapper.wrapper(new SetFunction("AVG", a, false));
    return wrapper;
  }

  public static final class AVG {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      final Numeric wrapper = (Numeric)a.clone();
      wrapper.wrapper(new SetFunction("AVG", a, true));
      return wrapper;
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MAX(final DataType a) {
    final DataType wrapper = (DataType)a.clone();
    wrapper.wrapper(new SetFunction("MAX", a, false));
    return wrapper;
  }

  public static final class MAX {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      final DataType wrapper = (DataType)a.clone();
      wrapper.wrapper(new SetFunction("MAX", a, true));
      return wrapper;
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MIN(final DataType a) {
    final DataType wrapper = (DataType)a.clone();
    wrapper.wrapper(new SetFunction("MIN", a, false));
    return wrapper;
  }

  public static final class MIN {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      final DataType wrapper = (DataType)a.clone();
      wrapper.wrapper(new SetFunction("MIN", a, true));
      return wrapper;
    }
  }

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal ADD(final Temporal a, final Interval interval) {
    final Temporal wrapper = (Temporal)a.clone();
    wrapper.wrapper(new TemporalExpression(Operator.PLUS, a, interval));
    return wrapper;
  }

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal SUB(final Temporal a, final Interval interval) {
    final Temporal wrapper = (Temporal)a.clone();
    wrapper.wrapper(new TemporalExpression(Operator.MINUS, a, interval));
    return wrapper;
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal PLUS(final Temporal a, final Interval interval) {
    return ADD(a, interval);
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal MINUS(final Temporal a, final Interval interval) {
    return SUB(a, interval);
  }

  public static type.FLOAT ADD(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.FLOAT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.FLOAT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.FLOAT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.FLOAT a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.FLOAT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final short a, final type.FLOAT b) {
    final type.FLOAT wrapper = b.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final float a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final int a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final long a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final BigInteger a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.FLOAT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.DOUBLE a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DOUBLE a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final short a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final float a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final int a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final long a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final BigInteger a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final BigInteger b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.abs().toString().length()), a.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.DECIMAL a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)Math.max(a.scale(), b.scale()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final short a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final float a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final double a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final int a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final long a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigInteger a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), (short)Math.max(a.scale(), b.scale()), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.SMALLINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.SMALLINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.SMALLINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT ADD(final type.SMALLINT a, final short b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.SMALLINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.SMALLINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.SMALLINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.SMALLINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.SMALLINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT ADD(final short a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final float a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final int a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final long a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final BigInteger a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.SMALLINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.MEDIUMINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.MEDIUMINT a, final short b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.MEDIUMINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final type.MEDIUMINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.MEDIUMINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.MEDIUMINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.MEDIUMINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final short a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(b.precision(), Numbers.precision(a)), b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT ADD(final float a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT ADD(final int a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final long a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final BigInteger a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = b.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.INT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.INT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final short b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final int b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final type.INT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.INT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.INT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final short a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final float a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final int a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT ADD(final long a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final BigInteger a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.INT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.BIGINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final short b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final int b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final long b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final type.BIGINT a, final BigInteger b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final type.BIGINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final short a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT(b.precision(), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final float a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE ADD(final double a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final int a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final long a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT ADD(final BigInteger a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL ADD(final BigDecimal a, final type.BIGINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.FLOAT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.FLOAT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.FLOAT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.FLOAT a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.FLOAT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final short a, final type.FLOAT b) {
    final type.FLOAT wrapper = b.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final float a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final int a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final long a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final BigInteger a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.FLOAT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.DOUBLE a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DOUBLE a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final short a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final float a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final int a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final long a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final BigInteger a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final BigInteger b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.abs().toString().length()), a.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.DECIMAL a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)Math.max(a.scale(), b.scale()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final short a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final float a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final double a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final int a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final long a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigInteger a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), (short)Math.max((short)a.scale(), b.scale()), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.SMALLINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.SMALLINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.SMALLINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT SUB(final type.SMALLINT a, final short b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.SMALLINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.SMALLINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.SMALLINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.SMALLINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.SMALLINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT SUB(final short a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final float a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final int a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final long a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final BigInteger a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.SMALLINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.MEDIUMINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.MEDIUMINT a, final short b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.MEDIUMINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final type.MEDIUMINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.MEDIUMINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.MEDIUMINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.MEDIUMINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final short a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(b.precision(), Numbers.precision(a)), b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT SUB(final float a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT SUB(final int a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final long a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final BigInteger a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.INT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.INT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final short b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final int b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final type.INT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.INT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.INT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final short a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final float a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final int a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.INT SUB(final long a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final BigInteger a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.INT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.BIGINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final short b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final int b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final long b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final type.BIGINT a, final BigInteger b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final type.BIGINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final short a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT(b.precision(), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final float a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE SUB(final double a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final int a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final long a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.BIGINT SUB(final BigInteger a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.DECIMAL SUB(final BigDecimal a, final type.BIGINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MINUS, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.FLOAT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.FLOAT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.FLOAT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.FLOAT a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.FLOAT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final short a, final type.FLOAT b) {
    final type.FLOAT wrapper = b.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final float a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final int a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final long a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final BigInteger a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.FLOAT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.DOUBLE a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DOUBLE a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final short a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final float a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final int a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final long a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final BigInteger a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final BigInteger b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.abs().toString().length()), a.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.DECIMAL a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)Math.max(a.scale(), b.scale()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final short a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final float a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final double a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final int a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final long a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigInteger a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), (short)Math.max(a.scale(), b.scale()), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.SMALLINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.SMALLINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.SMALLINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.SMALLINT MUL(final type.SMALLINT a, final short b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.SMALLINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.SMALLINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.SMALLINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.SMALLINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.SMALLINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.SMALLINT MUL(final short a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final float a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final int a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final long a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final BigInteger a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.SMALLINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.MEDIUMINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.MEDIUMINT a, final short b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.MEDIUMINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final type.MEDIUMINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.MEDIUMINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.MEDIUMINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.MEDIUMINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final short a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(b.precision(), Numbers.precision(a)), b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT MUL(final float a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT MUL(final int a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final long a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final BigInteger a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.INT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.INT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final short b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final int b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final type.INT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.INT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.INT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final short a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final float a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final int a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.INT MUL(final long a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final BigInteger a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.INT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.BIGINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final short b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final int b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final long b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final type.BIGINT a, final BigInteger b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final type.BIGINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final short a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT(b.precision(), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final float a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DOUBLE MUL(final double a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final int a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final long a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.BIGINT MUL(final BigInteger a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.DECIMAL MUL(final BigDecimal a, final type.BIGINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.FLOAT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.FLOAT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final type.INT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.FLOAT a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final short b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final double b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.FLOAT a, final int b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.FLOAT a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.FLOAT a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.FLOAT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final short a, final type.FLOAT b) {
    final type.FLOAT wrapper = b.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final float a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final int a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final long a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final BigInteger a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.FLOAT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DOUBLE a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.INT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT b) {
    final type.DOUBLE wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final short b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final int b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final long b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.DOUBLE a, final BigInteger b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DOUBLE a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final short a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final float a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final int a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final long a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final BigInteger a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.FLOAT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.DOUBLE b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.INT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT b) {
    final type.DECIMAL wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final short b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final float b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final double b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final int b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final long b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), Numbers.precision(b)), a.scale(), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final BigInteger b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.abs().toString().length()), a.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.DECIMAL a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)Math.max(a.scale(), b.scale()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final short a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final float a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final double a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL(b.precision(), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final int a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final long a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigInteger a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), b.scale(), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), Numbers.precision(a)), (short)Math.max(a.scale(), b.scale()), a.signum() >=0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.SMALLINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.SMALLINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.SMALLINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.SMALLINT DIV(final type.SMALLINT a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.SMALLINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.SMALLINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.SMALLINT DIV(final type.SMALLINT a, final short b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.SMALLINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.SMALLINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.SMALLINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.SMALLINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.SMALLINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.SMALLINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.SMALLINT DIV(final short a, final type.SMALLINT b) {
    final type.SMALLINT wrapper = new type.SMALLINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final float a, final type.SMALLINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.SMALLINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final int a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final long a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final BigInteger a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.SMALLINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL(a.precision(), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.SMALLINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.MEDIUMINT a, final type.FLOAT b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.MEDIUMINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.MEDIUMINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.MEDIUMINT a, final type.SMALLINT b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.MEDIUMINT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.MEDIUMINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.MEDIUMINT a, final short b) {
    final type.MEDIUMINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final type.MEDIUMINT a, final float b) {
    final type.FLOAT wrapper = new type.FLOAT(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.MEDIUMINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final type.MEDIUMINT a, final int b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.MEDIUMINT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.MEDIUMINT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.MEDIUMINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)b.scale(), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final short a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(b.precision(), Numbers.precision(a)), b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT DIV(final float a, final type.MEDIUMINT b) {
    final type.FLOAT wrapper = new type.FLOAT(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.MEDIUMINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.MEDIUMINT DIV(final int a, final type.MEDIUMINT b) {
    final type.MEDIUMINT wrapper = new type.MEDIUMINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final long a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final BigInteger a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.MEDIUMINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.INT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.INT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.INT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final type.SMALLINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final type.MEDIUMINT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.INT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final short b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.INT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.INT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final int b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final type.INT a, final long b) {
    final type.INT wrapper = new type.INT((short)Math.max(a.precision(), Numbers.precision(b)), a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.INT a, final BigInteger b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.abs().toString().length()), a.unsigned() && b.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.INT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final short a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final float a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.INT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final int a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.INT DIV(final long a, final type.INT b) {
    final type.INT wrapper = new type.INT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final BigInteger a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.INT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), (short)a.scale(), b.unsigned() && a.signum() >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.BIGINT a, final type.FLOAT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.BIGINT a, final type.DOUBLE b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.BIGINT a, final type.DECIMAL b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(a.precision(), b.precision()), b.scale(), b.unsigned() && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.PLUS, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final type.SMALLINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final type.MEDIUMINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final type.INT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(a.precision(), b.precision()), a.unsigned() && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final short b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.BIGINT a, final float b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final type.BIGINT a, final double b) {
    final type.DOUBLE wrapper = new type.DOUBLE(a.unsigned() && b >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final int b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final long b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final type.BIGINT a, final BigInteger b) {
    final type.BIGINT wrapper = a.clone();
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final type.BIGINT a, final BigDecimal b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)b.precision(), (short)b.scale(), b.signum() >= 0 && a.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final short a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT(b.precision(), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final float a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DOUBLE DIV(final double a, final type.BIGINT b) {
    final type.DOUBLE wrapper = new type.DOUBLE(b.unsigned() && a >= 0);
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final int a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final long a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.BIGINT DIV(final BigInteger a, final type.BIGINT b) {
    final type.BIGINT wrapper = new type.BIGINT((short)Math.max(Numbers.precision(a), b.precision()), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.DECIMAL DIV(final BigDecimal a, final type.BIGINT b) {
    final type.DECIMAL wrapper = new type.DECIMAL((short)Math.max(b.precision(), a.precision()), (short)a.scale(), a.signum() >= 0 && b.unsigned());
    wrapper.wrapper(new NumericExpression(Operator.DIVIDE, a, b));
    return wrapper;
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.FLOAT a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.FLOAT a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.FLOAT a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final short b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final float b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final double b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.FLOAT a, final int b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.FLOAT a, final long b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.FLOAT a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.FLOAT a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final short a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final float a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final int a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final long a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final BigInteger a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DOUBLE a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final short b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final float b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final double b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final int b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final long b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.DOUBLE a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DOUBLE a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final short a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final float a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final int a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final long a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final BigInteger a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final short b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final float b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final double b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final int b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final long b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.DECIMAL a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final short a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final float a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final double a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final int a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final long a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigInteger a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.SMALLINT a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.SMALLINT a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.SMALLINT a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.SMALLINT PLUS(final type.SMALLINT a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.SMALLINT a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.SMALLINT a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.SMALLINT PLUS(final type.SMALLINT a, final short b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.SMALLINT a, final float b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.SMALLINT a, final double b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.SMALLINT a, final int b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.SMALLINT a, final long b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.SMALLINT a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.SMALLINT a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.SMALLINT PLUS(final short a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final float a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final int a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final long a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final BigInteger a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.SMALLINT a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.MEDIUMINT a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.MEDIUMINT a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.MEDIUMINT a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.MEDIUMINT a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.MEDIUMINT a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.MEDIUMINT a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.MEDIUMINT a, final short b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final type.MEDIUMINT a, final float b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.MEDIUMINT a, final double b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final type.MEDIUMINT a, final int b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.MEDIUMINT a, final long b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.MEDIUMINT a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.MEDIUMINT a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final short a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT PLUS(final float a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.MEDIUMINT PLUS(final int a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final long a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final BigInteger a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.INT a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.INT a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.INT a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.INT a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final short b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.INT a, final float b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.INT a, final double b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final int b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final type.INT a, final long b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.INT a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.INT a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final short a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final float a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final int a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.INT PLUS(final long a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final BigInteger a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.BIGINT a, final type.FLOAT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.BIGINT a, final type.DOUBLE b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.BIGINT a, final type.DECIMAL b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final type.SMALLINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final type.MEDIUMINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final type.INT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final short b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.BIGINT a, final float b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final type.BIGINT a, final double b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final int b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final long b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final type.BIGINT a, final BigInteger b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final type.BIGINT a, final BigDecimal b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final short a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final float a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.DOUBLE PLUS(final double a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final int a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final long a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.BIGINT PLUS(final BigInteger a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.DECIMAL PLUS(final BigDecimal a, final type.BIGINT b) {
    return ADD(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.FLOAT a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.FLOAT a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.FLOAT a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final short b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final float b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final double b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.FLOAT a, final int b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.FLOAT a, final long b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.FLOAT a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.FLOAT a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final short a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final float a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final int a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final long a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final BigInteger a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DOUBLE a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final short b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final float b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final double b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final int b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final long b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.DOUBLE a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DOUBLE a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final short a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final float a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final int a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final long a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final BigInteger a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final short b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final float b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final double b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final int b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final long b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.DECIMAL a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final short a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final float a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final double a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final int a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final long a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigInteger a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.SMALLINT a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.SMALLINT a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.SMALLINT a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.SMALLINT MINUS(final type.SMALLINT a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.SMALLINT a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.SMALLINT a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.SMALLINT MINUS(final type.SMALLINT a, final short b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.SMALLINT a, final float b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.SMALLINT a, final double b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.SMALLINT a, final int b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.SMALLINT a, final long b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.SMALLINT a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.SMALLINT a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.SMALLINT MINUS(final short a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final float a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final int a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final long a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final BigInteger a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.SMALLINT a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.MEDIUMINT a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.MEDIUMINT a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.MEDIUMINT a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.MEDIUMINT a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.MEDIUMINT a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.MEDIUMINT a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.MEDIUMINT a, final short b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final type.MEDIUMINT a, final float b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.MEDIUMINT a, final double b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final type.MEDIUMINT a, final int b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.MEDIUMINT a, final long b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.MEDIUMINT a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.MEDIUMINT a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final short a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.FLOAT MINUS(final float a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.MEDIUMINT MINUS(final int a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final long a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final BigInteger a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.INT a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.INT a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.INT a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.INT a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final short b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.INT a, final float b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.INT a, final double b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final int b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final type.INT a, final long b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.INT a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.INT a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final short a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final float a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final int a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.INT MINUS(final long a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final BigInteger a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.BIGINT a, final type.FLOAT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.BIGINT a, final type.DOUBLE b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.BIGINT a, final type.DECIMAL b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final type.SMALLINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final type.MEDIUMINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final type.INT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final short b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.BIGINT a, final float b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final type.BIGINT a, final double b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final int b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final long b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final type.BIGINT a, final BigInteger b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final type.BIGINT a, final BigDecimal b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final short a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final float a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.DOUBLE MINUS(final double a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final int a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final long a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.BIGINT MINUS(final BigInteger a, final type.BIGINT b) {
    return SUB(a, b);
  }

  public static type.DECIMAL MINUS(final BigDecimal a, final type.BIGINT b) {
    return SUB(a, b);
  }

  private static class NOW extends type.DATETIME {
    protected NOW() {
      super((short)10);
      this.wrapper(new TemporalFunction("NOW"));
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
      super(true);
      this.wrapper(new function.numeric.Pi());
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
    return new BooleanTerm(Operator.AND, a, b, conditions);
  }

  public static type.BOOLEAN AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(Operator.AND, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static type.BOOLEAN AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(Operator.AND, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static type.BOOLEAN AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(Operator.AND, array[0], array[1], Arrays.subArray(array, 2));
  }

  @SafeVarargs
  public static type.BOOLEAN OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(Operator.OR, a, b, conditions);
  }

  public static type.BOOLEAN OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(Operator.OR, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static type.BOOLEAN OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(Operator.OR, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static type.BOOLEAN OR(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(Operator.OR, array[0], array[1], Arrays.subArray(array, 2));
  }

  /** Predicate **/

  protected static final class ALL<T> extends QuantifiedComparisonPredicate<T> {
    protected ALL(final select.SELECT<?> subQuery) {
      super("ALL", subQuery);
    }
  }

  protected static final class ANY<T> extends QuantifiedComparisonPredicate<T> {
    protected ANY(final select.SELECT<?> subQuery) {
      super("ANY", subQuery);
    }
  }

  protected static final class SOME<T> extends QuantifiedComparisonPredicate<T> {
    protected SOME(final select.SELECT<?> subQuery) {
      super("SOME", subQuery);
    }
  }

  public static <T>ALL<T> ALL(final select.SELECT<? extends Subject<T>> subQuery) {
    return new ALL<T>(subQuery);
  }

  public static <T>ANY<T> ANY(final select.SELECT<? extends Subject<T>> subQuery) {
    return new ANY<T>(subQuery);
  }

  public static <T>SOME<T> SOME(final select.SELECT<? extends Subject<T>> subQuery) {
    return new SOME<T>(subQuery);
  }

  public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final type.Numeric<? extends T> a, final type.Numeric<? extends T> b) {
    return new BetweenPredicate<T>(true, dataType, a, b);
  }

  public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final type.Textual<? super T> a, final type.Textual<? super T> b) {
    return new BetweenPredicate<T>(true, dataType, a, b);
  }

  public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final type.Temporal<? extends T> a, final type.Temporal<? extends T> b) {
    return new BetweenPredicate<T>(true, dataType, a, b);
  }

  public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final type.TIME a, final type.TIME b) {
    return new BetweenPredicate<LocalTime>(true, dataType, a, b);
  }

  public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final type.Numeric<? extends T> a, final T b) {
    return new BetweenPredicate<T>(true, dataType, a, type.DataType.wrap(b));
  }

  public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final type.Textual<? super T> a, final T b) {
    return new BetweenPredicate<T>(true, dataType, a, type.DataType.wrap(b));
  }

  public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final type.Temporal<? extends T> a, final T b) {
    return new BetweenPredicate<T>(true, dataType, a, type.DataType.wrap(b));
  }

  public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final type.TIME a, final LocalTime b) {
    return new BetweenPredicate<LocalTime>(true, dataType, a, type.DataType.wrap(b));
  }

  public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final T a, final type.Numeric<? extends T> b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), b);
  }

  public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final T a, final type.Textual<? super T> b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), b);
  }

  public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final T a, final type.Temporal<? extends T> b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), b);
  }

  public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final LocalTime a, final type.TIME b) {
    return new BetweenPredicate<LocalTime>(true, dataType, type.DataType.wrap(a), b);
  }

  public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final T a, final T b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
  }

  public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final T a, final T b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
  }

  public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final T a, final T b) {
    return new BetweenPredicate<T>(true, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
  }

  public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final LocalTime a, final LocalTime b) {
    return new BetweenPredicate<LocalTime>(true, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
  }

  public static Predicate<String> LIKE(final type.CHAR a, final CharSequence b) {
    return new LikePredicate(true, a, b);
  }

  public static <T>Predicate<T> IN(final type.DataType<T> a, final Collection<T> b) {
    return new InPredicate<T>(true, a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final type.DataType<T> a, final T ... b) {
    return new InPredicate<T>(true, a, b);
  }

  public static <T>Predicate<T> IN(final type.DataType<T> a, final select.SELECT<? extends type.DataType<T>> b) {
    return new InPredicate<T>(true, a, b);
  }

  public static <T>Predicate<T> EXISTS(final select.SELECT<?> subQuery) {
    return new ExistsPredicate<T>(subQuery);
  }

  public static final class NOT {
    public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final type.Numeric<? extends T> a, final type.Numeric<? extends T> b) {
      return new BetweenPredicate<T>(false, dataType, a, b);
    }

    public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final type.Textual<? super T> a, final type.Textual<? super T> b) {
      return new BetweenPredicate<T>(false, dataType, a, b);
    }

    public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final type.Temporal<? extends T> a, final type.Temporal<? extends T> b) {
      return new BetweenPredicate<T>(false, dataType, a, b);
    }

    public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final type.TIME a, final type.TIME b) {
      return new BetweenPredicate<LocalTime>(false, dataType, a, b);
    }

    public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final type.Numeric<? extends T> a, final T b) {
      return new BetweenPredicate<T>(false, dataType, a, type.DataType.wrap(b));
    }

    public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final type.Textual<? super T> a, final T b) {
      return new BetweenPredicate<T>(false, dataType, a, type.DataType.wrap(b));
    }

    public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final type.Temporal<? extends T> a, final T b) {
      return new BetweenPredicate<T>(false, dataType, a, type.DataType.wrap(b));
    }

    public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final type.TIME a, final LocalTime b) {
      return new BetweenPredicate<LocalTime>(false, dataType, a, type.DataType.wrap(b));
    }

    public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final T a, final type.Numeric<? extends T> b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), b);
    }

    public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final T a, final type.Textual<? super T> b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), b);
    }

    public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final T a, final type.Temporal<? extends T> b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), b);
    }

    public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final LocalTime a, final type.TIME b) {
      return new BetweenPredicate<LocalTime>(false, dataType, type.DataType.wrap(a), b);
    }

    public static <T extends Number>Predicate<T> BETWEEN(final type.Numeric<? extends T> dataType, final T a, final T b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
    }

    public static <T>Predicate<T> BETWEEN(final type.Textual<? super T> dataType, final T a, final T b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
    }

    public static <T extends Temporal>Predicate<T> BETWEEN(final type.Temporal<? extends T> dataType, final T a, final T b) {
      return new BetweenPredicate<T>(false, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
    }

    public static Predicate<LocalTime> BETWEEN(final type.TIME dataType, final LocalTime a, final LocalTime b) {
      return new BetweenPredicate<LocalTime>(false, dataType, type.DataType.wrap(a), type.DataType.wrap(b));
    }

    public static Predicate<String> LIKE(final type.CHAR a, final String b) {
      return new LikePredicate(false, a, b);
    }

    public static <T>Predicate<T> IN(final type.DataType<T> a, final Collection<T> b) {
      return new InPredicate<T>(true, a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final type.DataType<T> a, final T ... b) {
      return new InPredicate<T>(false, a, b);
    }

    public static <T>Predicate<T> IN(final type.DataType<T> a, final select.SELECT<? extends type.DataType<T>> b) {
      return new InPredicate<T>(false, a, b);
    }
  }

  public static final class IS {
    public static final class NOT {
      public static <T>Predicate<T> NULL(final type.DataType<T> dataType) {
        return new NullPredicate<T>(false, dataType);
      }
    }

    public static <T>Predicate<T> NULL(final type.DataType<T> dataType) {
      return new NullPredicate<T>(true, dataType);
    }
  }

  private DML() {
  }
}