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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxsb.runtime.BindingProxy;
import org.jaxsb.runtime.Bindings;
import org.libj.util.CollectionUtil;
import org.libj.util.RefDigraph;

// TODO: In addition to JAX-SB Schema objects, allow JAX-DB Schema objects also.
public final class Schemas {
  public static int[] drop(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), true, false, false);
  }

  public static int[] drop(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), true, false, false);
  }

  public static int[] drop(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), true, false, false);
  }

  public static int[] drop(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, false);
  }

  public static int[] dropBatched(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), true, false, true);
  }

  public static int[] dropBatched(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), true, false, true);
  }

  public static int[] dropBatched(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), true, false, true);
  }

  public static int[] dropBatched(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, false, true);
  }

  public static int[] create(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), false, true, false);
  }

  public static int[] create(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), false, true, false);
  }

  public static int[] create(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), false, true, false);
  }

  public static int[] create(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, false);
  }

  public static int[] createBatched(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), false, true, true);
  }

  public static int[] createBatched(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), false, true, true);
  }

  public static int[] createBatched(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), false, true, true);
  }

  public static int[] createBatched(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, false, true, true);
  }

  public static int[] recreate(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), true, true, false);
  }

  public static int[] recreate(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), true, true, false);
  }

  public static int[] recreate(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), true, true, false);
  }

  public static int[] recreate(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, false);
  }

  public static int[] recreateBatched(final Connection connection, final Schema[] schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length), schemas), true, true, true);
  }

  public static int[] recreateBatched(final Connection connection, final Schema schema) throws GeneratorExecutionException, SQLException {
    return exec(connection, Collections.singletonList(schema), true, true, true);
  }

  public static int[] recreateBatched(final Connection connection, final Schema schema, final Schema ... schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, CollectionUtil.asCollection(new ArrayList<>(schemas.length + 1), schema, schemas), true, true, true);
  }

  public static int[] recreateBatched(final Connection connection, final Collection<? extends Schema> schemas) throws GeneratorExecutionException, SQLException {
    return exec(connection, schemas, true, true, true);
  }

  private static int[] exec(final Connection connection, final Collection<? extends Schema> schemas, final boolean drop, final boolean create, final boolean batched) throws GeneratorExecutionException, SQLException {
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

  private static final Comparator<$Table> tableNameComparator = (o1, o2) -> o1 == null ? o2 == null ? 0 : 1 : o2 == null ? -1 : o1.getName$().text().compareTo(o2.getName$().text());

  private static Schema topologicalSort(final Schema schema) {
    final List<$Table> tables = new ArrayList<>(schema.getTable());
    schema.getTable().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<$Table,String> digraph = new RefDigraph<>(table -> table.getName$().text().toLowerCase());
    for (final $Table table : tables) {
      digraph.add(table);
      for (final $Column column : table.getColumn())
        if (column.getForeignKey() != null)
          digraph.add(table, column.getForeignKey().getReferences$().text().toLowerCase());

      if (table.getConstraints() != null && table.getConstraints().getForeignKey() != null)
        for (final $ForeignKeyComposite foreignKey : table.getConstraints().getForeignKey())
          digraph.add(table, foreignKey.getReferences$().text().toLowerCase());
    }

    if (digraph.hasCycle())
      throw new IllegalStateException("Cycle exists in relational model: " + CollectionUtil.toString(digraph.getCycle(), " -> "));

    final ListIterator<$Table> topological = digraph.getTopologicalOrder().listIterator(digraph.size());
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
      private final String declaringTableName = column.id() != null ? column.id() : (($Table)BindingProxy.owner(column)).getName$().text();

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

  private static void flattenTable(final $Table table, final Map<String,? extends $Table> tableNameToTable, final Set<? super String> flatTables) {
    if (flatTables.contains(table.getName$().text()))
      return;

    flatTables.add(table.getName$().text());
    if (table.getExtends$() == null)
      return;

    final $Table superTable = tableNameToTable.get(table.getExtends$().text());
    table.setExtends$(null);
    flattenTable(superTable, tableNameToTable, flatTables);
    if (superTable.getColumn() != null) {
      if (table.getColumn() != null) {
        for (int i = 0, len = superTable.getColumn().size(); i < len; ++i)
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
    final Compiler compiler = Compiler.getCompiler(DBVendor.valueOf(connection.getMetaData()));
    final java.sql.Statement statement = connection.createStatement();
    for (final $Table table : tables)
      statement.addBatch(compiler.truncate(table.getName$().text()));

    return statement.executeBatch();
  }

  private Schemas() {
  }
}