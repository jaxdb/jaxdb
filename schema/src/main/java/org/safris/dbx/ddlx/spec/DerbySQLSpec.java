/* Copyright (c) 2015 Seva Safris
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

package org.safris.dbx.ddlx.spec;

import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.safris.commons.io.Readers;
import org.safris.dbx.ddlx.GeneratorExecutionException;
import org.safris.dbx.ddlx.SQLDataTypes;
import org.safris.dbx.ddlx.xe.$ddlx_column;
import org.safris.dbx.ddlx.xe.$ddlx_enum;
import org.safris.dbx.ddlx.xe.$ddlx_foreignKey;
import org.safris.dbx.ddlx.xe.$ddlx_integer;
import org.safris.dbx.ddlx.xe.$ddlx_named;
import org.safris.dbx.ddlx.xe.$ddlx_table;
import org.safris.dbx.ddlx.xe.ddlx_schema;

public final class DerbySQLSpec extends SQLSpec {
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

  @Override
  public void init(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      statement.execute("CREATE PROCEDURE CREATE_SCHEMA_IF_NOT_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + DerbySQLSpec.class.getName() + ".createSchemaIfNotExists'");
      statement.execute("CREATE PROCEDURE CREATE_TABLE_IF_NOT_EXISTS(IN tableName VARCHAR(128), IN createClause CLOB) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + DerbySQLSpec.class.getName() + ".createTableIfNotExists'");
      statement.execute("CREATE PROCEDURE DROP_SCHEMA_IF_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + DerbySQLSpec.class.getName() + ".dropSchemaIfExists'");
      statement.execute("CREATE PROCEDURE DROP_TABLE_IF_EXISTS(IN tableName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + DerbySQLSpec.class.getName() + ".dropTableIfExists'");
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  public String createSchemaIfNotExists(final ddlx_schema schema) {
    return "CALL CREATE_SCHEMA_IF_NOT_EXISTS('" + schema._name$().text() + "')";
  }

  @Override
  public String createTableIfNotExists(final $ddlx_table table, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    return "CALL CREATE_TABLE_IF_NOT_EXISTS('" + table._name$().text() + "', '" + super.createTableIfNotExists(table, columnNameToColumn).replace("'", "''") + "')";
  }

  @Override
  public String dropTableIfExists(final $ddlx_table table) {
    return "CALL DROP_TABLE_IF_EXISTS('" + table._name$().text() + "')";
  }

  @Override
  protected String onUpdate(final $ddlx_foreignKey._onUpdate$ onUpdate) {
    return null;
  }

  @Override
  public String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() && !column._null$().text() ? "NOT NULL" : "";
  }

  @Override
  public String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return !column._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? "GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)" : "";
  }

  @Override
  public String truncate(final String tableName) {
    return "DELETE FROM " + tableName;
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $ddlx_named ... columns) {
    return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")";
  }
  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE" : "FLOAT";
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareBinary(final boolean varying, final long length) {
    return "CHAR" + (varying ? " VARYING" : "") + "(" + length + ") FOR BIT DATA";
  }

  @Override
  public String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final long length) {
    return "CLOB(" + length + ")";
  }

  @Override
  public String declareBlob(final long length) {
    return "BLOB" + "(" + length + ")";
  }

  @Override
  public String declareDecimal(final short precision, final short scale, final boolean unsigned) {
    SQLDataTypes.checkValidNumber(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "TIMESTAMP";
  }

  @Override
  public String declareTime(final short precision) {
    return "TIME";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareInt8(final short precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  public String declareInt16(final short precision, final boolean unsigned) {
    return unsigned ? "INTEGER" : "SMALLINT";
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    return unsigned ? "BIGINT" : "INTEGER";
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    if (type._values$().isNull())
      return "VARCHAR(0)";

    final List<String> enums = parseEnum(type._values$().text());
    int maxLength = 0;
    for (final String value : enums)
      maxLength = Math.max(maxLength, value.length());

    return "VARCHAR(" + maxLength + ")";
  }
}