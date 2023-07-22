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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxdb.jsql.data;
import org.jaxdb.jsql.generator.Relation.CurOld;

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

    private final LinkedHashSet<String>[] resets = new LinkedHashSet[] {new LinkedHashSet<>(), new LinkedHashSet<>()};

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

    void writeReset(final StringBuilder b, final CurOld curlOld) {
      for (final String reset : this.resets[curlOld.ordinal()])
        b.append("\n            ").append(reset);
    }

    void audit() {
//      if (this.declare[0] != null && !this.declareCalled[0])
//        throw new IllegalStateException();

//      if (this.declare[1] != null && !this.declareCalled[1])
//        throw new IllegalStateException();

//      if (this.reset[0] != null && !this.resetCalled[0])
//        throw new IllegalStateException();

//      if (this.reset[1] != null && !this.resetCalled[1])
//        throw new IllegalStateException();
    }

    String keyArgsExternal(final HashSet<String> declared) {
      if (keyArgs == null)
        return null;

      if (!declared.add(indexFieldName))
        return null;

      return data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + keyArgs + ")";
    }

    String keyArgsRange() {
      if (toArgs == null)
        return null;

      return data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + fromArgs + "), " + data.Key.class.getCanonicalName() + ".with(" + indexFieldName + ", " + toArgs + ")";
    }

    private String include(final String fieldName, final String name, final String args, final CurOld curOld) {
      final String declare = "    private " + data.Key.class.getCanonicalName() + " " + name + ";\n\n    " + data.Key.class.getCanonicalName() + " " + name + "() {\n      return " + name + " == null ? " + name + " = " + data.Key.class.getCanonicalName() + ".with(" + fieldName + ", " + args + ") : " + name + ";\n    }\n";
      if (curOld == null) { // Means that resetting the key is not supported (i.e. it is a MutableKey)
        declarations.put(declare, () -> {});
      }
      else {
        declarations.put(declare, () -> {});

        final String reset = "self." + name + " = null;";
        resets[curOld.ordinal()].add(reset);
      }

      return name + "()";
    }

    String keyRefArgsInternal(final String cacheSingletonName, final String cacheMethodName, final String toTableRefName, final String replace1, final CurOld curlOld, final boolean addSelfRef, final HashSet<String> declared, final String comment) {
      final String args = keyClauseValues.replace("{1}", replace1).replace("{2}", curlOld.toString());
      final String cacheColumnsRef = cacheSingletonName + "._" + ColumnModels.getInstanceNameForCache(cacheMethodName, toTableRefName) + "Index$";
      if (!declared.add(cacheColumnsRef + ":" + args))
        return null;

      final String key = data.Key.class.getCanonicalName() + ".with(" + cacheColumnsRef + ", " + args + ")";

//      if (isForeign)
//        return key;

        // FIXME: Uncomment to continue work on persistent keys
      final String argsXX = keyClauseValues.replace("{1}.this.", "").replace("{2}", curlOld.toString());
      final String xxx = argsXX.replace(".get" + curlOld, "").replace("()", "_").replace(", ", "");
      final String prefix = "_" + ColumnModels.getInstanceNameForCache(cacheMethodName, xxx) + "ON_" + toTableRefName + curlOld;
      final String name = prefix + "_Key$";
      return "assertKey(" + (addSelfRef ? "self." : "") + include(cacheColumnsRef, name, argsXX, curlOld) + ", " + key + ")";
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

  private final LinkedHashMap<String,Runnable> declarations;

  KeyModels(final int initialCapacity) {
    super(initialCapacity);
    this.declarations = new LinkedHashMap<>(initialCapacity);
  }

  KeyModel add(final boolean isForeign, final String singletonInstanceName, final String cacheMethodName, final String toTableRefName, final ColumnModels columns, final IndexType indexType) {
    final KeyModel keyModel = new KeyModel(isForeign, singletonInstanceName, cacheMethodName, toTableRefName, columns, indexType);
    super.add(keyModel);
    return keyModel;
  }

  void writeDeclare(final StringBuilder out) {
    if (declarations.size() == 0)
      return;

    out.append('\n');
    for (final Map.Entry<String,Runnable> entry : declarations.entrySet()) {
      out.append(entry.getKey()).append('\n');
      entry.getValue().run();
    }
  }
}