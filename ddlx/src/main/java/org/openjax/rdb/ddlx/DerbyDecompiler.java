/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.ddlx;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Bigint;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Binary;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Blob;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Boolean;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$ChangeRule;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Char;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Check;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Clob;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Column;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Date;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Datetime;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Decimal;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Double;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Float;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$ForeignKey;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Index;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Int;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Integer;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$RangeOperator;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Smallint;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Table;
import org.openjax.rdb.ddlx_0_3_9.xL0gluGCXYYJc.$Time;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.ext.util.Strings;

final class DerbyDecompiler extends Decompiler {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  protected $Column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $Column column;
    if ("BIGINT".equals(typeName)) {
      final $Bigint type = newColumn($Bigint.class);
//      type.setPrecision$(new $Bigint.Precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type.setDefault$(new $Bigint.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

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
      type.setLength$(new $Blob.Length$((long)size));
      column = type;
    }
    else if ("BOOLEAN".equals(typeName)) {
      final $Boolean type = newColumn($Boolean.class);
      if (_default != null)
        type.setDefault$(new $Boolean.Default$(Boolean.parseBoolean(_default)));

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
      type.setLength$(new $Clob.Length$((long)size));
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
//      type.setPrecision$(new $Datetime.Precision$((byte)size));
      if (_default != null)
        type.setDefault$(new $Datetime.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("DECIMAL".equals(typeName)) {
      final $Decimal type = newColumn($Decimal.class);
      type.setPrecision$(new $Decimal.Precision$((short)size));
      type.setScale$(new $Decimal.Scale$((short)decimalDigits));
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
//    else if ("ENUM".equals(typeName)) {
//      final $Enum type = newColumn($Enum.class);
//      if (_default != null)
//        type.setDefault$(new $Enum.Default$(_default));
//
//      column = type;
//    }
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
        type.setDefault$(new $Int.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $Smallint type = newColumn($Smallint.class);
      type.setPrecision$(new $Smallint.Precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type.setDefault$(new $Smallint.Default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type.setGenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("TIME".equals(typeName)) {
      final $Time type = newColumn($Time.class);
      type.setPrecision$(new $Time.Precision$((byte)size));
      if (_default != null)
        type.setDefault$(new $Time.Default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
//    else if ("TINYINT".equals(typeName)) {
//      final $Tinyint type = newColumn($Tinyint.class);
//      type.setPrecision$(new $Tinyint.Precision$((byte)size));
//      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
//        type.setDefault$(new $Tinyint.setDefault$(new BigInteger(_default)));
//
//      if (autoIncrement != null && autoIncrement)
//        type.GenerateOnInsert$(new $Integer.GenerateOnInsert$($Integer.GenerateOnInsert$.AUTO_5FINCREMENT));
//
//      column = type;
//    }
    else {
      throw new UnsupportedOperationException("Unsupported column type: " + typeName);
    }

    column.setName$(new $Column.Name$(columnName));
    if (nullable != null && !nullable)
      column.setNull$(new $Column.Null$(false));

    return column;
  }

  private static final String tablesSql = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, c.columnnumber, c.columnname ")
    .append("FROM sys.syscolumns c ")
    .append("JOIN sys.systables t ON t.tableid = c.referenceid ")
    .append("JOIN sys.sysschemas s ON t.schemaid = s.schemaid ")
    .append("WHERE s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename, c.columnnumber").toString();

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

//  private static final String tablesSql = new StringBuilder()
//    .append("SELECT s.schemaname, t.tablename, c.columnnumber, c.columnname, CAST(c.columndatatype AS VARCHAR(255)) ")
//    .append("FROM sys.syscolumns c ")
//    .append("JOIN sys.systables t ON t.tableid = c.referenceid ")
//    .append("JOIN sys.sysschemas s ON s.schemaid = t.schemaid ")
//    .append("WHERE t.tabletype = 'T' ")
//    .append("ORDER BY s.schemaname, t.tablename, c.columnnumber ").toString();
//
//  protected Map<String,List<$Column>> getColumns(final Connection connection) throws SQLException {
//    final Map<String,List<$Column>> nameToColumn = new HashMap<String,List<$Column>>();
//    final PreparedStatement statement = connection.prepareStatement(tablesSql);
//    final ResultSet rows = statement.executeQuery();
//    final List<AbstractMap.SimpleEntry<Integer,$Column>> columns = new ArrayList<AbstractMap.SimpleEntry<Integer,$Column>>();
//    while (rows.next()) {
//      final String schemaName = rows.getString(1).toLowerCase();
//      final String tableName = rows.getString(2).toLowerCase();
//      final String columnName = rows.getString(4).toLowerCase();
//      final String columnType = rows.getString(5).toLowerCase();
//      final int notNull = columnType.indexOf("not null");
//      final boolean nullable = notNull == -1;
//      final String typeName = nullable ? columnType : columnType.substring(0, notNull - 1);
//
//      final $Column column = makeColumn(columnName.toLowerCase(), typeName, null, decimalDigits, _default, nullable.length() == 0 ? null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
//      columns.add(new AbstractMap.SimpleEntry<Integer,$Column>(columnNumber, column));
//
//    }
//  }

  private static final String constraintsSql = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) ")
    .append("FROM sys.sysconstraints c ")
    .append("JOIN sys.systables t ON c.tableid = t.tableid ")
    .append("JOIN sys.sysschemas s ON s.schemaid = c.schemaid ")
    .append("JOIN sys.syskeys k ON k.constraintid = c.constraintid ")
    .append("JOIN sys.sysconglomerates cg ON k.conglomerateid = cg.conglomerateid ")
    .append("WHERE c.state = 'E' ")
    .append("AND c.type = 'U' ")
    .append("AND cg.isconstraint = true ")
    .append("AND s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename").toString();

  @Override
  protected Map<String,List<$Table.Constraints.Unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(constraintsSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,List<$Table.Constraints.Unique>> tableNameToUniques = new HashMap<>();
    String lastTable = null;
    List<$Table.Constraints.Unique> uniques = null;
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
      final String[] colRefs = descriptor.substring(open + 1, close).split(",");

      final $Table.Constraints.Unique unique = new $Table.Constraints.Unique();
      uniques.add(unique);
      for (int i = 0; i < colRefs.length; i++) {
        colRefs[i] = columns.get(Integer.parseInt(colRefs[i].trim()) - 1);
        final $Table.Constraints.Unique.Column column = new $Table.Constraints.Unique.Column();
        column.setName$(new $Table.Constraints.Unique.Column.Name$(colRefs[i].toLowerCase()));
        unique.addColumn(column);
      }
    }

    return tableNameToUniques;
  }

  private static $Check makeCheck(final String andOr, final String columnName, final String operator, final String value) {
    final $Check check = andOr == null ? new $Table.Constraints.Check() : "AND".equals(andOr) ? new $Table.Constraints.Check.And() : new $Table.Constraints.Check.Or();
    check.addColumn(new $Table.Constraints.Check.Column(columnName));
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

    check.setOperator(new $Table.Constraints.Check.Operator(operatorEnum));
    check.setValue(new $Table.Constraints.Check.Value(value));
    return check;
  }

  // TODO: This only supports single-column constraints
  private static $Check makeCheck(final String checkDefinition) {
    final String[] terms = checkDefinition.substring(1, checkDefinition.length() - 1).split(" ");
    $Check check = null;
    $Check previousCheck = null;
    for (int i = 0; i < terms.length; i += 3) {
      final $Check nextCheck = makeCheck(i == 0 ? null : terms[i++], Strings.trim(terms[i], '"'), terms[i + 1], terms[i + 2]);
      if (previousCheck == null)
        check = previousCheck = nextCheck;
      else {
        if (nextCheck instanceof $Table.Constraints.Check.And)
          previousCheck.setAnd(nextCheck);
        else if (nextCheck instanceof $Table.Constraints.Check.Or)
          previousCheck.setOr(nextCheck);
        else
          throw new UnsupportedOperationException("Unsupported check type: " + nextCheck.getClass().getName());

        previousCheck = nextCheck;
      }
    }

    return check;
  }

  private static final String checkSql = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, ch.checkdefinition, ch.referencedcolumns ")
    .append("FROM sys.syschecks ch ")
    .append("JOIN sys.sysconstraints co ON ch.constraintid = co.constraintid ")
    .append("JOIN sys.sysschemas s ON s.schemaid = co.schemaid ")
    .append("JOIN sys.systables t ON t.tableid = co.tableid ")
    .append("WHERE co.state = 'E' ")
    .append("AND co.type = 'C' ")
    .append("AND s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename ").toString();

  @Override
  protected Map<String,List<$Check>> getCheckConstraints(final Connection connection) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(checkSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,List<$Check>> tableNameToChecks = new HashMap<>();
    String lastTable = null;
    List<$Check> checks = null;
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

  private static final String indexSql = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) ")
    .append("FROM sys.sysconglomerates cg ")
    .append("JOIN sys.sysschemas s ON s.schemaid = cg.schemaid ")
    .append("JOIN sys.systables t ON t.tableid = cg.tableid ")
    .append("WHERE cg.isindex = true ")
    .append("AND cg.isconstraint = false ")
    .append("AND s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename ").toString();

  @Override
  protected Map<String,$Table.Indexes> getIndexes(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(indexSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,$Table.Indexes> tableNameToIndexes = new HashMap<>();
    String lastTable = null;
    $Table.Indexes indexes = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToIndexes.put(tableName, indexes = new $Table.Indexes());
      }

      final String descriptor = rows.getString(3);

      final boolean unique = descriptor.startsWith("UNIQUE");
      final $Index.Type$.Enum type = descriptor.startsWith("HASH") ? $Index.Type$.HASH : $Index.Type$.BTREE;

      final $Table.Indexes.Index index = new $Table.Indexes.Index();
      indexes.addIndex(index);
      if (!$Index.Type$.BTREE.equals(type))
        index.setType$(new $Index.Type$(type));

      if (unique)
        index.setUnique$(new $Index.Unique$(unique));

      final String[] columnNumbers = descriptor.substring(descriptor.lastIndexOf('(') + 1, descriptor.lastIndexOf(')')).split(",");
      for (final String columnNumber : columnNumbers) {
        final String columnName = columnNames.get(Integer.parseInt(columnNumber.trim()) - 1);
        final $Table.Indexes.Index.Column column = new $Table.Indexes.Index.Column();
        column.setName$(new $Table.Indexes.Index.Column.Name$(columnName.toLowerCase()));
        index.addColumn(column);
      }
    }

    return tableNameToIndexes;
  }

  private static final String foreignKeySql = new StringBuilder()
    .append("SELECT s.schemaname, ft.tablename, CAST(fcg.descriptor AS VARCHAR(255)), fk.deleterule, fk.updaterule, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) ")
    .append("FROM sys.sysconstraints fc ")
    .append("JOIN sys.systables ft ON fc.tableid = ft.tableid ")
    .append("JOIN sys.sysschemas s ON s.schemaid = fc.schemaid ")
    .append("JOIN sys.sysforeignkeys fk ON fk.constraintid = fc.constraintid ")
    .append("JOIN sys.sysconglomerates fcg ON fk.conglomerateid = fcg.conglomerateid ")
    .append("JOIN sys.syskeys k ON k.constraintid = fk.keyconstraintid ")
    .append("JOIN sys.sysconstraints c ON c.constraintid = k.constraintid ")
    .append("JOIN sys.systables t ON c.tableid = t.tableid ")
    .append("JOIN sys.sysconglomerates cg ON k.conglomerateid = cg.conglomerateid ")
    .append("WHERE fc.state = 'E' ")
    .append("AND fc.type = 'F' ")
    .append("AND fcg.isconstraint = true ")
    .append("AND s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, ft.tablename").toString();

  @Override
  protected Map<String,Map<String,$ForeignKey>> getForeignKeys(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(foreignKeySql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,Map<String,$ForeignKey>> tableNameToForeignKeys = new HashMap<>();
    String lastTable = null;
    Map<String,$ForeignKey> columnNameToForeignKey = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<>());
      }

      final String primaryTable = rows.getString(6);
      final String primaryDescriptor = rows.getString(7);
      final String primaryColumn = tableNameToColumns.get(primaryTable).get(Integer.parseInt(primaryDescriptor.substring(primaryDescriptor.lastIndexOf('(') + 1, primaryDescriptor.lastIndexOf(')'))) - 1);

      final $ForeignKey foreignKey = new $Column.ForeignKey();
      foreignKey.setReferences$(new $ForeignKey.References$(primaryTable.toLowerCase()));
      foreignKey.setColumn$(new $ForeignKey.Column$(primaryColumn.toLowerCase()));

      final String deleteRule = rows.getString(4);
      final $ChangeRule.Enum onDelete = deleteRule == null ? null : "S".equals(deleteRule) ? $ChangeRule.RESTRICT : "C".equals(deleteRule) ? $ChangeRule.CASCADE : "U".equals(deleteRule) ? $ChangeRule.SET_20NULL : null;
      if (onDelete != null)
        foreignKey.setOnDelete$(new $ForeignKey.OnDelete$(onDelete));

      final String updateRule = rows.getString(5);
      final $ChangeRule.Enum onUpdate = updateRule == null ? null : "S".equals(updateRule) ? $ChangeRule.RESTRICT : null;
      if (onUpdate != null)
        foreignKey.setOnUpdate$(new $ForeignKey.OnUpdate$(onUpdate));

      final String foreignDescriptor = rows.getString(3);
      final String foreignColumn = columnNames.get(Integer.parseInt(foreignDescriptor.substring(foreignDescriptor.lastIndexOf('(') + 1, foreignDescriptor.lastIndexOf(')'))) - 1);
      columnNameToForeignKey.put(foreignColumn, foreignKey);
    }

    return tableNameToForeignKeys;
  }
}