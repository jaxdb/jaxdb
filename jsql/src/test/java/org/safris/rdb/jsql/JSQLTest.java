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

package org.safris.rdb.jsql;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.safris.commons.lang.Resources;
import org.safris.commons.xml.XMLException;
import org.safris.rdb.jsql.generator.Generator;

public abstract class JSQLTest {
  protected static void createEntities(final String name) throws IOException, XMLException {
    final URL ddlx = Resources.getResource(name + ".ddlx").getURL();
    final File destDir = new File("target/generated-test-sources/rdb");
    Generator.generate(ddlx, destDir, true);
  }
}