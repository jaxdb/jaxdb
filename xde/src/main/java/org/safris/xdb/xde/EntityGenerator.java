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

package org.safris.xdb.xde;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Types;

import org.safris.commons.lang.Resources;
import org.safris.commons.lang.Strings;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.xdl.$xdl_bigint;
import org.safris.xdb.xdl.$xdl_blob;
import org.safris.xdb.xdl.$xdl_boolean;
import org.safris.xdb.xdl.$xdl_char;
import org.safris.xdb.xdl.$xdl_columnType;
import org.safris.xdb.xdl.$xdl_date;
import org.safris.xdb.xdl.$xdl_dateTime;
import org.safris.xdb.xdl.$xdl_decimal;
import org.safris.xdb.xdl.$xdl_enum;
import org.safris.xdb.xdl.$xdl_float;
import org.safris.xdb.xdl.$xdl_int;
import org.safris.xdb.xdl.$xdl_mediumint;
import org.safris.xdb.xdl.$xdl_namedType;
import org.safris.xdb.xdl.$xdl_smallint;
import org.safris.xdb.xdl.$xdl_tableType;
import org.safris.xdb.xdl.$xdl_time;
import org.safris.xdb.xdl.$xdl_timestamp;
import org.safris.xdb.xdl.$xdl_tinyint;
import org.safris.xdb.xdl.$xdl_varchar;
import org.safris.xdb.xdl.xdl_database;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.w3.x2001.xmlschema.$xs_anySimpleType;
import org.xml.sax.InputSource;

public class EntityGenerator {
  public static void go(final String[] args) throws Exception {
    final xdl_database database = (xdl_database)Bindings.parse(new InputSource(Resources.getResource("/entity.xdl").getURL().openStream()));
    final File dir = new File("target/generated-test-sources/xde");
    final String pkg = "xdb.xde";

    final File outDir = new File(dir, pkg.replace('.', '/'));
    if (!outDir.exists())
      if (!outDir.mkdirs())
        throw new Error("Unable to create output dir: " + outDir.getAbsolutePath());

    final String classSimpleName = Strings.toTitleCase(database._name$().text());
    String code = "package xdb.xde;\n\n";
    code += "public class " + classSimpleName + " implements " + Database.class.getName() + " {\n";
    String tables = "";
    for (final $xdl_tableType table : database._table())
      tables += "\n\n" + makeTable(table);

    code += tables.substring(2) + "\n}";

    final File javaFile = new File(outDir, classSimpleName + ".java");
    final FileOutputStream out = new FileOutputStream(javaFile);
    out.write(code.getBytes());
    out.close();
  }

  private static class Type {
    private static Type getType(final $xdl_columnType column) {
      final Class<?> cls = column.getClass().getSuperclass();
      if (column instanceof $xdl_bigint)
        return new Type(BigInteger.class.getName(), Types.class.getName() + ".BIGINT");

      if (column instanceof $xdl_blob)
        return new Type("byte[]", Types.class.getName() + ".BLOB");

      if (column instanceof $xdl_boolean)
        return new Type(Boolean.class.getName(), Types.class.getName() + ".BOOLEAN");

      if (column instanceof $xdl_char)
        return new Type(Character.class.getName(), Types.class.getName() + ".CHAR");

      if (column instanceof $xdl_date)
        return new Type(Date.class.getName(), Types.class.getName() + ".DATE");

      if (column instanceof $xdl_dateTime)
        return new Type(Date.class.getName(), Types.class.getName() + ".DATE");

      if (column instanceof $xdl_decimal)
        return new Type(Double.class.getName(), Types.class.getName() + ".DECIMAL");

      if (column instanceof $xdl_enum)
        return new Type(Strings.toTitleCase(column._name$().text()), Types.class.getName() + ".VARCHAR");

      if (column instanceof $xdl_float)
        return new Type(Float.class.getName(), Types.class.getName() + ".FLOAT");

      if (column instanceof $xdl_int)
        return new Type(Integer.class.getName(), Types.class.getName() + ".INTEGER");

      if (column instanceof $xdl_mediumint)
        return new Type(Integer.class.getName(), Types.class.getName() + ".INTEGER");

      if (column instanceof $xdl_smallint)
        return new Type(Short.class.getName(), Types.class.getName() + ".SMALLINT");

      if (column instanceof $xdl_time)
        return new Type(Time.class.getName(), Types.class.getName() + ".TIME");

      if (column instanceof $xdl_timestamp)
        return new Type(Date.class.getName(), Types.class.getName() + ".TIMESTAMP");

      if (column instanceof $xdl_tinyint)
        return new Type(Short.class.getName(), Types.class.getName() + ".TINYINT");

      if (column instanceof $xdl_varchar)
        return new Type(String.class.getName(), Types.class.getName() + ".VARCHAR");

      throw new IllegalArgumentException("Unknown type: " + cls);
    }

    public final String type;
    public final String sqlType;

    public Type(final String type, final String sqlType) {
      this.type = type;
      this.sqlType = sqlType;
    }
  }

  public static String getDefault(final $xdl_columnType column) {
    try {
      if (column instanceof $xdl_blob /* FIXME: and TEXT too */)
        return null;

      final Method method = Classes.getDeclaredMethodDeep(column.getClass(), "_default$");
      final $xs_anySimpleType value = ($xs_anySimpleType)method.invoke(column);
      if (value.isNull())
        return null;

      if (column instanceof $xdl_varchar)
        return "\"" + value.text() + "\"";

      if (column instanceof $xdl_enum)
        return Strings.toTitleCase(column._name$().text()) + "." + String.valueOf(value.text());

      return String.valueOf(value.text());
    }
    catch (final Exception e) {
      throw new Error(e);
    }
  }

  public static String makeTable(final $xdl_tableType table) {
    final String ext = !table._extends$().isNull() ? Strings.toTitleCase(table._extends$().text()) : Entity.class.getName();
    String out = "";
    String abs = "";
    if (table._abstract$().text())
      abs = table._abstract$().text() ? " abstract" : "";
    else
      out += "  @" + Table.class.getName() + "(name = \"" + table._name$().text() + "\")\n";

    out += "  public static" + abs + " class " + Strings.toTitleCase(table._name$().text()) + " extends " + ext + " {\n";
    String columns = "";
    for (final $xdl_columnType column : table._column())
      columns += "\n\n" + makeColumn(table, column);

    out += columns.substring(2);
    out += "\n  }";
    return out;
  }

  private static boolean isPrimary(final $xdl_tableType table, final $xdl_columnType column) {
    final $xdl_tableType._constraints._primaryKey constraint = table._constraints(0)._primaryKey(0);
    if (constraint.isNull())
      return false;

    for (final $xdl_namedType col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }

  /*private static boolean isUnique(final $xdl_tableType table, final $xdl_columnType column) {
    final $xdl_tableType._constraints._unique constraint = table._constraints(0)._unique(0);
    if (constraint.isNull())
      return false;

    for (final $xdl_namedType col : constraint._column())
      if (column._name$().text().equals(col._name$().text()))
        return true;

    return false;
  }*/

  public static String makeColumn(final $xdl_tableType table, final $xdl_columnType column) {
    final String instanceName = Strings.toCamelCase(column._name$().text());
    final String typeName = Strings.toTitleCase(column._name$().text());
    String out = "";
    final Type type = Type.getType(column);
    if (column instanceof $xdl_enum) {
      out += "    public static enum " + typeName + " {\n";
      String values = "";
      for (final String value : (($xdl_enum)column)._values$().text())
        values += ", " + value;

      out += "      " + values.substring(2);
      out += "\n    }\n\n";
    }

    final String _default = getDefault(column);
    final String primary = isPrimary(table, column) ? ", primary = true" : "";
    //final String unique = isUnique(table, column) ? ", unique = true" : "";
    //final String nullable = !column._null$().text() ? ", nullable = false" : "";
    out += "    @" + Column.class.getName() + "(name = \"" + column._name$().text() + "\", type = " + type.sqlType + "" + primary + /*unique + nullable + */")\n";
    out += "    private " + type.type + " " + instanceName;
    out += _default != null ? " = " + _default + ";\n\n" : ";\n\n";
    out += "    public void set" + typeName + "(final " + type.type + " value) {\n";
    out += "      this." + instanceName + " = value;\n    }\n\n";
    out += "    public " + type.type + " get" + typeName + "() {\n";
    out += "      return " + instanceName + ";\n    }";

    return out;
  }
}