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

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Variable<T> extends Subject<Variable<T>> {
  protected static <T>Variable<T> valueOf(final T object) {
    if (object instanceof Serializable)
      throw new AssertionError("Should not happen!");

    return new VariableWrapper<T>(object);
  }

  protected Variable(final T value) {
    this.value = value;
  }

  private boolean wasSet = false;

  protected final boolean wasSet() {
    return wasSet;
  }

  protected T value;

  public final T set(final T value) {
    this.wasSet = true;
    return this.value = value;
  }

  public final T get() {
    return value;
  }

  protected Subject<T> wrapper;

  protected void setWrapper(final Subject<T> wrapper) {
    this.wrapper = wrapper;
  }

  protected abstract Entity owner();
  protected abstract void get(final PreparedStatement statement, final int parameterIndex) throws SQLException;
  protected abstract void set(final ResultSet resultSet, final int columnIndex) throws SQLException;

  @Override
  @SuppressWarnings("unchecked")
  public Variable<T> clone() {
    try {
      final Constructor<? extends Variable<T>> constructor = (Constructor<? extends Variable<T>>)getClass().getDeclaredConstructor(getClass());
      constructor.setAccessible(true);
      return constructor.newInstance(this);
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}