/* Copyright (c) 2011 Seva Safris
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

package org.safris.xdb.xdl;

import java.util.List;

import org.safris.commons.lang.Strings;

public final class CodeGenUtil {
  public static String createField(final String type, final String name, final String def) {
    final String instanceName = Strings.toInstanceCase(name);
    final StringBuffer buffer = new StringBuffer();
    buffer.append("  private ").append(type).append(" ").append(instanceName);
    if (def != null)
      buffer.append(" = ").append(def);
    buffer.append(";\n\n");

    buffer.append("  public void set").append(Strings.toClassCase(name)).append("(final ").append(type).append(" ").append(instanceName).append(") {\n");
    buffer.append("    this.").append(instanceName).append(" = ").append(instanceName).append(";\n  }\n\n");
    buffer.append("  public ").append(type).append(" get").append(Strings.toClassCase(name)).append("() {\n");
    buffer.append("    return ").append(instanceName).append(";\n  }\n");
    return buffer.toString();
  }

  public static String createEquals(final boolean isAbstract, final String superClass, final String className, final List<String> fields) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("  public boolean equals(final ").append(Object.class.getName()).append(" obj) {\n");
    if (!isAbstract) {
      buffer.append("    if (obj == this)\n");
      buffer.append("      return true;\n\n");
    }

    if (superClass != null) {
      buffer.append("    if (!super.equals(obj))\n");
      buffer.append("      return false;\n\n");
    }
    buffer.append("    if (!(obj instanceof ").append(className).append("))\n");
    buffer.append("      return false;\n\n");

    if (fields.size() > 0) {
      buffer.append("    final ").append(className).append(" that = (").append(className).append(")obj;\n");
      for (final String field : fields) {
        buffer.append("    if (").append(field).append(" != null ? !").append(field).append(".equals(that.").append(field).append(") : that.").append(field).append(" != null)\n");
        buffer.append("      return false;\n\n");
      }
    }

    buffer.append("    return true;\n");
    buffer.append("  }\n");
    return buffer.toString();
  }

  public static String createHashCode(final String superClass, final List<String> fields) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("  public int hashCode() {\n");
    if (superClass != null)
      buffer.append("    int hashCode = super.hashCode();\n");
    else
      buffer.append("    int hashCode = 7;\n");

    for (final String field : fields)
      buffer.append("    hashCode += ").append(field).append(" != null ? ").append(field).append(".hashCode() : -1;\n");

    buffer.append("    return hashCode;\n");
    buffer.append("  }\n");
    return buffer.toString();
  }

  private CodeGenUtil() {
  }
}