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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.lang.Resources;
import org.safris.commons.logging.Logging;
import org.safris.commons.test.LoggableTest;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.data.DataTest;
import org.safris.xdb.entities.generator.Generator;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public class EntitiesTest extends LoggableTest {
  static {
    Logging.setLevel(Level.FINE);
  }

  @SuppressWarnings("unchecked")
  public static Connection createConnection(final String name) throws ClassNotFoundException, IOException, SQLException {
    final Connection connection = DataTest.createConnection();
    EntityRegistry.register((Class<? extends Schema>)Class.forName("xdb.ddl." + name), PreparedStatement.class, new EntityDataSource() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    return connection;
  }

  private static void createEntities(final String name) throws IOException, XMLException {
    final URL xds = Resources.getResource(name + ".xds").getURL();
    final File destDir = new File("target/generated-test-sources/xdb");
    Generator.generate(xds, destDir, true);
  }

  @BeforeClass
  public static void create() throws ClassNotFoundException, IOException, SQLException, XMLException {
    createEntities("classicmodels");
    createEntities("world");
    createConnection("world");
  }

  @Test
  public void testEntities() throws IOException, SQLException, XMLException {
    final URL xdd = Resources.getResource("world.xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    INSERT(data).execute();
  }

  @AfterClass
  public static void destroy() throws SQLException {
    new File("derby.log").deleteOnExit();
    try {
      DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }
    catch (final SQLException e) {
      if (!"XJ015".equals(e.getSQLState()))
        throw e;
    }
  }
}