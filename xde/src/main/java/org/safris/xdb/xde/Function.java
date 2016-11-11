/* Copyright (c) 2016 Seva Safris
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

import org.safris.commons.lang.reflect.Classes;

public class Function<T> extends Variable<T> {
  private final Class<? extends DataType<? extends T>> dataType;
  private final String function;

  protected Function(final Class<? extends DataType<? extends T>> dataType, final String function) {
    super(null);
    this.dataType = dataType;
    this.function = function;
  }

  @Override
  protected Entity owner() {
    return null;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    serialization.sql.append(function).append("()");
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    Class<?> cls = dataType.getClass();
    do {
      if (DataType.canSet(cls)) {
        DataType.set(statement, parameterIndex, (Class<T>)Classes.getGenericSuperclasses(dataType.getClass())[0], this.value);
        return;
      }
    }
    while ((cls = cls.getSuperclass()) != null);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    this.value = DataType.get((Class<T>)Classes.getGenericSuperclasses(dataType.getClass())[0], resultSet, columnIndex);
  }
}