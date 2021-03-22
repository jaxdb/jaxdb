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
    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends Executable.Query<T>, _UNION_TYPE<T> {
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> {
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> {
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      JOIN<T> LEFT_JOIN(type.Entity table);
      JOIN<T> RIGHT_JOIN(type.Entity table);
      JOIN<T> FULL_JOIN(type.Entity table);
      JOIN<T> JOIN(type.Entity table);

      ADV_JOIN<T> CROSS_JOIN(SELECT<?> select);
      ADV_JOIN<T> NATURAL_JOIN(SELECT<?> select);
      JOIN<T> LEFT_JOIN(SELECT<?> table);
      JOIN<T> RIGHT_JOIN(SELECT<?> select);
      JOIN<T> FULL_JOIN(SELECT<?> select);
      JOIN<T> JOIN(SELECT<?> select);
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> {
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _WHERE<T>, _GROUP_BY<T>, _JOIN<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> {
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> {
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> {
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION_TYPE<T extends type.Subject<?>> {
      interface ALL_TYPE<T extends type.Subject<?>> {
      }

      ALL_TYPE<T> UNION();
    }

    interface _UNION<T extends type.Subject<?>> extends _UNION_TYPE<T> {
      interface ALL<T extends type.Subject<?>> extends ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      UNION<T> UNION(SELECT<T> union);
    }

    interface UNION<T extends type.Subject<?>> extends Executable.Query<T>, _UNION_TYPE<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> {
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> {
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, _FOR<T> {
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> {
      FOR<T> FOR_SHARE(type.Entity ... tables);
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends SELECT<T> {
      NOWAIT<T> NOWAIT();
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends SELECT<T> {
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends SELECT<T> {
      NOWAIT<T> NOWAIT();
    }
  }

  interface ARRAY {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.ARRAY<Object> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.ARRAY<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.ARRAY<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.ARRAY<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.ARRAY<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.ARRAY<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.ARRAY<Object> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.ARRAY<Object> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.ARRAY<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.ARRAY<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface BIGINT {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.BIGINT.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.BIGINT.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.BIGINT.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.BIGINT.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.BIGINT.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.BIGINT.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.BIGINT.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.BIGINT.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.BIGINT.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.BIGINT.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.BIGINT {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.BIGINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.BIGINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.BIGINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.BIGINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.BIGINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.BIGINT {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.BIGINT {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.BIGINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.BIGINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface BINARY {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.BINARY {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.BINARY {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.BINARY {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.BINARY {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.BINARY {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.BINARY {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.BINARY {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.BINARY {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.BINARY {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.BINARY {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface BLOB {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.BLOB {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.BLOB {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.BLOB {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.BLOB {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.BLOB {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.BLOB {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.BLOB {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.BLOB {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.BLOB {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.BLOB {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface BOOLEAN {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.BOOLEAN {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.BOOLEAN {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.BOOLEAN {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.BOOLEAN {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.BOOLEAN {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.BOOLEAN {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.BOOLEAN {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.BOOLEAN {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.BOOLEAN {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.BOOLEAN {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface CHAR {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.CHAR {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.CHAR {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.CHAR {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.CHAR {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.CHAR {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.CHAR {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.CHAR {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.CHAR {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.CHAR {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.CHAR {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface CLOB {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.CLOB {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.CLOB {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.CLOB {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.CLOB {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.CLOB {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.CLOB {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.CLOB {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.CLOB {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.CLOB {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.CLOB {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface DataType {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.Entity<Object> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.Entity<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.Entity<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.Entity<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.Entity<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.Entity<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.Entity<Object> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.Entity<Object> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.Entity<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.Entity<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface DATE {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DATE {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DATE {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DATE {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DATE {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DATE {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DATE {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DATE {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DATE {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DATE {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DATE {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface DATETIME {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DATETIME {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DATETIME {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DATETIME {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DATETIME {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DATETIME {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DATETIME {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DATETIME {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DATETIME {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DATETIME {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DATETIME {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface DECIMAL {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DECIMAL.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DECIMAL.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DECIMAL.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DECIMAL.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DECIMAL.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DECIMAL.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DECIMAL.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DECIMAL.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DECIMAL.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DECIMAL.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DECIMAL {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DECIMAL {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DECIMAL {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DECIMAL {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DECIMAL {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DECIMAL {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DECIMAL {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DECIMAL {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DECIMAL {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DECIMAL {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface DOUBLE {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DOUBLE.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DOUBLE.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DOUBLE.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DOUBLE.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DOUBLE.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DOUBLE.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DOUBLE.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DOUBLE.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DOUBLE.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DOUBLE.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.DOUBLE {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.DOUBLE {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.DOUBLE {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.DOUBLE {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.DOUBLE {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.DOUBLE {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.DOUBLE {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.DOUBLE {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.DOUBLE {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.DOUBLE {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface Entity {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.Entity<Object> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.Entity<Object> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.Entity<Object> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.Entity<Object> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.Entity<Object> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.Entity<Object> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.Entity<Object> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.Entity<Object> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.Entity<Object> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.Entity<Object> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface ENUM<T extends Enum<?> & EntityEnum> {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.ENUM<Enum<?>> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.ENUM<Enum<?>> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.ENUM<Enum<?>> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.ENUM<Enum<?>> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.ENUM<Enum<?>> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.ENUM<Enum<?>> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.ENUM<Enum<?>> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.ENUM<Enum<?>> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.ENUM<Enum<?>> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.ENUM<Enum<?>> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface FLOAT {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.FLOAT.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.FLOAT.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.FLOAT.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.FLOAT.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.FLOAT.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.FLOAT.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.FLOAT.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.FLOAT.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.FLOAT.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.FLOAT.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.FLOAT {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.FLOAT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.FLOAT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.FLOAT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.FLOAT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.FLOAT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.FLOAT {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.FLOAT {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.FLOAT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.FLOAT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface INT {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.INT.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.INT.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.INT.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.INT.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.INT.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.INT.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.INT.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.INT.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.INT.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.INT.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.INT {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.INT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.INT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.INT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.INT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.INT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.INT {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.INT {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.INT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.INT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface LargeObject {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.LargeObject<Closeable> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.LargeObject<Closeable> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.LargeObject<Closeable> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.LargeObject<Closeable> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.LargeObject<Closeable> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.LargeObject<Closeable> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.LargeObject<Closeable> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.LargeObject<Closeable> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.LargeObject<Closeable> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.LargeObject<Closeable> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface Numeric {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.Numeric<Number> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.Numeric<Number> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.Numeric<Number> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.Numeric<Number> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.Numeric<Number> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.Numeric<Number> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.Numeric<Number> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.Numeric<Number> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.Numeric<Number> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.Numeric<Number> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface SMALLINT {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.SMALLINT.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.SMALLINT.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.SMALLINT.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.SMALLINT.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.SMALLINT.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.SMALLINT.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.SMALLINT.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.SMALLINT.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.SMALLINT.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.SMALLINT.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.SMALLINT {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.SMALLINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.SMALLINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.SMALLINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.SMALLINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.SMALLINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.SMALLINT {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.SMALLINT {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.SMALLINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.SMALLINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface Temporal {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.Temporal<java.time.temporal.Temporal> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.Temporal<java.time.temporal.Temporal> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.Temporal<java.time.temporal.Temporal> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface Textual<T> {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.Textual<Comparable<?>> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.Textual<Comparable<?>> {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.Textual<Comparable<?>> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.Textual<Comparable<?>> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.Textual<Comparable<?>> {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.Textual<Comparable<?>> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.Textual<Comparable<?>> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.Textual<Comparable<?>> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.Textual<Comparable<?>> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.Textual<Comparable<?>> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface TIME {
    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.TIME {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.TIME {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.TIME {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.TIME {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.TIME {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.TIME {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.TIME {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.TIME {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.TIME {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.TIME {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }

  interface TINYINT {
    interface UNSIGNED {
      interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
        @Override
        T AS(T as);
      }

      interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.TINYINT.UNSIGNED {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.TINYINT.UNSIGNED {
        @Override
        ADV_JOIN<T> CROSS_JOIN(type.Entity table);
        @Override
        ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
        @Override
        JOIN<T> LEFT_JOIN(type.Entity table);
        @Override
        JOIN<T> RIGHT_JOIN(type.Entity table);
        @Override
        JOIN<T> FULL_JOIN(type.Entity table);
        @Override
        JOIN<T> JOIN(type.Entity table);

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

      interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
      }

      interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
      }

      interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.TINYINT.UNSIGNED {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.TINYINT.UNSIGNED {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.TINYINT.UNSIGNED {
        @Override
        GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.TINYINT.UNSIGNED {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.TINYINT.UNSIGNED {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.TINYINT.UNSIGNED {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.TINYINT.UNSIGNED {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.TINYINT.UNSIGNED {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
      }

      interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
        @Override
        FOR<T> FOR_SHARE(type.Entity ... tables);
        @Override
        FOR<T> FOR_UPDATE(type.Entity ... tables);
      }

      interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
        @Override
        SKIP_LOCKED<T> SKIP_LOCKED();
      }

      interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
        @Override
        NOWAIT<T> NOWAIT();
      }
    }

    interface _SELECT<T extends type.Subject<?>> extends untyped._SELECT<T>, SELECT<T>, _FROM<T>, _LIMIT<T>, _FOR<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends untyped.SELECT<T>, _UNION<T> {
      @Override
      T AS(T as);
    }

    interface _FROM<T extends type.Subject<?>> extends untyped._FROM<T>, kind.TINYINT {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends untyped.FROM<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _HAVING<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends untyped._JOIN<T>, kind.TINYINT {
      @Override
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      @Override
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      @Override
      JOIN<T> LEFT_JOIN(type.Entity table);
      @Override
      JOIN<T> RIGHT_JOIN(type.Entity table);
      @Override
      JOIN<T> FULL_JOIN(type.Entity table);
      @Override
      JOIN<T> JOIN(type.Entity table);

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

    interface ADV_JOIN<T extends type.Subject<?>> extends untyped.ADV_JOIN<T>, SELECT<T>, _JOIN<T> {
    }

    interface JOIN<T extends type.Subject<?>> extends untyped.JOIN<T>, _ON<T> {
    }

    interface _ON<T extends type.Subject<?>> extends untyped._ON<T>, kind.TINYINT {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends untyped.ON<T>, SELECT<T>, _JOIN<T>, _WHERE<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends untyped._WHERE<T>, kind.TINYINT {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends untyped.WHERE<T>, SELECT<T>, _GROUP_BY<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends untyped._GROUP_BY<T>, kind.TINYINT {
      @Override
      GROUP_BY<T> GROUP_BY(kind.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends untyped.GROUP_BY<T>, SELECT<T>, _HAVING<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends untyped._HAVING<T>, kind.TINYINT {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends untyped.HAVING<T>, SELECT<T>, _ORDER_BY<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends untyped._UNION_TYPE<T>, kind.TINYINT {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends untyped._UNION_TYPE.ALL_TYPE<T>, kind.TINYINT {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends untyped.UNION<T>, _UNION<T> {
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends untyped._ORDER_BY<T>, kind.TINYINT {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends untyped.ORDER_BY<T>, SELECT<T>, _LIMIT<T>, _FOR<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends untyped._LIMIT<T>, kind.TINYINT {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends untyped.LIMIT<T>, SELECT<T>, _FOR<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface OFFSET<T extends type.Subject<?>> extends untyped.OFFSET<T>, SELECT<T> {
    }

    interface _FOR<T extends type.Subject<?>> extends untyped._FOR<T> {
      @Override
      FOR<T> FOR_SHARE(type.Entity ... tables);
      @Override
      FOR<T> FOR_UPDATE(type.Entity ... tables);
    }

    interface FOR<T extends type.Subject<?>> extends untyped.FOR<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface NOWAIT<T extends type.Subject<?>> extends untyped.NOWAIT<T>, SELECT<T> {
      @Override
      SKIP_LOCKED<T> SKIP_LOCKED();
    }

    interface SKIP_LOCKED<T extends type.Subject<?>> extends untyped.SKIP_LOCKED<T>, SELECT<T> {
      @Override
      NOWAIT<T> NOWAIT();
    }
  }
}