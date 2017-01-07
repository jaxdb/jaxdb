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

package org.safris.xdb.schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.io.Files;
import org.safris.commons.lang.Resources;
import org.safris.commons.logging.Logging;
import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.xml.validate.ValidationException;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.runtime.Bindings;
import org.safris.xsb.runtime.ParseException;
import org.xml.sax.InputSource;

@SuppressWarnings("unused")
public class SchemaTest {
  static {
    Logging.setLevel(Level.FINE);
    new EmbeddedDriver();
  }

  private static final File db = new File("target/generated-test-resources/test-db");

  private static Connection connection;

  @BeforeClass
  public static void create() throws IOException, SQLException {
    if (db.exists() && !Files.deleteAll(db.toPath()))
      throw new IOException("Unable to delete " + db.getPath());

    connection = new ConnectionProxy(DriverManager.getConnection("jdbc:derby:" + db.getPath() + ";create=true"));
  }

  @Test
  public void testSchema() throws GeneratorExecutionException, IOException, ParseException, SQLException, ValidationException {
    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final xds_schema schema;
    try (final InputStream in = xds.openStream()) {
      schema = (xds_schema)Bindings.parse(new InputSource(in));
    }

    DDL.create(schema, connection);
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