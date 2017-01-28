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

import java.time.temporal.Temporal;
import java.util.Collection;

import org.safris.commons.lang.Arrays;
import org.safris.xdb.entities.data.Char;
import org.safris.xdb.entities.data.DateTime;
import org.safris.xdb.entities.data.Decimal;
import org.safris.xdb.entities.data.Enum;
import org.safris.xdb.entities.data.Numeric;
import org.safris.xdb.entities.model.delete;
import org.safris.xdb.entities.model.expression;
import org.safris.xdb.entities.model.insert;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.entities.model.update;
import org.safris.xdb.xdd.xe.$xdd_data;

public final class DML {

  /** Ordering Specification **/

  public static <T,V extends DataType<T>>V ASC(final V dataType) {
    final V wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new OrderingSpec<T>(Operator.ASC, dataType));
    return wrapper;
  }

  public static <V extends DataType<T>,T>V DESC(final V dataType) {
    final V wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new OrderingSpec<T>(Operator.DESC, dataType));
    return wrapper;
  }

  /** CROSS **/

  public static class CROSS extends Provision<Subject<?>> {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.append("CROSS");
    }
  }

  public static final CROSS CROSS = new CROSS();

  /** NATURAL **/

  public static class NATURAL extends Provision<Subject<?>> {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.append("NATURAL");
    }
  }

  public static final NATURAL NATURAL = new NATURAL();

  /** TYPE **/

  public static abstract class TYPE extends Provision<Subject<?>> {
  }

  public static final TYPE INNER = new TYPE() {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.append("INNER");
    }
  };

  public static final class OUTER {
    public static final TYPE LEFT = new TYPE() {
      @Override
      protected void serialize(final Serialization serialization) {
        serialization.append("LEFT OUTER");
      }
    };

    public static final TYPE RIGHT = new TYPE() {
      @Override
      protected void serialize(final Serialization serialization) {
        serialization.append("RIGHT OUTER");
      }
    };

    public static final TYPE FULL = new TYPE() {
      @Override
      protected void serialize(final Serialization serialization) {
        serialization.append("FULL OUTER");
      }
    };
  }

  /** START ComparisonPredicate **/

  public static <T extends Number>data.Boolean EQ(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean EQ(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Temporal>data.Boolean EQ(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T>data.Boolean EQ(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.EQ, a, b);
  }

  public static <T extends Number>data.Boolean NE(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean NE(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Temporal>data.Boolean NE(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T>data.Boolean NE(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.NE, a, b);
  }

  public static <T extends Number>data.Boolean LT(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean LT(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Temporal>data.Boolean LT(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T>data.Boolean LT(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean GT(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Temporal>data.Boolean GT(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T>data.Boolean GT(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GT, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean LTE(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Temporal>data.Boolean LTE(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T>data.Boolean LTE(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.LTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final Numeric<? extends T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final T a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final Numeric<? extends T> a, final select.SELECT<? extends DataType<T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final select.SELECT<? extends Numeric<? extends T>> a, final Numeric<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final T a, final select.SELECT<? extends Numeric<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final select.SELECT<? extends Numeric<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Number>data.Boolean GTE(final Numeric<? extends T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final data.Temporal<? extends T> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final data.Temporal<? super T> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final T a, final data.Temporal<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final data.Temporal<? extends T> a, final select.SELECT<? extends data.Temporal<? extends T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final select.SELECT<? extends data.Temporal<? extends T>> a, final data.Temporal<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final T a, final select.SELECT<? extends data.Temporal<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final select.SELECT<? extends data.Temporal<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T extends Temporal>data.Boolean GTE(final data.Temporal<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final data.Textual<? extends T> a, final data.Textual<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final data.Textual<?> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final T a, final data.Textual<?> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final data.Boolean a, final data.Boolean b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final data.Textual<? super T> a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final select.SELECT<? extends data.Textual<? super T>> a, final data.Textual<? super T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final T a, final select.SELECT<? extends data.Textual<? super T>> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final select.SELECT<? extends data.Textual<? super T>> a, final T b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  public static <T>data.Boolean GTE(final data.Textual<? super T> a, final QuantifiedComparisonPredicate<? extends T> b) {
    return new ComparisonPredicate<T>(Operator.GTE, a, b);
  }

  /** END ComparisonPredicate **/

  /** SetQualifier **/

  protected static abstract class SetQualifier extends Provision<Subject<?>> {
  }

  public static class ALL extends SetQualifier {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.append("ALL");
    }
  }

  public static class DISTINCT extends SetQualifier {
    @Override
    protected void serialize(final Serialization serialization) {
      serialization.append("DISTINCT");
    }
  }

  public static final ALL ALL = new ALL();
  public static final DISTINCT DISTINCT = new DISTINCT();

  /** SELECT **/

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final T ... entities) {
    return DML.<T>SELECT(null, null, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final ALL all, final T ... entities) {
    return DML.<T>SELECT(all, null, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final DISTINCT distinct, final T ... entities) {
    return DML.<T>SELECT(null, distinct, entities);
  }

  @SafeVarargs
  public static <T extends Subject<?>>select._SELECT<T> SELECT(final ALL all, final DISTINCT distinct, final T ... entities) {
    return new Select.SELECT<T>(all, distinct, entities);
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
  public static <T extends Entity>insert.INSERT_SELECT<T> INSERT(final T ... entities) {
    return new Insert.INSERT<T>(entities);
  }

  public static insert.INSERT INSERT(final $xdd_data data) {
    return new Insert.INSERT<Entity>(Entities.toEntities(data));
  }

  /** String Functions **/

  public static Char CONCAT(final Char a, final Char b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Char c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Char c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Char d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Char d, final CharSequence e) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Enum<?> b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Enum<?> c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Enum<?> c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Enum<?> d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Enum<?> d, final CharSequence e) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Enum<?> b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Enum<?> c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Enum<?> c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Enum<?> d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b, final Enum<?> c, final CharSequence d) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c, final Enum<?> d, final CharSequence e) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Char b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Char c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Char c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Char d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final Char c, final CharSequence d) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c, final Char d, final CharSequence e) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c, d, e));
    return wrapper;
  }

  public static Char CONCAT(final Char a, final CharSequence b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Char b, final CharSequence c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final Enum<?> a, final CharSequence b) {
    final Char wrapper = new Char(a.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b));
    return wrapper;
  }

  public static Char CONCAT(final CharSequence a, final Enum<?> b, final CharSequence c) {
    final Char wrapper = new Char(b.owner);
    wrapper.setWrapper(new StringExpression(Operator.CONCAT, a, b, c));
    return wrapper;
  }

  /** Math Functions **/

  public static <T extends Number,N extends Numeric<T>>N ABS(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Abs<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N SIGN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Sign<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N FLOOR(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Floor<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N CEIL(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Ceil<T>(dataType));
    return wrapper;
  }

  // FIXME: Need to raise the precition here!
  public static <T extends Number,N extends Numeric<T>>N POW(final N x, final double y) {
    final N wrapper = x.newInstance(x.owner);
    wrapper.setWrapper(new function.numeric.Pow<T>(x, y));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new function.numeric.Mod<T>(a, b));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MOD(final Numeric<T> a, final Number b) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new function.numeric.Mod<T>(a, b));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N ROUND(final N dataType, final int decimal) {
    if (decimal < 0)
      throw new IllegalArgumentException("decimal < 0");

    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Round<T>(dataType, decimal));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N SQRT(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Sqrt<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N SIN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Sin<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N ASIN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Asin<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N COS(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Cos<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N ACOS(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Acos<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N TAN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Tan<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N ATAN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Atan<T>(dataType));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ATAN2(final Numeric<? extends T> a, final Numeric<? extends T> b) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new function.numeric.Atan2<T>(a, b));
    return wrapper;
  }

  // FIXME: Need to raise the precition here!
  public static <T extends Number,N extends Numeric<T>>N EXP(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Exp<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N LN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Ln<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N LOG(final N a, final N b) {
    final N wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new function.numeric.Log<T>(a, b));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N LOG(final N a, final Number b) {
    final N wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new function.numeric.Log<T>(a, b));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N LOG2(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Log2<T>(dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N LOG10(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new function.numeric.Log10<T>(dataType));
    return wrapper;
  }

  /** Aggregate **/

  // FIXME: Need COUNT(*) or COUNT(a.*) or COUNT(a.a)
  public static org.safris.xdb.entities.data.Long COUNT() {
    final org.safris.xdb.entities.data.Long wrapper = new org.safris.xdb.entities.data.Long();
    wrapper.setWrapper(CountFunction.STAR);
    return wrapper;
  }

  public static org.safris.xdb.entities.data.Long COUNT(final DataType<?> dataType) {
    final org.safris.xdb.entities.data.Long wrapper = new org.safris.xdb.entities.data.Long();
    wrapper.setWrapper(new CountFunction(null, dataType));
    return wrapper;
  }

  public static org.safris.xdb.entities.data.Long COUNT(final DISTINCT distinct, final DataType<?> dataType) {
    final org.safris.xdb.entities.data.Long wrapper = new org.safris.xdb.entities.data.Long();
    wrapper.setWrapper(new CountFunction(distinct, dataType));
    return wrapper;
  }

  public static org.safris.xdb.entities.data.Long COUNT(final ALL all, final DataType<?> dataType) {
    final org.safris.xdb.entities.data.Long wrapper = new org.safris.xdb.entities.data.Long();
    wrapper.setWrapper(new CountFunction(all, dataType));
    return wrapper;
  }

  // DT shall not be character string, bit string, or datetime.
  public static <T extends Number,N extends Numeric<T>>N SUM(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("SUM", dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N SUM(final DISTINCT distinct, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("SUM", distinct, dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N SUM(final ALL all, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("SUM", all, dataType));
    return wrapper;
  }

  // DT shall not be character string, bit string, or datetime.
  public static <T extends Number,N extends Numeric<T>>N AVG(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("AVG", dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N AVG(final DISTINCT distinct, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("AVG", distinct, dataType));
    return wrapper;
  }

  public static <T extends Number,N extends Numeric<T>>N AVG(final ALL all, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("AVG", all, dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MAX(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MAX", dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MAX(final DISTINCT distinct, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MAX", distinct, dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MAX(final ALL all, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MAX", all, dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MIN(final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MIN", dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MIN(final DISTINCT distinct, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MIN", distinct, dataType));
    return wrapper;
  }

  public static <T,N extends DataType<T>>N MIN(final ALL all, final N dataType) {
    final N wrapper = dataType.newInstance(dataType.owner);
    wrapper.setWrapper(new SetFunction<T>("MIN", all, dataType));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.DIVIDE, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.DIVIDE, args[0], args[1], Arrays.subArray(args, 2)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.DIVIDE, a, b, args));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> DIV(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = b.newInstance(b.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.DIVIDE, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> DIV(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.DIVIDE, a, args[0], Arrays.subArray(args, 1)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MULTIPLY, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MULTIPLY, args[0], args[1], Arrays.subArray(args, 2)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MULTIPLY, a, b, args));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MUL(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = b.newInstance(b.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MULTIPLY, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> MUL(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MULTIPLY, a, args[0], Arrays.subArray(args, 1)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.PLUS, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.PLUS, args[0], args[1], Arrays.subArray(args, 2)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.PLUS, a, b, args));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> ADD(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = b.newInstance(b.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.PLUS, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> ADD(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.PLUS, a, args[0], Arrays.subArray(args, 1)));
    return wrapper;
  }

  public static <T extends Temporal,V extends data.Temporal<T>>V ADD(final V a, final Interval interval) {
    final V wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new TemporalExpression<T>(Operator.PLUS, a, interval));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    return ADD(a, b, args);
  }

  public static <T extends Number>Numeric<T> PLUS(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    return ADD(args[0], args[1], Arrays.subArray(args, 2));
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    return ADD(a, b, args);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> PLUS(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    return ADD(a, b, args);
  }

  public static <T extends Number>Numeric<T> PLUS(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    return ADD(a, args[0], Arrays.subArray(args, 1));
  }

  public static <T extends Temporal,V extends data.Temporal<T>>V PLUS(final V a, final Interval interval) {
    return ADD(a, interval);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, args[0], args[1], Arrays.subArray(args, 2)));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Number a, Numeric<T> b) {
    final Numeric<T> wrapper = b.newInstance(b.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, a, b));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> SUB(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    final Numeric<T> wrapper = b.newInstance(b.owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, a, b, args));
    return wrapper;
  }

  public static <T extends Number>Numeric<T> SUB(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    final Numeric<T> wrapper = args[0].newInstance(args[0].owner);
    wrapper.setWrapper(new NumericExpression<T>(Operator.MINUS, a, args[0], Arrays.subArray(args, 1)));
    return wrapper;
  }

  public static <T extends Temporal,V extends data.Temporal<T>>V SUB(final V a, final Interval interval) {
    final V wrapper = a.newInstance(a.owner);
    wrapper.setWrapper(new TemporalExpression<T>(Operator.MINUS, a, interval));
    return wrapper;
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Numeric<T> b, final Numeric<T> ... args) {
    return SUB(a, b, args);
  }

  public static <T extends Number>Numeric<T> MINUS(final Numeric<T>[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("args.length < 2");

    return SUB(args[0], args[1], Arrays.subArray(args, 2));
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Numeric<T> a, final Number b, final Numeric<T> ... args) {
    return SUB(a, b, args);
  }

  @SafeVarargs
  public static <T extends Number>Numeric<T> MINUS(final Number a, final Numeric<T> b, final Numeric<T> ... args) {
    return SUB(a, b, args);
  }

  public static <T extends Number>Numeric<T> MINUS(final Number a, final Numeric<T>[] args) {
    if (args.length < 1)
      throw new IllegalArgumentException("args.length < 1");

    return SUB(a, args[0], Arrays.subArray(args, 1));
  }

  public static <T extends Temporal,V extends data.Temporal<T>>V MINUS(final V a, final Interval interval) {
    return SUB(a, interval);
  }

  private static class NOW extends DateTime {
    protected NOW() {
      super();
      this.setWrapper(new TemporalFunction<java.time.LocalDateTime>("NOW"));
    }

    @Override
    public final NOW clone() {
      return new NOW();
    }
  }

  private static final NOW NOW = new NOW();

  public static NOW NOW() {
    return NOW;
  }

  private static class PI extends Decimal {
    protected PI() {
      super();
      this.setWrapper(new function.numeric.Pi());
    }

    @Override
    public final PI clone() {
      return new PI();
    }
  }

  private static final PI PI = new PI();

  public static PI PI() {
    return PI;
  }

  /** Condition **/

  @SafeVarargs
  public static <T>data.Boolean AND(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(Operator.AND, a, b, conditions);
  }

  public static <T>data.Boolean AND(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(Operator.AND, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static <T>data.Boolean AND(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(Operator.AND, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static <T>data.Boolean AND(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(Operator.AND, array[0], array[1], Arrays.subArray(array, 2));
  }

  @SafeVarargs
  public static <T>data.Boolean OR(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    return new BooleanTerm(Operator.OR, a, b, conditions);
  }

  public static <T>data.Boolean OR(final Condition<?> a, final Condition<?>[] conditions) {
    if (conditions.length < 1)
      throw new IllegalArgumentException("conditions.length < 1");

    return new BooleanTerm(Operator.OR, a, conditions[0], Arrays.subArray(conditions, 1));
  }

  public static <T>data.Boolean OR(final Condition<?>[] conditions) {
    if (conditions.length < 2)
      throw new IllegalArgumentException("conditions.length < 2");

    return new BooleanTerm(Operator.OR, conditions[0], conditions[1], Arrays.subArray(conditions, 2));
  }

  public static <T>data.Boolean OR(final Collection<Condition<?>> conditions) {
    if (conditions.size() < 2)
      throw new IllegalArgumentException("conditions.size() < 2");

    final Condition<?>[] array = conditions.toArray(new Condition<?>[conditions.size()]);
    return new BooleanTerm(Operator.OR, array[0], array[1], Arrays.subArray(array, 2));
  }

  /** Predicate **/

  protected static final class qualified {
    protected static final class ALL<T> extends QuantifiedComparisonPredicate<T> {
      protected ALL(select.SELECT<?> subQuery) {
        super("ALL", subQuery);
      }
    }

    protected static final class ANY<T> extends QuantifiedComparisonPredicate<T> {
      protected ANY(select.SELECT<?> subQuery) {
        super("ANY", subQuery);
      }
    }

    protected static final class SOME<T> extends QuantifiedComparisonPredicate<T> {
      protected SOME(select.SELECT<?> subQuery) {
        super("SOME", subQuery);
      }
    }
  }

  public static <T>qualified.ALL<T> ALL(final select.SELECT<? extends Subject<T>> subQuery) {
    return new qualified.ALL<T>(subQuery);
  }

  public static <T>qualified.ANY<T> ANY(final select.SELECT<? extends Subject<T>> subQuery) {
    return new qualified.ANY<T>(subQuery);
  }

  public static <T>qualified.SOME<T> SOME(final select.SELECT<? extends Subject<T>> subQuery) {
    return new qualified.SOME<T>(subQuery);
  }

  public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final DataType<T> a, final DataType<T> b) {
    return new BetweenPredicate<T>(true, dataType, a, b);
  }

  public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final DataType<T> a, final T b) {
    return new BetweenPredicate<T>(true, dataType, a, DataType.wrap(b));
  }

  public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final T a, final DataType<T> b) {
    return new BetweenPredicate<T>(true, dataType, DataType.wrap(a), b);
  }

  public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final T a, final T b) {
    return new BetweenPredicate<T>(true, dataType, DataType.wrap(a), DataType.wrap(b));
  }

  public static Predicate<String> LIKE(final Char a, final CharSequence b) {
    return new LikePredicate(true, a, b);
  }

  public static <T>Predicate<T> IN(final DataType<T> a, final Collection<T> b) {
    return new InPredicate<T>(true, a, b);
  }

  @SafeVarargs
  public static <T>Predicate<T> IN(final DataType<T> a, final T ... b) {
    return new InPredicate<T>(true, a, b);
  }

  public static <T>Predicate<T> IN(final DataType<T> a, final select.SELECT<? extends DataType<T>> b) {
    return new InPredicate<T>(true, a, b);
  }

  public static <T>Predicate<T> EXISTS(final select.SELECT<?> subQuery) {
    return new ExistsPredicate<T>(subQuery);
  }

  public static final class NOT {
    public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final DataType<T> a, final DataType<T> b) {
      return new BetweenPredicate<T>(false, dataType, a, b);
    }

    public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final DataType<T> a, final T b) {
      return new BetweenPredicate<T>(false, dataType, a, DataType.wrap(b));
    }

    public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final T a, final DataType<T> b) {
      return new BetweenPredicate<T>(false, dataType, DataType.wrap(a), b);
    }

    public static <T>Predicate<T> BETWEEN(final DataType<T> dataType, final T a, final T b) {
      return new BetweenPredicate<T>(false, dataType, DataType.wrap(a), DataType.wrap(b));
    }

    public static Predicate<String> LIKE(final Char a, final String b) {
      return new LikePredicate(false, a, b);
    }

    public static <T>Predicate<T> IN(final DataType<T> a, final Collection<T> b) {
      return new InPredicate<T>(true, a, b);
    }

    @SafeVarargs
    public static <T>Predicate<T> IN(final DataType<T> a, final T ... b) {
      return new InPredicate<T>(false, a, b);
    }

    public static <T>Predicate<T> IN(final DataType<T> a, final select.SELECT<? extends DataType<T>> b) {
      return new InPredicate<T>(false, a, b);
    }
  }

  public static final class IS {
    public static final class NOT {
      public static <T>Predicate<T> NULL(final DataType<T> dataType) {
        return new NullPredicate<T>(false, dataType);
      }
    }

    public static <T>Predicate<T> NULL(final DataType<T> dataType) {
      return new NullPredicate<T>(true, dataType);
    }
  }

  private DML() {
  }
}