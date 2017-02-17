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
import org.safris.xdb.entities.model.case_;
import org.safris.xdb.entities.model.delete;
import org.safris.xdb.entities.model.insert;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.entities.model.update;
import org.safris.xdb.xdd.xe.$xdd_data;

@SuppressWarnings("hiding")
public final class DML {

  /** Ordering Specification **/

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V ASC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(Operator.ASC, dataType));
  }

  @SuppressWarnings("unchecked")
  public static <V extends type.DataType<T>,T>V DESC(final V dataType) {
    return (V)dataType.clone().wrapper(new OrderingSpec(Operator.DESC, dataType));
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

  public static Cast.INT CAST(final type.INT a) {
    return new Cast.INT(a);
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

  public static final class CASE {
    public static <T>case_.search.WHEN<T> WHEN(final Condition<T> condition) {
      return new Case.Search.WHEN<T>(null, condition);
    }
  }

  public static case_.simple.CASE<byte[]> CASE(final type.BINARY binary) {
    return new Case.Simple.CASE<byte[],type.BINARY>(binary);
  }

  public static case_.simple.CASE<Boolean> CASE(final type.BOOLEAN bool) {
    return new Case.Simple.CASE<Boolean,type.BOOLEAN>(bool);
  }

  public static <T extends Temporal>case_.simple.CASE<T> CASE(final type.Temporal<T> temporal) {
    return new Case.Simple.CASE<T,type.Temporal<T>>(temporal);
  }

  public static <T>case_.simple.CASE<T> CASE(final type.Textual<T> textual) {
    return new Case.Simple.CASE<T,type.Textual<T>>(textual);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Number>case_.simple.CASE<T> CASE(final type.Numeric<?> numeric) {
    return new Case.Simple.CASE<T,type.Numeric<T>>((type.Numeric<T>)numeric);
  }

  /** DELETE **/

  public static update.UPDATE_SET UPDATE(final Entity entity) {
    return new Update.UPDATE(entity);
  }

  public static update.UPDATE UPDATE(final Entity entity, final Entity ... entities) {
    return new Update.UPDATE(Arrays.splice(entities, 0, 0, entity));
  }

  public static delete.DELETE_WHERE DELETE(final Entity entity) {
    return new Delete.DELETE(entity);
  }

  public static delete.DELETE DELETE(final Entity entity, final Entity ... entities) {
    return new Delete.DELETE(Arrays.splice(entities, 0, 0, entity));
  }

  /** INSERT **/

  public static <E extends Entity>insert.INSERT_VALUES<E> INSERT(final E entity) {
    return new Insert.INSERT<E>(entity);
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <E extends Entity>insert.INSERT<E> INSERT(final E entity, final E ... entities) {
    return new Insert.INSERT<E>(Arrays.splice(entities, 0, 0, entity));
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<?>>insert.INSERT_VALUES<DataType> INSERT(final DataType column, final DataType ... columns) {
    return new Insert.INSERT<DataType>(Arrays.splice(columns, 0, 0, column));
  }

  public static insert.INSERT<?> INSERT(final $xdd_data data) {
    return new Insert.INSERT<Subject<?>>(Entities.toEntities(data));
  }

  /** String Functions **/

  public static type.CHAR CONCAT(final type.CHAR a, final type.CHAR b) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.CHAR b, final CharSequence c) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.CHAR c, final CharSequence d) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b, final CharSequence c) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.ENUM<?> c, final CharSequence d) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final type.ENUM<?> c, final CharSequence d) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b, final CharSequence c) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b, final type.ENUM<?> c, final CharSequence d) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c, final type.ENUM<?> d, final CharSequence e) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b, final CharSequence c) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b, final type.CHAR c, final CharSequence d) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final type.CHAR c, final CharSequence d) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c, final type.CHAR d, final CharSequence e) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
  }

  public static type.CHAR CONCAT(final type.CHAR a, final CharSequence b) {
    return (type.CHAR)a.clone().wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.CHAR b, final CharSequence c) {
    return (type.CHAR)b.clone().wrapper(new StringExpression(Operator.CONCAT, a, b, c));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final type.ENUM<?> a, final CharSequence b) {
    return (type.CHAR)new type.CHAR(a.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b));
  }

  public static type.CHAR CONCAT(final CharSequence a, final type.ENUM<?> b, final CharSequence c) {
    return (type.CHAR)new type.CHAR(b.length(), false).wrapper(new StringExpression(Operator.CONCAT, a, b, c));
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

  public static type.SMALLINT SIGN(final type.Numeric<?> a) {
    return (type.SMALLINT)new type.SMALLINT(1).wrapper(new function.numeric.Sign(a));
  }

  public static final type.FLOAT ROUND(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DOUBLE ROUND(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DOUBLE ROUND(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DECIMAL ROUND(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.DECIMAL.UNSIGNED ROUND(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Round(a, 0));
  }

  public static final type.FLOAT ROUND(final type.FLOAT a, final int scale) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.FLOAT.UNSIGNED a, final int scale) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DOUBLE ROUND(final type.DOUBLE a, final int scale) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.DOUBLE.UNSIGNED a, final int scale) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final type.SMALLINT a, final int scale) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.SMALLINT.UNSIGNED a, final int scale) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final type.MEDIUMINT a, final int scale) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT.UNSIGNED ROUND(final type.MEDIUMINT.UNSIGNED a, final int scale) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT ROUND(final type.INT a, final int scale) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.INT.UNSIGNED a, final int scale) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DOUBLE ROUND(final type.BIGINT a, final int scale) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DOUBLE.UNSIGNED ROUND(final type.BIGINT.UNSIGNED a, final int scale) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DECIMAL ROUND(final type.DECIMAL a, final int scale) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.DECIMAL.UNSIGNED ROUND(final type.DECIMAL.UNSIGNED a, final int scale) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Round(a, scale));
  }

  public static final type.FLOAT ABS(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DOUBLE ABS(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT ABS(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT ABS(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT.UNSIGNED ABS(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT ABS(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DOUBLE ABS(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DOUBLE.UNSIGNED ABS(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Abs(a));
  }

  public static final type.DECIMAL ABS(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Abs(a));
  }

  public static final type.DECIMAL.UNSIGNED ABS(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Abs(a));
  }

  public static final type.FLOAT FLOOR(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT.UNSIGNED FLOOR(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DOUBLE FLOOR(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DOUBLE.UNSIGNED FLOOR(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT FLOOR(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT.UNSIGNED FLOOR(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT FLOOR(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT.UNSIGNED FLOOR(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT FLOOR(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DOUBLE.UNSIGNED FLOOR(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DOUBLE FLOOR(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DOUBLE.UNSIGNED FLOOR(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Floor(a));
  }

  public static final type.DECIMAL FLOOR(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Floor(a));
  }

  public static final type.DECIMAL.UNSIGNED FLOOR(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Floor(a));
  }

  public static final type.FLOAT CEIL(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT.UNSIGNED CEIL(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DOUBLE CEIL(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DOUBLE.UNSIGNED CEIL(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT CEIL(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT.UNSIGNED CEIL(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT CEIL(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT.UNSIGNED CEIL(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT CEIL(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DOUBLE.UNSIGNED CEIL(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DOUBLE CEIL(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DOUBLE.UNSIGNED CEIL(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DECIMAL CEIL(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Ceil(a));
  }

  public static final type.DECIMAL.UNSIGNED CEIL(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Ceil(a));
  }

  public static final type.FLOAT SQRT(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DOUBLE SQRT(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT.UNSIGNED SQRT(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT SQRT(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DOUBLE SQRT(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DOUBLE.UNSIGNED SQRT(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DECIMAL SQRT(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.DECIMAL.UNSIGNED SQRT(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Sqrt(a));
  }

  public static final type.FLOAT EXP(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DOUBLE EXP(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT EXP(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT EXP(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT.UNSIGNED EXP(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT EXP(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DOUBLE EXP(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DOUBLE.UNSIGNED EXP(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Exp(a));
  }

  public static final type.DECIMAL EXP(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Exp(a));
  }

  public static final type.DECIMAL.UNSIGNED EXP(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Exp(a));
  }

  public static final type.FLOAT LN(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DOUBLE LN(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT LN(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT LN(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT.UNSIGNED LN(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT LN(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DOUBLE LN(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DOUBLE.UNSIGNED LN(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Ln(a));
  }

  public static final type.DECIMAL LN(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Ln(a));
  }

  public static final type.DECIMAL.UNSIGNED LN(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Ln(a));
  }

  public static final type.FLOAT LOG2(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DOUBLE LOG2(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT LOG2(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT LOG2(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT.UNSIGNED LOG2(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT LOG2(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DOUBLE LOG2(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG2(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log2(a));
  }

  public static final type.DECIMAL LOG2(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log2(a));
  }

  public static final type.DECIMAL.UNSIGNED LOG2(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log2(a));
  }

  public static final type.FLOAT LOG10(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DOUBLE LOG10(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT LOG10(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT LOG10(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT.UNSIGNED LOG10(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT LOG10(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DOUBLE LOG10(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DOUBLE.UNSIGNED LOG10(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log10(a));
  }

  public static final type.DECIMAL LOG10(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log10(a));
  }

  public static final type.DECIMAL.UNSIGNED LOG10(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log10(a));
  }

  public static final type.FLOAT SIN(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DOUBLE SIN(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT SIN(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT SIN(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT.UNSIGNED SIN(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT SIN(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DOUBLE SIN(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DOUBLE.UNSIGNED SIN(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Sin(a));
  }

  public static final type.DECIMAL SIN(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Sin(a));
  }

  public static final type.DECIMAL.UNSIGNED SIN(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Sin(a));
  }

  public static final type.FLOAT ASIN(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DOUBLE ASIN(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT ASIN(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT ASIN(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT.UNSIGNED ASIN(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT ASIN(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DOUBLE ASIN(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DOUBLE.UNSIGNED ASIN(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Asin(a));
  }

  public static final type.DECIMAL ASIN(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Asin(a));
  }

  public static final type.DECIMAL.UNSIGNED ASIN(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Asin(a));
  }

  public static final type.FLOAT COS(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DOUBLE COS(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT COS(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT COS(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT.UNSIGNED COS(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT COS(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DOUBLE COS(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DOUBLE.UNSIGNED COS(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Cos(a));
  }

  public static final type.DECIMAL COS(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Cos(a));
  }

  public static final type.DECIMAL.UNSIGNED COS(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Cos(a));
  }

  public static final type.FLOAT ACOS(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DOUBLE ACOS(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT ACOS(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT ACOS(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT.UNSIGNED ACOS(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT ACOS(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DOUBLE ACOS(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DOUBLE.UNSIGNED ACOS(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Acos(a));
  }

  public static final type.DECIMAL ACOS(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Acos(a));
  }

  public static final type.DECIMAL.UNSIGNED ACOS(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Acos(a));
  }

  public static final type.FLOAT TAN(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DOUBLE TAN(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT TAN(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT TAN(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT.UNSIGNED TAN(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT TAN(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DOUBLE TAN(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DOUBLE.UNSIGNED TAN(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Tan(a));
  }

  public static final type.DECIMAL TAN(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Tan(a));
  }

  public static final type.DECIMAL.UNSIGNED TAN(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Tan(a));
  }

  public static final type.FLOAT ATAN(final type.FLOAT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final type.FLOAT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DOUBLE ATAN(final type.DOUBLE a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final type.DOUBLE.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT ATAN(final type.SMALLINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final type.SMALLINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT ATAN(final type.MEDIUMINT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT.UNSIGNED ATAN(final type.MEDIUMINT.UNSIGNED a) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.FLOAT ATAN(final type.INT a) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final type.INT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DOUBLE ATAN(final type.BIGINT a) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DOUBLE.UNSIGNED ATAN(final type.BIGINT.UNSIGNED a) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan(a));
  }

  public static final type.DECIMAL ATAN(final type.DECIMAL a) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan(a));
  }

  public static final type.DECIMAL.UNSIGNED ATAN(final type.DECIMAL.UNSIGNED a) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan(a));
  }

  /** End Math Functions (1 parameter) **/

  /** Start Math Functions (2 parameter) **/

  public static final type.FLOAT POW(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final int b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.FLOAT a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final float b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final int a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL.UNSIGNED POW(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT.UNSIGNED POW(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE.UNSIGNED POW(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.SMALLINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.BIGINT a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final type.INT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DOUBLE POW(final long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT POW(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.DECIMAL POW(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Pow(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final int b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.FLOAT a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final float b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final int a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL.UNSIGNED MOD(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT.UNSIGNED MOD(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE.UNSIGNED MOD(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.SMALLINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.BIGINT a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final type.INT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DOUBLE MOD(final long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT MOD(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.DECIMAL MOD(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Mod(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final int b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.FLOAT a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final float b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final int a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL.UNSIGNED LOG(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT.UNSIGNED LOG(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE.UNSIGNED LOG(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.SMALLINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.BIGINT a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final type.INT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DOUBLE LOG(final long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT LOG(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.DECIMAL LOG(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Log(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final int b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.FLOAT a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final float b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final int a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL.UNSIGNED ATAN2(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT.UNSIGNED ATAN2(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE.UNSIGNED ATAN2(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.SMALLINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.BIGINT a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final type.INT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DOUBLE ATAN2(final long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.FLOAT ATAN2(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  public static final type.DECIMAL ATAN2(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new function.numeric.Atan2(a, b));
  }

  /** End Math Functions (2 parameters) **/

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal ADD(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new TemporalExpression(Operator.PLUS, a, interval));
  }

  @SuppressWarnings("unchecked")
  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal SUB(final Temporal a, final Interval interval) {
    return (Temporal)a.clone().wrapper(new TemporalExpression(Operator.MINUS, a, interval));
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal PLUS(final Temporal a, final Interval interval) {
    return ADD(a, interval);
  }

  public static <Temporal extends type.Temporal<T>,T extends java.time.temporal.Temporal>Temporal MINUS(final Temporal a, final Interval interval) {
    return SUB(a, interval);
  }

  /** Start Numeric Expressions **/

  public static final type.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() && b.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.MEDIUMINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final type.SMALLINT.UNSIGNED a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.INT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final type.SMALLINT a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final byte a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.INT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.SMALLINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.SMALLINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final short a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.SMALLINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.SMALLINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.FLOAT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.SMALLINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.SMALLINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final UNSIGNED.Integer a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED ADD(final UNSIGNED.Byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED ADD(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.MEDIUMINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final int a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final short a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.MEDIUMINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT ADD(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED ADD(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED ADD(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.INT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED ADD(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED ADD(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.SMALLINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.BIGINT a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT ADD(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final type.INT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DOUBLE ADD(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.BIGINT ADD(final long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.FLOAT ADD(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.DECIMAL ADD(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.PLUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() && b.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.MEDIUMINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final type.SMALLINT.UNSIGNED a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.INT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final type.SMALLINT a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final byte a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.INT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.SMALLINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.SMALLINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final short a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.SMALLINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.SMALLINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.FLOAT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.SMALLINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.SMALLINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final UNSIGNED.Integer a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.SMALLINT.UNSIGNED SUB(final UNSIGNED.Byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT.UNSIGNED SUB(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.MEDIUMINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final int a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final short a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.MEDIUMINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT SUB(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL.UNSIGNED SUB(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED SUB(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.INT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED SUB(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE.UNSIGNED SUB(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.SMALLINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.BIGINT a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT SUB(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final type.INT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DOUBLE SUB(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.BIGINT SUB(final long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.FLOAT SUB(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.DECIMAL SUB(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MINUS, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() && b.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.MEDIUMINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final type.SMALLINT.UNSIGNED a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.INT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final type.SMALLINT a, final byte b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final byte a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT)new type.SMALLINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.INT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.SMALLINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.SMALLINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final short a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.SMALLINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.SMALLINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.FLOAT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.SMALLINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.SMALLINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.SMALLINT.UNSIGNED a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final short a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Integer b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final UNSIGNED.Integer a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Byte b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.SMALLINT.UNSIGNED MUL(final UNSIGNED.Byte a, final type.SMALLINT.UNSIGNED b) {
    return (type.SMALLINT.UNSIGNED)new type.SMALLINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT.UNSIGNED MUL(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT.UNSIGNED)new type.BIGINT.UNSIGNED(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.MEDIUMINT a, final int b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final int a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT a, final short b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final short a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.MEDIUMINT a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT MUL(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT)new type.MEDIUMINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL.UNSIGNED MUL(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.MEDIUMINT.UNSIGNED MUL(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.MEDIUMINT.UNSIGNED)new type.MEDIUMINT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.INT a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.INT b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT.UNSIGNED MUL(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT.UNSIGNED)new type.INT.UNSIGNED(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE.UNSIGNED MUL(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.SMALLINT a, final type.BIGINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.BIGINT a, final type.SMALLINT b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.INT MUL(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.INT)new type.INT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final type.INT.UNSIGNED a, final long b) {
    return (type.BIGINT)new type.BIGINT(a.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DOUBLE MUL(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.BIGINT MUL(final long a, final type.INT.UNSIGNED b) {
    return (type.BIGINT)new type.BIGINT(b.precision()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT MUL(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.DECIMAL MUL(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.MULTIPLY, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.FLOAT b) {
    return (type.FLOAT)(a.unsigned() && b.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.INT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.Double a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.MEDIUMINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.INT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.INT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DOUBLE a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.DOUBLE b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.SMALLINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.BIGINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.BIGINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.FLOAT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.FLOAT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final long b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final long a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final double b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final double a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.Long a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.SMALLINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.MEDIUMINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final type.DOUBLE b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.FLOAT b) {
    return (type.DOUBLE)(a.unsigned() && b.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final float b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.INT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.INT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.Short b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Short a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.INT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.FLOAT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.FLOAT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Float b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final UNSIGNED.Float a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)(a.unsigned() ? new type.FLOAT.UNSIGNED() : new type.FLOAT()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.SMALLINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.SMALLINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DOUBLE a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.DOUBLE b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.BIGINT a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.DOUBLE.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final int b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.FLOAT a, final type.DECIMAL b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.FLOAT b) {
    return (type.DECIMAL)(a.unsigned() && b.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.BIGINT.UNSIGNED a, final type.DECIMAL b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT.UNSIGNED b) {
    return (type.DECIMAL)(a.unsigned() ? new type.DECIMAL.UNSIGNED(a.precision(), a.scale()) : new type.DECIMAL(a.precision(), a.scale())).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DOUBLE.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.DOUBLE.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE)(a.unsigned() ? new type.DOUBLE.UNSIGNED() : new type.DOUBLE()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT.UNSIGNED a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.BIGINT.UNSIGNED a, final UNSIGNED.Long b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Long a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final float b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final int b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final float a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.FLOAT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final int a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.FLOAT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.FLOAT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.DOUBLE a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.DOUBLE b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.SMALLINT.UNSIGNED a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.DECIMAL.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.DECIMAL.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT.UNSIGNED a, final type.MEDIUMINT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.MEDIUMINT a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final UNSIGNED.BigDecimal b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL.UNSIGNED DIV(final UNSIGNED.BigDecimal a, final type.SMALLINT.UNSIGNED b) {
    return (type.DECIMAL.UNSIGNED)new type.DECIMAL.UNSIGNED(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final type.BIGINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT.UNSIGNED a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT.UNSIGNED DIV(final type.MEDIUMINT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT.UNSIGNED)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT a, final long b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.SMALLINT.UNSIGNED a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final type.SMALLINT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT a, final double b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.INT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final type.INT.UNSIGNED a, final UNSIGNED.Double b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE.UNSIGNED DIV(final UNSIGNED.Double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE.UNSIGNED)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.SMALLINT a, final type.BIGINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.BIGINT a, final type.SMALLINT b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT.UNSIGNED a, final type.INT b) {
    return (type.FLOAT)new type.FLOAT.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.INT a, final type.SMALLINT.UNSIGNED b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.MEDIUMINT.UNSIGNED a, final double b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final type.INT.UNSIGNED a, final long b) {
    return (type.DOUBLE)new type.DOUBLE.UNSIGNED().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final double a, final type.MEDIUMINT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DOUBLE DIV(final long a, final type.INT.UNSIGNED b) {
    return (type.DOUBLE)new type.DOUBLE().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.FLOAT a, final type.SMALLINT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.FLOAT DIV(final type.SMALLINT a, final type.FLOAT b) {
    return (type.FLOAT)new type.FLOAT().wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final type.MEDIUMINT a, final BigDecimal b) {
    return (type.DECIMAL)new type.DECIMAL(a.precision(), a.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  public static final type.DECIMAL DIV(final BigDecimal a, final type.MEDIUMINT b) {
    return (type.DECIMAL)new type.DECIMAL(b.precision(), b.scale()).wrapper(new NumericExpression(Operator.DIVIDE, a, b));
  }

  /** End Numeric Expressions **/

  /** Start Aggregates **/

  public static type.INT COUNT() {
    return (type.INT)new type.INT(10).wrapper(CountFunction.STAR);
  }

  public static type.INT COUNT(final type.DataType<?> dataType) {
    return (type.INT)new type.INT(10).wrapper(new CountFunction(dataType, false));
  }

  public final static class COUNT {
    public static type.INT DISTINCT(final type.DataType<?> dataType) {
      return (type.INT)new type.INT(10).wrapper(new CountFunction(dataType, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric SUM(final Numeric a) {
    return (Numeric)a.clone().wrapper(new SetFunction("SUM", a, false));
  }

  public static final class SUM {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new SetFunction("SUM", a, true));
    }
  }

  // DT shall not be character string, bit string, or datetime.
  @SuppressWarnings("unchecked")
  public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric AVG(final Numeric a) {
    return (Numeric)a.clone().wrapper(new SetFunction("AVG", a, false));
  }

  public static final class AVG {
    @SuppressWarnings("unchecked")
    public static <Numeric extends type.Numeric<T>,T extends java.lang.Number>Numeric DISTINCT(final Numeric a) {
      return (Numeric)a.clone().wrapper(new SetFunction("AVG", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MAX(final DataType a) {
    return (DataType)a.clone().wrapper(new SetFunction("MAX", a, false));
  }

  public static final class MAX {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new SetFunction("MAX", a, true));
    }
  }

  @SuppressWarnings("unchecked")
  public static <DataType extends type.DataType<T>,T>DataType MIN(final DataType a) {
    return (DataType)a.clone().wrapper(new SetFunction("MIN", a, false));
  }

  public static final class MIN {
    @SuppressWarnings("unchecked")
    public static <DataType extends type.DataType<T>,T>DataType DISTINCT(final DataType a) {
      return (DataType)a.clone().wrapper(new SetFunction("MIN", a, true));
    }
  }

  /** End Aggregates **/

  private static class NOW extends type.DATETIME {
    protected NOW() {
      super(10);
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
      super();
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