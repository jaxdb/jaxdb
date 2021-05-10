/* Copyright (c) 2015 JAX-DB
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

import java.io.Closeable;

public interface Select {
  interface untyped {
    interface _SELECT<T extends type.Entity<?>> extends SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends Executable.Query<T>, _UNION<T> {
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> {
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends SELECT<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> {
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      JOIN<T> LEFT_JOIN(type.Table table);
      JOIN<T> RIGHT_JOIN(type.Table table);
      JOIN<T> FULL_JOIN(type.Table table);
      JOIN<T> JOIN(type.Table table);

      ADV_JOIN<T> CROSS_JOIN(SELECT<?> select);
      ADV_JOIN<T> NATURAL_JOIN(SELECT<?> select);
      JOIN<T> LEFT_JOIN(SELECT<?> table);
      JOIN<T> RIGHT_JOIN(SELECT<?> select);
      JOIN<T> FULL_JOIN(SELECT<?> select);
      JOIN<T> JOIN(SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> {
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends SELECT<T>, _WHERE<T>, _GROUP_BY<T>, _JOIN<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> {
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> {
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> {
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends Executable.Query<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> {
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> {
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends SELECT<T>, _FOR<T> {
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> {
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends SELECT<T> {
      NOWAIT<T> NOWAIT();
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends SELECT<T> {
    }
  }

  interface ARRAY {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.ARRAY<Object> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.ARRAY<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.ARRAY<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.ARRAY<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.ARRAY<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.ARRAY<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.ARRAY<Object> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.ARRAY<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.ARRAY<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface BIGINT {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.BIGINT {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.BIGINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.BIGINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.BIGINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.BIGINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.BIGINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.BIGINT {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.BIGINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.BIGINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface BINARY {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.BINARY {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.BINARY {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.BINARY {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.BINARY {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.BINARY {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.BINARY {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.BINARY {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.BINARY {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.BINARY {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface BLOB {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.BLOB {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.BLOB {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.BLOB {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.BLOB {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.BLOB {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.BLOB {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.BLOB {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.BLOB {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.BLOB {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface BOOLEAN {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.BOOLEAN {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.BOOLEAN {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.BOOLEAN {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.BOOLEAN {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.BOOLEAN {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.BOOLEAN {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.BOOLEAN {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.BOOLEAN {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.BOOLEAN {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface CHAR {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.CHAR {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.CHAR {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.CHAR {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.CHAR {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.CHAR {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.CHAR {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.CHAR {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.CHAR {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.CHAR {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface CLOB {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.CLOB {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.CLOB {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.CLOB {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.CLOB {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.CLOB {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.CLOB {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.CLOB {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.CLOB {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.CLOB {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface DataType {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.Table<Object> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.Table<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.Table<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.Table<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.Table<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.Table<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.Table<Object> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.Table<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.Table<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface DATE {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.DATE {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.DATE {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.DATE {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.DATE {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.DATE {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.DATE {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.DATE {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.DATE {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.DATE {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface DATETIME {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.DATETIME {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.DATETIME {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.DATETIME {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.DATETIME {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.DATETIME {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.DATETIME {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.DATETIME {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.DATETIME {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.DATETIME {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface DECIMAL {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.DECIMAL {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.DECIMAL {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.DECIMAL {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.DECIMAL {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.DECIMAL {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.DECIMAL {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.DECIMAL {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.DECIMAL {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.DECIMAL {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface DOUBLE {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.DOUBLE {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.DOUBLE {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.DOUBLE {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.DOUBLE {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.DOUBLE {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.DOUBLE {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.DOUBLE {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.DOUBLE {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.DOUBLE {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface Entity {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.Table<Object> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.Table<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.Table<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.Table<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.Table<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.Table<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.Table<Object> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.Table<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.Table<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface ENUM<T extends Enum<?> & EntityEnum> {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.ENUM<Enum<?>> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.ENUM<Enum<?>> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.ENUM<Enum<?>> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.ENUM<Enum<?>> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.ENUM<Enum<?>> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.ENUM<Enum<?>> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.ENUM<Enum<?>> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.ENUM<Enum<?>> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.ENUM<Enum<?>> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface FLOAT {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.FLOAT {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.FLOAT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.FLOAT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.FLOAT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.FLOAT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.FLOAT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.FLOAT {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.FLOAT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.FLOAT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface INT {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.INT {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.INT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.INT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.INT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.INT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.INT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.INT {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.INT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.INT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface LargeObject {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.LargeObject<Closeable> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.LargeObject<Closeable> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.LargeObject<Closeable> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.LargeObject<Closeable> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.LargeObject<Closeable> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.LargeObject<Closeable> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.LargeObject<Closeable> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.LargeObject<Closeable> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.LargeObject<Closeable> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface Numeric {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.Numeric<Number> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.Numeric<Number> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.Numeric<Number> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.Numeric<Number> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.Numeric<Number> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.Numeric<Number> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.Numeric<Number> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.Numeric<Number> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.Numeric<Number> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface SMALLINT {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.SMALLINT {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.SMALLINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.SMALLINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.SMALLINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.SMALLINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.SMALLINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.SMALLINT {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.SMALLINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.SMALLINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface Temporal {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.Temporal<java.time.temporal.Temporal> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface Textual<T> {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.Textual<Comparable<?>> {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.Textual<Comparable<?>> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.Textual<Comparable<?>> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.Textual<Comparable<?>> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.Textual<Comparable<?>> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.Textual<Comparable<?>> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.Textual<Comparable<?>> {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.Textual<Comparable<?>> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.Textual<Comparable<?>> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface TIME {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.TIME {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.TIME {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.TIME {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.TIME {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.TIME {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.TIME {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.TIME {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.TIME {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.TIME {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }

  interface TINYINT {
    interface _SELECT<T extends type.Entity<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Entity<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Entity<?>> extends untyped._FROM<T>, kind.TINYINT {
      @Override
      FROM<T> FROM(type.Table ... tables);
    }

    interface FROM<T extends type.Entity<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Entity<?>> extends untyped._JOIN<T>, kind.TINYINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Table table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Table table);
      @Override
      JOIN<T> LEFT_JOIN(type.Table table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Table table);
      @Override
      JOIN<T> FULL_JOIN(type.Table table);
      @Override
      JOIN<T> JOIN(type.Table table);

      @Override
      ADV_JOIN<T> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<T> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<T> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Entity<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Entity<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Entity<?>> extends untyped._ON<T>, kind.TINYINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Entity<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Entity<?>> extends untyped._WHERE<T>, kind.TINYINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Entity<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Entity<?>> extends untyped._GROUP_BY<T>, kind.TINYINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Entity<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Entity<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Entity<?>> extends untyped._HAVING<T>, kind.TINYINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Entity<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Entity<?>> extends kind.TINYINT {
      UNION<T> UNION(SELECT<T> union);
      UNION<T> UNION_ALL(SELECT<T> union);
    }

    interface UNION<T extends type.Entity<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Entity<?>> extends untyped._ORDER_BY<T>, kind.TINYINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Entity<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Entity<?>> extends untyped._LIMIT<T>, kind.TINYINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Entity<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Entity<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Entity<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity<?> ... subjects);
      @Override
      FOR<T> FOR_UPDATE(type.Entity<?> ... subjects);
    }

    interface FOR<T extends type.Entity<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Entity<?>> extends untyped.NOWAIT<T>, SELECT<T> {
    }

    interface SKIP_LOCKED<T extends type.Entity<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
    }
  }
}