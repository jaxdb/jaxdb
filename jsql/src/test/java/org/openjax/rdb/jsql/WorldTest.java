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

package org.openjax.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.transform.TransformerException;

import org.fastjax.jci.CompilationException;
import org.fastjax.xml.ValidationException;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.DDLxTest;
import org.openjax.rdb.ddlx.GeneratorExecutionException;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.rdb.sqlx.SQLxTest;

public abstract class WorldTest extends JSQLTest {
  private static final String name = "world";

  public static class Test extends JSQLTest {
    @org.junit.Test
    public void testCreate() throws CompilationException, IOException, JAXBException, TransformerException, ValidationException {
      // Keep this order! Otherwise, #createEntities() will fail due to ClassCastException
      // caused by collision of different binding builds for ddlx, sqlx, jsql schemas
      createEntities(name);
      SQLxTest.createXSDs("world");
    }
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends WorldTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends WorldTest {
  }

  @org.junit.Test
  public void testReloadJaxb(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SQLException, UnmarshalException, ValidationException {
    DDLxTest.recreateSchema(connection, name);
    JSQLTest.loadEntitiesJaxb(connection, name);
  }

  @org.junit.Test
  public void testReloadXsb(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SQLException, ValidationException {
    DDLxTest.recreateSchema(connection, name);
    JSQLTest.loadEntitiesXsb(connection, name);
  }
}