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

import static org.libj.util.function.Throwing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlType;

import org.jaxdb.ddlx.dt;
import org.jaxdb.ddlx.annotation.Column;
import org.jaxdb.ddlx.annotation.Table;
import org.jaxdb.sqlx_0_4.Database;
import org.jaxdb.sqlx_0_4.Row;
import org.jaxdb.vendor.DBVendor;
import org.libj.jci.CompilationException;
import org.libj.jci.InMemoryCompiler;
import org.libj.lang.Identifiers;
import org.libj.net.URIs;
import org.libj.util.CollectionUtil;
import org.libj.util.FlatIterableIterator;
import org.libj.util.primitive.ArrayIntList;
import org.libj.util.primitive.IntList;
import org.openjax.jaxb.xjc.XJCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SqlJaxb {
  private static final Logger logger = LoggerFactory.getLogger(SqlJaxb.class);

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
      return compiler.compile(new dt.CHAR(UUID.randomUUID().toString()));

    if ("TIMESTAMP".equals(generateOnInsert)) {
      if (dataType == dt.DATE.class)
        return compiler.compile(new dt.DATE(LocalDate.now()));

      if (dataType == dt.DATETIME.class)
        return compiler.compile(new dt.DATETIME(LocalDateTime.now()));

      if (dataType == dt.TIME.class)
        return compiler.compile(new dt.TIME(LocalTime.now()));
    }

    if ("EPOCH_MINUTES".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampMinutesFunction();

    if ("EPOCH_SECONDS".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampSecondsFunction();

    if ("EPOCH_MILLIS".equals(generateOnInsert))
      return compiler.getVendor().getDialect().currentTimestampMillisecondsFunction();

    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + dataType.getCanonicalName());
  }

  static class RowIterator extends FlatIterableIterator<Database,Row> {
    RowIterator(final Database database) {
      super(database);
    }

    @Override
    protected Iterator<?> iterator(final Database obj) {
      return new Iterator<Row>() {
        private final Database database = obj;
        private final String[] tableNames = database.getClass().getAnnotation(XmlType.class).propOrder();
        private Iterator<Row> rows = nextRows();
        private int index;

        @SuppressWarnings("unchecked")
        private Iterator<Row> nextRows() {
          if (index == tableNames.length)
            return null;

          Iterator<Row> rows;
          try {
            do {
              rows = ((Iterable<Row>)database.getClass().getMethod("get" + Identifiers.toClassCase(tableNames[index++])).invoke(database)).iterator();
            }
            while (!rows.hasNext() && index < tableNames.length);
          }
          catch (final IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
          }
          catch (final InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException)
              throw (RuntimeException)e.getCause();

            throw new RuntimeException(e.getCause());
          }

          return rows;
        }

        @Override
        public boolean hasNext() {
          return rows != null && rows.hasNext();
        }

        @Override
        public Row next() {
          if (rows == null)
            throw new NoSuchElementException();

          final Row row = rows.next();
          if (!rows.hasNext())
            rows = nextRows();

          return row;
        }
      };
    }

    @Override
    protected boolean isIterable(final Object obj) {
      return obj instanceof Database;
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

      columns.append(vendor.getDialect().quoteIdentifier(column.name()));
      values.append(value);
      hasValues = true;
    }

    final Table table = row.getClass().getAnnotation(Table.class);
    final StringBuilder builder = new StringBuilder("INSERT INTO ").append(vendor.getDialect().quoteIdentifier(table.name()));
    builder.append(" (").append(columns).append(") VALUES (").append(values).append(')');
    return builder.toString();
  }

  static int[] INSERT(final Connection connection, final RowIterator iterator) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());

    try {
      if (!iterator.hasNext())
        return new int[0];

      final IntList counts = new ArrayIntList();
      // TODO: Implement batch.
      while (iterator.hasNext()) {
        try (final Statement statement = connection.createStatement()) {
          counts.add(statement.executeUpdate(loadRow(vendor, iterator.next())));
        }
      }

      logger.warn("FIXME: SQLx does not yet consider sqlx:generateOnInsert");
      return counts.toArray();
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      if (e.getCause() instanceof SQLException)
        throw (SQLException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  public static int[] INSERT(final Connection connection, final Database database) throws SQLException {
    return INSERT(connection, new RowIterator(database));
  }

  public static void sqlx2sql(final DBVendor vendor, final Database database, final File sqlFile) throws IOException {
    sqlFile.getParentFile().mkdirs();

    final RowIterator iterator = new RowIterator(database);
    try (final OutputStreamWriter out = new FileWriter(sqlFile)) {
      for (int i = 0; iterator.hasNext(); ++i) {
        if (i > 0)
          out.write('\n');

        out.append(loadRow(vendor, iterator.next())).append(';');
      }
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      if (e.getCause() instanceof IOException)
        throw (IOException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  public static void xsd2jaxb(final File sourcesDestDir, final Set<URI> xsds) throws CompilationException, IOException, JAXBException {
    xsd2jaxb(sourcesDestDir, null, new LinkedHashSet<>(xsds));
  }

  static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final LinkedHashSet<URI> xsds) throws CompilationException, IOException, JAXBException {
    final XJCompiler.Command command = new XJCompiler.Command();
    command.setExtension(true);
    command.setDestDir(sourcesDestDir);

    final LinkedHashSet<URI> xjbs = new LinkedHashSet<>();
    xjbs.add(URIs.fromURL(ClassLoader.getSystemClassLoader().getResource("javaType.xjb")));
    xjbs.add(URIs.fromURL(ClassLoader.getSystemClassLoader().getResource("property-remote.xjb")));
    command.setXJBs(xjbs);

    command.setSchemas(xsds);

    XJCompiler.compile(command);

    final InMemoryCompiler compiler = new InMemoryCompiler();
    Files.walk(command.getDestDir().toPath())
      .filter(p -> p.getFileName().toString().endsWith(".java"))
      .map(Path::toFile)
      .forEach(rethrow((File f) -> compiler.addSource(new String(Files.readAllBytes(f.toPath())))));

    compiler.compile(new ArrayList<>(command.getClasspath()), classedDestDir);
  }

  static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final URI ... xsds) throws CompilationException, IOException, JAXBException {
    xsd2jaxb(sourcesDestDir, classedDestDir, CollectionUtil.asCollection(new LinkedHashSet<>(), xsds));
  }

  public static void xsd2jaxb(final File sourcesDestDir, final URI ... xsds) throws CompilationException, IOException, JAXBException {
    xsd2jaxb(sourcesDestDir, null, CollectionUtil.asCollection(new LinkedHashSet<>(), xsds));
  }

  private SqlJaxb() {
  }
}