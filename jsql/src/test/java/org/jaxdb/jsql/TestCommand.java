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

public abstract class TestCommand<E> extends Command<E> {
  public static class Select {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AssertSelect {
      boolean conditionOnlyPrimary();
      boolean cacheableExclusivity();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AssertCommand {
      boolean ignore();
    }

    private static ThreadLocal<AssertCommand> assertCommand = new ThreadLocal<>();
    private static ThreadLocal<AssertSelect> assertSelect = new ThreadLocal<>();

    public static void beforeInvokeExplosively(final Method method, final Transaction transaction) {
      Select.assertCommand.set(method.getAnnotation(AssertCommand.class));
      final AssertSelect assertSelect = method.getAnnotation(AssertSelect.class);
      Select.assertSelect.set(assertSelect);
      if (transaction != null) {
        transaction.getConnector().getSchema().defaultQueryConfig = new QueryConfig.Builder()
          .withCacheableExclusivity(assertSelect != null && assertSelect.cacheableExclusivity())
          .withCacheablePrimaryIndexEfficiencyExclusivity(assertSelect != null && assertSelect.conditionOnlyPrimary())
          .build();
      }
    }

    static class ARRAY {
      static class SELECT<D extends type.Entity> extends Command.Select.ARRAY.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class BIGINT {
      static class SELECT<D extends type.Entity> extends Command.Select.BIGINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class BINARY {
      static class SELECT<D extends type.Entity> extends Command.Select.BINARY.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class BLOB {
      static class SELECT<D extends type.Entity> extends Command.Select.BLOB.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class BOOLEAN {
      static class SELECT<D extends type.Entity> extends Command.Select.BOOLEAN.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class CHAR {
      static class SELECT<D extends type.Entity> extends Command.Select.CHAR.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class CLOB {
      static class SELECT<D extends type.Entity> extends Command.Select.CLOB.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class Column {
      static class SELECT<D extends type.Entity> extends Command.Select.Column.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class DATE {
      static class SELECT<D extends type.Entity> extends Command.Select.DATE.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class DATETIME {
      static class SELECT<D extends type.Entity> extends Command.Select.DATETIME.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class DECIMAL {
      static class SELECT<D extends type.Entity> extends Command.Select.DECIMAL.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class DOUBLE {
      static class SELECT<D extends type.Entity> extends Command.Select.DOUBLE.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class Entity {
      static class SELECT<D extends type.Entity> extends Command.Select.Entity.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class ENUM {
      static class SELECT<D extends type.Entity> extends Command.Select.ENUM.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class FLOAT {
      static class SELECT<D extends type.Entity> extends Command.Select.FLOAT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class INT {
      static class SELECT<D extends type.Entity> extends Command.Select.INT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class LargeObject {
      static class SELECT<D extends type.Entity> extends Command.Select.LargeObject.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class Numeric {
      static class SELECT<D extends type.Entity> extends Command.Select.Numeric.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class SMALLINT {
      static class SELECT<D extends type.Entity> extends Command.Select.SMALLINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class Temporal {
      static class SELECT<D extends type.Entity> extends Command.Select.Temporal.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class Textual {
      static class SELECT<D extends type.Entity> extends Command.Select.Textual.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class TIME {
      static class SELECT<D extends type.Entity> extends Command.Select.TIME.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class TINYINT {
      static class SELECT<D extends type.Entity> extends Command.Select.TINYINT.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }

    static class untyped {
      static class SELECT<D extends type.Entity> extends Command.Select.untyped.SELECT<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          if (!compilation.toString().isEmpty())
            return super.compile(compilation, isExpression);

          final boolean isSimple = super.compile(compilation, isExpression);
          final AssertCommand assertCommand = TestCommand.Select.assertCommand.get();
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertCommand.isConditionOnlyPrimary()", assertSelect.conditionOnlyPrimary(), isSimple);
          else if (assertCommand == null || !assertCommand.ignore())
            fail("@AssertSelect is not specified");

          return isSimple;
        }

        @Override
        Object[][] compile(final type.Entity[] entities, final int len, final int index, final int depth) {
          final Object[][] protoSunjectIndexes = super.compile(entities, len, index, depth);
          final AssertSelect assertSelect = TestCommand.Select.assertSelect.get();
          if (assertSelect != null)
            assertEquals("AssertSelect.cacheableExclusivity()", assertSelect.cacheableExclusivity(), isCacheable);

          return protoSunjectIndexes;
        }
      }
    }
  }
}