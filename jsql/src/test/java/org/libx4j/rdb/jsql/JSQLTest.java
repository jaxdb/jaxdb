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

import org.lib4j.jci.CompilationException;
import org.lib4j.jci.JavaCompiler;
import org.fastjax.util.JavaIdentifiers;
import org.lib4j.xml.ValidationException;
import org.lib4j.xml.jaxb.JaxbUtil;
import org.libx4j.rdb.ddlx.Schemas;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc;
import org.libx4j.rdb.jsql.generator.Generator;
import org.libx4j.rdb.sqlx_0_9_9.Database;
import org.libx4j.rdb.sqlx_0_9_9.xLzgluGCXYYJc.$Database;
import org.libx4j.xsb.runtime.Bindings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class JSQLTest {
  protected static void createEntities(final String name) throws CompilationException, IOException, ValidationException {
    final URL url = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx");
    final File destDir = new File("target/generated-test-sources/rdb");
    new Generator(url).generate(name, destDir);
    new JavaCompiler(destDir).compile(destDir);
  }

  @SuppressWarnings("unchecked")
  protected static int[] loadEntitiesXsb(final Connection connection, final String name) throws ClassNotFoundException, IOException, SQLException, ValidationException {
    Registry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name), new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL sqlx = Thread.currentThread().getContextClassLoader().getResource(name + ".sqlx");
    final $Database database = ($Database)Bindings.parse(sqlx);

    final xLzgluGCXYYJc.Schema schema;
    try (final InputStream in = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx").openStream()) {
      schema = (xLzgluGCXYYJc.Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.flatten(schema);
    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final Batch batch = new Batch();
    for (final type.Entity entity : Entities.toEntities(database))
      batch.addStatement(INSERT(entity));

    return batch.execute();
  }

  @SuppressWarnings("unchecked")
  protected static int[] loadEntitiesJaxb(final Connection connection, final String name) throws ClassNotFoundException, IOException, SAXException, SQLException {
    Registry.registerPrepared((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name), new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return connection;
      }
    });

    final URL sqlx = Thread.currentThread().getContextClassLoader().getResource(name + ".sqlx");
    final Database database;
    try (final InputStream in = sqlx.openStream()) {
      database = (Database)JaxbUtil.parse(Class.forName(name + ".sqlx." + JavaIdentifiers.toClassCase(name)), sqlx, false);
    }

    final xLzgluGCXYYJc.Schema schema;
    try (final InputStream in = Thread.currentThread().getContextClassLoader().getResource(name + ".ddlx").openStream()) {
      schema = (xLzgluGCXYYJc.Schema)Bindings.parse(new InputSource(in));
    }

    Schemas.flatten(schema);
    Schemas.truncate(connection, Schemas.flatten(schema).getTable());
    final Batch batch = new Batch();
    for (final type.Entity entity : Entities.toEntities(database))
      batch.addStatement(INSERT(entity));

    return batch.execute();
  }
}