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

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.runner.RunWith;
import org.openjax.standard.jci.CompilationException;
import org.openjax.standard.xml.api.ValidationException;
import org.openjax.rdb.ddlx.DDLxTest;
import org.openjax.rdb.ddlx.GeneratorExecutionException;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.rdb.ddlx.runner.VendorRunner.Order;
import org.xml.sax.SAXException;

public abstract class ClassicModelsTest extends SQLxTest {
  private static final String name = "classicmodels";

  public static class Test extends SQLxTest {
    @org.junit.Test
    public void testCreate() throws CompilationException, IOException, JAXBException, TransformerException {
      createXSDs(name);
    }
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends ClassicModelsTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends ClassicModelsTest {
  }

  @org.junit.Test
  @Order(0)
  public void testLoadData(final Connection connection) throws GeneratorExecutionException, IOException, SQLException, ValidationException {
    DDLxTest.recreateSchema(connection, name);
    assertEquals(3864, loadData(connection, name).length);
  }

  @org.junit.Test
  @Order(1)
  public void testCreateSql(final Connection connection) throws IOException, SAXException, SQLException {
    createSql(connection, name);
  }
}