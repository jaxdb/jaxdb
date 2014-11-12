/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import static org.safris.xdb.xde.EntityBridgeUtil.parseBindings;
import static org.safris.xdb.xde.EntityBridgeUtil.parseParts;
import static org.safris.xdb.xde.EntityBridgeUtil.prototypeAssignments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.safris.xdb.xde.EntityBridgeUtil.Assignment;
import org.safris.xdb.xdl.XDLTransformer;
import org.safris.xdb.xdl.xdl_database;

public final class EntityBridge {
  private static final Map<xdl_database,EntityBridge> instances = new HashMap<xdl_database,EntityBridge>();

  protected static EntityBridge getInstance(final xdl_database database) {
    EntityBridge instance = instances.get(database);
    if (instance != null)
      return instance;

    synchronized (instances) {
      instance = instances.get(database);
      if (instance != null)
        return instance;

      instances.put(database, instance = new EntityBridge(XDLTransformer.merge(database)));
      return instance;
    }
  }

  private final xdl_database database;
  private final Map<Class<?>,Assignment> bindingToAssignment;

  private EntityBridge(final xdl_database database) {
    this.database = database;
    this.bindingToAssignment = prototypeAssignments(database);
  }

  protected List<Assignment> parseAssignments(final String sql) {
    final Map<String,Class<?>> aliasToBinding = parseBindings(database, sql);
    final String select = sql.replaceAll("((^\\s*SELECT\\s*(DISTINCT)?\\s+)|(\\s+FROM\\s.*$))", "");
    final String[] selections = select.split(",");
    final List<Assignment> assignments = new ArrayList<Assignment>();
    String lastAlias = null;
    final List<String> columns = new ArrayList<String>();
    for (int i = 0; ; i++) {
      final String[] parts = i < selections.length ? parseParts(selections[i].trim().split("\\."), aliasToBinding) : null;
      if (parts == null || (lastAlias != null && (!lastAlias.equals(parts[0]) || "*".equals(parts[1])))) {
        final Class<?> binding = aliasToBinding.get(lastAlias);
        final Assignment prototype = bindingToAssignment.get(binding);
        final Assignment assignment = new Assignment(binding);
        for (final String column : columns)
          assignment.entries.put(column, prototype.entries.get(column));

        assignments.add(assignment);
        columns.clear();
        if (parts == null)
          break;
      }

      if ("*".equals(parts[1])) {
        final Class<?> binding = aliasToBinding.get(parts[0]);
        final Assignment prototype = bindingToAssignment.get(binding);
        assignments.add(prototype);
        lastAlias = null;
      }
      else {
        columns.add(parts[1]);
        lastAlias = parts[0];
      }
    }

    return assignments;
  }

  public EntityBindingConnection getConnection(final String url) throws SQLException {
    return EntityBindingConnection.getInstance(this, url);
  }
}