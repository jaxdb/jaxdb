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

package org.openjax.rdb.ddlx;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.$Column;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.$Decimal;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.$Table;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.Schema;
import org.openjax.rdb.vendor.DBVendor;
import org.openjax.rdb.vendor.Dialect;
import org.openjax.standard.xml.api.ValidationException;
import org.openjax.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public abstract class DDLxTest {
  public static Schema recreateSchema(final Connection connection, final String ddlx) throws GeneratorExecutionException, IOException, SQLException, ValidationException {
    return recreateSchema(connection, ddlx, false);
  }

  public static Schema recreateSchema(final Connection connection, final String ddlx, final boolean unaltered) throws GeneratorExecutionException, IOException, SQLException, ValidationException {
    final Schema schema;
    try (final InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(ddlx + ".ddlx")) {
      schema = (Schema)Bindings.parse(new InputSource(in));
    }

    if (!unaltered) {
      final Dialect dialect = DBVendor.valueOf(connection.getMetaData()).getDialect();
      for (final $Table table : schema.getTable())
        if (table.getColumn() != null)
          for (final $Column column : table.getColumn())
            if (column instanceof $Decimal)
              (($Decimal)column).setPrecision$(new $Decimal.Precision$(dialect.decimalMaxPrecision()));
    }

    Schemas.recreate(connection, schema);
    return schema;
  }
}