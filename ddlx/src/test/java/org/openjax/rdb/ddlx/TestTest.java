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

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestTest {
  private static final Logger logger = LoggerFactory.getLogger(TestTest.class);

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends TestTest {
  }

  @RunWith(VendorRunner.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
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