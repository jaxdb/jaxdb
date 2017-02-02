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
  protected static final class AS extends Provision<Subject<?>> {
    protected final type.DataType<?> castAs;
    protected final type.DataType<?> dataType;
    protected final Integer length;
    protected final Integer scale;

    protected AS(final type.DataType<?> dataType, final type.DataType<?> castAs, final int length, final boolean varying) {
      this.dataType = dataType;
      this.castAs = castAs;
      this.length = length;
      this.scale = null;
    }

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
      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        assert(length > 0);
        final type.CLOB cast = new type.CLOB((short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
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
      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.FLOAT value;

    public FLOAT(final type.FLOAT value) {
      this.value = value;
    }
  }

  public static final class DOUBLE {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DOUBLE value;

    public DOUBLE(final type.DOUBLE value) {
      this.value = value;
    }
  }

  public static final class DECIMAL {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DECIMAL value;

    public DECIMAL(final type.DECIMAL value) {
      this.value = value;
    }
  }

  public static final class TINYINT {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.TINYINT value;

    public TINYINT(final type.TINYINT value) {
      this.value = value;
    }
  }

  public static final class MEDIUMINT {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.MEDIUMINT value;

    public MEDIUMINT(final type.MEDIUMINT value) {
      this.value = value;
    }
  }

  public static final class INTEGER {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.INTEGER value;

    public INTEGER(final type.INTEGER value) {
      this.value = value;
    }
  }

  public static final class BIGINT {
    public final class AS {
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
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
      public type.FLOAT FLOAT(final boolean unsigned) {
        final type.FLOAT cast = new type.FLOAT(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DOUBLE DOUBLE(final boolean unsigned) {
        final type.DOUBLE cast = new type.DOUBLE(unsigned);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DECIMAL DECIMAL(final int precision, final int scale, final boolean unsigned) {
        assert((short)precision > 0 && scale > 0);
        final type.DECIMAL cast = new type.DECIMAL((short)precision, (short)scale, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision, scale));
        return cast;
      }

      public type.TINYINT TINYINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 3)
          throw new IllegalArgumentException("TINYINT has 2^8 = 256 values ((short)precision 3)");

        final type.TINYINT cast = new type.TINYINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.MEDIUMINT MEDIUMINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 8)
          throw new IllegalArgumentException("MEDIUMINT has 2^24 = 16777216 values ((short)precision 8)");

        final type.MEDIUMINT cast = new type.MEDIUMINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.INTEGER INTEGER(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 10)
          throw new IllegalArgumentException("INTEGER has 2^32 = 4294967296 values ((short)precision 10)");

        final type.INTEGER cast = new type.INTEGER((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public type.BIGINT BIGINT(final int precision, final boolean unsigned) {
        assert((short)precision > 0);
        if ((short)precision > 19)
          throw new IllegalArgumentException("BIGINT has maximum precision 19");

        final type.BIGINT cast = new type.BIGINT((short)precision, unsigned);
        cast.wrapper(new Cast.AS(value, cast, precision));
        return cast;
      }

      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        assert(length > 0);
        final type.CLOB cast = new type.CLOB((short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final int precision) {
        final type.TIME cast = new type.TIME((short)precision);
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME() {
        final type.TIME cast = new type.TIME();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.DATETIME DATETIME(final int precision) {
        final type.DATETIME cast = new type.DATETIME((short)precision);
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
      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
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
      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
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
      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }

      public type.DATE DATE() {
        final type.DATE cast = new type.DATE();
        cast.wrapper(new Cast.AS(value, cast));
        return cast;
      }

      public type.TIME TIME(final int precision) {
        final type.TIME cast = new type.TIME((short)precision);
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

    private final type.DATETIME value;

    public DATETIME(final type.DATETIME value) {
      this.value = value;
    }
  }

  public static final class CLOB {
    public final class AS {
      public final class VARYING {
        public type.CHAR CHAR(final int length) {
          assert(length > 0);
          final type.CHAR cast = new type.CHAR(true, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.CHAR CHAR(final int length) {
        assert(length > 0);
        final type.CHAR cast = new type.CHAR(false, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
        return cast;
      }

      public type.CLOB CLOB(final int length) {
        assert(length > 0);
        final type.CLOB cast = new type.CLOB((short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length, false));
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
        assert(length > 0);
        final type.BLOB cast = new type.BLOB((short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length));
        return cast;
      }

      public final class VARYING {
        public type.BINARY BINARY(final boolean varying, final int length) {
          assert(length > 0);
          final type.BINARY cast = new type.BINARY(varying, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.BINARY BINARY(final boolean varying, final int length) {
        assert(length > 0);
        final type.BINARY cast = new type.BINARY(varying, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length));
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
        assert(length > 0);
        final type.BLOB cast = new type.BLOB((short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length));
        return cast;
      }

      public final class VARYING {
        public type.BINARY BINARY(final boolean varying, final int length) {
          assert(length > 0);
          final type.BINARY cast = new type.BINARY(varying, (short)length);
          cast.wrapper(new Cast.AS(value, cast, (short)length, true));
          return cast;
        }
      }

      public final VARYING VARYING = new VARYING();

      public type.BINARY BINARY(final boolean varying, final int length) {
        assert(length > 0);
        final type.BINARY cast = new type.BINARY(varying, (short)length);
        cast.wrapper(new Cast.AS(value, cast, (short)length));
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