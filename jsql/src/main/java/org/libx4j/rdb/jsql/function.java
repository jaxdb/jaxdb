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

package org.libx4j.rdb.jsql;

import java.io.IOException;

import org.libx4j.rdb.jsql.type.Numeric;

final class function {
  static final class numeric {
    static final class Pi extends NumericFunction {
      protected Pi() {
        super();
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Abs extends NumericFunction {
      protected Abs(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sign extends NumericFunction {
      protected Sign(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Round extends NumericFunction {
      protected Round(final type.Numeric<? extends Number> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Round(final type.Numeric<? extends Number> a, final Number b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Floor extends NumericFunction {
      protected Floor(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Ceil extends NumericFunction {
      protected Ceil(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sqrt extends NumericFunction {
      protected Sqrt(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Pow extends NumericFunction {
      protected Pow(final type.Numeric<? extends Number> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Pow(final type.Numeric<? extends Number> a, final Number b) {
        super(a, b);
      }

      protected Pow(final Number a, final type.Numeric<? extends Number> b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Mod extends NumericFunction {
      protected Mod(final type.Numeric<? extends Number> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Mod(final type.Numeric<? extends Number> a, final Number b) {
        super(a, b);
      }

      protected Mod(final Number a, final type.Numeric<? extends Number> b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sin extends NumericFunction {
      protected Sin(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Asin extends NumericFunction {
      protected Asin(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Cos extends NumericFunction {
      protected Cos(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Acos extends NumericFunction {
      protected Acos(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Tan extends NumericFunction {
      protected Tan(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Atan extends NumericFunction {
      protected Atan(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Atan2 extends NumericFunction {
      protected Atan2(final type.Numeric<? extends Number> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Atan2(final type.Numeric<? extends Number> a, final Number b) {
        super(a, b);
      }

      protected Atan2(final Number a, final type.Numeric<? extends Number> b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Exp extends NumericFunction {
      protected Exp(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Ln extends NumericFunction {
      protected Ln(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log extends NumericFunction {
      protected Log(final type.Numeric<? extends Number> b, final Numeric<?> n) {
        super(b, n);
      }

      protected Log(final type.Numeric<? extends Number> b, final Number n) {
        super(b, n);
      }

      protected Log(final Number b, final type.Numeric<? extends Number> n) {
        super(b, n);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log2 extends NumericFunction {
      protected Log2(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log10 extends NumericFunction {
      protected Log10(final type.Numeric<? extends Number> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    private numeric() {
    }
  }

  private function() {
  }
}