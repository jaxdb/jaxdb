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
import java.sql.Connection;
import java.sql.SQLException;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.jaxsb.runtime.Bindings;
import org.xml.sax.SAXException;

public abstract class DDLxTest {
  public static Schema recreateSchema(final Connection connection, final String ddlx) throws GeneratorExecutionException, IOException, SAXException, SQLException {
    return recreateSchema(connection, ddlx, false);
  }

  public static Schema recreateSchema(final Connection connection, final String ddlx, final boolean unaltered) throws GeneratorExecutionException, IOException, SAXException, SQLException {
    final Schema schema = (Schema)Bindings.parse(ClassLoader.getSystemClassLoader().getResource(ddlx + ".ddlx"));
    if (!unaltered) {
      final Dialect dialect = DBVendor.valueOf(connection.getMetaData()).getDialect();
      for (final $Table table : schema.getTable()) {
        if (table.getColumn() != null) {
          for (final $Column column : table.getColumn()) {
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

    Schemas.recreate(connection, schema);
    return schema;
  }
}