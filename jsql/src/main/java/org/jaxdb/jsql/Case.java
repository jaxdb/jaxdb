/* Copyright (c) 2015 OpenJAX
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

interface Case {
  interface CASE<T> {
  }

  interface simple {
    interface CASE<T> extends Case.CASE<T> {
      Case.simple.WHEN<T> WHEN(T condition);
    }

    interface WHEN<T> {
      Case.BOOLEAN.simple.THEN THEN(type.BOOLEAN bool);
      Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
      Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
      Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
      Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
      Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
      Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
      Case.TINYINT.simple.THEN<T> THEN(type.TINYINT numeric);
      Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
      Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT numeric);
      Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
      Case.INT.simple.THEN<T> THEN(type.INT numeric);
      Case.INT.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
      Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
      Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

      Case.BOOLEAN.simple.THEN THEN(boolean bool);
      Case.FLOAT.simple.THEN<T> THEN(float numeric);
      Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
      Case.DOUBLE.simple.THEN<T> THEN(double numeric);
      Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
      Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
      Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
      Case.TINYINT.simple.THEN<T> THEN(byte numeric);
      Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
      Case.SMALLINT.simple.THEN<T> THEN(short numeric);
      Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
      Case.INT.simple.THEN<T> THEN(int numeric);
      Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
      Case.BIGINT.simple.THEN<T> THEN(long numeric);
      Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);

      Case.BINARY.simple.THEN<T> THEN(type.BINARY binary);
      Case.DATE.simple.THEN<T> THEN(type.DATE date);
      Case.TIME.simple.THEN<T> THEN(type.TIME time);
      Case.DATETIME.simple.THEN<T> THEN(type.DATETIME dateTime);
      Case.CHAR.simple.THEN<T> THEN(type.CHAR text);
      Case.ENUM.simple.THEN<T> THEN(type.ENUM<?> dateTime);
      Case.BINARY.simple.THEN<T> THEN(byte[] binary);
      Case.DATE.simple.THEN<T> THEN(LocalDate date);
      Case.TIME.simple.THEN<T> THEN(LocalTime time);
      Case.DATETIME.simple.THEN<T> THEN(LocalDateTime dateTime);
      Case.CHAR.simple.THEN<T> THEN(String text);
      Case.ENUM.simple.THEN<T> THEN(Enum<?> dateTime);
    }
  }

  interface search {
    interface WHEN<T> {
      Case.BOOLEAN.search.THEN<T> THEN(type.BOOLEAN bool);
      Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
      Case.FLOAT.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
      Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
      Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
      Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
      Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
      Case.TINYINT.search.THEN<T> THEN(type.TINYINT numeric);
      Case.TINYINT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
      Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT numeric);
      Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
      Case.INT.search.THEN<T> THEN(type.INT numeric);
      Case.INT.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
      Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
      Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

      Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
      Case.FLOAT.search.THEN<T> THEN(float numeric);
      Case.FLOAT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
      Case.DOUBLE.search.THEN<T> THEN(double numeric);
      Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
      Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
      Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
      Case.TINYINT.search.THEN<T> THEN(byte numeric);
      Case.TINYINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
      Case.SMALLINT.search.THEN<T> THEN(short numeric);
      Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
      Case.INT.search.THEN<T> THEN(int numeric);
      Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
      Case.BIGINT.search.THEN<T> THEN(long numeric);
      Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);

      Case.BINARY.search.THEN<T> THEN(type.BINARY binary);
      Case.DATE.search.THEN<T> THEN(type.DATE date);
      Case.TIME.search.THEN<T> THEN(type.TIME time);
      Case.DATETIME.search.THEN<T> THEN(type.DATETIME dateTime);
      Case.CHAR.search.THEN<T> THEN(type.CHAR text);
      Case.ENUM.search.THEN<T> THEN(type.ENUM<?> dateTime);
      Case.BINARY.search.THEN<T> THEN(byte[] binary);
      Case.DATE.search.THEN<T> THEN(LocalDate date);
      Case.TIME.search.THEN<T> THEN(LocalTime time);
      Case.DATETIME.search.THEN<T> THEN(LocalDateTime dateTime);
      Case.CHAR.search.THEN<T> THEN(String text);
      Case.ENUM.search.THEN<T> THEN(Enum<?> text);
    }
  }

  interface BOOLEAN {
    interface simple {
      interface WHEN {
        Case.BOOLEAN.simple.THEN THEN(type.BOOLEAN bool);
        Case.BOOLEAN.simple.THEN THEN(boolean bool);
      }

      interface THEN extends BOOLEAN.THEN {
        Case.BOOLEAN.simple.WHEN WHEN(boolean condition);
      }
    }

    interface search {
      interface CASE<T> {
        Case.BOOLEAN.search.THEN<T> THEN(type.BOOLEAN bool);
        Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
      }

      interface WHEN<T> {
        Case.BOOLEAN.search.THEN<T> THEN(type.BOOLEAN bool);
        Case.BOOLEAN.search.THEN<T> THEN(boolean bool);
      }

      interface THEN<T> extends BOOLEAN.THEN {
        Case.BOOLEAN.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.BOOLEAN.ELSE ELSE(type.BOOLEAN bool);
      Case.BOOLEAN.ELSE ELSE(boolean bool);
    }

    interface ELSE {
      type.BOOLEAN END();
    }
  }

  interface FLOAT {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.FLOAT.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.INT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.FLOAT.simple.THEN<T> THEN(byte numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DOUBLE.simple.THEN<T> THEN(short numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DOUBLE.simple.THEN<T> THEN(int numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DOUBLE.simple.THEN<T> THEN(long numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          Case.FLOAT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.FLOAT.search.THEN<T> THEN(type.TINYINT numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.INT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.BIGINT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.FLOAT.search.THEN<T> THEN(byte numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DOUBLE.search.THEN<T> THEN(short numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DOUBLE.search.THEN<T> THEN(int numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DOUBLE.search.THEN<T> THEN(long numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends FLOAT.UNSIGNED.THEN {
          Case.FLOAT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.FLOAT.ELSE ELSE(type.TINYINT numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.SMALLINT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.INT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.BIGINT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.FLOAT.ELSE ELSE(byte numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.DOUBLE.ELSE ELSE(short numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.DOUBLE.ELSE ELSE(int numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.DOUBLE.ELSE ELSE(long numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.FLOAT.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.FLOAT.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.FLOAT.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.INT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.FLOAT.simple.THEN<T> THEN(byte numeric);
        Case.DOUBLE.simple.THEN<T> THEN(short numeric);
        Case.DOUBLE.simple.THEN<T> THEN(int numeric);
        Case.DOUBLE.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends FLOAT.THEN {
        Case.FLOAT.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.FLOAT.search.THEN<T> THEN(type.TINYINT numeric);
        Case.FLOAT.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.INT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.BIGINT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.FLOAT.search.THEN<T> THEN(byte numeric);
        Case.DOUBLE.search.THEN<T> THEN(short numeric);
        Case.DOUBLE.search.THEN<T> THEN(int numeric);
        Case.DOUBLE.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends FLOAT.THEN {
        Case.FLOAT.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.FLOAT.ELSE ELSE(type.TINYINT numeric);
      Case.FLOAT.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.SMALLINT numeric);
      Case.DOUBLE.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.INT numeric);
      Case.DOUBLE.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.BIGINT numeric);
      Case.DOUBLE.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.FLOAT.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.FLOAT.ELSE ELSE(byte numeric);
      Case.DOUBLE.ELSE ELSE(short numeric);
      Case.DOUBLE.ELSE ELSE(int numeric);
      Case.DOUBLE.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.FLOAT END();
    }
  }

  interface DOUBLE {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.INT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.DOUBLE.simple.THEN<T> THEN(byte numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DOUBLE.simple.THEN<T> THEN(short numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DOUBLE.simple.THEN<T> THEN(int numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DOUBLE.simple.THEN<T> THEN(long numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          Case.DOUBLE.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.TINYINT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.INT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.BIGINT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.DOUBLE.search.THEN<T> THEN(byte numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DOUBLE.search.THEN<T> THEN(short numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DOUBLE.search.THEN<T> THEN(int numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DOUBLE.search.THEN<T> THEN(long numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends DOUBLE.UNSIGNED.THEN {
          Case.DOUBLE.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.TINYINT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.SMALLINT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.INT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.BIGINT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.DOUBLE.ELSE ELSE(byte numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.DOUBLE.ELSE ELSE(short numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.DOUBLE.ELSE ELSE(int numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.DOUBLE.ELSE ELSE(long numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.DOUBLE.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.INT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.DOUBLE.simple.THEN<T> THEN(byte numeric);
        Case.DOUBLE.simple.THEN<T> THEN(short numeric);
        Case.DOUBLE.simple.THEN<T> THEN(int numeric);
        Case.DOUBLE.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends DOUBLE.THEN {
        Case.DOUBLE.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.TINYINT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.INT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.BIGINT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.DOUBLE.search.THEN<T> THEN(byte numeric);
        Case.DOUBLE.search.THEN<T> THEN(short numeric);
        Case.DOUBLE.search.THEN<T> THEN(int numeric);
        Case.DOUBLE.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends DOUBLE.THEN {
        Case.DOUBLE.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.DOUBLE.ELSE ELSE(type.TINYINT numeric);
      Case.DOUBLE.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.SMALLINT numeric);
      Case.DOUBLE.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.INT numeric);
      Case.DOUBLE.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.DOUBLE.ELSE ELSE(type.BIGINT numeric);
      Case.DOUBLE.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.DOUBLE.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.DOUBLE.ELSE ELSE(byte numeric);
      Case.DOUBLE.ELSE ELSE(short numeric);
      Case.DOUBLE.ELSE ELSE(int numeric);
      Case.DOUBLE.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.DOUBLE END();
    }
  }

  interface DECIMAL {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.DECIMAL.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.INT numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DECIMAL.simple.THEN<T> THEN(float numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DECIMAL.simple.THEN<T> THEN(double numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.DECIMAL.simple.THEN<T> THEN(byte numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DECIMAL.simple.THEN<T> THEN(short numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DECIMAL.simple.THEN<T> THEN(int numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DECIMAL.simple.THEN<T> THEN(long numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          Case.DECIMAL.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.DECIMAL.search.THEN<T> THEN(type.FLOAT numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.TINYINT numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.INT numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.BIGINT numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DECIMAL.search.THEN<T> THEN(float numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DECIMAL.search.THEN<T> THEN(double numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.DECIMAL.search.THEN<T> THEN(byte numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.DECIMAL.search.THEN<T> THEN(short numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.DECIMAL.search.THEN<T> THEN(int numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.DECIMAL.search.THEN<T> THEN(long numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends DECIMAL.UNSIGNED.THEN {
          Case.DECIMAL.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DECIMAL.ELSE ELSE(type.FLOAT numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DOUBLE numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.TINYINT numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.SMALLINT numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.INT numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.BIGINT numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.DECIMAL.ELSE ELSE(float numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DECIMAL.ELSE ELSE(double numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.DECIMAL.ELSE ELSE(byte numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.DECIMAL.ELSE ELSE(short numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.DECIMAL.ELSE ELSE(int numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.DECIMAL.ELSE ELSE(long numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.DECIMAL.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.DECIMAL.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.INT numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DECIMAL.simple.THEN<T> THEN(float numeric);
        Case.DECIMAL.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.DECIMAL.simple.THEN<T> THEN(byte numeric);
        Case.DECIMAL.simple.THEN<T> THEN(short numeric);
        Case.DECIMAL.simple.THEN<T> THEN(int numeric);
        Case.DECIMAL.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends DECIMAL.THEN {
        Case.DECIMAL.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DECIMAL.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.TINYINT numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.INT numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.BIGINT numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DECIMAL.search.THEN<T> THEN(float numeric);
        Case.DECIMAL.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.DECIMAL.search.THEN<T> THEN(byte numeric);
        Case.DECIMAL.search.THEN<T> THEN(short numeric);
        Case.DECIMAL.search.THEN<T> THEN(int numeric);
        Case.DECIMAL.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends DECIMAL.THEN {
        Case.DECIMAL.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DECIMAL.ELSE ELSE(type.FLOAT numeric);
      Case.DECIMAL.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.DECIMAL.ELSE ELSE(type.TINYINT numeric);
      Case.DECIMAL.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.DECIMAL.ELSE ELSE(type.SMALLINT numeric);
      Case.DECIMAL.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.DECIMAL.ELSE ELSE(type.INT numeric);
      Case.DECIMAL.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.DECIMAL.ELSE ELSE(type.BIGINT numeric);
      Case.DECIMAL.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.DECIMAL.ELSE ELSE(float numeric);
      Case.DECIMAL.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.DECIMAL.ELSE ELSE(byte numeric);
      Case.DECIMAL.ELSE ELSE(short numeric);
      Case.DECIMAL.ELSE ELSE(int numeric);
      Case.DECIMAL.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.DECIMAL END();
    }
  }

  interface TINYINT {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.TINYINT.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.simple.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.TINYINT.simple.THEN<T> THEN(byte numeric);
          Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.SMALLINT.simple.THEN<T> THEN(short numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          Case.TINYINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.TINYINT.search.THEN<T> THEN(type.TINYINT numeric);
          Case.TINYINT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.search.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.TINYINT.search.THEN<T> THEN(byte numeric);
          Case.TINYINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.SMALLINT.search.THEN<T> THEN(short numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends TINYINT.UNSIGNED.THEN {
          Case.TINYINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.TINYINT.ELSE ELSE(type.TINYINT numeric);
        Case.TINYINT.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.ELSE ELSE(type.SMALLINT numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.INT.ELSE ELSE(type.INT numeric);
        Case.INT.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.TINYINT.ELSE ELSE(byte numeric);
        Case.TINYINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.SMALLINT.ELSE ELSE(short numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.INT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.TINYINT.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.TINYINT.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.TINYINT.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.simple.THEN<T> THEN(type.INT numeric);
        Case.INT.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.TINYINT.simple.THEN<T> THEN(byte numeric);
        Case.SMALLINT.simple.THEN<T> THEN(short numeric);
        Case.INT.simple.THEN<T> THEN(int numeric);
        Case.BIGINT.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends TINYINT.THEN {
        Case.TINYINT.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.TINYINT.search.THEN<T> THEN(type.TINYINT numeric);
        Case.TINYINT.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.search.THEN<T> THEN(type.INT numeric);
        Case.INT.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.TINYINT.search.THEN<T> THEN(byte numeric);
        Case.SMALLINT.search.THEN<T> THEN(short numeric);
        Case.INT.search.THEN<T> THEN(int numeric);
        Case.BIGINT.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends TINYINT.THEN {
        Case.TINYINT.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.TINYINT.ELSE ELSE(type.TINYINT numeric);
      Case.TINYINT.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.SMALLINT.ELSE ELSE(type.SMALLINT numeric);
      Case.SMALLINT.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.INT.ELSE ELSE(type.INT numeric);
      Case.INT.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.FLOAT.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.TINYINT.ELSE ELSE(byte numeric);
      Case.SMALLINT.ELSE ELSE(short numeric);
      Case.INT.ELSE ELSE(int numeric);
      Case.BIGINT.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.TINYINT END();
    }
  }

  interface SMALLINT {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.SMALLINT.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.simple.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.simple.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.SMALLINT.simple.THEN<T> THEN(byte numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.SMALLINT.simple.THEN<T> THEN(short numeric);
          Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          Case.SMALLINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.SMALLINT.search.THEN<T> THEN(type.TINYINT numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.search.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.FLOAT.search.THEN<T> THEN(float numeric);
          Case.FLOAT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.SMALLINT.search.THEN<T> THEN(byte numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.SMALLINT.search.THEN<T> THEN(short numeric);
          Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends SMALLINT.UNSIGNED.THEN {
          Case.SMALLINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.SMALLINT.ELSE ELSE(type.TINYINT numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.ELSE ELSE(type.SMALLINT numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.INT.ELSE ELSE(type.INT numeric);
        Case.INT.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.ELSE ELSE(float numeric);
        Case.FLOAT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.SMALLINT.ELSE ELSE(byte numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.SMALLINT.ELSE ELSE(short numeric);
        Case.SMALLINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.INT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.SMALLINT.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.FLOAT.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.SMALLINT.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.simple.THEN<T> THEN(type.INT numeric);
        Case.INT.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.SMALLINT.simple.THEN<T> THEN(byte numeric);
        Case.SMALLINT.simple.THEN<T> THEN(short numeric);
        Case.INT.simple.THEN<T> THEN(int numeric);
        Case.BIGINT.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends SMALLINT.THEN {
        Case.SMALLINT.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.FLOAT.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.TINYINT numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.SMALLINT.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.search.THEN<T> THEN(type.INT numeric);
        Case.INT.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.FLOAT.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.SMALLINT.search.THEN<T> THEN(byte numeric);
        Case.SMALLINT.search.THEN<T> THEN(short numeric);
        Case.INT.search.THEN<T> THEN(int numeric);
        Case.BIGINT.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends SMALLINT.THEN {
        Case.SMALLINT.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.FLOAT.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.SMALLINT.ELSE ELSE(type.TINYINT numeric);
      Case.SMALLINT.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.SMALLINT.ELSE ELSE(type.SMALLINT numeric);
      Case.SMALLINT.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.INT.ELSE ELSE(type.INT numeric);
      Case.INT.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.FLOAT.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.SMALLINT.ELSE ELSE(byte numeric);
      Case.SMALLINT.ELSE ELSE(short numeric);
      Case.INT.ELSE ELSE(int numeric);
      Case.BIGINT.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.SMALLINT END();
    }
  }

  interface INT {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.INT.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.INT.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.simple.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.INT.simple.THEN<T> THEN(byte numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.INT.simple.THEN<T> THEN(short numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.simple.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends INT.UNSIGNED.THEN {
          Case.INT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.INT.search.THEN<T> THEN(type.TINYINT numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.INT.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.INT.search.THEN<T> THEN(type.INT numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.INT.search.THEN<T> THEN(byte numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.INT.search.THEN<T> THEN(short numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.INT.search.THEN<T> THEN(int numeric);
          Case.INT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends INT.UNSIGNED.THEN {
          Case.INT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.INT.ELSE ELSE(type.TINYINT numeric);
        Case.INT.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.INT.ELSE ELSE(type.SMALLINT numeric);
        Case.INT.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.INT.ELSE ELSE(type.INT numeric);
        Case.INT.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.INT.ELSE ELSE(byte numeric);
        Case.INT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.INT.ELSE ELSE(short numeric);
        Case.INT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.INT.ELSE ELSE(int numeric);
        Case.INT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.INT.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.INT.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.INT.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.INT.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.INT.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.simple.THEN<T> THEN(type.INT numeric);
        Case.INT.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.INT.simple.THEN<T> THEN(byte numeric);
        Case.INT.simple.THEN<T> THEN(short numeric);
        Case.INT.simple.THEN<T> THEN(int numeric);
        Case.BIGINT.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends INT.THEN {
        Case.INT.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.INT.search.THEN<T> THEN(type.TINYINT numeric);
        Case.INT.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.INT.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.INT.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.INT.search.THEN<T> THEN(type.INT numeric);
        Case.INT.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.INT.search.THEN<T> THEN(byte numeric);
        Case.INT.search.THEN<T> THEN(short numeric);
        Case.INT.search.THEN<T> THEN(int numeric);
        Case.BIGINT.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends INT.THEN {
        Case.INT.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.INT.ELSE ELSE(type.TINYINT numeric);
      Case.INT.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.INT.ELSE ELSE(type.SMALLINT numeric);
      Case.INT.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.INT.ELSE ELSE(type.INT numeric);
      Case.INT.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.DOUBLE.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.INT.ELSE ELSE(byte numeric);
      Case.INT.ELSE ELSE(short numeric);
      Case.INT.ELSE ELSE(int numeric);
      Case.BIGINT.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.INT END();
    }
  }

  interface BIGINT {
    interface UNSIGNED {
      interface simple {
        interface WHEN<T> {
          Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.TINYINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.SMALLINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.INT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.simple.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.simple.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.BIGINT.simple.THEN<T> THEN(byte numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.BIGINT.simple.THEN<T> THEN(short numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.BIGINT.simple.THEN<T> THEN(int numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.simple.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          Case.BIGINT.simple.WHEN<T> WHEN(T condition);
        }
      }

      interface search {
        interface WHEN<T> {
          Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.FLOAT.UNSIGNED numeric);
          Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(type.DOUBLE.UNSIGNED numeric);
          Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(type.DECIMAL.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.TINYINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.SMALLINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.INT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
          Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

          Case.DOUBLE.search.THEN<T> THEN(float numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Float numeric);
          Case.DOUBLE.search.THEN<T> THEN(double numeric);
          Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Double numeric);
          Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
          Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
          Case.BIGINT.search.THEN<T> THEN(byte numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Byte numeric);
          Case.BIGINT.search.THEN<T> THEN(short numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Short numeric);
          Case.BIGINT.search.THEN<T> THEN(int numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Integer numeric);
          Case.BIGINT.search.THEN<T> THEN(long numeric);
          Case.BIGINT.UNSIGNED.search.THEN<T> THEN(org.jaxdb.jsql.UNSIGNED.Long numeric);
        }

        interface THEN<T> extends BIGINT.UNSIGNED.THEN {
          Case.BIGINT.search.WHEN<T> WHEN(Condition<T> condition);
        }
      }

      interface THEN {
        Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.FLOAT.UNSIGNED numeric);
        Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(type.DOUBLE.UNSIGNED numeric);
        Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(type.DECIMAL.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.TINYINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.SMALLINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.INT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.INT.UNSIGNED numeric);
        Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.ELSE ELSE(float numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Float numeric);
        Case.DOUBLE.ELSE ELSE(double numeric);
        Case.DOUBLE.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Double numeric);
        Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
        Case.DECIMAL.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.BigDecimal numeric);
        Case.BIGINT.ELSE ELSE(byte numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Byte numeric);
        Case.BIGINT.ELSE ELSE(short numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Short numeric);
        Case.BIGINT.ELSE ELSE(int numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Integer numeric);
        Case.BIGINT.ELSE ELSE(long numeric);
        Case.BIGINT.UNSIGNED.ELSE ELSE(org.jaxdb.jsql.UNSIGNED.Long numeric);
      }

      interface ELSE {
        type.BIGINT.UNSIGNED END();
      }
    }

    interface simple {
      interface WHEN<T> {
        Case.DOUBLE.simple.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.simple.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.simple.THEN<T> THEN(type.DECIMAL numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.TINYINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.SMALLINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.INT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.simple.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.simple.THEN<T> THEN(float numeric);
        Case.DOUBLE.simple.THEN<T> THEN(double numeric);
        Case.DECIMAL.simple.THEN<T> THEN(BigDecimal numeric);
        Case.BIGINT.simple.THEN<T> THEN(byte numeric);
        Case.BIGINT.simple.THEN<T> THEN(short numeric);
        Case.BIGINT.simple.THEN<T> THEN(int numeric);
        Case.BIGINT.simple.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends BIGINT.THEN {
        Case.BIGINT.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DOUBLE.search.THEN<T> THEN(type.FLOAT numeric);
        Case.DOUBLE.search.THEN<T> THEN(type.DOUBLE numeric);
        Case.DECIMAL.search.THEN<T> THEN(type.DECIMAL numeric);
        Case.BIGINT.search.THEN<T> THEN(type.TINYINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.TINYINT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.SMALLINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.SMALLINT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.INT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.INT.UNSIGNED numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT numeric);
        Case.BIGINT.search.THEN<T> THEN(type.BIGINT.UNSIGNED numeric);

        Case.DOUBLE.search.THEN<T> THEN(float numeric);
        Case.DOUBLE.search.THEN<T> THEN(double numeric);
        Case.DECIMAL.search.THEN<T> THEN(BigDecimal numeric);
        Case.BIGINT.search.THEN<T> THEN(byte numeric);
        Case.BIGINT.search.THEN<T> THEN(short numeric);
        Case.BIGINT.search.THEN<T> THEN(int numeric);
        Case.BIGINT.search.THEN<T> THEN(long numeric);
      }

      interface THEN<T> extends BIGINT.THEN {
        Case.BIGINT.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DOUBLE.ELSE ELSE(type.FLOAT numeric);
      Case.DOUBLE.ELSE ELSE(type.DOUBLE numeric);
      Case.DECIMAL.ELSE ELSE(type.DECIMAL numeric);
      Case.BIGINT.ELSE ELSE(type.TINYINT numeric);
      Case.BIGINT.ELSE ELSE(type.TINYINT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.SMALLINT numeric);
      Case.BIGINT.ELSE ELSE(type.SMALLINT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.INT numeric);
      Case.BIGINT.ELSE ELSE(type.INT.UNSIGNED numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT numeric);
      Case.BIGINT.ELSE ELSE(type.BIGINT.UNSIGNED numeric);

      Case.DOUBLE.ELSE ELSE(float numeric);
      Case.DOUBLE.ELSE ELSE(double numeric);
      Case.DECIMAL.ELSE ELSE(BigDecimal numeric);
      Case.BIGINT.ELSE ELSE(byte numeric);
      Case.BIGINT.ELSE ELSE(short numeric);
      Case.BIGINT.ELSE ELSE(int numeric);
      Case.BIGINT.ELSE ELSE(long numeric);
    }

    interface ELSE {
      type.BIGINT END();
    }
  }

  interface BINARY {
    interface simple {
      interface WHEN<T> {
        Case.BINARY.simple.THEN<T> THEN(type.BINARY binary);
        Case.BINARY.simple.THEN<T> THEN(byte[] binary);
      }

      interface THEN<T> extends BINARY.THEN {
        Case.BINARY.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.BINARY.search.THEN<T> THEN(type.BINARY binary);
        Case.BINARY.search.THEN<T> THEN(byte[] binary);
      }

      interface THEN<T> extends BINARY.THEN {
        Case.BINARY.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.BINARY.ELSE ELSE(type.BINARY binary);
      Case.BINARY.ELSE ELSE(byte[] binary);
    }

    interface ELSE {
      type.BINARY END();
    }
  }

  interface DATE {
    interface simple {
      interface WHEN<T> {
        Case.DATE.simple.THEN<T> THEN(type.DATE date);
        Case.DATE.simple.THEN<T> THEN(LocalDate date);
      }

      interface THEN<T> extends DATE.THEN {
        Case.DATE.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DATE.search.THEN<T> THEN(type.DATE date);
        Case.DATE.search.THEN<T> THEN(LocalDate date);
      }

      interface THEN<T> extends DATE.THEN {
        Case.DATE.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DATE.ELSE ELSE(type.DATE date);
      Case.DATE.ELSE ELSE(LocalDate date);
    }

    interface ELSE {
      type.DATE END();
    }
  }

  interface TIME {
    interface simple {
      interface WHEN<T> {
        Case.TIME.simple.THEN<T> THEN(type.TIME time);
        Case.TIME.simple.THEN<T> THEN(LocalTime time);
      }

      interface THEN<T> extends TIME.THEN {
        Case.TIME.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.TIME.search.THEN<T> THEN(type.TIME time);
        Case.TIME.search.THEN<T> THEN(LocalTime time);
      }

      interface THEN<T> extends TIME.THEN {
        Case.TIME.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.TIME.ELSE ELSE(type.TIME time);
      Case.TIME.ELSE ELSE(LocalTime time);
    }

    interface ELSE {
      type.TIME END();
    }
  }

  interface DATETIME {
    interface simple {
      interface WHEN<T> {
        Case.DATETIME.simple.THEN<T> THEN(type.DATETIME dateTime);
        Case.DATETIME.simple.THEN<T> THEN(LocalDateTime dateTime);
      }

      interface THEN<T> extends DATETIME.THEN {
        Case.DATETIME.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.DATETIME.search.THEN<T> THEN(type.DATETIME dateTime);
        Case.DATETIME.search.THEN<T> THEN(LocalDateTime dateTime);
      }

      interface THEN<T> extends DATETIME.THEN {
        Case.DATETIME.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.DATETIME.ELSE ELSE(type.DATETIME dateTime);
      Case.DATETIME.ELSE ELSE(LocalDateTime dateTime);
    }

    interface ELSE {
      type.DATETIME END();
    }
  }

  interface CHAR {
    interface simple {
      interface WHEN<T> {
        Case.CHAR.simple.THEN<T> THEN(type.ENUM<?> text);
        Case.CHAR.simple.THEN<T> THEN(type.CHAR text);
        Case.CHAR.simple.THEN<T> THEN(Enum<?> text);
        Case.CHAR.simple.THEN<T> THEN(String text);
      }

      interface THEN<T> extends CHAR.THEN {
        Case.CHAR.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.CHAR.search.THEN<T> THEN(type.ENUM<?> text);
        Case.CHAR.search.THEN<T> THEN(type.CHAR text);
        Case.CHAR.search.THEN<T> THEN(Enum<?> text);
        Case.CHAR.search.THEN<T> THEN(String text);
      }

      interface THEN<T> extends CHAR.THEN {
        Case.CHAR.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.CHAR.ELSE ELSE(type.ENUM<?> text);
      Case.CHAR.ELSE ELSE(type.CHAR text);
      Case.CHAR.ELSE ELSE(Enum<?> text);
      Case.CHAR.ELSE ELSE(String text);
    }

    interface ELSE {
      type.CHAR END();
    }
  }

  interface ENUM {
    interface simple {
      interface WHEN<T> {
        Case.ENUM.simple.THEN<T> THEN(type.ENUM<?> text);
        Case.CHAR.simple.THEN<T> THEN(type.CHAR text);
        Case.ENUM.simple.THEN<T> THEN(Enum<?> text);
        Case.CHAR.simple.THEN<T> THEN(String text);
      }

      interface THEN<T> extends ENUM.THEN {
        Case.ENUM.simple.WHEN<T> WHEN(T condition);
      }
    }

    interface search {
      interface WHEN<T> {
        Case.ENUM.search.THEN<T> THEN(type.ENUM<?> text);
        Case.CHAR.search.THEN<T> THEN(type.CHAR text);
        Case.ENUM.search.THEN<T> THEN(Enum<?> text);
        Case.CHAR.search.THEN<T> THEN(String text);
      }

      interface THEN<T> extends ENUM.THEN {
        Case.ENUM.search.WHEN<T> WHEN(Condition<T> condition);
      }
    }

    interface THEN {
      Case.ENUM.ELSE ELSE(type.ENUM<?> text);
      Case.CHAR.ELSE ELSE(type.CHAR text);
      Case.ENUM.ELSE ELSE(Enum<?> text);
      Case.CHAR.ELSE ELSE(String text);
    }

    interface ELSE {
      type.ENUM<?> END();
    }
  }
}