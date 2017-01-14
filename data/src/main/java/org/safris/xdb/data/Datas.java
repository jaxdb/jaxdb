/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.xml.transform.TransformerException;

import org.safris.commons.lang.ClassLoaders;
import org.safris.commons.lang.Resources;
import org.safris.xdb.schema.DBVendor;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xdd.xe.$xdd_row;
import org.safris.xsb.runtime.Binding;
import org.safris.xsb.runtime.Element;
import org.safris.xsb.runtime.QName;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
import org.w3.x2001.xmlschema.xe.$xs_string;

public final class Datas {
  private static QName getName(final Class<?> cls) {
    return cls.getAnnotation(QName.class);
  }

  private static String getColumn(final $xs_anySimpleType attribute) {
    final String id = attribute.id();
    return id.substring(id.indexOf('.') + 1);
  }

  private static String getValue(final $xs_anySimpleType attribute) {
    final Object value = attribute.text();
    if (value == null)
      return "NULL";

    if (attribute instanceof $xs_string)
      return "'" + String.valueOf(attribute.text()).replace("'", "''") + "'";

    return String.valueOf(value);
  }

  private static String getTableName(final DBVendor vendor, final $xdd_row row) {
    final Element element = (Element)row;
    final QName schemaName = getName(element.owner().getClass().getSuperclass());
    final QName tableName = getName(row.getClass().getSuperclass());
    if (vendor == DBVendor.MY_SQL)
      return schemaName.localPart() + "." + tableName.localPart();

    return tableName.localPart();
  }

  public static int[] loadData(final Connection connection, final $xdd_data data) throws SQLException {
    final DBVendor vendor = DBVendor.parse(connection.getMetaData());
    final Iterator<Binding> iterator = data.elementIterator();
    try (final Statement statement = connection.createStatement()) {
      while (iterator.hasNext())
        statement.addBatch(loadRow(vendor, ($xdd_row)iterator.next()));

      return statement.executeBatch();
    }
  }

  public static int loadRow(final Connection connection, final $xdd_row row) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      return statement.executeUpdate(loadRow(DBVendor.parse(connection.getMetaData()), row));
    }
  }

  private static String loadRow(final DBVendor vendor, final $xdd_row row) {
    final Iterator<? extends $xs_anySimpleType> iterator = row.attributeIterator();
    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();
    while (iterator.hasNext()) {
      final $xs_anySimpleType attribute = iterator.next();
      if (attribute != null) {
        columns.append(", ").append(getColumn(attribute));
        values.append(", ").append(getValue(attribute));
      }
    }

    final StringBuilder builder = new StringBuilder("INSERT INTO ").append(getTableName(vendor, row));
    builder.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
    return builder.toString();
  }

  public static void createXSD(final URL xdsFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    org.safris.commons.xml.transform.Transformer.transform(Resources.getResource("xdd.xsl").getURL(), xdsFile, xsdFile);
    ClassLoaders.addURL((URLClassLoader)ClassLoader.getSystemClassLoader(), xsdFile.getParentFile().toURI().toURL());
  }

  private Datas() {
  }
}