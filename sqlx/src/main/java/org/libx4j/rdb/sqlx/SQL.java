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

package org.libx4j.rdb.sqlx;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.TransformerException;

import org.lib4j.lang.Classes;
import org.lib4j.lang.Resources;
import org.lib4j.lang.Strings;
import org.libx4j.rdb.ddlx.annotation.Column;
import org.libx4j.rdb.ddlx.annotation.Table;
import org.libx4j.rdb.vendor.DBVendor;

public final class SQL {
  private static String getValue(final Compiler compiler, final dt.DataType<?> value) {
    if (value == null)
      return null;

    if (value instanceof dt.BIGINT)
      return compiler.compile((dt.BIGINT)value);

    if (value instanceof dt.BINARY)
      return compiler.compile((dt.BINARY)value);

    if (value instanceof dt.BLOB)
      return compiler.compile((dt.BLOB)value);

    if (value instanceof dt.BOOLEAN)
      return compiler.compile((dt.BOOLEAN)value);

    if (value instanceof dt.CHAR)
      return compiler.compile((dt.CHAR)value);

    if (value instanceof dt.CLOB)
      return compiler.compile((dt.CLOB)value);

    if (value instanceof dt.DATE)
      return compiler.compile((dt.DATE)value);

    if (value instanceof dt.DATETIME)
      return compiler.compile((dt.DATETIME)value);

    if (value instanceof dt.DECIMAL)
      return compiler.compile((dt.DECIMAL)value);

    if (value instanceof dt.DOUBLE)
      return compiler.compile((dt.DOUBLE)value);

    if (value instanceof dt.ENUM)
      return compiler.compile((dt.ENUM)value);

    if (value instanceof dt.FLOAT)
      return compiler.compile((dt.FLOAT)value);

    if (value instanceof dt.INT)
      return compiler.compile((dt.INT)value);

    if (value instanceof dt.SMALLINT)
      return compiler.compile((dt.SMALLINT)value);

    if (value instanceof dt.TIME)
      return compiler.compile((dt.TIME)value);

    if (value instanceof dt.TINYINT)
      return compiler.compile((dt.TINYINT)value);

    throw new UnsupportedOperationException("Unsupported type: " + value.getClass());
  }

  private static String generateValue(final Compiler compiler, final Class<?> dataType, final String generateOnInsert) {
    if ("UUID".equals(generateOnInsert) && dt.CHAR.class == dataType)
      return getValue(compiler, new dt.CHAR(UUID.randomUUID().toString()));

    if ("TIMESTAMP".equals(generateOnInsert)) {
      if (dataType == dt.DATE.class)
        return getValue(compiler, new dt.DATE(LocalDate.now()));

      if (dataType == dt.DATETIME.class)
        return getValue(compiler, new dt.DATETIME(LocalDateTime.now()));

      if (dataType == dt.TIME.class)
        return getValue(compiler, new dt.TIME(LocalTime.now()));
    }

    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + Classes.getStrictName(dataType));
  }

  @SuppressWarnings("unchecked")
  public static int[] INSERT(final Connection connection, final Insert insert) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final List<java.lang.Integer> counts = new ArrayList<java.lang.Integer>();

    try {
      final XmlType xmlType = insert.getClass().getAnnotation(XmlType.class);
      final List<Row> rows = new ArrayList<Row>();
      for (final String tableName : xmlType.propOrder()) {
        for (final Row row : (List<Row>)insert.getClass().getMethod("get" + Strings.toClassCase(tableName)).invoke(insert)) {
          rows.add(row);
        }
      }

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

  public static int[] INSERT(final Connection connection, final Database database) throws SQLException {
    try {
      return INSERT(connection, (Insert)database.getClass().getMethod("getInsert").invoke(database));
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

      String value = getValue(compiler, (dt.DataType<?>)method.invoke(row));
      if (value == null) {
        if (column.generateOnInsert().length() == 0)
          continue;

        value = generateValue(compiler, method.getReturnType(), column.generateOnInsert());
      }

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

  public static void ddl2xsd(final URL ddlxFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    org.lib4j.xml.transform.Transformer.transform(Resources.getResource("sqlx.xsl").getURL(), ddlxFile, xsdFile);
  }

  private SQL() {
  }
}