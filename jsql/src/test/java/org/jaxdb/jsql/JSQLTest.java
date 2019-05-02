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

package org.jaxdb.jsql;

import static org.junit.Assert.*;
import static org.jaxdb.jsql.DML.*;
import static org.openjax.ext.util.function.Throwing.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.UnmarshalException;

import org.openjax.ext.jaxb.xjc.JaxbUtil;
import org.openjax.ext.jci.CompilationException;
import org.openjax.ext.jci.InMemoryCompiler;
import org.jaxdb.ddlx.Schemas;
import org.jaxdb.www.ddlx_0_3_9.xLygluGCXYYJc;
import org.jaxdb.jsql.generator.Generator;
import org.jaxdb.sqlx_0_3_9.Database;
import org.jaxdb.www.sqlx_0_3_9.xLygluGCXYYJc.$Database;
import org.openjax.ext.util.Identifiers;
import org.openjax.ext.xml.api.ValidationException;
import org.openjax.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public abstract class JSQLTest {
  protected static void createEntities(final String name) throws CompilationException, IOException, ValidationException {
    final URL url = ClassLoader.getSystemClassLoader().getResource(name + ".ddlx");
    assertNotNull(url);
    final File destDir = new File("target/generated-test-sources/jaxdb");
    new Generator(url).generate(name, destDir);
    final InMemoryCompiler compiler = new InMemoryCompiler();
    Files.walk(destDir.toPath())
      .map(p -> p.toFile())
      .filter(f -> f.getName().endsWith(".java"))
      .forEach(rethrow((File f) -> compiler.addSource(new String(Files.readAllBytes(f.toPath())))));

    compiler.compile(destDir, "-g");
  }

  @SuppressWarnings("unchecked")
  protected static int[] loadEntitiesXsb(final Connection connection, final String name) throws ClassNotFoundException, IOException, SQLException, ValidationException {
    Registry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name), new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    final $Database database = ($Database)Bindings.parse(sqlx);

    final xLygluGCXYYJc.Schema schema;
    try (final InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(name + ".ddlx")) {
      schema = (xLygluGCXYYJc.Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.flatten(schema);
    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final Batch batch = new Batch();
    for (final type.Entity entity : Entities.toEntities(database))
      batch.addStatement(INSERT(entity));

    return batch.execute();
  }

  @SuppressWarnings("unchecked")
  protected static int[] loadEntitiesJaxb(final Connection connection, final String name) throws ClassNotFoundException, IOException, SQLException, UnmarshalException, ValidationException {
    Registry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name), new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    final Database database;
    try (final InputStream in = sqlx.openStream()) {
      database = (Database)JaxbUtil.parse(Class.forName(name + ".sqlx." + Identifiers.toClassCase(name)), sqlx, false);
    }

    final xLygluGCXYYJc.Schema schema;
    try (final InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(name + ".ddlx")) {
      schema = (xLygluGCXYYJc.Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.flatten(schema);
    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final Batch batch = new Batch();
    for (final type.Entity entity : Entities.toEntities(database))
      batch.addStatement(INSERT(entity));

    return batch.execute();
  }
}