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
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.logging.Level;

import javax.xml.transform.TransformerException;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.safris.commons.io.Files;
import org.safris.commons.jci.CompilationException;
import org.safris.commons.jci.JavaCompiler;
import org.safris.commons.lang.Resources;
import org.safris.commons.logging.Logging;
import org.safris.commons.sql.ConnectionProxy;
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
  private static final FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(final File pathname) {
      return pathname.getName().endsWith(".java");
    }
  };

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void generateBindings() throws GeneratorExecutionException, CompilationException, IOException, SQLException, TransformerException, XMLException, ClassNotFoundException {
    Logging.setLevel(Level.FINER);
    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final File destFile = new File("target/generated-test-resources/xdb/classicmodels.xsd");
    Transformer.xdsToXsd(xds, destFile);

    final File xsbDestDir = new File("target/generated-test-sources/xsb");
    final GeneratorContext generatorContext = new GeneratorContext(System.currentTimeMillis(), xsbDestDir, true, true);
    new org.safris.xsb.generator.Generator(generatorContext, Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), null, null).generate();

    final File xdeDestDir = new File("target/generated-test-sources/xdb");
    org.safris.xdb.entities.generator.Generator.generate(xds, xdeDestDir);

    final JavaCompiler compiler = new JavaCompiler(new File("target/test-classes"));
    compiler.compile(Files.listAll(xdeDestDir, fileFilter));

    new EmbeddedDriver();
    final EntityDataSource entityDataSource = new EntityDataSource() {
      @Override
      public Connection getConnection() throws SQLException {
        return new ConnectionProxy(DriverManager.getConnection("jdbc:derby:target/test-db;create=true"));
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

  @AfterClass
  public static void after() throws IOException, SQLException {
    DriverManager.getConnection("jdbc:derby:target/test-db;shutdown=true");
    Files.deleteAllOnExit(new File("target/test-db").toPath());
  }
}