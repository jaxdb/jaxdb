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
import org.xml.sax.SAXException;

public class DDLx {
  private static final Comparator<$Table> tableNameComparator = (o1, o2) -> o1 == null ? o2 == null ? 0 : 1 : o2 == null ? -1 : o1.getName$().text().compareTo(o2.getName$().text());
  private static final String xsl = "normalize.xsl";
  private static final URL resource;

  static {
    resource = DDLx.class.getClassLoader().getResource(xsl);
    if (resource == null)
      throw new ExceptionInInitializerError("Unable to find " + xsl + " in class loader " + DDLx.class.getClassLoader());
  }

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

  public final Map<String,$Table> tableNameToTable;
  private final URL url;
  private final String xml;
  private final Schema schema;

  public DDLx(final URL url) throws IOException, SAXException, TransformerException {
    this.url = url;
    this.xml = Transformer.transform(resource, url);
    this.tableNameToTable = new HashMap<>();
    this.schema = topologicalSort((Schema)Bindings.parse(xml));

    final Map<String,String> enumToValues = new HashMap<>();

    final List<$Column> templates = schema.getTemplate();
    if (templates != null) {
      for (final $Column template : templates) {
        if (!(template instanceof $Enum))
          throw new IllegalArgumentException("Input schema is not normalized");

        enumToValues.put(template.getName$().text(), (($Enum)template).getValues$().text());
      }
    }

    for (final $Table table : schema.getTable()) {
      tableNameToTable.put(table.getName$().text(), table);
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Enum && column.getTemplate$() != null) {
          final $Enum type = ($Enum)column;
          final String values = enumToValues.get(column.getTemplate$().text());
          type.setValues$(new $Enum.Values$(Objects.requireNonNull(values)));
        }
      }
    }
  }

  public boolean isPrimary(final $Table table, final $Named column) {
    if (table.getConstraints() != null && table.getConstraints().getPrimaryKey() != null)
      for (final $Named col : table.getConstraints().getPrimaryKey().getColumn())
        if (column.getName$().text().equals(col.getName$().text()))
          return true;

    return false;
  }

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

  public Schema getSchema() {
    return this.schema;
  }

  public URL getUrl() {
    return this.url;
  }

  public String getXml() {
    return this.xml;
  }
}