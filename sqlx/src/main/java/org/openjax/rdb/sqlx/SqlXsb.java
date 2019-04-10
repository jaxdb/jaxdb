/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.sqlx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Bigint;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Binary;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Blob;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Boolean;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Char;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Clob;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Date;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Datetime;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Decimal;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Double;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Enum;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Float;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Int;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Smallint;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Time;
import org.openjax.rdb.datatypes_0_9_9.xL5gluGCXYYJc.$Tinyint;
import org.openjax.rdb.ddlx.dt;
import org.openjax.rdb.sqlx_0_9_9.xL0gluGCXYYJc.$Database;
import org.openjax.rdb.sqlx_0_9_9.xL0gluGCXYYJc.$Insert;
import org.openjax.rdb.sqlx_0_9_9.xL0gluGCXYYJc.$Row;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.standard.io.FastFiles;
import org.openjax.standard.net.URLs;
import org.openjax.standard.util.ArrayIntList;
import org.openjax.standard.util.ClassLoaders;
import org.openjax.standard.util.Classes;
import org.openjax.standard.util.FastArrays;
import org.openjax.standard.util.IntList;
import org.openjax.standard.xml.sax.XMLDocument;
import org.openjax.standard.xml.sax.XMLDocuments;
import org.openjax.xsb.compiler.processor.GeneratorContext;
import org.openjax.xsb.compiler.processor.reference.SchemaReference;
import org.openjax.xsb.generator.Generator;
import org.openjax.xsb.runtime.Attribute;
import org.openjax.xsb.runtime.Binding;
import org.openjax.xsb.runtime.Bindings;
import org.openjax.xsb.runtime.Id;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.xml.sax.SAXException;

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


    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + type.getCanonicalName());
  }

  protected static class RowIterator implements Iterator<$Row> {
    private static $Insert getInsert(final $Database database) {
      final Iterator<Binding> interator = database.elementIterator();
      if (interator.hasNext()) {
        final Binding binding = interator.next();
        if (binding instanceof $Insert)
          return ($Insert)binding;

        throw new UnsupportedOperationException("Unsupported element: " + binding.name());
      }

      return null;
    }

    private Iterator<Binding> rows;

    public RowIterator(final $Insert insert) {
      this.rows = insert.elementIterator();
    }

    public RowIterator(final $Database database) {
      this(getInsert(database));
    }

    @Override
    public boolean hasNext() {
      return rows != null && rows.hasNext();
    }

    @Override
    public $Row next() {
      if (rows == null)
        throw new NoSuchElementException();

      return ($Row)rows.next();
    }
  }

  @SuppressWarnings("unchecked")
  private static String loadRow(final DBVendor vendor, final $Row row) {
    try {
      final StringBuilder columns = new StringBuilder();
      final StringBuilder values = new StringBuilder();
      final Compiler compiler = Compiler.getCompiler(vendor);

      boolean hasValues = false;
      final Method[] methods = Classes.getDeclaredMethodsWithAnnotationDeep(row.getClass(), Id.class);
      for (final Method method : methods) {
        if (!method.getName().startsWith("get") || !Attribute.class.isAssignableFrom(method.getReturnType()))
          continue;

        final Class<? extends $AnySimpleType> type = (Class<? extends $AnySimpleType>)method.getReturnType();
        final Id id = type.getAnnotation(Id.class);
        final $AnySimpleType attribute = ($AnySimpleType)method.invoke(row);
        final int colon1 = id.value().indexOf('-');
        final int colon2 = id.value().indexOf('-', colon1 + 1);
        final String columnName;
        final String generateOnInsert;
        if (colon2 != -1) {
          columnName = id.value().substring(colon1 + 1, colon2);
          generateOnInsert = id.value().substring(colon2 + 1);
        }
        else {
          columnName = id.value().substring(colon1 + 1);
          generateOnInsert = null;
        }

        String value = getValue(compiler, attribute);
        if (value == null) {
          if (generateOnInsert == null)
            continue;

          value = generateValue(compiler, type, generateOnInsert);
        }

        if (hasValues) {
          columns.append(", ");
          values.append(", ");
        }

        columns.append(vendor.getDialect().quoteIdentifier(columnName));
        values.append(value);
        hasValues = true;
      }

      final StringBuilder builder = new StringBuilder("INSERT INTO ").append(vendor.getDialect().quoteIdentifier(row.id()));
      builder.append(" (").append(columns).append(") VALUES (").append(values).append(')');
      return builder.toString();
    }
    catch (final IllegalAccessException | InvocationTargetException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  protected static int[] INSERT(final Connection connection, final RowIterator iterator) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final IntList counts = new ArrayIntList();

    if (!iterator.hasNext())
      return new int[0];

    // TODO: Implement batch.
    while (iterator.hasNext()) {
      try (final Statement statement = connection.createStatement()) {
        counts.add(statement.executeUpdate(loadRow(vendor, iterator.next())));
      }
    }

    final int[] array = new int[counts.size()];
    for (int i = 0; i < counts.size(); ++i)
      array[i] = counts.get(i);

    return array;
  }

  public static int[] INSERT(final Connection connection, final $Database database) throws SQLException {
    return INSERT(connection, new RowIterator(database));
  }

  public static int[] INSERT(final Connection connection, final $Insert insert) throws SQLException {
    return INSERT(connection, new RowIterator(insert));
  }

  public static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final URL ... xsds) {
    final Set<SchemaReference> schemas = new HashSet<>();
    for (final URL xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null);
  }

  public static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final Set<URL> xsds) {
    final Set<SchemaReference> schemas = new HashSet<>();
    for (final URL xsd : xsds)
      schemas.add(new SchemaReference(xsd, false));

    Generator.generate(new GeneratorContext(sourcesDestDir, true, classedDestDir, false, null, null), schemas, null);
  }

  public static void sqlx2sql(final DBVendor vendor, final URL sqlxFile, final File sqlFile, final File[] classpathFiles) throws IOException, SAXException {
    sqlFile.getParentFile().mkdirs();

    final XMLDocument xmlDocument = XMLDocuments.parse(sqlxFile, false, true);
    $Database database;
    try (final InputStream in = xmlDocument.getURL().openStream()) {
      database = ($Database)Bindings.parse(in);
    }
    catch (final Throwable t) {
      final File sqlxTempDir = new File(FastFiles.getTempDir(), "sqlx");
      // FIXME: Files.deleteAllOnExit() is not working!
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            FastFiles.deleteAll(sqlxTempDir.toPath());
          }
          catch (final IOException e) {
            throw new IllegalStateException(e);
          }
        }
      });

      xsd2xsb(sqlxTempDir, sqlxTempDir, xmlDocument.getSchemaLocation());

      final URLClassLoader classLoader = new URLClassLoader(FastArrays.concat(URLs.toURL(ClassLoaders.getClassPath()), sqlxTempDir.toURI().toURL()), ClassLoader.getSystemClassLoader());
      try (final InputStream in = xmlDocument.getURL().openStream()) {
        database = ($Database)Bindings.parse(in, classLoader);
      }
    }

    try (final OutputStreamWriter out = new FileWriter(sqlFile)) {
      final RowIterator iterator = new RowIterator(database);
      for (int i = 0; iterator.hasNext(); ++i) {
        if (i > 0)
          out.write('\n');

        out.append(loadRow(vendor, iterator.next())).append(';');
      }
    }
  }

  private SqlXsb() {
  }
}