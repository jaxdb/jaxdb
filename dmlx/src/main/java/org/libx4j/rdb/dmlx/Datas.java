/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.dmlx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.xml.transform.TransformerException;

import org.lib4j.lang.ClassLoaders;
import org.lib4j.lang.Resources;
import org.libx4j.rdb.dmlx.xe.$dmlx_binary;
import org.libx4j.rdb.dmlx.xe.$dmlx_blob;
import org.libx4j.rdb.dmlx.xe.$dmlx_boolean;
import org.libx4j.rdb.dmlx.xe.$dmlx_char;
import org.libx4j.rdb.dmlx.xe.$dmlx_clob;
import org.libx4j.rdb.dmlx.xe.$dmlx_data;
import org.libx4j.rdb.dmlx.xe.$dmlx_date;
import org.libx4j.rdb.dmlx.xe.$dmlx_dateTime;
import org.libx4j.rdb.dmlx.xe.$dmlx_decimal;
import org.libx4j.rdb.dmlx.xe.$dmlx_enum;
import org.libx4j.rdb.dmlx.xe.$dmlx_float;
import org.libx4j.rdb.dmlx.xe.$dmlx_integer;
import org.libx4j.rdb.dmlx.xe.$dmlx_row;
import org.libx4j.rdb.dmlx.xe.$dmlx_time;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;

public final class Datas {
  private static final Logger logger = LoggerFactory.getLogger(Datas.class);

  private static QName getName(final Class<?> cls) {
    return cls.getAnnotation(QName.class);
  }

  private static String getColumn(final $xs_anySimpleType attribute) {
    final String id = attribute.id();
    return id.substring(id.indexOf('.') + 1);
  }

  private static String getValue(final Compiler compiler, final $xs_anySimpleType attribute) {
    final Object value = attribute.text();
    if (value == null)
      return "NULL";

    if (attribute instanceof $dmlx_boolean)
      return compiler.compile(($dmlx_boolean)attribute);

    if (attribute instanceof $dmlx_float)
      return compiler.compile(($dmlx_float)attribute);

    if (attribute instanceof $dmlx_decimal)
      return compiler.compile(($dmlx_decimal)attribute);

    if (attribute instanceof $dmlx_integer)
      return compiler.compile(($dmlx_integer)attribute);

    if (attribute instanceof $dmlx_char)
      return compiler.compile(($dmlx_char)attribute);

    if (attribute instanceof $dmlx_clob)
      return compiler.compile(($dmlx_clob)attribute);

    if (attribute instanceof $dmlx_binary)
      return compiler.compile(($dmlx_binary)attribute);

    if (attribute instanceof $dmlx_blob)
      return compiler.compile(($dmlx_blob)attribute);

    if (attribute instanceof $dmlx_date)
      return compiler.compile(($dmlx_date)attribute);

    if (attribute instanceof $dmlx_time)
      return compiler.compile(($dmlx_time)attribute);

    if (attribute instanceof $dmlx_dateTime)
      return compiler.compile(($dmlx_dateTime)attribute);

    if (attribute instanceof $dmlx_enum)
      return compiler.compile(($dmlx_enum)attribute);

    throw new UnsupportedOperationException("Unsupported type: " + attribute.getClass());
  }

  private static String getTableName(final DBVendor vendor, final $dmlx_row row) {
//    final Element element = (Element)row;
//    final QName schemaName = getName(element.owner().getClass().getSuperclass());
    final QName tableName = getName(row.getClass().getSuperclass());
    return tableName.localPart();
  }

  public static int[] loadData(final Connection connection, final $dmlx_data data) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    int index = 0;
    Iterator<Binding> iterator = data.elementIterator();
    while (iterator.hasNext()) {
      iterator.next();
      index++;
    }

    final int[] counts = new int[index];
    index = 0;
    iterator = data.elementIterator();
    // TODO: implement batch
    try (final Statement statement = connection.createStatement()) {
      while (iterator.hasNext())
        counts[index++] = statement.executeUpdate(loadRow(vendor, ($dmlx_row)iterator.next()));

      return counts;
    }
  }

  public static int loadRow(final Connection connection, final $dmlx_row row) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      return statement.executeUpdate(loadRow(DBVendor.valueOf(connection.getMetaData()), row));
    }
  }

  private static String loadRow(final DBVendor vendor, final $dmlx_row row) {
    final Iterator<? extends $xs_anySimpleType> iterator = row.attributeIterator();
    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();
    final Compiler compiler = Compiler.getCompiler(vendor);
    boolean hasValues = false;
    while (iterator.hasNext()) {
      final $xs_anySimpleType attribute = iterator.next();
      if (attribute != null) {
        if (hasValues) {
          columns.append(", ");
          values.append(", ");
        }

        columns.append(getColumn(attribute));
        values.append(getValue(compiler, attribute));
        hasValues = true;
      }
    }

    final StringBuilder builder = new StringBuilder("INSERT INTO ").append(getTableName(vendor, row));
    builder.append(" (").append(columns).append(") VALUES (").append(values).append(")");
    return builder.toString();
  }

  public static void createXSD(final URL ddlxFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    org.lib4j.xml.transform.Transformer.transform(Resources.getResource("dmlx.xsl").getURL(), ddlxFile, xsdFile);
    // FIXME: This does not work in java 9.
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    if (classLoader instanceof URLClassLoader)
      ClassLoaders.addURL((URLClassLoader)classLoader, xsdFile.getParentFile().toURI().toURL());
    else
      logger.warn("Unable to add " + xsdFile.getParentFile().toURI().toURL() + " to system ClassLoader.");
  }

  private Datas() {
  }
}