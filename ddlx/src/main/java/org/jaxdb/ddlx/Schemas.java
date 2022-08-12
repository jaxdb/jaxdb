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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.xml.transform.TransformerException;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.xml.sax.SAXException;

// TODO: In addition to JAX-SB Schema objects, allow JAX-DB Schema objects also.
public final class Schemas {
  public static int[] drop(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, false, parseAudits(schemas));
  }

  public static int[] drop(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, false, parseAudits(schema));
  }

  public static int[] drop(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, false, parseAudits(schema, schemas));
  }

  public static int[] drop(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, false, parseAudits(schemas));
  }

  public static int[] dropBatched(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, true, parseAudits(schemas));
  }

  public static int[] dropBatched(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, true, parseAudits(schema));
  }

  public static int[] dropBatched(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, true, parseAudits(schema, schemas));
  }

  public static int[] dropBatched(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, false, true, parseAudits(schemas));
  }

  public static int[] create(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, false, parseAudits(schemas));
  }

  public static int[] create(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, false, parseAudits(schema));
  }

  public static int[] create(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, false, parseAudits(schema, schemas));
  }

  public static int[] create(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, false, parseAudits(schemas));
  }

  public static int[] createBatched(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, true, parseAudits(schemas));
  }

  public static int[] createBatched(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, true, parseAudits(schema));
  }

  public static int[] createBatched(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, true, parseAudits(schema, schemas));
  }

  public static int[] createBatched(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, false, true, true, parseAudits(schemas));
  }

  public static int[] recreate(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, false, parseAudits(schemas));
  }

  public static int[] recreate(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, false, parseAudits(schema));
  }

  public static int[] recreate(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, false, parseAudits(schema, schemas));
  }

  public static int[] recreate(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, false, parseAudits(schemas));
  }

  public static int[] recreateBatched(final Connection connection, final URL[] schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, true, parseAudits(schemas));
  }

  public static int[] recreateBatched(final Connection connection, final URL schema) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, true, parseAudits(schema));
  }

  public static int[] recreateBatched(final Connection connection, final URL schema, final URL ... schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, true, parseAudits(schema, schemas));
  }

  public static int[] recreateBatched(final Connection connection, final Collection<URL> schemas) throws GeneratorExecutionException, IOException, SQLException, SAXException, TransformerException {
    return exec(connection, true, true, true, parseAudits(schemas));
  }

  static void recreate(final Connection connection, final DDLx ... ddlxs) throws GeneratorExecutionException, SQLException {
    exec(connection, true, true, false, ddlxs);
  }

  private static final DDLx[] EMPTY_AUDITS = {};

  private static DDLx[] parseAudits(final URL[] urls) throws IOException, SAXException, TransformerException {
    if (urls.length == 0)
      return EMPTY_AUDITS;

    final DDLx[] audits = new DDLx[urls.length];
    for (int i = 0, i$ = urls.length; i < i$; ++i) // [A]
      audits[i] = new DDLx(urls[i]);

    return audits;
  }

  private static DDLx[] parseAudits(final URL url, URL ... urls) throws IOException, SAXException, TransformerException {
    final DDLx[] audits = new DDLx[urls.length + 1];
    audits[0] = new DDLx(url);
    for (int i = 0, i$ = urls.length; i < i$;) // [A]
      audits[++i] = new DDLx(urls[i]);

    return audits;
  }

  private static DDLx[] parseAudits(final Collection<URL> urls) throws IOException, SAXException, TransformerException {
    final int len = urls.size();
    if (len == 0)
      return EMPTY_AUDITS;

    final DDLx[] audits = new DDLx[len];
    final Iterator<URL> iterator = urls.iterator();
    for (int i = 0; i < len; ++i) // [A]
      audits[i] = new DDLx(iterator.next());

    return audits;
  }

  private static int[] exec(final Connection connection, final boolean drop, final boolean create, final boolean batched, final DDLx ... ddlxs) throws GeneratorExecutionException, SQLException {
    if (!drop && !create)
      return null;

    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    Compiler.getCompiler(vendor).init(connection);
    final int[] counts = new int[ddlxs.length];
    try (final java.sql.Statement sqlStatement = connection.createStatement()) {
      int i = 0;
      if (batched) {
        for (final DDLx ddlx : ddlxs) { // [A]
          final LinkedHashSet<Statement> statements = new Generator(ddlx).parse(vendor);
          for (final Statement statement : statements) // [S]
            if (drop && statement instanceof DropStatement || create && statement instanceof CreateStatement)
              sqlStatement.addBatch(statement.getSql());

          int count = 0;
          for (final int result : sqlStatement.executeBatch()) // [A]
            count += result;

          counts[i++] = count;
        }
      }
      else {
        for (final DDLx ddlx : ddlxs) { // [A]
          final LinkedHashSet<Statement> statements = new Generator(ddlx).parse(vendor);
          int count = 0;
          for (final Statement statement : statements) // [S]
            if (drop && statement instanceof DropStatement || create && statement instanceof CreateStatement)
              count += sqlStatement.executeUpdate(statement.getSql());

          counts[i++] = count;
        }
      }
    }

    return counts;
  }

  public static int[] truncate(final Connection connection, final $Table ... tables) throws SQLException {
    return truncate(connection, Arrays.asList(tables));
  }

  public static int[] truncate(final Connection connection, final Collection<? extends $Table> tables) throws SQLException {
    final Compiler compiler = Compiler.getCompiler(DBVendor.valueOf(connection.getMetaData()));
    try (final java.sql.Statement statement = connection.createStatement()) {
      for (final $Table table : tables) // [C]
        statement.addBatch(compiler.truncate(table.getName$().text()));

      return statement.executeBatch();
    }
  }

  private Schemas() {
  }
}