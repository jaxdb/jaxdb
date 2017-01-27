/* Copyright (c) 2017 Seva Safris
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

import java.io.IOException;

import org.safris.xdb.entities.data.Numeric;

final class function {
  static final class numeric {
    static final class Pi extends NumericFunction<Double> {
      protected Pi() {
        super();
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Abs<T extends Number> extends NumericFunction<T> {
      protected Abs(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sign<T extends Number> extends NumericFunction<T> {
      protected Sign(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Round<T extends Number> extends NumericFunction<T> {
      protected Round(final DataType<T> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Round(final DataType<T> a, final Number b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Floor<T extends Number> extends NumericFunction<T> {
      protected Floor(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Ceil<T extends Number> extends NumericFunction<T> {
      protected Ceil(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sqrt<T extends Number> extends NumericFunction<T> {
      protected Sqrt(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Pow<T extends Number> extends NumericFunction<T> {
      protected Pow(final DataType<T> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Pow(final DataType<T> a, final Number b) {
        super(a, b);
      }


      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Mod<T extends Number> extends NumericFunction<T> {
      protected Mod(final DataType<T> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Mod(final DataType<T> a, final Number b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Sin<T extends Number> extends NumericFunction<T> {
      protected Sin(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Asin<T extends Number> extends NumericFunction<T> {
      protected Asin(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Cos<T extends Number> extends NumericFunction<T> {
      protected Cos(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Acos<T extends Number> extends NumericFunction<T> {
      protected Acos(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Tan<T extends Number> extends NumericFunction<T> {
      protected Tan(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Atan<T extends Number> extends NumericFunction<T> {
      protected Atan(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Atan2<T extends Number> extends NumericFunction<T> {
      protected Atan2(final DataType<T> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Atan2(final DataType<T> a, final Number b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Exp<T extends Number> extends NumericFunction<T> {
      protected Exp(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Ln<T extends Number> extends NumericFunction<T> {
      protected Ln(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log<T extends Number> extends NumericFunction<T> {
      protected Log(final DataType<T> a, final Numeric<?> b) {
        super(a, b);
      }

      protected Log(final DataType<T> a, final Number b) {
        super(a, b);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log2<T extends Number> extends NumericFunction<T> {
      protected Log2(final DataType<T> a) {
        super(a);
      }

      @Override
      protected void serialize(final Serialization serialization) throws IOException {
        Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
      }
    }

    static final class Log10<T extends Number> extends NumericFunction<T> {
      protected Log10(final DataType<T> a) {
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