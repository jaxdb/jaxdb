/* Copyright (c) 2017 JAX-DB
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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;
import static org.libj.util.function.Throwing.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.ddlx.Schemas;
import org.jaxdb.jsql.statement.Modification.Result;
import org.jaxdb.jsql.generator.Generator;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxsb.runtime.Bindings;
import org.libj.jci.CompilationException;
import org.libj.jci.InMemoryCompiler;
import org.xml.sax.SAXException;

public abstract class JSqlTest {
  static void createEntities(final String name) throws CompilationException, GeneratorExecutionException, IOException, SAXException, TransformerException {
    final URL url = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(name + ".ddlx"));
    final File destDir = new File("target/generated-test-sources/jaxdb");
    Generator.generate(url, name, destDir);
    final InMemoryCompiler compiler = new InMemoryCompiler();
    Files.walk(destDir.toPath())
      .filter(p -> p.getFileName().toString().endsWith(".java"))
      .forEach(rethrow((Path p) -> compiler.addSource(new String(Files.readAllBytes(p)))));

    compiler.compile(destDir, "-g");
  }

  @SuppressWarnings("unchecked")
  static Result loadEntitiesJaxSB(final Connection connection, final String name) throws ClassNotFoundException, IOException, SAXException, SQLException, TransformerException {
    Database.threadLocal((Class<? extends Schema>)Class.forName(Entities.class.getPackage().getName() + "." + name)).connectPrepared((final Transaction.Isolation isolation) -> connection);

    final URL sqlx = ClassLoader.getSystemClassLoader().getResource("jaxdb/" + name + ".sqlx");
    assertNotNull(sqlx);
    final $Database database = ($Database)Bindings.parse(sqlx);

    final DDLx ddlx = new DDLx(ClassLoader.getSystemClassLoader().getResource(name + ".ddlx"));

    Schemas.truncate(connection, ddlx.getMergedSchema().getTable());
    final Batch batch = new Batch();
    final int expectedCount = DbVendor.valueOf(connection.getMetaData()) == DbVendor.ORACLE ? 0 : 1;
    for (final data.Table table : Entities.toEntities(database)) // [A]
      batch.addStatement(
        INSERT(table)
          .onExecute(c -> assertEquals(expectedCount, c)));

    return batch.execute();
  }
}