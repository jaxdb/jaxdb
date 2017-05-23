/* Copyright (c) 2012 lib4j
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.lib4j.xml.XMLException;
import org.libx4j.xsb.runtime.Bindings;
import org.safris.rdb.ddlx.xe.$ddlx_column;
import org.safris.rdb.ddlx.xe.$ddlx_columns;
import org.safris.rdb.ddlx.xe.$ddlx_constraints;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.ddlx.xe.ddlx_schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class BaseGenerator {
  protected static final Logger logger = LoggerFactory.getLogger(BaseGenerator.class);

  static {
    try {
      PackageLoader.getSystemPackageLoader().loadPackage(ddlx_schema.class.getPackage().getName());
    }
    catch (final PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static void writeOutput(final List<Statement> statements, final File file) {
    if (file == null)
      return;

    final StringBuilder builder = new StringBuilder();
    for (final Statement statement : statements)
      builder.append("\n\n").append(statement).append(";");

    try {
      if (file.getParentFile().isFile())
        throw new IllegalArgumentException(file.getParent() + " is a file.");

      if (!file.getParentFile().exists())
        if (!file.getParentFile().mkdirs())
          throw new IllegalArgumentException("Could not create path: " + file.getParent());

      try (final FileOutputStream out = new FileOutputStream(file)) {
        out.write(builder.substring(2).getBytes());
      }
    }
    catch (final IOException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  protected static ddlx_schema merge(final ddlx_schema schema) {
    final ddlx_schema merged;
    try {
      merged = (ddlx_schema)Bindings.clone(schema);
    }
    catch (final IOException | XMLException e) {
      throw new UnsupportedOperationException(e);
    }

    final Map<String,$ddlx_table> tableNameToTable = new HashMap<String,$ddlx_table>();
    // First, register the table names to be referencable by the @extends attribute
    for (final $ddlx_table table : merged._table())
      tableNameToTable.put(table._name$().text(), table);

    final Set<String> mergedTables = new HashSet<String>();
    for (final $ddlx_table table : merged._table())
      mergeTable(table, tableNameToTable, mergedTables);

    return merged;
  }

  protected final DDLxAudit audit;
  protected final ddlx_schema unmerged;
  protected final ddlx_schema merged;

  protected BaseGenerator(final DDLxAudit audit) {
    this.audit = audit;
    this.unmerged = audit.schema();
    this.merged = merge(audit.schema());

    final List<String> errors = getErrors();
    if (errors != null && errors.size() > 0) {
      for (final String error : errors)
        logger.warn(error);

      // System.exit(1);
    }
  }

  private List<String> getErrors() {
    final List<String> errors = new ArrayList<String>();
    for (final $ddlx_table table : merged._table()) {
      if (!table._abstract$().text()) {
        if (table._constraints(0)._primaryKey(0).isNull()) {
          errors.add("Table `" + table._name$().text() + "` does not have a primary key.");
        }
        else {
          for (final $ddlx_column column : table._column()) {
            if (audit.isPrimary(table, column) && column._null$().text())
              errors.add("Primary key column `" + column._name$().text() + "` on table `" + table._name$().text() + "` is NULL.");
          }
        }
      }
    }

    return errors;
  }

  private static void mergeTable(final $ddlx_table table, final Map<String,$ddlx_table> tableNameToTable, final Set<String> mergedTables) {
    if (mergedTables.contains(table._name$().text()))
      return;

    mergedTables.add(table._name$().text());
    if (table._extends$().isNull())
      return;

    final $ddlx_table superTable = tableNameToTable.get(table._extends$().text());
    if (!superTable._abstract$().text()) {
      logger.error("Table `" + superTable._name$().text() + "` must be abstract to be inherited by " + table._name$().text());
      System.exit(1);
    }

    mergeTable(superTable, tableNameToTable, mergedTables);
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
        if (parentConstraints._primaryKey() != null) {
          for (final $ddlx_columns columns : parentConstraints._primaryKey()) {
            table._constraints(0)._primaryKey(columns);
          }
        }

        if (parentConstraints._foreignKey() != null) {
          for (final $ddlx_table._constraints._foreignKey entry : parentConstraints._foreignKey()) {
            table._constraints(0)._foreignKey(entry);
          }
        }

        if (parentConstraints._unique() != null) {
          for (final $ddlx_columns columns : parentConstraints._unique()) {
            table._constraints(0)._unique(columns);
          }
        }
      }
    }

    if (superTable._indexes() != null) {
      if (table._indexes() == null) {
        table._indexes(superTable._indexes(0));
      }
      else {
        for (final $ddlx_table._indexes._index index : superTable._indexes(0)._index()) {
          table._indexes(0)._index(index);
        }
      }
    }
  }
}