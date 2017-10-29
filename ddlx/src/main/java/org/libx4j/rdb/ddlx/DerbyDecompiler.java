/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.ddlx;

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

import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_binary;
import org.libx4j.rdb.ddlx.xe.$ddlx_blob;
import org.libx4j.rdb.ddlx.xe.$ddlx_boolean;
import org.libx4j.rdb.ddlx.xe.$ddlx_changeRule;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_clob;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_date;
import org.libx4j.rdb.ddlx.xe.$ddlx_datetime;
import org.libx4j.rdb.ddlx.xe.$ddlx_decimal;
import org.libx4j.rdb.ddlx.xe.$ddlx_double;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_rangeOperator;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_time;
import org.libx4j.rdb.vendor.DBVendor;

final class DerbyDecompiler extends Decompiler {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  protected $ddlx_column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $ddlx_column column;
    if ("BIGINT".equals(typeName)) {
      final $ddlx_bigint type = newColumn($ddlx_bigint.class);
//      type._precision$(new $ddlx_bigint._precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type._default$(new $ddlx_bigint._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("CHAR () FOR BIT DATA".equals(typeName) || "VARCHAR () FOR BIT DATA".equals(typeName)) {
      final $ddlx_binary type = newColumn($ddlx_binary.class);
      if (typeName.startsWith("VARCHAR"))
        type._varying$(new $ddlx_binary._varying$(true));

      type._length$(new $ddlx_binary._length$(size));
      column = type;
    }
    else if ("BLOB".equals(typeName)) {
      final $ddlx_blob type = newColumn($ddlx_blob.class);
      type._length$(new $ddlx_blob._length$((long)size));
      column = type;
    }
    else if ("BOOLEAN".equals(typeName)) {
      final $ddlx_boolean type = newColumn($ddlx_boolean.class);
      if (_default != null)
        type._default$(new $ddlx_boolean._default$(Boolean.parseBoolean(_default)));

      column = type;
    }
    else if ("VARCHAR".equals(typeName) || "CHAR".equals(typeName)) {
      final $ddlx_char type = newColumn($ddlx_char.class);
      if ("VARCHAR".equals(typeName))
        type._varying$(new $ddlx_char._varying$(true));

      type._length$(new $ddlx_char._length$(size));
      if (_default != null)
        type._default$(new $ddlx_char._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("CLOB".equals(typeName)) {
      final $ddlx_clob type = newColumn($ddlx_clob.class);
      type._length$(new $ddlx_clob._length$((long)size));
      column = type;
    }
    else if ("DATE".equals(typeName)) {
      final $ddlx_date type = newColumn($ddlx_date.class);
      if (_default != null)
        type._default$(new $ddlx_date._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("TIMESTAMP".equals(typeName)) {
      final $ddlx_datetime type = newColumn($ddlx_datetime.class);
//      type._precision$(new $ddlx_datetime._precision$((byte)size));
      if (_default != null)
        type._default$(new $ddlx_datetime._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
    else if ("DECIMAL".equals(typeName)) {
      final $ddlx_decimal type = newColumn($ddlx_decimal.class);
      type._precision$(new $ddlx_decimal._precision$((short)size));
      type._scale$(new $ddlx_decimal._scale$((short)decimalDigits));
      if (_default != null)
        type._default$(new $ddlx_decimal._default$(new BigDecimal(_default)));

      column = type;
    }
    else if ("DOUBLE".equals(typeName)) {
      final $ddlx_double type = newColumn($ddlx_double.class);
      if (_default != null)
        type._default$(new $ddlx_double._default$(Double.valueOf(_default)));

      column = type;
    }
//    else if ("ENUM".equals(typeName)) {
//      final $ddlx_enum type = newColumn($ddlx_enum.class);
//      if (_default != null)
//        type._default$(new $ddlx_enum._default$(_default));
//
//      column = type;
//    }
    else if ("FLOAT".equals(typeName)) {
      final $ddlx_float type = newColumn($ddlx_float.class);
      if (_default != null)
        type._default$(new $ddlx_float._default$(Float.valueOf(_default)));

      column = type;
    }
    else if ("INTEGER".equals(typeName)) {
      final $ddlx_int type = newColumn($ddlx_int.class);
      type._precision$(new $ddlx_int._precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type._default$(new $ddlx_int._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("SMALLINT".equals(typeName)) {
      final $ddlx_smallint type = newColumn($ddlx_smallint.class);
      type._precision$(new $ddlx_smallint._precision$((byte)size));
      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
        type._default$(new $ddlx_smallint._default$(new BigInteger(_default)));

      if (autoIncrement != null && autoIncrement)
        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));

      column = type;
    }
    else if ("TIME".equals(typeName)) {
      final $ddlx_time type = newColumn($ddlx_time.class);
      type._precision$(new $ddlx_time._precision$((byte)size));
      if (_default != null)
        type._default$(new $ddlx_time._default$(_default.substring(1, _default.length() - 1)));

      column = type;
    }
//    else if ("TINYINT".equals(typeName)) {
//      final $ddlx_tinyint type = newColumn($ddlx_tinyint.class);
//      type._precision$(new $ddlx_tinyint._precision$((byte)size));
//      if (_default != null && !"GENERATED_BY_DEFAULT".equals(_default))
//        type._default$(new $ddlx_tinyint._default$(new BigInteger(_default)));
//
//      if (autoIncrement != null && autoIncrement)
//        type._generateOnInsert$(new $ddlx_integer._generateOnInsert$($ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT));
//
//      column = type;
//    }
    else {
      throw new UnsupportedOperationException("Unsupported column type: " + typeName);
    }

    column._name$(new $ddlx_column._name$(columnName));
    if (nullable != null && !nullable)
      column._null$(new $ddlx_column._null$(false));

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
    final Map<String,List<String>> tableNameToColumns = new HashMap<String,List<String>>();
    String lastTable = null;
    List<String> columns = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToColumns.put(tableName, columns = new ArrayList<String>());
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
//  protected Map<String,List<$ddlx_column>> getColumns(final Connection connection) throws SQLException {
//    final Map<String,List<$ddlx_column>> nameToColumn = new HashMap<String,List<$ddlx_column>>();
//    final PreparedStatement statement = connection.prepareStatement(tablesSql);
//    final ResultSet rows = statement.executeQuery();
//    final List<AbstractMap.SimpleEntry<Integer,$ddlx_column>> columns = new ArrayList<AbstractMap.SimpleEntry<Integer,$ddlx_column>>();
//    while (rows.next()) {
//      final String schemaName = rows.getString(1).toLowerCase();
//      final String tableName = rows.getString(2).toLowerCase();
//      final String columnName = rows.getString(4).toLowerCase();
//      final String columnType = rows.getString(5).toLowerCase();
//      final int notNull = columnType.indexOf("not null");
//      final boolean nullable = notNull == -1;
//      final String typeName = nullable ? columnType : columnType.substring(0, notNull - 1);
//
//      final $ddlx_column column = makeColumn(columnName.toLowerCase(), typeName, null, decimalDigits, _default, nullable.length() == 0 ? null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
//      columns.add(new AbstractMap.SimpleEntry<Integer,$ddlx_column>(columnNumber, column));
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
  protected Map<String,List<$ddlx_table._constraints._unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(constraintsSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,List<$ddlx_table._constraints._unique>> tableNameToUniques = new HashMap<String,List<$ddlx_table._constraints._unique>>();
    String lastTable = null;
    List<$ddlx_table._constraints._unique> uniques = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToUniques.put(tableName, uniques = new ArrayList<$ddlx_table._constraints._unique>());
      }

      final List<String> columns = tableNameToColumns.get(tableName);
      final String descriptor = rows.getString(3);
      final int close = descriptor.lastIndexOf(')');
      final int open = descriptor.lastIndexOf('(', close - 1);
      final String[] colRefs = descriptor.substring(open + 1, close).split(",");

      final $ddlx_table._constraints._unique unique = new $ddlx_table._constraints._unique();
      uniques.add(unique);
      for (int i = 0; i < colRefs.length; i++) {
        colRefs[i] = columns.get(Integer.parseInt(colRefs[i].trim()) - 1);
        final $ddlx_table._constraints._unique._column column = new $ddlx_table._constraints._unique._column();
        column._name$(new $ddlx_table._constraints._unique._column._name$(colRefs[i].toLowerCase()));
        unique._column(column);
      }
    }

    return tableNameToUniques;
  }

  private static $ddlx_check makeCheck(final String andOr, final String columnName, final String operator, final String value) {
    final $ddlx_check check = andOr == null ? new $ddlx_table._constraints._check() : "AND".equals(andOr) ? new $ddlx_table._constraints._check._and() : new $ddlx_table._constraints._check._or();
    check._column(new $ddlx_table._constraints._check._column(columnName));
    final $ddlx_rangeOperator.Enum operatorEnum;
    if ("!=".equals(operator))
      operatorEnum = $ddlx_rangeOperator.ne;
    else if ("<".equals(operator))
      operatorEnum = $ddlx_rangeOperator.lt;
    else if ("<=".equals(operator))
      operatorEnum = $ddlx_rangeOperator.lte;
    else if ("=".equals(operator))
      operatorEnum = $ddlx_rangeOperator.eq;
    else if (">".equals(operator))
      operatorEnum = $ddlx_rangeOperator.gt;
    else if (">=".equals(operator))
      operatorEnum = $ddlx_rangeOperator.gte;
    else
      throw new UnsupportedOperationException("Unsupported check operator: " + operator);

    check._operator(new $ddlx_table._constraints._check._operator(operatorEnum));
    check._value(new $ddlx_table._constraints._check._value(value));
    return check;
  }

  // TODO: This only supports single-column constraints
  private static $ddlx_check makeCheck(final String checkDefinition) {
    final String[] terms = checkDefinition.substring(1, checkDefinition.length() - 1).split(" ");
    $ddlx_check check = null;
    $ddlx_check previousCheck = null;
    for (int i = 0; i < terms.length; i += 3) {
      final $ddlx_check nextCheck = makeCheck(i == 0 ? null : terms[i++], terms[i + 0], terms[i + 1], terms[i + 2]);
      if (previousCheck == null)
        check = previousCheck = nextCheck;
      else {
        if (nextCheck instanceof $ddlx_table._constraints._check._and)
          previousCheck._and(nextCheck);
        else if (nextCheck instanceof $ddlx_table._constraints._check._or)
          previousCheck._or(nextCheck);
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
  protected Map<String,List<$ddlx_check>> getCheckConstraints(final Connection connection) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(checkSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,List<$ddlx_check>> tableNameToChecks = new HashMap<String,List<$ddlx_check>>();
    String lastTable = null;
    List<$ddlx_check> checks = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToChecks.put(tableName, checks = new ArrayList<$ddlx_check>());
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
  protected Map<String,$ddlx_table._indexes> getIndexes(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(indexSql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,$ddlx_table._indexes> tableNameToIndexes = new HashMap<String,$ddlx_table._indexes>();
    String lastTable = null;
    $ddlx_table._indexes indexes = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToIndexes.put(tableName, indexes = new $ddlx_table._indexes());
      }

      final String descriptor = rows.getString(3);

      final boolean unique = descriptor.startsWith("UNIQUE");
      final String type = descriptor.startsWith("HASH") ? "HASH" : "BTREE";

      final $ddlx_table._indexes._index index = new $ddlx_table._indexes._index();
      indexes._index(index);
      if (!"BTREE".equals(type))
        index._type$(new $ddlx_index._type$(type));

      if (unique)
        index._unique$(new $ddlx_index._unique$(unique));

      final String columnNumbers[] = descriptor.substring(descriptor.lastIndexOf('(') + 1, descriptor.lastIndexOf(')')).split(",");
      for (final String columnNumber : columnNumbers) {
        final String columnName = columnNames.get(Integer.parseInt(columnNumber.trim()) - 1);
        final $ddlx_table._indexes._index._column column = new $ddlx_table._indexes._index._column();
        column._name$(new $ddlx_table._indexes._index._column._name$(columnName.toLowerCase()));
        index._column(column);
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
  protected Map<String,Map<String,$ddlx_foreignKey>> getForeignKeys(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(foreignKeySql);
    final ResultSet rows = statement.executeQuery();
    final Map<String,Map<String,$ddlx_foreignKey>> tableNameToForeignKeys = new HashMap<String,Map<String,$ddlx_foreignKey>>();
    String lastTable = null;
    Map<String,$ddlx_foreignKey> columnNameToForeignKey = null;
    while (rows.next()) {
      final String tableName = rows.getString(2);
      final List<String> columnNames = tableNameToColumns.get(tableName);
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<String,$ddlx_foreignKey>());
      }

      final String primaryTable = rows.getString(6);
      final String primaryDescriptor = rows.getString(7);
      final String primaryColumn = tableNameToColumns.get(primaryTable).get(Integer.parseInt(primaryDescriptor.substring(primaryDescriptor.lastIndexOf('(') + 1, primaryDescriptor.lastIndexOf(')'))) - 1);

      final $ddlx_foreignKey foreignKey = new $ddlx_column._foreignKey();
      foreignKey._references$(new $ddlx_foreignKey._references$(primaryTable.toLowerCase()));
      foreignKey._column$(new $ddlx_foreignKey._column$(primaryColumn.toLowerCase()));

      final String deleteRule = rows.getString(4);
      final $ddlx_changeRule.Enum onDelete = deleteRule == null ? null : "S".equals(deleteRule) ? $ddlx_changeRule.RESTRICT : "C".equals(deleteRule) ? $ddlx_changeRule.CASCADE : "U".equals(deleteRule) ? $ddlx_changeRule.SET_20NULL : null;
      if (onDelete != null)
        foreignKey._onDelete$(new $ddlx_foreignKey._onDelete$(onDelete));

      final String updateRule = rows.getString(5);
      final $ddlx_changeRule.Enum onUpdate = updateRule == null ? null : "S".equals(updateRule) ? $ddlx_changeRule.RESTRICT : null;
      if (onUpdate != null)
        foreignKey._onUpdate$(new $ddlx_foreignKey._onUpdate$(onUpdate));

      final String foreignDescriptor = rows.getString(3);
      final String foreignColumn = columnNames.get(Integer.parseInt(foreignDescriptor.substring(foreignDescriptor.lastIndexOf('(') + 1, foreignDescriptor.lastIndexOf(')'))) - 1);
      columnNameToForeignKey.put(foreignColumn, foreignKey);
    }

    return tableNameToForeignKeys;
  }
}