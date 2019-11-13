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

import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$ForeignKey;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.Schema;
import org.libj.io.Readers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DerbyCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(DerbyCompiler.class);

  public static class Procedure {
    public static void createSchemaIfNotExists(final String schemaName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE schemaname = '" + schemaName + "'");
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute("CREATE SCHEMA \"" + schemaName + "\"");
      }
    }

    public static void createTableIfNotExists(final String tableName, final Clob createClause) throws IOException, SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName + "'");
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute(Readers.readFully(createClause.getCharacterStream()));
      }
    }

    public static void dropSchemaIfExists(final String schemaName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE sysschemas = '" + schemaName + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP SCHEMA \"" + schemaName + "\"");
      }
    }

    public static void dropTableIfExists(final String tableName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP TABLE \"" + tableName + "\"");
      }
    }

    public static void dropIndexIfExists(final String indexName) throws SQLException {
      try (final Connection connection = DriverManager.getConnection("jdbc:default:connection")) {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysconglomerates WHERE conglomeratename = '" + indexName + "'");
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP INDEX \"" + indexName + "\"");
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
  protected CreateStatement createSchemaIfNotExists(final Schema schema) {
    // NOTE: This has been commented out because it should not be up to jaxdb to set which schema the tables are written to.
    // NOTE: Derby's schema is like Oracle's schema -- a user-space differentiating facet.
    return null; //new CreateStatement("CALL CREATE_SCHEMA_IF_NOT_EXISTS('" + schema.Name$().text() + "')");
  }

  @Override
  protected CreateStatement createTableIfNotExists(final $Table table, final Map<String,$Column> columnNameToColumn) throws GeneratorExecutionException {
    return new CreateStatement("CALL CREATE_TABLE_IF_NOT_EXISTS('" + table.getName$().text() + "', '" + super.createTableIfNotExists(table, columnNameToColumn).getSql().replace("'", "''") + "')");
  }

  @Override
  protected DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("CALL DROP_TABLE_IF_EXISTS('" + table.getName$().text() + "')");
  }

  @Override
  protected DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("CALL DROP_INDEX_IF_EXISTS('" + indexName + "')");
  }

  @Override
  protected String onUpdate(final $ForeignKey.OnUpdate$ onUpdate) {
    return null;
  }

  @Override
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null && !column.getNull$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    return isAutoIncrement(column) ? "GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)" : "";
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return " ON " + q(table.getName$().text());
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    if ($Index.Type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by Derby's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getVendor().getDialect(), columns) + ")");
  }
}