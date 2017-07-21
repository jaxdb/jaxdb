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
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.lib4j.io.Readers;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
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
    return new CreateStatement("CALL CREATE_SCHEMA_IF_NOT_EXISTS('" + schema._name$().text() + "')");
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
}