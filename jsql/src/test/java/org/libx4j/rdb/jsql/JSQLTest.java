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

import static org.libx4j.rdb.jsql.DML.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.lib4j.lang.Resources;
import org.lib4j.xml.XMLException;
import org.libx4j.rdb.ddlx.Schemas;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.dmlx.xe.$dmlx_data;
import org.libx4j.rdb.jsql.generator.Generator;
import org.libx4j.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public abstract class JSQLTest {
  protected static void createEntities(final String name) throws IOException, XMLException {
    final URL url = Resources.getResource(name + ".ddlx").getURL();
    final File destDir = new File("target/generated-test-sources/rdb");
    new Generator(url).generate(destDir, true);
  }

  @SuppressWarnings("unchecked")
  protected static void loadEntities(final Connection connection, final String name) throws ClassNotFoundException, IOException, SQLException, XMLException {
    Registry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name), new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL dmlx = Resources.getResource(name + ".dmlx").getURL();
    final $dmlx_data data;
    try (final InputStream in = dmlx.openStream()) {
      data = ($dmlx_data)Bindings.parse(new InputSource(in));
    }

    final ddlx_schema schema;
    try (final InputStream in = Resources.getResource(name + ".ddlx").getURL().openStream()) {
      schema = (ddlx_schema)Bindings.parse(new InputSource(in));
    }
    Schemas.truncate(connection, Schemas.tables(schema));
    INSERT(data).execute();
  }
}