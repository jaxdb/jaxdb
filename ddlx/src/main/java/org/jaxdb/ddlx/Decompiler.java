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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.TreeMap;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$BigintCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$CharCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$CheckColumn;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$CheckReference;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$DecimalCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$DoubleCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$FloatCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnDelete$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.OnUpdate$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKey.References$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyUnary.Column$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$IntCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$SmallintCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$TinyintCheck;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxsb.runtime.BindingList;

abstract class Decompiler {
  private static final Decompiler[] decompilers = {/*new DB2Decompiler()*/null, new DerbyDecompiler(), /*new MariaDBDecompiler()*/null, /*new MySQLDecompiler()*/null, /*new OracleDecompiler()*/null, /*new PostgreSQLDecompiler()*/null, new SQLiteDecompiler()};

  static Decompiler getDecompiler(final DBVendor vendor) {
    final Decompiler decompiler = decompilers[vendor.ordinal()];
    if (decompiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return decompiler;
  }

  public static Schema createDDL(final Connection connection) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Decompiler decompiler = Decompiler.getDecompiler(vendor);
    final DatabaseMetaData metaData = connection.getMetaData();
    try (final ResultSet tableRows = metaData.getTables(null, null, null, new String[] {"TABLE"})) {
      final Schema schema = new Schema();
      final Map<String,BindingList<$CheckReference>> tableNameToChecks = decompiler.getCheckConstraints(connection);
      final Map<String,BindingList<$Table.Constraints.Unique>> tableNameToUniques = decompiler.getUniqueConstraints(connection);
      final Map<String,$Table.Indexes> tableNameToIndexes = decompiler.getIndexes(connection);
      final Map<String,Map<String,$ForeignKeyUnary>> tableNameToForeignKeys = decompiler.getForeignKeys(connection);
      final Map<String,$Column> columnNameToColumn = new HashMap<>();
      final Map<Integer,$Column> columnNumberToColumn = new TreeMap<>();
      final Map<String,TreeMap<Short,String>> indexNameToIndex = new HashMap<>();
      final Map<String,String> indexNameToType = new HashMap<>();
      final Map<String,Boolean> indexNameToUnique = new HashMap<>();
      while (tableRows.next()) {
        final String tableName = tableRows.getString(3);
        final $Table table = new Schema.Table();
        table.setName$(new $Named.Name$(tableName.toLowerCase()));
        schema.addTable(table);

        try (final ResultSet columnRows = metaData.getColumns(null, null, tableName, null)) {
          while (columnRows.next()) {
            final String columnName = columnRows.getString("COLUMN_NAME").toLowerCase();
            final String typeName = columnRows.getString("TYPE_NAME");
            final int columnSize = columnRows.getInt("COLUMN_SIZE");
            final String _default = columnRows.getString("COLUMN_DEF");
            final int index = columnRows.getInt("ORDINAL_POSITION");
            final String nullable = columnRows.getString("IS_NULLABLE");
            final String autoIncrement = columnRows.getString("IS_AUTOINCREMENT");
            final int decimalDigits = columnRows.getInt("DECIMAL_DIGITS");
            final $Column column = decompiler.makeColumn(columnName.toLowerCase(), typeName, columnSize, decimalDigits, _default, nullable.length() == 0 ? null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
            columnNameToColumn.put(columnName, column);
            columnNumberToColumn.put(index, column);
          }

          columnNumberToColumn.values().forEach(table::addColumn);

          try (final ResultSet primaryKeyRows = metaData.getPrimaryKeys(null, null, tableName)) {
            while (primaryKeyRows.next()) {
              final String columnName = primaryKeyRows.getString("COLUMN_NAME").toLowerCase();
              if (table.getConstraints() == null)
                table.setConstraints(new $Table.Constraints());

              if (table.getConstraints().getPrimaryKey() == null)
                table.getConstraints().setPrimaryKey(new $Table.Constraints.PrimaryKey());

              final $Table.Constraints.PrimaryKey.Column column = new $Table.Constraints.PrimaryKey.Column();
              column.setName$(new $Table.Constraints.PrimaryKey.Column.Name$(columnName));
              table.getConstraints().getPrimaryKey().addColumn(column);
            }
          }

          final BindingList<$Table.Constraints.Unique> uniques = tableNameToUniques == null ? null : tableNameToUniques.get(tableName);
          if (uniques != null && uniques.size() > 0) {
            if (table.getConstraints() == null)
              table.setConstraints(new $Table.Constraints());

            for (int i = 0, i$ = uniques.size(); i < i$; ++i) // [RA]
              table.getConstraints().addUnique(uniques.get(i));
          }

          try (final ResultSet indexRows = metaData.getIndexInfo(null, null, tableName, false, true)) {
            while (indexRows.next()) {
              final String columnName = indexRows.getString("COLUMN_NAME").toLowerCase();
              if (columnName == null)
                continue;

              final String indexName = indexRows.getString("INDEX_NAME").toLowerCase();
              TreeMap<Short,String> indexes = indexNameToIndex.get(indexName);
              if (indexes == null)
                indexNameToIndex.put(indexName, indexes = new TreeMap<>());

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
          }

          final $Table.Indexes indexes = tableNameToIndexes == null ? null : tableNameToIndexes.get(tableName);
          if (indexes != null)
            table.setIndexes(indexes);

          final BindingList<$CheckReference> checks = tableNameToChecks == null ? null : tableNameToChecks.get(tableName);
          if (checks != null) {
            for (int i = 0, i$ = checks.size(); i < i$; ++i) { // [RA]
              final $CheckReference check = checks.get(i);
              addCheck(columnNameToColumn.get(check.getColumn$().text()), check);
            }
          }

          final Map<String,$ForeignKeyUnary> foreignKeys = tableNameToForeignKeys == null ? null : tableNameToForeignKeys.get(tableName);
          if (foreignKeys != null)
            for (final Map.Entry<String,$ForeignKeyUnary> entry : foreignKeys.entrySet()) // [S]
              columnNameToColumn.get(entry.getKey().toLowerCase()).setForeignKey(entry.getValue());
        }

        columnNameToColumn.clear();
        columnNumberToColumn.clear();
        indexNameToIndex.clear();
        indexNameToType.clear();
      }

      return schema;
    }
  }

  private static String getType(final short type) {
    return type != 3 ? "HASH" : "BTREE";
  }

  private static void addCheck(final $Column column, final $CheckReference check) {
    if (column instanceof $Char)
      dt.CHAR.addCheck(($Char)column, check);
    else if (column instanceof $Tinyint)
      dt.TINYINT.addCheck(($Tinyint)column, check);
    else if (column instanceof $Smallint)
      dt.SMALLINT.addCheck(($Smallint)column, check);
    else if (column instanceof $Int)
      dt.INT.addCheck(($Int)column, check);
    else if (column instanceof $Bigint)
      dt.BIGINT.addCheck(($Bigint)column, check);
    else if (column instanceof $Float)
      dt.FLOAT.addCheck(($Float)column, check);
    else if (column instanceof $Double)
      dt.DOUBLE.addCheck(($Double)column, check);
    else if (column instanceof $Decimal)
      dt.DECIMAL.addCheck(($Decimal)column, check);
    else
      throw new UnsupportedOperationException("Unsupported check for column type: " + column.getClass().getName());
  }

  private static void addCheck(final $Column column, final $CheckColumn check) {
    if (check instanceof $CharCheck)
      dt.CHAR.addCheck(($Char)column, ($CharCheck)check);
    else if (check instanceof $TinyintCheck)
      dt.TINYINT.addCheck(($Tinyint)column, ($TinyintCheck)check);
    else if (check instanceof $SmallintCheck)
      dt.SMALLINT.addCheck(($Smallint)column, ($SmallintCheck)check);
    else if (check instanceof $IntCheck)
      dt.INT.addCheck(($Int)column, ($IntCheck)check);
    else if (check instanceof $BigintCheck)
      dt.BIGINT.addCheck(($Bigint)column, ($BigintCheck)check);
    else if (check instanceof $FloatCheck)
      dt.FLOAT.addCheck(($Float)column, ($FloatCheck)check);
    else if (check instanceof $DoubleCheck)
      dt.DOUBLE.addCheck(($Double)column, ($DoubleCheck)check);
    else if (check instanceof $DecimalCheck)
      dt.DECIMAL.addCheck(($Decimal)column, ($DecimalCheck)check);
    else
      throw new UnsupportedOperationException("Unsupported check for column type: " + check.getClass().getName());
  }

  abstract DBVendor getVendor();
  abstract $Column makeColumn(String columnName, String typeName, long size, int decimalDigits, String _default, Boolean nullable, Boolean autoIncrement);
  abstract <L extends List<$CheckReference> & RandomAccess>Map<String,L> getCheckConstraints(Connection connection) throws SQLException;
  abstract <L extends List<$Table.Constraints.Unique> & RandomAccess>Map<String,L> getUniqueConstraints(Connection connection) throws SQLException;
  abstract Map<String,$Table.Indexes> getIndexes(Connection connection) throws SQLException;

  private static $ChangeRule.Enum toBinding(final short rule) {
    if (rule == 1)
      return null;

    if (rule == 2)
      return $ChangeRule.CASCADE;

    if (rule == 3)
      return $ChangeRule.SET_20NULL;

    if (rule == 4)
      return $ChangeRule.SET_20DEFAULT;

    if (rule == 5)
      return $ChangeRule.RESTRICT;

    throw new UnsupportedOperationException("Unsupported change rule: " + rule);
  }

  @SuppressWarnings("null")
  // FIXME: This does not support composite foreign keys
  Map<String,Map<String,$ForeignKeyUnary>> getForeignKeys(final Connection connection) throws SQLException {
    final DatabaseMetaData metaData = connection.getMetaData();
    try (final ResultSet foreignKeyRows = metaData.getImportedKeys(null, null, null)) {
      final Map<String,Map<String,$ForeignKeyUnary>> tableNameToForeignKeys = new HashMap<>();
      String lastTable = null;
      Map<String,$ForeignKeyUnary> columnNameToForeignKey = null;
      while (foreignKeyRows.next()) {
        final String tableName = foreignKeyRows.getString("FKTABLE_NAME").toLowerCase();
        if (!tableName.equals(lastTable)) {
          lastTable = tableName;
          tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<>());
        }

        final String primaryTable = foreignKeyRows.getString("PKTABLE_NAME").toLowerCase();
        final String primaryColumn = foreignKeyRows.getString("PKCOLUMN_NAME").toLowerCase();
        final String columnName = foreignKeyRows.getString("FKCOLUMN_NAME").toLowerCase();
        final short updateRule = foreignKeyRows.getShort("UPDATE_RULE");
        final short deleteRule = foreignKeyRows.getShort("DELETE_RULE");
        final $ForeignKeyUnary foreignKey = new $Column.ForeignKey();
        foreignKey.setReferences$(new References$(primaryTable));
        foreignKey.setColumn$(new Column$(primaryColumn));

        final $ChangeRule.Enum onUpdate = toBinding(updateRule);
        if (onUpdate != null)
          foreignKey.setOnUpdate$(new OnUpdate$(onUpdate));

        final $ChangeRule.Enum onDelete = toBinding(deleteRule);
        if (onDelete != null)
          foreignKey.setOnDelete$(new OnDelete$(onDelete));

        columnNameToForeignKey.put(columnName, foreignKey);
      }

      return tableNameToForeignKeys;
    }
  }

  @SuppressWarnings("unchecked")
  static <T extends $Column>T newColumn(final Class<T> type) {
    if (type == $Bigint.class)
      return (T)new $Bigint() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Binary.class)
      return (T)new $Binary() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Blob.class)
      return (T)new $Blob() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Boolean.class)
      return (T)new $Boolean() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Char.class)
      return (T)new $Char() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Clob.class)
      return (T)new $Clob() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Date.class)
      return (T)new $Date() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Datetime.class)
      return (T)new $Datetime() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

      if (type == $Decimal.class)
        return (T)new $Decimal() {
          @Override
          protected $Named inherits() {
            return null;
          }
        };

    if (type == $Double.class)
      return (T)new $Double() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Enum.class)
      return (T)new $Enum() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Float.class)
      return (T)new $Float() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Int.class)
      return (T)new $Int() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Smallint.class)
      return (T)new $Smallint() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Time.class)
      return (T)new $Time() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Tinyint.class)
      return (T)new $Tinyint() {
        @Override
        protected $Named inherits() {
          return null;
        }
      };

    throw new UnsupportedOperationException("Unsupported column type: " + type.getName());
  }
}