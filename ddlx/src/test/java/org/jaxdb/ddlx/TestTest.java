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

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.vendor.DBVendor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(DBTestRunner.class)
public abstract class TestTest {
  private static final Logger logger = LoggerFactory.getLogger(TestTest.class);

  @Ignore("Intended for hands-on dev")
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends TestTest {
  }

  @Ignore("Intended for hands-on dev")
  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends TestTest {
  }

  @BeforeClass
  public static void beforeClass1() {
    if (logger.isDebugEnabled()) logger.debug("before class1:");
  }

  @BeforeClass
  public static void beforeClass2() {
    if (logger.isDebugEnabled()) logger.debug("before class2:");
  }

  @Before
  public void before() {
    if (logger.isDebugEnabled()) logger.debug("before0:");
  }

  @Before
  public void before(final Connection connection) throws SQLException {
    if (logger.isDebugEnabled()) logger.debug("before: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Test
  public void test1() {
    if (logger.isDebugEnabled()) logger.debug("test1:");
  }

  @Test
  public void test2(final Connection connection) throws SQLException {
    if (logger.isDebugEnabled()) logger.debug("test2: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Ignore("Should be ignored")
  public void testIgnore(final Connection connection) {
    fail("Should have been ignored " + connection);
  }
}