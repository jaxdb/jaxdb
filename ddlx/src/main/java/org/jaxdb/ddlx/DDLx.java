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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.transform.TransformerException;

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Indexes;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Indexes.Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table.Extends$;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxsb.runtime.BindingList;
import org.jaxsb.runtime.Bindings;
import org.libj.util.CollectionUtil;
import org.libj.util.RefDigraph;
import org.openjax.xml.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class DDLx {
  private static final Logger logger = LoggerFactory.getLogger(DDLx.class);
  private static final Comparator<$Table> tableNameComparator = (o1, o2) -> o1 == null ? o2 == null ? 0 : 1 : o2 == null ? -1 : o1.getName$().text().compareTo(o2.getName$().text());
  private static final URL normalizeXsl;
  private static final URL mergeXsl;

  static {
    normalizeXsl = findResource("normalize.xsl");
    mergeXsl = findResource("merge.xsl");
  }

  private static URL findResource(final String name) {
    final URL url = DDLx.class.getClassLoader().getResource(name);
    if (url == null)
      throw new ExceptionInInitializerError("Unable to find " + name + " in class loader " + DDLx.class.getClassLoader());

    return url;
  }

  private static Schema topologicalSort(final Schema schema) {
    final ArrayList<$Table> tables = new ArrayList<>(schema.getTable());
    schema.getTable().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<$Table,String> digraph = new RefDigraph<>(table -> table.getName$().text());
    for (int i = 0, i$ = tables.size(); i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      digraph.add(table);
      final Extends$ extends$ = table.getExtends$();
      if (extends$ != null)
        digraph.add(table, extends$.text());

      final BindingList<$Column> columns = table.getColumn();
      if (columns != null) {
        for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
          final $Column column = columns.get(j);
          if (column.getForeignKey() != null)
            digraph.add(table, column.getForeignKey().getReferences$().text());
        }
      }

      final $Constraints constraints = table.getConstraints();
      if (constraints != null) {
        final BindingList<$ForeignKeyComposite> foreignKeys = constraints.getForeignKey();
        if (foreignKeys != null) {
          for (int j = 0, j$ = foreignKeys.size(); j < j$; ++j) // [RA]
            digraph.add(table, foreignKeys.get(j).getReferences$().text());
        }
      }
    }

    if (digraph.hasCycle())
      throw new IllegalStateException("Cycle exists in relational model: " + CollectionUtil.toString(digraph.getCycle(), " -> "));

    final ArrayList<$Table> topologicalOrder = digraph.getTopologicalOrder();
    for (int i = topologicalOrder.size() - 1; i >= 0; --i) // [RA]
      schema.getTable().add(topologicalOrder.get(i));

    return schema;
  }

  private final URL url;
  private final String normalizeddXml;
  private final String mergedXml;
  private final Schema normalizedSchema;
  private final Schema mergedSchema;

  public DDLx(final URL url) throws IOException, SAXException, TransformerException {
    if (logger.isDebugEnabled()) logger.debug("new DDLx(\"" + url + "\")");

    this.url = url;

    this.normalizeddXml = Transformer.transform(normalizeXsl, url);
    this.normalizedSchema = topologicalSort((Schema)Bindings.parse(normalizeddXml));

    this.mergedXml = Transformer.transform(mergeXsl, normalizeddXml, null);
    this.mergedSchema = topologicalSort((Schema)Bindings.parse(mergedXml));
    final BindingList<$Table> tables = mergedSchema.getTable();
    for (int i = 0, i$ = tables.size(); i < i$; ++i) // [RA]
      if (tables.get(i).getExtends$() != null)
        throw new IllegalStateException("Input schema is not merged");
  }

  // FIXME: Remove this.
  public boolean isUnique(final $Table table, final $Named column) {
    final $Constraints constraints = table.getConstraints();
    if (constraints != null) {
      final BindingList<$Columns> uniques = constraints.getUnique();
      if (uniques != null) {
        for (int i = 0, i$ = uniques.size(); i < i$; ++i) { // [RA]
          final $Columns unique = uniques.get(i);
          if (unique.getColumn().size() == 1 && column.getName$().text().equals(unique.getColumn(0).getName$().text()))
            return true;
        }
      }
    }

    final $Indexes tableIndexes = table.getIndexes();
    if (tableIndexes != null) {
      final BindingList<Index> indexes = tableIndexes.getIndex();
      if (indexes != null) {
        for (int i = 0, i$ = indexes.size(); i < i$; ++i) { // [RA]
          final $Indexes.Index index = indexes.get(i);
          if (index.getUnique$() != null && index.getUnique$().text() && index.getColumn().size() == 1 && column.getName$().text().equals(index.getColumn(0).getName$().text()))
            return true;
        }
      }
    }

    return false;
  }

  public void recreate(final Connection connection) throws GeneratorExecutionException, SQLException {
    Schemas.recreate(connection, this);
  }

  public Schema getNormalizedSchema() {
    return normalizedSchema;
  }

  public Schema getMergedSchema() {
    return mergedSchema;
  }

  public URL getUrl() {
    return url;
  }

  public String getMergedXml() {
    return mergedXml;
  }
}