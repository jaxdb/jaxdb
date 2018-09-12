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
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fastjax.util.Classes;
import org.fastjax.util.JavaIdentifiers;
import org.fastjax.xml.ValidationException;
import org.libx4j.rdb.ddlx.DDLxAudit;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Bigint;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Binary;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Blob;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Boolean;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Char;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Clob;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Column;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Date;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Datetime;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Decimal;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Double;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Enum;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Float;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Int;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Smallint;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Time;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Tinyint;
import org.libx4j.rdb.jsql.EntityEnum;
import org.libx4j.rdb.jsql.GenerateOn;
import org.libx4j.rdb.jsql.Schema;
import org.libx4j.rdb.jsql.type;
import org.libx4j.rdb.jsql_0_9_9.xLzgluGCXYYJc.$Integer;
import org.libx4j.rdb.jsql_0_9_9.xLzgluGCXYYJc.$Table;
import org.libx4j.rdb.vendor.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Generator {
  private static final Logger logger = LoggerFactory.getLogger(Generator.class);

  private final JSQLAudit audit;

  public Generator(final URL url) throws IOException, ValidationException {
    this.audit = new JSQLAudit(new DDLxAudit(url));
  }

  public void generate(final String name, final File destDir) throws IOException {
    logger.info("Generating jSQL: " + name);

    final String pkg = type.class.getPackage().getName();

    final File dir = new File(destDir, pkg.replace('.', '/'));
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());

    final String classSimpleName = JavaIdentifiers.toInstanceCase(name);

    String code = "package " + pkg + ";\n\n";
    code += "public final class " + classSimpleName + " extends " + Classes.getStrictName(Schema.class) + " {\n";

    String tables = "";
    // First create the abstract entities
    for (final $Table table : audit.schema().getTable())
      if (table.getAbstract$().text())
        tables += "\n\n" + makeTable(table);

    // Then, in proper inheritance order, the real entities
    for (final $Table table : audit.schema().getTable())
      if (!table.getAbstract$().text())
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
    Files.write(javaFile.toPath(), code.getBytes());
  }

  private static final Object THIS = new Object();

  private Type getType(final xLzgluGCXYYJc.$Table table, final $Column column) {
    final Class<?> cls = column.getClass().getSuperclass();
    GenerateOn<?> generateOnInsert = null;
    GenerateOn<?> generateOnUpdate = null;
    final Object[] params = new Object[] {THIS, column.getName$().text(), audit.isUnique(table, column), audit.isPrimary(table, column), column.getNull$().text()};
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
      return new Type(column, type.ENUM.class, params, type.getDefault$() == null || type.getDefault$().text() == null ? null : JavaIdentifiers.toClassCase(column.getName$().text()) + "." + String.valueOf(type.getDefault$().text()), generateOnInsert, generateOnUpdate, type.getJsqlKeyForUpdate$() == null ? null : type.getJsqlKeyForUpdate$().text());
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
      final StringBuilder builder = new StringBuilder(Classes.getStrictName(type));
      if (type != type.ENUM.class)
        return builder.toString();

      builder.append('<');
      if (withGeneric)
        builder.append(JavaIdentifiers.toClassCase(column.getName$().text()));

      builder.append('>');
      return builder.toString();
    }

    @Override
    public String toString() {
      return "new " + getType(false) + "(" + compileParams() + (type == type.ENUM.class ? ", " + JavaIdentifiers.toClassCase(column.getName$().text()) + ".class" : "") + ")";
    }
  }

  private int getColumnCount(xLzgluGCXYYJc.$Table table, final boolean deep) {
    int count = 0;
    do {
      count += table.getColumn() != null ? table.getColumn().size() : 0;
    }
    while (deep && table.getExtends$() != null && (table = audit.tableNameToTable.get(table.getExtends$().text())) != null);
    return count;
  }

  private int getPrimaryColumnCount(xLzgluGCXYYJc.$Table table, final boolean deep) {
    int count = 0;
    do {
      if (table.getConstraints() != null && table.getConstraints().getPrimaryKey() != null)
        count += table.getConstraints().getPrimaryKey().getColumn().size();
    }
    while (deep && table.getExtends$() != null && (table = audit.tableNameToTable.get(table.getExtends$().text())) != null);
    return count;
  }

  public String makeTable(final $Table table) {
    final String ext = table.getExtends$() != null ? JavaIdentifiers.toClassCase(table.getExtends$().text()) : Classes.getStrictName(type.Entity.class);
    String out = "";
    String abs = "";
    if (table.getAbstract$().text())
      abs = table.getAbstract$().text() ? " abstract" : "";

    final String entityName = JavaIdentifiers.toClassCase(table.getName$().text());
    final int totalColumnCount = getColumnCount(table, true);
    final int totalPrimaryCount = getPrimaryColumnCount(table, true);
    final int localPrimaryCount = getPrimaryColumnCount(table, false);
    out += "  public static" + abs + " class " + entityName + " extends " + ext + " {\n";
    // FIXME: Gotta redesign this... right now, extended classes will all have their own copies of column and primary arrays
    if (!table.getAbstract$().text()) {
      out += "    protected static final " + entityName + " identity = new " + entityName + "();\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + Classes.getStrictName(String.class) + " name() {\n";
      out += "      return \"" + table.getName$().text() + "\";\n";
      out += "    }\n\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    protected " + entityName + " newInstance() {\n";
      out += "      return new " + entityName + "(true);\n";
      out += "    }\n\n";
      out += "    public " + entityName + "() {\n";
      out += "      this(false, new " + Classes.getStrictName(type.DataType.class) + "[" + totalColumnCount + "], new " + Classes.getStrictName(type.DataType.class) + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";
      out += "    protected " + entityName + "(final boolean wasSelected) {\n";
      out += "      this(wasSelected, new " + Classes.getStrictName(type.DataType.class) + "[" + totalColumnCount + "], new " + Classes.getStrictName(type.DataType.class) + "[" + totalPrimaryCount + "]);\n";
      out += "    }\n\n";

      // Constructor with primary key columns
      String set = "";
      if (table.getColumn() != null && totalPrimaryCount > 0) {
        out += "    public " + entityName + "(";
        xLzgluGCXYYJc.$Table t = table;
        String params = "";
        do {
          for (int i = 0; i < t.getColumn().size(); i++) {
            final $Column column = t.getColumn().get(i);
            if (audit.isPrimary(table, column)) {
              params += ", " + makeParam(t, column);
              final String columnName = JavaIdentifiers.toCamelCase(column.getName$().text());
              set += "\n      this." + columnName + ".set(" + columnName + ");";
            }
          }
        }
        while (t.getExtends$() != null && (t = audit.tableNameToTable.get(t.getExtends$().text())) != null);
        out += params.substring(2) + ") {\n";
        out += "      this();\n";
        out += set.substring(1) + "\n    }\n\n";
      }

      // Copy constructor
      if (table.getColumn() == null || table.getColumn().size() == 0)
        out += "    @" + SuppressWarnings.class.getName() + "(\"unused\")\n";

      out += "    public " + entityName + "(final " + entityName + " copy) {\n";
      out += "      this();\n";
      set = "";
      if (table.getColumn() != null) {
        for (int i = 0; i < table.getColumn().size(); i++) {
          final $Column column = table.getColumn().get(i);
          final String columnName = JavaIdentifiers.toCamelCase(column.getName$().text());
          set += "\n      this." + columnName + ".set(copy." + columnName + ".get());";
        }

        out += set.substring(1) + "\n";
      }

      out += "    }\n\n";
    }

    String defs = "";
    out += "    protected " + entityName + "(final boolean wasSelected, final " + Classes.getStrictName(type.DataType.class) + "<?>[] column, final " + Classes.getStrictName(type.DataType.class) + "<?>[] primary) {\n";
    out += "      super(wasSelected, column, primary);\n";

    defs = "";
    int primaryIndex = 0;
    if (table.getColumn() != null) {
      for (int i = 0; i < table.getColumn().size(); i++) {
        final $Column column = table.getColumn().get(i);
        final String columnName = JavaIdentifiers.toCamelCase(column.getName$().text());
        defs += "\n      column[" + (totalColumnCount - (table.getColumn().size() - i)) + "] = ";
        defs += audit.isPrimary(table, column) ? "primary[" + (totalPrimaryCount - (localPrimaryCount - primaryIndex++)) + "] = " : "";
        defs += columnName + ";";
      }

      out += defs.substring(1) + "\n";
    }

    out += "    }\n";

    if (table.getColumn() != null) {
      for (int i = 0; i < table.getColumn().size(); i++) {
        final $Column column = table.getColumn().get(i);
        out += makeColumn(table, column, i == table.getColumn().size());
      }

      out += "\n";
    }

    if (table.getAbstract$().text()) {
      out += "\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    public abstract " + entityName + " clone();\n";
    }
    else {
      out += "\n";
      out += "    @" + Classes.getStrictName(Override.class) + "\n";
      out += "    public " + entityName + " clone() {\n";
      out += "      return new " + entityName + "(this);\n";
      out += "    }\n";
    }

    out += "\n";
    out += "    @" + Classes.getStrictName(Override.class) + "\n";
    out += "    public boolean equals(final " + Classes.getStrictName(Object.class) + " obj) {\n";
    out += "      if (obj == this)\n        return true;\n\n";
    out += "      if (!(obj instanceof " + entityName + ")" + (table.getExtends$() != null ? " || !super.equals(obj)" : "") + ")\n        return false;\n\n";

    String eq = "";
    final List<$Column> primaryColumns = new ArrayList<>();
    final List<$Column> equalsColumns;
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn())
        if (audit.isPrimary(table, column))
          primaryColumns.add(column);

      equalsColumns = primaryColumns.size() > 0 ? primaryColumns : table.getColumn();
      out += "      final " + entityName + " that = (" + entityName + ")obj;\n";
      for (final $Column column : equalsColumns)
        eq += " && (this." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get() != null ? this." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get().equals(that." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get()) : that." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get() == null)";

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
      for (final $Column column : equalsColumns)
        eq += " + (this." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get() != null ? this." + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ".get().hashCode() : -1)";
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
    if (table.getColumn() != null)
      for (final $Column column : table.getColumn())
        out += "      builder.append(\"  " + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ": \").append(" + JavaIdentifiers.toInstanceCase(column.getName$().text()) + ").append(\"\\n\");\n";
    out += "      return builder.append('}').toString();";
    out += "\n    }";

    out += "\n  }";

    return out;
  }

  public String makeParam(final xLzgluGCXYYJc.$Table table, final $Column column) {
    final String columnName = JavaIdentifiers.toCamelCase(column.getName$().text());
    final Type type = getType(table, column);
    final String rawType;
    if (column instanceof $Enum)
      rawType = JavaIdentifiers.toClassCase(column.getName$().text());
    else
      rawType = Classes.getStrictName((Class<?>)Classes.getGenericSuperclasses(type.type)[0]);

    return "final " + rawType + " " + columnName;
  }

  private static final Map<Character,String> substitutions = Collections.singletonMap(' ', "_");

  public String makeColumn(final $Table table, final $Column column, final boolean isLast) {
    final String columnName = JavaIdentifiers.toCamelCase(column.getName$().text());
    final String typeName = JavaIdentifiers.toClassCase(column.getName$().text());
    final StringBuilder builder = new StringBuilder();
    final Type type = getType(table, column);
    if (column instanceof $Enum) {
      builder.append("\n    @").append(Classes.getStrictName(EntityEnum.Spec.class)).append("(table=\"").append(table.getName$().text()).append("\", column=\"").append(column.getName$().text()).append("\")");
      builder.append("\n    public static enum ").append(typeName).append(" implements ").append(EntityEnum.class.getName()).append(" {");
      final StringBuilder enums = new StringBuilder();
      final List<String> values = Dialect.parseEnum((($Enum)column).getValues$().text());
      for (final String value : values)
        enums.append(", ").append(JavaIdentifiers.toIdentifier(value, substitutions).toUpperCase().replace(' ', '_')).append("(\"").append(value).append("\")");

      builder.append("\n      ").append(enums.substring(2)).append(";\n\n");
      builder.append("      public static " + typeName + " fromString(final " + String.class.getName() + " string) {\n        if (string == null)\n          return null;\n\n        for (final " + typeName + " value : values())\n          if (string.equals(value.value))\n            return value;\n\n        return null;\n      }\n\n");
      builder.append("      private final " + String.class.getName() + " value;\n\n      " + typeName + "(final " + String.class.getName() + " value) {\n        this.value = value;\n      }\n\n");
      builder.append("      @" + Override.class.getName() + "\n      public " + String.class.getName() + " toString() {\n        return value;\n      }\n    }");
    }

    return builder.append("\n    public final ").append(type.getType(true)).append(' ').append(columnName).append(" = ").append(type).append(';').toString();
  }
}