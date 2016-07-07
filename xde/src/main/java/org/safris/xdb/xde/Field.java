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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Field<T> extends Data<Field<T>> {
  protected static <T>Field<T> valueOf(final T object) {
    if (object instanceof Serializable)
      throw new XDERuntimeException("Should not happen!");

    return new FieldWrapper<T>(object);
  }

  protected Field(final T value) {
    this.value = value;
  }

  private boolean wasSet = false;

  protected boolean wasSet() {
    return wasSet;
  }

  private T value;

  public T set(final T value) {
    this.wasSet = true;
    return this.value = value;
  }

  public T get() {
    return value;
  }

  protected abstract Entity entity();
  protected abstract void set(final PreparedStatement statement, final int parameterIndex) throws SQLException;
  protected abstract T get(final ResultSet resultSet, final int columnIndex) throws SQLException;
}