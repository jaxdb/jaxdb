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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface Select {
  public interface ARRAY {
    public interface OFFSET<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.ARRAY<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.ARRAY<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.ARRAY<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.ARRAY<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BIGINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.BIGINT.UNSIGNED<BigInteger>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.BIGINT<Long>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BIGINT<Long>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BIGINT<Long>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BIGINT<Long>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BIGINT<Long>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BINARY {
    public interface OFFSET<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.BINARY<byte[]>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BINARY<byte[]>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BINARY<byte[]>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BINARY<byte[]>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BINARY<byte[]>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BLOB {
    public interface OFFSET<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.BLOB<InputStream>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BLOB<InputStream>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BLOB<InputStream>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BLOB<InputStream>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BLOB<InputStream>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface BOOLEAN {
    public interface OFFSET<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.BOOLEAN<Boolean>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CHAR {
    public interface OFFSET<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.CHAR<String>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.CHAR<String>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.CHAR<String>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.CHAR<String>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.CHAR<String>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.CHAR<String>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.CHAR<String>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.CHAR<String>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.CHAR<String>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.CHAR<String>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.CHAR<String>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.CHAR<String>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.CHAR<String>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.CHAR<String>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface CLOB {
    public interface OFFSET<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.CLOB<Reader>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.CLOB<Reader>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.CLOB<Reader>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.CLOB<Reader>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.CLOB<Reader>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DataType {
    public interface OFFSET<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATE {
    public interface OFFSET<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.DATE<LocalDate>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DATE<LocalDate>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DATE<LocalDate>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DATE<LocalDate>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DATE<LocalDate>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DATETIME {
    public interface OFFSET<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DATETIME<LocalDateTime>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DECIMAL {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.DECIMAL.UNSIGNED<BigDecimal>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DECIMAL<BigDecimal>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface DOUBLE {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.DOUBLE.UNSIGNED<Double>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.DOUBLE<Double>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.DOUBLE<Double>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.DOUBLE<Double>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.DOUBLE<Double>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.DOUBLE<Double>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Entity {
    public interface OFFSET<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Entity<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Entity<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Entity<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Entity<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Entity<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Entity<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Entity<Object>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.Entity<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Entity<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Entity<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Entity<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Entity<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface ENUM<T extends Enum<?> & EntityEnum> {
    public interface OFFSET<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.ENUM<Enum<?>>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.ENUM<Enum<?>>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.ENUM<Enum<?>>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.ENUM<Enum<?>>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface FLOAT {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.FLOAT.UNSIGNED<Float>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.FLOAT<Float>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.FLOAT<Float>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.FLOAT<Float>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.FLOAT<Float>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.FLOAT<Float>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface INT {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.INT.UNSIGNED<Long>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.INT<Integer>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.INT<Integer>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.INT<Integer>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.INT<Integer>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.INT<Integer>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.INT<Integer>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.INT<Integer>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.INT<Integer>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.INT<Integer>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.INT<Integer>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.INT<Integer>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.INT<Integer>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.INT<Integer>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.INT<Integer>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface LargeObject {
    public interface OFFSET<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.LargeObject<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.LargeObject<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.LargeObject<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.LargeObject<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.LargeObject<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Numeric {
    public interface OFFSET<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Numeric<Number>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Numeric<Number>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Numeric<Number>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Numeric<Number>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Numeric<Number>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Numeric<Number>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Numeric<Number>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.Numeric<Number>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Numeric<Number>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Numeric<Number>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Numeric<Number>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Numeric<Number>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface SMALLINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.SMALLINT.UNSIGNED<Integer>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.SMALLINT<Short>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.SMALLINT<Short>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.SMALLINT<Short>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.SMALLINT<Short>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.SMALLINT<Short>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Temporal {
    public interface OFFSET<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Temporal<java.time.temporal.Temporal>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface Textual<T> {
    public interface OFFSET<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.Textual<Object>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.Textual<Object>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.Textual<Object>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.Textual<Object>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.Textual<Object>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.Textual<Object>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.Textual<Object>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.Textual<Object>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.Textual<Object>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.Textual<Object>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.Textual<Object>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.Textual<Object>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.Textual<Object>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.Textual<Object>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TIME {
    public interface OFFSET<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.TIME<LocalTime>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.TIME<LocalTime>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.TIME<LocalTime>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.TIME<LocalTime>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.TIME<LocalTime>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
    }
  }

  public interface TINYINT {
    public interface UNSIGNED {
      public interface OFFSET<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, untyped.OFFSET<T> {
      }

      public interface _LIMIT<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._LIMIT<T> {
        @Override
        public LIMIT<T> LIMIT(final int rows);
      }

      public interface LIMIT<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, untyped.LIMIT<T> {
        @Override
        public OFFSET<T> OFFSET(final int rows);
      }

      public interface _ORDER_BY<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._ORDER_BY<T> {
        @Override
        public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
      }

      public interface ORDER_BY<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
      }

      public interface _GROUP_BY<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._GROUP_BY<T> {
        @Override
        public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
      }

      public interface GROUP_BY<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
      }

      public interface _HAVING<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._HAVING<T> {
        @Override
        public HAVING<T> HAVING(final Condition<?> condition);
      }

      public interface HAVING<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
      }

      public interface _WHERE<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._WHERE<T> {
        @Override
        public WHERE<T> WHERE(final Condition<?> condition);
      }

      public interface WHERE<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
      }

      public interface _ON<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._ON<T> {
        @Override
        public ON<T> ON(final Condition<?> condition);
      }

      public interface ON<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
      }

      public interface _JOIN<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._JOIN<T> {
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

      public interface JOIN<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
      }

      public interface ADV_JOIN<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
      }

      public interface _FROM<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._FROM<T> {
        @Override
        public FROM<T> FROM(final type.Entity ... tables);
      }

      public interface FROM<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
      }

      public interface _SELECT<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
      }

      public interface _UNION<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._UNION_TYPE<T> {
        public UNION<T> UNION(final SELECT<T> union);

        public interface ALL<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, untyped._UNION_TYPE.ALL_TYPE<T> {
          public UNION<T> ALL(final SELECT<T> union);
        }

        @Override
        public ALL<T> UNION();
      }

      public interface UNION<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
      }

      public interface SELECT<T extends Subject<?>> extends kind.TINYINT.UNSIGNED<Short>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
        @Override
        public T AS(final T as);
      }
    }

    public interface OFFSET<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, untyped.OFFSET<T> {
    }

    public interface _LIMIT<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._LIMIT<T> {
      @Override
      public LIMIT<T> LIMIT(final int rows);
    }

    public interface LIMIT<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, untyped.LIMIT<T> {
      @Override
      public OFFSET<T> OFFSET(final int rows);
    }

    public interface _ORDER_BY<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._ORDER_BY<T> {
      @Override
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
    }

    public interface ORDER_BY<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _LIMIT<T>, untyped.ORDER_BY<T> {
    }

    public interface _GROUP_BY<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._GROUP_BY<T> {
      @Override
      public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects);
    }

    public interface GROUP_BY<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _LIMIT<T>, _HAVING<T>, untyped.GROUP_BY<T> {
    }

    public interface _HAVING<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._HAVING<T> {
      @Override
      public HAVING<T> HAVING(final Condition<?> condition);
    }

    public interface HAVING<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.HAVING<T> {
    }

    public interface _WHERE<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._WHERE<T> {
      @Override
      public WHERE<T> WHERE(final Condition<?> condition);
    }

    public interface WHERE<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _GROUP_BY<T>, _LIMIT<T>, _ORDER_BY<T>, untyped.WHERE<T> {
    }

    public interface _ON<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._ON<T> {
      @Override
      public ON<T> ON(final Condition<?> condition);
    }

    public interface ON<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _GROUP_BY<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.ON<T> {
    }

    public interface _JOIN<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._JOIN<T> {
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

    public interface JOIN<T extends Subject<?>> extends kind.TINYINT<Byte>, _JOIN<T>, _ON<T>, untyped.JOIN<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _JOIN<T>, untyped.ADV_JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._FROM<T> {
      @Override
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T>, untyped.FROM<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends kind.TINYINT<Byte>, SELECT<T>, _LIMIT<T>, _FROM<T>, untyped._SELECT<T> {
    }

    public interface _UNION<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._UNION_TYPE<T> {
      public UNION<T> UNION(final SELECT<T> union);

      public interface ALL<T extends Subject<?>> extends kind.TINYINT<Byte>, untyped._UNION_TYPE.ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      @Override
      public ALL<T> UNION();
    }

    public interface UNION<T extends Subject<?>> extends kind.TINYINT<Byte>, ExecuteQuery<T>, _UNION<T>, untyped.UNION<T> {
    }

    public interface SELECT<T extends Subject<?>> extends kind.TINYINT<Byte>, ExecuteQuery<T>, _UNION<T>, untyped.SELECT<T> {
      @Override
      public T AS(final T as);
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
      public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns);
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
      public ADV_JOIN<T> CROSS_JOIN(final type.Entity table);
      public ADV_JOIN<T> NATURAL_JOIN(final type.Entity table);
      public JOIN<T> LEFT_JOIN(final type.Entity table);
      public JOIN<T> RIGHT_JOIN(final type.Entity table);
      public JOIN<T> FULL_JOIN(final type.Entity table);
      public JOIN<T> JOIN(final type.Entity table);
    }

    public interface JOIN<T extends Subject<?>> extends _JOIN<T>, _ON<T> {
    }

    public interface ADV_JOIN<T extends Subject<?>> extends SELECT<T>, _JOIN<T> {
    }

    public interface _FROM<T extends Subject<?>> {
      public FROM<T> FROM(final type.Entity ... tables);
    }

    public interface FROM<T extends Subject<?>> extends SELECT<T>, _GROUP_BY<T>, _HAVING<T>, _JOIN<T>, _LIMIT<T>, _ORDER_BY<T>, _WHERE<T> {
    }

    public interface _SELECT<T extends Subject<?>> extends SELECT<T>, _LIMIT<T>, _FROM<T> {
    }

    public interface _UNION_TYPE<T extends Subject<?>> {
      public interface ALL_TYPE<T extends Subject<?>> {
      }

      public ALL_TYPE<T> UNION();
    }

    public interface _UNION<T extends Subject<?>> extends _UNION_TYPE<T> {
      public interface ALL<T extends Subject<?>> extends ALL_TYPE<T> {
        public UNION<T> ALL(final SELECT<T> union);
      }

      public UNION<T> UNION(final SELECT<T> union);
    }

    public interface UNION<T extends Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
    }

    public interface SELECT<T extends Subject<?>> extends ExecuteQuery<T>, _UNION_TYPE<T> {
      public T AS(final T as);
    }
  }
}