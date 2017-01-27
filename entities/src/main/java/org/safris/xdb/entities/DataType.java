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

package org.safris.xdb.entities;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.safris.commons.lang.Classes;
import org.safris.xdb.entities.data.Array;
import org.safris.xdb.schema.DBVendor;

public abstract class DataType<T> extends Subject<T> {
  protected static <T>void setValue(final DataType<T> dataType, final T value) {
    dataType.value = value;
  }

  protected static <T>String serialize(final DataType<T> dataType, final DBVendor vendor) throws IOException {
    return dataType.serialize(vendor);
  }

  @SuppressWarnings("unchecked")
  protected static <T,V extends DataType<T>>V wrap(final T value) {
    try {
      final V dataType = (V)data.typeToClass.get(value.getClass()).newInstance();
      dataType.set(value);
      return dataType;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  protected static <T>Array<T> wrap(final T[] value) {
    final Class<? extends DataType<T>> dataTypeClass = (Class<? extends DataType<T>>)data.typeToClass.get(value.getClass().getComponentType());
    final Array<T> array = new Array<T>(dataTypeClass);
    array.set(value);
    return array;
  }

  protected final Entity owner;
  protected final String name;
  protected final boolean unique;
  protected final boolean primary;
  protected final boolean nullable;
  protected final GenerateOn<? super T> generateOnInsert;
  protected final GenerateOn<? super T> generateOnUpdate;

  protected DataType(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
    this.value = _default;
    this.owner = owner;
    this.name = name;
    this.unique = unique;
    this.primary = primary;
    this.nullable = nullable;
    this.generateOnInsert = generateOnInsert;
    this.generateOnUpdate = generateOnUpdate;
  }

  protected DataType(final Entity owner) {
    this(owner, null, null, false, false, false, null, null);
  }

  protected DataType() {
    this(null);
  }

  protected T value;
  protected boolean wasSet;

  public void set(final T value) {
    this.wasSet = true;
    this.value = value;
  }

  public T get() {
    return value;
  }

  public boolean wasSet() {
    return wasSet;
  }

  public final <V extends DataType<T>>V AS(final V dataType) {
    dataType.setWrapper(new As<T>(this, dataType));
    return dataType;
  }

  @Override
  protected void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }

  private Class<T> type;

  @SuppressWarnings("unchecked")
  protected Class<T> type() {
    return type == null ? type = (Class<T>)Classes.getGenericSuperclasses(getClass())[0] : type;
  }

  protected abstract int sqlType();
  protected abstract void get(final PreparedStatement statement, final int parameterIndex) throws SQLException;
  protected abstract void set(final ResultSet resultSet, final int columnIndex) throws SQLException;
  protected abstract String serialize(final DBVendor vendor) throws IOException;
  protected abstract <V,D extends DataType<V>>D newInstance(final Entity owner);

  @Override
  protected abstract DataType<T> clone();

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (obj.getClass() != getClass())
      return false;

    final T get = get();
    return get != null ? get.equals(((DataType<?>)obj).get()) : ((DataType<?>)obj).get() == null;
  }
}