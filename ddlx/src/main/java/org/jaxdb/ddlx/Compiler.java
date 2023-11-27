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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jaxdb.ddlx.Generator.ColumnRef;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.DbVendorCompiler;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$BigintCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$CharCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Check;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ColumnIndex;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$DecimalCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$DoubleCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$FloatCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary.References$;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexesIndex;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IntCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$SmallintCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$TinyintCheck;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema.Table;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema.Table.Constraints.PrimaryKey;
import org.jaxsb.runtime.BindingList;
import org.libj.lang.Numbers;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$String;

abstract class Compiler extends DbVendorCompiler {
  private static final Compiler[] compilers = {new DB2Compiler(), new DerbyCompiler(), new MariaDBCompiler(), new MySQLCompiler(), new OracleCompiler(), new PostgreSQLCompiler(), new SQLiteCompiler()};

  static Compiler getCompiler(final DbVendor vendor) {
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

  protected Compiler(final DbVendor vendor) {
    super(vendor);
  }

  abstract CreateStatement createIndex(boolean unique, String indexName, $IndexType type, String tableName, $Named ... columns);

  abstract void init(Connection connection) throws SQLException;

  /**
   * Create a "SchemaIfNotExists" {@link CreateStatement} for the specified {@link Schema}.
   *
   * @param schema The {@link Schema}.
   * @return A "SchemaIfNotExists" {@link CreateStatement} for the specified {@link Schema}.
   */
  CreateStatement createSchemaIfNotExists(final Schema schema) {
    return null;
  }

  CreateStatement createTableIfNotExists(final LinkedHashSet<CreateStatement> alterStatements, final Table table, final Map<String,String> enumTemplateToValues, final Map<String,ColumnRef> columnNameToColumn, final Map<String,Map<String,String>> tableNameToEnumToOwner) throws GeneratorExecutionException {
    final StringBuilder b = new StringBuilder();
    final String tableName = table.getName$().text();
    b.append("CREATE TABLE ");
    q(b, tableName).append(" (\n");
    if (table.getColumn() != null)
      b.append(createColumns(alterStatements, table, enumTemplateToValues, tableNameToEnumToOwner));

    final CreateStatement constraints = createConstraints(columnNameToColumn, table);
    if (constraints != null)
      b.append(constraints);

    b.append("\n)");
    return new CreateStatement(b.toString());
  }

  private String createColumns(final LinkedHashSet<CreateStatement> alterStatements, final Table table, final Map<String,String> enumTemplateToValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final BindingList<$Column> columns = table.getColumn();
    final int size = columns.size();
    if (size == 0)
      return "";

    final StringBuilder builder = new StringBuilder();
    $Column column = null;
    int i = 0;
    do { // [RA]
      if (i > 0) {
        builder.append(',');
        final $String documentation;
        if (column != null && (documentation = column.getDocumentation()) != null)
          builder.append(" -- ").append(documentation.text().replace('\n', ' '));

        builder.append('\n');
      }

      builder.append("  ").append(createColumn(alterStatements, table, column = columns.get(i), enumTemplateToValues, tableNameToEnumToOwner));
    }
    while (++i < size);
    return builder.toString();
  }

  private CreateStatement createColumn(final LinkedHashSet<CreateStatement> alterStatements, final Table table, final $Column column, final Map<String,String> enumTemplateToValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final StringBuilder b = new StringBuilder();
    q(b, column.getName$().text()).append(' ');
    // FIXME: Passing null to compile*() methods will throw a NPE
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      final $Char.Length$ length$ = type.getLength$();
      getDialect().compileChar(b, type.getVarying$() != null && type.getVarying$().text(), length$ == null ? null : length$.text());
    }
    else if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      final $Binary.Varying$ varying$ = type.getVarying$();
      final $Binary.Length$ length$ = type.getLength$();
      getDialect().compileBinary(b, varying$ != null && varying$.text(), length$ == null ? null : length$.text());
    }
    else if (column instanceof $Blob) {
      final $Blob type = ($Blob)column;
      final $Blob.Length$ length$ = type.getLength$();
      getDialect().compileBlob(b, length$ == null ? null : length$.text());
    }
    else if (column instanceof $Clob) {
      final $Clob type = ($Clob)column;
      final $Clob.Length$ length$ = type.getLength$();
      getDialect().compileClob(b, length$ == null ? null : length$.text());
    }
    else if (column instanceof $Integer) {
      createIntegerColumn(b, ($Integer)column);
    }
    else if (column instanceof $Float) {
      final $Float type = ($Float)column;
      final $Float.Min$ min$ = type.getMin$();
      getDialect().declareFloat(b, min$ == null ? null : min$.text());
    }
    else if (column instanceof $Double) {
      final $Double type = ($Double)column;
      final $Double.Min$ min$ = type.getMin$();
      getDialect().declareDouble(b, min$ == null ? null : min$.text());
    }
    else if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      final $Decimal.Precision$ precision$ = type.getPrecision$();
      final $Decimal.Scale$ scale$ = type.getScale$();
      final $Decimal.Min$ min$ = type.getMin$();
      getDialect().declareDecimal(b, precision$ == null ? null : precision$.text(), scale$ == null ? 0 : scale$.text(), min$ == null ? null : min$.text());
    }
    else if (column instanceof $Date) {
      getDialect().declareDate(b);
    }
    else if (column instanceof $Time) {
      final $Time type = ($Time)column;
      final $Time.Precision$ precision$ = type.getPrecision$();
      getDialect().declareTime(b, precision$ == null ? null : precision$.text());
    }
    else if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      final $Datetime.Precision$ precision$ = type.getPrecision$();
      getDialect().declareDateTime(b, precision$ == null ? null : precision$.text());
    }
    else if (column instanceof $Boolean) {
      getDialect().declareBoolean(b);
    }
    else if (column instanceof $Enum) {
      final $Enum enumColumn = ($Enum)column;
      final $Enum.Values$ values$ = enumColumn.getValues$();
      getDialect().declareEnum(b, enumColumn, Objects.requireNonNull(values$ != null ? values$.text() : enumTemplateToValues.get(enumColumn.getTemplate$().text())), tableNameToEnumToOwner);
    }

    final String autoIncrementFragment = column instanceof $Integer ? $autoIncrement(alterStatements, table, ($Integer)column) : null;
    if (autoIncrementFragment == null)
      $default(b, column);

    $null(b, table, column);

    if (autoIncrementFragment != null)
      b.append(' ').append(autoIncrementFragment);

    return new CreateStatement(b.toString());
  }

  StringBuilder createIntegerColumn(final StringBuilder b, final $Integer column) {
    if (column instanceof $Tinyint) {
      final $Tinyint type = ($Tinyint)column;
      final $Tinyint.Precision$ precision$ = type.getPrecision$();
      final $Tinyint.Min$ min$ = type.getMin$();
      return getDialect().compileInt8(b, precision$ == null ? null : precision$.text(), min$ == null ? null : min$.text());
    }

    if (column instanceof $Smallint) {
      final $Smallint type = ($Smallint)column;
      final $Smallint.Precision$ precision$ = type.getPrecision$();
      final $Smallint.Min$ min$ = type.getMin$();
      return getDialect().compileInt16(b, precision$ == null ? null : precision$.text(), min$ == null ? null : min$.text());
    }

    if (column instanceof $Int) {
      final $Int type = ($Int)column;
      final $Int.Precision$ precision$ = type.getPrecision$();
      final $Int.Min$ min$ = type.getMin$();
      return getDialect().compileInt32(b, precision$ == null ? null : precision$.text(), min$ == null ? null : min$.text());
    }

    if (column instanceof $Bigint) {
      final $Bigint type = ($Bigint)column;
      final $Bigint.Precision$ precision$ = type.getPrecision$();
      final $Bigint.Min$ min$ = type.getMin$();
      return getDialect().compileInt64(b, precision$ == null ? null : precision$.text(), min$ == null ? null : min$.text());
    }

    throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
  }

  private void appendOnDeleteOnUpdate(final StringBuilder constraintsBuilder, final $ChangeRule onDelete, final $ChangeRule onUpdate) {
    // The ON DELETE and ON UPDATE rules must be the same at this point, given previous checks.
    if (onDelete != null) {
      constraintsBuilder.append(' ');
      onDelete(constraintsBuilder, onDelete);
    }

    if (onUpdate != null) {
      constraintsBuilder.append(' ');
      onUpdate(constraintsBuilder, onUpdate);
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
      for (int i = 0, i$ = keys.length; i < i$; ++i) // [A]
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

  private CreateStatement createConstraints(final Map<String,ColumnRef> columnNameToColumn, final Table table) throws GeneratorExecutionException {
    final StringBuilder constraintsBuilder = new StringBuilder();
    if (table.getConstraints() != null) {
      final Table.Constraints constraints = table.getConstraints();

      // UNIQUE constraint
      final List<$Columns> uniques = constraints.getUnique();
      final int i$;
      if (uniques != null && (i$ = uniques.size()) > 0) {
        final StringBuilder uniqueString = new StringBuilder();
        final StringBuilder uniqueBuilder = new StringBuilder();
        for (int i = 0; i < i$; ++i) { // [RA]
          final $Columns unique = uniques.get(i);
          final List<? extends $Named> columns = unique.getColumn();
          final int[] columnIndexes = new int[columns.size()];
          for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
            if (j > 0)
              uniqueBuilder.append(", ");

            final String columnName = columns.get(j).getName$().text();
            q(uniqueBuilder, columnName);
            columnIndexes[j] = columnNameToColumn.get(columnName).index;
          }

          uniqueString.append(",\n  CONSTRAINT ");
          q(uniqueString, getConstraintName("uq", table, null, columnIndexes)).append(" UNIQUE (").append(uniqueBuilder).append(')');
          uniqueBuilder.setLength(0);
        }

        constraintsBuilder.append(uniqueString);
      }

      // CHECK constraint
      final List<$CheckReference> checks = constraints.getCheck();
      final int j$;
      if (checks != null && (j$ = checks.size()) > 0) {
        final StringBuilder checkBuilder = new StringBuilder();
        for (int j = 0; j < j$; ++j) { // [RA]
          final $CheckReference check = checks.get(j);
          final StringBuilder checkRule = recurseCheckRule(check);
          if (checkRule.charAt(0) != '(')
            checkRule.insert(0, '(').append(')');

          checkBuilder.append(",\n  CONSTRAINT ");
          q(checkBuilder, getConstraintName("ck", new StringBuilder(hash(table.getName$().text() + checkRule)))).append(" CHECK ").append(checkRule);
        }

        constraintsBuilder.append(checkBuilder);
      }

      // PRIMARY KEY constraint
      blockPrimaryKey(constraintsBuilder, table, DDLx.getPrimaryKey(constraints), columnNameToColumn);

      // FOREIGN KEY constraints
      final List<$ForeignKeyComposite> foreignKeyComposites = constraints.getForeignKey();
      if (foreignKeyComposites != null) {
        for (int k = 0, k$ = foreignKeyComposites.size(); k < k$; ++k) { // [RA]
          final $ForeignKeyComposite foreignKeyComposite = foreignKeyComposites.get(k);
          final List<$ForeignKeyComposite.Column> columns = foreignKeyComposite.getColumn();
          final int[] columnIndexes = new int[columns.size()];
          final StringBuilder foreignKeyColumns = new StringBuilder();
          final StringBuilder foreignKeyReferences = new StringBuilder();
          for (int l = 0, l$ = columns.size(); l < l$; ++l) { // [RA]
            if (l > 0) {
              foreignKeyColumns.append(", ");
              foreignKeyReferences.append(", ");
            }

            final $ForeignKeyComposite.Column column = columns.get(l);
            final String columnName = column.getName$().text();
            q(foreignKeyColumns, columnName);
            q(foreignKeyReferences, column.getReferences$().text());
            columnIndexes[l] = columnNameToColumn.get(columnName).index;
          }

          constraintsBuilder.append(",\n  ").append(foreignKey(table, foreignKeyComposite.getReferences$(), columnIndexes)).append(" (").append(foreignKeyColumns);
          constraintsBuilder.append(") REFERENCES ");
          q(constraintsBuilder, foreignKeyComposite.getReferences$().text());
          constraintsBuilder.append(" (").append(foreignKeyReferences).append(')');
          appendOnDeleteOnUpdate(constraintsBuilder, foreignKeyComposite.getOnDelete$(), foreignKeyComposite.getOnUpdate$());
        }
      }
    }

    final List<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int c = 0, i$ = columns.size(); c < i$; ++c) { // [RA]
        final $Column column = columns.get(c);
        final $ForeignKeyUnary foreignKey = DDLx.getForeignKey(column);
        if (foreignKey != null) {
          final References$ references$ = foreignKey.getReferences$();
          constraintsBuilder.append(",\n  ").append(foreignKey(table, references$, c)).append(" (");
          q(constraintsBuilder, column.getName$().text());
          constraintsBuilder.append(") REFERENCES ");
          q(constraintsBuilder, references$.text());
          constraintsBuilder.append(" (");
          q(constraintsBuilder, foreignKey.getColumn$().text()).append(')');

          appendOnDeleteOnUpdate(constraintsBuilder, foreignKey.getOnDelete$(), foreignKey.getOnUpdate$());
        }
      }

      // Parse the min & max constraints of numeric types
      for (int c = 0, i$ = columns.size(); c < i$; ++c) { // [RA]
        final $Column column = columns.get(c);
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $Integer) {
          if (column instanceof $Tinyint) {
            final $Tinyint type = ($Tinyint)column;
            final $Tinyint.Min$ min$ = type.getMin$();
            minCheck = min$ != null ? String.valueOf(min$.text()) : null;
            final $Tinyint.Max$ max$ = type.getMax$();
            maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
          }
          else if (column instanceof $Smallint) {
            final $Smallint type = ($Smallint)column;
            final $Smallint.Min$ min$ = type.getMin$();
            minCheck = min$ != null ? String.valueOf(min$.text()) : null;
            final $Smallint.Max$ max$ = type.getMax$();
            maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
          }
          else if (column instanceof $Int) {
            final $Int type = ($Int)column;
            final $Int.Min$ min$ = type.getMin$();
            minCheck = min$ != null ? String.valueOf(min$.text()) : null;
            final $Int.Max$ max$ = type.getMax$();
            maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
          }
          else if (column instanceof $Bigint) {
            final $Bigint type = ($Bigint)column;
            final $Bigint.Min$ min$ = type.getMin$();
            minCheck = min$ != null ? String.valueOf(min$.text()) : null;
            final $Bigint.Max$ max$ = type.getMax$();
            maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
          }
          else {
            throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
          }
        }
        else if (column instanceof $Float) {
          final $Float type = ($Float)column;
          final $Float.Min$ min$ = type.getMin$();
          minCheck = min$ != null ? String.valueOf(min$.text()) : null;
          final $Float.Max$ max$ = type.getMax$();
          maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
        }
        else if (column instanceof $Double) {
          final $Double type = ($Double)column;
          final $Double.Min$ min$ = type.getMin$();
          minCheck = min$ != null ? String.valueOf(min$.text()) : null;
          final $Double.Max$ max$ = type.getMax$();
          maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
        }
        else if (column instanceof $Decimal) {
          final $Decimal type = ($Decimal)column;
          final $Decimal.Min$ min$ = type.getMin$();
          minCheck = min$ != null ? String.valueOf(min$.text()) : null;
          final $Decimal.Max$ max$ = type.getMax$();
          maxCheck = max$ != null ? String.valueOf(max$.text()) : null;
        }

        final $Named.Name$ name$ = column.getName$();
        if (minCheck != null) {
          if (maxCheck != null) {
            constraintsBuilder.append(",\n  ");
            check(constraintsBuilder, table, c, Operator.GTE, minCheck, Operator.LTE, maxCheck).append(" (");
            q(constraintsBuilder, name$.text()).append(" >= ").append(minCheck).append(" AND ");
            q(constraintsBuilder, name$.text()).append(" <= ").append(maxCheck).append(')');
          }
          else {
            constraintsBuilder.append(",\n  ");
            check(constraintsBuilder, table, c, Operator.GTE, minCheck, null, null).append(" (");
            q(constraintsBuilder, name$.text()).append(" >= ").append(minCheck).append(')');
          }
        }
        else if (maxCheck != null) {
          constraintsBuilder.append(",\n  ");
          check(constraintsBuilder, table, c, Operator.LTE, maxCheck, null, null).append(" (");
          q(constraintsBuilder, name$.text()).append(" <= ").append(maxCheck).append(')');
        }
      }

      // parse the <check/> element per type
      for (int c = 0, i$ = columns.size(); c < i$; ++c) { // [RA]
        final $Column column = columns.get(c);
        Operator operator = null;
        String condition = null;
        if (column instanceof $Char) {
          final $Char type = ($Char)column;
          final $CharCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = "'" + check.getValue$().text() + "'"; // FIXME: StringBuilder
          }
        }
        else if (column instanceof $Tinyint) {
          final $Tinyint type = ($Tinyint)column;
          final $TinyintCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Smallint) {
          final $Smallint type = ($Smallint)column;
          final $SmallintCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Int) {
          final $Int type = ($Int)column;
          final $IntCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Bigint) {
          final $Bigint type = ($Bigint)column;
          final $BigintCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Float) {
          final $Float type = ($Float)column;
          final $FloatCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Double) {
          final $Double type = ($Double)column;
          final $DoubleCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }
        else if (column instanceof $Decimal) {
          final $Decimal type = ($Decimal)column;
          final $DecimalCheck check = type.getCheck();
          if (check != null) {
            operator = Operator.valueOf(check.getOperator$().text());
            condition = String.valueOf(check.getValue$().text());
          }
        }

        if (operator != null) {
          if (condition != null) {
            constraintsBuilder.append(",\n  ");
            check(constraintsBuilder, table, c, operator, condition, null, null).append(" (");
            q(constraintsBuilder, column.getName$().text()).append(' ').append(operator.symbol).append(' ').append(condition).append(')');
          }
          else {
            throw new UnsupportedOperationException("Unsupported 'null' condition encountered on column '" + column.getName$().text());
          }
        }
        else if (condition != null) {
          throw new UnsupportedOperationException("Unsupported 'null' operator encountered on column '" + column.getName$().text());
        }
      }
    }

    return constraintsBuilder.length() == 0 ? null : new CreateStatement(constraintsBuilder.toString());
  }

  StringBuilder blockPrimaryKey(final StringBuilder b, final Table table, final $PrimaryKey primaryKey, final Map<String,ColumnRef> columnNameToColumn) throws GeneratorExecutionException {
    if (primaryKey == null)
      return b;

    final StringBuilder key = new StringBuilder();
    final List<? extends $Named> columns = primaryKey.getColumn();
    final PrimaryKey.Using$ using = primaryKey.getUsing$();
    final int[] columnIndexes = new int[columns.size()];
    for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
      final $Named primaryColumn = columns.get(i);
      final String primaryKeyColumn = primaryColumn.getName$().text();
      final ColumnRef ref = columnNameToColumn.get(primaryKeyColumn);
      if (ref == null)
        throw new GeneratorExecutionException("PRIMARY KEY column " + table.getName$().text() + "." + primaryKeyColumn + " is not defined");

      if (ref.column.getNull$() == null || ref.column.getNull$().text())
        throw new GeneratorExecutionException("Column " + ref.column.getName$() + " must be NOT NULL to be a PRIMARY KEY");

      if (i > 0)
        key.append(", ");

      q(key, primaryKeyColumn);
      columnIndexes[i] = ref.index;
    }

    b.append(",\n  ");
    return primaryKey(b, table, columnIndexes, using).append(" (").append(key).append(')');
  }

  /**
   * Returns the "FOREIGN KEY" keyword for the specified {@link Table}.
   *
   * @param table The {@link Table}.
   * @param references The optional "references" qualifier.
   * @param columns The indexes of the columns comprising the "FOREIGN KEY".
   * @return The "FOREIGN KEY" keyword for the specified {@link Table}.
   */
  StringBuilder foreignKey(final Table table, final xLygluGCXAA.$Name references, final int ... columns) {
    return q(new StringBuilder("CONSTRAINT "), getConstraintName("fk", table, references, columns)).append(" FOREIGN KEY");
  }

  /**
   * Appends the "PRIMARY KEY" keyword for the specified {@link Table} to the provided {@link StringBuilder}.
   *
   * @param b The {@link StringBuilder}.
   * @param table The {@link Table}.
   * @param columns The indexes of the columns comprising the "PRIMARY KEY".
   * @param using The index type.
   * @return The "PRIMARY KEY" keyword for the specified {@link Table}.
   */
  StringBuilder primaryKey(final StringBuilder b, final Table table, final int[] columns, final PrimaryKey.Using$ using) {
    b.append("CONSTRAINT ");
    q(b, getConstraintName("pk", table, null, columns)).append(" PRIMARY KEY");
    if (using != null)
      b.append(" USING ").append(using.text().toUpperCase());

    return b;
  }

  /**
   * Appends the "CHECK" keyword for the specified {@link Table} to the provided {@link StringBuilder}.
   *
   * @param b The {@link StringBuilder}.
   * @param table The {@link Table}.
   * @param column The index of the column on which the "CHECK" is to be declared.
   * @param operator1 The first {@link Operator} of the constraint.
   * @param arg1 The first argument of the constraint.
   * @param operator2 The second {@link Operator} of the constraint.
   * @param arg2 The second argument of the constraint.
   * @return The "CHECK" keyword for the specified {@link Table}.
   */
  StringBuilder check(final StringBuilder b, final Table table, final int column, final Operator operator1, final String arg1, final Operator operator2, final String arg2) {
    final StringBuilder builder = getConstraintName(table, column);
    builder.append('_').append(operator1.desc).append('_').append(arg1);
    if (operator2 != null)
      builder.append('_').append(operator2.desc).append('_').append(arg2);

    b.append("CONSTRAINT ");
    return q(b, getConstraintName("ck", builder)).append(" CHECK");
  }

  StringBuilder onDelete(final StringBuilder b, final $ChangeRule onDelete) {
    return b.append("ON DELETE ").append(changeRule(onDelete));
  }

  StringBuilder onUpdate(final StringBuilder b, final $ChangeRule onUpdate) {
    return b.append("ON UPDATE ").append(changeRule(onUpdate));
  }

  String changeRule(final $ChangeRule changeRule) {
    return changeRule.text();
  }

  private StringBuilder recurseCheckRule(final $CheckReference check) {
    final Operator operator = Operator.valueOf(check.getOperator$().text());
    final $Check.Value$ value$ = check.getValue$();
    final String text = value$.text();
    final String condition = Numbers.isNumber(text) ? Numbers.stripTrailingZeros(text) : "'" + text + "'";
    final StringBuilder b = new StringBuilder();
    q(b, check.getColumn$().text()).append(' ').append(operator.symbol).append(' ').append(condition);

    final $CheckReference and = check.getAnd();
    if (and != null)
      return b.insert(0, '(').append(" AND ").append(recurseCheckRule(and)).append(')');

    final $CheckReference or = check.getOr();
    if (or != null)
      return b.insert(0, '(').append(" OR ").append(recurseCheckRule(or)).append(')');

    return b;
  }

  /**
   * Delegate method to produce {@link CreateStatement} objects of {@code TRIGGER} clauses for the specified {@link Table table}.
   *
   * @param table The {@link Table} for which to produce {@code TRIGGER} clauses.
   * @return A list of {@link CreateStatement} objects of {@code TRIGGER} clauses for the specified {@link Table table}.
   */
  ArrayList<CreateStatement> triggers(final Table table) {
    return new ArrayList<>();
  }

  ArrayList<CreateStatement> indexes(final Table table, final Map<String,ColumnRef> columnNameToColumn) {
    final ArrayList<CreateStatement> statements = new ArrayList<>();
    final Table.Indexes tableIndexes = table.getIndexes();
    if (tableIndexes != null) {
      final List<$IndexesIndex> indexes = tableIndexes.getIndex();
      for (int i = 0, i$ = indexes.size(); i < i$; ++i) { // [RA]
        final $IndexesIndex index = indexes.get(i);
        final List<? extends $Named> columns = index.getColumn();
        final int[] columnIndexes = new int[columns.size()];
        for (int c = 0, c$ = columns.size(); c < c$; ++c) { // [RA]
          final $Named column = columns.get(c);
          columnIndexes[c] = columnNameToColumn.get(column.getName$().text()).index;
        }

        final CreateStatement createIndex = createIndex(index.getUnique$() != null && index.getUnique$().text(), getIndexName(table, index.getType$(), columnIndexes), index.getType$(), table.getName$().text(), index.getColumn().toArray(new $Named[index.getColumn().size()]));
        if (createIndex != null)
          statements.add(createIndex);
      }
    }

    final List<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int c = 0, i$ = columns.size(); c < i$; ++c) { // [RA]
        final $Column column = columns.get(c);
        final $ColumnIndex index = DDLx.getIndex(column);
        if (index != null) {
          final CreateStatement createIndex = createIndex(index.getUnique$() != null && index.getUnique$().text(), getIndexName(table, index.getType$(), c), index.getType$(), table.getName$().text(), column);
          if (createIndex != null)
            statements.add(createIndex);
        }
      }
    }

    return statements;
  }

  /**
   * Returns a list of {@link CreateStatement} objects for the creation of types for the specified {@link Table}.
   *
   * @param table The {@link Table}.
   * @return A list of {@link CreateStatement} objects for the creation of types for the specified {@link Table}.
   */
  ArrayList<CreateStatement> types(final Table table, final HashMap<String,String> enumTemplateToValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    return new ArrayList<>();
  }

  abstract StringBuilder dropIndexOnClause(Table table);

  final LinkedHashSet<DropStatement> dropTable(final Table table) {
    final LinkedHashSet<DropStatement> statements = new LinkedHashSet<>();
    // FIXME: Explicitly dropping indexes on tables that may not exist will throw errors!
    // if (table.getIndexes() != null)
    // for (final Table.getIndexes.getIndex index : table.getIndexes(0).getIndex()) // [L]
    // statements.add(dropIndexIfExists(getIndexName(table, index, ???) + dropIndexOnClause(table)));

    // if (table.getColumn() != null)
    // for (final $Column column : table.getColumn()) // [L]
    // if (column.getIndex() != null)
    // statements.add(dropIndexIfExists(getIndexName(table, column.getIndex(0), column) + dropIndexOnClause(table)));

    // FIXME: Explicitly dropping triggers on tables that may not exist will throw errors!
    // if (table.getTriggers() != null)
    // for (final Table.Triggers.Trigger trigger : table.getTriggers().getTrigger()) // [L]
    // for (final String action : trigger.getActions$().text()) // [L]
    // statements.add(new DropStatement("DROP TRIGGER IF EXISTS " + q(getTriggerName(table.getName$().text(), trigger, action)) + " ON "
    // + q(table.getName$().text())));

    final DropStatement dropTable = dropTableIfExists(table);
    if (dropTable != null)
      statements.add(dropTable);

    return statements;
  }

  /**
   * Returns a list of {@link DropStatement} objects for the dropping of types for the specified {@link Table}.
   *
   * @param table The {@link Table}.
   * @return A list of {@link DropStatement} objects for the dropping of types for the specified {@link Table}.
   */
  LinkedHashSet<DropStatement> dropTypes(final Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    return new LinkedHashSet<>();
  }

  DropStatement dropTableIfExists(final Table table) {
    return new DropStatement(q(new StringBuilder("DROP TABLE IF EXISTS "), table.getName$().text()).toString());
  }

  DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement(q(new StringBuilder("DROP INDEX IF EXISTS "), indexName).toString());
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
      final $Tinyint.Precision$ precision$ = type.getPrecision$();
      return precision$ == null ? null : precision$.text();
    }

    if (column instanceof $Smallint) {
      final $Smallint type = ($Smallint)column;
      final $Smallint.Precision$ precision$ = type.getPrecision$();
      return precision$ == null ? null : precision$.text();
    }

    if (column instanceof $Int) {
      final $Int type = ($Int)column;
      final $Int.Precision$ precision$ = type.getPrecision$();
      return precision$ == null ? null : precision$.text();
    }

    if (column instanceof $Bigint) {
      final $Bigint type = ($Bigint)column;
      final $Bigint.Precision$ precision$ = type.getPrecision$();
      return precision$ == null ? null : precision$.text();
    }

    throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
  }

  final StringBuilder $default(final StringBuilder b, final $Column column) {
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      if (type.getDefault$() == null)
        return b;

      final $Char.Length$ length$ = type.getLength$();
      if (type.getDefault$().text().length() > length$.text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column.getName$().text() + "' DEFAULT '" + type.getDefault$().text() + "' is longer than declared LENGTH(" + length$.text() + ")");

      return b.append(" DEFAULT '").append(type.getDefault$().text()).append('\'');
    }

    if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      if (type.getDefault$() == null)
        return b;

      final HexBinary defaultText = type.getDefault$().text();
      final $Binary.Length$ length$ = type.getLength$();
      if (defaultText.getBytes().length > length$.text())
        throw new IllegalArgumentException(type.name().getPrefix() + ":" + type.name().getLocalPart() + " column '" + column.getName$().text() + "' DEFAULT '" + defaultText + "' is longer than declared LENGTH " + length$.text());

      b.append(" DEFAULT '");
      return compileBinary(b, defaultText.toString());
    }

    if (column instanceof $Integer) {
      final Number _default;
      final Byte precision;
      final Number min;
      final Number max;
      if (column instanceof $Tinyint) {
        final $Tinyint type = ($Tinyint)column;
        if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
          return b;

        final $Tinyint.Precision$ precision$ = type.getPrecision$();
        precision = precision$ == null ? null : precision$.text();
        final $Tinyint.Min$ min$ = type.getMin$();
        min = min$ == null ? null : min$.text();
        final $Tinyint.Max$ max$ = type.getMax$();
        max = max$ == null ? null : max$.text();
      }
      else if (column instanceof $Smallint) {
        final $Smallint type = ($Smallint)column;
        if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
          return b;

        final $Smallint.Precision$ precision$ = type.getPrecision$();
        precision = precision$ == null ? null : precision$.text();
        final $Smallint.Min$ min$ = type.getMin$();
        min = min$ == null ? null : min$.text();
        final $Smallint.Max$ max$ = type.getMax$();
        max = max$ == null ? null : max$.text();
      }
      else if (column instanceof $Int) {
        final $Int type = ($Int)column;
        if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
          return b;

        final $Int.Precision$ precision$ = type.getPrecision$();
        precision = precision$ == null ? null : precision$.text();
        final $Int.Min$ min$ = type.getMin$();
        min = min$ == null ? null : min$.text();
        final $Int.Max$ max$ = type.getMax$();
        max = max$ == null ? null : max$.text();
      }
      else if (column instanceof $Bigint) {
        final $Bigint type = ($Bigint)column;
        if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
          return b;

        final $Bigint.Precision$ precision$ = type.getPrecision$();
        precision = precision$ == null ? null : precision$.text();
        final $Bigint.Min$ min$ = type.getMin$();
        min = min$ == null ? null : min$.text();
        final $Bigint.Max$ max$ = type.getMax$();
        max = max$ == null ? null : max$.text();
      }
      else {
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
      }

      checkNumericDefault(column, precision == null ? null : Integer.valueOf(precision), _default, min, max);
      return b.append(" DEFAULT ").append(_default);
    }

    if (column instanceof $Float) {
      final $Float type = ($Float)column;
      final Float _default;
      if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
        return b;

      final $Float.Min$ min$ = type.getMin$();
      final $Float.Max$ max$ = type.getMax$();
      checkNumericDefault(type, null, _default, min$ == null ? null : min$.text(), max$ == null ? null : max$.text());
      return b.append(" DEFAULT ").append(_default);
    }

    if (column instanceof $Double) {
      final $Double type = ($Double)column;
      final Double _default;
      if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
        return b;

      final $Double.Min$ min$ = type.getMin$();
      final $Double.Max$ max$ = type.getMax$();
      checkNumericDefault(type, null, _default, min$ == null ? null : min$.text(), max$ == null ? null : max$.text());
      return b.append(" DEFAULT ").append(_default);
    }

    if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      final BigDecimal _default;
      if (type.getDefault$() == null || (_default = type.getDefault$().text()) == null)
        return b;

      final $Decimal.Precision$ precision$ = type.getPrecision$();
      final $Decimal.Min$ min$ = type.getMin$();
      final $Decimal.Max$ max$ = type.getMax$();
      checkNumericDefault(type, precision$ == null ? null : precision$.text(), _default, min$ == null ? null : min$.text(), max$ == null ? null : max$.text());
      return b.append(" DEFAULT ").append(_default);
    }

    if (column instanceof $Date) {
      final $Date type = ($Date)column;
      final String _default;
      return type.getDefault$() == null || (_default = type.getDefault$().text()) == null ? b : compileDate(b.append(" DEFAULT "), _default);
    }

    if (column instanceof $Time) {
      final $Time type = ($Time)column;
      final String _default;
      return type.getDefault$() == null || (_default = type.getDefault$().text()) == null ? b : compileTime(b.append(" DEFAULT "), _default);
    }

    if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      final String _default;
      return type.getDefault$() == null || (_default = type.getDefault$().text()) == null ? b : compileDateTime(b.append(" DEFAULT "), _default);
    }

    if (column instanceof $Boolean) {
      final $Boolean type = ($Boolean)column;
      final Boolean _default;
      return type.getDefault$() == null || (_default = type.getDefault$().text()) == null ? b : b.append(" DEFAULT ").append(_default);
    }

    if (column instanceof $Enum) {
      final $Enum type = ($Enum)column;
      final String _default;
      return type.getDefault$() == null || (_default = type.getDefault$().text()) == null ? b : b.append(" DEFAULT '").append(_default).append('\'');
    }

    if (column instanceof $Clob || column instanceof $Blob)
      return null;

    throw new UnsupportedOperationException("Unknown type: " + column.getClass().getName());
  }

  StringBuilder truncate(final String tableName) {
    return q(new StringBuilder("DELETE FROM "), tableName);
  }

  abstract StringBuilder $null(StringBuilder b, Table table, $Column column);
  abstract String $autoIncrement(LinkedHashSet<CreateStatement> alterStatements, Table table, $Integer column);

  StringBuilder compileBinary(final StringBuilder b, final String value) {
    return b.append("X'").append(value).append('\'');
  }

  StringBuilder compileDate(final StringBuilder b, final String value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compileDateTime(final StringBuilder b, final String value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compileTime(final StringBuilder b, final String value) {
    return b.append('\'').append(value).append('\'');
  }
}