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

import java.io.Closeable;

public interface Select {
  public interface ARRAY {
    public interface OFFSET<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.ARRAY<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.ARRAY<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BIGINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.BIGINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.BIGINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.BIGINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.BIGINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.BIGINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.BIGINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.BIGINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.BIGINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.BIGINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.BIGINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.BIGINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.BIGINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.BIGINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BINARY {
    public interface OFFSET<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.BINARY, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.BINARY, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.BINARY, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.BINARY, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.BINARY, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.BINARY, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.BINARY, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.BINARY, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.BINARY, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.BINARY, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.BINARY, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.BINARY, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BLOB {
    public interface OFFSET<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.BLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.BLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.BLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.BLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.BLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.BLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.BLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.BLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.BLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.BLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.BLOB, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.BLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BOOLEAN {
    public interface OFFSET<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.BOOLEAN, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.BOOLEAN, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.BOOLEAN, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.BOOLEAN, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.BOOLEAN, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.BOOLEAN, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.BOOLEAN, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.BOOLEAN, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.BOOLEAN, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.BOOLEAN, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.BOOLEAN, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.BOOLEAN, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CHAR {
    public interface OFFSET<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.CHAR, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.CHAR, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.CHAR, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.CHAR, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.CHAR, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.CHAR, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.CHAR, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.CHAR, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.CHAR, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.CHAR, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.CHAR, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.CHAR, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CLOB {
    public interface OFFSET<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.CLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.CLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.CLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.CLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.CLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.CLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.CLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.CLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.CLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.CLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.CLOB, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.CLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DataType {
    public interface OFFSET<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.Entity<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATE {
    public interface OFFSET<T extends data.Subject<?>> extends type.DATE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.DATE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.DATE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.DATE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.DATE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.DATE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.DATE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.DATE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.DATE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.DATE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.DATE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.DATE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.DATE, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.DATE, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.DATE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.DATE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATETIME {
    public interface OFFSET<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.DATETIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.DATETIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.DATETIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.DATETIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.DATETIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.DATETIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.DATETIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.DATETIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.DATETIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.DATETIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.DATETIME, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.DATETIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DECIMAL {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.DECIMAL.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.DECIMAL, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.DECIMAL, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.DECIMAL, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.DECIMAL, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.DECIMAL, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.DECIMAL, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.DECIMAL, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.DECIMAL, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.DECIMAL, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.DECIMAL, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.DECIMAL, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.DECIMAL, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DOUBLE {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.DOUBLE.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.DOUBLE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.DOUBLE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.DOUBLE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.DOUBLE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.DOUBLE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.DOUBLE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.DOUBLE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.DOUBLE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.DOUBLE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.DOUBLE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.DOUBLE, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.DOUBLE, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Entity {
    public interface OFFSET<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.Entity<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface ENUM<T extends Enum<?> & EntityEnum> {
    public interface OFFSET<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface FLOAT {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.FLOAT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.FLOAT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.FLOAT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.FLOAT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.FLOAT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.FLOAT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.FLOAT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.FLOAT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.FLOAT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.FLOAT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.FLOAT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.FLOAT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.FLOAT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface INT {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.INT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.INT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.INT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.INT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.INT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.INT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.INT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.INT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.INT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.INT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.INT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.INT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.INT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.INT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.INT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.INT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.INT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.INT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.INT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.INT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.INT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.INT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.INT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.INT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.INT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.INT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.INT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface LargeObject {
    public interface OFFSET<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.LargeObject<Closeable>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.LargeObject<Closeable>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.LargeObject<Closeable>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.LargeObject<Closeable>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Numeric {
    public interface OFFSET<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.Numeric<Number>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.Numeric<Number>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.Numeric<Number>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface SMALLINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.SMALLINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.SMALLINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.SMALLINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.SMALLINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.SMALLINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.SMALLINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.SMALLINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.SMALLINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.SMALLINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.SMALLINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.SMALLINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.SMALLINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.SMALLINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Temporal {
    public interface OFFSET<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Textual<T> {
    public interface OFFSET<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.Textual<Comparable<?>>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TIME {
    public interface OFFSET<T extends data.Subject<?>> extends type.TIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.TIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.TIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.TIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.TIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.TIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.TIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.TIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.TIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.TIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.TIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.TIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.TIME, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.TIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.TIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.TIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TINYINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final data.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final data.Entity table);
        @Override
        public JOIN<T> JOIN(final data.Entity table);
      }

      public interface JOIN<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final data.Entity ... tables);
      }

      public interface FROM<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends data.Subject<?>> extends type.TINYINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> extends type.TINYINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> extends type.TINYINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> extends type.TINYINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> extends type.TINYINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> extends type.TINYINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends data.Subject<?>> extends type.TINYINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> extends type.TINYINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final data.Entity table);
      @Override
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends type.TINYINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> extends type.TINYINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends type.TINYINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends data.Subject<?>> extends type.TINYINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends data.Subject<?>> extends type.TINYINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends data.Subject<?>> extends type.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends type.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface untyped {
    public interface OFFSET<T extends data.Subject<?>> extends SELECT<T> {
    }

    public interface _LIMIT<T extends data.Subject<?>> {
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends data.Subject<?>> extends SELECT<T> {
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends data.Subject<?>> {
      public ORDER_BY<T> ORDER_BY(final data.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends data.Subject<?>> extends SELECT<T>, _LIMIT<T> {
    }

    public interface _GROUP_BY<T extends data.Subject<?>> {
      public GROUP_BY<T> GROUP_BY(final data.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends data.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
    }

    public interface _HAVING<T extends data.Subject<?>> {
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends data.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _WHERE<T extends data.Subject<?>> {
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends data.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _ON<T extends data.Subject<?>> {
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends data.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _JOIN<T extends data.Subject<?>> {
      public ADV_JOIN<T> CROSS_JOIN(final data.Entity table);
      public ADV_JOIN<T> NATURAL_JOIN(final data.Entity table);
      public JOIN<T> LEFT_JOIN(final data.Entity table);
      public JOIN<T> RIGHT_JOIN(final data.Entity table);
      public JOIN<T> FULL_JOIN(final data.Entity table);
      public JOIN<T> JOIN(final data.Entity table);
    }

    public interface JOIN<T extends data.Subject<?>> extends _JOIN<T>, _ON<T> {
    }

    public interface ADV_JOIN<T extends data.Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    public interface _FROM<T extends data.Subject<?>> {
      public FROM<T> FROM(final data.Entity ... tables);
    }

    public interface FROM<T extends data.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _SELECT<T extends data.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
    }

    public interface _UNION_TYPE<T extends data.Subject<?>> {
      public interface ALL_TYPE<T extends data.Subject<?>> {
      }

      public ALL_TYPE<T> UNION();
    }

    public interface _UNION<T extends data.Subject<?>> extends _UNION_TYPE<T> {
      public interface ALL<T extends data.Subject<?>> extends ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      public UNION<T> UNION(final SELECT<T> union);
    }

    public interface UNION<T extends data.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
    }

    public interface SELECT<T extends data.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
      public T AS(final T as);
    }
  }
}