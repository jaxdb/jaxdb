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

import java.io.Closeable;

public interface Select {
  interface ARRAY {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface BIGINT {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.BIGINT, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.BIGINT, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.BIGINT, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.BIGINT, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.BIGINT, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.BIGINT, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.BIGINT, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.BIGINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface BINARY {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.BINARY, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.BINARY, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.BINARY, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.BINARY, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.BINARY, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.BINARY, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.BINARY, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.BINARY, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.BINARY, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.BINARY, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface BLOB {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.BLOB, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.BLOB, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.BLOB, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.BLOB, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.BLOB, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.BLOB, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.BLOB, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.BLOB, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.BLOB, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.BLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface BOOLEAN {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface CHAR {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.CHAR, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.CHAR, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.CHAR, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.CHAR, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.CHAR, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.CHAR, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.CHAR, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.CHAR, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.CHAR, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.CHAR, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface CLOB {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.CLOB, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.CLOB, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.CLOB, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.CLOB, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.CLOB, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.CLOB, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.CLOB, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.CLOB, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.CLOB, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.CLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface DataType {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface DATE {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.DATE, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.DATE, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.DATE, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.DATE, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.DATE, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.DATE, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.DATE, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.DATE, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.DATE, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.DATE, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface DATETIME {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.DATETIME, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.DATETIME, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.DATETIME, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.DATETIME, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.DATETIME, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.DATETIME, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.DATETIME, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.DATETIME, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.DATETIME, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.DATETIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface DECIMAL {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.DECIMAL, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.DECIMAL, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.DECIMAL, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.DECIMAL, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.DECIMAL, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.DECIMAL, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.DECIMAL, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.DECIMAL, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface DOUBLE {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.DOUBLE, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.DOUBLE, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.DOUBLE, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.DOUBLE, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.DOUBLE, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.DOUBLE, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.DOUBLE, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.DOUBLE, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface Entity {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface ENUM<T extends Enum<?> & EntityEnum> {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface FLOAT {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.FLOAT, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.FLOAT, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.FLOAT, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.FLOAT, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.FLOAT, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.FLOAT, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.FLOAT, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.FLOAT, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface INT {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.INT, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.INT, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.INT, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.INT, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.INT, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.INT, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.INT, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.INT, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.INT, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.INT, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface LargeObject {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface Numeric {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface SMALLINT {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.SMALLINT, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.SMALLINT, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.SMALLINT, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.SMALLINT, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.SMALLINT, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.SMALLINT, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.SMALLINT, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.SMALLINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface Temporal {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface Textual<T> {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface TIME {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.TIME, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.TIME, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.TIME, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.TIME, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.TIME, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.TIME, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.TIME, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.TIME, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.TIME, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.TIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface TINYINT {
    interface UNSIGNED {
      interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
      }

      interface _LIMIT<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        LIMIT<T> LIMIT(int rows);
      }

      interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
        @Override
        OFFSET<T> OFFSET(int rows);
      }

      interface _ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
      }

      interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      interface _GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
      }

      interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      interface _HAVING<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        HAVING<T> HAVING(Condition<?> condition);
      }

      interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      interface _WHERE<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        WHERE<T> WHERE(Condition<?> condition);
      }

      interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      interface _ON<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._ON<T> {
        @Override
        ON<T> ON(Condition<?> condition);
      }

      interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      interface _JOIN<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._JOIN<T> {
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
      }

      interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      interface _FROM<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._FROM<T> {
        @Override
        FROM<T> FROM(type.Entity ... tables);
      }

      interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      interface _UNION<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._UNION_TYPE<T> {
        UNION<T> UNION(SELECT<T> union);

        interface ALL<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          UNION<T> ALL(SELECT<T> union);
        }

        @Override
        ALL<T> UNION();
      }

      interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
      }

      interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
        @Override
        T AS(T as);
      }
    }

    interface OFFSET<T extends type.Subject<?>> extends SELECT<T>, untyped.OFFSET<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> extends kind.TINYINT, untyped._LIMIT<T> {
      @Override
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T>, untyped.LIMIT<T> {
      @Override
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT, untyped._ORDER_BY<T> {
      @Override
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT, untyped._GROUP_BY<T> {
      @Override
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    interface _HAVING<T extends type.Subject<?>> extends kind.TINYINT, untyped._HAVING<T> {
      @Override
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    interface _WHERE<T extends type.Subject<?>> extends kind.TINYINT, untyped._WHERE<T> {
      @Override
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    interface _ON<T extends type.Subject<?>> extends kind.TINYINT, untyped._ON<T> {
      @Override
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    interface _JOIN<T extends type.Subject<?>> extends kind.TINYINT, untyped._JOIN<T> {
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
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> extends kind.TINYINT, untyped._FROM<T> {
      @Override
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    interface _UNION<T extends type.Subject<?>> extends kind.TINYINT, untyped._UNION_TYPE<T> {
      UNION<T> UNION(SELECT<T> union);

      interface ALL<T extends type.Subject<?>> extends kind.TINYINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        UNION<T> ALL(SELECT<T> union);
      }

      @Override
      ALL<T> UNION();
    }

    interface UNION<T extends type.Subject<?>> extends _UNION<T>, untyped.UNION<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends _UNION<T>, untyped.SELECT<T> {
      @Override
      T AS(T as);
    }
  }

  interface untyped {
    interface OFFSET<T extends type.Subject<?>> extends SELECT<T> {
    }

    interface _LIMIT<T extends type.Subject<?>> {
      LIMIT<T> LIMIT(int rows);
    }

    interface LIMIT<T extends type.Subject<?>> extends SELECT<T> {
      OFFSET<T> OFFSET(int rows);
    }

    interface _ORDER_BY<T extends type.Subject<?>> {
      ORDER_BY<T> ORDER_BY(type.DataType<?> ... columns);
    }

    interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T> {
    }

    interface _GROUP_BY<T extends type.Subject<?>> {
      GROUP_BY<T> GROUP_BY(type.Subject<?> ... subjects);
    }

    interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
    }

    interface _HAVING<T extends type.Subject<?>> {
      HAVING<T> HAVING(Condition<?> condition);
    }

    interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    interface _WHERE<T extends type.Subject<?>> {
      WHERE<T> WHERE(Condition<?> condition);
    }

    interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    interface _ON<T extends type.Subject<?>> {
      ON<T> ON(Condition<?> condition);
    }

    interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    interface _JOIN<T extends type.Subject<?>> {
      ADV_JOIN<T> CROSS_JOIN(type.Entity table);
      ADV_JOIN<T> NATURAL_JOIN(type.Entity table);
      JOIN<T> LEFT_JOIN(type.Entity table);
      JOIN<T> RIGHT_JOIN(type.Entity table);
      JOIN<T> FULL_JOIN(type.Entity table);
      JOIN<T> JOIN(type.Entity table);
    }

    interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T> {
    }

    interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    interface _FROM<T extends type.Subject<?>> {
      FROM<T> FROM(type.Entity ... tables);
    }

    interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
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

    interface UNION<T extends type.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
    }

    interface SELECT<T extends type.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
      T AS(T as);
    }
  }
}