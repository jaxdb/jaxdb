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

package org.safris.rdb.ddlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.util.TopologicalSort;
import org.safris.rdb.ddlx.xe.$ddlx_column;
import org.safris.rdb.ddlx.xe.$ddlx_constraints;
import org.safris.rdb.ddlx.xe.$ddlx_foreignKey;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.ddlx.xe.ddlx_schema;
import org.safris.rdb.vendor.DBVendor;

public final class Schemas {
  public static int[] drop(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, false, false);
  }

  public static int[] drop(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, false);
  }

  public static int[] dropBatched(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, false, true);
  }

  public static int[] dropBatched(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, true);
  }

  public static int[] create(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), false, true, false);
  }

  public static int[] create(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, false);
  }

  public static int[] createBatched(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), false, true, true);
  }

  public static int[] createBatched(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, true);
  }

  public static int[] recreate(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, true, false);
  }

  public static int[] recreate(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, false);
  }

  public static int[] recreateBatched(final Connection connection, final ddlx_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, true, true);
  }

  public static int[] recreateBatched(final Connection connection, final Collection<ddlx_schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, true);
  }

  private static int[] exec(final Connection connection, final Collection<ddlx_schema> schemas, final boolean drop, final boolean create, final boolean batched) throws GeneratorExecutionException, SQLException {
    if (!drop && !create)
      return null;

    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    Serializer.getSerializer(vendor).init(connection);
    final java.sql.Statement sqlStatement = connection.createStatement();
    final int[] counts = new int[schemas.size()];
    int i = 0;
    if (batched) {
      for (final ddlx_schema schema : schemas) {
        final List<Statement> statements = new Generator(new DDLxAudit(schema)).parse(vendor);
        for (final Statement statement : statements)
          if (drop && statement instanceof DropStatement || create && statement instanceof CreateStatement)
            sqlStatement.addBatch(statement.getSql());

        int count = 0;
        for (final int result : sqlStatement.executeBatch())
          count += result;

        counts[i++] = count;
      }
    }
    else {
      for (final ddlx_schema schema : schemas) {
        final List<Statement> statements = new Generator(new DDLxAudit(schema)).parse(vendor);
        int count = 0;
        for (final Statement statement : statements)
          if (drop && statement instanceof DropStatement || create && statement instanceof CreateStatement)
            count += sqlStatement.executeUpdate(statement.getSql());

        counts[i++] = count;
      }
    }

    return counts;
  }

  public static List<$ddlx_table> tables(final ddlx_schema schema) {
    final Map<String,$ddlx_table> tableNameToTable = new HashMap<String,$ddlx_table>();
    final Map<String,Set<String>> dependencyGraph = new HashMap<String,Set<String>>();
    for (final $ddlx_table table : schema._table()) {
      if (table._abstract$().text() || table._skip$().text())
        continue;

      final String tableName = table._name$().text();
      tableNameToTable.put(tableName, table);
      Set<String> dependants = dependencyGraph.get(tableName);
      if (dependants == null)
        dependencyGraph.put(tableName, dependants = new HashSet<String>());

      if (table._constraints() != null) {
        final $ddlx_constraints constraints = table._constraints(0);
        final List<$ddlx_table._constraints._foreignKey> foreignKeys = constraints._foreignKey();
        if (foreignKeys != null)
          for (final $ddlx_table._constraints._foreignKey foreignKey : foreignKeys)
            dependants.add(foreignKey._references$().text());
      }

      if (table._column() != null) {
        for (final $ddlx_column column : table._column()) {
          if (column._foreignKey() != null) {
            final $ddlx_foreignKey foreignKey = column._foreignKey(0);
            dependants.add(foreignKey._references$().text());
          }
        }
      }
    }

    final List<String> sortedNames = TopologicalSort.sort(dependencyGraph);
    final List<$ddlx_table> tables = new ArrayList<$ddlx_table>(sortedNames.size());
    for (final String tableName : sortedNames)
      tables.add(tableNameToTable.get(tableName));

    return tables;
  }

  public static int[] truncate(final Connection connection, final $ddlx_table ... tables) throws SQLException {
    return truncate(connection, Arrays.asList(tables));
  }

  public static int[] truncate(final Connection connection, final Collection<$ddlx_table> tables) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Serializer serializer = Serializer.getSerializer(vendor);
    final java.sql.Statement statement = connection.createStatement();
    for (final $ddlx_table table : tables)
      statement.addBatch(serializer.truncate(table._name$().text()));

    return statement.executeBatch();
  }

  private Schemas() {
  }
}