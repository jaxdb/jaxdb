/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collection;

import org.jaxdb.jsql.Command.CaseImpl;
import org.jaxdb.jsql.keyword.Case;
import org.jaxdb.jsql.keyword.Cast;
import org.jaxdb.jsql.keyword.Delete;
import org.jaxdb.jsql.keyword.Insert;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.jsql.keyword.Update;
import org.libj.util.ArrayUtil;

@SuppressWarnings("unchecked")
public final class DML {
  /* START Ordering Specification */

  public static <D extends data.Column<V>,V>D ASC(final D column) { return (D)column.clone().wrap(new OrderingSpec(true, column)); }
  public static <D extends data.Column<V>,V>D DESC(final D column) { return (D)column.clone().wrap(new OrderingSpec(false, column)); }

  /* END Ordering Specification */

  /* START NOT Specification */

  public static Predicate NOT(final type.Column<?> column) { return new NotPredicate(column); }
  public static Predicate NOT(final Select.untyped.SELECT<?> subQuery) { return new NotPredicate(subQuery); }

  /* END NOT Specification */

  /* START Cast */

  public static Cast.BIGINT CAST(final type.BIGINT a) { return new Cast.BIGINT(a); }
  public static Cast.BINARY CAST(final type.BINARY a) { return new Cast.BINARY(a); }
  public static Cast.BLOB CAST(final type.BLOB a) { return new Cast.BLOB(a); }
  public static Cast.BOOLEAN CAST(final type.BOOLEAN a) { return new Cast.BOOLEAN(a); }
  public static Cast.CLOB CAST(final type.CLOB a) { return new Cast.CLOB(a); }
  public static Cast.DATE CAST(final type.DATE a) { return new Cast.DATE(a); }
  public static Cast.DATETIME CAST(final type.DATETIME a) { return new Cast.DATETIME(a); }
  public static Cast.DECIMAL CAST(final type.DECIMAL a) { return new Cast.DECIMAL(a); }
  public static Cast.DOUBLE CAST(final type.DOUBLE a) { return new Cast.DOUBLE(a); }
  public static Cast.FLOAT CAST(final type.FLOAT a) { return new Cast.FLOAT(a); }
  public static Cast.INT CAST(final type.INT a) { return new Cast.INT(a); }
  public static Cast.SMALLINT CAST(final type.SMALLINT a) { return new Cast.SMALLINT(a); }
  public static Cast.CHAR CAST(final type.Textual<?> a) { return new Cast.CHAR(a); }
  public static Cast.TIME CAST(final type.TIME a) { return new Cast.TIME(a); }
  public static Cast.TINYINT CAST(final type.TINYINT a) { return new Cast.TINYINT(a); }

  /* END Cast */

  /* START ComparisonPredicate */

  public static data.BOOLEAN EQ(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final V a, final type.CHAR b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static data.BOOLEAN EQ(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <Textual extends CharSequence & Comparable<?>>data.BOOLEAN EQ(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Eq<Textual>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.CHAR a, final V b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static data.BOOLEAN EQ(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Eq<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Eq<String>(a, b); }
  public static data.BOOLEAN EQ(final type.CHAR a, final String b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Eq<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Eq<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Eq<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN EQ(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Eq<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Eq<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Eq<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN EQ(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN EQ(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Eq<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN EQ(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static data.BOOLEAN EQ(final String a, final type.CHAR b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN EQ(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN EQ(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Eq<>(a, b); }
  public static data.BOOLEAN GT(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final V a, final type.CHAR b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static data.BOOLEAN GT(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN GT(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.CHAR a, final V b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static data.BOOLEAN GT(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Gt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Gt<String>(a, b); }
  public static data.BOOLEAN GT(final type.CHAR a, final String b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Gt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Gt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GT(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GT(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN GT(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gt<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GT(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static data.BOOLEAN GT(final String a, final type.CHAR b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GT(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GT(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Gt<>(a, b); }
  public static data.BOOLEAN GTE(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final V a, final type.CHAR b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static data.BOOLEAN GTE(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN GTE(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.CHAR a, final V b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static data.BOOLEAN GTE(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Gte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Gte<String>(a, b); }
  public static data.BOOLEAN GTE(final type.CHAR a, final String b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Gte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Gte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GTE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GTE(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN GTE(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Gte<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN GTE(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static data.BOOLEAN GTE(final String a, final type.CHAR b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN GTE(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN GTE(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Gte<>(a, b); }
  public static data.BOOLEAN LT(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final V a, final type.CHAR b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static data.BOOLEAN LT(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN LT(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.CHAR a, final V b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static data.BOOLEAN LT(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Lt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Lt<String>(a, b); }
  public static data.BOOLEAN LT(final type.CHAR a, final String b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Lt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Lt<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LT(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LT(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN LT(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lt<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LT(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static data.BOOLEAN LT(final String a, final type.CHAR b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LT(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LT(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Lt<>(a, b); }
  public static data.BOOLEAN LTE(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final V a, final type.CHAR b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static data.BOOLEAN LTE(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN LTE(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.CHAR a, final V b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static data.BOOLEAN LTE(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Lte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Lte<String>(a, b); }
  public static data.BOOLEAN LTE(final type.CHAR a, final String b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Lte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Lte<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LTE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LTE(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN LTE(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Lte<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN LTE(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static data.BOOLEAN LTE(final String a, final type.CHAR b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN LTE(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN LTE(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Lte<>(a, b); }
  public static data.BOOLEAN NE(final boolean a, final type.BOOLEAN b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final V a, final type.CHAR b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final V a, final type.ENUM<V> b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static data.BOOLEAN NE(final type.BOOLEAN a, final boolean b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN NE(final type.BOOLEAN a, final type.BOOLEAN b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.CHAR a, final V b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static data.BOOLEAN NE(final type.CHAR a, final type.CHAR b) { return new ComparisonPredicate.Ne<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.CHAR a, final type.ENUM<V> b) { return new ComparisonPredicate.Ne<String>(a, b); }
  public static data.BOOLEAN NE(final type.CHAR a, final String b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.ENUM<V> a, final V b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.ENUM<V> a, final type.CHAR b) { return new ComparisonPredicate.Ne<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.ENUM<V> a, final type.ENUM<V> b) { return new ComparisonPredicate.Ne<String>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final type.ENUM<V> a, final String b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final type.Numeric<?> b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final V b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN NE(final type.Numeric<?> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<V> a, final type.Temporal<V> b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN NE(final type.Temporal<V> a, final V b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends CharSequence & Comparable<?>>data.BOOLEAN NE(final type.Textual<V> a, final QuantifiedComparisonPredicate<? extends V> b) { return new ComparisonPredicate.Ne<V>(a, b); }
  public static <V extends java.lang.Number>data.BOOLEAN NE(final V a, final type.Numeric<?> b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static data.BOOLEAN NE(final String a, final type.CHAR b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends EntityEnum>data.BOOLEAN NE(final String a, final type.ENUM<V> b) { return new ComparisonPredicate.Ne<>(a, b); }
  public static <V extends java.time.temporal.Temporal>data.BOOLEAN NE(final V a, final type.Temporal<V> b) { return new ComparisonPredicate.Ne<>(a, b); }

  /* END ComparisonPredicate */

  /* SELECT */

  @SafeVarargs public static <V>Select.ARRAY._SELECT<data.ARRAY<V>> SELECT(final type.ARRAY<? extends V> ... entities) { return new Command.Select.ARRAY.SELECT<>(false, entities); }
  @SafeVarargs public static Select.BIGINT._SELECT<data.BIGINT> SELECT(final type.BIGINT ... entities) { return new Command.Select.BIGINT.SELECT<>(false, entities); }
  @SafeVarargs public static Select.BINARY._SELECT<data.BINARY> SELECT(final type.BINARY ... entities) { return new Command.Select.BINARY.SELECT<>(false, entities); }
  @SafeVarargs public static Select.BLOB._SELECT<data.BLOB> SELECT(final type.BLOB ... entities) { return new Command.Select.BLOB.SELECT<>(false, entities); }
  @SafeVarargs public static Select.BOOLEAN._SELECT<data.BOOLEAN> SELECT(final type.BOOLEAN ... entities) { return new Command.Select.BOOLEAN.SELECT<>(false, entities); }
  @SafeVarargs public static Select.CHAR._SELECT<data.CHAR> SELECT(final type.CHAR ... entities) { return new Command.Select.CHAR.SELECT<>(false, entities); }
  @SafeVarargs public static Select.CLOB._SELECT<data.CLOB> SELECT(final type.CLOB ... entities) { return new Command.Select.CLOB.SELECT<>(false, entities); }
  @SafeVarargs public static <V>Select.Column._SELECT<data.Column<V>> SELECT(final type.Column<? extends V> ... entities) { return new Command.Select.Column.SELECT<>(false, entities); }
  @SafeVarargs public static Select.DATE._SELECT<data.DATE> SELECT(final type.DATE ... entities) { return new Command.Select.DATE.SELECT<>(false, entities); }
  @SafeVarargs public static Select.DATETIME._SELECT<data.DATETIME> SELECT(final type.DATETIME ... entities) { return new Command.Select.DATETIME.SELECT<>(false, entities); }
  @SafeVarargs public static Select.DECIMAL._SELECT<data.DECIMAL> SELECT(final type.DECIMAL ... entities) { return new Command.Select.DECIMAL.SELECT<>(false, entities); }
  @SafeVarargs public static Select.DOUBLE._SELECT<data.DOUBLE> SELECT(final type.DOUBLE ... entities) { return new Command.Select.DOUBLE.SELECT<>(false, entities); }
  @SafeVarargs public static <V extends EntityEnum>Select.ENUM._SELECT<data.ENUM<V>> SELECT(final type.ENUM<? extends V> ... entities) { return new Command.Select.ENUM.SELECT<>(false, entities); }
  @SafeVarargs public static Select.FLOAT._SELECT<data.FLOAT> SELECT(final type.FLOAT ... entities) { return new Command.Select.FLOAT.SELECT<>(false, entities); }
  @SafeVarargs public static Select.INT._SELECT<data.INT> SELECT(final type.INT ... entities) { return new Command.Select.INT.SELECT<>(false, entities); }
  @SafeVarargs public static <V extends Number>Select.Numeric._SELECT<data.Numeric<V>> SELECT(final type.Numeric<? extends V> ... entities) { return new Command.Select.Numeric.SELECT<>(false, entities); }
  @SafeVarargs public static Select.SMALLINT._SELECT<data.SMALLINT> SELECT(final type.SMALLINT ... entities) { return new Command.Select.SMALLINT.SELECT<>(false, entities); }
  @SafeVarargs public static <V extends java.time.temporal.Temporal>Select.Temporal._SELECT<data.Temporal<V>> SELECT(final type.Temporal<? extends V> ... entities) { return new Command.Select.Temporal.SELECT<>(false, entities); }
  @SafeVarargs public static <V extends CharSequence & Comparable<?>>Select.Textual._SELECT<data.Textual<V>> SELECT(final type.Textual<? extends V> ... entities) { return new Command.Select.Textual.SELECT<>(false, entities); }
  @SafeVarargs public static Select.TIME._SELECT<data.TIME> SELECT(final type.TIME ... entities) { return new Command.Select.TIME.SELECT<>(false, entities); }
  @SafeVarargs public static Select.TINYINT._SELECT<data.TINYINT> SELECT(final type.TINYINT ... entities) { return new Command.Select.TINYINT.SELECT<>(false, entities); }
  @SafeVarargs public static Select.Entity._SELECT<type.Entity> SELECT(final type.Entity ... entities) { return new Command.Select.Entity.SELECT<>(false, entities); }
  @SafeVarargs public static <D extends data.Table>Select.Entity._SELECT<D> SELECT(final D ... tables) { return new Command.Select.Entity.SELECT<>(false, tables); }

  public static final class SELECT {
    private SELECT() {}

    @SafeVarargs public static <V>Select.ARRAY._SELECT<data.ARRAY<V>> DISTINCT(final type.ARRAY<? extends V> ... entities) { return new Command.Select.ARRAY.SELECT<>(true, entities); }
    @SafeVarargs public static Select.BIGINT._SELECT<data.BIGINT> DISTINCT(final type.BIGINT ... entities) { return new Command.Select.BIGINT.SELECT<>(true, entities); }
    @SafeVarargs public static Select.BINARY._SELECT<data.BINARY> DISTINCT(final type.BINARY ... entities) { return new Command.Select.BINARY.SELECT<>(true, entities); }
    @SafeVarargs public static Select.BLOB._SELECT<data.BLOB> DISTINCT(final type.BLOB ... entities) { return new Command.Select.BLOB.SELECT<>(true, entities); }
    @SafeVarargs public static Select.BOOLEAN._SELECT<data.BOOLEAN> DISTINCT(final type.BOOLEAN ... entities) { return new Command.Select.BOOLEAN.SELECT<>(true, entities); }
    @SafeVarargs public static Select.CHAR._SELECT<data.CHAR> DISTINCT(final type.CHAR ... entities) { return new Command.Select.CHAR.SELECT<>(true, entities); }
    @SafeVarargs public static Select.CLOB._SELECT<data.CLOB> DISTINCT(final type.CLOB ... entities) { return new Command.Select.CLOB.SELECT<>(true, entities); }
    @SafeVarargs public static <V>Select.Column._SELECT<data.Column<V>> DISTINCT(final type.Column<? extends V> ... entities) { return new Command.Select.Column.SELECT<>(true, entities); }
    @SafeVarargs public static Select.DATE._SELECT<data.DATE> DISTINCT(final type.DATE ... entities) { return new Command.Select.DATE.SELECT<>(true, entities); }
    @SafeVarargs public static Select.DATETIME._SELECT<data.DATETIME> DISTINCT(final type.DATETIME ... entities) { return new Command.Select.DATETIME.SELECT<>(true, entities); }
    @SafeVarargs public static Select.DECIMAL._SELECT<data.DECIMAL> DISTINCT(final type.DECIMAL ... entities) { return new Command.Select.DECIMAL.SELECT<>(true, entities); }
    @SafeVarargs public static Select.DOUBLE._SELECT<data.DOUBLE> DISTINCT(final type.DOUBLE ... entities) { return new Command.Select.DOUBLE.SELECT<>(true, entities); }
    @SafeVarargs public static <V extends EntityEnum>Select.ENUM._SELECT<data.ENUM<V>> DISTINCT(final type.ENUM<? extends V> ... entities) { return new Command.Select.ENUM.SELECT<>(true, entities); }
    @SafeVarargs public static Select.FLOAT._SELECT<data.FLOAT> DISTINCT(final type.FLOAT ... entities) { return new Command.Select.FLOAT.SELECT<>(true, entities); }
    @SafeVarargs public static Select.INT._SELECT<data.INT> DISTINCT(final type.INT ... entities) { return new Command.Select.INT.SELECT<>(true, entities); }
    @SafeVarargs public static <V extends Number>Select.Numeric._SELECT<data.Numeric<V>> DISTINCT(final type.Numeric<? extends V> ... entities) { return new Command.Select.Numeric.SELECT<>(true, entities); }
    @SafeVarargs public static Select.SMALLINT._SELECT<data.SMALLINT> DISTINCT(final type.SMALLINT ... entities) { return new Command.Select.SMALLINT.SELECT<>(true, entities); }
    @SafeVarargs public static <V extends java.time.temporal.Temporal>Select.Temporal._SELECT<data.Temporal<V>> DISTINCT(final type.Temporal<? extends V> ... entities) { return new Command.Select.Temporal.SELECT<>(true, entities); }
    @SafeVarargs public static <V extends CharSequence & Comparable<?>>Select.Textual._SELECT<data.Textual<V>> DISTINCT(final type.Textual<? extends V> ... entities) { return new Command.Select.Textual.SELECT<>(true, entities); }
    @SafeVarargs public static Select.TIME._SELECT<data.TIME> DISTINCT(final type.TIME ... entities) { return new Command.Select.TIME.SELECT<>(true, entities); }
    @SafeVarargs public static Select.TINYINT._SELECT<data.TINYINT> DISTINCT(final type.TINYINT ... entities) { return new Command.Select.TINYINT.SELECT<>(true, entities); }
    @SafeVarargs public static Select.Entity._SELECT<type.Entity> DISTINCT(final type.Entity ... entities) { return new Command.Select.Entity.SELECT<>(true, entities); }
    @SafeVarargs public static <D extends data.Table>Select.Entity._SELECT<D> DISTINCT(final D ... tables) { return new Command.Select.Entity.SELECT<>(true, tables); }
  }

  /* CASE */

  public static final class CASE {
    private CASE() {}

    public static <V>Case.search.WHEN<V> WHEN(final Condition<V> condition) { return new CaseImpl.Search.WHEN<>(condition); }
  }

  public static Case.simple.CASE<byte[]> CASE(final data.BINARY binary) { return new CaseImpl.Simple.CASE<byte[],data.BINARY>(binary); }
  public static Case.simple.CASE<Boolean> CASE(final data.BOOLEAN bool) { return new CaseImpl.Simple.CASE<Boolean,data.BOOLEAN>(bool); }
  public static <V extends Number>Case.simple.CASE<V> CASE(final data.Numeric<V> numeric) { return new CaseImpl.Simple.CASE<V,data.Numeric<V>>(numeric); }
  public static <V extends Temporal>Case.simple.CASE<V> CASE(final data.Temporal<V> temporal) { return new CaseImpl.Simple.CASE<V,data.Temporal<V>>(temporal); }
  public static <V extends CharSequence & Comparable<?>>Case.simple.CASE<V> CASE(final data.Textual<V> textual) { return new CaseImpl.Simple.CASE<V,data.Textual<V>>(textual); }

  /* DELETE */

  public static Delete._DELETE DELETE(final data.Table table) { return new Command.Delete(table); }

  /* UPDATE */

  public static Update._SET UPDATE(final data.Table table) { return new Command.Update(table); }

  /* INSERT */

  @SafeVarargs public static <D extends data.Column<?>>Insert._INSERT<?> INSERT(final D column, final D ... columns) { return new Command.Insert<>(column, columns); }
  public static <D extends data.Table>Insert._INSERT<D> INSERT(final D table) { return new Command.Insert<>(table); }

  /* String Functions */

  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final V c, final type.CHAR d, final V e) { return new ExpressionImpl.Concat(a, b, c, d, e); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final V c, final type.CHAR d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final V c, final type.ENUM<?> d, final V e) { return new ExpressionImpl.Concat(a, b, c, d, e); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final V c, final type.ENUM<?> d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final type.CHAR c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final type.CHAR c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final type.ENUM<?> c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b, final type.ENUM<?> c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.CHAR b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final V c, final type.CHAR d, final V e) { return new ExpressionImpl.Concat(a, b, c, d, e); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final V c, final type.CHAR d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final V c, final type.ENUM<?> d, final V e) { return new ExpressionImpl.Concat(a, b, c, d, e); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final V c, final type.ENUM<?> d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final type.CHAR c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final type.CHAR c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final type.ENUM<?> c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b, final type.ENUM<?> c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final V a, final type.ENUM<?> b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final V b, final type.CHAR c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final V b, final type.CHAR c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final V b, final type.ENUM<?> c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final V b, final type.ENUM<?> c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final V b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final type.CHAR b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final type.CHAR b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.CHAR a, final type.ENUM<?> b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final V b, final type.CHAR c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final V b, final type.CHAR c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final V b, final type.ENUM<?> c, final V d) { return new ExpressionImpl.Concat(a, b, c, d); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final V b, final type.ENUM<?> c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final V b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final type.CHAR b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b, final V c) { return new ExpressionImpl.Concat(a, b, c); }
  public static <V extends CharSequence>exp.CHAR CONCAT(final type.ENUM<?> a, final type.ENUM<?> b) { return new ExpressionImpl.Concat(a, b); }
  public static <V extends CharSequence>data.CHAR LOWER(final V a) { return new data.CHAR().wrap(new ExpressionImpl.ChangeCase(function.Varchar.LOWER_CASE, String.valueOf(a))); }
  public static <V extends CharSequence>data.CHAR LOWER(final type.CHAR a) { return new data.CHAR().wrap(new ExpressionImpl.ChangeCase(function.Varchar.LOWER_CASE, a)); }
  public static <V extends CharSequence>data.CHAR UPPER(final V a) { return new data.CHAR().wrap(new ExpressionImpl.ChangeCase(function.Varchar.UPPER_CASE, String.valueOf(a))); }
  public static <V extends CharSequence>data.CHAR UPPER(final type.CHAR a) { return new data.CHAR().wrap(new ExpressionImpl.ChangeCase(function.Varchar.UPPER_CASE, a)); }
  public static <V extends CharSequence>data.INT LENGTH(final V a) { return new data.INT().wrap(new ExpressionImpl.Length(String.valueOf(a))); }
  public static data.INT LENGTH(final type.CHAR a) { return new data.INT().wrap(new ExpressionImpl.Length(a)); }
  public static data.INT LENGTH(final type.ENUM<?> a) { return new data.INT().wrap(new ExpressionImpl.Length(a)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), (data.INT)null, (data.INT)null)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final Integer from) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, (data.INT)null)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final type.INT from) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, (data.INT)null)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final Integer from, final type.INT to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, to)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final type.INT from, final Integer to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, to)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final Integer from, final Integer to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, to)); }
  public static <V extends CharSequence>data.CHAR SUBSTRING(final V a, final type.INT from, final type.INT to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, String.valueOf(a), from, to)); }
  public static data.CHAR SUBSTRING(final type.CHAR a) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, (data.INT)null, (data.INT)null)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final Integer from) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, (data.INT)null)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final type.INT from) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, (data.INT)null)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final Integer from, final type.INT to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, to)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final type.INT from, final Integer to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, to)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final Integer from, final Integer to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, to)); }
  public static data.CHAR SUBSTRING(final type.CHAR a, final type.INT from, final type.INT to) { return new data.CHAR().wrap(new ExpressionImpl.Substring(function.Varchar.SUBSTRING, a, from, to)); }

  /* Start Math Functions (1 parameter) */

  public static exp.TINYINT SIGN(final type.Numeric<?> a) { return new OperationImpl.Operation1.TINYINT(function.Function1.SIGN, a); }

  /* End Math Functions (1 parameter) */

  public static <D extends data.Temporal<V>,V extends java.time.temporal.Temporal>D ADD(final D a, final Interval interval) { return (D)a.clone().wrap(new ExpressionImpl.TemporalAdd<>(a, interval)); }
  public static <D extends data.Temporal<V>,V extends java.time.temporal.Temporal>D SUB(final D a, final Interval interval) { return (D)a.clone().wrap(new ExpressionImpl.TemporalSub<>(a, interval)); }

  /* Start Aggregates */

  public static exp.BIGINT COUNT(final data.Table table) { return new ExpressionImpl.Count(table, false); }
  public static <V>exp.BIGINT COUNT(final data.Column<V> column) { return new ExpressionImpl.Count(column, false); }
  public static final class COUNT {
    private COUNT() {}

    public static data.BIGINT DISTINCT(final data.Column<?> column) { return new data.BIGINT().wrap(new ExpressionImpl.Count(column, true)); }
  }

  // DT shall not be character string, bit string, or datetime.
  public static <D extends data.Numeric<V>,V extends java.lang.Number>D SUM(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.SUM, a, false)); }

  public static final class SUM {
    private SUM() {}

    public static <D extends data.Numeric<V>,V extends java.lang.Number>D DISTINCT(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.SUM, a, true)); }
  }

  // DT shall not be character string, bit string, or datetime.
  public static <D extends data.Numeric<V>,V extends java.lang.Number>D AVG(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.AVG, a, false)); }
  public static final class AVG {
    private AVG() {}

    public static <D extends data.Numeric<V>,V extends java.lang.Number>D DISTINCT(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.AVG, a, true)); }
  }

  public static <D extends data.Column<V>,V>D MAX(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.MAX, a, false)); }
  public static final class MAX {
    private MAX() {}

    public static <D extends data.Column<V>,V>D DISTINCT(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.MAX, a, true)); }
  }

  public static <D extends data.Column<V>,V>D MIN(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.MIN, a, false)); }
  public static final class MIN {
    private MIN() {}

    public static <D extends data.Column<V>,V>D DISTINCT(final D a) { return (D)a.clone().wrap(new ExpressionImpl.Set<>(function.Set.MIN, a, true)); }
  }

  /* End Aggregates */

  private static final exp.DOUBLE PI = new function.Function.PI();
  public static exp.DOUBLE PI() { return PI; }

  private static final exp.DATETIME NOW = new function.Function.NOW();
  public static exp.DATETIME NOW() { return NOW; }

  /* Condition */

  @SafeVarargs public static data.BOOLEAN AND(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) { return new BooleanTerm.And(a, b, conditions); }
  public static data.BOOLEAN AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm.And(a, conditions[0], ArrayUtil.subArray(conditions, 1));
  }

  public static data.BOOLEAN AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm.And(conditions[0], conditions[1], ArrayUtil.subArray(conditions, 2));
  }

  public static data.BOOLEAN AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm.And(array[0], array[1], ArrayUtil.subArray(array, 2));
  }

  @SafeVarargs public static data.BOOLEAN OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) { return new BooleanTerm.Or(a, b, conditions); }
  public static data.BOOLEAN OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm.Or(a, conditions[0], ArrayUtil.subArray(conditions, 1));
  }

  public static data.BOOLEAN OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm.Or(conditions[0], conditions[1], ArrayUtil.subArray(conditions, 2));
  }

  public static data.BOOLEAN OR(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm.Or(array[0], array[1], ArrayUtil.subArray(array, 2));
  }

  static final class ALL<V> extends QuantifiedComparisonPredicate<V> {
    ALL(final Select.untyped.SELECT<?> subQuery) { super("ALL", subQuery); }
  }

  static final class ANY<V> extends QuantifiedComparisonPredicate<V> {
    ANY(final Select.untyped.SELECT<?> subQuery) { super("ANY", subQuery); }
  }

  static final class SOME<V> extends QuantifiedComparisonPredicate<V> {
    SOME(final Select.untyped.SELECT<?> subQuery) { super("SOME", subQuery); }
  }

  public static <V>ALL<V> ALL(final Select.untyped.SELECT<? extends data.Entity> subQuery) { return new ALL<>(subQuery); }
  public static <V>ANY<V> ANY(final Select.untyped.SELECT<? extends data.Entity> subQuery) { return new ANY<>(subQuery); }
  public static <V>SOME<V> SOME(final Select.untyped.SELECT<? extends data.Entity> subQuery) { return new SOME<>(subQuery); }

  public static Predicate EXISTS(final Select.untyped.SELECT<?> subQuery) { return new ExistsPredicate(subQuery); }
  public static <V>Predicate IN(final type.Column<V> a, final Collection<V> b) { return new InPredicate(a, b); }
  public static <V>Predicate IN(final type.Column<V> a, final Select.untyped.SELECT<? extends data.Column<V>> b) { return new InPredicate(a, b); }
  @SafeVarargs public static <V>Predicate IN(final type.Column<V> a, final V ... b) { return new InPredicate(a, b); }
  public static Predicate LIKE(final type.CHAR a, final CharSequence b) { return new LikePredicate(a, b); }

  public static exp.TINYINT ADD(final type.TINYINT a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.TINYINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.TINYINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.TINYINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.TINYINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.TINYINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.TINYINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.TINYINT ADD(final type.TINYINT a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.TINYINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.TINYINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.TINYINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.TINYINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.TINYINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.TINYINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.SMALLINT a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.SMALLINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.SMALLINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.SMALLINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.SMALLINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.SMALLINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.SMALLINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.SMALLINT a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final type.SMALLINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.SMALLINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.SMALLINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.SMALLINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.SMALLINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.SMALLINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.INT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.INT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.INT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.INT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final type.INT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.INT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.INT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.INT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.INT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.BIGINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.BIGINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.BIGINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final type.BIGINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.BIGINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.BIGINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.BIGINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.DECIMAL a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DECIMAL a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final type.DECIMAL a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.DECIMAL a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DECIMAL a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.FLOAT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final type.FLOAT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.FLOAT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final type.DOUBLE a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.TINYINT ADD(final byte a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final byte a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final byte a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final byte a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final byte a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final byte a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final byte a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.TINYINT ADD(final byte a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final byte a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final byte a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final byte a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final byte a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final byte a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final byte a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final short a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final short a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final short a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final short a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final short a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final short a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final short a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final short a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.SMALLINT ADD(final short a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final short a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final short a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final short a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final short a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final short a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final int a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final int a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final int a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final int a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.INT ADD(final int a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final int a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final int a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final int a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final int a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final long a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final long a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final long a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.BIGINT ADD(final long a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final long a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final long a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final long a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final BigDecimal a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final BigDecimal a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.DECIMAL ADD(final BigDecimal a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final BigDecimal a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final BigDecimal a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final float a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.FLOAT ADD(final float a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final float a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }
  public static exp.DOUBLE ADD(final double a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.ADD, a, b); }

  public static exp.TINYINT SUB(final type.TINYINT a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.TINYINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.TINYINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.TINYINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.TINYINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.TINYINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.TINYINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.TINYINT SUB(final type.TINYINT a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.TINYINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.TINYINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.TINYINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.TINYINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.TINYINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.TINYINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.SMALLINT a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.SMALLINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.SMALLINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.SMALLINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.SMALLINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.SMALLINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.SMALLINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.SMALLINT a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final type.SMALLINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.SMALLINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.SMALLINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.SMALLINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.SMALLINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.SMALLINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.INT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.INT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.INT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.INT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final type.INT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.INT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.INT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.INT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.INT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.BIGINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.BIGINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.BIGINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final type.BIGINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.BIGINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.BIGINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.BIGINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.DECIMAL a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DECIMAL a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final type.DECIMAL a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.DECIMAL a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DECIMAL a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.FLOAT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final type.FLOAT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.FLOAT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final type.DOUBLE a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.TINYINT SUB(final byte a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final byte a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final byte a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final byte a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final byte a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final byte a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final byte a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.TINYINT SUB(final byte a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final byte a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final byte a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final byte a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final byte a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final byte a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final byte a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final short a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final short a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final short a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final short a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final short a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final short a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final short a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final short a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.SMALLINT SUB(final short a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final short a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final short a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final short a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final short a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final short a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final int a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final int a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final int a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final int a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.INT SUB(final int a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final int a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final int a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final int a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final int a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final long a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final long a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final long a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.BIGINT SUB(final long a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final long a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final long a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final long a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final BigDecimal a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final BigDecimal a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.DECIMAL SUB(final BigDecimal a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final BigDecimal a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final BigDecimal a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final float a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.FLOAT SUB(final float a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final float a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }
  public static exp.DOUBLE SUB(final double a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.SUB, a, b); }

  public static exp.TINYINT MUL(final type.TINYINT a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.TINYINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.TINYINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.TINYINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.TINYINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.TINYINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.TINYINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.TINYINT MUL(final type.TINYINT a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.TINYINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.TINYINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.TINYINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.TINYINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.TINYINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.TINYINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.SMALLINT a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.SMALLINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.SMALLINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.SMALLINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.SMALLINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.SMALLINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.SMALLINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.SMALLINT a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final type.SMALLINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.SMALLINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.SMALLINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.SMALLINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.SMALLINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.SMALLINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.INT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.INT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.INT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.INT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final type.INT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.INT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.INT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.INT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.INT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.BIGINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.BIGINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.BIGINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final type.BIGINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.BIGINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.BIGINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.BIGINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.DECIMAL a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DECIMAL a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final type.DECIMAL a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.DECIMAL a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DECIMAL a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.FLOAT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final type.FLOAT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.FLOAT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final type.DOUBLE a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.TINYINT MUL(final byte a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final byte a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final byte a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final byte a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final byte a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final byte a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final byte a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.TINYINT MUL(final byte a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final byte a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final byte a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final byte a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final byte a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final byte a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final byte a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final short a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final short a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final short a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final short a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final short a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final short a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final short a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final short a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.SMALLINT MUL(final short a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final short a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final short a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final short a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final short a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final short a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final int a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final int a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final int a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final int a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.INT MUL(final int a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final int a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final int a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final int a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final int a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final long a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final long a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final long a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.BIGINT MUL(final long a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final long a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final long a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final long a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final BigDecimal a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final BigDecimal a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.DECIMAL MUL(final BigDecimal a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final BigDecimal a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final BigDecimal a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final float a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.FLOAT MUL(final float a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final float a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }
  public static exp.DOUBLE MUL(final double a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.MUL, a, b); }

  public static exp.TINYINT DIV(final type.TINYINT a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.TINYINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.TINYINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.TINYINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.TINYINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.TINYINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.TINYINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.TINYINT DIV(final type.TINYINT a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.TINYINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.TINYINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.TINYINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.TINYINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.TINYINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.TINYINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.SMALLINT a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.SMALLINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.SMALLINT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.SMALLINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.SMALLINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.SMALLINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.SMALLINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.SMALLINT a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final type.SMALLINT a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.SMALLINT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.SMALLINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.SMALLINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.SMALLINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.SMALLINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.INT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.INT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.INT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.INT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final type.INT a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.INT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.INT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.INT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.INT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.BIGINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.BIGINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.BIGINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final type.BIGINT a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.BIGINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.BIGINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.BIGINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.DECIMAL a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DECIMAL a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final type.DECIMAL a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.DECIMAL a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DECIMAL a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.FLOAT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final type.FLOAT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.FLOAT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final type.DOUBLE a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.TINYINT DIV(final byte a, final type.TINYINT b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final byte a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final byte a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final byte a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final byte a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final byte a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final byte a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.TINYINT DIV(final byte a, final byte b) { return new OperationImpl.Operation2.TINYINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final byte a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final byte a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final byte a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final byte a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final byte a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final byte a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final short a, final type.TINYINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final short a, final type.SMALLINT b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final short a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final short a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final short a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final short a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final short a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final short a, final byte b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.SMALLINT DIV(final short a, final short b) { return new OperationImpl.Operation2.SMALLINT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final short a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final short a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final short a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final short a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final short a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final type.TINYINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final type.SMALLINT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final type.INT b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final int a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final int a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final int a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final int a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final byte b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final short b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.INT DIV(final int a, final int b) { return new OperationImpl.Operation2.INT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final int a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final int a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final int a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final int a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final type.TINYINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final type.SMALLINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final type.INT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final type.BIGINT b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final long a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final long a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final long a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final byte b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final short b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final int b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.BIGINT DIV(final long a, final long b) { return new OperationImpl.Operation2.BIGINT(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final long a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final long a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final long a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final BigDecimal a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final BigDecimal a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.DECIMAL DIV(final BigDecimal a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final BigDecimal a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final BigDecimal a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final float a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final short b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final int b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final long b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.FLOAT DIV(final float a, final float b) { return new OperationImpl.Operation2.FLOAT(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final float a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }
  public static exp.DOUBLE DIV(final double a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.NumericOperator2.DIV, a, b); }

  public static exp.FLOAT ABS(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.DOUBLE ABS(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ABS, a); }
  public static exp.DECIMAL ABS(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.DOUBLE ABS(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.DOUBLE ABS(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ABS, a); }
  public static exp.DECIMAL ABS(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ABS, a); }
  public static exp.FLOAT ABS(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ABS, a); }
  public static exp.DOUBLE ABS(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ABS, a); }

  public static exp.FLOAT ACOS(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.DOUBLE ACOS(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ACOS, a); }
  public static exp.DECIMAL ACOS(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.DOUBLE ACOS(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.DOUBLE ACOS(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ACOS, a); }
  public static exp.DECIMAL ACOS(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ACOS, a); }
  public static exp.FLOAT ACOS(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ACOS, a); }
  public static exp.DOUBLE ACOS(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ACOS, a); }

  public static exp.FLOAT ASIN(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.DOUBLE ASIN(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ASIN, a); }
  public static exp.DECIMAL ASIN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.DOUBLE ASIN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.DOUBLE ASIN(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ASIN, a); }
  public static exp.DECIMAL ASIN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ASIN, a); }
  public static exp.FLOAT ASIN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ASIN, a); }
  public static exp.DOUBLE ASIN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ASIN, a); }

  public static exp.FLOAT ATAN(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.DOUBLE ATAN(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ATAN, a); }
  public static exp.DECIMAL ATAN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.DOUBLE ATAN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.DOUBLE ATAN(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ATAN, a); }
  public static exp.DECIMAL ATAN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ATAN, a); }
  public static exp.FLOAT ATAN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ATAN, a); }
  public static exp.DOUBLE ATAN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ATAN, a); }

  public static exp.TINYINT CEIL(final type.TINYINT a) { return new OperationImpl.Operation1.TINYINT(function.Function1.CEIL, a); }
  public static exp.SMALLINT CEIL(final type.SMALLINT a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.CEIL, a); }
  public static exp.INT CEIL(final type.INT a) { return new OperationImpl.Operation1.INT(function.Function1.CEIL, a); }
  public static exp.BIGINT CEIL(final type.BIGINT a) { return new OperationImpl.Operation1.BIGINT(function.Function1.CEIL, a); }
  public static exp.DECIMAL CEIL(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.CEIL, a); }
  public static exp.FLOAT CEIL(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.CEIL, a); }
  public static exp.DOUBLE CEIL(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.CEIL, a); }
  public static exp.TINYINT CEIL(final byte a) { return new OperationImpl.Operation1.TINYINT(function.Function1.CEIL, a); }
  public static exp.SMALLINT CEIL(final short a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.CEIL, a); }
  public static exp.INT CEIL(final int a) { return new OperationImpl.Operation1.INT(function.Function1.CEIL, a); }
  public static exp.BIGINT CEIL(final long a) { return new OperationImpl.Operation1.BIGINT(function.Function1.CEIL, a); }
  public static exp.DECIMAL CEIL(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.CEIL, a); }
  public static exp.FLOAT CEIL(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.CEIL, a); }
  public static exp.DOUBLE CEIL(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.CEIL, a); }

  public static exp.FLOAT COS(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.FLOAT COS(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.FLOAT COS(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.DOUBLE COS(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.COS, a); }
  public static exp.DECIMAL COS(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.COS, a); }
  public static exp.FLOAT COS(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.DOUBLE COS(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.COS, a); }
  public static exp.FLOAT COS(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.FLOAT COS(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.FLOAT COS(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.DOUBLE COS(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.COS, a); }
  public static exp.DECIMAL COS(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.COS, a); }
  public static exp.FLOAT COS(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.COS, a); }
  public static exp.DOUBLE COS(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.COS, a); }

  public static exp.FLOAT DEGREES(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.DOUBLE DEGREES(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.DEGREES, a); }
  public static exp.DECIMAL DEGREES(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.DOUBLE DEGREES(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.DOUBLE DEGREES(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.DEGREES, a); }
  public static exp.DECIMAL DEGREES(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.DEGREES, a); }
  public static exp.FLOAT DEGREES(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.DEGREES, a); }
  public static exp.DOUBLE DEGREES(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.DEGREES, a); }

  public static exp.FLOAT EXP(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.DOUBLE EXP(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.EXP, a); }
  public static exp.DECIMAL EXP(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.DOUBLE EXP(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.DOUBLE EXP(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.EXP, a); }
  public static exp.DECIMAL EXP(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.EXP, a); }
  public static exp.FLOAT EXP(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.EXP, a); }
  public static exp.DOUBLE EXP(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.EXP, a); }

  public static exp.TINYINT FLOOR(final type.TINYINT a) { return new OperationImpl.Operation1.TINYINT(function.Function1.FLOOR, a); }
  public static exp.SMALLINT FLOOR(final type.SMALLINT a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.FLOOR, a); }
  public static exp.INT FLOOR(final type.INT a) { return new OperationImpl.Operation1.INT(function.Function1.FLOOR, a); }
  public static exp.BIGINT FLOOR(final type.BIGINT a) { return new OperationImpl.Operation1.BIGINT(function.Function1.FLOOR, a); }
  public static exp.DECIMAL FLOOR(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.FLOOR, a); }
  public static exp.FLOAT FLOOR(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.FLOOR, a); }
  public static exp.DOUBLE FLOOR(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.FLOOR, a); }
  public static exp.TINYINT FLOOR(final byte a) { return new OperationImpl.Operation1.TINYINT(function.Function1.FLOOR, a); }
  public static exp.SMALLINT FLOOR(final short a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.FLOOR, a); }
  public static exp.INT FLOOR(final int a) { return new OperationImpl.Operation1.INT(function.Function1.FLOOR, a); }
  public static exp.BIGINT FLOOR(final long a) { return new OperationImpl.Operation1.BIGINT(function.Function1.FLOOR, a); }
  public static exp.DECIMAL FLOOR(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.FLOOR, a); }
  public static exp.FLOAT FLOOR(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.FLOOR, a); }
  public static exp.DOUBLE FLOOR(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.FLOOR, a); }

  public static exp.FLOAT LN(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.FLOAT LN(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.FLOAT LN(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.DOUBLE LN(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LN, a); }
  public static exp.DECIMAL LN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LN, a); }
  public static exp.FLOAT LN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.DOUBLE LN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LN, a); }
  public static exp.FLOAT LN(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.FLOAT LN(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.FLOAT LN(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.DOUBLE LN(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LN, a); }
  public static exp.DECIMAL LN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LN, a); }
  public static exp.FLOAT LN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LN, a); }
  public static exp.DOUBLE LN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LN, a); }

  public static exp.FLOAT LOG10(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.DOUBLE LOG10(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG10, a); }
  public static exp.DECIMAL LOG10(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.DOUBLE LOG10(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.DOUBLE LOG10(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG10, a); }
  public static exp.DECIMAL LOG10(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LOG10, a); }
  public static exp.FLOAT LOG10(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG10, a); }
  public static exp.DOUBLE LOG10(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG10, a); }

  public static exp.FLOAT LOG2(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.DOUBLE LOG2(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG2, a); }
  public static exp.DECIMAL LOG2(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.DOUBLE LOG2(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.DOUBLE LOG2(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG2, a); }
  public static exp.DECIMAL LOG2(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.LOG2, a); }
  public static exp.FLOAT LOG2(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.LOG2, a); }
  public static exp.DOUBLE LOG2(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.LOG2, a); }

  public static exp.FLOAT RADIANS(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.DOUBLE RADIANS(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.RADIANS, a); }
  public static exp.DECIMAL RADIANS(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.DOUBLE RADIANS(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.DOUBLE RADIANS(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.RADIANS, a); }
  public static exp.DECIMAL RADIANS(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.RADIANS, a); }
  public static exp.FLOAT RADIANS(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.RADIANS, a); }
  public static exp.DOUBLE RADIANS(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.RADIANS, a); }

  public static exp.FLOAT ROUND(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.DOUBLE ROUND(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ROUND, a); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.DOUBLE ROUND(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ROUND, a); }
  public static exp.DECIMAL ROUND(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.ROUND, a); }
  public static exp.FLOAT ROUND(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.ROUND, a); }
  public static exp.DOUBLE ROUND(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.ROUND, a); }

  public static exp.TINYINT SIGN(final type.TINYINT a) { return new OperationImpl.Operation1.TINYINT(function.Function1.SIGN, a); }
  public static exp.SMALLINT SIGN(final type.SMALLINT a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.SIGN, a); }
  public static exp.INT SIGN(final type.INT a) { return new OperationImpl.Operation1.INT(function.Function1.SIGN, a); }
  public static exp.BIGINT SIGN(final type.BIGINT a) { return new OperationImpl.Operation1.BIGINT(function.Function1.SIGN, a); }
  public static exp.DECIMAL SIGN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SIGN, a); }
  public static exp.FLOAT SIGN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIGN, a); }
  public static exp.DOUBLE SIGN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIGN, a); }
  public static exp.TINYINT SIGN(final byte a) { return new OperationImpl.Operation1.TINYINT(function.Function1.SIGN, a); }
  public static exp.SMALLINT SIGN(final short a) { return new OperationImpl.Operation1.SMALLINT(function.Function1.SIGN, a); }
  public static exp.INT SIGN(final int a) { return new OperationImpl.Operation1.INT(function.Function1.SIGN, a); }
  public static exp.BIGINT SIGN(final long a) { return new OperationImpl.Operation1.BIGINT(function.Function1.SIGN, a); }
  public static exp.DECIMAL SIGN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SIGN, a); }
  public static exp.FLOAT SIGN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIGN, a); }
  public static exp.DOUBLE SIGN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIGN, a); }

  public static exp.FLOAT SIN(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.DOUBLE SIN(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIN, a); }
  public static exp.DECIMAL SIN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.DOUBLE SIN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.DOUBLE SIN(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIN, a); }
  public static exp.DECIMAL SIN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SIN, a); }
  public static exp.FLOAT SIN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SIN, a); }
  public static exp.DOUBLE SIN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SIN, a); }

  public static exp.FLOAT SQRT(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.DOUBLE SQRT(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SQRT, a); }
  public static exp.DECIMAL SQRT(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.DOUBLE SQRT(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.DOUBLE SQRT(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SQRT, a); }
  public static exp.DECIMAL SQRT(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.SQRT, a); }
  public static exp.FLOAT SQRT(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.SQRT, a); }
  public static exp.DOUBLE SQRT(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.SQRT, a); }

  public static exp.FLOAT TAN(final type.TINYINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final type.SMALLINT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final type.INT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.DOUBLE TAN(final type.BIGINT a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.TAN, a); }
  public static exp.DECIMAL TAN(final type.DECIMAL a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final type.FLOAT a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.DOUBLE TAN(final type.DOUBLE a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final byte a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final short a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final int a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.DOUBLE TAN(final long a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.TAN, a); }
  public static exp.DECIMAL TAN(final BigDecimal a) { return new OperationImpl.Operation1.DECIMAL(function.Function1.TAN, a); }
  public static exp.FLOAT TAN(final float a) { return new OperationImpl.Operation1.FLOAT(function.Function1.TAN, a); }
  public static exp.DOUBLE TAN(final double a) { return new OperationImpl.Operation1.DOUBLE(function.Function1.TAN, a); }

  public static exp.FLOAT ATAN2(final type.TINYINT y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.TINYINT y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.TINYINT y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.TINYINT y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.TINYINT y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.TINYINT y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.TINYINT y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.TINYINT y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.SMALLINT y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.SMALLINT y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.SMALLINT y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.SMALLINT y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.SMALLINT y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.SMALLINT y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.SMALLINT y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.INT y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.INT y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.INT y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.INT y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.INT y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.INT y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.INT y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final type.TINYINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final type.SMALLINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final type.INT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.BIGINT y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.BIGINT y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final byte x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final short x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final int x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.BIGINT y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.BIGINT y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.BIGINT y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final type.TINYINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final type.SMALLINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final type.INT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final type.BIGINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.DECIMAL y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DECIMAL y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final byte x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final short x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final int x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final long x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final type.DECIMAL y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.DECIMAL y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DECIMAL y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.BIGINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.DECIMAL x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.FLOAT y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final long x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final BigDecimal x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final type.FLOAT y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.FLOAT y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.TINYINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.SMALLINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.INT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.DECIMAL x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.FLOAT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final byte x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final short x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final int x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final BigDecimal x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final float x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final type.DOUBLE y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final byte y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final byte y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final byte y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final byte y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final byte y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final byte y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final byte y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final short y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final short y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final short y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final short y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final short y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final short y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final short y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final int y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final int y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final int y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final int y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final int y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final int y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final int y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final type.TINYINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final type.SMALLINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final type.INT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final long y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final long y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final byte x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final short x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final int x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final long y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final long y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final long y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final type.TINYINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final type.SMALLINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final type.INT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final type.BIGINT x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final type.DECIMAL x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final BigDecimal y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final BigDecimal y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final byte x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final short x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final int x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final long x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.DECIMAL ATAN2(final BigDecimal y, final BigDecimal x) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final BigDecimal y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final BigDecimal y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.TINYINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.SMALLINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.INT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.BIGINT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.DECIMAL x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final type.FLOAT x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final float y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final byte x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final short x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final int x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final long x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final BigDecimal x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT ATAN2(final float y, final float x) { return new OperationImpl.Operation2.FLOAT(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final float y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.TINYINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.SMALLINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.INT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.BIGINT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.DECIMAL x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.FLOAT x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final type.DOUBLE x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final byte x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final short x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final int x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final long x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final BigDecimal x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final float x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.DOUBLE ATAN2(final double y, final double x) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ATAN2, y, x); }
  public static exp.FLOAT LOG(final type.TINYINT b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.TINYINT b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.TINYINT b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.TINYINT b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.TINYINT b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.TINYINT b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.TINYINT b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.TINYINT b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.SMALLINT b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.SMALLINT b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.SMALLINT b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.SMALLINT b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.SMALLINT b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.SMALLINT b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.SMALLINT b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.INT b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.INT b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.INT b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.INT b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.INT b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.INT b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.INT b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.BIGINT b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.BIGINT b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.BIGINT b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.BIGINT b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.BIGINT b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.BIGINT b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.BIGINT b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DECIMAL b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.DECIMAL b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DECIMAL b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DECIMAL b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.DECIMAL b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DECIMAL b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DECIMAL b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.FLOAT b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.FLOAT b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.FLOAT b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.FLOAT b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.FLOAT b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.FLOAT b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.FLOAT b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DOUBLE b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.DOUBLE b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DOUBLE b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DOUBLE b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final type.DOUBLE b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final type.DOUBLE b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final type.DOUBLE b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final byte b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final byte b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final byte b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final byte b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final byte b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final byte b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final byte b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final short b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final short b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final short b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final short b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final short b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final short b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final short b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final int b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final int b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final int b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final int b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final int b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final int b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final int b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final long b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final long b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final long b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final long b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final long b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final long b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final long b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final BigDecimal b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final BigDecimal b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final BigDecimal b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final BigDecimal b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final BigDecimal b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final BigDecimal b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final BigDecimal b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final float b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final float b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final float b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final float b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final float b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final float b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final float b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final type.TINYINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final type.SMALLINT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final type.INT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final double b, final type.BIGINT n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final double b, final type.DECIMAL n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final type.FLOAT n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final double b, final type.DOUBLE n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final byte n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final short n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final int n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final double b, final long n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.DECIMAL LOG(final double b, final BigDecimal n) { return new OperationImpl.Operation2.DECIMAL(function.Function2.LOG, b, n); }
  public static exp.FLOAT LOG(final double b, final float n) { return new OperationImpl.Operation2.FLOAT(function.Function2.LOG, b, n); }
  public static exp.DOUBLE LOG(final double b, final double n) { return new OperationImpl.Operation2.DOUBLE(function.Function2.LOG, b, n); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.TINYINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.SMALLINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.INT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.BIGINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.DECIMAL m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.FLOAT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final type.DOUBLE m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final byte m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final short m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final int m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final long m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final BigDecimal m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final float m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final type.TINYINT n, final double m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.TINYINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.SMALLINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.INT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.BIGINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.DECIMAL m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.FLOAT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final type.DOUBLE m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final byte m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final short m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final int m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final long m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final BigDecimal m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final float m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final type.SMALLINT n, final double m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.TINYINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.SMALLINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.INT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.BIGINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.DECIMAL m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.FLOAT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final type.DOUBLE m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final byte m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final short m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final int m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final long m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final BigDecimal m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final float m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final type.INT n, final double m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.TINYINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.SMALLINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.INT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.BIGINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.DECIMAL m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.FLOAT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final type.DOUBLE m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final byte m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final short m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final int m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final long m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final BigDecimal m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final float m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final type.BIGINT n, final double m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.TINYINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.SMALLINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.INT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.BIGINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.DECIMAL m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.FLOAT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final type.DOUBLE m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final byte m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final short m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final int m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final long m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final BigDecimal m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final float m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final type.DECIMAL n, final double m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.TINYINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.SMALLINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.INT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.BIGINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.DECIMAL m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.FLOAT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final type.DOUBLE m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final byte m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final short m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final int m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final long m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final BigDecimal m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final float m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final type.FLOAT n, final double m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.TINYINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.SMALLINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.INT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.BIGINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.DECIMAL m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.FLOAT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final type.DOUBLE m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final byte m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final short m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final int m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final long m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final BigDecimal m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final float m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final type.DOUBLE n, final double m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.TINYINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.SMALLINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.INT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.BIGINT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.DECIMAL m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.FLOAT m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final type.DOUBLE m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final byte m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final short m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final int m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final long m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final BigDecimal m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final float m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.TINYINT MOD(final byte n, final double m) { return new OperationImpl.Operation2.TINYINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.TINYINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.SMALLINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.INT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.BIGINT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.DECIMAL m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.FLOAT m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final type.DOUBLE m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final byte m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final short m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final int m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final long m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final BigDecimal m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final float m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.SMALLINT MOD(final short n, final double m) { return new OperationImpl.Operation2.SMALLINT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.TINYINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.SMALLINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.INT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.BIGINT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.DECIMAL m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.FLOAT m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final type.DOUBLE m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final byte m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final short m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final int m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final long m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final BigDecimal m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final float m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.INT MOD(final int n, final double m) { return new OperationImpl.Operation2.INT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.TINYINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.SMALLINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.INT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.BIGINT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.DECIMAL m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.FLOAT m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final type.DOUBLE m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final byte m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final short m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final int m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final long m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final BigDecimal m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final float m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.BIGINT MOD(final long n, final double m) { return new OperationImpl.Operation2.BIGINT(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.TINYINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.SMALLINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.INT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.BIGINT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.DECIMAL m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.FLOAT m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final type.DOUBLE m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final byte m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final short m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final int m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final long m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final BigDecimal m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final float m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.DECIMAL MOD(final BigDecimal n, final double m) { return new OperationImpl.Operation2.DECIMAL(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.TINYINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.SMALLINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.INT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.BIGINT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.DECIMAL m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.FLOAT m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final type.DOUBLE m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final byte m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final short m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final int m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final long m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final BigDecimal m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final float m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.FLOAT MOD(final float n, final double m) { return new OperationImpl.Operation2.FLOAT(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.TINYINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.SMALLINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.INT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.BIGINT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.DECIMAL m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.FLOAT m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final type.DOUBLE m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final byte m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final short m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final int m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final long m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final BigDecimal m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final float m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.DOUBLE MOD(final double n, final double m) { return new OperationImpl.Operation2.DOUBLE(function.Function2.MOD, n, m); }
  public static exp.FLOAT POW(final type.TINYINT a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.TINYINT a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.TINYINT a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.TINYINT a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.TINYINT a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.TINYINT a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.TINYINT a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.TINYINT a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.SMALLINT a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.SMALLINT a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.SMALLINT a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.SMALLINT a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.SMALLINT a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.SMALLINT a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.SMALLINT a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.INT a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.INT a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.INT a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.INT a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.INT a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.INT a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.INT a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final type.TINYINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final type.SMALLINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final type.INT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.BIGINT a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.BIGINT a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final byte p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final short p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final int p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.BIGINT a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.BIGINT a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.BIGINT a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final type.TINYINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final type.SMALLINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final type.INT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final type.BIGINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.DECIMAL a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DECIMAL a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final byte p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final short p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final int p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final long p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final type.DECIMAL a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.DECIMAL a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DECIMAL a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.BIGINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.DECIMAL p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.FLOAT a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final long p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final BigDecimal p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final type.FLOAT a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.FLOAT a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.TINYINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.SMALLINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.INT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.DECIMAL p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.FLOAT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final byte p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final short p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final int p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final BigDecimal p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final float p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final type.DOUBLE a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final byte a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final byte a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final byte a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final byte a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final byte a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final byte a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final byte a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final short a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final short a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final short a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final short a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final short a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final short a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final short a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final int a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final int a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final int a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final int a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final int a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final int a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final int a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final type.TINYINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final type.SMALLINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final type.INT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final long a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final long a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final byte p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final short p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final int p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final long a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final long a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final long a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final type.TINYINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final type.SMALLINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final type.INT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final type.BIGINT p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final type.DECIMAL p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final BigDecimal a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final BigDecimal a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final byte p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final short p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final int p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final long p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.DECIMAL POW(final BigDecimal a, final BigDecimal p) { return new OperationImpl.Operation2.DECIMAL(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final BigDecimal a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final BigDecimal a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.TINYINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.SMALLINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.INT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.BIGINT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.DECIMAL p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final type.FLOAT p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final float a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final byte p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final short p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final int p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final long p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final BigDecimal p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.FLOAT POW(final float a, final float p) { return new OperationImpl.Operation2.FLOAT(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final float a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.TINYINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.SMALLINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.INT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.BIGINT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.DECIMAL p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.FLOAT p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final type.DOUBLE p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final byte p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final short p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final int p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final long p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final BigDecimal p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final float p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.DOUBLE POW(final double a, final double p) { return new OperationImpl.Operation2.DOUBLE(function.Function2.POW, a, p); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.TINYINT a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.TINYINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.TINYINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.TINYINT a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.TINYINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.TINYINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.TINYINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.SMALLINT a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.SMALLINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.SMALLINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.SMALLINT a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.SMALLINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.SMALLINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.SMALLINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.INT a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.INT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.INT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.INT a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.INT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.INT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.INT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.BIGINT a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.BIGINT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.BIGINT a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.BIGINT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.BIGINT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.DECIMAL a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DECIMAL a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final type.DECIMAL a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.DECIMAL a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DECIMAL a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.FLOAT a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final long b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final type.FLOAT a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.FLOAT a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final type.DOUBLE a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final byte a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final byte a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final byte a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final byte a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final byte a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final byte a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final byte a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final short a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final short a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final short a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final short a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final short a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final short a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final short a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final int a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final int a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final int a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final int a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final int a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final int a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final int a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final long a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final long a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final long a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final long a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final long a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final type.TINYINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final type.SMALLINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final type.INT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final type.BIGINT b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final type.DECIMAL b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final BigDecimal a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final BigDecimal a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final byte b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final short b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final int b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final long b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.DECIMAL ROUND(final BigDecimal a, final BigDecimal b) { return new OperationImpl.Operation2.DECIMAL(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final BigDecimal a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final BigDecimal a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.TINYINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.SMALLINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.INT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.BIGINT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.DECIMAL b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final type.FLOAT b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final float a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final byte b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final short b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final int b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final long b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final BigDecimal b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.FLOAT ROUND(final float a, final float b) { return new OperationImpl.Operation2.FLOAT(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final float a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.TINYINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.SMALLINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.INT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.BIGINT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.DECIMAL b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.FLOAT b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final type.DOUBLE b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final byte b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final short b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final int b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final long b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final BigDecimal b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final float b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }
  public static exp.DOUBLE ROUND(final double a, final double b) { return new OperationImpl.Operation2.DOUBLE(function.Function2.ROUND, a, b); }

  public static data.BOOLEAN BETWEEN(final type.Numeric<?> v, final type.Numeric<?> l, final type.Numeric<?> r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Numeric<?> v, final type.Numeric<?> l, final Number r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Numeric<?> v, final Number l, final type.Numeric<?> r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Numeric<?> v, final Number l, final Number r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final Number v, final type.Numeric<?> l, final type.Numeric<?> r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final Number v, final type.Numeric<?> l, final Number r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final Number v, final Number l, final type.Numeric<?> r) { return new BetweenPredicates.NumericBetweenPredicate<>(v, l, r); }

  public static data.BOOLEAN BETWEEN(final type.Textual<?> v, final type.Textual<?> l, final type.Textual<?> r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Textual<?> v, final type.Textual<?> l, final CharSequence r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Textual<?> v, final CharSequence l, final type.Textual<?> r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.Textual<?> v, final CharSequence l, final CharSequence r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final CharSequence v, final type.Textual<?> l, final type.Textual<?> r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final CharSequence v, final type.Textual<?> l, final CharSequence r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final CharSequence v, final CharSequence l, final type.Textual<?> r) { return new BetweenPredicates.TextualBetweenPredicate<>(v, l, r); }

  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATE l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATE l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATE l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATE l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATETIME l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATETIME l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATETIME l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final type.DATETIME l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDate l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDate l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDate l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDate l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDateTime l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDateTime l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDateTime l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATE v, final LocalDateTime l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATE l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATE l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATE l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATE l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATETIME l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATETIME l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATETIME l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final type.DATETIME l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDate l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDate l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDate l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDate l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDateTime l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDateTime l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDateTime l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.DATETIME v, final LocalDateTime l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATE l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATE l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATE l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATE l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATETIME l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATETIME l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATETIME l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final type.DATETIME l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final LocalDate l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final LocalDate l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final LocalDateTime l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDate v, final LocalDateTime l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATE l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATE l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATE l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATE l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATETIME l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATETIME l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATETIME l, final LocalDate r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final type.DATETIME l, final LocalDateTime r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final LocalDate l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final LocalDate l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final LocalDateTime l, final type.DATE r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalDateTime v, final LocalDateTime l, final type.DATETIME r) { return new BetweenPredicates.TemporalBetweenPredicate<>(v, l, r); }

  public static data.BOOLEAN BETWEEN(final type.TIME v, final type.TIME l, final type.TIME r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.TIME v, final type.TIME l, final LocalTime r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.TIME v, final LocalTime l, final type.TIME r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final type.TIME v, final LocalTime l, final LocalTime r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalTime v, final type.TIME l, final type.TIME r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalTime v, final type.TIME l, final LocalTime r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }
  public static data.BOOLEAN BETWEEN(final LocalTime v, final LocalTime l, final type.TIME r) { return new BetweenPredicates.TimeBetweenPredicate<>(v, l, r); }

  public static final class IS {
    private IS() {}
    public static Predicate NULL(final type.Column<?> column) { return new NullPredicate(column, true); }

    public static final class NOT {
      private NOT() {}
      public static Predicate NULL(final type.Column<?> column) { return new NullPredicate(column, false); }
    }
  }

  private DML() {
  }
}