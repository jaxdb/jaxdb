/* Copyright (c) 2017 Seva Safris
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class Direction<T> extends Variable<T> {
  private final Variable<?> variable;

  public Direction(final Variable<T> variable) {
    super(variable.value);
    this.variable = variable;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    serialization.addCaller(this);
    serialization.append(serialize());
  }

  @Override
  protected Entity owner() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected String serialize() {
    return variable.serialize() + " " + getClass().getSimpleName();
  }
}