/* Copyright (c) 2015 lib4j
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lib4j.io.Readers;
import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_binary;
import org.libx4j.rdb.ddlx.xe.$ddlx_blob;
import org.libx4j.rdb.ddlx.xe.$ddlx_boolean;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
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
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_time;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DerbyCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(DerbyCompiler.class);

  public static class Procedure {
    public static void createSchemaIfNotExists(final String schemaName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE schemaname = '" + schemaName.toUpperCase() + "'");
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute("CREATE SCHEMA " + schemaName);
      }
    }

    public static void createTableIfNotExists(final String tableName, final Clob createClause) throws IOException, SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName.toUpperCase() + "'");
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute(Readers.readFully(createClause.getCharacterStream()));
      }
    }

    public static void dropSchemaIfExists(final String schemaName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE sysschemas = '" + schemaName.toUpperCase() + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP SCHEMA " + schemaName);
      }
    }

    public static void dropTableIfExists(final String tableName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName.toUpperCase() + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP TABLE " + tableName);
      }
    }

    public static void dropIndexIfExists(final String indexName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysconglomerates WHERE conglomeratename = '" + indexName.toUpperCase() + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP INDEX " + indexName);
      }
    }
  }

  private static void initProcedure(final Statement statement, final String procedure) throws SQLException {
    try {
      statement.execute(procedure);
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      initProcedure(statement, "CREATE PROCEDURE CREATE_SCHEMA_IF_NOT_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".createSchemaIfNotExists'");
      initProcedure(statement, "CREATE PROCEDURE CREATE_TABLE_IF_NOT_EXISTS(IN tableName VARCHAR(128), IN createClause CLOB) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".createTableIfNotExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_SCHEMA_IF_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropSchemaIfExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_TABLE_IF_EXISTS(IN tableName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropTableIfExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_INDEX_IF_EXISTS(IN indexName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropIndexIfExists'");
    }
  }

  @Override
  protected CreateStatement createSchemaIfNotExists(final ddlx_schema schema) {
    // NOTE: This has been commented out because it should not be up to rdb to set which schema the tables are written to.
    // NOTE: Derby's schema is like Oracle's schema -- a user-space differentiating facet.
    return null; //new CreateStatement("CALL CREATE_SCHEMA_IF_NOT_EXISTS('" + schema._name$().text() + "')");
  }

  @Override
  protected CreateStatement createTableIfNotExists(final $ddlx_table table, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    return new CreateStatement("CALL CREATE_TABLE_IF_NOT_EXISTS('" + table._name$().text() + "', '" + super.createTableIfNotExists(table, columnNameToColumn).getSql().replace("'", "''") + "')");
  }

  @Override
  protected DropStatement dropTableIfExists(final $ddlx_table table) {
    return new DropStatement("CALL DROP_TABLE_IF_EXISTS('" + table._name$().text() + "')");
  }

  @Override
  protected DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("CALL DROP_INDEX_IF_EXISTS('" + indexName + "')");
  }

  @Override
  protected String onUpdate(final $ddlx_foreignKey._onUpdate$ onUpdate) {
    return null;
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() && !column._null$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return isAutoIncrement(column) ? "GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)" : "";
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $ddlx_index._type$ type, final String tableName, final $ddlx_named ... columns) {
    if ($ddlx_index._type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by Derby's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }

  @Override
  protected $ddlx_column makeColumn(final String columnName, final String typeName, final int size, final int decimalDigits, final String _default, final Boolean nullable, final Boolean autoIncrement) {
    final $ddlx_column column;
    if ("BIGINT".equals(typeName)) {
      final $ddlx_bigint type = newColumn($ddlx_bigint.class);
      type._precision$(new $ddlx_bigint._precision$((byte)size));
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
      type._precision$(new $ddlx_datetime._precision$((byte)size));
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

  private static final String tables = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, c.columnnumber, c.columnname ")
    .append("FROM sys.syscolumns c ")
    .append("JOIN sys.systables t ON t.tableid = c.referenceid ")
    .append("JOIN sys.sysschemas s ON t.schemaid = s.schemaid ")
    .append("WHERE s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename, c.columnnumber").toString();

  private static Map<String,List<String>> getTables(final Connection connection) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(tables);
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

  private static final String constraints = new StringBuilder()
    .append("SELECT s.schemaname, t.tablename, CAST(cg.descriptor AS VARCHAR(255)) ")
    .append("FROM sys.sysconstraints c ")
    .append("JOIN sys.systables t ON c.tableid = t.tableid ")
    .append("JOIN sys.sysschemas s ON s.schemaid = c.schemaid ")
    .append("JOIN sys.syskeys k ON k.constraintid = c.constraintid ")
    .append("JOIN sys.sysconglomerates cg ON k.conglomerateid = cg.conglomerateid ")
    .append("WHERE c.state = 'E' ")
    .append("AND c.type = 'U' ")
    .append("AND s.schemaname = CURRENT SCHEMA ")
    .append("ORDER BY s.schemaname, t.tablename").toString();

  @Override
  protected Map<String,List<$ddlx_table._constraints._unique>> getUniqueConstraints(final Connection connection) throws SQLException {
    final Map<String,List<String>> tableNameToColumns = getTables(connection);
    final PreparedStatement statement = connection.prepareStatement(constraints);
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
}