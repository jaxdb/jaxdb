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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.TransformerException;

import org.lib4j.lang.Resources;
import org.lib4j.lang.Strings;
import org.libx4j.rdb.ddlx.Column;
import org.libx4j.rdb.ddlx.Table;
import org.libx4j.rdb.vendor.DBVendor;

public final class Datas {
  private static String getValue(final Compiler compiler, final sqlx.Column<?> value) {
    if (value == null)
      return null;

    if (value instanceof sqlx.BIGINT)
      return compiler.compile((sqlx.BIGINT)value);

    if (value instanceof sqlx.BINARY)
      return compiler.compile((sqlx.BINARY)value);

    if (value instanceof sqlx.BLOB)
      return compiler.compile((sqlx.BLOB)value);

    if (value instanceof sqlx.BOOLEAN)
      return compiler.compile((sqlx.BOOLEAN)value);

    if (value instanceof sqlx.CHAR)
      return compiler.compile((sqlx.CHAR)value);

    if (value instanceof sqlx.CLOB)
      return compiler.compile((sqlx.CLOB)value);

    if (value instanceof sqlx.DATE)
      return compiler.compile((sqlx.DATE)value);

    if (value instanceof sqlx.DATETIME)
      return compiler.compile((sqlx.DATETIME)value);

    if (value instanceof sqlx.DECIMAL)
      return compiler.compile((sqlx.DECIMAL)value);

    if (value instanceof sqlx.DOUBLE)
      return compiler.compile((sqlx.DOUBLE)value);

    if (value instanceof sqlx.ENUM)
      return compiler.compile((sqlx.ENUM)value);

    if (value instanceof sqlx.FLOAT)
      return compiler.compile((sqlx.FLOAT)value);

    if (value instanceof sqlx.INT)
      return compiler.compile((sqlx.INT)value);

    if (value instanceof sqlx.SMALLINT)
      return compiler.compile((sqlx.SMALLINT)value);

    if (value instanceof sqlx.TIME)
      return compiler.compile((sqlx.TIME)value);

    if (value instanceof sqlx.TINYINT)
      return compiler.compile((sqlx.TINYINT)value);

    throw new UnsupportedOperationException("Unsupported type: " + value.getClass());
  }

  @SuppressWarnings("unchecked")
  public static int[] insert(final Connection connection, final Insert insert) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final List<Integer> counts = new ArrayList<Integer>();

    try {
      final XmlType xmlType = insert.getClass().getAnnotation(XmlType.class);
      final List<Row> rows = new ArrayList<Row>();
      for (final String tableName : xmlType.propOrder())
        for (final Row row : (List<Row>)insert.getClass().getMethod("get" + Strings.toClassCase(tableName)).invoke(insert))
          rows.add(row);

      if (rows.size() == 0)
        return new int[0];

      // TODO: Implement batch.
      try (final Statement statement = connection.createStatement()) {
        for (final Row row : rows)
          counts.add(statement.executeUpdate(loadRow(vendor, row)));
      }
    }
    catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new UnsupportedOperationException(e);
    }

    final int[] array = new int[counts.size()];
    for (int i = 0; i < counts.size(); i++)
      array[i] = counts.get(i);

    return array;
  }

  public static int[] insert(final Connection connection, final Database database) throws SQLException {
    try {
      return insert(connection, (Insert)database.getClass().getMethod("getInsert").invoke(database));
    }
    catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private static String loadRow(final DBVendor vendor, final Row row) throws IllegalAccessException, InvocationTargetException {
    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();
    final Compiler compiler = Compiler.getCompiler(vendor);
    boolean hasValues = false;
    for (final Method method : row.getClass().getMethods()) {
      if (!method.getName().startsWith("get"))
        continue;

      final Column column = method.getAnnotation(Column.class);
      if (column == null)
        continue;

      final String value = getValue(compiler, (sqlx.Column<?>)method.invoke(row));
      if (value == null)
        continue;

      if (hasValues) {
        columns.append(", ");
        values.append(", ");
      }

      columns.append(column.name());
      values.append(value);
      hasValues = true;
    }

    final Table table = row.getClass().getAnnotation(Table.class);
    final StringBuilder builder = new StringBuilder("INSERT INTO ").append(table.name());
    builder.append(" (").append(columns).append(") VALUES (").append(values).append(")");
    return builder.toString();
  }

  public static void createXSD(final URL ddlxFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    org.lib4j.xml.transform.Transformer.transform(Resources.getResource("dmlx.xsl").getURL(), ddlxFile, xsdFile);
  }

  private Datas() {
  }
}