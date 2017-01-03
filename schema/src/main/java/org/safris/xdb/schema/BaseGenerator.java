/* Copyright (c) 2012 Seva Safris
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.PackageNotFoundException;
import org.safris.commons.xml.XMLException;
import org.safris.maven.common.Log;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_columns;
import org.safris.xdb.xds.xe.$xds_constraints;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public abstract class BaseGenerator {
  static {
    try {
      PackageLoader.getSystemPackageLoader().loadPackage(xds_schema.class.getPackage().getName());
    }
    catch (final PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static xds_schema parseArguments(final URL url, final File outDir) throws IOException, XMLException {
    if (url == null)
      throw new IllegalArgumentException("url == null");

    if (outDir != null && !outDir.exists())
      throw new IllegalArgumentException("!outDir.exists()");

    try (final InputStream in = url.openStream()) {
      return (xds_schema)Bindings.parse(new InputSource(in));
    }
  }

  protected static void writeOutput(final String output, final File file) {
    if (file == null)
      return;

    try {
      if (file.getParentFile().isFile())
        throw new IllegalArgumentException(file.getParent() + " is a file.");

      if (!file.getParentFile().exists())
        if (!file.getParentFile().mkdirs())
          throw new IllegalArgumentException("Could not create path: " + file.getParent());

      try (final FileOutputStream out = new FileOutputStream(file)) {
        out.write(output.trim().getBytes());
      }
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  // FIXME: This should not be public! But it's been set this way to be usable by xde package.
  public static xds_schema merge(final xds_schema schema) {
    final xds_schema merged;
    try {
      merged = (xds_schema)Bindings.clone(schema);
    }
    catch (final Exception e) {
      throw new Error(e);
    }

    final Map<String,$xds_table> tableNameToTable = new HashMap<String,$xds_table>();
    // First, register the table names to be referencable by the @extends attribute
    for (final $xds_table table : merged._table())
      tableNameToTable.put(table._name$().text(), table);

    final Set<String> mergedTables = new HashSet<String>();
    for (final $xds_table table : merged._table())
      mergeTable(table, tableNameToTable, mergedTables);

    return merged;
  }

  protected final xds_schema unmerged;
  protected final xds_schema merged;

  public BaseGenerator(final xds_schema schema) {
    this.unmerged = schema;
    this.merged = merge(schema);

    final List<String> errors = getErrors();
    if (errors != null && errors.size() > 0) {
      for (final String error : errors)
        Log.warn(error);

      // System.exit(1);
    }
  }

  private List<String> getErrors() {
    final List<String> errors = new ArrayList<String>();
    for (final $xds_table table : merged._table())
      if (!table._abstract$().text() && (table._constraints(0) == null || table._constraints(0)._primaryKey() == null || table._constraints(0)._primaryKey(0)._column() == null))
        errors.add("Table " + table._name$().text() + " does not have a primary key.");

    return errors;
  }

  private static void mergeTable(final $xds_table table, final Map<String,$xds_table> tableNameToTable, final Set<String> mergedTables) {
    if (mergedTables.contains(table._name$().text()))
      return;

    mergedTables.add(table._name$().text());
    if (table._extends$().isNull())
      return;

    final $xds_table superTable = tableNameToTable.get(table._extends$().text());
    if (!superTable._abstract$().text()) {
      Log.error("Table " + superTable._name$().text() + " must be abstract to be inherited by " + table._name$().text());
      System.exit(1);
    }

    mergeTable(superTable, tableNameToTable, mergedTables);
    if (superTable._column() != null) {
      if (table._column() != null) {
        table._column().addAll(0, superTable._column());
      }
      else {
        for (final $xds_column column : superTable._column())
          table._column(column);
      }
    }

    if (superTable._constraints() != null) {
      final $xds_constraints parentConstraints = superTable._constraints(0);
      if (table._constraints() == null) {
        table._constraints(parentConstraints);
      }
      else {
        if (parentConstraints._primaryKey() != null) {
          for (final $xds_columns columns : parentConstraints._primaryKey()) {
            table._constraints(0)._primaryKey(columns);
          }
        }

        if (parentConstraints._foreignKey() != null) {
          for (final $xds_table._constraints._foreignKey entry : parentConstraints._foreignKey()) {
            table._constraints(0)._foreignKey(entry);
          }
        }

        if (parentConstraints._unique() != null) {
          for (final $xds_columns columns : parentConstraints._unique()) {
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
        for (final $xds_table._indexes._index index : superTable._indexes(0)._index()) {
          table._indexes(0)._index(index);
        }
      }
    }
  }
}