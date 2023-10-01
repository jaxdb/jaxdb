/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql.generator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.EntityEnum;
import org.jaxdb.jsql.data;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.libj.lang.Identifiers;
import org.libj.lang.Strings;
import org.libj.net.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class Generator {
  private static final Logger logger = LoggerFactory.getLogger(Generator.class);
  private static final Map<Character,String> substitutions = Collections.singletonMap(' ', "_");
  static final Comparator<TableModel> tableModelComparator = (final TableModel o1, final TableModel o2) -> o1.table.getName$().text().compareTo(o2.table.getName$().text());
  static final Comparator<$Named> namedComparator = (o1, o2) -> o1.getName$().text().compareTo(o2.getName$().text());

  static String enumStringToEnum(final String value) {
    return Identifiers.toIdentifier(value, substitutions).toUpperCase().replace(' ', '_');
  }

  public static void main(final String[] args) throws GeneratorExecutionException, IOException, SAXException, TransformerException {
    generate(URLs.create(args[0]), args[1], new File(args[2]));
  }

  public static void generate(final URL url, final String name, final File destDir) throws GeneratorExecutionException, IOException, SAXException, TransformerException {
    new Generator(new DDLx(url), name, destDir).generate();
  }

  public static void generate(final DDLx ddlx, final String name, final File destDir) throws GeneratorExecutionException, IOException {
    new Generator(ddlx, name, destDir).generate();
  }

  private final DDLx ddlx;
  private final String name;
  private final File destDir;

  private final String packageName;
  private final SchemaModel schemaModel;

  private Generator(final DDLx ddlx, final String name, final File destDir) throws GeneratorExecutionException {
    this.ddlx = ddlx;
    this.name = name;
    this.destDir = destDir;

    this.packageName = data.class.getPackage().getName();
    this.schemaModel = new SchemaModel(packageName, name, ddlx.getNormalizedSchema().getTable());
  }

  private void generate() throws GeneratorExecutionException, IOException {
    if (logger.isInfoEnabled()) { logger.info("Generating jSQL: " + name); }

    final File dir = new File(destDir, packageName.replace('.', '/'));
    if (!dir.exists() && !dir.mkdirs())
      throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());

    final String code = schemaModel.generate(ddlx);
    final File javaFile = new File(dir, schemaModel.schemaClassSimpleName + ".java");
    Files.write(javaFile.toPath(), code.getBytes());
  }

  static String declareEnumClass(final String containerClassName, final $Enum templateOrColumn, final int spaces) {
    final String classSimpleName = Identifiers.toClassCase(templateOrColumn.getName$().text(), '$');
    final String className = containerClassName + "." + classSimpleName;
    final String[] names = Dialect.parseEnum(templateOrColumn.getValues$().text());
    final StringBuilder out = new StringBuilder();
    final String s = Strings.repeat(' ', spaces);
    out.append('\n').append(s).append('@').append(EntityEnum.Type.class.getCanonicalName()).append("(\"");
    Dialect.getTypeName(out, templateOrColumn, null).append("\")");
    out.append('\n').append(s).append("public static class ").append(classSimpleName).append(" implements ").append(EntityEnum.class.getName()).append(" {");
    out.append('\n').append(s).append("  private static byte index = 0;");
    out.append('\n').append(s).append("  public static final ").append(String.class.getName());
    for (int i = 0, i$ = names.length; i < i$; ++i) { // [RA]
      final String name = names[i];
      out.append(" $").append(enumStringToEnum(name)).append(" = \"").append(name).append("\",");
    }

    out.setCharAt(out.length() - 1, ';');
    out.append('\n').append(s).append("  public static final ").append(className);
    for (int i = 0, i$ = names.length; i < i$; ++i) // [RA]
      out.append(' ').append(enumStringToEnum(names[i])).append(',');
    out.setCharAt(out.length() - 1, ';');

    out.append('\n').append(s).append("  private static final ").append(String.class.getName()).append("[] strings = {");
    for (int i = 0, i$ = names.length; i < i$; ++i) // [RA]
      out.append('$').append(enumStringToEnum(names[i])).append(", ");
    out.setCharAt(out.length() - 2, '}');
    out.setCharAt(out.length() - 1, ';');

    out.append('\n').append(s).append("  private static final ").append(className).append("[] values = {");
    for (int i = 0, i$ = names.length; i < i$; ++i) { // [RA]
      final String name = names[i];
      final String declare = enumStringToEnum(name);
      out.append(declare).append(" = new ").append(className).append("($").append(declare).append("), ");
    }
    out.setCharAt(out.length() - 2, '}');
    out.setCharAt(out.length() - 1, ';');

    out.append("\n\n").append(s).append("  public static ").append(String.class.getName()).append("[] strings() {");
    out.append('\n').append(s).append("    return strings;");
    out.append('\n').append(s).append("  }\n");
    out.append("\n").append(s).append("  public static ").append(className).append("[] values() {");
    out.append('\n').append(s).append("    return values;");
    out.append('\n').append(s).append("  }\n");
    out.append('\n').append(s).append("  public static ").append(className).append(" valueOf(final ").append(CharSequence.class.getName()).append(" string) {");
    out.append('\n').append(s).append("    if (string == null)");
    out.append('\n').append(s).append("      return null;\n");
    out.append('\n').append(s).append("    for (final ").append(className).append(" value : values) // [A]"); // FIXME: Implement binary search here
    out.append('\n').append(s).append("      if (").append(Strings.class.getName()).append(".equals(string, value.name))");
    out.append('\n').append(s).append("        return value;\n");
    out.append('\n').append(s).append("    return null;");
    out.append('\n').append(s).append("  }\n");
    out.append('\n').append(s).append("  private final byte ordinal;");
    out.append('\n').append(s).append("  private final ").append(String.class.getName()).append(" name;\n");
    out.append('\n').append(s).append("  protected ").append(classSimpleName).append("(final ").append(String.class.getName()).append(" name) {");
    out.append('\n').append(s).append("    this.ordinal = index++;");
    out.append('\n').append(s).append("    this.name = name;");
    out.append('\n').append(s).append("  }\n");
    out.append('\n').append(s).append("  @").append(Override.class.getName());
    out.append('\n').append(s).append("  public byte ordinal() {");
    out.append('\n').append(s).append("    return ordinal;");
    out.append('\n').append(s).append("  }\n");
    out.append('\n').append(s).append("  @").append(Override.class.getName()).append('\n').append(s).append("  public ").append(String.class.getName()).append(" toString() {\n").append(s).append("    return name;\n").append(s).append("  }\n").append(s).append('}');

    return out.toString();
  }
}