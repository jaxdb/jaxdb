/* Copyright (c) 2015 lib4j
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

interface Case {
  public interface CASE<T> {
  }

  public interface simple {
    public interface CASE<T> extends Case.CASE<T> {
      public Case.simple.WHEN<T> WHEN(final T condition);
    }

    public interface WHEN<T> {
      public Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool);
      public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
      public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
      public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
      public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
      public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
      public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
      public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric);
      public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
      public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
      public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
      public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
      public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
      public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

      public Case.BOOLEAN.simple.THEN THEN(final boolean bool);
      public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
      public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
      public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
      public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
      public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
      public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
      public Case.TINYINT.simple.THEN<T> THEN(final byte numeric);
      public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
      public Case.SMALLINT.simple.THEN<T> THEN(final short numeric);
      public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
      public Case.INT.simple.THEN<T> THEN(final int numeric);
      public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
      public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
      public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);

      public Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary);
      public Case.DATE.simple.THEN<T> THEN(final data.DATE date);
      public Case.TIME.simple.THEN<T> THEN(final data.TIME time);
      public Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime);
      public Case.CHAR.simple.THEN<T> THEN(final data.CHAR text);
      public Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> dateTime);
      public Case.BINARY.simple.THEN<T> THEN(final byte[] binary);
      public Case.DATE.simple.THEN<T> THEN(final LocalDate date);
      public Case.TIME.simple.THEN<T> THEN(final LocalTime time);
      public Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime);
      public Case.CHAR.simple.THEN<T> THEN(final String text);
      public Case.ENUM.simple.THEN<T> THEN(final Enum<?> dateTime);
    }
  }

  public interface search {
    public interface WHEN<T> {
      public Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool);
      public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
      public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
      public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
      public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
      public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
      public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
      public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric);
      public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
      public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric);
      public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
      public Case.INT.search.THEN<T> THEN(final data.INT numeric);
      public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
      public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

      public Case.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      public Case.FLOAT.search.THEN<T> THEN(final float numeric);
      public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
      public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
      public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
      public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
      public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
      public Case.TINYINT.search.THEN<T> THEN(final byte numeric);
      public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
      public Case.SMALLINT.search.THEN<T> THEN(final short numeric);
      public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
      public Case.INT.search.THEN<T> THEN(final int numeric);
      public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
      public Case.BIGINT.search.THEN<T> THEN(final long numeric);
      public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);

      public Case.BINARY.search.THEN<T> THEN(final data.BINARY binary);
      public Case.DATE.search.THEN<T> THEN(final data.DATE date);
      public Case.TIME.search.THEN<T> THEN(final data.TIME time);
      public Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime);
      public Case.CHAR.search.THEN<T> THEN(final data.CHAR text);
      public Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> dateTime);
      public Case.BINARY.search.THEN<T> THEN(final byte[] binary);
      public Case.DATE.search.THEN<T> THEN(final LocalDate date);
      public Case.TIME.search.THEN<T> THEN(final LocalTime time);
      public Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime);
      public Case.CHAR.search.THEN<T> THEN(final String text);
      public Case.ENUM.search.THEN<T> THEN(final Enum<?> text);
    }
  }

  public interface BOOLEAN {
    public interface simple {
      public interface WHEN {
        public Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool);
        public Case.BOOLEAN.simple.THEN THEN(final boolean bool);
      }

      public interface THEN extends BOOLEAN.THEN {
        public Case.BOOLEAN.simple.WHEN WHEN(final boolean condition);
      }
    }

    public interface search {
      public interface CASE<T> {
        public Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool);
        public Case.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      }

      public interface WHEN<T> {
        public Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool);
        public Case.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      }

      public interface THEN<T> extends BOOLEAN.THEN {
        public Case.BOOLEAN.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.BOOLEAN.ELSE ELSE(final data.BOOLEAN bool);
      public Case.BOOLEAN.ELSE ELSE(final boolean bool);
    }

    public interface ELSE {
      public data.BOOLEAN END();
    }
  }

  public interface FLOAT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.FLOAT.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.FLOAT.simple.THEN<T> THEN(final byte numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          public Case.FLOAT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.FLOAT.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.search.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.FLOAT.search.THEN<T> THEN(final byte numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          public Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.ELSE ELSE(final float numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.FLOAT.ELSE ELSE(final byte numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.DOUBLE.ELSE ELSE(final short numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.DOUBLE.ELSE ELSE(final int numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.DOUBLE.ELSE ELSE(final long numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.FLOAT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.FLOAT.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.FLOAT.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.FLOAT.simple.THEN<T> THEN(final byte numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends FLOAT.THEN {
        public Case.FLOAT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.FLOAT.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.FLOAT.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.FLOAT.search.THEN<T> THEN(final byte numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends FLOAT.THEN {
        public Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric);
      public Case.FLOAT.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.INT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.FLOAT.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.FLOAT.ELSE ELSE(final byte numeric);
      public Case.DOUBLE.ELSE ELSE(final short numeric);
      public Case.DOUBLE.ELSE ELSE(final int numeric);
      public Case.DOUBLE.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.FLOAT END();
    }
  }

  public interface DOUBLE {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          public Case.DOUBLE.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final byte numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          public Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.ELSE ELSE(final float numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.DOUBLE.ELSE ELSE(final byte numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.DOUBLE.ELSE ELSE(final short numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.DOUBLE.ELSE ELSE(final int numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.DOUBLE.ELSE ELSE(final long numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.DOUBLE.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DOUBLE.THEN {
        public Case.DOUBLE.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final byte numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DOUBLE.THEN {
        public Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.INT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.DOUBLE.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.DOUBLE.ELSE ELSE(final byte numeric);
      public Case.DOUBLE.ELSE ELSE(final short numeric);
      public Case.DOUBLE.ELSE ELSE(final int numeric);
      public Case.DOUBLE.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.DOUBLE END();
    }
  }

  public interface DECIMAL {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.DECIMAL.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.INT numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DECIMAL.simple.THEN<T> THEN(final float numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final double numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final short numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final int numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final long numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          public Case.DECIMAL.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.DECIMAL.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.INT numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DECIMAL.search.THEN<T> THEN(final float numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final double numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final byte numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final short numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final int numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final long numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          public Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.INT numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.DECIMAL.ELSE ELSE(final float numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DECIMAL.ELSE ELSE(final double numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.DECIMAL.ELSE ELSE(final byte numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.DECIMAL.ELSE ELSE(final short numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.DECIMAL.ELSE ELSE(final int numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.DECIMAL.ELSE ELSE(final long numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.DECIMAL.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.DECIMAL.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.INT numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DECIMAL.simple.THEN<T> THEN(final float numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final short numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final int numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DECIMAL.THEN {
        public Case.DECIMAL.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DECIMAL.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.INT numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DECIMAL.search.THEN<T> THEN(final float numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final byte numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final short numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final int numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DECIMAL.THEN {
        public Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric);
      public Case.DECIMAL.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric);
      public Case.DECIMAL.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.DECIMAL.ELSE ELSE(final data.INT numeric);
      public Case.DECIMAL.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric);
      public Case.DECIMAL.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.DECIMAL.ELSE ELSE(final float numeric);
      public Case.DECIMAL.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.DECIMAL.ELSE ELSE(final byte numeric);
      public Case.DECIMAL.ELSE ELSE(final short numeric);
      public Case.DECIMAL.ELSE ELSE(final int numeric);
      public Case.DECIMAL.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.DECIMAL END();
    }
  }

  public interface TINYINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.TINYINT.simple.THEN<T> THEN(final byte numeric);
          public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.simple.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          public Case.TINYINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.search.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.search.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.TINYINT.search.THEN<T> THEN(final byte numeric);
          public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.search.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.search.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          public Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric);
        public Case.TINYINT.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.ELSE ELSE(final data.INT numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.ELSE ELSE(final float numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.TINYINT.ELSE ELSE(final byte numeric);
        public Case.TINYINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.SMALLINT.ELSE ELSE(final short numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.INT.ELSE ELSE(final int numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.BIGINT.ELSE ELSE(final long numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.TINYINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.TINYINT.simple.THEN<T> THEN(final byte numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric);
        public Case.INT.simple.THEN<T> THEN(final int numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends TINYINT.THEN {
        public Case.TINYINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.TINYINT.search.THEN<T> THEN(final byte numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric);
        public Case.INT.search.THEN<T> THEN(final int numeric);
        public Case.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends TINYINT.THEN {
        public Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric);
      public Case.TINYINT.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric);
      public Case.SMALLINT.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.INT.ELSE ELSE(final data.INT numeric);
      public Case.INT.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.FLOAT.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.TINYINT.ELSE ELSE(final byte numeric);
      public Case.SMALLINT.ELSE ELSE(final short numeric);
      public Case.INT.ELSE ELSE(final int numeric);
      public Case.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.TINYINT END();
    }
  }

  public interface SMALLINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric);
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.simple.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          public Case.SMALLINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.search.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.FLOAT.search.THEN<T> THEN(final float numeric);
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final byte numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric);
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.search.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.search.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          public Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.ELSE ELSE(final data.INT numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.ELSE ELSE(final float numeric);
        public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.SMALLINT.ELSE ELSE(final byte numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.SMALLINT.ELSE ELSE(final short numeric);
        public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.INT.ELSE ELSE(final int numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.BIGINT.ELSE ELSE(final long numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.SMALLINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric);
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric);
        public Case.INT.simple.THEN<T> THEN(final int numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends SMALLINT.THEN {
        public Case.SMALLINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.FLOAT.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final byte numeric);
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric);
        public Case.INT.search.THEN<T> THEN(final int numeric);
        public Case.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends SMALLINT.THEN {
        public Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric);
      public Case.SMALLINT.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric);
      public Case.SMALLINT.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.INT.ELSE ELSE(final data.INT numeric);
      public Case.INT.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.FLOAT.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.SMALLINT.ELSE ELSE(final byte numeric);
      public Case.SMALLINT.ELSE ELSE(final short numeric);
      public Case.INT.ELSE ELSE(final int numeric);
      public Case.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.SMALLINT END();
    }
  }

  public interface INT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.INT.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.INT.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.INT.simple.THEN<T> THEN(final byte numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.INT.simple.THEN<T> THEN(final short numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.simple.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends INT.UNSIGNED.THEN {
          public Case.INT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.INT.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.INT.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.INT.search.THEN<T> THEN(final data.INT numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.INT.search.THEN<T> THEN(final byte numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.INT.search.THEN<T> THEN(final short numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.INT.search.THEN<T> THEN(final int numeric);
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.search.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends INT.UNSIGNED.THEN {
          public Case.INT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.INT.ELSE ELSE(final data.TINYINT numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.INT.ELSE ELSE(final data.SMALLINT numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.ELSE ELSE(final data.INT numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.ELSE ELSE(final float numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.INT.ELSE ELSE(final byte numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.INT.ELSE ELSE(final short numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.INT.ELSE ELSE(final int numeric);
        public Case.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.BIGINT.ELSE ELSE(final long numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.INT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.INT.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.INT.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.INT.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.INT.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric);
        public Case.INT.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.INT.simple.THEN<T> THEN(final byte numeric);
        public Case.INT.simple.THEN<T> THEN(final short numeric);
        public Case.INT.simple.THEN<T> THEN(final int numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends INT.THEN {
        public Case.INT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.INT.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.INT.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.INT.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.INT.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT numeric);
        public Case.INT.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.INT.search.THEN<T> THEN(final byte numeric);
        public Case.INT.search.THEN<T> THEN(final short numeric);
        public Case.INT.search.THEN<T> THEN(final int numeric);
        public Case.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends INT.THEN {
        public Case.INT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.INT.ELSE ELSE(final data.TINYINT numeric);
      public Case.INT.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.INT.ELSE ELSE(final data.SMALLINT numeric);
      public Case.INT.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.INT.ELSE ELSE(final data.INT numeric);
      public Case.INT.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.DOUBLE.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.INT.ELSE ELSE(final byte numeric);
      public Case.INT.ELSE ELSE(final short numeric);
      public Case.INT.ELSE ELSE(final int numeric);
      public Case.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.INT END();
    }
  }

  public interface BIGINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.TINYINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.INT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final byte numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final short numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final int numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          public Case.BIGINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.FLOAT.UNSIGNED numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final data.DOUBLE.UNSIGNED numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final data.DECIMAL.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.TINYINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.SMALLINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.INT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

          public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public Case.BIGINT.search.THEN<T> THEN(final byte numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public Case.BIGINT.search.THEN<T> THEN(final short numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public Case.BIGINT.search.THEN<T> THEN(final int numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public Case.BIGINT.search.THEN<T> THEN(final long numeric);
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          public Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.FLOAT.UNSIGNED numeric);
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final data.DOUBLE.UNSIGNED numeric);
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final data.DECIMAL.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.INT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.ELSE ELSE(final float numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public Case.DOUBLE.ELSE ELSE(final double numeric);
        public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public Case.BIGINT.ELSE ELSE(final byte numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public Case.BIGINT.ELSE ELSE(final short numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public Case.BIGINT.ELSE ELSE(final int numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public Case.BIGINT.ELSE ELSE(final long numeric);
        public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public data.BIGINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.TINYINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.INT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final byte numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final short numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final int numeric);
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends BIGINT.THEN {
        public Case.BIGINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.TINYINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.TINYINT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.SMALLINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.SMALLINT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.INT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.INT.UNSIGNED numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric);
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT.UNSIGNED numeric);

        public Case.DOUBLE.search.THEN<T> THEN(final float numeric);
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric);
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public Case.BIGINT.search.THEN<T> THEN(final byte numeric);
        public Case.BIGINT.search.THEN<T> THEN(final short numeric);
        public Case.BIGINT.search.THEN<T> THEN(final int numeric);
        public Case.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends BIGINT.THEN {
        public Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric);
      public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric);
      public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric);
      public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.TINYINT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.SMALLINT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.INT numeric);
      public Case.BIGINT.ELSE ELSE(final data.INT.UNSIGNED numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric);
      public Case.BIGINT.ELSE ELSE(final data.BIGINT.UNSIGNED numeric);

      public Case.DOUBLE.ELSE ELSE(final float numeric);
      public Case.DOUBLE.ELSE ELSE(final double numeric);
      public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public Case.BIGINT.ELSE ELSE(final byte numeric);
      public Case.BIGINT.ELSE ELSE(final short numeric);
      public Case.BIGINT.ELSE ELSE(final int numeric);
      public Case.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public data.BIGINT END();
    }
  }

  public interface BINARY {
    public interface simple {
      public interface WHEN<T> {
        public Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary);
        public Case.BINARY.simple.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> extends BINARY.THEN {
        public Case.BINARY.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.BINARY.search.THEN<T> THEN(final data.BINARY binary);
        public Case.BINARY.search.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> extends BINARY.THEN {
        public Case.BINARY.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.BINARY.ELSE ELSE(final data.BINARY binary);
      public Case.BINARY.ELSE ELSE(final byte[] binary);
    }

    public interface ELSE {
      public data.BINARY END();
    }
  }

  public interface DATE {
    public interface simple {
      public interface WHEN<T> {
        public Case.DATE.simple.THEN<T> THEN(final data.DATE date);
        public Case.DATE.simple.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> extends DATE.THEN {
        public Case.DATE.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DATE.search.THEN<T> THEN(final data.DATE date);
        public Case.DATE.search.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> extends DATE.THEN {
        public Case.DATE.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DATE.ELSE ELSE(final data.DATE date);
      public Case.DATE.ELSE ELSE(final LocalDate date);
    }

    public interface ELSE {
      public data.DATE END();
    }
  }

  public interface TIME {
    public interface simple {
      public interface WHEN<T> {
        public Case.TIME.simple.THEN<T> THEN(final data.TIME time);
        public Case.TIME.simple.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> extends TIME.THEN {
        public Case.TIME.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.TIME.search.THEN<T> THEN(final data.TIME time);
        public Case.TIME.search.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> extends TIME.THEN {
        public Case.TIME.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.TIME.ELSE ELSE(final data.TIME time);
      public Case.TIME.ELSE ELSE(final LocalTime time);
    }

    public interface ELSE {
      public data.TIME END();
    }
  }

  public interface DATETIME {
    public interface simple {
      public interface WHEN<T> {
        public Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime);
        public Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> extends DATETIME.THEN {
        public Case.DATETIME.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime);
        public Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> extends DATETIME.THEN {
        public Case.DATETIME.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.DATETIME.ELSE ELSE(final data.DATETIME dateTime);
      public Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime);
    }

    public interface ELSE {
      public data.DATETIME END();
    }
  }

  public interface CHAR {
    public interface simple {
      public interface WHEN<T> {
        public Case.CHAR.simple.THEN<T> THEN(final data.ENUM<?> text);
        public Case.CHAR.simple.THEN<T> THEN(final data.CHAR text);
        public Case.CHAR.simple.THEN<T> THEN(final Enum<?> text);
        public Case.CHAR.simple.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends CHAR.THEN {
        public Case.CHAR.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.CHAR.search.THEN<T> THEN(final data.ENUM<?> text);
        public Case.CHAR.search.THEN<T> THEN(final data.CHAR text);
        public Case.CHAR.search.THEN<T> THEN(final Enum<?> text);
        public Case.CHAR.search.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends CHAR.THEN {
        public Case.CHAR.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.CHAR.ELSE ELSE(final data.ENUM<?> text);
      public Case.CHAR.ELSE ELSE(final data.CHAR text);
      public Case.CHAR.ELSE ELSE(final Enum<?> text);
      public Case.CHAR.ELSE ELSE(final String text);
    }

    public interface ELSE {
      public data.CHAR END();
    }
  }

  public interface ENUM {
    public interface simple {
      public interface WHEN<T> {
        public Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> text);
        public Case.CHAR.simple.THEN<T> THEN(final data.CHAR text);
        public Case.ENUM.simple.THEN<T> THEN(final Enum<?> text);
        public Case.CHAR.simple.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends ENUM.THEN {
        public Case.ENUM.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> text);
        public Case.CHAR.search.THEN<T> THEN(final data.CHAR text);
        public Case.ENUM.search.THEN<T> THEN(final Enum<?> text);
        public Case.CHAR.search.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends ENUM.THEN {
        public Case.ENUM.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public Case.ENUM.ELSE ELSE(final data.ENUM<?> text);
      public Case.CHAR.ELSE ELSE(final data.CHAR text);
      public Case.ENUM.ELSE ELSE(final Enum<?> text);
      public Case.CHAR.ELSE ELSE(final String text);
    }

    public interface ELSE {
      public data.ENUM<?> END();
    }
  }
}