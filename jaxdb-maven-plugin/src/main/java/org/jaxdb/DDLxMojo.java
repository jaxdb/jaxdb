/* Copyright (c) 2018 JAX-DB
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

package org.jaxdb;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.xml.transform.TransformerException;

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.Generator;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.vendor.DbVendor;
import org.xml.sax.SAXException;

@Mojo(name = "ddlx", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.TEST)
@Execute(goal = "ddlx")
public class DDLxMojo extends SqlMojo<DDLxProduce,DDLx> {
  private static final HashMap<URL,Reserve<DDLx>> schemaToReserve = new HashMap<>();

  @Override
  HashMap<URL,Reserve<DDLx>> schemaToReserve() {
    return schemaToReserve;
  }

  @Override
  Reserve<DDLx> newReserve(final URL schema) throws IOException, SAXException, TransformerException {
    return new Reserve<>(new DDLx(schema));
  }

  @Override
  void makeSql(final Reserve<? extends DDLx> reserve, final DbVendor dbVendor, final File sqlFile) throws GeneratorExecutionException, IOException {
    getLog().info("Writing DDL to file: " + sqlFile);
    Generator.createDDL(reserve.obj, dbVendor).writeOutput(sqlFile);
  }

  @Override
  void loadSql(final Connection connection, final DDLx reserve) throws GeneratorExecutionException, SQLException {
    getLog().info("Loading DDL in DB: " + DbVendor.valueOf(connection.getMetaData()));
    reserve.recreate(connection);
  }

  @Override
  DDLxProduce[] values() {
    return DDLxProduce.values;
  }

  @Override
  final void execute(final Configuration configuration) throws Exception {
    for (final DDLxProduce produce : configuration.getProduce()) // [A]
      produce.execute(configuration, this);
  }
}