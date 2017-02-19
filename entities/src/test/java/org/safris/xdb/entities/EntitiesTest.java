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

package org.safris.xdb.entities;

import static org.safris.xdb.entities.DML.INSERT;

import java.io.File;
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
import org.safris.xdb.entities.generator.Generator;
import org.safris.xdb.schema.Schemas;
import org.safris.xdb.schema.VendorClassRunner;
import org.safris.xdb.schema.VendorIntegration;
import org.safris.xdb.schema.VendorTest;
import org.safris.xdb.schema.vendor.Derby;
import org.safris.xdb.schema.vendor.MySQL;
import org.safris.xdb.schema.vendor.PostgreSQL;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

@RunWith(VendorClassRunner.class)
@VendorTest(Derby.class)
@VendorIntegration({MySQL.class, PostgreSQL.class})
public class EntitiesTest {
  private static void createEntities(final String name) throws IOException, XMLException {
    final URL xds = Resources.getResource(name + ".xds").getURL();
    final File destDir = new File("target/generated-test-sources/xdb");
    Generator.generate(xds, destDir, true);
  }

  @BeforeClass
  public static void create() throws IOException, XMLException {
    createEntities("classicmodels");
    createEntities("world");
    createEntities("types");
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

    final URL xdd = Resources.getResource("world.xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    final xds_schema schema;
    try (final InputStream in = Resources.getResource("world.xds").getURL().openStream()) {
      schema = (xds_schema)Bindings.parse(new InputSource(in));
    }
    Schemas.truncate(connection, Schemas.tables(schema));
    INSERT(data).execute();
  }
}