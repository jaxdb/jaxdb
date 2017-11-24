/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.ddlx;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.lib4j.lang.Resources;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Column;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Decimal;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Table;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.Schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;
import org.libx4j.xsb.runtime.Bindings;
import org.libx4j.xsb.runtime.ParseException;
import org.xml.sax.InputSource;

public abstract class DDLxTest {
  public static Schema recreateSchema(final Connection connection, final String ddlx) throws GeneratorExecutionException, IOException, ParseException, SQLException, ValidationException {
    return recreateSchema(connection, ddlx, false);
  }

  public static Schema recreateSchema(final Connection connection, final String ddlx, final boolean unaltered) throws GeneratorExecutionException, IOException, ParseException, SQLException, ValidationException {
    final Schema schema;
    try (final InputStream in = Resources.getResource(ddlx + ".ddlx").getURL().openStream()) {
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