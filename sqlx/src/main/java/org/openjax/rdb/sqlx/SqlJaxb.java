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
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.fastjax.io.FastFiles;
import org.fastjax.jci.CompilationException;
import org.fastjax.jci.JavaCompiler;
import org.fastjax.net.URLs;
import org.fastjax.util.ArrayIntList;
import org.fastjax.util.ClassLoaders;
import org.fastjax.util.FastArrays;
import org.fastjax.util.FastCollections;
import org.fastjax.util.IntList;
import org.fastjax.util.JavaIdentifiers;
import org.fastjax.xml.jaxb.JaxbUtil;
import org.fastjax.xml.jaxb.XJCompiler;
import org.fastjax.xml.sax.XMLDocument;
import org.fastjax.xml.sax.XMLDocuments;
import org.openjax.rdb.ddlx.dt;
import org.openjax.rdb.ddlx.annotation.Column;
import org.openjax.rdb.ddlx.annotation.Table;
import org.openjax.rdb.sqlx_0_9_9.Database;
import org.openjax.rdb.sqlx_0_9_9.Insert;
import org.openjax.rdb.sqlx_0_9_9.Row;
import org.openjax.rdb.vendor.DBVendor;
import org.xml.sax.SAXException;

final class SqlJaxb {
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

    throw new UnsupportedOperationException("Unsupported generateOnInsert=" + generateOnInsert + " spec for " + dataType.getCanonicalName());
  }

  protected static class RowIterator implements Iterator<Row> {
    private final Insert insert;
    private final String[] tableNames;
    private Iterator<Row> rows;
    private int index = 0;

    public RowIterator(final Insert insert) {
      this.insert = insert;
      this.tableNames = insert.getClass().getAnnotation(XmlType.class).propOrder();
      nextRows();
    }

    public RowIterator(final Database database) {
      try {
        this.insert = (Insert)database.getClass().getMethod("getInsert").invoke(database);
        this.tableNames = insert.getClass().getAnnotation(XmlType.class).propOrder();
        nextRows();
      }
      catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @SuppressWarnings("unchecked")
    private void nextRows() {
      if (index >= tableNames.length)
        return;

      try {
        do {
          this.rows = ((List<Row>)insert.getClass().getMethod("get" + JavaIdentifiers.toClassCase(tableNames[index++])).invoke(insert)).iterator();
        }
        while (!this.rows.hasNext() && index < tableNames.length);
      }
      catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new UnsupportedOperationException(e);
      }
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
        nextRows();

      return row;
    }
  };

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

  protected static int[] INSERT(final Connection connection, final RowIterator iterator) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final IntList counts = new ArrayIntList();

    try {
      if (!iterator.hasNext())
        return new int[0];

      // TODO: Implement batch.
      while (iterator.hasNext()) {
        try (final Statement statement = connection.createStatement()) {
          counts.add(statement.executeUpdate(loadRow(vendor, iterator.next())));
        }
      }
    }
    catch (final IllegalAccessException | InvocationTargetException e) {
      throw new UnsupportedOperationException(e);
    }

    final int[] array = new int[counts.size()];
    for (int i = 0; i < counts.size(); ++i)
      array[i] = counts.get(i);

    return array;
  }

  @SuppressWarnings("unchecked")
  public static void sqlx2sql(final DBVendor vendor, final URL sqlxFile, final File sqlFile) throws IOException, SAXException, UnmarshalException {
    sqlFile.getParentFile().mkdirs();

    final XMLDocument xmlDocument = XMLDocuments.parse(sqlxFile, false, true);
    final QName rootElement = xmlDocument.getRootElement();

    Class<Database> bindingClass;
    try {
      bindingClass = (Class<Database>)Class.forName(rootElement.getLocalPart() + ".sqlx." + JavaIdentifiers.toClassCase(rootElement.getLocalPart()));
    }
    catch (final ClassNotFoundException e) {
      final File sqlxTempDir = new File(FastFiles.getTempDir(), "sqlx");
      // FIXME: Files.deleteAllOnExit() is not working!
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            FastFiles.deleteAll(sqlxTempDir.toPath());
          }
          catch (final IOException e) {
            throw new UnsupportedOperationException(e);
          }
        }
      });
      sqlxTempDir.deleteOnExit();
      final File tempDir = new File(sqlxTempDir, rootElement.getLocalPart());
      try {
        xsd2jaxb(tempDir, tempDir, xmlDocument.getSchemaLocation());
        final URLClassLoader classLoader = new URLClassLoader(FastArrays.concat(URLs.toURL(ClassLoaders.getClassPath()), tempDir.toURI().toURL()), Thread.currentThread().getContextClassLoader());
        bindingClass = (Class<Database>)Class.forName(rootElement.getLocalPart() + ".sqlx." + JavaIdentifiers.toClassCase(rootElement.getLocalPart()), true, classLoader);
      }
      catch (final ClassNotFoundException | CompilationException | JAXBException e1) {
        throw new UnsupportedOperationException(e1);
      }
    }

    try (final OutputStreamWriter out = new FileWriter(sqlFile)) {
      final Database database = JaxbUtil.parse(bindingClass, bindingClass.getClassLoader(), sqlxFile, false);
      final RowIterator iterator = new RowIterator(database);
      for (int i = 0; iterator.hasNext(); ++i) {
        if (i > 0)
          out.write('\n');

        out.append(loadRow(vendor, iterator.next())).append(';');
      }
    }
    catch (final IllegalAccessException | InvocationTargetException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final LinkedHashSet<URL> xsds) throws CompilationException, IOException, JAXBException {
    final XJCompiler.Command command = new XJCompiler.Command();
    command.setExtension(true);
    command.setDestDir(sourcesDestDir);

    final LinkedHashSet<URL> xjbs = new LinkedHashSet<>();
    xjbs.add(Thread.currentThread().getContextClassLoader().getResource("sqlx.xjb"));
    command.setXJBs(xjbs);

    final LinkedHashSet<URL> schemas = new LinkedHashSet<>();
    for (final URL xsd : xsds)
      schemas.add(xsd);

    command.setSchemas(xsds);

    XJCompiler.compile(command);
    new JavaCompiler(classedDestDir, command.getClasspath()).compile(command.getDestDir());
  }

  public static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final Set<URL> xsds) throws CompilationException, IOException, JAXBException {
    xsd2jaxb(sourcesDestDir, classedDestDir, new LinkedHashSet<>(xsds));
  }

  public static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final URL ... xsds) throws CompilationException, IOException, JAXBException {
    xsd2jaxb(sourcesDestDir, classedDestDir, FastCollections.asCollection(new LinkedHashSet<URL>(), xsds));
  }

  private SqlJaxb() {
  }
}