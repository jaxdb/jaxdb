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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.ddlx.runner.VendorRunner;
import org.junit.runner.RunWith;
import org.libj.jci.CompilationException;
import org.xml.sax.SAXException;

public abstract class ClassicModelsTest extends SQLxTest {
  private static final String name = "classicmodels";

  public static class Test extends SQLxTest {
    @org.junit.Test
    public void testCreate() throws CompilationException, IOException, JAXBException, TransformerException, URISyntaxException {
      createXSDs(name);
    }
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor(value=Derby.class, parallel=2)
  @VendorRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends ClassicModelsTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor(MySQL.class)
  @VendorRunner.Vendor(PostgreSQL.class)
  @VendorRunner.Vendor(Oracle.class)
  public static class RegressionTest extends ClassicModelsTest {
  }

  @org.junit.Test
  @VendorRunner.Order(0)
  public void testLoadData(final Connection connection) throws GeneratorExecutionException, IOException, SAXException, SQLException {
    DDLxTest.recreateSchema(connection, name);
    assertEquals(3864, loadData(connection, name).length);
  }

  @org.junit.Test
  @VendorRunner.Order(1)
  public void testCreateSql(final Connection connection) throws IOException, SAXException, SQLException, URISyntaxException {
    createSql(connection, name);
  }
}