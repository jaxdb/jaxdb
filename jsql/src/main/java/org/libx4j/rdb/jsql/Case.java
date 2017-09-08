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

package org.libx4j.rdb.jsql.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.libx4j.rdb.jsql.Condition;
import org.libx4j.rdb.jsql.type;

public interface case_ {
  public interface CASE<T> {
  }

  public interface simple {
    public interface CASE<T> extends case_.CASE<T> {
      public case_.simple.WHEN<T> WHEN(final T condition);
    }

    public interface WHEN<T> {
      public case_.BOOLEAN.simple.THEN THEN(final type.BOOLEAN bool);
      public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
      public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
      public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
      public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
      public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
      public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
      public case_.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric);
      public case_.TINYINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
      public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
      public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
      public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
      public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
      public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

      public case_.BOOLEAN.simple.THEN THEN(final boolean bool);
      public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
      public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
      public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
      public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
      public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
      public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
      public case_.TINYINT.simple.THEN<T> THEN(final byte numeric);
      public case_.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
      public case_.SMALLINT.simple.THEN<T> THEN(final short numeric);
      public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
      public case_.INT.simple.THEN<T> THEN(final int numeric);
      public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
      public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
      public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);

      public case_.BINARY.simple.THEN<T> THEN(final type.BINARY binary);
      public case_.DATE.simple.THEN<T> THEN(final type.DATE date);
      public case_.TIME.simple.THEN<T> THEN(final type.TIME time);
      public case_.DATETIME.simple.THEN<T> THEN(final type.DATETIME dateTime);
      public case_.CHAR.simple.THEN<T> THEN(final type.CHAR text);
      public case_.ENUM.simple.THEN<T> THEN(final type.ENUM<?> dateTime);
      public case_.BINARY.simple.THEN<T> THEN(final byte[] binary);
      public case_.DATE.simple.THEN<T> THEN(final LocalDate date);
      public case_.TIME.simple.THEN<T> THEN(final LocalTime time);
      public case_.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime);
      public case_.CHAR.simple.THEN<T> THEN(final String text);
      public case_.ENUM.simple.THEN<T> THEN(final Enum<?> dateTime);
    }
  }

  public interface search {
    public interface WHEN<T> {
      public case_.BOOLEAN.search.THEN<T> THEN(final type.BOOLEAN bool);
      public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
      public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
      public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
      public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
      public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
      public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
      public case_.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric);
      public case_.TINYINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
      public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric);
      public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
      public case_.INT.search.THEN<T> THEN(final type.INT numeric);
      public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
      public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

      public case_.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      public case_.FLOAT.search.THEN<T> THEN(final float numeric);
      public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
      public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
      public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
      public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
      public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
      public case_.TINYINT.search.THEN<T> THEN(final byte numeric);
      public case_.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
      public case_.SMALLINT.search.THEN<T> THEN(final short numeric);
      public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
      public case_.INT.search.THEN<T> THEN(final int numeric);
      public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
      public case_.BIGINT.search.THEN<T> THEN(final long numeric);
      public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);

      public case_.BINARY.search.THEN<T> THEN(final type.BINARY binary);
      public case_.DATE.search.THEN<T> THEN(final type.DATE date);
      public case_.TIME.search.THEN<T> THEN(final type.TIME time);
      public case_.DATETIME.search.THEN<T> THEN(final type.DATETIME dateTime);
      public case_.CHAR.search.THEN<T> THEN(final type.CHAR text);
      public case_.ENUM.search.THEN<T> THEN(final type.ENUM<?> dateTime);
      public case_.BINARY.search.THEN<T> THEN(final byte[] binary);
      public case_.DATE.search.THEN<T> THEN(final LocalDate date);
      public case_.TIME.search.THEN<T> THEN(final LocalTime time);
      public case_.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime);
      public case_.CHAR.search.THEN<T> THEN(final String text);
      public case_.ENUM.search.THEN<T> THEN(final Enum<?> text);
    }
  }

  public interface BOOLEAN {
    public interface simple {
      public interface WHEN {
        public case_.BOOLEAN.simple.THEN THEN(final type.BOOLEAN bool);
        public case_.BOOLEAN.simple.THEN THEN(final boolean bool);
      }

      public interface THEN extends BOOLEAN.THEN {
        public case_.BOOLEAN.simple.WHEN WHEN(final boolean condition);
      }
    }

    public interface search {
      public interface CASE<T> {
        public case_.BOOLEAN.search.THEN<T> THEN(final type.BOOLEAN bool);
        public case_.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      }

      public interface WHEN<T> {
        public case_.BOOLEAN.search.THEN<T> THEN(final type.BOOLEAN bool);
        public case_.BOOLEAN.search.THEN<T> THEN(final boolean bool);
      }

      public interface THEN<T> extends BOOLEAN.THEN {
        public case_.BOOLEAN.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.BOOLEAN.ELSE ELSE(final type.BOOLEAN bool);
      public case_.BOOLEAN.ELSE ELSE(final boolean bool);
    }

    public interface ELSE {
      public type.BOOLEAN END();
    }
  }

  public interface FLOAT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.FLOAT.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.INT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.FLOAT.simple.THEN<T> THEN(final byte numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final short numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final int numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final long numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          public case_.FLOAT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.FLOAT.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.INT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.search.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.FLOAT.search.THEN<T> THEN(final byte numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final short numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final int numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final long numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          public case_.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.FLOAT.ELSE ELSE(final type.TINYINT numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.SMALLINT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.INT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.BIGINT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.ELSE ELSE(final float numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.FLOAT.ELSE ELSE(final byte numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.DOUBLE.ELSE ELSE(final short numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.DOUBLE.ELSE ELSE(final int numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.DOUBLE.ELSE ELSE(final long numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.FLOAT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.FLOAT.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.FLOAT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.INT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.FLOAT.simple.THEN<T> THEN(final byte numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final short numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final int numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends FLOAT.THEN {
        public case_.FLOAT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.FLOAT.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.FLOAT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.INT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.FLOAT.search.THEN<T> THEN(final byte numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final short numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final int numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends FLOAT.THEN {
        public case_.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.FLOAT.ELSE ELSE(final type.TINYINT numeric);
      public case_.FLOAT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.SMALLINT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.INT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.BIGINT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.FLOAT.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.FLOAT.ELSE ELSE(final byte numeric);
      public case_.DOUBLE.ELSE ELSE(final short numeric);
      public case_.DOUBLE.ELSE ELSE(final int numeric);
      public case_.DOUBLE.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.FLOAT END();
    }
  }

  public interface DOUBLE {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.INT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final byte numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final short numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final int numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final long numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          public case_.DOUBLE.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.INT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final byte numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final short numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final int numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final long numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          public case_.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.TINYINT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.SMALLINT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.INT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.BIGINT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.ELSE ELSE(final float numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.DOUBLE.ELSE ELSE(final byte numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.DOUBLE.ELSE ELSE(final short numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.DOUBLE.ELSE ELSE(final int numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.DOUBLE.ELSE ELSE(final long numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.DOUBLE.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.INT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final byte numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final short numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final int numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DOUBLE.THEN {
        public case_.DOUBLE.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.INT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final byte numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final short numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final int numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DOUBLE.THEN {
        public case_.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.DOUBLE.ELSE ELSE(final type.TINYINT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.SMALLINT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.INT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.DOUBLE.ELSE ELSE(final type.BIGINT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.DOUBLE.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.DOUBLE.ELSE ELSE(final byte numeric);
      public case_.DOUBLE.ELSE ELSE(final short numeric);
      public case_.DOUBLE.ELSE ELSE(final int numeric);
      public case_.DOUBLE.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.DOUBLE END();
    }
  }

  public interface DECIMAL {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.DECIMAL.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.INT numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DECIMAL.simple.THEN<T> THEN(final float numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final double numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final byte numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final short numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final int numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final long numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          public case_.DECIMAL.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.DECIMAL.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.INT numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DECIMAL.search.THEN<T> THEN(final float numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final double numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final byte numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final short numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final int numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final long numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          public case_.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.DECIMAL.ELSE ELSE(final type.FLOAT numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.TINYINT numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.SMALLINT numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.INT numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.BIGINT numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.DECIMAL.ELSE ELSE(final float numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DECIMAL.ELSE ELSE(final double numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.DECIMAL.ELSE ELSE(final byte numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.DECIMAL.ELSE ELSE(final short numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.DECIMAL.ELSE ELSE(final int numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.DECIMAL.ELSE ELSE(final long numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.DECIMAL.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.DECIMAL.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.INT numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DECIMAL.simple.THEN<T> THEN(final float numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final byte numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final short numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final int numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DECIMAL.THEN {
        public case_.DECIMAL.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DECIMAL.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.INT numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DECIMAL.search.THEN<T> THEN(final float numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final byte numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final short numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final int numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends DECIMAL.THEN {
        public case_.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DECIMAL.ELSE ELSE(final type.FLOAT numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.DECIMAL.ELSE ELSE(final type.TINYINT numeric);
      public case_.DECIMAL.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.DECIMAL.ELSE ELSE(final type.SMALLINT numeric);
      public case_.DECIMAL.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.DECIMAL.ELSE ELSE(final type.INT numeric);
      public case_.DECIMAL.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.DECIMAL.ELSE ELSE(final type.BIGINT numeric);
      public case_.DECIMAL.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.DECIMAL.ELSE ELSE(final float numeric);
      public case_.DECIMAL.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.DECIMAL.ELSE ELSE(final byte numeric);
      public case_.DECIMAL.ELSE ELSE(final short numeric);
      public case_.DECIMAL.ELSE ELSE(final int numeric);
      public case_.DECIMAL.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.DECIMAL END();
    }
  }

  public interface TINYINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.TINYINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.TINYINT.simple.THEN<T> THEN(final byte numeric);
          public case_.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final short numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.simple.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          public case_.TINYINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.TINYINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.search.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.search.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.TINYINT.search.THEN<T> THEN(final byte numeric);
          public case_.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final short numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.search.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.search.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          public case_.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.TINYINT.ELSE ELSE(final type.TINYINT numeric);
        public case_.TINYINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.ELSE ELSE(final type.SMALLINT numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.ELSE ELSE(final type.INT numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.ELSE ELSE(final float numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.TINYINT.ELSE ELSE(final byte numeric);
        public case_.TINYINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.SMALLINT.ELSE ELSE(final short numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.INT.ELSE ELSE(final int numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.BIGINT.ELSE ELSE(final long numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.TINYINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.TINYINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.TINYINT.simple.THEN<T> THEN(final byte numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final short numeric);
        public case_.INT.simple.THEN<T> THEN(final int numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends TINYINT.THEN {
        public case_.TINYINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.TINYINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.TINYINT.search.THEN<T> THEN(final byte numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final short numeric);
        public case_.INT.search.THEN<T> THEN(final int numeric);
        public case_.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends TINYINT.THEN {
        public case_.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.TINYINT.ELSE ELSE(final type.TINYINT numeric);
      public case_.TINYINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.SMALLINT.ELSE ELSE(final type.SMALLINT numeric);
      public case_.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.INT.ELSE ELSE(final type.INT numeric);
      public case_.INT.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.FLOAT.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.TINYINT.ELSE ELSE(final byte numeric);
      public case_.SMALLINT.ELSE ELSE(final short numeric);
      public case_.INT.ELSE ELSE(final int numeric);
      public case_.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.TINYINT END();
    }
  }

  public interface SMALLINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final byte numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.SMALLINT.simple.THEN<T> THEN(final short numeric);
          public case_.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.simple.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          public case_.SMALLINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.search.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.FLOAT.search.THEN<T> THEN(final float numeric);
          public case_.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final byte numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.SMALLINT.search.THEN<T> THEN(final short numeric);
          public case_.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.search.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.search.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          public case_.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.SMALLINT.ELSE ELSE(final type.TINYINT numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.ELSE ELSE(final type.SMALLINT numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.ELSE ELSE(final type.INT numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.ELSE ELSE(final float numeric);
        public case_.FLOAT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.SMALLINT.ELSE ELSE(final byte numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.SMALLINT.ELSE ELSE(final short numeric);
        public case_.SMALLINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.INT.ELSE ELSE(final int numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.BIGINT.ELSE ELSE(final long numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.SMALLINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final byte numeric);
        public case_.SMALLINT.simple.THEN<T> THEN(final short numeric);
        public case_.INT.simple.THEN<T> THEN(final int numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends SMALLINT.THEN {
        public case_.SMALLINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.FLOAT.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final byte numeric);
        public case_.SMALLINT.search.THEN<T> THEN(final short numeric);
        public case_.INT.search.THEN<T> THEN(final int numeric);
        public case_.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends SMALLINT.THEN {
        public case_.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.FLOAT.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.SMALLINT.ELSE ELSE(final type.TINYINT numeric);
      public case_.SMALLINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.SMALLINT.ELSE ELSE(final type.SMALLINT numeric);
      public case_.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.INT.ELSE ELSE(final type.INT numeric);
      public case_.INT.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.FLOAT.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.SMALLINT.ELSE ELSE(final byte numeric);
      public case_.SMALLINT.ELSE ELSE(final short numeric);
      public case_.INT.ELSE ELSE(final int numeric);
      public case_.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.SMALLINT END();
    }
  }

  public interface INT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.INT.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.INT.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.INT.simple.THEN<T> THEN(final byte numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.INT.simple.THEN<T> THEN(final short numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.simple.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends INT.UNSIGNED.THEN {
          public case_.INT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.INT.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.INT.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.INT.search.THEN<T> THEN(final type.INT numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.INT.search.THEN<T> THEN(final byte numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.INT.search.THEN<T> THEN(final short numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.INT.search.THEN<T> THEN(final int numeric);
          public case_.INT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.search.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends INT.UNSIGNED.THEN {
          public case_.INT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.INT.ELSE ELSE(final type.TINYINT numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.INT.ELSE ELSE(final type.SMALLINT numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.ELSE ELSE(final type.INT numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.ELSE ELSE(final float numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.INT.ELSE ELSE(final byte numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.INT.ELSE ELSE(final short numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.INT.ELSE ELSE(final int numeric);
        public case_.INT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.BIGINT.ELSE ELSE(final long numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.INT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.INT.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.INT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.INT.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.INT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT numeric);
        public case_.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.INT.simple.THEN<T> THEN(final byte numeric);
        public case_.INT.simple.THEN<T> THEN(final short numeric);
        public case_.INT.simple.THEN<T> THEN(final int numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends INT.THEN {
        public case_.INT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.INT.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.INT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.INT.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.INT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT numeric);
        public case_.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.INT.search.THEN<T> THEN(final byte numeric);
        public case_.INT.search.THEN<T> THEN(final short numeric);
        public case_.INT.search.THEN<T> THEN(final int numeric);
        public case_.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends INT.THEN {
        public case_.INT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.INT.ELSE ELSE(final type.TINYINT numeric);
      public case_.INT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.INT.ELSE ELSE(final type.SMALLINT numeric);
      public case_.INT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.INT.ELSE ELSE(final type.INT numeric);
      public case_.INT.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.DOUBLE.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.INT.ELSE ELSE(final byte numeric);
      public case_.INT.ELSE ELSE(final short numeric);
      public case_.INT.ELSE ELSE(final int numeric);
      public case_.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.INT END();
    }
  }

  public interface BIGINT {
    public interface UNSIGNED {
      public interface simple {
        public interface WHEN<T> {
          public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.TINYINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.INT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final byte numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final short numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final int numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          public case_.BIGINT.simple.WHEN<T> WHEN(final T condition);
        }
      }

      public interface search {
        public interface WHEN<T> {
          public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.TINYINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.SMALLINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.INT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
          public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

          public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
          public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
          public case_.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
          public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
          public case_.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
          public case_.BIGINT.search.THEN<T> THEN(final byte numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
          public case_.BIGINT.search.THEN<T> THEN(final short numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
          public case_.BIGINT.search.THEN<T> THEN(final int numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
          public case_.BIGINT.search.THEN<T> THEN(final long numeric);
          public case_.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
        }

        public interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          public case_.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition);
        }
      }

      interface THEN {
        public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric);
        public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric);
        public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.TINYINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.SMALLINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.INT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.ELSE ELSE(final float numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Float numeric);
        public case_.DOUBLE.ELSE ELSE(final double numeric);
        public case_.DOUBLE.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Double numeric);
        public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
        public case_.DECIMAL.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.BigDecimal numeric);
        public case_.BIGINT.ELSE ELSE(final byte numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Byte numeric);
        public case_.BIGINT.ELSE ELSE(final short numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Short numeric);
        public case_.BIGINT.ELSE ELSE(final int numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Integer numeric);
        public case_.BIGINT.ELSE ELSE(final long numeric);
        public case_.BIGINT.UNSIGNED.ELSE ELSE(final org.libx4j.rdb.jsql.UNSIGNED.Long numeric);
      }

      public interface ELSE {
        public type.BIGINT.UNSIGNED END();
      }
    }

    public interface simple {
      public interface WHEN<T> {
        public case_.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.TINYINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.INT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.simple.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.simple.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final byte numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final short numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final int numeric);
        public case_.BIGINT.simple.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends BIGINT.THEN {
        public case_.BIGINT.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.TINYINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.SMALLINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.INT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric);
        public case_.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric);

        public case_.DOUBLE.search.THEN<T> THEN(final float numeric);
        public case_.DOUBLE.search.THEN<T> THEN(final double numeric);
        public case_.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric);
        public case_.BIGINT.search.THEN<T> THEN(final byte numeric);
        public case_.BIGINT.search.THEN<T> THEN(final short numeric);
        public case_.BIGINT.search.THEN<T> THEN(final int numeric);
        public case_.BIGINT.search.THEN<T> THEN(final long numeric);
      }

      public interface THEN<T> extends BIGINT.THEN {
        public case_.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DOUBLE.ELSE ELSE(final type.FLOAT numeric);
      public case_.DOUBLE.ELSE ELSE(final type.DOUBLE numeric);
      public case_.DECIMAL.ELSE ELSE(final type.DECIMAL numeric);
      public case_.BIGINT.ELSE ELSE(final type.TINYINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.SMALLINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.INT numeric);
      public case_.BIGINT.ELSE ELSE(final type.INT.UNSIGNED numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT numeric);
      public case_.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric);

      public case_.DOUBLE.ELSE ELSE(final float numeric);
      public case_.DOUBLE.ELSE ELSE(final double numeric);
      public case_.DECIMAL.ELSE ELSE(final BigDecimal numeric);
      public case_.BIGINT.ELSE ELSE(final byte numeric);
      public case_.BIGINT.ELSE ELSE(final short numeric);
      public case_.BIGINT.ELSE ELSE(final int numeric);
      public case_.BIGINT.ELSE ELSE(final long numeric);
    }

    public interface ELSE {
      public type.BIGINT END();
    }
  }

  public interface BINARY {
    public interface simple {
      public interface WHEN<T> {
        public case_.BINARY.simple.THEN<T> THEN(final type.BINARY binary);
        public case_.BINARY.simple.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> extends BINARY.THEN {
        public case_.BINARY.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.BINARY.search.THEN<T> THEN(final type.BINARY binary);
        public case_.BINARY.search.THEN<T> THEN(final byte[] binary);
      }

      public interface THEN<T> extends BINARY.THEN {
        public case_.BINARY.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.BINARY.ELSE ELSE(final type.BINARY binary);
      public case_.BINARY.ELSE ELSE(final byte[] binary);
    }

    public interface ELSE {
      public type.BINARY END();
    }
  }

  public interface DATE {
    public interface simple {
      public interface WHEN<T> {
        public case_.DATE.simple.THEN<T> THEN(final type.DATE date);
        public case_.DATE.simple.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> extends DATE.THEN {
        public case_.DATE.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DATE.search.THEN<T> THEN(final type.DATE date);
        public case_.DATE.search.THEN<T> THEN(final LocalDate date);
      }

      public interface THEN<T> extends DATE.THEN {
        public case_.DATE.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DATE.ELSE ELSE(final type.DATE date);
      public case_.DATE.ELSE ELSE(final LocalDate date);
    }

    public interface ELSE {
      public type.DATE END();
    }
  }

  public interface TIME {
    public interface simple {
      public interface WHEN<T> {
        public case_.TIME.simple.THEN<T> THEN(final type.TIME time);
        public case_.TIME.simple.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> extends TIME.THEN {
        public case_.TIME.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.TIME.search.THEN<T> THEN(final type.TIME time);
        public case_.TIME.search.THEN<T> THEN(final LocalTime time);
      }

      public interface THEN<T> extends TIME.THEN {
        public case_.TIME.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.TIME.ELSE ELSE(final type.TIME time);
      public case_.TIME.ELSE ELSE(final LocalTime time);
    }

    public interface ELSE {
      public type.TIME END();
    }
  }

  public interface DATETIME {
    public interface simple {
      public interface WHEN<T> {
        public case_.DATETIME.simple.THEN<T> THEN(final type.DATETIME dateTime);
        public case_.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> extends DATETIME.THEN {
        public case_.DATETIME.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.DATETIME.search.THEN<T> THEN(final type.DATETIME dateTime);
        public case_.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime);
      }

      public interface THEN<T> extends DATETIME.THEN {
        public case_.DATETIME.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.DATETIME.ELSE ELSE(final type.DATETIME dateTime);
      public case_.DATETIME.ELSE ELSE(final LocalDateTime dateTime);
    }

    public interface ELSE {
      public type.DATETIME END();
    }
  }

  public interface CHAR {
    public interface simple {
      public interface WHEN<T> {
        public case_.CHAR.simple.THEN<T> THEN(final type.ENUM<?> text);
        public case_.CHAR.simple.THEN<T> THEN(final type.CHAR text);
        public case_.CHAR.simple.THEN<T> THEN(final Enum<?> text);
        public case_.CHAR.simple.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends CHAR.THEN {
        public case_.CHAR.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.CHAR.search.THEN<T> THEN(final type.ENUM<?> text);
        public case_.CHAR.search.THEN<T> THEN(final type.CHAR text);
        public case_.CHAR.search.THEN<T> THEN(final Enum<?> text);
        public case_.CHAR.search.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends CHAR.THEN {
        public case_.CHAR.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.CHAR.ELSE ELSE(final type.ENUM<?> text);
      public case_.CHAR.ELSE ELSE(final type.CHAR text);
      public case_.CHAR.ELSE ELSE(final Enum<?> text);
      public case_.CHAR.ELSE ELSE(final String text);
    }

    public interface ELSE {
      public type.CHAR END();
    }
  }

  public interface ENUM {
    public interface simple {
      public interface WHEN<T> {
        public case_.ENUM.simple.THEN<T> THEN(final type.ENUM<?> text);
        public case_.CHAR.simple.THEN<T> THEN(final type.CHAR text);
        public case_.ENUM.simple.THEN<T> THEN(final Enum<?> text);
        public case_.CHAR.simple.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends ENUM.THEN {
        public case_.ENUM.simple.WHEN<T> WHEN(final T condition);
      }
    }

    public interface search {
      public interface WHEN<T> {
        public case_.ENUM.search.THEN<T> THEN(final type.ENUM<?> text);
        public case_.CHAR.search.THEN<T> THEN(final type.CHAR text);
        public case_.ENUM.search.THEN<T> THEN(final Enum<?> text);
        public case_.CHAR.search.THEN<T> THEN(final String text);
      }

      public interface THEN<T> extends ENUM.THEN {
        public case_.ENUM.search.WHEN<T> WHEN(final Condition<T> condition);
      }
    }

    interface THEN {
      public case_.ENUM.ELSE ELSE(final type.ENUM<?> text);
      public case_.CHAR.ELSE ELSE(final type.CHAR text);
      public case_.ENUM.ELSE ELSE(final Enum<?> text);
      public case_.CHAR.ELSE ELSE(final String text);
    }

    public interface ELSE {
      public type.ENUM<?> END();
    }
  }
}