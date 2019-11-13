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
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.jaxdb.ddlx.DDLxAudit;
import org.jaxdb.jsql.EntityEnum;
import org.jaxdb.jsql.GenerateOn;
import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.type;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Documented;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.jsql_0_4.xLygluGCXAA.$Integer;
import org.jaxdb.www.jsql_0_4.xLygluGCXAA.$Table;
import org.jaxsb.runtime.Bindings;
import org.libj.util.Classes;
import org.libj.util.Identifiers;
import org.libj.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema;
import org.xml.sax.SAXException;

public class Generator {
  private static final Logger logger = LoggerFactory.getLogger(Generator.class);
  private static final String GENERATED_VALUE = "Autogenerated by JAX-DB Compiler (" + Generator.class.getPackage().getImplementationVersion() + ")";
  private static final String GENERATED_DATE = LocalDate.now().toString();
  private static final String HEADER_COMMENT;

  static {
    final StringBuilder builder = new StringBuilder();
    builder.append("/**\n");
    builder.append(" * ").append(GENERATED_VALUE).append('\n');
    builder.append(" *\n");
    builder.append(" * THIS FILE SHOULD NOT BE EDITED\n");
    builder.append(" * @generated\n");
    builder.append(" */\n");
    HEADER_COMMENT = builder.toString();
  }

  private final JSqlAudit audit;

  public Generator(final URL url) throws IOException, SAXException {
    this.audit = new JSqlAudit(new DDLxAudit((xLygluGCXAA.Schema)Bindings.parse(url)));
  }

  private static String getDoc(final $Documented documented, final int depth, final char shart, final char end) {
    final XMLSchema.yAA.$String documentation = documented.getDocumentation();
    if (documentation == null)
      return "";

    final String doc = documentation.text().trim();
    if (doc.length() == 0)
      return "";

    final String indent = Strings.repeat(" ", depth * 2);
    final StringBuilder builder = new StringBuilder();
    if (shart != '\0')
      builder.append(shart);

    builder.append(indent).append("/** ").append(doc).append(" */");
    if (end != '\0')
      builder.append(end);

    return builder.toString();
  }

  public void generate(final String name, final File destDir) throws IOException {
    logger.info("Generating jSQL: " + name);

    final String pkg = type.class.getPackage().getName();

    final File dir = new File(destDir, pkg.replace('.', '/'));
    if (!dir.exists() && !dir.mkdirs())
      throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());

    final String classSimpleName = Identifiers.toInstanceCase(name);

    final StringBuilder codeBuilder = new StringBuilder(HEADER_COMMENT);
    codeBuilder.append("package ").append(pkg).append(";\n\n");
    codeBuilder.append(getDoc(audit.schema(), 0, '\0', '\n'));
    codeBuilder.append('@').append(Generated.class.getName()).append("(value=\"" + GENERATED_VALUE + "\", date=\"" + GENERATED_DATE + "\")\n");
    codeBuilder.append("public final class ").append(classSimpleName).append(" extends ").append(Schema.class.getCanonicalName()).append(" {\n");

    final StringBuilder tablesBuilder = new StringBuilder();
    // First create the abstract entities
    for (final $Table table : audit.schema().getTable())
      if (table.getAbstract$().text())
        tablesBuilder.append("\n\n").append(makeTable(table));

    // Then, in proper inheritance order, the real entities
    for (final $Table table : audit.schema().getTable())
      if (!table.getAbstract$().text())
        tablesBuilder.append("\n\n").append(makeTable(table));

    codeBuilder.append(tablesBuilder.substring(2)).append("\n\n");

    /*codeBuilder.append("  private " + Classes.getFormalName(String.class) + " name = \"" + classSimpleName + "\";\n\n";
    codeBuilder.append("  public boolean equals(final " + Classes.getFormalName(Object.class) + " obj) {\n";
    codeBuilder.append("    if (obj == this)\n      return true;\n\n";
    codeBuilder.append("    if (!(obj instanceof " + className + "))\n      return false;\n\n";
    codeBuilder.append("    return name.equals(((" + className + ")obj).name);\n  }\n\n";
    codeBuilder.append("  public int hashCode() {\n    return name.hashCode();\n  }\n\n";*/

    codeBuilder.append("  private ").append(classSimpleName).append("() {\n  }\n}");

    final File javaFile = new File(dir, classSimpleName + ".java");
    Files.write(javaFile.toPath(), codeBuilder.toString().getBytes());
  }

  private static final Object THIS = new Object();

  private Type getType(final xLygluGCXAA.$Table table, final $Column column) {
    final Class<?> cls = column.getClass().getSuperclass();
    GenerateOn<?> generateOnInsert = null;
    GenerateOn<?> generateOnUpdate = null;
    final Object[] params = {THIS, column.getName$().text(), audit.isUnique(table, column), audit.isPrimary(table, column), column.getNull$().text()};
    if (column instanceof $Char) {
      final $Char type = ($Char)column;
      if (type.getSqlxGenerateOnInsert$() != null && $Char.GenerateOnInsert$.UUID.text().equals(type.getSqlxGenerateOnInsert$().text()))
        generateOnInsert = GenerateOn.UUID;

      return new Type(column, type.CHAR.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getLength$() == null ? null : type.getLength$().text(), type.getVarying$().text());
    }

    if (column instanceof $Clob) {
      final $Clob type = ($Clob)column;
      return new Type(column, type.CLOB.class, params, null, generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getLength$() == null ? null : type.getLength$().text());
    }

    if (column instanceof $Binary) {
      final $Binary type = ($Binary)column;
      return new Type(column, type.BINARY.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getLength$() == null ? null : type.getLength$().text(), type.getVarying$().text());
    }

    if (column instanceof $Blob) {
      final $Blob type = ($Blob)column;
      return new Type(column, type.BLOB.class, params, null, generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getLength$() == null ? null : type.getLength$().text());
    }

    if (column instanceof $Integer) {
      final $Integer type = ($Integer)column;
      // no autogenerator is necessary for ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT
      if (type.getSqlxGenerateOnUpdate$() != null)
        if ($Integer.GenerateOnUpdate$.INCREMENT.text().equals(type.getSqlxGenerateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.INCREMENT;

      if (column instanceof $Tinyint) {
        final $Tinyint integer = ($Tinyint)column;
        return new Type(column, integer.getUnsigned$().text() ? type.TINYINT.UNSIGNED.class : type.TINYINT.class, params, integer.getDefault$() == null || integer.getDefault$().text() == null ? null : integer.getUnsigned$().text() ? integer.getDefault$().text().shortValue() : integer.getDefault$().text().byteValue(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : Short.valueOf(integer.getMin$().text().shortValue()), integer.getMax$() == null ? null : Short.valueOf(integer.getMax$().text().shortValue()));
      }

      if (column instanceof $Smallint) {
        final $Smallint integer = ($Smallint)column;
        return new Type(column, integer.getUnsigned$().text() ? type.SMALLINT.UNSIGNED.class : type.SMALLINT.class, params, integer.getDefault$() == null || integer.getDefault$().text() == null ? null : integer.getUnsigned$().text() ? integer.getDefault$().text().intValue() : integer.getDefault$().text().shortValue(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : Integer.valueOf(integer.getMin$().text().intValue()), integer.getMax$() == null ? null : Integer.valueOf(integer.getMax$().text().intValue()));
      }

      if (column instanceof $Int) {
        final $Int integer = ($Int)column;
        return new Type(column, integer.getUnsigned$().text() ? type.INT.UNSIGNED.class : type.INT.class, params, integer.getDefault$() == null || integer.getDefault$().text() == null ? null : integer.getUnsigned$().text() ? integer.getDefault$().text().longValue() : integer.getDefault$().text().intValue(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : Long.valueOf(integer.getMin$().text().longValue()), integer.getMax$() == null ? null : Long.valueOf(integer.getMax$().text().longValue()));
      }

      if (column instanceof $Bigint) {
        final $Bigint integer = ($Bigint)column;
        return new Type(column, integer.getUnsigned$().text() ? type.BIGINT.UNSIGNED.class : type.BIGINT.class, params, integer.getDefault$() == null ? null : integer.getUnsigned$().text() ? integer.getDefault$().text() : integer.getDefault$().text().longValue(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : BigInteger.valueOf(integer.getMin$().text().longValue()), integer.getMax$() == null ? null : BigInteger.valueOf(integer.getMax$().text().longValue()));
      }

      throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
    }

    if (column instanceof $Float) {
      final $Float type = ($Float)column;
      final Class<? extends type.DataType<?>> javaType = type.getUnsigned$().text() ? type.FLOAT.UNSIGNED.class : type.FLOAT.class;
      final Number min = type.getMin$() != null ? type.getMin$().text().floatValue() : null;
      final Number max = type.getMax$() != null ? type.getMax$().text().floatValue() : null;
      return new Type(column, javaType, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), min, max);
    }

    if (column instanceof $Double) {
      final $Double type = ($Double)column;
      final Class<? extends type.DataType<?>> javaType = type.getUnsigned$().text() ? type.DOUBLE.UNSIGNED.class : type.DOUBLE.class;
      final Number min = type.getMin$() != null ? type.getMin$().text().doubleValue() : null;
      final Number max = type.getMax$() != null ? type.getMax$().text().doubleValue() : null;
      return new Type(column, javaType, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), min, max);
    }

    if (column instanceof $Decimal) {
      final $Decimal type = ($Decimal)column;
      return new Type(column, type.getUnsigned$().text() ? type.DECIMAL.UNSIGNED.class : type.DECIMAL.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getPrecision$() == null ? null : type.getPrecision$().text().intValue(), type.getScale$() == null ? null : type.getScale$().text().intValue(), type.getMin$() == null ? null : type.getMin$().text(), type.getMax$() == null ? null : type.getMax$().text());
    }

    if (column instanceof $Date) {
      final $Date type = ($Date)column;
      if (type.getSqlxGenerateOnInsert$() != null)
        if ($Date.GenerateOnInsert$.TIMESTAMP.text().equals(type.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (type.getSqlxGenerateOnUpdate$() != null)
        if ($Date.GenerateOnUpdate$.TIMESTAMP.text().equals(type.getSqlxGenerateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.DATE.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text());
    }

    if (column instanceof $Time) {
      final $Time type = ($Time)column;
      if (type.getSqlxGenerateOnInsert$() != null)
        if ($Time.GenerateOnInsert$.TIMESTAMP.text().equals(type.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (type.getSqlxGenerateOnUpdate$() != null)
        if ($Time.GenerateOnUpdate$.TIMESTAMP.text().equals(type.getSqlxGenerateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.TIME.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getPrecision$().text());
    }

    if (column instanceof $Datetime) {
      final $Datetime type = ($Datetime)column;
      if (type.getSqlxGenerateOnInsert$() != null)
        if ($Datetime.GenerateOnInsert$.TIMESTAMP.text().equals(type.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (type.getSqlxGenerateOnUpdate$() != null)
        if ($Datetime.GenerateOnUpdate$.TIMESTAMP.text().equals(type.getSqlxGenerateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.DATETIME.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text(), type.getPrecision$().text());
    }

    if (column instanceof $Boolean) {
      final $Boolean type = ($Boolean)column;
      return new Type(column, type.BOOLEAN.class, params, type.getDefault$() == null ? null : type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text());
    }

    if (column instanceof $Enum) {
      final $Enum type = ($Enum)column;
      return new Type(column, type.ENUM.class, params, type.getDefault$() == null || type.getDefault$().text() == null ? null : Identifiers.toClassCase(column.getName$().text()) + "." + type.getDefault$().text(), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text());
    }

    throw new IllegalArgumentException("Unknown type: " + cls);
  }

  private class Type {
    private final $Column column;
    @SuppressWarnings("rawtypes")
    public final Class<? extends type.DataType> type;
    private final Object[] commonParams;
    private final GenerateOn<?> generateOnInsert;
    private final GenerateOn<?> generateOnUpdate;
    private final boolean keyForUpdate;
    private final Object[] customParams;
    private final Object _default;

    @SuppressWarnings("rawtypes")
    private Type(final $Column column, final Class<? extends type.DataType> type, final Object[] commonParams, final Object _default, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final boolean keyForUpdate, final Object ... params) {
      this.column = column;
      this.type = type;
      this.commonParams = commonParams;
      this._default = "null".equals(_default) ? null : _default;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;
      this.customParams = params;
    }

    private String compileParams() {
      final StringBuilder builder = new StringBuilder();
      for (final Object param : commonParams)
        builder.append(", ").append(param == THIS ? "this" : GeneratorUtil.compile(param));

      builder.append(", ").append(GeneratorUtil.compile(_default));
      builder.append(", ").append(GeneratorUtil.compile(generateOnInsert));
      builder.append(", ").append(GeneratorUtil.compile(generateOnUpdate));
      builder.append(", ").append(keyForUpdate);
      if (customParams != null)
        for (final Object param : customParams)
          builder.append(", ").append((param == THIS ? "this" : GeneratorUtil.compile(param)));

      return builder.substring(2);
    }

    public String getType(final boolean withGeneric) {
      final StringBuilder builder = new StringBuilder(type.getCanonicalName());
      if (type != type.ENUM.class)
        return builder.toString();

      builder.append('<');
      if (withGeneric)
        builder.append(Identifiers.toClassCase(column.getName$().text()));

      builder.append('>');
      return builder.toString();
    }

    @Override
    public String toString() {
      return "new " + getType(false) + "(" + compileParams() + (type == type.ENUM.class ? ", " + Identifiers.toClassCase(column.getName$().text()) + ".class" : "") + ")";
    }
  }

  private int getColumnCount(xLygluGCXAA.$Table table, final boolean deep) {
    int count = 0;
    do {
      count += table.getColumn() != null ? table.getColumn().size() : 0;
    }
    while (deep && table.getExtends$() != null && (table = audit.tableNameToTable.get(table.getExtends$().text())) != null);
    return count;
  }

  private int getPrimaryColumnCount(xLygluGCXAA.$Table table, final boolean deep) {
    int count = 0;
    do {
      if (table.getConstraints() != null && table.getConstraints().getPrimaryKey() != null)
        count += table.getConstraints().getPrimaryKey().getColumn().size();
    }
    while (deep && table.getExtends$() != null && (table = audit.tableNameToTable.get(table.getExtends$().text())) != null);
    return count;
  }

  public String makeTable(final $Table table) {
    final String ext = table.getExtends$() != null ? Identifiers.toClassCase(table.getExtends$().text()) : type.Entity.class.getCanonicalName();
    final StringBuilder out = new StringBuilder();
    String abs = "";
    if (table.getAbstract$().text())
      abs = table.getAbstract$().text() ? " abstract" : "";

    final String entityName = Identifiers.toClassCase(table.getName$().text());
    final int totalColumnCount = getColumnCount(table, true);
    final int totalPrimaryCount = getPrimaryColumnCount(table, true);
    final int localPrimaryCount = getPrimaryColumnCount(table, false);
    out.append(getDoc(table, 1, '\0', '\n'));
    out.append("  public static").append(abs).append(" class ").append(entityName).append(" extends ").append(ext).append(" {\n");
    // FIXME: Gotta redesign this... right now, extended classes will all have their own copies of column and primary arrays
    if (!table.getAbstract$().text()) {
      out.append("    protected static final ").append(entityName).append(" identity = new ").append(entityName).append("();\n\n");
      out.append("    @").append(Override.class.getName()).append("\n");
      out.append("    protected ").append(String.class.getName()).append(" name() {\n");
      out.append("      return \"").append(table.getName$().text()).append("\";\n");
      out.append("    }\n\n");
      out.append("    @").append(Override.class.getName()).append("\n");
      out.append("    protected ").append(entityName).append(" newInstance() {\n");
      out.append("      return new ").append(entityName).append("(true);\n");
      out.append("    }\n\n");
      out.append("    /** Creates a new {@code ").append(entityName).append("}. */\n");
      out.append("    public ").append(entityName).append("() {\n");
      out.append("      this(false, new ").append(type.DataType.class.getCanonicalName()).append("[").append(totalColumnCount).append("], new ").append(type.DataType.class.getCanonicalName()).append("[").append(totalPrimaryCount).append("]);\n");
      out.append("    }\n\n");
      out.append("    protected ").append(entityName).append("(final boolean wasSelected) {\n");
      out.append("      this(wasSelected, new ").append(type.DataType.class.getCanonicalName()).append("[").append(totalColumnCount).append("], new ").append(type.DataType.class.getCanonicalName()).append("[").append(totalPrimaryCount).append("]);\n");
      out.append("    }\n\n");

      // Constructor with primary key columns
      final StringBuilder set = new StringBuilder();
      if (table.getColumn() != null && totalPrimaryCount > 0) {
        out.append("    /** Creates a new {@code ").append(entityName).append("} with the specified primary key. */\n");
        out.append("    public ").append(entityName).append("(");
        xLygluGCXAA.$Table t = table;
        final StringBuilder params = new StringBuilder();
        do {
          for (int i = 0; i < t.getColumn().size(); ++i) {
            final $Column column = t.getColumn().get(i);
            if (audit.isPrimary(table, column)) {
              params.append(", ").append(makeParam(t, column));
              final String columnName = Identifiers.toCamelCase(column.getName$().text());
              set.append("\n      this.").append(columnName).append(".set(").append(columnName).append(");");
            }
          }
        }
        while (t.getExtends$() != null && (t = audit.tableNameToTable.get(t.getExtends$().text())) != null);
        out.append(params.substring(2)).append(") {\n");
        out.append("      this();\n");
        out.append(set.substring(1)).append("\n    }\n\n");
      }

      // Copy constructor
      if (table.getColumn() == null || table.getColumn().size() == 0)
        out.append("    @").append(SuppressWarnings.class.getName()).append("(\"unused\")\n");

        out.append("    /** Creates a new {@code ").append(entityName).append("} as a copy of the specified {@code ").append(entityName).append("} instance. */\n");
        out.append("    public ").append(entityName).append("(final ").append(entityName).append(" copy) {\n");
        out.append("      this();\n");
      set.setLength(0);
      if (table.getColumn() != null) {
        for (int i = 0; i < table.getColumn().size(); ++i) {
          final $Column column = table.getColumn().get(i);
          final String columnName = Identifiers.toCamelCase(column.getName$().text());
          set.append("\n      this.").append(columnName).append(".set(copy.").append(columnName).append(".get());");
        }

        out.append(set.substring(1)).append("\n");
      }

      out.append("    }\n\n");
    }

    out.append("    protected ").append(entityName).append("(final boolean wasSelected, final ").append(type.DataType.class.getCanonicalName()).append("<?>[] column, final ").append(type.DataType.class.getCanonicalName()).append("<?>[] primary) {\n");
    out.append("      super(wasSelected, column, primary);\n");

    final StringBuilder defs = new StringBuilder();
    int primaryIndex = 0;
    if (table.getColumn() != null) {
      for (int i = 0; i < table.getColumn().size(); ++i) {
        final $Column column = table.getColumn().get(i);
        final String columnName = Identifiers.toCamelCase(column.getName$().text());
        defs.append("\n      column[").append((totalColumnCount - (table.getColumn().size() - i))).append("] = ");
        if (audit.isPrimary(table, column))
          defs.append("primary[" + (totalPrimaryCount - (localPrimaryCount - primaryIndex++)) + "] = ");

        defs.append(columnName).append(';');
      }

      out.append(defs.substring(1)).append("\n");
    }

    out.append("    }\n");

    if (table.getColumn() != null) {
      for (int i = 0; i < table.getColumn().size(); ++i) {
        final $Column column = table.getColumn().get(i);
        out.append(makeColumn(table, column));
      }

      out.append("\n");
    }

    if (table.getAbstract$().text()) {
      out.append("\n");
      out.append("    @").append(Override.class.getName()).append("\n");
      out.append("    public abstract ").append(entityName).append(" clone();\n");
    }
    else {
      out.append("\n");
      out.append("    @").append(Override.class.getName()).append("\n");
      out.append("    public ").append(entityName).append(" clone() {\n");
      out.append("      return new ").append(entityName).append("(this);\n");
      out.append("    }\n");
    }

    out.append("\n");
    out.append("    @").append(Override.class.getName()).append("\n");
    out.append("    public boolean equals(final ").append(Object.class.getName()).append(" obj) {\n");
    out.append("      if (obj == this)\n        return true;\n\n");
    out.append("      if (!(obj instanceof ").append(entityName).append(")").append((table.getExtends$() != null ? " || !super.equals(obj)" : "")).append(")\n        return false;\n\n");

    final StringBuilder eq = new StringBuilder();
    final List<$Column> primaryColumns = new ArrayList<>();
    final List<$Column> equalsColumns;
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn())
        if (audit.isPrimary(table, column))
          primaryColumns.add(column);

      equalsColumns = primaryColumns.size() > 0 ? primaryColumns : table.getColumn();
      out.append("      final ").append(entityName).append(" that = (").append(entityName).append(")obj;\n");
      for (final $Column column : equalsColumns)
        eq.append(" && (this.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get() != null ? this.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get().equals(that.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get()) : that.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get() == null)");

      out.append("      return ").append(eq.substring(4)).append(';');
    }
    else {
      equalsColumns = null;
      out.append("      return true;");
    }
    out.append("\n    }");

    eq.setLength(0);
    if (equalsColumns != null && equalsColumns.size() > 0) {
      out.append("\n\n");
      out.append("    @").append(Override.class.getName()).append("\n");
      out.append("    public int hashCode() {\n");
      for (final $Column column : equalsColumns)
        eq.append(" + (this.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get() != null ? this.").append(Identifiers.toInstanceCase(column.getName$().text())).append(".get().hashCode() : -1)");
      out.append("      return ").append(eq.substring(3)).append(';');
      out.append("\n    }");
    }

    out.append("\n\n");
    out.append("    @").append(Override.class.getName()).append("\n");
    out.append("    public ").append(String.class.getName()).append(" toString() {\n");
    out.append("      final ").append(StringBuilder.class.getName()).append(" builder = new ").append(StringBuilder.class.getName()).append("(super.toString());\n");
    out.append("      if (builder.charAt(builder.length() - 1) == '}')\n");
    out.append("        builder.setLength(builder.length() - 1);\n");
    out.append("      else\n");
    out.append("        builder.append(\" {\\n\");\n\n");
    if (table.getColumn() != null)
      for (final $Column column : table.getColumn())
        out.append("      builder.append(\"  ").append(Identifiers.toInstanceCase(column.getName$().text())).append(": \").append(").append(Identifiers.toInstanceCase(column.getName$().text())).append(").append(\"\\n\");\n");
        out.append("      return builder.append('}').toString();");
        out.append("\n    }");

        out.append("\n  }");

    return out.toString();
  }

  public String makeParam(final xLygluGCXAA.$Table table, final $Column column) {
    final String columnName = Identifiers.toCamelCase(column.getName$().text());
    final Type type = getType(table, column);
    final String rawType;
    if (column instanceof $Enum)
      rawType = Identifiers.toClassCase(column.getName$().text());
    else
      rawType = ((Class<?>)Classes.getSuperclassGenericTypes(type.type)[0]).getCanonicalName();

    return "final " + rawType + " " + columnName;
  }

  private static final Map<Character,String> substitutions = Collections.singletonMap(' ', "_");

  public String makeColumn(final $Table table, final $Column column) {
    final String columnName = Identifiers.toCamelCase(column.getName$().text());
    final String typeName = Identifiers.toClassCase(column.getName$().text());
    final StringBuilder builder = new StringBuilder();
    final Type type = getType(table, column);
    if (column instanceof $Enum) {
      builder.append("\n    @").append(EntityEnum.Spec.class.getCanonicalName()).append("(table=\"").append(table.getName$().text()).append("\", column=\"").append(column.getName$().text()).append("\")");
      builder.append("\n    public static enum ").append(typeName).append(" implements ").append(EntityEnum.class.getName()).append(" {");
      final StringBuilder enums = new StringBuilder();
      final List<String> values = Dialect.parseEnum((($Enum)column).getValues$().text());
      for (final String value : values)
        enums.append(", ").append(Identifiers.toIdentifier(value, substitutions).toUpperCase().replace(' ', '_')).append("(\"").append(value).append("\")");

      builder.append("\n      ").append(enums.substring(2)).append(";\n\n");
      builder.append("      public static ").append(typeName).append(" fromString(final ").append(String.class.getName()).append(" string) {\n        if (string == null)\n          return null;\n\n        for (final ").append(typeName).append(" value : values())\n          if (string.equals(value.value))\n            return value;\n\n        return null;\n      }\n\n");
      builder.append("      private final ").append(String.class.getName()).append(" value;\n\n      ").append(typeName).append("(final ").append(String.class.getName()).append(" value) {\n        this.value = value;\n      }\n\n");
      builder.append("      @").append(Override.class.getName()).append("\n      public ").append(String.class.getName()).append(" toString() {\n        return value;\n      }\n    }");
    }

    builder.append(getDoc(column, 2, '\n', '\0'));
    return builder.append("\n    public final ").append(type.getType(true)).append(' ').append(columnName).append(" = ").append(type).append(';').toString();
  }
}