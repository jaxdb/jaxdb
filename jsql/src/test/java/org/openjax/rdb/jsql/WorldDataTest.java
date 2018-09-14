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

import org.fastjax.test.MixedTest;
import org.fastjax.xml.ValidationException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.DDLxTest;
import org.openjax.rdb.ddlx.GeneratorExecutionException;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.xml.sax.SAXException;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, SQLite.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class WorldDataTest extends JSQLTest {
  private static final String name = "world";

  @Test
  public void testReloadJaxb(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SAXException, SQLException {
    DDLxTest.recreateSchema(connection, name);
    JSQLTest.loadEntitiesJaxb(connection, name);
  }

  @Test
  public void testReloadXsb(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SQLException, ValidationException {
    DDLxTest.recreateSchema(connection, name);
    JSQLTest.loadEntitiesXsb(connection, name);
  }
}