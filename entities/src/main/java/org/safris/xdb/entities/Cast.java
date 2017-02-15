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

public class Cast {
  protected static final class AS extends Provision {
    protected final type.DataType<?> castAs;
    protected final type.DataType<?> dataType;
    protected final Integer length;
    protected final Integer scale;

    protected AS(final type.DataType<?> dataType, final type.DataType<?> castAs, final int length) {
      this.dataType = dataType;
      this.castAs = castAs;
      this.length = length;
      this.scale = null;
    }

    protected AS(final type.DataType<?> dataType, final type.DataType<?> castAs) {
      this.dataType = dataType;
      this.castAs = castAs;
      this.length = null;
      this.scale = null;
    }

    protected AS(final type.DataType<?> dataType, final type.DataType<?> castAs, final int length, final int scale) {
      this.dataType = dataType;
      this.castAs = castAs;
      this.length = length;
      this.scale = scale;
    }

    @Override
    protected void serialize(final Serialization serialization) throws IOException {
      Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
    }
  }

  public static final class BOOLEAN {
    public final class AS {
      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, true);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        final type.CLOB cast = new type.CLOB(length);
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final type.FLOAT value;

    public FLOAT(final type.FLOAT value) {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();
    }

    public final AS AS = new AS();

    private final type.DOUBLE value;

    public DOUBLE(final type.DOUBLE value) {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
        final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DECIMAL value;

    public DECIMAL(final type.DECIMAL value) {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
        final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.SMALLINT value;

    public SMALLINT(final type.SMALLINT value) {
      this.value = value;
    }
  }

  public static final class MEDIUMINT {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
        final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.MEDIUMINT value;

    public MEDIUMINT(final type.MEDIUMINT value) {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INT.UNSIGNED UNSIGNED(final int precision) {
        final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.INT value;

    public INT(final type.INT value) {
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

      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
        final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BIGINT value;

    public BIGINT(final type.BIGINT value) {
      this.value = value;
    }
  }

  public static final class CHAR {
    public final class AS {
      public type.DECIMAL DECIMAL(final int precision, final int scale) {
        final type.DECIMAL cast = new type.DECIMAL(precision, scale);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public final class DECIMAL {
        public type.DECIMAL.UNSIGNED UNSIGNED(final int precision, final int scale) {
          final type.DECIMAL.UNSIGNED cast = new type.DECIMAL.UNSIGNED(precision, scale);
          cast.wrapper(new Cast.AS(value, cast));
          return cast;
        }
      }

      public final DECIMAL DECIMAL = new DECIMAL();

      public type.SMALLINT SMALLINT(final int precision) {
        final type.SMALLINT cast = new type.SMALLINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class SMALLINT {
        public type.SMALLINT.UNSIGNED UNSIGNED(final int precision) {
          final type.SMALLINT.UNSIGNED cast = new type.SMALLINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final SMALLINT SMALLINT = new SMALLINT();

      public type.MEDIUMINT MEDIUMINT(final int precision) {
        final type.MEDIUMINT cast = new type.MEDIUMINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class MEDIUMINT {
        public type.MEDIUMINT.UNSIGNED UNSIGNED(final int precision) {
          final type.MEDIUMINT.UNSIGNED cast = new type.MEDIUMINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final MEDIUMINT MEDIUMINT = new MEDIUMINT();

      public type.INT INT(final int precision) {
        final type.INT cast = new type.INT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class INT {
        public type.INT.UNSIGNED UNSIGNED(final int precision) {
          final type.INT.UNSIGNED cast = new type.INT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final INT INT = new INT();

      public type.BIGINT BIGINT(final int precision) {
        final type.BIGINT cast = new type.BIGINT(precision);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class BIGINT {
        public type.BIGINT.UNSIGNED UNSIGNED(final int precision) {
          final type.BIGINT.UNSIGNED cast = new type.BIGINT.UNSIGNED(precision);
          cast.wrapper(new Cast.AS(value, cast, precision));
          return cast;
        }
      }

      public final BIGINT BIGINT = new BIGINT();

      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        final type.CLOB cast = new type.CLOB(length);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final int precision) {
        final type.TIME cast = new type.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME(final int precision) {
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

    private final type.Textual<?> value;

    public CHAR(final type.Textual<?> value) {
      this.value = value;
    }
  }

  public static final class DATE {
    public final class AS {
      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
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
      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.TIME TIME(final int precision) {
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

    private final type.TIME value;

    public TIME(final type.TIME value) {
      this.value = value;
    }
  }

  public static final class DATETIME {
    public final class AS {
      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final int precision) {
        final type.TIME cast = new type.TIME(precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME(final int precision) {
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

    private final type.DATETIME value;

    public DATETIME(final type.DATETIME value) {
      this.value = value;
    }
  }

  public static final class CLOB {
    public final class AS {
      public type.CHAR CHAR(final int length) {
        final type.CHAR cast = new type.CHAR(length, false);
        cast.wrapper(new Cast.AS(value, cast, length));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        final type.CLOB cast = new type.CLOB(length);
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
      public type.BLOB BLOB(final int length) {
        final type.BLOB cast = new type.BLOB(length);
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
      public type.BLOB BLOB(final int length) {
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

    private final type.BINARY value;

    public BINARY(final type.BINARY value) {
      this.value = value;
    }
  }
}