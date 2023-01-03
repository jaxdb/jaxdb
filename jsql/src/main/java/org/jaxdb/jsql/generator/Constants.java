package org.jaxdb.jsql.generator;

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Documented;
import org.libj.lang.Strings;
import org.w3.www._2001.XMLSchema;

class Constants {
  static final Object THIS = new Object();
  static final Object MUTABLE = new Object();
  static final Object PRIMARY_KEY = new Object();
  static final Object KEY_FOR_UPDATE = new Object();
  static final Object COMMIT_UPDATE = new Object();

  static String getDoc(final $Documented documented, final int depth, final char start, final char end) {
    final XMLSchema.yAA.$String documentation = documented.getDocumentation();
    if (documentation == null)
      return "";

    final String doc = documentation.text().trim();
    if (doc.length() == 0)
      return "";

    final String indent = Strings.repeat(" ", depth * 2);
    final StringBuilder out = new StringBuilder();
    if (start != '\0')
      out.append(start);

    out.append(indent).append("/** ").append(doc).append(" */");
    if (end != '\0')
      out.append(end);

    return out.toString();
  }
}