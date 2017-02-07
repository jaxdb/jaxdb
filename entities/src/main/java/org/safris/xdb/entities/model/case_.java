/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.entities.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.safris.xdb.entities.Condition;
import org.safris.xdb.entities.type;

public interface case_ {
  public interface CASE<T> {
  }

  public interface simple {
    public interface CASE<T> extends case_.CASE<T> {
      public case_.simple.WHEN<T> WHEN(final T condition);
    }

    public interface WHEN<T> {
      public case_.simple.BOOLEAN.THEN THEN(final type.BOOLEAN bool);
      public case_.simple.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
      public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
      public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
      public case_.simple.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric);
      public case_.simple.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
      public case_.simple.INT.THEN<T> THEN(final type.INT numeric);
      public case_.simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
      public case_.simple.FLOAT.THEN<T> THEN(final float numeric);
      public case_.simple.BOOLEAN.THEN THEN(final boolean bool);
      public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
      public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
      public case_.simple.FLOAT.THEN<T> THEN(final short numeric);
      public case_.simple.MEDIUMINT.THEN<T> THEN(final int numeric);
      public case_.simple.INT.THEN<T> THEN(final long numeric);
      public case_.simple.BIGINT.THEN<T> THEN(final BigInteger numeric);
      public case_.simple.CLOB.THEN<T> THEN(final type.CLOB clob);
      public case_.simple.BLOB.THEN<T> THEN(final type.BLOB clob);
      public case_.simple.BINARY.THEN<T> THEN(final type.BINARY binary);
      public case_.simple.DATE.THEN<T> THEN(final type.DATE date);
      public case_.simple.TIME.THEN<T> THEN(final type.TIME time);
      public case_.simple.DATETIME.THEN<T> THEN(final type.DATETIME dateTime);
      public case_.simple.CHAR.THEN<T> THEN(final type.CHAR text);
      public case_.simple.ENUM.THEN<T> THEN(final type.ENUM<?> dateTime);
      public case_.simple.BINARY.THEN<T> THEN(final byte[] binary);
      public case_.simple.DATE.THEN<T> THEN(final LocalDate date);
      public case_.simple.TIME.THEN<T> THEN(final LocalTime time);
      public case_.simple.DATETIME.THEN<T> THEN(final LocalDateTime dateTime);
      public case_.simple.CHAR.THEN<T> THEN(final String text);
      public case_.simple.ENUM.THEN<T> THEN(final Enum<?> dateTime);
    }

    public interface BOOLEAN {
      public interface WHEN {
        public case_.simple.BOOLEAN.THEN THEN(final type.BOOLEAN bool);
        public case_.simple.BOOLEAN.THEN THEN(final boolean bool);
      }

      public interface THEN {
        public case_.simple.BOOLEAN.ELSE ELSE(final type.BOOLEAN bool);
        public case_.simple.BOOLEAN.ELSE ELSE(final boolean bool);
        public case_.simple.BOOLEAN.WHEN WHEN(final boolean condition);
      }

      public interface ELSE {
        public type.BOOLEAN END();
      }
    }

    public interface FLOAT {
      public interface WHEN<T> {
        public case_.simple.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.FLOAT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.INT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.FLOAT.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.FLOAT.THEN<T> THEN(final short numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final int numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final long numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.FLOAT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.FLOAT.ELSE<T> ELSE(final short numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final int numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final long numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.FLOAT.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.FLOAT END();
      }
    }

    public interface DOUBLE {
      public interface WHEN<T> {
        public case_.simple.DOUBLE.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.INT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final short numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final int numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final long numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final short numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final int numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final long numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.DOUBLE.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.DOUBLE END();
      }
    }

    public interface DECIMAL {
      public interface WHEN<T> {
        public case_.simple.DECIMAL.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.INT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final float numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final short numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final int numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final long numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final float numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final short numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final int numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final long numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.DECIMAL.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.DECIMAL END();
      }
    }

    public interface SMALLINT {
      public interface WHEN<T> {
        public case_.simple.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.INT.THEN<T> THEN(final type.INT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.FLOAT.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.SMALLINT.THEN<T> THEN(final short numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final int numeric);
        public case_.simple.INT.THEN<T> THEN(final long numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.SMALLINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.SMALLINT.ELSE<T> ELSE(final short numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final int numeric);
        public case_.simple.INT.ELSE<T> ELSE(final long numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.SMALLINT.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.SMALLINT END();
      }
    }

    public interface MEDIUMINT {
      public interface WHEN<T> {
        public case_.simple.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.INT.THEN<T> THEN(final type.INT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.FLOAT.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final short numeric);
        public case_.simple.MEDIUMINT.THEN<T> THEN(final int numeric);
        public case_.simple.INT.THEN<T> THEN(final long numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final short numeric);
        public case_.simple.MEDIUMINT.ELSE<T> ELSE(final int numeric);
        public case_.simple.INT.ELSE<T> ELSE(final long numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.MEDIUMINT.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.MEDIUMINT END();
      }
    }

    public interface INT {
      public interface WHEN<T> {
        public case_.simple.DOUBLE.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.INT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.INT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.INT.THEN<T> THEN(final type.INT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.INT.THEN<T> THEN(final short numeric);
        public case_.simple.INT.THEN<T> THEN(final int numeric);
        public case_.simple.INT.THEN<T> THEN(final long numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.INT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.INT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.INT.ELSE<T> ELSE(final short numeric);
        public case_.simple.INT.ELSE<T> ELSE(final int numeric);
        public case_.simple.INT.ELSE<T> ELSE(final long numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.INT.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.INT END();
      }
    }

    public interface BIGINT {
      public interface WHEN<T> {
        public case_.simple.DOUBLE.THEN<T> THEN(final type.FLOAT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.INT numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final float numeric);
        public case_.simple.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final short numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final int numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final long numeric);
        public case_.simple.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.INT numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final float numeric);
        public case_.simple.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.simple.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final short numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final int numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final long numeric);
        public case_.simple.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.simple.BIGINT.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.BIGINT END();
      }
    }

    public interface CLOB {
      public interface WHEN<T> {
        public case_.simple.CLOB.THEN<T> THEN(final type.CLOB clob);
      }

      public interface THEN<T> {
        public case_.simple.CLOB.ELSE<T> ELSE(final type.CLOB clob);
        public case_.simple.CLOB.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.CLOB END();
      }
    }

    public interface BLOB {
      public interface WHEN<T> {
        public case_.simple.BLOB.THEN<T> THEN(final type.BLOB blob);
      }

      public interface THEN<T> {
        public case_.simple.BLOB.ELSE<T> ELSE(final type.BLOB blob);
        public case_.simple.BLOB.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.BLOB END();
      }
    }

    public interface BINARY {
      public interface WHEN<T> {
        public case_.simple.BINARY.THEN<T> THEN(final type.BINARY binary);
        public case_.simple.BINARY.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> {
        public case_.simple.BINARY.ELSE<T> ELSE(final type.BINARY binary);
        public case_.simple.BINARY.ELSE<T> ELSE(final byte[] binary);
        public case_.simple.BINARY.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.BINARY END();
      }
    }

    public interface DATE {
      public interface WHEN<T> {
        public case_.simple.DATE.THEN<T> THEN(final type.DATE date);
        public case_.simple.DATE.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> {
        public case_.simple.DATE.ELSE<T> ELSE(final type.DATE date);
        public case_.simple.DATE.ELSE<T> ELSE(final LocalDate date);
        public case_.simple.DATE.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.DATE END();
      }
    }

    public interface TIME {
      public interface WHEN<T> {
        public case_.simple.TIME.THEN<T> THEN(final type.TIME time);
        public case_.simple.TIME.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> {
        public case_.simple.TIME.ELSE<T> ELSE(final type.TIME time);
        public case_.simple.TIME.ELSE<T> ELSE(final LocalTime time);
        public case_.simple.TIME.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.TIME END();
      }
    }

    public interface DATETIME {
      public interface WHEN<T> {
        public case_.simple.DATETIME.THEN<T> THEN(final type.DATETIME dateTime);
        public case_.simple.DATETIME.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> {
        public case_.simple.DATETIME.ELSE<T> ELSE(final type.DATETIME dateTime);
        public case_.simple.DATETIME.ELSE<T> ELSE(final LocalDateTime dateTime);
        public case_.simple.DATETIME.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.DATETIME END();
      }
    }

    public interface CHAR {
      public interface WHEN<T> {
        public case_.simple.CHAR.THEN<T> THEN(final type.ENUM<?> text);
        public case_.simple.CHAR.THEN<T> THEN(final type.CHAR text);
        public case_.simple.CHAR.THEN<T> THEN(final Enum<?> text);
        public case_.simple.CHAR.THEN<T> THEN(final String text);
      }

      public interface THEN<T> {
        public case_.simple.CHAR.ELSE<T> ELSE(final type.ENUM<?> text);
        public case_.simple.CHAR.ELSE<T> ELSE(final type.CHAR text);
        public case_.simple.CHAR.ELSE<T> ELSE(final Enum<?> text);
        public case_.simple.CHAR.ELSE<T> ELSE(final String text);
        public case_.simple.CHAR.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.CHAR END();
      }
    }

    public interface ENUM {
      public interface WHEN<T> {
        public case_.simple.ENUM.THEN<T> THEN(final type.ENUM<?> text);
        public case_.simple.CHAR.THEN<T> THEN(final type.CHAR text);
        public case_.simple.ENUM.THEN<T> THEN(final Enum<?> text);
        public case_.simple.CHAR.THEN<T> THEN(final String text);
      }

      public interface THEN<T> {
        public case_.simple.ENUM.ELSE<T> ELSE(final type.ENUM<?> text);
        public case_.simple.CHAR.ELSE<T> ELSE(final type.CHAR text);
        public case_.simple.ENUM.ELSE<T> ELSE(final Enum<?> text);
        public case_.simple.CHAR.ELSE<T> ELSE(final String text);
        public case_.simple.ENUM.WHEN<T> WHEN(final T condition);
      }

      public interface ELSE<T> {
        public type.ENUM<?> END();
      }
    }
  }

  public interface search {
    public interface WHEN<T> {
      public case_.search.BOOLEAN.THEN<T> THEN(final type.BOOLEAN bool);
      public case_.search.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
      public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
      public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
      public case_.search.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric);
      public case_.search.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
      public case_.search.INT.THEN<T> THEN(final type.INT numeric);
      public case_.search.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
      public case_.search.BOOLEAN.THEN<T> THEN(final boolean bool);
      public case_.search.FLOAT.THEN<T> THEN(final float numeric);
      public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
      public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
      public case_.search.FLOAT.THEN<T> THEN(final short numeric);
      public case_.search.MEDIUMINT.THEN<T> THEN(final int numeric);
      public case_.search.INT.THEN<T> THEN(final long numeric);
      public case_.search.BIGINT.THEN<T> THEN(final BigInteger numeric);
      public case_.search.CLOB.THEN<T> THEN(final type.CLOB clob);
      public case_.search.BLOB.THEN<T> THEN(final type.BLOB clob);
      public case_.search.BINARY.THEN<T> THEN(final type.BINARY binary);
      public case_.search.DATE.THEN<T> THEN(final type.DATE date);
      public case_.search.TIME.THEN<T> THEN(final type.TIME time);
      public case_.search.DATETIME.THEN<T> THEN(final type.DATETIME dateTime);
      public case_.search.CHAR.THEN<T> THEN(final type.CHAR text);
      public case_.search.ENUM.THEN<T> THEN(final type.ENUM<?> dateTime);
      public case_.search.BINARY.THEN<T> THEN(final byte[] binary);
      public case_.search.DATE.THEN<T> THEN(final LocalDate date);
      public case_.search.TIME.THEN<T> THEN(final LocalTime time);
      public case_.search.DATETIME.THEN<T> THEN(final LocalDateTime dateTime);
      public case_.search.CHAR.THEN<T> THEN(final String text);
      public case_.search.ENUM.THEN<T> THEN(final Enum<?> text);
    }

    public interface BOOLEAN {
      public interface CASE<T> {
        public case_.search.BOOLEAN.THEN<T> THEN(final type.BOOLEAN bool);
        public case_.search.BOOLEAN.THEN<T> THEN(final boolean bool);
      }

      public interface WHEN<T> {
        public case_.search.BOOLEAN.THEN<T> THEN(final type.BOOLEAN bool);
        public case_.search.BOOLEAN.THEN<T> THEN(final boolean bool);
      }

      public interface THEN<T> {
        public case_.search.BOOLEAN.ELSE<T> ELSE(final type.BOOLEAN bool);
        public case_.search.BOOLEAN.ELSE<T> ELSE(final boolean bool);
        public case_.search.BOOLEAN.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.BOOLEAN END();
      }
    }

    public interface FLOAT {
      public interface WHEN<T> {
        public case_.search.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.FLOAT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.INT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.FLOAT.THEN<T> THEN(final float numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.FLOAT.THEN<T> THEN(final short numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final int numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final long numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.FLOAT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.FLOAT.ELSE<T> ELSE(final short numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final int numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final long numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.FLOAT.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.FLOAT END();
      }
    }

    public interface DOUBLE {
      public interface WHEN<T> {
        public case_.search.DOUBLE.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.INT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final float numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final short numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final int numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final long numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final float numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final short numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final int numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final long numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.DOUBLE.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.DOUBLE END();
      }
    }

    public interface DECIMAL {
      public interface WHEN<T> {
        public case_.search.DECIMAL.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.INT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final float numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final short numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final int numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final long numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final float numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final short numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final int numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final long numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.DECIMAL.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.DECIMAL END();
      }
    }

    public interface SMALLINT {
      public interface WHEN<T> {
        public case_.search.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.INT.THEN<T> THEN(final type.INT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.FLOAT.THEN<T> THEN(final float numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.SMALLINT.THEN<T> THEN(final short numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final int numeric);
        public case_.search.INT.THEN<T> THEN(final long numeric);
        public case_.search.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.SMALLINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.SMALLINT.ELSE<T> ELSE(final short numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final int numeric);
        public case_.search.INT.ELSE<T> ELSE(final long numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.SMALLINT.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.SMALLINT END();
      }
    }

    public interface MEDIUMINT {
      public interface WHEN<T> {
        public case_.search.FLOAT.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.INT.THEN<T> THEN(final type.INT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.FLOAT.THEN<T> THEN(final float numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final short numeric);
        public case_.search.MEDIUMINT.THEN<T> THEN(final int numeric);
        public case_.search.INT.THEN<T> THEN(final long numeric);
        public case_.search.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.FLOAT.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.FLOAT.ELSE<T> ELSE(final float numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final short numeric);
        public case_.search.MEDIUMINT.ELSE<T> ELSE(final int numeric);
        public case_.search.INT.ELSE<T> ELSE(final long numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.MEDIUMINT.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.MEDIUMINT END();
      }
    }

    public interface INT {
      public interface WHEN<T> {
        public case_.search.DOUBLE.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.INT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.INT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.INT.THEN<T> THEN(final type.INT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final float numeric);
        public case_.search.DOUBLE.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.INT.THEN<T> THEN(final short numeric);
        public case_.search.INT.THEN<T> THEN(final int numeric);
        public case_.search.INT.THEN<T> THEN(final long numeric);
        public case_.search.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.INT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.INT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.INT.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final float numeric);
        public case_.search.DOUBLE.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.INT.ELSE<T> ELSE(final short numeric);
        public case_.search.INT.ELSE<T> ELSE(final int numeric);
        public case_.search.INT.ELSE<T> ELSE(final long numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.INT.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.INT END();
      }
    }

    public interface BIGINT {
      public interface WHEN<T> {
        public case_.search.DECIMAL.THEN<T> THEN(final type.FLOAT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.MEDIUMINT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.INT numeric);
        public case_.search.BIGINT.THEN<T> THEN(final type.BIGINT numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final float numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final double numeric);
        public case_.search.DECIMAL.THEN<T> THEN(final BigDecimal numeric);
        public case_.search.BIGINT.THEN<T> THEN(final short numeric);
        public case_.search.BIGINT.THEN<T> THEN(final int numeric);
        public case_.search.BIGINT.THEN<T> THEN(final long numeric);
        public case_.search.BIGINT.THEN<T> THEN(final BigInteger numeric);
      }

      public interface THEN<T> {
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.FLOAT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DOUBLE numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final type.DECIMAL numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.SMALLINT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.MEDIUMINT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.INT numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final type.BIGINT numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final float numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final double numeric);
        public case_.search.DECIMAL.ELSE<T> ELSE(final BigDecimal numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final short numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final int numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final long numeric);
        public case_.search.BIGINT.ELSE<T> ELSE(final BigInteger numeric);
        public case_.search.BIGINT.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.BIGINT END();
      }
    }

    public interface CLOB {
      public interface WHEN<T> {
        public case_.search.CLOB.THEN<T> THEN(final type.CLOB clob);
      }

      public interface THEN<T> {
        public case_.search.CLOB.ELSE<T> ELSE(final type.CLOB clob);
        public case_.search.CLOB.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.CLOB END();
      }
    }

    public interface BLOB {
      public interface WHEN<T> {
        public case_.search.BLOB.THEN<T> THEN(final type.BLOB blob);
      }

      public interface THEN<T> {
        public case_.search.BLOB.ELSE<T> ELSE(final type.BLOB blob);
        public case_.search.BLOB.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.BLOB END();
      }
    }

    public interface BINARY {
      public interface WHEN<T> {
        public case_.search.BINARY.THEN<T> THEN(final type.BINARY binary);
        public case_.search.BINARY.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> {
        public case_.search.BINARY.ELSE<T> ELSE(final type.BINARY binary);
        public case_.search.BINARY.ELSE<T> ELSE(final byte[] binary);
        public case_.search.BINARY.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.BINARY END();
      }
    }

    public interface DATE {
      public interface WHEN<T> {
        public case_.search.DATE.THEN<T> THEN(final type.DATE date);
        public case_.search.DATE.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> {
        public case_.search.DATE.ELSE<T> ELSE(final type.DATE date);
        public case_.search.DATE.ELSE<T> ELSE(final LocalDate date);
        public case_.search.DATE.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.DATE END();
      }
    }

    public interface TIME {
      public interface WHEN<T> {
        public case_.search.TIME.THEN<T> THEN(final type.TIME time);
        public case_.search.TIME.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> {
        public case_.search.TIME.ELSE<T> ELSE(final type.TIME time);
        public case_.search.TIME.ELSE<T> ELSE(final LocalTime time);
        public case_.search.TIME.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.TIME END();
      }
    }

    public interface DATETIME {
      public interface WHEN<T> {
        public case_.search.DATETIME.THEN<T> THEN(final type.DATETIME dateTime);
        public case_.search.DATETIME.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> {
        public case_.search.DATETIME.ELSE<T> ELSE(final type.DATETIME dateTime);
        public case_.search.DATETIME.ELSE<T> ELSE(final LocalDateTime dateTime);
        public case_.search.DATETIME.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE<T> extends THEN<T> {
        public type.DATETIME END();
      }
    }

    public interface CHAR {
      public interface WHEN<T> {
        public case_.search.CHAR.THEN<T> THEN(final type.ENUM<?> text);
        public case_.search.CHAR.THEN<T> THEN(final type.CHAR text);
        public case_.search.CHAR.THEN<T> THEN(final Enum<?> text);
        public case_.search.CHAR.THEN<T> THEN(final String text);
      }

      public interface THEN<T> {
        public case_.search.CHAR.ELSE ELSE(final type.ENUM<?> text);
        public case_.search.CHAR.ELSE ELSE(final type.CHAR text);
        public case_.search.CHAR.ELSE ELSE(final Enum<?> text);
        public case_.search.CHAR.ELSE ELSE(final String text);
        public case_.search.CHAR.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE {
        public type.CHAR END();
      }
    }

    public interface ENUM {
      public interface WHEN<T> {
        public case_.search.ENUM.THEN<T> THEN(final type.ENUM<?> text);
        public case_.search.CHAR.THEN<T> THEN(final type.CHAR text);
        public case_.search.ENUM.THEN<T> THEN(final Enum<?> text);
        public case_.search.CHAR.THEN<T> THEN(final String text);
      }

      public interface THEN<T> {
        public case_.search.ENUM.ELSE ELSE(final type.ENUM<?> text);
        public case_.search.CHAR.ELSE ELSE(final type.CHAR text);
        public case_.search.ENUM.ELSE ELSE(final Enum<?> text);
        public case_.search.CHAR.ELSE ELSE(final String text);
        public case_.search.ENUM.WHEN<T> WHEN(final Condition<T> condition);
      }

      public interface ELSE {
        public type.ENUM<?> END();
      }
    }
  }
}