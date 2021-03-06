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

package org.jaxdb.jsql;

import java.io.IOException;
import java.util.Set;

public final class Cast {
  static final class AS extends Provision {
    final kind.DataType<?> dataType;
    final type.DataType<?> cast;
    final Integer length;
    final Integer scale;

    AS(final kind.DataType<?> dataType, final type.DataType<?> castAs, final Integer length) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = length;
      this.scale = null;
    }

    AS(final kind.DataType<?> dataType, final type.DataType<?> castAs) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = null;
      this.scale = null;
    }

    AS(final kind.DataType<?> dataType, final type.DataType<?> castAs, final Integer length, final Integer scale) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = length;
      this.scale = scale;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }

    @Override
    Object evaluate(final Set<Evaluable> visited) {
      return dataType instanceof Evaluable ? ((Evaluable)dataType).evaluate(visited) : null;
    }
  }

  public static final class BOOLEAN {
    public final class AS {
      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final Integer length) {
        final type.CLOB cast = new type.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.BOOLEAN value;

    public BOOLEAN(final kind.BOOLEAN value) {
      this.value = value;
    }
  }

  public static final class FLOAT {
    public final class AS {
      public type.FLOAT.UNSIGNED UNSIGNED() {
        final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public FLOAT(final kind.FLOAT value) {
      this.value = value;
    }

    public FLOAT(final kind.FLOAT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class DOUBLE {
    public final class AS {
      public type.DOUBLE.UNSIGNED UNSIGNED() {
        final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public DOUBLE(final kind.DOUBLE value) {
      this.value = value;
    }

    public DOUBLE(final kind.DOUBLE.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class DECIMAL {
    public final class AS {
      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.DECIMAL.UNSIGNED UNSIGNED() {
        final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
        final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public DECIMAL(final kind.DECIMAL value) {
      this.value = value;
    }

    public DECIMAL(final kind.DECIMAL.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class TINYINT {
    public final class AS {
      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.TINYINT.UNSIGNED UNSIGNED() {
        final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
        final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public TINYINT(final kind.TINYINT value) {
      this.value = value;
    }

    public TINYINT(final kind.TINYINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class SMALLINT {
    public final class AS {
      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.SMALLINT.UNSIGNED UNSIGNED() {
        final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
        final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public SMALLINT(final kind.SMALLINT value) {
      this.value = value;
    }

    public SMALLINT(final kind.SMALLINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class INT {
    public final class AS {
      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INT.UNSIGNED UNSIGNED() {
        final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
        final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public INT(final kind.INT value) {
      this.value = value;
    }

    public INT(final kind.INT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class BIGINT {
    public final class AS {
      public type.FLOAT FLOAT() {
        final type.FLOAT cast = new type.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public type.FLOAT.UNSIGNED UNSIGNED() {
          final type.FLOAT.UNSIGNED cast = new type.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public type.DOUBLE DOUBLE() {
        final type.DOUBLE cast = new type.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public type.DOUBLE.UNSIGNED UNSIGNED() {
          final type.DOUBLE.UNSIGNED cast = new type.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT.UNSIGNED UNSIGNED() {
        final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
        final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Numeric<?> value;

    public BIGINT(final kind.BIGINT value) {
      this.value = value;
    }

    public BIGINT(final kind.BIGINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class CHAR {
    public final class AS {
      public type.DECIMAL DECIMAL() {
        final type.DECIMAL cast = new type.DECIMAL();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final Integer precision, final Integer scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED() {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.DECIMAL.UNSIGNED UNSIGNED(final Integer precision, final Integer scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast, precision, scale));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.TINYINT TINYINT() {
        final type.TINYINT cast = new type.TINYINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TINYINT TINYINT(final Integer precision) {
        final type.TINYINT cast = new type.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public type.TINYINT.UNSIGNED UNSIGNED() {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.TINYINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.TINYINT.UNSIGNED cast = new type.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public type.SMALLINT SMALLINT() {
        final type.SMALLINT cast = new type.SMALLINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final Integer precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED() {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.SMALLINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision == null ? null : precision.shortValue());
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.INT INT() {
        final type.INT cast = new type.INT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.INT INT(final Integer precision) {
        final type.INT cast = new type.INT(precision == null ? null : precision.shortValue());
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED() {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.INT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT() {
        final type.BIGINT cast = new type.BIGINT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.BIGINT BIGINT(final Integer precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED() {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }

        public type.BIGINT.UNSIGNED UNSIGNED(final Integer precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final Integer length) {
        final type.CLOB cast = new type.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final Integer precision) {
        final type.TIME cast = new type.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME(final Integer precision) {
        final type.DATETIME cast = new type.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME() {
        final type.DATETIME cast = new type.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.Textual<?> value;

    public CHAR(final kind.Textual<?> value) {
      this.value = value;
    }
  }

  public static final class DATE {
    public final class AS {
      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.DATE value;

    public DATE(final kind.DATE value) {
      this.value = value;
    }
  }

  public static final class TIME {
    public final class AS {
      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.TIME TIME(final Integer precision) {
        final type.TIME cast = new type.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.TIME value;

    public TIME(final kind.TIME value) {
      this.value = value;
    }
  }

  public static final class DATETIME {
    public final class AS {
      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final Integer precision) {
        final type.TIME cast = new type.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME(final Integer precision) {
        final type.DATETIME cast = new type.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME() {
        final type.DATETIME cast = new type.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.DATETIME value;

    public DATETIME(final kind.DATETIME value) {
      this.value = value;
    }
  }

  public static final class CLOB {
    public final class AS {
      public type.CHAR CHAR(final Integer length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final Integer length) {
        final type.CLOB cast = new type.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.CLOB value;

    public CLOB(final kind.CLOB value) {
      this.value = value;
    }
  }

  public static final class BLOB {
    public final class AS {
      public type.BLOB BLOB(final Integer length) {
        final type.BLOB cast = new type.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.BLOB value;

    public BLOB(final kind.BLOB value) {
      this.value = value;
    }
  }

  public static final class BINARY {
    public final class AS {
      public type.BLOB BLOB(final Integer length) {
        final type.BLOB cast = new type.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.BINARY BINARY(final int length) {
        final type.BINARY cast = new type.BINARY(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final kind.BINARY value;

    public BINARY(final kind.BINARY value) {
      this.value = value;
    }
  }

  private Cast() {
  }
}