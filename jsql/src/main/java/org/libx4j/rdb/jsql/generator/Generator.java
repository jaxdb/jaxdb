/* Copyright (c) 2014 lib4j
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

package org.libx4j.rdb.jsql.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.lib4j.jci.CompilationException;
import org.lib4j.jci.JavaCompiler;
import org.lib4j.lang.ClassLoaders;
import org.lib4j.lang.Classes;
import org.lib4j.lang.Strings;
import org.lib4j.xml.validate.ValidationException;
import org.libx4j.rdb.ddlx.DDLxAudit;
import org.libx4j.rdb.jsql.Entity;
import org.libx4j.rdb.jsql.EntityEnum;
import org.libx4j.rdb.jsql.GenerateOn;
import org.libx4j.rdb.jsql.Schema;
import org.libx4j.rdb.jsql.type;
import org.libx4j.rdb.vendor.Dialect;
import org.libx4j.xsb.runtime.ParseException;
import org.safris.rdb.ddlx.xe.$ddlx_bigint;
import org.safris.rdb.ddlx.xe.$ddlx_binary;
import org.safris.rdb.ddlx.xe.$ddlx_blob;
import org.safris.rdb.ddlx.xe.$ddlx_boolean;
import org.safris.rdb.ddlx.xe.$ddlx_char;
import org.safris.rdb.ddlx.xe.$ddlx_clob;
import org.safris.rdb.ddlx.xe.$ddlx_column;
import org.safris.rdb.ddlx.xe.$ddlx_date;
import org.safris.rdb.ddlx.xe.$ddlx_dateTime;
import org.safris.rdb.ddlx.xe.$ddlx_decimal;
import org.safris.rdb.ddlx.xe.$ddlx_enum;
import org.safris.rdb.ddlx.xe.$ddlx_float;
import org.safris.rdb.ddlx.xe.$ddlx_int;
import org.safris.rdb.ddlx.xe.$ddlx_integer;
import org.safris.rdb.ddlx.xe.$ddlx_smallint;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.ddlx.xe.$ddlx_time;
import org.safris.rdb.ddlx.xe.$ddlx_tinyint;
import org.safris.rdb.jsql.xe.$jsql_integer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;

public class Generator {
  private static final Logger logger = LoggerFactory.getLogger(Generator.class);

  private final DDLxAudit audit;

  public Generator(final URL url) throws IOException, ParseException, ValidationException {
    this.audit = new DDLxAudit(url);
  }

  public void generate(final File destDir, final boolean compile) throws IOException {
    logger.info("Generating jSQL: " + audit.schema()._name$().text());

    final String pkg = type.class.getPackage().getName();

    final File dir = new File(destDir, pkg.replace('.', '/'));
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());

    final String classSimpleName = Strings.toInstanceCase(audit.schema()._name$().text());

    String code = "package " + pkg + ";\n\n";
    code += "public final class " + classSimpleName + " extends " + Classes.getStrictName(Schema.class) + " {\n";

    String tables = "";
    // First create the abstract entities
    for (final $ddlx_table table : audit.schema()._table())
      if (table._abstract$().text())
        tables += "\n\n" + makeTable(table);

    // Then, in proper inheritance order, the real entities
    for (final $ddlx_table table : audit.schema()._table())
      if (!table._abstract$().text())
        tables += "\n\n" + makeTable(table);

    code += tables.substring(2) + "\n\n";

    /*code += "  private " + Classes.getFormalName(String.class) + " name = \"" + classSimpleName + "\";\n\n";

    code += "  public boolean equals(final " + Classes.getFormalName(Object.class) + " obj) {\n";
    code += "    if (obj == this)\n      return true;\n\n";
    code += "    if (!(obj instanceof " + className + "))\n      return false;\n\n";
    code += "    return name.equals(((" + className + ")obj).name);\n  }\n\n";

    code += "  public int hashCode() {\n    return name.hashCode();\n  }\n\n";*/

    code += "  private " + classSimpleName + "() {\n  }\n}";

    final File javaFile = new File(dir, classSimpleName + ".java");
    try (final FileOutputStream out = new FileOutputStream(javaFile)) {
      out.write(code.getBytes());
    }

    if (compile) {
      try {
        new JavaCompiler(destDir).compile(destDir);
      }
      catch (final CompilationException e) {
        throw new UnsupportedOperationException(e);
      }

      ClassLoaders.addURL((URLClassLoader)ClassLoader.getSystemClassLoader(), destDir.toURI().toURL());
    }
  }

  private static final Object THIS = new Object();

  private Type getType(final $ddlx_table table, final $ddlx_column column) {
    final Class<?> cls = column.getClass().getSuperclass();
    final Object _default = getDefault(column);
    GenerateOn<?> generateOnInsert = null;
    GenerateOn<?> generateOnUpdate = null;
    final Object[] params = new Object[] {THIS, column._name$().text(), _default, audit.isUnique(table, column), audit.isPrimary(table, column), column._null$().text()};
    if (column instanceof $ddlx_char) {
      final $ddlx_char type = ($ddlx_char)column;
      if (!type.jsql_generateOnInsert$().isNull() && $ddlx_char.jsql_generateOnInsert$.UUID.text().equals(type.jsql_generateOnInsert$().text()))
        generateOnInsert = GenerateOn.UUID;

      return new Type(column, type.CHAR.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._varying$().text());
    }

    if (column instanceof $ddlx_clob) {
      final $ddlx_clob type = ($ddlx_clob)column;
      return new Type(column, type.CLOB.class, params, generateOnInsert, generateOnUpdate, type._length$().text());
    }

    if (column instanceof $ddlx_binary) {
      final $ddlx_binary type = ($ddlx_binary)column;
      return new Type(column, type.BINARY.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._varying$().text());
    }

    if (column instanceof $ddlx_blob) {
      final $ddlx_blob type = ($ddlx_blob)column;
      return new Type(column, type.BLOB.class, params, generateOnInsert, generateOnUpdate, type._length$().text());
    }

    if (column instanceof $jsql_integer) {
      final $jsql_integer type = ($jsql_integer)column;
      // no autogenerator is necessary for ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT
      if (!type.jsql_generateOnUpdate$().isNull())
        if ($jsql_integer.jsql_generateOnUpdate$.INCREMENT.text().equals(type.jsql_generateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.INCREMENT;

      if (column instanceof $ddlx_tinyint) {
        final $ddlx_tinyint integer = ($ddlx_tinyint)column;
        return new Type(column, integer._unsigned$().text() ? type.TINYINT.UNSIGNED.class : type.TINYINT.class, params, generateOnInsert, generateOnUpdate, integer._precision$().text().intValue(), integer._min$().isNull() ? null : new Short(integer._min$().text().shortValue()), integer._max$().isNull() ? null : new Short(integer._max$().text().shortValue()));
      }

      if (column instanceof $ddlx_smallint) {
        final $ddlx_smallint integer = ($ddlx_smallint)column;
        return new Type(column, integer._unsigned$().text() ? type.SMALLINT.UNSIGNED.class : type.SMALLINT.class, params, generateOnInsert, generateOnUpdate, integer._precision$().text().intValue(), integer._min$().isNull() ? null : new Integer(integer._min$().text().intValue()), integer._max$().isNull() ? null : new Integer(integer._max$().text().intValue()));
      }

      if (column instanceof $ddlx_int) {
        final $ddlx_int integer = ($ddlx_int)column;
        return new Type(column, integer._unsigned$().text() ? type.INT.UNSIGNED.class : type.INT.class, params, generateOnInsert, generateOnUpdate, integer._precision$().text().intValue(), integer._min$().isNull() ? null : new Long(integer._min$().text().longValue()), integer._max$().isNull() ? null : new Long(integer._max$().text().longValue()));
      }

      if (column instanceof $ddlx_bigint) {
        final $ddlx_bigint integer = ($ddlx_bigint)column;
        return new Type(column, integer._unsigned$().text() ? type.BIGINT.UNSIGNED.class : type.BIGINT.class, params, generateOnInsert, generateOnUpdate, integer._precision$().text().intValue(), integer._min$().isNull() ? null : BigInteger.valueOf(integer._min$().text().longValue()), integer._max$().isNull() ? null : BigInteger.valueOf(integer._max$().text().longValue()));
      }

      throw new UnsupportedOperationException("Unexpected type: " + column.getClass().getName());
    }

    if (column instanceof $ddlx_float) {
      final $ddlx_float type = ($ddlx_float)column;
      final Class<? extends type.DataType<?>> javaType;
      final Number min;
      final Number max;
      if (type._double$().text()) {
        javaType = type._unsigned$().text() ? type.DOUBLE.UNSIGNED.class : type.DOUBLE.class;
        min = type._min$().text() != null ? type._min$().text().doubleValue() : null;
        max = type._max$().text() != null ? type._max$().text().doubleValue() : null;
      }
      else {
        javaType = type._unsigned$().text() ? type.FLOAT.UNSIGNED.class : type.FLOAT.class;
        min = type._min$().text() != null ? type._min$().text().floatValue() : null;
        max = type._max$().text() != null ? type._max$().text().floatValue() : null;
      }

      return new Type(column, javaType, params, generateOnInsert, generateOnUpdate, min, max);
    }

    if (column instanceof $ddlx_decimal) {
      final $ddlx_decimal type = ($ddlx_decimal)column;
      return new Type(column, type._unsigned$().text() ? type.DECIMAL.UNSIGNED.class : type.DECIMAL.class, params, generateOnInsert, generateOnUpdate, type._precision$().text().intValue(), type._scale$().text().intValue(), type._min$().text(), type._max$().text());
    }

    if (column instanceof $ddlx_date) {
      final $ddlx_date type = ($ddlx_date)column;
      if (!type.jsql_generateOnInsert$().isNull())
        if ($ddlx_date.jsql_generateOnInsert$.TIMESTAMP.text().equals(type.jsql_generateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (!type.jsql_generateOnUpdate$().isNull())
        if ($ddlx_date.jsql_generateOnUpdate$.TIMESTAMP.text().equals(type.jsql_generateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.DATE.class, params, generateOnInsert, generateOnUpdate);
    }

    if (column instanceof $ddlx_time) {
      final $ddlx_time type = ($ddlx_time)column;
      if (!type.jsql_generateOnInsert$().isNull())
        if ($ddlx_time.jsql_generateOnInsert$.TIMESTAMP.text().equals(type.jsql_generateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (!type.jsql_generateOnUpdate$().isNull())
        if ($ddlx_time.jsql_generateOnUpdate$.TIMESTAMP.text().equals(type.jsql_generateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.TIME.class, params, generateOnInsert, generateOnUpdate, type._precision$().text());
    }

    if (column instanceof $ddlx_dateTime) {
      final $ddlx_dateTime type = ($ddlx_dateTime)column;
      if (!type.jsql_generateOnInsert$().isNull())
        if ($ddlx_dateTime.jsql_generateOnInsert$.TIMESTAMP.text().equals(type.jsql_generateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;

      if (!type.jsql_generateOnUpdate$().isNull())
        if ($ddlx_dateTime.jsql_generateOnUpdate$.TIMESTAMP.text().equals(type.jsql_generateOnUpdate$().text()))
          generateOnUpdate = GenerateOn.TIMESTAMP;

      return new Type(column, type.DATETIME.class, params, generateOnInsert, generateOnUpdate, type._precision$().text());
    }

    if (column instanceof $ddlx_boolean) {
      return new Type(column, type.BOOLEAN.class, params, generateOnInsert, generateOnUpdate);
    }

    if (column instanceof $ddlx_enum) {
      return new Type(column, type.ENUM.class, params, generateOnInsert, generateOnUpdate);
    }

    throw new IllegalArgumentException("Unknown type: " + cls);
  }

  private class Type {
    private final $ddlx_column column;
    @SuppressWarnings("rawtypes")
    public final Class<? extends type.DataType> type;
    private final Object[] commonParams;
    private final GenerateOn<?> generateOnInsert;
    private final GenerateOn<?> generateOnUpdate;
    private final Object[] customParams;

    @SuppressWarnings("rawtypes")
    private Type(final $ddlx_column column, final Class<? extends type.DataType> type, final Object[] commonParams, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
      this.column = column;
      this.type = type;
      this.commonParams = commonParams;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.customParams = params;
    }

    private String serializeParams() {
      String out = "";
      for (final Object param : commonParams)
        out += ", " + (param == THIS ? "this" : GeneratorUtil.serialize(param));

      out += ", " + GeneratorUtil.serialize(generateOnInsert);
      out += ", " + GeneratorUtil.serialize(generateOnUpdate);
      if (customParams != null)
        for (final Object param : customParams)
          out += ", " + (param == THIS ? "this" : GeneratorUtil.serialize(param));

      return out.substring(2);
    }

    public String getType() {
      return Classes.getStrictName(type) + (type == type.ENUM.class ? "<" + Strings.toTitleCase(column._name$().text()) + ">" : "");
    }

    @Override
    public String toString() {
      return "new " + getType() + "(" + serializeParams() + (type == type.ENUM.class ? ", " + Strings.toTitleCase(column._name$().text()) + ".class" : "") + ")";
    }
  }

  public static Object getDefault(final $ddlx_column column) {
    try {
      final Method method = Classes.getDeclaredMethodDeep(column.getClass(), "_default$");
      if (method == null)
        return null;

      final $xs_anySimpleType value = ($xs_anySimpleType)method.invoke(column);
      if (value.isNull() || "null".equals(value.text()))
        return null;

      if (column instanceof $ddlx_enum)
        return Strings.toTitleCase(column._name$().text()) + "." + String.valueOf(value.text());

      if (column instanceof $ddlx_integer) {
        if (column instanceof $ddlx_tinyint)
          return Short.valueOf(String.valueOf(value.text()));

        if (column instanceof $ddlx_smallint || column instanceof $ddlx_int)
          return Long.valueOf(String.valueOf(value.text()));

        return new BigInteger(String.valueOf(value.text()));
      }

      return value.text();
    }
    catch (final Exception e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private int getColumnCount($ddlx_table table, final boolean deep) {
    int count = 0;
    do {
      count += table._column() != null ? table._column().size() : 0;
    }
    while (deep && (table = audit.tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  private int getPrimaryColumnCount($ddlx_table table, final boolean deep) {
    int count = 0;
    do {
      if (!table._constraints(0)._primaryKey(0).isNull())
        count += table._constraints(0)._primaryKey(0)._column().size();
    }
    while (deep && (table = audit.tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  public String makeTable(final $ddlx_table table) {
    final String ext = !table._extends$().isNull() ? Strings.toTitleCase(table._extends$().text()) : Classes.getStrictName(Entity.class);
    String out = "";
    String abs = "";
    if (table._abstract$().text())
      abs = table._abstract$().text() ? " abstract" : "";

    final String entityName = Strings.toTitleCase(table._name$().text());
    final int totalColumnCount = getColumnCount(table, true);
    final int totalPrimaryCount = getPrimaryColumnCount(table, true);
    final int localPrimaryCount = getPrimaryColumnCount(table, false);
    out += "  public static" + abs + " class " + entityName + " extends " + ext + " {\n";
    if (!table._abstract$().text()) {
      out += "    protected static final " + Strings.toTitleCase(table._name$().text()) + " identity = new " + Strings.toTitleCase(table._name$().text()) + "();\n\n";
      out += "    protected final " + Classes.getStrictName(type.DataType.class) + "<?>[] column;\n";
      out += "    protected final " + Classes.getStrictName(type.DataType.class) + "<?>[] primary;\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + Classes.getStrictName(type.DataType.class) + "<?>[] column() {\n";
      out += "      return column;\n";
      out += "    }\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + Classes.getStrictName(type.DataType.class) + "<?>[] primary() {\n";
      out += "      return primary;\n";
      out += "    }\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + Classes.getStrictName(String.class) + " name() {\n";
      out += "      return \"" + table._name$().text() + "\";\n";
      out += "    }\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + entityName + " newInstance() {\n";
      out += "      return new " + entityName + "(true);\n";
      out += "    }\n\n";
      out += "    public " + Strings.toTitleCase(table._name$().text()) + "() {\n";
      out += "      this(false, new " + Classes.getStrictName(type.DataType.class) + "[" + totalColumnCount + "], new " + Classes.getStrictName(type.DataType.class) + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";
      out += "    protected " + Strings.toTitleCase(table._name$().text()) + "(final boolean wasSelected) {\n";
      out += "      this(wasSelected, new " + Classes.getStrictName(type.DataType.class) + "[" + totalColumnCount + "], new " + Classes.getStrictName(type.DataType.class) + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";

      // Constructor with primary key columns
      String set = "";
      if (table._column() != null && totalPrimaryCount > 0) {
        out += "    public " + Strings.toTitleCase(table._name$().text()) + "(";
        $ddlx_table t = table;
        String params = "";
        do {
          for (int i = 0; i < t._column().size(); i++) {
            final $ddlx_column column = t._column().get(i);
            if (audit.isPrimary(table, column)) {
              params += ", " + makeParam(t, column);
              final String columnName = Strings.toCamelCase(column._name$().text());
              set += "\n      this." + columnName + ".set(" + columnName + ");";
            }
          }
        }
        while (!t._extends$().isNull() && (t = audit.tableNameToTable.get(t._extends$().text())) != null);
        out += params.substring(2) + ") {\n";
        out += "      this();\n";
        out += set.substring(1) + "\n    }\n\n";
      }

      // Copy constructor
      out += "    public " + Strings.toTitleCase(table._name$().text()) + "(final " + Strings.toTitleCase(table._name$().text()) + " copy) {\n";
      out += "      this();\n";
      set = "";
      if (table._column() != null) {
        for (int i = 0; i < table._column().size(); i++) {
          final $ddlx_column column = table._column().get(i);
          final String columnName = Strings.toCamelCase(column._name$().text());
          set += "\n      this." + columnName + ".set(copy." + columnName + ".get());";
        }

        out += set.substring(1) + "\n";
      }

      out += "    }\n\n";
    }

    String defs = "";
    out += "    protected " + Strings.toTitleCase(table._name$().text()) + "(final boolean wasSelected, final " + Classes.getStrictName(type.DataType.class) + "<?>[] column, final " + Classes.getStrictName(type.DataType.class) + "<?>[] primary) {\n";
    out += "      super(wasSelected, column, primary);\n";
    if (!table._abstract$().text()) {
      out += "      this.column = column;\n";
      out += "      this.primary = primary;\n";
    }

    defs = "";
    int primaryIndex = 0;
    if (table._column() != null) {
      for (int i = 0; i < table._column().size(); i++) {
        final $ddlx_column column = table._column().get(i);
        final String columnName = Strings.toCamelCase(column._name$().text());
        defs += "\n      column[" + (totalColumnCount - (table._column().size() - i)) + "] = " + (audit.isPrimary(table, column) ? "primary[" + (totalPrimaryCount - (localPrimaryCount - primaryIndex++)) + "] = " : "") + columnName + ";";
      }

      out += defs.substring(1) + "\n";
    }

    out += "    }\n";

    if (table._column() != null) {
      for (int i = 0; i < table._column().size(); i++) {
        final $ddlx_column column = table._column().get(i);
        out += makeColumn(table, column, i == table._column().size());
      }

      out += "\n";
    }

    out += "\n";
    out += "    @" + Classes.getStrictName(Override.class) + "\n";
    out += "    public boolean equals(final " + Classes.getStrictName(Object.class) + " obj) {\n";
    out += "      if (obj == this)\n        return true;\n\n";
    out += "      if (!(obj instanceof " + entityName + ")" + (!table._extends$().isNull() ? " || !super.equals(obj)" : "") + ")\n        return false;\n\n";

    String eq = "";
    final List<$ddlx_column> primaryColumns = new ArrayList<$ddlx_column>();
    final List<$ddlx_column> equalsColumns;
    if (table._column() != null) {
      for (final $ddlx_column column : table._column())
        if (audit.isPrimary(table, column))
          primaryColumns.add(column);

      equalsColumns = primaryColumns.size() > 0 ? primaryColumns : table._column();
      out += "      final " + entityName + " that = (" + entityName + ")obj;\n";
      for (final $ddlx_column column : equalsColumns)
        eq += " && (this." + Strings.toInstanceCase(column._name$().text()) + ".get() != null ? this." + Strings.toInstanceCase(column._name$().text()) + ".get().equals(that." + Strings.toInstanceCase(column._name$().text()) + ".get()) : that." + Strings.toInstanceCase(column._name$().text()) + ".get() == null)";

      out += "      return " + eq.substring(4) + ";";
    }
    else {
      equalsColumns = null;
      out += "      return true;";
    }
    out += "\n    }";

    eq = "";
    if (equalsColumns != null && equalsColumns.size() > 0) {
      out += "\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    public int hashCode() {\n";
      for (final $ddlx_column column : equalsColumns)
        eq += " + (this." + Strings.toInstanceCase(column._name$().text()) + ".get() != null ? this." + Strings.toInstanceCase(column._name$().text()) + ".get().hashCode() : -1)";
      out += "      return " + eq.substring(3) + ";";
      out += "\n    }";
    }

    out += "\n\n";
    out += "    @" + Classes.getStrictName(Override.class) + "\n";
    out += "    public " + Classes.getStrictName(String.class) + " toString() {\n";
    out += "      final " + Classes.getStrictName(StringBuilder.class) + " builder = new " + Classes.getStrictName(StringBuilder.class) + "(super.toString());\n";
    out += "      if (builder.charAt(builder.length() - 1) == '}')\n";
    out += "        builder.setLength(builder.length() - 1);\n";
    out += "      else\n";
    out += "        builder.append(\" {\\n\");\n\n";
    if (table._column() != null)
      for (final $ddlx_column column : table._column())
        out += "      builder.append(\"  " + Strings.toInstanceCase(column._name$().text()) + ": \").append(" + Strings.toInstanceCase(column._name$().text()) + ").append(\"\\n\");\n";
    out += "      return builder.append('}').toString();";
    out += "\n    }";

    out += "\n  }";

    return out;
  }

  public String makeParam(final $ddlx_table table, final $ddlx_column column) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final Type type = getType(table, column);
    final String rawType;
    if (column instanceof $ddlx_enum)
      rawType = Strings.toTitleCase(column._name$().text());
    else
      rawType = Classes.getStrictName((Class<?>)Classes.getGenericSuperclasses(type.type)[0]);

    return "final " + rawType + " " + columnName;
  }

  public String makeColumn(final $ddlx_table table, final $ddlx_column column, final boolean isLast) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final String typeName = Strings.toTitleCase(column._name$().text());
    final StringBuilder builder = new StringBuilder();
    final Type type = getType(table, column);
    if (column instanceof $ddlx_enum) {
      builder.append("\n    public static enum ").append(typeName).append(" implements ").append(EntityEnum.class.getName()).append(" {");
      final StringBuilder enums = new StringBuilder();
      final List<String> values = Dialect.parseEnum((($ddlx_enum)column)._values$().text());
      for (final String value : values)
        enums.append(", ").append(value.toUpperCase().replace(' ', '_')).append("(\"").append(value).append("\")");

      builder.append("\n      ").append(enums.substring(2)).append(";\n\n");
      builder.append("      private final " + String.class.getName() + " table = \"" + table._name$().text() + "\";\n      private final " + String.class.getName() + " column = \"" + column._name$().text() + "\";\n      private final " + String.class.getName() + " value;\n\n      " + typeName + "(final " + String.class.getName() + " value) {\n        this.value = value;\n      }\n\n      @" + Override.class.getName() + "\n      public " + String.class.getName() + " table() {\n        return table;\n      }\n\n      @" + Override.class.getName() + "\n      public " + String.class.getName() + " column() {\n        return column;\n      }\n\n      @" + Override.class.getName() + "\n      public " + String.class.getName() + " toString() {\n        return value;\n      }\n    }");
    }

    return builder.append("\n    public final ").append(type.getType()).append(" ").append(columnName).append(" = ").append(type).append(";").toString();
  }
}