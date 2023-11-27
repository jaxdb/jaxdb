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
import java.util.Iterator;

import javax.xml.transform.TransformerException;

import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ColumnIndex;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$TableCommon;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$TableCommon.Extends$;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema.Table;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema.Table.Constraints;
import org.jaxsb.runtime.BindingList;
import org.jaxsb.runtime.Bindings;
import org.libj.util.CollectionUtil;
import org.libj.util.RefDigraph;
import org.openjax.xml.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.xml.sax.SAXException;

public class DDLx {
  private static final Logger logger = LoggerFactory.getLogger(DDLx.class);
  private static final Comparator<$Named> tableNameComparator = (final $Named o1, final $Named o2) -> o1 == null ? o2 == null ? 0 : 1 : o2 == null ? -1 : o1.getName$().text().compareTo(o2.getName$().text());
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

  /*static $ForeignKeyUnary newForeignKey(final $Column column) {
    if (column instanceof $Tinyint)
      return new $Tinyint.ForeignKey();

    if (column instanceof $Smallint)
      return new $Smallint.ForeignKey();

    if (column instanceof $Int)
      return new $Int.ForeignKey();

    if (column instanceof $Bigint)
      return new $Bigint.ForeignKey();

    if (column instanceof $Float)
      return new $Float.ForeignKey();

    if (column instanceof $Double)
      return new $Double.ForeignKey();

    if (column instanceof $Decimal)
      return new $Decimal.ForeignKey();

    if (column instanceof $Binary)
      return new $Binary.ForeignKey();

    if (column instanceof $Blob)
      return new $Blob.ForeignKey();

    if (column instanceof $Char)
      return new $Char.ForeignKey();

    if (column instanceof $Clob)
      return new $Clob.ForeignKey();

    if (column instanceof $Enum)
      return new $Enum.ForeignKey();

    if (column instanceof $Date)
      return new $Date.ForeignKey();

    if (column instanceof $Time)
      return new $Time.ForeignKey();

    if (column instanceof $Datetime)
      return new $Datetime.ForeignKey();

    if (column instanceof $Boolean)
      return new $Boolean.ForeignKey();

    throw new RuntimeException("Unknown column type: " + column.getClass().getName());
  }*/

  static void setForeignKey(final $Column column, final $ForeignKeyUnary foreignKey) {
    if (column instanceof $Tinyint)
      (($Tinyint)column).setForeignKey(foreignKey);
    else if (column instanceof $Smallint)
      (($Smallint)column).setForeignKey(foreignKey);
    if (column instanceof $Int)
      (($Int)column).setForeignKey(foreignKey);
    else if (column instanceof $Bigint)
      (($Bigint)column).setForeignKey(foreignKey);
    else if (column instanceof $Float)
      (($Float)column).setForeignKey(foreignKey);
    else if (column instanceof $Double)
      (($Double)column).setForeignKey(foreignKey);
    else if (column instanceof $Decimal)
      (($Decimal)column).setForeignKey(foreignKey);
    else if (column instanceof $Binary)
      (($Binary)column).setForeignKey(foreignKey);
    else if (column instanceof $Blob)
      (($Blob)column).setForeignKey(foreignKey);
    else if (column instanceof $Char)
      (($Char)column).setForeignKey(foreignKey);
    else if (column instanceof $Clob)
      (($Clob)column).setForeignKey(foreignKey);
    else if (column instanceof $Enum)
      (($Enum)column).setForeignKey(foreignKey);
    else if (column instanceof $Date)
      (($Date)column).setForeignKey(foreignKey);
    else if (column instanceof $Time)
      (($Time)column).setForeignKey(foreignKey);
    else if (column instanceof $Datetime)
      (($Datetime)column).setForeignKey(foreignKey);
    else if (column instanceof $Boolean)
      (($Boolean)column).setForeignKey(foreignKey);
    else
      throw new RuntimeException("Unknown column type: " + column.getClass().getName());
  }

  private static $AnyType<?> getChildElement(final $AnyType<String> element, final String localName) {
    final Iterator<$AnyType<?>> childElements = element.elementIterator();
    while (childElements.hasNext()) {
      final $AnyType<?> childElement = childElements.next();
      if (localName.equals(childElement.name().getLocalPart()))
        return childElement;
    }

    return null;
  }

  public static $PrimaryKey getPrimaryKey(final Constraints column) {
    return ($PrimaryKey)getChildElement(column, "primaryKey");
  }

  public static $ForeignKeyUnary getForeignKey(final $Column column) {
    return ($ForeignKeyUnary)getChildElement(column, "foreignKey");
  }

  public static $ColumnIndex getIndex(final $Column column) {
    return ($ColumnIndex)getChildElement(column, "index");
  }

  private static Schema topologicalSort(final Schema schema) {
    final ArrayList<Table> tables = new ArrayList<>(schema.getTable());
    schema.getTable().clear();
    tables.sort(tableNameComparator);
    final RefDigraph<Table,String> digraph = new RefDigraph<>((final Table table) -> table.getName$().text());
    for (int i = 0, i$ = tables.size(); i < i$; ++i) { // [RA]
      final Table table = tables.get(i);
      digraph.add(table);
      final Extends$ extends$ = table.getExtends$();
      if (extends$ != null)
        digraph.add(table, extends$.text());

      final BindingList<$Column> columns = table.getColumn();
      if (columns != null) {
        for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
          final $ForeignKeyUnary foreignKey = getForeignKey(columns.get(j));
          if (foreignKey != null)
            digraph.add(table, foreignKey.getReferences$().text());
        }
      }

      final Table.Constraints constraints = table.getConstraints();
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

    final ArrayList<Table> topologicalOrder = digraph.getTopologicalOrder();
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
    if (logger.isDebugEnabled()) { logger.debug("new DDLx(\"" + url + "\")"); }

    this.url = url;

    this.normalizeddXml = Transformer.transform(normalizeXsl, url);
    this.normalizedSchema = topologicalSort((Schema)Bindings.parse(normalizeddXml));

    this.mergedXml = Transformer.transform(mergeXsl, normalizeddXml, null);
    this.mergedSchema = topologicalSort((Schema)Bindings.parse(mergedXml));
    final BindingList<Table> tables = mergedSchema.getTable();
    for (int i = 0, i$ = tables.size(); i < i$; ++i) { // [RA]
      final $TableCommon table = tables.get(i);
      if (table.getExtends$() != null)
        throw new IllegalStateException("Input schema is not merged");
    }
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