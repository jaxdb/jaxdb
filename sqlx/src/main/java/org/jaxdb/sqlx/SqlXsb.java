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
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Bigint;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Binary;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Blob;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Boolean;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Char;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Clob;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Date;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Datetime;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Decimal;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Double;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Enum;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Float;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Int;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Smallint;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Time;
import org.jaxdb.www.datatypes_0_4.xL3gluGCXAA.$Tinyint;
import org.jaxdb.www.sqlx_0_4.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_4.xLygluGCXAA.$Row;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.generator.Generator;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.Id;
import org.libj.lang.Classes;
import org.libj.util.FlatIterableIterator;
import org.libj.util.primitive.ArrayIntList;
import org.libj.util.primitive.IntList;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

// FIXME: This class has a lot of copy+paste with SqlXsb
final class SqlXsb {
  private static String getValue(final Compiler compiler, final $AnySimpleType value) {
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
      return compiler.compile(value.text() == null ? null : new dt.INT((($Int)value).text().longValue()));

    if (value instanceof $Smallint)
      return compiler.compile(value.text() == null ? null : new dt.SMALLINT((($Smallint)value).text().intValue()));

    if (value instanceof $Time)
      return compiler.compile(new dt.TIME((($Time)value).text()));

    if (value instanceof $Tinyint)
      return compiler.compile(value.text() == null ? null : new dt.TINYINT((($Tinyint)value).text().shortValue()));

    throw new UnsupportedOperationException("Unsupported type: " + value.getClass());
  }

  private static String generateValue(final Compiler compiler, final Class<? extends $AnySimpleType> type, final String generateOnInsert) {
    if ("UUID".equals(generateOnInsert) && $Char.class.isAssignableFrom(type))
      return compiler.compile(new dt.CHAR(UUID.randomUUID().toString()));

    if ("TIMESTAMP".equals(generateOnInsert)) {
      if ($Date.class.isAssignableFrom(type))
        return compiler.getVendor().getDialect().currentDateFunction();

      if ($Datetime.class.isAssignableFrom(type))
        return compiler.getVendor().getDialect().currentDateTimeFunction();

      if ($Time.class.isAssignableFrom(type))
        return compiler.getVendor().getDialect().currentTimeFunction();
    }

    if ("EPOCH_MINUTES".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampMinutesFunction();

    if ("EPOCH_SECONDS".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampSecondsFunction();

    if ("EPOCH_MILLIS".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampMillisecondsFunction();

    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + type.getCanonicalName());
  }

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

  @SuppressWarnings("unchecked")
  private static String loadRow(final Compiler compiler, final $Row row, final Map<String,Map<String,Integer>> tableToColumnToIncrement) {
    try {
      final String tableName = row.id();
      final StringBuilder columns = new StringBuilder();
      final StringBuilder values = new StringBuilder();

      boolean hasValues = false;
      final Method[] methods = Classes.getDeclaredMethodsWithAnnotationDeep(row.getClass(), Id.class);
      for (final Method method : methods) {
        if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
          continue;

        final Class<? extends $AnySimpleType> type = (Class<? extends $AnySimpleType>)method.getReturnType();
        final String id = type.getAnnotation(Id.class).value();
        final int d1 = id.indexOf('-');
        final int d2 = id.indexOf('-', d1 + 1);
        final String columnName;
        final String generateOnInsert;
        final boolean isAutoIncremented;
        if (d2 != -1) {
          columnName = id.substring(d1 + 1, d2);
          generateOnInsert = id.substring(d2 + 1);
          isAutoIncremented = "AUTO_INCREMENT".equals(generateOnInsert);
        }
        else {
          columnName = id.substring(d1 + 1);
          generateOnInsert = null;
          isAutoIncremented = false;
        }

        final $AnySimpleType attribute = ($AnySimpleType)method.invoke(row);
        String value = getValue(compiler, attribute);
        if (value == null) {
          if (generateOnInsert == null || isAutoIncremented)
            continue;

          value = generateValue(compiler, type, generateOnInsert);
        }
        else if (isAutoIncremented) {
          final Integer intValue = Integer.valueOf(value);
          Map<String,Integer> columnToIncrement = tableToColumnToIncrement.get(tableName);
          if (columnToIncrement == null)
            tableToColumnToIncrement.put(tableName, columnToIncrement = new HashMap<>(1));

          final Integer increment = columnToIncrement.get(columnName);
          if (increment == null || increment < intValue)
            columnToIncrement.put(columnName, intValue);
        }

        if (hasValues) {
          columns.append(", ");
          values.append(", ");
        }

        columns.append(compiler.getVendor().getDialect().quoteIdentifier(columnName));
        values.append(value);
        hasValues = true;
      }

      return compiler.insert(tableName, columns, values);
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

  static int[] INSERT(final Connection connection, final RowIterator iterator) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());

    if (!iterator.hasNext())
      return new int[0];

    final Compiler compiler = Compiler.getCompiler(vendor);
    final IntList counts = new ArrayIntList();
    final Map<String,Map<String,Integer>> tableToColumnToIncrement = new HashMap<>();
    // TODO: Implement batch.
    while (iterator.hasNext()) {
      try (final Statement statement = connection.createStatement()) {
        counts.add(statement.executeUpdate(loadRow(compiler, iterator.next(), tableToColumnToIncrement)));
      }
    }

    if (tableToColumnToIncrement.size() > 0) {
      for (final Map.Entry<String,Map<String,Integer>> entry : tableToColumnToIncrement.entrySet()) {
        for (final Map.Entry<String,Integer> columnToIncrement : entry.getValue().entrySet()) {
          try (final Statement statement = connection.createStatement()) {
            final String sql = compiler.restartWith(entry.getKey(), columnToIncrement.getKey(), columnToIncrement.getValue() + 1);
            if (sql != null)
              statement.executeUpdate(sql);
          }
        }
      }
    }

    return counts.toArray();
  }

  public static int[] INSERT(final Connection connection, final $Database database) throws SQLException {
    return INSERT(connection, new RowIterator(database));
  }

  public static void xsd2xsb(final File destDir, final URI ... xsds) throws IOException {
    xsd2xsb(destDir, null, xsds);
  }

  static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final URI ... xsds) throws IOException {
    final Set<SchemaReference> schemas = new HashSet<>();
    for (final URI xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null, false);
  }

  public static void xsd2xsb(final File destDir, final Set<URI> xsds) throws IOException {
    xsd2xsb(destDir, null, xsds);
  }

  static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final Set<URI> xsds) throws IOException {
    final Set<SchemaReference> schemas = new HashSet<>();
    for (final URI xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null, false);
  }

  public static void sqlx2sql(final DBVendor vendor, final $Database database, final File sqlOutputFile) throws IOException {
    sqlOutputFile.getParentFile().mkdirs();

    final Compiler compiler = Compiler.getCompiler(vendor);
    final RowIterator rowIterator = new RowIterator(database);
    final Map<String,Map<String,Integer>> tableToColumnToIncrement = new HashMap<>();
    try (final OutputStreamWriter out = new FileWriter(sqlOutputFile)) {
      for (int i = 0; rowIterator.hasNext(); ++i) {
        if (i > 0)
          out.write('\n');

        out.append(loadRow(compiler, rowIterator.next(), tableToColumnToIncrement)).append(';');
      }

      if (tableToColumnToIncrement.size() > 0) {
        for (final Map.Entry<String,Map<String,Integer>> entry : tableToColumnToIncrement.entrySet()) {
          for (final Map.Entry<String,Integer> columnToIncrement : entry.getValue().entrySet()) {
            final String sql = compiler.restartWith(entry.getKey(), columnToIncrement.getKey(), columnToIncrement.getValue() + 1);
            if (sql != null)
              out.append('\n').append(sql).append(';');
          }
        }
      }
    }
  }

  private SqlXsb() {
  }
}