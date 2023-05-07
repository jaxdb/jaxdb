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
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.data.IndexType;
import org.jaxdb.vendor.DbVendor;
import org.libj.util.DiscreteTopology;

public abstract class Condition<V extends Serializable> extends data.Primitive<V> {
  @SuppressWarnings("rawtypes")
  static class Identity extends Condition {
    Identity() {
      super(null, false);
    }

    @Override
    protected boolean set(final Serializable value) {
      return false;
    }

    @Override
    protected boolean setIfNotNull(final Serializable value) {
      return value != null && set(value);
    }

    @Override
    boolean set(final Serializable value, final SetBy setBy) {
      return false;
    }

    @Override
    boolean setValue(final Serializable value) {
      return false;
    }

    @Override
    public void revert() {
    }

    @Override
    void _commitEntity$() {
    }

    @Override
    public Serializable get() {
      return null;
    }

    @Override
    public Serializable get(final Serializable defaultValue) {
      return null;
    }

    @Override
    public boolean isNull() {
      return false;
    }

    @Override
    Serializable getOld() {
      return null;
    }

    @Override
    boolean isNullOld() {
      return false;
    }

    @Override
    Class type() {
      return null;
    }

    @Override
    int sqlType() {
      return 0;
    }

    @Override
    DiscreteTopology getDiscreteTopology() {
      return null;
    }

    @Override
    Serializable parseString(final DbVendor vendor, final String s) {
      return null;
    }

    @Override
    void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
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
    Column scaleTo(final Column column) {
      return null;
    }

    @Override
    public Column clone() {
      return null;
    }

    @Override
    boolean equals(final Column that) {
      return false;
    }

    @Override
    int valueHashCode() {
      return 0;
    }

    @Override
    StringBuilder toJson(final StringBuilder b) {
      return null;
    }

    @Override
    void collectColumns(final ArrayList list) {
    }

    @Override
    String primitiveToString() {
      return null;
    }

    @Override
    public String toString() {
      return null;
    }
  }

  Condition(final data.Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final Consumer<? extends data.Table> commitUpdate, final boolean isNullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
    super(owner, mutable, name, primaryIndexType, isKeyForUpdate, commitUpdate, isNullable, generateOnInsert, generateOnUpdate);
  }

  Condition(final data.Table owner, final boolean mutable, final Condition<V> copy) {
    super(owner, mutable, copy);
  }

  Condition(final data.Table owner, final boolean mutable) {
    super(owner, mutable);
  }

  abstract void collectColumns(ArrayList<data.Column<?>> list);
//  abstract void andIntervalSet(IntervalSet<data.Key> intervalSet);
//  abstract void orIntervalSet(IntervalSet<data.Key> intervalSet);
}