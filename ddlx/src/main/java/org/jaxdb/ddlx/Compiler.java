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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jaxdb.ddlx.Generator.ColumnRef;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorBase;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnDelete$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnUpdate$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.libj.lang.Numbers;
import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;
import org.libj.util.ArrayUtil;
import org.libj.util.function.Throwing;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

abstract class Compiler extends DBVendorBase {
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

  @SuppressWarnings("rawtypes")
  static String getAttr(final String name, final $Integer column) {
    final Iterator<? extends $AnySimpleType> attributeIterator = column.attributeIterator();
    while (attributeIterator.hasNext()) {
      final $AnySimpleType<?> attr = attributeIterator.next();
      if (name.equals(attr.name().getLocalPart()))
        return String.valueOf(attr.text());
    }

    return null;
  }

  protected Compiler(final DBVendor vendor) {
    super(vendor);
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

  CreateStatement createTableIfNotExists(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final Map<String,ColumnRef> columnNameToColumn, final Map<String,Map<String,String>> tableNameToEnumToOwner) throws GeneratorExecutionException {
    final StringBuilder builder = new StringBuilder();
    final String tableName = table.getName$().text();
    builder.append("CREATE TABLE ").append(q(tableName)).append(" (\n");
    if (table.getColumn() != null)
      builder.append(createColumns(alterStatements, table, tableNameToEnumToOwner));

    final CreateStatement constraints = createConstraints(columnNameToColumn, table);
    if (constraints != null)
      builder.append(constraints);

    builder.append("\n)");
    return new CreateStatement(builder.toString());
  }

  private String createColumns(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final StringBuilder builder = new StringBuilder();
    final Iterator<$Column> iterator = table.getColumn().iterator();
    $Column column = null;
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0) {
        builder.append(',');
        if (column != null && column.getDocumentation() != null)
          builder.append(" -- ").append(column.getDocumentation().text().replace('\n', ' '));

        builder.append('\n');
      }

      builder.append("  ").append(createColumn(alterStatements, table, column = iterator.next(), tableNameToEnumToOwner));
    }

    return builder.toString();
  }

  private CreateStatement createColumn(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Column column, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final StringBuilder builder = new StringBuilder();
    builder.append(q(column.getName$().text())).append(' ');
    // FIXME: Passing null to compile*() methods will throw a NPE
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      builder.append(getDialect().compileChar(type.getVarying$() != null && type.getVarying$().text(), type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      builder.append(getDialect().compileBinary(type.getVarying$() != null && type.getVarying$().text(), type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Blob) {
      final $Blob type = ($Blob)column;
      builder.append(getDialect().compileBlob(type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Clob) {
      final $Clob type = ($Clob)column;
      builder.append(getDialect().compileClob(type.getLength$() == null ? null : type.getLength$().text()));
    }
    else if (column instanceof $Integer) {
      builder.append(createIntegerColumn(($Integer)column));
    }
    else if (column instanceof $Float) {
      final $Float type = ($Float)column;
      builder.append(getDialect().declareFloat(type.getMin$() == null ? null : type.getMin$().text()));
    }
    else if (column instanceof $Double) {
      final $Double type = ($Double)column;
      builder.append(getDialect().declareDouble(type.getMin$() == null ? null : type.getMin$().text()));
    }
    else if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      builder.append(getDialect().declareDecimal(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getScale$() == null ? null : type.getScale$().text(), type.getMin$() == null ? null : type.getMin$().text()));
    }
    else if (column instanceof $Date) {
      builder.append(getDialect().declareDate());
    }
    else if (column instanceof $Time) {
      final $Time type = ($Time)column;
      builder.append(getDialect().declareTime(type.getPrecision$() == null ? null : type.getPrecision$().text()));
    }
    else if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      builder.append(getDialect().declareDateTime(type.getPrecision$() == null ? null : type.getPrecision$().text()));
    }
    else if (column instanceof $Boolean) {
      builder.append(getDialect().declareBoolean());
    }
    else if (column instanceof $Enum) {
      builder.append(getDialect().declareEnum(($Enum)column, tableNameToEnumToOwner));
    }

    final String autoIncrementFragment = column instanceof $Integer ? $autoIncrement(alterStatements, table, ($Integer)column) : null;
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
      return getDialect().compileInt8(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getMin$() == null ? null : type.getMin$().text());
    }

    if (column instanceof $Smallint) {
      final $Smallint type = ($Smallint)column;
      return getDialect().compileInt16(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getMin$() == null ? null : type.getMin$().text());
    }

    if (column instanceof $Int) {
      final $Int type = ($Int)column;
      return getDialect().compileInt32(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getMin$() == null ? null : type.getMin$().text());
    }

    if (column instanceof $Bigint) {
      final $Bigint type = ($Bigint)column;
      return getDialect().compileInt64(type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getMin$() == null ? null : type.getMin$().text());
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

  static final class Operator {
    static final Operator EQ;
    static final Operator GT;
    static final Operator GTE;
    static final Operator LT;
    static final Operator LTE;
    static final Operator NE;

    private static byte index = 0;

    private static final Operator[] values = {
      EQ = new Operator("=", "eq"),
      GT = new Operator(">", "gt"),
      GTE = new Operator(">=", "gte"),
      LT = new Operator("<", "lt"),
      LTE = new Operator("<=", "lte"),
      NE = new Operator("!=", "ne")
    };

    private static final String[] keys = new String[values.length];

    static {
      for (int i = 0; i < keys.length; ++i)
        keys[i] = values[i].desc;
    }

    static Operator valueOf(final String key) {
      final int index = Arrays.binarySearch(keys, key);
      return index < 0 ? null : values[index];
    }

    static Operator[] values() {
      return values;
    }

    private final byte ordinal;
    final String symbol;
    final String desc;

    private Operator(final String symbol, final String desc) {
      this.ordinal = index++;
      this.symbol = symbol;
      this.desc = desc;
    }

    public byte ordinal() {
      return ordinal;
    }
  }

  private CreateStatement createConstraints(final Map<String,ColumnRef> columnNameToColumn, final $Table table) throws GeneratorExecutionException {
    final StringBuilder constraintsBuilder = new StringBuilder();
    if (table.getConstraints() != null) {
      final $Constraints constraints = table.getConstraints();

      // unique constraint
      final List<$Columns> uniques = constraints.getUnique();
      if (uniques != null) {
        final StringBuilder uniqueString = new StringBuilder();
        final StringBuilder uniqueBuilder = new StringBuilder();
        for (final $Columns unique : uniques) {
          final List<$Named> columns = unique.getColumn();
          final int[] columnIndexes = new int[columns.size()];
          for (int i = 0, len = columns.size(); i < len; ++i) {
            if (i > 0)
              uniqueBuilder.append(", ");

            final String columnName = columns.get(i).getName$().text();
            uniqueBuilder.append(q(columnName));
            columnIndexes[i] = columnNameToColumn.get(columnName).index;
          }

          uniqueString.append(",\n  CONSTRAINT ").append(q(getConstraintName("uq", table, null, columnIndexes))).append(" UNIQUE (").append(uniqueBuilder).append(')');
          uniqueBuilder.setLength(0);
        }

        constraintsBuilder.append(uniqueString);
      }

      // check constraint
      final List<$CheckReference> checks = constraints.getCheck();
      if (checks != null) {
        final StringBuilder checkBuilder = new StringBuilder();
        for (final $CheckReference check : checks) {
          final String checkRule = recurseCheckRule(check);
          final String checkClause = checkRule.startsWith("(") ? checkRule : "(" + checkRule + ")";
          checkBuilder.append(",\n  CONSTRAINT ").append(q(getConstraintName("ck", new StringBuilder(hash(table.getName$().text() + checkClause))))).append(" CHECK ").append(checkClause);
        }

        constraintsBuilder.append(checkBuilder);
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
          final int[] columnIndexes = new int[columns.size()];
          final String[] foreignKeyColumns = new String[columns.size()];
          final String[] foreignKeyReferences = new String[columns.size()];
          for (int i = 0, len = columns.size(); i < len; ++i) {
            final $ForeignKeyComposite.Column column = columns.get(i);
            final String columnName = column.getName$().text();
            foreignKeyColumns[i] = q(columnName);
            foreignKeyReferences[i] = q(column.getReferences$().text());
            columnIndexes[i] = columnNameToColumn.get(columnName).index;
          }

          constraintsBuilder.append(",\n  ").append(foreignKey(table, foreignKeyComposite.getReferences$(), columnIndexes)).append(" (").append(ArrayUtil.toString(foreignKeyColumns, ", "));
          constraintsBuilder.append(") REFERENCES ").append(q(foreignKeyComposite.getReferences$().text()));
          constraintsBuilder.append(" (").append(ArrayUtil.toString(foreignKeyReferences, ", ")).append(')');
          appendOnDeleteOnUpdate(constraintsBuilder, foreignKeyComposite);
        }
      }
    }

    if (table.getColumn() != null) {
      final List<$Column> columns = table.getColumn();
      for (int c = 0, len = columns.size(); c < len; ++c) {
        final $Column column = columns.get(c);
        final $ForeignKeyUnary foreignKey = column.getForeignKey();
        if (foreignKey != null) {
          constraintsBuilder.append(",\n  ").append(foreignKey(table, foreignKey.getReferences$(), c)).append(" (").append(q(column.getName$().text()));
          constraintsBuilder.append(") REFERENCES ").append(q(foreignKey.getReferences$().text()));
          constraintsBuilder.append(" (").append(q(foreignKey.getColumn$().text())).append(')');

          appendOnDeleteOnUpdate(constraintsBuilder, foreignKey);
        }
      }

      // Parse the min & max constraints of numeric types
      for (int c = 0, len = columns.size(); c < len; ++c) {
        final $Column column = columns.get(c);
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

        final String minCheckExp = minCheck == null ? null : q(column.getName$().text()) + " >= " + minCheck;
        final String maxCheckExp = maxCheck == null ? null : q(column.getName$().text()) + " <= " + maxCheck;

        if (minCheck != null) {
          if (maxCheck != null)
            constraintsBuilder.append(",\n  ").append(check(table, c, Operator.GTE, minCheck, Operator.LTE, maxCheck)).append(" (").append(minCheckExp).append(" AND ").append(maxCheckExp).append(')');
          else
            constraintsBuilder.append(",\n  ").append(check(table, c, Operator.GTE, minCheck, null, null)).append(" (").append(minCheckExp).append(')');
        }
        else if (maxCheck != null) {
          constraintsBuilder.append(",\n  ").append(check(table, c, Operator.LTE, maxCheck, null, null)).append(" (").append(maxCheckExp).append(')');
        }
      }

      // parse the <check/> element per type
      for (int c = 0, len = columns.size(); c < len; ++c) {
        final $Column column = columns.get(c);
        Operator operator = null;
        String condition = null;
        if (column instanceof $Char) {
          final $Char type = ($Char)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = "'" + type.getCheck().getValue$().text() + "'";
          }
        }
        else if (column instanceof $Tinyint) {
          final $Tinyint type = ($Tinyint)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Smallint) {
          final $Smallint type = ($Smallint)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Int) {
          final $Int type = ($Int)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Bigint) {
          final $Bigint type = ($Bigint)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Float) {
          final $Float type = ($Float)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Double) {
          final $Double type = ($Double)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }
        else if (column instanceof $Decimal) {
          final $Decimal type = ($Decimal)column;
          if (type.getCheck() != null) {
            operator = Operator.valueOf(type.getCheck().getOperator$().text());
            condition = String.valueOf(type.getCheck().getValue$().text());
          }
        }

        if (operator != null) {
          if (condition != null)
            constraintsBuilder.append(",\n  ").append(check(table, c, operator, condition, null, null)).append(" (").append(q(column.getName$().text())).append(' ').append(operator.symbol).append(' ').append(condition).append(')');
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

  String blockPrimaryKey(final $Table table, final $Constraints constraints, final Map<String,ColumnRef> columnNameToColumn) throws GeneratorExecutionException {
    if (constraints.getPrimaryKey() == null)
      return "";

    final StringBuilder builder = new StringBuilder();
    final List<$Named> columns = constraints.getPrimaryKey().getColumn();
    final int[] columnIndexes = new int[columns.size()];
    final Iterator<$Named> iterator = columns.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final $Named primaryColumn = iterator.next();
      final String primaryKeyColumn = primaryColumn.getName$().text();
      final ColumnRef ref = columnNameToColumn.get(primaryKeyColumn);
      if (ref == null)
        throw new GeneratorExecutionException("PRIMARY KEY column " + table.getName$().text() + "." + primaryKeyColumn + " is not defined");

      if (ref.column.getNull$() == null || ref.column.getNull$().text())
        throw new GeneratorExecutionException("Column " + ref.column.getName$() + " must be NOT NULL to be a PRIMARY KEY");

      if (i > 0)
        builder.append(", ");

      builder.append(q(primaryKeyColumn));
      columnIndexes[i] = ref.index;
    }

    return ",\n  " + primaryKey(table, columnIndexes) + " (" + builder + ")";
  }

  /**
   * Returns the "FOREIGN KEY" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @param references The optional "references" qualifier.
   * @param columns The indexes of the columns comprising the "FOREIGN KEY".
   * @return The "FOREIGN KEY" keyword for the specified {@link $Table}.
   */
  String foreignKey(final $Table table, final $ForeignKey.References$ references, final int ... columns) {
    return "CONSTRAINT " + q(getConstraintName("fk", table, references, columns)) + " FOREIGN KEY";
  }

  /**
   * Returns the "PRIMARY KEY" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @param columns The indexes of the columns comprising the "PRIMARY KEY".
   * @return The "PRIMARY KEY" keyword for the specified {@link $Table}.
   */
  String primaryKey(final $Table table, final int[] columns) {
    return "CONSTRAINT " + q(getConstraintName("pk", table, null, columns)) + " PRIMARY KEY";
  }

  /**
   * Returns the "CHECK" keyword for the specified {@link $Table}.
   *
   * @param table The {@link $Table}.
   * @param column The index of the column on which the "CHECK" is to be
   *          declared.
   * @param operator1 The first {@link Operator} of the constraint.
   * @param arg1 The first argument of the constraint.
   * @param operator2 The second {@link Operator} of the constraint.
   * @param arg2 The second argument of the constraint.
   * @return The "CHECK" keyword for the specified {@link $Table}.
   */
  String check(final $Table table, final int column, final Operator operator1, final String arg1, final Operator operator2, final String arg2) {
    final StringBuilder builder = getConstraintName(table, column);
    builder.append('_').append(operator1.desc).append('_').append(arg1);
    if (operator2 != null)
      builder.append('_').append(operator2.desc).append('_').append(arg2);

    return "CONSTRAINT " + q(getConstraintName("ck", builder)) + " CHECK";
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

  private String recurseCheckRule(final $CheckReference check) {
    final Operator operator = Operator.valueOf(check.getOperator$().text());
    final String condition = Numbers.isNumber(check.getValue$().text()) ? Numbers.stripTrailingZeros(check.getValue$().text()) : "'" + check.getValue$().text() + "'";
    final String clause = q(check.getColumn$().text()) + " " + operator.symbol + " " + condition;
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

  List<CreateStatement> indexes(final $Table table, final Map<String,ColumnRef> columnNameToColumn) {
    final List<CreateStatement> statements = new ArrayList<>();
    if (table.getIndexes() != null) {
      for (final $Table.Indexes.Index index : table.getIndexes().getIndex()) {
        final List<$Named> columns = index.getColumn();
        final int[] columnIndexes = new int[columns.size()];
        for (int c = 0, len = columns.size(); c < len; ++c) {
          final $Named column = columns.get(c);
          columnIndexes[c] = columnNameToColumn.get(column.getName$().text()).index;
        }

        final CreateStatement createIndex = createIndex(index.getUnique$() != null && index.getUnique$().text(), getIndexName(table, index, columnIndexes), index.getType$(), table.getName$().text(), index.getColumn().toArray(new $Named[index.getColumn().size()]));
        if (createIndex != null)
          statements.add(createIndex);
      }
    }

    if (table.getColumn() != null) {
      final List<$Column> columns = table.getColumn();
      for (int c = 0, len = columns.size(); c < len; ++c) {
        final $Column column = columns.get(c);
        if (column.getIndex() != null) {
          final CreateStatement createIndex = createIndex(column.getIndex().getUnique$() != null && column.getIndex().getUnique$().text(), getIndexName(table, column.getIndex(), c), column.getIndex().getType$(), table.getName$().text(), column);
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
  List<CreateStatement> types(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    return new ArrayList<>();
  }

  abstract String dropIndexOnClause($Table table);

  final LinkedHashSet<DropStatement> dropTable(final $Table table) {
    final LinkedHashSet<DropStatement> statements = new LinkedHashSet<>();
    // FIXME: Explicitly dropping indexes on tables that may not exist will throw errors!
//    if (table.getIndexes() != null)
//      for (final $Table.getIndexes.getIndex index : table.getIndexes(0).getIndex())
//        statements.add(dropIndexIfExists(getIndexName(table, index, ???) + dropIndexOnClause(table)));

//    if (table.getColumn() != null)
//      for (final $Column column : table.getColumn())
//        if (column.getIndex() != null)
//          statements.add(dropIndexIfExists(getIndexName(table, column.getIndex(0), column) + dropIndexOnClause(table)));

    // FIXME: Explicitly dropping triggers on tables that may not exist will throw errors!
//    if (table.getTriggers() != null)
//      for (final $Table.Triggers.Trigger trigger : table.getTriggers().getTrigger())
//        for (final String action : trigger.getActions$().text())
//          statements.add(new DropStatement("DROP TRIGGER IF EXISTS " + q(getTriggerName(table.getName$().text(), trigger, action)) + " ON " + q(table.getName$().text())));

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
  LinkedHashSet<DropStatement> dropTypes(final $Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    return new LinkedHashSet<>();
  }

  DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("DROP TABLE IF EXISTS " + q(table.getName$().text()));
  }

  DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("DROP INDEX IF EXISTS " + q(indexName));
  }

  private static void checkNumericDefault(final $Column type, final Integer precision, final Number defaultValue, final Number min, final Number max) {
    if (defaultValue == null)
      return;

    if (min != null && Numbers.compare(defaultValue, min) < 0)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is less than the declared min=\"" + min + "\"");

    if (max != null && Numbers.compare(defaultValue, max) > 0)
      throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is greater than the declared max=\"" + max + "\"");

    if (type instanceof $Decimal) {
      final BigDecimal defaultDecimal = (BigDecimal)defaultValue;
      if (defaultDecimal.precision() > precision)
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + type.getName$().text() + "' DEFAULT " + defaultValue + " is longer than declared PRECISION " + precision);
    }
  }

  Byte getPrecision(final $Integer column) {
    if (column instanceof $Tinyint) {
      final $Tinyint type = ($Tinyint)column;
      return type.getPrecision$() == null ? null : type.getPrecision$().text();
    }

    if (column instanceof $Smallint) {
      final $Smallint type = ($Smallint)column;
      return type.getPrecision$() == null ? null : type.getPrecision$().text();
    }

    if (column instanceof $Int) {
      final $Int type = ($Int)column;
      return type.getPrecision$() == null ? null : type.getPrecision$().text();
    }

    if (column instanceof $Bigint) {
      final $Bigint type = ($Bigint)column;
      return type.getPrecision$() == null ? null : type.getPrecision$().text();
    }

    throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
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
      final Number _default;
      final Byte precision;
      final Number min;
      final Number max;
      if (column instanceof $Tinyint) {
        final $Tinyint type = ($Tinyint)column;
        _default = type.getDefault$() == null ? null : type.getDefault$().text();
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        min = type.getMin$() == null ? null : type.getMin$().text();
        max = type.getMax$() == null ? null : type.getMax$().text();
      }
      else if (column instanceof $Smallint) {
        final $Smallint type = ($Smallint)column;
        _default = type.getDefault$() == null ? null : type.getDefault$().text();
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        min = type.getMin$() == null ? null : type.getMin$().text();
        max = type.getMax$() == null ? null : type.getMax$().text();
      }
      else if (column instanceof $Int) {
        final $Int type = ($Int)column;
        _default = type.getDefault$() == null ? null : type.getDefault$().text();
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        min = type.getMin$() == null ? null : type.getMin$().text();
        max = type.getMax$() == null ? null : type.getMax$().text();
      }
      else if (column instanceof $Bigint) {
        final $Bigint type = ($Bigint)column;
        _default = type.getDefault$() == null ? null : type.getDefault$().text();
        precision = type.getPrecision$() == null ? null : type.getPrecision$().text();
        min = type.getMin$() == null ? null : type.getMin$().text();
        max = type.getMax$() == null ? null : type.getMax$().text();
      }
      else {
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
      }

      if (_default == null)
        return null;

      checkNumericDefault(column, precision == null ? null : Integer.valueOf(precision), _default, min, max);
      return String.valueOf(_default);
    }

    if (column instanceof $Float) {
      final $Float type = ($Float)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(type, null, type.getDefault$().text(), type.getMin$() == null ? null : type.getMin$().text(), type.getMax$() == null ? null : type.getMax$().text());
      return type.getDefault$().text().toString();
    }

    if (column instanceof $Double) {
      final $Double type = ($Double)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(type, null, type.getDefault$().text(), type.getMin$() == null ? null : type.getMin$().text(), type.getMax$() == null ? null : type.getMax$().text());
      return type.getDefault$().text().toString();
    }

    if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      if (type.getDefault$() == null)
        return null;

      checkNumericDefault(type, type.getPrecision$() == null ? null : type.getPrecision$().text(), type.getDefault$().text(), type.getMin$() == null ? null : type.getMin$().text(), type.getMax$() == null ? null : type.getMax$().text());
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
  abstract String $autoIncrement(LinkedHashSet<CreateStatement> alterStatements, $Table table, $Integer column);

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