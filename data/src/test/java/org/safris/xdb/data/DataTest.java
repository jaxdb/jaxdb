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

package org.safris.xdb.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.junit.runner.RunWith;
import org.safris.commons.lang.Resources;
import org.safris.commons.util.Collections;
import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.schema.Schemas;
import org.safris.xdb.schema.runner.Derby;
import org.safris.xdb.schema.runner.MySQL;
import org.safris.xdb.schema.runner.PostgreSQL;
import org.safris.xdb.schema.runner.VendorRunner;
import org.safris.xdb.xdd.xe.$xdd_data;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.generator.Generator;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

@RunWith(VendorRunner.class)
@VendorRunner.Test(Derby.class)
@VendorRunner.Integration({MySQL.class, PostgreSQL.class})
public abstract class DataTest {
  private static final File sourcesDestDir = new File("target/generated-test-sources/xsb");
  protected static final File resourcesDestDir = new File("target/generated-test-resources/xdb");

  private static final Set<String> compiledDBs = new HashSet<String>();

  protected static void createSchemas(final String name) throws IOException, TransformerException {
    if (!compiledDBs.contains(name)) {
      final URL xds = Resources.getResource(name + ".xds").getURL();
      final File destFile = new File(resourcesDestDir, name + ".xsd");
      Datas.createXSD(xds, destFile);

      final Set<NamespaceURI> excludes = Collections.asCollection(HashSet.class, NamespaceURI.getInstance("http://xdb.safris.org/xdd.xsd"), NamespaceURI.getInstance("http://commons.safris.org/xml/datatypes.xsd"));
      final GeneratorContext generatorContext = new GeneratorContext(sourcesDestDir, true, true, false, null, excludes);
      new Generator(generatorContext, java.util.Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), null).generate();
      compiledDBs.add(name);
    }
  }

  protected static void testData(final Connection connection, final String name) throws IOException, SQLException, XMLException {
    final xds_schema schema;
    try (final InputStream in = Resources.getResource(name + ".xds").getURL().openStream()) {
      schema = (xds_schema)Bindings.parse(new InputSource(in));
    }
    Schemas.truncate(connection, Schemas.tables(schema));

    final URL xdd = Resources.getResource(name + ".xdd").getURL();
    final $xdd_data data;
    try (final InputStream in = xdd.openStream()) {
      data = ($xdd_data)Bindings.parse(new InputSource(in));
    }

    Datas.loadData(connection, data);
  }
}