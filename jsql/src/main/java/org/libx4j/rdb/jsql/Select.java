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
    public interface OFFSET<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.ARRAY<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BIGINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.BIGINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.BIGINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.BIGINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.BIGINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.BIGINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.BIGINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.BIGINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.BIGINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.BIGINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.BIGINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.BIGINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.BIGINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BINARY {
    public interface OFFSET<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.BINARY, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.BINARY, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.BINARY, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.BINARY, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.BINARY, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.BINARY, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.BINARY, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.BINARY, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.BINARY, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.BINARY, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.BINARY, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.BINARY, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.BINARY, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BLOB {
    public interface OFFSET<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.BLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.BLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.BLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.BLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.BLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.BLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.BLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.BLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.BLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.BLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.BLOB, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.BLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.BLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BOOLEAN {
    public interface OFFSET<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.BOOLEAN, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.BOOLEAN, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.BOOLEAN, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.BOOLEAN, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CHAR {
    public interface OFFSET<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.CHAR, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.CHAR, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.CHAR, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.CHAR, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.CHAR, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.CHAR, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.CHAR, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.CHAR, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.CHAR, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.CHAR, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.CHAR, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.CHAR, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.CHAR, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CLOB {
    public interface OFFSET<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.CLOB, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.CLOB, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.CLOB, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.CLOB, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.CLOB, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.CLOB, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.CLOB, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.CLOB, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.CLOB, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.CLOB, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.CLOB, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.CLOB, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.CLOB, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DataType {
    public interface OFFSET<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATE {
    public interface OFFSET<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.DATE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DATE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DATE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.DATE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.DATE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.DATE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.DATE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.DATE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.DATE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.DATE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.DATE, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.DATE, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.DATE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.DATE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATETIME {
    public interface OFFSET<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.DATETIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DATETIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DATETIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.DATETIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.DATETIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.DATETIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.DATETIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.DATETIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.DATETIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.DATETIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.DATETIME, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.DATETIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.DATETIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DECIMAL {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.DECIMAL.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.DECIMAL, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.DECIMAL, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.DECIMAL, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.DECIMAL, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.DECIMAL, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.DECIMAL, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.DECIMAL, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.DECIMAL, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.DECIMAL, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.DECIMAL, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.DECIMAL, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DOUBLE {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.DOUBLE.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.DOUBLE, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.DOUBLE, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.DOUBLE, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.DOUBLE, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.DOUBLE, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.DOUBLE, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.DOUBLE, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.DOUBLE, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.DOUBLE, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.DOUBLE, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.DOUBLE, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Entity {
    public interface OFFSET<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface ENUM<T extends Enum<?> & EntityEnum> {
    public interface OFFSET<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface FLOAT {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.FLOAT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.FLOAT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.FLOAT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.FLOAT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.FLOAT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.FLOAT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.FLOAT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.FLOAT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.FLOAT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.FLOAT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.FLOAT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.FLOAT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface INT {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.INT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.INT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.INT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.INT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.INT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.INT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.INT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.INT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.INT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.INT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.INT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.INT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.INT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.INT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.INT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.INT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.INT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.INT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.INT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.INT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.INT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface LargeObject {
    public interface OFFSET<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.LargeObject<Closeable>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Numeric {
    public interface OFFSET<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.Numeric<Number>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface SMALLINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.SMALLINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.SMALLINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.SMALLINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.SMALLINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.SMALLINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.SMALLINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.SMALLINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.SMALLINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.SMALLINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.SMALLINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.SMALLINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.SMALLINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Temporal {
    public interface OFFSET<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Textual<T> {
    public interface OFFSET<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.Textual<Comparable<?>>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TIME {
    public interface OFFSET<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.TIME, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.TIME, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.TIME, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.TIME, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.TIME, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.TIME, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.TIME, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.TIME, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.TIME, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.TIME, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.TIME, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.TIME, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.TIME, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.TIME, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TINYINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._JOIN<T> {
        @Override
        public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
        @Override
        public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> LEFT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> RIGHT_JOIN(final type.Entity table);
        @Override
        public JOIN<T> FULL_JOIN(final type.Entity table);
        @Override
        public JOIN<T> JOIN(final type.Entity table);
      }

      public interface JOIN<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends type.Subject<?>> extends kind.TINYINT.UNSIGNED, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> extends kind.TINYINT, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> extends kind.TINYINT, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> extends kind.TINYINT, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends type.Subject<?>> extends kind.TINYINT, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> extends kind.TINYINT, untyped._JOIN<T> {
      @Override
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      @Override
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      @Override
      public JOIN<T> FULL_JOIN(final type.Entity table);
      @Override
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends kind.TINYINT, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> extends kind.TINYINT, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends kind.TINYINT, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends type.Subject<?>> extends kind.TINYINT, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends type.Subject<?>> extends kind.TINYINT, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends type.Subject<?>> extends kind.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends kind.TINYINT, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface untyped {
    public interface OFFSET<T extends type.Subject<?>> extends SELECT<T> {
    }

    public interface _LIMIT<T extends type.Subject<?>> {
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends type.Subject<?>> extends SELECT<T> {
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends type.Subject<?>> {
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T> {
    }

    public interface _GROUP_BY<T extends type.Subject<?>> {
      public GROUP_BY<T> GROUP_BY(final type.Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _HAVING<T> {
    }

    public interface _HAVING<T extends type.Subject<?>> {
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _WHERE<T extends type.Subject<?>> {
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T> {
    }

    public interface _ON<T extends type.Subject<?>> {
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _JOIN<T extends type.Subject<?>> {
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      public JOIN<T> FULL_JOIN(final type.Entity table);
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends type.Subject<?>> extends _JOIN<T>, _ON<T> {
    }

    public interface ADV_JOIN<T extends type.Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    public interface _FROM<T extends type.Subject<?>> {
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends type.Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _SELECT<T extends type.Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
    }

    public interface _UNION_TYPE<T extends type.Subject<?>> {
      public interface ALL_TYPE<T extends type.Subject<?>> {
      }

      public ALL_TYPE<T> UNION();
    }

    public interface _UNION<T extends type.Subject<?>> extends _UNION_TYPE<T> {
      public interface ALL<T extends type.Subject<?>> extends ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      public UNION<T> UNION(final SELECT<T> union);
    }

    public interface UNION<T extends type.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
    }

    public interface SELECT<T extends type.Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
      public T AS(final T as);
    }
  }
}