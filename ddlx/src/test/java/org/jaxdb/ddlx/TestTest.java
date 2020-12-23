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

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.ddlx.runner.VendorRunner;
import org.jaxdb.vendor.DBVendor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(VendorRunner.class)
public abstract class TestTest {
  private static final Logger logger = LoggerFactory.getLogger(TestTest.class);

  @Ignore("Intended for hands-on dev")
  @VendorRunner.Vendor(value=Derby.class, parallel=2)
  @VendorRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends TestTest {
  }

  @Ignore("Intended for hands-on dev")
  @VendorRunner.Vendor(MySQL.class)
  @VendorRunner.Vendor(PostgreSQL.class)
  @VendorRunner.Vendor(Oracle.class)
  public static class RegressionTest extends TestTest {
  }

  @BeforeClass
  public static void beforeClass1() {
    logger.debug("before class1:");
  }

  @BeforeClass
  public static void beforeClass2() {
    logger.debug("before class2:");
  }

  @Before
  public void before() {
    logger.debug("before0:");
  }

  @Before
  public void before(final Connection connection) throws SQLException {
    logger.debug("before: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Test
  public void test1() {
    logger.debug("test1:");
  }

  @Test
  public void test2(final Connection connection) throws SQLException {
    logger.debug("test2: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Ignore
  public void testIgnore(final Connection connection) {
    fail("Should have been ignored " + connection);
  }
}