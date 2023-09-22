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

import javax.xml.transform.TransformerException;

import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.jaxsb.runtime.BindingList;
import org.libj.net.MemoryURLStreamHandler;
import org.xml.sax.SAXException;

public abstract class DDLxTest {
  public static Schema recreateSchema(final Connection connection, final String name) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    return recreateSchema(connection, name, false);
  }

  // FIXME: The efficiency of this is TERRIBLE!
  public static Schema recreateSchema(final Connection connection, final String name, final boolean unaltered) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final URL url = ClassLoader.getSystemClassLoader().getResource(name + ".ddlx");
    if (url == null)
      throw new IllegalStateException("Cannot find " + name + ".ddlx");

    final DDLx ddlx = new DDLx(url);
    final Schema schema = ddlx.getNormalizedSchema();
    if (!unaltered) {
      final Dialect dialect = DbVendor.valueOf(connection.getMetaData()).getDialect();
      final BindingList<$Table> tables = schema.getTable();
      for (int i = 0, i$ = tables.size(); i < i$; ++i) { // [RA]
        final $Table table = tables.get(i);
        final BindingList<$Column> columns = table.getColumn();
        if (columns != null) {
          for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
            final $Column column = columns.get(j);
            if (column instanceof $Decimal) {
              final $Decimal decimal = ($Decimal)column;
              final int maxPrecision = dialect.decimalMaxPrecision();
              decimal.setPrecision$(new $Decimal.Precision$(maxPrecision));
              if (decimal.getScale$() != null && decimal.getScale$().text() > maxPrecision)
                decimal.setScale$(new $Decimal.Scale$(maxPrecision));
            }
          }
        }
      }
    }

    Schemas.recreate(connection, MemoryURLStreamHandler.createURL(schema.toString().getBytes()));
    return schema;
  }
}