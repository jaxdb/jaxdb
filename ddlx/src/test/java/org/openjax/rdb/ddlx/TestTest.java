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

import org.fastjax.test.MixedTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.rdb.vendor.DBVendor;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, MySQL.class, PostgreSQL.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class})
@Category(MixedTest.class)
@Ignore
public class TestTest {
  @BeforeClass
  public static void beforeClass1() {
    System.out.println("before class1:");
  }

  @BeforeClass
  public static void beforeClass2() {
    System.out.println("before class2:");
  }

  @Before
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public void before() {
    System.out.println("before0:");
  }

  @Before
  public void before(final Connection connection) throws SQLException {
    System.out.println("before: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Test
  public void test1() {
    System.out.println("test1:");
  }

  @Test
  public void test2(final Connection connection) throws SQLException {
    System.out.println("test2: " + DBVendor.valueOf(connection.getMetaData()));
  }

  @Ignore
  @SuppressWarnings("unused")
  public void testIgnore(final Connection connection) {
    fail("Should have been ignored");
  }
}