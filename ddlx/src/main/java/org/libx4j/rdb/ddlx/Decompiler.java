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

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.lib4j.lang.PackageLoader;
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
import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_time;
import org.libx4j.rdb.ddlx.xe.$ddlx_tinyint;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;

abstract class Decompiler {
  private static final Decompiler[] decompilers = new Decompiler[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemContextPackageLoader().loadPackage(Compiler.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Decompiler.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Decompiler decompiler = (Decompiler)cls.newInstance();
          decompilers[decompiler.getVendor().ordinal()] = decompiler;
        }
      }
    }
    catch (final ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Decompiler getDecompiler(final DBVendor vendor) {
    final Decompiler decompiler = decompilers[vendor.ordinal()];
    if (decompiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return decompiler;
  }

  public static ddlx_schema createDDL(final Connection connection) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Decompiler decompiler = Decompiler.getDecompiler(vendor);
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet tableRows = metaData.getTables(null, null, null, new String[] {"TABLE"});
    final ddlx_schema schema = new ddlx_schema();
    final Map<String,List<$ddlx_check>> tableNameToChecks = decompiler.getCheckConstraints(connection);
    final Map<String,List<$ddlx_table._constraints._unique>> tableNameToUniques = decompiler.getUniqueConstraints(connection);
    final Map<String,$ddlx_table._indexes> tableNameToIndexes = decompiler.getIndexes(connection);
    final Map<String,Map<String,$ddlx_foreignKey>> tableNameToForeignKeys = decompiler.getForeignKeys(connection);
    while (tableRows.next()) {
      final String tableName = tableRows.getString(3);
      final $ddlx_table table = new ddlx_schema._table();
      table._name$(new $ddlx_named._name$(tableName.toLowerCase()));
      schema._table(table);

      final ResultSet columnRows = metaData.getColumns(null, null, tableName, null);
      final Map<String,$ddlx_column> columnNameToColumn = new HashMap<String,$ddlx_column>();
      final Map<Integer,$ddlx_column> columnNumberToColumn = new TreeMap<Integer,$ddlx_column>();
      while (columnRows.next()) {
        final String columnName = columnRows.getString("COLUMN_NAME").toLowerCase();
        final String typeName = columnRows.getString("TYPE_NAME");
        final int columnSize = columnRows.getInt("COLUMN_SIZE");
        final String _default = columnRows.getString("COLUMN_DEF");
        final int index = columnRows.getInt("ORDINAL_POSITION");
        final String nullable = columnRows.getString("IS_NULLABLE");
        final String autoIncrement = columnRows.getString("IS_AUTOINCREMENT");
        final int decimalDigits = columnRows.getInt("DECIMAL_DIGITS");
        final $ddlx_column column = decompiler.makeColumn(columnName.toLowerCase(), typeName, columnSize, decimalDigits, _default, nullable.length() == 0 ? null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
        columnNameToColumn.put(columnName, column);
        columnNumberToColumn.put(index, column);
      }

      columnNumberToColumn.values().stream().forEach(c -> table._column(c));

      final ResultSet primaryKeyRows = metaData.getPrimaryKeys(null, null, tableName);
      while (primaryKeyRows.next()) {
        final String columnName = primaryKeyRows.getString("COLUMN_NAME").toLowerCase();
        if (table._constraints() == null)
          table._constraints(new $ddlx_table._constraints());

        if (table._constraints(0)._primaryKey() == null)
          table._constraints(0)._primaryKey(new $ddlx_table._constraints._primaryKey());

        final $ddlx_table._constraints._primaryKey._column column = new $ddlx_table._constraints._primaryKey._column();
        column._name$(new $ddlx_table._constraints._primaryKey._column._name$(columnName));
        table._constraints(0)._primaryKey(0)._column(column);
      }

      final List<$ddlx_table._constraints._unique> uniques = tableNameToUniques == null ? null : tableNameToUniques.get(tableName);
      if (uniques != null && uniques.size() > 0) {
        if (table._constraints() == null)
          table._constraints(new $ddlx_table._constraints());

        for (final $ddlx_table._constraints._unique unique : uniques)
          table._constraints(0)._unique(unique);
      }

      final ResultSet indexRows = metaData.getIndexInfo(null, null, tableName, false, true);
      final Map<String,TreeMap<Short,String>> indexNameToIndex = new HashMap<String,TreeMap<Short,String>>();
      final Map<String,String> indexNameToType = new HashMap<String,String>();
      final Map<String,Boolean> indexNameToUnique = new HashMap<String,Boolean>();
      while (indexRows.next()) {
        final String columnName = indexRows.getString("COLUMN_NAME").toLowerCase();
        if (columnName == null)
          continue;

        final String indexName = indexRows.getString("INDEX_NAME").toLowerCase();
        TreeMap<Short,String> indexes = indexNameToIndex.get(indexName);
        if (indexes == null)
          indexNameToIndex.put(indexName, indexes = new TreeMap<Short,String>());

        final short ordinalPosition = indexRows.getShort("ORDINAL_POSITION");
        indexes.put(ordinalPosition, columnName);

        final String type = getType(indexRows.getShort("TYPE"));
        final String currentType = indexNameToType.get(indexName);
        if (currentType == null)
          indexNameToType.put(indexName, type);
        else if (!type.equals(currentType))
          throw new IllegalStateException("Expected " + type + " = " + currentType);

        final boolean unique = !indexRows.getBoolean("NON_UNIQUE");
        final Boolean currentUnique = indexNameToUnique.get(indexName);
        if (currentUnique == null)
          indexNameToUnique.put(indexName, unique);
        else if (unique != currentUnique)
          throw new IllegalStateException("Expected " + unique + " = " + currentType);
      }

      final $ddlx_table._indexes indexes = tableNameToIndexes == null ? null : tableNameToIndexes.get(tableName);
      if (indexes != null)
        table._indexes(indexes);

      final List<$ddlx_check> checks = tableNameToChecks == null ? null : tableNameToChecks.get(tableName);
      if (checks != null)
        for (final $ddlx_check check : checks)
          addCheck(columnNameToColumn.get(check._column(0).text()), check);

      final Map<String,$ddlx_foreignKey> foreignKeys = tableNameToForeignKeys == null ? null : tableNameToForeignKeys.get(tableName);
      if (foreignKeys != null)
        for (final Map.Entry<String,$ddlx_foreignKey> entry : foreignKeys.entrySet())
          columnNameToColumn.get(entry.getKey().toLowerCase())._foreignKey(entry.getValue());
    }

    return schema;
  }

  private static String getType(final short type) {
    return type != 3 ? "HASH" : "BTREE";
  }

  private static void addCheck(final $ddlx_column column, final $ddlx_check check) {
    if (column instanceof $ddlx_char)
      dt.CHAR.addCheck(($ddlx_char)column, check);
    else if (column instanceof $ddlx_tinyint)
      dt.TINYINT.addCheck(($ddlx_tinyint)column, check);
    else if (column instanceof $ddlx_smallint)
      dt.SMALLINT.addCheck(($ddlx_smallint)column, check);
    else if (column instanceof $ddlx_int)
      dt.INT.addCheck(($ddlx_int)column, check);
    else if (column instanceof $ddlx_bigint)
      dt.BIGINT.addCheck(($ddlx_bigint)column, check);
    else if (column instanceof $ddlx_float)
      dt.FLOAT.addCheck(($ddlx_float)column, check);
    else if (column instanceof $ddlx_double)
      dt.DOUBLE.addCheck(($ddlx_double)column, check);
    else if (column instanceof $ddlx_decimal)
      dt.DECIMAL.addCheck(($ddlx_decimal)column, check);
    else
      throw new UnsupportedOperationException("Unsupported check for column type: " + column.getClass().getName());
  }

  protected abstract DBVendor getVendor();
  protected abstract $ddlx_column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement);
  protected abstract Map<String,List<$ddlx_check>> getCheckConstraints(final Connection connection) throws SQLException;
  protected abstract Map<String,List<$ddlx_table._constraints._unique>> getUniqueConstraints(final Connection connection) throws SQLException;
  protected abstract Map<String,$ddlx_table._indexes> getIndexes(final Connection connection) throws SQLException;

  private static $ddlx_changeRule.Enum toBinding(final short rule) {
    if (rule == 1)
      return null;

    if (rule == 2)
      return $ddlx_changeRule.CASCADE;

    if (rule == 3)
      return $ddlx_changeRule.SET_20NULL;

    if (rule == 4)
      return $ddlx_changeRule.SET_20DEFAULT;

    if (rule == 5)
      return $ddlx_changeRule.RESTRICT;

    throw new UnsupportedOperationException("Unsupported change rule: " + rule);
  }

  protected Map<String,Map<String,$ddlx_foreignKey>> getForeignKeys(final Connection connection) throws SQLException {
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet foreignKeyRows = metaData.getImportedKeys(null, null, null);
    final Map<String,Map<String,$ddlx_foreignKey>> tableNameToForeignKeys = new HashMap<String,Map<String,$ddlx_foreignKey>>();
    String lastTable = null;
    Map<String,$ddlx_foreignKey> columnNameToForeignKey = null;
    while (foreignKeyRows.next()) {
      final String tableName = foreignKeyRows.getString("FKTABLE_NAME").toLowerCase();
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<String,$ddlx_foreignKey>());
      }

      final String primaryTable = foreignKeyRows.getString("PKTABLE_NAME").toLowerCase();
      final String primaryColumn = foreignKeyRows.getString("PKCOLUMN_NAME").toLowerCase();
      final String columnName = foreignKeyRows.getString("FKCOLUMN_NAME").toLowerCase();
      final short updateRule = foreignKeyRows.getShort("UPDATE_RULE");
      final short deleteRule = foreignKeyRows.getShort("DELETE_RULE");
      final $ddlx_foreignKey foreignKey = new $ddlx_column._foreignKey();
      foreignKey._references$(new $ddlx_foreignKey._references$(primaryTable));
      foreignKey._column$(new $ddlx_foreignKey._column$(primaryColumn));

      if (primaryTable.equalsIgnoreCase("t_bigint") && primaryColumn.equalsIgnoreCase("c_auto")) {
        System.out.println();
      }

      final $ddlx_changeRule.Enum onUpdate = toBinding(updateRule);
      if (onUpdate != null)
        foreignKey._onUpdate$(new $ddlx_foreignKey._onUpdate$(onUpdate));

      final $ddlx_changeRule.Enum onDelete = toBinding(deleteRule);
      if (onDelete != null)
        foreignKey._onDelete$(new $ddlx_foreignKey._onDelete$(onDelete));

      columnNameToForeignKey.put(columnName, foreignKey);
    }

    return tableNameToForeignKeys;
  }

  @SuppressWarnings("unchecked")
  protected static final <T extends $ddlx_column>T newColumn(final Class<T> type) {
    if (type == $ddlx_bigint.class)
      return (T)new $ddlx_bigint() {
        private static final long serialVersionUID = 8340538426557873933L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_binary.class)
      return (T)new $ddlx_binary() {
        private static final long serialVersionUID = -4511455354880159839L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_blob.class)
      return (T)new $ddlx_blob() {
        private static final long serialVersionUID = -3621793530236960424L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_boolean.class)
      return (T)new $ddlx_boolean() {
        private static final long serialVersionUID = -1244791230666618613L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_char.class)
      return (T)new $ddlx_char() {
        private static final long serialVersionUID = -1408310289647069164L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_clob.class)
      return (T)new $ddlx_clob() {
        private static final long serialVersionUID = 1338245363928630992L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_date.class)
      return (T)new $ddlx_date() {
        private static final long serialVersionUID = -2226523068522710931L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_datetime.class)
      return (T)new $ddlx_datetime() {
        private static final long serialVersionUID = -8962376436200133621L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_decimal.class)
      return (T)new $ddlx_decimal() {
        private static final long serialVersionUID = 5135485324988707324L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_double.class)
      return (T)new $ddlx_double() {
        private static final long serialVersionUID = 725203596626982344L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_enum.class)
      return (T)new $ddlx_enum() {
        private static final long serialVersionUID = -3659603056107131400L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_float.class)
      return (T)new $ddlx_float() {
        private static final long serialVersionUID = -3315953293206810433L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_int.class)
      return (T)new $ddlx_int() {
        private static final long serialVersionUID = 1949632547146151337L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_smallint.class)
      return (T)new $ddlx_smallint() {
        private static final long serialVersionUID = 1537745275729895670L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_time.class)
      return (T)new $ddlx_time() {
        private static final long serialVersionUID = 4269181853044686010L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    if (type == $ddlx_tinyint.class)
      return (T)new $ddlx_tinyint() {
        private static final long serialVersionUID = -5494299754935677721L;

        @Override
        protected $ddlx_named inherits() {
          return null;
        }
      };

    throw new UnsupportedOperationException("Unsupported column type: " + type.getClass().getName());
  }
}