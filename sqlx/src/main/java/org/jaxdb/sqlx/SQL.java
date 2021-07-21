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
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.Schemas;
import org.jaxdb.sqlx_0_5.Database;
import org.jaxdb.sqlx_0_5.Row;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Row;
import org.jaxsb.runtime.Bindings;
import org.libj.jci.CompilationException;
import org.libj.net.URIs;
import org.libj.net.URLs;
import org.openjax.xml.dom.DOMs;
import org.openjax.xml.transform.Transformer;
import org.xml.sax.SAXException;

public final class SQL {
  private static final String fileName = "sqlx.xsl";

  public static void ddlx2sqlXsd(final URI ddlxFile, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();
    final URL resource = SQL.class.getClassLoader().getResource(fileName);
    if (resource == null)
      throw new IllegalStateException("Unable to find " + fileName + " in class loader " + SQL.class.getClassLoader());

    try {
      final URL url = ddlxFile.toURL();
      final Schema schema = (Schema)Bindings.parse(url);
      final Schema sorted = Schemas.flatten(schema);
      final File parentFile = new File(System.getProperty("java.io.tmpdir"), "jaxdb");
      parentFile.deleteOnExit();

      final File file = new File(parentFile, "sqlx" + File.separator + URLs.getName(url));
      file.getParentFile().mkdirs();
      file.deleteOnExit();

      Files.write(file.toPath(), DOMs.domToString(sorted.marshal()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      Transformer.transform(URIs.fromURL(resource), file.toURI(), xsdFile);
    }
    catch (final SAXException e) {
      throw new RuntimeException(e);
    }
  }

  public static int[] INSERT(final Connection connection, final Database database) throws IOException, SQLException {
    return new SqlJaxbLoader(connection).INSERT(new SqlJaxbLoader.RowIterator(database));
  }

  public static void xsd2jaxb(final File destDir, final URI ... xsds) throws CompilationException, IOException, JAXBException {
    SqlJaxbLoader.xsd2jaxb(destDir, xsds);
  }

  public static void xsd2jaxb(final File destDir, final Collection<URI> xsds) throws CompilationException, IOException, JAXBException {
    SqlJaxbLoader.xsd2jaxb(destDir, xsds);
  }

  public static int[] INSERT(final Connection connection, final $Database database) throws IOException, SQLException {
    return new SqlXsbLoader(connection).INSERT(new SqlXsbLoader.RowIterator(database));
  }

  public static void xsd2xsb(final File destDir, final URI ... xsds) throws IOException {
    SqlXsbLoader.xsd2xsb(destDir, xsds);
  }

  public static void xsd2xsb(final File destDir, final Collection<URI> xsds) throws IOException {
    SqlXsbLoader.xsd2xsb(destDir, xsds);
  }

  public static void sqlx2sql(final DBVendor vendor, final $Database database, final File sqlOutputFile) throws IOException {
    SqlXsbLoader.sqlx2sql(vendor, database, sqlOutputFile);
  }

  public static Iterator<Row> newRowIterator(final Database database) {
    return new SqlJaxbLoader.RowIterator(database);
  }

  public static Iterator<$Row> newRowIterator(final $Database database) {
    return new SqlXsbLoader.RowIterator(database);
  }

  private SQL() {
  }
}