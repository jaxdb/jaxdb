/* Copyright (c) 2022 JAX-DB
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

package org.jaxdb.jsql.generator;

import static org.jaxdb.jsql.generator.GeneratorUtil.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.QueryConfig;
import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.type;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Schema.Table;
import org.jaxsb.runtime.BindingList;
import org.libj.lang.Identifiers;

class SchemaManifest {
  private static final String GENERATED = "(value=\"" + Generator.class.getName() + "\", date=\"" + LocalDateTime.now().toString() + "\")";
  private static final String HEADER_COMMENT;

  static {
    final StringBuilder out = new StringBuilder();
    out.append("/* ").append("Autogenerated by JAX-DB Compiler (").append(Generator.class.getPackage().getImplementationVersion()).append(")\n");
    out.append(" * THIS FILE SHOULD NOT BE EDITED */\n");
    HEADER_COMMENT = out.toString();
  }

  final String packageName;
  final String name;
  final String schemaClassSimpleName;
  final String schemaClassName;
  final Map<String,TableMeta> tableNameToTableMeta;

  SchemaManifest(final String packageName, final String name, final BindingList<Table> tables) throws GeneratorExecutionException {
    this.packageName = packageName;
    this.name = name;
    this.schemaClassSimpleName = Identifiers.toIdentifier(name);
    this.schemaClassName = packageName + "." + schemaClassSimpleName;

    final int noTables = tables.size();
    if (noTables == 0) {
      this.tableNameToTableMeta = Collections.EMPTY_MAP;
    }
    else {
      this.tableNameToTableMeta = new LinkedHashMap<>(noTables);

      for (int i = 0; i < noTables; ++i) { // [RA]
        final Table table = tables.get(i);
        tableNameToTableMeta.put(table.getName$().text(), new TableMeta(table, this));
      }

      final Collection<TableMeta> tableMetas = tableNameToTableMeta.values();
      for (final TableMeta tableMeta : tableMetas) { // [C]
        final ArrayList<TableMeta> ancestors = new ArrayList<>();
        ancestors.add(tableMeta);
        addAncestors(tableMetas, ancestors, tableMeta);
        tableMeta.ancestors.addAll(ancestors);
      }
    }
  }

  String generate(final DDLx ddlx) throws GeneratorExecutionException {
    final StringBuilder out = new StringBuilder(HEADER_COMMENT);
    out.append("package ").append(packageName).append(";\n\n");
    out.append(getDoc(ddlx.getNormalizedSchema(), 0, '\0', '\n', "Schema", schemaClassSimpleName));
    out.append('@').append(SuppressWarnings.class.getName()).append("(\"all\")\n");
    out.append('@').append(Generated.class.getName()).append(GENERATED).append('\n');

    out.append("public class ").append(schemaClassSimpleName).append(" extends ").append(Schema.class.getCanonicalName()).append(" {");

    final StringBuilder cachedTables = new StringBuilder();
    final Collection<TableMeta> tableMetas = tableNameToTableMeta.values();
    final int noTables = tableMetas.size();
    if (noTables > 0) {
      for (final TableMeta tableMeta : tableMetas) { // [C]
        tableMeta.init();
        if (!tableMeta.isAbstract)
          cachedTables.append(tableMeta.classCase).append("(), ");
      }
    }

    final int len = cachedTables.length();
    if (len > 0)
      cachedTables.setLength(len - 2);

    final BindingList<$Column> templates = ddlx.getNormalizedSchema().getTemplate();
    if (templates != null) {
      for (int i = 0, i$ = templates.size(); i < i$; ++i) { // [RA]
        final $Column template = templates.get(i);
        if (template instanceof $Enum) {
          final $Enum enumTemplate = ($Enum)template;
          if (enumTemplate.getValues$() != null)
            out.append(Generator.declareEnumClass(schemaClassName, enumTemplate, 2)).append('\n');
        }
      }
    }

    // Then, in proper inheritance order, the real entities
    final ArrayList<TableMeta> sortedTables = new ArrayList<>();

    // First create the abstract entities
    if (noTables > 0) {
      for (final TableMeta tableMeta : tableMetas) // [C]
        if (tableMeta.isAbstract)
          out.append(tableMeta.makeTable()).append('\n');

      for (final TableMeta tableMeta : tableMetas) { // [C]
        if (!tableMeta.isAbstract) {
          sortedTables.add(tableMeta);
          out.append(tableMeta.makeTable()).append('\n');
        }
      }
    }

    sortedTables.sort(Generator.tableMetaComparator);
    final int noSortedTables = sortedTables.size();

    out.append("\n  private static final ").append(String.class.getName()).append("[] names = {");
    for (int i = 0; i < noSortedTables; ++i) // [RA]
      out.append('"').append(sortedTables.get(i).tableName).append("\", ");

    out.setCharAt(out.length() - 2, '}');
    out.setCharAt(out.length() - 1, ';');
    out.append("\n  private final ").append(type.Table$.class.getCanonicalName()).append("[] tables = {");
    for (int i = 0; i < noSortedTables; ++i) // [RA]
      out.append(sortedTables.get(i).singletonInstanceName).append(", ");

    out.setCharAt(out.length() - 2, '}');
    out.setCharAt(out.length() - 1, ';');
    out.append('\n');

    out.append("\n  @").append(Override.class.getName());
    out.append("\n  public ").append(type.Table$.class.getCanonicalName()).append("[] getTables() {");
    out.append("\n    return tables;");
    out.append("\n  }\n");

    out.append("\n  @").append(Override.class.getName());
    out.append("\n  public ").append(type.Table$.class.getCanonicalName()).append(" getTable(final ").append(String.class.getName()).append(" name) {");
    out.append("\n    final int index = ").append(Arrays.class.getName()).append(".binarySearch(names, name);");
    out.append("\n    return index < 0 ? null : tables[index];");
    out.append("\n  }\n");

    out.append("\n  @").append(Override.class.getName());
    out.append("\n  public void setDefaultQueryConfig(final ").append(QueryConfig.class.getName()).append(" queryConfig) {");
    out.append("\n    defaultQueryConfig = queryConfig;");
    out.append("\n  }\n");

    out.append("\n  @").append(Override.class.getName());
    out.append("\n  public String getName() {");
    out.append("\n    return \"").append(name).append("\";");
    out.append("\n  }\n");

    out.append("\n  public ").append(schemaClassSimpleName).append("() {");
    out.append("\n  }");

    out.append("\n}");
    return out.toString();
  }

  private void addAncestors(final Collection<TableMeta> allTables, final ArrayList<TableMeta> ancestors, final TableMeta table) {
    if (allTables.size() > 0) {
      for (final TableMeta ancestor : allTables) { // [C]
        if (ancestor.superTable != null && table.tableName.equals(ancestor.superTable.tableName)) {
          ancestors.add(ancestor);
          addAncestors(allTables, ancestors, ancestor);
        }
      }
    }
  }
}