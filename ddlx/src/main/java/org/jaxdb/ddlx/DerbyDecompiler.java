/* Copyright (c) 2017 JAX-DB
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Check;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary.Column$;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$RangeOperator;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.libj.lang.Strings;

final class DerbyDecompiler extends Decompiler {
  @Override
  protected DbVendor getVendor() {
    return DbVendor.DERBY;
  }

  private static final String autoincrementStart = "AUTOINCREMENT: start ";
  private static final int autoincrementStartLen = autoincrementStart.length();

  private static String getDefault(final String _default) {
    return _default.startsWith(autoincrementStart) ? _default.substring(autoincrementStartLen, _default.indexOf(' ', autoincrementStartLen + 1)) : _default;
  }

  @Override
  $Column makeColumn(final String columnName, final String typeName, final long size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $Column column;
    if ("BIGINT".equals(typeName)) {
      final $Bigint type = newColumn($Bigint.class);
      // type.setPrecision$(new $Bigint.Precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type.setDefault$(new $Bigint.Default$(Long.valueOf(getDefault(_default))));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Bigint.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("CHAR () FOR BIT DATA".equals(typeName) || "VARCHAR () FOR BIT DATA".equals(typeName)) {
      final $Binary type = newColumn($Binary.class);
      if (typeName.startsWith("VARCHAR"))
        type.setVarying$(new $Binary.Varying$(true));

      type.setLength$(new $Binary.Length$(size));
      column = type;
    }
    else if ("BLOB".equals(typeName)) {
      final $Blob type = newColumn($Blob.class);
      type.setLength$(new $Blob.Length$(size));
      column = type;
    }
    else if ("BOOLEAN".equals(typeName)) {
      final $Boolean type = newColumn($Boolean.class);
      if (_default != null)
        type.setDefault$(new $Boolean.Default$(Boolean.valueOf(_default)));

      column = type;
    }
    else if ("VARCHAR".equals(typeName) || "CHAR".equals(typeName)) {
      final $Char type = newColumn($Char.class);
      if ("VARCHAR".equals(typeName))
        type.setVarying$(new $Char.Varying$(true));

      type.setLength$(new $Char.Length$(size));
      if (_default != null)
        type.setDefault$(new $Char.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("CLOB".equals(typeName)) {
      final $Clob type = newColumn($Clob.class);
      type.setLength$(new $Clob.Length$(size));
      column = type;
    }
    else if ("DATE".equals(typeName)) {
      final $Date type = newColumn($Date.class);
      if (_default != null)
        type.setDefault$(new $Date.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("TIMESTAMP".equals(typeName)) {
      final $Datetime type = newColumn($Datetime.class);
      // type.setPrecision$(new $Datetime.Precision$((byte)size));
      if (_default != null)
        type.setDefault$(new $Datetime.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("DECIMAL".equals(typeName)) {
      final int precision = (int)size;
      final $Decimal type = newColumn($Decimal.class);
      type.setPrecision$(new $Decimal.Precision$(precision));
      type.setScale$(new $Decimal.Scale$(decimalDigits));
      if (_default != null)
        type.setDefault$(new $Decimal.Default$(new BigDecimal(_default)));

      column = type;
    }
    else if ("DOUBLE".equals(typeName)) {
      final $Double type = newColumn($Double.class);
      if (_default != null)
        type.setDefault$(new $Double.Default$(Double.valueOf(_default)));

      column = type;
    }
    // else if ("ENUM".equals(typeName)) {
    // final $Enum type = newColumn($Enum.class);
    // if (_default != null)
    // type.setDefault$(new $Enum.Default$(_default));
    //
    // column = type;
    // }
    else if ("FLOAT".equals(typeName)) {
      final $Float type = newColumn($Float.class);
      if (_default != null)
        type.setDefault$(new $Float.Default$(Float.valueOf(_default)));

      column = type;
    }
    else if ("INTEGER".equals(typeName)) {
      final $Int type = newColumn($Int.class);
      type.setPrecision$(new $Int.Precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type.setDefault$(new $Int.Default$(Integer.valueOf(getDefault(_default))));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Int.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $Smallint type = newColumn($Smallint.class);
      type.setPrecision$(new $Smallint.Precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type.setDefault$(new $Smallint.Default$(Short.valueOf(getDefault(_default))));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Smallint.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("TIME".equals(typeName)) {
      final $Time type = newColumn($Time.class);
      type.setPrecision$(new $Time.Precision$((byte)size));
      if (_default != null)
        type.setDefault$(new $Time.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    // else if ("TINYINT".equals(typeName)) {
    // final $Tinyint type = newColumn($Tinyint.class);
    // type.setPrecision$(new $Tinyint.Precision$((byte)size));
    // if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
    // type.setDefault$(new $Tinyint.setDefault$(new BigInteger(getDefault(_default))));
    //
    // if (autoIncrement != null && autoIncrement)
    // type.GenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));
    //
    // column = type;
    // }
    else {
      throw new UnsupportedOperationException("Unsupported column type: " + typeName);
    }

    column.setName$(new $Column.Name$(columnName));
    if (nullable != null && !nullable)
      column.setNull$(new $Column.Null$(false));

    return column;
  }

  private static final String tablesSql =
    "SELECT s.schemaname, t.tablename, c.columnnumber, c.columnname " +
      "FROM sys.syscolumns c " +
      "JOIN sys.systables t ON t.tableid = c.referenceid " +
      "JOIN sys.sysschemas s ON t.schemaid = s.schemaid " +
      "WHERE s.schemaname = CURRENT SCHEMA " +
      "ORDER BY s.schemaname, t.tablename, c.columnnumber";

  @SuppressWarnings("null")
  private static Map<String,List<String>> getTables(final Connection connection) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(tablesSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,List<String>> tableNameToColumns = new HashMap<>();
    String lastTable = null;
    List<String> columns = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToColumns.put(tableName, columns = new ArrayList<>());
      }

      final String columnName = rows.getString(4);
      columns.add(columnName);
    }

    return tableNameToColumns;
  }

  // private static final String tablesSql =
  // "SELECT s.schemaname, t.tablename, c.columnnumber, c.columnname, CAST(c.columndatatype AS VARCHAR(255)) " +
  // "FROM sys.syscolumns c " +
  // "JOIN sys.systables t ON t.tableid = c.referenceid " +
  // "JOIN sys.sysschemas s ON s.schemaid = t.schemaid " +
  // "WHERE t.tabletype = 'T' " +
  // "ORDER BY s.schemaname, t.tablename, c.columnnumber ";
  //
  // Map<String,List<$Column>> getColumns(final Connection connection) throws SQLException {
  // final Map<String,List<$Column>> nameToColumn = new HashMap<String,List<$Column>>();
  // final PreparedStatement statement = connection.prepareStatement(tablesSql);
  // final ResultSet rows = statement.executeQuery();
  // final ArrayList<AbstractMap.SimpleEntry<Integer,$Column>> columns = new ArrayList<AbstractMap.SimpleEntry<Integer,$Column>>();
  // while (rows.next()) {
  // final String schemaName = rows.getString(1).toLowerCase();
  // final String tableName = rows.getString(2).toLowerCase();
  // final String columnName = rows.getString(4).toLowerCase();
  // final String columnType = rows.getString(5).toLowerCase();
  // final int notNull = columnType.indexOf("not null");
  // final boolean nullable = notNull == -1;
  // final String typeName = nullable ? columnType : columnType.substring(0, notNull - 1);
  //
  // final $Column column = makeColumn(columnName.toLowerCase(), typeName, null, decimalDigits, _default, nullable.length() == 0 ?
  // null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
  // columns.add(new AbstractMap.SimpleEntry<Integer,$Column>(columnNumber, column));
  //
  // }
  // }

  private static final String constraintsSql =
    "SELECT s.schemaname, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) " +
      "FROM sys.sysconstraints c " +
      "JOIN sys.systables t ON c.tableid = t.tableid " +
      "JOIN sys.sysschemas s ON s.schemaid = c.schemaid " +
      "JOIN sys.syskeys k ON k.constraintid = c.constraintid " +
      "JOIN sys.sysconglomerates cg ON k.conglomerateid = cg.conglomerateid " +
      "WHERE c.state = 'E' " +
      "AND c.type = 'U' " +
      "AND cg.isconstraint = true " +
      "AND s.schemaname = CURRENT SCHEMA " +
      "ORDER BY s.schemaname, t.tablename";

  @Override
  @SuppressWarnings("null")
  Map<String,ArrayList<Schema.Table.Constraints.Unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(constraintsSql);
    final ResultSet rows = statement.executeQuery();
    final HashMap<String,ArrayList<Schema.Table.Constraints.Unique>> tableNameToUniques = new HashMap<>();
    String lastTable = null;
    ArrayList<Schema.Table.Constraints.Unique> uniques = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToUniques.put(tableName, uniques = new ArrayList<>());
      }

      final List<String> columns = tableNameToColumns.get(tableName);
      final String descriptor = rows.getString(3);
      final int close = descriptor.lastIndexOf(')');
      final int open = descriptor.lastIndexOf('(', close - 1);
      final String[] colRefs = Strings.split(descriptor.substring(open + 1, close), ',');

      final Schema.Table.Constraints.Unique unique = new Schema.Table.Constraints.Unique();
      uniques.add(unique);
      for (int i = 0, i$ = colRefs.length; i < i$; ++i) { // [A]
        colRefs[i] = columns.get(Integer.valueOf(colRefs[i].trim()) - 1);
        final Schema.Table.Constraints.Unique.Column column = new Schema.Table.Constraints.Unique.Column();
        column.setName$(new Schema.Table.Constraints.Unique.Column.Name$(colRefs[i].toLowerCase()));
        unique.addColumn(column);
      }
    }

    return tableNameToUniques;
  }

  private static $CheckReference makeCheck(final String andOr, final String columnName, final String operator, final String value) {
    final $CheckReference check = andOr == null ? new Schema.Table.Constraints.Check() : "AND".equals(andOr) ? new Schema.Table.Constraints.Check.And() : new Schema.Table.Constraints.Check.Or();
    check.setColumn$(new $Check.Column$(columnName));
    final $RangeOperator.Enum operatorEnum;
    if ("!=".equals(operator))
      operatorEnum = $RangeOperator.ne;
    else if ("<".equals(operator))
      operatorEnum = $RangeOperator.lt;
    else if ("<=".equals(operator))
      operatorEnum = $RangeOperator.lte;
    else if ("=".equals(operator))
      operatorEnum = $RangeOperator.eq;
    else if (">".equals(operator))
      operatorEnum = $RangeOperator.gt;
    else if (">=".equals(operator))
      operatorEnum = $RangeOperator.gte;
    else
      throw new UnsupportedOperationException("Unsupported check operator: " + operator);

    check.setOperator$(new $Check.Operator$(operatorEnum));
    check.setValue$(new $Check.Value$(value));
    return check;
  }

  // TODO: This only supports single-column constraints
  private static $CheckReference makeCheck(final String checkDefinition) {
    final String[] terms = Strings.split(checkDefinition.substring(1, checkDefinition.length() - 1), ' ');
    $CheckReference check = null;
    $CheckReference previousCheck = null;
    for (int i = 0, i$ = terms.length; i < i$; i += 3) { // [A]
      final $CheckReference nextCheck = makeCheck(i == 0 ? null : terms[i++], Strings.trim(terms[i], '"'), terms[i + 1], terms[i + 2]);
      if (previousCheck == null)
        check = previousCheck = nextCheck;
      else {
        if (nextCheck instanceof Schema.Table.Constraints.Check.And)
          previousCheck.setAnd(nextCheck);
        else if (nextCheck instanceof Schema.Table.Constraints.Check.Or)
          previousCheck.setOr(nextCheck);
        else
          throw new UnsupportedOperationException("Unsupported check type: " + nextCheck.getClass().getName());

        previousCheck = nextCheck;
      }
    }

    return check;
  }

  private static final String checkSql =
    "SELECT s.schemaname, t.tablename, ch.checkdefinition, ch.referencedcolumns " +
      "FROM sys.syschecks ch " +
      "JOIN sys.sysconstraints co ON ch.constraintid = co.constraintid " +
      "JOIN sys.sysschemas s ON s.schemaid = co.schemaid " +
      "JOIN sys.systables t ON t.tableid = co.tableid " +
      "WHERE co.state = 'E' " +
      "AND co.type = 'C' " +
      "AND s.schemaname = CURRENT SCHEMA " +
      "ORDER BY s.schemaname, t.tablename ";

  @Override
  @SuppressWarnings("null")
  Map<String,ArrayList<$CheckReference>> getCheckConstraints(final Connection connection) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(checkSql);
    final ResultSet rows = statement.executeQuery();
    final HashMap<String,ArrayList<$CheckReference>> tableNameToChecks = new HashMap<>();
    String lastTable = null;
    ArrayList<$CheckReference> checks = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToChecks.put(tableName, checks = new ArrayList<>());
      }

      final String checkDefinition = rows.getString(3);
      final String referencedColumns = rows.getString(4);
      if (referencedColumns.contains(","))
        throw new UnsupportedOperationException("Only support single-column check constraints");

      checks.add(makeCheck(checkDefinition));
    }

    return tableNameToChecks;
  }

  private static final String indexSql =
    "SELECT s.schemaname, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) " +
      "FROM sys.sysconglomerates cg " +
      "JOIN sys.sysschemas s ON s.schemaid = cg.schemaid " +
      "JOIN sys.systables t ON t.tableid = cg.tableid " +
      "WHERE cg.isindex = true " +
      "AND cg.isconstraint = false " +
      "AND s.schemaname = CURRENT SCHEMA " +
      "ORDER BY s.schemaname, t.tablename ";

  @Override
  @SuppressWarnings("null")
  Map<String,Schema.Table.Indexes> getIndexes(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(indexSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,Schema.Table.Indexes> tableNameToIndexes = new HashMap<>();
    String lastTable = null;
    Schema.Table.Indexes indexes = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToIndexes.put(tableName, indexes = new Schema.Table.Indexes());
      }

      final String descriptor = rows.getString(3);

      final boolean unique = descriptor.startsWith("UNIQUE");
      final $IndexType.Enum type = descriptor.startsWith("HASH") ? $IndexType.HASH : $IndexType.BTREE;

      final Schema.Table.Indexes.Index index = new Schema.Table.Indexes.Index();
      indexes.addIndex(index);
      if (!$IndexType.BTREE.equals(type))
        index.setType$(new Schema.Table.Indexes.Index.Type$(type));

      if (unique)
        index.setUnique$(new Schema.Table.Indexes.Index.Unique$(unique));

      final String[] columnNumbers = Strings.split(descriptor.substring(descriptor.lastIndexOf('(') + 1, descriptor.lastIndexOf(')')), ',');
      for (final String columnNumber : columnNumbers) { // [A]
        final String columnName = columnNames.get(Integer.valueOf(columnNumber.trim()) - 1);
        final Schema.Table.Indexes.Index.Column column = new Schema.Table.Indexes.Index.Column();
        column.setName$(new Schema.Table.Indexes.Index.Column.Name$(columnName.toLowerCase()));
        index.addColumn(column);
      }
    }

    return tableNameToIndexes;
  }

  private static final String foreignKeySql =
    "SELECT s.schemaname, ft.tablename, CAST(fcg.descriptor AS VARCHAR(255)), fk.deleterule, fk.updaterule, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) " +
      "FROM sys.sysconstraints fc " +
      "JOIN sys.systables ft ON fc.tableid = ft.tableid " +
      "JOIN sys.sysschemas s ON s.schemaid = fc.schemaid " +
      "JOIN sys.sysforeignkeys fk ON fk.constraintid = fc.constraintid " +
      "JOIN sys.sysconglomerates fcg ON fk.conglomerateid = fcg.conglomerateid " +
      "JOIN sys.syskeys k ON k.constraintid = fk.keyconstraintid " +
      "JOIN sys.sysconstraints c ON c.constraintid = k.constraintid " +
      "JOIN sys.systables t ON c.tableid = t.tableid " +
      "JOIN sys.sysconglomerates cg ON k.conglomerateid = cg.conglomerateid " +
      "WHERE fc.state = 'E' " +
      "AND fc.type = 'F' " +
      "AND fcg.isconstraint = true " +
      "AND s.schemaname = CURRENT SCHEMA " +
      "ORDER BY s.schemaname, ft.tablename";

  @Override
  @SuppressWarnings("null")
  Map<String,Map<String,$ForeignKeyUnary>> getForeignKeys(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(foreignKeySql);
    final ResultSet rows = statement.executeQuery();
    final HashMap<String,Map<String,$ForeignKeyUnary>> tableNameToForeignKeys = new HashMap<>();
    String lastTable = null;
    Map<String,$ForeignKeyUnary> columnNameToForeignKey = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<>());
      }

      final String primaryTable = rows.getString(6);
      final String primaryDescriptor = rows.getString(7);
      final String primaryColumn = tableNameToColumns.get(primaryTable).get(Integer.valueOf(primaryDescriptor.substring(primaryDescriptor.lastIndexOf('(') + 1, primaryDescriptor.lastIndexOf(')'))) - 1);

      final $ForeignKeyUnary foreignKey = new $ForeignKeyUnary() {
        @Override
        protected $ForeignKeyUnary inherits() {
          return null;
        }
      };
      foreignKey.setReferences$(new $ForeignKeyUnary.References$(primaryTable.toLowerCase()));
      foreignKey.setColumn$(new Column$(primaryColumn.toLowerCase()));

      final String deleteRule = rows.getString(4);
      final $ChangeRule.Enum onDelete = deleteRule == null ? null : "S".equals(deleteRule) ? $ChangeRule.RESTRICT : "C".equals(deleteRule) ? $ChangeRule.CASCADE : "U".equals(deleteRule) ? $ChangeRule.SET_20NULL : null;
      if (onDelete != null)
        foreignKey.setOnDelete$(new $ForeignKeyUnary.OnDelete$(onDelete));

      final String updateRule = rows.getString(5);
      final $ChangeRule.Enum onUpdate = updateRule == null ? null : "S".equals(updateRule) ? $ChangeRule.RESTRICT : null;
      if (onUpdate != null)
        foreignKey.setOnUpdate$(new $ForeignKeyUnary.OnUpdate$(onUpdate));

      final String foreignDescriptor = rows.getString(3);
      final String foreignColumn = columnNames.get(Integer.valueOf(foreignDescriptor.substring(foreignDescriptor.lastIndexOf('(') + 1, foreignDescriptor.lastIndexOf(')'))) - 1);
      columnNameToForeignKey.put(foreignColumn, foreignKey);
    }

    return tableNameToForeignKeys;
  }
}