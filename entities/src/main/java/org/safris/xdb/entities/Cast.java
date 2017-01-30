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

public class Cast {
  protected static final class AS extends Provision<Subject<?>> {
    private final DataType<?> dataType;
    private final Integer length;
    private final Integer decimal;

    protected AS(final DataType<?> dataType, final int length, final boolean varying, final boolean national) {
      this.dataType = dataType;
      this.length = length;
      this.decimal = null;
    }

    protected AS(final DataType<?> dataType, final int length) {
      this.dataType = dataType;
      this.length = length;
      this.decimal = null;
    }

    protected AS(final DataType<?> dataType) {
      this.dataType = dataType;
      this.length = null;
      this.decimal = null;
    }

    protected AS(final DataType<?> dataType, final int length, final int decimal) {
      this.dataType = dataType;
      this.length = length;
      this.decimal = decimal;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
    }
  }

  public static final class Boolean {
    public final class AS {
      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }

          public type.Clob Clob(final int length) {
            final type.Clob cast = new type.Clob();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }

      public type.Clob Clob(final int length) {
        final type.Clob cast = new type.Clob();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Boolean value;

    public Boolean(final type.Boolean value) {
      this.value = value;
    }
  }

  public static final class Float {
    public final class AS {
      public type.Double Double() {
        final type.Double cast = new type.Double();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Float value;

    public Float(final type.Float value) {
      this.value = value;
    }
  }

  public static final class Double {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Double value;

    public Double(final type.Double value) {
      this.value = value;
    }
  }

  public static final class Decimal {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Double Double() {
        final type.Double cast = new type.Double();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Decimal value;

    public Decimal(final type.Decimal value) {
      this.value = value;
    }
  }

  public static final class SmallInt {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Double Double() {
        final type.Double cast = new type.Double();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.SmallInt value;

    public SmallInt(final type.SmallInt value) {
      this.value = value;
    }
  }

  public static final class MediumInt {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Double Double() {
        final type.Double cast = new type.Double();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.MediumInt value;

    public MediumInt(final type.MediumInt value) {
      this.value = value;
    }
  }

  public static final class Long {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Double Double() {
        final type.Double cast = new type.Double();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Long value;

    public Long(final type.Long value) {
      this.value = value;
    }
  }

  public static final class BigInt {
    public final class AS {
      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.BigInt value;

    public BigInt(final type.BigInt value) {
      this.value = value;
    }
  }

  public static final class Char {
    public final class AS {
      public type.Boolean Boolean() {
        return null;
      }

      public type.Float Float() {
        final type.Float cast = new type.Float();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Decimal Decimal(final int precision, final int decimal) {
        final type.Decimal cast = new type.Decimal();
        cast.wrapper(new Cast.AS(value, precision, decimal));
        return cast;
      }

      public type.SmallInt SmallInt(final int precision) {
        final type.SmallInt cast = new type.SmallInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.MediumInt MediumInt(final int precision) {
        final type.MediumInt cast = new type.MediumInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.Long Long(final int precision) {
        final type.Long cast = new type.Long();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public type.BigInt BigInt(final int precision) {
        final type.BigInt cast = new type.BigInt();
        cast.wrapper(new Cast.AS(value, precision));
        return cast;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }

          public type.Clob Clob(final int length) {
            final type.Clob cast = new type.Clob();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }

      public type.Clob Clob(final int length) {
        final type.Clob cast = new type.Clob();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }

      public type.Date Date() {
        final type.Date cast = new type.Date();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Time Time() {
        final type.Time cast = new type.Time();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.DateTime DateTime() {
        final type.DateTime cast = new type.DateTime();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Textual<?> value;

    public Char(final type.Textual<?> value) {
      this.value = value;
    }
  }

  public static final class Date {
    public final class AS {
      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Date value;

    public Date(final type.Date value) {
      this.value = value;
    }
  }

  public static final class Time {
    public final class AS {
      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Time value;

    public Time(final type.Time value) {
      this.value = value;
    }
  }

  public static final class DateTime {
    public final class AS {
      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }

      public type.Date Date() {
        final type.Date cast = new type.Date();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }

      public type.Time Time() {
        final type.Time cast = new type.Time();
        cast.wrapper(new Cast.AS(value));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.DateTime value;

    public DateTime(final type.DateTime value) {
      this.value = value;
    }
  }

  public static final class Clob {
    public final class AS {
      public type.Boolean Boolean(final int length) {
        return null;
      }

      public class NATIONAL {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, false, true));
          return cast;
        }

        public class VARYING {
          public type.Char Char(final int length) {
            final type.Char cast = new type.Char();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }

          public type.Clob Clob(final int length) {
            final type.Clob cast = new type.Clob();
            cast.wrapper(new Cast.AS(value, length, true, true));
            return cast;
          }
        }
      }

      public class VARYING {
        public type.Char Char(final int length) {
          final type.Char cast = new type.Char();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }

        public type.Clob Clob(final int length) {
          final type.Clob cast = new type.Clob();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Char Char(final int length) {
        final type.Char cast = new type.Char();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }

      public type.Clob Clob(final int length) {
        final type.Clob cast = new type.Clob();
        cast.wrapper(new Cast.AS(value, length, false, false));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Clob value;

    public Clob(final type.Clob value) {
      this.value = value;
    }
  }

  public static final class Blob {
    public final class AS {
      public type.Blob Blob(final int length) {
        final type.Blob cast = new type.Blob();
        cast.wrapper(new Cast.AS(value, length));
        return cast;
      }

      public class VARYING {
        public type.Binary Binary(final int length) {
          final type.Binary cast = new type.Binary();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Binary Binary(final int length) {
        final type.Binary cast = new type.Binary();
        cast.wrapper(new Cast.AS(value, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Blob value;

    public Blob(final type.Blob value) {
      this.value = value;
    }
  }

  public static final class Binary {
    public final class AS {
      public type.Blob Blob(final int length) {
        final type.Blob cast = new type.Blob();
        cast.wrapper(new Cast.AS(value, length));
        return cast;
      }

      public class VARYING {
        public type.Binary Binary(final int length) {
          final type.Binary cast = new type.Binary();
          cast.wrapper(new Cast.AS(value, length, true, false));
          return cast;
        }
      }

      public type.Binary Binary(final int length) {
        final type.Binary cast = new type.Binary();
        cast.wrapper(new Cast.AS(value, length));
        return cast;
      }
    }

    public final AS AS = new AS();

    private final type.Binary value;

    public Binary(final type.Binary value) {
      this.value = value;
    }
  }
}