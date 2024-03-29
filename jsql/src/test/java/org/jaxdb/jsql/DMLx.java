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

/**[import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;]**/
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
/*** public ***/ final class DMLx {
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

  /**** DMLx ****/

  public static final class IS {
    private IS() {}
    public static Predicate NULL(final type.Column<?> column) { return new NullPredicate(column, true); }

    public static final class NOT {
      private NOT() {}
      public static Predicate NULL(final type.Column<?> column) { return new NullPredicate(column, false); }
    }
  }

  private DMLx() {
  }
}