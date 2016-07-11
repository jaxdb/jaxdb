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

package org.safris.xdb.xde.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.safris.commons.lang.Strings;
import org.safris.commons.lang.reflect.Classes;
import org.safris.commons.xml.XMLException;
import org.safris.xdb.xde.DataType;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xde.Schema;
import org.safris.xdb.xde.datatype.BigInt;
import org.safris.xdb.xde.datatype.Bit;
import org.safris.xdb.xde.datatype.Blob;
import org.safris.xdb.xde.datatype.Char;
import org.safris.xdb.xde.datatype.DateTime;
import org.safris.xdb.xde.datatype.Decimal;
import org.safris.xdb.xde.datatype.MediumInt;
import org.safris.xdb.xde.datatype.SmallInt;
import org.safris.xdb.xdl.DDL;
import org.safris.xdb.xdl.SQLDataTypes;
import org.safris.xdb.xdl.xe.$xdl_bit;
import org.safris.xdb.xdl.xe.$xdl_blob;
import org.safris.xdb.xdl.xe.$xdl_boolean;
import org.safris.xdb.xdl.xe.$xdl_char;
import org.safris.xdb.xdl.xe.$xdl_column;
import org.safris.xdb.xdl.xe.$xdl_date;
import org.safris.xdb.xdl.xe.$xdl_dateTime;
import org.safris.xdb.xdl.xe.$xdl_decimal;
import org.safris.xdb.xdl.xe.$xdl_enum;
import org.safris.xdb.xdl.xe.$xdl_float;
import org.safris.xdb.xdl.xe.$xdl_integer;
import org.safris.xdb.xdl.xe.$xdl_named;
import org.safris.xdb.xdl.xe.$xdl_table;
import org.safris.xdb.xdl.xe.$xdl_time;
import org.safris.xdb.xdl.xe.xdl_database;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
import org.xml.sax.InputSource;

public class EntityGenerator {
  private static final Map<String,$xdl_table> tableNameToTable = new HashMap<String,$xdl_table>();

  public static void generate(final URL url, final File outDir) throws IOException, XMLException {
    final xdl_database database = (xdl_database)Bindings.parse(new InputSource(url.openStream()));
    for (final $xdl_table table : database._table())
      tableNameToTable.put(table._name$().text(), table);

    final String pkg = "xdb.xde";

    final File dir = new File(outDir, pkg.replace('.', '/'));
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new Error("Unable to create output dir: " + dir.getAbsolutePath());

    final String classSimpleName = database._name$().text();

    String code = "package " + pkg + ";\n\n";
    code += "public final class " + classSimpleName + " extends " + Schema.class.getName() + " {\n";

    String tables = "";
    // First create the abstract entities
    for (final $xdl_table table : database._table())
      if (table._abstract$().text())
        tables += "\n\n" + makeTable(table);

    // Then, in proper inheritance order, the real entities
    for (final $xdl_table table : database._table())
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
  }

  private static final Object THIS = new Object();

  private static class Type {
    private static Type getType(final $xdl_table table, final $xdl_column column) {
      final Class<?> cls = column.getClass().getSuperclass();
      final Object _default = getDefault(column);
      GenerateOn<?> generateOnInsert = null;
      GenerateOn<?> generateOnUpdate = null;
      final Object[] params = new Object[] {THIS, Strings.toInstanceCase(column._name$().text()), column._name$().text(), _default, isUnique(table, column), isPrimary(table, column), column._null$().text()};
      if (column instanceof $xdl_blob) {
        return new Type(column, Blob.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xdl_char) {
        final $xdl_char type = ($xdl_char)column;
        if (!type._generateOnInsert$().isNull())
          if ($xdl_char._generateOnInsert$.UUID.text().equals(type._generateOnInsert$().text()))
            generateOnInsert = GenerateOn.UUID;

        return new Type(column, Char.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._variant$().text());
      }

      if (column instanceof $xdl_bit) {
        final $xdl_bit type = ($xdl_bit)column;
        return new Type(column, Bit.class, params, generateOnInsert, generateOnUpdate, type._length$().text(), type._variant$().text());
      }

      if (column instanceof $xdl_integer) {
        final $xdl_integer type = ($xdl_integer)column;
        // no autogenerator is necessary for xdl_integer._generateOnInsert$.AUTO_5FINCREMENT
        if (!type._generateOnUpdate$().isNull())
          if ($xdl_integer._generateOnUpdate$.INCREMENT.text().equals(type._generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.INCREMENT;

        final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
        if (noBytes <= 2)
          return new Type(column, SmallInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().isNull() ? null : new Short(type._min$().text().shortValue()), type._max$().isNull() ? null : new Short(type._max$().text().shortValue()));

        if (noBytes <= 4)
          return new Type(column, MediumInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());

        if (noBytes <= 8)
          return new Type(column, org.safris.xdb.xde.datatype.Long.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());

        return new Type(column, BigInt.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
      }

      if (column instanceof $xdl_float) {
        final $xdl_float type = ($xdl_float)column;
        final Class<? extends DataType<?>> javaType;
        final Number min;
        final Number max;
        if (type._double$().text()) {
          javaType = org.safris.xdb.xde.datatype.Double.class;
          min = type._min$().text() != null ? type._min$().text().doubleValue() : null;
          max = type._max$().text() != null ? type._max$().text().doubleValue() : null;
        }
        else {
          javaType = org.safris.xdb.xde.datatype.Float.class;
          min = type._min$().text() != null ? type._min$().text().floatValue() : null;
          max = type._max$().text() != null ? type._max$().text().floatValue() : null;
        }

        return new Type(column, javaType, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._unsigned$().text(), min, max);
      }

      if (column instanceof $xdl_decimal) {
        final $xdl_decimal type = ($xdl_decimal)column;
        return new Type(column, Decimal.class, params, generateOnInsert, generateOnUpdate, type._precision$().text(), type._decimal$().text(), type._unsigned$().text(), type._min$().text() != null ? type._min$().text().doubleValue() : null, type._max$().text() != null ? type._max$().text().doubleValue() : null);
      }

      if (column instanceof $xdl_date) {
        final $xdl_date type = ($xdl_date)column;
        if (!type._generateOnInsert$().isNull())
          if ($xdl_date._generateOnInsert$.TIMESTAMP.text().equals(type._generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type._generateOnUpdate$().isNull())
          if ($xdl_date._generateOnUpdate$.TIMESTAMP.text().equals(type._generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, org.safris.xdb.xde.datatype.Date.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xdl_time) {
        final $xdl_time type = ($xdl_time)column;
        if (!type._generateOnInsert$().isNull())
          if ($xdl_time._generateOnInsert$.TIMESTAMP.text().equals(type._generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type._generateOnUpdate$().isNull())
          if ($xdl_time._generateOnUpdate$.TIMESTAMP.text().equals(type._generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, org.safris.xdb.xde.datatype.Time.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xdl_dateTime) {
        final $xdl_dateTime type = ($xdl_dateTime)column;
        if (!type._generateOnInsert$().isNull())
          if ($xdl_dateTime._generateOnInsert$.TIMESTAMP.text().equals(type._generateOnInsert$().text()))
            generateOnInsert = GenerateOn.TIMESTAMP;

        if (!type._generateOnUpdate$().isNull())
          if ($xdl_dateTime._generateOnUpdate$.TIMESTAMP.text().equals(type._generateOnUpdate$().text()))
            generateOnUpdate = GenerateOn.TIMESTAMP;

        return new Type(column, DateTime.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xdl_boolean) {
        return new Type(column, org.safris.xdb.xde.datatype.Boolean.class, params, generateOnInsert, generateOnUpdate);
      }

      if (column instanceof $xdl_enum) {
        return new Type(column, org.safris.xdb.xde.datatype.Enum.class, params, generateOnInsert, generateOnUpdate);
      }

      throw new IllegalArgumentException("Unknown type: " + cls);
    }

    private final $xdl_column column;
    @SuppressWarnings("rawtypes")
    public final Class<? extends DataType> type;
    private final Object[] commonParams;
    private final GenerateOn<?> generateOnInsert;
    private final GenerateOn<?> generateOnUpdate;
    private final Object[] customParams;

    @SuppressWarnings("rawtypes")
    private Type(final $xdl_column column, final Class<? extends DataType> type, final Object[] commonParams, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
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
      return type.getName() + (type == org.safris.xdb.xde.datatype.Enum.class ? "<" + Strings.toTitleCase(column._name$().text()) + ">" : "");
    }

    @Override
    public String toString() {
      return "new " + getType() + "(" + serializeParams() + (type == org.safris.xdb.xde.datatype.Enum.class ? ", " + Strings.toTitleCase(column._name$().text()) + ".class" : "") + ")";
    }
  }

  public static Object getDefault(final $xdl_column column) {
    try {
      if (column instanceof $xdl_blob)
        return null;

      final Method method = Classes.getDeclaredMethodDeep(column.getClass(), "_default$");
      final $xs_anySimpleType value = ($xs_anySimpleType)method.invoke(column);
      if (value.isNull() || "null".equals(value.text()))
        return null;

      if (column instanceof $xdl_enum)
        return Strings.toTitleCase(column._name$().text()) + "." + String.valueOf(value.text());

      if (column instanceof $xdl_integer) {
        final $xdl_integer type = ($xdl_integer)column;
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
      throw new Error(e);
    }
  }

  private static int getColumnCount($xdl_table table, final boolean deep) {
    int count = 0;
    do {
      count += table._column() != null ? table._column().size() : 0;
    }
    while (deep && (table = tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  private static int getPrimaryColumnCount($xdl_table table, final boolean deep) {
    int count = 0;
    do {
      if (!table._constraints(0)._primaryKey(0).isNull())
        count += table._constraints(0)._primaryKey(0)._column().size();
    }
    while (deep && (table = tableNameToTable.get(table._extends$().text())) != null);
    return count;
  }

  public static String makeTable(final $xdl_table table) {
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

      // Constructor with params
      String set = "";
      if (table._column() != null) {
        out += "    public " + Strings.toTitleCase(table._name$().text()) + "(";
        String params = "";
        for (int i = 0; i < table._column().size(); i++) {
          final $xdl_column column = table._column().get(i);
          params += ", " + makeParam(table, column);
        }

        out += params.substring(2) + ") {\n";
        out += "      this();\n";
        for (int i = 0; i < table._column().size(); i++) {
          final $xdl_column column = table._column().get(i);
          final String columnName = Strings.toCamelCase(column._name$().text());
          set += "\n      this." + columnName + ".set(" + columnName + ");";
        }

        out += set.substring(1) + "\n    }\n\n";
      }

      // Copy constructor
      out += "    public " + Strings.toTitleCase(table._name$().text()) + "(final " + Strings.toTitleCase(table._name$().text()) + " copy) {\n";
      out += "      this();\n";
      set = "";
      if (table._column() != null) {
        for (int i = 0; i < table._column().size(); i++) {
          final $xdl_column column = table._column().get(i);
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
        final $xdl_column column = table._column().get(i);
        final String columnName = Strings.toCamelCase(column._name$().text());
        defs += "\n      column[" + (totalColumnCount - (table._column().size() - i)) + "] = " + (isPrimary(table, column) ? "primary[" + (totalPrimaryCount - (localPrimaryCount - primaryIndex++)) + "] = " : "") + columnName + ";";
      }

      out += defs.substring(1) + "\n";
    }

    out += "    }\n";

    if (table._column() != null) {
      for (int i = 0; i < table._column().size(); i++) {
        final $xdl_column column = table._column().get(i);
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
    final List<$xdl_column> primaryColumns = new ArrayList<$xdl_column>();
    final List<$xdl_column> equalsColumns;
    if (table._column() != null) {
      for (final $xdl_column column : table._column())
        if (isPrimary(table, column))
          primaryColumns.add(column);

      equalsColumns = primaryColumns.size() > 0 ? primaryColumns : table._column();
      out += "      final " + entityName + " that = (" + entityName + ")obj;\n";
      for (final $xdl_column column : equalsColumns)
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
      for (final $xdl_column column : equalsColumns)
        eq += " + (this." + Strings.toInstanceCase(column._name$().text()) + ".get() != null ? this." + Strings.toInstanceCase(column._name$().text()) + ".get().hashCode() : -1)";
      out += "      return " + eq.substring(3) + ";";
      out += "\n    }";
    }

    out += "\n  }";

    return out;
  }

  private static boolean isPrimary(final $xdl_table table, final $xdl_column column) {
    final $xdl_table._constraints._primaryKey constraint = table._constraints(0)._primaryKey(0);
    if (constraint.isNull())
      return false;

    for (final $xdl_named col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }

  private static boolean isUnique(final $xdl_table table, final $xdl_column column) {
    final $xdl_table._constraints._unique constraint = table._constraints(0)._unique(0);
    if (constraint.isNull())
      return false;

    for (final $xdl_named col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }

  private static String serialize(final DDL[] ddls) {
    String out = "";
    for (final DDL ddl : ddls)
      out += ", new " + DDL.class.getName() + "(" + Serializer.serialize(ddl.name) + ", " + Serializer.serialize(ddl.drop) + ", " + Serializer.serialize(ddl.create) + ")";

    return "new " + DDL.class.getName() + "[] {" + out.substring(2) + "}";
  }

  public static String makeParam(final $xdl_table table, final $xdl_column column) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final Type type = Type.getType(table, column);
    final String rawType;
    if (column instanceof $xdl_enum)
      rawType = Strings.toTitleCase(column._name$().text());
    else
      rawType = Classes.getName((Class<?>)Classes.getGenericSuperclasses(type.type)[0]);

    return "final " + rawType + " " + columnName;
  }

  public static String makeColumn(final $xdl_table table, final $xdl_column column, final boolean isLast) {
    final String columnName = Strings.toCamelCase(column._name$().text());
    final String typeName = Strings.toTitleCase(column._name$().text());
    String out = "";
    final Type type = Type.getType(table, column);
    if (column instanceof $xdl_enum) {
      out += "\n    public static enum " + typeName + " {";
      String values = "";
      for (final String value : (($xdl_enum)column)._values$().text())
        values += ", " + value;

      out += values.substring(2) + "}";
    }

    return out + "\n    public final " + type.getType() + " " + columnName + " = " + type + ";";
  }
}