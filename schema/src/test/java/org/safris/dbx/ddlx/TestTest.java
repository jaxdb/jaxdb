package org.safris.dbx.ddlx;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.safris.dbx.ddlx.DBVendor;
import org.safris.dbx.ddlx.runner.Derby;
import org.safris.dbx.ddlx.runner.MySQL;
import org.safris.dbx.ddlx.runner.PostgreSQL;
import org.safris.dbx.ddlx.runner.VendorRunner;

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
    System.out.println("before: " + DBVendor.parse(connection.getMetaData()));
  }

  @Test
  public void test1() {
    System.out.println("test1:");
  }

  @Test
  public void test2(final Connection connection) throws SQLException {
    System.out.println("test2: " + DBVendor.parse(connection.getMetaData()));
  }
}