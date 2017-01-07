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

import javax.xml.transform.TransformerException;

import org.safris.commons.lang.Resources;

public final class Transformer {
  public static void main(final String[] args) throws IOException, TransformerException {
    final File file = new File(args[0]);
    final File out = new File(args[1]);
    xdsToXsd(file.toURI().toURL(), out);
  }

  public static void xdsToXsd(final URL file, final File out) throws IOException, TransformerException {
    out.getParentFile().mkdirs();
    org.safris.commons.xml.transform.Transformer.transform(Resources.getResource("xdd.xsl").getURL(), file, out);
  }

  private Transformer() {
  }
}