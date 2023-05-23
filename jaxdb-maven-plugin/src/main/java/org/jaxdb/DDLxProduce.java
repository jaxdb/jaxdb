/* Copyright (c) 2019 JAX-DB
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
import java.util.LinkedHashSet;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.generator.Generator;
import org.libj.net.URLs;
import org.libj.util.StringPaths;
import org.xml.sax.SAXException;

abstract class DDLxProduce extends Produce<JaxDbMojo<DDLxProduce>.Configuration,DDLxProduce,DDLx> {
  private static int index;
  static final DDLxProduce[] values = new DDLxProduce[5];

  private DDLxProduce(final String name) {
    super(name, values, index++);
  }

  static final DDLxProduce JSQL = new DDLxProduce("jsql") {
    @Override
    void execute(final JaxDbMojo<DDLxProduce>.Configuration configuration, final SqlMojo<DDLxProduce,DDLx> sqlMojo) throws GeneratorExecutionException, IOException, SAXException, TransformerException {
      final LinkedHashSet<URL> schemas = configuration.getSchemas();
      if (schemas.size() > 0) {
        for (final URL schema : schemas) { // [S]
          final Reserve<DDLx> reserve = sqlMojo.getReserve(schema);
          Generator.generate(reserve.obj, StringPaths.getSimpleName(reserve.get(schema, sqlMojo.rename)), configuration.getDestDir());
        }
      }
    }
  };

  static final DDLxProduce SQL = new DDLxProduce("sql") {
    @Override
    void execute(final JaxDbMojo<DDLxProduce>.Configuration configuration, final SqlMojo<DDLxProduce,DDLx> sqlMojo) throws Exception {
      sqlMojo.executeStaged(configuration);
    }
  };

  static final DDLxProduce SQL_XSD = new DDLxProduce("sqlxsd") {
    @Override
    void execute(final JaxDbMojo<DDLxProduce>.Configuration configuration, final SqlMojo<DDLxProduce,DDLx> sqlMojo) throws GeneratorExecutionException, IOException, TransformerException {
      final LinkedHashSet<URL> schemas = configuration.getSchemas();
      if (schemas.size() > 0)
        for (final URL schema : schemas) // [S]
          org.jaxdb.sqlx.SQL.ddlx2xsd(schema, new File(configuration.getDestDir(), JaxDbMojo.EXTENSION_PATTERN.matcher(URLs.getName(schema)).replaceAll(".xsd")));
    }
  };

  static final DDLxProduce JAXSB = new DDLxProduce("jaxsb") {
    @Override
    void execute(final JaxDbMojo<DDLxProduce>.Configuration configuration, final SqlMojo<DDLxProduce,DDLx> sqlMojo) throws Exception {
      SQL_XSD.execute(configuration, sqlMojo);
      SqlXsdProduce.JAXSB.execute(new SqlXsdMojo().configure(configuration), null);
    }
  };
}