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

import org.jaxdb.jsql.GenerateOn;
import org.jaxdb.jsql.data;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;

final class ColumnModel {
  final TableModel tableModel;
  final int index;
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

  @SuppressWarnings("rawtypes")
  ColumnModel(final TableModel tableModel, final int index, final $Column column, final boolean isPrimary, final boolean isKeyForUpdate, final Class<? extends data.Column> type, final Object[] commonParams, final Object _default, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
    this.tableModel = tableModel;
    this.index = index;
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