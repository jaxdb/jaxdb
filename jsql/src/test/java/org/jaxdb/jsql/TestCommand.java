/* Copyright (c) 2023 JAX-DB
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.jaxdb.jsql.RowIterator.Cacheability;

public abstract class TestCommand<E> extends Command<E> {
  public static class Select {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AssertSelect {
      boolean entityOnlySelect();
      boolean absolutePrimaryKeyCondition();
      boolean rowIteratorFullConsume();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AssertCommand {
      boolean ignore();
    }

    private static ThreadLocal<AssertCommand> assertCommand = new ThreadLocal<>();
    private static ThreadLocal<AssertSelect> assertSelect = new ThreadLocal<>();

    public static void beforeInvokeExplosively(final Method method) {
      Select.assertCommand.set(method.getAnnotation(AssertCommand.class));
      Select.assertSelect.set(method.getAnnotation(AssertSelect.class));
    }

    public static QueryConfig configure(final Transaction transaction) {
      final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
      if (assertSelect == null || transaction == null)
        return null;

      if (assertSelect.absolutePrimaryKeyCondition())
        return new QueryConfig.Builder()
          .withCacheability(Cacheability.SELECT_CACHEABLE_ENTITY_LOOKUP)
          .withCacheableRowIteratorFullConsume(assertSelect.rowIteratorFullConsume())
          .build();

      if (assertSelect.entityOnlySelect())
        return new QueryConfig.Builder()
          .withCacheability(Cacheability.SELECT_CACHEABLE_ENTITY)
          .withCacheableRowIteratorFullConsume(assertSelect.rowIteratorFullConsume())
          .build();

      return null;
    }

    private static boolean assertCompile(final boolean isAbsolutePrimaryKeyCondition, final boolean isEntityOnlySelect) {
      final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
      final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
      if (assertSelect != null) {
        assertEquals("AssertCommand.absolutePrimaryKeyCondition()", assertSelect.absolutePrimaryKeyCondition(), isAbsolutePrimaryKeyCondition);
        assertEquals("AssertSelect.entityOnlySelect()", assertSelect.entityOnlySelect(), isEntityOnlySelect);

      }
      else if (assertCommand == null || !assertCommand.ignore()) {
        fail("@AssertSelect is not specified");
      }

      return isAbsolutePrimaryKeyCondition;
    }

    private static void assertRowIterator(final boolean endReached) {
      final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
      if (assertSelect != null)
        assertEquals("AssertSelect.rowIteratorFullConsume()", assertSelect.rowIteratorFullConsume(), endReached);
    }

    static class ARRAY {
      static class SELECT<D extends type.Entity> extends Command.Select.ARRAY.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class BIGINT {
      static class SELECT<D extends type.Entity> extends Command.Select.BIGINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class BINARY {
      static class SELECT<D extends type.Entity> extends Command.Select.BINARY.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class BLOB {
      static class SELECT<D extends type.Entity> extends Command.Select.BLOB.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class BOOLEAN {
      static class SELECT<D extends type.Entity> extends Command.Select.BOOLEAN.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class CHAR {
      static class SELECT<D extends type.Entity> extends Command.Select.CHAR.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class CLOB {
      static class SELECT<D extends type.Entity> extends Command.Select.CLOB.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class Column {
      static class SELECT<D extends type.Entity> extends Command.Select.Column.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class DATE {
      static class SELECT<D extends type.Entity> extends Command.Select.DATE.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class DATETIME {
      static class SELECT<D extends type.Entity> extends Command.Select.DATETIME.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class DECIMAL {
      static class SELECT<D extends type.Entity> extends Command.Select.DECIMAL.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class DOUBLE {
      static class SELECT<D extends type.Entity> extends Command.Select.DOUBLE.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class Entity {
      static class SELECT<D extends type.Entity> extends Command.Select.Entity.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class ENUM {
      static class SELECT<D extends type.Entity> extends Command.Select.ENUM.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class FLOAT {
      static class SELECT<D extends type.Entity> extends Command.Select.FLOAT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class INT {
      static class SELECT<D extends type.Entity> extends Command.Select.INT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class LargeObject {
      static class SELECT<D extends type.Entity> extends Command.Select.LargeObject.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class Numeric {
      static class SELECT<D extends type.Entity> extends Command.Select.Numeric.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class SMALLINT {
      static class SELECT<D extends type.Entity> extends Command.Select.SMALLINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class Temporal {
      static class SELECT<D extends type.Entity> extends Command.Select.Temporal.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class Textual {
      static class SELECT<D extends type.Entity> extends Command.Select.Textual.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class TIME {
      static class SELECT<D extends type.Entity> extends Command.Select.TIME.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class TINYINT {
      static class SELECT<D extends type.Entity> extends Command.Select.TINYINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }

    static class untyped {
      static class SELECT<D extends type.Entity> extends Command.Select.untyped.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression, final Cacheability cacheability) throws IOException, SQLException {
          return compilation.toString().isEmpty() ? assertCompile(super.compile(compilation, isExpression, cacheability), isEntityOnlySelect) : super.compile(compilation, isExpression, cacheability);
        }

        @Override
        void assertRowIteratorClosed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          super.assertRowIteratorClosed(endReached, isCacheable, e, isCacheableRowIteratorFullConsume);
          assertRowIterator(endReached);
        }
      }
    }
  }
}