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

package org.safris.xdb.xdl;

import java.io.File;
import java.io.FileOutputStream;
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
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.xml.sax.InputSource;

public abstract class XDLTransformer {
  static {
    try {
      PackageLoader.getSystemPackageLoader().loadPackage(xdl_database.class.getPackage().getName());
    }
    catch (final PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static xdl_database parseArguments(final URL url, final File outDir) {
    if (url == null)
      throw new IllegalArgumentException("url == null");

    if (outDir != null && !outDir.exists())
      throw new IllegalArgumentException("!outDir.exists()");

    try {
      final InputStream in = url.openStream();
      final xdl_database database = (xdl_database)Bindings.parse(new InputSource(in));
      in.close();
      return database;
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected static void writeOutput(final String output, final File file) {
    try {
      if (file == null) {
        System.out.println(output);
      }
      else {
        if (file.getParentFile().isFile())
          throw new IllegalArgumentException(file.getParent() + " is a file.");

        if (!file.getParentFile().exists())
          if (!file.getParentFile().mkdirs())
            throw new IllegalArgumentException("Could not create path: " + file.getParent());

        final FileOutputStream out = new FileOutputStream(file);
        out.write(output.getBytes());
        out.close();
      }
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  // FIXME: This should not be public! But it's been set this way to be usable by xde package.
  public static xdl_database merge(final xdl_database database) {
    final xdl_database merged;
    try {
      merged = (xdl_database)Bindings.clone(database);
    }
    catch (final Exception e) {
      throw new Error(e);
    }

    final Map<String,$xdl_tableType> tableNameToTable = new HashMap<String,$xdl_tableType>();
    // First, register the table names to be referencable by the @extends attribute
    for (final $xdl_tableType table : merged._table())
      tableNameToTable.put(table._name$().text(), table);

    final Set<String> mergedTables = new HashSet<String>();
    for (final $xdl_tableType table : merged._table())
      mergeTable(table, tableNameToTable, mergedTables);

    return merged;
  }

  protected final xdl_database unmerged;
  protected final xdl_database merged;

  public XDLTransformer(final xdl_database database) {
    this.unmerged = database;
    this.merged = merge(database);

    final List<String> errors = getErrors();
    if (errors != null && errors.size() > 0) {
      for (final String error : errors)
        System.err.println("[WARNING] " + error);

      //System.exit(1);
    }
  }

  private List<String> getErrors() {
    final List<String> errors = new ArrayList<String>();
    for (final $xdl_tableType table : merged._table())
      if (!table._abstract$().text() && (table._constraints(0) == null || table._constraints(0)._primaryKey() == null || table._constraints(0)._primaryKey(0)._column() == null))
        errors.add("Table " + table._name$().text() + " does not have a primary key.");

    return errors;
  }

  private static void mergeTable(final $xdl_tableType table, final Map<String,$xdl_tableType> tableNameToTable, final Set<String> mergedTables) {
    if (mergedTables.contains(table._name$().text()))
      return;

    mergedTables.add(table._name$().text());
    if (table._extends$().isNull())
      return;

    if (table._name$().text().equals("invitation")) {
      int idsao = 0;
    }

    final $xdl_tableType superTable = tableNameToTable.get(table._extends$().text());
    if (!superTable._abstract$().text()) {
      System.err.println("[ERROR] Table " + superTable._name$().text() + " must be abstract to be inherited by " + table._name$().text());
      System.exit(1);
    }

    mergeTable(superTable, tableNameToTable, mergedTables);
    if (superTable._column() != null) {
      if (table._column() != null) {
        for (int i = table._column().size() - 1; 0 <= i; i--)
          if (table._column(i) instanceof $xdl_inherited)
            table._column().remove(i);

        table._column().addAll(0, superTable._column());
      }
      else {
        for (final $xdl_columnType column : superTable._column())
          table._column(column);
      }
    }

    if (superTable._constraints() == null)
      return;

    final List<$xdl_tableType._constraints> constraints = superTable._constraints();
    if (table._constraints() == null) {
      table._constraints(constraints.get(0));
      return;
    }

    if (constraints.get(0)._primaryKey() == null)
      return;

    final List<$xdl_tableType._constraints> constraints2 = table._constraints();
    if (constraints2 != null)
      constraints2.get(0)._primaryKey(constraints.get(0)._primaryKey(0));
  }
}