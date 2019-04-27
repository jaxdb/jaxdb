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
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.ext.xml.api.ValidationException;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;

public abstract class TypesTest extends DDLxTest {
  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends TypesTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends TypesTest {
  }

  @Test
  public void test(final Connection connection) throws GeneratorExecutionException, IOException, SQLException, ValidationException {
    recreateSchema(connection, "types");
  }
}