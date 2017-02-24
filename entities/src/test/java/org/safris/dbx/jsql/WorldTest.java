/* Copyright (c) 2017 Seva Safris
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

package org.safris.dbx.jsql;

import static org.safris.dbx.jsql.DML.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.safris.commons.lang.Resources;
import org.safris.commons.xml.XMLException;
import org.safris.dbx.ddlx.Schemas;
import org.safris.dbx.ddlx.runner.Derby;
import org.safris.dbx.ddlx.runner.MySQL;
import org.safris.dbx.ddlx.runner.PostgreSQL;
import org.safris.dbx.ddlx.runner.VendorRunner;
import org.safris.dbx.ddlx.xe.ddlx_schema;
import org.safris.dbx.dmlx.xe.$dmlx_data;
import org.safris.dbx.jsql.Entities;
import org.safris.dbx.jsql.EntityDataSource;
import org.safris.dbx.jsql.EntityRegistry;
import org.safris.dbx.jsql.Schema;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

@RunWith(VendorRunner.class)
@VendorRunner.Test(Derby.class)
@VendorRunner.Integration({MySQL.class, PostgreSQL.class})
public class WorldTest extends EntitiesTest {
  @BeforeClass
  @VendorRunner.RunIn(VendorRunner.Test.class)
  public static void create() throws IOException, XMLException {
    createEntities("world");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testEntities(final Connection connection) throws ClassNotFoundException, IOException, SQLException, XMLException {
    EntityRegistry.register((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + ".world"), PreparedStatement.class, new EntityDataSource() {
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