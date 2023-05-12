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
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.libj.lang.Classes;
import org.libj.lang.Identifiers;

final class ColumnMeta {
  final TableMeta tableMeta;
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
  ColumnMeta(final TableMeta tableMeta, final int index, final $Column column, final boolean isPrimary, final boolean isKeyForUpdate, final Class<? extends data.Column> type, final Object[] commonParams, final Object _default, final GenerateOn<?> generateOnInsert, final GenerateOn<?> generateOnUpdate, final Object ... params) {
    this.tableMeta = tableMeta;
    this.index = index;
    this.column = column;
    this.isPrimary = isPrimary;
    this.isKeyForUpdate = isKeyForUpdate;
    this.name = column.getName$().text();
    this.type = type;
    this.rawType = column instanceof $Enum ? tableMeta.getClassNameOfEnum(($Enum)column).toString() : ((Class<?>)Classes.getSuperclassGenericTypes(type)[0]).getCanonicalName();
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

  private String compileParams(final int columnIndex, final String commitUpdates) {
    final StringBuilder out = new StringBuilder();
    for (final Object param : commonParams) // [A]
      out.append(param == THIS ? "this" : param == MUTABLE ? "_mutable$" : param == PRIMARY_KEY ? "_column$[" + columnIndex + "] instanceof " + data.class.getCanonicalName() + ".IndexType ? (" + data.class.getCanonicalName() + ".IndexType)_column$[" + columnIndex + "] : null" : param == KEY_FOR_UPDATE ? "_column$[" + columnIndex + "] == " + data.class.getCanonicalName() + ".KEY_FOR_UPDATE" : param == COMMIT_UPDATE ? (commitUpdates != null ? instanceCase + " != null ? " + instanceCase + " : " + commitUpdates : instanceCase) : param).append(", ");

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
      out.append(tableMeta.getClassNameOfEnum(($Enum)column));

    out.append('>');
    return out;
  }

  void assignConstructor(final StringBuilder out, final int columnIndex, final String commitUpdates) {
    out.append(camelCase).append(" = ").append(getConstructor(columnIndex, commitUpdates));
  }

  void assignCopyConstructor(final StringBuilder out) {
    out.append(camelCase).append(" = ").append(getCopyConstructor());
  }

  String getCopyConstructor() {
    return "new " + getCanonicalName(false) + "(this, _mutable$, copy." + instanceCase + ")";
  }

  String getConstructor(final int columnIndex, final String commitUpdates) {
    final StringBuilder out = new StringBuilder("new ");
    out.append(getCanonicalName(true)).append('(');
    out.append(compileParams(columnIndex, commitUpdates));
    if (type == data.ENUM.class) {
      final StringBuilder enumClassName = tableMeta.getClassNameOfEnum(($Enum)column);
      out.append(", ").append(enumClassName).append(".values()");
      out.append(", ").append(enumClassName).append("::valueOf");
    }

    return out.append(')').toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ColumnMeta))
      return false;

    final ColumnMeta that = (ColumnMeta)obj;
    return name.equals(that.name) && tableMeta.isRelated(that.tableMeta);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return tableMeta.tableName + ":" + name;
  }
}