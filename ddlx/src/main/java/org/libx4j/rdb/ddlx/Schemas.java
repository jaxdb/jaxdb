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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.fastjax.util.Collections;
import org.fastjax.util.RefDigraph;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Column;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Columns;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Constraints;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Enum;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Named;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.Schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.BindingProxy;
import org.libx4j.xsb.runtime.Bindings;

public final class Schemas {
  public static int[] drop(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, false, false);
  }

  public static int[] drop(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, false);
  }

  public static int[] dropBatched(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, false, true);
  }

  public static int[] dropBatched(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, true);
  }

  public static int[] create(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), false, true, false);
  }

  public static int[] create(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, false);
  }

  public static int[] createBatched(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), false, true, true);
  }

  public static int[] createBatched(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, true);
  }

  public static int[] recreate(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, true, false);
  }

  public static int[] recreate(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, false);
  }

  public static int[] recreateBatched(final Connection connection, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, Arrays.asList(schemas), true, true, true);
  }

  public static int[] recreateBatched(final Connection connection, final Collection<Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, true);
  }

  private static int[] exec(final Connection connection, final Collection<Schema> schemas, final boolean drop, final boolean create, final boolean batched) throws GeneratorExecutionException, SQLException {
    if (!drop && !create)
      return null;

    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    Compiler.getCompiler(vendor).init(connection);
    final java.sql.Statement sqlStatement = connection.createStatement();
    final int[] counts = new int[schemas.size()];
    int i = 0;
    if (batched) {
      for (final Schema schema : schemas) {
        final LinkedHashSet<Statement> statements = new Generator(new DDLxAudit(schema)).parse(vendor);
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
      for (final Schema schema : schemas) {
        final LinkedHashSet<Statement> statements = new Generator(new DDLxAudit(schema)).parse(vendor);
        int count = 0;
        for (final Statement statement : statements)
          if (drop && statement instanceof DropStatement || create && statement instanceof CreateStatement)
            count += sqlStatement.executeUpdate(statement.getSql());

        counts[i++] = count;
      }
    }

    return counts;
  }

  private static final Comparator<$Table> tableNameComparator = new Comparator<>() {
    @Override
    public int compare(final $Table o1, final $Table o2) {
      return o1 == null ? (o2 == null ? 0 : 1) : o2 == null ? -1 : o1.getName$().text().compareTo(o2.getName$().text());
    }
  };

  private static Schema topologicalSort(final Schema schema) {
    final List<$Table> tables = new ArrayList<>(schema.getTable());
    schema.getTable().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<$Table,String> digraph = new RefDigraph<>(table -> table.getName$().text().toLowerCase());
    for (final $Table table : tables) {
      digraph.addVertex(table);
      for (final $Column column : table.getColumn())
        if (column.getForeignKey() != null)
          digraph.addEdgeRef(table, column.getForeignKey().getReferences$().text().toLowerCase());
    }

    if (digraph.hasCycle())
      throw new IllegalStateException("Cycle exists in relational model: " + Collections.toString(digraph.getCycle(), " -> "));

    final ListIterator<$Table> topological = digraph.getTopologicalOrder().listIterator(digraph.getSize());
    while (topological.hasPrevious())
      schema.getTable().add(topological.previous());

    return schema;
  }

  public static Schema flatten(final Schema schema) {
    final Schema flat = (Schema)Bindings.clone(schema);
    final Map<String,$Table> tableNameToTable = new HashMap<>();
    // First, register the table names to be referenceable by the @extends attribute
    for (final $Table table : flat.getTable())
      tableNameToTable.put(table.getName$().text(), table);

    final Set<String> flatTables = new HashSet<>();
    for (final $Table table : flat.getTable())
      flattenTable(table, tableNameToTable, flatTables);

    final Iterator<$Table> iterator = flat.getTable().iterator();
    while (iterator.hasNext())
      if (iterator.next().getAbstract$().text())
        iterator.remove();

    return Schemas.topologicalSort(flat);
  }

  private static $Column clone(final $Column column) {
    return column instanceof $Enum ? new $Enum(($Enum)column.clone()) {
      private static final long serialVersionUID = -8441464393229662197L;

      private final String declaringTableName = (($Table)BindingProxy.owner(column)).getName$().text();

      @Override
      public String id() {
        return declaringTableName;
      }

      @Override
      protected $Named inherits() {
        return column;
      }
    } : column.clone();
  }

  private static void flattenTable(final $Table table, final Map<String,$Table> tableNameToTable, final Set<String> flatTables) {
    if (flatTables.contains(table.getName$().text()))
      return;

    flatTables.add(table.getName$().text());
    if (table.getExtends$() == null)
      return;

    final $Table superTable = tableNameToTable.get(table.getExtends$().text());
    flattenTable(superTable, tableNameToTable, flatTables);
    if (superTable.getColumn() != null) {
      if (table.getColumn() != null) {
        for (int i = 0; i < superTable.getColumn().size(); i++)
          table.getColumn().add(i, clone(superTable.getColumn().get(i)));
      }
      else {
        for (final $Column column : superTable.getColumn())
          table.addColumn(clone(column));
      }
    }

    if (superTable.getConstraints() != null) {
      final $Constraints parentConstraints = superTable.getConstraints();
      if (table.getConstraints() == null) {
        table.setConstraints(parentConstraints.clone());
      }
      else {
        if (parentConstraints.getPrimaryKey() != null)
          table.getConstraints().setPrimaryKey(parentConstraints.getPrimaryKey().clone());

        if (parentConstraints.getUnique() != null)
          for (final $Columns columns : parentConstraints.getUnique())
            table.getConstraints().addUnique(columns.clone());
      }
    }

    if (superTable.getIndexes() != null) {
      if (table.getIndexes() == null) {
        table.setIndexes(superTable.getIndexes().clone());
      }
      else {
        for (final $Table.Indexes.Index index : superTable.getIndexes().getIndex())
          table.getIndexes().addIndex(index.clone());
      }
    }
  }

  public static int[] truncate(final Connection connection, final $Table ... tables) throws SQLException {
    return truncate(connection, Arrays.asList(tables));
  }

  public static int[] truncate(final Connection connection, final Collection<? extends $Table> tables) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Compiler compiler = Compiler.getCompiler(vendor);
    final java.sql.Statement statement = connection.createStatement();
    for (final $Table table : tables)
      statement.addBatch(compiler.truncate(table.getName$().text()));

    return statement.executeBatch();
  }

  private Schemas() {
  }
}