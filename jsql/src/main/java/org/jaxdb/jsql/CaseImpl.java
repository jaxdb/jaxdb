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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

final class CaseImpl implements Case {
  private abstract static class ChainedKeyword extends Keyword<type.Subject<?>> {
    final ChainedKeyword root;
    final ChainedKeyword parent;
    ArrayList<Object> whenThen;
    type.DataType<?> _else;

    ChainedKeyword(final ChainedKeyword root, final ChainedKeyword parent) {
      this.root = Objects.requireNonNull(root);
      this.parent = parent;
    }

    ChainedKeyword() {
      this.root = this;
      this.parent = null;
    }

    abstract type.DataType<?> createReturnType();

    final void WHEN(final type.DataType<?> when) {
      if (parent != null) {
        parent.WHEN(when);
      }
      else {
        if (whenThen == null)
          whenThen = new ArrayList<>();

        whenThen.add(when);
      }
    }

    final void THEN(final type.DataType<?> then) {
      if (parent != null)
        parent.THEN(then);
      else
        whenThen.add(then);
    }

    final void ELSE(final type.DataType<?> _else) {
      if (parent != null)
        parent.ELSE(_else);
      else
        this._else = _else;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      final Compiler compiler = compilation.compiler;
      if (whenThen != null)
        for (int i = 0; i < whenThen.size();)
          compiler.compileWhenThenElse((Compilable)whenThen.get(i++), (type.DataType<?>)whenThen.get(i++), _else, compilation);

      if (_else != null)
        compiler.compileElse(_else, compilation);
    }
  }

  private abstract static class CASE_THEN extends ChainedKeyword {
    CASE_THEN(final ChainedKeyword root, final ChainedKeyword parent) {
      super(root, parent);
    }

    CASE_THEN() {
      super();
    }
  }

  abstract static class CASE extends CASE_THEN {
    CASE() {
      super();
    }

    @Override
    final type.DataType<?> createReturnType() {
      return null;
    }
  }

  abstract static class WHEN<T> extends ChainedKeyword {
    WHEN(final ChainedKeyword root, final CASE_THEN parent, final type.DataType<T> when) {
      super(root, parent);
      WHEN(when);
    }

    WHEN(final type.DataType<T> when) {
      super();
      WHEN(when);
    }

    @Override
    final type.DataType<?> createReturnType() {
      return parent == null ? null : ((CASE_THEN)parent).createReturnType();
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compileWhen((Search.WHEN<?>)this, compilation);
      super.compile(compilation, isExpression);
    }
  }

  private abstract static class THEN_ELSE<T extends type.DataType<?>> extends CASE_THEN {
    final T value;

    THEN_ELSE(final ChainedKeyword root, final ChainedKeyword parent, final T value) {
      super(root, parent);
      this.value = value;
    }

    @Override
    final type.DataType<?> createReturnType() {
      final type.DataType<?> dataType = parent.createReturnType();
      return dataType != null ? dataType.scaleTo(value) : value;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      if (root == null)
        System.out.println();
      root.compile(compilation, isExpression);
    }
  }

  abstract static class THEN<T,R extends type.DataType<?>> extends THEN_ELSE<R> {
    THEN(final ChainedKeyword root, final WHEN<?> parent, final R then) {
      super(root, parent, then);
      THEN(then);
    }
  }

  abstract static class ELSE<T extends type.DataType<?>> extends THEN_ELSE<T> {
    ELSE(final ChainedKeyword root, final THEN_ELSE<?> parent, final T _else) {
      super(root, parent, _else);
      ELSE(_else);
    }
  }

  static final class Simple implements simple {
    static final class CASE<T,R extends type.DataType<?>> extends CaseImpl.CASE implements simple.CASE<T> {
      final type.DataType<T> variable;

      CASE(final type.DataType<T> variable) {
        super();
        this.variable = variable;
      }

      @Override
      public final simple.WHEN<T> WHEN(final T when) {
        return new WHEN<T,R>(this, type.DataType.wrap(when));
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException {
        final Compiler compiler = compilation.compiler;
        compiler.compileCaseElse(variable, _else, compilation);
        super.compile(compilation, isExpression);
      }
    }

    static final class WHEN<T,R extends type.DataType<?>> extends CaseImpl.WHEN<T> implements simple.WHEN<T> {
      WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
        super(parent, parent, when);
      }

      @Override
      public final Case.BOOLEAN.simple.THEN THEN(final type.BOOLEAN bool) {
        return new BOOLEAN.Simple.THEN<>(this, bool);
      }

      @Override
      public final Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
        return new FLOAT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
        return new FLOAT.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
        return new DOUBLE.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
        return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
        return new DECIMAL.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
        return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
        return new TINYINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
        return new TINYINT.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
        return new SMALLINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
        return new SMALLINT.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
        return new INT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
        return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
        return new BIGINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
        return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
        return new BOOLEAN.Simple.THEN<>(this, type.DataType.wrap(bool));
      }

      @Override
      public final Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
        return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
        return new FLOAT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
        return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
        return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
        return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
        return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
        return new TINYINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
        return new TINYINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
        return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
        return new SMALLINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.INT.simple.THEN<T> THEN(final int numeric) {
        return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
        return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
        return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
        return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BINARY.simple.THEN<T> THEN(final type.BINARY binary) {
        return new BINARY.Simple.THEN<>(this, binary);
      }

      @Override
      public final Case.DATE.simple.THEN<T> THEN(final type.DATE date) {
        return new DATE.Simple.THEN<>(this, date);
      }

      @Override
      public final Case.TIME.simple.THEN<T> THEN(final type.TIME time) {
        return new TIME.Simple.THEN<>(this, time);
      }

      @Override
      public final Case.DATETIME.simple.THEN<T> THEN(final type.DATETIME dateTime) {
        return new DATETIME.Simple.THEN<>(this, dateTime);
      }

      @Override
      public final Case.CHAR.simple.THEN<T> THEN(final type.CHAR text) {
        return new CHAR.Simple.THEN<>(this, text);
      }

      @Override
      public final Case.ENUM.simple.THEN<T> THEN(final type.ENUM<?> text) {
        return new ENUM.Simple.THEN<>(this, text);
      }

      @Override
      public final Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
        return new BINARY.Simple.THEN<>(this, type.DataType.wrap(binary));
      }

      @Override
      public final Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
        return new DATE.Simple.THEN<>(this, type.DataType.wrap(date));
      }

      @Override
      public final Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
        return new TIME.Simple.THEN<>(this, type.DataType.wrap(time));
      }

      @Override
      public final Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
        return new DATETIME.Simple.THEN<>(this, type.DataType.wrap(dateTime));
      }

      @Override
      public final Case.CHAR.simple.THEN<T> THEN(final String text) {
        return new CHAR.Simple.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
      }

      @Override
      public final Case.ENUM.simple.THEN<T> THEN(final Enum<?> text) {
        return new ENUM.Simple.THEN<>(this, (type.ENUM<?>)type.DataType.wrap(text));
      }
    }

    private Simple() {
    }
  }

  static final class Search implements search {
    static final class WHEN<T> extends CaseImpl.WHEN<T> implements search.WHEN<T> {
      WHEN(final type.DataType<T> when) {
        super(when);
      }

      @Override
      public final Case.BOOLEAN.search.THEN<T> THEN(final type.BOOLEAN bool) {
        return new BOOLEAN.Search.THEN<>(this, bool);
      }

      @Override
      public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
        return new FLOAT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
        return new FLOAT.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
        return new DOUBLE.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
        return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
        return new DECIMAL.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
        return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric) {
        return new TINYINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
        return new TINYINT.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
        return new SMALLINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
        return new SMALLINT.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.search.THEN<T> THEN(final type.INT numeric) {
        return new INT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
        return new INT.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
        return new BIGINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
        return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
        return new BOOLEAN.Search.THEN<>(this, type.DataType.wrap(bool));
      }

      @Override
      public final Case.FLOAT.search.THEN<T> THEN(final float numeric) {
        return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
        return new FLOAT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
        return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
        return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
        return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
        return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
        return new TINYINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
        return new TINYINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
        return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
        return new SMALLINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.INT.search.THEN<T> THEN(final int numeric) {
        return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
        return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.search.THEN<T> THEN(final long numeric) {
        return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
        return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
      }

      @Override
      public final Case.BINARY.search.THEN<T> THEN(final type.BINARY binary) {
        return new BINARY.Search.THEN<>(this, binary);
      }

      @Override
      public final Case.DATE.search.THEN<T> THEN(final type.DATE date) {
        return new DATE.Search.THEN<>(this, date);
      }

      @Override
      public final Case.TIME.search.THEN<T> THEN(final type.TIME time) {
        return new TIME.Search.THEN<>(this, time);
      }

      @Override
      public final Case.DATETIME.search.THEN<T> THEN(final type.DATETIME dateTime) {
        return new DATETIME.Search.THEN<>(this, dateTime);
      }

      @Override
      public final Case.CHAR.search.THEN<T> THEN(final type.CHAR text) {
        return new CHAR.Search.THEN<>(this, text);
      }

      @Override
      public final Case.ENUM.search.THEN<T> THEN(final type.ENUM<?> text) {
        return new ENUM.Search.THEN<>(this, text);
      }

      @Override
      public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
        return new BINARY.Search.THEN<>(this, type.DataType.wrap(binary));
      }

      @Override
      public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
        return new DATE.Search.THEN<>(this, type.DataType.wrap(date));
      }

      @Override
      public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
        return new TIME.Search.THEN<>(this, type.DataType.wrap(time));
      }

      @Override
      public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
        return new DATETIME.Search.THEN<>(this, type.DataType.wrap(dateTime));
      }

      @Override
      public final Case.CHAR.search.THEN<T> THEN(final String text) {
        return new CHAR.Search.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
      }

      @Override
      public final Case.ENUM.search.THEN<T> THEN(final Enum<?> text) {
        return new ENUM.Search.THEN<>(this, (type.ENUM<?>)type.DataType.wrap(text));
      }
    }

    private Search() {
    }
  }

  static final class BOOLEAN {
    static final class Simple implements Case.BOOLEAN.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.simple.WHEN {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final type.BOOLEAN bool) {
          return new BOOLEAN.Simple.THEN<>(this, bool);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
          return new BOOLEAN.Simple.THEN<>(this, type.DataType.wrap(bool));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.BOOLEAN> implements Case.BOOLEAN.simple.THEN {
        THEN(final CaseImpl.WHEN<T> parent, final type.BOOLEAN value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final type.BOOLEAN bool) {
          return new BOOLEAN.ELSE(this, bool);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
          return new BOOLEAN.ELSE(this, type.DataType.wrap(bool));
        }

        @Override
        @SuppressWarnings("unchecked")
        public final Case.BOOLEAN.simple.WHEN WHEN(final boolean condition) {
          return new BOOLEAN.Simple.WHEN<>(this, (type.DataType<T>)type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BOOLEAN.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.search.CASE<T>, Case.BOOLEAN.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final type.BOOLEAN bool) {
          return new BOOLEAN.Search.THEN<>(this, bool);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
          return new BOOLEAN.Search.THEN<>(this, type.DataType.wrap(bool));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.BOOLEAN> implements Case.BOOLEAN.search.THEN<T> {
        THEN(final CaseImpl.WHEN<?> parent, final type.BOOLEAN value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final type.BOOLEAN bool) {
          return new BOOLEAN.ELSE(this, bool);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
          return new BOOLEAN.ELSE(this, type.DataType.wrap(bool));
        }

        @Override
        public final Case.BOOLEAN.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BOOLEAN.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.BOOLEAN> implements Case.BOOLEAN.ELSE {
      ELSE(final THEN_ELSE<type.BOOLEAN> parent, final type.BOOLEAN value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.BOOLEAN END() {
        // FIXME: This can be compressed...
        final type.BOOLEAN dataType = new type.BOOLEAN();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class FLOAT {
    static final class UNSIGNED {
      static final class Simple implements Case.FLOAT.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.INT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final byte numeric) {
            return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.FLOAT.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final type.TINYINT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final byte numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.FLOAT.simple.WHEN<T> WHEN(final T condition) {
            return new FLOAT.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.FLOAT.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.INT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final byte numeric) {
            return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.FLOAT.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final type.TINYINT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final byte numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new FLOAT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.FLOAT.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.FLOAT.UNSIGNED END() {
          final type.FLOAT.UNSIGNED dataType = new type.FLOAT.UNSIGNED();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.FLOAT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.INT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final byte numeric) {
          return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.FLOAT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.TINYINT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final byte numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.FLOAT.simple.WHEN<T> WHEN(final T condition) {
          return new FLOAT.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.FLOAT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.INT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final byte numeric) {
          return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.FLOAT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.TINYINT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final byte numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new FLOAT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.FLOAT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.FLOAT END() {
        final type.FLOAT dataType = new type.FLOAT();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class DOUBLE {
    static final class UNSIGNED {
      static final class Simple implements Case.DOUBLE.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.INT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DOUBLE.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.TINYINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final byte numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.DOUBLE.simple.WHEN<T> WHEN(final T condition) {
            return new DOUBLE.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DOUBLE.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.INT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final byte numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DOUBLE.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.TINYINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final byte numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DOUBLE.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.DOUBLE.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.DOUBLE.UNSIGNED END() {
          final type.DOUBLE.UNSIGNED dataType = new type.DOUBLE.UNSIGNED();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.DOUBLE.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.INT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DOUBLE.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.TINYINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final byte numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.simple.WHEN<T> WHEN(final T condition) {
          return new DOUBLE.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DOUBLE.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.INT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final byte numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DOUBLE.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.TINYINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final byte numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DOUBLE.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.DOUBLE.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.DOUBLE END() {
        final type.DOUBLE dataType = new type.DOUBLE();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class TINYINT {
    static final class UNSIGNED {
      static final class Simple implements Case.TINYINT.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new TINYINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new TINYINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
            return new TINYINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new TINYINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.TINYINT.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final type.TINYINT numeric) {
            return new TINYINT.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new TINYINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final byte numeric) {
            return new TINYINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new TINYINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.TINYINT.simple.WHEN<T> WHEN(final T condition) {
            return new TINYINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.TINYINT.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new TINYINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new TINYINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
            return new TINYINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new TINYINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.TINYINT.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final type.TINYINT numeric) {
            return new TINYINT.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new TINYINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final byte numeric) {
            return new TINYINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.TINYINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new TINYINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new TINYINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.TINYINT.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.TINYINT.UNSIGNED END() {
          final type.TINYINT.UNSIGNED dataType = (type.TINYINT.UNSIGNED)((THEN_ELSE<?>)parent).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.TINYINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new TINYINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new TINYINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.TINYINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final type.TINYINT numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final byte numeric) {
          return new TINYINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.simple.WHEN<T> WHEN(final T condition) {
          return new TINYINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.TINYINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new TINYINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new TINYINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.TINYINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final type.TINYINT numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final byte numeric) {
          return new TINYINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new TINYINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.TINYINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.TINYINT END() {
        final type.TINYINT dataType = (type.TINYINT)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class SMALLINT {
    static final class UNSIGNED {
      static final class Simple implements Case.SMALLINT.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric) {
            return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.SMALLINT.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.TINYINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final byte numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.SMALLINT.simple.WHEN<T> WHEN(final T condition) {
            return new SMALLINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.SMALLINT.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final byte numeric) {
            return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.SMALLINT.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public final Case.FLOAT.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.TINYINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.FLOAT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new FLOAT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final byte numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new SMALLINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new SMALLINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.SMALLINT.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.SMALLINT.UNSIGNED END() {
          final type.SMALLINT.UNSIGNED dataType = (type.SMALLINT.UNSIGNED)((THEN_ELSE<?>)parent).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.SMALLINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric) {
          return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.SMALLINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.TINYINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final byte numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.simple.WHEN<T> WHEN(final T condition) {
          return new SMALLINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.SMALLINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final byte numeric) {
          return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.SMALLINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.TINYINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final byte numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new SMALLINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.SMALLINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.SMALLINT END() {
        final type.Numeric<?> numeric = (type.Numeric<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
        final type.SMALLINT dataType = numeric instanceof type.SMALLINT ? (type.SMALLINT)numeric.clone() : numeric instanceof type.ExactNumeric ? new type.SMALLINT(((type.ExactNumeric<?>)numeric).precision()) : new type.SMALLINT();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class INT {
    static final class UNSIGNED {
      static final class Simple implements Case.INT.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final byte numeric) {
            return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final short numeric) {
            return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.INT.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.TINYINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.SMALLINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final byte numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final short numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.INT.simple.WHEN<T> WHEN(final T condition) {
            return new INT.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.INT.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final byte numeric) {
            return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final short numeric) {
            return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.INT.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.TINYINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.SMALLINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final type.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new INT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final byte numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final short numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.INT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new INT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.INT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new INT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.INT.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.INT.UNSIGNED END() {
          final type.INT.UNSIGNED dataType = (type.INT.UNSIGNED)((THEN_ELSE<?>)parent).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.INT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final byte numeric) {
          return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final short numeric) {
          return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.INT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.TINYINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.SMALLINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final byte numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final short numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.INT.simple.WHEN<T> WHEN(final T condition) {
          return new INT.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.INT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final byte numeric) {
          return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final short numeric) {
          return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.INT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.TINYINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.SMALLINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final byte numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final short numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.INT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new INT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.INT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.INT END() {
        final type.Numeric<?> numeric = (type.Numeric<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
        final type.INT dataType = numeric instanceof type.INT ? (type.INT)numeric.clone() : numeric instanceof type.ExactNumeric ? new type.INT(((type.ExactNumeric<?>)numeric).precision()) : new type.INT();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class BIGINT {
    static final class UNSIGNED {
      static final class Simple implements Case.BIGINT.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.INT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final byte numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final short numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final int numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.BIGINT.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.TINYINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.INT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final byte numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final short numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final int numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.BIGINT.simple.WHEN<T> WHEN(final T condition) {
            return new BIGINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.BIGINT.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.INT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final byte numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final short numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final int numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.BIGINT.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public final Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.TINYINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.SMALLINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.INT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DOUBLE.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final byte numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final short numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final int numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.BIGINT.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new BIGINT.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new BIGINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.BIGINT.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.BIGINT.UNSIGNED END() {
          final type.BIGINT.UNSIGNED dataType = (type.BIGINT.UNSIGNED)((THEN_ELSE<?>)parent).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.BIGINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.INT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final byte numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final short numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final int numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.BIGINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.TINYINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.INT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final byte numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final short numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final int numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.simple.WHEN<T> WHEN(final T condition) {
          return new BIGINT.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BIGINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.INT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final byte numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final short numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final int numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.BIGINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.TINYINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.SMALLINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.INT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final byte numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final short numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final int numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BIGINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.BIGINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.BIGINT END() {
        final type.Numeric<?> numeric = (type.Numeric<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
        final type.BIGINT dataType = numeric instanceof type.BIGINT ? (type.BIGINT)numeric.clone() : numeric instanceof type.ExactNumeric ? new type.BIGINT(((type.ExactNumeric<?>)numeric).precision()) : new type.BIGINT();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class DECIMAL {
    static final class UNSIGNED {
      static final class Simple implements Case.DECIMAL.UNSIGNED.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.UNSIGNED.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.FLOAT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.TINYINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.INT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final type.BIGINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final float numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final double numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final short numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final int numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final long numeric) {
            return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.simple.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DECIMAL.UNSIGNED.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DECIMAL.UNSIGNED.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.FLOAT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DOUBLE numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.TINYINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.SMALLINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.INT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.BIGINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final float numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final double numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final byte numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final short numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final int numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final long numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.DECIMAL.simple.WHEN<T> WHEN(final T condition) {
            return new DECIMAL.Simple.WHEN<>(this, type.DataType.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DECIMAL.UNSIGNED.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.UNSIGNED.search.WHEN<T> {
          WHEN(final CaseImpl.CASE parent, final type.DataType<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DECIMAL.search.THEN<T> THEN(final type.FLOAT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public final Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DOUBLE numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.TINYINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.SMALLINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.INT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final type.BIGINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final float numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final double numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final byte numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final short numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final int numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final long numeric) {
            return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.search.THEN<T> THEN(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DECIMAL.UNSIGNED.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DECIMAL.UNSIGNED.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DECIMAL.ELSE ELSE(final type.FLOAT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public final Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DOUBLE numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.TINYINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.SMALLINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.INT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.INT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final type.BIGINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final float numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Float numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final double numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Double numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.BigDecimal numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final byte numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Byte numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final short numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Short numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final int numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Integer numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final long numeric) {
            return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.UNSIGNED.ELSE ELSE(final org.jaxdb.jsql.UNSIGNED.Long numeric) {
            return new DECIMAL.UNSIGNED.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
          }

          @Override
          public final Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DECIMAL.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.DECIMAL.UNSIGNED.ELSE {
        ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final type.DECIMAL.UNSIGNED END() {
          final type.Numeric<?> numeric = (type.Numeric<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
          final type.DECIMAL.UNSIGNED dataType = numeric instanceof type.DECIMAL.UNSIGNED ? (type.DECIMAL.UNSIGNED)numeric.clone() : numeric instanceof type.ExactNumeric ? new type.DECIMAL.UNSIGNED(((type.ExactNumeric<?>)numeric).precision(), Integer.valueOf(0)) : new type.DECIMAL.UNSIGNED();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    static final class Simple implements Case.DECIMAL.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.FLOAT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.TINYINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.INT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.BIGINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final float numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final double numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final short numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final int numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final long numeric) {
          return new DECIMAL.Simple.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DECIMAL.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.FLOAT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DOUBLE numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.TINYINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.SMALLINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.INT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.BIGINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final float numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final double numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final byte numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final short numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final int numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final long numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.simple.WHEN<T> WHEN(final T condition) {
          return new DECIMAL.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DECIMAL.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DECIMAL.search.THEN<T> THEN(final type.FLOAT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DECIMAL.search.THEN<T> THEN(final type.FLOAT.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DOUBLE numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DOUBLE.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.TINYINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.TINYINT.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.SMALLINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.SMALLINT.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.INT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.INT.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.BIGINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final type.BIGINT.UNSIGNED numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final float numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final double numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final byte numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final short numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final int numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final long numeric) {
          return new DECIMAL.Search.THEN<>(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Numeric<?>> implements Case.DECIMAL.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DECIMAL.ELSE ELSE(final type.FLOAT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public final Case.DECIMAL.ELSE ELSE(final type.FLOAT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DOUBLE numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DOUBLE.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.DECIMAL.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.TINYINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.TINYINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.SMALLINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.SMALLINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.INT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.INT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.BIGINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final type.BIGINT.UNSIGNED numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final float numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final double numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final byte numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final short numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final int numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final long numeric) {
          return new DECIMAL.ELSE(this, (type.Numeric<?>)type.DataType.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DECIMAL.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Numeric<?>> implements Case.DECIMAL.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.DECIMAL END() {
        final type.Numeric<?> numeric = (type.Numeric<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
        final type.DECIMAL dataType = numeric instanceof type.DECIMAL ? (type.DECIMAL)numeric.clone() : numeric instanceof type.ExactNumeric ? new type.DECIMAL(((type.ExactNumeric<?>)numeric).precision(), Integer.valueOf(0)) : new type.DECIMAL();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class BINARY {
    static final class Simple implements Case.BINARY.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.BINARY.simple.THEN<T> THEN(final type.BINARY binary) {
          return new BINARY.Simple.THEN<>(this, binary);
        }

        @Override
        public Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Simple.THEN<>(this, type.DataType.wrap(binary));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.BINARY> implements Case.BINARY.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.BINARY value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.BINARY.ELSE ELSE(final type.BINARY binary) {
          return new BINARY.ELSE(this, binary);
        }

        @Override
        public Case.BINARY.ELSE ELSE(final byte[] binary) {
          return new BINARY.ELSE(this, type.DataType.wrap(binary));
        }

        @Override
        public final Case.BINARY.simple.WHEN<T> WHEN(final T condition) {
          return new BINARY.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BINARY.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final type.BINARY binary) {
          return new BINARY.Search.THEN<>(this, binary);
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Search.THEN<>(this, type.DataType.wrap(binary));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.BINARY> implements Case.BINARY.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.BINARY value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BINARY.ELSE ELSE(final type.BINARY binary) {
          return new BINARY.ELSE(this, binary);
        }

        @Override
        public final Case.BINARY.ELSE ELSE(final byte[] binary) {
          return new BINARY.ELSE(this, type.DataType.wrap(binary));
        }

        @Override
        public final Case.BINARY.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BINARY.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.BINARY> implements Case.BINARY.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.BINARY value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.BINARY END() {
        final type.BINARY dataType = (type.BINARY)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class DATE {
    static final class Simple implements Case.DATE.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DATE.simple.THEN<T> THEN(final type.DATE date) {
          return new DATE.Simple.THEN<>(this, date);
        }

        @Override
        public Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
          return new DATE.Simple.THEN<>(this, type.DataType.wrap(date));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.DATE> implements Case.DATE.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.DATE value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DATE.ELSE ELSE(final type.DATE date) {
          return new DATE.ELSE(this, date);
        }

        @Override
        public Case.DATE.ELSE ELSE(final LocalDate date) {
          return new DATE.ELSE(this, type.DataType.wrap(date));
        }

        @Override
        public final Case.DATE.simple.WHEN<T> WHEN(final T condition) {
          return new DATE.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DATE.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final type.DATE date) {
          return new DATE.Search.THEN<>(this, date);
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
          return new DATE.Search.THEN<>(this, type.DataType.wrap(date));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.DATE> implements Case.DATE.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.DATE value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DATE.ELSE ELSE(final type.DATE date) {
          return new DATE.ELSE(this, date);
        }

        @Override
        public final Case.DATE.ELSE ELSE(final LocalDate date) {
          return new DATE.ELSE(this, type.DataType.wrap(date));
        }

        @Override
        public final Case.DATE.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DATE.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.DATE> implements Case.DATE.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.DATE value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.DATE END() {
        final type.DATE dataType = (type.DATE)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class TIME {
    static final class Simple implements Case.TIME.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.TIME.simple.THEN<T> THEN(final type.TIME time) {
          return new TIME.Simple.THEN<>(this, time);
        }

        @Override
        public Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
          return new TIME.Simple.THEN<>(this, type.DataType.wrap(time));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.TIME> implements Case.TIME.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.TIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.TIME.ELSE ELSE(final type.TIME time) {
          return new TIME.ELSE(this, time);
        }

        @Override
        public Case.TIME.ELSE ELSE(final LocalTime time) {
          return new TIME.ELSE(this, type.DataType.wrap(time));
        }

        @Override
        public final Case.TIME.simple.WHEN<T> WHEN(final T condition) {
          return new TIME.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.TIME.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final type.TIME time) {
          return new TIME.Search.THEN<>(this, time);
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
          return new TIME.Search.THEN<>(this, type.DataType.wrap(time));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.TIME> implements Case.TIME.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.TIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.TIME.ELSE ELSE(final type.TIME time) {
          return new TIME.ELSE(this, time);
        }

        @Override
        public final Case.TIME.ELSE ELSE(final LocalTime time) {
          return new TIME.ELSE(this, type.DataType.wrap(time));
        }

        @Override
        public final Case.TIME.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new TIME.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.TIME> implements Case.TIME.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.TIME value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.TIME END() {
        final type.TIME dataType = (type.TIME)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class DATETIME {
    static final class Simple implements Case.DATETIME.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DATETIME.simple.THEN<T> THEN(final type.DATETIME dateTime) {
          return new DATETIME.Simple.THEN<>(this, dateTime);
        }

        @Override
        public Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Simple.THEN<>(this, type.DataType.wrap(dateTime));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.DATETIME> implements Case.DATETIME.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.DATETIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DATETIME.ELSE ELSE(final type.DATETIME dateTime) {
          return new DATETIME.ELSE(this, dateTime);
        }

        @Override
        public Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
          return new DATETIME.ELSE(this, type.DataType.wrap(dateTime));
        }

        @Override
        public final Case.DATETIME.simple.WHEN<T> WHEN(final T condition) {
          return new DATETIME.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DATETIME.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final type.DATETIME dateTime) {
          return new DATETIME.Search.THEN<>(this, dateTime);
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Search.THEN<>(this, type.DataType.wrap(dateTime));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.DATETIME> implements Case.DATETIME.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.DATETIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DATETIME.ELSE ELSE(final type.DATETIME dateTime) {
          return new DATETIME.ELSE(this, dateTime);
        }

        @Override
        public final Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
          return new DATETIME.ELSE(this, type.DataType.wrap(dateTime));
        }

        @Override
        public final Case.DATETIME.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DATETIME.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.DATETIME> implements Case.DATETIME.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.DATETIME value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.DATETIME END() {
        final type.DATETIME dataType = (type.DATETIME)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class CHAR {
    static final class Simple implements Case.CHAR.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final type.ENUM<?> text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final type.CHAR text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final Enum<?> text) {
          return new CHAR.Simple.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final String text) {
          return new CHAR.Simple.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Textual<?>> implements Case.CHAR.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.ENUM<?> text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final Enum<?> text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text.toString()));
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.WHEN<T> WHEN(final T condition) {
          return new CHAR.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.CHAR.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final type.ENUM<?> text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final type.CHAR text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final String text) {
          return new CHAR.Search.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final Enum<?> text) {
          return new CHAR.Search.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Textual<?>> implements Case.CHAR.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.ENUM<?> text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final Enum<?> text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text.toString()));
        }

        @Override
        public final Case.CHAR.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new CHAR.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Textual<?>> implements Case.CHAR.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Textual<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.CHAR END() {
        final type.Textual<?> textual = (type.Textual<?>)((THEN_ELSE<?>)parent).createReturnType();
        final type.CHAR dataType = textual instanceof type.CHAR ? (type.CHAR)textual.clone() : new type.CHAR(textual.length());
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  static final class ENUM {
    static final class Simple implements Case.ENUM.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final type.ENUM<?> text) {
          return new ENUM.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final type.CHAR text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final Enum<?> text) {
          return new ENUM.Simple.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final String text) {
          return new CHAR.Simple.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Textual<?>> implements Case.ENUM.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final type.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final Enum<?> text) {
          return new ENUM.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final type.ENUM<?> text) {
          return new ENUM.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.ENUM.simple.WHEN<T> WHEN(final T condition) {
          return new ENUM.Simple.WHEN<>(this, type.DataType.wrap(condition));
        }
      }
    }

    static final class Search implements Case.ENUM.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final type.DataType<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final type.ENUM<?> text) {
          return new ENUM.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final type.CHAR text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final String text) {
          return new CHAR.Search.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final Enum<?> text) {
          return new ENUM.Search.THEN<>(this, (type.Textual<?>)type.DataType.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,type.Textual<?>> implements Case.ENUM.search.THEN<T> {
        THEN(final CaseImpl.WHEN<?> parent, final type.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final type.ENUM<?> text) {
          return new ENUM.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final type.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final Enum<?> text) {
          return new ENUM.ELSE(this, (type.ENUM<?>)type.DataType.wrap(text));
        }

        @Override
        public final Case.ENUM.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new ENUM.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<type.Textual<?>> implements Case.ENUM.ELSE {
      ELSE(final THEN_ELSE<?> parent, final type.Textual<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final type.ENUM<?> END() {
        final type.ENUM<?> dataType = (type.ENUM<?>)((THEN_ELSE<?>)parent).createReturnType().clone();
        dataType.wrapper(this);
        return dataType;
      }
    }
  }

  private CaseImpl() {
  }
}