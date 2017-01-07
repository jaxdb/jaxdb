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
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;

import javax.xml.transform.TransformerException;

import org.junit.AfterClass;
import org.junit.Test;
import org.safris.commons.lang.Resources;
import org.safris.commons.logging.Logging;
import org.safris.commons.util.Collections;
import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.XMLException;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.generator.Generator;

@SuppressWarnings("unused")
public class DataTest {
  static {
    Logging.setLevel(Level.FINE);
  }

  @Test
  public void testData() throws IOException, SQLException, TransformerException, XMLException {
    final URL xds = Resources.getResource("classicmodels.xds").getURL();
    final File destFile = new File("target/generated-test-resources/xdb/classicmodels.xsd");
    Transformer.xdsToXsd(xds, destFile);

    final GeneratorContext generatorContext = new GeneratorContext(new File("target/generated-test-sources/xsb"), true, true);
    new Generator(generatorContext, java.util.Collections.singleton(new SchemaReference(destFile.toURI().toURL(), false)), Collections.asCollection(HashSet.class, NamespaceURI.getInstance("http://xdb.safris.org/xdd.xsd"), NamespaceURI.getInstance("http://commons.safris.org/xml/datatypes.xsd")), null).generate();
  }

  @AfterClass
  public static void destroy() {
    new File("derby.log").deleteOnExit();
  }
}