/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.schema;

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
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_constraints;
import org.safris.xdb.xds.xe.$xds_foreignKey;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.xds_schema;

public final class Schemas {
  public static int[] create(final Connection connection, final xds_schema ... schemas) throws GeneratorExecutionException, SQLException {
    return create(connection, Arrays.asList(schemas));
  }

  public static int[] create(final Connection connection, final Collection<xds_schema> schemas) throws GeneratorExecutionException, SQLException {
    final DBVendor vendor = DBVendor.parse(connection.getMetaData());
    final java.sql.Statement statement = connection.createStatement();
    final int[] counts = new int[schemas.size()];
    int i = 0;
    for (final xds_schema schema : schemas) {
      final String[] sqls = Generator.createDDL(schema, vendor, null);
      for (final String sql : sqls)
        statement.addBatch(sql);

      int count = 0;
      final int[] results = statement.executeBatch();
      for (final int result : results)
        count += result;

      counts[i++] = count;
    }

    return counts;
  }

  public static List<$xds_table> tables(final xds_schema schema) {
    final Map<String,$xds_table> tableNameToTable = new HashMap<String,$xds_table>();
    final Map<String,Set<String>> dependencyGraph = new HashMap<String,Set<String>>();
    for (final $xds_table table : schema._table()) {
      if (table._abstract$().text() || table._skip$().text())
        continue;

      final String tableName = table._name$().text();
      tableNameToTable.put(tableName, table);
      Set<String> dependants = dependencyGraph.get(tableName);
      if (dependants == null)
        dependencyGraph.put(tableName, dependants = new HashSet<String>());

      if (table._constraints() != null) {
        final $xds_constraints constraints = table._constraints(0);
        final List<$xds_table._constraints._foreignKey> foreignKeys = constraints._foreignKey();
        if (foreignKeys != null)
          for (final $xds_table._constraints._foreignKey foreignKey : foreignKeys)
            dependants.add(foreignKey._references$().text());
      }

      if (table._column() != null) {
        for (final $xds_column column : table._column()) {
          if (column._foreignKey() != null) {
            final $xds_foreignKey foreignKey = column._foreignKey(0);
            dependants.add(foreignKey._references$().text());
          }
        }
      }
    }

    final List<String> sortedNames = TopologicalSort.sort(dependencyGraph);
    final List<$xds_table> tables = new ArrayList<$xds_table>(sortedNames.size());
    for (final String tableName : sortedNames)
      tables.add(tableNameToTable.get(tableName));

    return tables;
  }

  public static int[] truncate(final Connection connection, final $xds_table ... tables) throws SQLException {
    return truncate(connection, Arrays.asList(tables));
  }

  public static int[] truncate(final Connection connection, final Collection<$xds_table> tables) throws SQLException {
    final DBVendor vendor = DBVendor.parse(connection.getMetaData());
    final java.sql.Statement statement = connection.createStatement();
    for (final $xds_table table : tables)
      statement.addBatch(vendor.getSQLSpec().truncate(table._name$().text()));

    return statement.executeBatch();
  }

  private Schemas() {
  }
}