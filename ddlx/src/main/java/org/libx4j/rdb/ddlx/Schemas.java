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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lib4j.util.Collections;
import org.lib4j.util.RefDigraph;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_columns;
import org.libx4j.rdb.ddlx.xe.$ddlx_constraints;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Bindings;

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
    Compiler.getCompiler(vendor).init(connection);
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

  private static final Comparator<$ddlx_table> tableNameComparator = new Comparator<$ddlx_table>() {
    @Override
    public int compare(final $ddlx_table o1, final $ddlx_table o2) {
      return o1 == null ? (o2 == null ? 0 : 1) : o2 == null ? -1 : o1._name$().text().compareTo(o2._name$().text());
    }
  };

  private static ddlx_schema topologicalSort(final ddlx_schema schema) {
    final List<$ddlx_table> tables = new ArrayList<$ddlx_table>(schema._table());
    schema._table().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<$ddlx_table,String> digraph = new RefDigraph<$ddlx_table,String>(table -> table._name$().text().toLowerCase());
    for (final $ddlx_table table : tables) {
      digraph.addVertex(table);
      for (final $ddlx_column column : table._column())
        if (column._foreignKey() != null && column._foreignKey().size() > 0)
          digraph.addEdgeRef(table, column._foreignKey(0)._references$().text().toLowerCase());
    }

    if (digraph.hasCycle())
      throw new IllegalStateException("Cycle exists in relational model: " + Collections.toString(digraph.getCycle(), " -> "));

    final List<$ddlx_table> topological = digraph.getTopologicalOrder();
    for (final $ddlx_table table : topological)
      schema._table().add(table);

    return schema;
  }

  public static ddlx_schema flatten(final ddlx_schema schema) {
    final ddlx_schema flat;
    try {
      flat = (ddlx_schema)Bindings.clone(schema);
    }
    catch (final IOException e) {
      throw new UnsupportedOperationException(e);
    }

    final Map<String,$ddlx_table> tableNameToTable = new HashMap<String,$ddlx_table>();
    // First, register the table names to be referenceable by the @extends attribute
    for (final $ddlx_table table : flat._table())
      tableNameToTable.put(table._name$().text(), table);

    final Set<String> flatTables = new HashSet<String>();
    for (final $ddlx_table table : flat._table())
      flattenTable(table, tableNameToTable, flatTables);

    final Iterator<$ddlx_table> iterator = flat._table().iterator();
    while (iterator.hasNext())
      if (iterator.next()._abstract$().text())
        iterator.remove();

    return Schemas.topologicalSort(flat);
  }

  private static void flattenTable(final $ddlx_table table, final Map<String,$ddlx_table> tableNameToTable, final Set<String> flatTables) {
    if (flatTables.contains(table._name$().text()))
      return;

    flatTables.add(table._name$().text());
    if (table._extends$().isNull())
      return;

    final $ddlx_table superTable = tableNameToTable.get(table._extends$().text());
    flattenTable(superTable, tableNameToTable, flatTables);
    if (superTable._column() != null) {
      if (table._column() != null) {
        table._column().addAll(0, superTable._column());
      }
      else {
        for (final $ddlx_column column : superTable._column())
          table._column(column);
      }
    }

    if (superTable._constraints() != null) {
      final $ddlx_constraints parentConstraints = superTable._constraints(0);
      if (table._constraints() == null) {
        table._constraints(parentConstraints);
      }
      else {
        if (parentConstraints._primaryKey() != null)
          for (final $ddlx_columns columns : parentConstraints._primaryKey())
            table._constraints(0)._primaryKey(columns);

        if (parentConstraints._unique() != null)
          for (final $ddlx_columns columns : parentConstraints._unique())
            table._constraints(0)._unique(columns);
      }
    }

    if (superTable._indexes() != null) {
      if (table._indexes() == null) {
        table._indexes(superTable._indexes(0));
      }
      else {
        for (final $ddlx_table._indexes._index index : superTable._indexes(0)._index())
          table._indexes(0)._index(index);
      }
    }
  }

  public static int[] truncate(final Connection connection, final $ddlx_table ... tables) throws SQLException {
    return truncate(connection, Arrays.asList(tables));
  }

  public static int[] truncate(final Connection connection, final Collection<? extends $ddlx_table> tables) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Compiler compiler = Compiler.getCompiler(vendor);
    final java.sql.Statement statement = connection.createStatement();
    for (final $ddlx_table table : tables)
      statement.addBatch(compiler.truncate(table._name$().text()));

    return statement.executeBatch();
  }

  private Schemas() {
  }
}