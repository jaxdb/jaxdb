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
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Row;
import org.openjax.xml.transform.Transformer;
import org.xml.sax.SAXException;

public final class SQL {
  private static final String xsl = "sqlx.xsl";
  private static final URL resource;

  static {
    resource = SQL.class.getClassLoader().getResource(xsl);
    if (resource == null)
      throw new ExceptionInInitializerError("Unable to find " + xsl + " in class loader " + SQL.class.getClassLoader());
  }

  public static void ddlx2xsd(final URL ddlxUrl, final File xsdFile) throws IOException, TransformerException {
    xsdFile.getParentFile().mkdirs();

    try {
      final DDLx ddlx = new DDLx(ddlxUrl);
      Transformer.transform(resource, ddlx.getXml(), ddlx.getUrl().toString(), xsdFile);
    }
    catch (final SAXException e) {
      throw new RuntimeException(e);
    }
  }

  public static int[] INSERT(final Connection connection, final $Database database) throws IOException, SQLException {
    return new SqlJaxSBLoader(connection).INSERT(new SqlJaxSBLoader.RowIterator(database));
  }

  public static void xsd2jaxsb(final File destDir, final URL ... xsds) throws IOException {
    SqlJaxSBLoader.xsd2jaxsb(destDir, xsds);
  }

  public static void xsd2jaxsb(final File destDir, final Collection<URL> xsds) throws IOException {
    SqlJaxSBLoader.xsd2jaxsb(destDir, xsds);
  }

  public static void sqlx2sql(final DBVendor vendor, final $Database database, final File sqlOutputFile) throws IOException {
    SqlJaxSBLoader.sqlx2sql(vendor, database, sqlOutputFile);
  }

  public static Iterator<$Row> newRowIterator(final $Database database) {
    return new SqlJaxSBLoader.RowIterator(database);
  }

  private SQL() {
  }
}