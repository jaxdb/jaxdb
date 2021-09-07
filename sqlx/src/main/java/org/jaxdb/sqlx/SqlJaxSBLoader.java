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

package org.jaxdb.sqlx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Bigint;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Binary;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Blob;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Boolean;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Char;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Clob;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Date;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Datetime;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Decimal;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Double;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Enum;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Float;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Int;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Smallint;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Time;
import org.jaxdb.www.datatypes_0_5.xL3gluGCXAA.$Tinyint;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Row;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.generator.Generator;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.Id;
import org.libj.lang.Classes;
import org.libj.util.FlatIterableIterator;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

final class SqlJaxSBLoader extends SqlLoader {
  static class RowIterator extends FlatIterableIterator<$Database,$Row> {
    RowIterator(final $Database database) {
      super(database);
    }

    @Override
    protected Iterator<?> iterator(final $Database obj) {
      return obj.elementIterator();
    }

    @Override
    protected boolean isIterable(final Object obj) {
      return obj instanceof $Database;
    }
  }

  static void xsd2jaxsb(final File destDir, final URL ... xsds) throws IOException {
    xsd2jaxsb(destDir, null, xsds);
  }

  static void xsd2jaxsb(final File sourcesDestDir, final File classedDestDir, final URL ... xsds) throws IOException {
    final HashSet<SchemaReference> schemas = new HashSet<>();
    for (final URL xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null, false);
  }

  static void xsd2jaxsb(final File destDir, final Collection<URL> xsds) throws IOException {
    xsd2jaxsb(destDir, null, xsds);
  }

  static void xsd2jaxsb(final File sourcesDestDir, final File classedDestDir, final Collection<URL> xsds) throws IOException {
    final HashSet<SchemaReference> schemas = new HashSet<>();
    for (final URL xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null, false);
  }

  static void sqlx2sql(final DBVendor vendor, final $Database database, final File sqlOutputFile) throws IOException {
    sqlOutputFile.getParentFile().mkdirs();

    final ArrayList<Row> rows = new ArrayList<>();
    final RowIterator iterator = new RowIterator(database);
    final Compiler compiler = Compiler.getCompiler(vendor);
    final TableToColumnToIncrement tableToColumnToIncrement = new TableToColumnToIncrement();
    while (iterator.hasNext()) {
      loadRow(rows, iterator.next(), vendor.getDialect(), compiler, tableToColumnToIncrement);
    }

    rows.sort(null);
    final Iterator<Row> rowIterator = rows.iterator();
    try (final OutputStreamWriter out = new FileWriter(sqlOutputFile)) {
      for (int i = 0; rowIterator.hasNext(); ++i) {
        if (i > 0)
          out.write('\n');

        out.append(rowIterator.next().toString()).append(';');
      }

      if (tableToColumnToIncrement.size() > 0)
        for (final Map.Entry<String,Map<String,Integer>> entry : tableToColumnToIncrement.entrySet())
          for (final Map.Entry<String,Integer> columnToIncrement : entry.getValue().entrySet())
            compiler.sequenceReset(null, out, entry.getKey(), columnToIncrement.getKey(), columnToIncrement.getValue() + 1);
    }
    catch (final SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static String generateValue(final Dialect dialect, final Compiler compiler, final Class<? extends $AnySimpleType<?>> type, final String generateOnInsert) {
    if ("UUID".equals(generateOnInsert) && $Char.class.isAssignableFrom(type))
      return compiler.compile(new dt.CHAR(UUID.randomUUID().toString()));

    if ("TIMESTAMP".equals(generateOnInsert)) {
      if ($Date.class.isAssignableFrom(type))
        return dialect.currentDateFunction();

      if ($Datetime.class.isAssignableFrom(type))
        return dialect.currentDateTimeFunction();

      if ($Time.class.isAssignableFrom(type))
        return dialect.currentTimeFunction();
    }

    if ("EPOCH_MINUTES".equals(generateOnInsert))
      return dialect.currentTimestampMinutesFunction();

    if ("EPOCH_SECONDS".equals(generateOnInsert))
      return dialect.currentTimestampSecondsFunction();

    if ("EPOCH_MILLIS".equals(generateOnInsert))
      return dialect.currentTimestampMillisecondsFunction();

    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + type.getCanonicalName());
  }

  private static class Row implements Comparable<Row> {
    final int weight;
    final String sql;

    private Row(final int weight, final String sql) {
      this.weight = weight;
      this.sql = sql;
    }

    @Override
    public int compareTo(final Row o) {
      return Integer.compare(weight, o.weight);
    }

    @Override
    public int hashCode() {
      return weight * 31 + sql.hashCode() ;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Row))
        return false;

      final Row that = (Row)obj;
      return weight == that.weight && sql.equals(that.sql);
    }

    @Override
    public String toString() {
      return sql;
    }
  }

  @SuppressWarnings("unchecked")
  private static void loadRow(final ArrayList<Row> rows, final $Row row, final Dialect dialect, final Compiler compiler, final TableToColumnToIncrement tableToColumnToIncrement) {
    try {
      final int i = row.id().lastIndexOf('-');
      final String tableName = row.id().substring(0, i);
      final int weight = Integer.parseInt(row.id().substring(i + 1));
      final StringBuilder columns = new StringBuilder();
      final StringBuilder values = new StringBuilder();

      boolean hasValues = false;
      final Method[] methods = Classes.getDeclaredMethodsDeep(row.getClass());
      for (final Method method : methods) {
        if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
          continue;

        final Id id = method.getReturnType().getAnnotation(Id.class);
        if (id == null)
          continue;

        final String idValue = id.value();
        final int d1 = idValue.indexOf('-');
        final int d2 = idValue.indexOf('-', d1 + 1);
        final String columnName;
        final String generateOnInsert;
        final boolean isAutoIncremented;
        if (d2 != -1) {
          columnName = idValue.substring(d1 + 1, d2);
          generateOnInsert = idValue.substring(d2 + 1);
          isAutoIncremented = "AUTO_INCREMENT".equals(generateOnInsert);
        }
        else {
          columnName = idValue.substring(d1 + 1);
          generateOnInsert = null;
          isAutoIncremented = false;
        }

        final $AnySimpleType<?> attribute = ($AnySimpleType<?>)method.invoke(row);
        String value = getValue(compiler, attribute);

        if (value == null) {
          if (generateOnInsert == null || isAutoIncremented)
            continue;

          value = generateValue(dialect, compiler, (Class<? extends $AnySimpleType<?>>)method.getReturnType(), generateOnInsert);
        }
        else if (isAutoIncremented) {
          final Map<String,Integer> columnToIncrement = tableToColumnToIncrement.get(tableName);
          final Integer increment = columnToIncrement.get(columnName);
          final Integer intValue = Integer.valueOf(value);
          if (increment == null || increment < intValue)
            columnToIncrement.put(columnName, intValue);
        }

        if (hasValues) {
          columns.append(", ");
          values.append(", ");
        }

        columns.append(dialect.quoteIdentifier(columnName));
        values.append(value);
        hasValues = true;
      }

      rows.add(new Row(weight, compiler.insert(tableName, columns, values)));
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  private static String getValue(final Compiler compiler, final $AnySimpleType<?> value) {
    if (value == null)
      return null;

    if (value instanceof $Bigint)
      return compiler.compile(new dt.BIGINT((($Bigint)value).text()));

    if (value instanceof $Binary)
      return compiler.compile(new dt.BINARY((($Binary)value).text()));

    if (value instanceof $Blob)
      return compiler.compile(new dt.BLOB((($Blob)value).text()));

    if (value instanceof $Boolean)
      return compiler.compile(new dt.BOOLEAN((($Boolean)value).text()));

    if (value instanceof $Char)
      return compiler.compile(new dt.CHAR((($Char)value).text()));

    if (value instanceof $Clob)
      return compiler.compile(new dt.CLOB((($Clob)value).text()));

    if (value instanceof $Date)
      return compiler.compile(new dt.DATE((($Date)value).text()));

    if (value instanceof $Datetime)
      return compiler.compile(new dt.DATETIME((($Datetime)value).text()));

    if (value instanceof $Decimal)
      return compiler.compile(new dt.DECIMAL((($Decimal)value).text()));

    if (value instanceof $Double)
      return compiler.compile(new dt.DOUBLE((($Double)value).text()));

    if (value instanceof $Enum)
      return compiler.compile(new dt.ENUM((($Enum)value).text()));

    if (value instanceof $Float)
      return compiler.compile(new dt.FLOAT((($Float)value).text()));

    if (value instanceof $Int)
      return compiler.compile(value.text() == null ? null : new dt.INT((($Int)value).text()));

    if (value instanceof $Smallint)
      return compiler.compile(value.text() == null ? null : new dt.SMALLINT((($Smallint)value).text()));

    if (value instanceof $Time)
      return compiler.compile(new dt.TIME((($Time)value).text()));

    if (value instanceof $Tinyint)
      return compiler.compile(value.text() == null ? null : new dt.TINYINT((($Tinyint)value).text()));

    throw new UnsupportedOperationException("Unsupported type: " + value.getClass().getName());
  }

  SqlJaxSBLoader(final Connection connection) throws SQLException {
    super(connection);
  }

  int[] INSERT(final RowIterator iterator) throws IOException, SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    if (!iterator.hasNext())
      return new int[0];

    final int[] counts;
    final Compiler compiler = Compiler.getCompiler(vendor);
    final ArrayList<Row> rows = new ArrayList<>();
    final TableToColumnToIncrement tableToColumnToIncrement = new TableToColumnToIncrement();
    while (iterator.hasNext()) {
      loadRow(rows, iterator.next(), getDialect(), compiler, tableToColumnToIncrement);
    }

    rows.sort(null);
    try (final Statement statement = connection.createStatement()) {
      for (final Row row : rows)
        statement.addBatch(row.toString());

      counts = statement.executeBatch();
    }

    if (tableToColumnToIncrement.size() > 0)
      for (final Map.Entry<String,Map<String,Integer>> entry : tableToColumnToIncrement.entrySet())
        for (final Map.Entry<String,Integer> columnToIncrement : entry.getValue().entrySet())
          compiler.sequenceReset(connection, null, entry.getKey(), columnToIncrement.getKey(), columnToIncrement.getValue() + 1);

    return counts;
  }

  int[] INSERT(final $Database database) throws IOException, SQLException {
    return INSERT(new RowIterator(database));
  }
}