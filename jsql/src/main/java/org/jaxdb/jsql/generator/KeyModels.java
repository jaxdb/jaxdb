/* Copyright (c) 2023 JAX-DB
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

import java.util.LinkedHashSet;

import org.jaxdb.jsql.data;

class KeyModels extends LinkedHashSet<KeyModels.KeyModel> {
  class KeyModel {
    private final boolean isForeign;
    final String cacheMethodName;
    final String toTableRefName;
    final String indexFieldName;
    final ColumnModels columns;
    final IndexType indexType;

    private final String keyClauseValues;
    private final String keyArgs;
    private final String fromArgs;
    private final String toArgs;

    KeyModel(final boolean isForeign, final String singletonInstanceName, final String cacheMethodName, final String toTableRefName, final ColumnModels columns, final IndexType indexType) {
      this.isForeign = isForeign;
      this.cacheMethodName = cacheMethodName;
      this.toTableRefName = toTableRefName;
      this.indexFieldName = singletonInstanceName + "._" + ColumnModels.getInstanceNameForCache(cacheMethodName, toTableRefName) + "Index$";
      this.columns = columns;
      this.indexType = indexType;

      if (columns.size() > 0) {
        final StringBuilder keyClauseValues = new StringBuilder();
        final StringBuilder keyArgs = new StringBuilder();
        final StringBuilder fromArgs = new StringBuilder();
        final StringBuilder toArgs = new StringBuilder();

        for (final ColumnModel column : columns) { // [S]
          keyClauseValues.append("{1}.this.").append(column.camelCase).append(".get{2}(), ");
          keyArgs.append(column.instanceCase).append(", ");
        }

        keyClauseValues.setLength(keyClauseValues.length() - 2);
        this.keyClauseValues = keyClauseValues.toString();

        keyArgs.setLength(keyArgs.length() - 2);
        this.keyArgs = keyArgs.toString();

        if (indexType.isBTree()) {
          for (final ColumnModel column : columns) { // [S]
            fromArgs.append(column.instanceCase).append("From, ");
            toArgs.append(column.instanceCase).append("To, ");
          }

          fromArgs.setLength(fromArgs.length() - 2);
          this.fromArgs = fromArgs.toString();
          toArgs.setLength(toArgs.length() - 2);
          this.toArgs = toArgs.toString();
        }
        else {
          this.fromArgs = null;
          this.toArgs = null;
        }
      }
      else {
        this.keyClauseValues = null;
        this.keyArgs = null;
        this.fromArgs = null;
        this.toArgs = null;
      }
    }

    private void include(final String fieldName, final String name, final String args) {
      declare.add("    private " + data.Key.class.getCanonicalName() + " " + name + ";\n\n    " + data.Key.class.getCanonicalName() + " " + name + "() {\n      return " + name + " == null ? " + name + " = " + data.Key.class.getCanonicalName() + ".with(" + fieldName + ", " + args + ") : " + name + ";\n    }\n");
      clear.add(name + " = null;");
    }

    String keyArgs() {
      if (keyArgs == null)
        return null;

      return data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + keyArgs + ")";
    }

    String rangeArgs() {
      if (toArgs == null)
        return null;

      return data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + fromArgs + "), " + data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + toArgs + ")";
    }

    String keyClause(final String cacheSingletonName, final String cacheMethodName, final String toTableRefName, final String replace1, final String replace2) {
      final String cacheColumnsRef = cacheSingletonName + "._" + ColumnModels.getInstanceNameForCache(cacheMethodName, toTableRefName) + "Index$";
      if (!isForeign) {
        final String argsXX = keyClauseValues.replace("{1}.this.", "").replace("{2}", replace2);
        final String xxx = argsXX.replace(".get" + replace2, "").replace("()", "_").replace(", ", "");
        final String prefix = "_" + ColumnModels.getInstanceNameForCache(cacheMethodName, xxx) + "ON_" + toTableRefName + replace2;
        final String name = prefix + "_Key$";
        include(cacheColumnsRef, name, argsXX);
      }

      final String args = keyClauseValues.replace("{1}", replace1).replace("{2}", replace2);
      return data.Key.class.getCanonicalName() + ".with(" + cacheColumnsRef + ", " + args + ")";
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof KeyModel))
        return false;

      final KeyModel that = (KeyModel)obj;
      return indexFieldName.equals(that.indexFieldName) && columns.equals(that.columns) && indexType == that.indexType;
    }

    @Override
    public int hashCode() {
      return indexFieldName.hashCode() ^ columns.hashCode() ^ indexType.hashCode();
    }
  }

  private final LinkedHashSet<String> declare;
  private final LinkedHashSet<String> clear;

  KeyModels(int initialCapacity) {
    super(initialCapacity);
    this.declare = new LinkedHashSet<>(initialCapacity);
    this.clear = new LinkedHashSet<>(initialCapacity);
  }

  KeyModel add(final boolean isForeign, final String singletonInstanceName, final String cacheMethodName, final String toTableRefName, final ColumnModels columns, final IndexType indexType) {
    final KeyModel keyModel = new KeyModel(isForeign, singletonInstanceName, cacheMethodName, toTableRefName, columns, indexType);
    super.add(keyModel);
    return keyModel;
  }

  void writeDeclare(final StringBuilder out) {
    if (true || declare.size() == 0)
      return;

    for (final String str : declare)
      out.append(str).append('\n');
  }

  void writeClear(final StringBuilder out, final String indent) {
    if (clear.size() == 0)
      return;

    out.append('\n');
    for (final String str : clear)
      out.append(indent).append(str).append('\n');
  }
}