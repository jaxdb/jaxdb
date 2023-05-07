/* Copyright (c) 2023 JAX-DB
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
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.runner.DBTestRunner.Unsupported;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.runner.Vendor;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class OnSelectTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends OnSelectTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends OnSelectTest {
  }

  @Test
  @Spec(order = 0)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void setUp(@Schema(classicmodels.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.err.flush();
      System.exit(1);
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    DDLxTest.recreateSchema(transaction.getConnection(), "classicmodels");
    transaction.commit();

    final Connector connector = transaction.getConnector();
    final Database database = Database.global(transaction.getSchemaClass());
    database.configCache(connector, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }

      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), c -> c.with(classicmodels.getTables()));
  }

  @Test
  @Spec(order = 1)
  public void test(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    classicmodels.ProductLine.productLineToProductLine().select(null, null);
  }

  @AfterClass
  public static void recreateSchema(@Schema(classicmodels.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), "classicmodels");
  }
}