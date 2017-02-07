/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.entities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.safris.xdb.entities.model.case_;

final class Case extends SQLStatement implements case_ {
  protected static abstract class ChainedKeyword extends Keyword<Subject<?>> {
    protected ChainedKeyword(final Keyword<Subject<?>> parent) {
      super(parent);
    }

    protected abstract type.DataType<?> createReturnType();
  }

  protected static abstract class CASE_THEN extends ChainedKeyword {
    protected CASE_THEN(final Keyword<Subject<?>> parent) {
      super(parent);
    }
  }

  protected static abstract class CASE extends CASE_THEN {
    protected CASE(final Keyword<Subject<?>> parent) {
      super(parent);
    }

    @Override
    protected final type.DataType<?> createReturnType() {
      return null;
    }
  }

  protected static abstract class WHEN<T> extends ChainedKeyword {
    protected final type.DataType<T> condition;

    protected WHEN(final CASE_THEN parent, final type.DataType<T> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    protected final type.DataType<?> createReturnType() {
      return parent() == null ? null : ((CASE_THEN)parent()).createReturnType();
    }
  }

  protected static abstract class THEN_ELSE<T extends type.DataType<?>> extends CASE_THEN {
    protected final T value;

    protected THEN_ELSE(final ChainedKeyword parent, final T value) {
      super(parent);
      this.value = value;
    }

    @Override
    protected final type.DataType<?> createReturnType() {
      final type.DataType<?> dataType = ((ChainedKeyword)parent()).createReturnType();
      return dataType != null ? dataType.scaleTo(value) : value;
    }
  }

  protected static abstract class THEN<T,R extends type.DataType<?>> extends THEN_ELSE<R> {
    protected THEN(final WHEN<?> parent, final R value) {
      super(parent, value);
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static abstract class ELSE<T extends type.DataType<?>> extends THEN_ELSE<T> {
    protected ELSE(final THEN_ELSE<?> parent, final T value) {
      super(parent, value);
    }

    @Override
    protected final Command normalize() {
      final CaseCommand command = (CaseCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class Simple implements simple {
    protected static final class CASE<T,R extends type.DataType<?>> extends Case.CASE implements simple.CASE<T> {
      protected final type.DataType<T> variable;

      protected CASE(final Keyword<Subject<?>> parent, final type.DataType<T> variable) {
        super(parent);
        this.variable = variable;
      }

      @Override
      public simple.WHEN<T> WHEN(final T condition) {
        return new WHEN<T,R>(this, type.DataType.wrap(condition));
      }

      @Override
      protected Command normalize() {
        return new CaseCommand(this);
      }
    }

    protected static final class WHEN<T,R extends type.DataType<?>> extends Case.WHEN<T> implements simple.WHEN<T> {
      protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
        super(parent, condition);
      }

      @Override
      public final simple.BOOLEAN.THEN THEN(final type.BOOLEAN bool) {
        return new Simple.BOOLEAN.THEN<T>(this, bool);
      }

      @Override
      public final simple.FLOAT.THEN<T> THEN(final type.FLOAT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.INT.THEN<T> THEN(final type.INT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.BIGINT.THEN<T> THEN(final type.BIGINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.FLOAT.THEN<T> THEN(final float numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.BOOLEAN.THEN THEN(final boolean bool) {
        return new Simple.BOOLEAN.THEN<T>(this, type.DataType.wrap(bool));
      }

      @Override
      public final simple.DOUBLE.THEN<T> THEN(final double numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DECIMAL.THEN<T> THEN(final BigDecimal numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.FLOAT.THEN<T> THEN(final short numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.MEDIUMINT.THEN<T> THEN(final int numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.INT.THEN<T> THEN(final long numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.BIGINT.THEN<T> THEN(final BigInteger numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.CLOB.THEN<T> THEN(final type.CLOB clob) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.BLOB.THEN<T> THEN(final type.BLOB clob) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.BINARY.THEN<T> THEN(final type.BINARY binary) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DATE.THEN<T> THEN(final type.DATE date) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.TIME.THEN<T> THEN(final type.TIME time) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DATETIME.THEN<T> THEN(final type.DATETIME dateTime) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.CHAR.THEN<T> THEN(final type.CHAR text) {
        return new Simple.CHAR.THEN<T>(this, text);
      }

      @Override
      public final simple.ENUM.THEN<T> THEN(final type.ENUM<?> text) {
        return new Simple.ENUM.THEN<T>(this, text);
      }

      @Override
      public final simple.BINARY.THEN<T> THEN(final byte[] binary) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DATE.THEN<T> THEN(final LocalDate date) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.TIME.THEN<T> THEN(final LocalTime time) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.DATETIME.THEN<T> THEN(final LocalDateTime dateTime) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final simple.CHAR.THEN<T> THEN(final String text) {
        return new Simple.CHAR.THEN<T>(this, type.DataType.wrap(text));
      }

      @Override
      public final simple.ENUM.THEN<T> THEN(final Enum<?> text) {
        return new Simple.ENUM.THEN<T>(this, (type.ENUM<?>)type.DataType.wrap(text));
      }

      @Override
      protected final Command normalize() {
        return parent().normalize();
      }
    }

    protected static final class BOOLEAN implements simple.BOOLEAN {
      protected static final class WHEN<T> extends Case.WHEN<T> implements simple.BOOLEAN.WHEN {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public final simple.BOOLEAN.THEN THEN(final type.BOOLEAN bool) {
          throw new UnsupportedOperationException();
        }

        @Override
        public final simple.BOOLEAN.THEN THEN(final boolean bool) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected final Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.BOOLEAN> implements simple.BOOLEAN.THEN {
        protected THEN(final Case.WHEN<T> parent, final type.BOOLEAN value) {
          super(parent, value);
        }

        @Override
        public final simple.BOOLEAN.ELSE ELSE(final type.BOOLEAN bool) {
          return new ELSE(this, bool);
        }

        @Override
        public final simple.BOOLEAN.ELSE ELSE(final boolean bool) {
          return new ELSE(this, type.DataType.wrap(bool));
        }

        @Override
        public final simple.BOOLEAN.WHEN WHEN(final boolean condition) {
          return new Simple.BOOLEAN.WHEN<T>(this, (type.DataType<T>)type.DataType.wrap(condition));
        }
      }

      protected static class ELSE extends Case.ELSE<type.BOOLEAN> implements simple.BOOLEAN.ELSE {
        protected ELSE(final THEN_ELSE<type.BOOLEAN> parent, final type.BOOLEAN value) {
          super(parent, value);
        }

        @Override
        public type.BOOLEAN END() {
          final type.BOOLEAN dataType = new type.BOOLEAN();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    protected static final class CHAR implements simple.CHAR {
      protected static final class WHEN<T> extends Case.WHEN<T> implements simple.CHAR.WHEN<T> {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public simple.CHAR.THEN<T> THEN(final type.ENUM<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public simple.CHAR.THEN<T> THEN(final type.CHAR text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public final simple.CHAR.THEN<T> THEN(final Enum<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public final simple.CHAR.THEN<T> THEN(final String text) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected final Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.CHAR> implements simple.CHAR.THEN<T> {
        protected THEN(final Case.WHEN<T> parent, final type.CHAR value) {
          super(parent, value);
        }

        @Override
        public simple.CHAR.ELSE<T> ELSE(final type.ENUM<?> text) {
          return new ELSE<T>(this, text);
        }

        @Override
        public simple.CHAR.ELSE<T> ELSE(final type.CHAR text) {
          return new ELSE<T>(this, text);
        }

        @Override
        public final simple.CHAR.ELSE<T> ELSE(final Enum<?> text) {
          return new ELSE<T>(this, (type.Textual<?>)type.DataType.wrap(text.toString()));
        }

        @Override
        public final simple.CHAR.ELSE<T> ELSE(final String text) {
          return new ELSE<T>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final simple.CHAR.WHEN<T> WHEN(final T condition) {
          return new Simple.CHAR.WHEN<T>(this, type.DataType.wrap(condition));
        }
      }

      protected static class ELSE<T> extends Case.ELSE<type.Textual<?>> implements simple.CHAR.ELSE<T> {
        protected ELSE(final THEN_ELSE<?> parent, final type.Textual<?> value) {
          super(parent, value);
        }

        @Override
        public final type.CHAR END() {
          final type.Textual<?> textual = (type.Textual<?>)((THEN_ELSE<type.ENUM<?>>)parent()).createReturnType();
          final type.CHAR dataType = textual instanceof type.CHAR ? (type.CHAR)textual.clone() : new type.CHAR(textual.length());
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    protected static final class ENUM implements simple.ENUM {
      protected static final class WHEN<T> extends Case.WHEN<T> implements simple.ENUM.WHEN<T> {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public final simple.ENUM.THEN<T> THEN(final Enum<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public simple.ENUM.THEN<T> THEN(final type.ENUM<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public simple.CHAR.THEN<T> THEN(final type.CHAR text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public final simple.CHAR.THEN<T> THEN(final String text) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected final Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.ENUM<?>> implements simple.ENUM.THEN<T> {
        protected THEN(final Case.WHEN<T> parent, final type.ENUM<?> value) {
          super(parent, value);
        }

        @Override
        public final simple.ENUM.ELSE<T> ELSE(final Enum<?> text) {
          return new ELSE<T>(this, (type.ENUM<?>)type.DataType.wrap(text));
        }

        @Override
        public simple.ENUM.ELSE<T> ELSE(final type.ENUM<?> text) {
          return new ELSE<T>(this, text);
        }

        @Override
        public simple.CHAR.ELSE<T> ELSE(final type.CHAR text) {
          return new Simple.CHAR.ELSE<T>(this, text);
        }

        @Override
        public simple.CHAR.ELSE<T> ELSE(final String text) {
          return new Simple.CHAR.ELSE<T>(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final simple.ENUM.WHEN<T> WHEN(final T condition) {
          return new Simple.ENUM.WHEN<T>(this, type.DataType.wrap(condition));
        }
      }

      protected static class ELSE<T> extends Case.ELSE<type.ENUM<?>> implements simple.ENUM.ELSE<T> {
        protected ELSE(final THEN_ELSE<type.ENUM<?>> parent, final type.ENUM<?> value) {
          super(parent, value);
        }

        @Override
        public final type.ENUM<?> END() {
          final type.ENUM<?> dataType = (type.ENUM<?>)((THEN_ELSE<type.ENUM<?>>)parent()).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    private Simple() {
    }
  }

  protected static final class Search implements search {
    protected static class WHEN<T> extends Case.WHEN<T> implements search.WHEN<T> {
      protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
        super(parent, condition);
      }

      @Override
      public final search.BOOLEAN.THEN<T> THEN(final type.BOOLEAN bool) {
        return new Search.BOOLEAN.THEN<T>(this, bool);
      }

      @Override
      public final search.FLOAT.THEN<T> THEN(final type.FLOAT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DOUBLE.THEN<T> THEN(final type.DOUBLE numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DECIMAL.THEN<T> THEN(final type.DECIMAL numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.SMALLINT.THEN<T> THEN(final type.SMALLINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.MEDIUMINT.THEN<T> THEN(final type.MEDIUMINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.INT.THEN<T> THEN(final type.INT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.BIGINT.THEN<T> THEN(final type.BIGINT numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.FLOAT.THEN<T> THEN(final float numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.BOOLEAN.THEN<T> THEN(final boolean bool) {
        return new Search.BOOLEAN.THEN<T>(this, type.DataType.wrap(bool));
      }

      @Override
      public final search.DOUBLE.THEN<T> THEN(final double numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DECIMAL.THEN<T> THEN(final BigDecimal numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.FLOAT.THEN<T> THEN(final short numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.MEDIUMINT.THEN<T> THEN(final int numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.INT.THEN<T> THEN(final long numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.BIGINT.THEN<T> THEN(final BigInteger numeric) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.CLOB.THEN<T> THEN(final type.CLOB clob) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.BLOB.THEN<T> THEN(final type.BLOB clob) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.BINARY.THEN<T> THEN(final type.BINARY binary) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DATE.THEN<T> THEN(final type.DATE date) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.TIME.THEN<T> THEN(final type.TIME time) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DATETIME.THEN<T> THEN(final type.DATETIME dateTime) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.CHAR.THEN<T> THEN(final type.CHAR text) {
        return new Search.CHAR.THEN<T>(this, text);
      }

      @Override
      public final search.ENUM.THEN<T> THEN(final type.ENUM<?> text) {
        return new Search.ENUM.THEN<T>(this, text);
      }

      @Override
      public final search.BINARY.THEN<T> THEN(final byte[] binary) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DATE.THEN<T> THEN(final LocalDate date) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.TIME.THEN<T> THEN(final LocalTime time) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.DATETIME.THEN<T> THEN(final LocalDateTime dateTime) {
        throw new UnsupportedOperationException();
      }

      @Override
      public final search.CHAR.THEN<T> THEN(final String text) {
        return new Search.CHAR.THEN<T>(this, type.DataType.wrap(text));
      }

      @Override
      public search.ENUM.THEN<T> THEN(final Enum<?> text) {
        return new Search.ENUM.THEN<T>(this, (type.ENUM<?>)type.DataType.wrap(text));
      }

      @Override
      protected final Command normalize() {
        return new CaseCommand(this);
      }
    }

    protected static final class BOOLEAN implements search.BOOLEAN {
      protected static final class WHEN<T> extends Case.WHEN<T> implements search.BOOLEAN.CASE<T>, search.BOOLEAN.WHEN<T> {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public search.BOOLEAN.THEN<T> THEN(final type.BOOLEAN bool) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.BOOLEAN.THEN<T> THEN(final boolean bool) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.BOOLEAN> implements search.BOOLEAN.THEN<T> {
        protected THEN(final Case.WHEN<?> parent, final type.BOOLEAN value) {
          super(parent, value);
        }

        @Override
        public final search.BOOLEAN.ELSE<T> ELSE(final type.BOOLEAN bool) {
          return new ELSE<T>(this, bool);
        }

        @Override
        public final search.BOOLEAN.ELSE<T> ELSE(final boolean bool) {
          return new ELSE<T>(this, type.DataType.wrap(bool));
        }

        @Override
        public final search.BOOLEAN.WHEN<T> WHEN(final Condition<T> condition) {
          return new WHEN<T>(this, condition);
        }
      }

      protected static class ELSE<T> extends Case.ELSE<type.BOOLEAN> implements search.BOOLEAN.ELSE<T> {
        protected ELSE(final THEN_ELSE<?> parent, final type.BOOLEAN value) {
          super(parent, value);
        }

        @Override
        public final search.BOOLEAN.ELSE<T> ELSE(final type.BOOLEAN bool) {
          return new ELSE<T>(this, bool);
        }

        @Override
        public final search.BOOLEAN.ELSE<T> ELSE(final boolean bool) {
          return new ELSE<T>(this, type.DataType.wrap(bool));
        }

        @Override
        public final search.BOOLEAN.WHEN<T> WHEN(final Condition<T> condition) {
          return new WHEN<T>(this, condition);
        }

        @Override
        public final type.BOOLEAN END() {
          final type.BOOLEAN dataType = new type.BOOLEAN();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    protected static final class CHAR implements search.CHAR {
      protected static final class WHEN<T> extends Case.WHEN<T> implements search.CHAR.WHEN<T> {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public search.CHAR.THEN<T> THEN(final type.ENUM<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.CHAR.THEN<T> THEN(final type.CHAR text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.CHAR.THEN<T> THEN(final String text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.CHAR.THEN<T> THEN(final Enum<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.CHAR> implements search.CHAR.THEN<T> {
        protected THEN(final Case.WHEN<T> parent, final type.CHAR value) {
          super(parent, value);
        }

        @Override
        public search.CHAR.ELSE ELSE(final type.ENUM<?> text) {
          return new ELSE(this, text);
        }

        @Override
        public search.CHAR.ELSE ELSE(final type.CHAR text) {
          return new ELSE(this, text);
        }

        @Override
        public final search.CHAR.ELSE ELSE(final String text) {
          return new ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final search.CHAR.ELSE ELSE(final Enum<?> text) {
          return new ELSE(this, (type.Textual<?>)type.DataType.wrap(text.toString()));
        }

        @Override
        public final search.CHAR.WHEN<T> WHEN(final Condition<T> condition) {
          return new WHEN<T>(this, condition);
        }
      }

      protected static class ELSE extends Case.ELSE<type.Textual<?>> implements search.CHAR.ELSE {
        protected ELSE(final THEN_ELSE<?> parent, final type.Textual<?> value) {
          super(parent, value);
        }

        @Override
        public final type.CHAR END() {
          final type.Textual<?> textual = (type.Textual<?>)((THEN_ELSE<type.ENUM<?>>)parent()).createReturnType();
          final type.CHAR dataType = textual instanceof type.CHAR ? (type.CHAR)textual.clone() : new type.CHAR(textual.length());
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    protected static final class ENUM implements search.ENUM {
      protected static final class WHEN<T> extends Case.WHEN<T> implements search.ENUM.WHEN<T> {
        protected WHEN(final Case.CASE_THEN parent, final type.DataType<T> condition) {
          super(parent, condition);
        }

        @Override
        public search.ENUM.THEN<T> THEN(final type.ENUM<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.CHAR.THEN<T> THEN(final type.CHAR text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.CHAR.THEN<T> THEN(final String text) {
          throw new UnsupportedOperationException();
        }

        @Override
        public search.ENUM.THEN<T> THEN(final Enum<?> text) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected Command normalize() {
          throw new UnsupportedOperationException();
        }
      }

      protected static class THEN<T> extends Case.THEN<T,type.ENUM<?>> implements search.ENUM.THEN<T> {
        protected THEN(final Case.WHEN<?> parent, final type.ENUM<?> value) {
          super(parent, value);
        }

        @Override
        public search.ENUM.ELSE ELSE(final type.ENUM<?> text) {
          return new ELSE(this, text);
        }

        @Override
        public search.CHAR.ELSE ELSE(final type.CHAR text) {
          return new Search.CHAR.ELSE(this, text);
        }

        @Override
        public final search.CHAR.ELSE ELSE(final String text) {
          return new Search.CHAR.ELSE(this, (type.Textual<?>)type.DataType.wrap(text));
        }

        @Override
        public final search.ENUM.ELSE ELSE(final Enum<?> text) {
          return new ELSE(this, (type.ENUM<?>)type.DataType.wrap(text));
        }

        @Override
        public final search.ENUM.WHEN<T> WHEN(final Condition<T> condition) {
          return new WHEN<T>(this, condition);
        }
      }

      protected static class ELSE extends Case.ELSE<type.ENUM<?>> implements search.ENUM.ELSE {
        protected ELSE(final THEN_ELSE<?> parent, final type.ENUM<?> value) {
          super(parent, value);
        }

        @Override
        public type.ENUM<?> END() {
          final type.ENUM<?> dataType = (type.ENUM<?>)((THEN_ELSE<type.ENUM<?>>)parent()).createReturnType().clone();
          dataType.wrapper(this);
          return dataType;
        }
      }
    }

    private Search() {
    }
  }

  private Case() {
  }
}