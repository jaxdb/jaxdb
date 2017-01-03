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

package org.safris.xdb.test;

import static org.safris.xdb.entities.DML.INSERT;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

import javax.xml.transform.TransformerException;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.lang.Resources;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.data.Transformer;
import org.safris.xdb.entities.EntityDataSource;
import org.safris.xdb.entities.EntityRegistry;
import org.safris.xdb.entities.Schema;
import org.safris.xdb.schema.DDL;
import org.safris.xdb.schema.GeneratorExecutionException;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public class RegressionTest {
  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void generateBindings() throws GeneratorExecutionException, IOException, SQLException, TransformerException, XMLException, ClassNotFoundException {
    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final File destFile = new File("target/generated-test-resources/xdb/classicmodels.xsd");
    Transformer.xdsToXsd(xds, destFile);

    final GeneratorContext generatorContext = new GeneratorContext(System.currentTimeMillis(), new File("target/generated-test-sources/xsb"), true, true);
    new org.safris.xsb.generator.Generator(generatorContext, Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), null, null).generate();

    org.safris.xdb.entities.generator.Generator.generate(xds, new File("target/generated-test-sources/xdb"));

    new EmbeddedDriver();
    final EntityDataSource entityDataSource = new EntityDataSource() {
      @Override
      public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:xdb-test;create=true");
      }
    };

    EntityRegistry.register((Class<? extends Schema>)Class.forName("xdb.ddl.classicmodels"), PreparedStatement.class, entityDataSource);

    final xds_schema schema;
    try (final InputStream in = xds.openStream()) {
      schema = (xds_schema)Bindings.parse(new InputSource(in));
    }

    try (final Connection connection = entityDataSource.getConnection()) {
      DDL.create(schema, connection);
    }
  }

  @Test
  @SuppressWarnings("unused")
  public void testDerby() throws GeneratorExecutionException, IOException, SQLException, XMLException {
    final URL xdd = Resources.getResource("classicmodels.xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    INSERT(data).execute();
  }
}