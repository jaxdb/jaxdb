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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.TransformerException;

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Indexes;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
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
    final List<$Table> tables = new ArrayList<>(schema.getTable());
    schema.getTable().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<$Table,String> digraph = new RefDigraph<>(table -> table.getName$().text().toLowerCase());
    for (final $Table table : tables) {
      digraph.add(table);
      if (table.getColumn() != null)
        for (final $Column column : table.getColumn())
          if (column.getForeignKey() != null)
            digraph.add(table, column.getForeignKey().getReferences$().text().toLowerCase());

      if (table.getConstraints() != null && table.getConstraints().getForeignKey() != null)
        for (final $ForeignKeyComposite foreignKey : table.getConstraints().getForeignKey())
          digraph.add(table, foreignKey.getReferences$().text().toLowerCase());
    }

    if (digraph.hasCycle())
      throw new IllegalStateException("Cycle exists in relational model: " + CollectionUtil.toString(digraph.getCycle(), " -> "));

    final List<$Table> topologialOrder = digraph.getTopologicalOrder();
    final ListIterator<$Table> topological = topologialOrder.listIterator(digraph.size());
    while (topological.hasPrevious())
      schema.getTable().add(topological.previous());

    return schema;
  }

  private final URL url;
  private final String normalizeddXml;
  private final String mergedXml;
  private final Schema normalizedSchema;
  private final Schema mergedSchema;

  public DDLx(final URL url) throws IOException, SAXException, TransformerException {
    if (logger.isDebugEnabled())
      logger.debug("new DDLx(\"" + url + "\")");

    this.url = url;

    this.normalizeddXml = Transformer.transform(normalizeXsl, url);
    this.normalizedSchema = topologicalSort((Schema)Bindings.parse(normalizeddXml));
    consolidateEnums(normalizedSchema);

    this.mergedXml = Transformer.transform(mergeXsl, normalizeddXml, null);
    this.mergedSchema = topologicalSort((Schema)Bindings.parse(mergedXml));
    consolidateEnums(mergedSchema);
    for (final $Table table : mergedSchema.getTable())
      if (table.getExtends$() != null)
        throw new IllegalStateException("Input schema is not merged");
  }

  private static void consolidateEnums(final Schema schema) {
    final Map<String,String> enumToValues = new HashMap<>();
    final List<$Column> templates = schema.getTemplate();
    if (templates != null) {
      for (final $Column template : templates) {
        if (!(template instanceof $Enum))
          throw new IllegalStateException("Input schema is not normalized");

        enumToValues.put(template.getName$().text(), (($Enum)template).getValues$().text());
      }
    }

    final Map<String,$Table> tableNameToTable = new HashMap<>();
    for (final $Table table : schema.getTable()) {
      tableNameToTable.put(table.getName$().text(), table);
      if (table.getColumn() != null) {
        for (final $Column column : table.getColumn()) {
          if (column instanceof $Enum && column.getTemplate$() != null) {
            final $Enum type = ($Enum)column;
            final String values = enumToValues.get(column.getTemplate$().text());
            type.setValues$(new $Enum.Values$(Objects.requireNonNull(values)));
          }
        }
      }
    }
  }

  // FIXME: Remove this.
  public boolean isPrimary(final $Table table, final $Named column) {
    if (table.getConstraints() != null && table.getConstraints().getPrimaryKey() != null)
      for (final $Named col : table.getConstraints().getPrimaryKey().getColumn())
        if (column.getName$().text().equals(col.getName$().text()))
          return true;

    return false;
  }

  // FIXME: Remove this.
  public boolean isUnique(final $Table table, final $Named column) {
    if (table.getConstraints() != null && table.getConstraints().getUnique() != null)
      for (final $Columns unique : table.getConstraints().getUnique())
        if (unique.getColumn().size() == 1 && column.getName$().text().equals(unique.getColumn(0).getName$().text()))
          return true;

    if (table.getIndexes() != null && table.getIndexes().getIndex() != null)
      for (final $Indexes.Index index : table.getIndexes().getIndex())
        if (index.getUnique$() != null && index.getUnique$().text() && index.getColumn().size() == 1 && column.getName$().text().equals(index.getColumn(0).getName$().text()))
          return true;

    return false;
  }

  public void recreate(final Connection connection) throws GeneratorExecutionException, SQLException {
    Schemas.recreate(connection, this);
  }

  public Schema getNormalizedSchema() {
    return this.normalizedSchema;
  }

  public Schema getMergedSchema() {
    return this.mergedSchema;
  }

  public URL getUrl() {
    return this.url;
  }

  public String getMergedXml() {
    return this.mergedXml;
  }
}