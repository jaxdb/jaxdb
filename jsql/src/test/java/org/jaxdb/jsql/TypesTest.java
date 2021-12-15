/* Copyright (c) 2017 JAX-DB
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.GeneratorExecutionException;
import org.junit.Test;
import org.libj.jci.CompilationException;
import org.xml.sax.SAXException;

public class TypesTest extends JSqlTest {
  @Test
  public void test() throws CompilationException, GeneratorExecutionException, IOException, SAXException, TransformerException {
    createEntities("types");
  }
}