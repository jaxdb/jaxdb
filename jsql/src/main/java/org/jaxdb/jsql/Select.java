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
    interface _SELECT<D extends data.Entity<?>> extends SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends Executable.Query<D>, _UNION<D> {
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> {
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends SELECT<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _JOIN<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> {
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      JOIN<D> LEFT_JOIN(data.Table table);
      JOIN<D> RIGHT_JOIN(data.Table table);
      JOIN<D> FULL_JOIN(data.Table table);
      JOIN<D> JOIN(data.Table table);

      ADV_JOIN<D> CROSS_JOIN(SELECT<?> select);
      ADV_JOIN<D> NATURAL_JOIN(SELECT<?> select);
      JOIN<D> LEFT_JOIN(SELECT<?> table);
      JOIN<D> RIGHT_JOIN(SELECT<?> select);
      JOIN<D> FULL_JOIN(SELECT<?> select);
      JOIN<D> JOIN(SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> {
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends SELECT<D>, _WHERE<D>, _GROUP_BY<D>, _JOIN<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> {
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> {
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> {
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends Executable.Query<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> {
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> {
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends SELECT<D>, _FOR<D> {
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> {
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends SELECT<D> {
      NOWAIT<D> NOWAIT();
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends SELECT<D> {
    }
  }

  interface ARRAY {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.ARRAY<Object> {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.ARRAY<Object> {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.ARRAY<Object> {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.ARRAY<Object> {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.ARRAY<Object> {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.ARRAY<Object> {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.ARRAY<Object> {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.ARRAY<Object> {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.ARRAY<Object> {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface BIGINT {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.BIGINT {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.BIGINT {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.BIGINT {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.BIGINT {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.BIGINT {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.BIGINT {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.BIGINT {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.BIGINT {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.BIGINT {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface BINARY {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.BINARY {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.BINARY {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.BINARY {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.BINARY {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.BINARY {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.BINARY {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.BINARY {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.BINARY {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.BINARY {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface BLOB {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.BLOB {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.BLOB {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.BLOB {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.BLOB {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.BLOB {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.BLOB {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.BLOB {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.BLOB {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.BLOB {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface BOOLEAN {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.BOOLEAN {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.BOOLEAN {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.BOOLEAN {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.BOOLEAN {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.BOOLEAN {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.BOOLEAN {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.BOOLEAN {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.BOOLEAN {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.BOOLEAN {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface CHAR {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.CHAR {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.CHAR {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.CHAR {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.CHAR {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.CHAR {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.CHAR {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.CHAR {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.CHAR {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.CHAR {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface CLOB {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.CLOB {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.CLOB {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.CLOB {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.CLOB {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.CLOB {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.CLOB {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.CLOB {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.CLOB {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.CLOB {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface Column {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.Table {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.Table {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.Table {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.Table {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.Table {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.Table {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.Table {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.Table {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.Table {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface DATE {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.DATE {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.DATE {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.DATE {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.DATE {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.DATE {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.DATE {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.DATE {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.DATE {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.DATE {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface DATETIME {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.DATETIME {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.DATETIME {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.DATETIME {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.DATETIME {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.DATETIME {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.DATETIME {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.DATETIME {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.DATETIME {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.DATETIME {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface DECIMAL {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.DECIMAL {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.DECIMAL {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.DECIMAL {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.DECIMAL {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.DECIMAL {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.DECIMAL {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.DECIMAL {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.DECIMAL {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.DECIMAL {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface DOUBLE {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.DOUBLE {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.DOUBLE {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.DOUBLE {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.DOUBLE {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.DOUBLE {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.DOUBLE {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.DOUBLE {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.DOUBLE {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.DOUBLE {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface Entity {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.Table {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.Table {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.Table {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.Table {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.Table {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.Table {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.Table {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.Table {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.Table {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface ENUM<D extends Enum<?> & EntityEnum> {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.ENUM {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.ENUM {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.ENUM {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.ENUM {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.ENUM {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.ENUM {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.ENUM {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.ENUM {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.ENUM {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface FLOAT {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.FLOAT {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.FLOAT {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.FLOAT {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.FLOAT {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.FLOAT {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.FLOAT {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.FLOAT {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.FLOAT {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.FLOAT {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface INT {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.INT {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.INT {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.INT {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.INT {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.INT {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.INT {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.INT {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.INT {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.INT {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface LargeObject {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.LargeObject<Closeable> {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.LargeObject<Closeable> {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.LargeObject<Closeable> {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.LargeObject<Closeable> {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.LargeObject<Closeable> {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.LargeObject<Closeable> {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.LargeObject<Closeable> {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.LargeObject<Closeable> {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.LargeObject<Closeable> {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface Numeric {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.Numeric<Number> {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.Numeric<Number> {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.Numeric<Number> {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.Numeric<Number> {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.Numeric<Number> {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.Numeric<Number> {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.Numeric<Number> {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.Numeric<Number> {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.Numeric<Number> {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface SMALLINT {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.SMALLINT {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.SMALLINT {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.SMALLINT {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.SMALLINT {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.SMALLINT {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.SMALLINT {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.SMALLINT {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.SMALLINT {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.SMALLINT {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface Temporal {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.Temporal<java.time.temporal.Temporal> {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.Temporal<java.time.temporal.Temporal> {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface Textual<D> {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.Textual {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.Textual {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.Textual {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.Textual {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.Textual {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.Textual {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.Textual {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.Textual {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.Textual {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface TIME {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.TIME {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.TIME {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.TIME {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.TIME {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.TIME {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.TIME {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.TIME {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.TIME {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.TIME {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }

  interface TINYINT {
    interface _SELECT<D extends data.Entity<?>> extends untyped._SELECT<D>, SELECT<D>, _FROM<D>, _LIMIT<D>, _FOR<D> {
    }

    interface SELECT<D extends data.Entity<?>> extends untyped.SELECT<D>, _UNION<D> {
      @Override
      D AS(D as);
    }

    interface _FROM<D extends data.Entity<?>> extends untyped._FROM<D>, type.TINYINT {
      @Override
      FROM<D> FROM(data.Table ... tables);
    }

    interface FROM<D extends data.Entity<?>> extends untyped.FROM<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _HAVING<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _JOIN<D extends data.Entity<?>> extends untyped._JOIN<D>, type.TINYINT {
      @Override
      ADV_JOIN<D> CROSS_JOIN(data.Table table);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(data.Table table);
      @Override
      JOIN<D> LEFT_JOIN(data.Table table);
      @Override
      JOIN<D> RIGHT_JOIN(data.Table table);
      @Override
      JOIN<D> FULL_JOIN(data.Table table);
      @Override
      JOIN<D> JOIN(data.Table table);

      @Override
      ADV_JOIN<D> CROSS_JOIN(untyped.SELECT<?> select);
      @Override
      ADV_JOIN<D> NATURAL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> LEFT_JOIN(untyped.SELECT<?> table);
      @Override
      JOIN<D> RIGHT_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> FULL_JOIN(untyped.SELECT<?> select);
      @Override
      JOIN<D> JOIN(untyped.SELECT<?> select);
    }

    interface ADV_JOIN<D extends data.Entity<?>> extends untyped.ADV_JOIN<D>, SELECT<D>, _JOIN<D> {
    }

    interface JOIN<D extends data.Entity<?>> extends untyped.JOIN<D>, _ON<D> {
    }

    interface _ON<D extends data.Entity<?>> extends untyped._ON<D>, type.TINYINT {
      @Override
      ON<D> ON(Condition<?> condition);
    }

    interface ON<D extends data.Entity<?>> extends untyped.ON<D>, SELECT<D>, _JOIN<D>, _WHERE<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _WHERE<D extends data.Entity<?>> extends untyped._WHERE<D>, type.TINYINT {
      @Override
      WHERE<D> WHERE(Condition<?> condition);
    }

    interface WHERE<D extends data.Entity<?>> extends untyped.WHERE<D>, SELECT<D>, _GROUP_BY<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _GROUP_BY<D extends data.Entity<?>> extends untyped._GROUP_BY<D>, type.TINYINT {
      @Override
      GROUP_BY<D> GROUP_BY(type.Entity<?> ... subjects);
    }

    interface GROUP_BY<D extends data.Entity<?>> extends untyped.GROUP_BY<D>, SELECT<D>, _HAVING<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _HAVING<D extends data.Entity<?>> extends untyped._HAVING<D>, type.TINYINT {
      @Override
      HAVING<D> HAVING(Condition<?> condition);
    }

    interface HAVING<D extends data.Entity<?>> extends untyped.HAVING<D>, SELECT<D>, _ORDER_BY<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _UNION<D extends data.Entity<?>> extends type.TINYINT {
      UNION<D> UNION(SELECT<D> union);
      UNION<D> UNION_ALL(SELECT<D> union);
    }

    interface UNION<D extends data.Entity<?>> extends untyped.UNION<D>, _UNION<D> {
    }

    interface _ORDER_BY<D extends data.Entity<?>> extends untyped._ORDER_BY<D>, type.TINYINT {
      @Override
      ORDER_BY<D> ORDER_BY(data.Column<?> ... columns);
    }

    interface ORDER_BY<D extends data.Entity<?>> extends untyped.ORDER_BY<D>, SELECT<D>, _LIMIT<D>, _FOR<D> {
    }

    interface _LIMIT<D extends data.Entity<?>> extends untyped._LIMIT<D>, type.TINYINT {
      @Override
      LIMIT<D> LIMIT(int rows);
    }

    interface LIMIT<D extends data.Entity<?>> extends untyped.LIMIT<D>, SELECT<D>, _FOR<D> {
      @Override
      OFFSET<D> OFFSET(int rows);
    }

    interface OFFSET<D extends data.Entity<?>> extends untyped.OFFSET<D>, SELECT<D> {
    }

    interface _FOR<D extends data.Entity<?>> extends untyped._FOR<D> {
      @Override
      FOR<D> FOR_SHARE(data.Entity<?> ... subjects);
      @Override
      FOR<D> FOR_UPDATE(data.Entity<?> ... subjects);
    }

    interface FOR<D extends data.Entity<?>> extends untyped.FOR<D>, SELECT<D> {
      @Override
      NOWAIT<D> NOWAIT();
      @Override
      SKIP_LOCKED<D> SKIP_LOCKED();
    }

    interface NOWAIT<D extends data.Entity<?>> extends untyped.NOWAIT<D>, SELECT<D> {
    }

    interface SKIP_LOCKED<D extends data.Entity<?>> extends untyped.SKIP_LOCKED<D>, SELECT<D> {
    }
  }
}