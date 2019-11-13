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

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.ddlx.Schemas;
import org.jaxdb.ddlx.StatementBatch;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.Schema;
import org.jaxsb.runtime.Bindings;
import org.xml.sax.SAXException;

@Mojo(name="ddlx", defaultPhase=LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution=ResolutionScope.TEST)
@Execute(goal="ddlx")
public class DDLxMojo extends SqlMojo<DDLxProduce,Schema> {
  private static final HashMap<URL,Reserve<Schema>> schemaToReserve = new HashMap<>();

  @Override
  HashMap<URL,Reserve<Schema>> schemaToReserve() {
    return schemaToReserve;
  }

  @Override
  Reserve<Schema> newReserve(final URL schema) throws IOException, SAXException {
    return new Reserve<>((Schema)Bindings.parse(schema));
  }

  @Override
  void makeSql(final Reserve<Schema> reserve, final DBVendor dbVendor, final File sqlFile) throws GeneratorExecutionException, IOException {
    final StatementBatch statementBatch = org.jaxdb.ddlx.Generator.createDDL(reserve.obj, dbVendor);
    statementBatch.writeOutput(sqlFile);
  }

  @Override
  void loadSql(final Connection connection, final Schema reserve) throws GeneratorExecutionException, SQLException {
    Schemas.recreate(connection, reserve);
  }

  @Override
  DDLxProduce[] values() {
    return DDLxProduce.values;
  }

  @Override
  final void execute(final Configuration configuration) throws Exception {
    for (final DDLxProduce produce : configuration.getProduce()) {
      produce.execute(configuration, this);
    }
  }
}