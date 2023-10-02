/* Copyright (c) 2022 JAX-DB
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

import static org.jaxdb.jsql.generator.GeneratorUtil.*;

import java.util.Arrays;

import org.jaxdb.ddlx.DDLx;
import org.jaxdb.jsql.GenerateOn;
import org.jaxdb.jsql.data;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ColumnIndex;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.jsql_0_6.xLygluGCXAA.Cache$;
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;

final class ColumnModel {
  private static Cache$ getIndexCache(final $Column column) {
    if (column instanceof $Tinyint) {
      final $Tinyint.Index index = (($Tinyint)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Smallint) {
      final $Smallint.Index index = (($Smallint)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Int) {
      final $Int.Index index = (($Int)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Bigint) {
      final $Bigint.Index index = (($Bigint)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Float) {
      final $Float.Index index = (($Float)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Double) {
      final $Double.Index index = (($Double)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Decimal) {
      final $Decimal.Index index = (($Decimal)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Binary) {
      final $Binary.Index index = (($Binary)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Blob) {
      final $Blob.Index index = (($Blob)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Char) {
      final $Char.Index index = (($Char)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Clob) {
      final $Clob.Index index = (($Clob)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Enum) {
      final $Enum.Index index = (($Enum)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Date) {
      final $Date.Index index = (($Date)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Time) {
      final $Time.Index index = (($Time)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Datetime) {
      final $Datetime.Index index = (($Datetime)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    if (column instanceof $Boolean) {
      final $Boolean.Index index = (($Boolean)column).getIndex();
      return index == null ? null : index.getJsqlCache$();
    }

    throw new RuntimeException("Unknown column type: " + column.getClass().getName());
  }

  private static Cache$ getForeignKeyCache(final $Column column) {
    if (column instanceof $Tinyint) {
      final $Tinyint.ForeignKey foreignKey = (($Tinyint)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Smallint) {
      final $Smallint.ForeignKey foreignKey = (($Smallint)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Int) {
      final $Int.ForeignKey foreignKey = (($Int)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Bigint) {
      final $Bigint.ForeignKey foreignKey = (($Bigint)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Float) {
      final $Float.ForeignKey foreignKey = (($Float)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Double) {
      final $Double.ForeignKey foreignKey = (($Double)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Decimal) {
      final $Decimal.ForeignKey foreignKey = (($Decimal)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Binary) {
      final $Binary.ForeignKey foreignKey = (($Binary)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Blob) {
      final $Blob.ForeignKey foreignKey = (($Blob)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Char) {
      final $Char.ForeignKey foreignKey = (($Char)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Clob) {
      final $Clob.ForeignKey foreignKey = (($Clob)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Enum) {
      final $Enum.ForeignKey foreignKey = (($Enum)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Date) {
      final $Date.ForeignKey foreignKey = (($Date)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Time) {
      final $Time.ForeignKey foreignKey = (($Time)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Datetime) {
      final $Datetime.ForeignKey foreignKey = (($Datetime)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    if (column instanceof $Boolean) {
      final $Boolean.ForeignKey foreignKey = (($Boolean)column).getForeignKey();
      return foreignKey == null ? null : foreignKey.getJsqlCache$();
    }

    throw new RuntimeException("Unknown column type: " + column.getClass().getName());
  }

  final TableModel tableModel;
  final int position;
  final $Column column;
  final String name;
  final boolean isPrimary;
  final boolean isKeyForUpdate;
  @SuppressWarnings("rawtypes")
  private final Class<? extends data.Column> type;
  final String rawType;
  private final Object[] commonParams;
  private final GenerateOn<?> generateOnInsert;
  private final GenerateOn<?> generateOnUpdate;
  private final Object[] customParams;
  private final Object _default;
  final String instanceCase;
  final String camelCase;

  final boolean cacheIndex;
  final $ColumnIndex index;
  final boolean cacheForeignKey;
  final $ForeignKeyUnary foreignKey;

  @SuppressWarnings("rawtypes")
  ColumnModel(final TableModel tableModel, final int position, final $Column column, final boolean isPrimary, final boolean isKeyForUpdate, final Class<? extends data.Column> type, final Object[] commonParams, final Object _default, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
    this.tableModel = tableModel;
    this.position = position;
    this.column = column;
    this.isPrimary = isPrimary;
    this.isKeyForUpdate = isKeyForUpdate;
    this.name = column.getName$().text();
    this.type = type;
    this.rawType = column instanceof $Enum ? tableModel.getClassNameOfEnum(($Enum)column).toString() : ((Class<?>)Classes.getSuperclassGenericTypes(type)[0]).getCanonicalName();
    this.commonParams = commonParams;
    this._default = "null".equals(_default) ? null : _default;
    this.generateOnInsert = generateOnInsert;
    this.generateOnUpdate = generateOnUpdate;
    this.customParams = params;
    this.instanceCase = Identifiers.toInstanceCase(name);
    this.camelCase = Identifiers.toCamelCase(name, '_');

    this.index = DDLx.getIndex(column);
    final Cache$ indexCache = getIndexCache(column);
    this.cacheIndex = indexCache == null || indexCache.text();

    this.foreignKey = DDLx.getForeignKey(column);
    final Cache$ foreignKeyCache = getForeignKeyCache(column);
    this.cacheForeignKey = foreignKeyCache == null || foreignKeyCache.text();
  }

  String getEqualsClause() {
    if (type == data.ARRAY.class || type == data.BINARY.class)
      return Arrays.class.getName() + ".equals(this." + instanceCase + ".get(), that." + instanceCase + ".get())";

    return "this." + instanceCase + ".get().equals(that." + instanceCase + ".get())";
  }

  private String compileParams(final int columnIndex, final String commitUpdateChange) {
    final StringBuilder out = new StringBuilder();
    for (final Object param : commonParams) { // [A]
      if (param == THIS)
        out.append("this");
      else if (param == MUTABLE)
        out.append("_mutable$");
      else if (param == PRIMARY_KEY)
        out.append("_column$[").append(columnIndex).append("] instanceof ").append(data.class.getCanonicalName()).append(".IndexType ? (").append(data.class.getCanonicalName()).append(".IndexType)_column$[").append(columnIndex).append("] : null");
      else if (param == KEY_FOR_UPDATE)
        out.append("_column$[").append(columnIndex).append("] == ").append(data.class.getCanonicalName()).append(".KEY_FOR_UPDATE");
      else if (param == COMMIT_UPDATE_CHANGE)
        out.append(commitUpdateChange != null ? instanceCase + " != null ? " + instanceCase + " : " + commitUpdateChange : instanceCase);
      else
        out.append(param);

      out.append(", ");
    }

    out.append(compile(_default, type)).append(", ");
    out.append(compile(generateOnInsert, type)).append(", ");
    out.append(compile(generateOnUpdate, type)).append(", ");
    if (customParams != null)
      for (final Object param : customParams) // [A]
        out.append(param == THIS ? "this" : compile(param, type)).append(", ");

    out.setLength(out.length() - 2);
    return out.toString();
  }

  String declareColumn() {
    return getCanonicalName(true).append(' ').append(camelCase).toString();
  }

  StringBuilder getCanonicalName(final Boolean withGeneric) {
    final StringBuilder out = new StringBuilder();
    out.append(type.getCanonicalName());
    if (type != data.ENUM.class || withGeneric == null)
      return out;

    out.append('<');
    if (withGeneric)
      out.append(tableModel.getClassNameOfEnum(($Enum)column));

    out.append('>');
    return out;
  }

  void assignConstructor(final StringBuilder out, final int columnIndex, final String commitUpdateChange) {
    out.append(camelCase).append(" = ").append(getConstructor(columnIndex, commitUpdateChange));
  }

  void assignCopyConstructor(final StringBuilder out) {
    out.append(camelCase).append(" = ").append(getCopyConstructor());
  }

  String getCopyConstructor() {
    return "new " + getCanonicalName(false) + "(this, _mutable$, copy." + instanceCase + ")";
  }

  String getConstructor(final int columnIndex, final String commitUpdateChange) {
    final StringBuilder out = new StringBuilder("new ");
    out.append(getCanonicalName(true)).append('(');
    out.append(compileParams(columnIndex, commitUpdateChange));
    if (type == data.ENUM.class) {
      final StringBuilder enumClassName = tableModel.getClassNameOfEnum(($Enum)column);
      out.append(", ").append(enumClassName).append(".values()");
      out.append(", ").append(enumClassName).append("::valueOf");
    }

    return out.append(')').toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ColumnModel))
      return false;

    final ColumnModel that = (ColumnModel)obj;
    return name.equals(that.name) && tableModel.isRelated(that.tableModel);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return tableModel.tableName + ":" + name;
  }
}