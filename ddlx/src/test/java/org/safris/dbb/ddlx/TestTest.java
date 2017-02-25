package org.safris.dbb.ddlx;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.safris.dbb.ddlx.runner.Derby;
import org.safris.dbb.ddlx.runner.MySQL;
import org.safris.dbb.ddlx.runner.PostgreSQL;
import org.safris.dbb.ddlx.runner.VendorRunner;
import org.safris.dbb.vendor.DBVendor;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, MySQL.class, PostgreSQL.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class})
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
}