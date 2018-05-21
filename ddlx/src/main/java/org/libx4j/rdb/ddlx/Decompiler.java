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

import java.lang.reflect.InvocationTargetException;
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
import org.lib4j.lang.PackageNotFoundException;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Bigint;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Binary;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Blob;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Boolean;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$ChangeRule;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Char;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Check;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Clob;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Column;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Date;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Datetime;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Decimal;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Double;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Enum;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Float;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$ForeignKey;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Int;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Named;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Smallint;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Time;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Tinyint;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.Schema;
import org.libx4j.rdb.vendor.DBVendor;

abstract class Decompiler {
  private static final Decompiler[] decompilers = new Decompiler[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemContextPackageLoader().loadPackage(Compiler.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Decompiler.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Decompiler decompiler = (Decompiler)cls.getDeclaredConstructor().newInstance();
          decompilers[decompiler.getVendor().ordinal()] = decompiler;
        }
      }
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Decompiler getDecompiler(final DBVendor vendor) {
    final Decompiler decompiler = decompilers[vendor.ordinal()];
    if (decompiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return decompiler;
  }

  public static Schema createDDL(final Connection connection) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Decompiler decompiler = Decompiler.getDecompiler(vendor);
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet tableRows = metaData.getTables(null, null, null, new String[] {"TABLE"});
    final Schema schema = new Schema();
    final Map<String,List<$Check>> tableNameToChecks = decompiler.getCheckConstraints(connection);
    final Map<String,List<$Table.Constraints.Unique>> tableNameToUniques = decompiler.getUniqueConstraints(connection);
    final Map<String,$Table.Indexes> tableNameToIndexes = decompiler.getIndexes(connection);
    final Map<String,Map<String,$ForeignKey>> tableNameToForeignKeys = decompiler.getForeignKeys(connection);
    while (tableRows.next()) {
      final String tableName = tableRows.getString(3);
      final $Table table = new Schema.Table();
      table.setName$(new $Named.Name$(tableName.toLowerCase()));
      schema.addTable(table);

      final ResultSet columnRows = metaData.getColumns(null, null, tableName, null);
      final Map<String,$Column> columnNameToColumn = new HashMap<String,$Column>();
      final Map<Integer,$Column> columnNumberToColumn = new TreeMap<Integer,$Column>();
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

      columnNumberToColumn.values().stream().forEach(c -> table.addColumn(c));

      final ResultSet primaryKeyRows = metaData.getPrimaryKeys(null, null, tableName);
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

      final List<$Table.Constraints.Unique> uniques = tableNameToUniques == null ? null : tableNameToUniques.get(tableName);
      if (uniques != null && uniques.size() > 0) {
        if (table.getConstraints() == null)
          table.setConstraints(new $Table.Constraints());

        for (final $Table.Constraints.Unique unique : uniques)
          table.getConstraints().addUnique(unique);
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

      final $Table.Indexes indexes = tableNameToIndexes == null ? null : tableNameToIndexes.get(tableName);
      if (indexes != null)
        table.setIndexes(indexes);

      final List<$Check> checks = tableNameToChecks == null ? null : tableNameToChecks.get(tableName);
      if (checks != null)
        for (final $Check check : checks)
          addCheck(columnNameToColumn.get(check.getColumn(0).text()), check);

      final Map<String,$ForeignKey> foreignKeys = tableNameToForeignKeys == null ? null : tableNameToForeignKeys.get(tableName);
      if (foreignKeys != null)
        for (final Map.Entry<String,$ForeignKey> entry : foreignKeys.entrySet())
          columnNameToColumn.get(entry.getKey().toLowerCase()).setForeignKey(entry.getValue());
    }

    return schema;
  }

  private static String getType(final short type) {
    return type != 3 ? "HASH" : "BTREE";
  }

  private static void addCheck(final $Column column, final $Check check) {
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

  protected abstract DBVendor getVendor();
  protected abstract $Column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement);
  protected abstract Map<String,List<$Check>> getCheckConstraints(final Connection connection) throws SQLException;
  protected abstract Map<String,List<$Table.Constraints.Unique>> getUniqueConstraints(final Connection connection) throws SQLException;
  protected abstract Map<String,$Table.Indexes> getIndexes(final Connection connection) throws SQLException;

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

  protected Map<String,Map<String,$ForeignKey>> getForeignKeys(final Connection connection) throws SQLException {
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet foreignKeyRows = metaData.getImportedKeys(null, null, null);
    final Map<String,Map<String,$ForeignKey>> tableNameToForeignKeys = new HashMap<String,Map<String,$ForeignKey>>();
    String lastTable = null;
    Map<String,$ForeignKey> columnNameToForeignKey = null;
    while (foreignKeyRows.next()) {
      final String tableName = foreignKeyRows.getString("FKTABLE_NAME").toLowerCase();
      if (!tableName.equals(lastTable)) {
        lastTable = tableName;
        tableNameToForeignKeys.put(tableName, columnNameToForeignKey = new HashMap<String,$ForeignKey>());
      }

      final String primaryTable = foreignKeyRows.getString("PKTABLE_NAME").toLowerCase();
      final String primaryColumn = foreignKeyRows.getString("PKCOLUMN_NAME").toLowerCase();
      final String columnName = foreignKeyRows.getString("FKCOLUMN_NAME").toLowerCase();
      final short updateRule = foreignKeyRows.getShort("UPDATE_RULE");
      final short deleteRule = foreignKeyRows.getShort("DELETE_RULE");
      final $ForeignKey foreignKey = new $Column.ForeignKey();
      foreignKey.setReferences$(new $ForeignKey.References$(primaryTable));
      foreignKey.setColumn$(new $ForeignKey.Column$(primaryColumn));

      final $ChangeRule.Enum onUpdate = toBinding(updateRule);
      if (onUpdate != null)
        foreignKey.setOnUpdate$(new $ForeignKey.OnUpdate$(onUpdate));

      final $ChangeRule.Enum onDelete = toBinding(deleteRule);
      if (onDelete != null)
        foreignKey.setOnDelete$(new $ForeignKey.OnDelete$(onDelete));

      columnNameToForeignKey.put(columnName, foreignKey);
    }

    return tableNameToForeignKeys;
  }

  @SuppressWarnings("unchecked")
  protected static final <T extends $Column>T newColumn(final Class<T> type) {
    if (type == $Bigint.class)
      return (T)new $Bigint() {
        private static final long serialVersionUID = 8340538426557873933L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Binary.class)
      return (T)new $Binary() {
        private static final long serialVersionUID = -4511455354880159839L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Blob.class)
      return (T)new $Blob() {
        private static final long serialVersionUID = -3621793530236960424L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Boolean.class)
      return (T)new $Boolean() {
        private static final long serialVersionUID = -1244791230666618613L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Char.class)
      return (T)new $Char() {
        private static final long serialVersionUID = -1408310289647069164L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Clob.class)
      return (T)new $Clob() {
        private static final long serialVersionUID = 1338245363928630992L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Date.class)
      return (T)new $Date() {
        private static final long serialVersionUID = -2226523068522710931L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Datetime.class)
      return (T)new $Datetime() {
        private static final long serialVersionUID = -8962376436200133621L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Decimal.class)
      return (T)new $Decimal() {
        private static final long serialVersionUID = 5135485324988707324L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Double.class)
      return (T)new $Double() {
        private static final long serialVersionUID = 725203596626982344L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Enum.class)
      return (T)new $Enum() {
        private static final long serialVersionUID = -3659603056107131400L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Float.class)
      return (T)new $Float() {
        private static final long serialVersionUID = -3315953293206810433L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Int.class)
      return (T)new $Int() {
        private static final long serialVersionUID = 1949632547146151337L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Smallint.class)
      return (T)new $Smallint() {
        private static final long serialVersionUID = 1537745275729895670L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Time.class)
      return (T)new $Time() {
        private static final long serialVersionUID = 4269181853044686010L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    if (type == $Tinyint.class)
      return (T)new $Tinyint() {
        private static final long serialVersionUID = -5494299754935677721L;

        @Override
        protected $Named inherits() {
          return null;
        }
      };

    throw new UnsupportedOperationException("Unsupported column type: " + type.getClass().getName());
  }
}