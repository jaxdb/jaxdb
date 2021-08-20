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

package org.jaxdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.VendorRunner;
import org.jaxdb.sqlx.SQLxTest;
import org.junit.runner.RunWith;
import org.libj.jci.CompilationException;
import org.xml.sax.SAXException;

public abstract class WorldTest extends JSqlTest {
  private static final String name = "world";

  public static class Test extends JSqlTest {
    @org.junit.Test
    public void testCreate() throws CompilationException, GeneratorExecutionException, IOException, SAXException, TransformerException {
      // Keep this order! Otherwise, #createEntities() will fail due to ClassCastException
      // caused by collision of different binding builds for ddlx, sqlx, jsql schemas
      createEntities(name);
      SQLxTest.createXSDs("world");
    }
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Synchronized
  @VendorRunner.Vendor(Derby.class)
  @VendorRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends WorldTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Synchronized
  @VendorRunner.Vendor(MySQL.class)
  @VendorRunner.Vendor(PostgreSQL.class)
  @VendorRunner.Vendor(Oracle.class)
  public static class RegressionTest extends WorldTest {
  }

  @org.junit.Test
  public void testReloadJaxSB(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(connection, name);
    JSqlTest.loadEntitiesJaxSB(connection, name);
  }
}