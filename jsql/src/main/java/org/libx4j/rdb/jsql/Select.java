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

import org.libx4j.rdb.jsql.Condition;
import org.libx4j.rdb.jsql.EntityEnum;
import org.libx4j.rdb.jsql.Subject;
import org.libx4j.rdb.jsql.type;

@SuppressWarnings("rawtypes")
public interface select {
  public interface typed {
    public interface OFFSET<T extends Subject<?>> extends SELECT<T>, Entity.OFFSET<T>, DataType.OFFSET<T>, Numeric.OFFSET<T>, Textual.OFFSET<T>, ARRAY.OFFSET<T>, BIGINT.OFFSET<T>, BINARY.OFFSET<T>, BLOB.OFFSET<T>, BOOLEAN.OFFSET<T>, CHAR.OFFSET<T>, CLOB.OFFSET<T>, DATE.OFFSET<T>, DATETIME.OFFSET<T>, DECIMAL.OFFSET<T>, DOUBLE.OFFSET<T>, ENUM.OFFSET<T>, FLOAT.OFFSET<T>, INT.OFFSET<T>, SMALLINT.OFFSET<T>, TIME.OFFSET<T>, TINYINT.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends Entity._LIMIT<T>, DataType._LIMIT<T>, Numeric._LIMIT<T>, Textual._LIMIT<T>, ARRAY._LIMIT<T>, BIGINT._LIMIT<T>, BINARY._LIMIT<T>, BLOB._LIMIT<T>, BOOLEAN._LIMIT<T>, CHAR._LIMIT<T>, CLOB._LIMIT<T>, DATE._LIMIT<T>, DATETIME._LIMIT<T>, DECIMAL._LIMIT<T>, DOUBLE._LIMIT<T>, ENUM._LIMIT<T>, FLOAT._LIMIT<T>, INT._LIMIT<T>, SMALLINT._LIMIT<T>, TIME._LIMIT<T>, TINYINT._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends SELECT<T>, Entity.LIMIT<T>, DataType.LIMIT<T>, Numeric.LIMIT<T>, Textual.LIMIT<T>, ARRAY.LIMIT<T>, BIGINT.LIMIT<T>, BINARY.LIMIT<T>, BLOB.LIMIT<T>, BOOLEAN.LIMIT<T>, CHAR.LIMIT<T>, CLOB.LIMIT<T>, DATE.LIMIT<T>, DATETIME.LIMIT<T>, DECIMAL.LIMIT<T>, DOUBLE.LIMIT<T>, ENUM.LIMIT<T>, FLOAT.LIMIT<T>, INT.LIMIT<T>, SMALLINT.LIMIT<T>, TIME.LIMIT<T>, TINYINT.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends Entity._ORDER_BY<T>, DataType._ORDER_BY<T>, Numeric._ORDER_BY<T>, Textual._ORDER_BY<T>, ARRAY._ORDER_BY<T>, BIGINT._ORDER_BY<T>, BINARY._ORDER_BY<T>, BLOB._ORDER_BY<T>, BOOLEAN._ORDER_BY<T>, CHAR._ORDER_BY<T>, CLOB._ORDER_BY<T>, DATE._ORDER_BY<T>, DATETIME._ORDER_BY<T>, DECIMAL._ORDER_BY<T>, DOUBLE._ORDER_BY<T>, ENUM._ORDER_BY<T>, FLOAT._ORDER_BY<T>, INT._ORDER_BY<T>, SMALLINT._ORDER_BY<T>, TIME._ORDER_BY<T>, TINYINT._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, Entity.ORDER_BY<T>, DataType.ORDER_BY<T>, Numeric.ORDER_BY<T>, Textual.ORDER_BY<T>, ARRAY.ORDER_BY<T>, BIGINT.ORDER_BY<T>, BINARY.ORDER_BY<T>, BLOB.ORDER_BY<T>, BOOLEAN.ORDER_BY<T>, CHAR.ORDER_BY<T>, CLOB.ORDER_BY<T>, DATE.ORDER_BY<T>, DATETIME.ORDER_BY<T>, DECIMAL.ORDER_BY<T>, DOUBLE.ORDER_BY<T>, ENUM.ORDER_BY<T>, FLOAT.ORDER_BY<T>, INT.ORDER_BY<T>, SMALLINT.ORDER_BY<T>, TIME.ORDER_BY<T>, TINYINT.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends Entity._GROUP_BY<T>, DataType._GROUP_BY<T>, Numeric._GROUP_BY<T>, Textual._GROUP_BY<T>, ARRAY._GROUP_BY<T>, BIGINT._GROUP_BY<T>, BINARY._GROUP_BY<T>, BLOB._GROUP_BY<T>, BOOLEAN._GROUP_BY<T>, CHAR._GROUP_BY<T>, CLOB._GROUP_BY<T>, DATE._GROUP_BY<T>, DATETIME._GROUP_BY<T>, DECIMAL._GROUP_BY<T>, DOUBLE._GROUP_BY<T>, ENUM._GROUP_BY<T>, FLOAT._GROUP_BY<T>, INT._GROUP_BY<T>, SMALLINT._GROUP_BY<T>, TIME._GROUP_BY<T>, TINYINT._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, Entity.GROUP_BY<T>, DataType.GROUP_BY<T>, Numeric.GROUP_BY<T>, Textual.GROUP_BY<T>, ARRAY.GROUP_BY<T>, BIGINT.GROUP_BY<T>, BINARY.GROUP_BY<T>, BLOB.GROUP_BY<T>, BOOLEAN.GROUP_BY<T>, CHAR.GROUP_BY<T>, CLOB.GROUP_BY<T>, DATE.GROUP_BY<T>, DATETIME.GROUP_BY<T>, DECIMAL.GROUP_BY<T>, DOUBLE.GROUP_BY<T>, ENUM.GROUP_BY<T>, FLOAT.GROUP_BY<T>, INT.GROUP_BY<T>, SMALLINT.GROUP_BY<T>, TIME.GROUP_BY<T>, TINYINT.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends Entity._HAVING<T>, DataType._HAVING<T>, Numeric._HAVING<T>, Textual._HAVING<T>, ARRAY._HAVING<T>, BIGINT._HAVING<T>, BINARY._HAVING<T>, BLOB._HAVING<T>, BOOLEAN._HAVING<T>, CHAR._HAVING<T>, CLOB._HAVING<T>, DATE._HAVING<T>, DATETIME._HAVING<T>, DECIMAL._HAVING<T>, DOUBLE._HAVING<T>, ENUM._HAVING<T>, FLOAT._HAVING<T>, INT._HAVING<T>, SMALLINT._HAVING<T>, TIME._HAVING<T>, TINYINT._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, Entity.HAVING<T>, DataType.HAVING<T>, Numeric.HAVING<T>, Textual.HAVING<T>, ARRAY.HAVING<T>, BIGINT.HAVING<T>, BINARY.HAVING<T>, BLOB.HAVING<T>, BOOLEAN.HAVING<T>, CHAR.HAVING<T>, CLOB.HAVING<T>, DATE.HAVING<T>, DATETIME.HAVING<T>, DECIMAL.HAVING<T>, DOUBLE.HAVING<T>, ENUM.HAVING<T>, FLOAT.HAVING<T>, INT.HAVING<T>, SMALLINT.HAVING<T>, TIME.HAVING<T>, TINYINT.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends Entity._WHERE<T>, DataType._WHERE<T>, Numeric._WHERE<T>, Textual._WHERE<T>, ARRAY._WHERE<T>, BIGINT._WHERE<T>, BINARY._WHERE<T>, BLOB._WHERE<T>, BOOLEAN._WHERE<T>, CHAR._WHERE<T>, CLOB._WHERE<T>, DATE._WHERE<T>, DATETIME._WHERE<T>, DECIMAL._WHERE<T>, DOUBLE._WHERE<T>, ENUM._WHERE<T>, FLOAT._WHERE<T>, INT._WHERE<T>, SMALLINT._WHERE<T>, TIME._WHERE<T>, TINYINT._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, Entity.WHERE<T>, DataType.WHERE<T>, Numeric.WHERE<T>, Textual.WHERE<T>, ARRAY.WHERE<T>, BIGINT.WHERE<T>, BINARY.WHERE<T>, BLOB.WHERE<T>, BOOLEAN.WHERE<T>, CHAR.WHERE<T>, CLOB.WHERE<T>, DATE.WHERE<T>, DATETIME.WHERE<T>, DECIMAL.WHERE<T>, DOUBLE.WHERE<T>, ENUM.WHERE<T>, FLOAT.WHERE<T>, INT.WHERE<T>, SMALLINT.WHERE<T>, TIME.WHERE<T>, TINYINT.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends Entity._ON<T>, DataType._ON<T>, Numeric._ON<T>, Textual._ON<T>, ARRAY._ON<T>, BIGINT._ON<T>, BINARY._ON<T>, BLOB._ON<T>, BOOLEAN._ON<T>, CHAR._ON<T>, CLOB._ON<T>, DATE._ON<T>, DATETIME._ON<T>, DECIMAL._ON<T>, DOUBLE._ON<T>, ENUM._ON<T>, FLOAT._ON<T>, INT._ON<T>, SMALLINT._ON<T>, TIME._ON<T>, TINYINT._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, Entity.ON<T>, DataType.ON<T>, Numeric.ON<T>, Textual.ON<T>, ARRAY.ON<T>, BIGINT.ON<T>, BINARY.ON<T>, BLOB.ON<T>, BOOLEAN.ON<T>, CHAR.ON<T>, CLOB.ON<T>, DATE.ON<T>, DATETIME.ON<T>, DECIMAL.ON<T>, DOUBLE.ON<T>, ENUM.ON<T>, FLOAT.ON<T>, INT.ON<T>, SMALLINT.ON<T>, TIME.ON<T>, TINYINT.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends Entity._JOIN<T>, DataType._JOIN<T>, Numeric._JOIN<T>, Textual._JOIN<T>, ARRAY._JOIN<T>, BIGINT._JOIN<T>, BINARY._JOIN<T>, BLOB._JOIN<T>, BOOLEAN._JOIN<T>, CHAR._JOIN<T>, CLOB._JOIN<T>, DATE._JOIN<T>, DATETIME._JOIN<T>, DECIMAL._JOIN<T>, DOUBLE._JOIN<T>, ENUM._JOIN<T>, FLOAT._JOIN<T>, INT._JOIN<T>, SMALLINT._JOIN<T>, TIME._JOIN<T>, TINYINT._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends _JOIN<T>, _ON<T>, Entity.JOIN<T>, DataType.JOIN<T>, Numeric.JOIN<T>, Textual.JOIN<T>, ARRAY.JOIN<T>, BIGINT.JOIN<T>, BINARY.JOIN<T>, BLOB.JOIN<T>, BOOLEAN.JOIN<T>, CHAR.JOIN<T>, CLOB.JOIN<T>, DATE.JOIN<T>, DATETIME.JOIN<T>, DECIMAL.JOIN<T>, DOUBLE.JOIN<T>, ENUM.JOIN<T>, FLOAT.JOIN<T>, INT.JOIN<T>, SMALLINT.JOIN<T>, TIME.JOIN<T>, TINYINT.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends SELECT<T>, _JOIN<T>, Entity.ADV_JOIN<T>, DataType.ADV_JOIN<T>, Numeric.ADV_JOIN<T>, Textual.ADV_JOIN<T>, ARRAY.ADV_JOIN<T>, BIGINT.ADV_JOIN<T>, BINARY.ADV_JOIN<T>, BLOB.ADV_JOIN<T>, BOOLEAN.ADV_JOIN<T>, CHAR.ADV_JOIN<T>, CLOB.ADV_JOIN<T>, DATE.ADV_JOIN<T>, DATETIME.ADV_JOIN<T>, DECIMAL.ADV_JOIN<T>, DOUBLE.ADV_JOIN<T>, ENUM.ADV_JOIN<T>, FLOAT.ADV_JOIN<T>, INT.ADV_JOIN<T>, SMALLINT.ADV_JOIN<T>, TIME.ADV_JOIN<T>, TINYINT.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends Entity._FROM<T>, DataType._FROM<T>, Numeric._FROM<T>, Textual._FROM<T>, ARRAY._FROM<T>, BIGINT._FROM<T>, BINARY._FROM<T>, BLOB._FROM<T>, BOOLEAN._FROM<T>, CHAR._FROM<T>, CLOB._FROM<T>, DATE._FROM<T>, DATETIME._FROM<T>, DECIMAL._FROM<T>, DOUBLE._FROM<T>, ENUM._FROM<T>, FLOAT._FROM<T>, INT._FROM<T>, SMALLINT._FROM<T>, TIME._FROM<T>, TINYINT._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, Entity.FROM<T>, DataType.FROM<T>, Numeric.FROM<T>, Textual.FROM<T>, ARRAY.FROM<T>, BIGINT.FROM<T>, BINARY.FROM<T>, BLOB.FROM<T>, BOOLEAN.FROM<T>, CHAR.FROM<T>, CLOB.FROM<T>, DATE.FROM<T>, DATETIME.FROM<T>, DECIMAL.FROM<T>, DOUBLE.FROM<T>, ENUM.FROM<T>, FLOAT.FROM<T>, INT.FROM<T>, SMALLINT.FROM<T>, TIME.FROM<T>, TINYINT.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, Entity._SELECT<T>, DataType._SELECT<T>, Numeric._SELECT<T>, Textual._SELECT<T>, ARRAY._SELECT<T>, BIGINT._SELECT<T>, BINARY._SELECT<T>, BLOB._SELECT<T>, BOOLEAN._SELECT<T>, CHAR._SELECT<T>, CLOB._SELECT<T>, DATE._SELECT<T>, DATETIME._SELECT<T>, DECIMAL._SELECT<T>, DOUBLE._SELECT<T>, ENUM._SELECT<T>, FLOAT._SELECT<T>, INT._SELECT<T>, SMALLINT._SELECT<T>, TIME._SELECT<T>, TINYINT._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends Entity._UNION<T>, DataType._UNION<T>, Numeric._UNION<T>, Textual._UNION<T>, ARRAY._UNION<T>, BIGINT._UNION<T>, BINARY._UNION<T>, BLOB._UNION<T>, BOOLEAN._UNION<T>, CHAR._UNION<T>, CLOB._UNION<T>, DATE._UNION<T>, DATETIME._UNION<T>, DECIMAL._UNION<T>, DOUBLE._UNION<T>, ENUM._UNION<T>, FLOAT._UNION<T>, INT._UNION<T>, SMALLINT._UNION<T>, TIME._UNION<T>, TINYINT._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends Entity._UNION.ALL<T>, DataType._UNION.ALL<T>, Numeric._UNION.ALL<T>, Textual._UNION.ALL<T>, ARRAY._UNION.ALL<T>, BIGINT._UNION.ALL<T>, BINARY._UNION.ALL<T>, BLOB._UNION.ALL<T>, BOOLEAN._UNION.ALL<T>, CHAR._UNION.ALL<T>, CLOB._UNION.ALL<T>, DATE._UNION.ALL<T>, DATETIME._UNION.ALL<T>, DECIMAL._UNION.ALL<T>, DOUBLE._UNION.ALL<T>, ENUM._UNION.ALL<T>, FLOAT._UNION.ALL<T>, INT._UNION.ALL<T>, SMALLINT._UNION.ALL<T>, TIME._UNION.ALL<T>, TINYINT._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T>, Entity.UNION<T>, DataType.UNION<T>, Numeric.UNION<T>, Textual.UNION<T>, ARRAY.UNION<T>, BIGINT.UNION<T>, BINARY.UNION<T>, BLOB.UNION<T>, BOOLEAN.UNION<T>, CHAR.UNION<T>, CLOB.UNION<T>, DATE.UNION<T>, DATETIME.UNION<T>, DECIMAL.UNION<T>, DOUBLE.UNION<T>, ENUM.UNION<T>, FLOAT.UNION<T>, INT.UNION<T>, SMALLINT.UNION<T>, TIME.UNION<T>, TINYINT.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T>, Entity.SELECT<T>, DataType.SELECT<T>, Numeric.SELECT<T>, Textual.SELECT<T>, ARRAY.SELECT<T>, BIGINT.SELECT<T>, BINARY.SELECT<T>, BLOB.SELECT<T>, BOOLEAN.SELECT<T>, CHAR.SELECT<T>, CLOB.SELECT<T>, DATE.SELECT<T>, DATETIME.SELECT<T>, DECIMAL.SELECT<T>, DOUBLE.SELECT<T>, ENUM.SELECT<T>, FLOAT.SELECT<T>, INT.SELECT<T>, SMALLINT.SELECT<T>, TIME.SELECT<T>, TINYINT.SELECT<T> {
      @Override
      public T AS(final T as);

      // FIXME: Remove this:
      @Override
      public T AS();
    }
  }

  public interface untyped {
    public interface OFFSET<T extends Subject<?>> extends SELECT<T> {
    }

    public interface _LIMIT<T extends Subject<?>> {
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends SELECT<T> {
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> {
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> {
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
    }

    public interface _HAVING<T extends Subject<?>> {
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _WHERE<T extends Subject<?>> {
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _ON<T extends Subject<?>> {
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _JOIN<T extends Subject<?>> {
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends _JOIN<T>, _ON<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> {
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
    }

    public interface _UNION<T extends Subject<?>> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends ExecuteQuery<T>, _UNION<T> {
      public T AS(final T as);
      public T AS();
    }
  }

  public interface Entity {
    public interface OFFSET<T extends Subject<?>> extends kind.Entity, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Entity, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Entity, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Entity, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Entity, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Entity, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Entity, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Entity, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Entity, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.Entity, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Entity, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Entity, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Entity, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Entity, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Entity, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Entity, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface DataType {
    public interface OFFSET<T extends Subject<?>> extends kind.Entity, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Entity, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Entity, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Entity, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Entity, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Entity, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Entity, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Entity, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Entity, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.Entity, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Entity, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Entity, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Entity, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Entity, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Entity, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Entity, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Entity, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Entity, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface Numeric {
    public interface OFFSET<T extends Subject<?>> extends kind.Numeric, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Numeric, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Numeric, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Numeric, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Numeric, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Numeric, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Numeric, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Numeric, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Numeric, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.Numeric, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Numeric, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Numeric, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Numeric, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Numeric, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Numeric, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Numeric, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface Textual<T> {
    public interface OFFSET<T extends Subject<?>> extends kind.Textual, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Textual, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Textual, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Textual, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Textual, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Textual, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Textual, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Textual, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Textual, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Textual, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Textual, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Textual, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Textual, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Textual, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.Textual, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Textual, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Textual, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Textual, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Textual, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Textual, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Textual, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Textual, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Textual, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface ARRAY<T> {
    public interface OFFSET<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.ARRAY, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.ARRAY, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.ARRAY, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.ARRAY, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.ARRAY, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.ARRAY, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.ARRAY, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.ARRAY, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.ARRAY, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.ARRAY, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.ARRAY, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.ARRAY, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.ARRAY, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.ARRAY, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface BIGINT {
    public interface OFFSET<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BIGINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BIGINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BIGINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BIGINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BIGINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BIGINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BIGINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.BIGINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BIGINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BIGINT, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BIGINT, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface BINARY {
    public interface OFFSET<T extends Subject<?>> extends kind.BINARY, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BINARY, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BINARY, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BINARY, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BINARY, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BINARY, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BINARY, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BINARY, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BINARY, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.BINARY, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BINARY, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BINARY, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BINARY, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface BLOB {
    public interface OFFSET<T extends Subject<?>> extends kind.BLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.BLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BLOB, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BLOB, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface BOOLEAN {
    public interface OFFSET<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BOOLEAN, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BOOLEAN, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BOOLEAN, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BOOLEAN, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BOOLEAN, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BOOLEAN, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BOOLEAN, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.BOOLEAN, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BOOLEAN, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BOOLEAN, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BOOLEAN, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface CHAR {
    public interface OFFSET<T extends Subject<?>> extends kind.CHAR, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.CHAR, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.CHAR, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.CHAR, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.CHAR, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.CHAR, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.CHAR, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.CHAR, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.CHAR, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.CHAR, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.CHAR, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.CHAR, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.CHAR, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface CLOB {
    public interface OFFSET<T extends Subject<?>> extends kind.CLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.CLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.CLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.CLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.CLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.CLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.CLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.CLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.CLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.CLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.CLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.CLOB, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.CLOB, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface DATE {
    public interface OFFSET<T extends Subject<?>> extends kind.DATE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DATE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DATE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DATE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DATE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DATE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DATE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DATE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DATE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.DATE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DATE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DATE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DATE, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DATE, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DATE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DATE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface DATETIME {
    public interface OFFSET<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DATETIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DATETIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DATETIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DATETIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DATETIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DATETIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DATETIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.DATETIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DATETIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DATETIME, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DATETIME, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface DECIMAL {
    public interface OFFSET<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DECIMAL, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DECIMAL, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DECIMAL, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DECIMAL, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DECIMAL, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DECIMAL, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DECIMAL, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.DECIMAL, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DECIMAL, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DECIMAL, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DECIMAL, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface DOUBLE {
    public interface OFFSET<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DOUBLE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DOUBLE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DOUBLE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DOUBLE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DOUBLE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DOUBLE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DOUBLE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.DOUBLE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DOUBLE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DOUBLE, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DOUBLE, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface ENUM<T extends Enum<?> & EntityEnum> {
    public interface OFFSET<T extends Subject<?>> extends kind.ENUM, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.ENUM, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.ENUM, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.ENUM, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.ENUM, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.ENUM, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.ENUM, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.ENUM, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.ENUM, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.ENUM, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.ENUM, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.ENUM, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.ENUM, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.ENUM, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.ENUM, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.ENUM, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface FLOAT {
    public interface OFFSET<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.FLOAT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.FLOAT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.FLOAT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.FLOAT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.FLOAT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.FLOAT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.FLOAT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.FLOAT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.FLOAT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.FLOAT, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.FLOAT, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface INT {
    public interface OFFSET<T extends Subject<?>> extends kind.INT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.INT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.INT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.INT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.INT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.INT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.INT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.INT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.INT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.INT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.INT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.INT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.INT, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.INT, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.INT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.INT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface SMALLINT {
    public interface OFFSET<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.SMALLINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.SMALLINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.SMALLINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.SMALLINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.SMALLINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.SMALLINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.SMALLINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.SMALLINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.SMALLINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.SMALLINT, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.SMALLINT, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface TIME {
    public interface OFFSET<T extends Subject<?>> extends kind.TIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.TIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.TIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.TIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.TIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.TIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.TIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.TIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.TIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.TIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.TIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.TIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.TIME, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.TIME, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.TIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.TIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }

  public interface TINYINT {
    public interface OFFSET<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.TINYINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.TINYINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... column);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.TINYINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.TINYINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.TINYINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.TINYINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.TINYINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final org.libx4j.rdb.jsql.Entity table);
      @Override
      public JOIN<T> JOIN(final org.libx4j.rdb.jsql.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends kind.TINYINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.TINYINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final org.libx4j.rdb.jsql.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.TINYINT, untyped._UNION<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.TINYINT, untyped._UNION.ALL<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
      @Override
      public T AS();
    }
  }
}