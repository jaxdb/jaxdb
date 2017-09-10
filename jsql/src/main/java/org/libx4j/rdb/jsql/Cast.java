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
import java.util.Set;

public final class Cast {
  protected static final class AS extends Provision {
    protected final type.DataType<?> dataType;
    protected final data.DataType<?> cast;
    protected final Integer length;
    protected final Integer scale;

    protected AS(final type.DataType<?> dataType, final data.DataType<?> castAs, final int length) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = length;
      this.scale = null;
    }

    protected AS(final type.DataType<?> dataType, final data.DataType<?> castAs) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = null;
      this.scale = null;
    }

    protected AS(final type.DataType<?> dataType, final data.DataType<?> castAs, final int length, final int scale) {
      this.dataType = dataType;
      this.cast = castAs;
      this.length = length;
      this.scale = scale;
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    protected Object evaluate(final Set<Evaluable> visited) {
      return dataType instanceof Evaluable ? ((Evaluable)dataType).evaluate(visited) : null;
    }
  }

  public static final class BOOLEAN {
    public final class AS {
      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final int length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BOOLEAN value;

    public BOOLEAN(final type.BOOLEAN value) {
      this.value = value;
    }
  }

  public static final class FLOAT {
    public final class AS {
      public data.FLOAT.UNSIGNED UNSIGNED() {
        final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public FLOAT(final type.FLOAT value) {
      this.value = value;
    }

    public FLOAT(final type.FLOAT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class DOUBLE {
    public final class AS {
      public data.DOUBLE.UNSIGNED UNSIGNED() {
        final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public DOUBLE(final type.DOUBLE value) {
      this.value = value;
    }

    public DOUBLE(final type.DOUBLE.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class DECIMAL {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
        final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public DECIMAL(final type.DECIMAL value) {
      this.value = value;
    }

    public DECIMAL(final type.DECIMAL.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class TINYINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
        final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public TINYINT(final type.TINYINT value) {
      this.value = value;
    }

    public TINYINT(final type.TINYINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class SMALLINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
        final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public SMALLINT(final type.SMALLINT value) {
      this.value = value;
    }

    public SMALLINT(final type.SMALLINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class INT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.INT.UNSIGNED UNSIGNED(final int precision) {
        final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public INT(final type.INT value) {
      this.value = value;
    }

    public INT(final type.INT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class BIGINT {
    public final class AS {
      public data.FLOAT FLOAT() {
        final data.FLOAT cast = new data.FLOAT();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class FLOAT {
        public data.FLOAT.UNSIGNED UNSIGNED() {
          final data.FLOAT.UNSIGNED cast = new data.FLOAT.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final FLOAT FLOAT = new FLOAT();

      public data.DOUBLE DOUBLE() {
        final data.DOUBLE cast = new data.DOUBLE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public final class DOUBLE {
        public data.DOUBLE.UNSIGNED UNSIGNED() {
          final data.DOUBLE.UNSIGNED cast = new data.DOUBLE.UNSIGNED();
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DOUBLE DOUBLE = new DOUBLE();

      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
        final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Numeric<?> value;

    public BIGINT(final type.BIGINT value) {
      this.value = value;
    }

    public BIGINT(final type.BIGINT.UNSIGNED value) {
      this.value = value;
    }
  }

  public static final class CHAR {
    public final class AS {
      public data.DECIMAL DECIMAL(final int precision, final int scale) {
        final data.DECIMAL cast = new data.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public data.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final data.DECIMAL.UNSIGNED cast = new data.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public data.TINYINT TINYINT(final int precision) {
        final data.TINYINT cast = new data.TINYINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class TINYINT {
        public data.TINYINT.UNSIGNED UNSIGNED(final int precision) {
          final data.TINYINT.UNSIGNED cast = new data.TINYINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final TINYINT TINYINT = new TINYINT();

      public data.SMALLINT SMALLINT(final int precision) {
        final data.SMALLINT cast = new data.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public data.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final data.SMALLINT.UNSIGNED cast = new data.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public data.INT INT(final int precision) {
        final data.INT cast = new data.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public data.INT.UNSIGNED UNSIGNED(final int precision) {
          final data.INT.UNSIGNED cast = new data.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public data.BIGINT BIGINT(final int precision) {
        final data.BIGINT cast = new data.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public data.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final data.BIGINT.UNSIGNED cast = new data.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final int length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.DATE DATE() {
        final data.DATE cast = new data.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME(final int precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME(final int precision) {
        final data.DATETIME cast = new data.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME() {
        final data.DATETIME cast = new data.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Textual<?> value;

    public CHAR(final type.Textual<?> value) {
      this.value = value;
    }
  }

  public static final class DATE {
    public final class AS {
      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DATE value;

    public DATE(final type.DATE value) {
      this.value = value;
    }
  }

  public static final class TIME {
    public final class AS {
      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.TIME TIME(final int precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.TIME value;

    public TIME(final type.TIME value) {
      this.value = value;
    }
  }

  public static final class DATETIME {
    public final class AS {
      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.DATE DATE() {
        final data.DATE cast = new data.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME(final int precision) {
        final data.TIME cast = new data.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.TIME TIME() {
        final data.TIME cast = new data.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME(final int precision) {
        final data.DATETIME cast = new data.DATETIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public data.DATETIME DATETIME() {
        final data.DATETIME cast = new data.DATETIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DATETIME value;

    public DATETIME(final type.DATETIME value) {
      this.value = value;
    }
  }

  public static final class CLOB {
    public final class AS {
      public data.CHAR CHAR(final int length) {
        final data.CHAR cast = new data.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.CLOB CLOB(final int length) {
        final data.CLOB cast = new data.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.CLOB value;

    public CLOB(final type.CLOB value) {
      this.value = value;
    }
  }

  public static final class BLOB {
    public final class AS {
      public data.BLOB BLOB(final int length) {
        final data.BLOB cast = new data.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BLOB value;

    public BLOB(final type.BLOB value) {
      this.value = value;
    }
  }

  public static final class BINARY {
    public final class AS {
      public data.BLOB BLOB(final int length) {
        final data.BLOB cast = new data.BLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public data.BINARY BINARY(final int length) {
        final data.BINARY cast = new data.BINARY(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BINARY value;

    public BINARY(final type.BINARY value) {
      this.value = value;
    }
  }

  private Cast() {
  }
}