/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.entities.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.safris.commons.jci.CompilationException;
import org.safris.commons.jci.JavaCompiler;
import org.safris.commons.lang.ClassLoaders;
import org.safris.commons.lang.Strings;
import org.safris.commons.lang.reflect.Classes;
import org.safris.commons.xml.XMLException;
import org.safris.maven.common.Log;
import org.safris.xdb.entities.DataType;
import org.safris.xdb.entities.Entity;
import org.safris.xdb.entities.GenerateOn;
import org.safris.xdb.entities.Schema;
import org.safris.xdb.entities.datatype.BigInt;
import org.safris.xdb.entities.datatype.Binary;
import org.safris.xdb.entities.datatype.Blob;
import org.safris.xdb.entities.datatype.Char;
import org.safris.xdb.entities.datatype.Clob;
import org.safris.xdb.entities.datatype.DateTime;
import org.safris.xdb.entities.datatype.Decimal;
import org.safris.xdb.entities.datatype.MediumInt;
import org.safris.xdb.entities.datatype.SmallInt;
import org.safris.xdb.entities.datatype.Time;
import org.safris.xdb.schema.SQLDataTypes;
import org.safris.xdb.schema.spec.SQLSpec;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_columns;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;
import org.safris.xdb.xds.xe.xds_schema;
import org.safris.xsb.runtime.Bindings;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
import org.xml.sax.InputSource;

public class Generator {
  private static final Map<String,$xds_table> tableNameToTable = new HashMap<String,$xds_table>();

  public static void generate(final URL url, final File destDir, final boolean compile) throws IOException, XMLException {
    final xds_schema schema = (xds_schema)Bindings.parse(new InputSource(url.openStream()));
    Log.info("Generating database entities: " + schema._name$().text());
    for (final $xds_table table : schema._table())
      tableNameToTable.put(table._name$().text(), table);

    final String pkg = "xdb.ddl";

    final File dir = new File(destDir, pkg.replace('.', '/'));
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());

    final String classSimpleName = Strings.toInstanceCase(schema._name$().text());

    String code = "package " + pkg + ";\n\n";
    code += "public final class " + classSimpleName + " extends " + Schema.class.getName() + " {\n";

    String tables = "";
    // First create the abstract entities
    for (final $xds_table table : schema._table())
      if (table._abstract$().text())
        tables += "\n\n" + makeTable(table);

    // Then, in proper inheritance order, the real entities
    for (final $xds_table table : schema._table())
      if (!table._abstract$().text())
        tables += "\n\n" + makeTable(table);

    code += tables.substring(2) + "\n\n";

    /*code += "  private " + String.class.getName() + " name = \"" + classSimpleName + "\";\n\n";

    code += "  public boolean equals(final " + Object.class.getName() + " obj) {\n";
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

  private static class Type {
    private static Type getType(final $xds_table table, final $xds_column column) {
      final Class<?> cls = column.getClass().getSuperclass();
      final Object _default = getDefault(column);
      GenerateOn<?> generateOnInsert = null;
      GenerateOn<?> generateOnUpdate = null;
      final Object[] params = new Object[] {THIS, Strings.toInstanceCase(column._name$().text()), column._name$().text(), _default, isUnique(table, column), isPrimary(table, column), column._null$().text()};
      if (column instanceof $xds_char) {
        final $xds_char type = ($xds_char)column;
        if (!type.xde_generateOnInsert$().isNull() && $xds_char.xde_generateOnInsert$.UUID.text().equals(type.xde_generateOnInsert$().text()))
          generateOnInsert = GenerateOn.UUID;

        return new Type(column, Char.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._varying$().text(), type._national$().text());
      }

      if (column instanceof $xds_clob) {
        final $xds_clob type = ($xds_clob)column;
        return new Type(column, Clob.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._national$().text());
      }

      if (column instanceof $xds_binary) {
        final $xds_binary type = ($xds_binary)column;
        return new Type(column, Binary.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._varying$().text());
      }

      if (column instanceof $xds_blob) {
        final $xds_blob type = ($xds_blob)column;
        return new Type(column, Blob.class, params, generateOnInsert, generateOnUpdate, type._length$().text());
      }

      if (column instanceof $xds_integer) {
        final $xds_integer type = ($xds_integer)column;
        // no autogenerator is necessary for xds_integer._generateOnInsert$.AUTO_5FINCREMENT
        if (!type.xde_generateOnUpdate$().isNull())
          if ($xds_integer.xde_generateOnUpdate$.INCREMENT.text().equals(type.xde_generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.INCREMENT;

        final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
        if (noBytes <= 2)
          return new Type(column, SmallInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().isNull() ? null : new Short(type._min$().text().shortValue()), type._max$().isNull() ? null : new Short(type._max$().text().shortValue()));

        if (noBytes <= 4)
          return new Type(column, MediumInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());

        if (noBytes <= 8)
          return new Type(column, org.safris.xdb.entities.datatype.Long.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());

        return new Type(column, BigInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
      }

      if (column instanceof $xds_float) {
        final $xds_float type = ($xds_float)column;
        final Class<? extends DataType<?>> javaType;
        final Number min;
        final Number max;
        if (type._double$().text()) {
          javaType = org.safris.xdb.entities.datatype.Double.class;
          min = type._min$().text() != null ? type._min$().text().doubleValue() : null;
          max = type._max$().text() != null ? type._max$().text().doubleValue() : null;
        }
        else {
          javaType = org.safris.xdb.entities.datatype.Float.class;
          min = type._min$().text() != null ? type._min$().text().floatValue() : null;
          max = type._max$().text() != null ? type._max$().text().floatValue() : null;
        }

        return new Type(column, javaType, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), min, max);
      }

      if (column instanceof $xds_decimal) {
        final $xds_decimal type = ($xds_decimal)column;
        return new Type(column, Decimal.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._decimal$().text(), type._unsigned$().text(), type._min$().text() != null ? type._min$().text().doubleValue() : null, type._max$().text() != null ? type._max$().text().doubleValue() : null);
      }

      if (column instanceof $xds_date) {
        final $xds_date type = ($xds_date)column;
        if (!type.xde_generateOnInsert$().isNull())
          if ($xds_date.xde_generateOnInsert$.TIMESTAMP.text().equals(type.xde_generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type.xde_generateOnUpdate$().isNull())
          if ($xds_date.xde_generateOnUpdate$.TIMESTAMP.text().equals(type.xde_generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, org.safris.xdb.entities.datatype.Date.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xds_time) {
        final $xds_time type = ($xds_time)column;
        if (!type.xde_generateOnInsert$().isNull())
          if ($xds_time.xde_generateOnInsert$.TIMESTAMP.text().equals(type.xde_generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type.xde_generateOnUpdate$().isNull())
          if ($xds_time.xde_generateOnUpdate$.TIMESTAMP.text().equals(type.xde_generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, Time.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xds_dateTime) {
        final $xds_dateTime type = ($xds_dateTime)column;
        if (!type.xde_generateOnInsert$().isNull())
          if ($xds_dateTime.xde_generateOnInsert$.TIMESTAMP.text().equals(type.xde_generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type.xde_generateOnUpdate$().isNull())
          if ($xds_dateTime.xde_generateOnUpdate$.TIMESTAMP.text().equals(type.xde_generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, DateTime.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xds_boolean) {
        return new Type(column, org.safris.xdb.entities.datatype.Boolean.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xds_enum) {
        return new Type(column, org.safris.xdb.entities.datatype.Enum.class, params, generateOnInsert, generateOnUpdate);
      }

      throw new IllegalArgumentException("Unknown type: " + cls);
    }

    private final $xds_column column;
    @SuppressWarnings("rawtypes")
    public final Class<? extends DataType> type;
    private final Object[] commonParams;
    private final GenerateOn<?> generateOnInsert;
    private final GenerateOn<?> generateOnUpdate;
    private final Object[] customParams;

    @SuppressWarnings("rawtypes")
    private Type(final $xds_column column, final Class<? extends DataType> type, final Object[] commonParams, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
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
        out += ", " + (param == THIS ? "this" : Serializer.serialize(param));

      out += ", " + Serializer.serialize(generateOnInsert);
      out += ", " + Serializer.serialize(generateOnUpdate);
      if (customParams != null)
        for (final Object param : customParams)
          out += ", " + (param == THIS ? "this" : Serializer.serialize(param));

      return out.substring(2);
    }

    public String getType() {
      return type.getName() + (type == org.safris.xdb.entities.datatype.Enum.class ? "<" + Strings.toTitleCase(column._name$().text()) + ">" : "");
    }

    @Override
    public String toString() {
      return "new " + getType() + "(" + serializeParams() + (type == org.safris.xdb.entities.datatype.Enum.class ? ", " + Strings.toTitleCase(column._name$().text()) + ".class" : "") + ")";
    }
  }

  public static Object getDefault(final $xds_column column) {
    try {
      final Method method = Classes.getDeclaredMethodDeep(column.getClass(), "_default$");
      if (method == null)
        return null;

      final $xs_anySimpleType value = ($xs_anySimpleType)method.invoke(column);
      if (value.isNull() || "null".equals(value.text()))
        return null;

      if (column instanceof $xds_enum)
        return Strings.toTitleCase(column._name$().text()) + "." + String.valueOf(value.text());

      if (column instanceof $xds_integer) {
        final $xds_integer type = ($xds_integer)column;
        final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
        if (noBytes <= 2)
          return Short.valueOf(String.valueOf(value.text()));

        if (4 < noBytes && noBytes <= 8)
          return java.lang.Long.valueOf(String.valueOf(value.text()));

        return new BigInteger(String.valueOf(value.text()));
      }

      return value.text();
    }
    catch (final Exception e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private static int getColumnCount($xds_table table, final boolean deep) {
    int count = 0;
    do {
      count += table._column() != null ? table._column().size() : 0;
    }
    while (deep && (table = tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  private static int getPrimaryColumnCount($xds_table table, final boolean deep) {
    int count = 0;
    do {
      if (!table._constraints(0)._primaryKey(0).isNull())
        count += table._constraints(0)._primaryKey(0)._column().size();
    }
    while (deep && (table = tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  public static String makeTable(final $xds_table table) {
    final String ext = !table._extends$().isNull() ? Strings.toTitleCase(table._extends$().text()) : Entity.class.getName();
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
      out += "    protected final " + DataType.class.getName() + "<?>[] column;\n";
      out += "    protected final " + DataType.class.getName() + "<?>[] primary;\n\n";
      out += "    @" + Override.class.getName() + "\n";
      out += "    protected " + DataType.class.getName() + "<?>[] column() {\n";
      out += "      return column;\n";
      out += "    }\n\n";
      out += "    @" + Override.class.getName() + "\n";
      out += "    protected " + DataType.class.getName() + "<?>[] primary() {\n";
      out += "      return primary;\n";
      out += "    }\n\n";
      out += "    @" + Override.class.getName() + "\n";
      out += "    protected " + String.class.getName() + " name() {\n";
      out += "      return \"" + table._name$().text() + "\";\n";
      out += "    }\n\n";
      out += "    @" + Override.class.getName() + "\n";
      out += "    protected " + entityName + " newInstance() {\n";
      out += "      return new " + entityName + "(true);\n";
      out += "    }\n\n";
      out += "    public " + Strings.toTitleCase(table._name$().text()) + "() {\n";
      out += "      this(false, new " + DataType.class.getName() + "[" + totalColumnCount + "], new " + DataType.class.getName() + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";
      out += "    protected " + Strings.toTitleCase(table._name$().text()) + "(final boolean wasSelected) {\n";
      out += "      this(wasSelected, new " + DataType.class.getName() + "[" + totalColumnCount + "], new " + DataType.class.getName() + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";

      // Constructor with primary key columns
      String set = "";
      if (table._column() != null && totalPrimaryCount > 0) {
        out += "    public " + Strings.toTitleCase(table._name$().text()) + "(";
        $xds_table t = table;
        String params = "";
        do {
          for (int i = 0; i < t._column().size(); i++) {
            final $xds_column column = t._column().get(i);
            if (isPrimary(table, column)) {
              params += ", " + makeParam(t, column);
              final String columnName = Strings.toCamelCase(column._name$().text());
              set += "\n      this." + columnName + ".set(" + columnName + ");";
            }
          }
        }
        while (!t._extends$().isNull() && (t = tableNameToTable.get(t._extends$().text())) != null);
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
          final $xds_column column = table._column().get(i);
          final String columnName = Strings.toCamelCase(column._name$().text());
          set += "\n      this." + columnName + ".set(copy." + columnName + ".get());";
        }

        out += set.substring(1) + "\n";
      }

      out += "    }\n\n";
    }

    String defs = "";
    out += "    protected " + Strings.toTitleCase(table._name$().text()) + "(final boolean wasSelected, final " + DataType.class.getName() + "<?>[] column, final " + DataType.class.getName() + "<?>[] primary) {\n";
    out += "      super(wasSelected, column, primary);\n";
    if (!table._abstract$().text()) {
      out += "      this.column = column;\n";
      out += "      this.primary = primary;\n";
    }

    defs = "";
    int primaryIndex = 0;
    if (table._column() != null) {
      for (int i = 0; i < table._column().size(); i++) {
        final $xds_column column = table._column().get(i);
        final String columnName = Strings.toCamelCase(column._name$().text());
        defs += "\n      column[" + (totalColumnCount - (table._column().size() - i)) + "] = " + (isPrimary(table, column) ? "primary[" + (totalPrimaryCount - (localPrimaryCount - primaryIndex++)) + "] = " : "") + columnName + ";";
      }

      out += defs.substring(1) + "\n";
    }

    out += "    }\n";

    if (table._column() != null) {
      for (int i = 0; i < table._column().size(); i++) {
        final $xds_column column = table._column().get(i);
        out += makeColumn(table, column, i == table._column().size());
      }

      out += "\n";
    }

    out += "\n";
    out += "    @" + Override.class.getName() + "\n";
    out += "    public boolean equals(final " + Object.class.getName() + " obj) {\n";
    out += "      if (obj == this)\n        return true;\n\n";
    out += "      if (!(obj instanceof " + entityName + ")" + (!table._extends$().isNull() ? " || !super.equals(obj)" : "") + ")\n        return false;\n\n";

    String eq = "";
    final List<$xds_column> primaryColumns = new ArrayList<$xds_column>();
    final List<$xds_column> equalsColumns;
    if (table._column() != null) {
      for (final $xds_column column : table._column())
        if (isPrimary(table, column))
          primaryColumns.add(column);

      equalsColumns = primaryColumns.size() > 0 ? primaryColumns : table._column();
      out += "      final " + entityName + " that = (" + entityName + ")obj;\n";
      for (final $xds_column column : equalsColumns)
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
      out += "    @" + Override.class.getName() + "\n";
      out += "    public int hashCode() {\n";
      for (final $xds_column column : equalsColumns)
        eq += " + (this." + Strings.toInstanceCase(column._name$().text()) + ".get() != null ? this." + Strings.toInstanceCase(column._name$().text()) + ".get().hashCode() : -1)";
      out += "      return " + eq.substring(3) + ";";
      out += "\n    }";
    }

    out += "\n\n";
    out += "    @" + Override.class.getName() + "\n";
    out += "    public " + String.class.getName() + " toString() {\n";
    out += "      final " + StringBuilder.class.getName() + " builder = new " + StringBuilder.class.getName() + "(super.toString());\n";
    out += "      if (builder.charAt(builder.length() - 1) == '}')\n";
    out += "        builder.setLength(builder.length() - 1);\n";
    out += "      else\n";
    out += "        builder.append(\" {\\n\");\n\n";
    for (final $xds_column column : table._column())
      out += "      builder.append(\"  " + Strings.toInstanceCase(column._name$().text()) + ": \").append(" + Strings.toInstanceCase(column._name$().text()) + ").append(\"\\n\");\n";
    out += "      return builder.append('}').toString();";
    out += "\n    }";

    out += "\n  }";

    return out;
  }

  private static boolean isPrimary($xds_table table, final $xds_column column) {
    do {
      final $xds_columns constraint = table._constraints(0)._primaryKey(0);
      if (!constraint.isNull())
        for (final $xds_named col : constraint._column())
          if (column._name$().text().equals(col._name$().text()))
            return true;
    }
    while (!table._extends$().isNull() && (table = tableNameToTable.get(table._extends$().text())) != null);

    return false;
  }

  private static boolean isUnique(final $xds_table table, final $xds_column column) {
    final $xds_columns constraint = table._constraints(0)._unique(0);
    if (constraint.isNull())
      return false;

    for (final $xds_named col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }

  public static String makeParam(final $xds_table table, final $xds_column column) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final Type type = Type.getType(table, column);
    final String rawType;
    if (column instanceof $xds_enum)
      rawType = Strings.toTitleCase(column._name$().text());
    else
      rawType = Classes.getName((Class<?>)Classes.getGenericSuperclasses(type.type)[0]);

    return "final " + rawType + " " + columnName;
  }

  public static String makeColumn(final $xds_table table, final $xds_column column, final boolean isLast) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final String typeName = Strings.toTitleCase(column._name$().text());
    final StringBuilder builder = new StringBuilder();
    final Type type = Type.getType(table, column);
    if (column instanceof $xds_enum) {
      builder.append("\n    public static enum ").append(typeName).append(" {");
      final StringBuilder enums = new StringBuilder();
      final List<String> values = SQLSpec.parseEnum((($xds_enum)column)._values$().text());
      for (final String value : values)
        enums.append(", ").append(value.toUpperCase().replace(' ', '_'));

      builder.append(enums.substring(2)).append("}");
    }

    return builder.append("\n    public final ").append(type.getType()).append(" ").append(columnName).append(" = ").append(type).append(";").toString();
  }
}