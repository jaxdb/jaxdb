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

package org.jaxdb.ddlx;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorSpecific;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Check;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey.OnDelete$;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey.OnUpdate$;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.Schema;
import org.libj.lang.Numbers;
import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;
import org.libj.util.CollectionUtil;
import org.libj.util.function.Throwing;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

abstract class Compiler extends DBVendorSpecific {
  private static final Compiler[] compilers = new Compiler[DBVendor.values().length];

  static {
    try {
      PackageLoader.getContextPackageLoader().loadPackage(Compiler.class.getPackage(), Throwing.<Class<?>>rethrow((c) -> {
        if (Compiler.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
          final Compiler compiler = (Compiler)c.getDeclaredConstructor().newInstance();
          compilers[compiler.getVendor().ordinal()] = compiler;
        }

        return false;
      }));
    }
    catch (final IOException | PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  static boolean isAutoIncrement(final $Integer column) {
    return column.getGenerateOnInsert$() != null && $Integer.GenerateOnInsert$.AUTO_5FINCREMENT.text().equals(column.getGenerateOnInsert$().text());
  }

  static String getAttr(final String name, final $Integer column) {
    final Iterator<? extends $AnySimpleType> attributeIterator = column.attributeIterator();
    while (attributeIterator.hasNext()) {
      final $AnySimpleType attr = attributeIterator.next();
      if (name.equals(attr.name().getLocalPart()))
        return String.valueOf(attr.text());
    }

    return null;
  }

  abstract CreateStatement createIndex(boolean unique, String indexName, $Index.Type$ type, String tableName, $Named ... columns);

  abstract void init(Connection connection) throws SQLException;

  /**
   * Create a "SchemaIfNotExists" {@link CreateStatement} for the specified
   * {@link Schema}.
   *
   * @param schema The {@link Schema}.
   * @return A "SchemaIfNotExists" {@link CreateStatement} for the specified
   *         {@link Schema}.
   */
  CreateStatement createSchemaIfNotExists(final Schema schema) {
    return null;
  }

  CreateStatement createTableIfNotExists(final $Table table, final Map<String,? extends $Column> columnNameToColumn) throws GeneratorExecutionException {
    final StringBuilder builder = new StringBuilder();
    final String tableName = table.getName$().text();
    builder.append("CREATE TABLE ").append(q(tableName)).append(" (\n");
    if (table.getColumn() != null)
      builder.append(createColumns(table));

    final CreateStatement constraints = createConstraints(columnNameToColumn, table);
    if (constraints != null)
      builder.append(constraints);

    builder.append("\n)");
    return new CreateStatement(builder.toString());
  }

  private String createColumns(final $Table table) {
    final StringBuilder builder = new StringBuilder();
    final Iterator<$Column> iterator = table.getColumn().iterator();
    $Column column = null;
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0) {
        builder.append(',');
        if (column != null && column.getDocumentation() != null)
          builder.append(" -- ").append(column.getDocumentation().text());

        builder.append('\n');
      }

      builder.append("  ").append(createColumn(table, column = iterator.next()));
    }

    return builder.toString();
  }

  private CreateStatement createColumn(final $Table table, final $Column column) {
    final StringBuilder builder = new StringBuilder();
    builder.append(q(column.getName$().text())).append(' ');
    // FIXME: Passing null to compile*() methods will throw a NPE
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      builder.append(getVendor().getDialect().compileChar(type.getVarying$().text(), type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      builder.append(getVendor().getDialect().compileBinary(type.getVarying$().text(), type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Blob) {
      final $Blob type = ($Blob)column;
      builder.append(getVendor().getDialect().compileBlob(type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Clob) {
      final $Clob type = ($Clob)column;
      builder.append(getVendor().getDialect().compileClob(type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Integer) {
      builder.append(createIntegerColumn(($Integer)column));
    }
    else if (column instanceof $Float) {
      final $Float type = ($Float)column;
      builder.append(getVendor().getDialect().declareFloat(type.getUnsigned$().text()));
    }
    else if (column instanceof $Double) {
      final $Double type = ($Double)column;
      builder.append(getVendor().getDialect().declareDouble(type.getUnsigned$().text()));
    }
    else if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      builder.append(getVendor().getDialect().declareDecimal(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getScale$() == null ? null : type.getScale$().text(), type.getUnsigned$().text()));
    }
    else if (column instanceof $Date) {
      builder.append(getVendor().getDialect().declareDate());
    }
    else if (column instanceof $Time) {
      final $Time type = ($Time)column;
      builder.append(getVendor().getDialect().declareTime(type.getPrecision$() == null ? null : type.getPrecision$().text()));
    }
    else if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      builder.append(getVendor().getDialect().declareDateTime(type.getPrecision$() == null ? null : type.getPrecision$().text()));
    }
    else if (column instanceof $Boolean) {
      builder.append(getVendor().getDialect().declareBoolean());
    }
    else if (column instanceof $Enum) {
      builder.append(getVendor().getDialect().declareEnum(($Enum)column));
    }

    final String autoIncrementFragment = column instanceof $Integer ? $autoIncrement(table, ($Integer)column) : null;
    if (autoIncrementFragment == null || autoIncrementFragment.length() == 0) {
      final String defaultFragment = $default(column);
      if (defaultFragment != null && defaultFragment.length() > 0)
        builder.append(" DEFAULT ").append(defaultFragment);
    }

    final String nullFragment = $null(table, column);
    if (nullFragment != null && nullFragment.length() > 0)
      builder.append(' ').append(nullFragment);

    if (autoIncrementFragment != null && autoIncrementFragment.length() > 0)
      builder.append(' ').append(autoIncrementFragment);

    return new CreateStatement(builder.toString());
  }

  String createIntegerColumn(final $Integer column) {
    if (column instanceof $Tinyint) {
      final $Tinyint type = ($Tinyint)column;
      return getVendor().getDialect().compileInt8(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getUnsigned$().text());
    }

    if (column instanceof $Smallint) {
      final $Smallint type = ($Smallint)column;
      return getVendor().getDialect().compileInt16(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getUnsigned$().text());
    }

    if (column instanceof $Int) {
      final $Int type = ($Int)column;
      return getVendor().getDialect().compileInt32(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getUnsigned$().text());
    }

    if (column instanceof $Bigint) {
      final $Bigint type = ($Bigint)column;
      return getVendor().getDialect().compileInt64(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getUnsigned$().text());
    }

    throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
  }

  private void appendOnDeleteOnUpdate(final StringBuilder constraintsBuilder, final $ForeignKey foreignKey) {
    // The ON DELETE and ON UPDATE rules must be the same at this point, given previous checks.
    if (foreignKey.getOnDelete$() != null) {
      final String onDelete = onDelete(foreignKey.getOnDelete$());
      if (onDelete != null)
        constraintsBuilder.append(' ').append(onDelete);
    }

    if (foreignKey.getOnUpdate$() != null) {
      final String onUpdate = onUpdate(foreignKey.getOnUpdate$());
      if (onUpdate != null)
        constraintsBuilder.append(' ').append(onUpdate);
    }
  }

  private CreateStatement createConstraints(final Map<String,? extends $Column> columnNameToColumn, final $Table table) throws GeneratorExecutionException {
    final StringBuilder constraintsBuilder = new StringBuilder();
    if (table.getConstraints() != null) {
      final $Constraints constraints = table.getConstraints();

      // unique constraint
      final List<$Columns> uniques = constraints.getUnique();
      if (uniques != null) {
        final StringBuilder uniqueString = new StringBuilder();
        int uniqueIndex = 1;
        final StringBuilder builder = new StringBuilder();
        for (final $Columns unique : uniques) {
          final List<$Named> columns = unique.getColumn();
          final Iterator<$Named> iterator = columns.iterator();
          for (int i = 0; iterator.hasNext(); ++i) {
            if (i > 0)
              builder.append(", ");

            final $Named column = iterator.next();
            builder.append(q(column.getName$().text()));
          }

          uniqueString.append(",\n  CONSTRAINT ").append(q(table.getName$().text() + "_unique_" + uniqueIndex++)).append(" UNIQUE (").append(builder).append(')');
          builder.setLength(0);
        }

        constraintsBuilder.append(uniqueString);
      }

      // check constraint
      final List<$Check> checks = constraints.getCheck();
      if (checks != null) {
        final StringBuilder checkString = new StringBuilder();
        for (final $Check check : checks) {
          final String checkClause = recurseCheckRule(check);
          checkString.append(",\n  CHECK ").append(checkClause.startsWith("(") ? checkClause : "(" + checkClause + ")");
        }

        constraintsBuilder.append(checkString);
      }

      // primary key constraint
      final String primaryKeyConstraint = blockPrimaryKey(table, constraints, columnNameToColumn);
      if (primaryKeyConstraint != null)
        constraintsBuilder.append(primaryKeyConstraint);

      // foreign key constraints
      final List<$ForeignKeyComposite> foreignKeyComposites = constraints.getForeignKey();
      if (foreignKeyComposites != null) {
        for (final $ForeignKeyComposite foreignKeyComposite : foreignKeyComposites) {
          final List<$ForeignKeyComposite.Column> columns = foreignKeyComposite.getColumn();
          final List<String> foreignKeyColumns = new ArrayList<>();
          final List<String> foreignKeyReferences = new ArrayList<>();
          for (final $ForeignKeyComposite.Column column : columns) {
            foreignKeyColumns.add(q(column.getName$().text()));
            foreignKeyReferences.add(q(column.getReferences$().text()));
          }

          constraintsBuilder.append(",\n  ").append(foreignKey(table)).append(" (").append(CollectionUtil.toString(foreignKeyColumns, ", "));
          constraintsBuilder.append(") REFERENCES ").append(q(foreignKeyComposite.getReferences$().text()));
          constraintsBuilder.append(" (").append(CollectionUtil.toString(foreignKeyReferences, ", ")).append(')');
          appendOnDeleteOnUpdate(constraintsBuilder, foreignKeyComposite);
        }
      }
    }

    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        final $ForeignKeyUnary foreignKey = column.getForeignKey();
        if (foreignKey != null) {
          constraintsBuilder.append(",\n  ").append(foreignKey(table)).append(" (").append(q(column.getName$().text()));
          constraintsBuilder.append(") REFERENCES ").append(q(foreignKey.getReferences$().text()));
          constraintsBuilder.append(" (").append(q(foreignKey.getColumn$().text())).append(')');

          appendOnDeleteOnUpdate(constraintsBuilder, foreignKey);
        }
      }

      // Parse the min & max constraints of numeric types
      for (final $Column column : table.getColumn()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $Integer) {
          if (column instanceof $Tinyint) {
            final $Tinyint type = ($Tinyint)column;
            minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
            maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
          }
          else if (column instanceof $Smallint) {
            final $Smallint type = ($Smallint)column;
            minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
            maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
          }
          else if (column instanceof $Int) {
            final $Int type = ($Int)column;
            minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
            maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
          }
          else if (column instanceof $Bigint) {
            final $Bigint type = ($Bigint)column;
            minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
            maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
          }
          else {
            throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
          }
        }
        else if (column instanceof $Float) {
          final $Float type = ($Float)column;
          minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
          maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
        }
        else if (column instanceof $Double) {
          final $Double type = ($Double)column;
          minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
          maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
        }
        else if (column instanceof $Decimal) {
          final $Decimal type = ($Decimal)column;
          minCheck = type.getMin$() != null ? String.valueOf(type.getMin$().text()) : null;
          maxCheck = type.getMax$() != null ? String.valueOf(type.getMax$().text()) : null;
        }

        if (minCheck != null)
          minCheck = q(column.getName$().text()) + " >= " + minCheck;

        if (maxCheck != null)
          maxCheck = q(column.getName$().text()) + " <= " + maxCheck;

        if (minCheck != null) {
          if (maxCheck != null)
            constraintsBuilder.append(",\n  ").append(check(table)).append(" (").append(minCheck).append(" AND ").append(maxCheck).append(')');
          else
            constraintsBuilder.append(",\n  ").append(check(table)).append(" (").append(minCheck).append(')');
        }
        else if (maxCheck != null) {
          constraintsBuilder.append(",\n  ").append(check(table)).append(" (").append(maxCheck).append(')');
        }
      }

      // parse the <check/> element per type
      for (final $Column column : table.getColumn()) {
        String operator = null;
        String condition = null;
        if (column instanceof $Char) {
          final $Char type = ($Char)column;
          if (type.getCheck() != null) {
            operator = $Char.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Char.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : null;
            condition = "'" + type.getCheck().getCondition$().text() + "'";
          }
        }
        else if (column instanceof $Tinyint) {
          final $Tinyint type = ($Tinyint)column;
          if (type.getCheck() != null) {
            operator = $Tinyint.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Tinyint.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Tinyint.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Tinyint.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Tinyint.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Tinyint.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Smallint) {
          final $Smallint type = ($Smallint)column;
          if (type.getCheck() != null) {
            operator = $Smallint.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Smallint.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Smallint.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Smallint.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Smallint.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Smallint.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Int) {
          final $Int type = ($Int)column;
          if (type.getCheck() != null) {
            operator = $Int.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Int.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Int.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Int.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Int.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Int.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Bigint) {
          final $Bigint type = ($Bigint)column;
          if (type.getCheck() != null) {
            operator = $Bigint.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Bigint.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Bigint.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Bigint.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Bigint.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Bigint.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Float) {
          final $Float type = ($Float)column;
          if (type.getCheck() != null) {
            operator = $Float.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Float.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Float.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Float.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Float.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Float.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Double) {
          final $Double type = ($Double)column;
          if (type.getCheck() != null) {
            operator = $Double.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Double.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Double.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Double.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Double.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Double.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }
        else if (column instanceof $Decimal) {
          final $Decimal type = ($Decimal)column;
          if (type.getCheck() != null) {
            operator = $Decimal.Check.Operator$.eq.text().equals(type.getCheck().getOperator$().text()) ? "=" : $Decimal.Check.Operator$.ne.text().equals(type.getCheck().getOperator$().text()) ? "!=" : $Decimal.Check.Operator$.gt.text().equals(type.getCheck().getOperator$().text()) ? ">" : $Decimal.Check.Operator$.gte.text().equals(type.getCheck().getOperator$().text()) ? ">=" : $Decimal.Check.Operator$.lt.text().equals(type.getCheck().getOperator$().text()) ? "<" : $Decimal.Check.Operator$.lte.text().equals(type.getCheck().getOperator$().text()) ? "<=" : null;
            condition = String.valueOf(type.getCheck().getCondition$().text());
          }
        }

        if (operator != null) {
          if (condition != null)
            constraintsBuilder.append(",\n  ").append(check(table)).append(" (").append(q(column.getName$().text())).append(' ').append(operator).append(' ').append(condition).append(')');
          else
            throw new UnsupportedOperationException("Unsupported 'null' condition encountered on column '" + column.getName$().text());
        }
        else if (condition != null) {
          throw new UnsupportedOperationException("Unsupported 'null' operator encountered on column '" + column.getName$().text());
        }
      }
    }

    return constraintsBuilder.length() == 0 ? null : new CreateStatement(constraintsBuilder.toString());
  }

  /**
   * Returns the "CHECK" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @return The "CHECK" keyword for the specified {@link $Table}.
   */
  String check(final $Table table) {
    return "CHECK";
  }

  String blockPrimaryKey(final $Table table, final $Constraints constraints, final Map<String,? extends $Column> columnNameToColumn) throws GeneratorExecutionException {
    if (constraints.getPrimaryKey() == null)
      return "";

    final StringBuilder builder = new StringBuilder();
    final Iterator<$Named> iterator = constraints.getPrimaryKey().getColumn().iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final $Named primaryColumn = iterator.next();
      final String primaryKeyColumn = primaryColumn.getName$().text();
      final $Column column = columnNameToColumn.get(primaryKeyColumn);
      if (column == null)
        throw new GeneratorExecutionException("PRIMARY KEY column " + table.getName$().text() + "." + primaryKeyColumn + " is not defined");

      if (column.getNull$().text())
        throw new GeneratorExecutionException("Column " + column.getName$() + " must be NOT NULL to be a PRIMARY KEY");

      if (i > 0)
        builder.append(", ");

      builder.append(q(primaryKeyColumn));
    }

    return ",\n  " + primaryKey(table) + " (" + builder + ")";
  }

  /**
   * Returns the "FOREIGN KEY" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @return The "FOREIGN KEY" keyword for the specified {@link $Table}.
   */
  String foreignKey(final $Table table) {
    return "FOREIGN KEY";
  }

  /**
   * Returns the "PRIMARY KEY" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @return The "PRIMARY KEY" keyword for the specified {@link $Table}.
   */
  String primaryKey(final $Table table) {
    return "PRIMARY KEY";
  }

  String onDelete(final OnDelete$ onDelete) {
    return "ON DELETE " + changeRule(onDelete);
  }

  String onUpdate(final OnUpdate$ onUpdate) {
    return "ON UPDATE " + changeRule(onUpdate);
  }

  String changeRule(final $ChangeRule changeRule) {
    return changeRule.text();
  }

  private static String recurseCheckRule(final $Check check) {
    final String condition;
    if (check.getColumn().size() == 2)
      condition = check.getColumn(0).text();
    else if (check.getValue() != null)
      condition = Numbers.isNumber(check.getValue().text()) ? Numbers.stripTrailingZeros(check.getValue().text()) : "'" + check.getValue().text() + "'";
    else
      throw new UnsupportedOperationException("Unsupported condition on column '" + check.getColumn(0).text() + "'");

    final String clause = check.getColumn(0).text() + " " + check.getOperator().text() + " " + condition;
    if (check.getAnd() != null)
      return "(" + clause + " AND " + recurseCheckRule(check.getAnd()) + ")";

    if (check.getOr() != null)
      return "(" + clause + " OR " + recurseCheckRule(check.getOr()) + ")";

    return clause;
  }

  /**
   * Delegate method to produce {@link CreateStatement} objects of
   * {@code TRIGGER} clauses for the specified {@link $Table table}.
   *
   * @param table The {@link $Table} for which to produce {@code TRIGGER}
   *          clauses.
   * @return A list of {@link CreateStatement} objects of {@code TRIGGER}
   *         clauses for the specified {@link $Table table}.
   */
  List<CreateStatement> triggers(final $Table table) {
    return new ArrayList<>();
  }

  List<CreateStatement> indexes(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<>();
    if (table.getIndexes() != null) {
      for (final $Table.Indexes.Index index : table.getIndexes().getIndex()) {
        final CreateStatement createIndex = createIndex(index.getUnique$() != null && index.getUnique$().text(), SQLDataTypes.getIndexName(table, index), index.getType$(), table.getName$().text(), index.getColumn().toArray(new $Named[index.getColumn().size()]));
        if (createIndex != null)
          statements.add(createIndex);
      }
    }

    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column.getIndex() != null) {
          final CreateStatement createIndex = createIndex(column.getIndex().getUnique$() != null && column.getIndex().getUnique$().text(), SQLDataTypes.getIndexName(table, column.getIndex(), column), column.getIndex().getType$(), table.getName$().text(), column);
          if (createIndex != null)
            statements.add(createIndex);
        }
      }
    }

    return statements;
  }

  /**
   * Returns a list of {@link CreateStatement} objects for the creation of types
   * for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @return A list of {@link CreateStatement} objects for the creation of types
   *         for the specified {@link $Table}.
   */
  List<CreateStatement> types(final $Table table) {
    return new ArrayList<>();
  }

  abstract String dropIndexOnClause($Table table);

  final LinkedHashSet<DropStatement> dropTable(final $Table table) {
    final LinkedHashSet<DropStatement> statements = new LinkedHashSet<>();
    // FIXME: Explicitly dropping indexes on tables that may not exist will throw errors!
//    if (table.getIndexes() != null)
//      for (final $Table.getIndexes.getIndex index : table.getIndexes(0).getIndex())
//        statements.add(dropIndexIfExists(SQLDataTypes.getIndexName(table, index) + dropIndexOnClause(table)));

//    if (table.getColumn() != null)
//      for (final $Column column : table.getColumn())
//        if (column.getIndex() != null)
//          statements.add(dropIndexIfExists(SQLDataTypes.getIndexName(table, column.getIndex(0), column) + dropIndexOnClause(table)));

    if (table.getTriggers() != null)
      for (final $Table.Triggers.Trigger trigger : table.getTriggers().getTrigger())
        for (final String action : trigger.getActions$().text())
          statements.add(new DropStatement("DROP TRIGGER IF EXISTS " + q(SQLDataTypes.getTriggerName(table.getName$().text(), trigger, action))));

    final DropStatement dropTable = dropTableIfExists(table);
    if (dropTable != null)
      statements.add(dropTable);

    return statements;
  }

  /**
   * Returns a list of {@link DropStatement} objects for the dropping of types
   * for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @return A list of {@link DropStatement} objects for the dropping of types
   *         for the specified {@link $Table}.
   */
  LinkedHashSet<DropStatement> dropTypes(final $Table table) {
    return new LinkedHashSet<>();
  }

  DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("DROP TABLE IF EXISTS " + q(table.getName$().text()));
  }

  DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("DROP INDEX IF EXISTS " + q(indexName));
  }

  private static void checkNumericDefault(final DBVendor vendor, final $Column type, final Number defaultValue, final boolean positive, final Integer precision, final boolean unsigned) {
    if (!positive && unsigned)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is negative, but type is declared UNSIGNED");

    if (type instanceof $Bigint) {
      final BigInteger maxValue = vendor.getDialect().allowsUnsignedNumeric() ? BigInteger.valueOf(2).pow(8 * 8) : BigInteger.valueOf(2).pow(8 * 8).divide(BigInteger.valueOf(2));
      if (((BigInteger)defaultValue).compareTo(maxValue) >= 0)
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is larger than the maximum value of " + maxValue.subtract(BigInteger.ONE) + " allowed by " + vendor);
    }
    else if (type instanceof $Decimal) {
      final BigDecimal defaultDecimal = (BigDecimal)defaultValue;
      if (defaultDecimal.precision() > precision)
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is longer than declared PRECISION " + precision);
    }
  }

  String $default(final $Column column) {
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      if (type.getDefault$() == null)
        return null;

      if (type.getDefault$().text().length() > type.getLength$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column.getName$().text() + "' DEFAULT '" + type.getDefault$().text() + "' is longer than declared LENGTH(" + type.getLength$().text() + ")");

      return "'" + type.getDefault$().text() + "'";
    }

    if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      if (type.getDefault$() == null)
        return null;

      if (type.getDefault$().text().getBytes().length > type.getLength$().text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column.getName$().text() + "' DEFAULT '" + type.getDefault$().text() + "' is longer than declared LENGTH " + type.getLength$().text());

      return compileBinary(type.getDefault$().text().toString());
    }

    if (column instanceof $Integer) {
      final BigInteger _default;
      final Byte precision;
      final boolean unsigned;
      if (column instanceof $Tinyint) {
        final $Tinyint type = ($Tinyint)column;
        _default = type.getDefault$() == null ? null : BigInteger.valueOf(type.getDefault$().text());
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        unsigned = type.getUnsigned$() != null && type.getUnsigned$().text();
      }
      else if (column instanceof $Smallint) {
        final $Smallint type = ($Smallint)column;
        _default = type.getDefault$() == null ? null : BigInteger.valueOf(type.getDefault$().text());
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        unsigned = type.getUnsigned$() != null && type.getUnsigned$().text();
      }
      else if (column instanceof $Int) {
        final $Int type = ($Int)column;
        _default = type.getDefault$() == null ? null : BigInteger.valueOf(type.getDefault$().text());
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        unsigned = type.getUnsigned$() != null && type.getUnsigned$().text();
      }
      else if (column instanceof $Bigint) {
        final $Bigint type = ($Bigint)column;
        _default = type.getDefault$() == null ? null : type.getDefault$().text();
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        unsigned = type.getUnsigned$() != null && type.getUnsigned$().text();
      }
      else {
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
      }

      if (_default == null)
        return null;

      if (precision != null)
        checkNumericDefault(getVendor(), column, _default, _default.compareTo(BigInteger.ZERO) >= 0, precision.intValue(), unsigned);

      return String.valueOf(_default);
    }

    if (column instanceof $Float) {
      final $Float type = ($Float)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(getVendor(), type, type.getDefault$().text(), type.getDefault$().text() > 0, null, type.getUnsigned$().text());
      return type.getDefault$().text().toString();
    }

    if (column instanceof $Double) {
      final $Double type = ($Double)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(getVendor(), type, type.getDefault$().text(), type.getDefault$().text() > 0, null, type.getUnsigned$().text());
      return type.getDefault$().text().toString();
    }

    if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(getVendor(), type, type.getDefault$().text(), type.getDefault$().text().doubleValue() > 0, type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getUnsigned$().text());
      return type.getDefault$().text().toString();
    }

    if (column instanceof $Date) {
      final $Date type = ($Date)column;
      return type.getDefault$() == null ? null : compileDate(type.getDefault$().text());
    }

    if (column instanceof $Time) {
      final $Time type = ($Time)column;
      return type.getDefault$() == null ? null : compileTime(type.getDefault$().text());
    }

    if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      return type.getDefault$() == null ? null : compileDateTime(type.getDefault$().text());
    }

    if (column instanceof $Boolean) {
      final $Boolean type = ($Boolean)column;
      return type.getDefault$() == null ? null : type.getDefault$().text().toString();
    }

    if (column instanceof $Enum) {
      final $Enum type = ($Enum)column;
      return type.getDefault$() == null ? null : "'" + type.getDefault$().text() + "'";
    }

    if (column instanceof $Clob || column instanceof $Blob)
      return null;

    throw new UnsupportedOperationException("Unknown type: " + column.getClass().getName());
  }

  String truncate(final String tableName) {
    return "DELETE FROM " + q(tableName);
  }

  abstract String $null($Table table, $Column column);
  abstract String $autoIncrement($Table table, $Integer column);

  String compileBinary(final String value) {
    return "X'" + value + "'";
  }

  String compileDate(final String value) {
    return "'" + value + "'";
  }

  String compileDateTime(final String value) {
    return "'" + value + "'";
  }

  String compileTime(final String value) {
    return "'" + value + "'";
  }
}