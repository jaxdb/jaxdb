/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jaxdb.vendor.DbVendor;
import org.libj.util.DiscreteTopology;

public abstract class Condition<V> extends data.Primitive<V> {
  // FIXME: Move this back to `data`
  @SuppressWarnings({"rawtypes", "unchecked"})
  static class Identity extends Condition {
    Identity() {
      super(null, false, (OnModify<?>)null);
    }

    @Override
    void _commitEntity$() {
    }

    @Override
    public data.Column clone() {
      return null;
    }

    @Override
    void collectColumns(final ArrayList list) {
    }

    @Override
    StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return null;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return b;
    }

    @Override
    boolean equals(final data.Column that) {
      return false;
    }

    @Override
    public Object get() {
      return null;
    }

    @Override
    public Object get(final Object defaultValue) {
      return null;
    }

    @Override
    DiscreteTopology getDiscreteTopology() {
      return null;
    }

    @Override
    Object getOld() {
      return null;
    }

    @Override
    public boolean isNull() {
      return false;
    }

    @Override
    boolean isNullOld() {
      return false;
    }

    @Override
    Object parseString(final DbVendor vendor, final String s) {
      return null;
    }

    @Override
    String primitiveToString() {
      return null;
    }

    @Override
    void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void revert() {
    }

    @Override
    data.Column scaleTo(final data.Column column) {
      return null;
    }

    @Override
    protected boolean set(final Object value) {
      return false;
    }

    @Override
    boolean set(final Object value, final SetBy setBy) {
      return false;
    }

    @Override
    protected boolean setIfNotEqual(final Object value) {
      return false;
    }

    @Override
    protected boolean setIfNotNull(final Object value) {
      return value != null && set(value);
    }

    @Override
    protected boolean setIfNotNullOrEqual(final Object value) {
      return false;
    }

    @Override
    boolean setValue(final Object value) {
      return false;
    }

    @Override
    boolean setValueNull() {
      return false;
    }

    @Override
    int sqlType() {
      return 0;
    }

    @Override
    StringBuilder toJson(final StringBuilder b) {
      return null;
    }

    @Override
    public String toString() {
      return null;
    }

    @Override
    Class type() {
      return null;
    }

    @Override
    void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    int valueHashCode() {
      return 0;
    }

    @Override
    void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
    }
  }

  Condition(final data.Table owner, final boolean mutable, final OnModify<? extends data.Table> onModify) {
    super(owner, mutable, onModify);
  }

  Condition(final data.Table owner, final boolean mutable, final Condition<V> copy) {
    super(owner, mutable, copy);
  }

  Condition(final data.Table owner, final boolean mutable, final String name, final data.IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends data.Table> commitUpdate, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
    super(owner, mutable, name, primaryIndexType, isKeyForUpdate, commitUpdate, isNullable, _default, generateOnInsert, generateOnUpdate);
  }

  static final void collectColumn(final ArrayList<data.Column<?>> list, final Subject column) {
    final data.Column<?> col = column.getColumn();
    if (col instanceof Condition)
      ((Condition<?>)col).collectColumns(list);
    else
      list.add(col);
  }

  abstract void collectColumns(ArrayList<data.Column<?>> list);
  // abstract void andIntervalSet(IntervalSet<data.Key> intervalSet);
  // abstract void orIntervalSet(IntervalSet<data.Key> intervalSet);
}