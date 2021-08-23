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
import org.jaxdb.sqlx.SQL;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.sqlx_0_5.xLygluGCXAA.$Database;
import org.jaxsb.runtime.Bindings;
import org.xml.sax.SAXException;

@Mojo(name="sqlx", defaultPhase=LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution=ResolutionScope.TEST)
@Execute(goal="sqlx")
public class SQLxMojo extends SqlMojo<SQLxProduce,$Database> {
  private static final HashMap<URL,Reserve<$Database>> schemaToReserve = new HashMap<>();

  @Override
  HashMap<URL,Reserve<$Database>> schemaToReserve() {
    return schemaToReserve;
  }

  @Override
  Reserve<$Database> newReserve(final URL schema) throws IOException, SAXException {
    return new Reserve<>(($Database)Bindings.parse(schema));
  }

  @Override
  void makeSql(final Reserve<? extends $Database> reserve, final DBVendor dbVendor, final File sqlFile) throws IOException {
    getLog().info("Writing SQL to file: " + sqlFile);
    SQL.sqlx2sql(dbVendor, reserve.obj, sqlFile);
  }

  @Override
  void loadSql(final Connection connection, final $Database reserve) throws IOException, SQLException {
    getLog().info("Loading SQL in DB: " + DBVendor.valueOf(connection.getMetaData()));
    SQL.INSERT(connection, reserve);
  }

  @Override
  SQLxProduce[] values() {
    return SQLxProduce.values;
  }

  @Override
  void execute(final Configuration configuration) throws Exception {
    executeStaged(configuration);
  }
}