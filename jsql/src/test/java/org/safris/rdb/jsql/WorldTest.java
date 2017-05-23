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

package org.safris.rdb.jsql;

import static org.safris.rdb.jsql.DML.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lib4j.test.MixedTest;
import org.libx4j.xsb.runtime.Bindings;
import org.safris.commons.lang.Resources;
import org.safris.commons.xml.XMLException;
import org.safris.rdb.ddlx.Schemas;
import org.safris.rdb.ddlx.xe.ddlx_schema;
import org.safris.rdb.ddlx.runner.Derby;
import org.safris.rdb.ddlx.runner.MySQL;
import org.safris.rdb.ddlx.runner.Oracle;
import org.safris.rdb.ddlx.runner.PostgreSQL;
import org.safris.rdb.ddlx.runner.SQLite;
import org.safris.rdb.ddlx.runner.VendorRunner;
import org.safris.rdb.dmlx.xe.$dmlx_data;
import org.xml.sax.InputSource;

@RunWith(VendorRunner.class)
@VendorRunner.Test({Derby.class, SQLite.class})
@VendorRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class WorldTest extends JSQLTest {
  @BeforeClass
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public static void create() throws IOException, XMLException {
    createEntities("world");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testEntities(final Connection connection) throws ClassNotFoundException, IOException, SQLException, XMLException {
    DBRegistry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + ".world"), new DBConnector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL dmlx = Resources.getResource("world.dmlx").getURL();
    final $dmlx_data data;
    try (final InputStream in = dmlx.openStream()) {
      data = ($dmlx_data)Bindings.parse(new InputSource(in));
    }

    final ddlx_schema schema;
    try (final InputStream in = Resources.getResource("world.ddlx").getURL().openStream()) {
      schema = (ddlx_schema)Bindings.parse(new InputSource(in));
    }
    Schemas.truncate(connection, Schemas.tables(schema));
    INSERT(data).execute();
  }
}