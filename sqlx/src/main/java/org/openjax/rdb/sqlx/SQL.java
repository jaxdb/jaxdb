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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.fastjax.jci.CompilationException;
import org.fastjax.xml.transform.Transformer;
import org.libx4j.rdb.sqlx_0_9_9.Database;
import org.libx4j.rdb.sqlx_0_9_9.Insert;
import org.libx4j.rdb.sqlx_0_9_9.Row;
import org.libx4j.rdb.sqlx_0_9_9.xLzgluGCXYYJc.$Database;
import org.libx4j.rdb.sqlx_0_9_9.xLzgluGCXYYJc.$Insert;
import org.libx4j.rdb.sqlx_0_9_9.xLzgluGCXYYJc.$Row;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.rdb.sqlx.SqlJaxb.RowIterator;
import org.xml.sax.SAXException;

public final class SQL {
  public static void ddlx2sqlx(final URL ddlxFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    Transformer.transform(Thread.currentThread().getContextClassLoader().getResource("sqlx.xsl"), ddlxFile, xsdFile);
  }

  public static int[] INSERT(final Connection connection, final Database database) throws SQLException {
    return SqlJaxb.INSERT(connection, new RowIterator(database));
  }

  public static int[] INSERT(final Connection connection, final Insert insert) throws SQLException {
    return SqlJaxb.INSERT(connection, new RowIterator(insert));
  };

  public static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final URL ... xsds) throws CompilationException, IOException, JAXBException {
    SqlJaxb.xsd2jaxb(sourcesDestDir, classedDestDir, xsds);
  }

  public static void xsd2jaxb(final File sourcesDestDir, final File classedDestDir, final Set<URL> xsds) throws CompilationException, IOException, JAXBException {
    SqlJaxb.xsd2jaxb(sourcesDestDir, classedDestDir, xsds);
  }

  public static int[] INSERT(final Connection connection, final $Database database) throws SQLException {
    return SqlXsb.INSERT(connection, new SqlXsb.RowIterator(database));
  }

  public static int[] INSERT(final Connection connection, final $Insert insert) throws SQLException {
    return SqlXsb.INSERT(connection, new SqlXsb.RowIterator(insert));
  };

  public static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final URL ... xsds) {
    SqlXsb.xsd2xsb(sourcesDestDir, classedDestDir, xsds);
  }

  public static void xsd2xsb(final File sourcesDestDir, final File classedDestDir, final Set<URL> xsds) {
    SqlXsb.xsd2xsb(sourcesDestDir, classedDestDir, xsds);
  }

  public static void sqlx2sql(final DBVendor vendor, final URL sqlxFile, final File sqlFile, final File[] classpathFiles) throws IOException, SAXException {
    SqlXsb.sqlx2sql(vendor, sqlxFile, sqlFile, classpathFiles);
  }

  public static Iterator<Row> newRowIterator(final Database database) {
    return new SqlJaxb.RowIterator(database);
  }

  public static Iterator<$Row> newRowIterator(final $Database database) {
    return new SqlXsb.RowIterator(database);
  }

  private SQL() {
  }
}