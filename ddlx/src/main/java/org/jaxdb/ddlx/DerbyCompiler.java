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
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxdb.ddlx.Generator.ColumnRef;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ChangeRule;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Constraints.PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.libj.io.Readers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DerbyCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(DerbyCompiler.class);

  public static final class Procedure {
    public static void createSchemaIfNotExists(final String schemaName) throws SQLException {
      try (
        final Connection connection = DriverManager.getConnection("jdbc:default:connection");
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE schemaname = '" + schemaName + "'");
      ) {
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute("CREATE SCHEMA \"" + schemaName + "\"");
      }
    }

    public static void createTableIfNotExists(final String tableName, final Clob createClause) throws IOException, SQLException {
      try (
        final Connection connection = DriverManager.getConnection("jdbc:default:connection");
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName + "'");
      ) {
        if (!resultSet.next() || resultSet.getInt(1) < 1)
          statement.execute(Readers.readFully(createClause.getCharacterStream()));
      }
    }

    public static void dropSchemaIfExists(final String schemaName) throws SQLException {
      try (
        final Connection connection = DriverManager.getConnection("jdbc:default:connection");
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysschemas WHERE sysschemas = '" + schemaName + "'");
      ) {
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP SCHEMA \"" + schemaName + "\"");
      }
    }

    public static void dropTableIfExists(final String tableName) throws SQLException {
      try (
        final Connection connection = DriverManager.getConnection("jdbc:default:connection");
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.systables WHERE tablename = '" + tableName + "'");
      ) {
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP TABLE \"" + tableName + "\"");
      }
    }

    public static void dropIndexIfExists(final String indexName) throws SQLException {
      try (
        final Connection connection = DriverManager.getConnection("jdbc:default:connection");
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sys.sysconglomerates WHERE conglomeratename = '" + indexName + "'");
      ) {
        if (resultSet.next() && resultSet.getInt(1) > 0)
          statement.execute("DROP INDEX \"" + indexName + "\"");
      }
    }

    private Procedure() {
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

  DerbyCompiler() {
    super(DbVendor.DERBY);
  }

  @Override
  void init(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      initProcedure(statement, "CREATE PROCEDURE CREATE_SCHEMA_IF_NOT_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".createSchemaIfNotExists'");
      initProcedure(statement, "CREATE PROCEDURE CREATE_TABLE_IF_NOT_EXISTS(IN tableName VARCHAR(128), IN createClause CLOB) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".createTableIfNotExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_SCHEMA_IF_EXISTS(IN schemaName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropSchemaIfExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_TABLE_IF_EXISTS(IN tableName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropTableIfExists'");
      initProcedure(statement, "CREATE PROCEDURE DROP_INDEX_IF_EXISTS(IN indexName VARCHAR(128)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME '" + Procedure.class.getName() + ".dropIndexIfExists'");
    }
  }

  @Override
  CreateStatement createSchemaIfNotExists(final Schema schema) {
    // NOTE: This has been commented out because it should not be up to jaxdb to set which schema the tables are written to.
    // NOTE: Derby's schema is like Oracle's schema -- a user-space differentiating facet.
    return null; //new CreateStatement("CALL CREATE_SCHEMA_IF_NOT_EXISTS('" + schema.Name$().text() + "')");
  }

  @Override
  CreateStatement createTableIfNotExists(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final Map<String,String> enumTemplateToValues, final Map<String,ColumnRef> columnNameToColumn, final Map<String,Map<String,String>> tableNameToEnumToOwner) throws GeneratorExecutionException {
    return new CreateStatement("CALL CREATE_TABLE_IF_NOT_EXISTS('" + table.getName$().text() + "', '" + super.createTableIfNotExists(alterStatements, table, enumTemplateToValues, columnNameToColumn, tableNameToEnumToOwner).getSql().replace("'", "''") + "')");
  }

  @Override
  DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("CALL DROP_TABLE_IF_EXISTS('" + table.getName$().text() + "')");
  }

  @Override
  DropStatement dropIndexIfExists(final String indexName) {
    return new DropStatement("CALL DROP_INDEX_IF_EXISTS('" + indexName + "')");
  }

  @Override
  StringBuilder onUpdate(final StringBuilder b, final $ChangeRule onUpdate) {
    return b;
  }

  @Override
  StringBuilder $null(final StringBuilder b, final $Table table, final $Column column) {
    if (column.getNull$() != null && !column.getNull$().text())
      b.append(" NOT NULL");

    return b;
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    if (!Generator.isAuto(column))
      return null;

    if (logger.isWarnEnabled()) logger.warn("AUTO_INCREMENT does not support CYCLE.");

    final String _default = getAttr("default", column);
    final String min = getAttr("min", column);
    if (min != null && _default != null && logger.isWarnEnabled()) logger.warn("AUTO_INCREMENT does not consider min=\"" + min + "\" -- Ignoring min spec.");

    final String max = getAttr("max", column);
    if (max != null && logger.isWarnEnabled()) logger.warn("AUTO_INCREMENT does not consider max=\"" + max + "\" -- Ignoring max spec.");

    final String start = _default != null ? _default : min != null ? min : "1";
    return "GENERATED BY DEFAULT AS IDENTITY (START WITH " + start + ", INCREMENT BY 1)"; // FIXME: StringBuilder
  }

  @Override
  StringBuilder primaryKey(final StringBuilder b, final $Table table, final int[] columns, final PrimaryKey.Using$ using) {
    return super.primaryKey(b, table, columns, null);
  }

  @Override
  StringBuilder dropIndexOnClause(final $Table table) {
    return q(new StringBuilder(" ON "), table.getName$().text());
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $IndexType type, final String tableName, final $Named ... columns) {
    if ($IndexType.HASH.text().equals(type.text()) && logger.isWarnEnabled()) logger.warn("HASH index type specification is not explicitly supported by Derby's CREATE INDEX syntax. Creating index with default type.");

    final StringBuilder b = new StringBuilder("CREATE ");
    if (unique)
      b.append("UNIQUE ");

    b.append("INDEX ");
    q(b, indexName).append(" ON ");
    q(b, tableName).append(" (").append(SQLDataTypes.csvNames(getDialect(), columns)).append(')');
    return new CreateStatement(b.toString());
  }
}