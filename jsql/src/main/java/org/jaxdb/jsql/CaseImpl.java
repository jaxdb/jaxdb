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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import org.jaxdb.jsql.data.Table;

final class CaseImpl implements Case {
  private abstract static class ChainedKeyword extends Keyword<data.Entity<?>> {
    final ChainedKeyword root;
    final ChainedKeyword parent;
    ArrayList<data.Column<?>> whenThen;
    data.Column<?> _else;

    ChainedKeyword(final ChainedKeyword root, final ChainedKeyword parent) {
      this.root = Objects.requireNonNull(root);
      this.parent = parent;
    }

    ChainedKeyword() {
      this.root = this;
      this.parent = null;
    }

    @Override
    final Table table() {
      if (root != this)
        return root.table();

      for (final data.Column<?> column : whenThen)
        if (column.table() != null)
          return column.table();

      return null;
    }

    final void WHEN(final data.Column<?> when) {
      if (parent != null) {
        parent.WHEN(when);
      }
      else {
        if (whenThen == null)
          whenThen = new ArrayList<>();

        whenThen.add(when);
      }
    }

    final void THEN(final data.Column<?> then) {
      if (parent != null)
        parent.THEN(then);
      else
        whenThen.add(then);
    }

    final void ELSE(final data.Column<?> _else) {
      if (parent != null)
        parent.ELSE(_else);
      else
        this._else = _else;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Compiler compiler = compilation.compiler;
      if (whenThen != null)
        for (int i = 0, len = whenThen.size(); i < len;)
          compiler.compileWhenThenElse(whenThen.get(i++), whenThen.get(i++), _else, compilation);

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
    final data.Column<?> column() {
      return null;
    }
  }

  abstract static class WHEN<T> extends ChainedKeyword {
    WHEN(final ChainedKeyword root, final CASE_THEN parent, final data.Column<T> when) {
      super(root, parent);
      WHEN(when);
    }

    WHEN(final data.Column<T> when) {
      super();
      WHEN(when);
    }

    @Override
    final data.Column<?> column() {
      return parent == null ? null : ((CASE_THEN)parent).column();
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compileWhen((Search.WHEN<?>)this, compilation);
      super.compile(compilation, isExpression);
    }
  }

  private abstract static class THEN_ELSE<D extends data.Column<?>> extends CASE_THEN {
    final D value;

    THEN_ELSE(final ChainedKeyword root, final ChainedKeyword parent, final D value) {
      super(root, parent);
      this.value = value;
    }

    @Override
    final data.Column<?> column() {
      final data.Column<?> column = parent.column();
      return column != null ? column.scaleTo(value) : value;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      if (root == null)
        System.out.println();
      root.compile(compilation, isExpression);
    }
  }

  abstract static class THEN<T,D extends data.Column<?>> extends THEN_ELSE<D> {
    THEN(final ChainedKeyword root, final WHEN<?> parent, final D then) {
      super(root, parent, then);
      THEN(then);
    }
  }

  abstract static class ELSE<D extends data.Column<?>> extends THEN_ELSE<D> {
    ELSE(final ChainedKeyword root, final THEN_ELSE<?> parent, final D _else) {
      super(root, parent, _else);
      ELSE(_else);
    }
  }

  static final class Simple implements simple {
    static final class CASE<T,D extends data.Column<?>> extends CaseImpl.CASE implements simple.CASE<T> {
      final data.Column<T> variable;

      CASE(final data.Column<T> variable) {
        super();
        this.variable = variable;
      }

      @Override
      public final simple.WHEN<T> WHEN(final T when) {
        return new WHEN<T,D>(this, data.Column.wrap(when));
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        final Compiler compiler = compilation.compiler;
        compiler.compileCaseElse(variable, _else, compilation);
        super.compile(compilation, isExpression);
      }
    }

    static final class WHEN<T,D extends data.Column<?>> extends CaseImpl.WHEN<T> implements simple.WHEN<T> {
      WHEN(final CaseImpl.CASE parent, final data.Column<T> when) {
        super(parent, parent, when);
      }

      @Override
      public final Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool) {
        return new BOOLEAN.Simple.THEN<>(this, bool);
      }

      @Override
      public final Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
        return new FLOAT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
        return new DOUBLE.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
        return new DECIMAL.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
        return new TINYINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
        return new SMALLINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
        return new INT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
        return new BIGINT.Simple.THEN<>(this, numeric);
      }

      @Override
      public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
        return new BOOLEAN.Simple.THEN<>(this, data.Column.wrap(bool));
      }

      @Override
      public final Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
        return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
        return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
        return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
        return new TINYINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
        return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.INT.simple.THEN<T> THEN(final int numeric) {
        return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
        return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary) {
        return new BINARY.Simple.THEN<>(this, binary);
      }

      @Override
      public final Case.DATE.simple.THEN<T> THEN(final data.DATE date) {
        return new DATE.Simple.THEN<>(this, date);
      }

      @Override
      public final Case.TIME.simple.THEN<T> THEN(final data.TIME time) {
        return new TIME.Simple.THEN<>(this, time);
      }

      @Override
      public final Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime) {
        return new DATETIME.Simple.THEN<>(this, dateTime);
      }

      @Override
      public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
        return new CHAR.Simple.THEN<>(this, text);
      }

      @Override
      public final Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> text) {
        return new ENUM.Simple.THEN<>(this, text);
      }

      @Override
      public final Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
        return new BINARY.Simple.THEN<>(this, data.Column.wrap(binary));
      }

      @Override
      public final Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
        return new DATE.Simple.THEN<>(this, data.Column.wrap(date));
      }

      @Override
      public final Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
        return new TIME.Simple.THEN<>(this, data.Column.wrap(time));
      }

      @Override
      public final Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
        return new DATETIME.Simple.THEN<>(this, data.Column.wrap(dateTime));
      }

      @Override
      public final Case.CHAR.simple.THEN<T> THEN(final String text) {
        return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
      }

      @Override
      public final Case.ENUM.simple.THEN<T> THEN(final Enum<?> text) {
        return new ENUM.Simple.THEN<>(this, (data.ENUM<?>)data.Column.wrap(text));
      }
    }

    private Simple() {
    }
  }

  static final class Search implements search {
    static final class WHEN<T> extends CaseImpl.WHEN<T> implements search.WHEN<T> {
      WHEN(final data.Column<T> when) {
        super(when);
      }

      @Override
      public final Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool) {
        return new BOOLEAN.Search.THEN<>(this, bool);
      }

      @Override
      public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
        return new FLOAT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
        return new DOUBLE.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
        return new DECIMAL.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric) {
        return new TINYINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
        return new SMALLINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.INT.search.THEN<T> THEN(final data.INT numeric) {
        return new INT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
        return new BIGINT.Search.THEN<>(this, numeric);
      }

      @Override
      public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
        return new BOOLEAN.Search.THEN<>(this, data.Column.wrap(bool));
      }

      @Override
      public final Case.FLOAT.search.THEN<T> THEN(final float numeric) {
        return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
        return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
        return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
        return new TINYINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
        return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.INT.search.THEN<T> THEN(final int numeric) {
        return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.BIGINT.search.THEN<T> THEN(final long numeric) {
        return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
      }

      @Override
      public final Case.BINARY.search.THEN<T> THEN(final data.BINARY binary) {
        return new BINARY.Search.THEN<>(this, binary);
      }

      @Override
      public final Case.DATE.search.THEN<T> THEN(final data.DATE date) {
        return new DATE.Search.THEN<>(this, date);
      }

      @Override
      public final Case.TIME.search.THEN<T> THEN(final data.TIME time) {
        return new TIME.Search.THEN<>(this, time);
      }

      @Override
      public final Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime) {
        return new DATETIME.Search.THEN<>(this, dateTime);
      }

      @Override
      public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
        return new CHAR.Search.THEN<>(this, text);
      }

      @Override
      public final Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> text) {
        return new ENUM.Search.THEN<>(this, text);
      }

      @Override
      public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
        return new BINARY.Search.THEN<>(this, data.Column.wrap(binary));
      }

      @Override
      public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
        return new DATE.Search.THEN<>(this, data.Column.wrap(date));
      }

      @Override
      public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
        return new TIME.Search.THEN<>(this, data.Column.wrap(time));
      }

      @Override
      public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
        return new DATETIME.Search.THEN<>(this, data.Column.wrap(dateTime));
      }

      @Override
      public final Case.CHAR.search.THEN<T> THEN(final String text) {
        return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
      }

      @Override
      public final Case.ENUM.search.THEN<T> THEN(final Enum<?> text) {
        return new ENUM.Search.THEN<>(this, (data.ENUM<?>)data.Column.wrap(text));
      }
    }

    private Search() {
    }
  }

  static final class BOOLEAN {
    static final class Simple implements Case.BOOLEAN.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.simple.WHEN {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool) {
          return new BOOLEAN.Simple.THEN<>(this, bool);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
          return new BOOLEAN.Simple.THEN<>(this, data.Column.wrap(bool));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.BOOLEAN> implements Case.BOOLEAN.simple.THEN {
        THEN(final CaseImpl.WHEN<T> parent, final data.BOOLEAN value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final data.BOOLEAN bool) {
          return new BOOLEAN.ELSE(this, bool);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
          return new BOOLEAN.ELSE(this, data.Column.wrap(bool));
        }

        @Override
        @SuppressWarnings("unchecked")
        public final Case.BOOLEAN.simple.WHEN WHEN(final boolean condition) {
          return new BOOLEAN.Simple.WHEN<>(this, (data.Column<T>)data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BOOLEAN.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.search.CASE<T>, Case.BOOLEAN.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool) {
          return new BOOLEAN.Search.THEN<>(this, bool);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
          return new BOOLEAN.Search.THEN<>(this, data.Column.wrap(bool));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.BOOLEAN> implements Case.BOOLEAN.search.THEN<T> {
        THEN(final CaseImpl.WHEN<?> parent, final data.BOOLEAN value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final data.BOOLEAN bool) {
          return new BOOLEAN.ELSE(this, bool);
        }

        @Override
        public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
          return new BOOLEAN.ELSE(this, data.Column.wrap(bool));
        }

        @Override
        public final Case.BOOLEAN.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BOOLEAN.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.BOOLEAN> implements Case.BOOLEAN.ELSE {
      ELSE(final THEN_ELSE<data.BOOLEAN> parent, final data.BOOLEAN value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.BOOLEAN END() {
        // FIXME: This can be compressed...
        final data.BOOLEAN column = new data.BOOLEAN();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class FLOAT {
    static final class Simple implements Case.FLOAT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final byte numeric) {
          return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.FLOAT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final byte numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.FLOAT.simple.WHEN<T> WHEN(final T condition) {
          return new FLOAT.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.FLOAT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final byte numeric) {
          return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.FLOAT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final byte numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new FLOAT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.FLOAT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.FLOAT END() {
        final data.FLOAT column = new data.FLOAT();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class DOUBLE {
    static final class Simple implements Case.DOUBLE.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DOUBLE.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final byte numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.simple.WHEN<T> WHEN(final T condition) {
          return new DOUBLE.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DOUBLE.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final byte numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DOUBLE.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final byte numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final short numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final int numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final long numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DOUBLE.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.DOUBLE.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.DOUBLE END() {
        final data.DOUBLE column = new data.DOUBLE();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class TINYINT {
    static final class Simple implements Case.TINYINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new TINYINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.TINYINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final byte numeric) {
          return new TINYINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.simple.WHEN<T> WHEN(final T condition) {
          return new TINYINT.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.TINYINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new TINYINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.TINYINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric) {
          return new TINYINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.TINYINT.ELSE ELSE(final byte numeric) {
          return new TINYINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new TINYINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.TINYINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.TINYINT END() {
        final data.TINYINT column = (data.TINYINT)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class SMALLINT {
    static final class Simple implements Case.SMALLINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric) {
          return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.SMALLINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final byte numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.simple.WHEN<T> WHEN(final T condition) {
          return new SMALLINT.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.SMALLINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final byte numeric) {
          return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.SMALLINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
          return new FLOAT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new SMALLINT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.FLOAT.ELSE ELSE(final float numeric) {
          return new FLOAT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final byte numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.SMALLINT.ELSE ELSE(final short numeric) {
          return new SMALLINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new SMALLINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.SMALLINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.SMALLINT END() {
        final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).column().clone();
        final data.SMALLINT column = numeric instanceof data.SMALLINT ? (data.SMALLINT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.SMALLINT(((data.ExactNumeric<?>)numeric).precision()) : new data.SMALLINT();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class INT {
    static final class Simple implements Case.INT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final byte numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final short numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.INT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.TINYINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.SMALLINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final byte numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final short numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.INT.simple.WHEN<T> WHEN(final T condition) {
          return new INT.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.INT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final byte numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final short numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.INT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.TINYINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.SMALLINT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.INT.ELSE ELSE(final data.INT numeric) {
          return new INT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final byte numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final short numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.INT.ELSE ELSE(final int numeric) {
          return new INT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.INT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new INT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.INT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.INT END() {
        final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).column().clone();
        final data.INT column = numeric instanceof data.INT ? (data.INT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.INT(((data.ExactNumeric<?>)numeric).precision()) : new data.INT();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class BIGINT {
    static final class Simple implements Case.BIGINT.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.INT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final byte numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final short numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final int numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.BIGINT.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.INT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final byte numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final short numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final int numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.simple.WHEN<T> WHEN(final T condition) {
          return new BIGINT.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BIGINT.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.INT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final byte numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final short numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final int numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.BIGINT.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
          return new DOUBLE.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.INT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
          return new BIGINT.ELSE(this, numeric);
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final float numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DOUBLE.ELSE ELSE(final double numeric) {
          return new DOUBLE.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final byte numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final short numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final int numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.BIGINT.ELSE ELSE(final long numeric) {
          return new BIGINT.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BIGINT.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.BIGINT.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.BIGINT END() {
        final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).column().clone();
        final data.BIGINT column = numeric instanceof data.BIGINT ? (data.BIGINT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.BIGINT(((data.ExactNumeric<?>)numeric).precision()) : new data.BIGINT();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class DECIMAL {
    static final class Simple implements Case.DECIMAL.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.INT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final float numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final double numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final short numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final int numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.simple.THEN<T> THEN(final long numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DECIMAL.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.INT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final float numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final double numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final byte numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final short numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final int numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final long numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.simple.WHEN<T> WHEN(final T condition) {
          return new DECIMAL.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DECIMAL.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DECIMAL.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.INT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final float numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final double numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final byte numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final short numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final int numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.search.THEN<T> THEN(final long numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DECIMAL.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.INT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric) {
          return new DECIMAL.ELSE(this, numeric);
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final float numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final double numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final byte numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final short numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final int numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public Case.DECIMAL.ELSE ELSE(final long numeric) {
          return new DECIMAL.ELSE(this, (data.Numeric<?>)data.Column.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DECIMAL.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.DECIMAL.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.DECIMAL END() {
        final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).column().clone();
        final data.DECIMAL column = numeric instanceof data.DECIMAL ? (data.DECIMAL)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.DECIMAL(((data.ExactNumeric<?>)numeric).precision(), Integer.valueOf(0)) : new data.DECIMAL();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class BINARY {
    static final class Simple implements Case.BINARY.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary) {
          return new BINARY.Simple.THEN<>(this, binary);
        }

        @Override
        public Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Simple.THEN<>(this, data.Column.wrap(binary));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.BINARY> implements Case.BINARY.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.BINARY value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.BINARY.ELSE ELSE(final data.BINARY binary) {
          return new BINARY.ELSE(this, binary);
        }

        @Override
        public Case.BINARY.ELSE ELSE(final byte[] binary) {
          return new BINARY.ELSE(this, data.Column.wrap(binary));
        }

        @Override
        public final Case.BINARY.simple.WHEN<T> WHEN(final T condition) {
          return new BINARY.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.BINARY.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final data.BINARY binary) {
          return new BINARY.Search.THEN<>(this, binary);
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Search.THEN<>(this, data.Column.wrap(binary));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.BINARY> implements Case.BINARY.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.BINARY value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.BINARY.ELSE ELSE(final data.BINARY binary) {
          return new BINARY.ELSE(this, binary);
        }

        @Override
        public final Case.BINARY.ELSE ELSE(final byte[] binary) {
          return new BINARY.ELSE(this, data.Column.wrap(binary));
        }

        @Override
        public final Case.BINARY.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new BINARY.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.BINARY> implements Case.BINARY.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.BINARY value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.BINARY END() {
        final data.BINARY column = (data.BINARY)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class DATE {
    static final class Simple implements Case.DATE.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DATE.simple.THEN<T> THEN(final data.DATE date) {
          return new DATE.Simple.THEN<>(this, date);
        }

        @Override
        public Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
          return new DATE.Simple.THEN<>(this, data.Column.wrap(date));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.DATE> implements Case.DATE.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.DATE value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DATE.ELSE ELSE(final data.DATE date) {
          return new DATE.ELSE(this, date);
        }

        @Override
        public Case.DATE.ELSE ELSE(final LocalDate date) {
          return new DATE.ELSE(this, data.Column.wrap(date));
        }

        @Override
        public final Case.DATE.simple.WHEN<T> WHEN(final T condition) {
          return new DATE.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DATE.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final data.DATE date) {
          return new DATE.Search.THEN<>(this, date);
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
          return new DATE.Search.THEN<>(this, data.Column.wrap(date));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.DATE> implements Case.DATE.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.DATE value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DATE.ELSE ELSE(final data.DATE date) {
          return new DATE.ELSE(this, date);
        }

        @Override
        public final Case.DATE.ELSE ELSE(final LocalDate date) {
          return new DATE.ELSE(this, data.Column.wrap(date));
        }

        @Override
        public final Case.DATE.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DATE.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.DATE> implements Case.DATE.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.DATE value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.DATE END() {
        final data.DATE column = (data.DATE)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class TIME {
    static final class Simple implements Case.TIME.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.TIME.simple.THEN<T> THEN(final data.TIME time) {
          return new TIME.Simple.THEN<>(this, time);
        }

        @Override
        public Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
          return new TIME.Simple.THEN<>(this, data.Column.wrap(time));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.TIME> implements Case.TIME.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.TIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.TIME.ELSE ELSE(final data.TIME time) {
          return new TIME.ELSE(this, time);
        }

        @Override
        public Case.TIME.ELSE ELSE(final LocalTime time) {
          return new TIME.ELSE(this, data.Column.wrap(time));
        }

        @Override
        public final Case.TIME.simple.WHEN<T> WHEN(final T condition) {
          return new TIME.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.TIME.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final data.TIME time) {
          return new TIME.Search.THEN<>(this, time);
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
          return new TIME.Search.THEN<>(this, data.Column.wrap(time));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.TIME> implements Case.TIME.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.TIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.TIME.ELSE ELSE(final data.TIME time) {
          return new TIME.ELSE(this, time);
        }

        @Override
        public final Case.TIME.ELSE ELSE(final LocalTime time) {
          return new TIME.ELSE(this, data.Column.wrap(time));
        }

        @Override
        public final Case.TIME.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new TIME.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.TIME> implements Case.TIME.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.TIME value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.TIME END() {
        final data.TIME column = (data.TIME)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class DATETIME {
    static final class Simple implements Case.DATETIME.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime) {
          return new DATETIME.Simple.THEN<>(this, dateTime);
        }

        @Override
        public Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Simple.THEN<>(this, data.Column.wrap(dateTime));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.DATETIME> implements Case.DATETIME.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.DATETIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public Case.DATETIME.ELSE ELSE(final data.DATETIME dateTime) {
          return new DATETIME.ELSE(this, dateTime);
        }

        @Override
        public Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
          return new DATETIME.ELSE(this, data.Column.wrap(dateTime));
        }

        @Override
        public final Case.DATETIME.simple.WHEN<T> WHEN(final T condition) {
          return new DATETIME.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.DATETIME.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime) {
          return new DATETIME.Search.THEN<>(this, dateTime);
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Search.THEN<>(this, data.Column.wrap(dateTime));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.DATETIME> implements Case.DATETIME.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.DATETIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.DATETIME.ELSE ELSE(final data.DATETIME dateTime) {
          return new DATETIME.ELSE(this, dateTime);
        }

        @Override
        public final Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
          return new DATETIME.ELSE(this, data.Column.wrap(dateTime));
        }

        @Override
        public final Case.DATETIME.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new DATETIME.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.DATETIME> implements Case.DATETIME.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.DATETIME value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.DATETIME END() {
        final data.DATETIME column = (data.DATETIME)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class CHAR {
    static final class Simple implements Case.CHAR.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final data.ENUM<?> text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final Enum<?> text) {
          return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final String text) {
          return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.CHAR.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.ENUM<?> text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final Enum<?> text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text.toString()));
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.WHEN<T> WHEN(final T condition) {
          return new CHAR.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.CHAR.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final data.ENUM<?> text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final String text) {
          return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final Enum<?> text) {
          return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.CHAR.search.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.ENUM<?> text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final Enum<?> text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text.toString()));
        }

        @Override
        public final Case.CHAR.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new CHAR.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Textual<?>> implements Case.CHAR.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Textual<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.CHAR END() {
        final data.Textual<?> textual = (data.Textual<?>)((THEN_ELSE<?>)parent).column();
        final data.CHAR column = textual instanceof data.CHAR ? (data.CHAR)textual.clone() : new data.CHAR(textual.length());
        column.wrapper(this);
        return column;
      }
    }
  }

  static final class ENUM {
    static final class Simple implements Case.ENUM.simple {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.simple.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> text) {
          return new ENUM.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final Enum<?> text) {
          return new ENUM.Simple.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final String text) {
          return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.ENUM.simple.THEN<T> {
        THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final Enum<?> text) {
          return new ENUM.ELSE(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final data.ENUM<?> text) {
          return new ENUM.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.ENUM.simple.WHEN<T> WHEN(final T condition) {
          return new ENUM.Simple.WHEN<>(this, data.Column.wrap(condition));
        }
      }
    }

    static final class Search implements Case.ENUM.search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.search.WHEN<T> {
        WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> text) {
          return new ENUM.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final String text) {
          return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final Enum<?> text) {
          return new ENUM.Search.THEN<>(this, (data.Textual<?>)data.Column.wrap(text));
        }
      }

      static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.ENUM.search.THEN<T> {
        THEN(final CaseImpl.WHEN<?> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final data.ENUM<?> text) {
          return new ENUM.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
          return new CHAR.ELSE(this, text);
        }

        @Override
        public final Case.CHAR.ELSE ELSE(final String text) {
          return new CHAR.ELSE(this, (data.Textual<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.ENUM.ELSE ELSE(final Enum<?> text) {
          return new ENUM.ELSE(this, (data.ENUM<?>)data.Column.wrap(text));
        }

        @Override
        public final Case.ENUM.search.WHEN<T> WHEN(final Condition<T> condition) {
          return new ENUM.Search.WHEN<>(this, condition);
        }
      }
    }

    static final class ELSE extends CaseImpl.ELSE<data.Textual<?>> implements Case.ENUM.ELSE {
      ELSE(final THEN_ELSE<?> parent, final data.Textual<?> value) {
        super(parent.root, parent, value);
      }

      @Override
      public final data.ENUM<?> END() {
        final data.ENUM<?> column = (data.ENUM<?>)((THEN_ELSE<?>)parent).column().clone();
        column.wrapper(this);
        return column;
      }
    }
  }

  private CaseImpl() {
  }
}