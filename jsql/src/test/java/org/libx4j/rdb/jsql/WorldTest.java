/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.MixedTest;
import org.lib4j.xml.XMLException;
import org.libx4j.rdb.ddlx.DDLxTest;
import org.libx4j.rdb.ddlx.GeneratorExecutionException;
import org.libx4j.rdb.ddlx.runner.Derby;
import org.libx4j.rdb.ddlx.runner.MySQL;
import org.libx4j.rdb.ddlx.runner.Oracle;
import org.libx4j.rdb.ddlx.runner.PostgreSQL;
import org.libx4j.rdb.ddlx.runner.SQLite;
import org.libx4j.rdb.ddlx.runner.VendorRunner;
import org.libx4j.rdb.dmlx.DMLxTest;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, SQLite.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class WorldTest extends JSQLTest {
  private static final String name = "world";

  @BeforeClass
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public static void create() throws IOException, XMLException {
    createEntities(name);
  }

  @Test
  public void test(final Connection connection) throws ClassNotFoundException, GeneratorExecutionException, IOException, SQLException, TransformerException, XMLException {
    DDLxTest.recreateSchema(connection, name);
    DMLxTest.createSchemas(name);
    JSQLTest.loadEntities(connection, name);
  }
}